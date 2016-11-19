package com.express.action;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.express.dao.AuthDao;
import com.express.dao.RoleDao;
import com.express.model.Auth;
import com.express.model.PageBean;
import com.express.model.User;
import com.express.util.DbUtil;
import com.express.util.JsonUtil;
import com.express.util.ResponseUtil;
import com.express.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class AuthAction extends ActionSupport implements ServletRequestAware{

	private String action;
	private String parentId;
	
	private String roleId;
	
	private String authId;
	private String authName;
	private String authPath;
	private String authDescription;
	private String iconCls;
	
	
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getAuthPath() {
		return authPath;
	}
	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}
	public String getAuthDescription() {
		return authDescription;
	}
	public void setAuthDescription(String authDescription) {
		this.authDescription = authDescription;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}




	

	
	DbUtil dbUtil=new DbUtil();
	AuthDao authDao=new AuthDao();
	RoleDao roleDao=new RoleDao();
	HttpServletRequest request;		//获取request
	//StudentDao studentDao=new StudentDao();
	
	public String execute() throws Exception {
		Connection con=null;
		//String parentId=request.getParameter("parentId");
		
		try{
			con=dbUtil.getCon();
			/*权限控制
			 * 
			 * */
			//获取session  获取当前的用户
			User currentUser=(User)ServletActionContext.getRequest().getSession().getAttribute("currentUser");
			String authIds=roleDao.getAuthIdsById(con, currentUser.getRoleId());
			//System.out.println("RoleId"+currentUser.getRoleId());
			JSONArray jsonArray=authDao.getAuthsByParentId(con, parentId,authIds);
			
			
			//获取response 用ServletActionContext.getResponse()
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return null;
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	//角色复选框
	public String authMenuAction()throws Exception {
		//String parentId=request.getParameter("parentId");
		//String roleId=request.getParameter("roleId");//获取某个人的权限而不是当前人的
		Connection con=null;
		try{
			con=dbUtil.getCon();
			String authIds=roleDao.getAuthIdsById(con, Integer.parseInt(roleId));
			JSONArray jsonArray=authDao.getCheckedAuthsByParentId(con, parentId,authIds);
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	//菜单管理
	public String  authTreeGridMenuAction() throws Exception {
		//String parentId=request.getParameter("parentId");
		Connection con=null;
		try{
			con=dbUtil.getCon();
			JSONArray jsonArray=authDao.getListByParentId(con, parentId);
			ResponseUtil.write(ServletActionContext.getResponse(), jsonArray);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	//菜单管理 修改
	public String savemenuAction() throws Exception {
//		String authId=request.getParameter("authId");
//		String authName=request.getParameter("authName");
//		String authPath=request.getParameter("authPath");
//		String parentId =request.getParameter("parentId");
//		String authDescription=request.getParameter("authDescription");
//		String iconCls=request.getParameter("iconCls");
		Auth auth=new Auth(authName, authPath, authDescription, iconCls);
	/*	System.out.println(authId);
		System.out.println(parentId);*/
		if(StringUtil.isNotEmpty(authId)){
			auth.setAuthId(Integer.parseInt(authId));//这是更新操作
		}else{
			auth.setParentId(Integer.parseInt(parentId));//这是添加操作
		}
		Connection con=null;
		boolean isLeaf=false;
		try{
			JSONObject result=new JSONObject();
			con=dbUtil.getCon();
			int saveNums=0;
			if(StringUtil.isNotEmpty(authId)){
				//更新操作
				saveNums=authDao.authUpdate(con, auth);
			}else{
				//添加操作   如果添加父亲，父亲原来没有儿子，就要改变父亲的状态，如果父亲原来就有儿子，则不需要改变父亲的状态
				isLeaf=authDao.isLeaf(con, parentId);//先判断是不是叶子节点
				if(isLeaf){		//是叶子节点
					con.setAutoCommit(false);	//加事务 Jdbc
					authDao.updateStateByAuthId(con, "closed", parentId);
					saveNums=authDao.authAdd(con, auth);
					con.commit();
				}else{
					saveNums=authDao.authAdd(con, auth);//不是叶子节点
				}
			}
			if(saveNums>0){
				result.put("success", true);
			}else{
				result.put("success", true);
				result.put("errorMsg", "保存失败");
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		}catch(Exception e){
			if(isLeaf){
				try {
					con.rollback();//JDBC回滚
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	//删除菜单
	/*1.当有兄弟菜单的时候，删除菜单
	 * 2.当只有一个子菜单，要删除这个子菜单的时候，它的节点要改变状态，同时删除这个菜单
	 * 3.当删除节点的时候，但是他还有儿子的时候，这就不能够删除
	 * */
	public String deletemenuAction() throws Exception {
//		String authId=request.getParameter("authId");
//		String parentId=request.getParameter("parentId");
		Connection con=null;
		int sonNum=-1;
		try{
			con=dbUtil.getCon();
			JSONObject result=new JSONObject();
			if(!authDao.isLeaf(con, authId)){
				result.put("errorMsg", "该菜单节点有子节点，不能删除！");
			}else{
				int delNums=0;
				sonNum=authDao.getAuthCountByParentId(con, parentId);//假如只有一个孩子
				if(sonNum==1){			//
					con.setAutoCommit(false);
					authDao.updateStateByAuthId(con, "open", parentId);//修改父亲的状态
					delNums=authDao.authDelete(con, authId);
					con.commit();
				}else{			//不止一个孩子
					delNums=authDao.authDelete(con, authId);
				}
				if(delNums>0){
					result.put("success", true);
				}else{
					result.put("errorMsg", "删除失败");
				}
			}
			ResponseUtil.write(ServletActionContext.getResponse(), result);
		}catch(Exception e){
			if(sonNum==1){
				try {
					con.rollback();//回滚数据
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	

	
}
