package com.step4.jdbcdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Resource;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.step4.jdbcdemo.model.Address;
import com.step4.jdbcdemo.model.Customer;
import com.step4.jdbcdemo.model.Customer_ext;
import com.step4.jdbcdemo.model.AbstractItem;
import com.step4.jdbcdemo.repository.CustomerRepository;

@SpringBootTest
class JdbcdemoApplicationTests {

	
	@Resource(name="customerRepository")
	 MetaRepository<Customer , Long> customerRepository;
	 
	@Resource(name="customerRep")
	CustomerRepository rep ;
	
	
	@Test
	void contextLoads() {
	}

	
	@Test
	void getCustomer() {
		assertEquals(Long.valueOf(1), Long.valueOf(rep.count()) );
		Iterable<Customer> c = rep.findAll();
		List<Customer> l = StreamSupport.stream(c.spliterator(), false).collect(Collectors.toList());
		assertEquals("sergio", l.get(0).getFirstName().toLowerCase());
		assertEquals(2, l.get(0).getAddreses().size());
		Address a = l.get(0).getAddreses().get(0);
		assertEquals("Milano", a.getCity());
		
		List<Customer> l1 = rep.findByfirstName("sergio");
		assertEquals(1, l.size());
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
		
	AbstractItem t =  customerRepository.findAll().get(0);

	System.out.println(t.getProperties());
	
	Address address =((java.util.List<Address>) t.getProperty("addresses")).iterator().next();
	System.out.println(address.getProperties());

	
	assertEquals("Milano",address.getProperty("city"));
	
	assertEquals((Long)t.getProperty("id"),((AbstractItem)address.getProperty("customer")).getProperty("id"));
	System.out.println(address.getProperties());
	
	assertEquals("sergio",t.getProperty("firstName"));
	
	//assertEquals("custom1",((AbstractItem) t.getProperty("customer_ext")).getProperty("custom1"));
		
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
	
	private void printItemjs(AbstractItem item) {
		
		Context context = Context.newBuilder().allowAllAccess(true).build();
		context.getBindings("js").putMember("item", item);
		context.eval("js",  "console.log(item.getProperties())") ;
		assertEquals(2,context.eval("js",  "item.getProperty('addresses').size()").asInt()) ;
		assertEquals("sergio" , context.eval("js",  "item.getProperty('firstName')").asString()); 
	}
}
