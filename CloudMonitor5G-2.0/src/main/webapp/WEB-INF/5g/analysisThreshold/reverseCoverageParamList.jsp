<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>反向覆盖参数设置</title>

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
   		.incorrect:before {
		    content: '\2716';
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
			var angleThresholdForReverseCoverageLte = $("#angleThresholdForReverseCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "angleThresholdForReverseCoverageLte" + ",";
			fieldValue = fieldValue + angleThresholdForReverseCoverageLte + ",";
			
			var distanceThresholdForReverseCoverageLte = $("#distanceThresholdForReverseCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "distanceThresholdForReverseCoverageLte" + ",";
			fieldValue = fieldValue + distanceThresholdForReverseCoverageLte + ",";
			
			var rsrpThresholdForReverseCoverageLte = $("#rsrpThresholdForReverseCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "rsrpThresholdForReverseCoverageLte" + ",";
			fieldValue = fieldValue + rsrpThresholdForReverseCoverageLte + ",";
			
			var samplingPointRatioForReverseCoverageLte = $("#samplingPointRatioForReverseCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "samplingPointRatioForReverseCoverageLte" + ",";
			fieldValue = fieldValue + samplingPointRatioForReverseCoverageLte + ",";
			
			var cellSamplingPointNumberForReverseCoverageLte = $("#cellSamplingPointNumberForReverseCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointNumberForReverseCoverageLte" + ",";
			fieldValue = fieldValue + cellSamplingPointNumberForReverseCoverageLte + ",";
			
			var nCelAngleForReverseCoverageNR = $("#nCelAngleForReverseCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCelAngleForReverseCoverageNR" + ",";
			fieldValue = fieldValue + nCelAngleForReverseCoverageNR + ",";
			
			var nCellDistanceForReverseCoverageNR = $("#nCellDistanceForReverseCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellDistanceForReverseCoverageNR" + ",";
			fieldValue = fieldValue + nCellDistanceForReverseCoverageNR + ",";
			
			var nCellminBeamRsrpForReverseCoverageNR = $("#nCellminBeamRsrpForReverseCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellminBeamRsrpForReverseCoverageNR" + ",";
			fieldValue = fieldValue + nCellminBeamRsrpForReverseCoverageNR + ",";
			
			var cellSamplingPointRatioForReverseCoverageNR = $("#cellSamplingPointRatioForReverseCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointRatioForReverseCoverageNR" + ",";
			fieldValue = fieldValue + cellSamplingPointRatioForReverseCoverageNR + ",";
			
			var nCellSamplingPointNumberForReverseCoverageNR = $("#nCellSamplingPointNumberForReverseCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellSamplingPointNumberForReverseCoverageNR" + ",";
			fieldValue = fieldValue + nCellSamplingPointNumberForReverseCoverageNR + ",";
			
			var baseSsbOrCsiAnalysisForReverseCoverageNR = $('input[name="baseSsbOrCsiAnalysisForReverseCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "baseSsbOrCsiAnalysisForReverseCoverageNR" + ",";
			fieldValue = fieldValue + baseSsbOrCsiAnalysisForReverseCoverageNR + ",";
			
			var andOrRelationshipForReverseCoverageNR = $('input[name="andOrRelationshipForReverseCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "andOrRelationshipForReverseCoverageNR" + ",";
			fieldValue = fieldValue + andOrRelationshipForReverseCoverageNR + ",";
			
			var flag = $("#whetherSameConsiderLteCoverForReverseCoverageNR").is(":checked");
			if(flag){
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForReverseCoverageNR";
			fieldValue = fieldValue + "1";
			}else{
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForReverseCoverageNR";
			fieldValue = fieldValue + "0";
			}
			
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
					$("#angleThresholdForReverseCoverageLte").textbox('setValue',"150");
					$("#distanceThresholdForReverseCoverageLte").textbox('setValue',"150");
					$("#rsrpThresholdForReverseCoverageLte").textbox('setValue',"-85");
					$("#samplingPointRatioForReverseCoverageLte").textbox('setValue',"20");
					$("#cellSamplingPointNumberForReverseCoverageLte").textbox('setValue',"1000");
					
					$("#nCelAngleForReverseCoverageNR").textbox('setValue',"150");
					$("#nCellDistanceForReverseCoverageNR").textbox('setValue',"150");
					$("#nCellminBeamRsrpForReverseCoverageNR").textbox('setValue',"-85");
					$("#cellSamplingPointRatioForReverseCoverageNR").textbox('setValue',"20");
					$("#nCellSamplingPointNumberForReverseCoverageNR").textbox('setValue',"1000");
					
					$("#baseSsbOrCsiAnalysisForReverseCoverageNRssb").prop("checked", true); 
					$("#andOrRelationshipForReverseCoverageNRand").prop("checked", true); 
					$("#whetherSameConsiderLteCoverForReverseCoverageNR").prop("checked", true); 
				}
			});
		}
		
		
	</script>
  </head>
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div style="width:49%;height:90%;float:left; margin-right: 10px;border:1px solid #95b8e7;">
	  	<div data-options="region:'west',border:false,split:true">
	  		<div style="width:100%;height:50%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
					<div class="panel-heading">
						<div class="panel-title">LTE反向覆盖定义</div>
					</div>
				</div>
	   			<div class="inputDivShow">采样点和LTE锚点夹角>=
		    		<input id="angleThresholdForReverseCoverageLte" name="angleThresholdForReverseCoverageLte" style="width:120px;" value="${EmbbReverseCoverAnasis.angleThresholdForReverseCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:180 " />
		    		度，采样点到LTE锚点小区的距离>=
		    		<input id="distanceThresholdForReverseCoverageLte" name="distanceThresholdForReverseCoverageLte" style="width:120px;" value="${EmbbReverseCoverAnasis.distanceThresholdForReverseCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:100,max:10000 " />
		    		米
		    	</div>
		    	<div class="inputDivShow">且&nbsp;&nbsp;LTE小区电平>=
		    		<input id="rsrpThresholdForReverseCoverageLte" name="rsrpThresholdForReverseCoverageLte" style="width:120px;" value="${EmbbReverseCoverAnasis.rsrpThresholdForReverseCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow">LTE反向覆盖采样点占比>=
			    		<input id="samplingPointRatioForReverseCoverageLte" name="samplingPointRatioForReverseCoverageLte" style="width:120px;"  value="${EmbbReverseCoverAnasis.samplingPointRatioForReverseCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="cellSamplingPointNumberForReverseCoverageLte" name="cellSamplingPointNumberForReverseCoverageLte" style="width:120px;"  value="${EmbbReverseCoverAnasis.cellSamplingPointNumberForReverseCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为反向覆盖LTE锚点小区
			    	</div>
			    </div>
			 </div>
		</div>
	</div>
	<div style="width:50%;height:90%;float:left;border:1px solid #95b8e7;">
	  	<div data-options="region:'east',border:false,split:true">
	  		<div style="width:100%;height:50%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
					<div class="panel-heading">
						<div class="panel-title">5G反向覆盖定义</div>
					</div>
				</div>
				<div class="inputDivShow">
					<input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForReverseCoverageNRcsi" name="baseSsbOrCsiAnalysisForReverseCoverageNR" <c:if test="${EmbbReverseCoverAnasis.baseSsbOrCsiAnalysisForReverseCoverageNR == 'csi'}">checked="checked"</c:if> value="csi">基于CSI-RSRP进行分析
		            <input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForReverseCoverageNRssb" name="baseSsbOrCsiAnalysisForReverseCoverageNR" <c:if test="${EmbbReverseCoverAnasis.baseSsbOrCsiAnalysisForReverseCoverageNR == 'ssb'}">checked="checked"</c:if> value="ssb">基于SSB-RSRP进行分析
                </div>
	   			<div class="inputDivShow">采样点和NR辅小区夹角>=
		    		<input id="nCelAngleForReverseCoverageNR" name="nCelAngleForReverseCoverageNR" style="width:120px;"  value="${EmbbReverseCoverAnasis.nCelAngleForReverseCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100 "/>
		    		度，采样点到NR辅小区距离>=
		    		<input id="nCellDistanceForReverseCoverageNR" name="nCellDistanceForReverseCoverageNR" style="width:120px;" value="${EmbbReverseCoverAnasis.nCellDistanceForReverseCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:100,max:100000 " />
		    		米
		    	</div>
		    	<div class="inputDivShow">且&nbsp;&nbsp;NR辅小区最小的Beam RSRP>=
		    		<input id="nCellminBeamRsrpForReverseCoverageNR" name="nCellminBeamRsrpForReverseCoverageNR" style="width:120px;" value="${EmbbReverseCoverAnasis.nCellminBeamRsrpForReverseCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    	<div class="inputDivShow">
               		<input type="checkbox" style="width:10px" id="whetherSameConsiderLteCoverForReverseCoverageNR"   name="whetherSameConsiderLteCoverForReverseCoverageNR"  <c:if test="${EmbbReverseCoverAnasis.whetherSameConsiderLteCoverForReverseCoverageNR == 1}">checked="checked"</c:if> value="1" >是否同时考虑LTE覆盖情况
                	<input type="radio" style="width:10px" id="andOrRelationshipForReverseCoverageNRand" name="andOrRelationshipForReverseCoverageNR" <c:if test="${EmbbReverseCoverAnasis.andOrRelationshipForReverseCoverageNR == 'And'}">checked="checked"</c:if> value="And">且
		            <input type="radio" style="width:10px" id="andOrRelationshipForReverseCoverageNRor" name="andOrRelationshipForReverseCoverageNR" <c:if test="${EmbbReverseCoverAnasis.andOrRelationshipForReverseCoverageNR == 'Or'}">checked="checked"</c:if> value="Or">或
                </div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow" >NR反向覆盖采样点占比>=
			    		<input id="cellSamplingPointRatioForReverseCoverageNR"  name="cellSamplingPointRatioForReverseCoverageNR" style="width:120px;"  value="${EmbbReverseCoverAnasis.cellSamplingPointRatioForReverseCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="nCellSamplingPointNumberForReverseCoverageNR" name="nCellSamplingPointNumberForReverseCoverageNR" style="width:120px;"  value="${EmbbReverseCoverAnasis.nCellSamplingPointNumberForReverseCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为反向覆盖NR辅小区
			    	</div>
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
