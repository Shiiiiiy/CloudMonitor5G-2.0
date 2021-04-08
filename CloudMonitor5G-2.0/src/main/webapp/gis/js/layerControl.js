//显示图层并控制图层可见性
dojo.ready(function() {
	dojo.query(".Item").onclick(function(evt) {
		var id = evt.target.name;
		ShowOrHideDiv(id);
	});
	dojo.query(".navItem").onclick(function(evt) {
		navEvent(evt.target.id);
	});
	// 样式设置先加载后隐藏，否则无法显示
	$("#SetStyle").css("display", "none");
});
$(document).ready(function() {
	$("#ch_listall").click(function() {
		if (this.checked) {
			$("#eventsControl :checkbox").prop("checked", true);
		} else {
			$("#eventsControl :checkbox").prop("checked", false);
		}
	});
	$(".t1 tr").mouseover(function() {
		$(this).addClass("over");
	}).mouseout(function() {
		$(this).removeClass("over");
	});
	$(".t1 tr:even").addClass("alt");
// 给表哥 的偶数行添加class值为alt
});
var boshuFlage = false;
var toolbarType;
function initTools(bl) {
	CellLayers = [];
	toolbarType = '';
	CompareTrack = null;
	toolbarType = InitTools('toolbarType');
	initDataMap();
	if (toolbarType != null) {
		var s = parent.initToolbar(toolbarType);
		if (!s.showMapLayer) {
			hideDivByDivId('layerControldis');
		}
		if (!s.showCellAndSample) {
			hideDivByDivId('DisplayCleLPoint');
		}
		//yinzhipeng 2019-03-26
		/*if (s.showCell_Points) {
			showDivByDivId('DisplayCelL_Points');
		}
		if (s.showPoint_Cells) {
			showDivByDivId('DisplayPoint_Cells');
		}*/
		//
		if (!s.showCellAndEvent) {
			hideDivByDivId('DisplayCleLEvent');
		}
		if (!s.showCellAndCell) {
			hideDivByDivId('DisplayCleLArea');
		}
		if (!s.showStyleSet) {
			hideDivByDivId('StyleSet');
		}
		if (!s.showModel3) {
			hideDivByDivId('DisplayPCI');
		}
		if (!s.showTacModel) {
			hideDivByDivId('DisplayTAC');
		}
		if (!s.showRunOrientation) {
			hideDivByDivId('Azimuth');
		}
		if (!s.showGpsTrackShifting) {
			hideDivByDivId('TrackOffset');
		}
		if (!s.showDrawKpi) {
			hideDivByDivId('MapIndexTool');
		}
		if (!s.showDrawCallType) {
			hideDivByDivId('CallIndexTool');
			setSelectVal(s.drawKpi);
		}
		if (s.showDrawCallType) {
			setSelectVal(s.drawKpi);
			setCallTypeVal(s.drawCallType);

		}
		if (!s.showDrawRectangleFrame) {
			hideDivByDivId('DrawRectangleFrame');

		}
		if (!s.showSelectGpsPoint) {
			hideDivByDivId('selectGPSPoint');

		}
		if (s.showCell) { // 显示小区渲染工具
			var res = parent.getCellRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					AddCellbyArray(responses, bl);
				}
			});
		//GPSTrackRender();
		} else {
			hideDivByDivId('SearchCell');
		}

		if (s.eventShow) {
			var res = parent.getCellRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					F4ShpName = responses[2];
					NcellList4g = responses[3].NcellList4g;
					NcellList5g = responses[3].NcellList5g;
					AddCellbyArray(responses, bl);
				}
			});
		}

		if (s.SearchCell) {
			showDivByDivId('SearchCell');
		}

		if (s.showTestLogItemGpsTrack) {
			var res = parent.getTestLogItemRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGpsTrack(responses);
				}
			});
		}
		if (s.showQBRGpsTrack) {
			var res = parent.getTLI2QBRRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGpsTrackBadRoad(responses);
				}
			});
		}
		//yzp 2019-03-26
		if (s.showEmbbRoadPoints) {
			var res = parent.getTLI2QBRRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGPSTrackRender5g(responses);
				//ShowGpsTrackBadRoad(responses);
				}
			});
		}
		//maxuancheng 2019-10-22
		if (s.showRoadPoints) {
			var res = parent.getTLI2QBRRequestParam();
			if (res.requestUrl != null) {
				var content = {
					testLogItemIds : res.testLogItemIds,
					indexType : 1,
					'f' : "text"
				};
				var Request = esri.request({
					url : res.requestUrl,
					timeout : 400000,
					content : content,
					handleAs : "json",
					callbackParamName : "callback"
				});
				Request.then(function(responses) {
					if (responses != null) {
						for (var i = 0; i < responses[0].length; i++) {
							for (var j = 0; j < responses[0][i][1].length; j++) {
								responses[0][i][1][j]['beamId'] = 100 + j;
								responses[0][i][1][j]['nbeamId'] = 103;
							}
						}
						window.parent.showRoadPointsData = responses;
						ShowGPSTrackRender5g(responses);
					//ShowGpsTrackBadRoad(responses);
					}
				});
			}
		}
		//
		if (s.showEEGpsTrack) {
			var res = parent.getTLI2EERequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				eeType : res.eeType,
				iconType : res.iconType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGpsTrackExceptionCall(responses);
				}
			});
		}
		if (s.showHOFGpsTrack) {
			var res = parent.getTLI2HOFRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				hofType : res.hofType,
				iconType : res.iconType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGpsTrackExceptionCall(responses); // 复用通话异常
				}
			});
		}
		if (s.showCWBRGpsTrack) {
			var res = parent.getTLI2CWBRRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGpsCWBRTrack(responses);
				} else
					console.log(responses);
			});
		}
		if (s.showCEDEGpsTrack) {
			var res = parent.getTLI2CEDERequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				iconType : res.iconType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					ShowGpsCEDETrack(responses);
				} else
					console.log(responses);
			});
		}
		if (s.showCompareGrid) {
			GPSTrackRender();
		}

		if (s.BoshuAnalyseLi) {
			showDivByDivId('BoshuAnalyseLi');
		} else {
			hideDivByDivId('BoshuAnalyseLi');
		}

		if (s.stationLine) {
			showDivByDivId('stationLine');
		} else {
			hideDivByDivId('stationLine');
		}

		if (s.networkStructure) {
			showDivByDivId('networkStructure');
		} else {
			hideDivByDivId('networkStructure');
		}

		if (s.layerManager) {
			showDivByDivId('layerManager');
			//初始化查询颜色图例
			initColorMapLegend("mapColorSet");
		} else {
			hideDivByDivId('layerManager');
		}

		if (s.layerManager2) {
			showDivByDivId('layerManager2');
			//初始化查询颜色图例
			initColorMapLegend("mapNrPlanColor");
		} else {
			hideDivByDivId('layerManager2');
		}

		if (s.showCompareTestLogItemGpsTrack) {
			var res = parent.getCompareTestLogItemRequestParam();
			var content = {
				testLogItemIds : res.testLogItemIds,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(responses) {
				if (responses != null) {
					CompareTrack = responses;
					TrackOffset(CompareTrack, false);
				}
			});
		}
		if (bl) {
			setSelectVal(s.drawKpi);
		}
	}
}
// 通话异常轨迹点显示
function ShowGpsTrackExceptionCall(res) {
	var datas = res[0];
	var tdatas = res[1];
	var color = res[2].color;
	if (datas.length != 0) {
		for (var i = 0, n = datas.length; i < n; i++) {
			var data = datas[i];
			//var d = compress(data, 1);
			showLinesymByDataNoSign(data, color);
		}
	}
	if (tdatas.length != 0) {
		for (var i = 0, n = tdatas.length; i < n; i++) {
			var tdata = tdatas[i];
			var pt = new esri.geometry.Point(tdata.longitude, tdata.latitude, wgs);
			var t = tdata.index;
			var icon = tdata.iconType;
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/call/" + icon + ".png",
				"type" : "esriPMS"
			});
			symbol.setHeight(16);
			symbol.setWidth(16);
			var textsym = new esri.symbol.TextSymbol(t, null, new dojo.Color([ 216,
				12, 0, 1 ]));
			textsym.setOffset(-8, 8);
			var font = new esri.symbol.Font();
			font.setSize("12px");
			font.setWeight(esri.symbol.Font.WEIGHT_BOLD);
			textsym.setFont(font);
			var labelGraphic = new esri.Graphic(pt, textsym);
			var iconGraphic = new esri.Graphic(pt, symbol, null, null);
			map.graphics.add(iconGraphic);
			map.graphics.add(labelGraphic);
		}
	}

}
//呼叫时延建立异常
function ShowGpsCEDETrack(res) {
	var datas = res[0];
	var tdatas = res[1];
	var colors = res[2].colors;
	setMapCEDELegend(colors);
	for (var i = 0, ll = colors.length; i < ll; i++) {
		for (var j = 0, nn = datas.length; j < nn; j++) {
			if (colors[i].cedeType == datas[j][0]) {
				var color = colors[i].color;
				var da = datas[j],
					mm = da.length;
				if (mm > 1) {
					for (var k = 1; k < mm; k++) {
						var d = compress(da[k], 0.5);
						showLinesymByData(d, color);
					}
				}
				break;
			}
		}
	}
	for (var i = 0, n = tdatas.length; i < n; i++) {
		var tdata = tdatas[i];
		var pt = new esri.geometry.Point(tdata.longitude, tdata.latitude, wgs);
		var t = tdata.index;
		var icon = tdata.iconType;
		var symbol = new esri.symbol.PictureMarkerSymbol({
			"url" : "images/call/" + icon + ".png",
			"type" : "esriPMS"
		});
		symbol.setHeight(16);
		symbol.setWidth(16);
		var textsym = new esri.symbol.TextSymbol(t, null, new dojo.Color([ 216,
			12, 0, 1 ]));
		textsym.setOffset(-8, 8);
		var font = new esri.symbol.Font();
		font.setSize("12px");
		font.setWeight(esri.symbol.Font.WEIGHT_BOLD);
		textsym.setFont(font);
		var labelGraphic = new esri.Graphic(pt, textsym);
		var iconGraphic = new esri.Graphic(pt, symbol, null, null);
		map.graphics.add(iconGraphic);
		map.graphics.add(labelGraphic);
	}
}
// 所有质差路段的轨迹点显示
function ShowGpsTrackBadRoad(res) {
	var colors = res[1].colors;
	setMapLegend(colors);
	var data = res[0];
	for (var i = 0, ll = colors.length; i < ll; i++) {
		for (var j = 0, nn = data.length; j < nn; j++) {
			if (colors[i].qbrType == data[j][0]) {
				var color = colors[i].color;
				var da = data[j],
					mm = da.length;
				if (mm > 1) {
					for (var k = 1; k < mm; k++) {
						var d = compress(da[k], 0.5);
						showLinesymByData(d, color);
					}
				}
				break;
			}
		}
	}
}

//所有连续无限差路段的轨迹点显示
function ShowGpsCWBRTrack(res) {
	var colors = res[1].colors;
	setMapCWBRLegend(colors);
	var data = res[0];
	for (var i = 0, ll = colors.length; i < ll; i++) {
		for (var j = 0, nn = data.length; j < nn; j++) {
			if (colors[i].cwbrType == data[j][0]) {
				var color = colors[i].color;
				var da = data[j],
					mm = da.length;
				if (mm > 1) {
					for (var k = 1; k < mm; k++) {
						var d = compress(da[k], 0.5);
						showLinesymByData(d, color);
					}
				}
				break;
			}
		}
	}
}
function setMapCWBRLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		// color=color.colorRgb();
		// color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 80px;background-color:" + color
			+ "'></td><td>";
		var td = Rendercolor[i].cwbrTypeName;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
function setMapCEDELegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		// color=color.colorRgb();
		// color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 80px;background-color:" + color
			+ "'></td><td>";
		var td = Rendercolor[i].cedeTypeName;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
function SetTMLengend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		// color=color.colorRgb();
		// color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 80px;background-color:" + color
			+ "'></td><td>";
		var td = Rendercolor[i].tmTypeName;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
function setMapLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		// color=color.colorRgb();
		// color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 80px;background-color:" + color
			+ "'></td><td>";
		var td = Rendercolor[i].qbrTypeName;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
function setMapRPTLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		row += "<tr><td style='width: 80px;background-color:" + color
			+ "'></td><td>";
		var td = Rendercolor[i].lpTypeName;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
// 路段中间无标志
function showLinesymByDataNoSign(data, color) {
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	for (var i = 0, n = data.length; i < n; i++) {
		var pt = new esri.geometry.Point(data[i].longitude, data[i].latitude,
			wgs);
		GpsPoints.push(pt);
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
	var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
		esri.symbol.SimpleLineSymbol.STYLE_SOLID, color, 2);
	var GpsHistoryGraphic = new esri.Graphic(HistoryLine, GpsLineSymbol, null,
		null);
	map.graphics.add(GpsHistoryGraphic);
}
function showLinesymByMosBadNoSign(data, color) {
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	for (var i = 0, n = data.length; i < n; i++) {
		var pt = new esri.geometry.Point(data[i].longitude, data[i].latitude,
			wgs);
		GpsPoints.push(pt);
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
	var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
		esri.symbol.SimpleLineSymbol.STYLE_SOLID, color, 2);
	var GpsHistoryGraphic = new esri.Graphic(HistoryLine, GpsLineSymbol, null,
		null);
	MosBadTrackGraphicsLayer.add(GpsHistoryGraphic);
}
// 路段中间有标志
function showLinesymByData(data, color) {
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	for (var i = 0, n = data.length; i < n; i++) {
		var pt = new esri.geometry.Point(data[i].longitude, data[i].latitude,
			wgs);
		GpsPoints.push(pt);
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
	var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
		esri.symbol.SimpleLineSymbol.STYLE_SOLID, color, 2);
	var GpsHistorySymbol = new esri.Graphic(HistoryLine, GpsLineSymbol, null,
		null);
	map.graphics.add(GpsHistorySymbol);
	var n = Math.round(data.length / 2) - 1;
	var pt = new esri.geometry.Point(data[n].longitude, data[n].latitude, wgs);
	var symbol = new esri.symbol.PictureMarkerSymbol({
		"url" : "images/locate.png",
		"height" : 32,
		"width" : 32,
		"xoffset" : 0,
		"yoffset" : 0,
		"type" : "esriPMS"
	});
	var startGraphic = new esri.Graphic(pt, symbol, null, null);
	map.graphics.add(startGraphic);
}
//对比日志轨迹偏移
function showLinesByCompareDataOf(data, color) {
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	for (var i = 0, n = data.length; i < n; i++) {
		var pt = new esri.geometry.Point(data[i].longitude + 0.0001,
			data[i].latitude + 0.0001, wgs);
		GpsPoints.push(pt);
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
		esri.symbol.SimpleLineSymbol.STYLE_SOLID, color, 2);
	var GpsHistorySymbol = new esri.Graphic(HistoryLine, GpsLineSymbol, null,
		null);
	compareLogTrackLayer.add(GpsHistorySymbol);
	var n = Math.round(data.length / 2) - 1;
	var pt = new esri.geometry.Point(data[n].longitude + 0.0001,
		data[n].latitude + 0.0001, wgs);
	var symbol = new esri.symbol.PictureMarkerSymbol({
		"url" : "images/locate.png",
		"height" : 32,
		"width" : 32,
		"xoffset" : 0,
		"yoffset" : 0,
		"type" : "esriPMS"
	});
	var startGraphic = new esri.Graphic(pt, symbol, null, null);
	compareLogTrackLayer.add(startGraphic);

	map.setExtent(mapDataExtent);
}
//对比日志轨迹原始
function showLinesByCompareDataOr(data, color) {
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	for (var i = 0, n = data.length; i < n; i++) {
		var pt = new esri.geometry.Point(data[i].longitude, data[i].latitude,
			wgs);
		GpsPoints.push(pt);
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
		esri.symbol.SimpleLineSymbol.STYLE_SOLID, color, 2);
	var GpsHistorySymbol = new esri.Graphic(HistoryLine, GpsLineSymbol, null,
		null);
	compareLogTrackLayer.add(GpsHistorySymbol);
	var n = Math.round(data.length / 2) - 1;
	var pt = new esri.geometry.Point(data[n].longitude, data[n].latitude, wgs);
	var symbol = new esri.symbol.PictureMarkerSymbol({
		"url" : "images/locate.png",
		"height" : 32,
		"width" : 32,
		"xoffset" : 0,
		"yoffset" : 0,
		"type" : "esriPMS"
	});
	var startGraphic = new esri.Graphic(pt, symbol, null, null);
	compareLogTrackLayer.add(startGraphic);
	map.setExtent(mapDataExtent);
}
// 道格拉斯-普克算法 ----------简化线--------------
function douglasPeucker(data, begin, end, threshold) {
	if (begin > end) {
		return;
	}
	// 起止点
	var a = data[begin];
	var b = data[end];
	// 一些系数
	var M = Math.sqrt(Math.pow(a.longitude - b.longitude, 2)
		+ Math.pow(a.latitude - b.latitude, 2));
	var A = (a.latitude - b.latitude) / M;
	var B = (a.longitude - b.longitude) / M;
	var C = (a.longitude * b.latitude - b.longitude * a.latitude) / M;
	var i = begin + 1,
		k = begin + 1,
		max = 0;
	// 寻找最大值
	for (; i <= end - 1; i++) {
		var point = data[i];
		var current = Math.abs(A * point.longitude + B * point.latitude + C);
		if (max < current) {
			k = i;
			max = current;
		}
	}
	// 递归
	if (max > threshold) {
		douglasPeucker(data, begin, k, threshold);
		douglasPeucker(data, k, end, threshold);
	} else {
		// 标记被去掉的点
		for (i = begin + 1; i < end - 1; i++) {
			data[i].removed = true;
		}
	}
}
function compress(data, threshold) {
	douglasPeucker(data, 0, data.length - 1, threshold);
	// douglasPeucker(data,Math.floor(data.length/2),data.length-1,threshold);
	var d = [];
	for (var i = 0; i < data.length; i++) {
		if (data[i].removed != true) {
			d.push(data[i]);
		}
	}
	return d;
}
// 轨迹点渲染
function ShowGpsTrack(res) {
	var datas = res[0];
	var color = res[1].color;
	//setOrMapLegend(color);
	for (var i = 0, n = datas.length; i < n; i++) {
		var data = datas[i];
		var d = compress(data, 2);
		showLinesymByData(d, color);
	}
// DisplayDirection(res);
}
function setOrMapLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color = Rendercolor;
	row += "<tr><td style='width: 80px;background-color:" + color
		+ "'></td><td>";
	var td = "日志轨迹";
	row += td + "</td></tr>";
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
// 小区事件连线
function CellLineIconjunction() {
	if (toolbarType == "40" || toolbarType == "41" || toolbarType == "42" || toolbarType == "43" || toolbarType == "44" || toolbarType == "45") {
		var res = parent.getEEEventRequestParam();
		if (res.eeId == null || res.eeId == undefined) {
			return;
		}
		var callType = $("#callSelectIndex").find("option:selected").val();
		var content = {
			eeId : res.eeId,
			callType : callType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineIcon(re);
			}
		});
	}
	if (toolbarType == "11") {
		var res = parent.getHOFEventRequestParam();
		if (res.hofId == null || res.hofId == undefined) {
			return;
		}
		var content = {
			hofId : res.hofId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineIcon(re);
			}
		});
	}
	if (toolbarType == "18" || toolbarType == "19" || toolbarType == "20"
		|| toolbarType == "21") {
		var res = parent.getCEDEEventRequestParam();
		if (res.hofId == null || res.hofId == undefined) {
			return;
		}
		var content = {
			cedeId : res.cedeId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineIcon(re);
			}
		});
	}
	if (toolbarType == "24" || toolbarType == "25") {
		var res = parent.getCompareHofRequestParam();
		if (res.testLogItemIds == null || res.testLogItemIds == undefined
			|| res.compareTestLogItemIds == null
			|| res.compareTestLogItemIds == undefined
			|| res.hofType == null || res.hofType == undefined
			|| res.cellId == null || res.cellId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			testLogItemIds : res.testLogItemIds,
			compareTestLogItemIds : res.compareTestLogItemIds,
			hofType : res.hofType,
			failId : res.failId,
			cellId : res.cellId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				var res = re[1];
				ShowCellLineIcon(res);
			}
		});
	}
	if (toolbarType == "32" || toolbarType == "33" || toolbarType == "34"
		|| toolbarType == "35" || toolbarType == "36"
		|| toolbarType == "37" || toolbarType == "38"
		|| toolbarType == "39") {
		var res = parent.getVideoQBLTERequestParam();
		if (res.videoQualityBadId == null || res.videoQualityBadId == undefined) {
			return;
		}
		var content = {
			videoQualityBadId : res.videoQualityBadId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineIcon(re);
			}
		});
	}
}
// 小区点连线
function CellLineConjunction() {
	var ty = toolbarType;
	if (ty == "32" || ty == "33" || ty == "34" || ty == "35" || ty == "36"
		|| ty == "37" || ty == "38" || ty == "39") {
		var res = parent.getVideoQBRRequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null || indexType == undefined) {
			return;
		}
		if (res.videoQualityBadId != null) {
			var content = {
				videoQualityBadId : res.videoQualityBadId,
				indexType : indexType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(re) {
				if (re != null) {
					ShowCellLineCon(re);
				}
			});
		}
	}
	if (ty == "52" || ty == "53" || ty == "54" || ty == "55" || ty == "56"
		|| ty == "57" || ty == "58") {
		var res = parent.getStreamQBRRequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null || indexType == undefined) {
			return;
		}
		if (res.videoQualityBadId != null) {
			var content = {
				videoQualityBadId : res.videoQualityBadId,
				indexType : indexType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(re) {
				if (re != null) {
					ShowCellLineCon(re);
				}
			});
		}
	}

	if (ty == "1" || ty == "2" || ty == "3" || ty == "4" || ty == "5"
		|| ty == "6") {
		var res = parent.getQBRRequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null || indexType == undefined) {
			return;
		}
		if (res.badRoadId != null) {
			var content = {
				badRoadId : res.badRoadId,
				indexType : indexType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(re) {
				if (re != null) {
					ShowCellLineCon(re);
				}
			});
		}
	}
	if (ty == "40" || ty == "41" || ty == "42" || ty == "43" || ty == "44" || ty == "45") {
		var res = parent.getEERequestParam(); // *todo
		var indexType = $("#selectIndex").find("option:selected").val();
		var callType = $("#callSelectIndex").find("option:selected").val();
		if (res.eeId == null || res.eeId == undefined) {
			return;
		}
		var content = {
			eeId : res.eeId,
			indexType : indexType,
			callType : callType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineCon(re);
			}
		});

	}
	if (ty == "11") {
		var res = parent.getHOFRequestParam(); // *todo
		var indexType = $("#selectIndex").find("option:selected").val();
		if (res.hofId == null || res.hofId == undefined) {
			return;
		}
		var content = {
			hofId : res.hofId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineCon(re);
			}
		});

	}
	if (ty == "13" || ty == "14" || ty == "15" || ty == "16") {
		var res = parent.getCWBRRequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineCon(re);
			}
		});

	}
	if (ty == "18" || ty == "19" || ty == "20" || ty == "21") {
		var res = parent.getCEDERequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		if (res.cedeId == null || res.cedeId == undefined) {
			return;
		}
		var content = {
			cedeId : res.cedeId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellLineCon(re);
			}
		});

	}
	if (toolbarType == "24" || toolbarType == "25") {
		var res = parent.getCompareHofRequestParam();
		if (res.testLogItemIds == null || res.testLogItemIds == undefined
			|| res.compareTestLogItemIds == null
			|| res.compareTestLogItemIds == undefined
			|| res.hofType == null || res.hofType == undefined
			|| res.cellId == null || res.cellId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			testLogItemIds : res.testLogItemIds,
			compareTestLogItemIds : res.compareTestLogItemIds,
			hofType : res.hofType,
			failId : res.failId,
			cellId : res.cellId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				var res = re[0];
				ShowCellLineCon(res);
			}
		});
	}
	//yzp 2019-03-27
	if (ty == "81" || ty == "71") {
		var res = parent.getQBRRequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null || indexType == undefined) {
			return;
		}
		if (res.badRoadId != null) {
			var content = {
				badRoadId : res.badRoadId,
				indexType : indexType,
				'f' : "text"
			};
			var Request = esri.request({
				url : res.requestUrl,
				timeout : 400000,
				content : content,
				handleAs : "json",
				callbackParamName : "callback"
			});
			Request.then(function(re) {
				if (re != null) {
					ShowCellLineCon(re);
				}
			});
		}
	}
	//

}
// 小区点连线
var CellLineConGraphicsLayer = null;
function ShowCellLineCon(res) {
	var data = res[0];
	var color = res[1].colors;
	if (undefined == CellLineConGraphicsLayer
		|| null == CellLineConGraphicsLayer) {
		CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(CellLineConGraphicsLayer);
	} else {
		CellLineConGraphicsLayer.clear();
	}
	var Query = new esri.tasks.Query();
	Query.returnGeometry = true;
	var clc = $("#SelectCellColorLine").find("option:selected").val();
	for (var i = 0, p = data.length; i < p; i++) {
		Query.where = "CELLID='" + data[i][0] + "'";
		var cc = data[i][1];
		QureyResults(cc, Query, clc, color);
	}
}
var CellLineIconGraphicsLayer = null;
function ShowCellLineIcon(res) { // 默认和小区颜色同
	var data = res;
	if (undefined == CellLineIconGraphicsLayer
		|| null == CellLineIconGraphicsLayer) {
		CellLineIconGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(CellLineIconGraphicsLayer);
	} else {
		CellLineIconGraphicsLayer.clear();
	}
	var Query = new esri.tasks.Query();
	Query.returnGeometry = true;
	for (var i = 0, p = data.length; i < p; i++) {
		Query.where = "CELLID='" + data[i][0] + "'";
		var cc = data[i][1];
		QureyResultsIcon(cc, Query);
	}
}
function QureyResultsIcon(data, Query) {
	var cc = data;
	CellLayers[0].queryFeatures(Query,
		function(geometry) {
			var geo = geometry.features[0].geometry;
			var t = geometry.features[0].attributes.MODEL3;
			var clc = Model3Color(t);
			var pt = new esri.geometry.Point(geo.getExtent().getCenter()
				.getLongitude(), geo.getExtent().getCenter()
				.getLatitude(), wgs);
			var p1;
			for (var j = 0, c = cc.length; j < c; j++) {
				var line = new esri.geometry.Polyline(wgs);
				p1 = new esri.geometry.Point(cc[j].longitude,
					cc[j].latitude, wgs);
				line.addPath([ pt, p1 ]);
				var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
				var GpsHistorySymbol = new esri.Graphic(line,
					GpsLineSymbol, null, null);
				CellLineIconGraphicsLayer.add(GpsHistorySymbol);
			}
			CellLineIconGraphicsLayer.setOpacity(0.5);
		}, function(err) {
			alert(err);
		});
}
function QureyResults(data, Query, clc, color) {
	var cc = data;
	if (clc == "point") {
		CellLayers[0].queryFeatures(Query, function(geometry) {
			if (geometry) {
				var geo = geometry.features[0].geometry;
				var pt = new esri.geometry.Point(geo.getExtent().getCenter()
					.getLongitude(), geo.getExtent().getCenter().getLatitude(),
					wgs);
				var p1;
				var index = $("#selectIndex").find("option:selected").text();
				for (var j = 0, c = cc.length; j < c; j++) {
					if (cc[j] == null) {
						continue;
					}
					var t = cc[j].indexValue;
					if (index == "TM") {
						var clc = ColorTMValue(color, t);
					} else {
						var clc = ColorValue(color, t);
					}
					var line = new esri.geometry.Polyline(wgs);
					p1 = new esri.geometry.Point(cc[j].longitude, cc[j].latitude,
						wgs);
					line.addPath([ pt, p1 ]);
					var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
					var GpsHistorySymbol = new esri.Graphic(line, GpsLineSymbol,
						null, null);
					CellLineConGraphicsLayer.add(GpsHistorySymbol);
				}
				CellLineConGraphicsLayer.setOpacity(1);
			}

		}, function(err) {
			alert(err);
		});
	} else {
		CellLayers[0]
			.queryFeatures(
				Query,
				function(geometry) {
					if (geometry) {
						var geo = geometry.features[0].geometry;
						var t = geometry.features[0].attributes.MODEL3,
							clc = Model3Color(t);
						var pt = new esri.geometry.Point(geo.getExtent()
							.getCenter().getLongitude(), geo
							.getExtent().getCenter().getLatitude(), wgs);
						var p1;
						for (var j = 0, c = cc.length; j < c; j++) {
							if (cc[j] == null) {
								continue;
							}
							var line = new esri.geometry.Polyline(wgs);
							p1 = new esri.geometry.Point(cc[j].longitude,
								cc[j].latitude, wgs);
							line.addPath([ pt, p1 ]);
							var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
								esri.symbol.SimpleLineSymbol.STYLE_SOLID,
								clc, 2);
							var GpsHistorySymbol = new esri.Graphic(line,
								GpsLineSymbol, null, null);
							CellLineConGraphicsLayer.add(GpsHistorySymbol);
						}
						CellLineConGraphicsLayer.setOpacity(0.5);
					}
				}, function(err) {
					alert(err);
				});
	}
}
// 行驶方向
// var indexType = null;
function getDirectionRequestParam() {
	var ty = toolbarType;
	if (ty == "1" || ty == "2" || ty == "3" || ty == "4" || ty == "5"
		|| ty == "6") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getQBRDirectionRequestParam();

		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
	if (ty == "40" || ty == "41" || ty == "42" || ty == "43" || ty == "44" || ty == "45") {
		var indexType = $("#selectIndex").find("option:selected").val();
		var callType = $("#callSelectIndex").find("option:selected").val();
		if (indexType == null || callType == null) {
			return;
		}
		var res = parent.getEEDirectionRequestParam();
		if (res.eeId == null || res.eeId == undefined) {
			return;
		}
		var content = {
			eeId : res.eeId,
			indexType : indexType,
			callType : callType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
	if (ty == "11") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getHOFDirectionRequestParam();
		if (res.hofId == null || res.hofId == undefined) {
			return;
		}
		var content = {
			hofId : res.hofId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
	if (ty == "13" || ty == "14" || ty == "15" || ty == "16") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getCWBRDirectionRequestParam();
		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
	if (ty == "18" || ty == "19" || ty == "20" || ty == "21") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getCEDEDirectionRequestParam();
		if (res.cedeId == null || res.cedeId == undefined) {
			return;
		}
		var content = {
			cedeId : res.cedeId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}

	if (ty == "32" || ty == "33" || ty == "34" || ty == "35" || ty == "36"
		|| ty == "37" || ty == "38" || ty == "39") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getVideoQBRDirectionRequestParam();
		if (res.videoQualityBadId == null || res.videoQualityBadId == undefined) {
			return;
		}
		var content = {
			videoQualityBadId : res.videoQualityBadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
	if (ty == "52" || ty == "53" || ty == "54" || ty == "55" || ty == "56"
		|| ty == "57" || ty == "58") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getStreamQBRDirectionRequestParam();
		if (res.videoQualityBadId == null || res.videoQualityBadId == undefined) {
			return;
		}
		var content = {
			videoQualityBadId : res.videoQualityBadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
	//yzp 2019-03-27
	if (ty == "81" || ty == "71") {
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == null) {
			return;
		}
		var res = parent.getQBRDirectionRequestParam();

		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				console.log(responses);
				DisplayDirection(responses);
			}
		});
	}
}
// 指标事件点渲染
function GPSEventRender() {
	var ty = toolbarType;
	if (ty == "40" || ty == "41" || ty == "42" || ty == "43" || ty == "44" || ty == "45") {
		var res1 = parent.getEEEventRequestParam();
		if (res1.eeId == null || res1.eeId == undefined) {
			return;
		}
		var callType = $("#callSelectIndex").find("option:selected").val();
		var content1 = {
			eeId : res1.eeId,
			callType : callType,
			'f' : "text"
		};
		var Request1 = esri.request({
			url : res1.requestUrl,
			timeout : 400000,
			content : content1,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request1.then(function(re1) {
			if (re1 != null) {
				ShowEventTrackRender(re1);
			}
		});
	}
	if (ty == "11") {
		var res1 = parent.getHOFEventRequestParam();
		if (res1.hofId == null || res1.hofId == undefined) {
			return;
		}
		var content1 = {
			hofId : res1.hofId,
			'f' : "text"
		};
		var Request1 = esri.request({
			url : res1.requestUrl,
			timeout : 400000,
			content : content1,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request1.then(function(re1) {
			if (re1 != null) {
				ShowEventTrackRender(re1);
			}
		});
	}
	if (ty == "18" || ty == "19" || ty == "20" || ty == "21") {
		var res1 = parent.getCEDEEventRequestParam();
		if (res1.cedeId == null || res1.cedeId == undefined) {
			return;
		}
		var content1 = {
			cedeId : res1.cedeId,
			'f' : "text"
		};
		var Request1 = esri.request({
			url : res1.requestUrl,
			timeout : 400000,
			content : content1,
			handleAs : "json",
			callbackParamName : "callback"
		});

		Request1.then(function(re1) {
			if (re1 != null) {
				ShowEventTrackRender(re1);
			}
		});
	}
	if (ty == "32" || ty == "33" || ty == "34" || ty == "35" || ty == "36"
		|| ty == "37" || ty == "38" || ty == "39") {
		var res1 = parent.getVideoQBLTERequestParam();
		if (res1.videoQualityBadId == null
			|| res1.videoQualityBadId == undefined) {
			return;
		}
		var content1 = {
			videoQualityBadId : res1.videoQualityBadId,
			'f' : "text"
		};
		var Request1 = esri.request({
			url : res1.requestUrl,
			timeout : 400000,
			content : content1,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request1.then(function(re1) {
			if (re1 != null) {
				ShowEventTrackRender(re1);
			}
		});
	}
	if (ty == "52" || ty == "53" || ty == "54" || ty == "55" || ty == "56"
		|| ty == "57" || ty == "58" || ty == "59") {
		var res1 = parent.getStreamQBLTERequestParam();
		if (res1.videoQualityBadId == null
			|| res1.videoQualityBadId == undefined) {
			return;
		}
		var content1 = {
			videoQualityBadId : res1.videoQualityBadId,
			'f' : "text"
		};
		var Request1 = esri.request({
			url : res1.requestUrl,
			timeout : 400000,
			content : content1,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request1.then(function(re1) {
			if (re1 != null) {
				ShowEventTrackRender(re1);
			}
		});
	}
}

// 指标轨迹点渲染
function GPSTrackRender() {
	var ty = toolbarType;
	if (ty == "1" || ty == "2" || ty == "3" || ty == "4" || ty == "5"
		|| ty == "6" || ty == "70") {
		var res = parent.getQBRRequestParam(); // *todo
		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
			}
		});
	}
	if (ty == "40" || ty == "41" || ty == "42" || ty == "43" || ty == "44" || ty == "45") {
		var res = parent.getEERequestParam(); // *todo
		if (res.eeId == null || res.eeId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var callType = $("#callSelectIndex").find("option:selected").val();
		var content = {
			eeId : res.eeId,
			indexType : indexType,
			callType : callType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
				GPSEventRender();
			}
		});

	}
	if (ty == "11") {
		var res = parent.getHOFRequestParam(); // *todo
		if (res.hofId == null || res.hofId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			hofId : res.hofId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
				GPSEventRender();
			}
		});

	}
	if (ty == "13" || ty == "14" || ty == "15" || ty == "16") {
		var res = parent.getCWBRRequestParam(); // *todo
		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
			}
		});
	}
	if (ty == "18" || ty == "19" || ty == "20" || ty == "21") {
		var res = parent.getCEDERequestParam();
		if (res.cedeId == null || res.cedeId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			cedeId : res.cedeId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
				GPSEventRender();
			}
		});

	}
	if (ty == "23") {
		var res = parent.getMosBadRequestParam();
		if (res.badRoadId == null || res.badRoadId == undefined
			|| res.compareBadRoadIds == null
			|| res.compareBadRoadIds == undefined
			|| res.mosBadLatitude == null
			|| res.mosBadLatitude == undefined
			|| res.mosBadLongitude == null
			|| res.mosBadLongitude == undefined) {
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			compareBadRoadIds : res.compareBadRoadIds,
			mosBadLatitude : res.mosBadLatitude,
			mosBadLongitude : res.mosBadLongitude,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				mosBadTrackRender(re);
			}
		});
	}
	if (ty == "24" || ty == "25") {
		var res = parent.getCompareHofRequestParam();
		if (res.testLogItemIds == null || res.testLogItemIds == undefined
			|| res.compareTestLogItemIds == null
			|| res.compareTestLogItemIds == undefined
			|| res.hofType == null || res.hofType == undefined
			|| res.cellId == null || res.cellId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			testLogItemIds : res.testLogItemIds,
			compareTestLogItemIds : res.compareTestLogItemIds,
			hofType : res.hofType,
			failId : res.failId,
			cellId : res.cellId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCompareHofTrackRender(re);
			}
		});
	}
	if (ty == "26" || ty == "60") {
		var res = parent.getCompareGridRequestParam();
		if (res.testLogItemIds == null || res.testLogItemIds == undefined
			|| res.compareTestLogItemIds == null
			|| res.compareTestLogItemIds == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			testLogItemIds : res.testLogItemIds,
			compareTestLogItemIds : res.compareTestLogItemIds,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGridCompareRender(re);
			}
		});
	}
	if (ty == "32" || ty == "33" || ty == "34" || ty == "35" || ty == "36"
		|| ty == "37" || ty == "38" || ty == "39") {
		var res = parent.getVideoQBRRequestParam();
		if (res.videoQualityBadId == null || res.videoQualityBadId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			videoQualityBadId : res.videoQualityBadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
				GPSEventRender();
			}
		});
	}
	//流媒体视频质差
	if (ty == "52" || ty == "53" || ty == "54" || ty == "55" || ty == "56"
		|| ty == "57" || ty == "58") {

		var res = parent.getStreamQBRRequestParam();
		if (res.videoQualityBadId == null || res.videoQualityBadId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			videoQualityBadId : res.videoQualityBadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender(re);
				GPSEventRender();
			}
		});
	}
	//yzp 2019-03-26
	if (ty == "80") {
		var res = parent.getTLI2QBRRequestParam();
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			testLogItemIds : res.testLogItemIds,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				ShowGPSTrackRender5g(responses);
			}
		});
	}
	if (ty == "81") {
		var res = parent.getQBRRequestParam(); // *todo
		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender5g(re);
				ShowCellLineCon(re);
			}
		});
	}

	if (ty == "71") {
		var res = parent.getQBRRequestParam(); // *todo
		if (res.badRoadId == null || res.badRoadId == undefined) {
			return;
		}
		var indexType = $("#selectIndex").find("option:selected").val();
		var content = {
			badRoadId : res.badRoadId,
			indexType : indexType,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowGPSTrackRender5g(re);
				ShowCellLineCon(re);
			}
		});
	}
	//

}

var CompareHofTrackGraphicsLayer = null;
function ShowCompareHofTrackRender(res) {
	if (undefined != DirectionGraphicsLayer || null != DirectionGraphicsLayer) {
		DirectionGraphicsLayer.clear();
		DirectionGraphicsLayer = null;
		var ima2 = document.getElementById('Azimuth');
		ima2.src = "images/Azimuth_dis.png";
		ima2.title = "显示行驶方向";
		ima2.alt = "显示行驶方向";
	}
	if (undefined != CellLineConGraphicsLayer
		|| null != CellLineConGraphicsLayer) {
		CellLineConGraphicsLayer.clear();
		CellLineConGraphicsLayer = null;
		var ima = document.getElementById('DisplayCleLPoint');
		ima.src = "images/DisplayCleLPoint_dis.png";
		ima.title = "显示小区点连线";
		ima.alt = "显示小区点连线";
	}
	if (undefined != CellToCellGraphicsLayer || null != CellToCellGraphicsLayer) {
		CellToCellGraphicsLayer.clear();
		CellToCellGraphicsLayer = null;
		var ima1 = document.getElementById('DisplayCleLArea');
		ima1.src = "images/DisplayCleLArea_dis.png";
		ima1.title = "显示邻区连线";
		ima1.alt = "显示邻区连线";
	}
	if (undefined != CellLineIconGraphicsLayer
		|| null != CellLineIconGraphicsLayer) {
		CellLineIconGraphicsLayer.clear();
		CellLineIconGraphicsLayer = null;
		var ima3 = document.getElementById('DisplayCleLEvent');
		ima3.src = "images/DisplayCleLEvent_dis.png";
		ima3.title = "显示小区事件连线";
		ima3.alt = "显示小区事件连线";
	}
	var data = res[0];
	//	if (data.length==0){
	//		return;
	//	}
	SetMapLengend(res[2].colors, 1);
	var color = normalizingColor(res[2].colors);
	if (undefined == CompareHofTrackGraphicsLayer
		|| null == CompareHofTrackGraphicsLayer) {
		CompareHofTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		CompareHofTrackGraphicsLayer.clear();
	}
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	var style = "";
	switch ($("#PointStyle").find("option:selected").val()) {
	case "Circle":
		style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
		break;
	case "Square":
		style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
		break;
	}
	var index = $("#selectIndex").find("option:selected").text();
	if (index == "MOS值") {
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				var t = d[j].indexValue,
					cc = ColorValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				CompareHofTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(t, null,
					new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				var labelgraphic = new esri.Graphic(pt, textsym);
				CompareHofTrackGraphicsLayer.add(labelgraphic);
				GpsPoints.push(pt);
			}
		}
	} else {
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				var t = d[j].indexValue,
					cc = ColorValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				CompareHofTrackGraphicsLayer.add(startGraphic);
				GpsPoints.push(pt);
			}
		}
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table><tr><td>" + index
		+ "为： </td><td> ${indexValue} </td></tr></table>");
	CompareHofTrackGraphicsLayer.setInfoTemplate(infoTemplate);
	var data1 = res[1];
	for (var i = 0; i < data1.length; i++) {
		var tdata = data1[i][1];
		for (var j = 0, n = tdata.length; j < n; j++) {
			var pt = new esri.geometry.Point(tdata[j].longitude,
				tdata[j].latitude, wgs);
			var t = tdata[j].index;
			var icon = tdata[j].iconType;
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/call/" + icon + ".png",
				"type" : "esriPMS"
			});
			symbol.setHeight(16);
			symbol.setWidth(16);
			var textsym = new esri.symbol.TextSymbol(t, null, new dojo.Color([
				216, 12, 0, 1 ]));
			textsym.setOffset(-8, 8);
			var font = new esri.symbol.Font();
			font.setSize("12px");
			font.setWeight(esri.symbol.Font.WEIGHT_BOLD);
			textsym.setFont(font);
			var labelGraphic = new esri.Graphic(pt, textsym);
			var iconGraphic = new esri.Graphic(pt, symbol, null, null);
			CompareHofTrackGraphicsLayer.add(iconGraphic);
			CompareHofTrackGraphicsLayer.add(labelGraphic);
			GpsPoints.push(pt);
		}
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
	map.addLayer(CompareHofTrackGraphicsLayer);
}

var MosBadTrackGraphicsLayer = null;
function mosBadTrackRender(res) {
	var data0 = res[0];
	var data1 = res[1];
	var data2 = res[2];
	var color = res[3];
	setmosBadLegend(color);
	if (undefined == MosBadTrackGraphicsLayer
		|| null == MosBadTrackGraphicsLayer) {
		MosBadTrackGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(MosBadTrackGraphicsLayer);
	} else {
		MosBadTrackGraphicsLayer.clear();
	}
	for (var i = 0, n = data0.length; i < n; i++) {
		var data = data0[i];
		var d = compress(data, 1);
		showLinesymByMosBadNoSign(d, color.color);
	}
	for (var i = 0, n = data1.length; i < n; i++) {
		var data = data1[i];
		var d = compress(data, 1);
		showLinesymByMosBadNoSign(d, color.compareColor);
	}
	var pt = new esri.geometry.Point(data2.mosBadLongitude,
		data2.mosBadLatitude, wgs);
	var symbol = new esri.symbol.PictureMarkerSymbol({
		"url" : "images/locate.png",
		"height" : 32,
		"width" : 32,
		"xoffset" : 0,
		"yoffset" : 0,
		"type" : "esriPMS"
	});
	var startGraphic = new esri.Graphic(pt, symbol, null, null);
	MosBadTrackGraphicsLayer.add(startGraphic);
}
function setmosBadLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color = Rendercolor.color;
	row += "<tr><td style='width: 80px;background-color:" + color
		+ "'></td><td>";
	var td = Rendercolor.name;
	row += td + "</td></tr>";
	var color1 = Rendercolor.compareColor;
	row += "<tr><td style='width: 80px;background-color:" + color1
		+ "'></td><td>";
	var td1 = Rendercolor.compareName;
	row += td1 + "</td></tr>";
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}

var GridCompareGraphicsLayer = null;
function ShowGridCompareRender(res) {
	var datas = res[0];
	var color = res[1].colors;
	setGridComLegend(color);
	if (undefined == GridCompareGraphicsLayer
		|| null == GridCompareGraphicsLayer) {
		GridCompareGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GridCompareGraphicsLayer.clear();
	}
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	for (var i = 0; i < datas.length; i++) {
		var polygon = new esri.geometry.Polygon(wgs);
		var LineSymbol = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([ 255,
				255, 255, 0.8 ]), 2);
		var sym = new esri.symbol.SimpleFillSymbol("solid", LineSymbol,
			datas[i].color);
		var pt1 = new esri.geometry.Point(datas[i].minx, datas[i].miny, wgs);
		var pt2 = new esri.geometry.Point(datas[i].minx, datas[i].maxy, wgs);
		var pt3 = new esri.geometry.Point(datas[i].maxx, datas[i].maxy, wgs);
		var pt4 = new esri.geometry.Point(datas[i].maxx, datas[i].miny, wgs);
		GpsPoints.push(pt1);
		GpsPoints.push(pt2);
		GpsPoints.push(pt3);
		GpsPoints.push(pt4);
		polygon.addRing([ pt1, pt2, pt3, pt4, pt1 ]);
		var value = datas[i].value;
		var compareValue = datas[i].compareValue;
		var startGraphic = new esri.Graphic(polygon, sym, {
			"value" : value,
			"compareValue" : compareValue
		}, null);
		GridCompareGraphicsLayer.add(startGraphic);
	}
	var infoTemplate = new esri.InfoTemplate(
		"详情",
		"<table><tr><td>原始日志值： </td><td> ${value} </td></tr> <tr><td>对比日志值： </td><td> ${compareValue} </td></tr></table>");
	GridCompareGraphicsLayer.setInfoTemplate(infoTemplate);
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
	GridCompareGraphicsLayer.setOpacity(0.6);
	map.addLayer(GridCompareGraphicsLayer);
}

function setGridComLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		row += "<tr><td style='width: 80px;background-color:" + color
			+ "'></td><td>";
		var td = Rendercolor[i].name;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}

// var lineSym= new
// esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID,new
// dojo.Color([0, 255, 0]),1);
// 地图展示采样点（渲染）
var GPSTrackGraphicsLayer;
var GPSTrackLabelGraphicsLayer;
function ShowGPSTrackRender(res) {
	if (undefined != DirectionGraphicsLayer || null != DirectionGraphicsLayer) {
		DirectionGraphicsLayer.clear();
		DirectionGraphicsLayer = null;
		var ima2 = document.getElementById('Azimuth');
		ima2.src = "images/Azimuth_dis.png";
		ima2.title = "显示行驶方向";
		ima2.alt = "显示行驶方向";
	}
	if (undefined != CellLineConGraphicsLayer
		|| null != CellLineConGraphicsLayer) {
		CellLineConGraphicsLayer.clear();
		CellLineConGraphicsLayer = null;
		var ima = document.getElementById('DisplayCleLPoint');
		ima.src = "images/DisplayCleLPoint_dis.png";
		ima.title = "显示小区点连线";
		ima.alt = "显示小区点连线";
	}
	if (undefined != CellToCellGraphicsLayer || null != CellToCellGraphicsLayer) {
		CellToCellGraphicsLayer.clear();
		CellToCellGraphicsLayer = null;
		var ima1 = document.getElementById('DisplayCleLArea');
		ima1.src = "images/DisplayCleLArea_dis.png";
		ima1.title = "显示邻区连线";
		ima1.alt = "显示邻区连线";
	}
	if (undefined != CellLineIconGraphicsLayer
		|| null != CellLineIconGraphicsLayer) {
		CellLineIconGraphicsLayer.clear();
		CellLineIconGraphicsLayer = null;
		var ima3 = document.getElementById('DisplayCleLEvent');
		ima3.src = "images/DisplayCleLEvent_dis.png";
		ima3.title = "显示小区事件连线";
		ima3.alt = "显示小区事件连线";
	}
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	if (undefined == GPSTrackLabelGraphicsLayer
		|| null == GPSTrackLabelGraphicsLayer) {
		GPSTrackLabelGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackLabelGraphicsLayer.clear();
	}
	GPSTrackLabelGraphicsLayer.setVisibility(false);
	map.addLayer(GPSTrackGraphicsLayer);
	map.addLayer(GPSTrackLabelGraphicsLayer);
	var data = res[0];
	if (data.length == 0) {
		return;
	}
	var index = $("#selectIndex").find("option:selected").text();
	if (index == "TM") {
		var color = res[1].colors;
		SetTMLengend(color);

	} else {
		var color = normalizingColor(res[1].colors);
		SetMapLengend(color, 1);

	}
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	var style = "";
	switch ($("#PointStyle").find("option:selected").val()) {
	case "Circle":
		style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
		break;
	case "Square":
		style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
		break;
	}
	if (index == "TM") {
		document.getElementById('label_TrackLayer').disabled = true;
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				var t = d[j].indexValue,
					cc = ColorTMValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				GPSTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(t, null,
					new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				//var labelgraphic = new esri.Graphic(pt, textsym);
				//GPSTrackLabelGraphicsLayer.add(labelgraphic);
				GpsPoints.push(pt);
			}
		}
	} else if (index == "MOS值") {
		document.getElementById('label_TrackLayer').disabled = false;
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				var t = d[j].indexValue,
					cc = ColorValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				GPSTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(t, null,
					new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				var labelgraphic = new esri.Graphic(pt, textsym);
				GPSTrackLabelGraphicsLayer.add(labelgraphic);
				GpsPoints.push(pt);
			}
		}
	} else {
		document.getElementById('label_TrackLayer').disabled = true;
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				var t = d[j].indexValue,
					cc = ColorValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				GPSTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(t, null,
					new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				//var labelgraphic = new esri.Graphic(pt, textsym);
				//GPSTrackLabelGraphicsLayer.add(labelgraphic);
				GpsPoints.push(pt);
			}
		}
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table><tr><td>" + index
		+ "为： </td><td> ${indexValue} </td></tr></table>");
	GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
}

var gpsShowTarget = null;
var pointsFontColorSelected = null;
var pointsFontSizeSelected = null;
var pointZBChoose = null; //指标选择
var deviationX = 0; //偏移量
var deviationY = 0; //偏移量
//yzp 2019-03-27
function ShowGPSTrackRender5g(res) {
	if (undefined != DirectionGraphicsLayer || null != DirectionGraphicsLayer) {
		DirectionGraphicsLayer.clear();
		DirectionGraphicsLayer = null;
		var ima2 = document.getElementById('Azimuth');
		ima2.src = "images/Azimuth_dis.png";
		ima2.title = "显示行驶方向";
		ima2.alt = "显示行驶方向";
	}
	if (undefined != CellLineConGraphicsLayer
		|| null != CellLineConGraphicsLayer) {
		CellLineConGraphicsLayer.clear();
		CellLineConGraphicsLayer = null;
		var ima = document.getElementById('DisplayCleLPoint');
		ima.src = "images/DisplayCleLPoint_dis.png";
		ima.title = "显示小区点连线";
		ima.alt = "显示小区点连线";
	}
	if (undefined != CellToCellGraphicsLayer || null != CellToCellGraphicsLayer) {
		CellToCellGraphicsLayer.clear();
		CellToCellGraphicsLayer = null;
		var ima1 = document.getElementById('DisplayCleLArea');
		ima1.src = "images/DisplayCleLArea_dis.png";
		ima1.title = "显示邻区连线";
		ima1.alt = "显示邻区连线";
	}
	if (undefined != CellLineIconGraphicsLayer
		|| null != CellLineIconGraphicsLayer) {
		CellLineIconGraphicsLayer.clear();
		CellLineIconGraphicsLayer = null;
		var ima3 = document.getElementById('DisplayCleLEvent');
		ima3.src = "images/DisplayCleLEvent_dis.png";
		ima3.title = "显示小区事件连线";
		ima3.alt = "显示小区事件连线";
	}
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	if (undefined == GPSTrackLabelGraphicsLayer
		|| null == GPSTrackLabelGraphicsLayer) {
		GPSTrackLabelGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackLabelGraphicsLayer.clear();
	}
	GPSTrackLabelGraphicsLayer.setVisibility(false);
	if (index == "TM") {
		color = res[1].colors;
		SetTMLengend(color);
	} else {
		color = normalizingColor(res[1].colors);
		SetMapLengend(color, 1);
	}
	//-------------embb图例
	if (pointColorVal != null) {
		console.info(pointColorVal);
		color = colorMapEtgTral[pointColorVal];
		SetMapLengend(color, 1);
	}

	var data = res[0];
	if (data.length == 0) {
		return;
	}
	var index = $("#selectIndex").find("option:selected").text();
	var color;
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	var style = "";
	switch ($("#PointStyle").find("option:selected").val()) {
	case "Circle":
		style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
		break;
	case "Square":
		style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
		break;
	}
	if (index == "TM") {
		document.getElementById('label_TrackLayer').disabled = true;
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				if (d[j] == null || d[j] == []) {
					continue;
				}
				var t = d[j].indexValue,
					cc = ColorTMValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				GPSTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(t, null,
					new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				//var labelgraphic = new esri.Graphic(pt, textsym);
				//GPSTrackLabelGraphicsLayer.add(labelgraphic);
				GpsPoints.push(pt);
			}
		}
	} else if (index == "MOS值") {
		document.getElementById('label_TrackLayer').disabled = false;
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				if (d[j] == null || d[j] == []) {
					continue;
				}
				var t = d[j].indexValue,
					cc = ColorValue(color, t);
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t
				}, null);
				GPSTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(t, null,
					new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				var labelgraphic = new esri.Graphic(pt, textsym);
				GPSTrackLabelGraphicsLayer.add(labelgraphic);
				GpsPoints.push(pt);
			}
		}
	} else {
		document.getElementById('label_TrackLayer').disabled = true;
		for (var i = 0; i < data.length; i++) {
			var d = data[i][1];
			for (var j = 0, n = d.length; j < n; j++) {
				if (d[j] == null || d[j] == []) {
					continue;
				}
				var t = d[j].indexValue;
				var cc;
				/*if(data[i].length > 2 && data[i][2].indexOf("#") != -1){
					cc = data[i][2];
				}else{
					cc = ColorValue(color, t);
				}*/
				//if(pointZBChoose != null){
				cc = ColorValue(color, t);
				//}

				var pt = new esri.geometry.Point(d[j].longitude + (0.00001 * deviationX), d[j].latitude + (0.00001 * deviationY), wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,
					cc);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : t,
					"pci" : d[j].pci,
					"maxBeamNo" : d[j].maxBeamNo,
					"maxRsrp" : d[j].maxRsrp,
					"beamNo" : d[j].beamNo,
					"rsrp" : d[j].rsrp,
					"cellId" : data[i][0],
					"nc1" : d[j].nc1,
					"nc2" : d[j].nc2,
					"nc3" : d[j].nc3,
					"nc4" : d[j].nc4,
					"nc5" : d[j].nc5,
					"nc6" : d[j].nc6,
					"nc7" : d[j].nc7,
					"nc8" : d[j].nc8
				}, null);
				GPSTrackGraphicsLayer.add(startGraphic);
				if (gpsShowTarget) {
					/*var textsym = new esri.symbol.TextSymbol(d[j][gpsShowTarget], null,
							new dojo.Color([0,0,255,1]));*/
					var pointColor = "#000000";
					if (pointsFontColorSelected == '红色') {
						pointColor = "#FF0012";
					} else if (pointsFontColorSelected == '黑色') {
						pointColor = "#000000";
					} else if (pointsFontColorSelected == '蓝色') {
						pointColor = "#0A00FF";
					} else if (pointsFontColorSelected == '绿色') {
						pointColor = "#00FF00";
					}
					var textsym = new esri.symbol.TextSymbol(d[j].indexValue, null,
						new dojo.Color(pointColor));
					textsym.setOffset(2, 4);
					textsym.fofontSize = pointsFontSizeSelected;
					var labelgraphic = new esri.Graphic(pt, textsym);
					GPSTrackLabelGraphicsLayer.add(labelgraphic);
					GPSTrackLabelGraphicsLayer.setVisibility(true);
				}
				GpsPoints.push(pt);
			}
		}
	}

	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'><tr class='tr1'><td style='text-align:left;'>CELLID:</td><td style='text-align:left;'>${cellId}</td><td style='text-align:left;'>PCI:</td><td style='text-align:left;'>${pci}</td></tr>" +
		"<tr class='tr2'><td style='text-align:left;'>最强波束编号:</td><td style='text-align:left;'>${maxBeamNo}</td><td style='text-align:left;'>最强波束RSRP:</td><td style='text-align:left;'>${maxRsrp}</td></tr>" +
		"<tr class='tr1'><td style='text-align:left;'>占用波束编号:</td><td style='text-align:left;'>${maxRsrp}</td><td style='text-align:left;'>占用波束RSRP:</td><td style='text-align:left;'>${beamNo}</td></tr>" +
		"<tr class='tr2'><td style='text-align:left;'>" + index + ":</td><td style='text-align:left;'>${indexValue}</td><td></td><td></td></tr></table>");
	GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);

	map.addLayer(GPSTrackGraphicsLayer);
	map.addLayer(GPSTrackLabelGraphicsLayer);
	if (CellLayers.length == 0) {
		setTimeout(function() {
			//CellLayers[0].emit("click");
			CellLayers[0].on("click", function(event) {
				//console.log(event.graphic.attributes.CELLID);
				var ima = document.getElementById('DisplayCelL_Points');
				ima.src = "images/DisplayCleLPoint_hide.png";
				ima.title = "隐藏小区与采样点连线";
				ima.alt = "隐藏小区与采样点连线";
				var color = res[1].colors;
				if (undefined == CellLineConGraphicsLayer
					|| null == CellLineConGraphicsLayer) {
					CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
					map.addLayer(CellLineConGraphicsLayer);
				} else {
					CellLineConGraphicsLayer.clear();
				}
				var Query = new esri.tasks.Query();
				Query.returnGeometry = true;
				var clc = $("#SelectCellColorLine").find("option:selected").val();
				Query.where = "CELLID='" + event.graphic.attributes.CELLID + "'";
				var pointgraphics = GPSTrackGraphicsLayer.graphics;
				var cc = [];
				//console.log(pointgraphics);
				for (var ii = 0; ii < pointgraphics.length; ii++) {
					if (pointgraphics[ii].attributes.cellId == event.graphic.attributes.CELLID) {
						cc.push({
							indexValue : pointgraphics[ii].attributes.indexValue,
							longitude : pointgraphics[ii].geometry.x,
							latitude : pointgraphics[ii].geometry.y
						});
					}
				}
				QureyResults(cc, Query, clc, color);
			//var geo = event.graphic.geometry;
			//var pt = new esri.geometry.Point(geo.getExtent().getCenter().getLongitude(), geo.getExtent().getCenter().getLatitude(),
			//		wgs);
			});

			//GPSTrackGraphicsLayer.emit("click");
			GPSTrackGraphicsLayer.on("click", function(event) {
				var ima = document.getElementById('DisplayCelL_Points');
				ima.src = "images/DisplayCleLPoint_hide.png";
				ima.title = "隐藏小区与采样点连线";
				ima.alt = "隐藏小区与采样点连线";

				//console.log(event.graphic.attributes.cellId);
				//console.log(event.graphic.geometry.x);
				//console.log(event.graphic.geometry.y);
				var cc = [ {
					indexValue : event.graphic.attributes.indexValue,
					longitude : event.graphic.geometry.x,
					latitude : event.graphic.geometry.y
				} ];
				var color = res[1].colors;
				if (undefined == CellLineConGraphicsLayer
					|| null == CellLineConGraphicsLayer) {
					CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
					map.addLayer(CellLineConGraphicsLayer);
				} else {
					CellLineConGraphicsLayer.clear();
				}

				var clc = $("#SelectCellColorLine").find("option:selected").val();
				//采样点与主小区连线
				var Query = new esri.tasks.Query();
				Query.returnGeometry = true;
				Query.where = "CELLID='" + event.graphic.attributes.cellId + "'";
				QureyResults(cc, Query, clc, color);
				//采样点与邻小区连线
				if (event.graphic.attributes.nc1) {
					var Query1 = new esri.tasks.Query();
					Query1.returnGeometry = true;
					Query1.where = "CELLID='" + event.graphic.attributes.nc1 + "'";
					QureyResults(cc, Query1, clc, color);
				}
				if (event.graphic.attributes.nc2) {
					var Query2 = new esri.tasks.Query();
					Query2.returnGeometry = true;
					Query2.where = "CELLID='" + event.graphic.attributes.nc2 + "'";
					QureyResults(cc, Query2, clc, color);
				}
				if (event.graphic.attributes.nc3) {
					var Query3 = new esri.tasks.Query();
					Query3.returnGeometry = true;
					Query3.where = "CELLID='" + event.graphic.attributes.nc3 + "'";
					QureyResults(cc, Query3, clc, color);
				}
				if (event.graphic.attributes.nc4) {
					var Query4 = new esri.tasks.Query();
					Query4.returnGeometry = true;
					Query4.where = "CELLID='" + event.graphic.attributes.nc4 + "'";
					QureyResults(cc, Query4, clc, color);
				}
				if (event.graphic.attributes.nc5) {
					var Query5 = new esri.tasks.Query();
					Query5.returnGeometry = true;
					Query5.where = "CELLID='" + event.graphic.attributes.nc5 + "'";
					QureyResults(cc, Query5, clc, color);
				}
				if (event.graphic.attributes.nc6) {
					var Query6 = new esri.tasks.Query();
					Query6.returnGeometry = true;
					Query6.where = "CELLID='" + event.graphic.attributes.nc6 + "'";
					QureyResults(cc, Query6, clc, color);
				}
				if (event.graphic.attributes.nc7) {
					var Query7 = new esri.tasks.Query();
					Query7.returnGeometry = true;
					Query7.where = "CELLID='" + event.graphic.attributes.nc7 + "'";
					QureyResults(cc, Query7, clc, color);
				}
				if (event.graphic.attributes.nc8) {
					var Query8 = new esri.tasks.Query();
					Query8.returnGeometry = true;
					Query8.where = "CELLID='" + event.graphic.attributes.nc8 + "'";
					QureyResults(cc, Query8, clc, color);
				}

			});

			HistoryLine.addPath(GpsPoints);
			mapDataExtent = HistoryLine.getExtent();
			console.log('绑定事件1.1' + new Date().getTime());
			map.setExtent(mapDataExtent);
			console.log('绑定事件1.2' + new Date().getTime());
		}, 800);
	} else {
		//CellLayers[0].emit("click");
		CellLayers[0].on("click", function(event) {
			//console.log(event.graphic.attributes.CELLID);
			var ima = document.getElementById('DisplayCelL_Points');
			ima.src = "images/DisplayCleLPoint_hide.png";
			ima.title = "隐藏小区与采样点连线";
			ima.alt = "隐藏小区与采样点连线";
			var color = res[1].colors;
			if (undefined == CellLineConGraphicsLayer
				|| null == CellLineConGraphicsLayer) {
				CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
				map.addLayer(CellLineConGraphicsLayer);
			} else {
				CellLineConGraphicsLayer.clear();
			}
			var Query = new esri.tasks.Query();
			Query.returnGeometry = true;
			var clc = $("#SelectCellColorLine").find("option:selected").val();
			Query.where = "CELLID='" + event.graphic.attributes.CELLID + "'";
			var pointgraphics = GPSTrackGraphicsLayer.graphics;
			var cc = [];
			//console.log(pointgraphics);
			for (var ii = 0; ii < pointgraphics.length; ii++) {
				if (pointgraphics[ii].attributes.cellId == event.graphic.attributes.CELLID) {
					cc.push({
						indexValue : pointgraphics[ii].attributes.indexValue,
						longitude : pointgraphics[ii].geometry.x,
						latitude : pointgraphics[ii].geometry.y
					});
				}
			}
			QureyResults(cc, Query, clc, color);
		//var geo = event.graphic.geometry;
		//var pt = new esri.geometry.Point(geo.getExtent().getCenter().getLongitude(), geo.getExtent().getCenter().getLatitude(),
		//		wgs);
		});

		//GPSTrackGraphicsLayer.emit("click");
		GPSTrackGraphicsLayer.on("click", function(event) {
			var ima = document.getElementById('DisplayCelL_Points');
			ima.src = "images/DisplayCleLPoint_hide.png";
			ima.title = "隐藏小区与采样点连线";
			ima.alt = "隐藏小区与采样点连线";

			//console.log(event.graphic.attributes.cellId);
			//console.log(event.graphic.geometry.x);
			//console.log(event.graphic.geometry.y);
			var cc = [ {
				indexValue : event.graphic.attributes.indexValue,
				longitude : event.graphic.geometry.x,
				latitude : event.graphic.geometry.y
			} ];
			var color = res[1].colors;
			if (undefined == CellLineConGraphicsLayer
				|| null == CellLineConGraphicsLayer) {
				CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
				map.addLayer(CellLineConGraphicsLayer);
			} else {
				CellLineConGraphicsLayer.clear();
			}

			var clc = $("#SelectCellColorLine").find("option:selected").val();
			//采样点与主小区连线
			var Query = new esri.tasks.Query();
			Query.returnGeometry = true;
			Query.where = "CELLID='" + event.graphic.attributes.cellId + "'";
			QureyResults(cc, Query, clc, color);
			//采样点与邻小区连线
			if (event.graphic.attributes.nc1) {
				var Query1 = new esri.tasks.Query();
				Query1.returnGeometry = true;
				Query1.where = "CELLID='" + event.graphic.attributes.nc1 + "'";
				QureyResults(cc, Query1, clc, color);
			}
			if (event.graphic.attributes.nc2) {
				var Query2 = new esri.tasks.Query();
				Query2.returnGeometry = true;
				Query2.where = "CELLID='" + event.graphic.attributes.nc2 + "'";
				QureyResults(cc, Query2, clc, color);
			}
			if (event.graphic.attributes.nc3) {
				var Query3 = new esri.tasks.Query();
				Query3.returnGeometry = true;
				Query3.where = "CELLID='" + event.graphic.attributes.nc3 + "'";
				QureyResults(cc, Query3, clc, color);
			}
			if (event.graphic.attributes.nc4) {
				var Query4 = new esri.tasks.Query();
				Query4.returnGeometry = true;
				Query4.where = "CELLID='" + event.graphic.attributes.nc4 + "'";
				QureyResults(cc, Query4, clc, color);
			}
			if (event.graphic.attributes.nc5) {
				var Query5 = new esri.tasks.Query();
				Query5.returnGeometry = true;
				Query5.where = "CELLID='" + event.graphic.attributes.nc5 + "'";
				QureyResults(cc, Query5, clc, color);
			}
			if (event.graphic.attributes.nc6) {
				var Query6 = new esri.tasks.Query();
				Query6.returnGeometry = true;
				Query6.where = "CELLID='" + event.graphic.attributes.nc6 + "'";
				QureyResults(cc, Query6, clc, color);
			}
			if (event.graphic.attributes.nc7) {
				var Query7 = new esri.tasks.Query();
				Query7.returnGeometry = true;
				Query7.where = "CELLID='" + event.graphic.attributes.nc7 + "'";
				QureyResults(cc, Query7, clc, color);
			}
			if (event.graphic.attributes.nc8) {
				var Query8 = new esri.tasks.Query();
				Query8.returnGeometry = true;
				Query8.where = "CELLID='" + event.graphic.attributes.nc8 + "'";
				QureyResults(cc, Query8, clc, color);
			}

		});

		setTimeout(function() {
			HistoryLine.addPath(GpsPoints);
			mapDataExtent = HistoryLine.getExtent();
			console.log('绑定事件2.1,' + new Date().getTime());
			map.setExtent(mapDataExtent);
			console.log('绑定事件2.2,' + new Date().getTime());
		}, 800);
	}
}
//
var EventTrackGraphicsLayer;
var EventTrackLabelLayer;
function ShowEventTrackRender(res) {
	if (undefined == EventTrackGraphicsLayer || null == EventTrackGraphicsLayer) {
		EventTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		EventTrackGraphicsLayer.clear();
	}
	if (undefined == EventTrackLabelLayer || null == EventTrackLabelLayer) {
		EventTrackLabelLayer = new esri.layers.GraphicsLayer();
	} else {
		EventTrackLabelLayer.clear();
	}
	var data = res;
	if (data.length == 0) {
		return;
	}
	for (var i = 0; i < data.length; i++) {
		var d = data[i][1];
		for (var j = 0, n = d.length; j < n; j++) {
			var icon = d[j].iconType;
			var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude, wgs);
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/call/" + icon + ".png",
				"type" : "esriPMS"
			});
			symbol.setHeight(16);
			symbol.setWidth(16);
			var startGraphic = new esri.Graphic(pt, symbol, null, null);
			EventTrackGraphicsLayer.add(startGraphic);
			var textsym = new esri.symbol.TextSymbol(EventLabel(icon), null,
				new dojo.Color([ 0, 0, 230, 1 ]));
			textsym.setOffset(12, -15);
			var font = new esri.symbol.Font();
			font.setSize("12px");
			textsym.setFont(font);
			textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
			textsym.setHaloSize(1);
			var labelGraphic = new esri.Graphic(pt, textsym);
			EventTrackLabelLayer.add(labelGraphic);
		}
	}
	var check = document.getElementById('label_EventLayer').checked;
	EventTrackLabelLayer.setVisibility(check);
	map.addLayer(EventTrackGraphicsLayer);
	map.addLayer(EventTrackLabelLayer);
}
// 创建图例
function SetMapLengend(Rendercolor, opacity) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	// console.log(Rendercolor);
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		var start = Rendercolor[i].beginValue;
		var end = Rendercolor[i].endValue;
		color = Rendercolor[i].color;
		// color=color.colorRgb();
		// color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 50px;background-color:" + color
			+ "'></td><td>";
		var td = $("#KpiIndexTool").find("option:selected").text();
		if (start != null) {
			td = start + "≤" + td;
		}
		if (end != null) {
			td = td + "&lt" + end;
		}
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}

//创建embb图例
function SetEmbbMapLengend(Rendercolor, layerName) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	// console.log(Rendercolor);
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		var start = Rendercolor[i].beginValue;
		var end = Rendercolor[i].endValue;
		color = Rendercolor[i].color;
		//		 color=color.colorRgb();
		//		 color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 50px;background-color:" + color
			+ "'></td><td>";
		var td = layerName;
		if (start != null) {
			td = start + "≤" + td;
		}
		if (end != null) {
			td = td + "&lt" + end;
		}
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}

//创建pci图例
function SetPCiLengend(pciArry) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	// console.log(Rendercolor);
	for (let key in pciArry) {
		var color = pciArry[key].color;
		//		 color=color.colorRgb();
		//		 color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 50px;background-color:" + color
			+ "'></td><td> ";
		var td = key;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}

function RgbToRgba(rgb, a) {
	var aColor = rgb.replace(/(?:||rgb|RGB)*/g, "").replace(")", "").replace(
		"(", "").split(",");
	var s = "rgba(" + aColor.join(",") + "," + a + ")";
	return s;
}
var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
/* RGB颜色转换为16进制 */

String.prototype.colorHex = function() {
	var that = this;
	if (/^(rgb|RGB)/.test(that)) {
		var aColor = that.replace(/(?:||rgb|RGB)*/g, "").split(",");
		var strHex = "#";
		for (var i = 0; i < aColor.length; i++) {
			var hex = Number(aColor[i]).toString(16);
			if (hex === "0") {
				hex += hex;
			}
			strHex += hex;
		}
		if (strHex.length !== 7) {
			strHex = that;
		}
		return strHex;
	} else if (reg.test(that)) {
		var aNum = that.replace(/#/, "").split("");
		if (aNum.length === 6) {
			return that;
		} else if (aNum.length === 3) {
			var numHex = "#";
			for (var i = 0; i < aNum.length; i += 1) {
				numHex += (aNum[i] + aNum[i]);
			}
			return numHex;
		}
	} else {
		return that;
	}
};
// -------------------------------------------------
/* 16进制颜色转为RGB格式 */
String.prototype.colorRgb = function() {
	var sColor = this.toLowerCase();
	if (sColor && reg.test(sColor)) {
		if (sColor.length === 4) {
			var sColorNew = "#";
			for (var i = 1; i < 4; i += 1) {
				sColorNew += sColor.slice(i, i + 1).concat(
					sColor.slice(i, i + 1));
			}
			sColor = sColorNew;
		}
		// 处理六位的颜色值
		var sColorChange = [];
		for (var i = 1; i < 7; i += 2) {
			sColorChange.push(parseInt("0x" + sColor.slice(i, i + 2)));
		}
		return "RGB(" + sColorChange.join(",") + ")";
	} else {
		return sColor;
	}
};
// 获取颜色值
//更改999999为"∞"
function normalizingColor(color) {
	var cc = color;
	for (var i = 0, k = cc.length; i < k; i++) {
		if (!$.isNumeric(cc[i].beginValue)) {
			cc[i].beginValue = "-∞";
		}
		if (!$.isNumeric(cc[i].endValue)) {
			cc[i].endValue = "+∞";
		}
	}
	return cc;
}
// 给的采样点颜色
function ColorValue(color, value) {
	var v = "";
	for (var i = 0, k = color.length; i < k; i++) {
		if (color[i].beginValue == "-∞"
			&& Number(value) <= Number(color[i].endValue)) {
			v = color[i].color;
			break;
		}
		if (color[i].endValue == "+∞"
			&& Number(value) >= Number(color[i].beginValue)) {
			v = color[i].color;
			break;
		}
		if (Number(color[i].beginValue) <= Number(value)
			&& Number(value) < Number(color[i].endValue)) {
			v = color[i].color;
			break;
		}
	}
	return v;
}
function ColorTMValue(color, value) {
	var v = "";
	for (var i = 0, k = color.length; i <= k; i++) {
		if (i == k) {
			v = color[k - 1].color;
			break;
		}
		if (undefined != color[i].tmType && color[i].tmType == value) {
			v = color[i].color;
			break;
		}

	}
	return v;
}
// 跟小区颜色一样
function Model3Color(value) {
	var c = null;
	switch (value) {
	case "0":
		c = document.getElementById('Color0').value;
		break;
	case "1":
		c = document.getElementById('Color1').value;
		break;
	default:
	case "2":
		c = document.getElementById('Color2').value;
		break;
	}
	return c;
}
// 主动指标 轨迹点 渲染
function drawQBR() {
	GPSTrackRender();
}
//yzp 2019-03-26 主动EMBB指标渲染
function drawEmbb(tooltype_) {
	toolbarType = tooltype_;
	showDivByDivId('DisplayCelL_Points');
	showDivByDivId('Azimuth');
	GPSTrackRender();
}

//maxuancheng 2019-04-02 质差干扰指标渲染
function drawQuality5g(tooltype_) {
	toolbarType = tooltype_;
	showDivByDivId('DisplayCelL_Points');
	showDivByDivId('Azimuth');
	GPSTrackRender();
}

//
// 主动异常事件渲染
function drawEE() {
	GPSTrackRender();
}
function drawHOF() {
	GPSTrackRender();
}
function drawCWBR() {
	GPSTrackRender();
}
function drawCEDE() {
	GPSTrackRender();
}

function drawMosBad() {
	GPSTrackRender();
}
function drawCompareHof() {
	GPSTrackRender();
}

function drawVideoQBR() {
	GPSTrackRender();
}
function drawStreamQBR() {
	GPSTrackRender();
}
function drawTerminal(obj) {
	showTerminalTrack(obj);
	updateTerminalTrack(obj);
}

function drawLostPacket() {
	var res = parent.getTLI2LPRequestParam();
	var content = {
		testLogItemIds : res.testLogItemIds,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 400000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		if (responses != null) {
			ShowGpsTrackRTP(responses);
		}
	});
}
//所有连续无限差路段的轨迹点显示
function ShowGpsTrackRTP(res) {
	var colors = res[1].colors;
	setMapRPTLegend(colors);
	var data = res[0];
	for (var i = 0, ll = colors.length; i < ll; i++) {
		for (var j = 0, nn = data.length; j < nn; j++) {
			if (colors[i].lpType == data[j][0]) {
				var color = colors[i].color;
				var da = data[j],
					mm = da.length;
				if (mm > 1) {
					for (var k = 1; k < mm; k++) {
						var d = compress(da[k], 0.5);
						showLinesymByData(d, color);
					}
				}
				break;
			}
		}
	}
}

var TerminalHistoryTime = null;
var pLength = 0;
var pIndex = 0;
var pData = null;
function drawTerminalHistory(obj1, obj2) {
	if (null != timer2 || undefined != timer2) {
		clearTimeout(timer2);
	}
	if (null != HistoryTrackPointLayer || undefined != HistoryTrackPointLayer) {
		HistoryTrackPointLayer.clear();
	}
	if (null != TerminalHistoryGraphicsLayer
		|| undefined != TerminalHistoryGraphicsLayer) {
		TerminalHistoryGraphicsLayer.clear();
	}
	pLength = obj2[1][0][1].length;
	if (pLength === 0) {
		return;
	}
	showTerminalHistoryTrack(obj1);
	TerminalHistoryTime = obj2[0].time;
	pData = obj2[1][0][1];
	pIndex = 0;
	setTimeout('replayHistoryTrack()', 2000);
//setTimeout(show(obj), 2000);
}
var timer2 = null;
function pauseHistoryTrack(num) {
	if (num == 0) {
		if (null != timer2 || undefined != timer2) {
			clearTimeout(timer2);
		}
	} else if (num == 1) {
		if (pIndex <= pLength) {
			setTimeout('replayHistoryTrack()', 1000);
		} else
			return;
	}
}

var HistoryTrackPointLayer = null;
function replayHistoryTrack() {
	if (pIndex <= pLength) {
		if (undefined == HistoryTrackPointLayer
			|| null == HistoryTrackPointLayer) {
			HistoryTrackPointLayer = new esri.layers.GraphicsLayer();
			map.addLayer(HistoryTrackPointLayer);
		} else {
			HistoryTrackPointLayer.clear();
		}
		var pt = new esri.geometry.Point(pData[pIndex].longitude,
			pData[pIndex].latitude, wgs);
		var pointSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 9,
			new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID,
				new dojo.Color([ 23, 145, 255, 0.6 ]), 5),
			new dojo.Color([ 23, 145, 255, 1.0 ]));
		var pointGraphic = new esri.Graphic(pt, pointSymbol);
		HistoryTrackPointLayer.add(pointGraphic);
		pIndex = pIndex + 50;
		timer2 = setTimeout('replayHistoryTrack()', 2000);
	} else {
		if (undefined == HistoryTrackPointLayer
			|| null == HistoryTrackPointLayer) {
			HistoryTrackPointLayer = new esri.layers.GraphicsLayer();
			map.addLayer(HistoryTrackPointLayer);
		} else {
			HistoryTrackPointLayer.clear();
		}
		var pt = new esri.geometry.Point(pData[pLength - 1].longitude,
			pData[pLength - 1].latitude, wgs);
		var pointSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 9,
			new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID,
				new dojo.Color([ 23, 145, 255, 0.6 ]), 5),
			new dojo.Color([ 23, 145, 255, 1.0 ]));
		var pointGraphic = new esri.Graphic(pt, pointSymbol);
		HistoryTrackPointLayer.add(pointGraphic);
	}
}

// 主动问题路段渲染
function DisplayDirection(res) {
	if (DirectionGraphicsLayer == undefined || DirectionGraphicsLayer == null) {
		DirectionGraphicsLayer = new esri.layers.GraphicsLayer();
		DirectionGraphicsLayer.setScaleRange(32000, 0);
		map.addLayer(DirectionGraphicsLayer);
	}
	var d = res;
	if (d.length > 1000) {
		var thres = d.length / 3000;
		d = compress(res, thres);
	}
	AddSymbolByPointAngle(d);
}
var DirectionGraphicsLayer;
function AddSymbolByPointAngle(d) {
	var b = GetBusDirection(d[0], d[1]).Dir;
	var wandao = [],
		zhixian = [];
	for (var j = 0; j < d.length - 2; j++) {
		var res = GetBusDirection(d[j], d[j + 1]);
		d[j].Dir = res.Dir;
		if (Math.abs(res.Dir - b) > 90) {
			wandao.push(d[j]);
			b = res.Dir;
		} else {
			zhixian.push(d[j]);
		}
	}
	var kk = 2,
		vv = 2;
	if (wandao.length > 100) {
		kk = Math.floor(wandao.length / 7) + 1;
	}
	if (zhixian.length > 100) {
		vv = Math.floor(zhixian.length / 14) + 1;
	}
	for (var k = 0; k < wandao.length; k++) {
		if (k % kk == 0) {
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/arrow_up.png",
				"height" : 16,
				"width" : 16,
				"xoffset" : 7,
				"yoffset" : 7,
				"type" : "esriPMS",
				"angle" : d[k].Dir
			});
			var pt = new esri.geometry.Point(d[k].longitude, d[k].latitude, wgs);
			var startGraphic = new esri.Graphic(pt, symbol, {
				"Dir" : d[k].Dir
			}, null);
			DirectionGraphicsLayer.add(startGraphic);
		}
	}
	for (var i = 0; i < zhixian.length; i++) {
		if (i % vv == 0) {
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/arrow_up.png",
				"height" : 16,
				"width" : 16,
				"xoffset" : 7,
				"yoffset" : 7,
				"type" : "esriPMS",
				"angle" : d[i].Dir
			});
			var pt = new esri.geometry.Point(d[i].longitude, d[i].latitude, wgs);
			var startGraphic = new esri.Graphic(pt, symbol, {
				"Dir" : d[k].Dir
			}, null);
			DirectionGraphicsLayer.add(startGraphic);
		}
	}
}
// / <summary>
// /计算两点GPS坐标的距离
// / </summary>
// / <param name="n1">第一点的纬度坐标</param>
// / <param name="e1">第一点的经度坐标</param>
// / <param name="n2">第二点的纬度坐标</param>
// / <param name="e2">第二点的经度坐标</param>
// / <returns></returns>
function Distance(n1, e1, n2, e2) {
	var jl_jd = 102834.74258026089786013677476285;
	var jl_wd = 111712.69150641055729984301412873;
	var a = {};
	a.longitude = (e2 - e1) * jl_jd;
	a.latitude = (n2 - n1) * jl_wd;
	return a;
}
// / <summary>
// / 已知汽车行驶的两个GPS点，求汽车行驶的方向
// / </summary>
// / <param name="n1">第一个GPS点纬度</param>
// / <param name="e1">第一个GPS点经度</param>
// / <param name="n2">第二个GPS点纬度</param>
// / <param name="e2">第二个GPS点经度</param>
// / <returns></returns>
function GetBusDirection(p1, p2) {
	var n1 = p1.latitude,
		e1 = p1.longitude,
		n2 = p2.latitude,
		e2 = p2.longitude;
	var e3 = e1 + 0.005;
	var n3 = n1;
	var a = Distance(n1, e1, n2, e2);
	var res = {};
	res.Dir = Math.atan2(a.latitude, a.longitude) * 180 / Math.PI - 90;
	res.d = Math.sqrt(Math.pow(a.latitude, 2) + Math.pow(a.longitude, 2));
	// if (a.longitude > 0 && a.latitude >= 0) {
	// Dir =90- Math.atan2(a.longitude,a.latitude);
	// } else if (a.longitude < 0 && a.latitude >= 0) {
	// Dir = Math.atan(Math.abs(a.latitude / a.longitude)) + 270;
	// } else if (a.longitude < 0 && a.latitude <= 0) {
	// Dir = Math.atan(Math.abs(a.latitude / a.longitude))+180;
	// } else if (a.longitude > 0 && a.latitude <= 0) {
	// Dir = Math.atan(Math.abs(a.latitude / a.longitude)) + 90;
	// }
	return res;
}
/* 。。。。。。。。。。。。。。。。。。。。。。。。。添加小区与邻区的连线 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。 */

function CellToCellconection() {
	if (toolbarType == "33") {
		var res = parent.getVideoQBRCellToCellRequestParam();
		if (res.videoQualityBadId == null || res.videoQualityBadId == undefined) {
			alert("必须先选择路段");
			var ima11 = document.getElementById('DisplayCleLArea');
			ima11.src = "images/DisplayCleLArea_dis.png";
			ima11.title = "显示邻区连线";
			ima11.alt = "显示邻区连线";
			return;
		}
		var content = {
			videoQualityBadId : res.videoQualityBadId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				var k = re.length;
				if (k == 0) {
					alert("未查询到邻区数据！");
					var ima11 = document.getElementById('DisplayCleLArea');
					ima11.src = "images/DisplayCleLArea_dis.png";
					ima11.title = "显示邻区连线";
					ima11.alt = "显示邻区连线";
					return;
				}
				ShowCellToCell(re);
			}
		});
	}
	if (toolbarType == "3" || toolbarType == "70") {
		var res = parent.getQBRCellToCellRequestParam();
		if (res.badRoadId == null || res.badRoadId == undefined) {
			alert("必须先选择路段");
			var ima11 = document.getElementById('DisplayCleLArea');
			ima11.src = "images/DisplayCleLArea_dis.png";
			ima11.title = "显示邻区连线";
			ima11.alt = "显示邻区连线";
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				var k = re.length;
				if (k == 0) {
					alert("未查询到邻区数据！");
					var ima11 = document.getElementById('DisplayCleLArea');
					ima11.src = "images/DisplayCleLArea_dis.png";
					ima11.title = "显示邻区连线";
					ima11.alt = "显示邻区连线";
					return;
				}
				ShowCellToCell(re);
			}
		});
	}
	if (toolbarType == "11") {
		var res = parent.getHOFCellToCellRequestParam();
		if (res.hofId == null || res.hofId == undefined) {
			alert("必须先选择路段");
			var ima11 = document.getElementById('DisplayCleLArea');
			ima11.src = "images/DisplayCleLArea_dis.png";
			ima11.title = "显示邻区连线";
			ima11.alt = "显示邻区连线";
			return;
		}
		var content = {
			hofId : res.hofId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				var k = re.length;
				if (k == 0) {
					alert("未查询到邻区数据！");
					var ima11 = document.getElementById('DisplayCleLArea');
					ima11.src = "images/DisplayCleLArea_dis.png";
					ima11.title = "显示邻区连线";
					ima11.alt = "显示邻区连线";
					return;
				}
				ShowCellToCell(re);
			}
		});
	}
	if (toolbarType == "15") {
		var res = parent.getCWBRCellToCellRequestParam();
		if (res.badRoadId == null || res.badRoadId == undefined) {
			alert("必须先选择路段");
			var ima11 = document.getElementById('DisplayCleLArea');
			ima11.src = "images/DisplayCleLArea_dis.png";
			ima11.title = "显示邻区连线";
			ima11.alt = "显示邻区连线";
			return;
		}
		var content = {
			badRoadId : res.badRoadId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				var k = re.length;
				if (k == 0) {
					alert("未查询到邻区数据！");
					var ima11 = document.getElementById('DisplayCleLArea');
					ima11.src = "images/DisplayCleLArea_dis.png";
					ima11.title = "显示邻区连线";
					ima11.alt = "显示邻区连线";
					return;
				}
				ShowCellToCell(re);
			}
		});
	}

	if (toolbarType == "24" || toolbarType == "25") {
		var res = parent.getCompareHofCellToCellRequestParam();
		if (res.testLogItemIds == null || res.testLogItemIds == undefined
			|| res.compareTestLogItemIds == null
			|| res.compareTestLogItemIds == undefined
			|| res.hofType == null || res.hofType == undefined
			|| res.cellId == null || res.cellId == undefined) {
			return;
		}
		var content = {
			testLogItemIds : res.testLogItemIds,
			compareTestLogItemIds : res.compareTestLogItemIds,
			hofType : res.hofType,
			failId : res.failId,
			cellId : res.cellId,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(re) {
			if (re != null) {
				ShowCellToCell(re);
			}
		});
	}

}
var CellToCellGraphicsLayer = null;
function ShowCellToCell(res) {
	if (undefined == CellToCellGraphicsLayer || null == CellToCellGraphicsLayer) {
		CellToCellGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(CellToCellGraphicsLayer);
	} else {
		CellToCellGraphicsLayer.clear();
	}
	for (var i = 0; i < res.length; i++) {
		if (res[i].cellId == res[i].nbCellId) {
			return;
		}
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;
		Query.where = "CELLID='" + res[i].cellId + "' OR CELLID='"
			+ res[i].nbCellId + "'";
		CellLayers[0].queryFeatures(Query, function(geometry) {
			var geo = geometry.features[0].geometry;
			var geo1 = geometry.features[1].geometry;
			var t = geometry.features[0].attributes.MODEL3;
			var clc = Model3Color(t);
			var pt = new esri.geometry.Point(geo.getExtent().getCenter()
				.getLongitude(), geo.getExtent().getCenter().getLatitude(),
				wgs);
			var p1 = new esri.geometry.Point(geo1.getExtent().getCenter()
				.getLongitude(),
				geo1.getExtent().getCenter().getLatitude(), wgs);
			var line = new esri.geometry.Polyline(wgs);
			line.addPath([ pt, p1 ]);
			mapDataExtent = line.getExtent();
			map.setExtent(mapDataExtent);
			var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
			var GpsHistorySymbol = new esri.Graphic(line, GpsLineSymbol, null,
				null);
			CellToCellGraphicsLayer.add(GpsHistorySymbol);
		}, function(err) {
			alert(err);
			return;
		});
	}
}
// 加载小区图层
// bl表示小区是否居中显示
function AddCellbyArray(responses, bl) {
	if (responses[0] == null || responses[0].length == 0) {
		return;
	}
	CellLayers = [];
	var cells = responses[0];
	var color = responses[1];
	var locateCenter = responses[4].split(",");
	var layerName_Model = responses[5];
	console.info(locateCenter);
	$("#Color0").val(color.colors[0].color);
	$("#Color1").val(color.colors[1].color);
	$("#Color2").val(color.colors[2].color);
	//	document.getElementById('layerVisible').checked = true;
	//	document.getElementById('layerLabels').disabled = false;
	for (var i = 0; i < cells.length; i++) {
		if (layerName_Model.lte) {
			if (layerName_Model.lte == cells[i]) {
				AddCellToMap(cells[i] + ".shp", bl, "lte");
			}
		}
		if (layerName_Model.nr) {
			if (layerName_Model.nr == cells[i]) {
				AddCellToMap(cells[i] + ".shp", bl, "nr");
			}
		}
	}
	timeoutFlag2 = setInterval(function() {
		mapCenterAndZoom(locateCenter[0], locateCenter[1]);
		clearInterval(timeoutFlag2);
	}, 3000);

}

function setCellLayers(layer) {
	CellLayers.push(layer);
}
function drawCell() {
	var n = CellLayers.length;
	if (n > 0) {

		if (LabelCellLayers.length > 0) {
			for (var i = 0, n = CellLayers.length; i < n; i++) {
				map.removeLayer(LabelCellLayers[i]);
				map.removeLayer(CellLayers[i]);
			}
		} else {
			for (var i = 0, n = CellLayers.length; i < n; i++) {
				map.removeLayer(CellLayers[i]);
			}
		}
		CellLayers = [];
		LabelCellLayers = [];
	}
	var res = parent.getCellRequestParam();
	var content = {
		testLogItemIds : res.testLogItemIds,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 400000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		if (responses != null) {
			AddCellbyArray(responses, false);
		}
	});
}
// 设置下来指标值
function setSelectVal(kpiIndex) {
	var s = document.getElementById("selectIndex");
	if (s == null) {
		return;
	}
	var n = kpiIndex.length;
	for (var i = 0; i < n; i++) {
		s.options[i] = new Option(kpiIndex[i].name, kpiIndex[i].value);
	}
}
// 设置主被叫下拉值
function setCallTypeVal(callTypeIndex) {
	var s = document.getElementById("callSelectIndex");
	if (s == null) {
		return;
	}
	var n = callTypeIndex.length;
	for (var i = 0; i < n; i++) {
		s.options[i] = new Option(callTypeIndex[i].name, callTypeIndex[i].value);
	}
}
// 设置工具栏显示与否
function navEvent(id) {
	switch (id) {
	case 'Pan': { // 漫游
		map.enablePan();
		navToolbar.activate(esri.toolbars.Navigation.PAN);
		map.setMapCursor("default");
		break;
	}
	case 'PrevExtent': { // 前一视图
		navToolbar.zoomToPrevExtent();
		break;
	}
	case 'NextExtent': { // 后一视图
		navToolbar.zoomToNextExtent();
		break;
	}
	case 'FullExtent':
		// navToolbar.zoomToFullExtent();
		//map.extent = layer_diTu.fullExtent;
		var inPoint = new esri.geometry.Point(109.1540317, 33.735199607, wgs);
		map.centerAndZoom(inPoint, 5);
		break;
	case 'zoom_in': { // 放大
		navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);
		break;
	}
	case 'zoom_out': { // 缩小
		navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);
		break;
	}
	case 'ToolsLegend': {
		break;
	}
	case 'clear':
		mapClear();
		break;
	case 'deactivate':
		navToolbar.deactivate();
		break;
	case 'DistanceMeasure':
		DistanceByTwopoints();
		break;
	case 'DrawRectangleFrame':
		drawRectangleFrame();
		break;
	case 'AddPoints':
		AddpointSymbol();
		break;
	case 'Cluster':
		ProjectPoints();
		break;
	case 'MapChange': { // 图源切换
		var $a1 = $("#MapResource");
		if ($a1.is(":hidden")) {
			$a1.css("display", "block");
		} else {
			$a1.css("display", "none");
		}
		break;
	}
	case 'layerControl': { // 图层控制
		var $a = $("#mapLayerControl");
		if ($a.is(":hidden")) {
			$a.css("display", "block");
		} else {
			$a.css("display", "none");
		}
		break;
	}
	case 'timeSlider': {
		var $b = $("#slider");
		if ($b.is(":hidden")) {
			$b.css("display", "block");
			SetOpacity();
		} else {
			$b.css("display", "none");
		}
		break;
	}
	case 'GPS': { // GPS轨迹
		break;
	}
	case 'Cell': { // 小区绘制
		GetCell();
		break;
	}
	case 'TargetSelect': { // 读取指标
		var $ab = $("#dropDownButtonContainer");
		if ($ab.is(":hidden")) {
			$ab.css("display", "block");
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
			dojo.byId("dropDownButtonContainer").remove();
			var myDiv = document.createElement('div');
			myDiv.setAttribute("id", "dropDownButtonContainer");
			dojo.byId("MapTool").appendChild(myDiv);
			dojo.byId("dropDownButtonContainer").appendChild(myButton.domNode);
			myButton.startup();
		} else {
			$ab.css("display", "none");
		}
		break;
	}
	case "GPSSearch": { // 经纬度搜索
		myFormDialog.show();
		break;
	}
	//yzp 2019-03-28
	case 'DisplayCelL_Points': {
		var ima = document.getElementById('DisplayCelL_Points');
		if (ima.title == "隐藏小区与采样点连线") {
			ima.src = "images/DisplayCleLPoint_dis.png";
			ima.title = "显示小区与采样点连线";
			ima.alt = "显示小区与采样点连线";
			DisplayCellPoint(false);
		} else {
			ima.src = "images/DisplayCleLPoint_hide.png";
			ima.title = "隐藏小区与采样点连线";
			ima.alt = "隐藏小区与采样点连线";
			DisplayCellPoint(true);
		}
		break;
	}
	//
	case 'DisplayCleLPoint': { // 显示小区点连线
		var ima = document.getElementById('DisplayCleLPoint');
		if (ima.title == "显示小区点连线") {
			DisplayCleLPoint(true);
			ima.src = "images/DisplayCleLPoint_hide.png";
			ima.title = "隐藏小区点连线";
			ima.alt = "隐藏小区点连线";
		} else {
			DisplayCleLPoint(false);
			ima.src = "images/DisplayCleLPoint_dis.png";
			ima.title = "显示小区点连线";
			ima.alt = "显示小区点连线";
		}
		break;
	}
	case 'DisplayCleLEvent': { // 显示小区事件连线
		var ima3 = document.getElementById('DisplayCleLEvent');
		if (ima3.title == "显示小区事件连线") {
			DisplayCleLEvent(true);
			ima3.src = "images/DisplayCleLEvent_hide.png";
			ima3.title = "隐藏小区事件连线";
			ima3.alt = "隐藏小区事件连线";
		} else {
			DisplayCleLEvent(false);
			ima3.src = "images/DisplayCleLEvent_dis.png";
			ima3.title = "显示小区事件连线";
			ima3.alt = "显示小区事件连线";
		}
		break;
	}
	case 'DisplayCleLArea': { // 显示邻区连线
		var ima1 = document.getElementById('DisplayCleLArea');
		if (ima1.title == "显示邻区连线") {
			ima1.src = "images/DisplayCleLArea_hide.png";
			ima1.title = "隐藏邻区连线";
			ima1.alt = "隐藏邻区连线";
			DisplayCleLArea(true);
		} else {
			ima1.src = "images/DisplayCleLArea_dis.png";
			ima1.title = "显示邻区连线";
			ima1.alt = "显示邻区连线";
			DisplayCleLArea(false);
		}
		break;
	}
	case 'DisplayPCI': { // 显示模3核查
		var ima11 = document.getElementById('DisplayPCI');
		if (ima11.title == "显示模3核查") {
			ima11.src = "images/DisplayPCI_hide.png";
			ima11.title = "隐藏模3核查";
			ima11.alt = "隐藏模3核查";
			DisplayPCI(true);
		} else {
			ima11.src = "images/DisplayPCI_dis.png";
			ima11.title = "显示模3核查";
			ima11.alt = "显示模3核查";
			DisplayPCI(false);
		}
		break;
	}
	case 'DisplayTAC': { // 显示TAC
		var ima12 = document.getElementById('DisplayTAC');
		if (ima12.title == "显示TAC分布") {
			ima12.src = "images/DisplayTAC_hide.png";
			ima12.title = "隐藏TAC分布";
			ima12.alt = "隐藏TAC分布";
			DisplayTAC(true);
		} else {
			ima12.src = "images/DisplayTAC_dis.png";
			ima12.title = "显示TAC分布";
			ima12.alt = "显示TAC分布";
			DisplayTAC(false);
		}
		break;
	}
	case 'Azimuth': { // 显示行驶方向
		var ima2 = document.getElementById('Azimuth');
		if (ima2.title == "显示行驶方向") {
			Azimuth(true);
			ima2.src = "images/Azimuth_hide.png";
			ima2.title = "隐藏行驶方向";
			ima2.alt = "隐藏行驶方向";
		} else {
			Azimuth(false);
			ima2.src = "images/Azimuth_dis.png";
			ima2.title = "显示行驶方向";
			ima2.alt = "显示行驶方向";
		}
		break;
	}
	case 'TrackOffset': { // 对比日志轨迹偏移
		var ima21 = document.getElementById('TrackOffset');
		if (ima21.title == "对比日志轨迹偏移") {
			TrackOffset(CompareTrack, true);
			ima21.title = "对比日志轨迹还原";
			ima21.alt = "对比日志轨迹还原";
		} else {
			TrackOffset(CompareTrack, false);
			ima21.title = "对比日志轨迹偏移";
			ima21.alt = "对比日志轨迹偏移";
		}
		break;
	}
	case 'MapDataCenter': {
		if (null == mapDataExtent) {
			alert('数据范围为空');
		} else {
			map.setExtent(mapDataExtent);
		}
		break;
	}
	case 'SearchCell': { // 查找小区
		showDivByDivId('DivQueryCell');
		break;
	}
	case 'selectGPSPoint': { // 查找小区
		getPointSymbol();
		break;
	}
	}
}

// 查找小区
function QueryCell() {
	var QueryCon = new dijit.registry.byId('QueryCon').get('value');
	var QueryVal = new dijit.registry.byId('QueryValue').get('value');
	var Query = new esri.tasks.Query();
	Query.returnGeometry = true;
	switch (QueryCon) {
	case 'CELLID':
		Query.where = "CELLID='" + QueryVal + "'";
		break;
	case 'CELLNAME':
		Query.where = "CELLNAME like '%" + QueryVal + "%'";
		break;
	case 'ENODEBID':
		Query.where = "GNODEBID='" + QueryVal + "' ";
		console.info(Query.where);
		break;
	case 'SITENAME':
		// 基站名称
		Query.where = "SITENAME like '%" + QueryVal + "%'";
		break;
	}
	showWaitFlag(true);
	CellLayers[0].queryFeatures(Query, QuerySuccess, shibai);
}
function QuerySuccess(response) {
	var features = response.features;
	var n = features.length;
	if (n == 0) {
		QuerySecond();
		return;
	}
	map.setZoom(map.__tileInfo.lods.length - 1);
	map.graphics.clear();
	var sym = new esri.symbol.SimpleFillSymbol("solid", null, new dojo.Color([
		0, 0, 255, 1 ]));
	var feature = null;
	for (var i = 0; i < n; i++) {
		feature = features[i];
		feature.setSymbol(sym);
		map.graphics.add(feature);
	}
	map.centerAt(feature.geometry.getExtent().getCenter());
	showWaitFlag(false);
// map.setZoom(map.__tileInfo.lods.length-1);
//	map.setLevel(map.__tileInfo.lods.length - 1);
//	// QuerySecond();
//	setTimeout(QuerySecond, 1500);
}
function QuerySecond() {
	var QueryCon = new dijit.registry.byId('QueryCon').get('value');
	var QueryVal = new dijit.registry.byId('QueryValue').get('value');
	var Query = new esri.tasks.Query();
	Query.returnGeometry = true;
	switch (QueryCon) {
	case 'CELLID':
		Query.where = "CELLID='" + QueryVal + "'";
		break;
	case 'CELLNAME':
		Query.where = "CELLNAME like '%" + QueryVal + "%'";
		break;
	case 'ENODEBID':
		Query.where = "ENODEBID='" + QueryVal + "'";
		break;
	case 'SITENAME':
		// 基站名称
		Query.where = "SITENAME like '%" + QueryVal + "%'";
		break;
	}
	CellLayers[1].queryFeatures(Query, QuerySuccess1, shibai);
}
function shibai(ex) {
	showWaitFlag(false);
	alert(ex.toString());
}
function QuerySuccess1(response) {
	var features = response.features;
	var n = features.length;
	if (n == 0) {
		showWaitFlag(false);
		alert("未查询到数据");
	}
	map.setZoom(map.__tileInfo.lods.length - 1);
	map.graphics.clear();
	var sym = new esri.symbol.SimpleFillSymbol("solid", null, new dojo.Color([
		0, 0, 255, 1 ]));
	var feature = null;
	for (var i = 0; i < n; i++) {
		feature = features[i];
		feature.setSymbol(sym);
		map.graphics.add(feature);
	}
	map.centerAt(feature.geometry.getExtent().getCenter());
	showWaitFlag(false);
}
// 地图数据范围
var mapDataExtent = null;
// 显示行驶方向
function Azimuth(bl) {
	// 显示行驶方向
	if (bl) {
		if (DirectionGraphicsLayer == undefined
			|| DirectionGraphicsLayer == null) {
			getDirectionRequestParam();
		} else {
			DirectionGraphicsLayer.setVisibility(true);
		}
	}
	// 隐藏行驶方向
	else {
		if (DirectionGraphicsLayer == undefined
			|| DirectionGraphicsLayer == null) {
			return;
		} else {
			DirectionGraphicsLayer.setVisibility(false);
		}
	}
}

var compareLogTrackLayer = null;
function TrackOffset(res, bl) {
	if (null == compareLogTrackLayer || undefined == compareLogTrackLayer) {
		compareLogTrackLayer = new esri.layers.GraphicsLayer();
		map.addLayer(compareLogTrackLayer);
	} else {
		compareLogTrackLayer.clear();
	}
	if (bl) {
		var datas = res[0];
		var color = res[1].compareTestLogColor;
		setCompareLegend(res[1]);
		for (var i = 0, n = datas.length; i < n; i++) {
			var data = datas[i];
			var d = compress(data, 1);
			showLinesByCompareDataOf(d, color);
		}
	} else {
		var datas = res[0];
		var color = res[1].compareTestLogColor;
		setCompareLegend(res[1]);
		for (var i = 0, n = datas.length; i < n; i++) {
			var data = datas[i];
			var d = compress(data, 1);
			showLinesByCompareDataOr(d, color);
		}
	}
	return;
}

function setCompareLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color = Rendercolor.color;
	row += "<tr><td style='width: 80px;background-color:" + color
		+ "'></td><td>";
	var td = Rendercolor.name;
	row += td + "</td></tr>";
	var color1 = Rendercolor.compareTestLogColor;
	row += "<tr><td style='width: 80px;background-color:" + color1
		+ "'></td><td>";
	var td1 = Rendercolor.compareName;
	row += td1 + "</td></tr>";
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}
//TAC分布	
function DisplayTAC(bl) {
	// 显示TAC
	var n = CellLayers.length;
	if (bl) {
		if (n > 0) {
			for (var i = 0; i < n; i++) {
				map.removeLayer(CellLayers[i]);
				var defaultSym = new esri.symbol.SimpleFillSymbol("solid",
					null, new dojo.Color([ 255, 0, 255, 0.75 ]));
				var renderer = new esri.renderer.ClassBreaksRenderer(
					defaultSym, "MOD_TAC");
				renderer.addBreak(0, 0.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[0]));
				renderer.addBreak(0.9, 1.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[1]));
				renderer.addBreak(1.9, 2.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[2]));
				renderer.addBreak(2.9, 3.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[3]));
				renderer.addBreak(3.9, 4.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[4]));
				renderer.addBreak(4.9, 5.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[5]));
				renderer.addBreak(5.9, 6.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[6]));
				renderer.addBreak(6.9, 7.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[7]));
				renderer.addBreak(7.9, 8.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[8]));
				renderer.addBreak(8.9, 9.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, TacColor[9]));
				CellLayers[i].setRenderer(renderer);
				map.addLayer(CellLayers[i]);
			}
		} else {
			return;
		}
	}
	// 隐藏TAC
	else {
		if (n > 0) {
			for (var i = 0; i < n; i++) {
				map.removeLayer(CellLayers[i]);
				var linesym = new esri.symbol.SimpleLineSymbol("solid",
					new esri.Color([ 0, 0, 0 ]), 0.4);
				var renderer = new esri.renderer.SimpleRenderer(
					new esri.symbol.SimpleFillSymbol("solid", linesym,
						new esri.Color([ 0, 128, 0, 0.75 ])));
				CellLayers[i].setRenderer(renderer);
				map.addLayer(CellLayers[i]);
			}
		} else {
			return;
		}
	}
}
// 显示/隐藏模3核查
function DisplayPCI(bl) {
	// 显示模3核查
	var n = CellLayers.length;
	if (bl) {
		if (n > 0) {
			for (var i = 0; i < n; i++) {
				map.removeLayer(CellLayers[i]);
				var defaultSym = new esri.symbol.SimpleFillSymbol("solid",
					null, new dojo.Color([ 255, 0, 255, 0.75 ]));
				var renderer = new esri.renderer.ClassBreaksRenderer(
					defaultSym, "MODEL3");
				renderer
					.addBreak(0, 0.9, new esri.symbol.SimpleFillSymbol(
						"solid", null, document
							.getElementById('Color0').value));
				renderer
					.addBreak(0.9, 1.9, new esri.symbol.SimpleFillSymbol(
						"solid", null, document
							.getElementById('Color1').value));
				renderer
					.addBreak(1.9, 2.9, new esri.symbol.SimpleFillSymbol(
						"solid", null, document
							.getElementById('Color2').value));
				CellLayers[i].setRenderer(renderer);
				map.addLayer(CellLayers[i]);
			}
		} else {
			return;
		}
	}
	// 隐藏模3核查
	else {
		if (n > 0) {
			for (var i = 0; i < n; i++) {
				map.removeLayer(CellLayers[i]);
				var linesym = new esri.symbol.SimpleLineSymbol("solid",
					new esri.Color([ 0, 0, 0 ]), 0.4);
				var renderer = new esri.renderer.SimpleRenderer(
					new esri.symbol.SimpleFillSymbol("solid", linesym,
						new esri.Color([ 0, 128, 0, 0.75 ])));
				CellLayers[i].setRenderer(renderer);
				map.addLayer(CellLayers[i]);
			}
		} else {
			return;
		}
	}
}
// 显示/隐藏小区点连线
function DisplayCleLPoint(bl) {
	// 显示小区点连线
	if (bl) {
		if (CellLineConGraphicsLayer == undefined
			|| CellLineConGraphicsLayer == null) {
			CellLineConjunction();
		} else {
			CellLineConGraphicsLayer.setVisibility(true);
		}
	}
	// 隐藏小区点连线
	else {
		if (CellLineConGraphicsLayer == undefined
			|| CellLineConGraphicsLayer == null) {
			return;
		} else {
			CellLineConGraphicsLayer.setVisibility(false);
		}
	}
}

//yzp 2019-03-28
//显示/隐藏小区点连线
function DisplayCellPoint(bl) {
	// 显示小区点连线
	if (bl) {
		if (CellLineConGraphicsLayer == undefined
			|| CellLineConGraphicsLayer == null || CellLineConGraphicsLayer.graphics.length == 0) {
			CellLineConjunction();
		}
		CellLineConGraphicsLayer.setVisibility(true);

	}
	// 隐藏小区点连线
	else {
		if (CellLineConGraphicsLayer == undefined
			|| CellLineConGraphicsLayer == null) {
			return;
		} else {
			CellLineConGraphicsLayer.clear();
			CellLineConGraphicsLayer.setVisibility(false);
			CellLineConGraphicsLayer = null;
		}
	}
}
//

// 显示/隐藏小区事件连线
function DisplayCleLEvent(bl) {
	// 显示小区事件连线
	if (bl) {
		if (CellLineIconGraphicsLayer == undefined
			|| CellLineIconGraphicsLayer == null) {
			CellLineIconjunction();
		} else {
			CellLineIconGraphicsLayer.setVisibility(true);
		}
	}
	// 隐藏小区事件连线
	else {
		if (CellLineIconGraphicsLayer == undefined
			|| CellLineIconGraphicsLayer == null) {
			return;
		} else {
			CellLineIconGraphicsLayer.setVisibility(false);
		}
	}
}
// 显示/隐藏邻区连线
function DisplayCleLArea(bl) {
	// 显示邻区连线
	if (bl) {
		if (CellToCellGraphicsLayer == undefined
			|| CellToCellGraphicsLayer == null) {
			CellToCellconection();
		} else {
			CellToCellGraphicsLayer.setVisibility(bl);
		}
	}
	// 隐藏邻区连线
	else {
		if (CellToCellGraphicsLayer == undefined
			|| CellToCellGraphicsLayer == null) {
			return;
		} else {
			CellToCellGraphicsLayer.setVisibility(bl);
		}
	}
}
function ShowOrHideDiv(id) {
	var $a1 = $("#" + id);
	if ($a1.is(":hidden")) {
		$a1.css("display", "block");
	} else {
		$a1.css("display", "none");
	}
}
function Locate() {
	LocateByXY(dojo.byId("txt_lon").value, dojo.byId("txt_lat").value);
	myFormDialog.hide();
}
function LocateByXY(x, y) {
	var pt = esri.geometry.Point(x, y);
	var symbol = new esri.symbol.PictureMarkerSymbol({
		"url" : "images/position.png",
		"height" : 16,
		"width" : 16,
		"xoffset" : 20,
		"yoffset" : 20,
		"type" : "esriPMS"
	});
	map.setZoom(LodsLength - 2);
	map.centerAt(pt);
	var GPSGraphic = new esri.Graphic(pt, symbol, null, null);
	map.graphics.add(GPSGraphic);
}
// 处理 隐藏操作,及改变隐藏操作的图片
function hideMapToolTip(hideDivId, cltDiv) {
	var $a = $("#" + hideDivId);
	var ima = document.getElementById(cltDiv);
	if ($a.is(":hidden")) {
		$a.css("display", "block");
		ima.src = "images/out.png";
	} else {
		$a.css("display", "none");
		ima.src = "images/in.png";
	}
}

//改变隐藏操作的图片
function displayMapToolTip(hideDivId, cltDiv) {
	var $a = $("#" + hideDivId);
	var ima = document.getElementById(cltDiv);
	if ($a.is(":hidden")) {
		$a.css("display", "block");
		ima.src = "images/out.png";
	}
}

// 显示div控制
function showDivByDivId(id) {
	document.getElementById(id).style.display = "block";
}
function hideDivByDivId(id) {
	if (document.getElementById(id) == null) {
		return;
	}
	document.getElementById(id).style.display = "none";
}
// 鼠标移动DIV操作
function MoveDiv(obj) {
	var div_container = $(obj).parent().first().attr('id');
	var div_title = obj.id;
	var _x,
		_y;
	var $ab = $("#" + div_title),
		$parentAb = $("#" + div_container);
	var _move = false;
	$ab.mousedown(function(e) {
		this.style.cursor = 'move';
		_move = true;
		_x = e.pageX - parseInt($parentAb.css("left"));
		_y = e.pageY - parseInt($parentAb.css("top"));
	});
	$(document).mousemove(function(e) {
		if (_move) {
			var x = e.pageX - _x; // 移动时根据鼠标位置计算控件左上角的绝对位置
			var y = e.pageY - _y;
			$parentAb.css({
				top : y,
				left : x
			}); // 控件新位置
		}
	});
	$ab.mouseup(function() {
		this.style.cursor = 'default';
		_move = false;
	});
}
function showWaitFlag(bl) {
	if (bl) {
		$("#BackgroundDiv").css("display", "block");
		$("#waitImg").css("display", "block");
	} else {
		$("#BackgroundDiv").css("display", "none");
		$("#waitImg").css("display", "none");
	}
}
// 显示小区图层
var CellLayers = [];
var LabelCellLayers = [];
function DisplayLabelLayer(obj) {
	var TotalId = obj.id;
	var id = TotalId.split("_")[1];
	var check = document.getElementById(TotalId);
	switch (id) {
	case 'EventLayer':
		EventTrackLabelLayer.setVisibility(check.checked);
		break;
	case 'channelEventLayer':
		ChannelEventLabelLayer.setVisibility(check.checked);
		break;
	case 'BuildingsLayer':
		BuildingsLabelLayer.setVisibility(check.checked);
		break;
	case 'CellsLayer':
		var n = CellLayers.length,
			i = 0;
		if (n > 0) {
			for (; i < n; i++) {
				CellLayers[i].setShowLabels(check.checked);
			}
		}
		/*		var n = LabelCellLayers.length;
		 if (n > 0) {
		 for (var i = 0; i < n; i++) {
		 LabelCellLayers[i].setVisibility(check.checked);
		 }
		 } else {
		 for (var i = 0; i < CellLayers.length; i++) {
		 var sym = new esri.symbol.TextSymbol();
		 sym.font.setSize("7pt");
		 sym.setColor(new dojo.Color("#666"));
		 var renderer = new esri.renderer.SimpleRenderer(sym);
		 var LabelCellLayer = new esri.layers.LabelLayer({
		 id : "labels"
		 });
		 LabelCellLayer.addFeatureLayer(CellLayers[i], renderer,
		 "{CELLNAME}");
		 map.addLayer(LabelCellLayer);
		 LabelCellLayers.push(LabelCellLayer);
		 }
		 }*/
		break;
	case 'TrackLayer':
		GPSTrackLabelGraphicsLayer.setVisibility(check.checked);
		break;
	}

}
// 控制小区显示
function DisplayCellLayer(obj) {
	var TotalId = obj.id;
	var id = TotalId.split("_")[1];
	var check = document.getElementById(TotalId);
	//document.getElementById('label_' + id).disabled = !check.checked;
	// document.getElementById('layerLabels').disabled = !check.checked;
	switch (id) {
	case 'EventLayer':
		document.getElementById('label_' + id).disabled = !check.checked;
		EventTrackGraphicsLayer.setVisibility(check.checked);
		break;
	case 'channelEventLayer':
		document.getElementById('label_' + id).disabled = !check.checked;
		ChannelEventGraphicsLayer.setVisibility(check.checked);
		if (document.getElementById('label_' + id).disabled) {
			document.getElementById('label_' + id).checked = false;
			ChannelEventLabelLayer.setVisibility(false);
		}
		break;
	case 'BuildingsLayer':
		document.getElementById('label_' + id).disabled = !check.checked;
		BuildingsGraphicsLayer.setVisibility(check.checked);
		if (document.getElementById('label_' + id).disabled) {
			document.getElementById('label_' + id).checked = false;
			BuildingsLabelLayer.setVisibility(false);
		}
		break;
	case 'CellsLayer':
		document.getElementById('label_' + id).disabled = !check.checked;
		var n = CellLayers.length;
		if (n > 0) {
			for (var i = 0; i < n; i++) {
				CellLayers[i].setVisibility(check.checked);
			}
		}
		break;
	case 'TrackLayer':
		var indexType = $("#selectIndex").find("option:selected").val();
		if (indexType == 0) {
			document.getElementById('label_' + id).disabled = !check.checked;
		} else
			document.getElementById('label_' + id).disabled = true;
		GPSTrackGraphicsLayer.setVisibility(check.checked);
		break;
	}
}
//图层控制内容动态添加
function InsertTable(id, sName, cname) {
	var bodyObj = document.getElementById(id);
	// var rowObj = bodyObj.insertRow(bodyObj.rows.length);
	var rowObj = bodyObj.insertRow(0);
	var cell1 = rowObj.insertCell(0);
	var cell2 = rowObj.insertCell(1);
	var cell3 = rowObj.insertCell(2);
	var cell4 = rowObj.insertCell(3);
	cell1.innerHTML = "<input type='radio' id='radio_" + sName
		+ "' name='radio_tr'>";
	cell2.innerHTML = "<input type='checkbox' checked='checked' id='layer_"
		+ sName + "' onchange='DisplayCellLayer(this)'>";
	cell3.innerHTML = "<input type='checkbox' class='check_label' id='label_"
		+ sName + "'  onClick='DisplayLabelLayer(this)'>";
	cell4.innerHTML = cname;
}

/*
 * 地图中图层重排序
 */
function ReorderGraphicsLayer() {
	var layers = [];
	$('input:radio[name="radio_tr"]').each(function() {
		switch (this.id) {
		case 'radio_lte':
			layers.push(ltecellLayer);
			break;
		case 'radio_td':
			layers.push(tdcellLayer);
			break;
		case 'radio_gsm':
			layers.push(gsmcellLayer);
			break;
		case 'radio_rru':
			layers.push(rrucellLayer);
			break;
		case 'radio_MRpoint':
			layers.push(MRpointGraphicsLayer);
			break;
		case 'radio_event':
			layers.push(clusterLayer);
			break;
		case 'radio_problem':
			layers.push(ProblemRoadGraphicsLayer);
			break;
		}
	});
	setTimeout(function() {
		ReloadGraphicsLayer(layers.reverse());
	}, 800);
}
// 删除图层后，重新加载地图图层
function ReloadGraphicsLayer(layers) {
	for (var i = layers.length - 1; i > -1; i--) {
		map.removeLayer(layers[i]);
	}
	setTimeout(function() {
		map.addLayers(layers);
	}, 100);
}
/*
 * 图层控制移动-上移-下移-置底-置顶
 */
function moveUpRows() {
	var id = $('input:radio[name="radio_tr"]:checked');
	var sName = id.attr('id');
	if (sName == null || sName == undefined) {
		alert("必须先选中一行");
	} else {
		var cc = id.closest('tr');
		var pres = cc.prevAll('tr');
		if (pres.length < 1) {
			alert("已经置顶，不能再向上移动");
		} else {
			var pre = pres[0];
			var tmp = cc.clone(true);
			cc.remove();
			$(pre).before(tmp);
			setTimeout(ReorderGraphicsLayer, 200);
		}
	}
}

function moveDownRows() {
	var id = $('input:radio[name="radio_tr"]:checked');
	if (id.attr('id') == null || id.attr('id') == undefined) {
		alert("必须先选中一行");
	} else {
		var cc = id.closest('tr');
		var nexts = cc.nextAll('tr');
		if (nexts.length < 1) {
			alert("已经置底，不能再向下移动");
		} else {
			var next = nexts[0];
			var tmp = cc.clone(true);
			cc.remove();
			$(next).after(tmp);
			setTimeout(ReorderGraphicsLayer, 200);
		}
	}
}
function moveBottom() {
	var id = $('input:radio[name="radio_tr"]:checked');
	if (id.attr('id') == null || id.attr('id') == undefined) {
		alert("必须先选中一行");
	} else {
		var cc = id.closest('tr');
		var nexts = cc.nextAll('tr');
		if (nexts.length < 1) {
			alert("已经置底，不能再向下移动");
		} else {
			var next = nexts[nexts.length - 1];
			var tmp = cc.clone(true);
			$(next).after(tmp);
			cc.remove();
			setTimeout(ReorderGraphicsLayer, 200);
		}
	}
}
function moveTop() {
	var id = $('input:radio[name="radio_tr"]:checked');
	if (id.attr('id') == null || id.attr('id') == undefined) {
		alert("必须先选中一行");
	} else {
		var cc = id.closest('tr');
		var pres = cc.prevAll('tr');
		var n = pres.length;
		if (n < 1) {
			alert("已经置顶，不能再向上移动");
		} else {
			var pre = pres[n - 1];
			var tmp = cc.clone(true);
			$(pre).before(tmp);
			cc.remove();
			setTimeout(ReorderGraphicsLayer, 200);
		}
	}
}

// 创建小区图层
function CreateCellLayer1() {
	showWaitFlag(true);
	var Radsize = "d=50";
	var soeUrl = yewu_url + "/exts/myRestSOE/sampleOperation";
	var sqltype = "oracle";
	var content = {
		'sqlcom' : sqlcom,
		'connectionstring' : connectionstring,
		'path' : WorkspaceIDpath,
		'filename' : "cell1",
		'sqltype' : sqltype,
		'Radsize' : Radsize,
		'f' : "json"
	};
	var Request = esri.request({
		url : soeUrl,
		timeout : 40000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		showWaitFlag(false);
		if (responses.success == "成功") {
			alert("小区创建成功");
			AddCellToMap("cell1.shp", true);
		} else {
			alert(responses);
		}
	});
}
// 加载小区
var CellfeatureLayer;

//要渲染的小区名集合
var cellids = null;

function AddCellToMap(kpi, bl, model) {
	var dataSource = new esri.layers.TableDataSource();
	dataSource.workspaceId = WorkspaceID;
	dataSource.dataSourceName = kpi;
	var layerSource = new esri.layers.LayerDataSource();
	layerSource.dataSource = dataSource;
	// var infoTemplate=new esri.InfoTemplate("详情","${*}");
	var arrs = null;
	if (model == "nr") {
		arrs = [ 'REGION', 'AMFID', 'GNODEBID', 'SITENAME',
			'CELLNAME', 'CELLID', 'LOCALCELLI', 'TAC', 'PCI', 'FREQ1',
			'BANDWIDTH1', 'LONGITUDE', 'LATITUDE',
			'SECTORTYPE', 'DOORTYPE', 'TILTTOTAL', 'TILTM', 'TILTE', 'AZIMUTH', 'HEIGHT' ];
	} else {
		arrs = [ 'REGION', 'MMEGROUPID', 'MMEID', 'ENODEBID', 'SITENAME',
			'CELLNAME', 'CELLID', 'LOCALCELLI', 'TAC', 'PCI', 'FREQ1',
			'BANDWIDTH1', 'LONGITUDE', 'LATITUDE',
			'SECTORTYPE', 'DOORTYPE', 'TILTTOTAL', 'TILTM', 'TILTE', 'AZIMUTH', 'HEIGHT' ];
	}
	var cc = SetWindowContent(arrs);
	var infoTemplate = new esri.InfoTemplate("详情", cc);
	// create a new feature layer based on the table data source
	CellfeatureLayer = new esri.layers.FeatureLayer(yewu_url
		+ "/dynamicLayer", {
			mode : esri.layers.FeatureLayer.MODE_ONDEMAND,
			outFields : [ "*" ],
			infoTemplate : infoTemplate,
			source : layerSource,
			opacity : 0.5
		});

	if (bl) {
		CellfeatureLayer.on("load", function(evt) {
			// project the extent if the map's spatial reference is different
			// that the layer's extent.
			var gs = new esri.tasks.GeometryService(geometry_url);
			var extent = evt.layer.fullExtent;
			mapDataExtent = extent;
			if (extent.spatialReference.wkid === map.spatialReference.wkid) {
				map.setExtent(extent);
			} else {
				gs.project([ extent ], map.spatialReference).then(
					function(results) {
						map.setExtent(results[0]);
					});
			}
		});
	}
	var linesym = new esri.symbol.SimpleLineSymbol("solid", new esri.Color([ 0,
		0, 0 ]), 0.4);
	var renderer = new esri.renderer.SimpleRenderer(
		new esri.symbol.SimpleFillSymbol("solid", linesym, new esri.Color([
			0, 128, 0, 0.75 ])));
	CellfeatureLayer.setRenderer(renderer);
	CellfeatureLayer.setScaleRange(32000, 0);
	setCellLayers(CellfeatureLayer);
	var sym = new esri.symbol.TextSymbol();
	var pointColor = "#000000";
	sym.setColor(new dojo.Color(pointColor));
	sym.font.setSize("11pt");
	sym.font.setFamily("arial");
	var jso = {
		"labelExpressionInfo" : {
			"value" : "{CELLNAME}"
		},
		"labelPlacement" : "always-horizontal"
	}
	var LabelCell = new esri.layers.LabelClass(jso);
	LabelCell.symbol = sym;
	CellfeatureLayer.setLabelingInfo([ LabelCell ]);
	CellfeatureLayer.setShowLabels(false);
	map.addLayer(CellfeatureLayer);
	map.infoWindow.resize(300, 300);
	cellBorderToColor(cellids);
}


function cellBorderToColor(cellIds) {
	if (cellIds != null) {
		var n = CellLayers.length;
		if (n > 0) {
			for (var i = 0; i < n; i++) {
				map.removeLayer(CellLayers[i]);
				var defaultSym = new esri.symbol.SimpleFillSymbol("solid", null, new dojo.Color([ 0, 128, 0, 0.75 ]));
				var renderer = new esri.renderer.ClassBreaksRenderer(defaultSym, "CELLID");
				var cellidArry = cellIds.split(",");
				for (var j = 0; j < cellidArry.length; j++) {
					var linesym = new esri.symbol.SimpleLineSymbol("solid", new esri.Color([ 0,
						0, 0 ]), 2);
					renderer.addBreak(parseFloat(cellidArry[j]) - 0.1, parseFloat(cellidArry[j]) + 0.1,
						new esri.symbol.SimpleFillSymbol("solid", linesym, new dojo.Color([ 0, 128, 0, 0.75 ])));
				}
				CellLayers[i].setRenderer(renderer);
				map.addLayer(CellLayers[i]);
			}
		} else {
			return;
		}
	}
}

var TerminalColor = [ "#FA781E", "#72A98B" ];
//yinzhipeng 1.5.2 merge
//var callEventLabel = new Array("VoLTE语音主叫尝试", "VoLTE语音主叫接通", "VoLTE语音呼叫未接通",
//		"VoLTE语音主叫掉话", "VoLTE语音主叫尝试", "VoLTE语音主叫接通", "VoLTE语音被叫掉话",
//		"Attach 尝试", "Attach 尝试", "Attach失败");
var channelEventLabel = new Array(
	"Http下载发起", "Http下载失败", "Http下载成功", "Http下载掉线",
	"流媒体播放发起", "流媒体播放成功", "流媒体播放失败", "流媒体真正开始播放", "流媒体播放掉线",
	"VP主叫发起", "VP主叫振铃", "VP主叫失败", "VP主叫建立", "VP主叫接通", "VP主叫完成", "VP主叫掉话",
	"VP被叫发起", "VP被叫振铃", "VP被叫失败", "VP被叫建立", "VP被叫接通", "VP被叫成功", "VP被叫掉话",
	"FTP登陆服务器成功", "FTP登陆服务器失败", "FTP下载发起", "FTP下载失败", "FTP下载成功",
	"FTP上传发起", "FTP上传失败", "FTP上传成功", "FTP下载掉线", "FTP上传掉线",
	"建立与网络侧连接请求", "建立与网络侧连接成功", "建立与网络侧连接失败", "断开与网络侧连接",
	"Http浏览发起", "Http浏览失败", "Http浏览成功", "Http浏览掉线",
	"VOLTE主叫试呼", "VOLTE主叫振铃", "VOLTE主叫接通", "VOLTE主叫未接通", "VOLTE主叫挂机", "VOLTE主叫掉话",
	"VOLTE被叫试呼", "VOLTE被叫振铃", "VOLTE被叫接通", "VOLTE被叫挂机", "VOLTE被叫掉话",
	"ESRVCC切换尝试", "ESRVCC切换成功", "ESRVCC切换失败",
	"SIP注册请求", "SIP注册成功", "SIP注册失败",
	"回落请求", "回落成功", "回落失败",
	//yinzhipeng 1.5.2 merge
	"UDP下行灌包发起", "UDP下行灌包成功", "UDP下行灌包失败", "UDP下行灌包掉线", "UDP上行灌包失败", "UDP上行灌包成功", "UDP上行灌包失败",
	"Ping发起", "Ping失败", "Ping成功",
	"VP主叫发起", "VP主叫振铃", "VP主叫失败", "VP主叫建立", "VP主叫接通", "VP主叫完成", "VP主叫掉话",
	"VP被叫发起", "VP被叫振铃", "VP被叫失败", "VP被叫建立", "VP被叫接通", "VP被叫成功", "VP被叫掉话"
);
// 获取事件图标的标注
function EventLabel(type) {
	var t = type;
	var iconlabel = "";
	switch (t) {
	case 0:
		iconlabel = "VoLTE语音主叫尝试";
		break;
	case 1:
		iconlabel = "VoLTE语音主叫接通";
		break;
	case 2:
		iconlabel = "VoLTE语音呼叫未接通";
		break;
	case 3:
		iconlabel = "VoLTE语音主叫掉话";
		break;
	case 4:
		iconlabel = "VoLTE语音被叫尝试";
		break;
	case 5:
		iconlabel = "VoLTE语音被叫接通";
		break;
	case 6:
		iconlabel = "VoLTE语音被叫掉话";
		break;
	case 7:
		iconlabel = "Attach尝试";
		break;
	case 8:
		iconlabel = "Attach失败";
		break;
	case 9:
		iconlabel = "Attach成功";
		break;
	case 10:
		iconlabel = "IMS注册尝试";
		break;
	case 11:
		iconlabel = "IMS注册失败";
		break;
	case 12:
		iconlabel = "IMS注册成功";
		break;
	case 13:
		iconlabel = "CSFB尝试";
		break;
	case 14:
		iconlabel = "CSFB失败";
		break;
	case 15:
		iconlabel = "系统间切换尝试";
		break;
	case 16:
		iconlabel = "系统间切换成功";
		break;
	case 17:
		iconlabel = "系统间切换失败";
		break;
	case 18:
		iconlabel = "LTE系统内切换尝试";
		break;
	case 19:
		iconlabel = "LTE系统内切换成功";
		break;
	case 20:
		iconlabel = "LTE系统内切换失败";
		break;
	case 21:
		iconlabel = "VoLTE视频主叫尝试";
		break;
	case 22:
		iconlabel = "VoLTE视频主叫接通";
		break;
	case 23:
		iconlabel = "VoLTE视频主叫失败";
		break;
	case 24:
		iconlabel = "VoLTE视频主叫掉话";
		break;
	case 25:
		iconlabel = "VoLTE视频被叫尝试";
		break;
	case 26:
		iconlabel = "VoLTE视频被叫接通";
		break;
	case 27:
		iconlabel = "VoLTE视频被叫失败";
		break;
	case 28:
		iconlabel = "VoLTE视频被叫掉话";
		break;
	case 29:
		iconlabel = "VoLTE语音主叫振铃";
		break;
	case 52:
		iconlabel = "VoLTE语音被叫振铃";
		break;
	//yinzhipeng 1.5.2 merge
	//case 53:
	//	iconlabel = "Handover Start";
	//	break;
	//case 54:
	//	iconlabel = "Handover Success";
	//	break;
	//case 55:
	//	iconlabel = "Handover Failure";
	//	break;
	}
	return iconlabel;
}
var channelEventtpye = 0;
function cleanMap(num) {
	if (timer != null || timer != undefined) {
		clearTimeout(timer);
	}
	if (timer1 != null || timer1 != undefined) {
		clearTimeout(timer1);
	}
	if (timer2 != null || timer2 != undefined) {
		clearTimeout(timer2);
	}
	hideDivByDivId('eventControl');
	hideDivByDivId('layerControl');
	hideDivByDivId('MapResource');
	for (var j = 0; j < map.graphicsLayerIds.length; j++) {
		var glayer = map.getLayer(map.graphicsLayerIds[j]);
		glayer.clear();
	}
	channelEventtpye = num;
}

function drawChannelEvent() {
	if (channelEventtpye == 0) {
		var res = parent.getGPSAtmomentEventRequestParam();
		if (res.boxIDs == null || res.boxIDs == undefined) {
			return;
		}
		var arrChs = new Array();
		var checkeds = $(':checkbox[name=ch_list]:checked');
		$(checkeds).each(function() {
			arrChs.push(this.value);
		});
		if (arrChs.length == 0) {
			alert("选择至少一个通道");
			return;
		}
		var galleryIDs = arrChs.toString();
		var content = {
			galleryIDs : galleryIDs,
			boxIDs : res.boxIDs,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				displayChannelEvent(responses);
			}
		});
	}
	if (channelEventtpye == 1) {
		var res = parent.getGPSEventRequestParam();
		if (res.boxIDs == null || res.boxIDs == undefined) {
			return;
		}
		var arrChs = new Array();
		var checkeds = $(':checkbox[name=ch_list]:checked');
		$(checkeds).each(function() {
			arrChs.push(this.value);
		});
		if (arrChs.length == 0) {
			alert("选择至少一个通道");
			return;
		}
		var galleryIDs = arrChs.toString();
		var content = {
			galleryIDs : galleryIDs,
			boxIDs : res.boxIDs,
			time : TerminalHistoryTime,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				displayChannelEvent(responses);
			}
		});
	}

}
var timer = null;
var timer1 = null;
function updateTerminalTrack(obj) {
	var res = parent.getGPSRefreshRequestParam();
	if (res.boxIDs == null || res.boxIDs == undefined) {
		return;
	}
	var content = {
		time : obj[0].time,
		boxIDs : res.boxIDs,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 4000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		if (responses != null) {
			if (null != responses[1] && 0 != responses[1].length) {
				refreshTerminalTrack(responses);
				timer = setTimeout(function() {
					updateTerminalTrack(responses);
				}, 3000);
			} else {
				timer1 = setTimeout(function() {
					updateTerminalTrack(responses);
				}, 10000);
			}

		}
	});
}

function refreshTerminalTrack(res) {
	if (TerminalCurrentGraphicsLayer != undefined
		|| TerminalCurrentGraphicsLayer != null) {
		TerminalCurrentGraphicsLayer.clear();
	}
	if (TerminalCurrentLabelLayer != undefined
		|| TerminalCurrentLabelLayer != null) {
		TerminalCurrentLabelLayer.clear();
	}
	var data = res[1];
	if (data.length == 0) {
		return;
	}
	var LineExtent = new esri.geometry.Polyline(wgs);
	for (var i = 0; i < data.length; i++) {
		var d = data[i][1];
		var label = data[i][0];
		var lastpt = new esri.geometry.Point(d[0].longitude, d[0].latitude, wgs);
		var textsym = new esri.symbol.TextSymbol(label, null, new dojo.Color([
			0, 0, 230, 1 ]));
		textsym.setOffset(12, -15);
		var font = new esri.symbol.Font();
		font.setSize("12px");
		textsym.setFont(font);
		textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
		textsym.setHaloSize(1);
		var labelGraphic = new esri.Graphic(lastpt, textsym);
		var pointSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 9,
			new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID,
				new dojo.Color([ 23, 145, 255, 0.6 ]), 5),
			new dojo.Color([ 23, 145, 255, 1.0 ]));
		var pointGraphic = new esri.Graphic(lastpt, pointSymbol);
		TerminalCurrentLabelLayer.add(labelGraphic);
		TerminalCurrentGraphicsLayer.add(pointGraphic);
		var HistoryLine = new esri.geometry.Polyline(wgs);
		var GpsPoints = [];
		for (var j = 0, n = d.length; j < n; j++) {
			var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude, wgs);
			GpsPoints.push(pt);
		}
		HistoryLine.addPath(GpsPoints);
		LineExtent.addPath(GpsPoints);
		var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, TerminalColor[i], 2);
		var GpsHistoryGraphic = new esri.Graphic(HistoryLine, GpsLineSymbol,
			null, null);
		TerminalTrackGraphicsLayer.add(GpsHistoryGraphic);

	}
}

var TerminalTrackGraphicsLayer = null;
var TerminalCurrentGraphicsLayer = null;
var TerminalCurrentLabelLayer = null;
function showTerminalTrack(res) {
	if (timer != null || timer != undefined) {
		clearTimeout(timer);
	}
	if (timer1 != null || timer1 != undefined) {
		clearTimeout(timer1);
	}
	if (undefined == TerminalTrackGraphicsLayer
		|| null == TerminalTrackGraphicsLayer) {
		TerminalTrackGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(TerminalTrackGraphicsLayer);
	} else {
		TerminalTrackGraphicsLayer.clear();
	}
	if (undefined == TerminalCurrentGraphicsLayer
		|| null == TerminalCurrentGraphicsLayer) {
		TerminalCurrentGraphicsLayer = new esri.layers.GraphicsLayer({
			id : "CurrentPointlayer"
		});
		map.addLayer(TerminalCurrentGraphicsLayer);
	} else {
		TerminalCurrentGraphicsLayer.clear();
	}
	if (undefined == TerminalCurrentLabelLayer
		|| null == TerminalCurrentLabelLayer) {
		TerminalCurrentLabelLayer = new esri.layers.GraphicsLayer();
		map.addLayer(TerminalCurrentLabelLayer);
	} else {
		TerminalCurrentLabelLayer.clear();
	}
	var data = res[1];
	if (data.length == 0) {
		return;
	}
	var LineExtent = new esri.geometry.Polyline(wgs);
	var LinePoints = [];
	for (var i = 0; i < data.length; i++) {
		var ds = data[i][1];
		var label = data[i][0];
		for (var m = 0; m < ds.length; m++) {
			var d = ds[m];
			var HistoryLine = new esri.geometry.Polyline(wgs);
			var GpsPoints = [];
			for (var j = 0, n = d.length; j < n; j++) {
				var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude,
					wgs);
				GpsPoints.push(pt);
				LinePoints.push(pt);
			}
			HistoryLine.addPath(GpsPoints);
			var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID, TerminalColor[i],
				2);
			var GpsHistoryGraphic = new esri.Graphic(HistoryLine,
				GpsLineSymbol, null, null);
			TerminalTrackGraphicsLayer.add(GpsHistoryGraphic);
		}
		var lastpt = new esri.geometry.Point(ds[0][0].longitude,
			ds[0][0].latitude, wgs);
		var textsym = new esri.symbol.TextSymbol(label, null, new dojo.Color([
			0, 0, 230, 1 ]));
		textsym.setOffset(12, -15);
		var font = new esri.symbol.Font();
		font.setSize("12px");
		textsym.setFont(font);
		textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
		textsym.setHaloSize(1);
		var labelGraphic = new esri.Graphic(lastpt, textsym);
		var pointSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 9,
			new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID,
				new dojo.Color([ 23, 145, 255, 0.6 ]), 5),
			new dojo.Color([ 23, 145, 255, 1.0 ]));
		var pointGraphic = new esri.Graphic(lastpt, pointSymbol);
		TerminalCurrentLabelLayer.add(labelGraphic);
		TerminalCurrentGraphicsLayer.add(pointGraphic);
	}
	LineExtent.addPath(LinePoints);
	mapDataExtent = LineExtent.getExtent();
	map.setExtent(mapDataExtent);
}

function testpoint() {
	if (undefined == TerminalCurrentGraphicsLayer
		|| null == TerminalCurrentGraphicsLayer) {
		TerminalCurrentGraphicsLayer = new esri.layers.GraphicsLayer({
			id : "CurrentPointlayer"
		});
		map.addLayer(TerminalCurrentGraphicsLayer);
	} else {
		TerminalCurrentGraphicsLayer.clear();
	}
	if (undefined == TerminalCurrentLabelLayer
		|| null == TerminalCurrentLabelLayer) {
		TerminalCurrentLabelLayer = new esri.layers.GraphicsLayer();
		map.addLayer(TerminalCurrentLabelLayer);
	} else {
		TerminalCurrentLabelLayer.clear();
	}

	for (var i = 1; i < 960; i++) {
		var lat = 31.4586 + Math.random() * (380 - i) * 0.0002;
		var lon = 104.6688 + Math.random() * (i - 480) * 0.0002;
		var label = "0810000" + i.toString();
		var lastpt = new esri.geometry.Point(lon, lat, wgs);
		var textsym = new esri.symbol.TextSymbol(label, null, new dojo.Color([
			0, 0, 230, 1 ]));
		textsym.setOffset(12, -15);
		var font = new esri.symbol.Font();
		font.setSize("12px");
		textsym.setFont(font);
		textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
		textsym.setHaloSize(1);
		var labelGraphic = new esri.Graphic(lastpt, textsym);
		var pointSymbol = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 9,
			new esri.symbol.SimpleLineSymbol(
				esri.symbol.SimpleLineSymbol.STYLE_SOLID,
				new dojo.Color([ 23, 145, 255, 0.6 ]), 5),
			new dojo.Color([ 23, 145, 255, 1.0 ]));
		var pointGraphic = new esri.Graphic(lastpt, pointSymbol);
		TerminalCurrentLabelLayer.add(labelGraphic);
		TerminalCurrentGraphicsLayer.add(pointGraphic);
	}
}

var TerminalHistoryGraphicsLayer;
function showTerminalHistoryTrack(res) {
	if (undefined == TerminalHistoryGraphicsLayer
		|| null == TerminalHistoryGraphicsLayer) {
		TerminalHistoryGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(TerminalHistoryGraphicsLayer);
	} else {
		TerminalHistoryGraphicsLayer.clear();
	}
	var data = res;
	if (data.length == 0) {
		return;
	}
	var LineExtent = new esri.geometry.Polyline(wgs);
	var LinePoints = [];
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var HistoryLine = new esri.geometry.Polyline(wgs);
		var GpsPoints = [];
		for (var j = 0, n = d.length; j < n; j++) {
			var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude, wgs);
			GpsPoints.push(pt);
			LinePoints.push(pt);
		}
		HistoryLine.addPath(GpsPoints);
		var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([ 250,
				120, 30, 1 ]), 2);
		var GpsHistoryGraphic = new esri.Graphic(HistoryLine, GpsLineSymbol,
			null, null);
		TerminalHistoryGraphicsLayer.add(GpsHistoryGraphic);

	}
	LineExtent.addPath(LinePoints);
	mapDataExtent = LineExtent.getExtent();
	map.setExtent(mapDataExtent);
}

var ChannelEventGraphicsLayer;
var ChannelEventLabelLayer;
function displayChannelEvent(res) {
	if (undefined == ChannelEventGraphicsLayer
		|| null == ChannelEventGraphicsLayer) {
		ChannelEventGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		ChannelEventGraphicsLayer.clear();
	}
	if (undefined == ChannelEventLabelLayer || null == ChannelEventLabelLayer) {
		ChannelEventLabelLayer = new esri.layers.GraphicsLayer();
	} else {
		ChannelEventLabelLayer.clear();
	}
	var data = res;
	if (data.length == 0) {
		return;
	}
	for (var i = 0; i < data.length; i++) {
		var d = data[i][1];
		for (var j = 0, n = d.length; j < n; j++) {
			var type = d[j].eventType;
			var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude, wgs);
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/event/" + type + ".png",
				"type" : "esriPMS"
			});
			symbol.setHeight(16);
			symbol.setWidth(16);
			var startGraphic = new esri.Graphic(pt, symbol, null, null);
			ChannelEventGraphicsLayer.add(startGraphic);
			var textsym = new esri.symbol.TextSymbol(channelEventLabel[type],
				null, new dojo.Color([ 0, 0, 230, 1 ]));
			textsym.setOffset(12, -15);
			var font = new esri.symbol.Font();
			font.setSize("12px");
			textsym.setFont(font);
			textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
			textsym.setHaloSize(1);
			var labelGraphic = new esri.Graphic(pt, textsym);
			ChannelEventLabelLayer.add(labelGraphic);
		}
	}
	ChannelEventLabelLayer.setVisibility(false);
	map.addLayer(ChannelEventGraphicsLayer);
	map.addLayer(ChannelEventLabelLayer);
}

function drawBuildings() {
	var res = parent.getFloorInfoByIndexRequestParam();
	if (res.state == "0") {
		alert("未选择");
		return;
	}
	var indexType = $("#floorKpiIndex").find("option:selected").val();
	var content = {
		Index : indexType,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 400000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		if (responses != null) {
			displayBuildings(responses);
		}
	});
}
function drawpoints() {
	var res = parent.getTestLogItemRequestParam();
	var indexType = $("#floorKpiIndex").find("option:selected").val();
	var content = {
		testLogItemIds : res.testLogItemIds,
		indexType : indexType,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 400000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		if (responses != null) {
			ShowPointTrackRender(responses);
		}
	});
}

var PointTrackGraphicsLayer;
var PointTrackLabelGraphicsLayer;
function ShowPointTrackRender(res) {
	if (undefined == PointTrackGraphicsLayer || null == PointTrackGraphicsLayer) {
		PointTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		PointTrackGraphicsLayer.clear();
	}
	if (undefined == PointTrackLabelGraphicsLayer
		|| null == PointTrackLabelGraphicsLayer) {
		PointTrackLabelGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		PointTrackLabelGraphicsLayer.clear();
	}
	PointTrackLabelGraphicsLayer.setVisibility(false);
	map.addLayer(PointTrackGraphicsLayer);
	map.addLayer(PointTrackLabelGraphicsLayer);
	var data = res[0];
	if (data.length == 0) {
		return;
	}
	SetMapLengend(res[1].colors, 1);
	var color = normalizingColor(res[1].colors);
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	var style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
	var index = $("#floorKpiIndex").find("option:selected").text();
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var t = d.indexValue,
			cc = ColorValue(color, t);
		var pt = new esri.geometry.Point(d.longitude, d.latitude, wgs);
		var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, cc);
		var startGraphic = new esri.Graphic(pt, symbol, {
			"indexValue" : t
		}, null);
		PointTrackGraphicsLayer.add(startGraphic);
		var textsym = new esri.symbol.TextSymbol(t, null, new dojo.Color([ 0,
			0, 255, 1 ]));
		textsym.setOffset(2, 4);
		GpsPoints.push(pt);
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table><tr><td>" + index
		+ "为： </td><td> ${indexValue} </td></tr></table>");
	PointTrackGraphicsLayer.setInfoTemplate(infoTemplate);
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
}

var ShowPointTrackGraphicsLayer;
var ShowPointTrackLabelGraphicsLayer;
var timeoutFlag2;
function showPointOfLonAndLat(placeData1) {
	if (undefined == ShowPointTrackGraphicsLayer || null == ShowPointTrackGraphicsLayer) {
		ShowPointTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		ShowPointTrackGraphicsLayer.clear();
	}
	if (undefined == ShowPointTrackLabelGraphicsLayer || null == ShowPointTrackLabelGraphicsLayer) {
		ShowPointTrackLabelGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		ShowPointTrackLabelGraphicsLayer.clear();
	}
	ShowPointTrackLabelGraphicsLayer.setVisibility(false);

	timeoutFlag2 = setInterval(function() {
		if (map) {
			clearInterval(timeoutFlag2);
			$("#MapTool").hide();
			showMap('img');
			map.addLayer(ShowPointTrackGraphicsLayer);
			map.addLayer(ShowPointTrackLabelGraphicsLayer);
			var data = placeData1;
			if (data.length == 0) {
				return;
			}

			//var color = normalizingColor(res[1].colors);
			var HistoryLine = new esri.geometry.Polyline(wgs);
			var GpsPoints = [];
			var style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;

			for (var i = 0; i < data.length; i++) {
				var d = data[i];
				//var t = d.indexValue
				//var cc = ColorValue(color, t);
				var pt = new esri.geometry.Point(d[0], d[1], wgs);
				var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, d[2]);
				var startGraphic = new esri.Graphic(pt, symbol, {
					"indexValue" : 0
				}, null);
				ShowPointTrackGraphicsLayer.add(startGraphic);
				var textsym = new esri.symbol.TextSymbol(0, null, new dojo.Color([ 0, 0, 255, 1 ]));
				textsym.setOffset(2, 4);
				GpsPoints.push(pt);
			}

			HistoryLine.addPath(GpsPoints);
			mapDataExtent = HistoryLine.getExtent();
			map.setExtent(mapDataExtent);
		}
	}, 1000);

}

var BuildingsGraphicsLayer;
var BuildingsLabelLayer;
function displayBuildings(res) {
	if (undefined == BuildingsGraphicsLayer || null == BuildingsGraphicsLayer) {
		BuildingsGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(BuildingsGraphicsLayer);
		BuildingsGraphicsLayer.on('click', function(evt) {
			var UUID = evt.graphic.attributes.UUID;
			var floorName = evt.graphic.attributes.floorName;
			showBuildingstooltip(floorName, UUID);
		});
	} else {
		BuildingsGraphicsLayer.clear();
	}
	if (undefined == BuildingsLabelLayer || null == BuildingsLabelLayer) {
		BuildingsLabelLayer = new esri.layers.GraphicsLayer();
		map.addLayer(BuildingsLabelLayer);
	} else {
		BuildingsLabelLayer.clear();
	}
	var colors = res[1][0].colors;
	SetBuildingsLengend(colors, 1);
	var data = res[0];
	if (data.length == 0) {
		return;
	}
	var LineExtent = new esri.geometry.Polyline(wgs);
	var LinePoints = [];
	for (var i = 0; i < data.length; i++) {
		if (data[i].index == null)
			continue;
		var t = data[i].index,
			cc = ColorValue(colors, t);
		var pt = new esri.geometry.Point(data[i].longitude, data[i].latitude,
			wgs);
		LinePoints.push(pt);
		var markerSymbol = new esri.symbol.SimpleMarkerSymbol();
		markerSymbol
			.setPath("M16,3.5c-4.142,0-7.5,3.358-7.5,7.5c0,4.143,7.5,18.121,7.5,18.121S23.5,15.143,23.5,11C23.5,6.858,20.143,3.5,16,3.5z M16,14.584c-1.979,0-3.584-1.604-3.584-3.584S14.021,7.416,16,7.416S19.584,9.021,19.584,11S17.979,14.584,16,14.584z");
		markerSymbol.setColor(cc);
		var startGraphic = new esri.Graphic(pt, markerSymbol, {
			"UUID" : data[i].id,
			"floorName" : data[i].floorName
		}, null);
		BuildingsGraphicsLayer.add(startGraphic);
		var textsym = new esri.symbol.TextSymbol(data[i].floorName, null,
			new dojo.Color([ 0, 0, 230, 1 ]));
		textsym.setOffset(12, -15);
		var font = new esri.symbol.Font();
		font.setSize("12px");
		textsym.setFont(font);
		textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
		textsym.setHaloSize(1);
		var labelGraphic = new esri.Graphic(pt, textsym);
		BuildingsLabelLayer.add(labelGraphic);
		startGraphic.attr("id", String(data[i].id));
	//var node = startGraphic.getNode();
	//$(node).attr("name","263");
	//var ti =$("#56");
	//$(startGraphic).attr("id", "57");
	//var ti =$("#57");
	}
	LineExtent.addPath(LinePoints);
	mapDataExtent = LineExtent.getExtent();
	map.setExtent(mapDataExtent);
	BuildingsLabelLayer.setVisibility(false);

}
var tootipOptions;
function updateTooltipContent() {
	tootipOptions.content.panel({
		width : 800,
		height : 400,
		border : false,
		href : './floor.html'
	});
}
function updateTooltipDate(floorName) {
	var res = parent.getFloorInfoRequestParam();
	$.post(res.requestUrl + "?floorName=" + floorName, function(result) {
		if (result.errorMsg) {
			$.messager.alert("系统提示", result.errorMsg, 'error');
		} else {
			if (result.info) {
				$("#moveDataTable").datagrid('loadData', result.info.moveData);
				$("#moveVoiceTable")
					.datagrid('loadData', result.info.moveVoice);
				$("#moveVideoTable")
					.datagrid('loadData', result.info.moveVideo);
				$("#linkDataTable").datagrid('loadData', result.info.linkData);
				$("#linkVoiceTable")
					.datagrid('loadData', result.info.linkVoice);
				$("#linkVideoTable")
					.datagrid('loadData', result.info.linkVideo);
				$("#telecomDataTable").datagrid('loadData',
					result.info.telecomData);
				$("#telecomVoiceTable").datagrid('loadData',
					result.info.telecomVoice);
				$("#telecomVideoTable").datagrid('loadData',
					result.info.telecomVideo);
			}

		}
	}, 'json');
}
function statis() {
	window.parent.goAdd();
}

function downloading() {
	window.parent.goDownload();
}
function closeTooltips() {
	if (te) {
		te.tooltip('destroy');
	}
}
var te;
function showBuildingstooltip(name, id) {
	var UUID = id;
	var floorName = name;
	var too = $("#" + String(UUID));
	too.tooltip({
		position : 'top',
		content : $('<div></div>'),
		showEvent : 'click',
		showDelay : 10,
		onShow : function() {
			var t = $(this);
			te = t;
			t.bind('mouseleave', function() {
				t.tooltip('show');
			});
			t.tooltip('tip').unbind().bind('mouseenter', function() {
				t.tooltip('show');
			}).bind('mouseleave', function() {
				t.tooltip('show');
			});
		},
		onPosition : function() {
			var t = $(this);
			t.tooltip('show');
		}
	});
	too.tooltip("show");
	tootipOptions = too.tooltip('options');
	too.tooltip('update', updateTooltipContent());
	setTimeout(function() {
		updateTooltipDate(floorName);
	}, 500);
}

function SetBuildingsLengend(Rendercolor, opacity) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	// console.log(Rendercolor);
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		var start = Rendercolor[i].beginValue;
		var end = Rendercolor[i].endValue;
		color = Rendercolor[i].color;
		// color=color.colorRgb();
		// color=RgbToRgba(color,opacity);
		row += "<tr><td style='width: 50px;background-color:" + color
			+ "'></td><td>";
		var td = $("#floorKpiIndex").find("option:selected").text();
		if (start != null) {
			td = start + "&lt" + td;
		}
		if (end != null) {
			td = td + "≤" + end;
		}
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}

// 获取小区或栅格基本信息框
var SetWindowContent = function(arrs) {
	var res = "<table class='t2'>";
	var t = "";
	for (var i = 0, n = arrs.length; i < n; i++) {
		var name = arrs[i];
		if (arrs[i] == "HEIGHT") {
			name = "站高";
		} else if (arrs[i] == "REGION") {
			name = "区域";
		}
		if (i % 2 == 0) {
			t += "<tr class='tr1'><td>" + name + "</td><td>" + "${"
				+ arrs[i] + "}</td></tr>";
		} else {
			t += "<tr class='tr2'><td>" + name + "</td><td>" + "${"
				+ arrs[i] + "}</td></tr>";
		}
	}
	res += t + "</table>";
	return res;
}
/**
 * Created by zhaobenfu on 2015/9/8.
 */

/*整体分析渲染地图*/
function initDataMap() {
	if (toolbarType == '31') { //volte视频质差整体
		var res = parent.getVideoQBRequestParam();
		var content = {
			testLogItemIds : res.testLogItemIds,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				ShowGpsVideoBadRoad(responses);
			}
		});
	} else if (toolbarType == '51') { //流媒体视频质差整体
		var res = parent.getStreamQBRequestParam();
		var content = {
			testLogItemIds : res.testLogItemIds,
			'f' : "text"
		};
		var Request = esri.request({
			url : res.requestUrl,
			timeout : 400000,
			content : content,
			handleAs : "json",
			callbackParamName : "callback"
		});
		Request.then(function(responses) {
			if (responses != null) {
				ShowGpsVideoBadRoad(responses);
			}
		});
	}
}

/**
 * 初始化查询地图图例颜色设置
 */
function initColorMapLegend(colorMapType) {
	var res = parent.getDefaultMapLegendUrl();
	$.ajax({
		type : "GET",
		url : res.requestUrl,
		async : false,
		data : {
			'colorMapType' : colorMapType
		},
		dataType : "json", //服务器响应的数据类型
		success : function(data) {
			if (data != undefined && data != null) {
				colorMapEtgTral = {};
				colorMapEtgTral = data[0];
				initColorMapParam = data[1];
			}
		}
	});
}

function saveMapLegend(colorMapType) {
	var res = parent.saveMapLegendUrl();
	var data = {};
	if (colorMapEtgTral != null) {
		var mapLegendArry = Object.keys(colorMapEtgTral);
		for (var i = 0; i < mapLegendArry.length; i++) {
			var array = colorMapEtgTral[mapLegendArry[i]];
			var colors = [];
			for (var j = 0; j < array.length; j++) {
				colors.push(array[j].color);
			}
			var colorName = mapLegendArry[i] + "Color";
			data['legendMap[\'' + colorName + '\']'] = colors.join(",");
		}
		data['colorMapType'] = colorMapType;

		$.ajax({
			type : "GET",
			url : res.requestUrl,
			data : data,
			dataType : "json", //服务器响应的数据类型
			success : function(data) {
				if (data != undefined && data != null) {
					if (data.errorMsg) {
						alert(data.errorMsg);
					} else {
						console.info("图例保存成功");
					}
				}
			}
		});
	}

}


// 所有视频质差路段的轨迹点显示
function ShowGpsVideoBadRoad(res) {
	var colors = res[1].colors;
	setMapVideoLegend(colors);
	var data = res[0];
	for (var i = 0, ll = colors.length; i < ll; i++) {
		for (var j = 0, nn = data.length; j < nn; j++) {
			if (colors[i].videoQBType == data[j][0]) {
				var color = colors[i].color;
				var da = data[j],
					mm = da.length;
				if (mm > 1) {
					showPointByData(da, color);
				// for (var k = 1; k < mm; k++) {
				// 	var d = compress(da[k], 0.5);
				// 	showLinesymByData(d, color);
				// }
				}
				break;
			}
		}
	}
}
function setMapVideoLegend(Rendercolor) {
	var html = "<table style='white-space:nowrap'>";
	var row = "";
	var color;
	for (var i = 0, b = Rendercolor.length; i < b; i++) {
		color = Rendercolor[i].color;
		row += "<tr><td style='height:20px;width: 20px;background-color:" + color
			+ "'></td><td style='font-size:12px;float:left;'>";
		var td = Rendercolor[i].videoQBTypeName;
		row += td + "</td></tr>";
	}
	html = html + row + "</table>";
	$("#mapLegend").html(html);
}



function showPointByData(res, color) {
	var HistoryLine = new esri.geometry.Polyline(wgs);
	var GpsPoints = [];
	var cc = color;
	var data = res;
	var style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
	var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, cc);
	for (var i = 1, n = data.length; i < n; i++) {
		var pt = new esri.geometry.Point(data[i].longitude, data[i].latitude,
			wgs);
		var stGraphic = new esri.Graphic(pt, symbol, null, null);
		map.graphics.add(stGraphic);
		GpsPoints.push(pt);
	}
	HistoryLine.addPath(GpsPoints);
	mapDataExtent = HistoryLine.getExtent();
	map.setExtent(mapDataExtent);
}

var boshuClick1;
var boshuClick2;
var stationLineClick;
var stationLineLteClick;
var ncellCheckButtonClick;
var equalFreqPciButtonClick;
//图层图例
var colorMapEtgTral = null;
//界面图层的初始化图例
var initColorMapParam = null;

$(document).ready(function() {
	$("#cellBoshuAnalyseButton").click(function() {

		if (boshuClick1 == undefined || boshuClick1 == null) {
			boshuClick1 = dojo.connect(CellLayers[0], "onClick", function(e) {
				var lon = e.graphic.attributes.LONGITUDE;
				var lat = e.graphic.attributes.LATITUDE;
				var azimuth = e.graphic.attributes.AZIMUTH;
				var res = parent.getEmbbCoverBoshuAnalyseParam();
				$.ajax({
					type : "GET",
					url : res.requestUrl,
					data : {
						'cellNameParam' : e.graphic.attributes.CELLNAME,
						'testLogItemIds' : res.testLogItemIds,
						'netType' : 2,
						'lon' : lon,
						'lat' : lat,
						'azimuth' : azimuth
					},
					dataType : "json", //服务器响应的数据类型
					success : function(data) {
						window.parent.$('#cellBoshuAnalyse').window('open');
						window.parent.analyseMap(data.data);
					}
				});
			});
		/*boshuClick2 = dojo.connect(CellLayers[1], "onClick", function(e){
			var lon = e.graphic.attributes.LONGITUDE;
			var lat = e.graphic.attributes.LATITUDE;
			var azimuth = e.graphic.attributes.AZIMUTH;
			var res = parent.getEmbbCoverBoshuAnalyseParam();
			$.ajax({
	                type:"GET",
	                url:res.requestUrl,
	                data:{
	                	'cellNameParam' : e.graphic.attributes.CELLNAME,
	                	'testLogItemIds' : res.testLogItemIds,
	                	'netType' : 2,
	                	'lon' : lon,
	                	'lat' : lat,
	                	'azimuth' : azimuth
					},
	                dataType:"json",//服务器响应的数据类型
	                success:function(data){
		            window.parent.$('#cellBoshuAnalyse').window('open');
					window.parent.analyseMap(data.data);
	                }
	            });
		});*/
		} else {
			boshuDisconnect();
		}
	});
	$("#cellBoshuConditionButton").click(function() {
		window.parent.$('#cellBoshuCondition').window('open');
	});
	//站间距点击事件
	$("#stationLine").click(function() {
		if (undefined == stationLineClick || null == stationLineClick) {
			stationLineClick = dojo.connect(CellLayers[0], "onClick", function(e) {
				if (undefined == CellToCellGraphicsLayer || null == CellToCellGraphicsLayer) {
					CellToCellGraphicsLayer = new esri.layers.GraphicsLayer();
					map.addLayer(CellToCellGraphicsLayer);
				} else {
					CellToCellGraphicsLayer.clear();
				}
				var lon = e.graphic.attributes.LONGITUDE;
				var lat = e.graphic.attributes.LATITUDE;
				var pt = new esri.geometry.Point(lon, lat, wgs);

				var cellNameArrys = [];
				cellNameArry = queryNCellNames(e.graphic.attributes.CELLNAME, "2");
				var Query = new esri.tasks.Query();
				Query.returnGeometry = true;
				if (cellNameArry.length > 0) {
					Query.where = "CELLNAME='" + e.graphic.attributes.CELLNAME + "' ";
					for (var i = 0; i < cellNameArry.length; i++) {
						Query.where = Query.where + "OR CELLNAME = '" + cellNameArry[i] + "' ";
					}
				}
				CellLayers[0].queryFeatures(Query, function(ncellGeometry) {
					if (ncellGeometry != null && ncellGeometry.features.length > 0) {
						for (var j = 0; j < ncellGeometry.features.length; j++) {
							var clc = "#EFCF08";
							//标注方位角
							var p1 = new esri.geometry.Point(ncellGeometry.features[j].attributes.LONGITUDE,
								ncellGeometry.features[j].attributes.LATITUDE, wgs);
							//							var pictureMarkerSymbol = new esri.symbol.PictureMarkerSymbol(
							//									'http://localhost:8080/CloudMonitor5G/gis/images/arrow.png',100,50);
							var pictureMarkerSymbol = new esri.symbol.PictureMarkerSymbol(
								'', 100, 50);
							pictureMarkerSymbol.setAngle(parseFloat(ncellGeometry.features[j].attributes.AZIMUTH) - 90);
							//pictureMarkerSymbol.setOffset(25,0);
							var pms = new esri.Graphic(p1, pictureMarkerSymbol);
							CellToCellGraphicsLayer.add(pms);
							//标注距离
							var pz = new esri.geometry.Point((parseFloat(lon) + parseFloat(ncellGeometry.features[j].attributes.LONGITUDE)) / 2,
								(parseFloat(lat) + parseFloat(ncellGeometry.features[j].attributes.LATITUDE)) / 2, wgs);
							var distance = getDistance(lon, lat, ncellGeometry.features[j].attributes.LONGITUDE, ncellGeometry.features[j].attributes.LATITUDE);
							if (distance == 0) { //如果距离为0 说明是同站小区,就不进行拉线和标记距离
								continue;
							}
							var textsym = new esri.symbol.TextSymbol(distance + 'M', null, new dojo.Color("#000000"));
							textsym.setOffset(2, 4);
							var font = new esri.symbol.Font();
							font.setSize("14px");
							textsym.setFont(font);
							textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
							textsym.setHaloSize(1);
							var labelGraphic = new esri.Graphic(pz, textsym);
							CellToCellGraphicsLayer.add(labelGraphic);
							//邻小区连线
							var line = new esri.geometry.Polyline(wgs);
							line.addPath([ pt, p1 ]);
							mapDataExtent = line.getExtent();
							map.setExtent(mapDataExtent);
							var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
							var GpsHistorySymbol = new esri.Graphic(line, GpsLineSymbol, null, null);
							CellToCellGraphicsLayer.add(GpsHistorySymbol);
						}
					}
				}, function(err) {
					alert(err);
					return;
				});
			});
		} else {
			if (undefined == CellToCellGraphicsLayer || null == CellToCellGraphicsLayer) {
				CellToCellGraphicsLayer = new esri.layers.GraphicsLayer();
				map.addLayer(CellToCellGraphicsLayer);
			} else {
				CellToCellGraphicsLayer.clear();
			}
			dojo.disconnect(stationLineClick);
			stationLineClick = null;
		}

		if (undefined == stationLineLteClick || null == stationLineLteClick) {
			stationLineLteClick = dojo.connect(CellLayers[1], "onClick", function(e) {
				if (undefined == CellToCellGraphicsLayer || null == CellToCellGraphicsLayer) {
					CellToCellGraphicsLayer = new esri.layers.GraphicsLayer();
					map.addLayer(CellToCellGraphicsLayer);
				} else {
					CellToCellGraphicsLayer.clear();
				}
				var lon = e.graphic.attributes.LONGITUDE;
				var lat = e.graphic.attributes.LATITUDE;
				var pt = new esri.geometry.Point(lon, lat, wgs);

				var cellNameArrys = [];
				cellNameArry = queryNCellNames(e.graphic.attributes.CELLNAME, "1");
				var Query = new esri.tasks.Query();
				Query.returnGeometry = true;
				if (cellNameArry.length > 0) {
					Query.where = "CELLNAME='" + e.graphic.attributes.CELLNAME + "' ";
					for (var i = 0; i < cellNameArry.length; i++) {
						Query.where = Query.where + "OR CELLNAME = '" + cellNameArry[i] + "' ";
					}
				}
				CellLayers[1].queryFeatures(Query, function(ncellGeometry) {
					if (ncellGeometry != null && ncellGeometry.features.length > 0) {
						for (var j = 0; j < ncellGeometry.features.length; j++) {
							var clc = "#EFCF08";
							//标注方位角
							var p1 = new esri.geometry.Point(ncellGeometry.features[j].attributes.LONGITUDE,
								ncellGeometry.features[j].attributes.LATITUDE, wgs);
							var pictureMarkerSymbol = new esri.symbol.PictureMarkerSymbol(
								'', 100, 50);
							pictureMarkerSymbol.setAngle(parseFloat(ncellGeometry.features[j].attributes.AZIMUTH) - 90);
							//pictureMarkerSymbol.setOffset(25,0);
							var pms = new esri.Graphic(p1, pictureMarkerSymbol);
							CellToCellGraphicsLayer.add(pms);
							//标注距离
							var pz = new esri.geometry.Point((parseFloat(lon) + parseFloat(ncellGeometry.features[j].attributes.LONGITUDE)) / 2,
								(parseFloat(lat) + parseFloat(ncellGeometry.features[j].attributes.LATITUDE)) / 2, wgs);
							var distance = getDistance(lon, lat, ncellGeometry.features[j].attributes.LONGITUDE, ncellGeometry.features[j].attributes.LATITUDE);
							if (distance == 0) { //如果距离为0 说明是同站小区,就不进行拉线和标记距离
								continue;
							}
							var textsym = new esri.symbol.TextSymbol(distance + 'M', null, new dojo.Color("#000000"));
							textsym.setOffset(2, 4);
							var font = new esri.symbol.Font();
							font.setSize("14px");
							textsym.setFont(font);
							textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
							textsym.setHaloSize(1);
							var labelGraphic = new esri.Graphic(pz, textsym);
							CellToCellGraphicsLayer.add(labelGraphic);
							//邻小区连线
							var line = new esri.geometry.Polyline(wgs);
							line.addPath([ pt, p1 ]);
							mapDataExtent = line.getExtent();
							map.setExtent(mapDataExtent);
							var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
							var GpsHistorySymbol = new esri.Graphic(line, GpsLineSymbol, null, null);
							CellToCellGraphicsLayer.add(GpsHistorySymbol);
						}
					}
				}, function(err) {
					alert(err);
					return;
				});
			});
		} else {
			if (undefined == CellToCellGraphicsLayer || null == CellToCellGraphicsLayer) {
				CellToCellGraphicsLayer = new esri.layers.GraphicsLayer();
				map.addLayer(CellToCellGraphicsLayer);
			} else {
				CellToCellGraphicsLayer.clear();
			}
			dojo.disconnect(stationLineLteClick);
			stationLineLteClick = null;
		}
	});

	function queryNCellNames(cellName, coverType) {
		var cellNameArry = [];
		var res = parent.getNcellNameRequestParam();
		$.ajax({
			type : "GET",
			url : res.requestUrl,
			async : false,
			data : {
				'cellName' : cellName,
				'coverType' : coverType
			},
			dataType : "json", //服务器响应的数据类型
			success : function(data) {
				if (data != undefined && data != null) {
					cellNameArry = data.split(",");
				}
			}
		});
		return cellNameArry;
	}


	$("#layerManager").click(function() {
		var data = {
			'layerChooseData' : [
				/*				{'recSeqNo':true,'name':'测试数据','mark':false,'deviationX':'2','deviationY':'0'},*/
				{
					'recSeqNo' : true,
					'name' : 'LTE小区',
					'mark' : false,
					'deviationX' : '0',
					'deviationY' : '0'
				},
				{
					'recSeqNo' : true,
					'name' : '5G小区',
					'mark' : true,
					'deviationX' : '0',
					'deviationY' : '0'
				}
			],
			'conventionalSignsData' : { //指标选择
				/* 样例
				 * 'targetChooseData':[{label: 'rsrp',value: '1',"selected":true},{label: 'rsrq',value:'9',"selected":false},
					{label: '下行ibler(NR DL IBLER(73000))',value: '101',"selected":false},{label: 'beam数量',value: '102',"selected":false}],
				'targetChooseData':[{label: 'Nr rsrp',value: '109',"selected":true},{label: 'Lte rsrp',value:'110',"selected":false},
					{label: 'Nr sinr',value: '111',"selected":false},{label: 'Lte sinr',value: '112',"selected":false},{label: 'beam数量',value: '102',"selected":false}],
				'targetChooseTableData':{//标签选择表数据
					'rsrp':[{color:'FF0000', scope:'X<-110',data:'(1184)3%'},
						{color:'FFC000', scope:'-110<=X<-105',data:'(1367)3%'},
						{color:'FFFF00', scope:'-105<=X<-100',data:'(3549)8%'},
						{color:'8DB4E2', scope:'-100<=X<-95',data:'(4435)13%'},
						{color:'1F497D', scope:'-95<=X<-85',data:'(2347)23%'},
						{color:'92D050', scope:'-85<=X<-75',data:'(2347)23%'},
						{color:'00B050', scope:'-75<=X<-40',data:'(2347)23%'}],
					'sinr':[{color:'FF0000', scope:'X<-3',data:'(1184)3%'},
						{color:'FFC000', scope:'-3<=X<0',data:'(1367)3%'},
						{color:'FFFF00', scope:'0<=X<3',data:'(3549)8%'},
						{color:'8DB4E2', scope:'3<=X<6',data:'(4435)13%'},
						{color:'1F497D', scope:'6<=X<9',data:'(2347)23%'},
						{color:'92D050', scope:'9<=X<15',data:'(2347)23%'},
						{color:'00B050', scope:'15<=X',data:'(2347)23%'}],
					'beam':[{color:'00ff00', scope:'1<=X<3',data:'(1184)26%'},
						{color:'0000ff', scope:'3<=X<5',data:'(1367)3%'},
						{color:'ccaaa0', scope:'5<=X<7',data:'(3549)8%'},
						{color:'ddbbac', scope:'7<=X<9',data:'(4435)13%'},
						{color:'acbb00', scope:'9<=X<11',data:'(2347)23%'}],
				},*/
				'targetChooseData' : initColorMapParam,
				'targetChooseTableData' : colorMapEtgTral, //标签选择表数据
				'sampleScopeData' : {
					'sampleScope' : 2
				},
				'sampletargetData' : {
					'sampletarget' : 1
				},
				'targetFoatData' : [
					/*	{name:'测试数据', fontColor:'黑色,红色,蓝色,绿色',fontColorSelected:'红色',
							fontSize:'10,11,12,13,14,15,16',fontSizeSelected:'10'},*/
					{
						name : 'LTE小区',
						fontColor : '黑色,红色,蓝色,绿色',
						fontColorSelected : '黑色',
						fontSize : '10,11,12,13,14,15,16',
						fontSizeSelected : '13'
					},
					{
						name : '5G小区',
						fontColor : '黑色,红色,蓝色,绿色',
						fontColorSelected : '红色',
						fontSize : '10,11,12,13,14,15,16',
						fontSizeSelected : '16'
					}
				],
				'sampleTypeData' : [ {
					label : '圆形',
					value : 'Circle',
					"selected" : true
				}, {
					label : '正方形',
					value : 'Square',
					"selected" : false
				} ]
			},
			'cellData' : {
				'showImg' : {
					'checked' : true,
					'networkType' : 5,
					'unifyRadio' : {
						'checked' : true,
						'unifyColor' : '3F97F1'
					},
					'unifyRadio4G' : {
						'checked' : true,
						'unifyColor' : '3F97F1'
					},
					'unifyRadio5G' : {
						'checked' : true,
						'unifyColor' : '3F97F1'
					},
					'pciMo4Radio' : {
						'checked' : false,
						'pciMo4Color' : 'FFAAFF,379D16,D98B27,26BAD8'
					},
					'pciMo3Radio' : {
						'checked' : false,
						'pciMo3Color' : 'FFAAFF,379D16,D98B27'
					},
					'cellRange' : 20
				},
				'showTarget' : {
					'checked' : true,
					'showTargetOption' : 'CELLNAME,CELLID'
				}
			},
			'line' : {
				'cellCoverThreshold' : {
					'checked' : true,
					'cellCoverThresholdVal' : 20,
					'cellLinkColor' : {
						'value' : '2',
						'cellLinkColorInput' : '0AAB5B'
					}
				},
				'showNumber' : {
					'checked' : true,
					'showNumberSelect' : 4,
					'ncellLinkColor' : {
						'value' : '1',
						'ncellLinkColorInput' : '0AAB5B'
					},
					'topColor' : {
						'top1' : '379D16',
						'top2' : '37aD16',
						'top3' : '37AD16',
						'top4' : '379D16',
						'top5' : '37FD16',
						'top6' : '37DD16'
					}
				}
			},
			'eventData' : {
				'eventTreeData' : [ {
					id : 1,
					text : 'SA制式移动性事件',
					state : 'closed',
					children : [ {
						text : '5G切换尝试'
					}, {
						text : '5G切换成功'
					}, {
						text : '5G切换失败'
					},
						{
							text : '5G同频切换尝试'
						}, {
							text : '5G同频切换成功'
						}, {
							text : '5G异频切换尝试'
						},
						{
							text : '5G异频切换成功'
						}, {
							text : '位置更新尝试'
						}, {
							text : '位置更新成功'
						}, {
							text : '位置更新失败'
						} ]
				}, {
					id : 2,
					text : 'NSA制式事件',
					state : 'closed',
					children : [ {
						text : 'Same_LTE_Diff_NR成功'
					}, {
						text : 'Same_LTE_Diff_NR尝试'
					}, {
						text : 'Diff_LTE_intrafreq_Same_NR成功'
					},
						{
							text : 'Diff_LTE_intrafreq_Same_NR尝试'
						}, {
							text : 'Diff_LTE_interfreq_Same_NR成功'
						}, {
							text : 'Diff_LTE_interfreq_Same_NR尝试'
						},
						{
							text : 'Diff_LTE_intrafreq_Diff_NR成功'
						}, {
							text : 'Diff_LTE_intrafreq_Diff_NR尝试'
						}, {
							text : 'Diff_LTE_interfreq_Diff_NR成功'
						},
						{
							text : 'Diff_LTE_interfreq_Diff_NR尝试'
						}, {
							text : 'Scell add成功'
						}, {
							text : 'Scell add尝试'
						} ]
				}, {
					id : 3,
					text : 'SA制式接入性事件',
					state : 'closed',
					children : [ {
						text : 'RRC连接建立尝试'
					}, {
						text : 'RRC连接建立成功'
					}, {
						text : 'Service建立尝试'
					}, {
						text : 'Service建立成功'
					}, {
						text : 'PDU会话建立尝试'
					},
						{
							text : 'PDU会话建立成功'
						}, {
							text : 'PDU会话修改尝试'
						}, {
							text : 'PDU会话修改成功'
						}, {
							text : '注册请求次数'
						}, {
							text : '注册成功次数'
						} ]
				}, {
					id : 4,
					text : '4/5G互操作事件',
					state : 'closed',
					children : [ {
						text : 'LTE-to-NR重定向尝试'
					}, {
						text : 'LTE-to-NR重定向成功'
					}, {
						text : 'NR-to-LTE重定向尝试'
					}, {
						text : 'NR-to-LTE重定向成功'
					},
						{
							text : 'LTE-to-NR切换尝试'
						}, {
							text : 'LTE-to-NR切换成功'
						}, {
							text : 'NR-to-LTE切换尝试'
						},
						{
							text : 'NR-to-LTE切换成功'
						}, {
							text : 'LTE-to-NR重选尝试'
						}, {
							text : 'LTE-to-NR重选成功'
						}, {
							text : 'NR-to-LTE重选尝试'
						},
						{
							text : 'NR-to-LTE重选成功'
						} ]
				} ],
				'eventTableData' : [
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'Same_LTE_Diff_NR失败',
						'imgUrl' : 'Same_LTE_Diff_NR失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'Diff_LTE _Same_NR失败',
						'imgUrl' : 'Diff_LTE_intrafreq_Same_NR失败'
					},
					/*{'eventRecSeqNo':false,'parentNodeId':0,'eventName':'Diff_LTE_interfreq_Same_NR失败',
						'imgUrl':'Diff_LTE_interfreq_Same_NR失败'},*/
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'Diff_LTE _Diff_NR失败',
						'imgUrl' : 'Diff_LTE_intrafreq_Diff_NR失败'
					},
					/*{'eventRecSeqNo':false,'parentNodeId':0,'eventName':'Diff_LTE_interfreq_Diff_NR失败',
						'imgUrl':'Diff_LTE_interfreq_Diff_NR失败'},*/
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'Scell add失败',
						'imgUrl' : 'Scell add失败'
					},
					/*{'eventRecSeqNo':false,'parentNodeId':0,'eventName':'5G切换失败',
							'imgUrl':'5G切换失败'},*/
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : '5G同频切换失败',
						'imgUrl' : '5G同频切换失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : '5G异频切换失败',
						'imgUrl' : '5G异频切换失败'
					},
					/*{'eventRecSeqNo':false,'parentNodeId':0,'eventName':'5G到LTE互操作失败',
						'imgUrl':'5G到LTE互操作失败'},*/
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'PDU会话修改失败',
						'imgUrl' : 'PDU会话修改失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : '注册失败',
						'imgUrl' : '注册失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'RRC失败',
						'imgUrl' : 'RRC失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'PDU会话建立失败',
						'imgUrl' : 'PDU会话建立失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'LTE-to-NR重定向失败',
						'imgUrl' : 'LTE-to-NR重定向失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'NR-to-LTE重定向失败',
						'imgUrl' : 'NR-to-LTE重定向失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'LTE-to-NR切换失败',
						'imgUrl' : 'LTE-to-NR切换失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'NR-to-LTE切换失败',
						'imgUrl' : 'NR-to-LTE切换失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'LTE-to-NR重选失败',
						'imgUrl' : 'LTE-to-NR重选失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'NR-to-LTE重选失败',
						'imgUrl' : 'NR-to-LTE重选失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'Service建立失败',
						'imgUrl' : 'Service建立失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'en-dc失败',
						'imgUrl' : 'EN-DC无线链路失败'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'FTP下载掉线',
						'imgUrl' : 'FTP下载掉线'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'FTP上传掉线',
						'imgUrl' : 'FTP上传掉线'
					},
					{
						'eventRecSeqNo' : false,
						'parentNodeId' : 0,
						'eventName' : 'PING失败',
						'imgUrl' : 'PING失败'
					}
				]
			}
		};
		if (window.parent.layerManageData.layerChooseData) {
			//data = window.parent.layerManageData.conventionalSignsData;
			data = window.parent.layerManageData;
		}
		window.parent.initLayerManageFunc(data);
		window.parent.$('#layerManagerWin').window('open');
	});

	$("#layerManager2").click(function() {
		var data = {
			'layerChooseData' : [
				/*				{'recSeqNo':true,'name':'测试数据','mark':false,'deviationX':'2','deviationY':'0'},*/
				{
					'recSeqNo' : true,
					'name' : 'LTE小区',
					'mark' : false,
					'deviationX' : '0',
					'deviationY' : '0'
				},
				{
					'recSeqNo' : true,
					'name' : '5G小区',
					'mark' : true,
					'deviationX' : '0',
					'deviationY' : '0'
				}
			],
			'conventionalSignsData' : { //指标选择
				'targetChooseData' : initColorMapParam,
				'targetChooseTableData' : colorMapEtgTral, //标签选择表数据
				'sampleScopeData' : {
					'sampleScope' : 2
				},
				'sampletargetData' : {
					'sampletarget' : 1
				},
				'targetFoatData' : [
					/*	{name:'测试数据', fontColor:'黑色,红色,蓝色,绿色',fontColorSelected:'红色',
							fontSize:'10,11,12,13,14,15,16',fontSizeSelected:'10'},*/
					{
						name : 'LTE小区',
						fontColor : '黑色,红色,蓝色,绿色',
						fontColorSelected : '黑色',
						fontSize : '10,11,12,13,14,15,16',
						fontSizeSelected : '13'
					},
					{
						name : '5G小区',
						fontColor : '黑色,红色,蓝色,绿色',
						fontColorSelected : '红色',
						fontSize : '10,11,12,13,14,15,16',
						fontSizeSelected : '16'
					}
				],
				'sampleTypeData' : [ {
					label : '圆形',
					value : 'Circle',
					"selected" : true
				}, {
					label : '正方形',
					value : 'Square',
					"selected" : false
				} ]
			},
			'cellData' : {
				'showImg' : {
					'checked' : true,
					'networkType' : 5,
					'unifyRadio4G' : {
						'checked' : true,
						'unifyColor' : '3F97F1'
					},
					'unifyRadio5G' : {
						'checked' : true,
						'unifyColor' : '3F97F1'
					},
					'pciMo4Radio' : {
						'checked' : false,
						'pciMo4Color' : 'FFAAFF,379D16,D98B27,26BAD8'
					},
					'pciMo3Radio' : {
						'checked' : false,
						'pciMo3Color' : 'FFAAFF,379D16,D98B27'
					},
					'cellRange' : 20
				},
				'showTarget' : {
					'checked' : true,
					'showTargetOption' : 'CELLNAME,CELLID'
				}
			},
		};
		if (window.parent.layerManageData.cellData) {
			data = window.parent.layerManageData;
		}
		window.parent.initLayerManageFunc(data);
		window.parent.$('#layerManagerWin').window('open');
	});

	//邻区核查
	$("#ncellCheckButton").click(function() {
		//document.body.style.cursor = 'help';
		if (undefined == ncellCheckButtonClick || null == ncellCheckButtonClick) {
			ncellCheckButtonClick = dojo.connect(CellLayers[0], "onClick", function(e) {
				var cellIds = [ '254200873', '6710020', '59133481', '6710021', '54944297', '54944299', '108403627' ];
				ncellLayerTocolor(cellIds);
				console.log('7777777777888888888888888888');
				var inPoint = new esri.geometry.Point(parseFloat('112.847020'), parseFloat('35.514030'), wgs);
				map.centerAndZoom(inPoint, 15);
			});
		} else {
			dojo.disconnect(ncellCheckButtonClick);
			ncellLayerTocolor([]);
			ncellCheckButtonClick = null;
		}

	});

	//全图特定波束统计
	$("#equalFreqPciButton").click(function() {
		if (undefined == equalFreqPciButtonClick || null == equalFreqPciButtonClick) {
			equalFreqPciButtonClick = dojo.connect(CellLayers[0], "onClick", function(e) {
				equalFreqPciToColor(e.graphic.attributes.FREQ1, e.graphic.attributes.PCI);
			});
		} else {
			dojo.disconnect(equalFreqPciButtonClick);
			ncellLayerTocolor([]);
			equalFreqPciButtonClick = null;
		}
	});
});

function ncellLayerTocolor(cellIds) {
	var n = CellLayers.length;
	var colors = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' ];
	if (n > 0) {
		console.log(n);
		for (var i = 0; i < n; i++) {
			map.removeLayer(CellLayers[i]);
			var defaultSym = new esri.symbol.SimpleFillSymbol("solid", null, new dojo.Color([ 0, 128, 0, 0.75 ]));
			var renderer = new esri.renderer.ClassBreaksRenderer(defaultSym, "CELLID");
			if (cellIds.length > 0) {
				for (var j = 0; j < cellIds.length; j++) {
					var cc = '#';
					for (var n = 0; n < 6; n++) {
						cc = cc + colors[Math.ceil(Math.random() * 15)];
					}
					renderer.addBreak(parseFloat(cellIds[j]) - 0.1, parseFloat(cellIds[j]) + 0.1,
						new esri.symbol.SimpleFillSymbol("solid", null, cc));
				}
			}
			CellLayers[i].setRenderer(renderer);
			map.addLayer(CellLayers[i]);
		}
		console.log('9999999100000');
	} else {
		return;
	}
}

//同频同PCI核查
function equalFreqPciToColor(freq, pci) {
	var n = CellLayers.length;
	if (n > 0) {
		for (var i = 0; i < n; i++) {
			var cellIds = [];
			for (var j = 0; j < CellLayers[i].graphics.length; j++) {
				if (CellLayers[i].graphics[j].attributes.FREQ1 == freq && CellLayers[i].graphics[j].attributes.PCI == pci) {
					cellIds.push(CellLayers[i].graphics[j].attributes.CELLID);
				}
			}
			ncellLayerTocolor(cellIds);
		}
	} else {
		return;
	}
}

//取消波束按钮绑定事件
function boshuDisconnect() {
	dojo.disconnect(boshuClick1);
	boshuClick1 = null;
}

//获取两个经纬度之间的距离
/**
 * lon1,lat1,lon2,lat2
 */
function getDistance(lon1, lat1, lon2, lat2) {
	var radLat1 = rad(lat1);
	var radLat2 = rad(lat2);
	var a = radLat1 - radLat2;
	var b = rad(lon1) - rad(lon2);
	var s = 2 * Math.asin(
		Math.sqrt(
			Math.pow(Math.sin(a / 2), 2)
			+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
		)
	);
	s = s * 6378137;
	s = Math.round(s * 10000) / 10000;
	return s;
}

function rad(d) {
	return d * Math.PI / 180.0;
}

//根据indextype类型获取采样点数据(根据图层管理数据) maxuancheng
//indexType:指标   val:指标颜色表的key字段   scope:显示范围   deviationX,deviationY 偏移量
function againGPSTrackRender(indexType, val, scope, x, y) {
	deviationX = x;
	deviationY = y;
	var res = parent.getCompareTestLogItemRequestParam();
	if (res.testLogItemIds == null || res.testLogItemIds == undefined) {
		return;
	}
	var indexType = $("#selectIndex").find("option:selected").val();
	var content = {
		testLogItemIds : res.testLogItemIds,
		indexType : indexType,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 400000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(responses) {
		if (responses != null) {
			if (scope == 1) {
				for (var i = 0; i < responses[0].length; i++) {
					for (var j = 0; j < responses[0][i][1].length; j++) {
						responses[0][i][1][j]['beamId'] = 100 + j;
						responses[0][i][1][j]['nbeamId'] = 103;
					}
				}
				for (var i = 0; i < responses[1].colors.length; i++) {
					responses[1].colors[i].color = '#' + window.parent.layerManageData.conventionalSignsData.targetChooseTableData[val][i].color;
				}
				window.parent.showRoadPointsData = responses;
				window.parent.showRoadPointsDataFunc();
			} else {
				var ress = parent.getTLI2QBRRequestParam();
				var contents = {
					testLogItemIds : ress.testLogItemIds,
					indexType : indexType,
					'f' : "text"
				};
				var Requests = esri.request({
					url : ress.requestUrl,
					timeout : 400000,
					content : contents,
					handleAs : "json",
					callbackParamName : "callback"
				});
				Requests.then(function(res) {
					if (res != null) {
						for (var i = 0; i < res[0].length; i++) {
							for (var j = 0; j < res[0][i][1].length; j++) {
								res[0][i][1][j]['beamId'] = 100 + j;
								res[0][i][1][j]['nbeamId'] = 103;
							}
						}
						for (var i = 0; i < res[0].length; i++) {
							var key = "#83827C";
							res[0][i].push(key);
						}
						for (var i = 0; i < responses[0].length; i++) {
							res[0].push(responses[0][i]);
						}
						for (var i = 0; i < res[1].colors.length; i++) {
							res[1].colors[i].color = '#' + window.parent.layerManageData.conventionalSignsData.targetChooseTableData[val][i].color;
						}
						pointColorVal = val;
						window.parent.showRoadPointsData = res;
						window.parent.showRoadPointsDataFunc();
					}
				});
			}
		}
	});
}

var pointColorVal = null;

//根据制式显示不同图层
function netTypeLayer(cellShowFlag, colorsValue) {
	var n = CellLayers.length;
	if (n > 0) {
		for (var i = 0; i < n; i++) {
			var colors = [];
			map.removeLayer(CellLayers[i]);

			var showflag = false;
			if (cellShowFlag != null) {
				if (F4ShpName != null && F4ShpName.length > 0) { //有4g小区图层，并把图层名赋给F4ShpName
					for (var m = 0; m < F4ShpName.length; m++) {
						if (cellShowFlag == 45 && F4ShpName[m] == CellLayers[i].name) {
							showflag = true;
							colors = colorsValue.colors4g;
							continue;
						} else if (cellShowFlag == 45 && F4ShpName[m] != CellLayers[i].name) {
							showflag = true;
							colors = colorsValue.colors5g;
						}

						if (cellShowFlag == 4 && F4ShpName[m] == CellLayers[i].name) {
							showflag = true;
							colors = colorsValue.colors4g;
						} else if (cellShowFlag == 5 && F4ShpName[m] != CellLayers[i].name) {
							showflag = true;
							colors = colorsValue.colors5g;
						}
					}
				} else { //假设无4g小区图层情况
					if (cellShowFlag == 45 || cellShowFlag == 5) {
						showflag = true;
						colors = colorsValue.colors5g;
					} else {
						showflag = false;
						colors = colorsValue.colors4g;
					}
				}
			}
			var defaultSym = new esri.symbol.SimpleFillSymbol("solid",
				null, new dojo.Color([ 255, 0, 255, 0.75 ]));
			var renderer = new esri.renderer.ClassBreaksRenderer(
				defaultSym, "MODEL3");

			renderer
				.addBreak(0, 0.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[0]));
			renderer
				.addBreak(0.9, 1.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[1]));
			renderer
				.addBreak(1.9, 2.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[2]));
			renderer
				.addBreak(2.9, 4, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[3]));
			CellLayers[i].setRenderer(renderer);
			CellLayers[i].setVisibility(showflag);
			map.addLayer(CellLayers[i]);
		}
	} else {
		return;
	}
}

var F4ShpName = null;
var fgFontColorSelected = null;
var fgFontSizeSelected = "11";
var fgUnderlineTypeSelected;
var fgUnderlineColorSelected;
var LTEFontColorSelected = null;
var LTEFontSizeSelected = "13";
var cellLayerSelect = null;
//图层管理  加载小区图层
//bl表示小区是否居中显示 cellShowFlag:是否显示小区图层 colors:颜色集合,cellRange:透明度,showTargetOption:选中的标签集合,showTargetFlag:是否显示标注
function LayerManageAddCellbyArray(cellShowFlag, colors, cellRange, showTargetOption, showTargetFlag) {
	/*	LayerManagePCI(colors);*/

	document.getElementById('label_CellsLayer').disabled = !cellShowFlag;
	netTypeLayer(cellShowFlag, colors);
	//是否显示小区图层
	var n = CellLayers.length;

	if (n > 0) {
		var str = "";
		if (showTargetOption.length > 0) {
			var options = showTargetOption.split(',');
			for (var i = 0; i < options.length; i++) {
				if (i != 0) {
					str = str + ',';
				}
				str = str + options[i] + ":{" + options[i] + "}";
			}
		}
		var pointColor = "#000000";
		var fontSize = "11pt";
		if (cellLayerSelect == null || cellLayerSelect == 5 || cellLayerSelect == 45) {
			if (fgFontColorSelected) {
				fontSize = fgFontSizeSelected + "pt";
				if (fgFontColorSelected == '红色') {
					pointColor = "#FF0012";
				} else if (fgFontColorSelected == '黑色') {
					pointColor = "#000000";
				} else if (fgFontColorSelected == '蓝色') {
					pointColor = "#0A00FF";
				} else if (fgFontColorSelected == '绿色') {
					pointColor = "#00FF00";
				}
			}
		} else if (cellLayerSelect == 4) {
			if (LTEFontColorSelected) {
				fontSize = LTEFontSizeSelected + "pt";
				if (LTEFontColorSelected == '红色') {
					pointColor = "#FF0012";
				} else if (LTEFontColorSelected == '黑色') {
					pointColor = "#000000";
				} else if (LTEFontColorSelected == '蓝色') {
					pointColor = "#0A00FF";
				} else if (LTEFontColorSelected == '绿色') {
					pointColor = "#00FF00";
				}
			}
		}

		for (var i = 0; i < n; i++) {
			var sym = new esri.symbol.TextSymbol();
			sym.font.setSize(fontSize);
			sym.font.setFamily("arial");
			sym.setColor(new dojo.Color(pointColor));
			var jso = {
				"labelExpressionInfo" : {
					"value" : str
				},
				"labelPlacement" : "always-horizontal"
			}
			var LabelCell = new esri.layers.LabelClass(jso);
			LabelCell.symbol = sym;
			CellLayers[i].setLabelingInfo([ LabelCell ]);
			CellLayers[i].setShowLabels(showTargetFlag);
		}
	}

	window.parent.ShowTrackRender(); //是否展示测试数据(采样点)图层

}

//图层管理  加载小区图层
//bl表示小区是否居中显示 cellShowFlag:是否显示小区图层 colors:颜色集合,cellRange:透明度,showTargetOption:选中的标签集合,showTargetFlag:是否显示标注
function LayerManageAddCellbyArray2(cellShowFlag, colors, cellRange, showTargetOption, showTargetFlag) {
	//	LayerManagePCI(colors);

	document.getElementById('label_CellsLayer').disabled = !cellShowFlag;
	//是否显示小区图层
	netTypeLayer(cellShowFlag, colors);
	var n = CellLayers.length;
	//	netTypeLayer(cellShowFlag);
	/*if (n > 0) {
		for (var i = 0; i < n; i++) {
			CellLayers[i].setVisibility(cellShowFlag);
		}
	}*/

	if (n > 0) {
		var str = "";
		var options = showTargetOption.split(',');
		for (var i = 0; i < options.length; i++) {
			if (i != 0) {
				str = str + ',';
			}
			str = str + options[i] + ":{" + options[i] + "}";
		}
		var pointColor = "#000000";
		if (fgFontColorSelected) {
			if (fgFontColorSelected == '红色') {
				pointColor = "#FF0012";
			} else if (fgFontColorSelected == '黑色') {
				pointColor = "#000000";
			} else if (fgFontColorSelected == '蓝色') {
				pointColor = "#0A00FF";
			} else if (fgFontColorSelected == '绿色') {
				pointColor = "#00FF00";
			}
		}

		for (var i = 0; i < n; i++) {
			var sym = new esri.symbol.TextSymbol();
			sym.font.setSize(fgFontSizeSelected + "pt");
			sym.font.setFamily("arial");
			sym.setColor(new dojo.Color(pointColor));
			var jso = {
				"labelExpressionInfo" : {
					"value" : str
				},
				"labelPlacement" : "always-horizontal"
			}
			var LabelCell = new esri.layers.LabelClass(jso);
			LabelCell.symbol = sym;
			CellLayers[i].setLabelingInfo([ LabelCell ]);
			CellLayers[i].setShowLabels(showTargetFlag);
		}
	}

	window.parent.ShowTrackRender(); //是否展示测试数据(采样点)图层

}

//小区(PCI)渲染
function LayerManagePCI(colors) {
	var n = CellLayers.length;
	if (n > 0) {
		for (var i = 0; i < n; i++) {
			map.removeLayer(CellLayers[i]);
			var defaultSym = new esri.symbol.SimpleFillSymbol("solid",
				null, new dojo.Color([ 255, 0, 255, 0.75 ]));
			var renderer = new esri.renderer.ClassBreaksRenderer(
				defaultSym, "MODEL3");
			renderer
				.addBreak(0, 0.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[0]));
			renderer
				.addBreak(0.9, 1.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[1]));
			renderer
				.addBreak(1.9, 2.9, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[2]));
			renderer
				.addBreak(2.9, 4, new esri.symbol.SimpleFillSymbol(
					"solid", null, colors[3]));
			CellLayers[i].setRenderer(renderer);
			map.addLayer(CellLayers[i]);
		}
	} else {
		return;
	}
}

var NcellList4g;
var NcellList5g;
var nCellToCellLineData;
var nCellToCellLineColor;
//小区和小区连线  格式:[{'cellId':'','nCellId':'','top':1}]
function LayerManageShowCellToCell(colors, netType) {
	/*var res = parent.getCWBRCellToCellRequestParam();
	var content = {
		badRoadId : 4,
		'f' : "text"
	};
	var Request = esri.request({
		url : res.requestUrl,
		timeout : 400000,
		content : content,
		handleAs : "json",
		callbackParamName : "callback"
	});
	Request.then(function(re) {
		if (re != null) {
			//ShowCellToCell(re);
			console.log('------------------------邻区拉线---------------------------');
			console.log(re);
		}
	});*/

	var res = [
		{
			cellId : '110493873',
			nbCellId : '58084395',
			top : 1
		},
		{
			cellId : '110493873',
			nbCellId : '55984170',
			top : 2
		},
		{
			cellId : '110493873',
			nbCellId : '55968553',
			top : 3
		},
		{
			cellId : '110493873',
			nbCellId : '58089002',
			top : 2
		},
		{
			cellId : '58084395',
			nbCellId : '6749225',
			top : 4
		},
		{
			cellId : '58084395',
			nbCellId : '6709290',
			top : 5
		},
		{
			cellId : '58084395',
			nbCellId : '55972650',
			top : 6
		},
		{
			cellId : '58084395',
			nbCellId : '108422578',
			top : 2
		},
		{
			cellId : '55968297',
			nbCellId : '6709289',
			top : 3
		},
		{
			cellId : '55968297',
			nbCellId : '108404907',
			top : 3
		},
		{
			cellId : '55968297',
			nbCellId : '109502636',
			top : 3
		},
		{
			cellId : '55968297',
			nbCellId : '55996715',
			top : 3
		},
		{
			cellId : '55968297',
			nbCellId : '59153707',
			top : 3
		}
	];
	var res;
	/*if(netType){
		res = netType;
	}*/
	if (undefined == CellToCellGraphicsLayer || null == CellToCellGraphicsLayer) {
		CellToCellGraphicsLayer = new esri.layers.GraphicsLayer();
		map.addLayer(CellToCellGraphicsLayer);
	} else {
		CellToCellGraphicsLayer.clear();
	}

	nCellToCellLineData = res;
	nCellToCellLineColor = colors;

	for (var i = 0; i < res.length; i++) {
		if (res[i].cellId == res[i].nbCellId) {
			return;
		}
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;
		Query.where = "CELLID='" + res[i].cellId + "' OR CELLID='"
			+ res[i].nbCellId + "'";
		CellLayers[0].queryFeatures(Query, function(geometry) {
			var geo = geometry.features[0].geometry;
			var geo1 = geometry.features[1].geometry;
			var t = geometry.features[0].attributes.MODEL3;
			var clc = getCellToCellLineColor(geometry.features[0].attributes.CELLID, geometry.features[1].attributes.CELLID);
			var pt = new esri.geometry.Point(geo.getExtent().getCenter().getLongitude(), geo.getExtent().getCenter().getLatitude(), wgs);
			var p1 = new esri.geometry.Point(geo1.getExtent().getCenter().getLongitude(), geo1.getExtent().getCenter().getLatitude(), wgs);
			var line = new esri.geometry.Polyline(wgs);
			line.addPath([ pt, p1 ]);
			mapDataExtent = line.getExtent();
			map.setExtent(mapDataExtent);
			var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
			var GpsHistorySymbol = new esri.Graphic(line, GpsLineSymbol, null, null);
			CellToCellGraphicsLayer.add(GpsHistorySymbol);
		}, function(err) {
			alert(err);
			return;
		});
	}



}

//获取小区和邻区连线颜色
function getCellToCellLineColor(cellId, ncellId) {
	for (var i = 0; i < nCellToCellLineData.length; i++) {
		if ((nCellToCellLineData[i].cellId == cellId && nCellToCellLineData[i].nbCellId == ncellId)
			|| (nCellToCellLineData[i].cellId == ncellId && nCellToCellLineData[i].nbCellId == cellId)) {
			return nCellToCellLineColor[nCellToCellLineData[i].top];
		}
	}
	return "#000000";
}

var LayerManageShowEventData;
/**
 * 绘制事件
 */
function LayerManageShowEventTrackRender(res) {
	if (undefined == EventTrackGraphicsLayer || null == EventTrackGraphicsLayer) {
		EventTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		EventTrackGraphicsLayer.clear();
	}
	if (undefined == EventTrackLabelLayer || null == EventTrackLabelLayer) {
		EventTrackLabelLayer = new esri.layers.GraphicsLayer();
	} else {
		EventTrackLabelLayer.clear();
	}
	var data = res;
	if (data.length == 0) {
		return;
	}
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		for (var j = 0, n = d.length; j < n; j++) {
			var icon = d[j].iconType;
			var pt = new esri.geometry.Point(d[j].longitude, d[j].latitude, wgs);
			var symbol = new esri.symbol.PictureMarkerSymbol({
				"url" : "images/" + icon + ".png",
				"type" : "esriPMS"
			});
			symbol.setHeight(26);
			symbol.setWidth(26);
			var startGraphic = new esri.Graphic(pt, symbol, null, null);
			EventTrackGraphicsLayer.add(startGraphic);
			var textsym = new esri.symbol.TextSymbol(icon, null,
				new dojo.Color([ 0, 0, 255, 1 ]));
			textsym.setOffset(2, 4);
			var font = new esri.symbol.Font();
			font.setSize("12px");
			textsym.setFont(font);
			textsym.setHaloColor(new dojo.Color([ 255, 255, 255, 1 ]));
			textsym.setHaloSize(1);
			var labelGraphic = new esri.Graphic(pt, textsym);
			EventTrackLabelLayer.add(labelGraphic);
		}
	}
	EventTrackLabelLayer.setVisibility(true);
	map.addLayer(EventTrackGraphicsLayer);
	map.addLayer(EventTrackLabelLayer);
}

function tableClickFunc(data, lon, lat, event) {
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	map.addLayer(GPSTrackGraphicsLayer);
	var colorArry = {};
	if (window.parent.layerManageData.layerChooseData) {
		//data = window.parent.layerManageData.conventionalSignsData;
		colorArry = window.parent.layerManageData;
		for (var i = 0; i < colorMapEtgTral.rsrp.length; i++) {
			colorMapEtgTral.rsrp[i].color = colorArry.conventionalSignsData.targetChooseTableData.rsrp[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.sinr.length; i++) {
			colorMapEtgTral.sinr[i].color = colorArry.conventionalSignsData.targetChooseTableData.sinr[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.beam.length; i++) {
			colorMapEtgTral.beam[i].color = colorArry.conventionalSignsData.targetChooseTableData.beam[i].color;
		}
		var sampleTypeData = colorArry.conventionalSignsData.sampleTypeData;
		var pointStyle = "Circle";
		for (var i = 0; i < sampleTypeData.length; i++) {
			if (sampleTypeData[i].selected == true) {
				pointStyle = sampleTypeData[i].value;
			}
		}
		switch (pointStyle) {
		case "Circle":
			style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
			break;
		case "Square":
			style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
			break;
		}
	} else {
		style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
	}

	var color = null;
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var cc = '#FF0012';
		if (event == "nrRsrp") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, event);
				color = colorMapEtgTral.rsrp;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[event]));
		} else if (event == "lteRsrp") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, event);
				color = colorMapEtgTral.rsrp;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[event]));
		} else if (event == "nrSinr") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, event);
				color = colorMapEtgTral.sinr;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.sinr, parseFloat(d[event]));
		} else if (event == "lteSinr") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, event);
				color = colorMapEtgTral.sinr;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.sinr, parseFloat(d[event]));
		} else if (event == "beamSum") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.beam, event);
				color = colorMapEtgTral.beam;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.beam, parseFloat(d[event]));
		}
		if (cc == "") {
			cc = '#FF0000';
		}
		var pt = new esri.geometry.Point(d.longtitude, d.latitude, wgs);
		var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, cc);
		var startGraphic = new esri.Graphic(pt, symbol, {
			"nrCellid" : d.nrCellid,
			"nrPCI" : d.nrPci,
			"nrRsrp" : d.nrRsrp,
			"lteCellid" : d.lteCellid,
			"ltePCI" : d.ltePci,
			"lteRsrp" : d.lteRsrp,
			"nrCellNumber" : d.recseqno,
			"nrNcellNumber1" : d.nrNcellnumber1,
			"nrNcellNumber2" : d.nrNcellnumber2,
			"nrNcellNumber3" : d.nrNcellnumber3,
			"nrNcellNumber4" : d.nrNcellnumber4,
			"nrNcellNumber5" : d.nrNcellnumber5,
			"nrNcellNumber6" : d.nrNcellnumber6,
			"nrNcellNumber7" : d.nrNcellnumber7,
			"nrNcellNumber8" : d.nrNcellnumber8,
		}, null);
		GPSTrackGraphicsLayer.add(startGraphic);
	/*var pt = new esri.geometry.Point(d.longitude, d.latitude,wgs);
	var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null,cc);
	var startGraphic = new esri.Graphic(pt, symbol, {}, null);
	GPSTrackGraphicsLayer.add(startGraphic);*/
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>CELLID:</td> " +
		"<td style='text-align:left;'>${nrCellid}</td> " +
		"<td style='text-align:left;'>NR PCI:</td> " +
		"<td style='text-align:left;'>${nrPCI}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>占用波束编号:</td><td style='text-align:left;'>${nrCellNumber}</td> " +
		"<td style='text-align:left;'>占用波束RSRP:</td><td style='text-align:left;'>${nrRsrp}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区1波束:</td><td style='text-align:left;'>${nrNcellNumber1}</td> " +
		"<td style='text-align:left;'>邻小区2波束:</td><td style='text-align:left;'>${nrNcellNumber2}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区3波束:</td><td style='text-align:left;'>${nrNcellNumber3}</td> " +
		"<td style='text-align:left;'>邻小区4波束:</td><td style='text-align:left;'>${nrNcellNumber4}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区5波束:</td><td style='text-align:left;'>${nrNcellNumber5}</td> " +
		"<td style='text-align:left;'>邻小区6波束:</td><td style='text-align:left;'>${nrNcellNumber6}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区7波束:</td><td style='text-align:left;'>${nrNcellNumber7}</td> " +
		"<td style='text-align:left;'>邻小区8波束:</td><td style='text-align:left;'>${nrNcellNumber8}</td> " +
		"</tr> " +
		"</table>");
	GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);

	if (window.parent.layerManageData.layerChooseData) {
		if (window.parent.layerManageData.line.cellCoverThreshold.cellLinkColor.value == '1') {
			for (var z = 0; z < color.length; z++) {
				color[z].color = '#' + window.parent.layerManageData.line.cellCoverThreshold.cellLinkColor.cellLinkColorInput;
			}
		}
	}

	CellLayers[0].on("click", function(eventParam) {
		var ima = document.getElementById('DisplayCleLPoint');
		//ima.src = "images/DisplayCleLPoint_hide.png";

		if (undefined == CellLineConGraphicsLayer
			|| null == CellLineConGraphicsLayer) {
			CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
			map.addLayer(CellLineConGraphicsLayer);
		} else {
			CellLineConGraphicsLayer.clear();
		}
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;
		console.log("====================1===============");
		console.log(eventParam.graphic.attributes);
		var clc = $("#SelectCellColorLine").find("option:selected").val();
		Query.where = "PCI='" + eventParam.graphic.attributes.PCI + "'";
		var pointgraphics = GPSTrackGraphicsLayer.graphics;
		var cc = [];
		for (var ii = 0; ii < pointgraphics.length; ii++) {
			if (pointgraphics[ii].attributes.nrPCI == eventParam.graphic.attributes.PCI) {
				cc.push({
					indexValue : pointgraphics[ii].attributes[event],
					longitude : pointgraphics[ii].geometry.x,
					latitude : pointgraphics[ii].geometry.y
				});
			}
		}
		embbEtgTralQureyResults(cc, Query, clc, color);
	});
	CellLayers[1].on("click", function(eventParam) {
		var ima = document.getElementById('DisplayCleLPoint');
		//ima.src = "images/DisplayCleLPoint_hide.png";

		if (undefined == CellLineConGraphicsLayer
			|| null == CellLineConGraphicsLayer) {
			CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
			map.addLayer(CellLineConGraphicsLayer);
		} else {
			CellLineConGraphicsLayer.clear();
		}
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;
		var clc = $("#SelectCellColorLine").find("option:selected").val();
		Query.where = "PCI='" + eventParam.graphic.attributes.PCI + "'";
		var pointgraphics = GPSTrackGraphicsLayer.graphics;
		var cc = [];
		for (var ii = 0; ii < pointgraphics.length; ii++) {
			if (pointgraphics[ii].attributes.ltePCI == eventParam.graphic.attributes.PCI) {
				cc.push({
					indexValue : pointgraphics[ii].attributes[event],
					longitude : pointgraphics[ii].geometry.x,
					latitude : pointgraphics[ii].geometry.y
				});
			}
		}
		embbEtgTralQureyResults(cc, Query, clc, color);
	});

	//GPSTrackGraphicsLayer.emit("click");
	GPSTrackGraphicsLayer.on("click", function(eventParam) {
		var ima = document.getElementById('DisplayCleLPoint');
		//ima.src = "images/DisplayCleLPoint_hide.png";
		//		console.log(eventParam.graphic.attributes);
		//		console.log(eventParam.graphic.geometry);
		//		console.log(eventParam.graphic.geometry.x);
		//		console.log(eventParam.graphic.geometry.y);
		var cc = [ {
			indexValue : eventParam.graphic.attributes[event],
			longitude : eventParam.graphic.geometry.x,
			latitude : eventParam.graphic.geometry.y
		} ];

		if (undefined == CellLineConGraphicsLayer
			|| null == CellLineConGraphicsLayer) {
			CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
			map.addLayer(CellLineConGraphicsLayer);
		} else {
			CellLineConGraphicsLayer.clear();
		}

		var clc = $("#SelectCellColorLine").find("option:selected").val();
		//采样点与主小区连线
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;
		Query.where = "PCI='" + eventParam.graphic.attributes.nrPCI + "'";
		embbEtgTralQureyResults(cc, Query, clc, color);
		/*//采样点与邻小区连线
		if(eventParam.graphic.attributes.nrNcellNumber1){
			var Query1 = new esri.tasks.Query();
			Query1.returnGeometry = true;
			Query1.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber1 + "'";
			embbEtgTralQureyResults(cc, Query1, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber2){
			var Query2 = new esri.tasks.Query();
			Query2.returnGeometry = true;
			Query2.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber2 + "'";
			embbEtgTralQureyResults(cc, Query2, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber3){
			var Query3 = new esri.tasks.Query();
			Query3.returnGeometry = true;
			Query3.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber3 + "'";
			embbEtgTralQureyResults(cc, Query3, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber4){
			var Query4 = new esri.tasks.Query();
			Query4.returnGeometry = true;
			Query4.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber4 + "'";
			embbEtgTralQureyResults(cc, Query4, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber5){
			var Query5 = new esri.tasks.Query();
			Query5.returnGeometry = true;
			Query5.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber5 + "'";
			embbEtgTralQureyResults(cc, Query5, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber6){
			var Query6 = new esri.tasks.Query();
			Query6.returnGeometry = true;
			Query6.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber6 + "'";
			embbEtgTralQureyResults(cc, Query6, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber7){
			var Query7 = new esri.tasks.Query();
			Query7.returnGeometry = true;
			Query7.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber7 + "'";
			embbEtgTralQureyResults(cc, Query7, clc, color);
		}
		if(eventParam.graphic.attributes.nrNcellNumber8){
			var Query8 = new esri.tasks.Query();
			Query8.returnGeometry = true;
			Query8.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber8 + "'";
			embbEtgTralQureyResults(cc, Query8, clc, color);
		}*/

	});

	//设置中心经纬度
	var inPoint = new esri.geometry.Point(parseFloat(lon), parseFloat(lat), wgs);
	map.centerAndZoom(inPoint, 15);
}

function mapCenterAndZoom(lon, lat) {
	var inPoint = new esri.geometry.Point(parseFloat(lon), parseFloat(lat), wgs);
	map.centerAndZoom(inPoint, 15);
}

function mapCenterAndZoom2(lon, lat) {
	var inPoint = new esri.geometry.Point(parseFloat(lon), parseFloat(lat), wgs);
	map.centerAndZoom(inPoint, 20);
}

function stationEtgTralFunc(data, event) {
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	map.addLayer(GPSTrackGraphicsLayer);
	if (data == null || data.length < 1) {
		return;
	}
	style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var cc = new esri.Color([ 255, 0, 0, 0.75 ]);
		if (event == "nrRsrp") {
			if (d[event] == null) {
				continue;
			} else if (parseFloat(d[event]) < -110) {
				cc = new esri.Color([ 255, 0, 0, 0.75 ]);
			} else if (-110 <= parseFloat(d[event]) && parseFloat(d[event]) < -105) {
				cc = new esri.Color([ 250, 191, 143, 0.75 ]);
			} else if (-105 <= parseFloat(d[event]) && parseFloat(d[event]) < -100) {
				cc = new esri.Color([ 255, 255, 0, 0.75 ]);
			} else if (-100 <= parseFloat(d[event]) && parseFloat(d[event]) < -95) {
				cc = new esri.Color([ 149, 179, 215, 0.75 ]);
			} else if (-95 <= parseFloat(d[event]) && parseFloat(d[event]) < -85) {
				cc = new esri.Color([ 84, 141, 212, 0.75 ]);
			} else if (-85 <= parseFloat(d[event]) && parseFloat(d[event]) < -75) {
				cc = new esri.Color([ 146, 208, 80, 0.75 ]);
			} else if (-75 <= parseFloat(d[event]) && parseFloat(d[event]) <= -45) {
				cc = new esri.Color([ 0, 176, 80, 0.75 ]);
			}
		} else if (event == "nrSinr") {
			if (d[event] == null) {
				continue;
			} else if (parseFloat(d[event]) < -3) {
				cc = new esri.Color([ 255, 0, 0, 0.75 ]);
			} else if (-3 <= parseFloat(d[event]) && parseFloat(d[event]) < 0) {
				cc = new esri.Color([ 250, 191, 143, 0.75 ]);
			} else if (0 <= parseFloat(d[event]) && parseFloat(d[event]) < 3) {
				cc = new esri.Color([ 255, 255, 0, 0.75 ]);
			} else if (3 <= parseFloat(d[event]) && parseFloat(d[event]) < 6) {
				cc = new esri.Color([ 149, 179, 215, 0.75 ]);
			} else if (6 <= parseFloat(d[event]) && parseFloat(d[event]) < 9) {
				cc = new esri.Color([ 84, 141, 212, 0.75 ]);
			} else if (9 <= parseFloat(d[event]) && parseFloat(d[event]) < 15) {
				cc = new esri.Color([ 146, 208, 80, 0.75 ]);
			} else if (15 <= parseFloat(d[event])) {
				cc = new esri.Color([ 0, 176, 80, 0.75 ]);
			}
		} else if (event == "nrMacthrputdl") {
			if (d[event] == null) {
				continue;
			} else if (0 < parseFloat(d[event]) && parseFloat(d[event]) < 40) {
				cc = new esri.Color([ 255, 0, 0, 0.75 ]);
			} else if (40 <= parseFloat(d[event]) && parseFloat(d[event]) < 120) {
				cc = new esri.Color([ 250, 191, 143, 0.75 ]);
			} else if (120 <= parseFloat(d[event]) && parseFloat(d[event]) < 400) {
				cc = new esri.Color([ 255, 255, 0, 0.75 ]);
			} else if (400 <= parseFloat(d[event]) && parseFloat(d[event]) < 800) {
				cc = new esri.Color([ 149, 179, 215, 0.75 ]);
			} else if (800 <= parseFloat(d[event]) && parseFloat(d[event]) < 1200) {
				cc = new esri.Color([ 84, 141, 212, 0.75 ]);
			} else if (1200 <= parseFloat(d[event]) && parseFloat(d[event]) < 1600) {
				cc = new esri.Color([ 146, 208, 80, 0.75 ]);
			} else if (1600 <= parseFloat(d[event])) {
				cc = new esri.Color([ 0, 176, 80, 0.75 ]);
			}
		} else if (event == "nrMacthrputul") {
			if (d[event] == null) {
				continue;
			} else if (d[event] == null) {
				cc = new esri.Color([ 255, 0, 0, 0.75 ]);
			} else if (0 < parseFloat(d[event]) && parseFloat(d[event]) < 10) {
				cc = new esri.Color([ 255, 0, 0, 0.75 ]);
			} else if (10 <= parseFloat(d[event]) && parseFloat(d[event]) < 20) {
				cc = new esri.Color([ 250, 191, 143, 0.75 ]);
			} else if (20 <= parseFloat(d[event]) && parseFloat(d[event]) < 30) {
				cc = new esri.Color([ 255, 255, 0, 0.75 ]);
			} else if (30 <= parseFloat(d[event]) && parseFloat(d[event]) < 35) {
				cc = new esri.Color([ 149, 179, 215, 0.75 ]);
			} else if (35 <= parseFloat(d[event]) && parseFloat(d[event]) < 40) {
				cc = new esri.Color([ 84, 141, 212, 0.75 ]);
			} else if (40 <= parseFloat(d[event]) && parseFloat(d[event]) < 200) {
				cc = new esri.Color([ 146, 208, 80, 0.75 ]);
			} else if (200 <= parseFloat(d[event])) {
				cc = new esri.Color([ 0, 176, 80, 0.75 ]);
			}
		}
		var pt = new esri.geometry.Point(d.longtitude, d.latitude, wgs);
		var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, cc);
		var startGraphic = new esri.Graphic(pt, symbol, {
			"nrCellid" : d.nrCellid,
			"nrPCI" : d.nrPCI,
			"nrCellNumber" : d.nrCellNumber,
			"nrRsrp" : d.nrRsrp,
			"nrNcellNumber1" : d.nrNcellNumber1,
			"nrNcellNumber2" : d.nrNcellNumber2,
			"nrNcellNumber3" : d.nrNcellNumber3,
			"nrNcellNumber4" : d.nrNcellNumber4,
			"nrNcellNumber5" : d.nrNcellNumber5,
			"nrNcellNumber6" : d.nrNcellNumber6,
			"nrNcellNumber7" : d.nrNcellNumber7,
			"nrNcellNumber8" : d.nrNcellNumber8
		}, null);
		GPSTrackGraphicsLayer.add(startGraphic);
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>CELLID:</td> " +
		"<td style='text-align:left;'>${nrCellid}</td> " +
		"<td style='text-align:left;'>PCI:</td> " +
		"<td style='text-align:left;'>${nrPCI}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>占用波束编号:</td><td style='text-align:left;'>${nrCellNumber}</td> " +
		"<td style='text-align:left;'>占用波束RSRP:</td><td style='text-align:left;'>${nrRsrp}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区1波束:</td><td style='text-align:left;'>${nrNcellNumber1}</td> " +
		"<td style='text-align:left;'>邻小区2波束:</td><td style='text-align:left;'>${nrNcellNumber2}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区3波束:</td><td style='text-align:left;'>${nrNcellNumber3}</td> " +
		"<td style='text-align:left;'>邻小区4波束:</td><td style='text-align:left;'>${nrNcellNumber4}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区5波束:</td><td style='text-align:left;'>${nrNcellNumber5}</td> " +
		"<td style='text-align:left;'>邻小区6波束:</td><td style='text-align:left;'>${nrNcellNumber6}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区7波束:</td><td style='text-align:left;'>${nrNcellNumber7}</td> " +
		"<td style='text-align:left;'>邻小区8波束:</td><td style='text-align:left;'>${nrNcellNumber8}</td> " +
		"</tr> " +
		"</table>");
	GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);



	//设置中心经纬度
	var lon = parseFloat(data[0].longtitude);
	var lat = parseFloat(data[0].latitude);
	timeoutFlag2 = setInterval(function() {
		clearInterval(timeoutFlag2);
		mapCenterAndZoom(lon, lat);
	}, 3000);
}

function stationEtgTralFunc2(data, event, saveimg, lon, lat, pciMap) {
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	map.graphics.clear();
	map.addLayer(GPSTrackGraphicsLayer);
	if (data == null || data.length < 1) {
		if (saveimg) {
			window.parent.downloadImagejie(null);
		}
		return;
	}

	if (window.parent.layerManageData != undefined && window.parent.layerManageData.layerChooseData) {
		//data = window.parent.layerManageData.conventionalSignsData;
		var colorArry = {};
		colorArry = window.parent.layerManageData;
		for (var i = 0; i < colorMapEtgTral.rsrp.length; i++) {
			colorMapEtgTral.rsrp[i].color = colorArry.conventionalSignsData.targetChooseTableData.rsrp[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.sinr.length; i++) {
			colorMapEtgTral.sinr[i].color = colorArry.conventionalSignsData.targetChooseTableData.sinr[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.macthrputdl.length; i++) {
			colorMapEtgTral.macthrputdl[i].color = colorArry.conventionalSignsData.targetChooseTableData.macthrputdl[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.macthrputul.length; i++) {
			colorMapEtgTral.macthrputul[i].color = colorArry.conventionalSignsData.targetChooseTableData.macthrputul[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.macthrputul.length; i++) {
			colorMapEtgTral.pci[i].color = colorArry.conventionalSignsData.targetChooseTableData.pci[i].color;
		}
		var sampleTypeData = colorArry.conventionalSignsData.sampleTypeData;
		var pointStyle = "Circle";
		for (var i = 0; i < sampleTypeData.length; i++) {
			if (sampleTypeData[i].selected == true) {
				pointStyle = sampleTypeData[i].value;
			}
		}
		switch (pointStyle) {
		case "Circle":
			style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
			break;
		case "Square":
			style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
			break;
		}
	} else {
		style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
	}

	if (colorMapEtgTral == null || colorMapEtgTral == undefined) {
		if (saveimg) {
			window.parent.downloadImagejie(null);
		}
		return;
	}

	var opacity = 0.75;

	var pciArry = pciMap;
	var color = null;
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var cc = [ 255, 0, 0, 0.75 ];
		if (event.indexOf("NR RSRP") != -1) {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, "RSRP");
			}
			var kpiName = "nrRsrp";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[kpiName]));
		} else if (event.indexOf("LTE RSRP") != -1) {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, "RSRP");
			}
			var kpiName = "lteRsrp";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[kpiName]));
		} else if (event.indexOf("NR SINR") != -1) {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, "SINR");
			}
			var kpiName = "nrSinr";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.sinr, parseFloat(d[kpiName]));
		} else if (event.indexOf("LTE SINR") != -1) {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, "SINR");
			}
			var kpiName = "lteSinr";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.sinr, parseFloat(d[kpiName]));
		} else if (event == "NR PHY Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "nrPhyThrUL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "NR PHY Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "nrPhyThrDL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "NR MAC Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "nrMacthrputul";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "NR MAC Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "nrMacthrputdl";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "NR RLC Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "nrRlcThrUL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "NR RLC Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "nrRlcThrDL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "NR PDCP Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "nrPdcpThrUL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "NR PDCP Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "nrPdcpThrDL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "NR SDAP Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "nrSdcpThrUL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "NR SDAP Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "nrSdcpThrDL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "LTE PDCP Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "ltePdcpThrUL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "LTE PDCP Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "ltePdcpThrDL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "LTE RLC Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "lteRlcThrUL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "LTE RLC Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "lteRlcThrDL";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "LTE MAC Thr UL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "lteMacthrputul";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "LTE MAC Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "lteMacthrputdl";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "LTE PHY Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, event);
			}
			var kpiName = "ltePhyThrUl";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputul, parseFloat(d[kpiName]));
		} else if (event == "LTE PHY Thr DL(M)") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, event);
			}
			var kpiName = "ltePhyThrDl";
			if (d[kpiName] == null) {
				continue;
			}
			color = ColorValue(colorMapEtgTral.macthrputdl, parseFloat(d[kpiName]));
		} else if (event == "NR PCI") {
			var kpiName = "nrPci";
			if (d[kpiName] == null) {
				continue;
			}
			var pciValue = d[kpiName];
			if (pciArry[pciValue].color == null) {
				var pciIndex = pciArry[pciValue].pciIndex;
				pciArry[pciValue].color = colorMapEtgTral.pci[pciIndex].color;
				color = pciArry[pciValue].color;
				//对5G小区进行pci颜色渲染
				pciToCellRender(pciArry[pciValue].cellName, color);
			} else {
				color = pciArry[pciValue].color;
			}
		} else if (event == "LTE PCI") {
			var kpiName = "ltePci";
			if (d[kpiName] == null) {
				continue;
			}
			var pciValue = d[kpiName];
			if (pciArry[pciValue].color == null) {
				var pciIndex = pciArry[pciValue].pciIndex;
				pciArry[pciValue].color = colorMapEtgTral.pci[pciIndex].color;
				color = pciArry[pciValue].color;
			} else {
				color = pciArry[pciValue].color;
			}
		}

		if (color != null) {
			color = color.colorRgb();
			color = RgbToRgba(color, opacity);
			cc = color.replace(/(?:||rgba|RGBA)*/g, "").replace(")", "").replace(
				"(", "").split(",");
		}
		var pt = new esri.geometry.Point(d.longtitude, d.latitude, wgs);
		var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, new esri.Color(cc));
		var startGraphic = new esri.Graphic(pt, symbol, {
			"nrCellid" : d.nrCellid,
			"nrPCI" : d.nrPci,
			"nrCellNumber" : d.nrCellNumber,
			"nrRsrp" : d.nrRsrp,
			"nrNcellNumber1" : d.nrNcellNumber1,
			"nrNcellNumber2" : d.nrNcellNumber2,
			"nrNcellNumber3" : d.nrNcellNumber3,
			"nrNcellNumber4" : d.nrNcellNumber4,
			"nrNcellNumber5" : d.nrNcellNumber5,
			"nrNcellNumber6" : d.nrNcellNumber6,
			"nrNcellNumber7" : d.nrNcellNumber7,
			"nrNcellNumber8" : d.nrNcellNumber8
		}, null);
		GPSTrackGraphicsLayer.add(startGraphic);
	}
	data = null;
	//设置pci的图例
	if (event == "NR PCI" || event == "LTE PCI") {
		SetPCiLengend(pciArry);
	}
	if (color == null) {
		if (saveimg) {
			window.parent.downloadImagejie(null);
		}
		return;
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>CELLID:</td> " +
		"<td style='text-align:left;'>${nrCellid}</td> " +
		"<td style='text-align:left;'>PCI:</td> " +
		"<td style='text-align:left;'>${nrPCI}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>占用波束编号:</td><td style='text-align:left;'>${nrCellNumber}</td> " +
		"<td style='text-align:left;'>占用波束RSRP:</td><td style='text-align:left;'>${nrRsrp}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区1波束:</td><td style='text-align:left;'>${nrNcellNumber1}</td> " +
		"<td style='text-align:left;'>邻小区2波束:</td><td style='text-align:left;'>${nrNcellNumber2}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区3波束:</td><td style='text-align:left;'>${nrNcellNumber3}</td> " +
		"<td style='text-align:left;'>邻小区4波束:</td><td style='text-align:left;'>${nrNcellNumber4}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区5波束:</td><td style='text-align:left;'>${nrNcellNumber5}</td> " +
		"<td style='text-align:left;'>邻小区6波束:</td><td style='text-align:left;'>${nrNcellNumber6}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区7波束:</td><td style='text-align:left;'>${nrNcellNumber7}</td> " +
		"<td style='text-align:left;'>邻小区8波束:</td><td style='text-align:left;'>${nrNcellNumber8}</td> " +
		"</tr> " +
		"</table>");
	GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);

	//设置中心经纬度
	timer1 = setTimeout(function() {
		var inPoint = new esri.geometry.Point(parseFloat(lon), parseFloat(lat), wgs);
		map.centerAndZoom(inPoint, 16);
		var timer4 = setTimeout(function() {
			if (saveimg) {
				// 调用打印服务保存图片的printTools()，此方法已经弃用 
				// map指地图的id，使用cancas打印 takeScreenshot("map","");
				takeScreenshot("map", "");
			}
		}, 2000);
	}, 1000);
}

//5G小区根据pci进行颜色渲染，针对单个站点
function pciToCellRender(cellName, color) {
	var opacity = 0.75;
	if (color != null) {
		color = color.colorRgb();
		color = RgbToRgba(color, opacity);
		var colorRgbValue = color.replace(/(?:||rgba|RGBA)*/g, "").replace(")", "").replace(
			"(", "").split(",");
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;

		Query.where = "CELLNAME like '%" + cellName + "%'";
		console.info(cellName);
		showWaitFlag(true);
		CellLayers[0].queryFeatures(Query, function(response) {
			var features = response.features;
			var n = features.length;
			if (n == 0) {
				alert("找不到小区进行pci渲染");
				showWaitFlag(false);
				return;
			}
			map.setZoom(map.__tileInfo.lods.length - 1);
			var sym = new esri.symbol.SimpleFillSymbol("solid", null, new esri.Color(colorRgbValue));
			var feature = null;
			for (var i = 0; i < n; i++) {
				feature = features[i];
				feature.setSymbol(sym);
				map.graphics.add(feature);
			}
			map.centerAt(feature.geometry.getExtent().getCenter());
			showWaitFlag(false);
		}, function(err) {
			alert(err);
			return;
		});

	}

}


function embbEtgTralQureyResults(data, Query, clc, color) {
	var cc = data;
	if (clc == "point") {
		CellLayers[0].queryFeatures(Query, function(geometry) {
			if (geometry) {
				var geo = geometry.features[0].geometry;
				var pt = new esri.geometry.Point(geo.getExtent().getCenter()
					.getLongitude(), geo.getExtent().getCenter().getLatitude(),
					wgs);
				var p1;
				var index = $("#selectIndex").find("option:selected").text();
				for (var j = 0, c = cc.length; j < c; j++) {
					if (cc[j] == null) {
						continue;
					}
					var t = cc[j].indexValue;
					if (index == "TM") {
						var clc = ColorTMValue(color, t);
					} else {
						var clc = ColorValue(color, t);
					}
					var line = new esri.geometry.Polyline(wgs);
					p1 = new esri.geometry.Point(cc[j].longitude, cc[j].latitude,
						wgs);
					line.addPath([ pt, p1 ]);
					var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_SOLID, clc, 2);
					var GpsHistorySymbol = new esri.Graphic(line, GpsLineSymbol,
						null, null);
					CellLineConGraphicsLayer.add(GpsHistorySymbol);
				}
				CellLineConGraphicsLayer.setOpacity(1);
			}

		}, function(err) {
			alert(err);
		});
	} else {
		CellLayers[0]
			.queryFeatures(
				Query,
				function(geometry) {
					if (geometry) {
						var geo = geometry.features[0].geometry;
						var t = geometry.features[0].attributes.MODEL3,
							clc = Model3Color(t);
						var pt = new esri.geometry.Point(geo.getExtent()
							.getCenter().getLongitude(), geo
							.getExtent().getCenter().getLatitude(), wgs);
						var p1;
						for (var j = 0, c = cc.length; j < c; j++) {
							if (cc[j] == null) {
								continue;
							}
							var line = new esri.geometry.Polyline(wgs);
							p1 = new esri.geometry.Point(cc[j].longitude,
								cc[j].latitude, wgs);
							line.addPath([ pt, p1 ]);
							var GpsLineSymbol = new esri.symbol.SimpleLineSymbol(
								esri.symbol.SimpleLineSymbol.STYLE_SOLID,
								clc, 2);
							var GpsHistorySymbol = new esri.Graphic(line,
								GpsLineSymbol, null, null);
							CellLineConGraphicsLayer.add(GpsHistorySymbol);
						}
						CellLineConGraphicsLayer.setOpacity(0.5);
					}
				}, function(err) {
					alert(err);
				});
	}
}

function stationEmbbEtgTral(data, event) {
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	map.addLayer(GPSTrackGraphicsLayer);
	if (data == null || data.length < 1) {
		return;
	}
	var colorArry = {};
	if (window.parent.layerManageData.layerChooseData) {
		//data = window.parent.layerManageData.conventionalSignsData;
		colorArry = window.parent.layerManageData;
		for (var i = 0; i < colorMapEtgTral.rsrp.length; i++) {
			colorMapEtgTral.rsrp[i].color = colorArry.conventionalSignsData.targetChooseTableData.rsrp[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.sinr.length; i++) {
			colorMapEtgTral.sinr[i].color = colorArry.conventionalSignsData.targetChooseTableData.sinr[i].color;
		}
		for (var i = 0; i < colorMapEtgTral.beam.length; i++) {
			colorMapEtgTral.beam[i].color = colorArry.conventionalSignsData.targetChooseTableData.beam[i].color;
		}
		var sampleTypeData = colorArry.conventionalSignsData.sampleTypeData;
		var pointStyle = "Circle";
		for (var i = 0; i < sampleTypeData.length; i++) {
			if (sampleTypeData[i].selected == true) {
				pointStyle = sampleTypeData[i].value;
			}
		}
		switch (pointStyle) {
		case "Circle":
			style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
			break;
		case "Square":
			style = esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE;
			break;
		}
	} else {
		style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
	}
	var color = null;
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var cc = '#FF0000';
		if (event == "nrRsrp") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, event);
				color = colorMapEtgTral.rsrp;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[event]));
		} else if (event == "lteRsrp") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, event);
				color = colorMapEtgTral.rsrp;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[event]));
		} else if (event == "nrSinr") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, event);
				color = colorMapEtgTral.sinr;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.sinr, parseFloat(d[event]));
		} else if (event == "lteSinr") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, event);
				color = colorMapEtgTral.sinr;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.sinr, parseFloat(d[event]));
		} else if (event == "beamSum") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.beam, event);
				color = colorMapEtgTral.beam;
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.beam, parseFloat(d[event]));
		}
		if (cc == "") {
			cc = '#FF0000';
		}
		var pt = new esri.geometry.Point(d.longtitude, d.latitude, wgs);
		var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, cc);
		var startGraphic = new esri.Graphic(pt, symbol, {
			"nrCellid" : d.nrCellid,
			"nrPCI" : d.nrPci,
			"nrCellNumber" : d.recseqno,
			"nrRsrp" : d.nrRsrp,
			"nrNcellNumber1" : d.nrNcellNumber1,
			"nrNcellNumber2" : d.nrNcellNumber2,
			"nrNcellNumber3" : d.nrNcellNumber3,
			"nrNcellNumber4" : d.nrNcellNumber4,
			"nrNcellNumber5" : d.nrNcellNumber5,
			"nrNcellNumber6" : d.nrNcellNumber6,
			"nrNcellNumber7" : d.nrNcellNumber7,
			"nrNcellNumber8" : d.nrNcellNumber8
		}, null);
		GPSTrackGraphicsLayer.add(startGraphic);
	}

	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>CELLID:</td> " +
		"<td style='text-align:left;'>${nrCellid}</td> " +
		"<td style='text-align:left;'>NR PCI:</td> " +
		"<td style='text-align:left;'>${nrPCI}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>占用波束编号:</td><td style='text-align:left;'>${nrCellNumber}</td> " +
		"<td style='text-align:left;'>占用波束RSRP:</td><td style='text-align:left;'>${nrRsrp}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区1波束:</td><td style='text-align:left;'>${nrNcellNumber1}</td> " +
		"<td style='text-align:left;'>邻小区2波束:</td><td style='text-align:left;'>${nrNcellNumber2}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区3波束:</td><td style='text-align:left;'>${nrNcellNumber3}</td> " +
		"<td style='text-align:left;'>邻小区4波束:</td><td style='text-align:left;'>${nrNcellNumber4}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区5波束:</td><td style='text-align:left;'>${nrNcellNumber5}</td> " +
		"<td style='text-align:left;'>邻小区6波束:</td><td style='text-align:left;'>${nrNcellNumber6}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区7波束:</td><td style='text-align:left;'>${nrNcellNumber7}</td> " +
		"<td style='text-align:left;'>邻小区8波束:</td><td style='text-align:left;'>${nrNcellNumber8}</td> " +
		"</tr> " +
		"</table>");

	GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);

	if (window.parent.layerManageData.layerChooseData) {
		if (window.parent.layerManageData.line.cellCoverThreshold.cellLinkColor.value == '1') {
			for (var z = 0; z < color.length; z++) {
				color[z].color = '#' + window.parent.layerManageData.line.cellCoverThreshold.cellLinkColor.cellLinkColorInput;
			}
		}
	}

	CellLayers[0].on("click", function(eventParam) {
		var ima = document.getElementById('DisplayCleLPoint');
		//ima.src = "images/DisplayCleLPoint_hide.png";

		if (undefined == CellLineConGraphicsLayer
			|| null == CellLineConGraphicsLayer) {
			CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
			map.addLayer(CellLineConGraphicsLayer);
		} else {
			CellLineConGraphicsLayer.clear();
		}
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;

		var clc = $("#SelectCellColorLine").find("option:selected").val();
		Query.where = "CELLID='" + eventParam.graphic.attributes.CELLID + "'";
		var pointgraphics = GPSTrackGraphicsLayer.graphics;
		var cc = [];
		for (var ii = 0; ii < pointgraphics.length; ii++) {
			if (pointgraphics[ii].attributes.nrCellid == eventParam.graphic.attributes.CELLID) {
				cc.push({
					indexValue : pointgraphics[ii].attributes[event],
					longitude : pointgraphics[ii].geometry.x,
					latitude : pointgraphics[ii].geometry.y
				});
			}
		}
		embbEtgTralQureyResults(cc, Query, clc, color);
	});
	CellLayers[1].on("click", function(eventParam) {
		var ima = document.getElementById('DisplayCleLPoint');
		//ima.src = "images/DisplayCleLPoint_hide.png";

		if (undefined == CellLineConGraphicsLayer
			|| null == CellLineConGraphicsLayer) {
			CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
			map.addLayer(CellLineConGraphicsLayer);
		} else {
			CellLineConGraphicsLayer.clear();
		}
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;

		var clc = $("#SelectCellColorLine").find("option:selected").val();
		Query.where = "CELLID='" + eventParam.graphic.attributes.CELLID + "'";
		var pointgraphics = GPSTrackGraphicsLayer.graphics;
		var cc = [];
		for (var ii = 0; ii < pointgraphics.length; ii++) {
			if (pointgraphics[ii].attributes.nrCellid == eventParam.graphic.attributes.CELLID) {
				cc.push({
					indexValue : pointgraphics[ii].attributes[event],
					longitude : pointgraphics[ii].geometry.x,
					latitude : pointgraphics[ii].geometry.y
				});
			}
		}
		embbEtgTralQureyResults(cc, Query, clc, color);
	});

	//GPSTrackGraphicsLayer.emit("click");
	GPSTrackGraphicsLayer.on("click", function(eventParam) {
		var ima = document.getElementById('DisplayCleLPoint');
		//ima.src = "images/DisplayCleLPoint_hide.png";
		//		console.log(eventParam.graphic.attributes);
		//		console.log(eventParam.graphic.geometry);
		//		console.log(eventParam.graphic.geometry.x);
		//		console.log(eventParam.graphic.geometry.y);
		var cc = [ {
			indexValue : eventParam.graphic.attributes[event],
			longitude : eventParam.graphic.geometry.x,
			latitude : eventParam.graphic.geometry.y
		} ];

		if (undefined == CellLineConGraphicsLayer
			|| null == CellLineConGraphicsLayer) {
			CellLineConGraphicsLayer = new esri.layers.GraphicsLayer();
			map.addLayer(CellLineConGraphicsLayer);
		} else {
			CellLineConGraphicsLayer.clear();
		}

		var clc = $("#SelectCellColorLine").find("option:selected").val();
		//采样点与主小区连线
		var Query = new esri.tasks.Query();
		Query.returnGeometry = true;
		Query.where = "CELLID='" + eventParam.graphic.attributes.nrCellid + "'";
		embbEtgTralQureyResults(cc, Query, clc, color);
		//采样点与邻小区连线
		if (eventParam.graphic.attributes.nrNcellNumber1) {
			var Query1 = new esri.tasks.Query();
			Query1.returnGeometry = true;
			Query1.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber1 + "'";
			embbEtgTralQureyResults(cc, Query1, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber2) {
			var Query2 = new esri.tasks.Query();
			Query2.returnGeometry = true;
			Query2.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber2 + "'";
			embbEtgTralQureyResults(cc, Query2, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber3) {
			var Query3 = new esri.tasks.Query();
			Query3.returnGeometry = true;
			Query3.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber3 + "'";
			embbEtgTralQureyResults(cc, Query3, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber4) {
			var Query4 = new esri.tasks.Query();
			Query4.returnGeometry = true;
			Query4.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber4 + "'";
			embbEtgTralQureyResults(cc, Query4, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber5) {
			var Query5 = new esri.tasks.Query();
			Query5.returnGeometry = true;
			Query5.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber5 + "'";
			embbEtgTralQureyResults(cc, Query5, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber6) {
			var Query6 = new esri.tasks.Query();
			Query6.returnGeometry = true;
			Query6.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber6 + "'";
			embbEtgTralQureyResults(cc, Query6, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber7) {
			var Query7 = new esri.tasks.Query();
			Query7.returnGeometry = true;
			Query7.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber7 + "'";
			embbEtgTralQureyResults(cc, Query7, clc, color);
		}
		if (eventParam.graphic.attributes.nrNcellNumber8) {
			var Query8 = new esri.tasks.Query();
			Query8.returnGeometry = true;
			Query8.where = "CELLID='" + eventParam.graphic.attributes.nrNcellNumber8 + "'";
			embbEtgTralQureyResults(cc, Query8, clc, color);
		}

	});
	//设置中心经纬度
	var lon = parseFloat(data[0].longtitude);
	var lat = parseFloat(data[0].latitude);
	mapCenterAndZoom(lon, lat);
}




/**
 * 采样点指标轨迹绘制
 */
function stationSAMTralFunc(data, event) {
	if (undefined == GPSTrackGraphicsLayer || null == GPSTrackGraphicsLayer) {
		GPSTrackGraphicsLayer = new esri.layers.GraphicsLayer();
	} else {
		GPSTrackGraphicsLayer.clear();
	}
	map.addLayer(GPSTrackGraphicsLayer);
	if (data == null || data.length < 1) {
		return;
	}
	style = esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE;
	for (var i = 0; i < data.length; i++) {
		var d = data[i];
		var cc = '#FF0000';
		if (event == "nrRsrp") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, event);
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[event]));
		} else if (event == "lteRsrp") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.rsrp, event);
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.rsrp, parseFloat(d[event]));
		} else if (event == "nrSinr") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, event);
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.sinr, parseFloat(d[event]));
		} else if (event == "lteSinr") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.sinr, event);
			}
			if (d[event] == null) {
				continue;
			}
			cc = ColorValue(colorMapEtgTral.sinr, parseFloat(d[event]));
		} else if (event == "nrMacthrputdl") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputdl, "FTP下行速率");
			}
			var value = 0;
			/*if(d["nrMacthrputdl"] == null && d["lteMacthrputdl"] == null){
				continue;
			} 
			if(d["nrMacthrputdl"] != null){
				value = parseFloat(d["nrMacthrputdl"]);
			}
			if(d["lteMacthrputdl"] != null){
				value = value + parseFloat(d["lteMacthrputdl"]);
			}*/
			if (d["nrMacthrputdl"] == null) {
				continue;
			} else {
				value = parseFloat(d["nrMacthrputdl"]);
			}
			cc = ColorValue(colorMapEtgTral.macthrputdl, value);
		} else if (event == "nrMacthrputul") {
			if (i == 0) {
				SetEmbbMapLengend(colorMapEtgTral.macthrputul, "FTP上行速率");
			}
			var value = 0;
			/*if(d["nrMacthrputul"] == null && d["lteMacthrputul"] == null){
				continue;
			} 
			if(d["nrMacthrputul"] != null){
				value = parseFloat(d["nrMacthrputul"]);
			}
			if(d["lteMacthrputul"] != null){
				value = value + parseFloat(d["lteMacthrputul"]);
			}*/
			if (d["nrMacthrputul"] == null) {
				continue;
			} else {
				value = parseFloat(d["nrMacthrputul"]);
			}
			cc = ColorValue(colorMapEtgTral.macthrputul, value);
		}
		var pt = new esri.geometry.Point(d.longtitude, d.latitude, wgs);
		var symbol = new esri.symbol.SimpleMarkerSymbol(style, 7, null, cc);
		var startGraphic = new esri.Graphic(pt, symbol, {
			"nrCellid" : d.nrCellid,
			"nrPCI" : d.nrPCI,
			"nrCellNumber" : d.nrCellNumber,
			"nrRsrp" : d.nrRsrp,
			"nrNcellNumber1" : d.nrNcellNumber1,
			"nrNcellNumber2" : d.nrNcellNumber2,
			"nrNcellNumber3" : d.nrNcellNumber3,
			"nrNcellNumber4" : d.nrNcellNumber4,
			"nrNcellNumber5" : d.nrNcellNumber5,
			"nrNcellNumber6" : d.nrNcellNumber6,
			"nrNcellNumber7" : d.nrNcellNumber7,
			"nrNcellNumber8" : d.nrNcellNumber8
		}, null);
		GPSTrackGraphicsLayer.add(startGraphic);
	}
	var infoTemplate = new esri.InfoTemplate("详情", "<table class='t2'> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>CELLID:</td> " +
		"<td style='text-align:left;'>${nrCellid}</td> " +
		"<td style='text-align:left;'>PCI:</td> " +
		"<td style='text-align:left;'>${nrPCI}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>占用波束编号:</td><td style='text-align:left;'>${nrCellNumber}</td> " +
		"<td style='text-align:left;'>占用波束RSRP:</td><td style='text-align:left;'>${nrRsrp}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区1波束:</td><td style='text-align:left;'>${nrNcellNumber1}</td> " +
		"<td style='text-align:left;'>邻小区2波束:</td><td style='text-align:left;'>${nrNcellNumber2}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区3波束:</td><td style='text-align:left;'>${nrNcellNumber3}</td> " +
		"<td style='text-align:left;'>邻小区4波束:</td><td style='text-align:left;'>${nrNcellNumber4}</td> " +
		"</tr> " +
		"<tr class='tr1'> " +
		"<td style='text-align:left;'>邻小区5波束:</td><td style='text-align:left;'>${nrNcellNumber5}</td> " +
		"<td style='text-align:left;'>邻小区6波束:</td><td style='text-align:left;'>${nrNcellNumber6}</td> " +
		"</tr> " +
		"<tr class='tr2'> " +
		"<td style='text-align:left;'>邻小区7波束:</td><td style='text-align:left;'>${nrNcellNumber7}</td> " +
		"<td style='text-align:left;'>邻小区8波束:</td><td style='text-align:left;'>${nrNcellNumber8}</td> " +
		"</tr> " +
		"</table>");
//GPSTrackGraphicsLayer.setInfoTemplate(infoTemplate);
//	//设置中心经纬度
//	var lon = parseFloat(data[0].longtitude);
//	var lat = parseFloat(data[0].latitude);
//	mapCenterAndZoom(lon,lat);
}

/**
 * 单站查看进度 加载小区图层方法
 */
function StationAddCellToMap(kpi, bl, lon, lat, arrs) {
	var dataSource = new esri.layers.TableDataSource();
	dataSource.workspaceId = WorkspaceID;
	dataSource.dataSourceName = kpi;
	var layerSource = new esri.layers.LayerDataSource();
	layerSource.dataSource = dataSource;
	var cc = SetWindowContent2(arrs);
	var infoTemplate = new esri.InfoTemplate("详情", cc);
	// create a new feature layer based on the table data source
	CellfeatureLayer = new esri.layers.FeatureLayer(yewu_url
		+ "/dynamicLayer", {
			mode : esri.layers.FeatureLayer.MODE_ONDEMAND,
			outFields : [ "*" ],
			infoTemplate : infoTemplate,
			source : layerSource,
			opacity : 0.5
		});

	if (bl) {
		CellfeatureLayer.on("load", function(evt) {
			// project the extent if the map's spatial reference is different
			// that the layer's extent.
			var gs = new esri.tasks.GeometryService(geometry_url);
			var extent = evt.layer.fullExtent;
			mapDataExtent = extent;
			if (extent.spatialReference.wkid === map.spatialReference.wkid) {
				map.setExtent(extent);
			} else {
				gs.project([ extent ], map.spatialReference).then(
					function(results) {
						map.setExtent(results[0]);
					});
			}
		});
	}
	var linesym = new esri.symbol.SimpleLineSymbol("solid", new esri.Color([ 0,
		0, 0 ]), 0.4);
	var renderer = new esri.renderer.SimpleRenderer(
		new esri.symbol.SimpleFillSymbol("solid", linesym, new esri.Color([
			0, 128, 0, 0.75 ])));
	CellfeatureLayer.setRenderer(renderer);
	CellfeatureLayer.setScaleRange(32000, 0);
	setCellLayers(CellfeatureLayer);

	var str = "";
	var showTargetOption = 'CELLNAME,CELLID';
	if (showTargetOption.length > 0) {
		var options = showTargetOption.split(',');
		for (var i = 0; i < options.length; i++) {
			if (i != 0) {
				str = str + ',';
			}
			str = str + options[i] + ":{" + options[i] + "}";
		}
	}
	var sym = new esri.symbol.TextSymbol();
	var pointColor = "#000000";
	sym.setColor(new dojo.Color([ 255, 0, 18, 0.75 ]));
	sym.font.setSize("15pt");
	sym.font.setFamily("微软雅黑");
	var jso = {
		"labelExpressionInfo" : {
			"value" : str
		},
		"labelPlacement" : "always-horizontal"
	}
	var LabelCell = new esri.layers.LabelClass(jso);
	LabelCell.symbol = sym;
	CellfeatureLayer.setLabelingInfo([ LabelCell ]);
	CellfeatureLayer.setShowLabels(true);
	map.addLayer(CellfeatureLayer);
	var inPoint = new esri.geometry.Point(parseFloat(lon), parseFloat(lat), wgs);
	map.centerAndZoom(inPoint, 15);
	map.infoWindow.resize(300, 300);

}


//获取小区或栅格基本信息框
var SetWindowContent2 = function(arrs) {
	var res = "<table class='t2'>";
	var t = "";
	for (var i = 0, n = arrs.length; i < n; i++) {
		var name = arrs[i];
		if (arrs[i] == "HEIGHT") {
			name = "站高";
		} else if (arrs[i] == "CITY") {
			name = "区域";
		}
		if (i % 2 == 0) {
			t += "<tr class='tr1'><td>" + name + "</td><td>" + "${"
				+ arrs[i] + "}</td></tr>";
		} else {
			t += "<tr class='tr2'><td>" + name + "</td><td>" + "${"
				+ arrs[i] + "}</td></tr>";
		}
	}
	res += t + "</table>";
	return res;
}

//小区(PCI)渲染
function StationLayerManage() {
	var n = CellLayers.length;
	if (n > 0) {
		for (var i = 0; i < n; i++) {
			map.removeLayer(CellLayers[i]);
			var defaultSym = new esri.symbol.SimpleFillSymbol("solid",
				null, new dojo.Color([ 255, 0, 255, 0.75 ]));
			var renderer = new esri.renderer.UniqueValueRenderer(
				defaultSym, "MODEL3");
			renderer
				.addValue("有更新", new esri.symbol.SimpleFillSymbol(
					"solid", null, new esri.Color([ 255, 0, 0, 0.75 ])));
			renderer
				.addValue("未完成", new esri.symbol.SimpleFillSymbol(
					"solid", null, new esri.Color([ 255, 0, 0, 0.75 ])));
			renderer
				.addValue("全部完成", new esri.symbol.SimpleFillSymbol(
					"solid", null, new esri.Color([ 146, 208, 80, 0.75 ])));
			CellLayers[i].setRenderer(renderer);

			map.addLayer(CellLayers[i]);
		}
	} else {
		return;
	}
}

//单验进度查看小区渲染
function StationCompletionLayerManage(testingList) {
	var n = CellLayers.length;
	if (n > 0) {
		for (var i = 0; i < n; i++) {
			map.removeLayer(CellLayers[i]);
			var defaultSym = new esri.symbol.SimpleFillSymbol("solid",
				null, new dojo.Color([ 255, 0, 255, 0.75 ]));
			var renderer = new esri.renderer.ClassBreaksRenderer(defaultSym, "CELLID");
			for (var j = 0; j < testingList.length; j++) {
				renderer.addBreak(testingList[j] - 0.1, testingList[j] + 0.1, new esri.symbol.SimpleFillSymbol("solid", null, "#FF0000"));
			}
			CellLayers[i].setRenderer(renderer);
			map.addLayer(CellLayers[i]);
		}
	} else {
		return;
	}
}

//5G基础统计图层管理  加载小区图层
//bl表示小区是否居中显示 cellShowFlag:是否显示小区图层 colors:颜色集合,cellRange:透明度
function FGLayerManageAddCellbyArray(cellShowFlag, colors, cellRange) {

	//	LayerManagePCI(colors);
	document.getElementById('label_CellsLayer').disabled = !cellShowFlag;
	//是否显示小区图层
	netTypeLayer(cellShowFlag, colors);
	var n = CellLayers.length;
	//	netTypeLayer(cellShowFlag);
	if (n > 0) {
		var str = "";
		var pointColor = "#000000";
		var fontSize = "11pt";
		if (cellLayerSelect == null || cellLayerSelect == 5 || cellLayerSelect == 45) {
			if (fgFontColorSelected) {
				fontSize = fgFontSizeSelected + "pt";
				if (fgFontColorSelected == '红色') {
					pointColor = "#FF0012";
				} else if (fgFontColorSelected == '黑色') {
					pointColor = "#000000";
				} else if (fgFontColorSelected == '蓝色') {
					pointColor = "#0A00FF";
				} else if (fgFontColorSelected == '绿色') {
					pointColor = "#00FF00";
				}
			}
		} else if (cellLayerSelect == 4) {
			if (LTEFontColorSelected) {
				fontSize = LTEFontSizeSelected + "pt";
				if (LTEFontColorSelected == '红色') {
					pointColor = "#FF0012";
				} else if (LTEFontColorSelected == '黑色') {
					pointColor = "#000000";
				} else if (LTEFontColorSelected == '蓝色') {
					pointColor = "#0A00FF";
				} else if (LTEFontColorSelected == '绿色') {
					pointColor = "#00FF00";
				}
			}
		}

		for (var i = 0; i < n; i++) {
			var sym = new esri.symbol.TextSymbol();
			sym.font.setSize(fontSize);
			sym.font.setFamily("arial");
			sym.setColor(new dojo.Color(pointColor));
			var jso = {
				"labelExpressionInfo" : {
					"value" : str
				},
				"labelPlacement" : "always-horizontal"
			}
			var LabelCell = new esri.layers.LabelClass(jso);
			LabelCell.symbol = sym;
			CellLayers[i].setLabelingInfo([ LabelCell ]);
			CellLayers[i].setShowLabels(false);
		}
	}
//window.parent.ShowTrackRender();//是否展示测试数据(采样点)图层
}

//单站报告展示的图层管理  加载小区图层
//bl表示小区是否居中显示 cellShowFlag:是否显示小区图层 colors:颜色集合,cellRange:透明度,showTargetOption:选中的标签集合,showTargetFlag:是否显示标注
function ReportLayerManageAddCell(cellShowFlag, colors, cellRange, showTargetOption, showTargetFlag) {
	/*	LayerManagePCI(colors);*/

	document.getElementById('label_CellsLayer').disabled = !cellShowFlag;
	netTypeLayer(cellShowFlag, colors);
	//是否显示小区图层
	var n = CellLayers.length;

	if (n > 0) {
		var str = "";
		if (showTargetOption.length > 0) {
			var options = showTargetOption.split(',');
			for (var i = 0; i < options.length; i++) {
				if (i != 0) {
					str = str + ',';
				}
				str = str + options[i] + ":{" + options[i] + "}";
			}
		}
		var pointColor = "#000000";
		var fontSize = "11pt";
		if (cellLayerSelect == null || cellLayerSelect == 5 || cellLayerSelect == 45) {
			if (fgFontColorSelected) {
				fontSize = fgFontSizeSelected + "pt";
				if (fgFontColorSelected == '红色') {
					pointColor = "#FF0012";
				} else if (fgFontColorSelected == '黑色') {
					pointColor = "#000000";
				} else if (fgFontColorSelected == '蓝色') {
					pointColor = "#0A00FF";
				} else if (fgFontColorSelected == '绿色') {
					pointColor = "#00FF00";
				}
			}
		} else if (cellLayerSelect == 4) {
			if (LTEFontColorSelected) {
				fontSize = LTEFontSizeSelected + "pt";
				if (LTEFontColorSelected == '红色') {
					pointColor = "#FF0012";
				} else if (LTEFontColorSelected == '黑色') {
					pointColor = "#000000";
				} else if (LTEFontColorSelected == '蓝色') {
					pointColor = "#0A00FF";
				} else if (LTEFontColorSelected == '绿色') {
					pointColor = "#00FF00";
				}
			}
		}

		for (var i = 0; i < n; i++) {
			var sym = new esri.symbol.TextSymbol();
			sym.font.setSize(fontSize);
			sym.font.setFamily("arial");
			sym.setColor(new dojo.Color(pointColor));
			var jso = {
				"labelExpressionInfo" : {
					"value" : str
				},
				"labelPlacement" : "always-horizontal"
			}
			var LabelCell = new esri.layers.LabelClass(jso);
			console.info(LabelCell);
			LabelCell.symbol = sym;
			CellLayers[i].setLabelingInfo([ LabelCell ]);
			CellLayers[i].setShowLabels(showTargetFlag);
		}
	}
	//重新渲染轨迹点 针对单站验证功能
	//window.parent.eventChooseFunc();
	//保存修改图例到数据库,mapNrPlanColor代表单验验证的图例
	//saveMapLegend("mapNrPlanColor");
	//window.parent.ShowTrackRender();//是否展示测试数据(采样点)图层

}

/**
 * 打印地图工具
 * ARGIS的打印工具保存的地图轨迹截图
 * 建议使用html2canvas截图
 */
function printTools() {
	require([ "esri/tasks/PrintTask", "esri/tasks/PrintTemplate", "esri/tasks/PrintParameters" ], function(PrintTask, PrintTemplate, PrintParameters) {
		var layer = new esri.layers.ArcGISDynamicMapServiceLayer(yewu_url);
		map.addLayer(layer);

		//创建地图打印对象
		var url = printTools_url;
		var printMap = new PrintTask(url, "sync");
		//输出图片的空间参考
		printMap.outSpatialReference = map.SpatialReference;
		//创建地图打印模版
		var template = new PrintTemplate();
		//创建地图的打印参数，参数里面包括：模版和地图
		var params = new PrintParameters();
		//打印图片的各种参数
		template.exportOptions = {
			width : 1200,
			height : 1200,
			dpi : 96
		};
		//打印输出的格式
		template.format = "jpg";
		//输出地图的布局
		template.layout = "A4 Landscape";
		template.preserveScale = false;
		// PrintTemplate
		//设置参数地图
		params.map = map;
		//设置参数模版   
		params.template = template;
		//运行结果 
		printMap.execute(params, function(result) {
			if (result != null) {
				//result.url保存了图片在argis地图服务器的保存地址
				//ajax上传到服务端保存截图

				//需要再次保存图例
				takeScreenshot("Legend", result.url);
			}
		})
	})

}

/**
 * html2canvas保存地图截图
 * 注意：服务端保存截图不能使用GET请求，要使用POST
 */
function takeScreenshot(id, url) {
	//	  html2canvas(document.getElementById(id), {
	//	   	  allowTaint: false,
	//	      taintTest: true
	//	  }).then(function(canvas) {
	//		  var imagestr = canvas.toDataURL("image/png", 0.2);
	//		  //需要去除"data:image/png;base64,"才能转换为图片
	//		  imagestr = imagestr.replaceAll("data:image/png;base64,", "");
	//		  //服务端保存截图
	//		  window.parent.downloadImagejie(imagestr,url);
	//	   
	//	  }).catch(function(e) {
	//		  console.error('error', e);
	//	  });

	var targetDom = $("#" + id);
	//克隆截图区域
	var copyDom = targetDom.clone();
	copyDom.width(targetDom.width() + "px");
	copyDom.height(targetDom.height() + "px");
	copyDom.attr("id", "copyDom");
	$("body").append(copyDom);
	//    var pathName = document.location.pathname;
	//    var ctxPath = pathName.substring(1, pathName.substr(1).indexOf('/') + 1);
	html2canvas(copyDom[0], {
		useCORS : true,
		imageTimeout : 0,
		allowTaint : false,
		taintTest : true
	//, proxy: "/" + ctxPath + "/proxy/proxyScreenShot"
	}).then(function(canvas) {
		//克隆DOM删除
		copyDom.remove();
		var imagestr = canvas.toDataURL("image/png", 0.2);
		//需要去除"data:image/png;base64,"才能转换为图片
		imagestr = imagestr.replaceAll("data:image/png;base64,", "");
		//服务端保存截图
		window.parent.downloadImagejie(imagestr, "");
	});
}