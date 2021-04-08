commandContext = {

	commands : [{
		'id' : '0x0500',
		'name' : 'GSM/TD/WCDMA/LTE语音业务主叫',
		'properties' : ["id", "callNumber","calledNo","randomCall", "duration", "repeat",
				"interval", "maxTime", "testMOS", "callMOSServer", "mosLimit",/*'channelNo',*/'callStyle']
	}, {
		'id' : '0x0501',
		'name' : 'GSM/TD/WCDMA/LTE语音业务被叫',
		'properties' : ["id", "testMOS", "mosLimit"]
	},
	{
		'id' : '0x0502',
		'name' : 'IDLE/等待',
		'properties' : ["id","apn", "waitTimes"]
	}, 
	{
		'id' : '0x0602',
		'name' : '(E)GPRS Attach',
		'properties' : ["id", "repeat", "apn","interval", "keeptime","timeOut","waitTime"]
	},
	{
		'id' : '0x0603',
		'name' : '(E)GPRS PDP Activation',
		'properties' : ["id", "repeat", "apn", "interval", "keeptime"]
	}, {
		'id' : '0x0604',
		'name' : '移动互联网应用层业务-PING',
		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
				"ip", "apn","netLink"]
	}
//	, {
//		'id' : '0x0605',
//		'name' : 'GPRS KJava',
//		'properties' : ["id", "repeat", "interval", "timeOut", "url", "apn",
//				"agent", "gateway", "port", "connectionMode"]
//	}
	, {
		'id' : '0x0606',
		'name' : '(E)GPRS/CDMA Wap登录',
		'properties' : ["id", "gateway", "port", "connectionMode", "url",
				"timeOut", "agent", "interval", "apn"]
	}, {
		'id' : '0x0607',
		'name' : '(E)GPRS/CDMA Wap页面刷新',
		'properties' : ["id", "gateway", "agent", "port", "connectionMode",
				"url", "deep", "downloadPicture", "timeOut", "blockLink",
				"agent", "interval", "apn"]
	}, {
		'id' : '0x0608',
		'name' : '(E)GPRS/CDMA Wap图铃下载',
		'properties' : ["id", "interval", "timeOut", "url", "apn", "agent",
				"connectionMode", "gateway", "port"]
	}, {
		'id' : '0x0609',
		'name' : '接收/发送短消息',
		'properties' : ["id", "repeat", "interval", "timeOut", "destination",
				"mode", "text", "report", "content", "serverCenterAddress"]
	}, {
		'id' : '0x060A',
		'name' : '发送彩信',
		'properties' : ["id", "repeat", "interval", "timeOut", "content",
				"mediaFileSize", "destination", "syncMSNO", "apn", "agent",
				"connectionMode", "account", "password", "gateway", "port",
				"serverAddress"]
	}, {
		'id' : '0x060B',
		'name' : '接收彩信',
		'properties' : ["id", "syncMSNOs", "ptimeOut", "timeOut", "agent",
				"connectionMode", "account", "password", "gateway", "port"]
	}, {
		'id' : '0x060C',
		'name' : '移动互联网应用层业务-FTP',
		'properties' : ["id", "repeat","remoteHost", "port", "account", "password",
				"timeOut", "passive", "binary", "download", "remoteFile",
				"interval", "apn","upLinkSpeed","downLinkSpeed","fileSize","threadNum","maxDialNum","maxFTPland"]
	}
//	, {
//		'id' : '0x060D',
//		'name' : 'GSM/TD飞信登陆',
//		'properties' : ["id", "repeat", "interval", "timeOut", "apn", "agent"]
//	}, {
//		'id' : '0x060E',
//		'name' : 'GSM/TD飞信手机(短信)测试',
//		'properties' : ["id", "repeat", "interval", "timeOut", "fetionNumber",
//				"password", "apn", "content", "sdestination",
//				"serverCenterAddress", "text", "report", "content",
//				"fdestination"]
//	}
	, {
		'id' : '0x060F',
		'name' : '移动互联网应用层业务-HTTP',
		'properties' : ["id", "repeat", "interval", "timeOut", "apn", "port",
				"url", "proxy", "address", "port", "proxyType","model","dlFileSize","proxyPort","urlMust","urlRandom","urlInterval","urlRandomNum"]
	}, {
		'id' : '0x0611',
		'name' : '移动互联网应用层业务-流媒体',
		'properties' : ["id", "repeat", "interval", "apn", "version", "url",
				"username", "password", "agent", "rtp", "rtspHttpPort",
				"localRTPport", "preBufferLength", "rebufferLength", "playTime","bufferLength","bufferPlayThreshold","videoResolution"]
	}, {
		'id' : '0x0612',
		'name' : '移动互联网应用层业务-Email接收',
		'properties' : ["id", "repeat", "interval", "timeOut", "apn",
				"mailServer", "port", "username", "password", "deletemail",
				"path", "ssl"]
	}, {
		'id' : '0x0613',
		'name' : '移动互联网应用层业务-Email发送',
		'properties' : ["id", "repeat", "interval", "timeOut", "apn", "sender",
				"from", "to", "fileSize", "subject", "body", "address", "port",
				"authentication", "account", "password", "encoding", "html",
				"ssl"]
	}, 
	{
		'id' : '0x0620',
		'name' : 'UDP测试',
		'properties' : ["id","repeat","interval","transferTime","dropTime",
		        "apn","remoteHost","port","account","password","passive",
		        "binary","download","bufferSize", "packetSize","bandWidth","netLink"]
	},//yinzhipeng 1.5.2新增  20170728
	{
		'id' : '0x0700',
		'name' : 'TD/LTE视频电话主叫',
		'properties' : ["id", "phoneNumber","calledNo","dialMode", "randomCall",
				"duration", "maxTime", "timeOut", "testVMOS", "callVMOSServer",
				"mosILimit", "sampleFile",/*'channelNo',*/'callStyle']
	}, {
		'id' : '0x0701',
		'name' : 'TD/LTE视频电话被叫',
		'properties' : ["id", "testVMOS", "mosILimit"]
	}, {
		'id' : '0x0800',
		'name' : 'CDMA语音主叫',
		'properties' : ["id", "callNumber","calledNo", "dialMode", "randomCall",
				"duration", "maxTime", "interval", "testMOS", "callMOSServer",
				"mosLimit", "amrRate",/*'channelNo',*/'callStyle']
	}, {
		'id' : '0x0801',
		'name' : 'CDMA语音被叫',
		'properties' : ["id", "testMOS", "mosLimit"]
	}, {
		'id' : '0x0802',
		'name' : 'CDMA发送短消息',
		'properties' : ["id", "repeat", "interval", "timeOut", "destination",
				"sender", "content", "synchronize", "destMSNO"]
	}, {
		'id' : '0x0803',
		'name' : 'CDMA接收短消息',
		'properties' : ["id", "destination", "timeOut", "synchronize",
				"destMSNO"]
	},{
		'id' : '0x0b0c',
		'name' : '并发业务',
		'properties' : ["id","ie","firstServiceEndDelay","secondServiceStartDelay","callNumber","calledNo","randomCall", "duration", 
		                "repeat", "maxTime", "testMOS", "callMOSServer", "mosLimit",'callStyle',
		                "remoteHost", "port", "account", "password","timeOut", "passive", "binary", "download", "remoteFile",
		                "interval", "apn","upLinkSpeed","downLinkSpeed","fileSize","threadNum","maxDialNum","maxFTPland"]
	},{
	'id' : '0x8500',
	'name' : '微信实时通信主叫',
	'properties' : ["id","callName","randomCall","duration","repeat","interval","maxTime","callModel","testMOS","callMOSServer","timeOut"]
	},{
		'id' : '0x8501',
		'name' : '微信实时通信被叫测试命令',
		'properties' : ["id","testMOS","callModel","mosLimit"]
	}
//	, {
//		'id' : '0x0901',
//		'name' : 'WLAN AP关联',
//		'properties' : ["id", "name", "repeat", "ssid", "interval",
//				"timeOut", "holdtime"]
//	}, {
//		'id' : '0x0902',
//		'name' : 'WLAN WEB用户认证',
//		'properties' : ["id", "name", "repeat", "user", "password", "ssid", "interval", "timeOut"]
//	}, {
//		'id' : '0x0903',
//		'name' : 'WLAN Http网站访问',
//		'properties' : ["id", "name", "repeat", "user", "password", "ssid", "interval", "timeOut", "url"]
//	}, {
//		'id' : '0x0904',
//		'name' : 'WLAN FTP下载/上传',
//		'properties' : ["id", "name", "repeat", "user", "password", "ssid", "interval", "timeOut", "remoteHost",
//				"upLinkSpeed","downLinkSpeed","port","ftpAccount","ftpPassword","fileSize","binary","passive","download","remoteFile"]
//	}, {
//		'id' : '0x0905',
//		'name' : 'WLAN Ping',
//		'properties' : ["id", "name", "repeat", "user", "password", "ssid", "interval", "timeOut","packagesize","ip"]
//	},{							
//		'id' : '0x0912',
//		'name' : 'QQ业务',
//		'properties' : ["id", "repeat", "interval", "timeOut","packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0913',
//		'name' : '飞信业务',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//			 "apn"]
//	}, {
//		'id' : '0x0914',
//		'name' : '腾讯微博',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0915',
//		'name' : '腾讯微信',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0916',
//		'name' : '移动微博',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0917',
//		'name' : 'UC浏览器',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0918',
//		'name' : '手机报',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0919',
//		'name' : '音乐下载',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0920',					
//		'name' : '音乐随身听',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0921',
//		'name' : '飞信',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0922',
//		'name' : '手机游戏',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0923',
//		'name' : '手机阅读',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}, {
//		'id' : '0x0924',
//		'name' : '手机证劵',
//	'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//			 "apn"]
//	}, {
//		'id' : '0x0925',
//		'name' : '手机视频',
//		'properties' : ["id", "repeat", "interval", "timeOut", "packagesize",
//				 "apn"]
//	}
	],

	findCommand : function(commandId) {
		for (var i = 0; i < this.commands.length; i++) {
			if (this.commands[i].id == commandId) {
				return this.commands[i];
			}
		}
		alert('commandId do not exist');
	},
	showElements : function(commandId) {
		var command = this.findCommand(commandId);
		var elements = command.properties;
		for (var i = 0; i < elements.length; i++) {
			if (document.getElementById(elements[i]) == null) {
				alert(elements[i]);
			}
			document.getElementById(elements[i]).className = "inputDivShow";
			$(":input", document.getElementById(elements[i])).attr("disabled",false);
		}
	},

	getName : function(commandId) {
		var command = this.findCommand(commandId);
		return command.name;
	}

}
