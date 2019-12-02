package com.step4.jdbcdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.step4.jdbcdemo.model.Address;
import com.step4.jdbcdemo.model.Customer;
import com.step4.jdbcdemo.model.Customer_ext;
import com.step4.jdbcdemo.model.Item;
import com.step4.jdbcdemo.repository.CustomerRepository;

@SpringBootTest
class JdbcdemoApplicationTests {

	
	@Resource(name="customerRepository")
	 MetaRepository<Customer> customerRepository;
	 
	
	@Test
	void contextLoads() {
	}

	
	@Test
	void registerCustomer() {
		List<Customer>  customers = customerRepository.findAll();
		assertEquals(1, customers.size());
		
		Customer a = new Customer();
		a.setFirstName("giovanni");
		
		customerRepository.save(a);
		assertEquals(2, customerRepository.findAll().size());
		
		
	}
	
	
	@Test
	void customerTest() {
		
	Item t =  customerRepository.findAll().get(0);

	System.out.println(t.getProperties());
	
	Address address =((java.util.List<Address>) t.getProperty("addresses")).iterator().next();
	System.out.println(address.getProperties());

	
	assertEquals("Milano",address.getProperty("city"));
	
	assertEquals(t.getPk(),((Item)address.getProperty("customer")).getPk());
	System.out.println(address.getProperties());
	
	assertEquals("sergio",t.getProperty("firstName"));
	
	assertEquals("custom1",((Item) t.getProperty("customer_ext")).getProperty("custom1"));
		
	printItemjs(t);
	
	
	}
	
	@Test
	void testJs(){
		
		Customer customer = new Customer();
		customer.setFirstName("sergio");
		
		Context context = Context.newBuilder().allowAllAccess(true).build();
		
		
		context.getBindings("js").putMember("customer", customer);
		
	
		
		Value value =context.eval("js",  "customer.getFirstName()") ;
		assertEquals("sergio", value.asString());
	}
	
	private void printItemjs(Item item) {
		
		Context context = Context.newBuilder().allowAllAccess(true).build();
		context.getBindings("js").putMember("item", item);
		context.eval("js",  "console.log(item.getProperties())") ;
		assertEquals(2,context.eval("js",  "item.getProperty('addresses').size()").asInt()) ;
		assertEquals("sergio" , context.eval("js",  "item.getProperty('firstName')").asString()); 
	}
}
