<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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


		<%@ include file="../../taglibs/easyui.jsp"%>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/modelLock.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/command.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" charset="utf-8"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" charset="utf-8"></script>
	  <script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist/echarts.min.js"></script>

</head>
<script>

	var initPage = false;
	var initLayout = false;

	$(function(){

		Jh.Data.roadData = eval('${roadData}');
		Jh.Data.exceptEventData = eval('${exceptEventData}');
		if(!Jh.Data.roadData){
			Jh.Data.roadData = [];
		}
		if(!Jh.Data.exceptEventData){
			Jh.Data.exceptEventData = [];
		}


		$("#outDiv").css('margin-top',$("#innerDiv").height());

		var s = '${logIds}';
		var n = '${logNames}';

		if(!s){
			$.messager.alert("系统提示", "查询不到关联日志数据",'',function(){
				$(".tabs-selected .tabs-close",parent.document)[0].click();
			});
			$(".panel-tool-close").hide();
			return ;
		}

		initMap();

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

						data =  data.sort(function(a,b){
							return new Date(a['time']).getTime() - new Date(b['time']).getTime();
						});


						$.each(data,function(index,item){
							if(index==0 && item){
								MyPlayer.Data.startTime = item.time;
								MyPlayer.Data.currentTime = item.time;
							}
							if(index==data.length-1 && item){
								MyPlayer.Data.endTime = item.time;
							}
							if(item){
								ieData[item.time] = item;
							}
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
					url: "${pageContext.request.contextPath}/logback/pcapData.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						Jh.Data.pcapData =  data.items;
						Jh.PData.maxPcapId = data.maxId;
						Jh.PData.minPcapId = data.minId;
					}
				}),

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/lineChart.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						MyChart.Data.all = data;
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
			initPage = true;
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
			initLayout = true;
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
		if(initLayout){
			initLayout =false;
			return;
		}
		$("#portal").empty();
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

	function syncPcapData(){
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/logback/syncPcapData.action",
			data: {'logId': MyPlayer.Data.logId,'time':MyPlayer.Data.currentTime},
	//		async:false,
			dataType: "json",
			success: function(data){
				data =  data.sort(function(a,b){
					return new Date(a['time']).getTime() - new Date(b['time']).getTime();
				});
				Jh.Data.pcapData =  data;

				Jh.fn._refreshTable('view8');

				var view = 'view8';
				var target;
				var dataArray;

				dataArray = Jh.Data.pcapData.map(c=>c.time);

				dataArray.push(MyPlayer.Data.currentTime);
				dataArray.sort();
				var position =  dataArray.indexOf(MyPlayer.Data.currentTime);
				if(position == 0){
					target = 0;
				}else if(position == dataArray.length - 1){
					target = dataArray.length -2;
				}else{
					var before = new Date(MyPlayer.Data.currentTime).getTime()  - new Date(dataArray[position-1]).getTime();
					var after =  new Date(dataArray[position+1]).getTime() - new Date(MyPlayer.Data.currentTime).getTime();
					if(before <after){
						target = position -1;
					}else{
						target = position;
					}
				}
				var obj = $("."+view+"_"+target)[0];
				var firstobj = $("."+view+"_0")[0];
				//				var obj = $("."+ view +"_"+Jh.Util.dateSlice(MyPlayer.Data.currentTime)+":first")[0];
				if(obj){
					var scrollTo = obj.offsetTop-firstobj.offsetTop;
					var currentScroll = $("#"+view+"_tbody").scrollTop();
					var bodyHeight = $("#"+view+"_tbody").height();
					var objHeight = $("."+view+"_"+target).height();
					if( bodyHeight - objHeight >= scrollTo - currentScroll &&   scrollTo >=  currentScroll  ){
					}else{
						$("#"+view+"_tbody").animate({
							scrollTop:obj.offsetTop-firstobj.offsetTop
						});
					}
					$("#"+view+" .datagrid-row-selected").removeClass("datagrid-row-selected")
					$(obj).addClass("datagrid-row-selected");
				}else{
					//console.log("."+ view +"_"+  Jh.Util.dateSlice(MyPlayer.Data.currentTime) +":first");
				}
			}
		});
	}

	function loadMorePcapData(direction){
		if(Jh.PData.loadingPcapData ){
			return;
		}
		var beginId = $("#view8_tbody tr:last-child").attr('did');
		if(0 == direction ){
			beginId = $("#view8_tbody tr:first-child").attr('did');
			if(!(Jh.PData.minPcapId < beginId)){
				return;
			}
		}else{
			if(!(Jh.PData.maxPcapId > beginId)){
				return;
			}
		}
		Jh.PData.loadingPcapData = true;
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/logback/morePcapData.action",
			data: {'logId': MyPlayer.Data.logId,beginId:beginId,direction:direction},
			async:true,
			dataType: "json",
			success: function(data){
				Jh.fn._pcapLoadingData(data,direction);
				Jh.PData.loadingPcapData = false;

				setTimeout(function(){
					//防止滚动条 拖到最高挥着最低的地方后无法继续加载的问题
					if($("#view8_tbody").scrollTop() ==0){
						$("#view8_tbody").scrollTop(1);
					}
					if($("#view8_tbody")[0].scrollHeight - $("#view8_tbody").height() - $("#view8_tbody").scrollTop() < 1 ){
						$("#view8_tbody").scrollTop($("#view8_tbody")[0].scrollHeight - $("#view8_tbody").height() - 1);
					}
				},300);

			},
			error:function(){
				Jh.PData.loadingPcapData = false;
			}
		});



	}


	function loadNewLog() {

		if(initPage){
			initPage = false;
			return;
		}
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

						data =  data.sort(function(a,b){
							return new Date(a['time']).getTime() - new Date(b['time']).getTime();
						});

						$.each(data,function(index,item){
							if(index==0  && item){
								MyPlayer.Data.startTime = item.time;
								MyPlayer.Data.currentTime = item.time;
							}
							if(index==data.length-1 && item){
								MyPlayer.Data.endTime = item.time;
							}
							if(item){
								ieData[item.time] = item;
							}

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
					url: "${pageContext.request.contextPath}/logback/pcapData.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						Jh.Data.pcapData =  data.items;
						Jh.PData.maxPcapId = data.maxId;
						Jh.PData.minPcapId = data.minId;
					}
				}),

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/lineChart.action",
					data: {'logId': MyPlayer.Data.logId},
					dataType: "json",
					success: function(data){
						MyChart.Data.all = data;
					}
				}),




		).done(function(){

			var lay =  $("#layoutSelect").combobox('getValue');

			if(lay){
				$.each( MyPlayer.Data.layoutArr,function(k,v){
					if(v.id == lay){
						toNewLayout(v.value);
						MySlider.fn.init($("#timer"));
					}
				});
			}else{

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/getLayout.action",
					dataType: "json",
					success: function(data){
						toNewLayout(data);
						MySlider.fn.init($("#timer"));
					}
				})
			}
		})

}





</script>
<body>
	<div id="outDiv" style="width:100%;">
		<div id ="innerDiv" style="height: 50%;width:100%;position:fixed;left:0;top:0;background:white;z-index:8000">
			<div  class="noselect" style="height:11%;text-align:center;margin-top:18px;">
				<div style="width:100%;height:40px;line-height:40px;vertical-align:middle">
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

		<div id="addLayConfig" class="easyui-dialog" style="overflow:auto;width:400px;height:200px;padding:10px" data-options="title:'布局配置',resizable:true,border:false,closable:true,closed:true,modal:false">
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
			<div style="white-space:pre-line;word-break:break-all" id="signDetail">
			</div>
		</div>



		<div id="displayCfgWindow" class="easyui-window" title="显示设置" data-options="closed:true,maximizable:false,minimizable:false,collapsible:false" style="width: 700px;height: 500px;">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false" style="padding: 0px;">
					<div class="easyui-tabs" data-options="border:false,tabWidth:85" style="width:100%;height: 420px">
						<div title="工参" style="padding: 10px;">
							<div class="qrDiv">
								<div class="qrText">联通-4G</div>
								<input class="qrColor" type="color" id="unicom4g" value="${cfgEntity.unicom4g}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">联通-5G</div>
								<input class="qrColor" type="color" id="unicom5g" value="${cfgEntity.unicom5g}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">移动-4G</div>
								<input class="qrColor" type="color" id="mobile4g" value="${cfgEntity.mobile4g}"   />
							</div>
							<div class="qrDiv">
								<div class="qrText">移动-5G</div>
								<input class="qrColor" type="color" id="mobile5g" value="${cfgEntity.mobile5g}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">电信-4G</div>
								<input class="qrColor" type="color" id="telcom4g" value="${cfgEntity.telcom4g}"   />
							</div>
							<div class="qrDiv">
								<div class="qrText">电信-5G</div>
								<input class="qrColor" type="color" id="telcom5g" value="${cfgEntity.telcom5g}"  />
							</div>
						</div>
						<div title="轨迹" style="padding: 10px;">
							<div class="inputDivShow">筛选
								<input style="width: 160px" id=""  name=""  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
							</div>



						</div>
						<div title="拉线" style="padding: 10px;">
							3
						</div>
						<div id="questionRoad" title="问题路段" style="padding: 10px;">
							<div class="qrDiv">
								<div class="qrText">弱覆盖问题路段</div>
								<input class="qrColor" type="color" id="weakCoverQuestionRoad" value="${cfgEntity.weakCoverQuestionRoad}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">重叠覆盖问题路段</div>
								<input class="qrColor" type="color" id="overlapCoverQuestionRoad" value="${cfgEntity.overlapCoverQuestionRoad}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">上行质差问题路段</div>
								<input class="qrColor" type="color" id="upQualityDiffQuestionRoad" value="${cfgEntity.upQualityDiffQuestionRoad}"   />
							</div>
							<div class="qrDiv">
								<div class="qrText">下行质差问题路段</div>
								<input class="qrColor" type="color" id="downQualityDiffQuestionRoad" value="${cfgEntity.downQualityDiffQuestionRoad}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">上行低速率问题路段</div>
								<input class="qrColor" type="color" id="upLowSpeedQuestionRoad" value="${cfgEntity.upLowSpeedQuestionRoad}"  />
							</div>
							<div class="qrDiv">
								<div class="qrText">下行低速率问题路段</div>
								<input class="qrColor" type="color"  id="downLowSpeedQuestionRoad" value="${cfgEntity.downLowSpeedQuestionRoad}"  />
							</div>
						</div>
						<div title="异常事件" style="padding: 10px;">
							5
						</div>
						<div title="网格对比" style="padding: 10px;">
							6
						</div>
						<div title="采样点过滤" style="padding: 10px;">
							7
						</div>
					</div>

				</div>
				<div data-options="region:'south',border:false" style="text-align: center;padding: 5px;">
					<a class="easyui-linkbutton"  href="javascript:void(0)" onclick="saveDisplayCfg()" style="width: 80px;">确定</a>
					<a class="easyui-linkbutton"  href="javascript:void(0)" onclick="closeCfgWindow();" style="width: 80px;">取消</a>
				</div>
			</div>
		</div>


		<script>
			function showCfgWindow(){
				$("#displayCfgWindow").window('open');
				$("#displayCfgWindow").window('center');
			}
			function closeCfgWindow(){
				$("#displayCfgWindow").window('close');
			}

			/* 保存修改 */
			function saveDisplayCfg(){
				var fieldNameEn = "";
				var fieldValue = "";
				$("#questionRoad input").each(function(i,a){
					fieldNameEn += $(a).attr('id') +",";
					fieldValue += $(a).val() + ',';
				})
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
							closeCfgWindow();
						}
					}
				});

			}


		</script>

		<style>
			.qrDiv{
				height: 50px;
				padding-top: 10px;
				box-sizing: border-box;
				border-bottom: dashed 1px #8f818194;
			}
			.qrText{
				margin-left: 20px;
				height: 30px;
				line-height: 30px;
				font-size: 15px;
				float: left;
				width: 200px;
				text-align: left;
			}
			.qrColor{
				border:none;
				padding:0;
				background-color:white;
				width: 120px;
				height: 30px;
			}

		</style>

</body>
</html>