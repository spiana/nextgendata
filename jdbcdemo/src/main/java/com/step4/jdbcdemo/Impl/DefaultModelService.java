package com.step4.jdbcdemo.Impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import com.step4.jdbcdemo.ModelRowMapper;
import com.step4.jdbcdemo.ModelService;

import com.step4.jdbcdemo.PersistanceDictionary;
import com.step4.jdbcdemo.PersistenceEntity;
import com.step4.jdbcdemo.model.Customer;
import com.step4.jdbcdemo.model.Item;

public class DefaultModelService implements ModelService {

	private JdbcTemplate jdbcTemplate;

	private PersistanceDictionary persistenceDictionary;

	public List<? extends Item> query(String query) {
		Assert.notNull(query);
		//return jdbcTemplate.query(query, new ModelRowMapper(createModel(Customer.class), persistenceDictionary));
		return null;
	}

	@Override
	public void save(Item item) {

		String typecode = item.getClass().getSimpleName().toLowerCase();
		PersistenceEntity entity = getPersistenceDictionary().get(typecode);

		Assert.notNull(typecode, "entity not found !");

		StringBuffer updateValues = new StringBuffer();

		StringBuffer updateQuery = new StringBuffer();
		updateQuery.append("inser into table ");
		updateQuery.append(entity.getName());
		updateQuery.append("(");

//		for (PersistenceEntity.PersistanceAttribute attribute : entity.getAttributes()) {
//			if (attribute.getRelationType().equals(RelationType.NONE)) {
//				if (item.getProperty(attribute.getName()) != null) {
//					updateQuery.append(attribute.getColumnName());
//					if( item.getProperty(attribute.getName()) instanceof Item)
//						updateValues.append(((Item) item.getProperty(attribute.getName())).getPk());
//					else if ()
//				}
//			}
//		}

	}

	public void setPersistenceDictionary(PersistanceDictionary persistenceEntity) {
		this.persistenceDictionary = persistenceEntity;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public <T extends Item> T createModel(Class modelClass) {
		T a = null;

		try {
			a = (T) modelClass.newInstance();
		//	a.modelService = this;
			a.isnew = true;

		} catch (InstantiationException | IllegalAccessException e) {
			;
		}

		return a;
	}

	public PersistanceDictionary getPersistenceDictionary() {
		return persistenceDictionary;
	}

}
