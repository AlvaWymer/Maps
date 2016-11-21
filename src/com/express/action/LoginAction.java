package com.express.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.express.dao.HRoleDao;
import com.express.dao.HUserDao;
import com.express.dao.RoleDao;
import com.express.dao.UserDao;
import com.express.model.User;
import com.express.util.DbUtil;
import com.express.util.ResponseUtil;
import com.express.util.SendSimpleMail;
import com.express.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements ServletRequestAware{
	private User user;
	private String error;
	private String imageCode;
	private String userId;
	private String newPassword;
	private HUserDao hUserDao=new HUserDao();
	private HRoleDao hRoleDao=new HRoleDao();
	

	HttpServletRequest request;		//获取requesthah

	
	@Override
	public String execute() throws Exception {
		// 获取Session
		Map session=ActionContext.getContext().getSession();
		if(StringUtil.isEmpty(user.getUserName())||StringUtil.isEmpty(user.getPassword())){
			error="用户名或密码为空！";
			return ERROR;
		}
		else if(StringUtil.isEmpty(imageCode)){
			error="验证码为空！";
			return ERROR;
		}
		else if(!imageCode.equals(session.get("sRand"))){
			error="验证码错误！";
			return ERROR;
		}else{
			User currentUser=hUserDao.login(user);
			if(currentUser==null){
				error="用户名或密码错误！";
				return ERROR;
			}else{
				String roleName=hRoleDao.getRoleNameById(currentUser.getRoleId());//获取角色
				currentUser.setRoleName(roleName);
				session.put("currentUser", currentUser);//设置session
				return SUCCESS;
			}
		}
	}
	//注销
	public String loginout() throws Exception {
		//清空session
		ActionContext.getContext().getSession().clear();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session1 = request.getSession();
		session1.invalidate();
		return SUCCESS;
		
	}
	//更改密码
	public String updatepassword() throws Exception {
		User user=new User();
		user.setUserId(Integer.parseInt(userId));
		user.setPassword(newPassword);
		JSONObject result=new JSONObject();
		int updateNum=hUserDao.modifyPassword(user);
		if(updateNum>0){
			result.put("success", "true");
			//传入从数据库中查到的从前台差U纳入的用户名的邮箱   三个参数（修改人的姓名，邮箱，新密码）
			user=hUserDao.userinfo(user.getUserId());
			System.out.println(user.getUserName()+"user.getUserName()");
			System.out.println(user.getEmail()+"user.getEmail()");
			System.out.println(user.getPassword()+"user.getPassword()");
			SendSimpleMail.updatepasswordtomail(user.getUserName(),user.getEmail(),user.getPassword());
		}else{
			result.put("success", "true");//=必须写
			result.put("errorMsg", "修改密码失败！");
		}
		ResponseUtil.write(ServletActionContext.getResponse(), result);		
		return null;
		
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public HUserDao gethUserDao() {
		return hUserDao;
	}

	public void sethUserDao(HUserDao hUserDao) {
		this.hUserDao = hUserDao;
	}

	public HRoleDao gethRoleDao() {
		return hRoleDao;
	}

	public void sethRoleDao(HRoleDao hRoleDao) {
		this.hRoleDao = hRoleDao;
	}
}
