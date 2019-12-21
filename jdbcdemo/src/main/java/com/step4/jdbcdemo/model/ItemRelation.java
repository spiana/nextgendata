package com.step4.jdbcdemo.model;

import java.util.List;

import com.step4.jdbcdemo.RelationType;

public class ItemRelation<T extends Item> {
	
	public String  referredModel;
	public String referredColumn;
	
	public Long referredPK;
	
	public T item;
	
	public RelationType reaRelationType = RelationType.NONE;
	
	public List<T> elements;


	public ItemRelation() {
		super();
	}



	public ItemRelation(String referredModel, String referredColumn, RelationType relationType,Long referredPK, T item) {
		super();
		this.referredModel = referredModel;
		this.referredColumn = referredColumn;
		this.referredPK = referredPK;
		this.reaRelationType = relationType;
		this.item = item;
		
	}
	

}
