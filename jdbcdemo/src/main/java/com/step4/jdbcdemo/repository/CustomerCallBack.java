package com.step4.jdbcdemo.repository;

import java.io.IOException;
import java.net.URL;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.step4.jdbcdemo.model.Customer;

@Component
@CallBack(typeCode="customer")
public class CustomerCallBack implements BeforeSaveCallBack<Customer> , AfterSaveCallBack<Customer>{
	Logger LOG = LoggerFactory.getLogger(CustomerCallBack.class);
	
	
	@Override
	public void onPreCreate(Customer item) {
		Context context = Context.newBuilder().allowAllAccess(true).build();
		context.getBindings("js").putMember("item", item);
		
		URL is = CustomerCallBack.class.getResource("/static/customerprecreate.js");
		
		try {
			Source source = Source.newBuilder("js", is).build();
			context.eval(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPostCreate(Customer item) {
		
		LOG.info("this customer have id : {}" , item.getId());
	}
}
