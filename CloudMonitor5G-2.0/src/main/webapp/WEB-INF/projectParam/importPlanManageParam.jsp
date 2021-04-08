<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>导入工参</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<style>
		.filebox-label{
		background:url('../../themes/default/images/blank.gif');
		}
	</style> 
	
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {
		//必须是excel文件
		fileType : {
			validator : function(value, param) {
				var pos = value.lastIndexOf(".");
				var lastname = value.substring(pos+1, value.length); //此处文件后缀名也可用数组方式获得str.split(".")
				return lastname.toLowerCase() == param[0].toLowerCase()?true:(param.length>1?(lastname.toLowerCase() == param[1].toLowerCase()):false);
			},
			message : '文件类型不匹配'
		}
	});
	/* 菜单树只能选中city级别的node */
	function onbeforeselect(node){
		if(node.attributes.type=='City'){
			return true;
		}
		return false;
	}
	/* 上传文件 */
	function saveImport(){
		$("#saveForm").form('submit',{
			url:"${pageContext.request.contextPath}/projectParam/importPlanManageParam",
			success:function(result){
				var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("系统提示", resultData.errorMsg,"error");
				} else {
					var info ="<strong>"+resultData.operatorType+"</strong>规划工参文件导入<br><共"+resultData.totalRowNum+"条,失败"+resultData.failRowNum+"条>"
					if(resultData.failRowNum>0){
						
						var errorInfo = resultData.msg;
						if(errorInfo.length > 120){
							errorInfo = errorInfo.substring(0, 120)+"......";
						}
						info = info+"失败原因："+errorInfo;
					}
					parent.parent.messagerAlert("导入信息", info,"info");
					$("#saveForm").form('reset');
				}
				$.messager.progress('close');
				return;
			},
			onSubmit:function(param){
				var isValid = $(this).form('validate');
				if(isValid){
					var areaNodes = $('#areaTree').tree('getSelected');
					if(null!=areaNodes && 0!=areaNodes.length){
						param.cityId = areaNodes.attributes.refId;
						$.messager.progress({
							text:"解析中",
							interval:200
						});
						return true;
					}else{
						$.messager.alert("系统提示", "<font color='red'><strong>请选择某个市级域<strong></font>");
						return false;
					}
				}
				return $(this).form('validate');
			}
		});
	}
	/* 下载模板 */
	function downloadDataToExcel(operatorType){
		goToPage('${pageContext.request.contextPath}/projectParam/downloadTemp?operatorType='+operatorType);
		//$.post('${pageContext.request.contextPath}/projectParam/downloadTemp?operatorType='+operatorType);
	}
	</script>
  </head>
  
  <body style="width:100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow-x:hidden;overflow-y:auto;">
<!--    <h3 style="border-bottom: 1px solid #95B8E7;color: #3366CC;font-size: 16px;font-weight:800;height:32px;padding: 0 10px;">筛选参数设置</h3> -->
    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">规划工参导入</div></div>
    	<div  style="padding-top: 4px;">
	    	<a id="saveButton" class="easyui-linkbutton" style="width:100px;" onclick="goBack();" >返回</a>
	    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a class="easyui-linkbutton" style="width:100px;" onclick="saveImport();" >上传</a>
			<a class="easyui-linkbutton" style="width:100px;" onclick="downloadDataToExcel('${MODEL_5G}')" >5G规划工参模板</a>
			<a class="easyui-linkbutton" style="width:100px;" onclick="downloadDataToExcel('${MODEL_4G}')" >4G规划工参模板</a>
    	</div>
    	<form id="saveForm" method="post" enctype="multipart/form-data">
	    	<table>
	    		
	    		<tr style="height:200px;">
	    			<td width="100px"  align="right" valign="top">
						<span class="label" style="font-size:12px;line-height:26px;">所属域：</span>
					</td>
	    			<td width="300px" height="200px" colspan="3" align="left">
						<div class="easyui-panel" fit="true" style="overflow: auto;">
							<ul id="areaTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',lines:true,onBeforeSelect:onbeforeselect"></ul>
						</div>
					</td>
	    		</tr>
	    		<tr style="height:30px;">
	    			<td width="100px" align="right" rowspan="1">
		    			<span class="label" style="font-size:12px;line-height:26px;">工参类型：</span>
					</td>
					<td width="100px" align="left">
						<span class="label" style="font-size:12px;line-height:26px;">
							<input type="radio" checked name="operatorType" value="5G" />5G
						</span>
					</td>
					<td width="100px" align="left" >
						<span class="label" style="font-size:12px;line-height:26px;">
							<input type="radio" checked name="operatorType" value="4G" />4G
						</span>
					</td>
				</tr>
	    		<tr style="height:30px;">
	    			<td width="100px"  align="right" valign="top">
						<span class="label" style="font-size:12px;line-height:26px;">选择文件：</span>
					</td>
	    			<td width="300px" colspan="3" align="left">
						<input class="easyui-filebox" style="width:500px;" name="importFile" validType="fileType['xls','xlsx']" data-options="buttonText:'浏览',required:true,invalidMessage:'文件类型必须为.xls或.xlsx'" />
					</td>
	    		</tr>
	    		
	    	</table>
    	</form>
  </body>
</html>
