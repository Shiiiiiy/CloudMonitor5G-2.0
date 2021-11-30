<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>弱覆盖参数设置</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script> --%>
	<style type="text/css">
		.inputDivShow{
			display: inline-block; *
		display: inline;
			font-size: 12px;
			margin: 5px;
			padding-right: 0;
			text-align: left;
			width: 100%;
			padding-left: 10px;
		}
		.inputDivShow1{
			display: inline-block; *
		display: inline;
			font-size: 12px;
			margin: 5px;
			padding-left: 0;
			padding-right: 0;
			text-align: center;
			width: 160px;
		}
		.inputDivShow2{
			display: inline-block; *
		display: inline;
			font-size: 12px;
			margin: 5px;
			padding-left: 0;
			padding-right: 0;
			text-align: center;
			width: 80px;
		}
		.titleDivShow{
			display: inline-block; *
		display: inline;
			font-size: 12px;
			margin: 5px;
			padding-left: 0;
			padding-right: 0;
			text-align: right;
			width: 180px;
		}

	</style>
	<script type="text/javascript">
		$(function(){
			//是否在页面加载完成的时候加载门限参数的初始值缓存到页面?
		});
		/* 保存修改 */
		function submitForm(){
			var fieldNameEn = "";
			var fieldValue = "";
			var lteCoverage = $("#lteCoverage").textbox('getValue');
			fieldNameEn = fieldNameEn + "lteCoverage" + ",";
			fieldValue = fieldValue + lteCoverage + ",";

			var nrCoverage = $("#nrCoverage").textbox('getValue');
			fieldNameEn = fieldNameEn + "nrCoverage" + ",";
			fieldValue = fieldValue + nrCoverage + ",";

			var lterrcSuccess = $("#lterrcSuccess").textbox('getValue');
			fieldNameEn = fieldNameEn + "lterrcSuccess" + ",";
			fieldValue = fieldValue + lterrcSuccess + ",";

			var nrrrcSuccess = $("#nrrrcSuccess").textbox('getValue');
			fieldNameEn = fieldNameEn + "nrrrcSuccess" + ",";
			fieldValue = fieldValue + nrrrcSuccess + ",";

			var lteChange = $("#lteChange").textbox('getValue');
			fieldNameEn = fieldNameEn + "lteChange" + ",";
			fieldValue = fieldValue + lteChange + ",";

			var nrChange = $("#nrChange").textbox('getValue');
			fieldNameEn = fieldNameEn + "nrChange" + ",";
			fieldValue = fieldValue + nrChange + ",";

			var dnsDelay = $("#dnsDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "dnsDelay" + ",";
			fieldValue = fieldValue + dnsDelay + ",";

			var dnsSuccess = $("#dnsSuccess").textbox('getValue');
			fieldNameEn = fieldNameEn + "dnsSuccess" + ",";
			fieldValue = fieldValue + dnsSuccess + ",";

			var tcpDelay = $("#tcpDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "tcpDelay" + ",";
			fieldValue = fieldValue + tcpDelay + ",";

			var tcpSuccess = $("#tcpSuccess").textbox('getValue');
			fieldNameEn = fieldNameEn + "tcpSuccess" + ",";
			fieldValue = fieldValue + tcpSuccess + ",";

			var tcpRetry = $("#tcpRetry").textbox('getValue');
			fieldNameEn = fieldNameEn + "tcpRetry" + ",";
			fieldValue = fieldValue + tcpRetry + ",";

			var httpDelay = $("#httpDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "httpDelay" + ",";
			fieldValue = fieldValue + httpDelay + ",";

			var httpSuccess = $("#httpSuccess").textbox('getValue');
			fieldNameEn = fieldNameEn + "httpSuccess" + ",";
			fieldValue = fieldValue + httpSuccess + ",";

			var textDelay = $("#textDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "textDelay" + ",";
			fieldValue = fieldValue + textDelay + ",";

			var picDelay = $("#picDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "picDelay" + ",";
			fieldValue = fieldValue + picDelay + ",";

			var musicDelay = $("#musicDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "musicDelay" + ",";
			fieldValue = fieldValue + musicDelay + ",";

			var videoDelay = $("#videoDelay").textbox('getValue');
			fieldNameEn = fieldNameEn + "videoDelay" + ",";
			fieldValue = fieldValue + videoDelay + ",";



			$.ajax({
				url:"${pageContext.request.contextPath}/coverageParam/saveWeakCoverThreshold.action",
				dataType:"json",
				type:"post",
				data: {'fieldNameEn':fieldNameEn,'fieldValue':fieldValue},
				success:function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$.messager.alert('提示','修改成功','info');
					}
				}
			});

		}
		/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					$("#lteCoverage").textbox('setValue',"90");
					$("#nrCoverage").textbox('setValue',"90");
					$("#lterrcSuccess").textbox('setValue',"95");
					$("#nrrrcSuccess").textbox('setValue',"95");
					$("#lteChange").textbox('setValue',"95");
					$("#nrChange").textbox('setValue',"95");

					$("#dnsDelay").textbox('setValue',"100");
					$("#dnsSuccess").textbox('setValue',"95");
					$("#tcpDelay").textbox('setValue',"50");
					$("#tcpSuccess").textbox('setValue',"95");
					$("#tcpRetry").textbox('setValue',"10");

					$("#httpDelay").textbox('setValue',"100");
					$("#httpSuccess").textbox('setValue',"95");
					$("#textDelay").textbox('setValue',"200");
					$("#picDelay").textbox('setValue',"500");
					$("#musicDelay").textbox('setValue',"500");
					$("#videoDelay").textbox('setValue',"500");



				}
			});
		}


	</script>
</head>
<body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<div style="width:32%;height:90%;float:left; margin-right: 10px;border:1px solid #95b8e7;">
	<div data-options="region:'west',border:false,split:true">
		<div style="width:100%;height:100%;">
			<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
				<div class="panel-heading">
					<div class="panel-title">物理层</div>
				</div>
			</div>
			<div class="inputDivShow">LTE覆盖率>=
				<input id="lteCoverage" name="lteCoverage" value="${entity.lteCoverage}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">NR覆盖率>=
				<input id="nrCoverage" name="nrCoverage" value="${entity.nrCoverage}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">LTE RRC连接成功率>=
				<input id="lterrcSuccess" name="lterrcSuccess" value="${entity.lterrcSuccess}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">NR RRC连接成功率>=
				<input id="nrrrcSuccess" name="nrrrcSuccess" value="${entity.nrrrcSuccess}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">LTE切换成功率>=
				<input id="lteChange" name="lteChange" value="${entity.lteChange}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">NR切换成功率>=
				<input id="nrChange" name="nrChange" value="${entity.nrChange}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>

		</div>
	</div>
</div>
<div style="width:32%;height:90%;float:left; margin-right: 10px;border:1px solid #95b8e7;">
	<div data-options="region:'west',border:false,split:true">
		<div style="width:100%;height:100%;">
			<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
				<div class="panel-heading">
					<div class="panel-title">链路层</div>
				</div>
			</div>
			<div class="inputDivShow">DNS解析时延<=
				<input id="dnsDelay" name="dnsDelay" value="${entity.dnsDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
				ms
			</div>
			<div class="inputDivShow">DNS解析成功率>=
				<input id="dnsSuccess" name="dnsSuccess" value="${entity.dnsSuccess}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">TCP建链时延<=
				<input id="tcpDelay" name="tcpDelay" value="${entity.tcpDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
				ms
			</div>
			<div class="inputDivShow">TCP建链成功率>=
				<input id="tcpSuccess" name="tcpSuccess" value="${entity.tcpSuccess}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">TCP重传率<=
				<input id="tcpRetry" name="tcpRetry" value="${entity.tcpRetry}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
		</div>
	</div>
</div>

<div style="width:32%;height:90%;float:left; margin-right: 10px;border:1px solid #95b8e7;">
	<div data-options="region:'west',border:false,split:true">
		<div style="width:100%;height:100%;">
			<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
				<div class="panel-heading">
					<div class="panel-title">应用层</div>
				</div>
			</div>
			<div class="inputDivShow">HTTP响应时延<=
				<input id="httpDelay" name="httpDelay" value="${entity.httpDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
				ms
			</div>
			<div class="inputDivShow">HTTP响应成功率>=
				<input id="httpSuccess" name="httpSuccess" value="${entity.httpSuccess}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:0,max:100 " />
				%
			</div>
			<div class="inputDivShow">文本加载时延<=
				<input id="textDelay" name="textDelay" value="${entity.textDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
				ms
			</div>
			<div class="inputDivShow">图片加载时延<=
				<input id="picDelay" name="picDelay" value="${entity.picDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
				ms
			</div>
			<div class="inputDivShow">音频加载时延<=
				<input id="musicDelay" name="musicDelay" value="${entity.musicDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
				ms
			</div>
			<div class="inputDivShow">视频加载时延<=
				<input id="videoDelay" name="videoDelay" value="${entity.videoDelay}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
				ms
			</div>
		</div>
	</div>
</div>

<div data-options="region:'south',border:false" style="height:10%;">
	<table width="100%" style="border-top:1px solid #95b8e7;">
		<tr height="35px">
			<td width="50%;" align="right">
				<shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
					<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >保存</a>
				</shiroextend:hasAnyPermissions>
			</td>
			<td width="50%;">
				<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">
					<a class="easyui-linkbutton" iconCls="icon-reload" style="width:100px;" onclick="resetForm();" >重置</a>
				</shiroextend:hasAnyPermissions>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
