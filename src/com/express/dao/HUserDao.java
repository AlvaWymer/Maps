package com.express.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.hibernate.Query;
import org.hibernate.Session;

import com.express.model.PageBean;
import com.express.model.Role;
import com.express.model.User;
import com.express.util.HibernateUtil;
import com.express.util.StringUtil;

public class HUserDao {
	/**
	 * 登录验证
	 */
	private static JSONArray jsonArray;
	public User login(User user) throws Exception{
		User user2=null;
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("from t_admin_user where userName=? and password=?");
		query.setParameter(0, user.getUserName());
		query.setParameter(1, user.getPassword());
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(users.size()!=0){return users.get(0);}
		else{return user2;}
	}
	
	//修改密码
	public int modifyPassword(User user) throws Exception{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("update t_admin_user u set u.password=? where u.userId=?");
		query.setParameter(0, user.getPassword());
		query.setParameter(1, user.getUserId());
		query.executeUpdate();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return 1;
	}
	
	//UserManage.html 分页查询用户
	public static JSONArray userList(PageBean pageBean,User user)throws Exception{
		int start=0;int rsows=0;
		StringBuffer sb=new StringBuffer("from t_admin_user u where u.userType!=1 ");//超级管理员不能够被查出来
		if(StringUtil.isNotEmpty(user.getUserName())){
			sb.append(" and u.userName like '%"+user.getUserName()+"%'");
		}
		if(user.getRoleId()!=-1){
			sb.append(" and u.roleId="+user.getRoleId());
		}
		if(pageBean!=null){
			start=pageBean.getStart();rsows=pageBean.getRows();
		}
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery(sb.toString());
		query.setFirstResult(start);   
		query.setMaxResults(rsows);
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		
		Session session1=HibernateUtil.getSession();
		session1.beginTransaction();
		Query query1=session1.createQuery("from t_role");
		List<Role> roles=(List<Role>)query1.list();
		session1.getTransaction().commit();
		
		for(int i=0;i<users.size();i++){
			for(int j=0;j<roles.size();j++){
				if(users.get(i).getRoleId()==roles.get(j).getRoleId()){
					users.get(i).setRoleName(roles.get(j).getRoleName());
				}
			}
		}
		
		jsonArray = JSONArray.fromObject(users);
		HibernateUtil.closeSession(session1);
		return jsonArray;
	}
	
	
	//UserManage.html 分页查询用户
	public List<User> userList2()throws Exception{
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("from t_admin_user");
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		Session session1=HibernateUtil.getSession();
		session1.beginTransaction();
		Query query1=session1.createQuery("from t_role");
		List<Role> roles=(List<Role>)query1.list();
		session1.getTransaction().commit();
		
		for(int i=0;i<users.size();i++){
			for(int j=0;j<roles.size();j++){
				if(users.get(i).getRoleId()==roles.get(j).getRoleId()){
					users.get(i).setRoleName(roles.get(j).getRoleName());
				}
			}
		}
		
		HibernateUtil.closeSession(session1);
		HibernateUtil.closeSession(session);
		return users;
	}
	
	//UserManage.html 分页查询用户的总记录数
	public static int userCount(User user)throws Exception{
		StringBuffer sb=new StringBuffer("select count(*) from t_admin_user u ,t_role r where u.roleId=r.roleId and u.userType!=1");
		if(StringUtil.isNotEmpty(user.getUserName())){
			sb.append(" and u.userName like '%"+user.getUserName()+"%'");
		}
		if(user.getRoleId()!=-1){
			sb.append(" and u.roleId="+user.getRoleId());
		}
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery(sb.toString());
		long count = (Long)query.uniqueResult();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return (int)count;
	}

	
	public int userUpdate(User user)throws Exception{
//		String sql="update t_admin_user set userName=?,password=?,roleId=?,userDescription=?,email=?,tel=?,img=? where userId=?";
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return 1;
	}
	
////UserManage.html 判断用户名 是否存在
	public boolean existUserWithUserName(String userName)throws Exception{
		String hql="from t_admin_user where userName=?";
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery(hql);
		query.setParameter(0, userName);
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(users.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	public int userDelete(String delIds){
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("delete from t_admin_user where userId in ("+delIds+")");
		query.executeUpdate();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return 1;
	}
	
	//删除角色的时候判断角色下面已经没有用户了
	public boolean existUserWithRoleId(String roleId)throws Exception{
		String hql="from t_admin_user where roleId=?";
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery(hql);
		query.setParameter(0, Integer.parseInt(roleId));
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(users.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	public User userinfo(String name){
		User user=null;
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("from t_admin_user u where u.userName=?");
		query.setParameter(0, name);
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(users.size()!=0){return users.get(0);}
		else{return user;}
	}
	public User userinfo(int id){
		User user=null;
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("from t_admin_user u where u.userId=?");
		query.setParameter(0, id);
		List<User> users=(List<User>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(users.size()!=0){return users.get(0);}
		else{return user;}
	}
	//忘记密码时需要验证的属性
	public boolean usercheck(String name,String email,String tel){
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query q = session.createQuery("from t_admin_user where userName=?");
		q.setParameter(0, name);
		List<User> users=(List<User>)q.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		if(users.size()!=0){
			if(email.equals(users.get(0).getEmail())&&tel.equals(users.get(0).getTel())){
				return true;
			}
			return false;
		}else{
			return false;
		}
	}
	
	//修改密码
	public void updatePassword(String name,String pass) throws Exception{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("update t_admin_user u set u.password=? where u.userName=?");
		query.setParameter(0, pass);
		query.setParameter(1, name);
		query.executeUpdate();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
	}
	
	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	//统计
	public List<Integer> statisticsUser() throws Exception{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Iterator it = session.createQuery("select a.roleId,count(a) from t_admin_user a group by a.roleId").iterate();
		List<Integer> valnum=new ArrayList<Integer>();
		while(it.hasNext()){
			Object[] oc = (Object[])it.next();
			long count = (long)oc[1];
			valnum.add((int)count);
		}
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return valnum;
	}
}
