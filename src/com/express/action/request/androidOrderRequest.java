package com.express.action.request;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.express.dao.HOrderDao;
import com.express.model.Order;
import com.express.model.OrderLocation;
import com.opensymphony.xwork2.ActionSupport;

public class androidOrderRequest extends ActionSupport{
	private List<Order> orders;
	private HOrderDao hOrderDao = new HOrderDao();	
	//查询快递订单
	public void query(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		try {
			out = response.getWriter();
			String orderid=new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8");
			hOrderDao.query(orderid);
			out.println("success");
		} catch (IOException e) {
			e.printStackTrace();
			out.println("error");
		}
		out.flush();
		out.close();
	}
	//添加快递订单
	public void add(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		Order order=new Order();
		OrderLocation orderLocation = new OrderLocation();
		try {
			out = response.getWriter();
			order.setOrderid(tostring(request.getParameter("order_number")));
			if(hOrderDao.check(order.getOrderid())){
				order.setUsername(tostring(request.getParameter("entername")));
				order.setCost(tostring(request.getParameter("order_cost")));
				order.setWeight(tostring(request.getParameter("order_weight")));
				order.setPubtime(tostring(request.getParameter("dateString")));
				order.setSpostcode(tostring(request.getParameter("post_mailnumber")));
				order.setSname(tostring(request.getParameter("post_name")));
				order.setStel(tostring(request.getParameter("post_tel")));
				order.setSaddress(tostring(request.getParameter("post_address")));
				order.setRname(tostring(request.getParameter("receive_name")));
				order.setRtel(tostring(request.getParameter("receive_tel")));
				order.setRaddress(tostring(request.getParameter("receive_address")));
				order.setRpostcode(tostring(request.getParameter("receive_mailnumberString")));
				order.setStatus(1);
				hOrderDao.add(order);
				orderLocation.setOrderid(tostring(request.getParameter("order_number")));
				orderLocation.setContext("揽件成功");
				orderLocation.setStatus(1);
				orderLocation.setTime(time());
				hOrderDao.addloc(orderLocation);
				out.println("true");
			}else{
				out.println("repeat");
			}
		} catch (IOException e) {
			e.printStackTrace();
			out.println("false");
		}
		out.flush();
		out.close();
	}
	
	//查询快递单所在位置信息
	public void queryloc(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		String orderid=null;
		try {
			out = response.getWriter();
			orderid=tostring(request.getParameter("orderid"));
			out.println(hOrderDao.queryloc(orderid));
		} catch (IOException e) {
			e.printStackTrace();
			out.println(hOrderDao.queryloc(orderid));
		}
		out.flush();
		out.close();
	}
	
	//查询某个揽件人的所有揽件订单
	public void queryName(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		String username=null;
		try {
			out = response.getWriter();
			username=new String(request.getParameter("username").getBytes("iso-8859-1"), "UTF-8");
			out.println(hOrderDao.queryName(username));
		} catch (IOException e) {
			e.printStackTrace();
			out.println(hOrderDao.queryName(username));
		}
		out.flush();
		out.close();
	}
	
	//添加快递订单的位置信息
	public void addloc(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		OrderLocation orderLocation = new OrderLocation();
		try {
			out = response.getWriter();
			String orderid1 = tostring(request.getParameter("order_number"));
			if(hOrderDao.check(orderid1)==false){
				out = response.getWriter();
				int status1 = Integer.parseInt(request.getParameter("status"));
				orderLocation.setStatus(status1);
				orderLocation.setOrderid(orderid1);
				orderLocation.setContext(tostring(request.getParameter("content")));
				orderLocation.setTime(time());
				hOrderDao.addloc(orderLocation);
				//修改订单表的状态
				hOrderDao.update(orderid1, status1);
				out.println("true");
			}else{
				out.println("null");
			}
		} catch (IOException e) {
			e.printStackTrace();
			out.println("false");
		}
		out.flush();
		out.close();
	}
	//统计我的订单总数   需要派送的订单数 退回件 有问题的订单数
	public void statistics(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		
		try{
			out = response.getWriter();
			out.println(hOrderDao.statistics(tostring(request.getParameter("username"))));
			System.out.println(hOrderDao.statistics(tostring(request.getParameter("username"))));
			
		}catch (Exception e) {
			e.printStackTrace();
			out.println("error");
		}
		out.flush();
		out.close();
	}
	
	//获取当前的时间转换为String类型
	public String time(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		Date date=new Date();  
		return sdf.format(date);  
	}
	//解决中文乱码
	public String tostring(String o) throws UnsupportedEncodingException{
		return new String(o.getBytes("iso-8859-1"), "UTF-8");
	}


	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public HOrderDao gethOrderDao() {
		return hOrderDao;
	}
	public void sethOrderDao(HOrderDao hOrderDao) {
		this.hOrderDao = hOrderDao;
	}
	
	
}
