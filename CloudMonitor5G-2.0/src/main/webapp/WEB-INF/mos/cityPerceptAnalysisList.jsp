<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%;margin: 0 auto;padding: 0 auto;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>城市维度感知数据分析</title>
<%@ include file="../../taglibs/jquery.jsp" %>
<%@ include file="../../taglibs/easyui.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
<style type="text/css">
	.inputDivShow{ 
		display: inline-block;*
		display: inline;
	    font-size: 12px;
	    margin: 10px;
	    padding-left: 0;
	    padding-right: 0;
	    text-align: right;
	    width: 200px;
  		}
  		.inputDivShow input{
  			width:100px;
  		}
  		.inputDivShow select{
  			width:100px;
  		}
       .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber {
           text-overflow: ellipsis;
       }
</style>

<script type="text/javascript">
	var dataColumns=
        	[[
				{field:'province',width:80,align:'center',title:'省',hidden:false},
				{field:'city',width:80,align:'center',title:'市',hidden:false},
				{field:'district',width:80,align:'center',title:'区',hidden:true},
				{field:'nrRsrp',width:80,align:'center',title:'NR RSRP'},
				{field:'nrSinr',width:80,align:'center',title:'NR SINR'},
				{field:'lteNetwork',width:80,align:'center',title:'LTE信号强度'},
				{field:'sinr',width:60,align:'center',title:'LTE SINR'},
				{field:'pingDelay',width:60,align:'center',title:'PING时延(ms)'},
				{field:'upSpeedAvg',width:90,align:'center',title:'上传平均速度(Mbps)',
					formatter:function(value,row,index){
						var rlt = value/1000*8; 
						return rlt.toFixed(1);
					}
				},
				{field:'downSpeedAvg',width:90,align:'center',title:'下载平均速度(Mbps)',
					formatter:function(value,row,index){
						var rlt = value/1000*8;
						return rlt.toFixed(1);
					}
				},
				{field:'vmos',width:60,align:'center',title:'VMOS值'},
				{field:'sQuality',width:80,align:'center',title:'视频质量分'},
				{field:'sLoading',width:100,align:'center',title:'视频始缓冲分'},
				{field:'sStalling',width:80,align:'center',title:'视频卡顿分'},
				{field:'httpDlRate',width:80,align:'center',title:'HTTP下载速率(Mbps)'},
				{field:'httpTimeDelay',width:80,align:'center',title:'HTTP浏览时延(ms)'},
				/*{field:'phoneNUmber',width:80,align:'center',title:'手机号'},
				{field:'friendRate',width:80,align:'center',title:'朋友圈打开速率'}, */
				
			]];

	$(function(){
		init();
	});
	
	
	var cityAll; 
	var statisticType;
	
	function init(){
		statisticType = "1";
		//初始化开始时间和结束时间
		var nowDate = new Date();
		var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");	
		$("#endDate").datetimebox('setValue',dateString);
		$('#beginDate').datebox('calendar').calendar({
			validator: function(date){
				var endDate = $.fn.datebox.defaults.parser($("#endDate").datebox('getValue'));
				return date<=endDate;
			}
		});
		nowDate.setDate(nowDate.getDate()-7);
		dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss"); 
		$("#beginDate").datetimebox('setValue',dateString);
		$('#endDate').datebox('calendar').calendar({
			validator: function(date){
				var startDate = $.fn.datebox.defaults.parser($("#beginDate").datebox('getValue'));
				return date>=startDate;
			}
		});
		
		//初始化省
		$.post('${pageContext.request.contextPath}/cityPerceptAnalysis/getInitProvince.action', {},
				function(result) {
					//初始化省选项
					initSelect(result.tlList,result.provinceStr,'provinceQuery');
					cityAll = result.regionMap;
					//初始化市选项
					var data = [];
					for(var i=0;i<result.tlList.length;i++){
						var province = result.tlList[i].name;
						var citys = result.regionMap[province];
						for(var j=0;j<citys.length;j++){
							var oneUp = {};
							oneUp.id = citys[j];
							oneUp.name = citys[j];
							oneUp.selected = true;
							data.push(oneUp);
						}
					}
					$("#cityQuery").combobox({
						valueField: 'id', 
						textField: 'name',
						multiple:true, //多选
						data: data,
						filter: function(q, row){
							var opts = $(this).combobox('options');
							return row[opts.textField].toLowerCase().indexOf(q.toLowerCase())>-1;
						}
				    });
				    initTable();
				}
		,'json');
	}
	
	function initTable(){
		var citys = $("#cityQuery").combobox('getValues').join(",");
		$("#mainTable").datagrid({
			// 表头
			columns:dataColumns,
			url:'${pageContext.request.contextPath}/cityPerceptAnalysis/doPageListJson?citys='+citys+'&statisticType=1',
			// 分页条
			pagination:true,
			title:'统计结果',
			pageSize:20,
			pageList:[20,50,100,200,500,1000],
			//填满区域
			fit:true,
			fitColumns:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:true,
			// 工具栏 【表格上方功能按钮】
			toolbar:'#tt3' 
		});
	}
	
	/*填充站点名称下拉框*/
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
			multiple:true, //多选
			data: data,
			filter: function(q, row){
				var opts = $(this).combobox('options');
				return row[opts.textField].toLowerCase().indexOf(q.toLowerCase())>-1;
			}
	    }); 
	}
	
	function provinceSelect(record){
		var rows = $('#cityQuery').combobox('getData');
		var cityArry = cityAll[record.name];
		for(var k=0; k < cityArry.length; k++){
			var oneUp = {};
			oneUp.id = cityArry[k];
			oneUp.name = cityArry[k];
			oneUp.selected = true;
			rows.push(oneUp);
		}
        $('#cityQuery').combobox('loadData', rows).combobox('setValues',[]); 
	}
	
	function provinceUnSelect(record){
		var rows = $('#cityQuery').combobox('getData');
		var cityArry = cityAll[record.name];
		for(var k=0; k< cityArry.length; k++){
			for (var i = 0; i < rows.length; i++){
	        	if (rows[i].name == cityArry[k]) {
	        		rows.splice(i, 1); 
	        	}
	        }
		}
        $('#cityQuery').combobox('loadData', rows).combobox('setValues',[]); 

	}
	
	function loadData(){
		if($("#queryForm").form('validate')){
			
			var beginDate = $("#beginDate").datetimebox('getValue');
			var endDate = $("#endDate").datetimebox('getValue');
			var provinces = $("#provinceQuery").combobox('getValues').join(",");
			var citys = $("#cityQuery").combobox('getValues').join(",");
			
			$("#mainTable").datagrid({
				// 表头
				columns:dataColumns,
				url:'${pageContext.request.contextPath}/cityPerceptAnalysis/doPageListJson?beginDate='+beginDate+'&endDate='+endDate+
							'&provinces='+provinces+'&citys='+citys+'&statisticType='+statisticType,
				// 分页条
				pagination:true,
				title:'统计结果',
				pageSize:20,
				pageList:[20,50,100,200,500,1000],
				//填满区域
				fit:true,
				fitColumns:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				// 工具栏 【表格上方功能按钮】
				toolbar:'#tt3' 
			});
			
			//查询排名
			$('#queryForm').form('submit', {
			    url:'${pageContext.request.contextPath}/cityPerceptAnalysis/queryCityRankData.action',
			    onSubmit: function(param){
			    	param.statisticType = statisticType;
			    },
			    success:function(data){
				    data = eval('(' + data + ')');
				    if(data.videoRank["TOP1"]!=null){
				    	$("#videoScoreFirst").html(data.videoRank["TOP1"]); 
				    }else{
				    	$("#videoScoreFirst").html(""); 
				    }
				    if(data.videoRank["TOP2"]!=null){
				    	$("#videoScoreSecond").html(data.videoRank["TOP2"]); 
				    }else{
				    	$("#videoScoreSecond").html(""); 
				    }
				    if(data.videoRank["TOP3"]!=null){
				    	$("#videoScoreThird").html(data.videoRank["TOP3"]); 
				    }else{
				    	$("#videoScoreThird").html(""); 
				    }
				    if(data.videoRank["TOP4"]!=null){
					    if(data.videoRank["TOP-1"]!=null){
					    	$("#videoScoreLastFirst").html(data.videoRank["TOP-1"]); 
					    }else{
					    	$("#videoScoreLastFirst").html(""); 
					    }
					    if(data.videoRank["TOP-2"]!=null){
					    	$("#videoScoreLastSecond").html(data.videoRank["TOP-2"]); 
					    }else{
					    	$("#videoScoreLastSecond").html(""); 
					    }
					    if(data.videoRank["TOP-3"]!=null){
					    	$("#videoScoreLastThird").html(data.videoRank["TOP-3"]); 
					    }else{
					    	$("#videoScoreLastThird").html(""); 
					    }
				    }else{
				    	$("#videoScoreLastFirst").html(""); 
				    	$("#videoScoreLastSecond").html("");
				    	$("#videoScoreLastThird").html(""); 
				    }
				    
				    
				    if(data.dataRank["TOP1"]!=null){
				    	$("#dataScoreFirst").html(data.dataRank["TOP1"]); 
				    }else{
				    	$("#dataScoreFirst").html(""); 
				    }
				    if(data.dataRank["TOP2"]!=null){
				    	$("#dataScoreSecond").html(data.dataRank["TOP2"]); 
				    }else{
				    	$("#dataScoreSecond").html(""); 
				    }
				    if(data.dataRank["TOP3"]!=null){
				    	$("#dataScoreThird").html(data.dataRank["TOP3"]); 
				    }else{
				    	$("#dataScoreThird").html(""); 
				    }
				    
				    if(data.videoRank["TOP4"]!=null){
				    	if(data.dataRank["TOP-1"]!=null){
					    	$("#dataScoreLastFirst").html(data.dataRank["TOP-1"]); 
					    }else{
					    	$("#dataScoreLastFirst").html(""); 
					    }
					    if(data.dataRank["TOP-2"]!=null){
					    	$("#dataScoreLastSecond").html(data.dataRank["TOP-2"]); 
					    }else{
					    	$("#dataScoreLastSecond").html(""); 
					    }
					    if(data.dataRank["TOP-3"]!=null){
					    	$("#dataScoreLastThird").html(data.dataRank["TOP-3"]); 
					    }else{
					    	$("#dataScoreLastThird").html(""); 
					    }
				    }else{
				    	$("#dataScoreLastFirst").html(""); 
				    	$("#dataScoreLastSecond").html("");
				    	$("#dataScoreLastThird").html("");  
				    }
			    }
			});
		}
	}
	
	function analysis(type){ 
		statisticType = type;
		if(statisticType==0){
			dataColumns[0][0].hidden = false;
			dataColumns[0][1].hidden = true;
			dataColumns[0][2].hidden = true;
		}else if(statisticType==1){
			dataColumns[0][0].hidden = false;
			dataColumns[0][1].hidden = false;
			dataColumns[0][2].hidden = true;
		}else if(statisticType==2){
			dataColumns[0][0].hidden = false;
			dataColumns[0][1].hidden = false;
			dataColumns[0][2].hidden = false; 
		}
		loadData();
	}
	
	function queryFormRest(){
		$('#queryForm').form('reset');
		$('#queryForm').form('clear');
	}

</script>
</head>
<body  class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
	<div data-options="region:'north',title:'分析条件设置',collapsible:false," style="overflow-y:auto;height:13%;"> 
    	<form method="post" style="margin-bottom: 5px;" id="queryForm" >
    		<div class="inputDivShow" >省：
    			<input id="provinceQuery" class="easyui-combobox" name="provinces" data-options="editable:false,onSelect:provinceSelect,onUnselect:provinceUnSelect" style="width:130px"/>
	    	
	    	</div>
    	
    		<div class="inputDivShow" >市：
	    		<input id="cityQuery" class="easyui-combotree" name="citys" data-options="panelWidth:200,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:false" style="width:130px"/>
	    	</div>
 
    		<div class="inputDivShow" style="width:250px">
	    		开始时间：
	       		<input  style="width:150px" data-options="required:true,editable:false"  class="easyui-datetimebox" name="beginDate"  id="beginDate">
    		</div>
    		<div class="inputDivShow" style="width:250px">
	    		结束时间：
	       		<input style="width:150px" data-options="required:true,editable:false" class="easyui-datetimebox" name="endDate" id="endDate">
    		</div>
    		
    		<td width="49%" style="padding-right:10px;" align="right"><a class="easyui-linkbutton" style="width:80px" onclick="loadData();" data-options="iconCls:'icon-search'">分析</a></td>
		    <td width="49%" style="padding-left:10px;" align="left"><a class="easyui-linkbutton" style="width:80px" onclick="queryFormRest();" data-options="iconCls:'icon-reload'">重置</a></td>
    	</form>
    </div>
    
   <div data-options="region:'west',collapsible:false,title:'视频业务打分排名'" style="width:50%;height:20%;overflow-y:auto;">   
   		<div style="width:100%;">
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #ADD597;line-height: 30px;text-align: center;">
	                <strong>TOP1</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #CDE2C3;line-height: 30px;text-align: center;">
	                <strong id="videoScoreFirst"></strong>
	            </div>
	        </div>
	        
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #E78081;line-height: 30px;text-align: center;">
	                <strong>-TOP1</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #F1D1D2;line-height: 30px;text-align: center;">
	                <strong id="videoScoreLastFirst"></strong>
	            </div>
	        </div>
	    </div>
	    
	    <div style="width:100%;">
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #ADD597;line-height: 30px;text-align: center;">
	                <strong>TOP2</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #CDE2C3;line-height: 30px;text-align: center;">
	                <strong id="videoScoreSecond"></strong>
	            </div>
	        </div>
	        
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #E78081;line-height: 30px;text-align: center;">
	                <strong>-TOP2</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #F1D1D2;line-height: 30px;text-align: center;">
	                <strong id="videoScoreLastSecond"></strong>
	            </div>
	        </div>
	    </div>
	    
	    <div style="width:100%;">
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #ADD597;line-height: 30px;text-align: center;">
	                <strong>TOP3</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #CDE2C3;line-height: 30px;text-align: center;">
	                <strong id="videoScoreThird"></strong>
	            </div>
	        </div>
	        
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #E78081;line-height: 30px;text-align: center;">
	                <strong>-TOP3</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #F1D1D2;line-height: 30px;text-align: center;">
	                <strong id="videoScoreLastThird"></strong>
	            </div>
	        </div>
	    </div>
   
    </div>
    
    <div data-options="region:'east',collapsible:false,title:'数据业务打分排名'" style="width:50%;height:20%;overflow-y:auto;"> 
       	<div style="width:100%;">
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #ADD597;line-height: 30px;text-align: center;">
	                <strong>TOP1</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #CDE2C3;line-height: 30px;text-align: center;">
	                <strong id="dataScoreFirst"></strong>
	            </div>
	        </div>
	        
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #E78081;line-height: 30px;text-align: center;">
	                <strong>-TOP1</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #F1D1D2;line-height: 30px;text-align: center;">
	                <strong id="dataScoreLastFirst"></strong>
	            </div>
	        </div>
	    </div>
	    
	    <div style="width:100%;">
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #ADD597;line-height: 30px;text-align: center;">
	                <strong>TOP2</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #CDE2C3;line-height: 30px;text-align: center;">
	                <strong id="dataScoreSecond"></strong>
	            </div>
	        </div>
	        
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #E78081;line-height: 30px;text-align: center;">
	                <strong>-TOP2</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #F1D1D2;line-height: 30px;text-align: center;">
	                <strong id="dataScoreLastSecond"></strong>
	            </div>
	        </div>
	    </div>
	    
	    <div style="width:100%;">
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #ADD597;line-height: 30px;text-align: center;">
	                <strong>TOP3</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #CDE2C3;line-height: 30px;text-align: center;">
	                <strong id="dataScoreThird"></strong>
	            </div>
	        </div>
	        
	        <div style="float: left;margin-left: 5px;margin-top: 5px">
	            <div style="float: left;width: 176px;height: 30px;background-color: #E78081;line-height: 30px;text-align: center;">
	                <strong>-TOP3</strong>
	            </div>
	            <div style="float: left;width: 176px;height: 30px;background-color: #F1D1D2;line-height: 30px;text-align: center;">
	                <strong id="dataScoreLastThird"></strong>
	            </div>
	        </div>
	    </div>
    </div>
   
   	<div data-options="region:'south',collapsible:false" style="height:65%;width:100%;overflow-y:hidden;">
     	<table id="mainTable" ></table> 
     	
    </div>
    
    <div id="tt3">
    	<a  href="#" class="easyui-linkbutton"   style="width:80px;">下钻</a>
    	<a  href="#" class="easyui-linkbutton"   style="width:80px;">导出</a>
		<a  href="#" data-options="toggle:true,group:'statisticType'" class="easyui-linkbutton"  onclick="analysis(2);" style="width:80px;margin-right: 20px;float: right;">区-分析粒度</a> 
    	<a  href="#" data-options="toggle:true,group:'statisticType',selected:true" class="easyui-linkbutton" onclick="analysis(1);"  style="width:80px;margin-right: 20px;float: right;">市-分析粒度</a>
    	<a  href="#" data-options="toggle:true,group:'statisticType'" class="easyui-linkbutton" onclick="analysis(0);"  style="width:80px;margin-right: 20px;float: right;">省-分析粒度</a>
    </div>
 
   
    <!-- <div data-options="region:'south',collapsible:false" style="overflow-y:auto;height:60%;">
     	<table id="mainTable" ></table>
    </div>
     
    <div id="tt3" style="display: none;">
		<a href="#" class="easyui-menubutton" data-options="menu:'#refresh',iconCls:'icon-reload'">实时刷新</a>
		<a href="#" class="easyui-menubutton" data-options="menu:'#refresh',iconCls:'icon-reload'">实时刷新</a>
		<a href="#" class="easyui-menubutton" data-options="menu:'#refresh',iconCls:'icon-reload'">实时刷新</a>
		<a href="#" class="easyui-menubutton" data-options="menu:'#refresh',iconCls:'icon-reload'">实时刷新</a>
    </div> -->
</body>
</html>