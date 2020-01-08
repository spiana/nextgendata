package com.step4.jdbcdemo;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.step4.jdbcdemo.model.AbstractItem;

public interface ModelService  {

	
	
	public List<? extends AbstractItem> query(String query ) ;
	public void save(AbstractItem item)	;
	public JdbcTemplate getJdbcTemplate();
	public <T extends AbstractItem> T createModel(Class modelClass);
	public Map<String, PersistenceEntity> getPersistenceDictionary();
	
			
}
