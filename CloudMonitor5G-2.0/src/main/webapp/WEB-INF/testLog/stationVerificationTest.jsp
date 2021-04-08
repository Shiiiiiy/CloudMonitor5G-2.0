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
    <title>单验日志管理</title>
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
	</style>
	<script type="text/javascript">
		
		$(function(){
			initTable();
		});
		var cityId = -1;
		/* 初始化测试计划列表 */
		function initTable(){
			$("#stationLogTable").datagrid({
				// 表头
				columns:[[
					{field:'id',checkbox:true},
					{field:'fileName',width:100,align:'center',title:'文件名',width:'450px'}, 
					{field:'logSource',width:100,align:'center',title:'数据来源',
						formatter:function(value,row,index){
							if(value == 0){
								return 'APP';
							}else{
								return value;
							}
						}
					},
					{field:'testFileStatus',width:80,align:'center',title:'是否解析',
						formatter:function(value,row,index){
							if(value == 2){
								return '已解析';
							}else{
								return '未解析';
							}
						}
					},
					{field:'uploadedSize',width:100,align:'center',title:'文件大小(M)',
						formatter:function(value,row,index){
							if(value){
							var sizeMb = value/1024/1024;
							sizeMb = sizeMb.toFixed(3);
								return sizeMb;
							}else{
								return value;
							}
						} 
					},
					{field:'region',width:100,align:'center',title:'区域'},
					{field:'correlativeCell',width:80,align:'center',title:'相关小区'},
					{field:'testService',width:100,align:'center',title:'测试业务',
						formatter:function(value,row,index){
							return value;
						}
					},
					{field:'wirelessStatus',width:100,align:'center',title:'无线情况',
						formatter:function(value,row,index){
							if(value == "J"){
							 	return '极好点'
							}else if(value == "Z"){
								return '中点';
							}else if(value == "H"){
								return '好点';
							}else if(value == "C"){
								return '差点';
							}
						}
					},
					{field:'logVersion',width:75,align:'center',title:'测试计划版本号'},
					{field:'testLevel',width:75,align:'center',title:'测试级别',
						formatter: function(value,row,index){
							if('organizationCheck' == value){
								return '组织巡检';
							}else if('dailyOptimiz' == value){
								return '日常优化';
							}else if('deviceDebug' == value){
								return '设备调试(单站验证)';
							}else{
								return value;
							}
						}
					},
					{field:'testTarget',width:75,align:'center',title:'测试目标'},						
					//{field:'operatorName',width:100,align:'center',title:'运营商'},
					/* {field:'reportStatus',width:80,align:'center',title:'是否生成报告',
						formatter:function(value,row,index){
							if(value){
								return '已生成';
							}else{
								return '未生成';
							}
						}
					}, */
					{field:'uploadTime',width:100,align:'center',title:'上传时间',
						formatter:function(value,row,index){
							if(value){
							 	var time = new Date(value);
							 	var y = time.getFullYear(); //getFullYear方法以四位数字返回年份
						        var M = time.getMonth() + 1; // getMonth方法从 Date 对象返回月份 (0 ~ 11)，返回结果需要手动加一
						        var d = time.getDate(); // getDate方法从 Date 对象返回一个月中的某一天 (1 ~ 31)
						        var h = time.getHours(); // getHours方法返回 Date 对象的小时 (0 ~ 23)
						        var m = time.getMinutes(); // getMinutes方法返回 Date 对象的分钟 (0 ~ 59)
						        var s = time.getSeconds(); // getSeconds方法返回 Date 对象的秒数 (0 ~ 59)
						        return y + '-' + M + '-' + d + ' ' + h + ':' + m + ':' + s;
							}else{
								return '';
							}
						}
					},
					{field:'testTime',width:100,align:'center',title:'测试时间'},
					{field:'satifyTarget',width:100,align:'center',title:'是否达标',
						formatter:function(value,row,index){
							if(value == "0"){
							 	return '否'
							}else if(value == "1"){
								return '是';
							}else{
								return value;
							}
						}
					},
					{field:'boxId',width:100,align:'center',title:'测试设备ID'}
				]],
				url:'${pageContext.request.contextPath}/stationVerificationTest/doPageListJson.action?cityId='+cityId,
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
		        	var nodes = $('#stationLogTable').datagrid('getSelections');
		        	if(nodes.length == 1){
				        
				        var value = nodes[0].testService.replace(" ", "");
						if(value=="绕点"){
							ts = "DT";
						}else if(value=="FTP上传"){
							ts = "UL";
						}else if(value=="FTP下载"){
							ts = "DL";
						}else if(value=="PING（32）测试"){
							ts = "PING#32";
						}else if(value=="PING（1500）测试"){
							ts = "PING#1500";
						}else if(value=="ENDC成功率测试"){
							ts = "ATT";
						}else if(value=="volte测试"){
							ts = "volteTest";
						}else if(value=="CSFB测试"){
							ts = "CS";
						}else if(value=="绕点_下载"){
							ts = "raoDL";
						}else if(value=="绕点_上传"){
							ts = "raoUL";
						}else{
							ts = "";
						}
			        	$('#updateParamId').linkbutton('enable');
			        	$("#correlativeCellParam").textbox('setValue',nodes[0].correlativeCell);
						$("#testServiceParam").combobox('setValue',ts);
						$("#wirelessStatusParam").combobox('setValue',nodes[0].wirelessStatus);
						$("#testTimeParam").numberbox('setValue',nodes[0].testTime);
		        	}else if(nodes.length > 1){
		        		$('#updateParamId').linkbutton('enable');
			        	$("#correlativeCellParam").textbox('setValue',"");
						$("#testServiceParam").combobox('setValue',"");
						$("#wirelessStatusParam").combobox('setValue',"");
						$("#testTimeParam").numberbox('setValue',"");
		        	}else{
		        		$('#updateParamId').linkbutton('disable');
			        	$("#correlativeCellParam").textbox('setValue',"");
						$("#testServiceParam").combobox('setValue',"");
						$("#wirelessStatusParam").combobox('setValue',"");
						$("#testTimeParam").numberbox('setValue',"");
		        	}
		        }
			});
		}
	
		function areaTreeFunction(node){
			/* 单击树节点 */
			if(-1==node.id||'Province'==node.attributes.type){
				return ; 
			}
			cityId = node.attributes.refId;
			initTable();
			$('#updateParamId').linkbutton('disable');
		}
		/*多条件查询*/
		function pageQuery(){
			cityId = -1;
			var select = $('#areaTree').tree('getSelected');
			if(select == null || select.children != null){
				cityId = -1;
			}else{
				cityId = select.id;
			}
			var testFileStatus = null;
			if($("#testFileStatusQuery").combobox('getValue') == '未解析'){
				testFileStatus = 1;
			}else if($("#testFileStatusQuery").combobox('getValue') == '已解析'){
				testFileStatus = 2;
			}else{
				testFileStatus = null;
			}
			$("#stationLogTable").datagrid('load',{
				testTimeStartQuery:$("#testTimeStartQuery").numberbox('getValue'),
				testTimeEndQuery:$("#testTimeEndQuery").numberbox('getValue'),
				logSource:$("#logSourceQuery").combobox('getValue'),
				fileName:$("#fileNameQuery").textbox('getValue'),
				testFileStatus:testFileStatus,
				correlativeCell:$("#correlativeCellQuery").textbox('getValue'),
				testService:$("#testServiceQuery").combobox('getValue'),
				wirelessStatus:$("#wirelessStatusQuery").combobox('getValue')
			});
			$('#updateParamId').linkbutton('disable');
		}
		
		/*删除操作*/
		function delTestPlan(){
			var ids = '';
			var checked = $("#stationLogTable").datagrid('getSelections');
			if(checked.length <1){// 操作前至少选中一条
				$.messager.alert("提示","请选择一条记录!",'warning');
				return ;
			}
			for(var i = 0; i < checked.length;i++){
				ids = ids + checked[i].id;
				if(i != checked.length-1){
					ids = ids + ',';
				}
			}
			$.messager.confirm("系统提示", "您确定要删除选中的日志记录吗?", function(r) {
				if (r) {
					$.post(
						"${pageContext.request.contextPath}/stationVerificationTest/delete.action",
						{idStr:ids},
						function(result){
							$.messager.alert('提示','删除成功','info');
							pageQuery();
						}
					,"json");	
				}
			});
		}
		
		/*下载操作*/
		function downloadLog(){
			var ids = '';
			var checked = $("#stationLogTable").datagrid('getSelections');
			if(checked.length < 1){// 操作前至少选中一条
				$.messager.alert("提示","请选择一条记录!",'warning');
				return ;
			}
			for(var i = 0; i < checked.length;i++){
				ids = ids + checked[i].id;
				if(i != checked.length-1){
					ids = ids + ',';
				}
			}
			window.location= "${pageContext.request.contextPath}/stationVerificationTest/downloadLog.action?idStr="+ids;
		}
		
		/*修改参数*/
		function updateParam(){
			var checked = $("#stationLogTable").datagrid('getSelections');
			var ids = [];
			for(var i = 0; i < checked.length;i++){
				ids.push(checked[i].id);
			}
			$.post(
					"${pageContext.request.contextPath}/stationVerificationTest/updateParam.action",
					{
						idStr:ids.join(","),
						correlativeCell:$("#correlativeCellParam").textbox('getValue'),
						testService:$("#testServiceParam").combobox('getValue'),
						wirelessStatus:$("#wirelessStatusParam").combobox('getValue'),
						testTime:$("#testTimeParam").numberbox('getValue')
					},
					function(result){
						$.messager.alert('提示','修改成功','info');
						pageQuery();
					}
			,"json");
		}
		
		
	</script>

  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div data-options="region:'west',title:'区域选择',split:false,tools:'#tt3'" style="width:20%;" class="easyui-layout">
		<div  data-options="region:'north',title:'',split:false" style="height:70%;overflow: auto;" class="easyui-layout">
	    	<ul id="areaTree" class="easyui-tree"  data-options="onClick:areaTreeFunction,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',lines:true,
	    	formatter:function(node){
		    	return node.text;
	    	}"></ul>
	    </div>
	    <div data-options="region:'south',title:'日志属性确认',split:false" style="height:30%;" class="easyui-layout">
	    	<div style="width:100%;margin-top: 5px;">
	    		<div style="width: 35%;float: left;text-align: right;margin-top: 5px;">相关小区：</div>
	    		<div style="width: 65%;float: left;margin-top: 5px;">
	    			<input id="correlativeCellParam" class="easyui-textbox" data-options=""style="width:150px;"/>
	    		</div>
	    		
	    	</div>
	    	<div style="width:100%;margin-top: 5px;'">
	    		<div style="width: 35%;float: left;text-align: right;margin-top: 5px;">测试业务：</div>
    		    <div style="width: 65%;float: left;margin-top: 5px;">
	    		    <select id="testServiceParam" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
				        <option value="">&nbsp;</option>
				        <option value="DT">绕点</option>
				        <option value="DL">FTP下载</option>
				        <option value="UL">FTP上传</option>
				        <option value="ATT">ENDC成功率测试</option>
				        <option value="PING#32">PING（32）测试</option>
				        <option value="PING#1500">PING（1500）测试</option>
				        <option value="CS">CSFB测试</option>
				        <option value="volteTest">volte测试</option>
				        <option value="raoDL">绕点_下载</option>
				        <option value="raoUL">绕点_上传</option>
				    </select>
				 </div>
			</div>
	    	<div style="width:100%;margin-top: 5px;">
	    		<div style="width: 35%;float: left;text-align: right;margin-top: 5px;">无线情况：</div>
	    		<div style="width: 65%;float: left;margin-top: 5px;">
		    		<select id="wirelessStatusParam" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
				        <option value="">&nbsp;</option>
				        <option value="J">极好点</option>
				        <option value="H">好点</option>
				        <option value="Z">中点</option>
				        <option value="C">差点</option>
				    </select>
				 </div>
			</div>
	    	<div style="width:100%;margin-top: 5px;">
	    		<div style="width: 35%;float: left;text-align: right;margin-top: 5px;">测试日期：</div>
	    		<div style="width: 65%;float: left;margin-top: 5px;">
	    			<input id="testTimeParam" class="easyui-numberbox" data-options="" style="width:150px;"/>
	    		</div>
	    	</div>
	    	<div style="width:100%;text-align: center;margin-top: 5px;">
	    		<a style="margin-top: 5px;" class="easyui-linkbutton" id="updateParamId" data-options="plain:true,disabled:true" onclick="updateParam();">日志属性确认</a>
	    	</div>
	    	
	    </div>
  	</div>
    <div data-options="region:'center',title:'日志列表',split:false,tools:'#tt3'"  style="width:70%;">
    	<table id="stationLogTable"></table>
	    	<div id="tb">
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">测试开始日期：</div>
	    			<div style="width: 65%;float:left;"><input id="testTimeStartQuery" class="easyui-numberbox" style="width:150px;"/></div>
	    		</div>
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">测试结束日期：</div>
	    			<div style="width: 65%;float:left;"><input id="testTimeEndQuery" class="easyui-numberbox" style="width:150px;"/></div>
	    		</div>
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">数据来源：</div>
	    			<div style="width: 65%;float:left;">
		    			<select id="logSourceQuery" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
					        <option value="">&nbsp;</option>
					        <option value="ETG">ETG</option>
					        <option value="0">APP</option>
					    </select>
					</div>
	    		</div>
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">文件名：</div>
	    			<div style="width: 65%;float:left;"><input id="fileNameQuery" class="easyui-textbox" style="width:150px;"/></div>
	    		</div>
	    		<!--<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">运营商：</div>
	    			<div style="width: 65%;float:left;">
		    			<select id="operatorNameQuery" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
					        <option value="">&nbsp;</option>
					        <option>中国移动</option>
					   	</select>
					</div>
	    		</div>   -->
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">解析状态：</div>
	    			<div style="width: 65%;float:left;">
		    			<select id="testFileStatusQuery" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
					        <option value="">&nbsp;</option>
					        <option>已解析</option>
					        <option>未解析</option>
					    </select>
					</div>
	    		</div>
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">相关小区：</div>
	    			<div style="width: 65%;float:left;"><input id="correlativeCellQuery" class="easyui-textbox" style="width:150px;"/></div>
	    		</div>
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">测试业务：</div>
	    			<div style="width: 65%;float:left;">
		    			<select id="testServiceQuery" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
					        <option value="">&nbsp;</option>
					        <option value="绕点">绕点</option>
					        <option value="FTP下载">FTP下载</option>
					        <option value="FTP上传">FTP上传</option>
					        <option value="ENDC成功率测试">ENDC成功率测试</option>
					        <option value="PING#32">ping小包测试</option>
					        <option value="PING#1500">ping大包测试</option>
					    	<option value="绕点_下载">绕点_下载</option>
				        	<option value="绕点_上传">绕点_上传</option>
				        	<option value="volte测试">volte测试</option>
				        	<option value="CSFB测试">CSFB测试</option>
					    </select>
					</div>
	    		</div>
	    		<div style="width: 25%;float:left;">
	    			<div style="width: 35%;float:left;text-align:right;">无线情况：</div>
	    			<div style="width: 65%;float:left;">
		    			<select id="wirelessStatusQuery" class="easyui-combobox" name="dept" style="width:150px;" data-options="editable:false">
					        <option value="">&nbsp;</option>
					       	<option value="J">极好点</option>
					        <option value="H">好点</option>
					        <option value="Z">中点</option> 
					        <option value="C">差点</option>
					        <option value="未确定">未确定</option>
					    </select>
					</div>
	    		</div>
			<shiroextend:hasAnyPermissions name="stationVeri:query">
				<a href="#" class="easyui-linkbutton" style="width:80px;" onclick="pageQuery();" iconCls="icon-search" plain='true'>查询</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="stationVeri:upload">
				<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-download',plain:true" onclick="downloadLog();">下载</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="stationVeri:del">
				<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-remove',plain:true" onclick="delTestPlan();">删除</a>
			</shiroextend:hasAnyPermissions>
		</div>
	</div>
  </body>
</html>
