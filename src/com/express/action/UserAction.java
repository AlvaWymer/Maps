package com.express.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.express.dao.HRoleDao;
import com.express.dao.HUserDao;
import com.express.model.PageBean;
import com.express.model.User;
import com.express.util.ExcelUtil;
import com.express.util.ResponseUtil;
import com.express.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport implements ServletRequestAware{
	private String page;
	private String rows;
	private String s_userName;
	private String s_roleId;
	private String userName;
	private String password;
	private String roleId;
	private String userDescription;
	private String userId;
	private String delIds;
	private HUserDao hUserDao = new HUserDao();
	private HRoleDao hRoleDao = new HRoleDao();
	
	HttpServletRequest request;		//获取request
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	//UserManage.html传来的list
	public String list() throws Exception {
		User user=new User();
		if(StringUtil.isNotEmpty(s_userName)){
			user.setUserName(s_userName);
		}
		if(StringUtil.isNotEmpty(s_roleId)){
			user.setRoleId(Integer.parseInt(s_roleId));
		}
		//封装pagebean
		PageBean pageBean=new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=hUserDao.userList(pageBean,user);
		int total=hUserDao.userCount(user);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	
	//保存添加的管理员
	public String save() throws Exception {
		User user=new User(userName, password, Integer.parseInt(roleId), userDescription);
		if(StringUtil.isNotEmpty(userId)){		//判断是否是更新
			user.setUserId(Integer.parseInt(userId));
		}
		JSONObject result=new JSONObject();
		int saveNums=0;
		if(StringUtil.isNotEmpty(userId)){
			saveNums=hUserDao.userUpdate(user);//更新
		}else{
			if(hUserDao.existUserWithUserName(userName)){//用户名是否存在
				saveNums=-1;
			}else{
				saveNums=hUserDao.userUpdate(user);					
			}
		}
		if(saveNums==-1){
			result.put("success", true);
			result.put("errorMsg", "此用户名已经存在");
		}else if(saveNums==0){
			result.put("success", true);
			result.put("errorMsg", "保存失败");
		}else{
			result.put("success", true);
		}
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	
	//删除管理用户
	public String delete() throws Exception{
		JSONObject result=new JSONObject();
		try{
			int delNums=hUserDao.userDelete(delIds);
			result.put("success", true);
			result.put("delNums", delNums);
		}catch (Exception e) {
			e.printStackTrace();
			result.put("errorMsg", "删除失败");
		}
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	//导出到excel
	public String export()throws Exception{
		try {
			Workbook wb=new HSSFWorkbook();
			String headers[]={"编号","姓名","用户密码","用户角色","备注"};
			List<User> users= hUserDao.userList2();
			ExcelUtil.fillExcelData2(wb, headers, users);
			ResponseUtil.export(ServletActionContext.getResponse(), wb, "用户信息导出excel.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getS_userName() {
		return s_userName;
	}

	public void setS_userName(String s_userName) {
		this.s_userName = s_userName;
	}

	public String getS_roleId() {
		return s_roleId;
	}

	public void setS_roleId(String s_roleId) {
		this.s_roleId = s_roleId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDelIds() {
		return delIds;
	}

	public void setDelIds(String delIds) {
		this.delIds = delIds;
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
