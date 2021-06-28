<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">


	  <link href="${pageContext.request.contextPath}/js/portal/css/main.css" rel="stylesheet" type="text/css" />
	  <link href="${pageContext.request.contextPath}/js/portal/css/jquery-fallr-1.3.css" rel="stylesheet" type="text/css" />
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/jquery-1.7.1.min.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/ui/ui.core.min.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/ui/ui.sortable.min.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/jquery-fallr-1.3.pack.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/jquery.easing.1.3.js"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/Jh.js" charset="utf-8"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/linechart.js" charset="utf-8"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/player.js" charset="utf-8"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/slider.js" charset="utf-8"></script>


<%--
      <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/js/json/lineChart.js" charset="utf-8"></script>
--%>



		<%@ include file="../../taglibs/easyui.jsp"%>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/modelLock.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/command.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" charset="utf-8"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" charset="utf-8"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist/echarts.min.js"></script>

</head>
<script>




	$(function(){

		$("#outDiv").css('margin-top',$("#innerDiv").height());

		initMap();


		var s = '${logIds}';
		var n = '${logNames}';


		var ids = s.split(",");
		var names = n.split(",");

		var logs = [];
		for (var i = 0; i <ids.length ; i++) {
			if(i==0){
				MyPlayer.Data.logId = ids[0];
			}
			var log = {};
			log.logsId = ids[i];
			log.logName = names[i];
			logs.push(log);
		}

		MyPlayer.Data.playList = logs;




		var config;

		$.when(
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/doLogBackPlay.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						var ieData = {};
						$.each(data,function(index,item){
							if(index==0){
								MyPlayer.Data.startTime = item.time;
								MyPlayer.Data.currentTime = item.time;
							}
							if(index==data.length-1){
								MyPlayer.Data.endTime = item.time;
							}
							ieData[item.time] = item;
						});
						Jh.Data.ieData =  ieData;
					}
				}),
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/sigleWindowData.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						Jh.Data.signData =  data;
					}
				}),
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/evtWindowData.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						Jh.Data.eventData =  data;
					}
				}),


				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/lineChart.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){


						MyChart.Data.all = data;
						if(data[0]){
							MyChart.Data.initTime =  data[0].time;
						}


					}
				}),

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/getLayout.action",
					dataType: "json",
					success: function(data){
						config =  eval("("+data+")");
						config.resize = function(){
							MyChart.fn.resize();
						};
					}
				})
		).done(function(){
			Jh.base.init(config);
			Jh.fn.init(config);
			MyChart.fn.init();
			MySlider.fn.init($("#timer"));

			MyPlayer.fn.sync = function(sourceId){
				if(sourceId !== Jh.Config.syncSourceId ){
					Jh.fn.synced();
				}
				if(sourceId !== MyChart.Config.syncSourceId ){
					MyChart.fn.synced();
				}
				if(sourceId !== MySlider.Config.syncSourceId ){
					MySlider.fn.synced();
				}
				syncMap();
			}

			initLayoutSelect();

			$('#logs').combobox({
				data:MyPlayer.Data.playList,
				valueField:'logsId',
				textField:'logName',
				editable:false,
				onChange:function(newValue,oldValue){
					MyPlayer.Data.logId = newValue;
					loadNewLog();
				}
			});
			$('#logs').combobox('setValue',MyPlayer.Data.logId);

		});
	});

	function initLayoutSelect(){

		$.when(
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/getAllLayout.action",
					dataType: "json",
					success: function(data){
						var arr =  [];
						$.each(data,function(k,v){
							var s = {};
							s.id = v.id;
							s.name  = v.name;
							s.value = v.value;

							if(v.status == '1'){
								MyPlayer.Data.layoutId = v.id;
							}
							arr.push(s);
						});

						MyPlayer.Data.layoutArr = arr;
					}
				})
		).done(function(){

			$('#layoutSelect').combobox({
				data:MyPlayer.Data.layoutArr,
				valueField:'id',
				textField:'name',
				editable:false,
				onChange:function(newValue,oldValue){

					$.each( MyPlayer.Data.layoutArr,function(k,v){
						if(v.id == newValue){
							MyPlayer.Data.layoutId = v.id;
							toNewLayout(v.value);
						};
					});

				}
			});

			$('#layoutSelect').combobox('setValue',MyPlayer.Data.layoutId);
		})

	}

	function initMap(){

		var src = $("#mapIframe").attr("src");
		if(!src){
			$("#mapIframe").attr("src", "${pageContext.request.contextPath}/map/GpsTrace.html");
		}
		setTimeout(function () {
			var data = {
				'dataType':'logreplay',
				'logids':MyPlayer.Data.logId
			}
			document.getElementById('mapIframe').contentWindow.postMessage(data);

		},2000)

	}

	function syncMap(){
		var data = {
				'dataType':'synchlog',
				'logid':MyPlayer.Data.logId,
				'timestamp':new Date(MyPlayer.Data.currentTime).getTime()
			}

		document.getElementById('mapIframe').contentWindow.postMessage(data);
	}



	function saveLayoutConfig(){

		$.messager.confirm("提示","是否保存到当前配置",function(r){
			if(r){

				var value = Jh.base._getConfig();

				$.ajax({

					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/saveLayout.action",
					data:{'configValue':value,'configId':MyPlayer.Data.layoutId},
					dataType: "json",
					success: function(data){
						initLayoutSelect();
						$.messager.alert('操作提示','配置更新成功');
					}
				});

			}else{

				$("#addLayConfig").dialog('open');
				$("#addLayConfig").window('center');

			}
		});
	}


	function submitSaveLayout(){

		var s =  $("#addLayConfig").form('validate');

		if(s){
			var value = Jh.base._getConfig();
			var name = $("#layoutName").textbox('getValue');
			$.ajax({
				type: "POST",
				url: "${pageContext.request.contextPath}/logReplay/saveLayout.action",
				data:{'configValue':value,'configName':name},
				dataType: "json",
				success: function(data){
					$.messager.alert('操作提示','新增配置成功');
					initLayoutSelect();
				}
			});
			$("#addLayConfig").dialog('close');
		}
	}

	function toNewLayout(config){

		$("#portal").remove();
		config =  eval("("+config+")");
		config.resize = function(){
			MyChart.fn.resize();
		};
		Jh.fn.init(config);
		MyChart.fn.init();
		$(".active").removeClass("active");
		$($(".layoutText")[config.layout]).addClass('active');
	}


	function syncViewData(id){
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/logback/synOper.action",
			data: {'logId': MyPlayer.Data.logId,'time':MyPlayer.Data.currentTime},
			dataType: "json",
			success: function(data){
				Jh.fn._refresh(data[0],id);
			}
		});
	}


	function loadNewLog() {

		MyPlayer.fn.stop();

		initMap();
		$.when(

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/doLogBackPlay.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						var ieData = {};
						$.each(data,function(index,item){
							if(index==0){
								MyPlayer.Data.startTime = item.time;
								MyPlayer.Data.currentTime = item.time;
							}
							if(index==data.length-1){
								MyPlayer.Data.endTime = item.time;
							}
							ieData[item.time] = item;
						});
						Jh.Data.ieData =  ieData;
					}
				}),
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/sigleWindowData.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						Jh.Data.signData =  data;
					}
				}),
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/evtWindowData.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						Jh.Data.eventData =  data;
					}
				}),


				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/lineChart.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						MyChart.Data.all = data;

						if(data.length>0){
							MyPlayer.Data.startTime = new Date(data[0].time).Format("yyyy-MM-dd hh:mm:ss");
							MyPlayer.Data.currentTime = MyPlayer.Data.startTime;
							MyPlayer.Data.endTime = new Date(data[data.length-1].time).Format("yyyy-MM-dd hh:mm:ss");
						}
					}
				}),

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/getLayout.action",
					dataType: "json",
					success: function(data){
						config =  eval("("+data+")");
						config.resize = function(){
							MyChart.fn.resize();
						};

					}
				})


		).done(function(){

			var lay =  $("#layoutSelect").combobox('getValue');
			$.each( MyPlayer.Data.layoutArr,function(k,v){
				if(v.id == lay){
					toNewLayout(v.value);
				}
			});
			MySlider.fn.init($("#timer"))
		})

}


</script>
<body>
	<div id="outDiv" style="width:100%;">
		<div id ="innerDiv" style="height: 50%;width:100%;position:fixed;left:0;top:0;background:white;z-index:8000">
			<div  class="noselect" style="height:11%;text-align:center;margin-top:18px;">
				<div style="width:80%;height:40px;line-height:40px;vertical-align:middle">
					<div style="height:35px;width:380px;display:inline-block;vertical-align:middle" >
						<input id="logs" style="width:100%;display: none">
					</div>
					<div style="line-height:26px;margin-left:10px;height:40px;display:inline-block;vertical-align:middle">
						<span class="tstop ticon" onclick="MyPlayer.fn.stop()"></span>
						<span id="tplay" class="tplay ticon" onclick="MyPlayer.fn.play()"></span>
						<span id="tpause" style="display: none" class="tpause ticon" onclick="MyPlayer.fn.pause()"></span>
						<span class="trewind ticon" onclick="MyPlayer.fn.rewind()"></span>
						<span class="tfastforwad ticon" onclick="MyPlayer.fn.fastForward()"></span>
						<span class="tnext ticon" onclick="MyPlayer.fn.nextFrame()"></span>
						<span class="trestart ticon" onclick="MyPlayer.fn.restart()"></span>
						<span id="speedSpan" class="ticon" style="text-align:left;width:30px;font-size:18px;">1X</span>
					</div>

					<div style="margin-left:20px;height:40px;display:inline-block;width:380px;vertical-align:middle;">
						<input style="float:left"  class="easyui-slider" id="timer"  style="width:180px"  data-options="showTip:true">
					</div>
				</div>
			</div>


			<div style="height:83%;width:100%;">
				<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
			</div>

		</div>
	</div>


	<div style="margin-top:5px;margin-left:20px;" class="layoutContent" >
			<table width="100%">
				<tr style="font-size:14px;">
					<td width="1%" align="left"><a class="layoutText" rel="1"  >1</a></td>
					<td width="2%" align="left"><a class="layoutText" rel="1:1" >1:1</a></td>
					<td width="2%" align="left"><a class="layoutText" rel="1:2"  >1:2</a></td>
					<td width="2%" align="left"><a class="layoutText" rel="2:1" >2:1</a></td>
					<td width="3%" align="left"><a class="layoutText" rel="1:1:1"  >1:1:1</a></td>
					<td width="5" align="right"><input id = "layoutSelect" style="display: none"> </td>
					<td width="6%" align="right"><a class="easyui-linkbutton" id="saveLayoutConfig" style="width: 80px;"  >保存配置</a></td>
					<td width="79%" align="left"><a class="easyui-linkbutton" id="addPanelBtn"  style="width: 80px;">添加窗口</a></td>
				</tr>
			</table>
		</div>

		<div id="addPanel" class="easyui-dialog" style="overflow:auto;width:400px;height:200px;padding:10px" data-options="title:'添加窗口',resizable:true,border:false,closable:true,closed:true,modal:false">
			<table style="margin-top:20px;width:100%;">
				<tr  style="width:100%;">
					<td style="width:100%;" align="center"> <input id="panelSelect" style="margin-left:100px ;width:200px"></td>
				</tr>
			</table>
			<table style="width:100%;margin-top:50px;">
				<tr style="width:100%">
					<td style="width:43%" align="right" > <a iconCls="icon-ok" id="submitAddPanelBtn" class="easyui-linkbutton"  >确定</a></td>
					<td style="width:13%" align="center"> </td>
					<td style="width:43%" align="left"> <a class="easyui-linkbutton" iconCls="icon-undo" onclick="$('#addPanel').dialog('close');" >取消</a></td>
				</tr>
			</table>
		</div>

		<div id="addLayConfig" class="easyui-dialog" style="overflow:auto;width:400px;height:200px;padding:10px" data-options="title:'添加窗口',resizable:true,border:false,closable:true,closed:true,modal:false">
			<table style="margin-top:20px;width:100%;">
				<tr  style="width:100%;">
					<td style="width:27%" align="right">配置名称:</td>
					<td style="width:3%" align="center"> </td>
					<td style="width:70%;" align="left"> <input class="easyui-textbox" type="text" data-options="required:true" id="layoutName" style=""></td>
				</tr>
			</table>
			<table style="width:100%;margin-top:50px;">
				<tr style="width:100%">
					<td style="width:43%" align="right" > <a iconCls="icon-ok" id="addLayConfigBtn" onclick="submitSaveLayout()" class="easyui-linkbutton"  >确定</a></td>
					<td style="width:13%" align="center"> </td>
					<td style="width:43%" align="left"> <a class="easyui-linkbutton" iconCls="icon-undo" onclick="$('#addLayConfig').dialog('close');" >取消</a></td>
				</tr>
			</table>
		</div>




		<div id="signDetailDiv" class="easyui-dialog" style="overflow:auto;width:550px;height:400px;padding:10px" data-options="title:'信令明细',resizable:true,border:false,closable:true,closed:true,modal:false">
			<div id="signDetail">
			<div>
		</div>


</body>
</html>