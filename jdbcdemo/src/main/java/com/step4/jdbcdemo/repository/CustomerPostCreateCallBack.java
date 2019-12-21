package com.step4.jdbcdemo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.step4.jdbcdemo.model.Customer;

@CallBack(typeCode="customer")
@Component
public class CustomerPostCreateCallBack implements AfterSaveCallBack<Customer>{

	Logger LOG = LoggerFactory.getLogger(CustomerPostCreateCallBack.class);
	
	@Override
	public void onPostCreate(Customer item) {
		
		LOG.info("this customer have id : {}" , item.getPk());
	}

}
