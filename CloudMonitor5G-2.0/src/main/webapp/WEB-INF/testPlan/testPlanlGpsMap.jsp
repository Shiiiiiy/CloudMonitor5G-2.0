<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>测试区域输入</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js"></script>
	<script type="text/javascript">
		$(function(){
	        init();
			var t2 = window.setInterval(function(){
        		if(mapIframe.window.map){
        			mapIframe.window.hideDivByDivId('EventControldis');
		        	window.clearInterval(t2);
        		}
        	},1000);
        });
	
		
		function init(){
			$('#mapWindowId').window('open');
		}
		
		/* 保存矩形框经纬度 */
		function saveGeometry() {
			$("#testSuitForm").form('submit',{
				onSubmit : function(param) {
					return $(this).form('validate');
				},
				success : function(result) {
					var resultData =$.parseJSON(result);
					if (resultData.errorMsg) {
						$.messager.alert("失败", resultData.errorMsg,"error");
						return;
					} else {
						$("#addTestScheme").panel('close');
						$("#testSchemeList").panel('resize');
						$("#testSchemeList").panel('open');
						$("#testSchemeListTable").datagrid('reload');
						//$("#ly").layout('expand','north');
					}
				}
			});
		}
		
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
   		<!--地图界面 -->
		<div style="width:100%;height:95%;border:2px;">
		  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default3.html?toolbarType=103"
	        scrolling="auto" frameborder="0"
	        style="width:100%;height:100%;border:2px;margin: 5px 5px 5px 5px;"></iframe>
	    </div>
	   	<div data-options="region:'south',border:false" style="margin-top:4%;">
		   	<table width="100%" style="border-top:1px solid #95b8e7;">
		   		<tr height="35px">
		    		<td width="50%;" align="center">
		    			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-ok',plain:true" onclick="saveGeometry();">保存</a>
						<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-undo',plain:true" onclick="delTestPlan();">返回</a>
		    		</td>
		    		<%-- <td width="50%;">
		    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">
		    				<a class="easyui-linkbutton" iconCls="icon-reload" style="width:100px;" onclick="resetForm();" >重置</a>
		    			</shiroextend:hasAnyPermissions>
		    		</td> --%>
		   		</tr>
		   	</table>
	  	</div>
  </body>
</html>
