<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>业务类异常事件分析参数</title>

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
            padding-left: 0;
            padding-right: 0;
            text-align: right;
            width: 200px;
        }
        .inputDivShow1{
            display: inline-block; *
        display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: center;
            width: 160px;
        }
        .inputDivShow2{
            display: inline-block; *
        display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: center;
            width: 80px;
        }
        .titleDivShow{
            display: inline-block; *
        display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: right;
            width: 180px;
        }
        .inputDivShow input,.inputDivShow1 input{
            width:60px;
        }
        .inputDivShow select.inputDivShow1 select{
            width:100px;
        }
    </style>
    <script type="text/javascript">
    	/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					$("#area_before").textbox('setValue',"30");
					$("#area_after").textbox('setValue',"30");
					$("#ftpDL_LostConnect_Cover").textbox('setValue',"30");
					$("#ftpDL_LostConnect_Quality").textbox('setValue',"30");
					$("#ftpUL_LostConnect_Cover").textbox('setValue',"30");
					$("#ftpUL_LostConnect_Quality").textbox('setValue',"30");
					$("#ping_fail_Cover").textbox('setValue',"30");
					$("#ping_fail_Quality").textbox('setValue',"30");
				}
			});
		}
    
        var submitForm = function () {
        	$('#infoform').form('submit', {
			    url:"${pageContext.request.contextPath}/exceptionparambussiness/updateexceptioneventbussinessmethod.action",
			    onSubmit: function(param){
			    	param.description = "业务类异常事件分析参数";
			    	param.gateType = 5;
			    },
			    success:function(data){
			    	var data = eval('(' + data + ')');
					if (data.success) {
						$.messager.alert('提示','数据更新成功','info');
					} else {
						$.messager.alert('提示','数据更新失败','info');
					}
			    }
			});
		}

    </script>
</head>
<body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<div data-options="region:'center',border:false" style="overflow-x: hidden;overflow-y: auto;">
	<form id="infoform" method="post">
        <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">业务类异常事件无线指标评估范围</div></div>
        <div style="margin-left: 100px">
            <p>
                <label><strong>失败发生之前</strong>
                    <input id="area_before" name="timeHighBusiness" value="${timeHighBusiness }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:1000" /> s
                </label>
                &nbsp;&nbsp;
                <label><strong>失败发生之后</strong>
                    <input id="area_after" name="timeLowBusiness" value="${timeLowBusiness }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:10000" /> s
                </label>
            </p>
        </div>
        <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">业务类异常事件无线原因分析门限设置</div></div>

        <div style="width:100%;">
            <div style="float: left;margin-left: 50px;margin-top: 20px">
                <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
                    <strong>FTP下载掉线</strong>
                </div>
            </div>
            <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
                <div><strong>弱覆盖采样点占比 >= </strong>
                    <input id="ftpDL_LostConnect_Cover" name="ftpDLLostConnectCover"  style="display: block" value="${ftpDLLostConnectCover}"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
                </div>
                <br>
                <div><strong>质差采样点占比 >= </strong>
                    <input id="ftpDL_LostConnect_Quality" name="ftpDLLostConnectQuality" value="${ftpDLLostConnectQuality}" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
                </div>
            </div>
        </div>

        <div style="width:100%;display: inline-block">
            <div style="float: left;margin-left: 50px;margin-top: 20px">
                <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
                    <strong>FTP上传掉线</strong>
                </div>
            </div>
            <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
                <div><strong>弱覆盖采样点占比 >= </strong>
                    <input id="ftpUL_LostConnect_Cover" name="ftpULLostConnectCover"  style="display: block" value="${ftpULLostConnectCover }"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
                </div>
                <br>
                <div><strong>质差采样点占比 >= </strong>
                    <input id="ftpUL_LostConnect_Quality" name="ftpULLostConnectQuality" value="${ftpULLostConnectQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
                </div>
            </div>
        </div>

	    <div style="width:100%;">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 150px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
	                <strong>ping掉线</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖采样点占比 >= </strong>
	                <input id="ping_fail_Cover" name="pingFailCover"  style="display: block" value="${pingFailCover }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="ping_fail_Quality" name="pingFailQuality" value="${pingFailQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
    
    </form>



</div>
<div data-options="region:'south',border:false" style="height:40px;">
    <table width="100%" style="border-top:1px solid #95b8e7;">
        <tr height="35px">
            <td width="50%;" align="right">
                <shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
                    <a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >确认</a>
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
