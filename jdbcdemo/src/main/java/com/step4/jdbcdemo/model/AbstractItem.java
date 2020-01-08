package com.step4.jdbcdemo.model;

import java.io.Serializable;
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
import com.step4.jdbcdemo.Registry;
import com.step4.jdbcdemo.RelationType;

@JsonDeserialize(using = ItemDeserializer.class)
public abstract class  AbstractItem<ID> implements Persistable<ID> {

	@Transient
	@JsonIgnore
	public boolean isnew = true;

	@Transient
	@JsonIgnore
	public ApplicationContext context;

	@JsonIgnore
	@Transient
	public Long pk;

	
	@Transient
	public String typeCode;

	@Transient
	@JsonIgnore
	public String id_column;
	
	public AbstractItem() {
		super();
		this.context = Registry.getRegistry();
	}

	public AbstractItem(String typeCode) {
		super();
		this.typeCode = typeCode;
		this.context = Registry.getRegistry();
	}

	@Transient
	public Map<String, Object> properties = new HashMap<String, Object>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getProperty(String key) {
		T o = (T) properties.get(key);

		T retVal = null;

		if (o instanceof ItemRelation) {

			@SuppressWarnings("rawtypes")
			ItemRelation relation = (ItemRelation) o;

			if (relation.reaRelationType.equals(RelationType.ONE_2_MANY)) {
				MetaRepository repo = context.getBean(relation.referredModel + "Repository",
						MetaRepository.class);

				retVal = (T) repo.findAllByColumnId(relation.referredColumn, relation.referredPK);

			} else if (relation.reaRelationType.equals(RelationType.ONE_2_ONE)) {
				MetaRepository repo = context.getBean(relation.referredModel + "Repository",
						MetaRepository.class);

				List<? extends AbstractItem> item = repo.findAllByColumnId(relation.referredColumn, relation.referredPK);

				retVal = !item.isEmpty() ?  (T) item.get(0) : null;

				//properties.put(key, retVal);
			}
		} else if (o instanceof LazyLoadedColumn) {
			LazyLoadedColumn column = (LazyLoadedColumn) o;

			Class item_class = null;
			try {
				item_class = Class.forName(column.javaType);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (AbstractItem.class.isAssignableFrom(item_class)) {
				MetaRepository repo = context.getBean(column.model + "Repository",
						MetaRepository.class);
				retVal =  (T) repo.findById(column.pk).orElse(null);

			} else {
				throw new RuntimeException("Wrong model type");
			}

			//properties.put(key, retVal);

		} else {
			retVal = o;
		}

		return retVal;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public void putProperty(String name, Object value) {
		properties.put(name, value);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public ID getId() {

		return (ID) getProperty(id_column);
	}

	@Override
	@Transient
	public boolean isNew() {
		return isnew;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
