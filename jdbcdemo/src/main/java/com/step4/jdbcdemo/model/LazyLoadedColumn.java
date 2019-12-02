package com.step4.jdbcdemo.model;

public class LazyLoadedColumn<T extends Item> {

	public String model;
	public String column;
	
	public Long pk;
	
	public String javaType;

	public LazyLoadedColumn(String model, String column, Long pk, String javaType) {
		super();
		this.model = model;
		this.column = column;
		this.pk = pk;
		this.javaType = javaType;
	}
	
	
}
