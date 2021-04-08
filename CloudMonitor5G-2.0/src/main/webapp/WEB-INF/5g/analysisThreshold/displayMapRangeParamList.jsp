<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>轨迹图阈值门限(参数设置)</title>

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
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 200px;
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
   		.inputDivShow input,.inputDivShow1 input{
   			width:100px;
   		}
   		.inputDivShow select.inputDivShow1 select{
   			width:100px;
   		}
	</style>
	<script type="text/javascript">
		var cityId = ${cityId};
		var colorMapType = '${colorMapType}';
	
		$(function(){
			//是否在页面加载完成的时候加载门限参数的初始值缓存到页面?
		});
		/* 保存修改 */
		function submitForm(){
			var mapParamSelOptions =  $('#mapParamSel').combobox('getData');
			var data = {};
			data['colorMapType'] = colorMapType;
			data['cityId'] = cityId;
			
			for ( var i = 0 ; i < mapParamSelOptions.length ; i++ ){
				var option = mapParamSelOptions[i].value;
				var optionIndexName = option+"Index";
				var optionColorName = option+"Color";
				var fieldValue = "";
				var fieldColor = "";
				var inputList = $("."+option+"_div");
				if(option != "pci"){
					$(inputList).each(function(i,item){
						var minValue = $(item).find("[name = 'minValue']").val();
						if(minValue==""){
							$.messager.alert("系统提示", "输入值不能为空",'error');
							return false;
						}
						var maxValue = $(item).find("[name = 'maxValue']").val();
						if(maxValue==""){
							$.messager.alert("系统提示", "输入值不能为空",'error');
							return false;
						}
						var range1 = $(item).find("[name = 'range1']").val();
						var range2 = $(item).find("[name = 'range2']").val();
						var color = $(item).find("[name = 'color']").val();
						
						fieldValue = fieldValue + range1 + minValue + "," + maxValue + range2 +"@";
						fieldColor = fieldColor + color + ",";
					})
					data['legendMap[\''+optionIndexName+'\']'] = fieldValue.substring(0, fieldValue.length-1);
					data['legendMap[\''+optionColorName+'\']'] = fieldColor.substring(0, fieldColor.length-1);
				}else{
					$(inputList).each(function(i,item){
						var color = $(item).find("[name = 'color']").val();
						fieldColor = fieldColor + color + ",";
					})
					data['legendMap[\''+optionColorName+'\']'] = fieldColor.substring(0, fieldColor.length-1);
				}
			}
			$.ajax({
				url:"${pageContext.request.contextPath}/mapTrailLegendParam/saveMapRangeParamThreshold.action",
				dataType:"json",
				type:"post",
				data: data,
				success:function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$.messager.alert('提示','修改成功','info');
					}
				}
			});
			
		}
		
		//参考的数据库表的默认值
		// rsrp默认值:[-∞,-110)@[-110,-105)@[-105,-100)@[-100,-95)@[-95,-85)@[-85,-75)@[-75,-45)
		// sinr默认值:[-∞,-3)@[-3,0)@[0,3)@[3,6)@[6,9)@[9,15)@[15,+∞)
		// macthrputdl默认值:[0,40)@[40,120)@[120,400)@[400,800)@[800,1200)@[1200,1600)@[1600,+∞)
		// macthrputul默认值:[0,10)@[10,20)@[20,30)@[30,35)@[35,40)@[40,200)@[200,+∞)
		
		// rsrp颜色默认值:#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050
		// sinr颜色默认值:#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050
		// macthrputdl颜色默认值:#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050
		// macthrputul颜色默认值:#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050
		// pci颜色默认值:#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050,#FFC000
		var defaultValue = {"rsrp":"[-∞,-110)@[-110,-105)@[-105,-100)@[-100,-95)@[-95,-85)@[-85,-75)@[-75,-45)",
								"sinr":"[-∞,-3)@[-3,0)@[0,3)@[3,6)@[6,9)@[9,15)@[15,+∞)",
								"macthrputdl":"[0,40)@[40,120)@[120,400)@[400,800)@[800,1200)@[1200,1600)@[1600,+∞)",
								"macthrputul":"[1,10)@[10,20)@[20,30)@[30,35)@[35,40)@[40,200)@[200,+∞)"};
		var defaultColor = {"rsrp":"#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050",
								"sinr":"#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050",
								"macthrputdl":"#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050",
								"macthrputul":"#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050",
								"pci":"#FF0000,#FABF8F,#FFFF00,#95B3D7,#548DD4,#92D050,#00B050,#FFC000"};
		 
		/* 重置初始值 */
		function resetForm(){
			document.location.reload();
		}
		
		
		
		function mapParamSel(record){
			var mapParamSelOptions =  $('#mapParamSel').combobox('getData');
			for ( var i = 0 ; i < mapParamSelOptions.length ; i++ ){
				if(record.value==mapParamSelOptions[i].value){
					$('#'+mapParamSelOptions[i].value).css('display','block');
				}else{
					$('#'+mapParamSelOptions[i].value).css('display','none');
				}
			}
		}
		
		var i=0; //保证，easyui的combobox的class的唯一性
		function addRow(){
			var id=0; //保证input的id按顺序排列
			var mapParamSelOption =  $('#mapParamSel').combobox('getValue');
			var inputList = $("."+mapParamSelOption+"_div");
			var valueArry = defaultValue[mapParamSelOption].split("@");
			if($(inputList).length>=valueArry.length){
				id = $(inputList).length;
			}
			if($(inputList).length<valueArry.length){
				id = $(inputList).length-1;
			}
			
			var inHtml = '<div class="'+mapParamSelOption+'_context '+mapParamSelOption+'_div">'+
				'<div class="inputDivShow2"> <strong>区间：</strong> </div>&nbsp;'+
				'<div class="inputDivShow1">'+
					'<select class="addCombobox'+i+'" name="range1" id="'+mapParamSelOption+id+'_range1" style="width:100px;" data-options="required:true,editable:false,disabled:true">'+
						'<option value="[" selected="selected">大于等于</option><option value="(" >大于</option>'+
					'</select>'+
				'</div>&nbsp;'+
				'<div class="inputDivShow1"><input name="minValue" id="'+mapParamSelOption+id+'_minValue" class="easyui-numberbox" data-options="required:true"  />&nbsp;';
			if(mapParamSelOption == "dlbler"){
				inHtml = inHtml +'<strong>%</strong></div>&nbsp;';
			}else if(mapParamSelOption == "beam"){
				inHtml = inHtml +'<strong></strong></div>&nbsp;';
			}else if(mapParamSelOption =='macthrputdl' || mapParamSelOption =='macthrputul'){
				inHtml = inHtml +'<strong>Mbps</strong></div>&nbsp;';
			}else{
				inHtml = inHtml +'<strong>dBm</strong></div>&nbsp;';
			}
			inHtml = inHtml +'<div class="inputDivShow2"> <strong>且</strong> </div>&nbsp;'+
				'<div class="inputDivShow1">'+
					'<select class="addCombobox'+i+'" name="range2" id="'+mapParamSelOption+id+'_range2" style="width:100px;" data-options="required:true,editable:false,disabled:true">'+
						'<option value="]">小于等于</option> <option value=")" selected="selected">小于</option>'+
					'</select>'+
				'</div>&nbsp;'+
				'<div class="inputDivShow1"><input name="maxValue" id="'+mapParamSelOption+id+'_maxValue" class="easyui-numberbox" data-options="required:true"  />&nbsp;';
			if(mapParamSelOption == "dlbler"){
				inHtml = inHtml +'<strong>%</strong></div>&nbsp;';
			}else if(mapParamSelOption == "beam"){
				inHtml = inHtml +'<strong></strong></div>&nbsp;';
			}else if(mapParamSelOption =='macthrputdl' || mapParamSelOption =='macthrputul'){
				inHtml = inHtml +'<strong>Mbps</strong></div>&nbsp;';
			}else{
				inHtml = inHtml +'<strong>dBm</strong></div>&nbsp;';
			}
			inHtml = inHtml +'<div class="inputDivShow2"> <strong>颜色：</strong> </div>&nbsp;'+
				'<div class="inputDivShow1"><input type="color" name="color" id="'+mapParamSelOption+id+'_color" value="#00b050"  style="border:none;padding:0;background-color:white;"/></div>'+
				'</div>';
			$('.'+mapParamSelOption+'_last').before(inHtml);
			$('.easyui-numberbox').numberbox();
			$('.addCombobox'+i).combobox();
			i++;
		}
		
		function removeRow(){
			var mapParamSelOption =  $('#mapParamSel').combobox('getValue');
			$('.'+mapParamSelOption+'_context:last').remove();
		}
	</script>
  </head>
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div data-options="region:'center',border:false">
		<form id="thresholdForm0" method="post">
	    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">地图参数</div></div>
	    	
    		<div class="inputDivShow1">
    			<select id="mapParamSel" class="easyui-combobox" name="dept" data-options="onSelect:mapParamSel,required:true,editable:false" style="width:100px;">
	    			<c:forEach items="${MapParam}" var="quality" varStatus="status">
					    <option value="${quality[0].nameEh}" <c:if test="${status.first}">selected="selected"</c:if>>${quality[0].nameCh}</option>
					</c:forEach>
				</select>
			</div>
			
			<c:forEach items="${MapParam}" var="quality" varStatus="status">
	    		<div id="${quality[0].nameEh}" <c:if test="${!status.first}">style="display:none;"</c:if> >
		    		<c:forEach items="${quality}" var="row" varStatus="status_inner">
			    		<c:if test="${status_inner.first}">
			    			<c:if test="${quality[0].nameEh == 'pci'}">
	    						<div class="${row.nameEh}_first ${row.nameEh}_div">
					    			<div class="inputDivShow2"> <strong>区间：</strong> </div>
					    			<div class="inputDivShow1">
						    			${row.scope}
					    			</div>
					    			<div class="inputDivShow2"> <strong>颜色：</strong> </div>
					    			<div class="inputDivShow1"><input type="color" id="${row.nameEh}${status_inner.index}_color" name="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
					    		</div>
			    			</c:if>
			    			
			    			<c:if test="${quality[0].nameEh != 'pci'}">
			    				<div class="${row.nameEh}_first ${row.nameEh}_div">
					    			<div class="inputDivShow2"> <strong>区间：</strong> </div>
					    			<div class="inputDivShow1">
						    			<select class="easyui-combobox" name="range1"  id="${row.nameEh}${status_inner.index}_range1" style="width:100px;" data-options="required:true,editable:false,disabled:true">
											    <option value="[" <c:if test="${row.range1== '[' }">selected="selected"</c:if> >大于等于</option>
											    <option value="(" <c:if test="${row.range1== '(' }">selected="selected"</c:if> >大于</option>
										</select>
					    			</div>
					    			<div class="inputDivShow1"><input name="minValue" id="${row.nameEh}${status_inner.index}_minValue" value="${row.begin}" class="easyui-textbox" data-options="required:true"/>
						    			<c:choose>
										    <c:when test="${row.nameEh =='dlbler'}">
										        <strong>%</strong>
										    </c:when>
										    <c:when test="${row.nameEh =='beam'}">
										    	<strong></strong>
										    </c:when>
										    <c:when test="${row.nameEh =='macthrputdl' || row.nameEh =='macthrputul'}">
										    	<strong>Mbps</strong>
										    </c:when>
										    <c:otherwise>
										        <strong>dBm</strong>
										    </c:otherwise>
										</c:choose>
					    			</div>
					    			<div class="inputDivShow2"> <strong>且</strong> </div>
					    			<div class="inputDivShow1">
						    			<select class="easyui-combobox" name="range2" id="${row.nameEh}${status_inner.index}_range2" style="width:100px;" data-options="required:true,editable:false,disabled:true">
											    <option value="]" <c:if test="${row.range2== ']' }">selected="selected"</c:if> >小于等于</option>
											    <option value=")" <c:if test="${row.range2== ')' }">selected="selected"</c:if> >小于</option>
										</select>
									</div>
					    			<div class="inputDivShow1"><input name="maxValue" id="${row.nameEh}${status_inner.index}_maxValue" value="${row.end}" class="easyui-numberbox" data-options="required:true"  />
					    				<c:choose>
										    <c:when test="${row.nameEh =='dlbler'}">
										        <strong>%</strong>
										    </c:when>
										    <c:when test="${row.nameEh =='beam'}">
										    	<strong></strong>
										    </c:when>
										    <c:when test="${row.nameEh =='macthrputdl' || row.nameEh =='macthrputul'}">
										    	<strong>Mbps</strong>
										    </c:when>
										    <c:otherwise>
										        <strong>dBm</strong>
										    </c:otherwise>
										</c:choose>
					    			</div>
					    			<div class="inputDivShow2"> <strong>颜色：</strong> </div>
					    			<div class="inputDivShow1"><input type="color" id="${row.nameEh}${status_inner.index}_color" name="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
					    		</div>
			    			</c:if>
			    		</c:if>
			    		<c:if test="${status_inner.last}">
			    			<c:if test="${quality[0].nameEh == 'pci'}">
	    						<div class="${row.nameEh}_last ${row.nameEh}_div">
					    			<div class="inputDivShow2"> <strong>区间：</strong> </div>
					    			<div class="inputDivShow1">
						    			${row.scope}
					    			</div>
					    			<div class="inputDivShow2"> <strong>颜色：</strong> </div>
					    			<div class="inputDivShow1"><input type="color" id="${row.nameEh}${status_inner.index}_color" name="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
					    		</div>
			    			</c:if>
			    			
			    			<c:if test="${quality[0].nameEh != 'pci'}">
			    				<div class="${row.nameEh}_last ${row.nameEh}_div">
					    			<div class="inputDivShow2"> <strong>区间：</strong> </div>
					    			<div class="inputDivShow1">
						    			<select class="easyui-combobox" name="range1"  id="${row.nameEh}${status_inner.index}_range1" style="width:100px;" data-options="required:true,editable:false,disabled:true">
											    <option value="[" <c:if test="${row.range1== '[' }">selected="selected"</c:if> >大于等于</option>
											    <option value="(" <c:if test="${row.range1== '(' }">selected="selected"</c:if> >大于</option>
										</select>
					    			</div>
					    			<div class="inputDivShow1"><input name="minValue" id="${row.nameEh}${status_inner.index}_minValue" value="${row.begin}" class="easyui-numberbox" data-options="required:true"  />
					    				<c:choose>
										    <c:when test="${row.nameEh =='dlbler'}">
										        <strong>%</strong>
										    </c:when>
										    <c:when test="${row.nameEh =='beam'}">
										    	<strong></strong>
										    </c:when>
										    <c:when test="${row.nameEh =='macthrputdl' || row.nameEh =='macthrputul'}">
										    	<strong>Mbps</strong>
										    </c:when>
										    <c:otherwise>
										        <strong>dBm</strong>
										    </c:otherwise>
										</c:choose>
					    			</div>
					    			<div class="inputDivShow2"> <strong>且</strong> </div>
					    			<div class="inputDivShow1">
						    			<select class="easyui-combobox" name="range2" id="${row.nameEh}${status_inner.index}_range2" style="width:100px;" data-options="required:true,editable:false,disabled:true">
											    <option value="]" <c:if test="${row.range2== ']' }">selected="selected"</c:if> >小于等于</option>
											    <option value=")" <c:if test="${row.range2== ')' }">selected="selected"</c:if> >小于</option>
										</select>
									</div>
					    			<div class="inputDivShow1"><input name="maxValue" id="${row.nameEh}${status_inner.index}_maxValue" value="${row.end}" class="easyui-textbox"  data-options="required:true" />
					    				<c:choose>
										    <c:when test="${row.nameEh =='dlbler'}">
										        <strong>%</strong>
										    </c:when>
										    <c:when test="${row.nameEh =='beam'}">
										    	<strong></strong>
										    </c:when>
										    <c:when test="${row.nameEh =='macthrputdl' || row.nameEh =='macthrputul'}">
										    	<strong>Mbps</strong>
										    </c:when>
										    <c:otherwise>
										        <strong>dBm</strong>
										    </c:otherwise>
										</c:choose>
					    			</div>
					    			<div class="inputDivShow2"> <strong>颜色：</strong> </div>
					    			<div class="inputDivShow1"><input type="color" name="color" id="${row.nameEh}${status_inner.index}_color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
					    			<div class="inputDivShow2"></div>
					    			<a class="easyui-linkbutton" data-options="plain:true" iconCls="icon-add" style="width:40px;"  onclick="addRow();"  ></a>
					    			<a class="easyui-linkbutton" data-options="plain:true" iconCls="icon-remove" style="width:40px;" onclick="removeRow();" ></a>
					    		</div>
			    			</c:if>
			    		</c:if>
			    		<c:if test="${!status_inner.last&&!status_inner.first}">
			    			<c:if test="${quality[0].nameEh == 'pci'}">
	    						<div class="${row.nameEh}_context ${row.nameEh}_div">
					    			<div class="inputDivShow2"> <strong>区间：</strong> </div>
					    			<div class="inputDivShow1">
						    			${row.scope}
					    			</div>
					    			<div class="inputDivShow2"> <strong>颜色：</strong> </div>
					    			<div class="inputDivShow1"><input type="color" id="${row.nameEh}${status_inner.index}_color" name="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
					    		</div>
			    			</c:if>
			    			
			    			<c:if test="${quality[0].nameEh != 'pci'}">
			    				<div class="${row.nameEh}_context ${row.nameEh}_div">
				    				<div class="inputDivShow2"> <strong>区间：</strong> </div>
				    				<div class="inputDivShow1">
						    			<select class="easyui-combobox" name="range1" id="${row.nameEh}${status_inner.index}_range1" style="width:100px;" data-options="required:true,editable:false,disabled:true">
											    <option value="[" <c:if test="${row.range1== '[' }">selected="selected"</c:if> >大于等于</option>
											    <option value="(" <c:if test="${row.range1== '(' }">selected="selected"</c:if> >大于</option>
										</select>
					    			</div>
					    			<div class="inputDivShow1"><input value="${row.begin}" id="${row.nameEh}${status_inner.index}_minValue" name="minValue" class="easyui-numberbox" data-options="required:true"  />
					    				<c:choose>
										    <c:when test="${row.nameEh =='dlbler'}">
										        <strong>%</strong>
										    </c:when>
										    <c:when test="${row.nameEh =='beam'}">
										    	<strong></strong>
										    </c:when>
										    <c:when test="${row.nameEh =='macthrputdl' || row.nameEh =='macthrputul'}">
										    	<strong>Mbps</strong>
										    </c:when>
										    <c:otherwise>
										        <strong>dBm</strong>
										    </c:otherwise>
										</c:choose>
					    			</div>
					    			<div class="inputDivShow2"> <strong>且</strong> </div>
					    			<div class="inputDivShow1">
						    			<select class="easyui-combobox" name="range2" id="${row.nameEh}${status_inner.index}_range2" style="width:100px;" data-options="required:true,editable:false,disabled:true">
											    <option value="]" <c:if test="${row.range2== ']' }">selected="selected"</c:if> >小于等于</option>
											    <option value=")" <c:if test="${row.range2== ')' }">selected="selected"</c:if> >小于</option>
										</select>
									</div>
					    			<div class="inputDivShow1"><input value="${row.end}" name="maxValue" id="${row.nameEh}${status_inner.index}_maxValue" class="easyui-numberbox" data-options="required:true"  />
					    				<c:choose>
										    <c:when test="${row.nameEh =='dlbler'}">
										        <strong>%</strong>
										    </c:when>
										    <c:when test="${row.nameEh =='beam'}">
										    	<strong></strong>
										    </c:when>
										    <c:when test="${row.nameEh =='macthrputdl' || row.nameEh =='macthrputul'}">
										    	<strong>Mbps</strong>
										    </c:when>
										    <c:otherwise>
										        <strong>dBm</strong>
										    </c:otherwise>
										</c:choose>
					    			</div>
					    			<div class="inputDivShow2"> <strong>颜色：</strong> </div>
					    			<div class="inputDivShow1"><input type="color" name="color" id="${row.nameEh}${status_inner.index}_color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
				    			</div>
			    			</c:if>
			    		</c:if>
	    			</c:forEach>
	    		</div>
  			</c:forEach>
	    </form>
	</div>
  	<div data-options="region:'south',border:false" style="height:40px;">
	   	<table width="100%" style="border-top:1px solid #95b8e7;">
	   		<tr height="35px">
	    		<td width="50%;" align="right">
	    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
	    				<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >确认</a>
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
