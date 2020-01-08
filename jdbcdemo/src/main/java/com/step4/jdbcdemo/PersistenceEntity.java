package com.step4.jdbcdemo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PersistenceEntity {
	private String className;
	private String name;
	private String type = "ENTITY";
	
	private List<String> ids = new ArrayList<String>();
	
	private  String parentModel;
	
	private  Set<PersistanceAttribute>  attributes;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PersistanceAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<PersistanceAttribute> attributes) {
		this.attributes = attributes;
	}
	
	
	public void addAttribute(PersistanceAttribute attribute) {
		if (this.attributes == null) {
			this.attributes = new LinkedHashSet<PersistanceAttribute>();
		}
		this.attributes.add(attribute);
		if (attribute.isId()) {
			this.ids.add(attribute.getName());
		}
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParentModel() {
		return parentModel;
	}

	public void setParentModel(String parentModel) {
		this.parentModel = parentModel;
	}
}
