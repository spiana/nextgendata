package com.step4.jdbcdemo.model;

import java.util.List;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

@AccessType(Type.PROPERTY)
public class Customer extends AbstractItem<Long> {
	
	public Customer() {
		super("customer");
	}
	
	@Column("pk")
	@Id
	public Long getId() {
		return  getProperty("id");
	}
	
	public void setId(Long id) {
		putProperty("id",id);
	}
	
	@Column("first_Name" )
	public String getFirstName() {
		return  getProperty("firstName");
	}
	public void setFirstName(String firstName) {
		 putProperty("firstName",firstName);
	}
	
	
	@Column("last_name" )
	public String getLastName() {
		return  getProperty("lastName");
	}
	public void setLastName(String lastName) {
		putProperty("lastName", lastName);
	}
	
	@MappedCollection(idColumn = "customer_id" , keyColumn = "pk" )
	public List<Address> getAddreses(){
		return getProperty("address");
	}
	
	public void setAddreses(List<Address> addreses){
		putProperty("address", addreses);
	}

}
