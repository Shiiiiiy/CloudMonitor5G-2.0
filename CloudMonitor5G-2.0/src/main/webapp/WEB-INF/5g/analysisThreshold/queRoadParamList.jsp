<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>问题路段分析参数设置</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script> --%>
	<style type="text/css">
		.inputDivShow{
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-right: 0;
		    text-align: left;
		    width: 100%;
   			padding-left: 10px;
   		}

   		.incorrect:before {
		    content: '\2716';
		}

	</style>
	<script type="text/javascript">
		$(function(){
			//是否在页面加载完成的时候加载门限参数的初始值缓存到页面?
		});
		/* 保存修改 */
		function submitForm(){
		    var fieldNameEn = "";
		    var fieldValue = "";

			var weakcoverrsrp  = $("#weakcoverrsrp").textbox('getValue');
			fieldNameEn = fieldNameEn + 'weakcoverrsrp' +',';
			fieldValue = fieldValue + weakcoverrsrp +','
			var weakcoversamprate  = $("#weakcoversamprate").textbox('getValue');
			fieldNameEn = fieldNameEn + 'weakcoversamprate' +',';
			fieldValue = fieldValue + weakcoversamprate +','
			var weakcoverroadlen  = $("#weakcoverroadlen").textbox('getValue');
			fieldNameEn = fieldNameEn + 'weakcoverroadlen' +',';
			fieldValue = fieldValue + weakcoverroadlen +','

			var downqualitydiffsinr  = $("#downqualitydiffsinr").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downqualitydiffsinr' +',';
			fieldValue = fieldValue + downqualitydiffsinr +','
			var downqualitydiffrsrp  = $("#downqualitydiffrsrp").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downqualitydiffrsrp' +',';
			fieldValue = fieldValue + downqualitydiffrsrp +','
			var downqualitydiffsamprate  = $("#downqualitydiffsamprate").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downqualitydiffsamprate' +',';
			fieldValue = fieldValue + downqualitydiffsamprate +','
			var downqualitydiffroadlen  = $("#downqualitydiffroadlen").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downqualitydiffroadlen' +',';
			fieldValue = fieldValue + downqualitydiffroadlen +','

			var upqualitydifftxpower  = $("#upqualitydifftxpower").textbox('getValue');
			fieldNameEn = fieldNameEn + 'upqualitydifftxpower' +',';
			fieldValue = fieldValue + upqualitydifftxpower +','
			var upqualitydiffbler  = $("#upqualitydiffbler").textbox('getValue');
			fieldNameEn = fieldNameEn + 'upqualitydiffbler' +',';
			fieldValue = fieldValue + upqualitydiffbler +','
			var upqualitydiffsamprate  = $("#upqualitydiffsamprate").textbox('getValue');
			fieldNameEn = fieldNameEn + 'upqualitydiffsamprate' +',';
			fieldValue = fieldValue + upqualitydiffsamprate +','
			var upqualitydiffroadlen  = $("#upqualitydiffroadlen").textbox('getValue');
			fieldNameEn = fieldNameEn + 'upqualitydiffroadlen' +',';
			fieldValue = fieldValue + upqualitydiffroadlen +','

			var downlowerspeedrlc  = $("#downlowerspeedrlc").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downlowerspeedrlc' +',';
			fieldValue = fieldValue + downlowerspeedrlc +','
			var downlowerspeedsamprate  = $("#downlowerspeedsamprate").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downlowerspeedsamprate' +',';
			fieldValue = fieldValue + downlowerspeedsamprate +','
			var downlowerspeedroadlen  = $("#downlowerspeedroadlen").textbox('getValue');
			fieldNameEn = fieldNameEn + 'downlowerspeedroadlen' + ',';
			fieldValue = fieldValue + downlowerspeedroadlen + ",";

			var uplowerspeedrlc  = $("#uplowerspeedrlc").textbox('getValue');
			fieldNameEn = fieldNameEn + 'uplowerspeedrlc' +',';
			fieldValue = fieldValue + uplowerspeedrlc +','
			var uplowerspeedsamprate  = $("#uplowerspeedsamprate").textbox('getValue');
			fieldNameEn = fieldNameEn + 'uplowerspeedsamprate' +',';
			fieldValue = fieldValue + uplowerspeedsamprate +','
			var uplowerspeedroadlen  = $("#uplowerspeedroadlen").textbox('getValue');
			fieldNameEn = fieldNameEn + 'uplowerspeedroadlen';
			fieldValue = fieldValue + uplowerspeedroadlen;


			$.ajax({
				url:"${pageContext.request.contextPath}/questionRoadParam/saveThreshold.action",
				dataType:"json",
				type:"post",
				data: {'fieldNameEn':fieldNameEn,'fieldValue':fieldValue},
				success:function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$.messager.alert('提示','修改成功','info');
					}
				}
			});
			
		}
		/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					$("#weakcoverrsrp").textbox('setValue','-105');
					$("#weakcoversamprate").textbox('setValue','50');
					$("#weakcoverroadlen").textbox('setValue','30');
					$("#downqualitydiffsinr").textbox('setValue','-3');
					$("#downqualitydiffrsrp").textbox('setValue','-105');
					$("#downqualitydiffsamprate").textbox('setValue','50');
					$("#downqualitydiffroadlen").textbox('setValue','30');
					$("#upqualitydifftxpower").textbox('setValue','22');
					$("#upqualitydiffbler").textbox('setValue','10');
					$("#upqualitydiffsamprate").textbox('setValue','50');
					$("#upqualitydiffroadlen").textbox('setValue','30');
					$("#downlowerspeedrlc").textbox('setValue','100');
					$("#downlowerspeedsamprate").textbox('setValue','50');
					$("#downlowerspeedroadlen").textbox('setValue','30');
					$("#uplowerspeedrlc").textbox('setValue','5');
					$("#uplowerspeedsamprate").textbox('setValue','50');
					$("#uplowerspeedroadlen").textbox('setValue','30');
				}
			});
		}



	</script>
  </head>
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div style="width:49%;height:90%;float:left; margin-right: 10px;border:1px solid #95b8e7;">
	  	<div data-options="region:'west',border:false,split:true">
	  		<div style="width:100%;height:33%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
					<div class="panel-heading">
						<div class="panel-title">弱覆盖路段判定</div>
					</div>
				</div>
	   			<div class="inputDivShow">弱覆盖采样点定义:NR覆盖电平<=
		    		<input id="weakcoverrsrp" name="weakcoverrsrp" style="width:120px;" value="${questionRoadParam.weakcoverrsrp}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]' " />
		    		dBm
		    	</div>
		    	<div class="inputDivShow">弱覆盖路段定义:弱覆盖采样点占比>=
		    		<input id="weakcoversamprate" name="weakcoversamprate" style="width:120px;" value="${questionRoadParam.weakcoversamprate}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:30,max:100 " />
		    		%
		    	</div>
				<div class="inputDivShow">弱覆盖路段长度>=
					<input id="weakcoverroadlen" name="weakcoverroadlen" style="width:120px;" value="${questionRoadParam.weakcoverroadlen}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
					m
				</div>
		    </div>
	    	<div style="width:100%;height:33%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">下行质差路段判定</div></div>
	    			<div class="inputDivShow">下行质差采样点定义:SS-SINR<=
			    		<input id="downqualitydiffsinr" name="downqualitydiffsinr" style="width:120px;"  value="${questionRoadParam.downqualitydiffsinr}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
			    		dB 且NR覆盖电平>=
			    		<input id="downqualitydiffrsrp" name="downqualitydiffrsrp" style="width:120px;"  value="${questionRoadParam.downqualitydiffrsrp}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />
			    		dBm
			    	</div>
					<div class="inputDivShow">下行质差路段定义:质差采样点占比>=
						<input id="downqualitydiffsamprate" name="downqualitydiffsamprate" style="width:120px;"  value="${questionRoadParam.downqualitydiffsamprate}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:30,max:100" />
						%
					</div>
					<div class="inputDivShow">下行质差路段长度>=
						<input id="downqualitydiffroadlen" name="downqualitydiffroadlen" style="width:120px;"  value="${questionRoadParam.downqualitydiffroadlen}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						m
					</div>
			    </div>
			 </div>
			<div style="width:100%;height:33%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">上行质差路段定义</div></div>
					<div class="inputDivShow">上行质差采样点定义:TxPower>=
						<input id="upqualitydifftxpower" name="upqualitydifftxpower" style="width:120px;"  value="${questionRoadParam.upqualitydifftxpower}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						dBm 且 上行BLER>=
						<input id="upqualitydiffbler" name="upqualitydiffbler" style="width:120px;"  value="${questionRoadParam.upqualitydiffbler}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />
						%
					</div>
					<div class="inputDivShow">上行质差路段定义:质差采样点占比>=
						<input id="upqualitydiffsamprate" name="upqualitydiffsamprate" style="width:120px;"  value="${questionRoadParam.upqualitydiffsamprate}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:30,max:100" />
						%
					</div>
					<div class="inputDivShow">上行质差路段长度>=
						<input id="upqualitydiffroadlen" name="upqualitydiffroadlen" style="width:120px;"  value="${questionRoadParam.upqualitydiffroadlen}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						m
					</div>
				</div>
			</div>

		</div>
	</div>
	<div style="width:50%;height:90%;float:left;border:1px solid #95b8e7;">
	  	<div data-options="region:'east',border:false,split:true">
	  		<div style="width:100%;height:33%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">下行低速率路段判定</div></div>
					<div class="inputDivShow">下行低速率采样点定义:下行RLC速率>=
						<input id="downlowerspeedrlc" name="downlowerspeedrlc" style="width:120px;"  value="${questionRoadParam.downlowerspeedrlc}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						Mbps
					</div>
					<div class="inputDivShow">下行低速率路段定义:下行低速率采样点占比>=
						<input id="downlowerspeedsamprate" name="downlowerspeedsamprate" style="width:120px;"  value="${questionRoadParam.downlowerspeedsamprate}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:30,max:100" />
						%
					</div>
					<div class="inputDivShow">下行低速率路段长度>=
						<input id="downlowerspeedroadlen" name="downlowerspeedroadlen" style="width:120px;"  value="${questionRoadParam.downlowerspeedroadlen}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						m
					</div>
				</div>
		    </div>
	    	<div style="width:100%;height:66%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">上行低速率路段判定</div></div>
					<div class="inputDivShow">上行低速率采样点定义:上行RLC速率>=
						<input id="uplowerspeedrlc" name="uplowerspeedrlc" style="width:120px;"  value="${questionRoadParam.uplowerspeedrlc}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						Mbps
					</div>
					<div class="inputDivShow">上行低速率路段定义:上行低速率采样点占比>=
						<input id="uplowerspeedsamprate" name="uplowerspeedsamprate" style="width:120px;"  value="${questionRoadParam.uplowerspeedsamprate}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:30,max:100" />
						%
					</div>
					<div class="inputDivShow">上行低速率路段长度>=
						<input id="uplowerspeedroadlen" name="uplowerspeedroadlen" style="width:120px;"  value="${questionRoadParam.uplowerspeedroadlen}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />
						m
					</div>
				</div>
			 </div>
		</div>
	</div>
  	<div data-options="region:'south',border:false" style="height:10%;">
	   	<table width="100%" style="border-top:1px solid #95b8e7;">
	   		<tr height="35px">
	    		<td width="50%;" align="right">
	    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
	    				<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >保存</a>
	    			</shiroextend:hasAnyPermissions>
	    		</td>
	    		<td width="50%;">
	    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">
	    				<a class="easyui-linkbutton" iconCls="icon-reload" style="width:100px;" onclick="resetForm();" >重置</a>
	    			</shiroextend:hasAnyPermissions>
	    		</td>
	   		</tr>
	   	</table>
  	</div>
  </body>
</html>
