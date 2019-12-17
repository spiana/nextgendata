package com.step4.jdbcdemo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import com.step4.jdbcdemo.model.Item;

public class ModelRowUnMapper<T extends Item> implements RowUnmapper<T> {

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
				if (attribute.relationType.equals(RelationType.NONE) && !attribute.isId()) {
					if (!Item.class.isAssignableFrom(Class.forName(attribute.type))) {
						columns.put(attribute.getColumnName(), t.getProperty(attribute.getName()));
					} else {
						Item _item = (Item)t.getProperty(attribute.getName());
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
