<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>反开3d报告生成</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<style type="text/css">
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
   		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    padding-left: 0;
		    padding-right: 0;
		     margin: 5px;
		    text-align: right;
		    width: 23%;
   		}
	</style>
	<script type="text/javascript">
		
		$(function(){
			initTable();
		});
		var city = "全部";
		
		function colorFunc(value,leftValue){
			if(value == 1){
				return "<div style='height:20px;width:20px;background: #00B050;margin-left:"+leftValue+"px;'></div>";
			}else if(value == 2){
				return "<div style='height:20px;width:20px;background: #FFFF00;margin-left:"+leftValue+"px;'></div>";
			}else if(value == 0){
				return "<div style='height:20px;width:20px;background: red;margin-left:"+leftValue+"px;'></div>"
			}
		}
		
		/* 初始化测试计划列表 */
		function initTable(){
			$("#stationLogTable").datagrid({
				// 表头
				columns:[[
					{field:'id',checkbox:true},
					{field:'siteName',width:100,align:'center',title:'SiteName'}, 
					{field:'enbId',width:100,align:'center',title:'enbId'},
					{field:'cellName',width:100,align:'center',title:'CellName'},
					{field:'longitude',width:100,align:'center',title:'经度'},
					{field:'latitude',width:100,align:'center',title:'纬度'},
					{field:'localCellId',width:100,align:'center',title:'LocalCellID'},
					/* {field:'reportStatus',width:80,align:'center',title:'报告是否生成',
						formatter:function(value,row,index){
							if(row.stationVerificationLogPojoList != null
								&& row.stationVerificationLogPojoList.size > 0
								&& row.stationVerificationLogPojoList[0].reportStatus == 1
								){
								return "已生成";
							}else{
								return "未生成";
							}
						}
					}, */
					{field:'createReportDate',width:80,align:'center',title:'生成报告日期'},
					{field:'region',width:100,align:'center',title:'区域'},
					{field:'ftpDownloadGood',width:80,align:'center',title:'极好点FTP下载',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'ftpUploadGood',width:80,align:'center',title:'极好点FTP上传',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'ping32Good',width:80,align:'center',title:'极好点ping（32bytes）',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'goodEndcSuccessRatio',width:80,align:'center',title:'ENDC成功率测试测试',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'raodianTest',width:80,align:'center',title:'绕点',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'csfTest',width:80,align:'center',title:'CSFB',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'volteTest',width:80,align:'center',title:'Volte',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					}
				]],
				url:'${pageContext.request.contextPath}/oppositeOpen3dReportCreate/doPageListJson.action?region='+city,
				border:false,
				fitColumns:false,
				pagination:true,
				pageSize:20,
				pageList:[20,50,100,200,500],
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				//singleSelect:true,
				toolbar:'#tb',
				onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
		            
		        },
		        onClickRow: function(rowIndex, rowData){
		        	$('#updateParamId').linkbutton('enable');
		        }
			});
		}
	
		function areaTreeFunction(node){
			/* 单击树节点 */
			if(-1==node.id||'Province'==node.attributes.type){
				return ; 
			}
			city = node.text;
			initTable();
		}
		/*多条件查询*/
		function pageQuery(){
			city = "全部";
			var select = $('#areaTree').tree('getSelected');
			if(select == null || select.children != null){
				city = "全部";
			}else{
				city = select.text;
			}
			var startTime = $("#testTimeStartQuery").datebox('getValue');
			var endTime = $("#testTimeEndQuery").datebox('getValue');
			var testService = $("#testServiceParam").combobox('getValue');
			var testStatus = $("#testStatusParam").combobox('getValue');
			var siteName = $("#SiteNameQuery").textbox('getValue');
			var gnbId = $("#gnbIdQuery").numberbox('getValue');
			if(startTime != null && startTime != ""){
				startTime = startTime.replace(/-/g,"")
			}
			if(endTime != null && endTime != ""){
				endTime = endTime.replace(/-/g,"")
			}
			if(testService != "" && testStatus == ""){
				$.messager.alert("提示","请选择测试状态",'warning');
				return ;
			}
			
			if(testService == "" && testStatus != ""){
				$.messager.alert("提示","请选择测试业务类型",'warning');
				return ;
			}
			
			$("#stationLogTable").datagrid('load',{
				startTime:startTime,
				endTime : endTime,
				testService : testService,
				testStatus : testStatus,
				siteName : siteName,
				enbId : gnbId
			});
		}
		
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
		}
		
		function createReport(){
			var nodes = $('#stationLogTable').datagrid('getSelections');
			var ids = '';
			var siteNames = '';
			var flag = false;
			if(nodes.length > 0){
				for(var i = 0; i < nodes.length;i++){
					if(i != 0){
						ids = ids + ',';
						siteNames = siteNames + ',';
					}
					ids = ids + nodes[i].id;
					siteNames = siteNames + nodes[i].siteName;
					if(nodes[i].reportCreateDate != null && nodes[i].reportCreateDate != ""){
						flag = true;
					}
				}
			}else{
				$.messager.alert('提示','请选择需要生成报告的站点','info');
				return;
			}
			if(flag){
				$.messager.confirm("系统提示", "有的站点单验报告已经生成，这次生成会覆盖旧的记录，确认生成吗?", function(r) {
					if (r) {
						$.post(
							"${pageContext.request.contextPath}/oppositeOpen3dReportCreate/createReport.action",
							{ids:ids,siteNames:siteNames},
							function(result){
								if(result.errorMsg != null){
									$.messager.alert('提示',result.errorMsg,'info');
								}else if(result.cellNames == null){
									$.messager.alert('提示','报告已生成','info');
								}else{
									$.messager.alert('提示','报告已生成,其中'+result.cellNames+'无单站数据,无法生成单验报告!','info');
								}
								pageQuery();
							}
						,"json");
					}
				});
			}else{
				$.post(
					"${pageContext.request.contextPath}/oppositeOpen3dReportCreate/createReport.action",
						{ids:ids},
						function(result){
								if(result.errorMsg != null){
									$.messager.alert('提示','报告已生成,其中:'+result.errorMsg,'info');
								}else if(result.cellNames == null){
									$.messager.alert('提示','报告已生成','info');
								}else{
									$.messager.alert('提示','报告已生成,其中'+result.cellNames+'无单站数据,无法生成单验报告!','info');
								}
								pageQuery();
					}
				,"json");
			}
		}
		
	</script>

  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div data-options="region:'west',title:'区域列表',split:true,tools:'#tt3'" style="width:20%;overflow: auto;" class="easyui-layout">
    	<ul id="areaTree" class="easyui-tree"  data-options="onClick:areaTreeFunction,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',lines:true,
    	formatter:function(node){
	    	return node.text;
    	}"></ul>
  	</div>
    <div data-options="region:'center',title:'日志列表',split:false,tools:'#tt3'"  style="width:70%;">
    	<table id="stationLogTable"></table>
    	<div id="tb">
	    	<form id="queryForm" class="esyui-form" method="post" >
	    		<div class="inputDivShow">
	    			报告生成开始日期：<input id="testTimeStartQuery" class="easyui-datebox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			报告生成结束日期：<input id="testTimeEndQuery" class="easyui-datebox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			测试业务类型：
	    			<select id="testServiceParam" class="easyui-combobox" name="dept" style="width:140px;" data-options="editable:false">
				        <option value="">&nbsp;</option>
				        <option value="0">FTP下载</option>
				        <option value="1">FTP上传</option>
				        <option value="2">ENDC成功率测试</option>
				        <option value="3">CSFB测试</option>
				        <option value="4">Volte测试</option>
				        <option value="5">ping（32）测试</option>
				        <option value="6">绕点测试</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow">
	    			测试状态：
					<select id="testStatusParam" class="easyui-combobox" name="dept" style="width:140px;" data-options="editable:false">
				        <option value="">&nbsp;</option>
				        <option value="1">已完成</option>
				        <option value="0">未完成</option>
				        <option value="2">有更新</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow">
	    			SiteName：<input id="SiteNameQuery" class="easyui-textbox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			eNodeB-ID：<input id="gnbIdQuery" class="easyui-numberbox" style="width:125px;"/>
	    		</div>
				<table width="100%">
		    		<tr>
			    		<td width="45%" align="right">
			    			<shiroextend:hasAnyPermissions name="oppositeCreate:create">
			    				<a class="easyui-linkbutton" onclick="createReport();" style="width: 80px;" data-options="iconCls:'icon-excel'" >生成报告</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="10%" align="center">
			    			<shiroextend:hasAnyPermissions name="oppositeCreate:query">
			    				<a class="easyui-linkbutton" onclick="pageQuery();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="45%" align="left"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a></td>
		    		</tr>
		    	</table>
	    	</form>
		</div>
	</div>
  </body>
</html>
