<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>4&5G互操作异常事件</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <%@ include file="../../../taglibs/jquery.jsp" %>
    <%@ include file="../../../taglibs/easyui.jsp" %>
    <%@ include file="../../../gis/BoshuAnalyse.jsp" %>

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
    </style>
    <script type="text/javascript">
        // field用于匹配远程json属性，width宽度,align居左右中对齐
        var testLogInfoColumns=[[
	    		{field:'itemid',align:'center',colspan:6,title:'异常事件基础信息'},
	    		{field:'Becalse',align:'center',colspan:2,title:'原因分析'},
	    		{field:'lteCell',align:'center',colspan:9,title:'服务小区信息'},
			],[
				/*异常事件基础信息*/
	            {field:'id',width:40,align:'center',title:'序号',hidden:true}, // 一个对象代表一列 <th>
	            {field:'city',width:150,align:'center',title:'城市',sortable:true},
	            {field:'scellChangeReqStartTime',width:100,align:'center',title:'互操作请求发起时间',sortable:true},
	            {field:'scellChangeFailTime',width:100,align:'center',title:'互操作失败发生时间',sortable:true},
	            {field:'scellChangeFailTimeLong',width:150,align:'center',title:'失败发生时间时间戳',sortable:true,hidden:true},
	            {field:'fileName',width:60,align:'center',title:'文件名称',sortable:true,
	                formatter:function(value,row,index){
	                    if(row.fileName){
	                    	var filename = value.substring(value.lastIndexOf("/")+1,value.length );
	                        return '<a title="" href="${pageContext.request.contextPath}/samoveexceptionview/downloadFile.action?fileId='+row.id+'&reqSeqNo='+row.testLogItem.recSeqNo+'" >'+filename+'</a>';
	                    }
	                    return value;
	                }
	            },
	            //{field:'roadName',width:100,align:'center',title:'道路名称',sortable:true,hidden:true},
	            {field:'longitude',width:60,align:'center',title:'经度',sortable:true},
	            {field:'dimension',width:60,align:'center',title:'纬度',sortable:true},
	            /*原因分析*/
	            {field:'scellChangeFailType',width:60,align:'center',title:'互操作失败类型',sortable:true},
	            {field:'wirelessFailReason',width:100,align:'center',title:'无线原因分析',sortable:true,id:"wirelessFailReason_val"},
	            /*服务小区信息*/
	            {field:'cellTac',width:100,align:'center',title:'小区TAC',sortable:true},
	            {field:'cellId',width:100,align:'center',title:'小区CELLID',sortable:true},
	            {field:'cellFriendlyName',width:100,align:'center',title:'小区小区友好名',sortable:true},
	            {field:'cellFrequencyPoint',width:60,align:'center',title:'小区频点',sortable:true},
	            {field:'cellPci',width:90,align:'center',title:'小区PCI',sortable:true},
	            {field:'cellSSRsrp',width:100,align:'center',title:'小区ss-rsrp',sortable:true},
	            {field:'cellDLIbler',width:100,align:'center',title:'小区DL_ibler',sortable:true}
	        ]];

        $(function(){
            initTable();
            var t1 = window.setInterval(function(){
	       		if(mapIframe.window.map){
	       			mapIframe.window.hideDivByDivId('BoshuAnalyseLi');
	       			var tab_option = $('#layerManagerTab').tabs('getTab',"事件").panel('options').tab;  
    				tab_option.hide();  
	       		}
	       },1000);
        });

        function initTable(){
            $("#testLogInfoTable").datagrid({
                // 表头
                columns:testLogInfoColumns,
                url:'${pageContext.request.contextPath}/samoveexceptionview/doPageListJson.action?',
                title:'4&5G互操作异常事件列表表结构',
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
                	getGpsPointData(rowData.testLogItem.recSeqNo,rowData.scellChangeFailTimeLong,rowData.scellChangeFailType
                			,rowData.longitude,rowData.dimension);
                },
                onLoadSuccess:function(data){
                	gisEventShow(data.rows);
                },
            });

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
        function getGpsPointData(recSeqNo,timeLong,typeName,lon,lat){
        	$.ajax({
                type:"GET",
                url:"${pageContext.request.contextPath}/samoveexceptionview/getGpsPointData.action",
                data:{
                	'recSeqNo':recSeqNo,
                	'timeLong':timeLong
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
                	var eventData = [[{iconType:typeName,longitude:lon,latitude:lat}]];
                	var cellToCellList = [];
                	var cellId = null;
                	for(var i = 0;i < data.length;i++){
                		var oneCelltoCell = {};
                		oneCelltoCell.cellId = data.nrCellid;
                		oneCelltoCell.nbCellId = data.lteCellid;
                		if(data.nrCellid != cellId){
                			cellId = data.nrCellid;
	                		oneCelltoCell.top = i+1;
                		}
                		cellToCellList.push(oneCelltoCell);
                	}
                	mapIframe.window.LayerManageShowEventTrackRender(eventData);
                	mapIframe.window.tableClickFunc(data,lon,lat,tc);
                	mapIframe.window.nCellToCellLineData = cellToCellList;
                }
            });
        }
        
        var query = function(){
            var roadName = $("#roadName").val();

            $("#testLogInfoTable").datagrid({
                // 表头
                columns:testLogInfoColumns,
                url:'${pageContext.request.contextPath}/samoveexceptionview/doPageListJson.action?nrFriendlyName='+roadName,
                title:'4&5G互操作异常事件列表表结构',
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
                	getGpsPointData(rowData.testLogItem.recSeqNo,rowData.scellChangeFailTimeLong,rowData.scellChangeFailType
                			,rowData.longitude,rowData.dimension);
                }
            });

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
    		return testLogItemIds;
    	}
    	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
    	function getTLI2QBRGpsPointActionUrl(){
    		return "<%=basePath%>gisSql/queryEmbbCoverRoadsPoints.action?coverType=1";
    	}
    	
    	//存储质差路段id
    	var embbrid;
    	//gisCommon会调用获取质差路段ID
    	function getGpsPointQBRId(){
    		return 5;
    	}
    	//gisCommon会调用获取生成质差路段轨迹的url
    	function getQBRGpsPointActionUrl(){
    		return "<%=basePath%>gisSql/queryEmbbCoverRoadPoints.action";
    	}
    	
    	//gisCommon会调用获取质差路段ID
    	function getGpsPointCWBRId(){
    		return 5;
    	}
    	//gisCommon会调用获取小区与邻区连线的url
    	function CWBRCellToCellActionUrl(){
    		return "<%=basePath%>gisSql/queryCWBRCellToCell.action";
    	}

    	//gisCommon会调用获取对比测试日志ID
	 	function getGpsPointCompareTestLogItemIds(){
			return testLogItemIds;
		}
		//gisCommon会调用获取生成对比 测试日志轨迹的url
		function getCompareTestLogItemGpsPointActionUrl(){
			return "<%=basePath%>gisSql/queryCompareTestLogItemGpsPoint.action";
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
	  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=100"
        scrolling="auto" frameborder="0"
        style="width:100%;height:100%;border:2px;margin: 5px 5px 5px 5px;"></iframe>
    </div>

	<div id="ly" class="easyui-layout"  style="width:100%;height: 40%;padding:4px;top:4px;left:4px;">
	    <div  data-options="region:'center',border:false" style="width:100%;height: 75%;" >
	    	<!-- <div style="height:12%;width: 100%;">
		        <form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
		            <div class="inputDivShow" style="width:50%;">源小区友好名
		                <input id="roadName" name="roadName"  class="" data-options="required:true,editable:false" value=""/>
		                <a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
		            </div>
		        </form>
	    	</div> -->
	    	<div style="height:100%;width: 100%;">
		        <table id="testLogInfoTable"> </table>
	    	</div>
	    </div>
	</div>

</body>
</html>
