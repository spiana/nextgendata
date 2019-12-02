package com.step4.jdbcdemo;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.step4.jdbcdemo.Impl.DefaultModelService;

@SpringBootApplication
//@EnableJdbcRepositories
@ComponentScan("com.step4.jdbcdemo")
@ComponentScan("com.step4.jdbcdemo.mvc")
@EnableWebMvc
public class JdbcdemoApplication {

	private static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		context =SpringApplication.run(JdbcdemoApplication.class, args);
	}

	  public static void restart() {
	        ApplicationArguments args = context.getBean(ApplicationArguments.class);
	 
	        Thread thread = new Thread(() -> {
	            context.close();
	            context = SpringApplication.run(JdbcdemoApplication.class, args.getSourceArgs());
	        });
	 
	        thread.setDaemon(false);
	        thread.start();
	    }
	
	
	 @Bean("dataSource")
	  public DataSource dataSource() {

	    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
	    return builder.setType(EmbeddedDatabaseType.H2).addScript("static/create-data.sql").build();
	  }
	 
	 @Bean("jdbcTemplate")
	 public JdbcTemplate getJdbctemplate(@Qualifier("dataSource") DataSource datasource) {
		
		 
		 JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
		 
		 return jdbcTemplate;
	 }
	 
	 @Bean("persistanceDictionary")
	 PersistanceDictionary persistanceDictionary() throws JsonParseException, JsonMappingException, IOException {
		 
		 ObjectMapper mapper = new ObjectMapper();
		 
		 return mapper.readValue(JdbcdemoApplication.class.getClassLoader().getResourceAsStream("dictionary.json"), PersistanceDictionary.class);
		 
		 
//		 PersistenceEntity entity = new PersistenceEntity();
//		 entity.setClassName("com.step4.jdbcdemo.model.Customer");
//		 entity.setName("customer");
//		 entity.getIds().add("PK");
//		 
//		 PersistanceAttribute a =  new PersistanceAttribute();
//		 
//		 a.name="id";
//		 a.id=true;
//		 a.type="java.lang.Long";
//		 a.columnName="PK";
//		 
//		 entity.addAttribute(a);
//		 
//		 PersistanceAttribute a1 =  new PersistanceAttribute();
//		 
//		 a1.name="firstName";
//		 a1.id=false;
//		 a1.type="java.lang.String";
//		 a1.columnName="first_name";
//		 
//		 
//		 entity.addAttribute(a1);
//		 
//		 PersistanceAttribute a2 =  new PersistanceAttribute();
//		 
//		 a2.name="lastName";
//		 a2.id=false;
//		 a2.type="java.lang.String";
//		 a2.columnName="last_name";
//				 
//		 entity.addAttribute(a2);
//		 
//		 entity.addAttribute(new PersistanceAttribute("addresses", null ,"java.lang.Long", RelationType.ONE_2_MANY , "address" , "customer_id" ));
//		 entity.addAttribute(new PersistanceAttribute("customer_ext", null ,"java.lang.Long", RelationType.ONE_2_ONE , "customer_ext" , "customer_id" ));
//		 
//		 
//		 PersistenceEntity entity1 = new PersistenceEntity();
//		 entity1.setClassName("com.step4.jdbcdemo.model.Address");
//		 entity1.setName("address");
//		 entity1.getIds().add("PK");
//		 
//		 entity1.addAttribute(new PersistanceAttribute("id", "PK","java.lang.Long", true));
//		 entity1.addAttribute(new PersistanceAttribute("street", "street" , "java.lang.String"));
//		 entity1.addAttribute(new PersistanceAttribute("streetNumber", "street_number" , "java.lang.String"));
//		 entity1.addAttribute(new PersistanceAttribute("zipCode", "zip_code" , "java.lang.String"));
//		 entity1.addAttribute(new PersistanceAttribute("city", "city", "java.lang.String"));
//		 entity1.addAttribute(new PersistanceAttribute("customer", "customer_id", "com.step4.jdbcdemo.model.Customer"));
//		 
//		 
//		 PersistenceEntity entity2 = new PersistenceEntity();
//		 entity2.setClassName("com.step4.jdbcdemo.model.Customer_ext");
//		 entity2.setName("customer_ext");
//		 entity2.getIds().add("PK");
//		 
//		 entity2.addAttribute(new PersistanceAttribute("id", "PK","java.lang.Long", true));
//		 entity2.addAttribute(new PersistanceAttribute("custom1", "custom1" , "java.lang.String"));
//		 entity2.addAttribute(new PersistanceAttribute("custom2", "custom2" , "java.lang.String"));
//		 entity2.addAttribute(new PersistanceAttribute("customer", "customer_id" , "com.step4.jdbcdemo.model.Item"));
//		 
//		 
//		 PersistanceDictionary map = new PersistanceDictionary();
//		 map.put("customer", entity );
//		 map.put("address", entity1);
//		 
//		 map.put("customer_ext", entity2);
//		 
//		 
//		 return map ;
	 }
	 
	 @Bean()
	 ModelService modelService(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate , @Qualifier("persistanceDictionary") PersistanceDictionary map) {
		 
		 DefaultModelService m = new DefaultModelService();
		 m.setPersistenceDictionary(map);
		 m.setJdbcTemplate(jdbcTemplate);
		 		 
		 
		 return m ;
	 }
			 
	  
	 
}
