package com.step4.jdbcdemo;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.step4.jdbcdemo.model.Item;
import com.step4.jdbcdemo.model.ItemRelation;
import com.step4.jdbcdemo.model.LazyLoadedColumn;

public class ModelRowMapper<T extends Item> implements RowMapper<T> {

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

		type.setPk(rs.getObject("pk", Long.class));

		PersistenceEntity entity = dictionary.get(typeCode);

		List<String> columns = new ArrayList<String>();
		for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
			columns.add(rs.getMetaData().getColumnName(x).toLowerCase());
		}

		if (entity == null)
			throw new RuntimeException(String.format("no entity found  %s ", type.getClass().getSimpleName()));

		for (PersistanceAttribute attribute : entity.getAttributes()) {
			try {
				if (attribute.relationType == RelationType.NONE) {

					if (!Item.class.isAssignableFrom(Class.forName(attribute.type))) {
						if (columns.contains(attribute.columnName.toLowerCase()))
							type.addProperty(attribute.name,
									rs.getObject(attribute.columnName, Class.forName(attribute.type)));
					} else {
						type.addProperty(attribute.name, new LazyLoadedColumn<Item>(attribute.name, "pk",
								rs.getLong(attribute.columnName), attribute.type));

						type.addProperty(attribute.name, type.getProperty(attribute.name));

					}
				} else if (attribute.relationType == RelationType.ONE_2_MANY
						|| attribute.relationType == RelationType.ONE_2_ONE) {

					PersistenceEntity related_entity = dictionary.get(attribute.getRelationObject());

					Item related;

					related = (Item) createModel(Class.forName(related_entity.getClassName()), related_entity.getName());

					ItemRelation<Item> relation = new ItemRelation<Item>(attribute.relationObject,
							attribute.getReferredColumn(), attribute.relationType, type.getPk(), related);

					type.addProperty(attribute.name, relation);
				}

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return type;
	}

	private <T extends Item> T createModel(Class modelClass, String typeCode) {
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
}
