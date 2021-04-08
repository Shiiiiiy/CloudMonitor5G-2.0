<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="com.datang.common.util.DES3Utils"%>
<%@page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@page import="org.apache.shiro.authc.UnknownAccountException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>5G路测平台</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css" type="text/css" />
	<%@ include file="taglibs/jquery.jsp"%>
	<%@ include file="taglibs/easyui.jsp"%>
	<script type="text/javascript">
	function onPageLoad(){
		 var nameStr=myBrowser();
		 if(nameStr=="FF"||nameStr=="Chrome"){
			//$.messager.alert("提示","浏览器对啦！！！",'warning');
		}else{
			$.messager.alert("提示","推荐使用谷歌或火狐浏览器，其他浏览器可能会导致某些支持错误！！！",'warning');
		} 
		if(top.location!=self.location){
			top.location="<%=request.getContextPath()%>";
		}
	}
	<shiro:authenticated>
		window.location="${pageContext.request.contextPath}/login";
	</shiro:authenticated>
	function myBrowser(){
	    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	    if (userAgent.indexOf("Firefox") > -1) {
	        return "FF";
	    } //判断是否Firefox浏览器
	    if (userAgent.indexOf("Chrome") > -1){
	  		return "Chrome";
	 	}
	}
	</script>
</head>
<%
	Cookie[] c = request.getCookies();
	if(null!=c){
		//request.getRequestDispatcher("/login").forward(request, response);
		for(int i = 0 ;i<c.length;i++){
			String key = c[i].getName();
			if(key.equals("LOGIN_USERNAME")){
				String username = c[i].getValue();
				byte[] username_data = DES3Utils.des3DecodeECB(new BASE64Decoder().decodeBuffer(DES3Utils.USERNAME_KEY),new BASE64Decoder().decodeBuffer(username));// 解密
				String desUsename = new String(username_data);
				request.setAttribute("cookie_username", desUsename);
			}else if(key.equals("LOGIN_PASSWORD")){
				String password = c[i].getValue();
				byte[] password_data = DES3Utils.des3DecodeECB(new BASE64Decoder().decodeBuffer(DES3Utils.PASSWORD_KEY),new BASE64Decoder().decodeBuffer(password));// 解密
				String desPassword = new String(password_data);
				request.setAttribute("cookie_password", desPassword);
			}else if(key.equals("LOGIN_REMEMBER")){
				String remeber = c[i].getValue();
				request.setAttribute("cookie_remember", remeber);
			}
		}
	}
 %>
<body style="width:100%;height:100%;border:0;margin: 0;position:absolute;" onload="onPageLoad(); ">
	<div style="min-width:450px;width:100%;height:402px;position:absolute;top:50%;margin-top:-201px;background: url(${pageContext.request.contextPath}/images/login_01.jpg) repeat center center;">
		<div style="width:100%;height:402px;background: url(${pageContext.request.contextPath}/images/pick_bg.jpg) no-repeat center center;text-align: center;" align="center">
			<div style="width:593px;height:389px;top:2px;position:absolute;left:50%;margin-left:-296px;background: url(${pageContext.request.contextPath}/images/login.png) no-repeat center center;">
				<div style="width:100%;heigt:100%;position:absolute;top:170px;left:250px;text-align: left;" align="left">
					<form action="${pageContext.request.contextPath}/login" method="post">
						<div style="color: red;top:-20px;left:0px;position:absolute;" align="left" >${error}</div>
						<table >
							<tr>
								<td >账&nbsp;&nbsp;&nbsp;&nbsp;号</td>
								<td height="40px" colspan="2" align="right"><input type="text"    style="width:220px;height:30px;line-height:30px;border-color:#5b97db;border-width: 1px;border-style: solid;" name="username" value="${userName==null?cookie_username:userName}"/></td>
							</tr>
							<tr>
								<td >密&nbsp;&nbsp;&nbsp;&nbsp;码</td>
								<td height="40px" colspan="2" align="right"><input type="password" style="width:220px;height:30px;line-height:30px;border-color:#5b97db;border-width: 1px;border-style: solid;" name="password" value="${cookie_password}" /></td>
							</tr>
							<tr>
								<td></td>
								<td align="left">
									<input  type="checkbox" style="width:14px;height:14px;" <c:if test="${cookie_remember}">checked</c:if> name="rememberPassword" value="true" /><label style="font-size: 12px;">记住密码</label>
								</td>
								<td align="right">
									<input class="login"  type="submit" style="width:110px;height:33px;border:0" value="" />
								</td>
							</tr>
						</table>
					</form> 
				</div>
			</div>
		</div>
	</div>
	<%-- <div style="width: 208px;heigt:70px;z-index:9;position:absolute;left:30px;top:50%;margin-top: -300px;">
		<img width="208px"  heigt="70px" src="${pageContext.request.contextPath}/images/logo.png" />
	</div> --%>
	
	<div class="copyright">&copy; 版权所有 
		<span class="tip">
			<a href="javascript:void(0);" title="大唐移动">DTmobile</a>
			 推荐使用
			<a href="${pageContext.request.contextPath}/browser/chrome.exe" style="text-decoration:underline;" title="下载谷歌浏览器">谷歌</a>,
			<a href="${pageContext.request.contextPath}/browser/firefox.exe" style="text-decoration:underline;" title="下载火狐浏览器">火狐</a>
			<br>技术支持:
			<a href="javascript:void(0);" title="大唐移动">DTmobile</a> 
		</span>
	</div>
</body>
</html>