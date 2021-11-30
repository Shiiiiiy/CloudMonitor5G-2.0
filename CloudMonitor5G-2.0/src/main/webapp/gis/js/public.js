/**
 * Created by zhaobenfu on 2015/9/8.
 */
function InitTools(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	}
	return null;
}
dojo.require("dojo.io.script");
//读取xml文件
function loadXML(flag, xml) {
	var xmlDoc;
	//针对IE浏览器
	if (window.ActiveXObject) {
		var aVersions = [ "MSXML2.DOMDocument.6.0", "MSXML2.DOMDocument.5.0",
				"MSXML2.DOMDocument.4.0", "MSXML2.DOMDocument.3.0",
				"MSXML2.DOMDocument", "Microsoft.XmlDom" ];
		for (var i = 0; i < aVersions.length; i++) {
			try {
				//建立xml对象
				xmlDoc = new ActiveXObject(aVersions[i]);
				break;
			} catch (oError) {
			}
		}
		if (xmlDoc != null) {
			//同步方式加载XML数据
			xmlDoc.async = false;
			//根据XML文档名称装载
			if (flag == true) {
				xmlDoc.load(xml);
			} else {
				//根据表示XML文档的字符串装载
				xmlDoc.loadXML(xml);
			}
			//返回XML文档的根元素节点。
			return xmlDoc.documentElement;
		}
	} else {
		//针对非IE浏览器
		if (document.implementation && document.implementation.createDocument) {
			/*
			 第一个参数表示XML文档使用的namespace的URL地址
			 第二个参数表示要被建立的XML文档的根节点名称
			 第三个参数是一个DOCTYPE类型对象，表示的是要建立的XML文档中DOCTYPE部分的定义，通常我们直接使用null
			 这里我们要装载一个已有的XML文档，所以首先建立一个空文档，因此使用下面的方式
			 */
			xmlDoc = document.implementation.createDocument("", "", null);
			if (xmlDoc != null) {
				//根据XML文档名称装载
				if (flag == true) {
					//同步方式加载XML数据
					xmlDoc.async = false;
					xmlDoc.load(xml);
				} else {
					//根据表示XML文档的字符串装载
					var oParser = new DOMParser();
					xmlDoc = oParser.parseFromString(xml, "text/xml");
				}
				//返回XML文档的根元素节点。
				return xmlDoc.documentElement;
			}
		}
	}
	return null;
}
//激活工具栏

function DistanceByTwopoints() {
	map.setMapCursor("url(images/distance.cur),auto");
	handle = dojo.connect(map, "onClick", mapClickHandler);
	handle_double = dojo.connect(map, "onDblClick", mapDoubleClickHandler);
}
//清除地图要素
function mapClear() {
	map.graphics.clear();
	// 地图测距取消
	map.setMapCursor("default");
	inputPoints = [];
	totalDistance = 0;
	dojo.disconnect(handle);
	dojo.disconnect(handle_double);
	//选点取消
	dojo.disconnect(selectLoction_double);
	navToolbar.deactivate();
	if (undefined != graphicLayerRectangle
			&& null != graphicLayerRectangle) {
		map.removeLayer(graphicLayerRectangle);
		graphicLayerRectangle = null;
		if (undefined != tb
				&& null != tb) {
			 tb.deactivate();
		}
	}
	// tb.deactivate();
}
// 两点距离测算
var handle;
var handle_double;


var inputPoints = [];
var endGraphic;
var totalDistance = 0;
function mapDoubleClickHandler(evt) {
	var font = new esri.symbol.Font("18px", esri.symbol.Font.STYLE_NORMAL,
			esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
	var markerSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 6,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
							204, 102, 51 ]), 1), new dojo.Color([ 158, 184, 71,
					0.65 ]));
	var polylineSymbol = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([ 255, 0,
					0 ]), 1);
	map.setMapCursor("default");
	var inPoint = new esri.geometry.Point(evt.mapPoint.x, evt.mapPoint.y,
			map.spatialReference);
	inputPoints.push(inPoint);
	if (inputPoints.length >= 2) {
		var distParams = new esri.tasks.DistanceParameters();
		distParams.distanceUnit = esri.tasks.GeometryService.UNIT_METER;
		distParams.geometry1 = inputPoints[inputPoints.length - 2];
		distParams.geometry2 = inputPoints[inputPoints.length - 1];
		distParams.geodesic = true;
		var geometryService = new esri.tasks.GeometryService(geometry_url);
		geometryService.distance(distParams, function(distance) {
			if (isNaN(distance)) {
				distance = 0;
			}
			var s = dojo.number.format(distance, {
				places : 2
			});
			if (distance > 1000) {
				s = dojo.number.format(distance / 1000, {
					places : 2
				}) + 'km';
			} else {
				s = s + 'm';
			}
			var textSymbol = new esri.symbol.TextSymbol(s, font, new dojo.Color([
					255, 0, 0 ]));
			totalDistance += distance;
			//dojo.byId('distanceDetails').innerHTML = content;
			var polyline = new esri.geometry.Polyline(map.spatialReference);
			polyline.addPath([ distParams.geometry1, distParams.geometry2 ]);
			map.graphics.add(new esri.Graphic(polyline, polylineSymbol));
			var total_dis;
			if (totalDistance > 1000) {
				total_dis = dojo.number.format(totalDistance / 1000, {
					places : 2
				}) + "km";
			} else {
				total_dis = dojo.number.format(totalDistance, {
					places : 2
				}) + "m";
			}
			textSymbol = new esri.symbol.TextSymbol("总长:" + total_dis, font,
					new dojo.Color([ 255, 0, 0 ]));
			textSymbol.yoffset = 18;
			textSymbol.xoffset = 20;
			map.graphics.add(new esri.Graphic(evt.mapPoint, textSymbol));
			map.graphics.add(new esri.Graphic(evt.mapPoint, markerSymbol));
			dojo.disconnect(handle);
			dojo.disconnect(handle_double);
			inputPoints = [];
			totalDistance = 0;
		});
	}
}
function mapClickHandler(evt) {
	var font = new esri.symbol.Font("18px", esri.symbol.Font.STYLE_NORMAL,
			esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
	var markerSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 6,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
							204, 102, 51 ]), 1), new dojo.Color([ 158, 184, 71,
					0.65 ]));
	var polylineSymbol = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([ 255, 0,
					0 ]), 1);
	var inPoint = new esri.geometry.Point(evt.mapPoint.x, evt.mapPoint.y,
			map.spatialReference);
	inputPoints.push(inPoint);
	//define the symbology for the graphics
	if (inputPoints.length === 1) { //start location label
		var textSymbol = new esri.symbol.TextSymbol("起点", font, new dojo.Color([
				255, 0, 0 ]));
		textSymbol.yoffset = 18;
		textSymbol.xoffset = 10;
		map.graphics.add(new esri.Graphic(evt.mapPoint, textSymbol));
	}
	if (inputPoints.length >= 2) { //end location label
		var distParams = new esri.tasks.DistanceParameters();
		distParams.distanceUnit = esri.tasks.GeometryService.UNIT_METER;
		distParams.geometry1 = inputPoints[inputPoints.length - 2];
		distParams.geometry2 = inputPoints[inputPoints.length - 1];
		distParams.geodesic = true;
		//draw a polyline to connect the input points
		var polyline = new esri.geometry.Polyline(map.spatialReference);
		polyline.addPath([ distParams.geometry1, distParams.geometry2 ]);
		map.graphics.add(new esri.Graphic(polyline, polylineSymbol));
		var geometryService = new esri.tasks.GeometryService(geometry_url);
		geometryService.distance(distParams, function(distance) {
			if (isNaN(distance)) {
				distance = 0;
			}
			var s = dojo.number.format(distance, {
				places : 2
			});
			if (distance > 1000) {
				s = dojo.number.format(distance / 1000, {
					places : 2
				}) + 'km';
			} else {
				s = s + 'm';
			}
			textSymbol = new esri.symbol.TextSymbol(s, font, new dojo.Color([
					255, 0, 0 ]));
			totalDistance += distance;
			var content = "";
			textSymbol.yoffset = 8;
			textSymbol.xoffset = 8;
			endGraphic = new esri.Graphic(evt.mapPoint, textSymbol);
			map.graphics.add(endGraphic);
			//dojo.byId('distanceDetails').innerHTML = content;
		});

	}
	//add a graphic for the clicked location
	map.graphics.add(new esri.Graphic(evt.mapPoint, markerSymbol));
}
function AddpointSymbol() {
	var n = 10000;
	var x = 112.11;
	var y = 23.31;
	var graphiclayer = new esri.layers.GraphicsLayer();
	var graphiclayer1 = new esri.layers.GraphicsLayer();
	map.addLayer(graphiclayer1);
	map.addLayer(graphiclayer);
	var symbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 10,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
							255, 0, 0 ]), 1), new dojo.Color(
					[ 0, 255, 0, 0.25 ]));
	var symbol1 = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 10,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
							0, 255, 0 ]), 1), new dojo.Color(
					[ 0, 255, 0, 0.25 ]));
	for (var i = 0; i < n; i++) {
		var location = new esri.geometry.Point(x + Math.random(), y
				+ Math.random(), map.spatialReference);
		var location1 = new esri.geometry.Point(x + Math.random(), y
				+ Math.random(), map.spatialReference);
		graphiclayer.add(new esri.Graphic(location, symbol));
		graphiclayer1.add(new esri.Graphic(location1, symbol1));
	}
}
//GPS轨迹点显示
var GpsHistoryLine;
var GpsHistorySymbol;
function RandomPoints(k) {
	var t = [], kk = 0;
	for (var i = 0; i < k; i++) {
		var cc = {};
		cc.Lon = 112.11 + Math.random();
		cc.Lat = 23.31 + Math.random();
		cc.kpi = Math.random();
		cc.Azmith = Math.round(Math.random() * 360);
		t.push(cc);
	}
	return t;
}

var divN_content = null;
function NCreateDiv(div1) {
	var mydiv = document.createElement('div');
	mydiv.setAttribute("id", "itemdiv" + Math.floor(Math.random() * 1000) + 1);
	divN_content = mydiv;
	document.getElementById(div1).appendChild(divN_content);
	return divN_content;
}
//  动态删除Div
function NDeleteDiv(div1) {
	// document.getElementById('timeInfo').childNodes[1].removeNode(true);
	if (document.getElementById(divN_content.id) == null) {
		return;
	}
	document.getElementById(div1).removeChild(
			document.getElementById(divN_content.id));
}
//获取指标
function GetTagetIndex() {
	$("#dropDownButtonContainer").html("");
	if (document.getElementById("dropDownButtonContainer1") != null) {
		document.getElementById("dropDownButtonContainer").removeChild(
				document.getElementById("dropDownButtonContainer1"));
	}
	var mydiv = document.createElement('div');
	mydiv.setAttribute("id", "dropDownButtonContainer1");
	document.getElementById("dropDownButtonContainer").appendChild(mydiv);
	var myDialog = new dijit.TooltipDialog(
			{
				content : '<label for="select_index">选择指标:</label>'
						+ '<select   data-dojo-type="dijit/form/Select"  id="select_index" name="name">'
						+ '<option value="MOS值">MOS值</option><option value="LTE网络RSRP" selected="selected">LTE网络RSRP</option></select><br>'
						+ '<button data-dojo-type="dijit/form/Button" type="submit">刷新</button>'
						+ '<button data-dojo-type="dijit/form/Button" type="button">清除</button>'
			});
	var myButton = new dijit.form.DropDownButton({
		label : "地图显示指标",
		dropDown : myDialog
	});
	dojo.byId("dropDownButtonContainer1").appendChild(myButton.domNode);
	myButton.startup();
}
//设置透明度
function SetOpacity() {
	$("#slider").html("");
	if (divN_content != null) {
		NDeleteDiv("slider");
	}
	NCreateDiv("slider");
	var rule = new dijit.form.HorizontalRule({
		count : 10,
		style : "height:3px"
	});
	var ruleLabels = new dijit.form.HorizontalRuleLabels({
		labels : [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" ],
		style : "height:1em;font-size:75%;color:gray;"
	});
	var slider = new dijit.form.HorizontalSlider({
		name : "slider",
		value : 0,
		minimum : 0,
		step : 1,
		maximum : 10,
		intermediateChanges : true,
		style : "width:300px;",
		onChange : function(value) {

		}
	}, divN_content.id);
	slider.addChild(rule);
	slider.addChild(ruleLabels);
	slider.startup();
}
//轨迹点显示
var GpsLayer;
function DisplayGpsHistory() {
	var cc = RandomPoints(1000);
	//请求数据
	if (GpsHistorySymbol != null) {
		map.graphics.remove(GpsHistorySymbol);
	}
	var GpsHistory = cc;
	GpsHistoryLine = new esri.geometry.Polyline(map.spatialReference);
	var GpsPoints = [];
	for (var i = 0; i < GpsHistory.length; i++) {
		var x = GpsHistory[i].Lon;
		var y = GpsHistory[i].Lat;
		var pt = new esri.geometry.Point(x, y, map.spatialReference);
		var kpi = GpsHistory[i].kpi;
		var PointSymbol;
		if (kpi > 0.5) {
			PointSymbol = new esri.symbol.SimpleMarkerSymbol({
				"color" : [ 255, 255, 255, 255 ],
				"size" : 6,
				"xoffset" : 0,
				"yoffset" : 0,
				"type" : "esriSMS",
				"style" : "esriSMSCircle",
				"outline" : {
					"color" : [ 0, 0, 0, 255 ],
					"width" : 1,
					"type" : "esriSLS",
					"style" : "esriSLSSolid"
				}
			})
		} else {
			if (kpi > 0.3) {
				PointSymbol = new esri.symbol.SimpleMarkerSymbol({
					"color" : [ 255, 25, 25, 255 ],
					"size" : 6,
					"xoffset" : 0,
					"yoffset" : 0,
					"type" : "esriSMS",
					"style" : "esriSMSCircle",
					"outline" : {
						"color" : [ 0, 0, 0, 255 ],
						"width" : 1,
						"type" : "esriSLS",
						"style" : "esriSLSSolid"
					}
				})

			} else {
				PointSymbol = new esri.symbol.SimpleMarkerSymbol({
					"color" : [ 125, 55, 255, 64 ],
					"size" : 6,
					"xoffset" : 0,
					"yoffset" : 0,
					"type" : "esriSMS",
					"style" : "esriSMSCircle",
					"outline" : null
				})
			}
		}
		var Graphic = new esri.Graphic(pt, PointSymbol, null, null);
		map.graphics.add(Graphic);

	}
	var symbol = new esri.symbol.PictureMarkerSymbol({
		"url" : "images/arrow_up.png",
		"height" : 16,
		"width" : 16,
		"xoffset" : 20,
		"yoffset" : 20,
		"type" : "esriPMS",
		"angle" : -Math.round(Math.random() * 360)
	});
	var startGraphic = new esri.Graphic(pt, symbol, null, null);
	map.graphics.add(startGraphic);
	GpsHistoryLine.addPath(GpsPoints);
	var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([ 255,
					125, 125 ]), 2);
	GpsHistorySymbol = new esri.Graphic(GpsHistoryLine, GpsLineSymbol, null,
			null);
	map.graphics.add(GpsHistorySymbol);
}

function Dialog(title, content, footer) {
	document.getElementById("dialog_content").innerHTML = (content);
	document.getElementById("dialog_title").innerHTML = (title);
	document.getElementById("dialog_footer").innerHTML = (footer);
	$("#dialog_div").show();
}
function ContentShow() {
	var $a = $("#dialog_content");
	if ($a.is(":hidden")) {
		$a.css("display", "block");
	} else {
		$a.css("display", "none");
	}
}

var tb,graphicLayerRectangle;
//左上角经纬度
var rectAngleLeftLon,rectAngleLeftLat;
//右下角经纬度
var rectAngleRightLon,rectAngleRightLat;

//画区域矩形框
function drawRectangleFrame(){
	if (undefined == graphicLayerRectangle
			|| null == graphicLayerRectangle) {
		require(["esri/toolbars/draw"], function (Draw) {
			//var toolbar = new Draw();
			//使用toolbar上的绘图工具
			tb = new Draw(map);
			graphicLayerRectangle = new esri.layers.GraphicsLayer();
			map.addLayer(graphicLayerRectangle);
			dojo.connect(tb, "onDrawEnd", addGraphic);
			
			tb.activate(esri.toolbars.Draw.EXTENT);
	    })
	} else {
		map.removeLayer(graphicLayerRectangle);
		graphicLayerRectangle = null;
		if (undefined != tb
				&& null != tb) {
			tb.deactivate();
		}
	}
}

function addGraphic(geometry) {
    console.log(geometry);
    rectAngleLeftLon = geometry.xmin;
    rectAngleLeftLat = geometry.ymax;
    rectAngleRightLon = geometry.xmax;
    rectAngleRightLat = geometry.ymin;
    graphicLayerRectangle.clear();
    symbol = tb.fillSymbol;
    graphicLayerRectangle.add(new esri.Graphic(geometry, symbol));
}

//根据左上角和右下角经纬度画矩形框区域
function showRectAngle() {
	if(rectAngleLeftLon!=null && rectAngleLeftLon!=undefined && rectAngleLeftLon!=""){
		var symbol = new esri.symbol.SimpleFillSymbol().setColor(null).setOutline(
				new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new esri.Color("blue"), 2));
		var extent = new esri.geometry.Extent(rectAngleLeftLon, rectAngleRightLat, rectAngleRightLon, rectAngleLeftLat,
			new esri.SpatialReference({
				wkid : 4326
			}));
		
		if (undefined != graphicLayerRectangle
				&& null != graphicLayerRectangle) {
			map.removeLayer(graphicLayerRectangle);
			if (undefined != tb
					&& null != tb) {
				tb.deactivate();
			}
		}
		
		graphicLayerRectangle = new esri.layers.GraphicsLayer({
			id : "extent"
		});
		map.addLayer(graphicLayerRectangle);
		var graphic = new esri.Graphic(extent, symbol);
		graphicLayerRectangle.add(graphic);
	}
}

var selectLoction_double;
var graphicLayerPointText;
var graphicLayerPointSymbol;
//双击地图获取经纬度坐标并标注
function getPointSymbol(){
	map.setMapCursor("pointer");
	handle_double = dojo.connect(map, "onDblClick", mapDlClickSelectLoction);
}

function mapDlClickSelectLoction(evt) {
	map.graphics.clear();
	var font = new esri.symbol.Font("18px", esri.symbol.Font.STYLE_NORMAL,
			esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
	var markerSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 6,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
							204, 102, 51 ]), 1), new dojo.Color([ 158, 184, 71,
					0.65 ]));
	map.setMapCursor("default");
	var inPoint = new esri.geometry.Point(evt.mapPoint.x, evt.mapPoint.y,
			map.spatialReference);
	
	var text = "经度:" + evt.mapPoint.x.toFixed(6) + ",纬度:"+ evt.mapPoint.y.toFixed(6);
	var textSymbol = new esri.symbol.TextSymbol(text, font, new dojo.Color([
			255, 0, 0 ]));
	textSymbol.yoffset = 18;
	textSymbol.xoffset = 10;
	
	graphicLayerPointText = new esri.Graphic(evt.mapPoint, textSymbol);
	graphicLayerPointSymbol = new esri.Graphic(evt.mapPoint, markerSymbol);
	map.graphics.add(graphicLayerPointText);
	map.graphics.add(graphicLayerPointSymbol);

	//把坐标输入到input框中
	parent.inputSelctLocation(evt.mapPoint.x.toFixed(6),evt.mapPoint.y.toFixed(6));
}

//选择线路点
var isConnected = 0;
var sid = null;
var stationName = null;
var graphicClick1;
var graphicClick2;
function selectLinePointSymbol(){
	var lineLoctionTye = parent.getLineLoctionTye();
	if(lineLoctionTye==null){
		return;
	}
	if(graphicClick1!=undefined &&  graphicClick1!=null){
		dojo.disconnect(graphicClick1);
	}
	if(graphicClick2!=undefined &&  graphicClick2!=null){
		dojo.disconnect(graphicClick2);
	}
	if(isConnected==0){
		map.setMapCursor("pointer");
		if(lineLoctionTye == 0){
			graphicClick1 = dojo.connect(map, "onClick", mapDlClickSelectStationPoint);
		}if(lineLoctionTye == 1){
			graphicClick2 = dojo.connect(map, "onClick", mapDlClickAddLinePoint);
		}
		isConnected = 1;
	}else if(isConnected==1){
		isConnected = 0;
	}
}

function mapDlClickSelectStationPoint(evt) {
	var stationInfoMap = parent.getStationInfo();
	sid = stationInfoMap["sid"];
	stationName = stationInfoMap["stationName"];

	if (undefined == stationGraphicsLayer
		|| null == stationGraphicsLayer) {
		stationGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(stationGraphicsLayer);
	} else {
		var stationGraphicArry =[];//用于承接点的graphic对象
		dojo.forEach(stationGraphicsLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var pointTag = feature.attributes.sid;//point对象
			if (sid == pointTag) {
				stationGraphicArry.push(feature);
			}
		})
		for (let j = 0; j <stationGraphicArry.length; j++) {
			stationGraphicsLayer.remove(stationGraphicArry[j]);
		}
	}
	if (undefined == stationLabelLayer
		|| null == stationLabelLayer) {
		stationLabelLayer = new esri.layers.GraphicsLayer();
		map.addLayer(stationLabelLayer);
	} else {
		var stationGraphicArry =[];//用于承接点的graphic对象
		dojo.forEach(stationLabelLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var pointTag = feature.attributes.sid;//point对象
			if (sid == pointTag) {
				stationGraphicArry.push(feature);
			}
		})
		for (let j = 0; j <stationGraphicArry.length; j++) {
			stationLabelLayer.remove(stationGraphicArry[j]);
		}
	}

	var font = new esri.symbol.Font("13px", esri.symbol.Font.STYLE_NORMAL,
		esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
	var markerSymbol = new esri.symbol.SimpleMarkerSymbol(
		esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 6,
		new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
				204, 102, 51 ]), 1), new dojo.Color([ 158, 184, 71,
			0.65 ]));
	map.setMapCursor("default");
	var inPoint = new esri.geometry.Point(evt.mapPoint.x, evt.mapPoint.y,
		map.spatialReference);

	var text = stationName;
	var textSymbol = new esri.symbol.TextSymbol(text, font, new dojo.Color([
		255, 0, 0 ]));
	textSymbol.yoffset = 10;
	textSymbol.xoffset = 20;

	graphicLayerPointText = new esri.Graphic(evt.mapPoint, textSymbol, {
		"sid" : sid,
		"stationName" : stationName,
		'lon' :evt.mapPoint.x,
		'lat' :evt.mapPoint.y,
	}, null);
	graphicLayerPointSymbol = new esri.Graphic(evt.mapPoint, markerSymbol, {
		"sid" : sid,
		"stationName" : stationName,
		'lon' :evt.mapPoint.x.toFixed(6),
		'lat' :evt.mapPoint.y.toFixed(6),
	}, null);
	stationGraphicsLayer.add(graphicLayerPointSymbol);
	stationLabelLayer.add(graphicLayerPointText);

	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>序号:</td> " +
		"<td style='text-align:left;'>${sid}</td> " +
		"<td style='text-align:left;'>站点名称:</td> " +
		"<td style='text-align:left;'>${stationName}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>经度:</td><td style='text-align:left;'>${lon}</td> " +
		"<td style='text-align:left;'>纬度:</td><td style='text-align:left;'>${lat}</td> " +
		"</tr> " +
		"</table>");
	stationGraphicsLayer.setInfoTemplate(infoTemplate);

	//把坐标输入到input框中
	parent.inputSelctLocation(evt.mapPoint.x.toFixed(6),evt.mapPoint.y.toFixed(6));
}

var stationGraphicsLayer;
var stationLabelLayer;
function stationLocationDisplay(stops){
	dojo.disconnect(graphicClick1);
	if (undefined == stationGraphicsLayer
		|| null == stationGraphicsLayer) {
		stationGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(stationGraphicsLayer);
	} else {
		stationGraphicsLayer.clear();
	}
	if (undefined == stationLabelLayer
		|| null == stationLabelLayer) {
		stationLabelLayer = new esri.layers.GraphicsLayer();
		map.addLayer(stationLabelLayer);
	} else {
		stationLabelLayer.clear();
	}
	var multipoint = new esri.geometry.Multipoint(new esri.SpatialReference({wkid:4326}));
	for (let i = 0; i < stops.length; i++) {
		var font = new esri.symbol.Font("13px", esri.symbol.Font.STYLE_NORMAL,
			esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);
		var markerSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 6,
			new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
					204, 102, 51 ]), 1), new dojo.Color([ 158, 184, 71,
				0.65 ]));
		map.setMapCursor("default");
		var inPoint = new esri.geometry.Point(stops[i].lon, stops[i].lat,
			map.spatialReference);

		var text = stops[i].name;
		var textSymbol = new esri.symbol.TextSymbol(text, font, new dojo.Color([
			255, 0, 0 ]));
		textSymbol.yoffset = 10;
		textSymbol.xoffset = 20;

		graphicLayerPointText = new esri.Graphic(inPoint, textSymbol, {
			"sid" : i,
			"stationName" : text,
			'lon' :stops[i].lon,
			'lat' :stops[i].lat,
		}, null);
		graphicLayerPointSymbol = new esri.Graphic(inPoint, markerSymbol, {
			"sid" : i,
			"stationName" : text,
			'lon' :stops[i].lon,
			'lat' :stops[i].lat,
		}, null);
		stationGraphicsLayer.add(graphicLayerPointSymbol);
		stationLabelLayer.add(graphicLayerPointText);

		var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
			"<tr class='tr1'> " +
			"<td style='text-align:left;'>序号:</td> " +
			"<td style='text-align:left;'>${sid}</td> " +
			"<td style='text-align:left;'>站点名称:</td> " +
			"<td style='text-align:left;'>${stationName}</td> " +
			"</tr> " +
			"<tr class='tr2'> " +
			"<td style='text-align:left;'>经度:</td><td style='text-align:left;'>${lon}</td> " +
			"<td style='text-align:left;'>纬度:</td><td style='text-align:left;'>${lat}</td> " +
			"</tr> " +
			"</table>");
		stationGraphicsLayer.setInfoTemplate(infoTemplate);

		multipoint.addPoint(inPoint);
		mapDataExtent = multipoint.getExtent();
		map.setExtent(mapDataExtent);
	}
}

var pointGraphicsLayer;
function mapDlClickAddLinePoint(evt) {
	if (undefined == pointGraphicsLayer
		|| null == pointGraphicsLayer) {
		pointGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(pointGraphicsLayer);
	}
	var stationInfoMap = parent.getStationInfo();
	sid = stationInfoMap["sid"];
	stationName = stationInfoMap["stationName"];
	var markerSymbol = new esri.symbol.SimpleMarkerSymbol(
		esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 6,
		new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([
				0,0,0 ]), 1), new dojo.Color([ 	0,0,0,
			0.65 ]));
	map.setMapCursor("default");

	let graphicLayerSymbol1 = new esri.Graphic(evt.mapPoint, markerSymbol, {
		"sid" : sid,
		"stationName" : stationName,
		'lon' :evt.mapPoint.x.toFixed(6),
		'lat' :evt.mapPoint.y.toFixed(6),
	}, null);
	pointGraphicsLayer.add(graphicLayerSymbol1);

	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>序号:</td> " +
		"<td style='text-align:left;'>${sid}</td> " +
		"<td style='text-align:left;'>站点名称:</td> " +
		"<td style='text-align:left;'>${stationName}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>经度:</td><td style='text-align:left;'>${lon}</td> " +
		"<td style='text-align:left;'>纬度:</td><td style='text-align:left;'>${lat}</td> " +
		"</tr> " +
		"</table>");
	pointGraphicsLayer.setInfoTemplate(infoTemplate);

	//把坐标输入到input框中
	// parent.inputSelctLocation(evt.mapPoint.x.toFixed(6),evt.mapPoint.y.toFixed(6));
	// dojo.disconnect(handle);
}


//框选区域
function frameSelect(){
	if (undefined == graphicLayerRectangle
		|| null == graphicLayerRectangle) {
		require(["esri/toolbars/draw"], function (Draw) {
			//var toolbar = new Draw();
			//使用toolbar上的绘图工具
			tb = new Draw(map);
			graphicLayerRectangle = new esri.layers.GraphicsLayer();
			map.addLayer(graphicLayerRectangle);
			dojo.connect(tb, "onDrawEnd", addGraphic);

			tb.activate(esri.toolbars.Draw.EXTENT);
		})
	} else {
		map.removeLayer(graphicLayerRectangle);
		graphicLayerRectangle = null;
		if (undefined != tb
			&& null != tb) {
			tb.deactivate();
		}
	}
}

function addGraphic(geometry) {
	// console.log(geometry);
	// rectAngleLeftLon = geometry.xmin;
	// rectAngleLeftLat = geometry.ymax;
	// rectAngleRightLon = geometry.xmax;
	// rectAngleRightLat = geometry.ymin;
	graphicLayerRectangle.clear();
	symbol = tb.fillSymbol;
	graphicLayerRectangle.add(new esri.Graphic(geometry, symbol));
}

function delLayer() {
	var graphicArry =[];//用于承接点的graphic对象
	dojo.forEach(graphicLayerRectangle.graphics, function (graphic) {//循环遍历每一个多边形graphic对象
		var polygon = graphic.geometry;//polygon为geometry对象，用来提取出graphic对象中的geometry对象
		dojo.forEach(pointGraphicsLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var point = feature.geometry;//point对象
			if (polygon.contains(point)) {//判断点是否在多边形内，返回值为Boolean
				graphicArry.push(feature);
			}
		})
	});
	for (let j = 0; j <graphicArry.length; j++) {
		pointGraphicsLayer.remove(graphicArry[j]);
	}
	navToolbar.deactivate();
	if (undefined != graphicLayerRectangle
		&& null != graphicLayerRectangle) {
		map.removeLayer(graphicLayerRectangle);
		graphicLayerRectangle = null;
		if (undefined != tb
			&& null != tb) {
			tb.deactivate();
		}
	}
}

function delStation(index) {
	if (undefined != stationGraphicsLayer
		&& null != stationGraphicsLayer) {
		var stationGraphicArry =[];//用于承接点的graphic对象
		dojo.forEach(stationGraphicsLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var sid = feature.attributes.sid;//point对象
			if (sid == index) {
				stationGraphicArry.push(feature);
			}
		})
		for (let j = 0; j <stationGraphicArry.length; j++) {
			stationGraphicsLayer.remove(stationGraphicArry[j]);
		}
	}
	if (undefined != stationLabelLayer
		&& null != stationLabelLayer) {
		var labelGraphicArry =[];//用于承接点的graphic对象
		dojo.forEach(stationLabelLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var sid = feature.attributes.sid;//point对象
			if (sid == index) {
				labelGraphicArry.push(feature);
			}
		})
		for (let j = 0; j <labelGraphicArry.length; j++) {
			stationLabelLayer.remove(labelGraphicArry[j]);
		}
	}
	if (undefined != pointGraphicsLayer
		&& null != pointGraphicsLayer) {
		var pointGraphicArry =[];//用于承接点的graphic对象
		dojo.forEach(pointGraphicsLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var sid = feature.attributes.sid;//point对象
			if (sid == index) {
				pointGraphicArry.push(feature);
			}
		})
		for (let j = 0; j <pointGraphicArry.length; j++) {
			pointGraphicsLayer.remove(pointGraphicArry[j]);
		}
	}
}

function disconnect(){
	dojo.disconnect(graphicClick1);
	dojo.disconnect(graphicClick2);
	navToolbar.deactivate();
	if (undefined != graphicLayerRectangle
		&& null != graphicLayerRectangle) {
		map.removeLayer(graphicLayerRectangle);
		graphicLayerRectangle = null;
		if (undefined != tb
			&& null != tb) {
			tb.deactivate();
		}
	}
}

function getTrainPoint(index){
	var pointGraphicArry =[];
	if (undefined != pointGraphicsLayer
		&& null != pointGraphicsLayer) {4
		dojo.forEach(pointGraphicsLayer.graphics, function (feature) {//再遍历循环每一个点graphic对象
			var sid = feature.attributes.sid;//point对象
			if (sid == index) {
				var map = {};
				map["lon"] = feature.geometry.x.toString();
				map["lat"] = feature.geometry.y.toString();
				pointGraphicArry.push(map);
			}
		})
	}
	return pointGraphicArry;
}