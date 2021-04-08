<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>设备管理----批量导入界面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<style type="text/css">
		.inputDivShow{ 
			display: inline-block;*
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 300px;
   		}
   		.inputDivShow input{
   			width:200px;
   		}
   		.inputDivShow select{
   			width:200px;
   		}
		.filebox-label{
			background:url('../../themes/default/images/blank.gif');
		}
	</style>
	<!--引用百度地图API-->
<!--     <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=liKpDfLP41rNnZmM1D33WljN"></script> -->
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
	/* 上传文件 */
	function saveImport(){
		$("#saveForm").form('submit',{
			url:"${pageContext.request.contextPath}/terminal/importTerminal",
			success:function(result){
				var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("系统提示", resultData.errorMsg,"error");
				} else {
					if(null!=resultData.why){
						parent.parent.messagerAlert("导入成功", "<strong>文件导入<br><共"+resultData.totalRowNum+"条,失败"+resultData.failRowNum+"条>"+"失败原因："+resultData.why,"info");
					}else{ 
						parent.parent.messagerAlert("导入成功", "<strong>文件导入<br><共"+resultData.totalRowNum+"条,失败"+resultData.failRowNum+"条>","info");
					}
					$("#saveForm").form('reset');
				}
				$.messager.progress('close');
				return;
			},
			onSubmit:function(param){
				var validateFlag = $(this).form('validate');
				if(validateFlag){
					param.cellType = 0;
					$.messager.progress({
						text:"解析中",
						interval:200
					});
				}
				return validateFlag;
			}
		});
	}
	/* 下载模板 */
	function downloadDataToExcel(){
		goToPage('${pageContext.request.contextPath}/terminal/downloadTemp.action');
	}
	
	
	</script>
  </head>
  <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<form method="post" style="margin-bottom: 5px;" id="saveForm" enctype="multipart/form-data">
	    	<div class="inputDivShow">文件:
	    		<input class="easyui-filebox" name="importFile" validType="fileType['xls','xlsx']" data-options="buttonText:'浏览',required:true,invalidMessage:'文件类型必须为.xls或.xlsx'" />
	    	</div>
	    	 <a id="saveButton" class="easyui-linkbutton" style="width:100px;" onclick="goBack();" >返回</a>&nbsp;&nbsp;&nbsp;&nbsp;
		    <a class="easyui-linkbutton" style="width:80px" onclick="saveImport();" data-options="iconCls:'icon-upload'">上传</a>
		  
		    <a class="easyui-linkbutton" style="width:120px" onclick="downloadDataToExcel();" data-options="iconCls:'icon-download'">下载模板</a>
    	</form>
    	<div style="width:100%;border-bottom:1px solid #95b8e7;"></div>
  </body>
</html>