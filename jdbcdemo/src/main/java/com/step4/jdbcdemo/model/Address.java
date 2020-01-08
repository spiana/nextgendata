package com.step4.jdbcdemo.model;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

@AccessType(Type.PROPERTY)
public class Address extends AbstractItem<Long> {

	public Address() {
		super("address");
		
	}
	
	@Column("pk")
	@Id
	public Long getId() {
		return  getProperty("id");
	}
	
	public void setId(Long id) {
		putProperty("id",id);
	}

	public String getCity() {
		return getProperty("city");
	}
	
	public void setCity(String city) {
		putProperty("city", city);
	}
	
	
	
}
