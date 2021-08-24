<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<head>
	<title>联通日志列表页面</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<style type="text/css">
		.inputDivShow{
			display: inline-block; *
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
        // field用于匹配远程json属性，width宽度,align居左右中对齐
        /* var testLogInfoColumns=[[
            {field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th>
            {field:'fileName',width:150,align:'center',title:'文件名'},
            {field:'logVersion',width:75,align:'center',title:'测试计划版本号'},
            {field:'testLevel',width:75,align:'center',title:'测试级别',
                formatter: function(value,row,index){
                    if('organizationCheck' == value){
                        return '组织巡检';
                    }else if('dailyOptimiz' == value){
                        return '日常优化';
                    }else if('deviceDebug' == value){
                        return '设备调试(单站验证)';
                    }else{
                        return value;
                    }
                }
            },
            {field:'testTarget',width:75,align:'center',title:'测试目标'},
            {field:'startDate',width:90,align:'center',title:'开始时间'},
            {field:'endDate',width:90,align:'center',title:'结束时间'},
            {field:'terminalGroup',width:45,align:'center',title:'区域'},
            {field:'logSource',width:60,align:'center',title:'数据来源',
                formatter: function(value1,row1,index1){
                    if(0==value1||2==value1){
                        return '室外';
                    }else if(1==value1||3==value1){
                        return '室内';
                    }
                    return value1;
                }
            },
            {field:'operatorName',width:45,align:'center',title:'运营商'},
            {field:'testFileStatus',width:75,align:'center',title:'预统计状态',
                formatter: function(value,row,index){
                    if(0==value||1==value){
                        return '统计中';
                    }else if(2==value){
                        return '完成';
                    }else{
                        return '统计失败';
                    }
                    return value;
                },
                styler: function(value,row,index){
                    if(2!=value){
                        return 'background-color:#34aaf1;';
                    }
                }
            },
            {field:'uploadedSize',width:75,align:'center',title:'文件大小(KB)',
                formatter: function(value2,row2,index2){
                    if (value2){
                        return (value2/1024).toFixed(2);
                    }
                    return value2;
                }
            }
        ]]; */
        var unicomLogInfoColumns=[[
            //{field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th>
            /* {field:'prov',width:75,align:'center',title:'省份'},
            {field:'city',width:75,align:'center',title:'城市'},
            {field:'fileName',width:150,align:'center',title:'文件名'},
            {field:'testType',width:75,align:'center',title:'测试类型'},
            {field:'operatorName',width:75,align:'center',title:'运营商'},		 */
            {field:'testName',width:80,align:'center',title:'测试名称'},
            {field:'startDate',width:160,align:'center',title:'开始时间'},
            {field:'endDate',width:160,align:'center',title:'结束时间'},
            {field:'timeDiff',width:90,align:'center',title:'测试时长'},
            {field:'uploadedSize',width:80,align:'center',title:'文件大小(KB)',
                formatter: function(value2,row2,index2){
                    if (value2){
                        return (value2/1024).toFixed(2);
                    }
                    return value2;
                }
            },
            {field:'testFileStatus',width:80,align:'center',title:'状态',
                formatter: function(value,row,index){
                    if(0==value||1==value){
                        return '解析中';
                    }else if(2==value){
                        return '解析完成';
                    }else{
                        return '解析失败';
                    }
                    return value;
                },
                styler: function(value,row,index){
                    if(2!=value){
                        return 'background-color:#34aaf1;';
                    }
                }
            },
            {field:'md5Check',width:85,align:'center',title:'MD5校验'},
            {field:'trafficCheck',width:85,align:'center',title:'业务事件校验'},
            {field:'kpi1',width:85,align:'center',title:'5G时长驻留比'},
            {field:'kpi2',width:120,align:'center',title:'覆盖率（-105&-3）'},
            {field:'kpi3',width:85,align:'center',title:'FTP下行速率'},
            {field:'kpi4',width:85,align:'center',title:'FTP上行速率'},
            {field:'kpi5',width:85,align:'center',title:'百度云下载'},
            {field:'kpi6',width:120,align:'center',title:'VOLTE主叫EPS FB接通率'},
            {field:'kpi7',width:120,align:'center',title:'VOLTE主叫掉线率'},
            {field:'kpi8',width:85,align:'center',title:'FR成功率'}
        ]]

        $(function(){

            //初始化开始时间和结束时间
            /* var nowDate=new Date();
            var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
            $("#endDate").datetimebox('setValue',dateString);
            nowDate.setDate(nowDate.getDate()-31);
            dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
            $("#beginDate").datetimebox('setValue',dateString); */

            initTable();
        });
        function initTable(){
            $("#testLogInfoTable").datagrid({
                // 表头
                frozenColumns:[[
                    {field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th>
                    {field:'prov',width:75,align:'center',title:'省份'},
                    {field:'city',width:75,align:'center',title:'城市'},
                    {field:'fileName',width:200,align:'center',title:'文件名'},
                    {field:'testType',width:75,align:'center',title:'测试类型'},
                    {field:'operatorName',width:75,align:'center',title:'运营商'}	]],
                columns:unicomLogInfoColumns,
                url:'${pageContext.request.contextPath}/unicomLogItem/doPageListJson.action?isFinished=true',
                title:'日志列表',
                //fitColumns:true,
                //填满区域
                fit:true,
                //奇偶变色
                striped:true,
                //toolbar:'#tb',
                // 行号
                rownumbers:true,
                pagination:true,
                pageSize:20,
                pageList:[10,20,50,100,200,500,1000,10000,100000],
                scrollbarSize:0,
                onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
                    if (data.rows.length > 0) {
                        var checkAllDisable = false;
                        //循环判断操作为新增的不能选择
                        for (var i = 0; i < data.rows.length; i++) {
                            //根据operate让某些行不可选
                            if (2 != data.rows[i].testFileStatus) {
                                checkAllDisable = true;
                                $("input[type='checkbox']")[i + 1].disabled = true;
                            }
                        }
                        if(checkAllDisable){
                            $(".datagrid-header-check > input[type='checkbox']").attr("disabled","disabled");
                        }else{
                            $(".datagrid-header-check > input[type='checkbox']").removeAttr("disabled");
                        }
                    }
                },
                onSelectAll:function(){
                    var nodes = $('#testLogInfoTable').datagrid('getSelections');
                    if(nodes.length > 0){
                        $('#showMapId').linkbutton('enable');
                    }else{
                        $('#showMapId').linkbutton('disable');
                    }
                },
                onUnselectAll:function(){
                    var nodes = $('#testLogInfoTable').datagrid('getSelections');
                    if(nodes.length > 0){
                        $('#showMapId').linkbutton('enable');
                    }else{
                        $('#showMapId').linkbutton('disable');
                    }
                },
                onCheck: function(rowIndex, rowData){
                    var nodes = $('#testLogInfoTable').datagrid('getSelections');
                    if(nodes.length > 0){
                        $('#showMapId').linkbutton('enable');
                    }else{
                        $('#showMapId').linkbutton('disable');
                    }
                },
                onUncheck: function(rowIndex, rowData){
                    var nodes = $('#testLogInfoTable').datagrid('getSelections');
                    if(nodes.length > 0){
                        $('#showMapId').linkbutton('enable');
                    }else{
                        $('#showMapId').linkbutton('disable');
                    }
                },
                onClickRow: function(rowIndex, rowData){
                    var nodes = $('#testLogInfoTable').datagrid('getSelections');
                    if(nodes.length > 0){
                        $('#showMapId').linkbutton('enable');
                    }else{
                        $('#showMapId').linkbutton('disable');
                    }
                    //加载完毕后获取所有的checkbox遍历
                    $("input[type='checkbox']").each(function(index, el){
                        //如果当前的复选框不可选，则不让其选中
                        if (el.disabled == true) {
                            $("#testLogInfoTable").datagrid('unselectRow', index - 1);
                        }
                    });
                }
            });

        }
        /* 多条件查询 */
        function query(){
            if($("#queryForm").form('validate')){
                //获取cityIds请求参数
                var areaTree = $("#areaTree").combotree('tree');
                var checkNodes = areaTree.tree('getChecked');
                var cityIds = [];
                for (var int = 0; int < checkNodes.length; int++) {
                    if(checkNodes[int].attributes.type=='City'){
                        cityIds.push(checkNodes[int].attributes.refId);
                    }
                }
                var cityIdsString = cityIds.join(",");

                //获取boxIds请求参数
                /* var boxIds=[];
                if(cityIdsString){
                    var values = $("#boxIds").combobox('getValues');
                    for (var int1 = 0; int1 < values.length; int1++) {
                        boxIds.push(values[int1]);
                    }
                }
                var boxIdsString = boxIds.join(","); */
                //获取数据来源请求参数
                /* var logSource=[];
                var logSourcevalues = $("#logSource").combobox('getValues');
                for (var int2 = 0; int2 < logSourcevalues.length; int2++) {
                    logSource.push(logSourcevalues[int2]);
                }
                var logSourceString = logSource.join(","); */
                $("#testLogInfoTable").datagrid('load',{
                    beginDate:$("#beginDate").datetimebox('getValue'),
                    endDate:$("#endDate").datetimebox('getValue'),
                    cityIds:cityIdsString,
                    md5Check:$("#md5Check").combobox('getValue'),
                    trafficCheck:$("#bizEventCheck").combobox('getValue'),
                    operators:$("#operators").combobox('getValue'),
                    testName:$("#testName").textbox('getValue'),
                    //prestorecityIds:$("#prestorecityIds").combotree('getValue'),
                    //logSource:logSourceString,
                    //boxIds:boxIdsString,
                    prov:$("#prov").combobox('getValue'),
                    city:$("#city").combobox('getValue'),
                    fileName:$("#fileName").textbox('getValue'),
                    testFileStatus:$("#testFileStatus").combobox('getValue')
                });
            }
        }
        /* 重置表单 */
        function resetForm(){
            $("#queryForm").form('reset');
            /* var nowDate=new Date();
            var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
            $("#endDate").datetimebox('setValue',dateString);
            nowDate.setDate(nowDate.getDate()-7);
            dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
            $("#beginDate").datetimebox('setValue',dateString); */
        }

        function deleteLog(){
            var testLogItemsRows = $("#testLogInfoTable").datagrid('getSelections');//获取表格中用户选中 所有数据
            if(testLogItemsRows.length==0){// 操作前至少选中一条
                $.messager.alert("提示","请选择某些日志!",'warning');
                return ;
            }
            $.messager.confirm("系统提示", "您确定要删除选中的内容吗?", function(r) {
                if(r){
                    var idArray = new Array();
                    for(var i=0; i<testLogItemsRows.length;i++){
                        idArray.push(testLogItemsRows[i].recSeqNo);
                    }
                    // 将选中多条记录id 用","拼接为一个字符串
                    var ids = idArray.join(",");
                    // 以ajax方式，发送到服务器，完成操作
                    $.post("${pageContext.request.contextPath}/unicomLogItem/deleteTestLogItem",{"testLogItemIds":ids},
                        function(result){
                            if (result.errorMsg) {
                                $.messager.alert("系统提示", result.errorMsg,'error');
                            } else {
                                $.messager.alert("系统提示", "删除成功",'success');
                            }
                            $("#testLogInfoTable").datagrid("reload");
                        }
                        ,"json");
                }
            });

        }

        /* 初始化BOXIDS */
        function initBoxId(newValue,oldValue){
            var areaTree = $("#areaTree").combotree('tree');
            var checkNodes = areaTree.tree('getChecked');
            var cityIds = [];
            for (var int = 0; int < checkNodes.length; int++) {
                if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
                    cityIds.push(checkNodes[int].attributes.refId);
                }
            }
            var cityIdsString = cityIds.join(",");
            /* $("#boxIds").combobox({
                url:'${pageContext.request.contextPath}/terminalGroup/terminalTree.action?cityIds='+cityIdsString,
				method: 'post',
				panelWidth:200,
				editable:false,
				multiple:true,
				valueField:'value',
				textField:'text',
				groupField:'group'
			}); */
        }



        /* 开始分析,分析按钮 */
        function doAnalysis(){
            var testLogItemsRows = $("#testLogInfoTable").datagrid('getSelections');//获取表格中用户选中 所有数据
            if(testLogItemsRows.length==0){// 操作前至少选中一条
                $.messager.alert("提示","请选择某些日志!",'warning');
                return ;
            }
            var idArray = new Array();
            //var logSize = 0;
            for(var i=0; i<testLogItemsRows.length;i++){
                idArray.push(testLogItemsRows[i].recSeqNo);
                //logSize += testLogItemsRows[i].uploadedSize;
            }
            //去除筛选日志大小的限制
            //if(logSize>1024*1024*2){
            //	$.messager.alert("提示","所选日志大小超出[2G]限制!",'warning');
            //	return ;
            //}
            // 将选中多条记录id 用","拼接为一个字符串
            var ids = idArray.join(",");
            // 以ajax方式，发送到服务器，完成操作
            $.post("${pageContext.request.contextPath}/testLogItem/saveAnalysisTestLogItem",{"testLogItemIds":ids},
                function(result){
                    if (result.errorMsg) {
                        $.messager.alert("系统提示", result.errorMsg,'error');
                    } else {
                        //关闭VoLTE专题分析的所有TAB
                        parent.closeTab('呼叫建立时延异常');
                        parent.closeTab('连续无线差');
                        parent.closeTab('VoLTE总体概览');
                        parent.closeTab('VoLTE语音质差');
                        parent.closeTab('VoLTE语音未接通');
                        parent.closeTab('VoLTE语音掉话');
                        parent.closeTab('IMS注册失败');
                        parent.closeTab('CSFB失败');
                        parent.closeTab('系统内切换失败');
                        parent.closeTab('SRVCC切换失败');
                        parent.closeTab('VoLTE对比分析');
                        parent.closeTab('语音RTP连续丢包');
                        parent.closeTab('VOLTE视频质差');
                        parent.closeTab('VoLTE视频未接通');
                        parent.closeTab('VoLTE视频掉话');
                        parent.closeTab('流媒体总体概览');
                        parent.closeTab('流媒体视频质差');
                        parent.closeTab('5G对比分析');
                        parent.closeTab('eMBB覆盖分析');
                        parent.selectTab('测试日志');
                        //$.messager.alert("系统提示",'分析成功,请前往专题查看分析结果!','info');
                        $.messager.show({
                            title:'系统提示',
                            msg:'分析成功,请前往专题查看分析结果!'
                        });
                    }
                }
                ,"json");
        }

        /*下载操作*/
        function downloadLog(){
            var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');
            if(checked.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择一条记录!",'warning');
                return ;
            }
            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }
            window.location= "${pageContext.request.contextPath}/unicomLogItem/downloadLog.action?testLogItemIds="+ids;
        }

        function cheakDetail(){
            var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');

            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }

            var params = {
                'testLogItemIds': ids,
                'beginDate': $("#beginDate").datetimebox('getValue'),
                'endDate': $("#endDate").datetimebox('getValue')
            }

            MaskUtil.mask();

            var url = "${pageContext.request.contextPath}/unicomLogItem/downloadFullCheck.action";

            $.ajax({
                type: "post",
                url: url,
                data: params,
                success: function(response,status,request){
                    var disp = request.getResponseHeader('Content-Disposition');
                    if(disp && disp.search('attachment') != -1){
                        var form = $('<form method="POST" action="' + url + '">');
                        $.each(params, function(k, v){
                            form.append($('<input type="hidden" name="' + k + '" value="' + v +'">'));
                        })
                        $('body').append(form);
                        form.submit();
                    }

                    MaskUtil.unmask();
                }
            });



            /* var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');
            if(checked.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择一条记录!",'warning');
                return ;
            }
            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }
            window.location= "${pageContext.request.contextPath}/unicomLogItem/downloadFullCheck.action?testLogItemIds="+ids; */



        }

        //数据概览
        function seeInfo(){
            var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');
            if(checked.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择一条记录!",'warning');
                return ;
            }
            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }
            goToPage("${pageContext.request.contextPath}/unicomLogItem/seeInfo.action?testLogItemIds="+ids+"&dPage=0");
        }

        //
        function questionIndex(){

        }

        //轨迹详情
        function showMap(){
            var ids = '';
            var nodes = $('#testLogInfoTable').datagrid('getSelections');
            for(var i = 0; i < nodes.length;i++){
                ids = ids + nodes[i].recSeqNo;
                if(i != nodes.length-1){
                    ids = ids + ',';
                }
            }

            //goToPage("${pageContext.request.contextPath}/mapTrail.jsp?ids="+ids);

            window.location= "${pageContext.request.contextPath}/mapTrail.jsp?ids="+ids;


            /* var ids = '';
            var nodes = $('#testLogInfoTable').datagrid('getSelections');
            for(var i = 0; i < nodes.length;i++){
                ids = ids + nodes[i].recSeqNo;
                if(i != nodes.length-1){
                    ids = ids + ',';
                }
            }

            $('#winMap').window('open');



            var src = $("#mapIframe").attr("src");
            if(!src){
                $("#mapIframe").attr("src", "${pageContext.request.contextPath}/map/GpsTrace.html");
			}

			$('#mapWindowId').window('open');

			$.ajax({
   	             type: "GET",
   	             url: "${pageContext.request.contextPath}/unicomLogItem/mapTrail",
   	             data: {'testLogItemIds': ids},
   	             dataType: "json",
   	             success: function(data){
   					document.getElementById('mapIframe').contentWindow.postMessage(data,'*');
                  }
     	    }); */

        }

        function seeQuestion(){
            var questionIdx = $("#questionIdx").combotree('tree');
            var checkNodes = questionIdx.tree('getChecked');
            if(checkNodes.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择问题索引!",'warning');
                return ;
            }
            var questionIdxs = [];
            for (var int = 0; int < checkNodes.length; int++) {
                if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
                    questionIdxs.push(checkNodes[int].attributes.refId);
                }
            }
            var questionIdxsString = questionIdxs.join(",");


            var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');
            if(checked.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择一条记录!",'warning');
                return ;
            }
            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }


            goToPage("${pageContext.request.contextPath}/unicomLogItem/seeQuestionview.action?testLogItemIds="+ids+"&questionIndex="+questionIdxsString);
        }


        function downloadMiddleOriginal(){
            var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');
            if(checked.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择一条记录!",'warning');
                return ;
            }
            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }

            var params = {
                'testLogItemIds': ids
            }

            MaskUtil.mask();

            var url = "${pageContext.request.contextPath}/unicomLogItem/downloadMiddleOriginal.action";

            $.ajax({
                type: "post",
                url: url,
                data: params,
                timeout: 300000,//5min
                success: function(response,status,request){
                    var disp = request.getResponseHeader('Content-Disposition');
                    if(disp && disp.search('attachment') != -1){
                        var form = $('<form method="POST" action="' + url + '">');
                        $.each(params, function(k, v){
                            form.append($('<input type="hidden" name="' + k + '" value="' + v +'">'));
                        })
                        $('body').append(form);
                        form.submit();
                    }

                    MaskUtil.unmask();
                }
            });

            //window.location="${pageContext.request.contextPath}/unicomLogItem/downloadMiddleOriginal.action?testLogItemIds="+ids
        }

        function downloadMiddleGrid(){
            var ids = '';
            var checked = $("#testLogInfoTable").datagrid('getSelections');
            if(checked.length < 1){// 操作前至少选中一条
                $.messager.alert("提示","请选择一条记录!",'warning');
                return ;
            }
            for(var i = 0; i < checked.length;i++){
                ids = ids + checked[i].recSeqNo;
                if(i != checked.length-1){
                    ids = ids + ',';
                }
            }

            var params = {
                'testLogItemIds': ids
            }

            MaskUtil.mask();

            var url = "${pageContext.request.contextPath}/unicomLogItem/downloadMiddleGrid.action";

            $.ajax({
                type: "post",
                url: url,
                data: params,
                timeout: 300000,//5min
                success: function(response,status,request){
                    var disp = request.getResponseHeader('Content-Disposition');
                    if(disp && disp.search('attachment') != -1){
                        var form = $('<form method="POST" action="' + url + '">');
                        $.each(params, function(k, v){
                            form.append($('<input type="hidden" name="' + k + '" value="' + v +'">'));
                        })
                        $('body').append(form);
                        form.submit();
                    }

                    MaskUtil.unmask();
                }
            });


            //window.location="${pageContext.request.contextPath}/unicomLogItem/downloadMiddleGrid.action?testLogItemIds="+ids
        }


        $(function(){
            var clear_btn = $.extend([], $.fn.datetimebox.defaults.buttons);
            clear_btn.splice(1,0,{
                text: '清空',
                handler: function(target){
                    $(target).datetimebox('setValue','');
                }
            });
            $('.easyui-datetimebox').datetimebox({
                buttons: clear_btn
            })



            //http://127.0.0.1:8080/CloudMonitor5G/unicomLogItem/provInput
            //http://127.0.0.1:8080/CloudMonitor5G/unicomLogItem/cityInput?prov=%E6%B2%B3%E5%8C%97
            $('#prov').combobox({
                url:'${pageContext.request.contextPath}/unicomLogItem/provInput',
                cache:false,
                paneHeight:'auto',
                valueField:'value',
                textField:'label',
                onSelect: function(rec){
                    //var url = '';
                    //$('#city').combobox('reload', url);
                    $('#city').combobox({
                        url:'${pageContext.request.contextPath}/unicomLogItem/cityInput?prov='+rec.value,
                        cache:false,
                        paneHeight:'auto',
                        valueField:'value',
                        textField:'label'
                    })
                }
            })


        })






	</script>
</head>

<body  style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">

<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">

	<div  data-options="region:'center',border:false" >
		<table id="testLogInfoTable"> </table>
	</div>

	<div data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
	    		//收束的时候显示title
	    		var title = $('#ly').layout('panel','north').panel('options').title;
	    		$('.layout-expand-north .panel-title').html(title);
	    	}" style="height:160px;overflow-y:auto;">
		<!-- <div style="width:100%;height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;"><div class="panel-title">工程参数导入</div></div> -->
		<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
			<div class="inputDivShow">开始时间
				<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:false,editable:true" />
			</div>
			<div class="inputDivShow">结束时间
				<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="required:false,editable:true" />
			</div>
			<div class="inputDivShow">省
				<select id="prov" name="prov"  class="easyui-combobox" data-options="editable:true,multiple:false" >
				</select>
			</div>
			<div class="inputDivShow">市
				<select id="city" name="city"  class="easyui-combobox" data-options="editable:true,multiple:false" >
					<option selected="selected" value="">全部</option>
				</select>
				<!-- <select id="city" name="city"  class="easyui-combobox" data-options="editable:false,multiple:false,valueField:'',textField:''" >
                </select> -->
			</div>
			<div class="inputDivShow">区域
				<select id="areaTree" name="cityIds"  class="easyui-combotree" data-options="panelWidth:200,onChange:initBoxId,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:false,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action'"  >
				</select>
			</div>
			<!-- 			    	<div class="inputDivShow">预存区域
                                    <select id="prestorecityIds" name="prestorecityIds" class="easyui-combotree" style="margin:none;" data-options="panelWidth:200,readonly:true,disabled:true,editable:false,multiple:true" >
                                    </select>
                                </div> -->
			<!-- <div class="inputDivShow">数据来源
                <select id="logSource" name="logSource"  class="easyui-combobox" data-options="editable:false,multiple:true" >
                    <option selected="selected" value="0">室外自动路测</option>
                    <option value="1">室内自动路测</option>
                    <option value="2">Miou室外路测</option>
                    <option value="3">Miou室内测试</option>
                    <option value="4">pc测试软件</option>
                    <option value="5">5g单模块商务终端</option>
                </select>
            </div>
            <div class="inputDivShow">测试终端
                <select id="boxIds" name="boxIds"  class="easyui-combobox"  >
                </select>
            </div>
            -->
			<div class="inputDivShow">文件名
				<input id="fileName" name="fileName"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
			</div>
			<div class="inputDivShow">状态
				<select id="testFileStatus" name="testFileStatus"  class="easyui-combobox" data-options="editable:false,multiple:false" >
					<option selected="selected" value="">全部</option>
					<option value="1">解析中</option>
					<option value="2">解析完成</option>
					<option value="3">解析失败</option>
				</select>
			</div>
			<div class="inputDivShow">MD5校验
				<select id="md5Check" name="md5Check"  class="easyui-combobox" data-options="editable:false,multiple:false" >
					<option selected="selected" value="">全部</option>
					<option value="是">是</option>
					<option value="否">否</option>
				</select>
			</div>
			<div class="inputDivShow">业务事件校验
				<select id="bizEventCheck" name="bizEventCheck"  class="easyui-combobox" data-options="editable:false,multiple:false" >
					<option selected="selected" value="">全部</option>
					<option value="是">是</option>
					<option value="否">否</option>
				</select>
			</div>
			<div class="inputDivShow">运营商
				<select id="operators" name="operators" class="easyui-combobox" data-options="editable:false,multiple:false" >
					<option selected="selected" value="">全部</option>
					<option value="联通">联通</option>
					<option value="移动">移动</option>
					<option value="电信">电信</option>
				</select>
			</div>
			<div class="inputDivShow">测试名称
				<input id="testName" name="testName"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
			</div>
			<div class="inputDivShow">问题索引
				<select id="questionIdx" name="questionIdx"  class="easyui-combotree" data-options="panelWidth:200,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:false,url:'${pageContext.request.contextPath}/unicomLogItem/questionTree.action'">
				</select>
			</div>
			<table width="100%">
				<tr>
					<td width="100%" align="center">
						<a class="easyui-linkbutton" onclick="query();" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-search'" >查找</a>
						<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-reload'" >重置</a>

						<shiroextend:hasAnyPermissions name="testlogitemUnicom:download">
							<a class="easyui-linkbutton" style="width:80px;margin-right: 5px;"  onclick="downloadLog();" data-options="iconCls:'icon-download'">下载</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:delete">
							<a class="easyui-linkbutton" onclick="deleteLog();" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-reload'" >删除</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:cheakDetail">
							<a class="easyui-linkbutton" onclick="cheakDetail();" style="width: 80px;margin-right: 5px;">校验详单</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:seeInfo">
							<a class="easyui-linkbutton" onclick="seeInfo();" style="width: 80px;margin-right: 5px;">数据概览</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:showMap">
							<a class="easyui-linkbutton" onclick="showMap();" id="showMapId" style="width: 80px;margin-right: 5px;" data-options="iconCls:'icon-networkStructure',disabled:true">轨迹详情</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:seeQuestion">
							<a class="easyui-linkbutton" onclick="seeQuestion();" style="width: 80px;margin-right: 5px;">问题索引</a>
						</shiroextend:hasAnyPermissions>
						<shiroextend:hasAnyPermissions name="testlogitemUnicom:exportTable">
							<a class="easyui-menubutton" data-options="menu:'#_type'" style="width: 100px;margin-right: 5px;">导出中间表</a>
							<div id="_type">
								<div onclick="downloadMiddleOriginal()">原始文件</div>
								<div onclick="downloadMiddleGrid()">栅格文件</div>
							</div>
						</shiroextend:hasAnyPermissions>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<!-- <div id="tt3">
        <a href="#" style="width:50px;text-decoration:underline;margin-right:20px"  onclick="doAnalysis();" title="测试日志分析">日志分析</a>
    </div> -->
</div>


<!-- <div id="tb">
    <a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-add',plain:true" onclick="add();">新增</a>
    <a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-remove',plain:true" onclick="deleteLog();">删除</a>
</div> -->

<div id="winMap" class="easyui-dialog" buttons="#atu-buttons"title="轨迹详情" style="width:950px;height:550px;overflow: auto;" data-options="onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true">
	<div style="height: 100%;width: 100%;z-index: 90;float:left;">
		<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
	</div>
</div>



</body>
</html>
