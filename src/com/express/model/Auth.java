package com.express.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//菜单的model
@Entity(name="t_auth")
public class Auth {

	private int authId;
	private String authName;
	private String authPath;
	private int parentId;
	private String authDescription;
	private String state;
	private String iconCls;
	
	
	
	public Auth() {
		super();
	}
	
	
	public Auth(String authName, String authPath, String authDescription,
			String iconCls) {
		super();
		this.authName = authName;
		this.authPath = authPath;
		this.authDescription = authDescription;
		this.iconCls = iconCls;
	}

	@Id
	@GeneratedValue
	public int getAuthId() {
		return authId;
	}
	public void setAuthId(int authId) {
		this.authId = authId;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getAuthPath() {
		return authPath;
	}
	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getAuthDescription() {
		return authDescription;
	}
	public void setAuthDescription(String authDescription) {
		this.authDescription = authDescription;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	
}
