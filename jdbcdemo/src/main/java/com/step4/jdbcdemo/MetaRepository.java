package com.step4.jdbcdemo;

import java.util.List;
import java.util.Map;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.step4.jdbcdemo.model.Item;
import com.step4.jdbcdemo.repository.RepositoryCallBackMapper;
import com.step4.jdbcdemo.repository.PostCreateCallBack;
import com.step4.jdbcdemo.repository.PreCreateCallBack;

public class MetaRepository<T extends Item> extends JdbcRepository<T, Long> {
	PersistanceDictionary dictionary;
	RepositoryCallBackMapper<T> callBackMapper;

	public MetaRepository(String tableName, Class<?> type, String typeCode, PersistanceDictionary dictionary,
			RepositoryCallBackMapper<T> callBackMapper) {
		super(new ModelRowMapper<T>(type, typeCode, dictionary), new ModelRowUnMapper<T>(tableName, dictionary), tableName,
				"pk");
		this.dictionary = dictionary;
		this.callBackMapper = callBackMapper;
	}

	@Override
	protected <S extends T> S postCreate(S entity, Number generatedId) {
		entity.setPk(generatedId.longValue());
		List<PostCreateCallBack<T>> _l = callBackMapper.getPostCreateCallBack(entity.getTypeCode());
		if (_l != null) {
			for (PostCreateCallBack<T> repositoryPostCreateCallBack : _l) {
				repositoryPostCreateCallBack.onPostCreate(entity);
			}
		}
		return entity;
	}

	@Override
	protected Map<String, Object> preUpdate(T entity, Map<String, Object> columns) {
		
		return super.preUpdate(entity, columns);
	}

	@Override
	protected Map<String, Object> preCreate(Map<String, Object> columns, T entity) {
		List<PreCreateCallBack<T>> _l = callBackMapper.getPreCreateCallBack(entity.getTypeCode());
		if (_l != null) {
			for (PreCreateCallBack<T> repositoryPostCreateCallBack : _l) {
				repositoryPostCreateCallBack.onPreCreate(entity);
			}
		}
		
		return super.preCreate(columns, entity);
	}

	@Override
	protected <S extends T> S postUpdate(S entity) {
	
		return super.postUpdate(entity);
	}

}
