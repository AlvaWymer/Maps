package com.express.action.request;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.express.dao.HUserDao;
import com.express.dao.UserDao;
import com.express.model.SendMsg_webchinese;
import com.express.model.User;
import com.opensymphony.xwork2.ActionSupport;

public class androidActionRequest extends ActionSupport{
	private HUserDao hUserDao=new HUserDao();
	private User user=new User();
	
	public void update(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		try {
			out = response.getWriter();
			user = hUserDao.userinfo(new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8"));
//			user.setUserName(new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8"));
/*			user.setPassword(new String(request.getParameter("password").getBytes(
					"iso-8859-1"), "UTF-8"));
			user.setEmail(new String(request.getParameter("email").getBytes(
					"iso-8859-1"), "UTF-8"));
			user.setTel(new String(request.getParameter("tel").getBytes(
					"iso-8859-1"), "UTF-8"));*/
			user.setImg(new String(request.getParameter("img").getBytes("iso-8859-1"), "UTF-8"));
			System.out.println(new String(request.getParameter("img").getBytes("iso-8859-1"), "UTF-8"));
			hUserDao.userUpdate(user);
			out.println("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("false");
		}
		out.flush();
		out.close();
	}
	
	public void usercheck(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		try {
			out = response.getWriter();
			String name=new String(request.getParameter("forget_name").getBytes("iso-8859-1"), "UTF-8");
			String email=new String(request.getParameter("forget_email").getBytes("iso-8859-1"), "UTF-8");
			String tel=new String(request.getParameter("forget_tel").getBytes("iso-8859-1"), "UTF-8");
			boolean b = hUserDao.usercheck(name, email, tel);	
			if(b){
				Random random = new Random();
				int pass = random.nextInt(899999);
				int pass2 = pass+100000;
				System.out.println(pass2);
				hUserDao.updatePassword(name, pass2+"");
/*SendMsg_webchinese sendmm=new SendMsg_webchinese();
	sendmm.check(pass,tel);*/
				out.println("true");
			}else{
				out.println("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.println("false");
		}
		out.flush();
		out.close();
	}
	
	public void userinfo(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		try {
			out = response.getWriter();
			User user = new User();
			UserDao userDao = new UserDao();
			user = userDao.userinfo(new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8"));
//			out.println(user);
//			out.println(user.getUserName());
			System.out.println(user.getEmail()+":"+user.getTel());
			if(user.getImg()==null){
				user.setImg("imageisnull");
			}
			out.println(user.getEmail()+":"+user.getTel()+":"+user.getImg());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	public HUserDao gethUserDao() {
		return hUserDao;
	}
	public void sethUserDao(HUserDao hUserDao) {
		this.hUserDao = hUserDao;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
