<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>楼宇统计</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<style type="text/css">
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: left;
		    width: 350px;
   		}
   		.inputDivShow input{
   			width:140px;
   		}
   		.inputDivShow select{
   			width:140px;
   		}
   		.listTitle {
		    font-family: "Microsoft Sans Serif";
		    font-size: 12px;
		    font-weight: bold;
		  	/*border-left:4px solid #54b4f8;*/
		    margin:4px 0px;
		}
		.testPlan_tabs{
			background-color: #f6f9ff;
		}
		.modulebutton{
			color: black;
			border-color: #d9d8d7;
			background: #d3e9ff;
		}
		.modulebuttonhover,.modulebutton:hover{
			color: #FFF;
			border-color: #71b0dc;
			background: #62baf9;
		}
		.inputDiv {
			zoom: 1;
			font-size: 12px;
			margin: 5px;
			width: 255px;
			text-align: right;
			display: none;
			padding-left: 0;
			padding-right: 0;
		}
		.inputDivShow .datagrid-header {
			display:none;
		}
	</style>
	<!--引用百度地图API-->
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=liKpDfLP41rNnZmM1D33WljN"></script>
	<script type="text/javascript">
	var state=0;
	//gisCommon会调用获取楼宇不同指标显示的url
	function getFloorInfoByIndexPointActionUrl(){
		return "<%=basePath%>floorReport/getFloorInfoByIndex.action";
	}
	//gisCommon会调用获取请求状态参数
	function getStateInfo(){
		return state;
	}
	//gisCommon会调用获取查询楼宇信息URL
	function getgetFloorInfoPointActionUrl(){
		return "<%=basePath%>floorReport/getFloorInfo.action";
	}
	
	$(function(){
		$("#floorTab").datagrid({
			// 表头
			columns:[[
				{field:'floorName',width:110,align:'center',title:'楼宇名称',
					formatter:function(value,row,index){
						return '<a href="#" title="'+value+'" onclick="seeInfo(\''+value+'\',\''+row.id+'\');" >'+value+'</a>';
						//return '<a href="#" title="'+value+'" onclick="alertAttention();" >'+value+'</a>';
				}	
				},
				{field:'id',hidden:true},
				{field:'rsrpaverage',width:120,align:'center',title:'RSRP均值',formatter:showTooltip},
				{field:'sinraverage',width:120,align:'center',title:'SINR均值',formatter:showTooltip},
				{field:'ltecoverage110rate',width:90,align:'center',title:'覆盖率',formatter:showTooltip}
				
			]],
			fitColumns:true,
			//填满区域
			fit:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:false
		});
		
	});
	//点击楼宇弹出详情指标
	function seeInfo(floorName,id){
		//alert(floorName);
		mapIframe.window.showBuildingstooltip(floorName,id);
	}
	//地图页面统计按钮
	function goAdd(){
		 //window.open("${pageContext.request.contextPath}/floorReport/goAddInfo.action");  
		//goToPage("${pageContext.request.contextPath}/floorReport/goAddInfo.action");
		parent.addTab(null,"${pageContext.request.contextPath}/floorReport/goAddInfo.action","CQT统计",null);
	}
	//地图页面下载按钮
	function goDownload(){
		goToPage("${pageContext.request.contextPath}/floorReport/downloadExcel.action");
	}
	//打开添加域窗口
	function addATU(){
		$('#winATU').dialog('open');
		//$('#areaTree').tree('expandAll');
		//$('#areaTree').combotree('tree').tree("expandAll")
		 $("#areaTree").tree('showPanel');
	}
	//打开添加终端窗口且加载信息
	function addBox(){
		var city=$("#atuTab").datagrid('getData');
		var rows1 = $("#atuTab").datagrid('getRows');
		if(rows1.length==0||city.rows[0].cityId===""){
			$.messager.alert('系统提示','请选择区域!','warning');
			return false;
		}
		$('#winBox').window('open');
		var checkNodes = $("#areaTree").tree('getChecked');
		var cityIds = [];
		for (var int = 0; int < checkNodes.length; int++) {
			if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
				cityIds.push(checkNodes[int].attributes.refId);
			}
		}
		var cityIdsString = cityIds.join(",");
		$.post("${pageContext.request.contextPath}/cqtReport/terminalTree.action?cityIds="+cityIdsString+"",
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#boxTab2").datagrid('loadData',result.boxInfo);
					}
				}
			,"json");
	}
	//打开添加日志
	function addLog(){
		
		    obj = document.getElementsByName("testRanks");
		    if(obj.length==0||obj[0].value===""){
		    	$.messager.alert('系统提示','请选择测试级别!','warning');
				return false;
		    }
		    var check_val = [];
		    for(k in obj){
		        if(obj[k].checked)
		            check_val.push(obj[k].value);
		    }
		    var testRankString=check_val.join(",");
		 if(testRankString===""){
		    	$.messager.alert('系统提示','请选择测试级别!','warning');
				return false;
		    }
		var city=$("#atuTab").datagrid('getData');
		var rows1 = $("#atuTab").datagrid('getRows');
		if(rows1.length==0||city.rows[0].cityId===""){
			$.messager.alert('系统提示','请选择区域!','warning');
			return false;
		}
		var cityIds=[];
		for (var i = 0; i < rows1.length; i++) {
		 cityIds.push(city.rows[i].cityId);
		}
		var cityIdsString = cityIds.join(",");
		
		var box=$("#boxTab").datagrid('getData');
		var rows2 = $("#boxTab").datagrid('getRows'); 
		if(rows2.length==0||box.rows[0].boxId===""){
			$.messager.alert('系统提示','请选择测试终端!','warning');
			return false;
		}
		var boxIds=[];

		for (var i = 0; i < rows2.length; i++) {
			boxIds.push(box.rows[i].boxId);
		}
		var boxIdsString = boxIds.join(",");
		var beginDate = $('#beginDate').datebox('getValue');
		if(beginDate===""){
			$.messager.alert('系统提示','请选择开始时间!','warning');
			return false;
		}
		var endDate = $('#endDate').datebox('getValue');
		if(endDate===""){
			$.messager.alert('系统提示','请选择结束时间!','warning');
			return false;
		}
		$('#winLog').window('open');
		
		$.post("${pageContext.request.contextPath}/cqtReport/testLogItem.action?cityIds="+cityIdsString+"&boxIds="+boxIdsString+"&beginDate="+beginDate+"&endDate="+endDate+"&testRank="+testRankString+"",
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#logTab2").datagrid('loadData',result.testLog);
					}
				}
			,"json");
	}
	/*
	添加选择项，方法共用
	*/
	function add(i){
		if(i==1){
			var checkNodes = $("#areaTree").tree('getChecked');
			var cityIds = [];
			var names=[];
			for (var int = 0; int < checkNodes.length; int++) {
				if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
					cityIds.push(checkNodes[int].attributes.refId);
					names.push(checkNodes[int].text);
				}
			}
			//回填域值
			//[{cityId:cityIds[0]},{},{}]
			var list=[];
			var list1=[];
			for (var t = 0; t < cityIds.length; t++) {
				var map = {};
				map["name"]=names[t];
				map["cityId"] = cityIds[t];
				list1.push(cityIds[t]);
				list.push(map);
			}
			var listString=list1.join(",");
			//$("#cityIds").value=listString; 
			$("#cityIds").val(listString);
			$("#atuTab").datagrid('loadData',list);
			$('#winATU').dialog('close');
		}else if(i==2){
			//回填终端boxID
			var values=$('#boxTab2').datagrid('getSelections');	
			if(0!=values.length){
				var list2=[];
				for (var c = 0; c < values.length; c++) {
					list2.push(values[c].boxId);
				}
				var list2String=list2.join(",");
				$("#boxIds").val(list2String);
				//$("#boxIds").value=list2String; 
			}
			$("#boxTab").datagrid('loadData',values);
			$('#winBox').window('close');
		}else if(i==3){
			//回填LogID
			var valuess=$('#logTab2').datagrid('getSelections');
			if(0!=valuess.length){
				var list3=[];
				for (var l = 0; l < valuess.length; l++) {
					list3.push(valuess[l].id);
				}
				var list3String=list3.join(",");
				//$("#logIds").textbox('setValue',list3String);
				//$("#logIds")[0].value=list3String; 
				$("#logIds").val(list3String);
			}
			$("#logTab").datagrid('loadData',valuess);
			$('#winLog').window('close');
		}
	}
	/*删除选择项，方法共用*/
	function del(i){
		if(i==1){
			var rows=$('#atuTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#atuTab').datagrid('getRowIndex',rows[u]) ;
					$('#atuTab').datagrid('deleteRow',index) ;
				}
		        var rows = $('#atuTab').datagrid('getRows');//获取当前页的数据行  
		        if(0!=rows.length){
		        var list1 = []; 
		        for (var i = 0; i < rows.length; i++) {  
		        	list1.push(rows[i]['cityId']) ; //获取指定列  
		        }  
		        var list1String=list1.join(",");
		        $("#cityIds").val(list1String);
		        }
			}
			
		}else if(i==2){
			var rows=$('#boxTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#boxTab').datagrid('getRowIndex',rows[u]) ;
					$('#boxTab').datagrid('deleteRow',index) ;
				}
				 var rows = $('#boxTab').datagrid('getRows');//获取当前页的数据行 
				 if(0!=rows.length){
			        var list2 = [];  
			        for (var i = 0; i < rows.length; i++) {  
			        	list2.push(rows[i]['boxId']) ; //获取指定列  
			        }  
			        var list2String=list2.join(",");
			        $("#boxIds").val(list2String);
			}
			}
		}else if(i==3){
			var rows=$('#logTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#logTab').datagrid('getRowIndex',rows[u]) ;
					$('#logTab').datagrid('deleteRow',index) ;
				}
				var rows = $('#logTab').datagrid('getRows');//获取当前页的数据行  
				 if(0!=rows.length){
		        var list3 = [];  
		        for (var i = 0; i < rows.length; i++) {  
		        	list3.push(rows[i]['id']) ; //获取指定列  
		        }  
		        var list3String=list3.join(",");
		        $("#logIds").val(list3String);
			}
			}
		}
	}
	/*展开节点*/
	function open(node,data){
		 $("#areaTree").tree('expandAll');//展开所有节点 
	}
	/*清空选择共用方法*/
	function clean(i){
		if(i==1){
			$("#areaTree").tree('reload');
		}else if(i==2){
			$('#boxTab2').datagrid('uncheckAll');
		}else if(i==3){
			$('#logTab2').datagrid('uncheckAll');
		}
	}
	//初始化条件设置
	var menuNo=1;
	$(function(){
		$("#mb").unbind();
		$("#mm3").unbind();
		$("#mb").bind('click',function(){
			if(menuNo==1){
			$("#mm3").menu({onHide:function(){$("#mm3").menu('show',{top:30});}});
			$("#mm3").menu('show',{top:30});
			menuNo=2;
			}else{
				$("#mm3").menu({onHide:function(){return true;}});
				$("#mm3").menu('hide');
				menuNo=1;
			}
		}); 
	});
	/* 开始分析 */
	function saveReport() {
		$("#reportForm").form('submit',{
			onSubmit : function(param) {
				  if($('#atuTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择区域!','warning');
					return false;
				}else if($('#boxTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择测试终端!','warning');
					return false;
				}else if($('#logTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择Log文件!','warning');
					return false;
				}
				return $(this).form('validate');   
				//return true;
			},
			success : function(result) {
				 var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("失败", resultData.errorMsg,"error");
					return;
				} else {
					$("#floorTab").datagrid('loadData',resultData.info.floor);
					$("#mm3").menu({onHide:function(){return true;}});
					$("#mm3").menu('hide');
					menuNo=1;
					 if(resultData.info.gps[0].length!=0){
						for(var i=0;i<resultData.info.gps[0].length;i++){
							if(resultData.info.gps[0][i].longitude==null||resultData.info.gps[0][i].latitude==null){
								var myGeo = new BMap.Geocoder();
								myGeo.getPoint(resultData.info.gps[0][i].floorName, function(point){
									if (point) {
										resultData.info.gps[0][i].longitude=point.lng;
										resultData.info.gps[0][i].latitude=point.lat;
										//alert(point.lat+","+point.lng);
									}
								}, "上海市");
							}
						}
					} 
					mapIframe.window.displayBuildings(resultData.info.gps);
					state=1;
				 } 
			}
		});
	}
	//清空条件设置
	function cleanAll(){
		$('#atuTab').datagrid('loadData', { total: 0, rows: [] });
		$('#boxTab').datagrid('loadData', { total: 0, rows: [] });
		$('#logTab').datagrid('loadData', { total: 0, rows: [] });
		 $("cityIds").attr("value","");
		 $("boxIds").attr("value","");
		 $("logIds").attr("value","");
		 $('#beginDate').datebox('setValue', '');
		 $('#endDate').datebox('setValue', '');
		 //$('#beginDate').combo('setText','');
		 //$('#endDate').combo('setText','');
		 //$(".testRank").attr("checked",false);
		  //var checks=$("input:checked");
		   checks=$(".testRank");
		 if(checks.length!=0){
			 for(var i=0;i<checks.length;i++){
				 if(checks[i].value!=2){
					 checks[i].checked=false;
				 }
			 }
		 } 
	}
	//以下为逆地址解析
	var myIndex = 0;
	var myGeo = new BMap.Geocoder();
	 var myAdds = ["美罗城"];//需要解析的地址 31.199482,121.44621
	var myPoint=[];//解析完成的百度经纬度坐标,按myAdds顺序
	var myDistrict=[];//解析完成的所属区域,按myAdds顺序
	/*逆地址解析方法入口*/
	function MyBdGEO(){
		var myAdd = myAdds[myIndex];
		myGeocodeSearch(myAdd);
	}
	/*逆地址解析方法主体*/
	function myGeocodeSearch(myAdd){
		myGeo.getPoint(myAdd, function(point){
			if (point) {
				myPoint.push(point.lat+","+point.lng);
				alert(point.lat+","+point.lng);
				myGeo.getLocation(point,function(rs){
					var addComp = rs.addressComponents;
					myDistrict.push(addComp.district);
                    //alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
					//递归进行逆地址解析
                  	myIndex++;
                   if(myIndex < myAdds.length){
						MyBdGEO();
                   } else{
                     myIndex=0;
                     //alert(JSON.stringify(myPoint));
                     //alert(JSON.stringify(myDistrict));
                     
                  /*  $.post("${pageContext.request.contextPath}/workSheet/updateWorkSheet.action?workSheetNumList="+workSheetNums+"&myPoint="+myPoint+"&myDistrict="+myDistrict+"",
 					function(result){
                	   myPoint=[];
                       myDistrict=[];
                       workSheetNums=[];
 					}
 				,"json"); */
                   		//1.将myPoint和myDistrict按需post到后台
                     	//2.post成功后记得清空myPoint和myDistrict
                    
                    
                   }
				});
			}
		}, "上海市");
	}
	</script>
  </head>
  <body id="cc"class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	 <div data-options="region:'north',split:false"  style="height:30px;">
	 <a href="javascript:void(0)" class="easyui-menubutton"id="mb" data-options="menu:'#mm3'">条件设置</a>
	    <div id="mm3" class="menu-content" style="width:456px;height:290px;" >
	 	<form id="reportForm" style="margin:0px;" method="post" action="${pageContext.request.contextPath}/floorReport/doAnalysis.action" >
			<div class="inputDivShow" style="overflow: auto;width:100%">
			测试目标
				<input class="testRank" type="checkbox" style="width:10px" name="test" checked="checked" value="2" >CQT
			</div>
			<br>
			<div class="inputDivShow"style="overflow: auto;width:100%">
				开始时间
				<input id="beginDate" class="easyui-datetimebox"  name="cqtStatisticeTaskRequest.beginDate" data-options="required:true,editable:false" >
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				结束时间
				<input id="endDate"class="easyui-datetimebox"  name="cqtStatisticeTaskRequest.endDate" data-options="required:true,editable:false" >
			</div>		
		<br>
			<div class="inputDivShow" style="overflow: auto;width:100%">
			测试级别
			    	<input class="testRank" type="checkbox" style="width:10px" name="testRanks"  value="1" >组织巡检
			    	<input class="testRank" type="checkbox" style="width:10px" name="testRanks" checked="checked"value="2" >日常优化
			    	<input class="testRank" type="checkbox" style="width:10px" name="testRanks"  value="3" >设备调试
			</div>
		<br>
			<div class="inputDivShow" style="overflow: auto;width:136px;"><font color="red">*</font>测试域
				<br>
				<table  id="atuTab"class="easyui-datagrid" style="width:136px;height:100px">
    			    <thead>
						<tr>
							<th data-options="field:'cityId',hidden:true"></th>
							<th data-options="field:'name',width:276"></th>
						</tr>
					</thead>
    			</table>
			    <span> 
					<input type="button" style="width:66px;" value="添加" class="button"onclick="addATU()" />
				</span>					
				<span> 
					<input type="button" style="width:66px;" value="删除" class="button"onclick="del(1)" /> 
				</span>
			</div>
			<div id="winATU" class="easyui-dialog"  title="添加ATU域" style="width:400px;height:290px;overflow: auto;" data-options="onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:false,closed:true,top:30,left:456,buttons:'#atu-buttons'">
				<ul id="areaTree" name="cqtStatisticeTaskRequest.cityIds"  class="easyui-tree" multiple="multiple" data-options="onLoadSuccess:open,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:true,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action'"  >
			    </ul>
			    <div id="atu-buttons">
			    	<a iconCls="icon-ok" class="easyui-linkbutton" style="width:100px; left:100; bottom:0;" onclick="add(1);"  >确定</a>
		    		<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;right:100; bottom:0;" onclick="clean(1);" >取消</a>
        		</div>
        	</div>
        	 <div class="inputDivShow" style="overflow: auto;width:136px;"><font color="red">*</font>测试终端
    			  <br>
    			<table  id="boxTab"class="easyui-datagrid" style="width:136px;height:100px">
    			    <thead>
					<tr>
						<th data-options="field:'boxId',width:276"></th>
						</tr>
					 </thead> 
    			</table>
				<span> 
					<input type="button" style="width:66px;"value="添加" class="button"onclick="addBox()" />
				</span>					
				<span> 
					<input type="button" style="width:66px;"value="删除" class="button"onclick="del(2)" /> 
				</span>
			  </div>
			   <div id="winBox"class="easyui-dialog"  title="添加测试终端" style="width:400px;height:290px;overflow: auto;"data-options="onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,buttons:'#box-buttons',top:30,left:456">
			   		<table  id="boxTab2"class="easyui-datagrid">
    					<thead>
							<tr>
								<th data-options="field:'',checkbox:true"></th>
								<th data-options="field:'boxId',width:125,align:'center'">BOXID</th>
								<th data-options="field:'testTarget',width:100,align:'center'">终端类型</th>
								<th data-options="field:'atuName',width:120,align:'center'">测试域</th>
							</tr>
					    </thead>
					</table>
					<div id="box-buttons">
		    			<a iconCls="icon-ok" class="easyui-linkbutton" style="width:100px; left:100; bottom:0;" onclick="add(2);"  >确定</a>
		    			<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px; right:100; bottom:0;" onclick="clean(2);" >取消</a>
        			</div>
        		</div>
			    <div class="inputDivShow" style="overflow: auto;width:136px;"><font color="red">*</font>Log文件
			    	<br>
			    	<table  id="logTab"class="easyui-datagrid" style="width:136px;height:100px">
    			    	<thead>
							<tr>
								<th data-options="field:'id',hidden:true"></th>
								<th data-options="field:'fileName',width:276"></th>
							</tr>
					    </thead>
    			    </table>
			    	<span> 
						<input type="button" style="width:66px;"value="添加" class="button"onclick="addLog()" />
					</span>					
					<span> 
						<input type="button" style="width:66px;"value="删除" class="button"onclick="del(3)" /> 
					</span>
			    	</div>
			    	<div id="winLog" class="easyui-dialog" buttons="#log-buttons" title="添加Log文件" style="width:400px;height:290px;"data-options="onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true,buttons:'#log-buttons',top:30,left:456">
				    	<table  id="logTab2"class="easyui-datagrid"style="width:100%;height:100%">
    						<thead>
								<tr>
									<th data-options="field:'',checkbox:true"></th>
									<th data-options="field:'id',hidden:true">ID</th>
									<th data-options="field:'fileName',align:'center'">文件名</th>
								</tr>
					    	</thead>
						</table>
						<div id="log-buttons">
							<a iconCls="icon-ok" class="easyui-linkbutton" style="width:100px; left:100; bottom:0;" onclick="add(3);"  >确定</a>
		    				<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px; right:100; bottom:0;" onclick="clean(3);" >取消</a>
        				</div>
        			</div>
        			<input  type="hidden"  name="cqtStatisticeTaskRequest.cityIds"   id="cityIds">
						<input  type="hidden"   name="cqtStatisticeTaskRequest.boxIds"   id="boxIds">
						<input  type="hidden"  name="cqtStatisticeTaskRequest.logIds"  id="logIds">
		</form> 
			<table width="100%" style="border-top:1px solid #95b8e7;">
		   		<tr height="25px">
		    		<td width="50%;" align="right">
		    			<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="saveReport();"  >分析</a>
		    		</td>
		    		<td width="50%;">
		    			<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="cleanAll();" >清空</a>
		    		</td>
		   		</tr>
		   	</table>
	 </div>
	 </div>
	<div data-options="region:'east',title:'已测试楼宇',split:false"  style="width:20%;">
		<table  id="floorTab"class="easyui-datagrid">
		</table>
	</div>
	
	<div data-options="region:'center',split:false,border:false">
		<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default4.html?toolbarType=28" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
	</div>
	
  </body>
</html>
