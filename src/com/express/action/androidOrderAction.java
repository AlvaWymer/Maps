package com.express.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.express.action.request.androidOrderRequest;
import com.express.dao.HOrderDao;
import com.opensymphony.xwork2.ActionSupport;

public class androidOrderAction extends ActionSupport{
	private androidOrderRequest orderRequest = new androidOrderRequest();
	//查询快递订单
	public String query(){
		orderRequest.query(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	//查询某个揽件人的所有揽件订单
	public String queryName(){
		orderRequest.queryName(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	//添加快递单
	public String add(){
		orderRequest.add(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}

	//查询快递单的位置信息
	public String queryloc(){
		orderRequest.queryloc(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	
	//添加快递的位置信息
	public String addloc(){
		orderRequest.addloc(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	//统计
	public String statistics(){
		orderRequest.statistics(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	
	
	public androidOrderRequest getOrderRequest() {
		return orderRequest;
	}
	public void setOrderRequest(androidOrderRequest orderRequest) {
		this.orderRequest = orderRequest;
	}
	
}
