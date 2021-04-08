<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>地图监控</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript">
	//gisCommon会调用获取终端boxID
	function getPointTerminalIds(){
		return boxIDArray;
	}
	//gisCommon会调用获取实时地图监控实时刷新轨迹的url
	function getTerminalGpsPointActionUrl(){
		return "<%=basePath%>mapMonitor/queryGPSRefreshRequestParam.action";
	}
	//gisCommon会调用获取历史事件终端boxID
	function getHistoryGpsPointTerminalIds(){
		var acd=addMap.window.getGpsPointTerminalIds();
		return acd;
	}
	//gisCommon会调用获取历史事件轨迹的url
	function getEventHistoryGpsPointActionUrl(){
		return "<%=basePath%>mapMonitor/queryGPSEventRequestParam.action";
	}
	//gisCommon会调用获取实时事件轨迹的url
	function getAtmomentEventGpsPointActionUrl(){
		return "<%=basePath%>mapMonitor/queryGPSRefreshEventRequestParam.action";
	}
	var boxIDArray=new Array();
	$(function(){
		queryTerminal();
	});
	/*查询在线终端*/
	function queryTerminal(){
		$.post("${pageContext.request.contextPath}/mapMonitor/allTerminalNumber.action",
				function(result){
			$('#nn').numberbox('setValue', Number(result));
				}
			,"json");
	}
		/* 刷新域 */
		function reloadArea(){
			queryTerminal();
			$("#areaTree").tree('reload');
		}
		       
								
		/* 当用户选中实时面板时*/
		function pitch(实时监控,index){
			reloadArea();
			if(index==1){
			$('#dd').layout("remove","west"); 
			
			mapIframe.window.cleanMap(0);
					  }
			if(index==2){
				addTable();
			}
		}
		
		/*打开历史轨迹页*/
		function addTable(){
			mapIframe.window.cleanMap(1);
			$('#dd').layout('add',{
				name:'addMap',
		        region: 'west',
		        width: '50%',
		        //createIframe("${pageContext.request.contextPath}/gis/mapAdd.jsp"),
		        content : '<iframe name="addMap" scrolling="auto" frameborder="0" style="width:100%;height:100%;border:0;margin: 0;" src="${pageContext.request.contextPath}/gis/mapAdd.jsp"></iframe>',
		        title: '历史轨迹',
		        split: false
		        /*tools: [{
		    		iconCls:'icon-remove',
		    		handler:function(){
		    			reloadMap();
		    			$('#dd').layout("remove","west");
		    		}
		        }]*/
		    });
		}
		//用户点击显示轨迹时触发
		    function showTrack(){
		    	var nodes = $("#areaTree").tree('getChecked');
		    	if(nodes==null||nodes.length==0){
		    		alert("未选择在线终端");
		    	}else{
		    		boxIDArray=[];
		    	for(var i=0;i<nodes.length;i++){
		    		boxIDArray.push(nodes[i].attributes.boxID);
		    	}
		    	$.post("${pageContext.request.contextPath}/mapMonitor/queryTerminalGpsPoint.action?boxIDs="+boxIDArray+"",
						function(result){
		    		
		    		mapIframe.window.drawTerminal(result);
						
		    	}
					,"json");
		    	}
    	}
			/*  数据加载完成时筛选在线终端复选框可选中*/
			  function judge(treeNode,nodeData){
				var provinceNodes = $(this).tree('getChildren',nodeData[0].target);
				for(var j=0;j<provinceNodes.length;j++){
					var cityNodes = $(this).tree('getChildren',provinceNodes[j].target);
					for(var i=0;i<cityNodes.length;i++){
						//alert(cityNodes[i].state);
						//cityNodes[i].state=closed;
						//alert(cityNodes[i].state);
						var terminalNodes = $(this).tree('getChildren',cityNodes[i].target);
						for(var ii=0;ii<terminalNodes.length;ii++){
							if(terminalNodes[ii].attributes&&'Terminal'==terminalNodes[ii].attributes.type&&!terminalNodes[ii].attributes.online){
								//$(terminalNodes[ii].target).find('span.tree-checkbox').css('background-color','red');
								$(terminalNodes[ii].target).find('span.tree-checkbox').unbind().click(function(){	  
									   return false;
						    	});	
							}
						}
					}
				}
			  }
			/*判断只能选两个终端*/
			function danji(node, checked){
				
				var nodes = $("#areaTree").tree('getChecked');
		    	if(nodes.length>=2){
		    		if(!checked){
		    			return true;
		    		}
		    		$.messager.alert('警告','最多只能选取两个终端','warning');
		    		return false;
		    	}else return true;
			}
	//刷新地图
			function reloadMap(i){
				mapIframe.window.cleanMap(i);
			}
	</script>
  </head>
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	<div data-options="region:'west',title:'监控管理',split:false,tools:'#tt3'"  style="width:20%;">
		<div class="easyui-accordion" data-options="onSelect:pitch,border:false" style="width:100%;height:100%;" id="planId">
			<div id="number" title="在线用户数" >
				<p style="margin:0;padding:10px;">当前在线用户个数为:</p><input id="nn" type="text" class="easyui-numberbox" data-options="min:0,precision:0">
			</div>
			<div title="实时监控" style="overflow:auto;" >
				<ul id="areaTree" name="boxIds" class="easyui-tree" checkbox="true" onlyLeafCheck="true" data-options="onLoadSuccess:judge,onBeforeCheck:danji,url:'${pageContext.request.contextPath}/mapMonitor/terminalGroupTree.action',lines:true,formatter:function(node){
					if(node.attributes&&node.attributes.online){
						return '<font color=green><strong>'+node.text+'</strong></font>';
					}
					return node.text;
				}"></ul>
			</div>
			<div title="历史查询" ></div>
		</div>
	</div>
	<div data-options="region:'center',split:false,border:false"style="width:80%;height:100%;">
			<%-- <div  data-options="region:'west',title:'历史轨迹',tools:'#dd3'" style="width:40%;height:100%;" >
		  			<iframe name="queryIframe" src="${pageContext.request.contextPath}/gis/mapAdd.jsp" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
		  		</div> --%>
			<!-- 东,地图 -->
		<div id="dd" class="easyui-layout" style="width:100%;height: 100%;" >
			<div data-options="region:'center',split:false,border:false">
				<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default3.html?toolbarType=27" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
			</div>
		</div>
	</div>
   	<div id="tt3">
		<a class="icon-reload" title="刷新终端" onclick="reloadArea()"></a>
		<a class="icon-search" title="显示已选终端实时轨迹" onclick="showTrack()"></a>
	</div>
	
  </body>
</html>
