package com.step4.jdbcdemo.model;

public class LazyLoadedColumn<T extends AbstractItem> {

	public String model;
	public String column;
	public boolean jsonInclude = false;
	
	public Long pk;
	
	public String javaType;

	public LazyLoadedColumn(String model, String column, Long pk, String javaType , boolean jsonInclude) {
		super();
		this.model = model;
		this.column = column;
		this.pk = pk;
		this.javaType = javaType;
		this.jsonInclude = this.jsonInclude;
		
	}
	
	
}
