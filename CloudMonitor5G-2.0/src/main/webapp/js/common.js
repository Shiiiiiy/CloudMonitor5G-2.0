/**
 * 项目共用js
 * @author yinzhipeng
 * @date:2015年9月16日 上午11:33:23
 * @version
 */

/**
 * 跳转到url的page
 * @param url
 */
function goToPage(url){
	self.window.location=url;
}
/**
 * 返回上一页
 */
function goBack(){
	self.window.history.go(-1);
}
/**
 * 创建Iframe
 * @param url
 * @returns String
 */
function createIframe(url){
	return '<iframe src="'+url+'" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;" ></iframe>';
}
/**
 * 当选中tab的时候重新加载该tab中的内容
 * @param title
 * @param index
 */
function reloadTab(title,index){
	var target = this;
	var currTab =  $(target).tabs('getSelected'); 
	var url = $(currTab.panel('options').content).attr('src');
	$(target).tabs('update', {
		tab : currTab,
		options : {
			content : createIframe(url)
		}
	}); 
} 
/**
 * 添加tab
 * @param obj为html的dom必须具备url=""和text,obj的优先级高于url和tabTitle两个参数
 * @param url为创建tab的url
 * @param tabTitle为创建tab的标题
 * @param tabObjId为tab的id默认使用id为'mytabs'
 */
function addTab(obj,url,tabTitle,tabObjId) {
	var mytabs = $("#mytabs");
	if(tabObjId){
		mytabs=$("#"+tabObjId);
	}
	if(obj){
		url = $(obj).attr('url');
		tabTitle = $(obj).text();
	}
	if (mytabs.tabs('exists', tabTitle)) {
		mytabs.tabs('select', tabTitle);
	} else {
		mytabs.tabs('add',{
			title : tabTitle,
			content : createIframe(url),
			closable : true
		});
	}
}
/**
 * 关闭tab
 * @param tabTitle 为要删除tab的标题
 * @param tabObjId 为tab的id默认使用id为'mytabs'
 */
function closeTab(tabTitle,tabObjId){
	var mytabs = $("#mytabs");
	if(tabObjId){
		mytabs=$("#"+tabObjId);
	}
	if (mytabs.tabs('exists', tabTitle)) {
		mytabs.tabs('close', tabTitle);
	}
}
/**
 * 选中tab
 * @param tabTitle 为要选中tab的标题
 * @param tabObjId 为tab的id默认使用id为'mytabs'
 */
function selectTab(tabTitle,tabObjId){
	var mytabs = $("#mytabs");
	if(tabObjId){
		mytabs=$("#"+tabObjId);
	}
	if (mytabs.tabs('exists', tabTitle)) {
		mytabs.tabs('select', tabTitle);
	}
}
/**
 * 敬请关注弹出框
 */
function alertAttention(){
	$.messager.alert("提示","该功能暂未实现,即将推出,敬请关注!","info");
}
function messagerAlert(title,text,icon){
	$.messager.alert(title,text,icon);
}

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
 var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
}
/**
 * 弹出框拖动范围限定
 */
function onDrag(moveLeft,moveTop){
	var moveFlag = false;
	if (moveLeft < 0){
		moveLeft = 0;
		moveFlag=true;
	}
	if (moveTop < 0){
		moveTop = 0;
		moveFlag=true;
	}
	if (moveLeft + $(this).parent().outerWidth() > $(this).parent().parent().width()){
		moveLeft = $(this).parent().parent().width() - $(this).outerWidth()-12;
		moveFlag=true;
	}
	if (moveTop + $(this).parent().outerHeight() > $(this).parent().parent().height()){
		moveTop = $(this).parent().parent().height() - $(this).parent().outerHeight();
		moveFlag=true;
	}
	if(moveFlag){
		var that = this;
		setTimeout(function(){
			$(that).dialog('move',{left:moveLeft,top:moveTop});
		},100);
	}
}