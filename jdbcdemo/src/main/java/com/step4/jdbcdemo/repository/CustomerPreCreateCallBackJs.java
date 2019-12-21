package com.step4.jdbcdemo.repository;

import java.io.IOException;
import java.net.URL;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.springframework.stereotype.Component;
import com.step4.jdbcdemo.model.Customer;

@Component
@CallBack(typeCode = "customer")
public class CustomerPreCreateCallBackJs  implements BeforeSaveCallBack<Customer>{

	@Override
	public void onPreCreate(Customer item) {
		Context context = Context.newBuilder().allowAllAccess(true).build();
		context.getBindings("js").putMember("item", item);
		
		URL is = CustomerPreCreateCallBackJs.class.getResource("/static/customerprecreate.js");
		
		try {
			Source source = Source.newBuilder("js", is).build();
			context.eval(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
