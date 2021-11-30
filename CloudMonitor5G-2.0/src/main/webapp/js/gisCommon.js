 /**
 * GIS地图共用JS
 * 
 * @author yinzhipeng
 * @date 2015年11月11日 下午午13:33:23
 * @version
 */

/**
 * 指标渲染下拉菜单指标
 */
var drawKpis=[{
	name:'RSRP',
    value:1
},{
	name:'MOS值',
    value:0
},{
	name:'SINR',
    value:2
},{
	name:'上行MCS等级',
    value:3
},{
	name:'下行MCS等级',
    value:4
},{
	name:'RTP时延',
    value:5
},{
	name:'RTP丢包率',
    value:6
},{
	name:'上行PRB占用数',
    value:7
},{
	name:'下行PRB占用数',
    value:8
}/*,{
	name:'VMOS',
	value:9
},{
	name:'LTE系统内切换',
	value:10
}*/];

/**
 * 主被叫切换下拉列表
 */
var drawCallTypes=[{
	name:'主叫',
    value:0
},{
	name:'被叫',
    value:1
}];

/**
 * 地图工具栏
 */
var gisToolBars={
	wholePreview:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:true,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常轨迹
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'qbr'//模块类型
	},
	whole:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'qbr'//模块类型
	},
	weakOver:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:true,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'qbr'//模块类型
	},
	distrub:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:true,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'qbr'//模块类型
	},
	ShowPointLonAndLat:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:[],//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:[],//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		showSelectGpsPoint:false, //是否显示选取经纬度点工具
		showFrameSelect:false, //是否显示选框
		showDelLayer: false, //显示删除图层按钮
		showSelectLinePoint:false, //是否显示线路点选取工具
		moduleType:''//模块类型
	},
	distrub5g:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'qbr'//模块类型
	},
	nbCell:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:true,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:true,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'qbr'//模块类型
	},
	paramError:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:true,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'qbr'//模块类型
	},
	other:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:true,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'qbr'//模块类型
	},
	exceptionEventWhole:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:true,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'ee'//模块类型
	},
	exceptionEventProblem:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:true,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:true,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:true,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'ee'//模块类型
	},
	handoverFailWhole:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:true,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'hof'//模块类型
	},
	handoverFailProblem:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:true,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:true,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'hof'//模块类型
	},
	compareWholePreview:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:true,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:true,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:true,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常轨迹
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'compare'//模块类型
	},
	compareGrid:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常轨迹
		showCompareGrid:true,//是否界面初始化对比日志栅格渲染
		moduleType:'compare'//模块类型
	},
	compareMosBad:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常轨迹
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'compare'//模块类型
	},
	compareHOF:{
		showMapLayer:true,//是否显示图层控制
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:true,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:true,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'compare'//模块类型
	},
	none:{
		showMapLayer:false,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		moduleType:'qbr'//模块类型
	},
	embbCover:{
		showMapLayer:true ,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:true,//是否显示样式设置工具
		showModel3:true,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:true, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:true,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'embb'//模块类型
	},
	nsaAnalysis:{
		SearchCell:true,//是否显示查找
		showDrawCallType:false,//是否显示主被叫切换工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showCell:false,//是否显示小区渲染
		BoshuAnalyseLi:true,//是否显示波束分析按钮
		layerManager:true,//是否显示图层管理按钮
		layerManager2:false,//是否显示图层管理按钮
		stationLine:true,//站间线
		networkStructure:false,//网络结构
		moduleType:'nsa',//模块类型
		showCellAndSample:true,// 是否显示显示/清除服务小区与采样点连线工具
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		showSelectGpsPoint:false, //是否显示选取经纬度点工具
		eventShow:true//是否显示异常事件图标
	},
	stationReport:{
		showMapLayer:false ,//是否显示图层控制
		showCellAndSample:false,// 是否显示显示/清除服务小区与采样点连线工具
		showCellAndEvent:false,//是否显示显示/清除服务小区与事件图标连线工具
		showCellAndCell:false,// 是否显示显示/清除待添加邻区关系连线工具
		showStyleSet:false,//是否显示样式设置工具
		showModel3:false,//是否显示模3核查工具
		showTacModel:false,//是否显示TAC工具
		showRunOrientation:false,// 是否显示行驶方向工具
		showGpsTrackShifting:false,//是否显示轨迹偏移
		showDrawKpi:false, //是否显示地图渲染指标工具
		drawKpi:drawKpis,//地图渲染指标下拉列表
		showDrawCallType:false,//是否显示主被叫切换工具
		drawCallType:drawCallTypes,//主被叫切换工具下拉列表
		showCell:false,//是否显示小区渲染
		showTestLogItemGpsTrack:false,//是否页面初始化测试日志轨迹
		showCompareTestLogItemGpsTrack:false,//是否页面初始化对比测试日志轨迹
		showQBRGpsTrack:false,//是否页面初始化渲染质差路段轨迹
		showEEGpsTrack:false,//是否页面初始化异常事件轨迹
		showHOFGpsTrack:false,//是否页面初始化切换失败轨迹
		showCWBRGpsTrack:false,//是否页面初始化连续无线差轨迹
		showCEDEGpsTrack:false,//是否界面初始化呼叫建立时延异常
		showCompareGrid:false,//是否界面初始化对比日志栅格渲染
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		moduleType:'stationReport',//模块类型
		SearchCell:false,//是否显示查找
		BoshuAnalyseLi:false,//是否显示波束分析按钮
		layerManager:false,//是否显示图层管理按钮1
		layerManager2:false,//是否显示图层管理按钮2
		stationLine:false,//站间线
		networkStructure:false,//网络结构
		eventShow:false,//事件展示
		showDrawRectangleFrame:false, //是否显示画矩形框工具
		showSelectGpsPoint:false, //是否显示选取经纬度点工具
		showRoadPoints:false
	}
};
/**
 * 
 * 获取地图工具栏配置,获取地图渲染指标下拉列表配置
 * @param toolbarType 质差专题:0整体,1弱覆盖,2干扰,3邻区,4参数,5核心网,6其他;
 * <br>整体概览:7整体概览;
 * <br>异常事件专题:8整体分析,40语音未接通,41语音掉话,42注册失败,43CSFB失败,44视频未接通,45视频掉话;
 * <br>切换失败专题:10整体分析,11问题分析;
 * <br>连续无线差专题:12整体,13弱覆盖,14干扰,15邻区,16其他;
 * <br>呼叫建立时延异常专题:17整体,18弱覆盖,19重叠覆盖,20被叫位置更新,21其他
 * <br>对比分析专题:22整体概览,23MOS差黑点,24SRVCC切换失败对比,25系统内切换失败对比,26栅格对比
 * <br>27实时监控 28历史回放
 * <br>Volte视频质差分析:31整体分析,32乒乓切换,33邻区问题,34弱覆盖,35干扰,36重叠覆盖,37模式转换,38下行调度小,39其他
 * <br>流媒体视频质差分析:51整体分析,52乒乓切换,53邻区问题,54弱覆盖,55干扰,56重叠覆盖,57下行调度小,58其他
 * @returns
 */
function initToolbar(toolbarType){	
	if(toolbarType){
		console.dir(toolbarType);
		if('0'==toolbarType){
			if(getTestLogItemIds2QBR()==''){
				gisToolBars.whole.showQBRGpsTrack=false;
			}else{
				gisToolBars.whole.showQBRGpsTrack=true;
			}
			return gisToolBars.whole;
		}else if('1'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.weakOver.showCell=false;
			}else{
				gisToolBars.weakOver.showCell=true;
			}
			return gisToolBars.weakOver;
		}else if('2'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.distrub.showCell=false;
			}else{
				gisToolBars.distrub.showCell=true;
			}
			return gisToolBars.distrub;
		}else if('3'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			return gisToolBars.nbCell;
		}else if('4'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.paramError.showCell=false;
			}else{
				gisToolBars.paramError.showCell=true;
			}
			return gisToolBars.paramError;
		}else if('5'==toolbarType){
			return gisToolBars.none;
		}else if('6'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.other.showCell=false;
			}else{
				gisToolBars.other.showCell=true;
			}
			return gisToolBars.other;
		}else if('7'==toolbarType){
			if(getGpsPointTestLogItemIds()==''){
				gisToolBars.wholePreview.showTestLogItemGpsTrack=false;
			}else{
				gisToolBars.wholePreview.showTestLogItemGpsTrack=true;
			}
			return gisToolBars.wholePreview;
		}else if('8'==toolbarType){
			if(getTestLogItemIds2EE()==''){
				gisToolBars.exceptionEventWhole.showEEGpsTrack=false;
			}else{
				gisToolBars.exceptionEventWhole.showEEGpsTrack=true;
			}
			return gisToolBars.exceptionEventWhole;
		}else if('40'==toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.exceptionEventProblem.showCell=false;
			}else{
				gisToolBars.exceptionEventProblem.showCell=true;
			}
			gisToolBars.exceptionEventProblem.drawKpi=drawKpis;
			return gisToolBars.exceptionEventProblem;
		}else if('41'==toolbarType){
				var drawKpis = [
				{
					name: "RSRP",
					value: 1
				},
				{
					name: "SINR",
					value: 2
				},
				{
					name: "RTP时延",
					value: 5
				},
				{
					name: "RTP丢包率",
					value: 6
				},
				{
					name: "上行PRB占用数",
					value: 7
				},
				{
					name: "下行PRB占用数",
					value: 8
				}
				];
			if(getCellTestLogItemIds()==''){
				gisToolBars.exceptionEventProblem.showCell=false;
			}else{
				gisToolBars.exceptionEventProblem.showCell=true;
			}
			gisToolBars.exceptionEventProblem.drawKpi=drawKpis;
			return gisToolBars.exceptionEventProblem;
		}else if('42'==toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.exceptionEventProblem.showCell=false;
			}else{
				gisToolBars.exceptionEventProblem.showCell=true;
			}
			gisToolBars.exceptionEventProblem.drawKpi=drawKpis;
			return gisToolBars.exceptionEventProblem;
		}else if('43'==toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.exceptionEventProblem.showCell=false;
			}else{
				gisToolBars.exceptionEventProblem.showCell=true;
			}
			gisToolBars.exceptionEventProblem.drawKpi=drawKpis;
			return gisToolBars.exceptionEventProblem;
		}else if('44'==toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			},{
				name:'SINR',
			    value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.exceptionEventProblem.showCell=false;
			}else{
				gisToolBars.exceptionEventProblem.showCell=true;
			}
			gisToolBars.exceptionEventProblem.drawKpi=drawKpis;
			return gisToolBars.exceptionEventProblem;
		}else if('45'==toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			},{
				name:'SINR',
			    value:2
			},{
				name:'RTP语音丢包率',
			    value:8
			},{
				name:'RTP视频丢包率',
			    value:13
			},{
				name:'RTP语音抖动',
			    value:9
			},{
				name:'RTP视频抖动',
			    value:12
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.exceptionEventProblem.showCell=false;
			}else{
				gisToolBars.exceptionEventProblem.showCell=true;
			}
			gisToolBars.exceptionEventProblem.drawKpi=drawKpis;
			return gisToolBars.exceptionEventProblem;
		}else if('10'==toolbarType){
			if(getTestLogItemIds2HOF()==''){
				gisToolBars.handoverFailWhole.showHOFGpsTrack=false;
			}else{
				gisToolBars.handoverFailWhole.showHOFGpsTrack=true;
			}
			return gisToolBars.handoverFailWhole;
		}else if('11'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.handoverFailProblem.showCell=false;
			}else{
				gisToolBars.handoverFailProblem.showCell=true;
			}
			return gisToolBars.handoverFailProblem;
		}else if('12'==toolbarType){
			if(getTestLogItemIds2CWBR()==''){
				gisToolBars.whole.showCWBRGpsTrack=false;
			}else{
				gisToolBars.whole.showCWBRGpsTrack=true;
			}
			gisToolBars.whole.moduleType='cwbr';
			return gisToolBars.whole;
		}else if('13'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.weakOver.showCell=false;
			}else{
				gisToolBars.weakOver.showCell=true;
			}
			gisToolBars.weakOver.moduleType='cwbr';
			return gisToolBars.weakOver;
		}else if('14'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.distrub.showCell=false;
			}else{
				gisToolBars.distrub.showCell=true;
			}
			gisToolBars.distrub.moduleType='cwbr';
			return gisToolBars.distrub;
		}else if('15'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.moduleType='cwbr';
			return gisToolBars.nbCell;
		}else if('16'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.other.showCell=false;
			}else{
				gisToolBars.other.showCell=true;
			}
			gisToolBars.other.moduleType='cwbr';
			return gisToolBars.other;
		}else if('17'==toolbarType){
			if(getTestLogItemIds2CEDE()==''){
				gisToolBars.whole.showCEDEGpsTrack=false;
			}else{
				gisToolBars.whole.showCEDEGpsTrack=true;
			}
			gisToolBars.whole.moduleType='cede';
			return gisToolBars.whole;
		}else if('18'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.weakOver.showCell=false;
			}else{
				gisToolBars.weakOver.showCell=true;
			}
			gisToolBars.weakOver.moduleType='cede';
			return gisToolBars.weakOver;
		}else if('19'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.weakOver.showCell=false;
			}else{
				gisToolBars.weakOver.showCell=true;
			}
			gisToolBars.weakOver.moduleType='cede';
			return gisToolBars.weakOver;
		}else if('20'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.distrub.showCell=false;
			}else{
				gisToolBars.distrub.showCell=true;
			}
			gisToolBars.distrub.moduleType='cede';
			gisToolBars.distrub.showTacModel=true;
			return gisToolBars.distrub;
		}else if('21'==toolbarType){
			if(getCellTestLogItemIds()==''){
				gisToolBars.other.showCell=false;
			}else{
				gisToolBars.other.showCell=true;
			}
			gisToolBars.other.moduleType='cede';
			return gisToolBars.other;
			
		}else if('22'==toolbarType){
			if(getGpsPointTestLogItemIds()==''){
				gisToolBars.compareWholePreview.showTestLogItemGpsTrack=false;
			}else{
				gisToolBars.compareWholePreview.showTestLogItemGpsTrack=true;
			}
			if(getGpsPointCompareTestLogItemIds()==''){
				gisToolBars.compareWholePreview.showCompareTestLogItemGpsTrack=false;
			}else{
				gisToolBars.compareWholePreview.showCompareTestLogItemGpsTrack=true;
			}
			return gisToolBars.compareWholePreview;
		}else if('23'==toolbarType){
			return gisToolBars.compareMosBad;
		}else if('24'==toolbarType){
			return gisToolBars.compareHOF;
		}else if('25'==toolbarType){
			return gisToolBars.compareHOF;
		}else if('26'==toolbarType){
			return gisToolBars.compareGrid;
		}else if('60'==toolbarType){
			//5G栅格对比
			var drawKpis=[{
				name:'RSRP',
			    value:1
			},{
				name:'SINR',
			    value:2
			},{
				name:'PUSCCH发射功率',
			    value:6
			}];
			gisToolBars.compareGrid.drawKpi=drawKpis;
			return gisToolBars.compareGrid;
		}
		else if('27'==toolbarType){
			gisToolBars.none.showMapLayer=true;
			return gisToolBars.none;
		}
		else if('28'==toolbarType){
			gisToolBars.none.showMapLayer=true;
			gisToolBars.none.showDrawKpi=true;
			return gisToolBars.none;
		}else if('31'===toolbarType){//视频质差分析
			return gisToolBars.whole;
		}
		else if('32'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;			 
		}
		else if('33'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			return gisToolBars.nbCell;	
		}
		else if('34'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}
		else if('35'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}else if('36'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}else if('37'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			},{
				name:'TM',
				value:6
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		} else if('38'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			},{
				name:'PDCCHDLGrantNum',
				value:7
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		} else if('39'===toolbarType){
			var drawKpis=[{
				name:'RSRP',
			    value:1
			} ,{
				name:'SINR',
			    value:2
			},{
				name:'VMOS',
				value:0
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}else if('51'===toolbarType){//流媒体视频质差分析
			return gisToolBars.whole;
		}
		else if('52'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			} ,{
				name:'卡顿比例',
			    value:16
			},{
				name:'初始缓冲时延',
				value:17
			},{
				name:'视频全程感知速率',
				value:18
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;			 
		}
		else if('53'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			} ,{
				name:'卡顿比例',
			    value:16
			},{
				name:'初始缓冲时延',
				value:17
			},{
				name:'视频全程感知速率',
				value:18
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			return gisToolBars.nbCell;	
		}
		else if('54'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			} ,{
				name:'卡顿比例',
			    value:16
			},{
				name:'初始缓冲时延',
				value:17
			},{
				name:'视频全程感知速率',
				value:18
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}
		else if('55'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			} ,{
				name:'卡顿比例',
			    value:16
			},{
				name:'初始缓冲时延',
				value:17
			},{
				name:'视频全程感知速率',
				value:18
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}else if('56'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			} ,{
				name:'卡顿比例',
			    value:16
			},{
				name:'初始缓冲时延',
				value:17
			},{
				name:'视频全程感知速率',
				value:18
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}else if('57'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			},{
				name:'PDCCHDLGrantNum',
				value:7
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		} else if('58'===toolbarType){
			var drawKpis=[{
				name:'VMOS',
			    value:15
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			},{
				name:'PDCCHDLGrantNum',
				value:7
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nbCell.showCell=false;
			}else{
				gisToolBars.nbCell.showCell=true;
			}
			gisToolBars.nbCell.drawKpi=drawKpis;
			gisToolBars.nbCell.showCellAndEvent=true;
			gisToolBars.nbCell.showCellAndCell=false;
			return gisToolBars.nbCell;	
		}else if('70'==toolbarType){
			var drawKpis=[{
				name:'SINR',
				value:2
			},{
				name:'RSRP',
				value:1
			},{
				name:'SINQ',
				value:33
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.distrub5g.showCell=false;
				gisToolBars.distrub5g.showEmbbRoadPoints=false;
			}else{
				gisToolBars.distrub5g.showCell=true;
				gisToolBars.distrub5g.showEmbbRoadPoints=true;
			}
			gisToolBars.distrub5g.drawKpi=drawKpis;
			return gisToolBars.distrub5g;
		}else if('90'==toolbarType){
			var drawKpis=[];
			gisToolBars.ShowPointLonAndLat.showCell=false;
			gisToolBars.ShowPointLonAndLat.showEmbbRoadPoints=false;
			gisToolBars.ShowPointLonAndLat.drawKpi=drawKpis;
			return gisToolBars.ShowPointLonAndLat;
		} else if('80'==toolbarType){//yzp 2019-03-28
			var drawKpis=[{
				name:'RSRP',
				value:1
			},{
				name:'SINR',
				value:2
			},{
				name:'RSRQ',
				value:9
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.embbCover.showCell=false;
				gisToolBars.embbCover.showEmbbRoadPoints=false;
			}else{
				gisToolBars.embbCover.showCell=true;
				gisToolBars.embbCover.showEmbbRoadPoints=true;
			}
			gisToolBars.embbCover.drawKpi=drawKpis;
			//gisToolBars.embbCover.showCell_Points=true;
			//gisToolBars.embbCover.showPoint_Cells=true;
			return gisToolBars.embbCover;
		} 
		else if('100'==toolbarType){
			var drawKpis=[{
				name:'rsrp',
				value:109
			},{
				name:'rsrp',
				value:110
			},{
				name:'rsrq',
				value:9
			},{
				name:'sinr',
				value:111
			},{
				name:'sinr',
				value:112
			},{
				name:'ibler',
				value:101
			},{
				name:'beam',
				value:102
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nsaAnalysis.showCell=false;
				gisToolBars.nsaAnalysis.showRoadPoints=false;
			}else{
				gisToolBars.nsaAnalysis.showCell=false;
				gisToolBars.nsaAnalysis.showRoadPoints=false;
				gisToolBars.nsaAnalysis.eventShow=true;//是否显示异常事件
			}
			gisToolBars.nsaAnalysis.drawKpi=drawKpis;
			return gisToolBars.nsaAnalysis;
		}
		else if('101'==toolbarType){
			var drawKpis=[{
				name:'rsrp',
				value:109
			},{
				name:'rsrp',
				value:110
			},{
				name:'rsrq',
				value:9
			},{
				name:'sinr',
				value:111
			},{
				name:'sinr',
				value:112
			},{
				name:'ibler',
				value:101
			},{
				name:'beam',
				value:102
			}];
			if(getCellTestLogItemIds()==''){
				gisToolBars.nsaAnalysis.showCell=false;
				gisToolBars.nsaAnalysis.showRoadPoints=false;
			}else{
				gisToolBars.nsaAnalysis.showCell=false;
				gisToolBars.nsaAnalysis.showRoadPoints=false;
				gisToolBars.nsaAnalysis.eventShow=true;//是否显示异常事件
			}
			gisToolBars.nsaAnalysis.drawKpi=drawKpis;
			return gisToolBars.nsaAnalysis;
		}else if('102'==toolbarType){
			var drawKpis=[{
				name:'rsrp',
				value:109
			},{
				name:'rsrp',
				value:110
			},{
				name:'rsrq',
				value:9
			},{
				name:'sinr',
				value:111
			},{
				name:'sinr',
				value:112
			},{
				name:'ibler',
				value:101
			},{
				name:'beam',
				value:102
			}];
			gisToolBars.nsaAnalysis.showCell=false;
			gisToolBars.nsaAnalysis.layerManager=false;
			gisToolBars.nsaAnalysis.layerManager2=true;
			gisToolBars.nsaAnalysis.showRoadPoints=true;
			gisToolBars.nsaAnalysis.eventShow=true;//是否显示异常事件
			gisToolBars.nsaAnalysis.drawKpi=drawKpis;
			return gisToolBars.nsaAnalysis;
		}else if('103'==toolbarType){
			var drawKpis=[];
			gisToolBars.ShowPointLonAndLat.showCell=false;
			gisToolBars.ShowPointLonAndLat.showEmbbRoadPoints=false;
			gisToolBars.ShowPointLonAndLat.showDrawRectangleFrame=true;
			gisToolBars.ShowPointLonAndLat.drawKpi=drawKpis;
			return gisToolBars.ShowPointLonAndLat;
		}else if('104'==toolbarType){
			var drawKpis=[{
				name:'rsrp',
				value:109
			},{
				name:'rsrp',
				value:110
			},{
				name:'rsrq',
				value:9
			},{
				name:'sinr',
				value:111
			},{
				name:'sinr',
				value:112
			},{
				name:'ibler',
				value:101
			},{
				name:'beam',
				value:102
			}];
			gisToolBars.nsaAnalysis.showCell=false;
			gisToolBars.nsaAnalysis.layerManager=false;
			gisToolBars.nsaAnalysis.layerManager2=true;
			gisToolBars.nsaAnalysis.showRoadPoints=false;
			gisToolBars.nsaAnalysis.eventShow=false;//是否显示异常事件
			gisToolBars.nsaAnalysis.showSelectGpsPoint=true;
			gisToolBars.nsaAnalysis.drawKpi=drawKpis;
			return gisToolBars.nsaAnalysis;
		}else if('105'==toolbarType){  //单站报告采样点显示
			var drawKpis=[{
				name:'rsrp',
				value:109
			},{
				name:'rsrp',
				value:110
			},{
				name:'rsrq',
				value:9
			},{
				name:'sinr',
				value:111
			},{
				name:'sinr',
				value:112
			},{
				name:'ibler',
				value:101
			},{
				name:'beam',
				value:102
			},{
				name:'macthrputdl',
				value:113
			},{
				name:'macthrputul',
				value:114
			},{
				name:'pci',
				value:115
			}];
			gisToolBars.stationReport.showCell=false;
			gisToolBars.stationReport.layerManager=false;
			gisToolBars.stationReport.layerManager2=true;
			gisToolBars.stationReport.drawKpi=drawKpis;
			return gisToolBars.stationReport;
		}else if('106'==toolbarType){
			var drawKpis=[];
			gisToolBars.ShowPointLonAndLat.showCell=false;
			gisToolBars.ShowPointLonAndLat.showEmbbRoadPoints=false;
			gisToolBars.ShowPointLonAndLat.showFrameSelect=true;
			gisToolBars.ShowPointLonAndLat.showSelectLinePoint=true;
			gisToolBars.ShowPointLonAndLat.showDelLayer=true;
			gisToolBars.ShowPointLonAndLat.drawKpi=drawKpis;
			return gisToolBars.ShowPointLonAndLat;
		}
		else {
			return gisToolBars.none;	
		}
	}
	return gisToolBars.none;
}
/**
 * 获取小区信息的请求参数及生成小区信息的url
 * @returns 
 */
function getCellRequestParam(){
	var tlids = getCellTestLogItemIds();
	var actionURL = getCellActionUrl();
	return {
		testLogItemIds:tlids,
	    requestUrl:actionURL
	};
}
/**
 * 获取测试日志轨迹的请求参数及生成日志轨迹点的url
 * @returns 
 */
function getTestLogItemRequestParam(){
	var tlids1 = getGpsPointTestLogItemIds();
	var actionURL1 = getTestLogItemGpsPointActionUrl();
	return {
		testLogItemIds:tlids1,
	    requestUrl:actionURL1
	};
}

/**
 * 获取质差路段轨迹的请求参数及生成质差路段轨迹点的url
 */
function getQBRRequestParam(){
	var qbrid2 = getGpsPointQBRId();
	var actionURL2 = getQBRGpsPointActionUrl();
	return {
		badRoadId:qbrid2,
	    requestUrl:actionURL2
	};
}
/**
 * 获取质差路段行驶方向的请求参数及生成质差路段行驶方向的url
 */
function getQBRDirectionRequestParam(){
	var qbrid3 = getGpsPointQBRId();
	var actionURL3 = getQBRDirectionActionUrl();
	return {
		badRoadId:qbrid3,
	    requestUrl:actionURL3
	};
}
/**
 * 获取小区与邻区连线的请求参数及生成小区连线的url
 */
function getQBRCellToCellRequestParam(){
	var qbrid4 = getGpsPointQBRId();
	var actionURL4 = QBRCellToCellActionUrl();
	return {
		badRoadId:qbrid4,
	    requestUrl:actionURL4
	};
}
/**
 * 获取测试日志下所有质差路段轨迹点的请求参数及生成轨迹点的url
 */
function getTLI2QBRRequestParam(){
	var tlids5 = getTestLogItemIds2QBR();
	var actionURL5 = getTLI2QBRGpsPointActionUrl();
	return {
		testLogItemIds:tlids5,
	    requestUrl:actionURL5
	};
}


/**
 * 获取测试日志下某种异常事件轨迹点的请求参数及生成轨迹点的url
 */
function getTLI2EERequestParam(){
	var tlids6 = getTestLogItemIds2EE();
	var eeType = getEEType();
	var iconType = getIconType();
	var actionURL6 = getTLI2EEGpsPointActionUrl();
	return {
	   testLogItemIds:tlids6,//’测试日志的id按”,”分隔的字符串’,
	   eeType:eeType,//’异常事件类型’,
	   iconType:iconType,//’异常事件图标类型’,
	   requestUrl:actionURL6//’ url’
	};
}
/**
 * 获取异常事件轨迹的请求参数及生成异常事件轨迹点的url
 */
function getEERequestParam(){
	var eeid = getGpsPointEEId();
	var actionURL7 = getEEGpsPointActionUrl();
	return {
	    eeId:eeid,//异常事件的id,
	    requestUrl:actionURL7,//’ url’
	};
}
/**
 * 获取异常事件的事件图标轨迹的请求参数及生成异常事件图标轨迹点的url
 */
function getEEEventRequestParam(){
	var eeid1 = getGpsPointEEId();
	var actionURL8 = getEEEventGpsPointActionUrl();
	return {
		eeId:eeid1,//异常事件的id,
	    requestUrl:actionURL8,//’ url’
	};
}
/**
 * 获取异常事件行驶方向的请求参数及生成异常事件行驶方向的url
 */
function getEEDirectionRequestParam(){
	var eeid2 = getGpsPointEEId();
	var actionURL9 = getEEDirectionActionUrl();
	return {
		eeId:eeid2,//异常事件的id,
	    requestUrl:actionURL9,//’ url’
	};
}
/**
 * 获取测试日志下某种切换失败轨迹点的请求参数及生成轨迹点的url
 */
function getTLI2HOFRequestParam(){
	var tlids6 = getTestLogItemIds2HOF();
	var hofType = getHofType();
	var iconType = getIconType();
	var actionURL6 = getTLI2HOFGpsPointActionUrl();
	return {
	   testLogItemIds:tlids6,//’测试日志的id按”,”分隔的字符串’,
	   hofType:hofType,//’切换失败类型’,
	   iconType:iconType,//’切换失败图标类型’,
	   requestUrl:actionURL6//’ url’
	};
}
/**
 * 获取切换失败轨迹的请求参数及切换失败轨迹点的url
 */
function getHOFRequestParam(){
	var hofid = getGpsPointHofId();
	var actionURL7 = getHOFGpsPointActionUrl();
	return {
	    hofId:hofid,//切换失败的id,
	    requestUrl:actionURL7,//’ url’
	};
}
/**
 * 获取切换失败的图标轨迹的请求参数及切换失败图标轨迹点的url
 */
function getHOFEventRequestParam(){
	var hofid1 = getGpsPointHofId();
	var actionURL8 = getHOFEventGpsPointActionUrl();
	return {
		hofId:hofid1,//切换失败的id,
	    requestUrl:actionURL8,//’ url’
	};
}
/**
 * 获取切换失败行驶方向的请求参数及切换失败行驶方向的url
 */
function getHOFDirectionRequestParam(){
	var hofid2 = getGpsPointHofId();
	var actionURL9 = getHOFDirectionActionUrl();
	return {
		hofId:hofid2,//切换失败的id,
	    requestUrl:actionURL9//’ url’
	};
}
/**
 * 获取切换失败专题源小区与目标小区连线的请求参数及生成小区连线的url
 */
function getHOFCellToCellRequestParam(){
	var hofid3 = getGpsPointHofId();
	var actionURL10 = getHOFCellToCellActionUrl();
	return {
		hofId:hofid3,//切换失败的id,
	    requestUrl:actionURL10//’ url’
	};
}
/**
 * 获取测试日志下所有的volte连续无线差路段轨迹点的请求参数及生成轨迹点的url
 */
function getTLI2CWBRRequestParam(){
	var testids = getTestLogItemIds2CWBR();
	var actionURL11 = getTLI2CWBRGpsPointActionUrl();
	return {
		testLogItemIds:testids,
	    requestUrl:actionURL11
	};
}
/**
 * 获取连续无线差路段轨迹的请求参数及生成连续无线差路段轨迹点的url
 */
function getCWBRRequestParam(){
	var cwbrid2 = getGpsPointCWBRId();
	var actionURL12 = getCWBRGpsPointActionUrl();
	return {
		badRoadId:cwbrid2,
	    requestUrl:actionURL12
	};
}
/**
 * 获取连续无线差路段行驶方向的请求参数及生成连续无线差路段行驶方向的url
 */
function getCWBRDirectionRequestParam(){
	var cwbrid3 = getGpsPointCWBRId();
	var actionURL13 = getCWBRDirectionActionUrl();
	return {
		badRoadId:cwbrid3,
	    requestUrl:actionURL13
	};
}
/**
 * 获取连续无现差专题小区与邻区连线的请求参数及生成小区连线的url
 */
function getCWBRCellToCellRequestParam(){
	var cwbrid4 = getGpsPointCWBRId();
	var actionURL14 = CWBRCellToCellActionUrl();
	return {
		badRoadId:cwbrid4,
	    requestUrl:actionURL14
	};
}
/**
 * 获取测试日志下呼叫建立时延异常轨迹点的请求参数及生成轨迹点的url
 */
function getTLI2CEDERequestParam(){
	var testids = getTestLogItemIds2CEDE();
	var iconType = getIconType();
	var actionURL15 = getTLI2CEDEGpsPointActionUrl();
	return {
		testLogItemIds:testids,
		iconType:iconType,
	    requestUrl:actionURL15
	};
}
/**
 * 获取呼叫建立时延异常轨迹的请求参数及生成呼叫建立时延异常轨迹点的url
 */
function getCEDERequestParam(){
	var cedeid2 = getGpsPointCEDEId();
	var actionURL16 = getCEDEGpsPointActionUrl();
	return {
		cedeId:cedeid2,
	    requestUrl:actionURL16
	};
}
/**
 * 获取呼叫建立时延异常行驶方向的请求参数及生成呼叫建立时延异常行驶方向的url
 */
function getCEDEDirectionRequestParam(){
	var cedeid3 = getGpsPointCEDEId();
	var actionURL17 = getCEDEDirectionActionUrl();
	return {
		cedeId:cedeid3,
	    requestUrl:actionURL17
	};
}
/**
 * 获取呼叫建立时延异常的事件图标轨迹的请求参数及生成事件图标轨迹点的url
 */
function getCEDEEventRequestParam(){
	var cedeid4 = getGpsPointCEDEId();
	var actionURL18 = getCEDEEventGpsPointActionUrl();
	return {
		cedeId:cedeid4,
	    requestUrl:actionURL18
	};
}

/**
 * 获取对比测试日志轨迹的请求参数及生成对比日志轨迹点的url
 * @returns 
 */
function getCompareTestLogItemRequestParam(){
	var tlids10 = getGpsPointCompareTestLogItemIds();
	var actionURL19 = getCompareTestLogItemGpsPointActionUrl();
	return {
		testLogItemIds:tlids10,
	    requestUrl:actionURL19
	};
}

function getEmbbCoverBoshuAnalyseParam(){
	var ids = getEmbbCoverBoshuAnalyseLogItemIds();
	var url = getEmbbCoverBoshuAnalyseUrl();
	return {
		testLogItemIds:ids,
	    requestUrl:url
	};
}

/**
 * 获取对比分析栅格渲染的请求参数及生成对比分析栅格渲染的url
 * @returns 
 */
function getCompareGridRequestParam(){
	var tlids11 = getGridTestLogItemIds();
	var comparetlids = getGridCompareTestLogItemIds();
	var actionURL20 = getCompareGridActionUrl();
	return {
		testLogItemIds:tlids11,
		compareTestLogItemIds:comparetlids,
	    requestUrl:actionURL20
	};
}

/**
 * 获取对比分析MOS差黑点的请求参数及生成对比分析MOS差黑点的url
 * @returns 
 */
function getMosBadRequestParam(){
	var mosBadId = getMosBadId();
	var compareMosBadIds = getCompareMosBadIds();
	var mosBadLatitude = getMosBadLatitude();
	var mosBadLongitude = getMosBadLongitude();
	var actionURL21 = getCompareMosBadActionUrl();
	return {
	    badRoadId:mosBadId,
	    compareBadRoadIds:compareMosBadIds,
	    mosBadLatitude:mosBadLatitude,
	    mosBadLongitude:mosBadLongitude,
	    requestUrl:actionURL21
	};
}
/**
 * 获取对比分析专题切换失败指标和事件图标渲染的请求参数及url
 * @returns 
 */
function getCompareHofRequestParam(){
	var testLogItemIds = getHofTestLogItemIds();
	var compareTestLogItemIds = getCompareHofTestLogItemIds();
	var hofType = getCompareHofType();
	var cellId = getCompareHofCellId();
	var failId = getCompareHofFailId();
	var actionURL22 = getCompareHOFGpsPointActionUrl();
	return {
		testLogItemIds:testLogItemIds,
		compareTestLogItemIds:compareTestLogItemIds,
		hofType:hofType,
		cellId:cellId,
		failId:failId,
	    requestUrl:actionURL22
	};
}
/**
 * 获取对比分析专题切换失败源小区与目标小区连线的请求参数及url
 * @returns 
 */
function getCompareHofCellToCellRequestParam(){
	var testLogItemIds = getHofTestLogItemIds();
	var compareTestLogItemIds = getCompareHofTestLogItemIds();
	var hofType = getCompareHofType();
	var cellId = getCompareHofCellId();
	var failId = getCompareHofFailId();
	var actionURL23 = getCompareHOFCellToCellActionUrl();
	return {
		testLogItemIds:testLogItemIds,
		compareTestLogItemIds:compareTestLogItemIds,
		hofType:hofType,
		cellId:cellId,
		failId:failId,
	    requestUrl:actionURL23
	};
}
/**
 * 获取实时地图监控实时刷新轨迹的请求参数及url
 */
function getGPSRefreshRequestParam(){
	var tids = getPointTerminalIds();
	var actionURL24 = getTerminalGpsPointActionUrl();
	return {
		boxIDs:tids,
	    requestUrl:actionURL24
	};
}
/**
 * 获取实时地图监控历史事件轨迹点信息的请求参数及url
 */
function getGPSEventRequestParam(){
	var tids = getHistoryGpsPointTerminalIds();
	var actionURL25 = getEventHistoryGpsPointActionUrl();
	return {
		boxIDs:tids,
	    requestUrl:actionURL25
	};
}
/**
 * 获取实时地图监控实时事件轨迹点信息的请求参数及url
 */
function getGPSAtmomentEventRequestParam(){
	var tids = getPointTerminalIds();
	var actionURL26 = getAtmomentEventGpsPointActionUrl();
	return {
		boxIDs:tids,
	    requestUrl:actionURL26
	};
}
/**
 * 获取楼宇不同指标显示url
 */
function getFloorInfoByIndexRequestParam(){
	var actionURL27 = getFloorInfoByIndexPointActionUrl();
	var stateInfo=getStateInfo();
	return {
		state:stateInfo,
		requestUrl:actionURL27
	};
}
/**
 * 获取查询楼宇信息URL
 */
function getFloorInfoRequestParam(){
	var actionURL28 = getgetFloorInfoPointActionUrl();
	return {
		requestUrl:actionURL28
	};
}
/**
 * 获取测试日志下所有RTP连续丢包路段轨迹点的请求参数及生成轨迹点的url
 */
function getTLI2LPRequestParam(){
	var tlids12 = getTestLogItemIds2LP();
	var actionURL29 = getTLI2LPGpsPointActionUrl();
	return {
		testLogItemIds:tlids12,
	    requestUrl:actionURL29
	};
}
/**
 * 获取视频质差轨迹的请求参数及生成质差轨迹点的url
 */
function getVideoQBRRequestParam(){
	var videoqbid1 = getGpsPointVQBId();
	var actionURL30 = getVQBGpsPointActionUrl();
	return {
		videoQualityBadId:videoqbid1,
	    requestUrl:actionURL30
	};
}
/**
 * 获取视频质差行驶方向的请求参数及生成质差行驶方向的url
 */
function getVideoQBRDirectionRequestParam(){
	var videoqbid2 = getGpsPointVQBId();
	var actionURL31 = getVQBDirectionActionUrl();
	return {
		videoQualityBadId:videoqbid2,
	    requestUrl:actionURL31
	};
}
/**
 * 获取视频质差小区与邻区连线的请求参数及生成小区连线的url
 */
function getVideoQBRCellToCellRequestParam(){
	var videoqbid3 = getGpsPointVQBId();
	var actionURL32 = VQBCellToCellActionUrl();
	return {
		videoQualityBadId:videoqbid3,
	    requestUrl:actionURL32
	};
}
/**
 * 获取测试日志下所有视频质差轨迹点的请求参数及生成轨迹点的url
 */
function getVideoQBRequestParam(){
	var testlogids = getTestLogItemIds2VQB();
	var actionURL33 = getTLI2VQBGpsPointActionUrl();
	return {
		testLogItemIds:testlogids,
	    requestUrl:actionURL33
	};
}
/**
 * 获取某个视频质差LTE切换事件轨迹的请求参数及生成质差轨迹点的url
 */
function getVideoQBLTERequestParam(){
	var videoqbid4 = getGpsPointVQBId();
	var actionURL34 = getVQBLTEGpsPointActionUrl();
	return {
		videoQualityBadId:videoqbid4,
	    requestUrl:actionURL34
	};
}
/**
 * 获取流媒体视频质差轨迹的请求参数及生成质差轨迹点的url
 */
function getStreamQBRRequestParam(){
	var streamqbid1 = getGpsPointSVQBId();
	var actionURL35 = getSVQBGpsPointActionUrl();
	return {
		videoQualityBadId:streamqbid1,
		requestUrl:actionURL35
	};
}
/**
 * 获取流媒体视频质差行驶方向的请求参数及生成质差行驶方向的url
 */
function getStreamQBRDirectionRequestParam(){
	var streamqbid2 = getGpsPointSVQBId();
	var actionURL36 = getSVQBDirectionActionUrl();
	return {
		videoQualityBadId:streamqbid2,
		requestUrl:actionURL36
	};
}
/**
 * 获取流媒体视频质差小区与邻区连线的请求参数及生成小区连线的url
 */
function getStreamQBRCellToCellRequestParam(){
	var streamqbid3 = getGpsPointSVQBId();
	var actionURL37 = SVQBCellToCellActionUrl();
	return {
		videoQualityBadId:streamqbid3,
		requestUrl:actionURL37
	};
}
/**
 * 获取测试日志下所有流媒体视频质差轨迹点的请求参数及生成轨迹点的url
 */
function getStreamQBRequestParam(){
	var testlogids2 = getTestLogItemIds2SVQB();
	var actionURL38 = getTLI2SVQBGpsPointActionUrl();
	return {
		testLogItemIds:testlogids2,
		requestUrl:actionURL38
	};
}
/**
 * 获取某个流媒体视频质差LTE切换事件轨迹的请求参数及生成质差轨迹点的url
 */
function getStreamQBLTERequestParam(){
	var streamqbid4 = getGpsPointSVQBId();
	var actionURL39 = getSVQBLTEGpsPointActionUrl();
	return {
		videoQualityBadId:streamqbid4,
		requestUrl:actionURL39
	};
}

/**
 * 获取小区的最近邻区名称
 */
function getNcellNameRequestParam(){
	var actionURL40 = getNcellNameActionUrl();
	return {
		requestUrl:actionURL40
	};
}

/**
 * 获取地图轨迹点图例
 */
function getDefaultMapLegendUrl(){
	var actionURL41 = getParamColorUrl();
	return {
		requestUrl:actionURL41
	};
}

/**
 * 保存地图轨迹点图例
 */
function saveMapLegendUrl(){
	var actionURL42 = getSaveMapLegendUrl();
	return {
		requestUrl:actionURL42
	};
}