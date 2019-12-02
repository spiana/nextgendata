package com.step4.jdbcdemo;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.MissingRowUnmapper;
import com.oracle.truffle.api.profiles.LongValueProfile;
import com.step4.jdbcdemo.model.Item;


public class MetaRepository<T extends Item > extends JdbcRepository<T, Long> {
	PersistanceDictionary dictionary;
	
	public MetaRepository(String tableName, Class type , String typeCode , PersistanceDictionary dictionary) {
		super(new ModelRowMapper(type,typeCode, dictionary), new ModelRowUnMapper(tableName, dictionary), tableName, "pk");
		this.dictionary = dictionary;
	}

	@Override
	protected <S extends T> S postCreate(S entity, Number generatedId) {
		entity.setPk(generatedId.longValue());
		return entity;
	}
}
