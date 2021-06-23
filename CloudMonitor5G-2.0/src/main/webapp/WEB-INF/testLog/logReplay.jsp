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


						Mychart.Data.all = data;
						if(data[0]){
							Mychart.Data.initTime =  data[0].time;
						}


					}
				}),

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/getLayout.action",
					dataType: "json",
					success: function(data){
						config =  eval("("+data+")");
						config.sync = function(a){
							Mychart.Data.canSync = false;
							Mychart.fn.calcIndex(a);
							Mychart.fn.play();
							Mychart.Data.canSync = true;
							setSliderValue();
							syncMap();
						};
						config.resize = function(){
							Mychart.fn.resize();
						};
						config.syncViewData = function(id){

							$.ajax({
								type: "POST",
								url: "${pageContext.request.contextPath}/logback/synOper.action",
								data: {'logId': MyPlayer.Data.logId,'time':MyPlayer.Data.currentTime},
								dataType: "json",
								success: function(data){
									Jh.Portal._refresh(data[0],id);
								}
							});


						};

					}
				})
		).done(function(){
			Jh.fn.init(config);
			Jh.Portal.init(config);

			Mychart.fn.init({
				sync:function(){
					setSliderValue();
					Jh.Portal._syncData("#view7");
				}
			});

			MyPlayer.fn.init({
				playOnce:function(time){
					Mychart.fn.calcIndex(time);
					Mychart.fn.play();
					Jh.Portal._refresh();

					syncMap();
				},
				play:function(time){
					Mychart.fn.disableClick();
					Mychart.fn.play();
					Jh.Portal._refresh();
					Mychart.fn.calcIndex(time);
					syncMap();
					setSliderValue();
				},pause:function(){
					Mychart.fn.enableClick();
				}
			});


			$('#timer').slider({
				showTip:true,
				tipFormatter: function(value){
					return  getSliderTime(value);
				},
				rule:[MyPlayer.Data.startTime,'|',MyPlayer.Data.endTime],
				onChange:function(newValue,oldValue){
					MyPlayer.Data.currentTime = getSliderTime(newValue);

					if(sliderSync){
						var ct = MyPlayer.Data.currentTime;
						setTimeout(function(){
							if(ct == MyPlayer.Data.currentTime){
								console.log("同步。。。。");
								MyPlayer.fn.playOnce();
							}
						},500);
					}
				}
			});

			$(".slider-rulelabel").find("span:last-child").css("margin-left","-117px")


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







	function getSliderTime(value){

		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		t = t*value/100;
		var nd =  new Date(	MyPlayer.Data.startTime);
		nd.setMilliseconds(nd.getMilliseconds()+t);

		return  nd.Format("yyyy-MM-dd hh:mm:ss");
	}

	var sliderSync = true;
	function setSliderValue(){

		sliderSync = false;
		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		var t2 =  new Date(MyPlayer.Data.currentTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;

		var value = t2/t*100;
		$('#timer').slider('setValue',value);

		sliderSync = true;
	}


	function saveLayoutConfig(value){

		$.messager.confirm("提示","是否保存到当前配置",function(r){
			if(r){

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


				$.fallr("show", {
					buttons: {
						button1: {
							//确定
							text: "\u786e\u5b9a", onclick: function () {
								var configName =  $("#configName").val();
								if(configName){
									$.fallr("hide");
									$.ajax({
										type: "POST",
										url: "${pageContext.request.contextPath}/logReplay/saveLayout.action",
										data:{'configValue':value,'configName':configName},
										dataType: "json",
										success: function(data){
											$.messager.alert('操作提示','新增配置成功');
											initLayoutSelect();
										}
									});
								}
							}
							//取消
						}, button2: {text: "\u53d6\u6d88"}
					},
					//模块名：   模块Code： 模块位置： 左中右
					content: '<form style="margin-left:20px"><p>配置名称:</p>' +
							'<input type="text" size="15" id="configName">' +

							//     '<input type="text" size="15" id="modulekey" />' +
							'</form>',
					//			content: '<form style="margin-left:20px"><p>\u6a21\u5757\u540d\uff1a</p><input type="text" size="15" id="modulename" /><p>\u6a21\u5757Code\uff1a</p><input type="text" size="15" id="modulekey" /><p>\u6a21\u5757\u4f4d\u7f6e\uff1a</p>\u5de6:<input type="radio" name="modulelayout" checked="checked" value="left"/>&nbsp&nbsp\u4e2d:<input type="radio" name="modulelayout" value="center"/>&nbsp&nbsp\u53f3:<input type="radio" name="modulelayout" value="right"/></form>',

					icon: "add",
					position: "center"
				});
			}
		});
	}

	function toNewLayout(config){

		$("#portal").remove();
		config =  eval("("+config+")");
		Jh.Portal.init(config);

		Mychart.fn.init({
			sync:function(){
				setSliderValue();
				Jh.Portal._syncData("#view7");
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


						Mychart.Data.all = data;
						if(data[0]){
							Mychart.Data.initTime =  data[0].time;
						}


					}
				}),

				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logReplay/getLayout.action",
					dataType: "json",
					success: function(data){
						config =  eval("("+data+")");
						config.sync = function(a){
							Mychart.Data.canSync = false;
							Mychart.fn.calcIndex(a);
							Mychart.fn.play();
							Mychart.Data.canSync = true;
							setSliderValue();
							syncMap();
						};
						config.resize = function(){
							Mychart.fn.resize();
						};
						config.syncViewData = function(id){

							$.ajax({
								type: "POST",
								url: "${pageContext.request.contextPath}/logback/synOper.action",
								data: {'logId': MyPlayer.Data.logId,'time':MyPlayer.Data.currentTime},
								dataType: "json",
								success: function(data){
									Jh.Portal._refresh(data[0],id);
								}
							});


						};

					}
				})


		).done(function(){

			var lay =  $("#layoutSelect").combobox('getValue');
			$.each( MyPlayer.Data.layoutArr,function(k,v){
				if(v.id == lay){
					toNewLayout(v.value);
				};
			});


			MyPlayer.fn.init({
				playOnce:function(time){
					Mychart.fn.calcIndex(time);
					Mychart.fn.play();
					Jh.Portal._refresh();

					syncMap();
				},
				play:function(time){
					Mychart.fn.disableClick();
					Mychart.fn.play();
					Jh.Portal._refresh();
					Mychart.fn.calcIndex(time);
					syncMap();
					setSliderValue();
				},pause:function(){
					Mychart.fn.enableClick();
				}
			});


			$('#timer').slider({
				showTip:true,
				tipFormatter: function(value){
					return  getSliderTime(value);
				},
				rule:[MyPlayer.Data.startTime,'|',MyPlayer.Data.endTime],
				onChange:function(newValue,oldValue){
					MyPlayer.Data.currentTime = getSliderTime(newValue);

					if(sliderSync){
						var ct = MyPlayer.Data.currentTime;
						setTimeout(function(){
							if(ct == MyPlayer.Data.currentTime){
								console.log("同步。。。。");
								MyPlayer.fn.playOnce();
							}
						},500);
					}
				}
			});

			$(".slider-rulelabel").find("span:last-child").css("margin-left","-117px")

		})

}


</script>
<body>


	<div style="margin:5px 30px;width:380px;display:inline-block;" >
		<input id="logs" style="width:100%"  >
	</div>

	<div style="display: inline-block">
		<span class="tstop ticon" onclick="MyPlayer.fn.stop()"></span>
		<span id="tplay" class="tplay ticon" onclick="MyPlayer.fn.play()"></span>
		<span id="tpause" style="display: none" class="tpause ticon" onclick="MyPlayer.fn.pause()"></span>
		<span class="trewind ticon" onclick="MyPlayer.fn.rewind()"></span>
		<span class="tfastforwad ticon" onclick="MyPlayer.fn.fastForward()"></span>
		<span class="tnext ticon" onclick="MyPlayer.fn.next()"></span>
		<span class="trestart ticon" onclick="MyPlayer.fn.pause()"></span>
		<span id="speedSpan" style="display:inline-block;width:90px;height:30px;line-height:30px;font-size:30px;">1X</span>
	</div>

	<div style="display:inline-block;width:380px;margin-left:100px;margin-top:30px;">
		<input style="float:left"  class="easyui-slider" id="timer"  style="width:180px"  data-options="showTip:true">

	</div>



	<div style="padding-top: 4px;width:100%;">
		<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:50%;border:0;margin: 0;" ></iframe>
	</div>


	<div style="margin:5px 30px;width:200px;display:inline-block;" >
		<input id="layoutSelect" style="width:100%"  >
	</div>





</body>
</html>