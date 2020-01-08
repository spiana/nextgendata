package com.step4.jdbcdemo.model;

import java.util.List;

import com.step4.jdbcdemo.RelationType;

public class ItemRelation<T extends AbstractItem , ID> {
	
	public String  referredModel;
	public String referredColumn;
	public boolean jsonInclude = false;
	
	public ID referredPK;
	
	public T item;
	
	public RelationType reaRelationType = RelationType.NONE;
	
	public List<T> elements;


	public ItemRelation() {
		super();
	}



	public ItemRelation(String referredModel, String referredColumn, RelationType relationType,ID referredPK, T item , boolean jsonInclude) {
		super();
		this.referredModel = referredModel;
		this.referredColumn = referredColumn;
		this.referredPK = referredPK;
		this.reaRelationType = relationType;
		this.item = item;
		this.jsonInclude = jsonInclude;
		
	}
	

}
