package com.step4.jdbcdemo;

import java.util.List;

import org.springframework.data.domain.Persistable;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.step4.jdbcdemo.model.AbstractItem;
import com.step4.jdbcdemo.repository.AfterSaveCallBack;
import com.step4.jdbcdemo.repository.BeforeSaveCallBack;
import com.step4.jdbcdemo.repository.RepositoryCallBackMapper;

public class MetaRepository<T extends AbstractItem<ID>, ID> extends JdbcRepository<T, ID>  {
	
	
	PersistanceDictionary dictionary;
	RepositoryCallBackMapper<T> callBackMapper;

	public MetaRepository(String tableName, Class<?> type, String typeCode, PersistanceDictionary dictionary,
			RepositoryCallBackMapper<T> callBackMapper , List<PersistanceAttribute> columnIds) {

		super(new ModelRowMapper<T>(type, typeCode, dictionary), new ModelRowUnMapper<T>(tableName, dictionary), tableName,
				columnIds.iterator().next().columnName );
	
		this.dictionary = dictionary;
		this.callBackMapper = callBackMapper;
	}
	public MetaRepository(String tableName, Class<?> type, String typeCode, PersistanceDictionary dictionary,
			RepositoryCallBackMapper<T> callBackMapper , String columnId , List<String> columns) {

		super(new ModelRowMapper<T>(type, typeCode, dictionary), new ModelRowUnMapper<T>(tableName, dictionary), tableName,
				columnId );
	
		this.dictionary = dictionary;
		this.callBackMapper = callBackMapper;
	}
	
	
	@Override
	protected <S extends T> S postCreate(S entity, Number generatedId) {
		if (generatedId != null)
			entity.putProperty(entity.id_column, generatedId.longValue());
		return entity;
	}
	

	

	@Override
	protected <S extends T> S beforeSave(S entity) {
		List<BeforeSaveCallBack<T>> _l = callBackMapper.getPreCreateCallBack(entity.getTypeCode());
		if (_l != null) {
			for (BeforeSaveCallBack<T> repositoryPostCreateCallBack : _l) {
				repositoryPostCreateCallBack.onPreCreate(entity);
			}
		}
		
		return super.beforeSave( entity);
	}

	@Override
	protected <S extends T> S afterSave(S entity) {
		List<AfterSaveCallBack<T>> _l = callBackMapper.getPostCreateCallBack(entity.getTypeCode());
		if (_l != null) {
			for (AfterSaveCallBack<T> repositoryPostCreateCallBack : _l) {
				repositoryPostCreateCallBack.onPostCreate(entity);
			}
		}
		return super.afterSave(entity);
	}


}
