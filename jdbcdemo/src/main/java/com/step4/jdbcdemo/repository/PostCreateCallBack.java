package com.step4.jdbcdemo.repository;

import com.step4.jdbcdemo.model.Item;

public interface PostCreateCallBack<T extends Item> extends RepositoryCallBack {

	public void onPostCreate(T item);
}
