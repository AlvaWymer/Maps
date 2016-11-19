package com.express.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.express.dao.UserDao;
import com.express.model.User;
import com.express.util.DbUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class androidLogin extends ActionSupport{
	
	public String loginuser(){
		login(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	
	public void login(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		try {
			out = response.getWriter();
			User user = new User();
			user.setUserName(new String(request.getParameter("name").getBytes(
					"iso-8859-1"), "UTF-8"));
			user.setPassword(new String(request.getParameter("password").getBytes(
					"iso-8859-1"), "UTF-8"));
			DbUtil dbUtil=new DbUtil();
			UserDao userDao=new UserDao();
			Connection con=null;
			try {
				con=dbUtil.getCon();
				User currentUser=userDao.login(con, user);
				if(currentUser==null){
					out.println("false");
				}else{
					User user2 = new User();
					user2=userDao.userinfo(user.getUserName());
					ActionContext.getContext().getSession().put("userinfo",user2);
//					ServletActionContext.getRequest().getSession().setAttribute("userinfo", user2);
					out.println("true");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dbUtil.closeCon(con);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		out.flush();
		out.close();
		}
	
}
