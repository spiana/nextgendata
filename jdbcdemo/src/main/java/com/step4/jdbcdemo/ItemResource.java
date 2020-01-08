package com.step4.jdbcdemo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.UIDefaults.LazyValue;

import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.step4.jdbcdemo.model.AbstractItem;
import com.step4.jdbcdemo.model.ItemRelation;
import com.step4.jdbcdemo.model.LazyLoadedColumn;
import com.step4.jdbcdemo.mvc.ModelControllerImpl;

public class ItemResource extends RepresentationModel<ItemResource> {
	
	public String typeCode;

	public Map<String, Object> properties = new HashMap<String, Object>();
	
	
	public ItemResource(AbstractItem item ) {
		super();
		generateItemResource(item , false);
	}
	public ItemResource(AbstractItem item , boolean nexted) {
		super();
		generateItemResource(item , nexted);
	}
	
	protected void generateItemResource(AbstractItem item , boolean nexted) {
	
		this.typeCode = item.typeCode;
	
		
		 Map<String, Object> props = item.getProperties();
		
		add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(item.typeCode).slash(item.getId()).withSelfRel());

		for (String key : props.keySet()) {
			Object value = props.get(key);
			if (value instanceof AbstractItem) {
				if (nexted) {
					AbstractItem _i  = (AbstractItem) value;
					add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(_i.typeCode).slash(_i.getId()).withRel(_i.typeCode));
				}
				else {	
					this.properties.put(key, new ItemResource( (AbstractItem)value));
				}
			} else if (value instanceof LazyLoadedColumn) {
				LazyLoadedColumn<AbstractItem> column = (LazyLoadedColumn<AbstractItem>) value;
				if (column.jsonInclude) {
					AbstractItem _item = (AbstractItem) item.getProperty(key);
					if (_item != null)
						this.properties.put(key, new ItemResource(_item, true));
				}else {
					add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(column.model).slash(column.pk).withRel(column.model));
				}
			} else if (value instanceof ItemRelation) {
				ItemRelation relation = (ItemRelation) value;
				if (relation.jsonInclude) {
						if (relation.reaRelationType == RelationType.ONE_2_ONE) {
							AbstractItem _item = (AbstractItem) item.getProperty(key);
							if (_item != null)
								this.properties.put(key, new ItemResource(_item, true));
						}else {
							this.properties.put(key, ((Collection<AbstractItem>)item.getProperty(key)).stream().map(p->new ItemResource(p, true)).collect(Collectors.toList()));
						}
				}else {
					add(WebMvcLinkBuilder.linkTo(ModelControllerImpl.class).slash(relation.referredModel).slash(relation.referredColumn).slash(relation.referredPK).withRel(relation.referredModel));
				}
				
			}else {
				this.properties.put(key, value);
			}

		}

		
	}

}
