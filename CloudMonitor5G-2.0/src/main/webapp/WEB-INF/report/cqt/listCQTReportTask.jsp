<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  <head>
    <title>CQT统计任务</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
		<style type="text/css">
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 255px;
   		}
   		.inputDivShow input{
   			width:140px;
   		}
   		.inputDivShow select{
   			width:140px;
   		}
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
		$(function(){
			initTable();
		});
		function initTable(){
			$("#mainTable").datagrid({
				// 表头
				columns:[[
					{field:'id',width:20,align:'center',checkbox:true},
					{field:'createrName',width:60,align:'center',title:'创建人',formatter:showTooltip},
					{field:'name',width:80,align:'center',title:'任务名称',formatter:showTooltip},
					{field:'terminalGroup',width:20,align:'center',title:'域',formatter:showTooltip},
					{field:'creatDate',width:80,align:'center',title:'创建时间',formatter:showTooltip},
					{field:'nam',width:80,align:'center',title:'查看详情',
						formatter:function(value,row,index){
								return '<a href="#" title="查看详情" onclick="seeInfo('+row.id+');" >查看详情</a>';
						}
					},
					{field:'na',width:40,align:'center',title:'移动',
						formatter:function(value,row,index){
							return '<a href="#" title="下载" onclick="alertAttention();" >移动</a>';
						}
					}, 
					{field:'n',width:40,align:'center',title:'电信',
						formatter:function(value,row,index){
							return '<a href="#" title="下载" onclick="alertAttention();" >电信</a>';
						}
					}, 
					{field:'am',width:40,align:'center',title:'联通',
						formatter:function(value,row,index){
							return '<a href="#" title="下载" onclick="alertAttention();" >联通</a>';
						}
					}
				]],
				url:'${pageContext.request.contextPath}/cqtReport/doPageListJson.action',
				title:'统计任务列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				pagination:true,
				pageSize:20,
				loadMsg:0,
				pageList:[10,20,50,100,200,500,1000],
				scrollbarSize:0,
				checkOnSelect:false,
				toolbar:'#tb'
				
			});
			
		}
		/*查看详情*/
		function seeInfo(id){
			goToPage("${pageContext.request.contextPath}/cqtReport/seeInfo.action?idLong="+id+"");
		}
		/* 跳转到添加任务页面 */
		function add(){
			goToPage('${pageContext.request.contextPath}/cqtReport/goAdd.action');
		}
		/* 删除任务*/
		function delReport(){
			var row = $('#mainTable').datagrid('getChecked');
			
			if (row.length!=0) {
				$.messager.confirm("系统提示", "确定要删除选中任务？", function(r) {
					if (r) {
						var idss=new Array();
						for(i=0;i<row.length;i++){
							idss.push(row[i].id);
						}
						var idsString = idss.join(",");
						$.post('${pageContext.request.contextPath}/cqtReport/delReport.action', {ids : idsString}, 
							function(result) {
								if (result.errorMsg) {
									$.messager.alert("系统提示", result.errorMsg,'error');
								} else {
									$.messager.alert("系统提示", "已成功删除选中任务记录!");
									$("#mainTable").datagrid("reload");
								}
							},
						'json');
					}
				});
			}else{
				$.messager.alert("系统提示", "请选择某个任务!");
			}
		}
		/* 多条件查询 */
		function query(){
			if($("#queryForm").form('validate')){
				$("#mainTable").datagrid('load',{
					createrName:$("#createrName").textbox('getValue'),
					name:$("#name").textbox('getValue'),
					beginDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue'),
				});
			}
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
			/* var nowDate=new Date();
			var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");		
			$("#endDate").datetimebox('setValue',dateString);
			nowDate.setDate(nowDate.getDate()-7);
			dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			$("#beginDate").datetimebox('setValue',dateString); */
			
		}
	</script>
  </head>
  
  <body  style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	
    	<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">
	 
	    		<!-- 数据列表 -->
		<div  data-options="region:'center',border:false" >
			<table id="mainTable" ></table>
		
	 </div>
	    	<div style="width:100%;height:15%;" data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
	    		//收束的时候显示title
	    		var title = $('#ly').layout('panel','north').panel('options').title;
	    		$('.layout-expand-north .panel-title').html(title);
	    	}" style="height:160px;overflow-y:auto;">
    			<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
			    	<div class="inputDivShow">创建人
			    		<input id="createrName" name="createrName"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
			    	</div>
			    	<div class="inputDivShow">任务名称
			    		<input id="name" name="name"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
			    	</div>
			    	<div class="inputDivShow">创建起始时间
			    		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:false,editable:false" />
			    	</div>
			    	<div class="inputDivShow">创建截至时间
			    		<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="required:false,editable:false" />
			    	</div>

			    	<table width="100%">
			    		<tr>
				    		<td width="50%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a></td>
				    		<td width="50%" align="left"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a></td>
			    		</tr>
			    	</table>
		    	</form>
   			</div>
    		<div id="tb">
			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-add',plain:true" onclick="add();">新增</a>
			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-remove',plain:true" onclick="delReport();">删除</a>
	</div>
    	</div>
    	
  </body>
</html>
