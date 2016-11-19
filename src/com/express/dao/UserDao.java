package com.express.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.express.model.PageBean;
import com.express.model.User;
import com.express.util.DbUtil;
import com.express.util.StringUtil;


public class UserDao {

	/**
	 * 登录验证
	 * @param con
	 * @param user
	 * @return
	 * @throws Exception
	 */
	DbUtil dbUtil=new DbUtil();
	public User login(Connection con,User user) throws Exception{
		User resultUser=null;
		String sql="select * from t_admin_user where userName=? and password=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserName());
		pstmt.setString(2, user.getPassword());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){
			resultUser=new User();		//将得到的属性塞进user里面
			resultUser.setUserId(rs.getInt("userId"));
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setPassword(rs.getString("password"));
			resultUser.setRoleId(rs.getInt("roleId"));		//传入 RoleId  
		}
		return resultUser;
	}
	//修改密码
	public int modifyPassword(Connection con,User user)throws Exception{
		String sql="update t_admin_user set password=? where userId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getPassword());
		pstmt.setInt(2, user.getUserId());
		return pstmt.executeUpdate();
	}
	
	//UserManage.html 分页查询用户
	public ResultSet userList(Connection con,PageBean pageBean,User user)throws Exception{
		StringBuffer sb=new StringBuffer("select * from t_admin_user u ,t_role r where u.roleId=r.roleId and u.userType!=1 ");//超级管理员不能够被查出来
		if(StringUtil.isNotEmpty(user.getUserName())){
			sb.append(" and u.userName like '%"+user.getUserName()+"%'");
		}
		if(user.getRoleId()!=-1){
			sb.append(" and u.roleId="+user.getRoleId());
		}
		if(pageBean!=null){
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getRows());//分页
		}
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		return pstmt.executeQuery();
	}
	
	//UserManage.html 分页查询用户的总记录数
	public int userCount(Connection con,User user)throws Exception{
		StringBuffer sb=new StringBuffer("select count(*) as total from t_admin_user u ,t_role r where u.roleId=r.roleId and u.userType!=1");
		
		if(StringUtil.isNotEmpty(user.getUserName())){
			sb.append(" and u.userName like '%"+user.getUserName()+"%'");
		}
		if(user.getRoleId()!=-1){
			sb.append(" and u.roleId="+user.getRoleId());
		}
		
		PreparedStatement pstmt=con.prepareStatement(sb.toString());
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()){		//有值的话
			return rs.getInt("total");
		}else{
			return 0;
		}
	}
	
	
	
	
	//UserManage.html 用户添加
	public int userAdd(Connection con,User user)throws Exception{
		String sql="insert into t_admin_user values(null,?,?,2,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserName());
		pstmt.setString(2, user.getPassword());
		pstmt.setInt(3, user.getRoleId());//角色id
		pstmt.setString(4, user.getUserDescription());
		pstmt.setString(5, user.getEmail());
		pstmt.setString(7, user.getTel());
		pstmt.setString(8, user.getImg());
		return pstmt.executeUpdate();
	}
	
	////UserManage.html 用户修改（更新）
	public int userUpdate(Connection con,User user)throws Exception{
		String sql="update t_admin_user set userName=?,password=?,roleId=?,userDescription=?,email=?,tel=?,img=? where userId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, user.getUserName());
		pstmt.setString(2, user.getPassword());
		pstmt.setInt(3, user.getRoleId());
		pstmt.setString(4, user.getUserDescription());
		pstmt.setInt(5, user.getUserId());
		pstmt.setString(6, user.getEmail());
		pstmt.setString(7, user.getTel());
		pstmt.setString(8, user.getImg());
		return pstmt.executeUpdate();
	}
	
	public boolean update(User user){
		Connection con=null;
		boolean b = false;
		try {
			con = dbUtil.getCon();
			String sql="update t_admin_user set password=?,roleId=?,userDescription=?,email=?,tel=?,img=? where userName=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, user.getPassword());
			pstmt.setInt(2, user.getRoleId());
			pstmt.setString(3, user.getUserDescription());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getTel());
			pstmt.setString(6, user.getImg());
			pstmt.setString(7, user.getUserName());
			pstmt.executeUpdate();
			b=true;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbUtil.closeCon(con);
		}
		return b;
	}
	public void updatePassword(String name,String password){
		Connection con=null;
//		boolean b = false;
		try {
			con = dbUtil.getCon();
			String sql="update t_admin_user set password=? where userName=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
//			b=true;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbUtil.closeCon(con);
		}
//		return b;
	}
	
	
	////UserManage.html 判断用户名 是否存在
	public boolean existUserWithUserName(Connection con,String userName)throws Exception{
		String sql="select * from t_admin_user where userName=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userName);
		return pstmt.executeQuery().next();//
	}
	
	//删除usermanage管理员
	public int userDelete(Connection con,String delIds)throws Exception{
		String sql="delete from t_admin_user where userId in ("+delIds+")";
		PreparedStatement pstmt=con.prepareStatement(sql);
		return pstmt.executeUpdate();
	}
		
		//删除角色的时候判断角色下面已经没有用户了
	public boolean existUserWithRoleId(Connection con,String roleId)throws Exception{
		String sql="select * from t_admin_user where roleId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, roleId);
		return pstmt.executeQuery().next();
	}
	//
	public User userinfo(String name){
		User resultUser=null;
		Connection con=null;
		try {
			con = dbUtil.getCon();
			String sql="select * from t_admin_user where userName=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, name);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				resultUser=new User();		//将得到的属性塞进user里面
				resultUser.setUserId(rs.getInt("userId"));
				resultUser.setUserName(rs.getString("userName"));
				resultUser.setPassword(rs.getString("password"));
				resultUser.setRoleId(rs.getInt("roleId"));		//传入 RoleId  
				resultUser.setEmail(rs.getString("email"));
				resultUser.setTel(rs.getString("tel"));
				resultUser.setImg(rs.getString("img"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbUtil.closeCon(con);
		}
		return resultUser;
	}
	
	public boolean usercheck(String name,String email,String tel){
		Connection con=null;
		boolean b=false;
		try {
			con = dbUtil.getCon();
			String sql="select * from t_admin_user where userName=?";
			PreparedStatement pstmt=con.prepareStatement(sql);
			pstmt.setString(1, name);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				String uemail = rs.getString("email");
				String utel = rs.getString("tel");
				if(email.trim().equals(uemail.trim())&&tel.trim().equals(utel.trim())){
					b=true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbUtil.closeCon(con);
		}
		return b;
	}
	
}
