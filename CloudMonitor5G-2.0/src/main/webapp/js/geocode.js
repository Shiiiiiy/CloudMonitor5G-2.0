/**
 * gecode质差路段路段名称
 * @author yinzhipeng
 * @date 2016年02月23日 下午午16:34:23
 * @version
 */

//easyui datagrid rows
var tableRows;
//tableRows index
var forindex = 0;
//BMap AK
var bMapAk = 'liKpDfLP41rNnZmM1D33WljN';
//table row id
var rowIDs = [];
//table row name
var rowRoadNames = [];
/**
 * easyui datagrid data 加载完成geocode质差路的名称
 * @param tableData: datagrid data
 */
function mainTableLoadSuccess(tableData){
	tableRows = tableData.rows;
	if(tableRows&&0!=tableRows.length){
		//火狐和google浏览器无法跨域浏览
		jQuery.support.cors = true;
		geocodeRoadName(tableRows[forindex]);
	}
}

/**
 * 
 * 调用百度地图接口geocode质差路段名称
 * @param tableRowData: datagrid data rows
 */
function geocodeRoadName(tableRowData){
	
	//路段名称为空才执行解析
	if(!tableRowData.m_stRoadName){
		 //地理编码路段经纬度优先级:1开始经纬度,2中间经纬度,3结束经纬度,4没有经纬度
		if (tableRowData.beginLatitude && tableRowData.beginLongitude
				&& 0 != tableRowData.beginLatitude && 0 != tableRowData.beginLongitude) {
			//1开始经纬度
			 $.ajax({      
		 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
		    	type:'get',     //from:1gps,3google 
		    	async : false, //默认为true 异步
		    	data:{ak:bMapAk,location:tableRowData.beginLatitude+','+tableRowData.beginLongitude},
		    	cache: true, 
		    	crossDomain: true,   
				jsonpCallback:"geocodeSearch",
				dataType:'jsonp'
			}); 
		}else{
			if (tableRowData.courseLatitude && tableRowData.courseLongitude
					&& 0 != tableRowData.courseLatitude && 0 != tableRowData.courseLongitude) {
				//2中间经纬度
				$.ajax({      
			 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
			    	type:'get',     //from:1gps,3google 
			    	async : false, //默认为true 异步
			    	data:{ak:bMapAk,location:tableRowData.courseLatitude+','+tableRowData.courseLongitude},
			    	cache: true, 
			    	crossDomain: true,   
					jsonpCallback:"geocodeSearch",
					dataType:'jsonp'
				}); 
			}else{
				if (tableRowData.endLatitude && tableRowData.endLongitude
						&& 0 != tableRowData.endLatitude && 0 != tableRowData.endLongitude) {
					//3结束经纬度
					$.ajax({      
				 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
				    	type:'get',     //from:1gps,3google 
				    	async : false, //默认为true 异步
				    	data:{ak:bMapAk,location:tableRowData.endLatitude+','+tableRowData.endLongitude},
				    	cache: true, 
				    	crossDomain: true,   
						jsonpCallback:"geocodeSearch",
						dataType:'jsonp'
					}); 
				}else{
					//4没有经纬度
					//直接执行下一条
					forindex++;
					if(forindex < tableRows.length){
						geocodeRoadName(tableRows[forindex]);
					}else{
						forindex=0;
						$.ajax({      
					 		url:getRoadNameCallBackUrl(),
					    	type:'post', 
					    	async : true, //默认为true 异步
					    	traditional:true,
					    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
					    	cache: true, 
							dataType:'text'
						}); 
					}
				}
			}
		}
	}else{
		//路段名称不为空直接执行下一条
		forindex++;
		if(forindex < tableRows.length){
			geocodeRoadName(tableRows[forindex]);
		}else{
			forindex=0;
			$.ajax({      
		 		url:getRoadNameCallBackUrl(),
		    	type:'post', 
		    	async : true, //默认为true 异步
		    	traditional:true,
		    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
		    	cache: true, 
				dataType:'text'
			}); 
		}
	}
} 

/**
 * 
 * 解析百度地图返回的地址信息,回填datagrid row,此方法会调用全局变量mainTableId,页面需要指定mainTableId变量
 * @param resultData: 百度地图gecode返回的地理化信息
 */
function geocodeSearch(resultData){
	if (0 == resultData.status) {
		//geocode成功,解析返回数据获取地址
		if (resultData.result.addressComponent) {
			var rowIndex = $('#'+mainTableId).datagrid('getRowIndex',tableRows[forindex]);
			var rowID = tableRows[forindex].id;
			var rowRoadName = resultData.result.addressComponent.city + resultData.result.addressComponent.district + resultData.result.addressComponent.street;
			rowIDs.push(rowID);
			rowRoadNames.push(rowRoadName);
			if(rowIndex||0==rowIndex){
				$('#'+mainTableId).datagrid('updateRow',{
			    	index: rowIndex,
			    	row: {
			    		m_stRoadName:rowRoadName
			    	}
			    });
			}
		}
	}
	forindex++;
	if(forindex < tableRows.length){
		geocodeRoadName(tableRows[forindex]);
	}else{
		forindex=0;
		$.ajax({      
	 		url:getRoadNameCallBackUrl(),
	    	type:'post', 
	    	async : true, //默认为true 异步
	    	traditional:true,
	    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
	    	cache: true, 
			dataType:'text'
		}); 
	}
} 


//easyui datagrid rows
var tableRows1;
//tableRows index
var forindex1 = 0;
//table row id
var rowIDs1 = [];
//table row name
var rowRoadNames1 = [];
/**
 * easyui datagrid data 加载完成geocode质差路的名称
 * @param tableData: datagrid data
 */
function mainTableLoadSuccess1(tableData1){
	tableRows1 = tableData1.rows;
	if(tableRows1&&0!=tableRows1.length){
		//火狐和google浏览器无法跨域浏览
		jQuery.support.cors = true;
		geocodeRoadName1(tableRows1[forindex1]);
	}
}

/**
 * 
 * 调用百度地图接口geocode质差路段名称
 * @param tableRowData: datagrid data rows
 */
function geocodeRoadName1(tableRowData1){
	
	//路段名称为空才执行解析
	if(!tableRowData1.m_stRoadName){
		 //地理编码路段经纬度优先级:1开始经纬度,2中间经纬度,3结束经纬度,4没有经纬度
		if (tableRowData1.beginLatitude && tableRowData1.beginLongitude
				&& 0 != tableRowData1.beginLatitude && 0 != tableRowData1.beginLongitude && navigator.onLine) {
			//1开始经纬度
			 $.ajax({      
		 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
		    	type:'get',     //from:1gps,3google 
		    	async : false, //默认为true 异步
		    	data:{ak:bMapAk,location:tableRowData1.beginLatitude+','+tableRowData1.beginLongitude},
		    	cache: true, 
		    	crossDomain: true,   
				jsonpCallback:"geocodeSearch1",
				dataType:'jsonp'
			}); 
		}else{
			if (tableRowData1.courseLatitude && tableRowData1.courseLongitude
					&& 0 != tableRowData1.courseLatitude && 0 != tableRowData1.courseLongitude && navigator.onLine) {
				//2中间经纬度
				$.ajax({      
			 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
			    	type:'get',     //from:1gps,3google 
			    	async : false, //默认为true 异步
			    	data:{ak:bMapAk,location:tableRowData1.courseLatitude+','+tableRowData1.courseLongitude},
			    	cache: true, 
			    	crossDomain: true,   
					jsonpCallback:"geocodeSearch1",
					dataType:'jsonp'
				}); 
			}else{
				if (tableRowData1.endLatitude && tableRowData1.endLongitude
						&& 0 != tableRowData1.endLatitude && 0 != tableRowData1.endLongitude && navigator.onLine) {
					//3结束经纬度
					$.ajax({      
				 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
				    	type:'get',     //from:1gps,3google 
				    	async : false, //默认为true 异步
				    	data:{ak:bMapAk,location:tableRowData1.endLatitude+','+tableRowData1.endLongitude},
				    	cache: true, 
				    	crossDomain: true,   
						jsonpCallback:"geocodeSearch1",
						dataType:'jsonp'
					}); 
				}else{
					//4没有经纬度
					//直接执行下一条
					forindex1++;
					if(forindex1 < tableRows1.length){
						geocodeRoadName1(tableRows1[forindex1]);
					}else{
						forindex1=0;
						$.ajax({      
					 		url:getRoadNameCallBackUrl1(),
					    	type:'post', 
					    	async : true, //默认为true 异步
					    	traditional:true,
					    	data:{rowIDs:rowIDs1,rowRoadNames:rowRoadNames1},
					    	cache: true, 
							dataType:'text'
						}); 
					}
				}
			}
		}
	}else{
		//路段名称不为空直接执行下一条
		forindex1++;
		if(forindex1 < tableRows1.length){
			geocodeRoadName1(tableRows1[forindex1]);
		}else{
			forindex1=0;
			$.ajax({      
		 		url:getRoadNameCallBackUrl1(),
		    	type:'post', 
		    	async : true, //默认为true 异步
		    	traditional:true,
		    	data:{rowIDs:rowIDs1,rowRoadNames:rowRoadNames1},
		    	cache: true, 
				dataType:'text'
			}); 
		}
	}
} 

/**
 * 
 * 解析百度地图返回的地址信息,回填datagrid row,此方法会调用全局变量mainTableId1,页面需要指定mainTableId1变量
 * @param resultData1: 百度地图gecode返回的地理化信息
 */
function geocodeSearch1(resultData1){
	if (0 == resultData1.status) {
		//geocode成功,解析返回数据获取地址
		if (resultData1.result.addressComponent) {
			var rowIndex1 = $('#'+mainTableId1).datagrid('getRowIndex',tableRows1[forindex1]);
			var rowID1 = tableRows1[forindex1].id;
			var rowRoadName1 = resultData1.result.addressComponent.city + resultData1.result.addressComponent.district + resultData1.result.addressComponent.street;
			rowIDs1.push(rowID1);
			rowRoadNames1.push(rowRoadName1);
			if(rowIndex1||0==rowIndex1){
				$('#'+mainTableId1).datagrid('updateRow',{
			    	index: rowIndex1,
			    	row: {
			    		m_stRoadName:rowRoadName1
			    	}
			    });
			}
		}
	}
	forindex1++;
	if(forindex1 < tableRows1.length){
		geocodeRoadName1(tableRows1[forindex1]);
	}else{
		forindex1=0;
		$.ajax({      
	 		url:getRoadNameCallBackUrl1(),
	    	type:'post', 
	    	async : true, //默认为true 异步
	    	traditional:true,
	    	data:{rowIDs:rowIDs1,rowRoadNames:rowRoadNames1},
	    	cache: true, 
			dataType:'text'
		}); 
	}
} 

/**
 * easyuide datagrid的单元格保留两位小数
 * @param value
 * @param row
 * @param index
 * @returns
 */
function numToFixed2Formatter(value,row,index){
	if(isNaN(value)){
		return value;
	}
	if (value&&"0"!=value){
		return new Number(value).toFixed(2);
	}else if("0"==value){
		return 0;
	}
	return value;
}
/**
 * easyuide datagrid的单元格保留四位小数
 * @param value
 * @param row
 * @param index
 * @returns
 */
function numToFixed4Formatter(value,row,index){
	if(isNaN(value)){
		return value;
	}
	if (value&&"0"!=value){
		return new Number(value).toFixed(4);
	}else if("0"==value){
		return 0;
	}
	return value;
}
/**
 * easyuide datagrid的单元格数据乘100保留两位小数
 * @param value
 * @param row
 * @param index
 * @returns
 */
function numMultiply100ToFixed2Formatter(value,row,index){
	if(isNaN(value)){
		return value;
	}
	if (value&&"0"!=value){
		return new Number(value*100).toFixed(2);
	}else if("0"==value){
		return 0;
	}
	return value;
}
/**
 * easyuide datagrid的单元格数据除1000
 * @param value
 * @param row
 * @param index
 * @returns
 */
function numDivide1000Formatter(value,row,index){
	if(isNaN(value)){
		return value;
	}
	if (value&&"0"!=value){
		return value/1000;
	}else if("0"==value){
		return 0;
	}
	return value;
}
/**
 * easyuide datagrid的单元格数据转换为16进制
 * @param value
 * @param row
 * @param index
 * @returns
 */
function num16Formatter(value,row,index){
	if(isNaN(value)){
		return value;
	}
	if (value&&"0"!=value){
		//取绝对值
		var str=Math.abs(value);
		//var ss=parseInt(value,16)
		return str.toString(16);
	}else if("0"==value){
		return 0;
	}
	return value;
	//return return parseInt(value,16);
}
/*
  * easyuide datagrid的单元格数据除1000保留三位
 * @param value
 * @param row
 * @param index
 * @returns 
 */
function numDivide1000ToFixed3Formatter(value,row,index){
//	if(isNaN(value)){
//		return value;
//	}
//	if (value&&"0"!=value){
//		return new Number(value).toFixed(3);
//	}else if("0"==value){
//		return 0;
//	}
	return value;
}
/**
 * easyui datagrid的单元格提示
 * @param value
 * @param row
 * @param index
 * @returns
 */
function showTooltip(value,row,index){
	if (value){
		return '<div title="'+value+'" >'+value+'</div>';
	}
	return value;
}
