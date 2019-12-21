package com.step4.jdbcdemo;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.step4.jdbcdemo.model.Item;

public interface ModelService  {

	
	
	public List<? extends Item> query(String query ) ;
	public void save(Item item)	;
	public JdbcTemplate getJdbcTemplate();
	public <T extends Item> T createModel(Class modelClass);
	public Map<String, PersistenceEntity> getPersistenceDictionary();
	
			
}
