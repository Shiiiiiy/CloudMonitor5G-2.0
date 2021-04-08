<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>embb覆盖分析</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <%@ include file="../../../taglibs/jquery.jsp" %>
    <%@ include file="../../../taglibs/easyui.jsp" %>
    <%@ include file="../../../gis/layerManagerEmbb.jsp" %>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js"></script>

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
        
        .typeButton{
        	width: 80px;
        	height: 24px;
        	float: left;
        	border: solid 1px;
        	background-color:white;
        	line-height:24px;
        	cursor:pointer;
        }
        
    </style>
    <script type="text/javascript">
        // field用于匹配远程json属性，width宽度,align居左右中对齐
        var lteWeakCoverColumns=
        	[[
        		{field:'cellData',align:'center',colspan:7,title:'小区信息'},
        		{field:'cellTarget',align:'center',colspan:4,title:'小区指标'},
        		{field:'pointAnalyse',align:'center',colspan:6,title:'弱覆盖采样点分析'}
        	],[
        		{field:'id',width:40,align:'center',title:'序号',hidden:true}, // 一个对象代表一列 <th>
	            {field:'cellName',width:150,align:'center',title:'小区友好名',sortable:true},
	            {field:'regin',width:150,align:'center',title:'区域',sortable:true},
	            {field:'cellId',width:100,align:'center',title:'CELLID',sortable:true},
	            {field:'tac',width:100,align:'center',title:'TAC',sortable:true},
	            {field:'downtilt',width:100,align:'center',title:'天馈下倾角',sortable:true},
	            {field:'azimuth',width:100,align:'center',title:'天馈方位角',sortable:true},
	            {field:'stationDistance',width:75,align:'center',title:'站间距(米)',sortable:true},
	            {field:'rrsrpAverage',width:75,align:'center',title:'覆盖电平',sortable:true},
	            {field:'pointNumberSum',width:75,align:'center',title:'采样点数',sortable:true},
	            {field:'distanceSum',width:100,align:'center',title:'总里程(米)',sortable:true},
	            {field:'durationSum',width:100,align:'center',title:'测试时长(秒)',sortable:true},
	            {field:'pointRatio',width:150,align:'center',title:'弱覆盖采样点占比(%)',sortable:true},
	            {field:'pointToStationSpace',width:150,align:'center',title:'弱覆盖采样点到站点距离',sortable:true},
	            {field:'rsrpAverage',width:150,align:'center',title:'弱覆盖采样点RSRP均值(dBm)',sortable:true},
	            {field:'rsrpBestAverage',width:150,align:'center',title:'弱覆盖采样点最强邻区RSRP均值(dBm)',sortable:true,hidden:false},
	            {field:'sinrAverage',width:150,align:'center',title:'弱覆盖采样点SINR均值(dBm)',sortable:true}
           ]
        ];

        var netType = 1;
        var coverType = 1;

		var cellidStr = null;
        $(function(){
	       initTable();
	       getCellids();
	       $("#netTypeChose").combobox("setValue",netType);
	       $("#coverChose").combobox("setValue",coverType);
	       var t1 = window.setInterval(function(){
	       		if(mapIframe.window.map){
	       			mapIframe.window.hideDivByDivId('cellBoshuConditionButton');
	       		}
	       		if(cellidStr != null){
		       			mapIframe.window.cellids = cellidStr;
		       			cellidStr ==null;
		       			window.clearInterval(t1);
	       		}
	       },1000);
        });

        function initTable(){
            $("#testLogInfoTable").datagrid({
                // 表头
                columns: lteWeakCoverColumns,
                url:'${pageContext.request.contextPath}/embbCoverAnalyse/doPageListJson.action?netType=1&coverType=1&testLogItemIds='+testLogItemIds,
                title:'',
                fitColumns:false,
                //填满区域
                fit:true,
                //奇偶变色
                striped:true,
                singleSelect:true,
                // 行号
                rownumbers:true,
                pagination:true,
                remoteSort:false,
                pageSize:20,
                pageList:[10,20,50,100],
                scrollbarSize:18,
                onLoadSuccess:function(data){
                	
                	//gisEventShow(data.rows);
                },
                onDblClickRow:function(rowIndex, rowData){
                	getGpsPointData(rowData.cellName,testLogItemIds,netType);
                }
            });
        }
        
        function getCellids(){
        	$.ajax({
                type:"GET",
                url:"${pageContext.request.contextPath}/embbCoverAnalyse/getCellIds.action",
                data:{
                	'testLogItemIds':testLogItemIds,
                	'netType':netType,
                	'coverType':coverType
					},
                dataType:"json",//服务器响应的数据类型
                success:function(data){
                	cellidStr = data.cellids;
                	console.info("--------");
                	console.info(cellidStr);
                }
            });
        }
        
        
        /*制式点击事件*/
        function netClick(selectOption){
        	selectedLog();
        	netType = selectOption.value;
        	/* if(netType == 1){
        		$('#lteNet').css("background-color","#70AADA");
        		$('#lteNet').css("fontWeight","bolder");
        		$('#nrNet').css("background-color","white");
        		$('#nrNet').css("fontWeight","normal");
        	}else if(netType == 2){
        		$('#lteNet').css("background-color","white");
        		$('#lteNet').css("fontWeight","normal");
        		$('#nrNet').css("background-color","#70AADA");
        		$('#nrNet').css("fontWeight","bolder");
        	} */
        	changeColumn();
        }
        
        /*覆盖点击事件*/
        function coverClick(selectOption){
        	selectedLog();
        	
        	coverType = selectOption.value;
        	/* var id = null;
        	if(val == 1){
        		id = 'weakCover';
        	}else if(val == 2){
        		id = 'overCover';
        	}else if(val == 3){
        		id = 'overlapCover';
        	}else if(val == 4){
        		id = 'distantCover';
        	}else if(val == 5){
        		id = 'contrarilyCover';
        	}
        	for(var i = 0;i < $('div[name=coverChose]').length;i++){
        		if($('div[name=coverChose]')[i].id == id){
        			$('div[name=coverChose]')[i].style.backgroundColor = "#70AADA";
        			$('div[name=coverChose]')[i].style.fontWeight = "bolder";
        		}else{
        			$('div[name=coverChose]')[i].style.backgroundColor = "white";
        			$('div[name=coverChose]')[i].style.fontWeight = "normal";
        		}
        	} */
        	changeColumn();
        }
        
        function selectedLog(){
        	if(testLogItemIds == null || testLogItemIds == ''){
        		$.messager.alert("提示","请选择分析日志!",'warning');
        		return ;
        	}
        }
        
        /*修改列头*/
        function changeColumn(){
        	if(netType == 1 && coverType == 1){
        		lteWeakCoverColumns[0][1].colspan = 4;
        		if(lteWeakCoverColumns[1][8].title != '覆盖电平'&&lteWeakCoverColumns[1][8].title != '最强beam rsrp均值（dBm）'){
        			lteWeakCoverColumns[1].splice(8,0,{field:'rrsrpAverage',width:75,align:'center',title:'覆盖电平',sortable:true});
        		}else{
        			lteWeakCoverColumns[1].splice(8,1,{field:'rrsrpAverage',width:75,align:'center',title:'覆盖电平',sortable:true});
        		}
        	}else if(netType == 2 && coverType == 1){
        		lteWeakCoverColumns[0][1].colspan = 4;
        		if(lteWeakCoverColumns[1][8].title != '覆盖电平'&&lteWeakCoverColumns[1][8].title != '最强beam rsrp均值（dBm）'){
        			lteWeakCoverColumns[1].splice(8,0,{field:'rrsrpAverage',width:75,align:'center',title:'最强beam rsrp均值（dBm）',sortable:true});
        		}else{
        			lteWeakCoverColumns[1].splice(8,1,{field:'rrsrpAverage',width:75,align:'center',title:'最强beam rsrp均值（dBm）',sortable:true});
        		}
        	}else{
        		lteWeakCoverColumns[0][1].colspan = 3;
        		if(lteWeakCoverColumns[1][8].title == '覆盖电平'||lteWeakCoverColumns[1][8].title == '最强beam rsrp均值（dBm）'){
        			lteWeakCoverColumns[1].splice(8,2,{field:'pointNumberSum',width:75,align:'center',title:'采样点数',sortable:true});
        		}
        	}
        	var length = lteWeakCoverColumns[1].length;
        	var str = null;
        	if(coverType == 1){
        		lteWeakCoverColumns[0][2].title = '弱覆盖采样点分析';
        		str = '弱';
        	}else if(coverType == 2){
        		lteWeakCoverColumns[0][2].title = '过覆盖采样点分析';
        		str = '过';
        	}else if(coverType == 3){
        		lteWeakCoverColumns[0][2].title = '重叠覆盖采样点分析';
        		str = '重叠';
        	}else if(coverType == 4){
        		lteWeakCoverColumns[0][2].title = '超远覆盖采样点分析';
        		str = '超远';
        	}else if(coverType == 5){
        		lteWeakCoverColumns[0][2].title = '反向覆盖采样点分析';
        		str = '反向';
        	}
        	lteWeakCoverColumns[1][length - 5].title = str + '覆盖采样点占比(%)';
        	if(coverType != 5){
        		lteWeakCoverColumns[1][length - 4].title = str + '覆盖采样点到站点距离';
        	}else{
        		lteWeakCoverColumns[1][length - 4].title = str + '覆盖采样点到站点角度';
        	}
        	lteWeakCoverColumns[1][length - 3].title = str + '覆盖采样点RSRP均值(dBm)';
        	lteWeakCoverColumns[1][length - 2].title = str + '覆盖采样点最强邻区RSRP均值(dBm)';
        	
        	if(netType == 2){
        		lteWeakCoverColumns[1][length - 1].title = str + '覆盖采样点ibler均值(%)';
        		lteWeakCoverColumns[1][length - 1].field = 'iblerAverage';
        	}else if(netType == 1){
	        	lteWeakCoverColumns[1][length - 1].title = str + '覆盖采样点SINR均值(dBm)';
	        	lteWeakCoverColumns[1][length - 1].field = 'sinrAverage';
        	}
        	
        	if(coverType == 3){
        		lteWeakCoverColumns[1][length - 2].hidden = true;
        	}else{
        		lteWeakCoverColumns[1][length - 2].hidden = false;
        	}
        	
        	
        	$("#cellNameQuery").textbox('setValue','');
        	getCellids();
        	query();
        	var t2 = window.setInterval(function(){
        	 	console.info(cellidStr);
	       		if(cellidStr != null){
        			mapIframe.window.cellBorderToColor(cellidStr);
        			mapIframe.window.cellids = cellidStr;
        			cellidStr = null;
        			window.clearInterval(t2);
	       		}
	       },1000);
        }
        
        function gisEventShow(data){
        	var d = [[]];
        	for(var i = 0;i < data.length;i++){
	        	var c= {};
        		c.iconType = data[i].scellChangeFailType;
        		c.longitude = data[i].longitude;
        		c.latitude = data[i].dimension;
        		d[0].push(c);
        	}
        	var t1 = window.setInterval(function(){
        		if(mapIframe.window.map){
		        	mapIframe.window.LayerManageShowEventTrackRender(d);
		        	if(d[0].length > 0){
			        	mapIframe.window.mapCenterAndZoom(d[0][0].longitude,d[0][0].latitude);
		        	}
		        	window.clearInterval(t1);
        		}
        	},1000);
        }
        
        /*获取采样点经纬度*/
        function getGpsPointData(cellNameParam,testLogItemIds,netType){
        	$.ajax({
                type:"GET",
                url:"${pageContext.request.contextPath}/embbCoverAnalyse/getGpsPointData.action",
                data:{
                	'cellNameParam':cellNameParam,
                	'testLogItemIds':testLogItemIds,
                	'netType':netType
					},
                dataType:"json",//服务器响应的数据类型
                success:function(data){
                	var tc;
                	if(!$('#targetChoose').combobox('getValue')){
                		tc = 109;
                	}else{
                		tc = $('#targetChoose').combobox('getValue');
                	}
               		if(tc == 109){
		    			tc = 'nrRsrp';
			    	}else if(tc == 110){
			    		tc = 'lteRsrp';
			    	}else if(tc == 111){
			    		tc = 'nrSinr';
			    	}else if(tc == 112){
			    		tc = 'lteSinr';
			    	}else if(tc == 102){
			    		tc = 'beamSum';
			    	}
			    	console.info(tc);
                	
                	mapIframe.window.stationEmbbEtgTral(data.pointList,tc);
                	var t1 = window.setInterval(function(){
		        		if(data.longitude!=null && data.longitude>0){
                			mapIframe.window.mapCenterAndZoom(data.longitude,data.latitude);
                			window.clearInterval(t1);
                		}
		        	},1000);
                	/* var eventData = [[{iconType:typeName,longitude:lon,latitude:lat}]];
                	var cellToCellList = [];
                	var cellId = null;
                	for(var i = 0;i < data.length;i++){
                		var oneCelltoCell = {};
                		oneCelltoCell.cellId = data.nrCellId;
                		oneCelltoCell.nbCellId = data.lteCellId;
                		if(data.nrCellId != cellId){
                			cellId = data.nrCellId;
	                		oneCelltoCell.top = i+1;
                		}
                		cellToCellList.push;
                	}
                	mapIframe.window.LayerManageShowEventTrackRender(eventData);
                	mapIframe.window.tableClickFunc(data,lon,lat);
                	mapIframe.window.nCellToCellLineData = cellToCellList; */
                }
            });
        }


        var query = function(){
            var cellNameQuery = $("#cellNameQuery").val();

            $("#testLogInfoTable").datagrid({
                // 表头
                columns: lteWeakCoverColumns,
                url:'${pageContext.request.contextPath}/embbCoverAnalyse/doPageListJson.action?netType='+ netType + 
                		'&coverType=' + coverType +'&cellName=' + cellNameQuery + '&testLogItemIds=' + testLogItemIds,
                title:'',
                fitColumns:false,
                //填满区域
                fit:true,
                //奇偶变色
                striped:true,
                singleSelect:true,
                // 行号
                rownumbers:true,
                pagination:true,
                pageSize:20,
                pageList:[10,20,50,100],
                scrollbarSize:18,
                onDblClickRow:function(rowIndex, rowData){
                	getGpsPointData(rowData.cellName,testLogItemIds,netType);
                }
            });
        }
        
        function downloadData(){
        	var cellNameQuery = $("#cellNameQuery").val();
        	var currPageNumber = $("#testLogInfoTable" ).datagrid("getPager" ).data("pagination" ).options.pageNumber;
        	var currPageSize = $("#testLogInfoTable" ).datagrid("getPager" ).data("pagination" ).options.pageSize;
        	window.location= "${pageContext.request.contextPath}/embbCoverAnalyse/downloadData.action?netType="+ netType + '&coverType=' + coverType 
        			+ '&cellName=' + cellNameQuery + '&testLogItemIds=' + testLogItemIds + '&rows=' + currPageSize + '&page=' + currPageNumber;
        }
        
      	//存储日志id,按","分隔
    	var testLogItemIds = '${testLogItemIds}';
    
    	//gisCommon会调用获取测试日志ID
    	function getCellTestLogItemIds(){
    		if(testLogItemIds == null || testLogItemIds == ''){
    			$.messager.alert("提示","请选择分析日志!",'warning');
    		}
    		return testLogItemIds;
    	}
    	//gisCommon会调用获取生成小区SQL的url
    	function getCellActionUrl(){
    		return "<%=basePath%>gisSql/queryCellInfo.action";
    	}
    	
    	//gisCommon会调用获取测试日志ID
    	function getTestLogItemIds2QBR(){
    		return -1;
    	}
    	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
    	function getTLI2QBRGpsPointActionUrl(){
    		return "<%=basePath%>gisSql/queryEmbbCoverRoadsPoints.action?coverType=1";
    	}
    	
    	//存储质差路段id
    	var embbrid;
    	//gisCommon会调用获取质差路段ID
    	function getGpsPointQBRId(){
    		return testLogItemIds;
    	}
    	//gisCommon会调用获取生成质差路段轨迹的url
    	function getQBRGpsPointActionUrl(){
    		return "<%=basePath%>gisSql/queryEmbbCoverRoadPoints.action";
    	}
    	
    	//gisCommon会调用获取对比测试日志ID
		function getGpsPointCompareTestLogItemIds(){
			return testLogItemIds;
		}
		//gisCommon会调用获取生成对比 测试日志轨迹的url
		function getCompareTestLogItemGpsPointActionUrl(){
			return "<%=basePath%>gisSql/queryCompareTestLogItemGpsPoint.action";
		}
    	
    	//gisCommon会调用获取质差路段ID
    	function getGpsPointCWBRId(){
    		return testLogItemIds;
    	}
    	//gisCommon会调用获取小区与邻区连线的url
    	function CWBRCellToCellActionUrl(){
    		return "<%=basePath%>gisSql/queryCompareTestLogItemGpsPoint.action";
    	}
    	
    	function getEmbbCoverBoshuAnalyseLogItemIds(){
    		return testLogItemIds;
    	}
    	
    	function getEmbbCoverBoshuAnalyseUrl(){
    		return "${pageContext.request.contextPath}/embbCoverAnalyse/getGpsPointDis.action";
    	}
    	
    	//获取小区的最近8个邻区
    	function getNcellNameActionUrl(){
    		return "<%=basePath%>gisSql/getNcellNamesByCellName.action";
    	}

    </script>

</head>

<body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">

	<!--地图界面 -->
	<div style="width:100%;height:60%;border:2px;">
	  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=101"
        scrolling="auto" frameborder="0"
        style="width:100%;height:100%;border:2px;margin: 5px 5px 5px 5px;"></iframe>
    </div>

	<div id="ly" class="easyui-layout"  style="width:100%;height: 40%;padding:4px;top:4px;left:4px;">
	    <div  data-options="region:'center',border:false" style="width:100%;height: 75%;" >
	    	<div style="height:12%;width: 100%;">
		        <form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
		            <div style="width:30%;text-align: center;float: left;">
		                <input id="cellNameQuery" name="cellNameQuery"  class="easyui-textbox" data-options="prompt:'请输入小区名称'" value=""/>
		                <a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
		            </div>
		            <div style="width:20%;text-align: center;float: left;">
		            	制式：
		            	<select id="netTypeChose" name="netTypeChose"  class="easyui-combobox" style="width:100px;" data-options="required:true,editable:false,onSelect:netClick" >
								<option value="1" selected="selected">LTE</option>
								<option value="2">NR</option>
						</select>
		            	<!-- <div style="background-color: #70AADA;font-weight:bolder;" id="lteNet" class="typeButton" onclick="netClick(1)">LTE</div>
		            	<div style="" class="typeButton" id="nrNet" onclick="netClick(2)">NR</div> -->
		            </div>
		            <div style="width:30%;text-align: center;float: left;">
		            	覆盖问题类型：
		            	<select id="coverChose" name="coverChose"  class="easyui-combobox" style="width:100px;" data-options="required:true,editable:false,onSelect:coverClick" >
								<option value="1" selected="selected">弱覆盖</option>
								<option value="2">过覆盖</option>
								<option value="3">重叠覆盖</option>
								<option value="4">超远覆盖</option>
								<option value="5">反向覆盖</option>
						</select>
		            	<!-- <div name='coverChose' style="background-color: #70AADA;font-weight: bolder;" id="weakCover" class="typeButton" onclick="coverClick(1)">弱覆盖</div>
		            	<div name='coverChose' style="" class="typeButton" id="overCover" onclick="coverClick(2)">过覆盖</div>
		            	<div name='coverChose' style="" class="typeButton" id="overlapCover" onclick="coverClick(3)">重叠覆盖</div>
		            	<div name='coverChose' style="" class="typeButton" id="distantCover" onclick="coverClick(4)">超远覆盖</div>
		            	<div name='coverChose' style="" class="typeButton" id="contrarilyCover" onclick="coverClick(5)">反向覆盖</div> -->
		            </div>
		            <div style="width:20%;text-align: center;float: left;">
		                <a class="easyui-linkbutton" onclick="downloadData();" data-options="iconCls:'icon-download'" >导出</a>
		            </div>
		        </form>
	    	</div>
	    	<div style="height:88%;width: 100%;">
		        <table id="testLogInfoTable"> </table>
	    	</div>
	    </div>
	</div>

</body>
</html>
