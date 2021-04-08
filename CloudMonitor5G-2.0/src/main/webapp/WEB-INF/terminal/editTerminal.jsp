<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>编辑设备</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<style type="text/css">
		.inputDivShow{ 
			display: inline-block;*
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 255px;
   		}
   		.inputDivShow input{
   			width:140px;
   		}
   		.inputDivShow select{
   			width:140px;
   		}
	</style>
	<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {
			/*必须和某个字段相等*/
			number : {
				validator : function(value, param) {
					var minLength = param[0];
					var maxLength = param[1];
					return $.isNumeric(value)?(value.length>=minLength&&value.length<=maxLength):false;
				},
				message : ''
			}
		});
		$(function(){
			initTable();
		});
		
		/* 终端模块table列 */
		var moduleTableInfoColumns=[[
			{field:'moduleType',width:60,align:'center',title:'模块类型 '}, 				
			{field:'channelsNo',width:60,align:'center',title:'模块序号'},
			{field:'cmoschipType',width:60,align:'center',title:'芯片类型'},
			{field:'simCard',width:90,align:'center',title:'sim卡号'},
			{field:'operator',width:60,align:'center',title:'运营商'},
			{field:'msgCenterNo',width:90,align:'center',title:'短消息中心号'},
			{field:'msgAlarmNo',width:90,align:'center',title:'短消息告警号'},
			{field:'testTerminalType',width:90,align:'center',title:'测试设备类型'}
		]];
		var moduleData = [];
		<c:forEach items="${terminal.testModuls}" var="testModul">
	    	moduleData.push({
	    		moduleType:'${testModul.moduleType}',
	    		channelsNo:'${testModul.channelsNo}',
	    		cmoschipType:'${testModul.cmoschipType}',
	    		simCard:'${testModul.simCard}',
	    		operator:'${testModul.operator}',
	    		msgCenterNo:'${testModul.msgCenterNo}',
	    		msgAlarmNo:'${testModul.msgAlarmNo}',
	    		testTerminalType:'${testModul.testTerminalType}'
	    	});
	    </c:forEach>
	    
		/* 初始化终端模块列表 */
		function initTable(){
			$("#moduleTable").datagrid({
				// 表头
				columns:moduleTableInfoColumns,
				idField:'channelsNo',
				data:moduleData,
				title:'模块列表',
				fitColumns:true,
				singleSelect:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				scrollbarSize:0,
				toolbar:[
					{text:'新增',iconCls:'icon-add',handler:addTestModule},'-',
					{text:'编辑',iconCls:'icon-edit',handler:editTestModule},'-',
					{text:'删除',iconCls:'icon-cancel',handler:delTestModule}
				]
			});
		}
		
		/* 添加终端模块 */
		function addTestModule(){
			$.post("${pageContext.request.contextPath}/terminal/newTeModuleInfo.action",{},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						testModuleNo = null;
						$("#testModuleInfoForm").form('reset');
						$("#channelsNo").combobox('readonly',false),
						$("#testModuleInfoDlg").dialog('setTitle','添加终端模块').dialog('open');
					}
				}
			,"json");
		}
		
		/* 修改终端模块 */
		var testModuleNo;
		function editTestModule(){
			var moduleTableRow = $("#moduleTable").datagrid('getSelected');//获取表格中用户选中 所有数据
			if(!moduleTableRow){// 操作前至少选中一条
				$.messager.alert("提示","请选择一个终端模块!",'warning');
				return ;
			}
			testModuleNo = moduleTableRow.channelsNo;
			$.post("${pageContext.request.contextPath}/testModule/getTestModuleContent.action",{testModuleNo:moduleTableRow.channelsNo},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#testModuleInfoForm").form('load',result);
						$("#channelsNo").combobox('readonly'),
						$("#testModuleInfoDlg").dialog('setTitle','编辑终端模块').dialog('open');
					}
				}
			,"json");
		}
		
		/* 保存终端模块信息 */
		function saveOrUpdate(){
			$("#testModuleInfoForm").form('submit',{
				onSubmit : function(param) {
					return $(this).form('validate');
				},
				success : function(result) {
					var resultData =$.parseJSON(result);
					if (resultData.errorMsg) {
						$.messager.alert("系统提示", resultData.errorMsg,"error");
						return;
					} else {
						$("#testModuleInfoDlg").dialog('close');
						if(testModuleNo||0==testModuleNo){
							//update
							$("#moduleTable").datagrid('updateRow',{
								index:$("#moduleTable").datagrid('getRowIndex',testModuleNo),
								row:
								{
									moduleType:$("#moduleType").combobox('getValue'),
									channelsNo:$("#channelsNo").combobox('getValue'),
									cmoschipType:$("#cmoschipType").combobox('getValue'),
									simCard:$("#simCard").textbox('getValue'),
									operator:$("#operator").combobox('getValue'),
									msgCenterNo:$("#msgCenterNo").textbox('getValue'),
									msgAlarmNo:$("#msgAlarmNo").textbox('getValue'),
									testTerminalType:$("#testTerminalType").combobox('getValue')
								}
							});
						}else{
							//append
							$("#moduleTable").datagrid('appendRow',
								{
									moduleType:$("#moduleType").combobox('getValue'),
									channelsNo:$("#channelsNo").combobox('getValue'),
									cmoschipType:$("#cmoschipType").combobox('getValue'),
									simCard:$("#simCard").textbox('getValue'),
									operator:$("#operator").combobox('getValue'),
									msgCenterNo:$("#msgCenterNo").textbox('getValue'),
									msgAlarmNo:$("#msgAlarmNo").textbox('getValue'),
									testTerminalType:$("#testTerminalType").combobox('getValue')
								}
							);
						}
						$("#testModuleInfoForm").form('reset');
					}
				}
			});
		}
		
		/* 删除终端模块 */
		function delTestModule(){
			var moduleTableRow = $("#moduleTable").datagrid('getSelected');//获取表格中用户选中 所有数据
			if(!moduleTableRow){// 操作前至少选中一条
				$.messager.alert("提示","请选择一个终端模块!",'warning');
				return ;
			}
			$.messager.confirm("系统提示", "您确定要删除选中的终端模块吗?", function(r) {
				if (r) {
					$.post("${pageContext.request.contextPath}/testModule/deleteTestModule.action",{channelsNos:moduleTableRow.channelsNo},
						function(result){
							if (result.errorMsg) {
								$.messager.alert("系统提示", result.errorMsg,'error');
							} else {
								$("#moduleTable").datagrid('deleteRow',$("#moduleTable").datagrid('getRowIndex',moduleTableRow.channelsNo));
							}
						}
					,"json");	
				}
			});
		}
		
		
		/* 修改终端 */
		function editTerminal(){
			$("#editTerminalForm").form('submit',{
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var resultData =$.parseJSON(result);
					if (resultData.errorMsg) {
						$.messager.alert("系统提示", resultData.errorMsg,"error");
						return;
					} else {
						$.messager.alert("系统提示", "修改成功");
						parent.centerLayoutGoToPage('【${name}】终端列表','${pageContext.request.contextPath}/terminal/terminalListUI.action?cityId=${id}');
					}
				}
			
			});
		}
		/* 返回 */
		function turnBack(){
			parent.centerLayoutGoToPage('【${name}】终端列表','${pageContext.request.contextPath}/terminal/terminalListUI.action?cityId=${id}');
		}
		/* 选中终端类型,boxid首位字符建议 */
		function testTargetSelect(selectOption){
			/* var boxidValue = $("#te_boxid").textbox('getValue');
			var subBoxidValue = boxidValue.substr(1, boxidValue.length-1);
			 switch(selectOption.value){
				case '0':
					boxidValue = '0'+subBoxidValue;
					break;
				case '1':
					boxidValue = '9'+subBoxidValue;
					break;
				case '2':
					boxidValue = '4'+subBoxidValue;
					break;
				default:
			} 
			$("#te_boxid").textbox('setValue',boxidValue);  */
		}
		
		$(function(){
			$("#editTerminalForm").form('load',{
				"name":'${terminal.name}',
				"testTarget":'${terminal.testTarget}',
				"boxId":'${terminal.boxId}',
				"softwareVersion":'${terminal.softwareVersion}',
				"hardwareVersion":'${terminal.hardwareVersion}',
				"testPlanVersion":'${terminal.testPlanVersion}',
				"factoryDate":'${terminal.factoryDate}',
				"produceDate":'${terminal.produceDate}',
				"remark":'${terminal.remark}',
				"manufacturer":'${terminal.manufacturer}',
				"enable":'${terminal.enable}',
				"id":'${terminal.id}'
			});
		});
		$(function(){
			/* boxid输入框更换,增加首字符建议 */
		    $("input",$("#te_boxid").next("span")).keyup(function(){
		    	//var testTargetSelect = $("#testTarget").combobox('getValue');
		        //var boxidValue = $("#te_boxid").textbox('getValue');
		        var boxidValue = $(this).val();
				/* var subBoxidValue = boxidValue.substr(1, boxidValue.length-1);
				 switch(testTargetSelect){
					case '0':
						boxidValue = '0'+subBoxidValue;
						break;
					case '1':
						boxidValue = '9'+subBoxidValue;
						break;
					case '2':
						boxidValue = '4'+subBoxidValue;
						break;
					default:
				}  */
				$("#te_boxid").textbox('setValue',boxidValue); 
		    });
		})
	</script>
  </head>
  
  <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<form method="post" style="margin-bottom: 5px;" action="${pageContext.request.contextPath}/terminal/updateTeInfo.action" id="editTerminalForm" >
	    	<input type="hidden" name="id" />
	    	<div class="inputDivShow">终端名称
	    		<input name="name"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
	    	</div>
	    	<div class="inputDivShow">终端类型
	    		<select id="testTarget" name="testTarget"  class="easyui-combobox" data-options="required:true,editable:false,onSelect:testTargetSelect" >
	    			<option value="0">自动LTE</option>
	    			<option value="1">LTE单模块商务终端</option>
	    			<option value="2">LTE-FI</option>
	    			<option value="3">5G单模块商务终端</option>
	    			<option value="4">pc测试软件</option>
	    		</select>
	    	</div>
	    	<div class="inputDivShow">ID
	    		<input id="te_boxid" name="boxId" value="0"  class="easyui-textbox" data-options="required:true" />
	    	</div>
	    	<div class="inputDivShow">软件版本
	    		<input name="softwareVersion"  class="easyui-textbox" data-options="validType:'length[1,24]'" />
	    	</div>
	    	<div class="inputDivShow">硬件版本
	    		<input name="hardwareVersion"  class="easyui-textbox" data-options="validType:'length[1,24]'" />
	    	</div>
	    	<div class="inputDivShow">测试计划版本
	    		<input id="testPlanVersion" name="testPlanVersion"  class="easyui-textbox" data-options="validType:'length[1,24]',readonly:true" />
	    	</div>
	    	<div class="inputDivShow">生产日期
	    		<input name="factoryDate"  class="easyui-datetimebox" data-options="editable:false" />
	    	</div>
	    	<div class="inputDivShow">出厂日期
	    		<input name="produceDate"  class="easyui-datetimebox" data-options="editable:false" />
	    	</div>
	    	<div class="inputDivShow">备注
	    		<input name="remark"  class="easyui-textbox" data-options="validType:'length[1,24]'" />
	    	</div>
	    	<div class="inputDivShow">厂商
	    		<select name="manufacturer"  class="easyui-combobox" data-options="required:true,editable:false" >
	    			<option value="大唐">大唐</option>
					<option value="鼎利">鼎利</option>
					<option value="华星">华星</option>
					<option value="诺优">诺优</option>
					<option value="普天">普天</option>
					<option value="未知">未知</option>
	    		</select>
	    	</div>
	    	<div class="inputDivShow">是否可用
	    		<select name="enable"  class="easyui-combobox" data-options="required:true,editable:false" >
	    			<option selected="selected" value="true">可用</option>
					<option value="false">禁用</option>
	    		</select>
	    	</div>
	    	
	    	<table width="100%" >
		    	<tr >
			    	<td width="50%" style="padding-right:10px;" align="right"><a class="easyui-linkbutton" style="width:80px" onclick="editTerminal();" data-options="iconCls:'icon-save'">保存</a></td>
			    	<td width="50%" style="padding-left:10px;" align="left"><a class="easyui-linkbutton" style="width:80px" onclick="turnBack();" data-options="iconCls:'icon-undo'">返回</a></td>
		    	</tr>
	    	</table>
    	</form>
    	<table id="moduleTable" style="margin-top: 0px;"></table>
    	
    	<!-- 添加终端模块-->
   		<div id="testModuleInfoDlg" class="easyui-dialog" style="width:600px;height:370px;padding:5px 0px;" data-options="onMove:onDrag,closed:true,modal:true,buttons:'#testModuleInfoDlg-buttons'">
    		<form id="testModuleInfoForm" method="post" action="${pageContext.request.contextPath}/testModule/saveOrUpdate.action">
				<div class="inputDivShow">通道号
					<select id="channelsNo" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="channelsNo">
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
				<div class="inputDivShow">sim卡号
					<input id="simCard" class="easyui-textbox" name="simCard" data-options="min:0">
				</div>
				<div class="inputDivShow">模块类型
					<select id="moduleType" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="moduleType">
						<option value="GSM">GSM</option>
						<option value="CDMA">CDMA</option>
						<option value="TDS">TDS</option>
						<option value="WCDMA">WCDMA</option>
						<option value="EVDO">EVDO</option>
						<option value="TD_LTE">TD_LTE</option>
						<option value="FDD_LTE">FDD_LTE</option>
						<option value="NB_IoT">NB_IoT</option>
						<option value="eMTC">eMTC</option>
						<option selected value="FG">5G</option>
						<option selected value="APP_FG">APP单站验证模块</option>
					</select>
				</div>
				<div class="inputDivShow">芯片类型
					<select id="cmoschipType" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="cmoschipType">
						<option selected value="高通">高通</option>
					</select>
				</div>
				<div class="inputDivShow">phase
					<input class="easyui-textbox" name="phase">
				</div>
				<div class="inputDivShow">Class
					<input class="easyui-textbox" name="clazz">
				</div>
				<div class="inputDivShow">运营商
					<select id="operator" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="operator">
						<option selected value="中国移动">中国移动</option>
						<option value="中国联通">中国联通</option>
						<option value="中国电信">中国电信</option>
					</select>
				</div>
				<div class="inputDivShow">短消息中心号
					<input id="msgCenterNo" class="easyui-textbox"  name="msgCenterNo">
				</div>
				<div class="inputDivShow">短消息告警号
					<input id="msgAlarmNo" class="easyui-textbox" name="msgAlarmNo">
				</div>
				<div class="inputDivShow">波特率
					<input class="easyui-textbox" name="baudRate">
				</div>
				<div class="inputDivShow">拨号超时时间
					<input class="easyui-numberbox" name="timeout">
				</div>
				<div class="inputDivShow">PPP拨号失败门限
					<input class="easyui-numberbox" name="pptDialFailBarrier">
				</div>
				<div class="inputDivShow">登录失败门限
					<input class="easyui-numberbox" name="loginFailBarrier">
				</div>
				<div class="inputDivShow">连接失败门限
					<input class="easyui-numberbox" name="connectFailBarrier">
				</div>
				<div class="inputDivShow">是否独立
					<select class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="independency">
						<option  selected value="true">是</option>
						<option value="false">否</option>
					</select>
				</div>
				<div class="inputDivShow">是否可用
					<select class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="enable">
						<option selected value="true">是</option>
						<option value="false">否</option>
					</select>
				</div>
				<div class="inputDivShow">测试设备类型
					<select id="testTerminalType" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="testTerminalType">
						<option selected value="其他">其他</option>
						<option value="CA">CA</option>
						<option value="VoLTE">VoLTE</option>
						<option value="CSFB">CSFB</option>
					</select>
				</div>
			</form>
   		</div>
		<div id="testModuleInfoDlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-ok" onclick="saveOrUpdate();">确定</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#testModuleInfoDlg').dialog('close')">取消</a>
		</div>
  </body>
</html>
