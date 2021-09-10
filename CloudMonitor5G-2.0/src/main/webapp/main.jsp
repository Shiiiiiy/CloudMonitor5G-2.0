<%@page import="java.lang.annotation.Target"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>5G路测云平台</title>
	<style type="text/css">
		* {
			margin: 0;
			padding: 0;
			list-style: none;
		}
		.main ul {
			width: 80px;
			border: 0;
			overflow: visible;
		}
		.main ul .first {
			width: 80px;
			height: 80px;
			text-align: center;
			margin-top: -1px;
			position: relative;
		}
		.main ul li.home {
			margin-top: 0;
		}
		.main ul .first a {
			width: 80px;
			height: 70px;
			color: #212e50;
			background: #f0f5f8;;
			font-size: 12px;
			display: block;
			text-decoration: none;
			padding-top: 10px;
		}
		.main ul .first a:hover {
			background: #83abdf;
		}
		.main ul .first span {
			font-family: "iconfont";
			font-size: 36px;
			color: #5e8dca;
		}
		.first ul {
			width: 135px;
			display: none;
			position: absolute;
			top: 0;
			left: 80px;
			border: 1px solid #c9e1f9;
			background: #f0f5f8;;
		}
		.first ul .second {
			width: 135px;
			height: 30px;
			text-align:left;
			line-height: 30px;
			background: #f0f5f8;
			position: relative;
		}
		.first ul .second a {
			width: 120px;
			height: 30px;
			padding-left: 15px;
			padding-top: 0;
		}
		.first ul .second a:hover {
			color: #fff;
			background: #83abdf;
		}
		.first ul .second a div{
			width: 16px;
			height:16px;
			top:7px;
			right:0px;
			position:absolute;
			background: url(${pageContext.request.contextPath}/js/easyui/themes/default/images/menu_arrows.png) repeat -32px 0px;
			display: inline-block;
		}
		.first ul .second a .hover{
			background: url(${pageContext.request.contextPath}/js/easyui/themes/default/images/menu_arrows_hover.png) repeat -32px 0px;
		}
		.second ul {
			width: 135px;
			left: 135px;
			top: -1px;
			border: 1px solid #c9e1f9;
		}
		.second ul .third {
			width: 200px;
			height: 30px;
			line-height: 30px;
			background: #f0f5f8;
		}
		.second ul .third a {
			width: 250px;
			height: 30px;
			padding-left: 15px;
			padding-top: 0;
		}
		.move:hover {
			color: #fff;
			background: #83abdf;
		}
		.login{
			background:url('css/img/login_01.png') no-repeat;
		}
		.login:hover{
			background:url('css/img/login_02.png') no-repeat;
		}
	</style>
	<%@ include file="taglibs/jquery.jsp"%>
	<%@ include file="taglibs/easyui.jsp"%>
	<script type="text/javascript">
        $(function() {
            $('.main>ul>li').hover(function(e) {
                $(this).children('ul').toggle();
            });
            $('.first>ul>li').hover(function(e) {
                $(this).children('ul').toggle();
                $(this).children('a').children('div').addClass('hover');
            },function(e){
                $(this).children('ul').toggle();
                $(this).children('a').children('div').removeClass('hover');
            });
            checkLastPostTime();
        });

        /* 存储移除的menu */
        var noteArray = new Array();
        /* 移除menu */
        function up() {
            if ($(".main>ul>li").length > 1) {
                noteArray.push($(".main>ul>li").first().clone(true));
                $(".main>ul>li").first().remove();
            }
        }
        /* 还原menu */
        function down() {
            var node = noteArray.pop();
            if (node != null) {
                node.show().prependTo(".main>ul");
            }
        }
        window.setInterval(function() {
            var realTime = new Date();
            var realYear = realTime.getFullYear();
            var realMouth = realTime.getMonth() + 1;
            var realDate = realTime.getDate();
            var realHour = realTime.getHours();
            var realMinute = realTime.getMinutes();
            var realSecond = realTime.getSeconds();
            $("#realTime").html(
                realYear + "年" + realMouth + "月" + realDate + "日" + "&nbsp;"
                + realHour + ":" + realMinute + ":" + realSecond);
        }, 500);

        var checkLastPostTimeInterval;
        function checkLastPostTime(){
            checkLastPostTimeInterval = window.setInterval(function() {
                $.post("${pageContext.request.contextPath}/session/checkLastPostTime.action",{},
                    function(result){
                        if (result) {
                            window.clearInterval(checkLastPostTimeInterval);
                            $("#checkLastPostTimeDialogError").html(" ");
                            $("#checkLastPostTimeDialog").dialog('open');
                        }
                    }
                    ,"json");
            }, 300000);
        }

        function checkLastPostTimeDialogFormSumbit(){
            if($('#checkLastPostTimeDialogForm').form('validate')){
                $.post("${pageContext.request.contextPath}/jsonLogin.action",{
                        username:$("#checkLastPostTimeDialogFormUserName").val(),
                        password:$("#checkLastPostTimeDialogFormPassword").textbox('getValue')
                    },
                    function(result){
                        if (result.errorMsg) {
                            $("#checkLastPostTimeDialogError").html(result.errorMsg);
                        } else {
                            $("#checkLastPostTimeDialog").dialog('close');
                            checkLastPostTime();
                        }
                    }
                    ,"json");
            }
        }

        function changepwd() {
            $("#changePasswordDlg").dialog('open').dialog('setTitle', '修改【<shiro:principal/>】密码');
            url = 'user/changePassword';
        }

        function changepassword() {
            $('#changePasswordForm').form('submit', {
                url : url,
                onSubmit : function() {
                    return $(this).form('validate');
                },
                success : function(result) {
                    result =$.parseJSON(result);
                    if (result.errorMsg) {
                        $.messager.alert("系统提示", result.errorMsg);
                        return;
                    } else {
                        $.messager.alert("系统提示", "修改密码成功");
                        $('#changePasswordDlg').dialog('close');
                        $("#dg").datagrid("reload");
                    }
                }
            });
        }
        function logout(){
            $.messager.confirm("提示","你确定要退出该系统吗?",function(flag){
                if(flag){
                    window.location="${pageContext.request.contextPath}/logout";
                }
            });
        }
        $.extend($.fn.validatebox.defaults.rules, {
            /*必须和某个字段相等*/
            equalTo : {
                validator : function(value, param) {
                    return $(param[0]).val() == value;
                },
                message : '字段不匹配'
            }
        });
        /**
         *
         */
        function messagerAlert(title,text,icon){
            $.messager.alert(title,text,icon);
        }
	</script>
	<script type="text/javascript" src="js/common.js" ></script>

</head>
<body class="easyui-layout">
<!-- LOGO -->
<div data-options="region:'north',border:false" style="height:48px;background: url(${pageContext.request.contextPath}/images/topBg_01.jpg) repeat center center;">
	<img src="${pageContext.request.contextPath}/images/topBg.jpg"
		 style="z-index: 2;" width="1200px" height="48px">
	<div
			style="text-align:right; padding-top:10px;position: absolute;top:0;right:5%; z-index: 1;width:95%;height:100%;">
		<strong><label id="realTime" style="color:#ecedf1"></label></strong><br />
		<label style="color:#ecedf1">用户名:&nbsp;</label> <label
			style="color:#ecedf1"><strong><shiro:principal />&nbsp;&nbsp;&nbsp;&nbsp;</strong></label>
		<!-- 提供menu属性，指向下拉菜单项div的id -->
		<a href="javascript:void(0)" class="easyui-menubutton"
		   data-options="menu:'#bm'">控制面板</a>
	</div>
</div>

<!-- 版权 -->
<div data-options="region:'south'"
	 style="height:20px;background:#e1ecff;">
	<div
			style="width:100%;height:100%;text-align:center;font-size:1em;z-index: 1;">
		<table width=100% height=100%>
			<tr align="center">
				<td><font color="#212e50">Copyright&copy;2018&nbsp;DTmobile&nbsp;@Version&nbsp;0.0.1<!-- 大唐移动通信设备有限公司 --></font>
				</td>
			</tr>
		</table>
	</div>
</div>

<!-- 菜单 -->
<div data-options="region:'west',title:'菜单'"
	 style="width:82px;background:#f0f5f8;position: absolute;">
	<!-- 上移菜单 -->
	<div class="move" onclick="up();" align="center">
		<p
				style="width:16px;height:16px;background:url(${pageContext.request.contextPath}/js/easyui/themes/default/images/menu_arrows.png) -16px 0;"></p>
	</div>
	<!-- first一级菜单,second二级菜单,third三级菜单 -->
	<div class="main">
		<ul>
			<shiroextend:hasAnyPermissions name="activation:show">
				<%-- <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/VoLTE_diss.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />活跃度查看</a>
                    <ul>
                        <shiroextend:hasAnyPermissions name="activation:show">
                            <li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="activationShowAction/goToActivationShowList.action">活跃度查看</a></li>
                        </shiroextend:hasAnyPermissions>
                     </ul>
                </li> --%>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions  name="user:show,usergroup:show,projectparam:show,volteanalysisthreshold:show" >
				<li class="first">
					<a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/platform.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />平台管理</a>
					<ul>
						<shiroextend:hasAnyPermissions name="user:show,usergroup:show">
							<li class="second"><a href="javascript:void(0);" >权限管理<div></div></a>
								<ul>
									<shiroextend:hasAnyPermissions name="user:show">
										<li class="third"><a href="javascript:void(0);"  onclick="addTab(this);" url="user/userListUI.action">分析账号管理</a></li>
									</shiroextend:hasAnyPermissions>
									<shiroextend:hasAnyPermissions name="usergroup:show">
										<li class="third"><a href="javascript:void(0);"  onclick="addTab(this);" url="userGroup/userGroupListUI.action">分析账号组管理</a></li>
									</shiroextend:hasAnyPermissions>
								</ul>
							</li>
						</shiroextend:hasAnyPermissions>

						<shiroextend:hasAnyPermissions name="projectparam:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="projectParam/projectParamListUI.action">工程参数</a></li>
						</shiroextend:hasAnyPermissions>

						<shiroextend:hasAnyPermissions name="stationParam:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="stationParam/getToStationParamJsp.action">单站参数设置</a></li>
						</shiroextend:hasAnyPermissions>

						<shiroextend:hasAnyPermissions name="volteanalysisthreshold:show">
							<li class="second"><a href="javascript:void(0);"  >分析门限<div ></div></a>
								<ul>
									<shiroextend:hasAnyPermissions name="volteanalysisthreshold:show">
										<!-- <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="aboutThreshold/volteDissThresholdListUI">VoLTE专题分析门限</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="aboutThreshold/streamDissThresholdListUI">流媒体专题分析门限</a></li> -->
										<!-- <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="contrastThreshold/contrastDissThresholdListUI">对比分析门限</a></li> -->
										<!-- <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="fgAnaThreshold/fgAnalysisThresholdListUI">参数设置</a></li> -->
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/weakCoverageParamListUI">弱覆盖参数设置</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/overCoverageParamListUI">过覆盖参数设置</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/overlayCoverageParamListUI">重叠覆盖参数设置</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/spfarCoverageParamListUI">超远覆盖参数设置</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/reverseCoverageParamListUI">反向覆盖参数设置</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/lteQualityBadListUI">质差参数设置</a></li>
										<!-- <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="coverageParam/displayMapRangeParamListUI">覆盖指标地图显示区间设置</a></li> -->
										<%--todo 根据参数类型配置--%>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="exceptionparam45g/exceptionparam45glistui">4/5G互操作的异常事件分析参数</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="exceptionparamsaaccess/exceptionparamsaaceesslistui">SA模式接入性异常事件分析参数</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="exceptionparamsamove/exceptionparamsamovelistui">SA模式移动性异常事件分析参数</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="exceptionparamnsa/exceptionparamsnsalistui">NSA模式异常事件分析参数</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="exceptionparambussiness/exceptionparambussinesslistui">业务类异常事件分析参数</a></li>
										<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="questionRoadParam/questionRoadListUI">问题路段分析参数</a></li>

									</shiroextend:hasAnyPermissions>
								</ul>
							</li>
						</shiroextend:hasAnyPermissions>

						<shiroextend:hasAnyPermissions name="volteanalysisthreshold:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="mapTrailLegendParam/listUI.action">轨迹图阈值设置</a></li>
						</shiroextend:hasAnyPermissions>

						<shiroextend:hasAnyPermissions name="stationParam:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="customrReportTemplate/listUI.action">自定义报表模板</a></li>
						</shiroextend:hasAnyPermissions>
					</ul>
				</li>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="terminalmanage:show">
				<li class="first">
					<a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/testManage.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />测试管理</a>
					<ul>
						<shiroextend:hasAnyPermissions name="terminalmanage:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="terminalGroup/terminalGroupListUI.action">终端管理</a></li>
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="testPlan/testPlanUI.action">测试计划</a></li>
							<li class="second"><a href="javascript:void(0);">监控管理<div ></div></a>
								<ul>
									<li class="second"><a href="javascript:void(0);" url="ATULogin/listUI.action"  onclick="addTab(this);">ATU登陆日志</a></li>
									<li class="second"><a href="javascript:void(0);" url="event/listUI.action"  onclick="addTab(this);">事件监控</a></li>
									<li class="second"><a href="javascript:void(0);" url="alarm/listUI.action"  onclick="addTab(this);">告警监控</a></li>
									<li class="second"><a href="javascript:void(0);" url="mosValue/listUI.action"  onclick="addTab(this);">MOS监控</a></li>
									<li class="second"><a href="javascript:void(0);" url="status/listUI.action"  onclick="addTab(this);">终端状态监控</a></li>
									<li class="second"><a href="javascript:void(0);" url="mapMonitor/listUI.action"  onclick="addTab(this);">地图监控</a></li>
									<!-- <li class="second"><a href="javascript:void(0);" url="terminalInfo/listUI.action"  onclick="addTab(this);">终端信息监控</a></li> -->
								</ul>
							</li>
							<li class="second"><a href="javascript:void(0);" url="errorLogManage/errorLogManageListUI.action"  onclick="addTab(this);">错误日志管理</a></li>
						</shiroextend:hasAnyPermissions>
					</ul>
				</li>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="testlogitem:upload,testlogitem:analysis,stationVeri:show,testlogitemUnicom:show">
				<li class="first">
					<a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/testLog.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />测试日志</a>
					<ul>
						<shiroextend:hasAnyPermissions name="testlogitem:upload">
							<li class="second"><a href="javascript:void(0);" url="logUpload/listUI.action" onclick="addTab(this);">上传日志</a></li>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitem:analysis">
							<li class="second"><a href="javascript:void(0);" url="testLogItem/testLogItemListUI.action"  onclick="addTab(this);">已完成日志</a></li>
						</shiroextend:hasAnyPermissions>
						<!-- 							<li class="second"><a href="javascript:void(0);" url="testLogItem/unfinishedTestLogItemListUI.action" onclick="addTab(this);">未完成日志</a></li>
                                                    <li class="second"><a href="javascript:void(0);" url="networkTestLogItem/networkTestLogItemListUI.action" onclick="addTab(this);">网络侧日志</a></li> -->
						<shiroextend:hasAnyPermissions name="stationVeri:show">
							<li class="second"><a href="javascript:void(0);" url="stationVerificationTest/goToStationVerificationJsp.action" onclick="addTab(this);">单站验证日志</a></li>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:show">
							<li class="second"><a href="javascript:void(0);" url="unicomLogItem/unicomLogItemListUI.action" onclick="addTab(this);">联通日志</a></li>
						</shiroextend:hasAnyPermissions>
							<%-- 							<shiroextend:hasAnyPermissions name="testlogitem:upload">
                                                            <li class="second"><a href="javascript:void(0);" url="customeUploadTestLog/goToCustomReportLogJsp.action" onclick="addTab(this);">自定义报表日志</a></li>
                                                        </shiroextend:hasAnyPermissions> --%>
					</ul>
				</li>
			</shiroextend:hasAnyPermissions>
			<%-- <shiroextend:hasAnyPermissions name="voltetotal:show,voltecompare:show,voltevoicebadroad:show,continueWirelessBadRoad:show,voltevoicenotconnect:show,voltevoicedropcall:show,volteimsregistfail:show,voltecsfbfail:show,voltesrvcccutfail:show,voltesystemcutfail:show,callEstablishDelayException:show">
                <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/VoLTE_diss.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />VoLTE专题</a>
                    <ul>
                        <shiroextend:hasAnyPermissions name="voltetotal:show">
                            <li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="volteWhole/wholePreList.action">VoLTE总体概览</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="voltecompare:show">
                            <li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="voiceCompare/goToCompareAnalysisListUI.action">VoLTE对比分析</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="voltevoicebadroad:show,continueWirelessBadRoad:show,voltevideobadroad:show,voicertplosspage:show">
                            <li class="second"><a href="javascript:void(0);">VoLTE质量专题<div ></div></a>
                                <ul>
                                    <shiroextend:hasAnyPermissions name="voltevoicebadroad:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="voiceQBR/goToqualityBadRoadList">VoLTE语音质差</a></li>
                                    </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="continueWirelessBadRoad:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="volteCWBR/goTocontinueWirelessBadRoadList">连续无线差</a></li>
                                     </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="voicertplosspage:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="lostPacket/goToLostPacketList">语音RTP连续丢包</a></li>
                                     </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="voltevideobadroad:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="videoQuality/goTovideoQualityBadList">VOLTE视频质差</a></li>
                                     </shiroextend:hasAnyPermissions>
                                 </ul>
                            </li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="voltevoicenotconnect:show,voltevoicedropcall:show,volteimsregistfail:show,voltecsfbfail:show,voltevideonotconnect:show,voltevideodropcall:show">
                            <li class="second"><a href="javascript:void(0);">VoLTE异常事件<div ></div></a>
                                <ul>
                                    <shiroextend:hasAnyPermissions name="voltevoicenotconnect:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="voiceNotConnect/goToNotConnectList">VoLTE语音未接通</a></li>
                                    </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="voltevoicedropcall:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="voiceDropCall/goToDropCallList">VoLTE语音掉话</a></li>
                                    </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="volteimsregistfail:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="imsRegistFail/goToRegistFailList">IMS注册失败</a></li>
                                    </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="voltecsfbfail:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="csfbFail/goToCsfbFailList">CSFB失败</a></li>
                                     </shiroextend:hasAnyPermissions>
                                     <shiroextend:hasAnyPermissions name="voltevideonotconnect:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="videoNotConnect/goToNotConnectList">VoLTE视频未接通</a></li>
                                    </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="voltevideodropcall:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="videoDropCall/goToDropCallList">VoLTE视频掉话</a></li>
                                    </shiroextend:hasAnyPermissions>
                                 </ul>
                            </li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="voltesrvcccutfail:show,voltesystemcutfail:show">
                            <li class="second"><a href="javascript:void(0);">VoLTE切换失败<div ></div></a>
                                <ul>
                                    <shiroextend:hasAnyPermissions name="voltesrvcccutfail:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="droppingSRVCC/goToDroppingSRVCCList">SRVCC切换失败</a></li>
                                    </shiroextend:hasAnyPermissions>
                                    <shiroextend:hasAnyPermissions name="voltesystemcutfail:show">
                                        <li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="droppingInt/goToDroppingIntList">系统内切换失败</a></li>
                                     </shiroextend:hasAnyPermissions>
                                 </ul>
                            </li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="callEstablishDelayException:show">
                            <li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="callEstablish/goToCallEstablishDelayExceptionList">呼叫建立时延异常</a></li>
                        </shiroextend:hasAnyPermissions>
                     </ul>
                </li>
            </shiroextend:hasAnyPermissions> --%>
			<%-- <shiroextend:hasAnyPermissions name="voltetotal:show">
                <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/stream.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />流媒体专题</a>
                    <ul>
                        <shiroextend:hasAnyPermissions name="voltetotal:show">
                            <li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="streamWhole/wholePreList.action">流媒体总体概览</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="voltecompare:show">
                            <li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="streamQuality/goToStreamQualityBadList.action">流媒体视频质差</a></li>
                        </shiroextend:hasAnyPermissions>
                    </ul>
                </li>
            </shiroextend:hasAnyPermissions> --%>
			<%-- <shiroextend:hasAnyPermissions name="basereport:show">
                <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/analysis.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />统计分析</a>
                    <ul>
                    <shiroextend:hasAnyPermissions name="basereport:show">
                        <li class="second"><a href="javascript:void(0);" url="report/listUI.action"  onclick="addTab(this);">基础统计</a></li>
                        </shiroextend:hasAnyPermissions>
                        <!-- <li class="second"><a href="javascript:void(0);" url="cqtReport/listUI.action"  onclick="addTab(this);">CQT统计</a></li>
                        <li class="second"><a href="javascript:void(0);" url="floorReport/listUI.action"  onclick="addTab(this);">楼宇统计</a></li> -->
                    </ul>
                </li>
            </shiroextend:hasAnyPermissions> --%>
			<shiroextend:hasAnyPermissions name="basereport:show">
				<li class="first">
					<a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/analysis.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />统计分析</a>
					<ul>
						<!-- <li class="second"><a href="javascript:void(0);" url="compareAnalysis5g/goToCompareAnalysis5gListUI.action"  onclick="addTab(this);">5G对比分析</a></li> -->
						<li class="second"><a href="javascript:void(0);" url="report5g/listUI.action"  onclick="addTab(this);">5G基础统计</a></li>
						<li class="second"><a href="javascript:void(0);" url="customeLogReport/listUI.action"  onclick="addTab(this);">自定义报表统计</a></li>
						<li class="second"><a href="javascript:void(0);" url="knowFeeling/listUI.action"  onclick="addTab(this);">业务感知详单</a></li>
					</ul>
				</li>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="voltevoicebadroad:show">
				<li class="first">
					<a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/VoLTE_diss.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />专题分析</a>
					<ul>
							<%-- <shiroextend:hasAnyPermissions name="voltevoicebadroad:show">
								<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="embbCover5g/goToEmbbCoverBadRoadList.action">eMBB覆盖类专题分析</a></li>
							</shiroextend:hasAnyPermissions>

							<shiroextend:hasAnyPermissions name="voltevoicebadroad:show">
								<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="qualityBadRoad5g/goToQualityBadRoad5gJSP.action">质差分析专题</a></li>
							</shiroextend:hasAnyPermissions>

							todo 异常事件专题分析 --%>
						<shiroextend:hasAnyPermissions name="voltevoicebadroad:show">
							<li class="second"><a href="javascript:void(0);">eMBB事件类专题分析</a>
								<ul>
									<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="nasexceptionview/nasexceptionviewlistui?pageNum=1&pageSize=20">NSA模式异常事件分析</a></li>
									<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="samoveexceptionview/samoveviewui">4/5G互操作的异常事件分析</a></li>
									<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="samoveswitchexceptionview/samoveswitchviewui">SA模式移动性异常事件分析</a></li>
									<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="saaccessexceptionview/saaccessviewui">SA模式接入性异常事件分析</a></li>
									<li class="third"><a href="javascript:void(0);" onclick="addTab(this);" url="bussinessexceptionview/bussinessviewui">业务类异常事件分析</a></li>
								</ul>


							</li>
						</shiroextend:hasAnyPermissions>

						<shiroextend:hasAnyPermissions name="voltevoicebadroad:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="embbCoverAnalyse/nasexceptionviewlistui?pageNum=1&pageSize=20">eMBB覆盖分析</a></li>
						</shiroextend:hasAnyPermissions>
					</ul>
				</li>
			</shiroextend:hasAnyPermissions>
			<%-- <shiroextend:hasAnyPermissions name="station:show">
                <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/station.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />单站验证</a>
                    <ul>
                        <shiroextend:hasAnyPermissions name="stationCreate:show">
                            <li class="second"><a href="javascript:void(0);" url="stationReportCreat/goToStationReportCreatJsp.action"  onclick="addTab(this);">单验报告生成</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="stationShow:show">
                            <li class="second"><a href="javascript:void(0);" url="stationReportShow/goToStationReportShowJsp.action"  onclick="addTab(this);">单验报告查看</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="stationCompletion:show">
                            <li class="second"><a href="javascript:void(0);" url="stationCompletionShow/goTOStationCompletionShowJsp.action"  onclick="addTab(this);">单验进度查看</a></li>
                        </shiroextend:hasAnyPermissions>
                    </ul>
                </li>
            </shiroextend:hasAnyPermissions>
            <shiroextend:hasAnyPermissions name="opposite:show">
                <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/opposite3D.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />反开3d</a>
                    <ul>
                        <shiroextend:hasAnyPermissions name="oppositeCreate:show">
                            <li class="second"><a href="javascript:void(0);" url="oppositeOpen3dReportCreate/getOppositeOpen3dReportCreateJsp.action"  onclick="addTab(this);">反开3d报告生成</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="oppositeShow:show">
                            <li class="second"><a href="javascript:void(0);" url="oppositeOpen3dReportShow/goToOppositeOpen3dReportShowJsp.action"  onclick="addTab(this);">反开3d报告查看</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="oppositeCompletion:show">
                            <li class="second"><a href="javascript:void(0);" url="oppositeOpen3dCompletionShow/goToOppositeOpen3dCompletionShowJsp.action"  onclick="addTab(this);">反开3d进度查看</a></li>
                        </shiroextend:hasAnyPermissions>
                    </ul>
                </li>
            </shiroextend:hasAnyPermissions>
            <shiroextend:hasAnyPermissions name="CQTTask:show,stationTask:show">
                <li class="first">
                    <a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/opposite3D.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />工单</a>
                    <ul>
                        <shiroextend:hasAnyPermissions name="CQTTask:show">
                            <li class="second"><a href="javascript:void(0);" url="testTaskManage/listUI.action"  onclick="addTab(this);">指定测试</a></li>
                        </shiroextend:hasAnyPermissions>
                        <shiroextend:hasAnyPermissions name="stationTask:show">
                            <li class="second"><a href="javascript:void(0);" url="stationReportCreatTask/listUI.action"  onclick="addTab(this);">单验任务</a></li>
                        </shiroextend:hasAnyPermissions>
                    </ul>
                </li>
            </shiroextend:hasAnyPermissions> --%>
			<shiroextend:hasAnyPermissions name="appTestInfoMonitor:show">
				<li class="first">
					<a href="javascript:void(0);"><img src="${pageContext.request.contextPath}/images/realtimeMontitoring.png" style="border:none;padding-bottom: 5px;" width="40px" height="40px" /><br />实时监控</a>
					<ul>
						<shiroextend:hasAnyPermissions name="appTestInfoMonitor:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="mos/appTestInfoListUI.action">APP测试信息监控</a></li>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="appTestInfoMonitor:show">
							<li class="second"><a href="javascript:void(0);" onclick="addTab(this);" url="cityPerceptAnalysis/appTestInfoListUI.action">城市维度感知数据分析</a></li>
						</shiroextend:hasAnyPermissions>
					</ul>
				</li>
			</shiroextend:hasAnyPermissions>
		</ul>

	</div>
	<!-- 恢复菜单 -->
	<div class="move" align="center" onclick="down();"
		 style="width:100%;position:absolute;bottom:0;">
		<p style="width:16px;height:16px;background:url(${pageContext.request.contextPath}/js/easyui/themes/default/images/menu_arrows.png) 0 0;"></p>
	</div>
</div>

<div id="changePasswordDlg" class="easyui-dialog" data-options="onMove:onDrag"
	 style="width:400px;height:200px;padding:10px 20px" closed="true"
	 buttons="#changePasswordDlg-buttons">
	<form id="changePasswordForm" method="post">
		<table cellspacing="10px;">
			修改密码：
			<input id="password" name="password" class="easyui-textbox"
				   required="true" type="password" value="" />
			<br /><br /> 确认密码：
			<input type="password" id="repassword"
				   required="true" class="easyui-textbox"
				   validType="equalTo['#password']" invalidMessage="两次输入密码不匹配" />
		</table>
	</form>
</div>

<div id="changePasswordDlg-buttons">
	<a href="javascript:void(0)" class="easyui-linkbutton"
	   iconCls="icon-ok" onclick="changepassword()">确定</a> <a
		href="javascript:void(0)" class="easyui-linkbutton"
		iconCls="icon-cancel"
		onclick="javascript:$('#changePasswordDlg').dialog('close')">取消</a>
</div>


<!-- 页面主体 -->
<div data-options="region:'center',border:false">
	<!-- tabs -->
	<div id="mytabs" class="easyui-tabs" data-options="fit:true">
		<div data-options="closeable:'false',title:'主页',content:createIframe('homepage.html')" style="padding: 0;">
		</div>
	</div>
</div>
<div id="bm">
	<div data-options="iconCls:'icon-edit'" onclick="changepwd()">修改密码</div>
	<div onclick="logout();">
		注销
	</div>
</div>
<div id="checkLastPostTimeDialog" class="easyui-dialog" style="width:400px;height:200px;padding:20px;" data-options="title:'已锁定',border:false,closable:false,draggable:false,resizable:false,closed:true,modal:true,tools:'#checkLastPostTimeDialogTool'">
	<form id="checkLastPostTimeDialogForm" method="post">
		<div id="checkLastPostTimeDialogError" style="color:red;position:absolute;right:20px;" align="right" ></div>
		<table width="100%" >
			<tr>
				<td >账&nbsp;&nbsp;&nbsp;&nbsp;号:</td>
				<td height="40px"  align="left"><shiro:principal /><input id="checkLastPostTimeDialogFormUserName" type="hidden"    style="width:220px;height:30px;line-height:30px;border-color:#5b97db;border-width: 1px;border-style: solid;" name="username" value="<shiro:principal />" /></td>
			</tr>
			<tr>
				<td >密&nbsp;&nbsp;&nbsp;&nbsp;码:</td>
				<td height="40px"  align="left"><input id="checkLastPostTimeDialogFormPassword" required="true" class="easyui-textbox" type="password" style="width:220px;height:30px;line-height:30px;border-color:#5b97db;border-width: 1px;border-style: solid;" name="password" value="" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input class="login"  type="button" onclick="checkLastPostTimeDialogFormSumbit();" style="width:110px;height:33px;border:0" value="" />
				</td>
			</tr>
		</table>
	</form>
</div>
<div id="checkLastPostTimeDialogTool">
	<a href="#" style="width:30px;text-decoration:underline;line-height:16px;font-size:12px;" onclick="logout();" >注销</a>
</div>
</body>
</html>