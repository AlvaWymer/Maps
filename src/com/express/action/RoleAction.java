package com.express.action;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.express.dao.HRoleDao;
import com.express.dao.HUserDao;
import com.express.model.PageBean;
import com.express.model.Role;
import com.express.util.ResponseUtil;
import com.express.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class RoleAction extends ActionSupport implements ServletRequestAware{

	private String page;
	private String rows;
	private String s_roleName;
	private String delIds;
	private String roleName;
	private String roleDescription;
	private String roleId;
	private String authIds;
	private HRoleDao hRoleDao=new HRoleDao();
	private HUserDao hUserDao=new HUserDao();
	
	HttpServletRequest request;		//获取request
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	//角色列表显示
	public String comBoList() throws Exception {
		//System.out.println("角色列表显示");
		JSONArray jsonArray=new JSONArray();
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("roleId", "");
		jsonObject.put("roleName", "请选择...");//默认显示请选择
		jsonArray.add(jsonObject);
		jsonArray.addAll(hRoleDao.roleList(null,new Role()));
		ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
		return null;
	}
	
	
	//搜索
	public String roleList() throws Exception {
		Role role=new Role();
		if(StringUtil.isNotEmpty(s_roleName)){
			role.setRoleName(s_roleName);
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		JSONObject result=new JSONObject();
		JSONArray jsonArray=hRoleDao.roleList(pageBean,role);
		int total=hRoleDao.roleCount(role);
		result.put("rows", jsonArray);
		result.put("total", total);
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	
	//删除角色
	public String roleDelete() throws Exception {
		JSONObject result=new JSONObject();
		String str[]=delIds.split(",");
		for(int i=0;i<str.length;i++){		//判断每个要删除的角色
			boolean f=hUserDao.existUserWithRoleId(str[i]);//如果角色下面有用户
			if(f){
				result.put("errorIndex", i);
				result.put("errorMsg", "角色下面有用户，不能删除！");
				ResponseUtil.write(ServletActionContext.getResponse(), result);
				return null;
			}
		}
		try{
			int delNums=hRoleDao.roleDelete(delIds);
			result.put("success", true);
			result.put("delNums", delNums);
		}catch (Exception e) {
			e.printStackTrace();
			result.put("errorMsg", "删除失败");
		}
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	//添加角色
	public String roleSave() throws Exception {
		Role role=new Role(roleName, roleDescription); //封装构造
		if(StringUtil.isNotEmpty(roleId)){
			role.setRoleId(Integer.parseInt(roleId));
		}
		JSONObject result=new JSONObject();
		int saveNums=0;
		role.setAuthIds("1");;
		saveNums=hRoleDao.roleUpdate(role);
        if(saveNums>0){
        	result.put("success", true);
		}else{
			result.put("success", true);
			result.put("errorMsg", "保存失败");
		}
		ResponseUtil.write(ServletActionContext.getResponse(), result);
		return null;
	}
	
	//角色授权 按钮点击之后
	public void auth() throws Exception {
		//String roleId=request.getParameter("roleId");
		//String authIds=request.getParameter("authIds");//权限菜单的集合
		Role role=new Role(Integer.parseInt(roleId), authIds);
		JSONObject result=new JSONObject();
		int updateNums=hRoleDao.roleAuthIdsUpdate(role);
        if(updateNums>0){
        	result.put("success", true);
		}else{
			result.put("errorMsg", "授权失败");
		}
		ResponseUtil.write(ServletActionContext.getResponse(), result);
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

	public String getS_roleName() {
		return s_roleName;
	}

	public void setS_roleName(String s_roleName) {
		this.s_roleName = s_roleName;
	}
	
	public String getDelIds() {
		return delIds;
	}

	public void setDelIds(String delIds) {
		this.delIds = delIds;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getAuthIds() {
		return authIds;
	}

	public void setAuthIds(String authIds) {
		this.authIds = authIds;
	}

	
	
	public HRoleDao gethRoleDao() {
		return hRoleDao;
	}

	public void sethRoleDao(HRoleDao hRoleDao) {
		this.hRoleDao = hRoleDao;
	}

	public HUserDao gethUserDao() {
		return hUserDao;
	}

	public void sethUserDao(HUserDao hUserDao) {
		this.hUserDao = hUserDao;
	}

}
