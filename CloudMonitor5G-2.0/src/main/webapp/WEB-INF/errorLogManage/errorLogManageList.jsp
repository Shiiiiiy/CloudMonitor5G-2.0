<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>导入工参</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
		$(function(){
			initTable();
		});
		function initTable(){
			$("#errorLogManageListTable").datagrid({
				// 表头
				columns:[[
					{field:'city',width:60,align:'center',title:'所属省市'}, 				
					{field:'softwareV',width:90,align:'center',title:'软件版本号'},
					{field:'terminalType',width:30,align:'center',title:'终端类型',
						formatter:function(value,row,index){
							if(value == 1){
								return "APP";
							}else if(value == 2){
								return "OUTUM";
							}
						return value;
					}},
					{field:'terminalV',width:60,align:'center',title:'终端版本号'},
					{field:'osV',width:30,align:'center',title:'操作系统版本'},
					{field:'uploadTime',width:60,align:'center',title:'错误信息日志上报时间',
						formatter:function(value,row,index){
							var date = row.uploadTime;
							var str = date + "";
							str = str.replace('T',' ');
						return str;
					}},
					{field:'download',width:75,align:'center',title:'测试计划版本',
						formatter:function(value,row,index){
							//return '<a title="测试日志下载" href="${pageContext.request.contextPath}/errorLogManage/downloadFTPFile.action?fileName=OUT_20180303170020ms1.lte" >下载</a>';
							return '<a href="#" onclick="downloadFile(' + index + ')" title="测试日志下载">下载</a> ';
						}
					},
					{field:'boxid',width:30,align:'center',title:'设备',hidden:'true'},
					{field:'id',width:30,align:'center',title:'id',hidden:'true'}
				]],
				url:'${pageContext.request.contextPath}/errorLogManage/doPageListJson.action',
				border:false,
				fitColumns:true,
				pagination:true,
				singleSelect:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				toolbar:'#tb'
			});
		}
		/* 多条件查询 */
		function pageQuery(){
			var city = $("#city").val();
			var softwareV = $("#softwareV").val();
			var osV = $("#osV").val();
			var terminalType = $("#terminalType").val();
			if(terminalType.toUpperCase  == "APP"){
				terminalType = "1";
			}else if(terminalType.toUpperCase  == "OUTUM"){
				terminalType = "2";
			}else{
				terminalType = "";
			}
			//var uploadTime = $("#uploadTime").val();
			$("#errorLogManageListTable").datagrid('load',{
				city : city,
				softwareV : softwareV,
				osV : osV,
				terminalType : terminalType,
				startTime : $("#startTime").datetimebox('getValue'),
				endTime : $("#endTime").datetimebox('getValue')
			});
		}
		
		function downloadFile(index){
			var rows = $("#errorLogManageListTable").datagrid("getRows");
			var row = rows[index];
			var date = row.uploadTime;
			var boxid = row.boxid;
			var time = date + "";
			time = time.replace('T',' ');
			if(time == null || time == '' || boxid == null || boxid == ''){
				 alert("无相关日志");
			}else{
				$.ajax({
		             type: "GET",
		             url: "${pageContext.request.contextPath}/errorLogManage/downloadFTPFile.action",
		             data: {'uploadTime':time,'boxid':boxid},
		             dataType: "json",
		             success: function(data){
		            	 if(data.msg != null){
		            		 alert(data.msg);
		            	 }
	                 }
		         });
			}
		}
		
	</script>
  </head>
  
  <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<!-- <s:property value="exception.message"/> -->
   	<table id="errorLogManageListTable"></table>
   	<div id="tb">
		所属省市： <input id="city" class="easyui-textbox" name="city" data-options="prompt:'请输入省市名...'" style="width:150px;">
		软件版本号： <input id="softwareV" class="easyui-textbox" name="softwareV" data-options="prompt:'请输入软件版本号...'" style="width:150px;">
		操作系统版本号： <input id="osV" class="easyui-textbox" name="osV" data-options="prompt:'请输入操作系统版本号...'" style="width:150px;">
		终端类型： <input id="terminalType" class="easyui-textbox" name="terminalType" data-options="prompt:'请输入终端类型...'" style="width:150px;">
		开始上报时间： <input id="startTime" class="easyui-datetimebox" name="uploadTime" data-options="prompt:'请输入开始时间...'" style="width:150px;">
		结束上报时间： <input id="endTime" class="easyui-datetimebox" name="uploadTime" data-options="prompt:'请输入结束时间...'" style="width:150px;">
		<!-- <div class="inputDivShow">开始时间
    		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:true,editable:false" />
    	</div> -->
		<a href="#" class="easyui-linkbutton" style="width:80px;" onclick="pageQuery();" iconCls="icon-search" plain='true'>查询</a>
	</div>
  </body>
</html>
