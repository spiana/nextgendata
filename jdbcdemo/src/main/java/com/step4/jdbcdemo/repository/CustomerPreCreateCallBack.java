package com.step4.jdbcdemo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.step4.jdbcdemo.model.Customer;

@Component
@CallBack(typeCode="customer")
public class CustomerPreCreateCallBack implements BeforeSaveCallBack<Customer> {
	Logger LOG = LoggerFactory.getLogger(CustomerPreCreateCallBack.class);
	@Override
	public void onPreCreate(Customer item) {
		LOG.info("this customer have id : {}" , item.getPk());
	}

}
