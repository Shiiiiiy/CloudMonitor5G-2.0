<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>日志上传</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="${pageContext.request.contextPath}/css/stream-v1.css" rel="stylesheet" type="text/css">
	
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/stream-v1.js"></script>
	<script type="text/javascript">
		var _t;
		function beginUpload(){
			if($("#saveForm").form('validate')){
				var boxId = $("#boxIds").combobox('getValue');
				config.postVarsPerFile.boxId=boxId;
				_t.upload();
			}
		}
		/**
		 * 配置文件（如果没有默认字样，说明默认值就是注释下的值）
		 * 但是，on*（onSelect， onMaxSizeExceed...）等函数的默认行为
		 * 是在ID为i_stream_message_container的页面元素中写日志
		 */
		var config = {
			browseFileId : "i_select_files", /** 选择文件的ID, 默认: i_select_files */
			browseFileBtn : "<div><strong>请点击这里选取多个日志文件</strong></br>按住ctrl或者shift多选,ctrl+a全选</div>", /** 显示选择文件的样式, 默认: `<div>请选择文件</div>` */
			dragAndDropArea: "i_select_files", /** 拖拽上传区域，Id（字符类型"i_select_files"）或者DOM对象, 默认: `i_select_files` */
			dragAndDropTips: "<div></div>", /** 拖拽提示, 默认: `<span>把文件(文件夹)拖拽到这里</span>` */
			filesQueueId : "i_stream_files_queue", /** 文件上传容器的ID, 默认: i_stream_files_queue */
			filesQueueHeight : 400, /** 文件上传容器的高度（px）, 默认: 450 */
			messagerId : "i_stream_message_container", /** 消息显示容器的ID, 默认: i_stream_message_container */
			multipleFiles:true, /** 多个文件一起上传, 默认: false */
			autoUploading: false, /** 选择文件后是否自动上传, 默认: true */
			autoRemoveCompleted : true, /** 是否自动删除容器中已上传完毕的文件, 默认: false */
			maxSize: 1048576000,//1000M, /** 单个文件的最大大小，默认:2G */
	//		retryCount : 5, /** HTML5上传失败的重试次数 */
			postVarsPerFile : { /** 上传文件时传入的参数，默认: {} */
	//			param1: "val1",
	//			param2: "val2"
			},
			swfURL : "${pageContext.request.contextPath}/swf/FlashUploader.swf", /** SWF文件的位置 */
			tokenURL : "${pageContext.request.contextPath}/logUpload/token.action", /** 根据文件名、大小等信息获取Token的URI（用于生成断点续传、跨域的令牌） */
		//	frmUploadURL : "${pageContext.request.contextPath}/uploadFile/formupload.servlet", /** Flash上传的URI */
			frmUploadURL : "${pageContext.request.contextPath}/logUpload/formUploadFile.action?", /** Flash上传的URI */
			//uploadURL : "http://localhost:8080/mos/file/upload.action?myfileType=sampleFile", /** HTML5上传的URI */
			uploadURL : "${pageContext.request.contextPath}/logUpload/html5UploadFile.action", /** HTML5上传的URI */
			simLimit:10, /** 单次最大上传文件个数 */
			extFilters: [".l5g",".CU"], /** 允许的文件扩展名, 默认: [] */
	//		onSelect: function(list) {alert('onSelect')}, /** 选择文件后的响应事件 */
	//		onMaxSizeExceed: function(size, limited, name) {alert('onMaxSizeExceed')}, /** 文件大小超出的响应事件 */
	//		onFileCountExceed: function(selected, limit) {alert('onFileCountExceed')}, /** 文件数量超出的响应事件 */
	//		onExtNameMismatch: function(name, filters) {alert('onExtNameMismatch')}, /** 文件的扩展名不匹配的响应事件 */
	//		onCancel : function(file) {alert('Canceled:  ' + file.name)}, /** 取消上传文件的响应事件 */
			onComplete: function(file) {
				//alert(file.XHR.responseText);
				$("#returnOld").linkbutton('disable');
				fShowMessage1("文件:" + file.name + ",大小:" + file.size + "Byte,上传完成[ok]");
				//alert('onComplete');
			}, /** 单个文件上传完毕的响应事件 */
			onQueueComplete: function() {
				fShowMessage1("所有文件上传完成	====>	[OK]");
				$("#returnOld").linkbutton('enable');
				$.messager.alert('提示','所有文件上传完成','info',function(){
					goToPage('${pageContext.request.contextPath}/logUpload/listUI.action');
				});
				//_t.showBrowseBlock();
				//_t.destroy();
				//_t = new Stream(config); 
			}, /** 所以文件上传完毕的响应事件 */
			onUploadError: function(status, msg) {
				var objMsg = eval("("+msg+")");
				fShowMessage1("上传错误.响应状态:" + status + ", 错误提示: " + objMsg.massage, true);
			} /** 文件上传出错的响应事件 */
		};
		
		function fShowMessage1(msg, warning) {
			var o = document.getElementById("i_stream_message_container");
			o && (o.innerHTML += "<br>" + (!!warning ? ("<span style='color:red;'>" + msg + "</span>"): msg));
		}
	 	window.onload = function() {
	 		_t = new Stream(config);
			if (!window.applicationCache) {
				//IE中判断是否安装插件
				try{
					new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
				}catch(e){
					$.messager.alert('警告','因采用FLASH/HTML5上传,该浏览器未安装FLASH插件!请安装FLASH插件或改用支持HTML5的浏览器!','error',function(){
						window.open("https://get2.adobe.com/cn/flashplayer/");
					});
				}
				//var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
				//if(!swf){
				//	$.messager.alert('警告','因采用FLASH/HTML5上传,该浏览器未安装FLASH插件!请安装FLASH插件或改用支持HTML5的浏览器!','error');
				//}
				
			}        
		};
	
	/* 菜单树只能选中city级别的node */
	function onbeforeselect(node){
		if(node.attributes.type=='City'){
			//加载终端下拉框
			var cityIds = [];
			cityIds.push(node.attributes.refId);
			var cityIdsString = cityIds.join(",");
			$("#boxIds").combobox({
				url:'${pageContext.request.contextPath}/terminalGroup/terminalTree.action?cityIds='+cityIdsString,
				method: 'post',
				panelWidth:300,
				editable:false,
				multiple:false,
				valueField:'value',
				textField:'text',
				groupField:'group'
			});
			return true;
		}
		return false;
	}
	//所有文件上传完成时通知后台解析
	function goSocket(){
		$.post("${pageContext.request.contextPath}/networkTestLogItem/goSocket.action",{},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} 
				}
			,"json");
	}
	function initialize(){
		_t = new Stream(config);
		alert("laila");
	}
	</script>
  </head>
  
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow-x:hidden;overflow-y:auto;">
		<!-- <h3 style="border-bottom: 1px solid #95B8E7;color: #3366CC;font-size: 16px;font-weight:800;height:32px;padding: 0 10px;">筛选参数设置</h3> -->
    	<!-- <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">网络侧日志上传</div></div> -->
    	<div style="width:500px;" data-options="region:'east',split:false,border:false">
	    	<form id="saveForm" method="post" enctype="multipart/form-data">
		    	<table>
		    		<tr style="height:400px;">
		    			<td width="100px"  align="right" valign="top">
							<span class="label" style="font-size:12px;line-height:26px;">所属域：</span>
						</td>
		    			<td width="300px" height="400px" colspan="4" align="left">
							<div class="easyui-panel" fit="true" style="overflow: auto;">
								<ul id="areaTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',onBeforeSelect:onbeforeselect,multiple:false,lines:true"></ul>
							</div>
						</td>
		    		</tr>
		    		<tr style="height:30px;">
		    			<td width="100px" align="right">
			    			<span class="label" style="font-size:12px;line-height:26px;">测试终端：</span>
						</td>
						<td width="300px" align="left">
							<select id="boxIds" style="width:300px;" name="boxIds" data-options="required:true,editable:false" class="easyui-combobox"  ></select>
						</td>
		    		</tr>
		    		<tr style="height:30px;">
		    			<td colspan="2" align="center">
							<a class="easyui-linkbutton" id="beginUpload" style="width:80px;"  data-options="iconCls:'icon-upload'" onclick="javascript:beginUpload();">开始上传</a>
							<a id="returnOld" class="easyui-linkbutton" onclick="javascript:goBack();" data-options="iconCls:'icon-undo'" style="width:80px;" >返回</a>
						</td>
		    		</tr>
		    	</table>
	    	</form>
	    	
    	</div>
    	<div data-options="region:'center',split:false,border:false" style="padding:5px;">
			<div id="i_select_files" style="width: 99%;text-align: center">
			</div>
			<div id="i_stream_files_queue" style="width:99%;">
			</div>
			<strong>文件上传反馈:</strong>
			<div id="i_stream_message_container" class="stream-main-upload-box" style="overflow:auto;height:30%;width: 99%;">
			</div>
		</div>
  </body>
</html>
