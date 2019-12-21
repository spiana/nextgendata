package com.step4.jdbcdemo.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.step4.jdbcdemo.model.Item;

@Component
public class RepositoryCallBackMapper<T extends Item> implements InitializingBean , ApplicationContextAware  {

	ApplicationContext applicationContext;
	Map<String , List<AfterSaveCallBack<T>>> postCreateCallBack;
	Map<String , List<BeforeSaveCallBack<T>>> preCreateCallBack;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		 postCreateCallBack = new LinkedHashMap<String, List<AfterSaveCallBack<T>>>();
		 preCreateCallBack = new LinkedHashMap<String, List<BeforeSaveCallBack<T>>>();
		 
		Map<String, RepositoryCallBack> beans =applicationContext.getBeansOfType(RepositoryCallBack.class);
		
		Iterator<Map.Entry<String , RepositoryCallBack>> iterator =  beans.entrySet().iterator();
		
		while (iterator.hasNext()) {
			RepositoryCallBack _callBack =iterator.next().getValue();
			String _typeCode  = null;
			
			if (_callBack.getClass().getAnnotation(CallBack.class) != null)
				_typeCode = _callBack.getClass().getAnnotation(CallBack.class).typeCode();
			
			if (_typeCode == null)
				continue;
			
			if (_callBack instanceof AfterSaveCallBack) {
				List<AfterSaveCallBack<T>> _l = postCreateCallBack.get(_typeCode) ;
				if (_l == null) {
					_l = new ArrayList<AfterSaveCallBack<T>>();
				}
				_l.add((AfterSaveCallBack)_callBack);
				postCreateCallBack.put(_typeCode, _l);
			}else if (_callBack instanceof BeforeSaveCallBack){
				List<BeforeSaveCallBack<T>> _l = preCreateCallBack.get(_typeCode) ;
				if (_l == null) {
					_l = new ArrayList<BeforeSaveCallBack<T>>();
				}
				_l.add((BeforeSaveCallBack)_callBack);
				preCreateCallBack.put(_typeCode, _l);
			}
			
		}
		
	}

	public  List<AfterSaveCallBack<T>> getPostCreateCallBack(String typeCode){
		return postCreateCallBack.get(typeCode);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}

	public List<BeforeSaveCallBack<T>> getPreCreateCallBack(String typeCode) {
		return preCreateCallBack.get(typeCode);
	}

}
