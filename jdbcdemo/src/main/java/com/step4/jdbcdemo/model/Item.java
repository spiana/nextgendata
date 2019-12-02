package com.step4.jdbcdemo.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.step4.jdbcdemo.ItemDeserializer;
import com.step4.jdbcdemo.MetaRepository;
import com.step4.jdbcdemo.ModelService;
import com.step4.jdbcdemo.Registry;
import com.step4.jdbcdemo.RelationType;


@JsonDeserialize(using = ItemDeserializer.class)
public  class Item implements Persistable<Long> {


	@Transient
	@JsonIgnore
	public boolean isnew;
	
	@Transient
	@JsonIgnore
	public ApplicationContext context ;

	@Id
	public Long pk;
	
	@Transient
	public String typeCode;


	public Item() {
		super();
		this.context = Registry.getRegistry();
	}
	
	public Item(String typeCode) {
		super();
		this.typeCode = typeCode;
		this.context = Registry.getRegistry();
	}

	@Transient
	public Map<String, Object> properties = new HashMap<String, Object>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getProperty(String key) {
		Object o = properties.get(key);
		
		Object retVal = null;
		
		if (o instanceof ItemRelation) {
			

			@SuppressWarnings("rawtypes")
			ItemRelation relation = (ItemRelation) o;

			if (relation.reaRelationType.equals(RelationType.ONE_2_MANY)) {
					MetaRepository<Item> repo =  context.getBean(relation.referredModel+"Repository" , MetaRepository.class);
				
					retVal= repo.findAllByColumnId(relation.referredColumn, relation.referredPK);
		
			} else if (relation.reaRelationType.equals(RelationType.ONE_2_ONE)) {
					MetaRepository<Item> repo =  context.getBean(relation.referredModel+"Repository" , MetaRepository.class);
				
					
				List<Item> item = repo.findAllByColumnId(relation.referredColumn, relation.referredPK);
			
				retVal =  !item.isEmpty()? item.get(0) :null;
				properties.put(key, retVal);
			}
		}else if (o instanceof LazyLoadedColumn) {
			LazyLoadedColumn column = (LazyLoadedColumn) o;
			
			
			
			Class item_class = null;
			try {
				 item_class = Class.forName(column.javaType);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if (Item.class.isAssignableFrom(item_class)) {
				MetaRepository<Item> repo =  context.getBean(column.model+"Repository" , MetaRepository.class);
				retVal= repo.findById(column.pk).orElse(null);
				
			}else {
				throw new RuntimeException("wrong model type");
			}
		
			properties.put(key, retVal);
			
		}else {
			retVal=o;
		}

		return  retVal;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public Long getId() {
		
		return getPk();
	}

	@Override
	public boolean isNew() {
		return pk == null ? true :false;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
