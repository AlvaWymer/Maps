<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统主界面</title>
<%
	if(session.getAttribute("currentUser")==null){
		response.sendRedirect("WebsiteBackgroundManagementSystemIndex.jsp");
		return;
	}
%>
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
function setDateTime(){
			var date=new Date();
			var day=date.getDay();
			var week;
			switch(day){
			case 0:week="星期日";break;
			case 1:week="星期一";break;
			case 2:week="星期二";break;
			case 3:week="星期三";break;
			case 4:week="星期四";break;
			case 5:week="星期五";break;
			case 6:week="星期六";break;
			}
			var today=date.getFullYear()+"年"+(date.getMonth()+1)+"月"+date.getDate()+"日  "+week+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
			document.getElementById("today").innerHTML=today;
		}
		
		window.setInterval("setDateTime()", 1000);
		
	$(function(){
		$("#tree").tree({
			lines:true,//树加线线
			url:'AuthAction?parentId=-1',//parentId=-1是在数据库中获得整个系统的节点
			onLoadSuccess:function(){			//默认树全部展开
				$("#tree").tree('expandAll');
			},
			onClick:function(node){		//onClick事件  树节点
				if(node.id==16){	//id为数据库中安全退出的按钮的时候，安全退出操作
					logout();
				}else if(node.id==15){//修改密码
					openPasswordModifyDialog();
				}else　if(node.attributes.authPath){//判断防止点击节点的时候出现tab
					openTab(node);  		//调用下面的方法
				}
			}
		});
		
		function logout(){//安全退出方法
			$.messager.confirm('系统提示','您确定要退出系统吗？',function(r){//系统提示
				if(r){
					window.location.href='loginout';//重定向
				}
			});
		}
		
		function openPasswordModifyDialog(){
			url="update";		//全局的url，下面的方法会用到modifyPassword()
			//打开dialog
			$("#dlg").dialog("open").dialog("setTitle","修改密码");
		}
		
		function openTab(node){						//专门用来打开tab的 方法
		
			if($("#tabs").tabs("exists",node.text)){  //判断重复  
				$("#tabs").tabs("select",node.text);	//如果存在的话就选中
			}else{
			//node.attributes.authPath  是后台传入的  在AuthDao里面如下：
			//	attributeObject.put("authPath", rs.getString("authPath"));//先赋值
			//jsonObject.put("attributes", attributeObject);//再放入  他也是个对象
				var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src="+node.attributes.authPath+"></iframe>"
				$("#tabs").tabs("add",{
					title:node.text,
					iconCls:node.iconCls,			//tab显示图标
					closable:true,					//有关闭按钮
					content:content
				});
			}
		}
	});
	
	
	function modifyPassword(){				//保存按钮事件
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
			//获取密码  jquarly方法获取
				var oldPassword=$("#oldPassword").val();
				var newPassword=$("#newPassword").val();
				var newPassword2=$("#newPassword2").val();
				
				//验证方法
				if(!$(this).form("validate")){
					return false;
				}
				//原密码是否正确
				if(oldPassword!='${currentUser.password}'){
					$.messager.alert('系统提示','原密码输入错误！');
					return false;
				}
				if(newPassword!=newPassword2){
					$.messager.alert('系统提示','确认密码不匹配，请重新输入！');
					return false;
				}
				return true;
			},
			success:function(result){
				//alert(result);
				var result=eval('('+result+')');//json串变为对象
				if(result.errorMsg){
					$.messager.alert('系统提示',result.errorMsg);
					return;
				}else{
					$.messager.alert('系统提示','密码修改成功，下一次登录生效！');
					closePasswordModifyDialog();//修改完关闭掉窗口
				}
			}
		});
	}
	
	function closePasswordModifyDialog(){
		$("#dlg").dialog("close");//关闭
		$("#oldPassword").val("");
		$("#newPassword").val("");//重置密码框
		$("#newPassword2").val("");
	}
</script>
<script language="JavaScript">

javascript:window.history.forward(1);  //解决后退问题

</script>
</head>
<body class="easyui-layout" >
<div region="north" style="height: 68px;">
<div style="padding: 0px;margin: 0px;background-color: #E0ECFF;">
<table>
	<tr>
		<td><img src="images/mainlogo.png"/></td>
		<td valign="bottom"><font  size="4">欢迎：</font><font color="red" size="4">${currentUser.userName }</font><font  size="4">&nbsp;『${currentUser.roleName}』</font>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;
		<font id="today" class="currentDateTime" style="font-size:20px;"></font></td>
	</tr>
</table>
</div>
</div>
<div region="center">
	<div class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="首页" data-options="iconCls:'icon-home'">
			<div align="center" style="padding-top: 100px;"><font color="red" size="10">欢迎进入物流管理后台</font></div>
		</div>
	</div>
</div>
<div region="west" style="width: 160px;padding: 5px;" title="导航菜单" split="true">
<ul id="tree" class="easyui-tree"></ul>
</div>
<div region="south" style="height: 25px;padding: 5px;" align="center">
	版权所有 2014 程序猿物流网 <a href="" target="_blank">留白</a>
</div>

<div id="dlg" class="easyui-dialog" style="width: 400px;height: 220px;padding: 10px 20px" 
 closed="true" buttons="#dlg-buttons" data-options="iconCls:'icon-modifyPassword'">
 <form id="fm" method="post">
 	<table cellspacing="4px;">
 		<tr>
 			<td>用户名：</td>
 			<td><input type="hidden" name="userId" id="userId" value="${currentUser.userId }"><!-- 隐藏域 -->
 			<input type="text" name="userName" id="userName" readonly="readonly" value="${currentUser.userName }" style="width: 200px;" /></td>
 		</tr>
 		<tr>
 			<td>原密码：</td>
 			<td><input type="password" class="easyui-validatebox" name="oldPassword" id="oldPassword" style="width: 200px;" required="true" /></td>
 		</tr>
 		<tr>
 			<td>新密码：</td>
 			<td><input type="password" class="easyui-validatebox" name="newPassword" id="newPassword" style="width: 200px;" required="true"  /></td>
 		</tr>
 		<tr>
 			<td>确认新密码：</td>
 			<td><input type="password" class="easyui-validatebox" name="newPassword2" id="newPassword2" style="width: 200px;" required="true" /></td>
 		</tr>
 	</table>
 </form>
</div>
<div id="dlg-buttons">
	<a href="javascript:modifyPassword()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	<a href="javascript:closePasswordModifyDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>