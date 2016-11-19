package com.express.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.express.action.request.androidActionRequest;
import com.express.dao.UserDao;
import com.express.model.SendMsg_webchinese;
import com.express.model.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class androidAction extends ActionSupport{
	private androidActionRequest actionRequest = new androidActionRequest();
	public String updateuser(){
		actionRequest.update(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	public String userinfo(){
		actionRequest.userinfo(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	public String usercheck(){
		actionRequest.usercheck(ServletActionContext.getRequest(), ServletActionContext.getResponse());
		return null;
	}
	/*public void update(HttpServletRequest request,HttpServletResponse response){ 
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=null;
		try {
			out = response.getWriter();
//			HttpSession session = request.getSession();
//			user = (User) session.getAttribute("userinfo");
			User user = new User();
			UserDao userDao = new UserDao();
			user = userDao.userinfo(new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8"));
//			user.setUserName(new String(request.getParameter("name").getBytes("iso-8859-1"), "UTF-8"));
			user.setPassword(new String(request.getParameter("password").getBytes(
					"iso-8859-1"), "UTF-8"));
			user.setEmail(new String(request.getParameter("email").getBytes(
					"iso-8859-1"), "UTF-8"));
			user.setTel(new String(request.getParameter("tel").getBytes(
					"iso-8859-1"), "UTF-8"));
			user.setImg(new String(request.getParameter("img").getBytes("iso-8859-1"), "UTF-8"));
			System.out.println(new String(request.getParameter("img").getBytes("iso-8859-1"), "UTF-8"));
			boolean b = userDao.update(user);
			out.println(b);
		} catch (IOException e) {
			e.printStackTrace();
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
			UserDao userDao = new UserDao();
			boolean b = userDao.usercheck(name, email, tel);	
			if(b){
				Random random = new Random();
				int pass = random.nextInt(899999);
				int pass2 = pass+100000;
				System.out.println(pass2);
				userDao.updatePassword(name, pass2+"");
//				SendMsg_webchinese sendmm=new SendMsg_webchinese();
				try {
//					sendmm.check(pass,tel);
				} catch (Exception e) {
					e.printStackTrace();
				}
				out.println("true");
			}else{
				out.println("false");
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	}*/
	public androidActionRequest getActionRequest() {
		return actionRequest;
	}
	public void setActionRequest(androidActionRequest actionRequest) {
		this.actionRequest = actionRequest;
	}
	
}
