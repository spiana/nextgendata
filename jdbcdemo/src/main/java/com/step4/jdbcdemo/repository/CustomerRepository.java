package com.step4.jdbcdemo.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.step4.jdbcdemo.model.Customer;

@Repository("customerRep")
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query( "select * from customer as c where c.first_name=:name")
	public List<Customer>  findByfirstName(@Param("name")String name); 
}
