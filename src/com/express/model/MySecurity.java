package com.express.model;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/** 
 * @ClassName: MySecurity 
 * @Description: javamail模型层  
 * @project: LogisticsManagementSystem3
 * @package: com.express.model
 * @author: yangnaihua
 * @version: V1.0
 * @since: JDK 1.7
 * @date: 2014-6-2 
 * @param <T> 
 */ 
public class MySecurity extends Authenticator {
	private String name ;
	private String password ;
	public MySecurity(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.name,this.password);
	}
	
}
