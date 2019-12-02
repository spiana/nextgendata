package com.step4.jdbcdemo.repository;

import org.springframework.data.repository.CrudRepository;

import com.step4.jdbcdemo.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
