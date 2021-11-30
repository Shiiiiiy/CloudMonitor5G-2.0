<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<head>
	<title>地铁线路页面</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
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
	<script type="text/javascript">
		// field用于匹配远程json属性，width宽度,align居左右中对齐
		var railwayInfoColumns=[[
			{field:'id',checkbox:true}, // 一个对象代表一列 <th>
			{field:'city',width:150,align:'center',title:'城市'},
			{field:'lineNo',width:75,align:'center',title:'线路号'},
			{field:'updateTime',width:90,align:'center',title:'更新时间'},
			{field:'lineXml',width:45,align:'center',title:'路线文件'}
		]];

		$(function(){
			//初始化开始时间和结束时间
			// var nowDate=new Date();
			// var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			// $("#endDate").datetimebox('setValue',dateString);
			// nowDate.setDate(nowDate.getDate()-7);
			// dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			// $("#beginDate").datetimebox('setValue',dateString);

			initTable({
				startDate:$("#beginDate").datetimebox('getValue'),
				endDate:$("#endDate").datetimebox('getValue'),
				city:$("#city").textbox('getValue'),
				lineNo:$("#lineNo").textbox('getValue')
			});
		});

		function initTable(params){
			$("#subwaLineTable").datagrid({
				// 表头
				columns:railwayInfoColumns,
				url:'${pageContext.request.contextPath}/subwayLine/doPageListJson.action',
				title:'文件列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				pagination:true,
				pageSize:20,
				pageList:[10,20,50,100,200,500,1000],
				scrollbarSize:0,
				queryParams:params,
				onClickRow: function(rowIndex, rowData){
					//加载完毕后获取所有的checkbox遍历
					// $("input[type='checkbox']").each(function(index, el){
					// 	//如果当前的复选框不可选，则不让其选中
					// 	if (el.disabled == true) {
					// 		$("#subwaLineTable").datagrid('unselectRow', index - 1);
					// 	}
					// });
				}
			});

		}
		/* 多条件查询 */
		function query(){
			if($("#queryForm").form('validate')){
				$("#subwaLineTable").datagrid('load',{
					startDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue'),
					city:$("#city").textbox('getValue'),
					lineNo:$("#lineNo").textbox('getValue')
				});
			}
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
			// var nowDate=new Date();
			// var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			// $("#endDate").datetimebox('setValue',dateString);
			// nowDate.setDate(nowDate.getDate()-7);
			// dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			// $("#beginDate").datetimebox('setValue',dateString);
		}

		function deleteFile(){
			var rows = $("#subwaLineTable").datagrid('getSelections');//获取表格中用户选中 所有数据
			if(rows.length==0){// 操作前至少选中一条
				$.messager.alert("提示","请选择某些文件!",'warning');
				return ;
			}
			$.messager.confirm("系统提示", "您确定要删除选中的文件吗?", function(r) {
				if(r){
					var idArray = new Array();
					for(var i=0; i<rows.length;i++){
						idArray.push(rows[i].id);
					}
					// 将选中多条记录id 用","拼接为一个字符串
					var ids = idArray.join(",");
					// 以ajax方式，发送到服务器，完成操作
					$.post("${pageContext.request.contextPath}/subwayLine/deleteXmlFile",{"trainXmlIds":ids},
							function(result){
								if (result.errorMsg) {
									$.messager.alert("系统提示", result.errorMsg,'error');
								} else {
									$.messager.alert("系统提示", "删除成功",'success');
								}
								$("#subwaLineTable").datagrid("reload");
							}
							,"json");
				}
			});

		}

		/*下载操作*/
		function downloadFile(){
			var ids = '';
			var checked = $("#subwaLineTable").datagrid('getSelections');
			if(checked.length < 1){// 操作前至少选中一条
				$.messager.alert("提示","请选择文件!",'warning');
				return ;
			}
			for(var i = 0; i < checked.length;i++){
				ids = ids + checked[i].id;
				if(i != checked.length-1){
					ids = ids + ',';
				}
			}
			window.location= "${pageContext.request.contextPath}/subwayLine/downloadTrainLog.action?trainXmlIds="+ids;
		}



		function importLineFile(){
			goToPage('${pageContext.request.contextPath}/subwayLine/goImport.action');
		}

		function addXml(){
			goToPage('${pageContext.request.contextPath}/subwayLine/goAddSubwayXmlPage.action');
		}




	</script>
</head>

<body  style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">

<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">

	<div  data-options="region:'center',border:false" >
		<table id="subwaLineTable"> </table>
	</div>

	<div data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
	    		//收束的时候显示title
	    		var title = $('#ly').layout('panel','north').panel('options').title;
	    		$('.layout-expand-north .panel-title').html(title);
	    	}" style="height:140px;overflow-y:auto;">
		<!-- <div style="width:100%;height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;"><div class="panel-title">工程参数导入</div></div> -->
		<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
			<div class="inputDivShow">开始时间
				<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="editable:false" />
			</div>
			<div class="inputDivShow">结束时间
				<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="editable:false" />
			</div>
			<div class="inputDivShow">城市
				<input id="city" name="city"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
				</select>
			</div>
			<div class="inputDivShow">线路名
				<input id="lineNo" name="lineNo"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
				</select>
			</div>
			<table width="100%">
				<tr>
					<td width="100%" align="center">
						<a class="easyui-linkbutton" onclick="query();" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-search'" >查找</a>
						<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-reload'" >重置</a>
							<a class="easyui-linkbutton" style="width:80px;margin-right: 5px;"  onclick="downloadFile();" data-options="iconCls:'icon-download'">下载</a>
						<shiroextend:hasAnyPermissions name="subwayLine:delete">
							<a class="easyui-linkbutton" onclick="deleteFile();" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-cancel'" >删除</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="subwayLine:import">
							<a class="easyui-linkbutton" onclick="importLineFile();" style="width: 80px;margin-right: 5px;">导入</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="subwayLine:import">
							<a class="easyui-linkbutton" onclick="addXml();" style="width: 80px;margin-right: 5px;">新增</a>
						</shiroextend:hasAnyPermissions>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>
