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


      <script type="text/javascript" src="${pageContext.request.contextPath}/js/portal/json/lineChart.js" charset="utf-8"></script>

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
		var config;

		$.when(
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/doLogBackPlay.action",
					data: {'logId': 575},
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
					data: {'logId': 575},
					dataType: "json",
					success: function(data){
						Jh.Data.signData =  data;
					}
				}),
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/evtWindowData.action",
					data: {'logId': 575},
					dataType: "json",
					success: function(data){
						Jh.Data.eventData =  data;
					}
				}),


				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/logback/lineChart.action",
					data: {'logId': 575},
					dataType: "json",
					success: function(data){

                        if(data.length<0){
                            console.log("测试数据")
                            Mychart.Data.all = data5;
                            Mychart.Data.initTime =  data5[0].time;
                        }else{
                            Mychart.Data.all = data;
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
						};
						config.resize = function(){
							Mychart.fn.resize();
						};
						config.syncViewData = function(id){

							$.ajax({
								type: "POST",
								url: "${pageContext.request.contextPath}/logback/synOper.action",
								data: {'logId': 575,'time':MyPlayer.Data.currentTime},
								dataType: "json",
								async:false,
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
		});


	});


	function play(){
		MyPlayer.fn.play();

	}

	function pause(){
		MyPlayer.fn.pause();
	}

	function getData5(){
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/logback/synOper.action",
			data: {'logId': 575,'time':'2021-05-15 08:59:59.633'},
			dataType: "json",
			async:false,
			success: function(data){
				console.log(data);
			}
		});
	}

	function initMap(){

		var src = $("#mapIframe").attr("src");
		if(!src){
			$("#mapIframe").attr("src", "${pageContext.request.contextPath}/map/GpsTrace.html");
		}
		var data = {
				'dataType':'logreplay',
				'logids':'575'
			}
		document.getElementById('mapIframe').contentWindow.postMessage(data);
	}

	function syncMap(){
		var data = {
				'dataType':'synchlog',
				'logid':'575',
				'timestamp':new Date(MyPlayer.Data.currentTime).getTime()
			}

		document.getElementById('mapIframe').contentWindow.postMessage(data);
	}


	$('#logs').combobox({
		data:MyPlayer.Data.playList,
		valueField:'logsId',
		textField:'logName',
		editable:false
	});


	$('#timer').slider({
		showTip:true,
		tipFormatter: function(value){

			return  getSliderTime(value);
		},
		rule:[MyPlayer.Data.startTime,'|',MyPlayer.Data.endTime],
		onChange:function(newValue,oldValue){
			MyPlayer.Data.currentTime = getSliderTime(newValue);

			MyPlayer.fn.playOnce();
		}
	});



	function getSliderTime(value){

		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		t = t*value/100;
		var nd =  new Date(	MyPlayer.Data.startTime);
		nd.setMilliseconds(nd.getMilliseconds()+t);

		return  nd.Format("yyyy-MM-dd hh:mm:ss");
	}

	function setSliderValue(){
		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		var t2 =  new Date(MyPlayer.Data.currentTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;

		var value = t2/t*100;
		$('#timer').slider('setValue',value);

	}



</script>
<body>

	<div style="margin:5px 30px;width:380px;display:inline-block;" >
		<input id="logs" style="width:100%"  >
	</div>
	<div style="display:inline-block;width:380px;margin-left:100px;margin-top:30px;">
		<input style="float:left"  class="easyui-slider" id="timer"  style="width:180px"  data-options="showTip:true">

	</div>


	<div style="padding-top: 4px;width:100%;">
		<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:50%;border:0;margin: 0;" ></iframe>
	</div>


</body>
</html>