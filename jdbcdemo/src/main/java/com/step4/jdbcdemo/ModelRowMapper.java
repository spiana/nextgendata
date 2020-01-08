package com.step4.jdbcdemo;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Persistable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import com.step4.jdbcdemo.model.AbstractItem;
import com.step4.jdbcdemo.model.ItemRelation;
import com.step4.jdbcdemo.model.LazyLoadedColumn;

public class ModelRowMapper<T extends AbstractItem> implements RowMapper<T> {

	Class type_class;
	private PersistanceDictionary dictionary;
	String typeCode;

	public ModelRowMapper(Class type, String typeCode, PersistanceDictionary dictionary) {
		super();
		this.type_class = type;
		this.dictionary = dictionary;
		this.typeCode = typeCode;
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T type = createModel(type_class, typeCode);
		
		PersistenceEntity entity = dictionary.get(typeCode);
		type.id_column = entity.getIds().get(0);

		//type.setPk(rs.getObject(entity.getIds().get(0), Long.class));
			
		List<String> columns = new ArrayList<String>();
		for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
			columns.add(rs.getMetaData().getColumnName(x).toLowerCase());
		}

		if (entity == null)
			throw new RuntimeException(String.format("no entity found  %s ", type.getClass().getSimpleName()));

		
		populateProperties(entity, columns, type, rs);
	
		
		return type;
	}

	private <T extends AbstractItem> T createModel(Class modelClass, String typeCode) {
		T a = null;
		try {
			a = (T) modelClass.getConstructor(String.class).newInstance(typeCode);
		} catch (NoSuchMethodException e) {
		
			try {
				a = (T) modelClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (IllegalArgumentException | InvocationTargetException  | SecurityException | IllegalAccessException  |InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return a;
	}
	
	private void populateProperties(PersistenceEntity entity ,List<String> columns  , T type , ResultSet rs ) throws SQLException{
		
		if (entity.getParentModel() != null) {
			PersistenceEntity _entity = dictionary.get(entity.getParentModel());
			if (_entity != null) {
				populateProperties(_entity, columns, type, rs);
			}
		}
		//Populate Ids columns
		populateProperties(entity.getAttributes().stream().filter(p -> p.isId() ).collect(Collectors.toList()), columns, type, rs);
		populateProperties(entity.getAttributes(), columns, type, rs);
	}
	
	private void populateProperties(Collection<PersistanceAttribute> attributes , List<String> columns  , T type , ResultSet rs) throws SQLException {

		type.isnew= false;	
		for (PersistanceAttribute attribute : attributes) {
			try {
				if (attribute.relationType == RelationType.NONE) {

					if (!AbstractItem.class.isAssignableFrom(Class.forName(attribute.type))) {
						if (columns.contains(attribute.columnName.toLowerCase()))
							type.putProperty(attribute.name,
									rs.getObject(attribute.columnName, Class.forName(attribute.type)));
					} else {
						PersistenceEntity _e = dictionary.get(attribute.name);
						Assert.notNull(_e);
						
						type.putProperty(attribute.name, new LazyLoadedColumn<AbstractItem>(attribute.name, _e.getIds().get(0),
								rs.getLong(attribute.columnName), attribute.type, attribute.jsonInclude));

						type.putProperty(attribute.name, type.getProperty(attribute.name));

					}
				} else if (attribute.relationType == RelationType.ONE_2_MANY
						|| attribute.relationType == RelationType.ONE_2_ONE) {

					PersistenceEntity related_entity = dictionary.get(attribute.getRelationObject());

					AbstractItem related;

					related = (AbstractItem) createModel(Class.forName(related_entity.getClassName()), related_entity.getName());

					ItemRelation relation = new ItemRelation(attribute.relationObject,
							attribute.getReferredColumn(), attribute.relationType, type.getId(), related, attribute.jsonInclude);

					type.putProperty(attribute.name, relation);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
