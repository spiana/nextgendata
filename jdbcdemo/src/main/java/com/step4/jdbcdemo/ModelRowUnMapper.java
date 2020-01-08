package com.step4.jdbcdemo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import com.step4.jdbcdemo.model.AbstractItem;

public class ModelRowUnMapper<T extends AbstractItem> implements RowUnmapper<T> {

	String typeCode;

	public ModelRowUnMapper(String typeCode, PersistanceDictionary dictionary) {
		super();
		this.typeCode = typeCode;
		this.dictionary = dictionary;
	}

	private PersistanceDictionary dictionary;

	@Override
	public Map<String, Object> mapColumns(T t) {
		final LinkedHashMap<String, Object> columns = new LinkedHashMap<String, Object>();

		PersistenceEntity entity = dictionary.get(typeCode);

		Assert.notNull(entity);

		for (PersistanceAttribute attribute : entity.getAttributes()) {
			try {
				if (attribute.relationType.equals(RelationType.NONE) ) {
					if (!AbstractItem.class.isAssignableFrom(Class.forName(attribute.type))) {
						columns.put(attribute.getColumnName(), t.getProperty(attribute.getName()));
					} else {
						AbstractItem _item = (AbstractItem)t.getProperty(attribute.getName());
						if (_item != null)
							columns.put(attribute.getColumnName(), _item.getId());
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

		return columns;
	}

}
