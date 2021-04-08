<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	var commandId = ${command.id};
	$(document).ready( function() {
		commandContext.showElements(commandId);
		$('#commandId').val(commandContext.getName(commandId));
		if ($('#commandName').val() == null || $('#commandName').val() == '') {
			$('#commandName').val($('#commandId').val());
		}
	});
	/* 选中请选择被叫,设置呼叫号码 */
	function setCallNo(modulePhoneNum){
		var phoneNum = modulePhoneNum.text.substring(modulePhoneNum.text.indexOf("-")+1);
		if(phoneNum){
			$("#commandCallNumber").textbox('setValue',phoneNum);
			$("#commandPhoneNumber").textbox('setValue',phoneNum);
		}
	}
</script>
<form id="commandForm" method="post" action="${pageContext.request.contextPath}/testCommand/saveCommand.action">
	<div id="tabs-1" style="overflow: auto; zoom: 1">
		<div style="display: none;" id="id">
			命令id
			<input type="text" id="commandId" readonly="readonly" style="background-color: #cccccc" />
			<input type="hidden" name="command.id" value="${command.id}" />
		</div>
		<div class="inputDivShow" id="name">
			<font color="red">*</font>
			名称
			<input class="easyui-textbox" data-options="required:true,validType:'length[1,50]'" name="command.name" value="${command.name}" id="commandName" />
		</div>
		<div class="inputDivShow" id="repeat">
			<font color="red">*</font>
			测试次数
			<input class="easyui-numberbox" data-options="required:true,min:1,max:65535" name="command.repeat" value="${command.repeat}" id="commandReapet" />
		</div>
		<div class="inputDiv" id="callNumber">
			<font color="red">*</font>
			呼叫号码
			<input class="easyui-textbox" data-options="required:true" name="command.callNumber" value="${command.callNumber}" disabled="true" id="commandCallNumber" />
		</div>
		
		<div class="inputDiv" id="ie">
			IE
			<input class="easyui-textbox" data-options="readonly:true" value = "1" id="commandIe"/>
		</div>
		<div class="inputDiv" id="firstServiceEndDelay">
			<font color="red">*</font>
			语音业务结束多久以后数据业务结束
			<input class="easyui-numberbox" style="width:50px" name="command.firstServiceEndDelay" value="${command.firstServiceEndDelay}" id="commandFirstServiceEndDelay" data-options="required:true,min:1,max:999" disabled="true"/>
		</div>
		<div class="inputDiv" id="secondServiceStartDelay">
			<font color="red">*</font>
			数据业务开始多久以后语音呼叫
			<input class="easyui-numberbox" style="width:50px" name="command.secondServiceStartDelay" value="${command.secondServiceStartDelay}" id="commandSecondServiceStartDelay" data-options="required:true,min:1,max:999" disabled="true" />
		</div>
		
		<div class="inputDiv" id="callName">
			账户昵称
			<input class="easyui-textbox" id="commandCallName" name="command.callName" value="${command.callName}" disabled="true" />
		</div>
		
		<div class="inputDiv" id="callModel">
			通话模式
			<select class="easyui-combobox" data-options="editable:false<c:if test="${command.callModel!=null}">,value:'${command.callModel}'</c:if>" name="command.callModel" disabled="true">
				<option value="0">微信语音通话</option>
				<option value="1">微信视频通话</option>
			</select>
		</div>
		
		<div class="inputDiv" id="randomCall">
			随机通话时长
			<select class="easyui-combobox" data-options="editable:false<c:if test="${command.randomCall!=null}">,value:'${command.randomCall}'</c:if>" name="command.randomCall" disabled="true">
				<option value="0">固定通话时长</option>
				<option value="1">随机通化时长</option>
			</select>
		</div>
		<div class="inputDiv" id="duration">
			固定通话时长(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535" data-options="min:1,max:65535" name="command.duration" value="${command.duration}" disabled="true" />
		</div>
		<div class="inputDiv" id="interval">
			<font color="red">*</font>
			呼叫间隔(秒)
			<input class="easyui-numberbox" data-options="required:true,min:1,max:65535" data-options="min:1,max:65535" name="command.interval" id="txtInterval" value="${command.interval}" disabled="true" />
		</div>
		<div class="inputDiv" id="testMOS">
			是否MOS测试
			<select class="easyui-combobox" data-options="editable:false<c:if test="${command.testMOS!=null}">,value:'${command.testMOS}'</c:if>" name="command.testMOS" disabled="true">
				<option value="0">不做MOS测试</option>
				<option value="1">进行MOS测试</option>
			</select>
		</div>
		<div class="inputDiv" id="maxTime">
			最长通话时长(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.maxTime" value="${command.maxTime}" disabled="true" />
		</div>
		<!-- <div class="inputDiv" id="callMOSServer">
			呼叫转移
			<select class="easyui-combobox" data-options="editable:false<c:if test="${command.callMOSServer!=null}">,value:'${command.callMOSServer}'</c:if>" name="command.callMOSServer" disabled="true">
				<option value="0">电话遇忙转移</option>
				<option value="1">无条件转移</option>
				<option value="1">无应答转移</option>
			</select>
		</div> -->
		<div class="inputDiv" id="callMOSServer">
			是否呼叫语音评估服务器
			<select class="easyui-combobox" style="width:80px;" data-options="editable:false<c:if test="${command.callMOSServer!=null}">,value:'${command.callMOSServer}'</c:if>" name="command.callMOSServer" disabled="true">
				<option value="0">不呼叫</option>
				<option value="1">呼叫</option>
			</select>
		</div>
		<div class="inputDiv" id="mosLimit">
			MOS最低门限
			<input class="easyui-textbox" name="command.mosLimit" value="${command.mosLimit}" disabled="true" />
		</div>
		<div class="inputDiv" id="channelNo">
			通道号
			<select class="easyui-combobox" name="command.channelNo" data-options="editable:false<c:if test="${command.channelNo!=null}">,value:'${command.channelNo}'</c:if>" disabled="true">
				<option value="0">0</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="11">11</option>
			</select>
		</div>
		<div class="inputDiv" id="calledNo">
			请选择被叫
			<select class="easyui-combobox" data-options="editable:false,data:modulePhoneNum,valueField:'value',textField:'text',onSelect:setCallNo" disabled="true">
			</select>
		</div>
		<div class="inputDiv" id="callStyle" style="display:none;">
			呼叫方式
			<select class="easyui-combobox" name="command.callStyle" data-options="editable:false<c:if test="${command.callStyle!=null}">,value:'${command.callStyle}'</c:if>" disabled="true">
				<option value="0">按呼叫号码</option>
				<option value="1">通道号</option>
			</select>
		</div>
		
		
		
		
		<div class="inputDiv" id="timeOut">
			超时时间(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535"  name="command.timeOut" id="txtTimeOut" value="${command.timeOut}" disabled="true" />
		</div>
		<div class="inputDiv" id="remoteHost">
			远端站点
			<input class="easyui-textbox" name="command.remoteHost" value="${command.remoteHost}" disabled="true" />
		</div>
		<div class="inputDiv"  id="model">
			业务类型
			<select  class="easyui-combobox" name="command.model" data-options="editable:false<c:if test="${command.model!=null}">,value:'${command.model}'</c:if>" disabled="true">
				<option value="0">登录</option>
				<!-- <option value="1">页面刷新</option>-->
				<option value="2">文件下载</option>
			</select>
		</div>
		<div class="inputDiv"  id="dlFileSize">
			下载文件大小(byte)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.dlFileSize"  id="dlFileSize" value="${command.dlFileSize}" disabled="true" />
		</div>
		<div class="inputDiv" id="url">
			<c:if test="${command.id == '0x060F'}">下载地址</c:if>
			<c:if test="${command.id == '0x0611'}">流媒体的链接地址</c:if>
			<c:if test="${command.id != '0x060F' && command.id != '0x0611' }">url</c:if>
			<input class="easyui-textbox" name="command.url" value="${command.url}" disabled="true" />
		</div>
		<div class="inputDiv" id="port">
			端口
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.port" value="${command.port}" disabled="true" />
		</div>
		<div class="inputDiv" id="version">
			版本号
			<input class="easyui-textbox" name="command.version" value="${command.version}" disabled="true" />
		</div>
		<div class="inputDiv" id="download">
			上传/下载
			<select class="easyui-combobox" name="command.download" data-options="editable:false<c:if test="${command.download!=null}">,value:'${command.download}'</c:if>" disabled="true">
				<option value="0">上传</option>
				<option value="1">下载</option>
				<!-- @yinzhipeng udp测试新增 -->
				<c:if test="${command.id == '0x0620'}">
					<option value="2">上传和下载</option>
				</c:if>
				<!--  -->
			</select>
		</div>
		<div class="inputDiv" id="remoteFile">
			上传/下载文件地址
			<input class="easyui-textbox" name="command.remoteFile" value="${command.remoteFile}" disabled="true" />
		</div>
		<div class="inputDiv" id="ssid">
			SSID
			<select class="easyui-combobox" name="command.ssid" data-options="editable:false<c:if test="${command.ssid!=null}">,value:'${command.ssid}'</c:if>" disabled="true">
				<option value="CMCC">CMCC</option>
				<option value="ChinaUnicom">ChinaUnicom</option>
				<option value="ChinaNet">ChinaNet</option>
			</select>
		</div>
		
		<div class="inputDiv" id="passive">
			<!-- @yinzhipeng udp测试新增 -->
			<c:if test="${command.id == '0x0620'}">
				传输协议
				<select class="easyui-combobox" name="command.passive" data-options="editable:false<c:if test="${command.passive!=null}">,value:'${command.passive}'</c:if>" disabled="true">
					<option value="0">TCP协议</option>
					<option value="1">UDP协议</option>
				</select>
			</c:if>
			<!--  -->
			<c:if test="${command.id != '0x0620'}">
				模式(主动/被动)
				<select class="easyui-combobox" name="command.passive" data-options="editable:false<c:if test="${command.passive!=null}">,value:'${command.passive}'</c:if>" disabled="true">
					<option value="0">主动模式</option>
					<option value="1">被动模式</option>
				</select>
			</c:if>
		</div>
		<div class="inputDiv" id="binary">
			传输模式
			<!-- @yinzhipeng udp测试新增 -->
			<c:if test="${command.id == '0x0620'}">
				<select class="easyui-combobox" name="command.binary" data-options="editable:false<c:if test="${command.binary!=null}">,value:'${command.binary}'</c:if>" disabled="true">
					<option value="0">Telnet</option>
					<option value="1">SSH2</option>
				</select>
			</c:if>
			<!--  -->
			<c:if test="${command.id != '0x0620'}">
				<select class="easyui-combobox" name="command.binary" data-options="editable:false<c:if test="${command.binary!=null}">,value:'${command.binary}'</c:if>" disabled="true">
					<option value="0">Text模式传输</option>
					<option value="1">二进制模式</option>
				</select>
			</c:if>
		</div>
		

		
		<div class="inputDiv" id="deep">
			刷新深度
			<input class="easyui-textbox" name="command.deep" value="${command.deep}" disabled="true" />
		</div>
		<div class="inputDiv" id="downloadPicture">
			是否下载图片
			<select class="easyui-combobox" name="command.downloadPicture" data-options="editable:false<c:if test="${command.downloadPicture!=null}">,value:'${command.downloadPicture}'</c:if>" disabled="true">
				<option value="0">不下载图片</option>
				<option value="1">下载图片</option>
			</select>
		</div>

		<div class="inputDiv" id="blockLink">
			禁用的链接
			<input class="easyui-textbox" name="command.blockLink" value="${command.blockLink}" disabled="true" />
		</div>
		
		<!-- @yinzhipeng udp测试新增 -->
		<div class="inputDiv" id="transferTime">
			上传/下载传输时间
			<input class="easyui-numberbox" data-options="min:1,max:65535"  name="command.transferTime" id="txtTransferTime" value="${command.transferTime}" disabled="true" />
		</div>
		<div class="inputDiv" id="dropTime">
			掉线时间(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535"  name="command.dropTime" id="txtDropTime" value="${command.dropTime}" disabled="true" />
		</div>
		<div class="inputDiv" id="bufferSize">
			缓冲大小(KB)
			<input class="easyui-numberbox" data-options="min:1,max:65535"  name="command.bufferSize" id="txtBufferSize" value="${command.bufferSize}" disabled="true" />
		</div>
		<div class="inputDiv" id="packetSize">
			数据包大小(KB)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.packetSize" value="${command.packetSize}" disabled="true" />
		</div>
		<div class="inputDiv" id="bandWidth">
			带宽(KB/S)
			<input class="easyui-numberbox" data-options="min:1,max:65535"  name="command.bandWidth" id="txtBandWidth" value="${command.bandWidth}" disabled="true" />
		</div>
		<div class="inputDiv" id="netLink">
			网络连接类型
			<select class="easyui-combobox" name="command.netLink" data-options="editable:true<c:if test="${command.netLink!=null}">,value:'${command.netLink}'</c:if>" disabled="true">
				<option value="1">PPP连接</option>
				<option value="2">AT指令</option>
			</select>
		</div>
		<!--  -->
		
		<div class="inputDiv" id="apn">
			<c:if test="${command.id == '0x0620'}">拨号方式</c:if>
			<c:if test="${command.id != '0x0620'}">接入点号码</c:if>
			<select class="easyui-combobox" name="command.apn" data-options="editable:true<c:if test="${command.apn!=null}">,value:'${command.apn}'</c:if>" disabled="true">
				<option value="1">cmwap</option>
				<option value="2">cmnet</option>
				<option value="3">ctwap</option>
				<option value="4">ctnet</option>
				<option value="5">uninet</option>
				<option value="6">3gnet</option>
			</select>
		</div>
		<div class="inputDiv" id="videoResolution">
			视频分辨率
			<select class="easyui-combobox" name="command.videoResolution" data-options="editable:true<c:if test="${command.videoResolution!=null}">,value:'${command.videoResolution}'</c:if>" disabled="true">
				<option value="1">标清</option>
				<option value="2">高清</option>
				<option value="3">超清</option>
				<option value="4">1080P</option>
			</select>
		</div>
		
		<div class="inputDiv" id="connectionMode">
			连接模式
			<!-- 需要完善处理 -->
			<select class="easyui-combobox" name="command.connectionMode" data-options="editable:false<c:if test="${command.connectionMode!=null}">,value:'${command.connectionMode}'</c:if>" disabled="true" id="connectionModeSelect">
				<option value="0">CONN_CL(WAP 1.x)</option>
				<option value="1">CONN_CO(WAP 1.x)</option>
				<option value="2">CONN_SCO(WAP 1.x)</option>
				<option value="3">CONN_SCL(WAP 1.x)</option>
				<option value="4">CONN_HTTPS(WAP 1.x)</option>
				<option value="10">CONN_HTTP(WAP 2.x)</option>
			</select>
		</div>
		<div class="inputDiv" id="gateway">
			网关地址
			<input class="easyui-textbox" name="command.gateway" value="${command.gateway}" disabled="true" />
		</div>
		
		
		
		
		<div class="inputDiv" id="phoneNumber">
			<font color="red">*</font>
			呼叫号码
			<input class="easyui-textbox" data-options="required:true" name="command.phoneNumber" value="${command.phoneNumber}" disabled="true" id="commandPhoneNumber" />
		</div>
		
		<div class="inputDiv" id="waitTimes">
			空闲等待时间(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.waitTimes" value="${command.waitTimes}" disabled="true" />
		</div>
		<!-- @yinzhipeng (E)GPRS Attach新增 -->
		<div class="inputDiv" id="waitTime">
			等待时长(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.waitTime" value="${command.waitTime}" disabled="true" />
		</div>
		<!--  -->
		<div class="inputDiv" id="keeptime">
			保持时长(秒)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.keeptime" value="${command.keeptime}" disabled="true" />
		</div>
		<div class="inputDiv" id="packagesize">
			包的大小(byte)
			<input class="easyui-numberbox" data-options="min:1,max:65535" name="command.packagesize" value="${command.packagesize}" disabled="true" />
		</div>
		
		
        <!-- ip字段 -->		
		<div class="inputDiv" id="ip">
			ip
			<input class="easyui-textbox" name="command.ip" value="${command.ip}" disabled="true" />
		</div>
		<div class="inputDiv" id="destination">
			目标号码
			<input class="easyui-textbox" name="command.destination" value="${command.destination}" disabled="true" />
		</div>
		<div class="inputDiv" id="mode">
			发送或接受
			<select class="easyui-combobox" name="command.mode" data-options="editable:false<c:if test="${command.mode!=null}">,value:'${command.mode}'</c:if>" disabled="true">
				<option value="0">接收短信</option>
				<option value="1">发送短信</option>
			</select>
		</div>
		<div class="inputDiv" id="text">
			发送模式
			<select class="easyui-combobox" name="command.text" data-options="editable:false<c:if test="${command.text!=null}">,value:'${command.text}'</c:if>" disabled="true">
				<option value="0">发送PDU模式</option>
				<option value="1">发送文本模式</option>
			</select>
		</div>
		<div class="inputDiv" id="report">
			状态报告
			<select class="easyui-combobox" name="command.report" data-options="editable:false<c:if test="${command.report!=null}">,value:'${command.report}'</c:if>" disabled="true">
				<option value="0">不需要</option>
				<option value="1">需要</option>
			</select>
		</div>
		

		<div class="inputDiv" id="content">
			短信内容
			<select class="easyui-combobox" name="command.content" data-options="editable:false<c:if test="${command.content!=null}">,value:'${command.content}'</c:if>" disabled="true">
				<option value="0">10字</option>
				<option value="1">100字</option>
			</select>
				<!--  
			<input class="easyui-textbox" name="command.content" value="${command.content}"
				disabled="true" />
				-->
		</div>
		<div class="inputDiv" id="serverCenterAddress">
			服务中心号码
			<input class="easyui-textbox" name="command.serverCenterAddress" value="${command.serverCenterAddress}" disabled="true" />
		</div>
		<div class="inputDiv" id="mediaFileSize">
			文件大小(KB)
			<input class="easyui-textbox" name="command.mediaFileSize" value="${command.mediaFileSize}" disabled="true" />
		</div>
		<div class="inputDiv" id="syncMSNO">
			接收MMS模块号
			<input class="easyui-textbox" name="command.syncMSNO" value="${command.syncMSNO}" disabled="true" />
		</div>
		<div class="inputDiv" id="serverAddress">
			服务器地址
			<input class="easyui-textbox" name="command.serverAddress" value="${command.serverAddress}" disabled="true" />
		</div>
		<div class="inputDiv" id="syncMSNOs">
			发送MMS模块号
			<input class="easyui-textbox" name="command.syncMSNOs" value="${command.syncMSNOs}" disabled="true" />
		</div>
		<div class="inputDiv" id="ptimeOut">
			PUSH超时时间(秒)
			<input class="easyui-textbox" name="command.ptimeOut" value="${command.ptimeOut}" disabled="true" />
		</div>
		<div class="inputDiv" id="fetionNumber">
			登录飞信号
			<input class="easyui-textbox" name="command.fetionNumber" value="${command.fetionNumber}" disabled="true" />
		</div>
		<div class="inputDiv" id="sdestination">
			目标手机号码
			<input class="easyui-textbox" name="command.sdestination" value="${command.sdestination}" disabled="true" />
		</div>
		<div class="inputDiv" id="fdestination">
			目标飞信号码
			<input class="easyui-textbox" name="command.fdestination" value="${command.fdestination}" disabled="true" />
		</div>
		<div class="inputDiv" id="proxy">
			使用代理
			<select class="easyui-combobox" name="command.proxy" data-options="editable:false<c:if test="${command.proxy!=null}">,value:'${command.proxy}'</c:if>" disabled="true">
				<option value="0">不使用代理</option>
				<option value="1">使用代理</option>
			</select>
		</div>
		<div class="inputDiv" id="proxyType">
			代理类型
			<select class="easyui-combobox" name="command.proxyType" data-options="editable:false<c:if test="${command.proxyType!=null}">,value:'${command.proxyType}'</c:if>" disabled="true">
				<option value="0">HTTP</option>
				<option value="1">SOCKS5</option>
				<option value="2">SOCKS4</option>
			</select>
		</div>
		<div class="inputDiv" id="address">
			代理地址
			<input class="easyui-textbox" name="command.address" value="${command.address}" disabled="true" />
		</div>
		
		<div class="inputDiv" id="ftpAccount">
			FTP用户名
			<input class="easyui-textbox" name="command.ftpAccount" value="${command.ftpAccount}" disabled="true" />
		</div>
		<div class="inputDiv" id="ftpPassword">
			FTP密码
			<input class="easyui-textbox" name="command.ftpPassword" value="${command.ftpPassword}" disabled="true" />
		</div>
		<div class="inputDiv" id="mailServer">
			服务器地址
			<input class="easyui-textbox" name="command.mailServer" value="${command.mailServer}" disabled="true" />
		</div>
		<div class="inputDiv" id="deletemail">
			自动删除邮件
			<select class="easyui-combobox" name="command.deletemail" data-options="editable:false<c:if test="${command.deletemail!=null}">,value:'${command.deletemail}'</c:if>" disabled="true">
				<option value="0">不删除</option>
				<option value="1">删除</option>
			</select>
		</div>

		<div class="inputDiv" id="path">
			附件路径
			<input class="easyui-textbox" name="command.path" value="${command.path}" disabled="true" />
		</div>
		<div class="inputDiv" id="ssl">
			是否使用SSL
			<select class="easyui-combobox" name="command.ssl" data-options="editable:false<c:if test="${command.ssl!=null}">,value:'${command.ssl}'</c:if>" disabled="true">
				<option value="0">不使用</option>
				<option value="1">使用</option>
			</select>
		</div>
		<div class="inputDiv" id="sender">
			发送者名称
			<input class="easyui-textbox" name="command.sender" value="${command.sender}" disabled="true" />
		</div>
		<div class="inputDiv" id="from">
			发送者地址
			<input class="easyui-textbox" name="command.from" value="${command.from}" disabled="true" />
		</div>
		<div class="inputDiv" id="to">
			目标地址
			<input class="easyui-textbox" name="command.to" value="${command.to}" disabled="true" />
		</div>
		<div class="inputDiv" id="fileSize">
			上传文件大小(MB)
			<input class="easyui-textbox" name="command.fileSize" value="${command.fileSize}" disabled="true" />
		</div>
		<div class="inputDiv" id="upLinkSpeed">
			上行速度(KB)
			<select class="easyui-combobox" name="command.upLinkSpeed" data-options="editable:false<c:if test="${command.upLinkSpeed!=null}">,value:'${command.upLinkSpeed}'</c:if>" disabled="true">
				<option value="64">64K</option>
				<option value="128">128K</option>
				<option value="384">384K</option>
				<option value="2048">2M</option>
				<option value="3686.4">3.6M</option>
			</select>
		</div>
		<div class="inputDiv" id="downLinkSpeed">
			下行速度(KB)
			<select class="easyui-combobox" name="command.downLinkSpeed" data-options="editable:false<c:if test="${command.downLinkSpeed!=null}">,value:'${command.downLinkSpeed}'</c:if>" disabled="true">
				<option value="64">64K</option>
				<option value="128">128K</option>
				<option value="384">384K</option>
				<option value="2048">2M</option>
				<option value="3686.4">3.6M</option>
				<option value="7372.8">7.2M</option>
			</select>
		</div>
		<div class="inputDiv" id="subject">
			邮件主题
			<input class="easyui-textbox" name="command.subject" value="${command.subject}" disabled="true" />
		</div>
		<div class="inputDiv" id="body">
			邮件内容
			<input class="easyui-textbox" name="command.body" value="${command.body}" disabled="true" />
		</div>
		<div class="inputDiv" id="authentication">
			验证模式
			<select class="easyui-combobox" data-options="editable:false<c:if test="${command.authentication!=null}">,value:'${command.authentication}'</c:if>" name="command.authentication" disabled="true">
				<option value="0">None</option>
				<option value="1">CRAM MD5</option>
				<option value="2">AUTH LOGIN</option>
				<option value="3">LOGIN PLAIN</option>
			</select>
		</div>
		<div class="inputDiv" id="encoding">
			编码模式
			<select class="easyui-combobox" data-options="editable:false<c:if test="${command.encoding!=null}">,value:'${command.encoding}'</c:if>" name="command.encoding" disabled="true">
				<option value="0">Arabic (Windows)</option>
				<option value="1">Baltic (Windows)</option>
				<option value="2">Central European (ISO)</option>
				<option value="3">Central European (Windows)</option>
				<option value="4">Chinese Simplified (GB2312)</option>
				<option value="5">Chinese Simplified (HZ)</option>
				<option value="6">Chinese Traditional (Big5)</option>
				<option value="7">Cyrilic (KOI8-R)</option>
				<option value="8">Cyrillic (Windows)</option>
				<option value="9">Greek (Windows)</option>
				<option value="10">Hebrew (Windows)</option>
				<option value="11">Japanese (JIS)</option>
				<option value="12">Korean</option>
				<option value="13">Korean (EUC)</option>
				<option value="14">Latin 9 (ISO)</option>
				<option value="15">Thai (Windows)</option>
				<option value="16">Turkish (Windows)</option>
				<option value="17">Unicode (UTF-7)</option>
				<option value="18">Unicode (UTF-8)</option>
				<option value="19">Vietnamese (Windows)</option>
				<option value="20">Western European (ISO)</option>
				<option value="21">Western European (Windows)</option>
			</select>
		</div>
		<div class="inputDiv" id="html">
			是否使用HTML
			<select class="easyui-combobox" name="command.html" data-options="editable:false<c:if test="${command.html!=null}">,value:'${command.html}'</c:if>" disabled="true">
				<option value="0">不使用</option>
				<option value="1">使用</option>
			</select>
		</div>

		<div class="inputDiv" id="dialMode">
			拨号方式
			<select class="easyui-combobox" name="command.dialMode" data-options="editable:false<c:if test="${command.dialMode!=null}">,value:'${command.dialMode}'</c:if>" disabled="true">
				<option value="0">键盘模式</option>
				<option value="1">AT指令</option>
				<option value="2">TRACE指令</option>
			</select>
		</div>
		<div class="inputDiv" id="testVMOS">
			是否VMOS测试
			<select class="easyui-combobox" name="command.testVMOS" data-options="editable:false<c:if test="${command.testVMOS!=null}">,value:'${command.testVMOS}'</c:if>" disabled="true">
				<option value="0">不做VMOS测试</option>
				<option value="1">进行VMOS测试</option>
			</select>
		</div>
		<div class="inputDiv" id="callVMOSServer">
			语音评估服务
			<select class="easyui-combobox" name="command.callVMOSServer" data-options="editable:false<c:if test="${command.callVMOSServer!=null}">,value:'${command.callVMOSServer}'</c:if>" disabled="true">
				<option value="0">不做语音评估服务 </option>
				<option value="1">进行语音评估服务</option>
			</select>
		</div>
		<div class="inputDiv" id="mosILimit">
			MOS-I最低门限
			<input class="easyui-textbox" name="command.mosILimit" value="${command.mosILimit}" disabled="true" />
		</div>
		<div class="inputDiv" id="sampleFile">
			文件路径
			<input class="easyui-textbox" name="command.sampleFile" value="${command.sampleFile}" disabled="true" />
		</div>
		<div class="inputDiv" id="amrRate">
			amrRate
			<select class="easyui-combobox" name="command.amrRate" data-options="editable:false<c:if test="${command.amrRate!=null}">,value:'${command.amrRate}'</c:if>" disabled="true">
				<option value="0">Voice IS 96</option>
				<option value="1">8K Loopback</option>
				<option value="2">Markov old 8K</option>
				<option value="3">Reserved</option>
				<option value="4">Voice IS 96 A</option>
				<option value="5">Voice 13K </option>
				<option value="6">RateSet 2 Markov new 13K</option>
				<option value="7">RateSet 1 Markov new 8K</option>
				<option value="8">13K Loopback</option>
				<option value="9">13K Markov old 13K</option>
				<option value="10">EVRC</option>
			</select>
		</div>
		<div class="inputDiv" id="synchronize">
			是否同步处理
			<select class="easyui-combobox" name="command.synchronize" data-options="editable:false<c:if test="${command.synchronize!=null}">,value:'${command.synchronize}'</c:if>" disabled="true">
				<option value="0">不同步</option>
				<option value="1">同步</option>
			</select>
		</div>
		<div class="inputDiv" id="destMSNO">
			内部模块号
			<input class="easyui-textbox" name="command.destMSNO" value="${command.destMSNO}" disabled="true" />
		</div>
		<div class="inputDiv" id="account">
			账号
			<input class="easyui-textbox" name="command.account" value="${command.account}" disabled="true" id="accountText" />
		</div>
		<div class="inputDiv" id="user">
			<c:if test="${command.id == '0x0901' || command.id == '0x0902' || command.id == '0x0903' || command.id == '0x0904' || command.id == '0x0905'}">
				<font color="red">*</font>
				用户名
				<input class="easyui-textbox" data-options="required:true" id="userName" name="command.user" value="${command.user}" disabled="true" />	
			</c:if>
			<c:if test="${command.id != '0x0901' && command.id != '0x0902' && command.id != '0x0903' && command.id != '0x0904' && command.id != '0x0905'}">
				用户名
				<input class="easyui-textbox" id="userName" name="command.user" value="${command.user}" disabled="true" />
			</c:if>
		</div>
		<div class="inputDiv" id="username">
			用户名
			<input class="easyui-textbox" name="command.username" value="${command.username}" disabled="true" />
		</div>
		<div class="inputDiv" id="password">
			<c:if test="${command.id == '0x0901' || command.id == '0x0902' || command.id == '0x0903' || command.id == '0x0904' || command.id == '0x0905'}">
				<font color="red">*</font>
				密码
				<input class="easyui-textbox" data-options="required:true" name="command.password" value="${command.password}" disabled="true" id="passwordText" />	
			</c:if>
			<c:if test="${command.id != '0x0901' && command.id != '0x0902' && command.id != '0x0903' && command.id != '0x0904' && command.id != '0x0905'}">
				密码
				<input class="easyui-textbox" name="command.password" value="${command.password}" disabled="true" id="passwordText" />
			</c:if>
		</div>
		<div class="inputDiv" id="agent">
			模拟客户端Agent
			<input class="easyui-textbox" name="command.agent" value="${command.agent}" disabled="true" />
		</div>
		<div class="inputDiv" id="rtp">
			是否使用rtp over rtsp(tcp)
			<select class="easyui-combobox" style="width:90px" data-options="editable:false<c:if test="${command.rtp!=null}">,value:'${command.rtp}'</c:if>" name="command.rtp" disabled="true">
				<option value="0">不使用</option>
				<option value="1">使用</option>
			</select>
		</div>
		<div class="inputDiv" id="rtspHttpPort">
			转接http端口
			<input class="easyui-textbox" name="command.rtspHttpPort" value="${command.rtspHttpPort}" disabled="true" />
		</div>
		<div class="inputDiv" id="localRTPport">
			本地rtp/udp端口
			<input class="easyui-textbox" name="command.localRTPport" value="${command.localRTPport}" disabled="true" />
		</div>
		<div class="inputDiv" id="preBufferLength">
			初始缓冲时间(ms)
			<input class="easyui-textbox" name="command.preBufferLength" value="${command.preBufferLength}" disabled="true" />
		</div>

		<div class="inputDiv" id="rebufferLength">
			重新缓冲时间(ms)
			<input class="easyui-textbox" name="command.rebufferLength" value="${command.rebufferLength}" disabled="true" />
		</div>
		<div class="inputDiv" id="playTime">
			测试播放时间(秒)
			<input class="easyui-textbox" name="command.playTime" value="${command.playTime}" disabled="true" />
		</div>
		<div class="inputDiv" id="holdtime">
			保持时间(单位 秒)
			<input class="easyui-textbox" name="command.holdtime" value="${command.holdtime}" disabled="true" id="holdtime" />
		</div>		
		<div class="inputDiv" id="threadNum">
			线程个数
			<input class="easyui-textbox" name="command.threadNum" onblur="checkNumValue(threadNum);" id="threadNum" value="${command.threadNum}" disabled="true" />
		</div>
		<div class="inputDiv"  id="maxDialNum">
			单次业务最多可拨号次数
			<input class="easyui-textbox" style="width:90px" name="command.maxDialNum" onblur="checkNumValue(maxDialNum);" id="maxDialNum" value="${command.maxDialNum}" disabled="true" />
		</div>
		<div class="inputDiv"  id="maxFTPland">
			单次业务最多FTP登陆次数
			<input class="easyui-textbox" style="width:90px" name="command.maxFTPland" onblur="checkNumValue(maxFTPland);" id="maxFTPland" value="${command.maxFTPland}" disabled="true" />
		</div>
		
		<div class="inputDiv"  id="proxyPort">
			代理端口
			<input class="easyui-textbox" name="command.proxyPort" onblur="checkNumValue(proxyPort);" id="proxyPort" value="${command.proxyPort}" disabled="true" />
		</div>
		<div class="inputDiv"  id="urlInterval">
			浏览时间间隔
			<input class="easyui-textbox" name="command.urlInterval" onblur="checkNumValue(urlInterval);" id="urlInterval" value="${command.urlInterval}" disabled="true" />
		</div>
		<div class="inputDiv"  id="urlRandomNum">
			每次随机地址个数
			<input class="easyui-textbox" name="command.urlRandomNum" onblur="checkNumValue(urlRandomNum);" id="urlRandomNum" value="${command.urlRandomNum}" disabled="true" />
		</div>
		<div class="inputDiv"  id="bufferLength">
			缓冲区总时长
			<input class="easyui-textbox" name="command.bufferLength" onblur="checkNumValue(bufferLength);" id="bufferLength" value="${command.bufferLength}" disabled="true" />
		</div>
		<div class="inputDiv"  id="bufferPlayThreshold">
			缓冲播放门限
			<input class="easyui-textbox" name="command.bufferPlayThreshold"  id="bufferPlayThreshold" value="${command.bufferPlayThreshold}" disabled="true" />
		</div>
		<div class="inputDivShow" >
			执行序号
			<input class="easyui-numberbox" name="command.runOrder"  value="${command.runOrder}" />
		</div>
		<br>
		<div class="inputDiv"  id="urlMust">
			<font color="#ff0000">* </font>必选浏览地址
			<select class="easyui-combobox" data-options="required:true,editable:false,multiline:true" id="mustUrlSet" multiple="true"  name="mustUrlIds" style="height: 120px;" disabled="true" >
				<c:forEach items="${urlMustList}" var="URL">
					<option value="${URL.id}" <c:if test="${URL.selected}">selected="selected"</c:if> >${URL.urlName}</option>
				</c:forEach>
			</select>
		</div>
		<div class="inputDiv"  id="urlRandom">
			<font color="#ff0000">*</font>随机浏览地址
			<select class="easyui-combobox" data-options="required:true,editable:false,multiline:true" id="randomUrlSet" multiple="true" name="randomUrlIds" style="height: 120px;" disabled="true" >
				<c:forEach items="${urlRandomList}" var="URL">
					<option value="${URL.id}" <c:if test="${URL.selected}">selected="selected"</c:if> >${URL.urlName}</option>
				</c:forEach>
			</select>
		</div>
	</div>
</form>



