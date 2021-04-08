<%--
  Created by IntelliJ IDEA.
  User: czz19
  Date: 2019/9/16
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>NSA模式异常事件分析参数配置</title>
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
					$("#diffLteDiffNrCover").textbox('setValue',"30");
					$("#diffLteDiffNrQuality").textbox('setValue',"30");
					$("#sameLteDiffNrCover").textbox('setValue',"30");
					$("#sameLteDiffNrQuality").textbox('setValue',"30");
					$("#diffLteSameNrCover").textbox('setValue',"30");
					$("#diffLteSameNrQuality").textbox('setValue',"30");
					$("#scellAddCover").textbox('setValue',"30");
					$("#scellAddQuality").textbox('setValue',"30");
					$("#endcWirelessCover").textbox('setValue',"30");
					$("#endcWirelessQuality").textbox('setValue',"30");
				}
			});
		}
    	
        var submitForm = function () {
        	$('#infoform').form('submit', {
			    url:"${pageContext.request.contextPath}/exceptionparamnsa/updateexceptionnsamethod.action",
			    onSubmit: function(param){
			    	param.description = "NSA模式异常事件分析参数配置";
			    	param.gateType = 3;
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
            /* var timeHigh = $("#area_before").val();
            var timeLow = $("#area_after").val();

            var diffLteDiffNrCover = $("#diffLteDiffNrCover").val();
            var diffLteDiffNrQuality = $("#diffLteDiffNrQuality").val();

            var sameLteDiffNrCover = $("#sameLteDiffNrCover").val();
            var sameLteDiffNrQuality = $("#sameLteDiffNrQuality").val();

            var diffLteSameNrCover = $("#diffLteSameNrCover").val();
            var diffLteSameNrQuality = $("#diffLteSameNrQuality").val();
            
            var endcWirelessCover = $("#endcWirelessCover").val();
            var endcWirelessQuality = $("#endcWirelessQuality").val();

            var scellAddCover = $("#scellAddCover").val();
            var scellAddQuality = $("#scellAddQuality").val();

            var datas = {
                "gateType":3,
                "description":"NSA模式异常事件分析设置",
                "timeHigh":timeHigh,
                "timeLow":timeLow,


                "diffLteDiffNrCover":diffLteDiffNrCover,
                "diffLteDiffNrQuality":diffLteDiffNrQuality,
                "sameLteDiffNrCover":sameLteDiffNrCover,
                "sameLteDiffNrQuality":sameLteDiffNrQuality,
                "diffLteSameNrCover":diffLteSameNrCover,
                "diffLteSameNrQuality":diffLteSameNrQuality,
                "endcWirelessCover":endcWirelessCover,
                "endcWirelessQuality":endcWirelessQuality,
                "scellAddCover":scellAddCover,
                "scellAddQuality":scellAddQuality
            }

            $.ajax({
                url:"${pageContext.request.contextPath}/exceptionparamnsa/updateexceptionnsamethod.action",
                type:"POST",
                dataType:"text",
                data:datas,
                success:function(){
                    alert("数据更新成功");
                },
                error:function (result) {
                    alert("数据更新失败");
                }
            }); */
        }
    </script>
</head>
<body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<div data-options="region:'center',border:false" style="overflow-x: hidden;overflow-y: auto;">
	<form id="infoform" method="post">
	    <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">NSA制式接入失败时候无线指标评估范围</div></div>
	    <div style="margin-left: 100px">
	        <p>
	            <label><strong>终端辅小区变换之前</strong>
	                <input id="area_before" name="timeHighNSA" value="${timeHighNSA }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:1000" /> s
	            </label>
	            &nbsp;&nbsp;
	            <label><strong>终端辅小区变换之后</strong>
	                <input id="area_after" name="timeLowNSA" value="${timeLowNSA }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:10000" /> s
	            </label>
	        </p>
	    </div>
	    <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">NSA制式接入失败时候无线原因分析门限设置</div></div>
	
	    <div style="width:100%;">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 200px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
	                <strong>Scell add失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖点占比 >= </strong>
	                <input id="scellAddCover" name="scellAddCover"  style="display: block" value="${scellAddCover }"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="scellAddQuality" name="scellAddQuality" value="${scellAddQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
	
	
	    <div style="width:100%;display: inline-block">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 200px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;">
	                <strong>Diff_LTE_Diff_NR失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖点占比 >= </strong>
	                <input id="diffLteDiffNrCover" name="diffLteDiffNrCover"  style="display: block" value="${diffLteDiffNrCover }"   class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="diffLteDiffNrQuality" name="diffLteDiffNrQuality" value="${diffLteDiffNrQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
	
	    <div style="width:100%;">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 200px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
	                <strong>Same_LTE_Diff_NR失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖点占比 >= </strong>
	                <input id="sameLteDiffNrCover" name="sameLteDiffNrCover"  style="display: block" value="${sameLteDiffNrCover }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="sameLteDiffNrQuality" name="sameLteDiffNrQuality" value="${sameLteDiffNrQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
	            </div>
	        </div>
	    </div>
	
	
	    <div style="width:100%;display: inline-block">
		    <div style="float: left;margin-left: 50px;margin-top: 20px">
		        <div style="width: 200px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
		            <strong>Diff_LTE_Same_NR失败</strong>
		        </div>
		    </div>
		    <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
		        <div><strong>弱覆盖点占比 >= </strong>
		            <input id="diffLteSameNrCover" name="diffLteSameNrCover"  style="display: block" value="${diffLteSameNrCover }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
		        </div>
		        <br>
		        <div><strong>质差采样点占比 >= </strong>
		            <input id="diffLteSameNrQuality" name="diffLteSameNrQuality" value="${diffLteSameNrQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
		        </div>
		    </div>
		</div>
	
	    <div style="width:100%;display: inline-block">
	        <div style="float: left;margin-left: 50px;margin-top: 20px">
	            <div style="width: 200px;height: 100px;background-color: #0e84b5;line-height: 100px;text-align: center;word-break: break-all;">
	                <strong>EN-DC无线链路失败</strong>
	            </div>
	        </div>
	        <div style="float: left;margin-left: 10px;margin-top: 40px;width: 500px;height: 100px">
	            <div><strong>弱覆盖点占比 >= </strong>
	                <input id="endcWirelessCover" name="endcWirelessCover"  style="display: block" value="${endcWirelessCover }"  class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /> <strong>% 认为是弱覆盖原因</strong>
	            </div>
	            <br>
	            <div><strong>质差采样点占比 >= </strong>
	                <input id="endcWirelessQuality" name="endcWirelessQuality" value="${endcWirelessQuality }" class="easyui-numberbox" data-options="required:false,validType:'length[1,24]',min:1,max:100" /><strong>% 认为是质差原因</strong>
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