package com.step4.jdbcdemo.repository;

import com.step4.jdbcdemo.model.Item;

public interface PreCreateCallBack<T extends Item> extends RepositoryCallBack {

	public void onPreCreate(T item);
}
