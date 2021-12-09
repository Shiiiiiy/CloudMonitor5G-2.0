<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>感知监控</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts-5.0.0/echarts.min.js"></script>

	<style type="text/css">

		*{margin: 0;padding: 0;border: 0;list-style: none;text-decoration: none;color: inherit;font-weight: normal;box-sizing: border-box;}
		body{min-width: 1280px;}

		.inputDivShow{
			display: inline-block; *
			display: inline;
			font-size: 12px;
			margin: 5px;
			padding-left: 0;
			padding-right: 0;
			text-align: left;
			height: 23px;
		}
		.inputDivShow input{
			width:150px;
		}
		.inputDivShow select{
			width:140px;
		}

		.picContainer{
			width: 100%;
		}

		.title{
			margin: 2px;
			font-size: 14px;
			height: 25px;
			width: 80px;
			line-height: 25px;
			border-left: solid #ffd44a;
			text-align: center;
			background: #efefef;
		}
		.diagnose{
			float: left;
			width: 33.33%;
		}

		.diagnose .box{
			width: 80%;
			font-size: 12px;
			text-align: center;
			line-height: 18px;
			margin: 0 auto;
		}
		.diagnose .icon{
			width: 18px;
			height: 18px;
			float: left;
		}
		.diagnose .text{
			border: solid 1px gray;
			border-bottom: none;
			float: left;
			width: 60%;
			height: 18px;
		}
		.diagnose .value{
			border: solid 1px gray;
			border-left: none;
			border-bottom: none;
			height: 18px;
			text-align: center;
			float: left;
			width: calc(40% - 18px);
		}
		.diagnose .box:last-child .text,
		.diagnose .box:last-child .value
		{
			border-bottom: solid 1px gray;
		}
		.dTitle{
			height: 23px;
			line-height: 23px;
			font-size: 13.5px;
			border-radius: 3px;
			text-align: center;
			width: 80px;
			background-color: #bed768;
			margin: 0 auto 3px;
		}




		.w1200{
			width: 49%;
			height: 30%;
			margin: 0 auto;
			position: relative;
			overflow:hidden;
			float:left;
			min-height: 160px;
		}
		.picContainer .w1200:first-child{
			margin-right: 2%;
		}
		.w1200 ul{
			top:30px;
			position:absolute;
			z-index: 5;
			height: calc(100% - 30px);
		}
		.w1200 ul li{
			margin:0 5px;
			float:left;
			height: 97%;
			background: #FFFFFF;
			border-radius: 5px;
			text-align: center;

			box-shadow: 0px 0px 10px rgba(200,200,200,.5);
		}

		.pieChart{
			float: left;
			width: 50%;
			height: calc(100% - 50px);
		}
		.speedText{
			float: left;
			width: 50%;
			height: calc(100% - 50px);
			text-align: left;
			font-size: 13px;
			display:flex;
			justify-content: space-evenly;
			flex-direction: column;
		}

		.subTitle{
			margin: 0 auto;
			height: 50px;
			width: 100%;
		}

		.titleIcon{
			width: 30%;
			float: left;
			height: 100%;
			position: relative;
		}
		.trafficIcon{
			position: absolute;
			width:30px;
			height:30px;
			background-size: cover;
			right: 6px;
			top: 15px;
		}
		.titleText{
			padding-top: 20px;
			width: 70%;
			float: left;
			height: 100%;
			text-align: left;
		}

		.lineBar{
			height: calc(50% - 35px);
			padding:0 10px;
		}
		.appIcon{
			margin: 0 auto;
			height: 50px;
			width: 50px;

			background-size: cover;
		}

		.mail139{
			background-image: url("../images/kfmonitor/139mail.jpg");
		}

		.alipay{
			background-image: url("../images/kfmonitor/alipay.png");
		}

		.baidumap{
			background-image: url("../images/kfmonitor/baidumap.jpg");
		}

		.bdnetdisk{
			background-image: url("../images/kfmonitor/bdnetdisk.jpg");
		}

		.ctrip{
			background-image: url("../images/kfmonitor/ctrip.jpg");
		}

		.douyu{
			background-image: url("../images/kfmonitor/douyu.jpg");
		}

		.gdmap{
			background-image: url("../images/kfmonitor/gdmap.jpg");
		}

		.jingdong{
			background-image: url("../images/kfmonitor/jingdong.jpg");
		}

		.kuaishou{
			background-image: url("../images/kfmonitor/kuaishou.jpg");
		}

		.qiyi{
			background-image: url("../images/kfmonitor/qiyi.jpg");
		}

		.qq{
			background-image: url("../images/kfmonitor/qq.jpg");
		}

		.qqdownloader{
			background-image: url("../images/kfmonitor/qqdownload.jpg");
		}

		.qqlive{
			background-image: url("../images/kfmonitor/qqlive.jpg");
		}

		.qqmail{
			background-image: url("../images/kfmonitor/qqmail.jpg");
		}

		.qqmusic{
			background-image: url("../images/kfmonitor/qqmusic.jpg");
		}

		.qunar{
			background-image: url("../images/kfmonitor/qunar.jpg");
		}

		.sina{
			background-image: url("../images/kfmonitor/sina.jpg");
		}

		.speedtest{
			background-image: url("../images/kfmonitor/speedtest.jpg");
		}

		.taobao{
			background-image: url("../images/kfmonitor/taobao.jpg");
		}

		.ths{
			background-image: url("../images/kfmonitor/ths.jpg");
		}

		.tiktok{
			background-image: url("../images/kfmonitor/tiktok.jpg");
		}

		.toutiao{
			background-image: url("../images/kfmonitor/toutiao.png");
		}

		.txxw{
			background-image: url("../images/kfmonitor/txxw.jpg");
		}

		.wechat{
			background-image: url("../images/kfmonitor/wechat.jpg");
		}

		.weread{
			background-image: url("../images/kfmonitor/weread.jpg");
		}

		.wymail{
			background-image: url("../images/kfmonitor/wymail.jpg");
		}

		.wyxw{
			background-image: url("../images/kfmonitor/wyxw.jpg");
		}

		.wyymusic{
			background-image: url("../images/kfmonitor/wyymusic.jpg");
		}

		.wzry{
			background-image: url("../images/kfmonitor/wzry.jpg");
		}

		.xhs{
			background-image: url("../images/kfmonitor/xhs.jpg");
		}

		.youku{
			background-image: url("../images/kfmonitor/youku.png");
		}

		.all{
			background-image: url("../images/kfmonitor/traffic/all.png");
		}
		.browse{
			background-image: url("../images/kfmonitor/traffic/browse.png");
		}
		.download{
			background-image: url("../images/kfmonitor/traffic/download.png");
		}
		.game{
			background-image: url("../images/kfmonitor/traffic/game.png");
		}
		.mail{
			background-image: url("../images/kfmonitor/traffic/mail.png");
		}
		.message{
			background-image: url("../images/kfmonitor/traffic/message.png");
		}
		.navigation{
			background-image: url("../images/kfmonitor/traffic/navigation.png");
		}
		.pay{
			background-image: url("../images/kfmonitor/traffic/pay.png");
		}
		.speedtest{
			background-image: url("../images/kfmonitor/traffic/speedtest.png");
		}
		.video{
			background-image: url("../images/kfmonitor/traffic/video.png");
		}





		.leftArrow{
			position: absolute;
			z-index: 10;
			height: 30px;
			line-height: 30px;
			top: calc(50% + 30px - 30px);
			font-weight: bold;
			font-size: 30px;
			font-family: monospace;
			left: 5px;
			cursor: pointer;
			color: #cbcbcb;

		}
		.rightArrow{
			position: absolute;
			z-index: 10;
			height: 30px;
			line-height: 30px;
			top: calc(50% + 30px - 30px);
			font-weight: bold;
			font-size: 30px;
			font-family: monospace;
			right: 5px;
			cursor: pointer;
			color: #cbcbcb;
		}
	</style>
	<script type="text/javascript">
		$(function(){
			showPro('prov', 'city');
			initHeight();
			initTable();
			query();


		});

		/* 初始化高度 */
		function initHeight(){
			var highTotal = $(document.body).outerHeight();
			$("#tableDiv").height(highTotal* 0.3);
		}


		//省市联动效果
		function showPro(pro,city){
			$.ajax({
				url:"${pageContext.request.contextPath}/json/pca-code.json",
				dataType: 'json',
				success: function (jsonstr) {
					jsonstr.unshift({
						'code': '',
						'name': '全国'
					});
					$('#'+pro).combobox({
						/*data: jsonstr,*/
						valueField:'code',
						textField:'name',
						editable:false,
						onLoadSuccess:function (data){
							var data = $('#'+pro).combobox('getData');
							//console.log(JSON.stringify(data))
							if(data.length>0){
								$('#'+pro).combobox('select',data[0].code);
							}
						},
						onChange : function(newValue, oldValue) {
							if (newValue) {
								$('#'+city).combobox("clear");

								var flag = $("#"+city).prop('disabled');
								if (!flag) {
									showCity(newValue,city)
								}
							}else{
								$('#'+city).combobox("clear");
								$('#'+city).combobox("loadData", {});
							}
						}
					});
					$('#'+pro).combobox("loadData", jsonstr);
				}
			});
		}
		function showCity(newValue,city){
			$.ajax({
				url:"${pageContext.request.contextPath}/json/pca-code.json",
				dataType: 'json',
				success: function (json) {
					var cityJson = json;
					//console.log(JSON.stringify(cityJson))
					$.each(cityJson, function(i, val) {
						if(val.code == newValue){ //判断省份的code 是否与省份的val 相同
							//console.log(JSON.stringify(val))
							val.childs.unshift({
								'code': '',
								'name': '全省\\市'
							});
							$('#'+city).combobox({
								/*data: val.childs, */
								valueField:'code',
								textField:'name',
								editable:false,
								onLoadSuccess:function (data){
									var data = $('#'+city).combobox('getData');
									if(data.length>0){
										$('#'+city).combobox('select',data[0].code);
									}
								}
							});
							$('#'+city).combobox("loadData", val.childs);
						}
					})
				}
			})
		}

		function initTable(){
			//console.log(location.href.split('?')[1].split('&')[0])
			$("#mainTable").datagrid({

				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:false,
				// 行号
				rownumbers:true,
				onSelect:function (index){
					$("#mainTable").datagrid('unselectRow',index);
				},
				onClickRow:function (){
					$("#mainTable").datagrid('clearSelections');
				}
			});

		}

		var colorIndex = 0;
		function initPie(id,count,total){

			var colors = ['#bed768','#ff9800','#bb3450','#1c9c98','#475a9a','#bbad48','#ef6d58'];


			var progressPie = echarts.init(document.getElementById(id));
			var value = Math.round(count/total * 100) + '%';
			var option = {
				color: [colors[colorIndex],'#ffffff'],
				title: {
					text:value,
					left:'45%',
					top:'38%',
					textAlign:'center',
					textStyle:{
						color:'#797979',
						fontSize:14,
					},
				},
				series: [

					{
						name: '',
						type: 'pie',
						radius: ['60%', '70%'],
						center: ['50%', '50%'],
						emphasis: {
							scale:false
						},
						hoverAnimation:false,
						avoidLabelOverlap: false,
						label: {
							show: false,
							position: 'center'
						},
						labelLine: {
							//		show: false
						},
						data: [
							{ value: count, name: '数量' },
							{ value: total-count, name: '其他' },
						]
					}
				]
			};
			progressPie.setOption(option);

			colorIndex++;

			if(colorIndex>=colors.length){
				colorIndex = 0;
			}
		//	window.onresize = progressPie.resize;
		}


		function initBar(id,value,num){
			var color;
			var title;
			if(num == 1){
				color = '#8bc34a';
				title = 'Speed Experience';
			}else{
				color = '#ff9800';
				title = 'Response Delay Experience';
			}

			var chart = echarts.init(document.getElementById(id));
			var option = {
				title:{
					text:title,
					textStyle: {
						fontSize: 12,
						fontWeight:10,
					}
				},
				grid: {
					x:2,
					x2:40,
					y:90

				},
				xAxis:  {
					type: '',
					show : false,  //这个属性控制x坐标轴的显示隐藏
				},
				yAxis: {
					show : false,  //这个属性控制y坐标轴的显示隐藏
					type: 'category',

				},
				series: [
					{
						type: 'bar',
						data:[value],
						tooltip: { show: false},
						barWidth: 10, // 柱宽度
						barMaxWidth: 100,// 最大柱宽度
						z: 10, // 控制图表前后顺序
						itemStyle: { // 柱子样式
							normal: {
								barBorderRadius:[10,10,10,10],
								color: color, // 柱状图颜色
								label: {
									show: false
								}
							},
						}
					},
					{
						type: 'bar',
						data: [100],
						tooltip: { show: false},
						barWidth: 10,
						barMaxWidth: 100,
						barGap: '-100%', // 两个柱子之间的距离，如果要重叠设置为-100%
						itemStyle: {
							emphasis:{
								color:'#F0F2F5'
							},
							normal: {
								color: '#F0F2F5', // 柱子颜色，作为底层背景
								barBorderRadius:[10,10,10,10],
								label: {
									show: true,
									formatter: function(val){
										return value+'%';
									},
									position: 'right',
									testStyle: {
										color: '#000'
									}
								}
							}
						}
					}
				]
			};

			chart.setOption(option);

		}

		var trafficAll = 0;

		function query(){

			$.ajax({
				type:"GET",
				data:{
					beginDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue'),
					prov:$("#prov").combobox('getText').replace("全国",""),
					city:$("#city").combobox('getText').replace("全省\\市","").replace("市辖区","")
				},
				url:"${pageContext.request.contextPath}/knowFeelingMonitor/queryData.action",
				dataType:"json",//服务器响应的数据类型
				success:function(data){

					colorIndex = 0;
					$("#mainTable").datagrid("loadData",data);
					$(".tree-dnd-yes").removeClass("tree-dnd-yes");
					$(".tree-dnd-no").removeClass("tree-dnd-no");

					var param = {};
					if(data.thresholds){
						$.each(data.thresholds,function(i,item){
							param[item.thresholdType] = item.currentThreshold;
						});
					}


					if(data.diagnose){
						$.each(data.diagnose,function(k,v){

							var id = "#" + k.toUpperCase();

							var flag = true;
							if(param[k.toUpperCase()]){
								if ($(id).prevAll('div').html().indexOf("时延") > -1 || $(id).prevAll('div').html().indexOf("重传率") > -1) {
									if(v>param[k.toUpperCase()]){
										flag = false;
									}
								}else{
									if(v<param[k.toUpperCase()]){
										flag = false;
									}
								}
							}

							if ($(id).prevAll('div').html().indexOf("率") > -1) {
								if(v || v ==0){
									v +='%'
								}
							}
							$(id).html(v);

							if(v || v == 0){
								if(flag) {
									$(id).parent().find(".icon").addClass("tree-dnd-yes")
								}else{
									$(id).parent().find(".icon").addClass("tree-dnd-no")
								}
							}

						});
					}

					$("#box1 ul").empty();
					$("#box1 ul").animate({left:0});
					$("#box1 ul").attr('slide',0);

					if(data.traffic){
						var num = data.traffic.length > 3 ? data.traffic.length : 3;

						$("#box1 ul").width($("#box1 ul").parent().width()/3 * num );

						var width = $("#box1 ul").width()/num -10;
						$("#box1 ul").width((width + 10) * num );

						$.each(data.traffic,function(i,item){

							if(i == 0){
								trafficAll = item.count;
							}
							if(trafficAll>0){
								var li =
										'<li style="width:'+width+'px">' +
										'<div class="subTitle">' +
										'<div class="titleIcon">' +
											'<div class="trafficIcon '+item.icon+'"></div>'+
										'</div>' +
										'<div class="titleText">'+item.traffic +'</div>' +
										'</div>' +
										'<div class="content">' +
										'<div class="pieChart" id="chart'+item.icon+'">' +
										'</div>' +
										'<div class="speedText">' +
										'<div>下行&nbsp;'+item.dl+'&nbsp;Mbps</div>' +
										'<div>上行&nbsp;'+item.ul+'&nbsp;Mbps</div>' +
										'</div>' +
										'</div>' +
										'</li>';

								$("#box1 ul").append(li);

								initPie("chart"+item.icon,item.count,trafficAll);


								$("#box1 ul").attr('num',$("#box1 ul li").length);
							}
						});

					}else{
						$("#box1 ul").attr('num',0);
					}

					$("#box2 ul").empty();
					$("#box2 ul").animate({left:0});
					$("#box2 ul").attr('slide',0);
					if(data.app){
						var num = data.app.length > 3 ? data.app.length : 3;
						$("#box2 ul").width($("#box2 ul").parent().width()/3 * num );
						var width = Math.round($("#box2 ul").width()/num -10);
						$("#box2 ul").width((width + 10) * num );

						var all = 0;
						$.each(data.app,function(i,item){
							all += item.count;
						});

						$.each(data.app,function(i,item){

							var traffic =  Math.round(item.count/all * 100) ;


							var li = '<li style="width:'+width+'px">' +
										'<div class="subTitle">' +
											'<div class="appIcon '+item.icon+' " >' +

											'</div>' +
										'</div>' +
										'<div class="" style="height: 20px;padding:0 14px ">' +
											'<div style="font-size:12px;width: 50%;float:left;text-align:left">Traffic,Rate</div>' +
											'<div style="font-size:12px;width: 50%;float:left;text-align:right">'+item.dl+'Mbps/'+traffic+'%</div>' +
										'</div>' +
										'<div id="bar'+item.icon+'1" class="lineBar" >' +

										'</div>' +
										'<div id="bar'+item.icon+'2" class="lineBar">' +
										'</div>' +
									'</li>'

							$("#box2 ul").append(li);

							var speed = item.dl > 50 ? 100 :  Math.round(item.dl/50 * 100) ;
							initBar("bar"+item.icon+"1",speed,1);

							var delay;
							if(item.delay >= 1000){
								delay = 100;
							}else if(item.delay  <=50){
								delay = 0;
							}else{
								delay = Math.round( (item.delay -50) /950 * 100) ;
							}

							delay = 100 -delay;

							initBar("bar"+item.icon+"2",delay,2);

							$("#box2 ul").attr('num',$("#box2 ul li").length);
						});
					}else{
						$("#box2 ul").attr('num',0);
					}

					arrowControl();
				}
			});


		}

		function leftSlide(obj){
			var num =  $(obj).parent().find('ul').attr('num');

			if(num>3){
				var slide =  $(obj).parent().find('ul').attr('slide');
				if(num-3>slide){
					slide ++;
					var s = $(obj).parent().find('ul li').width() + 10;
					$(obj).parent().find('ul').animate({left: slide*-1*s });
					$(obj).parent().find('ul').attr('slide',slide);
				}
			}

			arrowControl();
		}

		function rightSlide(obj){
			var num =  $(obj).parent().find('ul').attr('num');

			if(num>3){
				var slide =  $(obj).parent().find('ul').attr('slide');
				if(slide > 0){
					slide--;
					var s = $(obj).parent().find('ul li').width() + 10;
					$(obj).parent().find('ul').animate({left: slide*-1*s });
					$(obj).parent().find('ul').attr('slide',slide);
				}
			}

			arrowControl();
		}

		function arrowControl(){
			var num1 = $("#box1 ul").attr('num');
			var slide1 = $("#box1 ul").attr('slide');

			if(num1 <=3){
				$("#box1 .leftArrow").hide();
				$("#box1 .rightArrow").hide();
			}else{
				if(slide1==0){
					$("#box1 .leftArrow").show();
					$("#box1 .rightArrow").hide();
				}else{
					$("#box1 .rightArrow").show();
					if(num1 - slide1 <=3){
						$("#box1 .leftArrow").hide();
					}else{
						$("#box1 .leftArrow").show();
					}
				}
			}

			var num2 = $("#box2 ul").attr('num');
			var slide2 = $("#box2 ul").attr('slide');

			if(num2 <=3){
				$("#box2 .leftArrow").hide();
				$("#box2 .rightArrow").hide();
			}else{
				if(slide2==0){
					$("#box2 .leftArrow").show();
					$("#box2 .rightArrow").hide();
				}else{
					$("#box2 .rightArrow").show();
					if(num2 - slide2 <=3){
						$("#box2 .leftArrow").hide();
					}else{
						$("#box2 .leftArrow").show();
					}
				}
			}

		}


		function exportExcel(){

			var printApplyForm = $("<form method='post' action='${pageContext.request.contextPath}/knowFeelingMonitor/exportExcelData.action'  id='printApplyForm' target='_blank' ></form>");

			var beginDate = $("#beginDate").datetimebox('getValue');
			var	endDate = $("#endDate").datetimebox('getValue');
			var prov = $("#prov").combobox('getText').replace("全国","");
			var	city = $("#city").combobox('getText').replace("全省\\市","").replace("市辖区","");

			var input1 =  $("<input type='hidden' name='beginDate' value='"+beginDate+"' />");
			var input2 =  $("<input type='hidden' name='endDate' value='"+endDate+"' />");
			var input3 =  $("<input type='hidden' name='prov' value='"+prov+"' />");
			var input4 =  $("<input type='hidden' name='city' value='"+city+"' />");

			printApplyForm.append(input1);
			printApplyForm.append(input2);
			printApplyForm.append(input3);
			printApplyForm.append(input4);
			printApplyForm.css('display','none');

			$('body').append(printApplyForm);
			$("#printApplyForm").submit();
			$("#printApplyForm").remove();
		}


	</script>
</head>
<body style="width: 100%;margin: 0 auto;padding: 0;list-style:none;overflow:auto;">


	<div class="inputDivShow">

		<select id="prov" name="prov" class="easyui-combobox" style="width:140px;"></select>
		<select id="city" name="city" class="easyui-combobox" style="width:140px;"></select>
		&nbsp;&nbsp;
		时间
		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="editable:false" />
		~
		<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="editable:false" />
		&nbsp;&nbsp;
		<a class="easyui-linkbutton" onclick="query();" style="width: 70px;margin-right: 5px;" data-options="iconCls:'icon-search'" >查询</a>
	</div>

	<div class="picContainer">
		<div id="box1" class="w1200">
			<div class="title">总体质量</div>
			<ul></ul>
			<div class="leftArrow" onclick="leftSlide(this)">
				<
			</div>
			<div class="rightArrow" onclick="rightSlide(this)">
				>
			</div>
		</div>

		<div id="box2" class="w1200">
			<div class="title">APP质量</div>
			<ul>
			</ul>
			<div class="leftArrow" onclick="leftSlide(this)">
				<
			</div>
			<div class="rightArrow" onclick="rightSlide(this)">
				>
			</div>
		</div>
	</div>



	<div class="title" style="float: left">详细信息</div>
	<a id="export" class="easyui-linkbutton" data-options="plain:false,disabled:false"  onclick="exportExcel();" style="margin-right: 10px;width:100px;float: right">导出</a>

		<div id="tableDiv" style="width:100%;margin-right: 10px;">
			<table id="mainTable" style="" >
				<thead frozen="true">
				<tr>
					<th field='prov' width="100px" style="text-align:center">省</th>
					<th field='city' width="100px" style="text-align:center">市</th>
					<th field='operator_name' width="100px" style="text-align:center">运营商</th>
					<th field='kf01' width="100px" style="text-align:center">网络制式</th>
					<th field='kf02' width="100px" style="text-align:center">RSRP</th>
					<th field='kf03' width="100px" style="text-align:center">SINR</th>
				</tr>
				</thead>
				<thead>
			<%--	<tr>
					<th colspan="1">即时通信</th>
					<th colspan="1">视频</th>
					<th colspan="1">浏览</th>
					<th colspan="1">测速</th>
					<th colspan="1">下载</th>
					<th colspan="1">支付</th>
					<th colspan="1">游戏</th>
					<th colspan="1">导航</th>
					<th colspan="1">邮箱</th>
				</tr>--%>
				<tr>
					<th field='message' width="100px" style="text-align:center">即时通信</th>
					<th field='video' width="100px" style="text-align:center">视频</th>
					<th field='browse' width="100px" style="text-align:center">浏览</th>
					<th field='speed' width="100px" style="text-align:center">测速</th>
					<th field='download' width="100px" style="text-align:center">下载</th>
					<th field='pay' width="100px" style="text-align:center">支付</th>
					<th field='game' width="80px" style="text-align:center">游戏</th>
					<th field='navigation' width="80px" style="text-align:center">导航</th>
					<th field='mail' width="80px" style="text-align:center">邮箱</th>
				</tr>
				</thead>
			</table>
		</div>


	<div>
		<div class="title">诊断信息</div>
		<div class="diagnose">
			<div class="dTitle">物理层</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">LTE覆盖</div>
				<div id="KF02" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">NR覆盖</div>
				<div id="KF04" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">LTE RRC连接成功率</div>
				<div id="KF06" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">NR RRC连接成功率</div>
				<div id="KF07" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">LTE切换成功率</div>
				<div id="KF08" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">NR切换成功率</div>
				<div id="KF09" class="value"></div>
			</div>

		</div>
		<div class="diagnose">
			<div class="dTitle">链路层</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">DNS解析时延</div>
				<div id="KF11" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">DNS解析成功率</div>
				<div id="KF12" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">TCP建链时延</div>
				<div id="KF13" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">TCP建链成功率</div>
				<div id="KF14" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">TCP重传率</div>
				<div id="KF15" class="value"></div>
			</div>

		</div>
		<div class="diagnose">
			<div class="dTitle">应用层</div>

			<div class="box">
				<div class="icon"></div>
				<div class="text">HTTP响应时延</div>
				<div id="KF16" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">HTTP响应成功率</div>
				<div id="KF17" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">文本加载时延</div>
				<div id="KF21" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">图片加载时延</div>
				<div id="KF22" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">音频加载时延</div>
				<div id="KF23" class="value"></div>
			</div>
			<div class="box">
				<div class="icon"></div>
				<div class="text">视频加载时延</div>
				<div id="KF24" class="value"></div>
			</div>
		</div>
	</div>



</body>
</html>