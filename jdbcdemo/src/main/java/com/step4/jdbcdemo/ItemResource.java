package com.step4.jdbcdemo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.step4.jdbcdemo.model.Item;
import com.step4.jdbcdemo.model.ItemRelation;
import com.step4.jdbcdemo.model.LazyLoadedColumn;
import com.step4.jdbcdemo.mvc.ModelControllerImpl;

public class ItemResource extends RepresentationModel<ItemResource> {
	
	public String typeCode;

	public Map<String, Object> properties = new HashMap<String, Object>();

	public ItemResource(Item item) {
		super();
		this.typeCode = item.typeCode;
	
		
		 Map<String, Object> props = item.getProperties();
		
		add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(item.typeCode).slash(item.getId()).withSelfRel());

		for (String key : props.keySet()) {
			Object value = props.get(key);
			if (value instanceof Item) {
				this.properties.put(key, new ItemResource( (Item)value));
				
			} else if (value instanceof LazyLoadedColumn) {
				LazyLoadedColumn<Item> column = (LazyLoadedColumn<Item>) value;
				add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(column.model).slash(column.pk).withRel(column.model));
				
			} else if (value instanceof ItemRelation) {
				ItemRelation<Item> relation = (ItemRelation<Item>) value;
				add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(relation.referredModel).slash(relation.referredColumn).slash(relation.referredPK).withRel(relation.referredModel));
				
				
			}else {
				this.properties.put(key, value);
			}

		}

		
	}

}
