package com.express.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.JsonArray;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;

import com.express.model.Order;
import com.express.model.OrderLocation;
import com.express.model.PageBean;
import com.express.model.Qlocation;
import com.express.model.Role;
import com.express.util.HibernateUtil;

public class HOrderDao {
	private static JSONArray jsonArray;
	private static JSONObject jsonObject;
	//查询所有揽件订单
	public JSONArray list(PageBean pageBean){
		int start=0;int rsows=0;
		if(pageBean!=null){
			start=pageBean.getStart();
			rsows=pageBean.getRows();
		}
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("from t_order");
		query.setFirstResult(start);
		query.setMaxResults(rsows);
		List<Order> orders=(List<Order>)query.list();
		session.getTransaction().commit();
		jsonArray = JSONArray.fromObject(orders);
		HibernateUtil.closeSession(session);
		return jsonArray;
	}
	//查询所有揽件订单
	public List<Order> excellist(){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("from t_order");
		List<Order> orders=(List<Order>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return orders;
	}
	//查询所有揽件订单
	public int ordercount(){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("select count(*) from t_order");
		List<Order> orders=(List<Order>)query.list();
		long count=(long)query.uniqueResult();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return (int)count;
	}
	//查询快递单
	public Order query(String orderid){
		Order order=null;
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("from t_order o where o.orderid=?");
		query.setParameter(0, orderid);
		List<Order> orders=(List<Order>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(orders.size()!=0){return orders.get(0);}
		else{return order;}
	}
	
	//查询快递单
	public boolean check(String orderid){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("from t_order o where o.orderid=?");
		query.setParameter(0, orderid);
		List<Order> orders=(List<Order>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(orders.size()!=0){return false;}
		else{return true;}
	}
	
	//添加快递单
	public void add(Order order){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		session.save(order);
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
	}
	//修改快递单
	public void update(String orderid ,int status){
		System.out.println(orderid+"？？？？？？？？？");
		System.out.println(status+"?????????");
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("update t_order o set o.status=? where o.orderid=?");
		query.setParameter(0, status);
		query.setParameter(1, orderid);
		query.executeUpdate();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
	}
	
	//添加快递单的位置信息
	public void addloc(OrderLocation orderLocation){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		session.save(orderLocation);
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
	}
	
	//查询快递单的位置信息
	public JSONObject queryloc(String orderid){
		List<Qlocation> qlo = new ArrayList<Qlocation>();
		Qlocation qlocation = new Qlocation();
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("from t_orderlocation o where o.orderid=? order by o.id desc");
		query.setParameter(0, orderid);
		List<OrderLocation> orders=(List<OrderLocation>)query.list();
		session.getTransaction().commit();
		if(orders.size()!=0){
			qlocation.setStatus(orders.get(orders.size()-1).getStatus());
			qlocation.setMessage("");
			qlocation.setData(orders);
			qlocation.setMailNo(orderid);
			qlocation.setExpTextName("物流");
			qlocation.setExpSpellName("wuliu");
			jsonObject = JSONObject.fromObject(qlocation);
		}else{
			qlocation.setStatus(5);
			qlocation.setMessage("快递单号不存在");
			qlocation.setMailNo(orderid);
			qlocation.setExpTextName("物流");
			qlocation.setExpSpellName("wuliu");
			qlocation.setErrCode(1);
			jsonObject = JSONObject.fromObject(qlocation);
		}
		HibernateUtil.closeSession(session);
		return jsonObject;
	}
	
	//查询某个揽件人的所有揽件订单
	public JSONArray queryName(String username){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("from t_order o where o.username=?");
		query.setParameter(0, username);
		List<Order> orders=(List<Order>)query.list();
		session.getTransaction().commit();
		jsonArray = JSONArray.fromObject(orders);
		HibernateUtil.closeSession(session);
		return jsonArray;
	}
	
	//统计我的订单总数   需要派送的订单数 退回件 有问题的订单数
	public String statistics(String username){JSONArray jsonArray=new JSONArray();
	Session session = HibernateUtil.getCuttSession();
	session.beginTransaction();
	Iterator it = session.createQuery("select o.status,count(o) from t_order o where o.username=? group by o.status").setParameter(0, username).iterate();
	JSONObject jObject=new JSONObject();
	int[] val={0,0,0,0};
	int i=1;
	while(it.hasNext()){
		Object[] oc = (Object[])it.next();
		int c1 = (Integer)oc[0];
		if(c1==1||c1==4||c1==5){
			long count = (long)oc[1];
			val[i]=(int) count;
			i++;
		}
	}
	session.getTransaction().commit();
	Session session1 = HibernateUtil.getCuttSession();
	session1.beginTransaction();
	Query query = session1.createQuery("select count(*) from t_order o where o.username=?");
	query.setParameter(0, username);
	long all = (Long)query.uniqueResult();
	val[0]=(int) all;
	jObject.put("value", val);
	jObject.put("color", "#1f7e92");
	jObject.put("name", "北京");
	jsonArray.add(jObject);
	session1.getTransaction().commit();
	HibernateUtil.closeSession(session1);
	HibernateUtil.closeSession(session);
	return jsonArray.toString();
}

	public static JSONArray getJsonArray() {
		return jsonArray;
	}
	public static void setJsonArray(JSONArray jsonArray) {
		HOrderDao.jsonArray = jsonArray;
	}
	public static JSONObject getJsonObject() {
		return jsonObject;
	}
	public static void setJsonObject(JSONObject jsonObject) {
		HOrderDao.jsonObject = jsonObject;
	}
	/*//统计角色
		public List<Role> statisticsRole() throws Exception{
			Session session = HibernateUtil.getSession();
			session.beginTransaction();
			Query query=session.createQuery("from t_role");
			List<Role> roles=(List<Role>)query.list();
			session.getTransaction().commit();
			HibernateUtil.closeSession(session);
			return roles;
		}*/
		
		public int[] statisticsAdmin(){
			JSONArray jsonArray=new JSONArray();
			Session session = HibernateUtil.getCuttSession();
			session.beginTransaction();
			Iterator it = session.createQuery("select o.status,count(o) from t_order o group by o.status").iterate();
			JSONObject jObject=new JSONObject();
			int[] val={0,0,0,0,0};
			int i=2;
			while(it.hasNext()){
				Object[] oc = (Object[])it.next();
				int c1 = (Integer)oc[0];
				if(c1==2||c1==3||c1==5){
					long count = (long)oc[1];
					val[i]=(int) count;
					i++;
				}
			}
			session.getTransaction().commit();
			Session session1 = HibernateUtil.getCuttSession();
			session1.beginTransaction();
//			Query query = session1.createQuery("select count(*) from t_order");
			Query query = session1.createQuery("select sum(cost) from t_order");
			String aa = (String)query.uniqueResult();
			val[0]=(int) (Double.parseDouble(aa));
			session1.getTransaction().commit();
			
			Session session11 = HibernateUtil.getCuttSession();
			session11.beginTransaction();
			Query query1 = session11.createQuery("select count(*) from t_order");
			long all = (long)query1.uniqueResult();
			val[1]=(int) all;
			session11.getTransaction().commit();
			
			HibernateUtil.closeSession(session1);
			HibernateUtil.closeSession(session);
			HibernateUtil.closeSession(session11);
		//	System.out.println(val[0]+" "+val[1]+" "+val[2]+" "+val[3]+" "+val[4]);
			return val;
	}
	
}
