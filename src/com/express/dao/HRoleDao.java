package com.express.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import net.sf.json.JSONArray;

import org.hibernate.Query;
import org.hibernate.Session;

import com.express.model.PageBean;
import com.express.model.Role;
import com.express.util.HibernateUtil;
import com.express.util.StringUtil;

public class HRoleDao {

	private static JSONArray jsonArray;
	public String getRoleNameById(int id)throws Exception{
		Session session=HibernateUtil.getSession();
		session.beginTransaction();
		Query query=session.createQuery("from t_role r where r.roleId=?");
		query.setParameter(0, id);
		List<Role> roles=(List<Role>)query.list();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return roles.get(0).getRoleName();
	}
	
	public static JSONArray roleList(PageBean pageBean,Role role)throws Exception{
		int start=0;int rsows=0;
		StringBuffer sb=new StringBuffer("from t_role r");
		if(StringUtil.isNotEmpty(role.getRoleName())){
			sb.append(" where r.roleName like '%"+role.getRoleName()+"%'");
		}
		if(pageBean!=null){
			start=pageBean.getStart();rsows=pageBean.getRows();
		}
		Session session1=HibernateUtil.getSession();
		session1.beginTransaction();
		Query query1=session1.createQuery(sb.toString());
		query1.setFirstResult(start);   
		query1.setMaxResults(rsows);
		List<Role> roles=(List<Role>)query1.list();
		session1.getTransaction().commit();
		jsonArray = JSONArray.fromObject(roles);
		HibernateUtil.closeSession(session1);
		return jsonArray;
	}
	
	public int roleCount(Role role)throws Exception{
		StringBuffer sb=new StringBuffer("select count(*) from t_role r");
		if(StringUtil.isNotEmpty(role.getRoleName())){
			sb.append(" where r.roleName like '%"+role.getRoleName()+"%'");
		}
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery(sb.toString());
		long count = (Long)query.uniqueResult();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return (int)count;
	}
	
	//ROLEmaNAGE.HTML删除 角色
	public int roleDelete(String delIds)throws Exception{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("delete from t_role where roleId in ("+delIds+")");
		query.executeUpdate();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return 1;
	}
	
	//ROLEmaNAGE.HTML修改
	public int roleUpdate(Role role)throws Exception{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		session.saveOrUpdate(role);
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return 1;
	}
	//授权的时候更改他的roleid
	public int roleAuthIdsUpdate(Role role)throws Exception{
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		Query query = session.createQuery("update t_role set authIds=? where roleId=?");
		query.setParameter(0,  role.getAuthIds());
		query.setParameter(1, role.getRoleId());
		query.executeUpdate();
		session.getTransaction().commit();
		HibernateUtil.closeSession(session);
		return 1;
	}
	

	public static JSONArray getJsonArray() {
		return jsonArray;
	}

	public static void setJsonArray(JSONArray jsonArray) {
		HRoleDao.jsonArray = jsonArray;
	}
	//统计角色
		public List<Role> statisticsRole() throws Exception{
			Session session = HibernateUtil.getSession();
			session.beginTransaction();
			Query query=session.createQuery("from t_role");
			List<Role> roles=(List<Role>)query.list();
			session.getTransaction().commit();
			HibernateUtil.closeSession(session);
			return roles;
		}
	
	
}
