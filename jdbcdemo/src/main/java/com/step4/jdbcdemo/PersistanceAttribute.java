package com.step4.jdbcdemo;

public class PersistanceAttribute{
	String type;
	String name;
	String columnName;
	String referredColumn;
	
	boolean id = false;
	RelationType relationType= RelationType.NONE;
	String relationObject;
	
	public PersistanceAttribute() {}
	
	public PersistanceAttribute(String name, String columnName , String type) {
		super();
		this.name = name;
		this.columnName = columnName;
		this.type= type;
		
	}
	
	public PersistanceAttribute(String name, String columnName ,String type,  RelationType relationType , String relationObject , String referredColumn ) {
		super();
		this.name = name;
		this.columnName = columnName;
		this.relationType= relationType;
		this.relationObject = relationObject;
		this.type= type;
		this.referredColumn= referredColumn;
		
	}
	
	public PersistanceAttribute(String name, String columnName,String type, boolean id) {
		super();
		this.name = name;
		this.columnName = columnName;
		this.id = id;
		this.type= type;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isId() {
		return id;
	}
	public void setId(boolean id) {
		this.id = id;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	public String getRelationObject() {
		return relationObject;
	}

	public void setRelationObject(String relationObject) {
		this.relationObject = relationObject;
	}

	public String getReferredColumn() {
		return referredColumn;
	}

	public void setReferredColumn(String referedColumn) {
		this.referredColumn = referedColumn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersistanceAttribute other = (PersistanceAttribute) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
