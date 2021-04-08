<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>超远覆盖参数设置</title>

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
			var maxDistanceForSpfarCoverageLte = $("#maxDistanceForSpfarCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "maxDistanceForSpfarCoverageLte" + ",";
			fieldValue = fieldValue + maxDistanceForSpfarCoverageLte + ",";
			
			var minDistanceForSpfarCoverageLte = $("#minDistanceForSpfarCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "minDistanceForSpfarCoverageLte" + ",";
			fieldValue = fieldValue + minDistanceForSpfarCoverageLte + ",";
			
			var rsrpThresholdForSpfarCoverageLte = $("#rsrpThresholdForSpfarCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "rsrpThresholdForSpfarCoverageLte" + ",";
			fieldValue = fieldValue + rsrpThresholdForSpfarCoverageLte + ",";
			
			var samplingPointRatioForSpfarCoverageLte = $("#samplingPointRatioForSpfarCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "samplingPointRatioForSpfarCoverageLte" + ",";
			fieldValue = fieldValue + samplingPointRatioForSpfarCoverageLte + ",";
			
			var cellSamplingPointNumberForSpfarCoverageLte = $("#cellSamplingPointNumberForSpfarCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointNumberForSpfarCoverageLte" + ",";
			fieldValue = fieldValue + cellSamplingPointNumberForSpfarCoverageLte + ",";
			
			var nCellMaxDistanceForSpfarCoverageNR = $("#nCellMaxDistanceForSpfarCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellMaxDistanceForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + nCellMaxDistanceForSpfarCoverageNR + ",";
			
			var nCellMinDistanceForSpfarCoverageNR = $("#nCellMinDistanceForSpfarCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellMinDistanceForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + nCellMinDistanceForSpfarCoverageNR + ",";
			
			var nCellminBeamRsrpForSpfarCoverageNR = $("#nCellminBeamRsrpForSpfarCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellminBeamRsrpForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + nCellminBeamRsrpForSpfarCoverageNR + ",";
			
			var cellSamplingPointRatioForSpfarCoverageNR = $("#cellSamplingPointRatioForSpfarCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointRatioForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + cellSamplingPointRatioForSpfarCoverageNR + ",";
			
			var nCellSamplingPointNumberForSpfarCoverageNR = $("#nCellSamplingPointNumberForSpfarCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellSamplingPointNumberForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + nCellSamplingPointNumberForSpfarCoverageNR + ",";
			
			var baseSsbOrCsiAnalysisForSpfarCoverageNR = $('input[name="baseSsbOrCsiAnalysisForSpfarCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "baseSsbOrCsiAnalysisForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + baseSsbOrCsiAnalysisForSpfarCoverageNR + ",";
			
			var andOrRelationshipForSpfarCoverageNR = $('input[name="andOrRelationshipForSpfarCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "andOrRelationshipForSpfarCoverageNR" + ",";
			fieldValue = fieldValue + andOrRelationshipForSpfarCoverageNR + ",";
			
			var flag = $("#whetherSameConsiderLteCoverForSpfarCoverageNR").is(":checked");
			if(flag){
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForSpfarCoverageNR";
			fieldValue = fieldValue + "1";
			}else{
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForSpfarCoverageNR";
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
					$("#maxDistanceForSpfarCoverageLte").textbox('setValue',"3000");
					$("#minDistanceForSpfarCoverageLte").textbox('setValue',"2000");
					$("#rsrpThresholdForSpfarCoverageLte").textbox('setValue',"-85");
					$("#samplingPointRatioForSpfarCoverageLte").textbox('setValue',"20");
					$("#cellSamplingPointNumberForSpfarCoverageLte").textbox('setValue',"1000");
					
					$("#nCellMaxDistanceForSpfarCoverageNR").textbox('setValue',"3000");
					$("#nCellMinDistanceForSpfarCoverageNR").textbox('setValue',"2000");
					$("#nCellminBeamRsrpForSpfarCoverageNR").textbox('setValue',"-85");
					$("#cellSamplingPointRatioForSpfarCoverageNR").textbox('setValue',"20");
					$("#nCellSamplingPointNumberForSpfarCoverageNR").textbox('setValue',"1000");
					
					$("#baseSsbOrCsiAnalysisForSpfarCoverageNRssb").prop("checked", true); 
					$("#andOrRelationshipForSpfarCoverageNRand").prop("checked", true); 
					$("#whetherSameConsiderLteCoverForSpfarCoverageNR").prop("checked", true); 
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
						<div class="panel-title">LTE超远覆盖定义</div>
					</div>
				</div>
	   			<div class="inputDivShow">
		    		<input id="maxDistanceForSpfarCoverageLte" name="maxDistanceForSpfarCoverageLte" style="width:120px;" value="${EmbbSpfarCoverAnasis.maxDistanceForSpfarCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:100,max:10000 " />
		    		米>采样点距离LTE锚点的距离>
		    		<input id="minDistanceForSpfarCoverageLte" name="minDistanceForSpfarCoverageLte" style="width:120px;" value="${EmbbSpfarCoverAnasis.minDistanceForSpfarCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:100,max:10000 " />
		    		米
		    	</div>
		    	<div class="inputDivShow">且&nbsp;&nbsp;LTE小区电平>=
		    		<input id="rsrpThresholdForSpfarCoverageLte" name="rsrpThresholdForSpfarCoverageLte" style="width:120px;" value="${EmbbSpfarCoverAnasis.rsrpThresholdForSpfarCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow">LTE超远覆盖采样点占比>=
			    		<input id="samplingPointRatioForSpfarCoverageLte" name="samplingPointRatioForSpfarCoverageLte" style="width:120px;"  value="${EmbbSpfarCoverAnasis.samplingPointRatioForSpfarCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="cellSamplingPointNumberForSpfarCoverageLte" name="cellSamplingPointNumberForSpfarCoverageLte" style="width:120px;"  value="${EmbbSpfarCoverAnasis.cellSamplingPointNumberForSpfarCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为超远覆盖LTE锚点小区
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
						<div class="panel-title">5G超远覆盖定义</div>
					</div>
				</div>
				<div class="inputDivShow">
					<input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForSpfarCoverageNRcsi" name="baseSsbOrCsiAnalysisForSpfarCoverageNR" <c:if test="${EmbbSpfarCoverAnasis.baseSsbOrCsiAnalysisForSpfarCoverageNR == 'csi'}">checked="checked"</c:if> value="csi">基于CSI-RSRP进行分析
		            <input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForSpfarCoverageNRssb" name="baseSsbOrCsiAnalysisForSpfarCoverageNR" <c:if test="${EmbbSpfarCoverAnasis.baseSsbOrCsiAnalysisForSpfarCoverageNR == 'ssb'}">checked="checked"</c:if> value="ssb">基于SSB-RSRP进行分析
                </div>
	   			<div class="inputDivShow">
		    		<input id="nCellMaxDistanceForSpfarCoverageNR" name="nCellMaxDistanceForSpfarCoverageNR" style="width:120px;"  value="${EmbbSpfarCoverAnasis.nCellMaxDistanceForSpfarCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:100,max:10000 "/>
		    		米>采样点距离NR辅小区的距离>
		    		<input id="nCellMinDistanceForSpfarCoverageNR" name="nCellMinDistanceForSpfarCoverageNR" style="width:120px;" value="${EmbbSpfarCoverAnasis.nCellMinDistanceForSpfarCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:100,max:10000 " />
		    		米
		    	</div>
		    	<div class="inputDivShow">且&nbsp;&nbsp;NR辅小区最小的Beam RSRP>=
		    		<input id="nCellminBeamRsrpForSpfarCoverageNR" name="nCellminBeamRsrpForSpfarCoverageNR" style="width:120px;" value="${EmbbSpfarCoverAnasis.nCellminBeamRsrpForSpfarCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    	<div class="inputDivShow">
               		<input type="checkbox" style="width:10px" id="whetherSameConsiderLteCoverForSpfarCoverageNR"   name="whetherSameConsiderLteCoverForSpfarCoverageNR"  <c:if test="${EmbbSpfarCoverAnasis.whetherSameConsiderLteCoverForSpfarCoverageNR == 1}">checked="checked"</c:if> value="1" >是否同时考虑LTE覆盖情况
                	<input type="radio" style="width:10px" id="andOrRelationshipForSpfarCoverageNRand" name="andOrRelationshipForSpfarCoverageNR" <c:if test="${EmbbSpfarCoverAnasis.andOrRelationshipForSpfarCoverageNR == 'And'}">checked="checked"</c:if> value="And">且
		            <input type="radio" style="width:10px" id="andOrRelationshipForSpfarCoverageNRor" name="andOrRelationshipForSpfarCoverageNR" <c:if test="${EmbbSpfarCoverAnasis.andOrRelationshipForSpfarCoverageNR == 'Or'}">checked="checked"</c:if> value="Or">或
                </div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow" >NR超远覆盖采样点占比>=
			    		<input id="cellSamplingPointRatioForSpfarCoverageNR"  name="cellSamplingPointRatioForSpfarCoverageNR" style="width:120px;"  value="${EmbbSpfarCoverAnasis.cellSamplingPointRatioForSpfarCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="nCellSamplingPointNumberForSpfarCoverageNR" name="nCellSamplingPointNumberForSpfarCoverageNR" style="width:120px;"  value="${EmbbSpfarCoverAnasis.nCellSamplingPointNumberForSpfarCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为超远覆盖NR辅小区
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
