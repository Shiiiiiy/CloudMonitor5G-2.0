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
    <title>单验报告生成</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist/echarts.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/html2canvas.js"></script>
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
				        <option value="0">绕点</option>
				        <option value="1">FTP下载</option>
				        <option value="2">FTP上传</option>
				        <option value="3">ENDC成功率测试</option>
				        <option value="4">ping（32）测试</option>
				        <option value="5">ping（1500）测试</option>
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
	    			gNBID：<input id="gnbIdQuery" class="easyui-numberbox" style="width:125px;"/>
	    		</div>
				<table width="100%">
		    		<tr>
			    		<td width="45%" align="right">
			    			<shiroextend:hasAnyPermissions name="stationCreate:create">
			    				<a class="easyui-linkbutton" onclick="createReport();" style="width: 80px;" data-options="iconCls:'icon-excel'" >生成报告</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="10%" align="center">
			    			<shiroextend:hasAnyPermissions name="stationCreate:query">
			    				<a class="easyui-linkbutton" onclick="pageQuery();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="45%" align="left">
			    			<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a>
			    		</td>
		    		</tr>
		    	</table>
	    	</form>
		</div>
	</div>
	<div id="mapWindowId" class="easyui-window" title="地图轨迹" style="width:900px;height:550px"
        data-options="iconCls:'icon-save',modal:true,resizable:false,maximizable:false">
		<div style="height: 100%;width: 100%;z-index: 90;float:left;">
			<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
		</div>
        <div style="width: 100%;z-index: 100;float:left;margin-top:-470px;height: 20px;">
        	<div style="width:28%;text-align: center;float:left;">
    			小区选择：
			    <%-- <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id',onSelect:cellNameChooseFunc,
			    	editable:false,textField:'text',url:'${pageContext.request.contextPath}/stationReportShow/getAllCell.action'"> --%>
			    <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id', editable:false, textField:'text'">
    		</div>
    		<div style="width:28%;text-align: center;float:left;">
    			指标选择：
    			<input id="eventChoose" class="easyui-combobox" name="eventChoose" data-options="valueField:'id',editable:false,textField:'text'" >
    		</div>
        </div>
	</div>
	<div id="chartWindowId" class="easyui-window" title="chart速率图" style="width:900px;height:550px"
        data-options="iconCls:'icon-save',modal:true,resizable:false,maximizable:false">
		<div id="reportAnalysissCensus" style="height: 90%;width: 90%;float:left;"></div>
	</div>
  </body>
  
  <script type="text/javascript">
		
		var city = "全部";
		var reportAnalysissCensus;
		
		$(function(){
			initTable();
			initChart();
			$('#mapWindowId').window('close'); 
			$('#chartWindowId').window('close'); 
		});
		
		/*填充名称下拉框*/
		function initSelect(ugList,nameList,id){
			var data = [];
			for(var i = 0;i < ugList.length;i++){
				var oneUp = {};
				oneUp.id = ugList[i].name;
				oneUp.name = ugList[i].name;
				if(nameList.indexOf(ugList[i].name)!=-1){
					oneUp.selected = true;
				}
				data.push(oneUp);
			}
		    $("#"+id+"").combobox({
				valueField: 'id', 
				textField: 'name',
				data: data,
				filter: function(q, row){
					var opts = $(this).combobox('options');
					return row[opts.textField].toLowerCase().indexOf(q.toLowerCase())>-1;
				}
		    }); 
		}
		
		
		function colorFunc(value,leftValue){
			if(value == 1){
				return "<div style='height:20px;width:20px;background: #00B050;margin-left:"+leftValue+"px;'></div>";
			}else if(value == 2){
				return "<div style='height:20px;width:20px;background: #FFFF00;margin-left:"+leftValue+"px;'></div>";
			}else if(value == 0){
				return "<div style='height:20px;width:20px;background: red;margin-left:"+leftValue+"px;'></div>"
			}
		}
		
		function initChart(){
			reportAnalysissCensus = echarts.init(document.getElementById("reportAnalysissCensus")); 
			reportAnalysissCensus.setOption(reportAnalysisCensusOpen);
		}
		
		/* 初始化测试计划列表 */
		function initTable(){
			$("#stationLogTable").datagrid({
				// 表头
				columns:[[
					{field:'id',checkbox:true},
					{field:'siteName',width:100,align:'center',title:'SiteName'}, 
					{field:'gnbId',width:100,align:'center',title:'gNBID'},
					{field:'cellName',width:100,align:'center',title:'CellName'},
					{field:'lon',width:100,align:'center',title:'经度'},
					{field:'lat',width:100,align:'center',title:'纬度'},
					{field:'localCellID',width:100,align:'center',title:'LocalCellID'},
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
					{field:'reportCreateDate',width:80,align:'center',title:'生成报告日期'},
					{field:'city',width:100,align:'center',title:'区域'},
					{field:'goodFtpUpload',width:80,align:'center',title:'好点FTP上传',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'midFtpUpload',width:80,align:'center',title:'中点FTP上传',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'badFtpUpload',width:100,align:'center',title:'差点FTP上传',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'goodFtpdownload',width:80,align:'center',title:'好点FTP下载',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'midFtpdownload',width:80,align:'center',title:'中点FTP下载',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'badFtpdownload',width:80,align:'center',title:'差点FTP下载',
						formatter:function(value,row,index){
							return colorFunc(value,30);
						}
					},
					{field:'raodianTest',width:100,align:'center',title:'绕点测试',
						formatter:function(value,row,index){
							return colorFunc(value,40);
						}
					},
					{field:'goodPing32',width:100,align:'center',title:'好点ping小包业务',
						formatter:function(value,row,index){
							return colorFunc(value,40);
						}
					},
					{field:'goodPing1500',width:100,align:'center',title:'好点ping大包业务',
						formatter:function(value,row,index){
							return colorFunc(value,40);
						}
					},
					{field:'goodEndcSuccessRatio',width:140,align:'center',title:'好点ENDC成功率测试',
						formatter:function(value,row,index){
							return colorFunc(value,60);
						}
					},
					{field:'midEndcSuccessRatio',width:140,align:'center',title:'中点ENDC成功率测试',
						formatter:function(value,row,index){
							return colorFunc(value,60);
						}
					},
					{field:'badEndcSuccessRatio',width:140,align:'center',title:'差点ENDC成功率测试',
						formatter:function(value,row,index){
							return colorFunc(value,60);
						}
					}
				]],
				url:'${pageContext.request.contextPath}/stationReportCreat/doPageListJson.action?city='+city,
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
				gnbId : gnbId
			});
		}
		
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
		}
		
		var eventOptionValue = 0; //指标选择项序号
		var cellOptionValue = 0; //小区选择项序号
		var currentProcess =0; //生成进度到第几项
		var eventValue; //选择的指标值
		var cellId; 
		var processSum = 0;
		
		var outTrailType; //所选小区截图类型，0：小区级，1：基站级，2：小区级和基站级都有
		var currentOutType; //现有执行的截图类型
		var nodeData = [];
		
		//生成报告
		function createReport(){
			var nodes = $('#stationLogTable').datagrid('getSelections');
			var cn = '';
			var sitename= '';
			if(nodes.length > 0){
				for(var i = 0; i < nodes.length;i++){
					if(cn == ''){
						cn = nodes[i].city;
					}else if(cn != nodes[i].city){
						$.messager.alert('提示','请选择同一区域小区进行生成','info');
						return;
					}
					if(sitename == ''){
						sitename = nodes[i].siteName;
					}else if(sitename != nodes[i].siteName){
						$.messager.alert('提示','同一基站小区才能生成','info'); 
						return;
					}
				}
			}else{
				$.messager.alert('提示','请选择需要生成报告的站点','info');
				return;
			}
			
			$.messager.confirm("系统提示", "有的站点单验报告参数已经生成，这次生成会覆盖旧的记录，确认生成吗?", function(r) {
				if (r) {
					creatReportProcess(nodes)
				}
			});
		}
		
		var ids = '';
		function creatReportProcess(nodes){
		
			$.messager.progress({
	           title: '提示',
	           msg: '生成单站报告中...',
	           text: ""
	       });
		
			ids = '';
			nodeData = [];
			for(var i = 0; i < nodes.length;i++){
				if(i != 0){
					ids = ids + ',';
				}
				ids = ids + nodes[i].id;
				
				var oneNode = {id:nodes[i].id,text:nodes[i].cellName};
				nodeData.push(oneNode);
			}
			var cityName = nodes[0].city; 
			
			$.post('${pageContext.request.contextPath}/stationReportShow/getInitTrailKpi.action?cityNamesStr='+cityName, {}, 
					function(result) {
						initSelect(result.tlList,result.tlList[0].name,'eventChoose');
						outTrailType = result.outPutType;
						if(outTrailType==0){
							currentOutType = 0; //执行小区级截图
						}else if(outTrailType==1){
							currentOutType = 1;  //执行基站级截图
						}else if(outTrailType==-1){
							currentOutType = 0; //默认先执行小区级截图，再执行基站级截图，先0后1
						}
			       	    processSum = 0;
			       	    currentProcess = 0;
					}
			,'json');
			
			$.post("${pageContext.request.contextPath}/stationReportCreat/createReport.action",{ids:ids},
					function(result){
						$.messager.progress('close');
						if(result.errorMsg != null){
							$.messager.alert('提示','报告已生成,其中:'+result.errorMsg,'info');
						}else if(result.cellNames == null){
							$.messager.alert('提示','报告已生成','info');
						}else{
							$.messager.alert('提示','报告已生成,其中'+result.cellNames+'无单站数据,无法生成单验报告!','info');
						}
						pageQuery();
						
						$.messager.confirm("系统提示", "报告已生成,确认继续生成轨迹和速率图片吗？", function(r) {
							if (r) {
								initMapTrail(cityName);
							}else{
								$.messager.confirm("系统提示", "需要继续生成单站报告吗？", function(result) {
									if (result) {
										//保存单验报告
										saveStationReort();
									}
								});
							}
						});
					}
			,"json");
			
		}
		
		function initMapTrail(cityName){
			$('#mapWindowId').window('open'); 
			var src = $("#mapIframe").attr("src");
			if(!src){
				$("#mapIframe").attr("src", "${pageContext.request.contextPath}/gis/default3.html?toolbarType=90");
			}
			//生成轨迹图片
			$.messager.progress({
	           title: '提示',
	           msg: '生成轨迹图片中...',
	           text: "0"
	       	});
	       	
	       	var t2 = window.setInterval(function(){
        		if(mapIframe.window.map){
        			window.clearInterval(t2);
	        		$.ajax({
		  	             type: "GET",
		  	             url: "${pageContext.request.contextPath}/stationCompletionShow/getShpName.action",
		  	             data: {'cityName':cityName,'cityId':null,'cityType':null},
		  	             dataType: "json",
		  	             success: function(data){
		  	             		var arrs = ['GNBID','CELLID','SITENAME','CELLNAME','TAC','PCI','FREQUENCY1','LONGITUDE','LATITUDE','TILTM','AZIMUTH','HEIGHT','CITY'];
		      					mapIframe.window.StationAddCellToMap(data.shpName,true,data.lon,data.lat,arrs);
		      					mapIframe.window.StationLayerManage();
		      					mapIframe.window.showDivByDivId('Legend');
		      					mapIframe.window.displayMapToolTip('DivSec','showHide1');
		                 }
		       	   });
		       	   //初始化查询图例
		       	   $.ajax({
		  	             type: "GET",
		  	             url: "${pageContext.request.contextPath}/mapTrailLegendParam/displayLegnd.action",
		  	             data: {'colorMapType' : "mapNrPlanColor",'cityName':cityName},
		  	             dataType: "json",
		  	             success: function(data){
		  	             		if(data!=undefined && data!=null){
					        		mapIframe.window.colorMapEtgTral = data[0];
					        		mapIframe.window.initColorMapParam= data[1];
					        	}
		                 }
		       	   });
		       	   
		       	   setTimeout(function() {
						creatTrailImage(cityName);
					}, 1000);
        		}
        	},1000);
		}
		
		//生成轨迹图片
		function creatTrailImage(cityName){
			$.post('${pageContext.request.contextPath}/stationReportShow/getInitTrailKpi.action?cityNamesStr='+cityName, {}, 
					function(result) {
						console.info(result.tlList);
						initSelect(result.tlList,result.tlList[0].name,'eventChoose');
						outTrailType = result.outPutType;
						if(outTrailType==0){
							currentOutType = 0; //执行小区级截图
						}else if(outTrailType==1){
							currentOutType = 1;  //执行基站级截图
						}else if(outTrailType==-1){
							currentOutType = 0; //默认先执行小区级截图，再执行基站级截图，先0后1
						}
			       	    processSum = 0;
			       	    currentProcess = 0;
			       	    getCellName();						
					}
			,'json');
			
		}
		
		
		/*填充小区*/
		function getCellName(){
			eventOptionValue = 0;
			cellOptionValue = 0;
			$("#cellNameChoose").combobox('loadData',[]);
			if(currentOutType==0){  //小区级
				$("#cellNameChoose").combobox('loadData',nodeData);
				$("#cellNameChoose").combobox('setValue',nodeData[0].id);
			}else if(currentOutType==1){ //基站级
				$("#cellNameChoose").combobox('loadData',[nodeData[0]]);
				$("#cellNameChoose").combobox('setValue',nodeData[0].id);
			}
			
		    var eventdata = $("#eventChoose").combobox('getData');
       		eventValue = eventdata[eventOptionValue].id; 
       		
       		if(outTrailType==0){
				processSum = nodeData.length * eventdata.length;
			}else if(outTrailType==1){
				processSum = eventdata.length;
			}else if(outTrailType==-1){
				processSum = nodeData.length * eventdata.length + eventdata.length;
			}
       		
			showEventLegnd();
		}
		
		
		function showEventLegnd(){
			$("#eventChoose").combobox("setValue",eventValue);
			var data = $("#cellNameChoose").combobox('getData');
			showCellimage(data[cellOptionValue].id,data[cellOptionValue].text);
		}
		
		var recentTime;
		function showCellimage(id,cellName){
			$("#cellNameChoose").combobox("setValue",id);
			cellId = id;
			recentTime = null;
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/stationReportShow/getCreatReportPoint.action",
	             data: {'cellName':cellName,'id':id,'event':eventValue,"currentOutType":currentOutType},
	             dataType: "json",
	             success: function(data){
	            	if(data != null){
	            		recentTime = data.time;
            			mapIframe.window.stationEtgTralFunc2(data.data,eventValue,true,data.lon,data.lat,data.pciMap);
           			}else{
           				mapIframe.window.stationEtgTralFunc2([],eventValue,true,data.lon,data.lat,data.pciMap); 
           			}
              	 }
	         });
		}
		
		//保存地图的截图
		function downloadImagejie(imageByte,url){
			if(imageByte!=null){
				$.ajax({
		             type: "POST",
		             url: "${pageContext.request.contextPath}/stationReportShow/saveImge.action", 
		             data: {'url':url,'id':cellId,'event':eventValue,"currentOutType":currentOutType,"reportCreateTime":recentTime,"imageByte":imageByte},
		             dataType: "json",
		             success: function(result){
		             	imageByte = null;
		             	url = null;
		            	if (result.errorMsg) {
		            		$.messager.progress('close');
							$.messager.alert("系统提示", result.errorMsg,"error");
						}
	              	 }
		         });
			}
			var data = $("#cellNameChoose").combobox('getData');
			var eventdata = $("#eventChoose").combobox('getData');
			//进度修改
			currentProcess++;
			var processPercent = Math.round(currentProcess / processSum *100);
			$.messager.progress('bar').progressbar({
				text: processPercent+"%"
			});
			
			cellOptionValue++;
			if(cellOptionValue!=data.length){
				eventValue = eventdata[eventOptionValue].id;
				setTimeout(function() {
					showEventLegnd();
				},1000);
			}else{
				cellOptionValue = 0;
				eventOptionValue++;
				if(eventOptionValue!= eventdata.length){
					eventValue = eventdata[eventOptionValue].id;
					setTimeout(function() {
						showEventLegnd();
					}, 1000);
				}else{
					if(outTrailType==-1 && currentOutType==0){
						currentOutType = 1; //执行小区级截图完毕，再执行基站级截图，先0后1
						setTimeout(function() {
							getCellName();
						}, 1000);
					}else{
						$.messager.progress('close');
						$('#mapWindowId').window('close');
						
						//开始生成速率chart图
						setTimeout(function() {
							creatRateChart();
						}, 1000);
					}
				}
			}
							
		 }
		 
		 var rateValues=[];
	 	 var reportAnalysisCensusOpen =  {
		     title: {
		        text: 'Chart速率图'
		    },
		    tooltip: {
		        trigger: 'axis'
		        /* formatter : function(params){  
					// 格式化成时分秒
		           let date = new Date(params[0].value[0]);
		           let nowHours = timeAdd0(date.getHours().toString());
		           let nowMin = timeAdd0(date.getMinutes().toString());
		           let nowSeconds = timeAdd0(date.getSeconds().toString());
		           var texts = [nowHours,nowMin,nowSeconds];
		           return texts.join(':') + "<br/>" +"值:"+params[0].value[1];
				} */
		    },
		    xAxis: {
		        type: 'time',
		        splitLine: {
		            show: false
		        },
		        // x轴的字
		       axisLabel: {
		         show: true,
		         showMinLabel: true,
		         showMaxLabel: true
		         /* formatter: function (value, index) {
		           // 格式化成时分秒
		           let date = new Date(value);
		           let nowHours = timeAdd0(date.getHours().toString());
		           let nowMin = timeAdd0(date.getMinutes().toString());
		           let nowSeconds = timeAdd0(date.getSeconds().toString());
		           var texts = [nowHours,nowMin,nowSeconds];
		           return texts.join(':');
		         } */
		       }
		    },
		    yAxis: {
		        type: 'value',
		        boundaryGap: [0, '100%'],
		        splitLine: {
		            show: false
		        }
		    },
		    series: [{
		        name: '模拟数据',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        // 仅仅修改折线段的颜色
	            lineStyle: {
	                color: "#4EC43E"
	            },
		        markPoint: {
	                data: [
	                    {type: 'max', name: '最大值'}
	                ]
	            },
	            markLine:{
					data:[
						{type:'average',name:'平均值'},
					]
				},
		        data: rateValues
		    }]
    	 };
		 
		 //生成速率chart图
		 var eventArry = [];
		 //"J":极好点,"H":好点,"Z":中点 ,"C":差点  
		 var wireStatusArry = ["J,H","Z","C"];
		 function creatRateChart(){
		 	//保存查询速率事件的名称
		 	$.post('${pageContext.request.contextPath}/stationReportShow/getInitChartKpi.action', {}, 
					function(result) {
						eventArry = [];
						for(var i = 0;i < result.tlList.length;i++){
							eventArry.push(result.tlList[i].name);
						}
						$('#chartWindowId').window('open'); 
						$.messager.progress({
				           title: '提示',
				           text: '生成速率Chart图片中...'
				       	});
					 	
					 	var cellidArry = ids.split(",");
					 	chartProcess(cellidArry,0,0,0);
					}
			,'json');
		 
		 }
		 
		 //开始查询数据并生成chart
		 function chartProcess(cellidArry,idIndex,eventIndex,wireStatusIndex){
		 	$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/stationReportShow/gainRatePointData.action",
	             data: {'id':cellidArry[idIndex],'event':eventArry[eventIndex],'wireStatus':wireStatusArry[wireStatusIndex]},
	             dataType: "json",
	             success: function(data){
	             	console.info(data);
	            	if(data != null && data.value.length>0){
	            		rateValues  = data.value;
            			var option = reportAnalysissCensus.getOption();
            			var wireName= "";
            			if(wireStatusArry[wireStatusIndex]=="J,H"){
            				wireName="好点";
            			}else if(wireStatusArry[wireStatusIndex]=="Z"){
            				wireName="中点";
            			}else if(wireStatusArry[wireStatusIndex]=="C"){
            				wireName="差点";
            			}
            			option.title[0].text= "指标:"+eventArry[eventIndex]+", "+wireName;
					    option.series[0].data = rateValues;
					    reportAnalysissCensus.setOption(option);
					    
					    //2秒后保存截图
					    setTimeout(function() {
							reportTakeScreenshot("reportAnalysissCensus",cellidArry,idIndex,eventIndex,wireStatusIndex,data.time);
						}, 3000);
						
           			}else{
           				rateChartFinish(cellidArry,idIndex,eventIndex,wireStatusIndex);
           			}
              	 }
	         });
		 }
		 
		/**
		 * html2canvas保存速率截图
		 * 注意：服务端保存截图不能使用GET请求，要使用POST
		 */
		 function reportTakeScreenshot(id,cellidArry,idIndex,eventIndex,wireStatusIndex,recentTime) {
			  html2canvas(document.getElementById(id), {
			   	  allowTaint: false,
			      taintTest: true
			  }).then(function(canvas) {
				  var imagestr = canvas.toDataURL("image/png", 0.2);
				  //需要去除"data:image/png;base64,"才能转换为图片
				  imagestr = imagestr.replaceAll("data:image/png;base64,", "");
				  //服务端保存截图
				  saveRateChartImage(imagestr,cellidArry,idIndex,eventIndex,wireStatusIndex,recentTime);
			   
			  }).catch(function(e) {
				  console.error(e);
			  });
		 }
		 
		 //保存速率截图
		 function saveRateChartImage(imageByte,cellidArry,idIndex,eventIndex,wireStatusIndex,recentTime){
			if(imageByte!=null){
				$.ajax({
		             type: "POST",
		             url: "${pageContext.request.contextPath}/stationReportShow/savePdcpRateImge.action", 
		             data: {'id':cellidArry[idIndex],'event':eventArry[eventIndex],'wireStatus':wireStatusArry[wireStatusIndex],"reportCreateTime":recentTime,"imageByte":imageByte},
		             dataType: "json",
		             success: function(result){
		             	imageByte = null;
		             	url = null;
		            	if (result.errorMsg) {
		            		$.messager.progress('close');
							$.messager.alert("系统提示", result.errorMsg,"error");
						}
	              	 }
		         });
			}
			rateChartFinish(cellidArry,idIndex,eventIndex,wireStatusIndex);
		 }
		 
		 function rateChartFinish(cellidArry,idCurentIndex,eventCurentIndex,wireStatusIndex){
		 	if(wireStatusIndex != wireStatusArry.length-1){
		    	chartProcess(cellidArry,idCurentIndex,eventCurentIndex,wireStatusIndex+1);
		    }else if(eventCurentIndex != eventArry.length-1){
		    	chartProcess(cellidArry,idCurentIndex,eventCurentIndex+1,0);
		    }else if(idCurentIndex != cellidArry.length-1){
		    	chartProcess(cellidArry,idCurentIndex+1,0,0);
		    }else{
		    	$.messager.progress('close');
		    	$('#chartWindowId').window('close'); 
		    	saveStationReort();
		    }
		 }
		 
		 function saveStationReort(){
		 	//保存单验报告
	 		$.messager.progress({
	           title: '提示',
	           text:'保存报告中...'
	       	});
			$.ajax({
  	             type: "GET",
  	             url: "${pageContext.request.contextPath}/stationReportShow/saveStationReport.action",
  	             data: {'idsStr':ids},
  	             dataType: "json",
  	             success: function(data){
  	             	$.messager.progress('close');
  	             	if(data.errorMsg){
  	             		$.messager.alert('提示',"图片保存成功,"+ data.errorMsg,'error');
  	             	}else{
  	             		$.messager.alert('提示',"图片和报告都已保存成功",'info');
  	             	}
                 }
       	   });
			return;
		 }
	</script>
</html>
