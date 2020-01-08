package com.step4.jdbcdemo.repository;

import org.springframework.data.domain.Persistable;

import com.step4.jdbcdemo.model.AbstractItem;

public interface AfterSaveCallBack<T extends Persistable> extends RepositoryCallBack {

	public void onPostCreate(T item);
}
