<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>手动新增高铁xml页面</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <%@ include file="../../../taglibs/jquery.jsp" %>
    <%@ include file="../../../taglibs/easyui.jsp" %>
    <%@ include file="../../../gis/layerManagerEmbb.jsp" %>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js"></script>

    <style type="text/css">
        .inputDivShow{
            display: inline-block; *
        	display: inline;
            font-size: 12px;
            margin: 5px;
            padding-left: 0;
            padding-right: 0;
            text-align: center;
            width: 200px;
            height: 3%;
        }
        .inputDivShow input{
            width:160px;
        }
        .inputDivShow select{
            width:140px;
        }

        .stationInput{
            width: 80px;
        }

        .tableClass table{
            border-collapse:collapse;
            width:100%;
            overflow:hidden;
        }
        .tableClass thead{
            width:100%;
            height:30px;
            line-height:30px;
        }
        .tableClass th{
            font-size:12px;
            border:solid 1px #e1e1e1;
            background-color:#f4f4f5
        }
        .tableClass td{
            font-size:13px;
            border:solid 1px #e1e1e1;
            text-align:center;
            height:25px;
            line-height:25px;
        }
        .tableClass tbody{
            overflow:hidden;
        }
        
    </style>
    <script type="text/javascript">
        // field用于匹配远程json属性，width宽度,align居左右中对齐

		var processIndex = 1;
        var sid = 0;
        var selectLocationType = null;
        var xmlParam = {};
        var sidArr=[];
        var stationArr=[];
        $(function(){
	       var t1 = window.setInterval(function(){
			   if(mapIframe.window.map){
				   mapIframe.window.hideDivByDivId('EventControldis');
			   }
	       		// if(cellidStr != null){
		       	// 		mapIframe.window.cellids = cellidStr;
		       	// 		cellidStr ==null;
		       	// 		window.clearInterval(t1);
	       		// }
	       },1000);
            addClick();
        });

        function addClick(){
            $("#stationInfo .iconfont").off('click')
            //点击单选
            $("#stationInfo .iconfont").click(function(){
                //遍历所有单选
                let clectedArr = [];
                let stationCheckkedArr = [];
                $('#stationInfo td .iconfont').each(function(){
                    if($(this).is(":checked")){
                        // console.info($(this).parent().parent().children("#tdSid"));
                        let index = $(this).parent().parent().children("#tdSid")[0].innerHTML;
                        let name = "stationName"+index;
                        let stationName = $("#"+name).textbox('getValue');
                        clectedArr.push(index);
                        stationCheckkedArr.push(stationName);
                    }
                });
                sidArr = clectedArr;
                stationArr = stationCheckkedArr;
            })
        }

        function before(){
            if(processIndex==2){
                processIndex--;
                selectLocationType = null;
                $("#trainCodeInfo").css('display','block');
                $("#stationInfo").css('display','none');
                $('#beforeButton').linkbutton('disable');
                $("#trainCode").textbox('enable');
            }if(processIndex==3){
                processIndex--;
                sidArr = [];
                stationArr=[];
                let stationInputs = $(".stationInput");
                for(var i=0;i<stationInputs.length;i++){
                    $(stationInputs[i]).textbox('enable');
                }
                $('#stationInfo td .iconfont').each(function(){
                    $(this).attr('checked',null);
                });
                selectLocationType = 0;
                mapIframe.window.disconnect();
                $('#nextButton').linkbutton('enable');
                $("#saveButton").linkbutton('disable');
                $("#stationButton").css('display','block');
                $("#pointButton").css('display',' none');
            }
        }

        function next(){
            if(processIndex==1){
                let isValid = $('#trainff').form('validate');
                if(isValid){
                    processIndex++;
                    selectLocationType = 0;
                    xmlParam.name = $("#trainCode").textbox('getValue');
                    xmlParam.startStation = $("#startStation").textbox('getValue');
                    xmlParam.destStation = $("#endStation").textbox('getValue');
                    xmlParam.startTime = $("#metroLineStartTime").timespinner('getValue');
                    xmlParam.arriveTime = $("#metroLineEndTime").timespinner('getValue');
                    $("#trainCodeInfo").css('display','none');
                    $("#stationInfo").css('display','block');
                    $("#trainCode").textbox('disable');
                    $('#beforeButton').linkbutton('enable');
                }
            }else if(processIndex==2){
                let isValid = $('#stationff').form('validate');
                if(isValid){
                    processIndex++;

                    sidArr = [];
                    stationArr=[];
                    let stationInputs = $(".stationInput");
                    for(var i=0;i<stationInputs.length;i++){
                        $(stationInputs[i]).textbox('disable');
                    }
                    if(xmlParam.stops==null ||xmlParam.stops==undefined){
                        xmlParam.stops = [];
                    }
                    $('#stationInfo td .iconfont').each(function(){
                        $(this).attr('checked',null);
                        let index = $(this).parent().parent().children("#tdSid")[0].innerHTML;
                        let name = $("#stationName"+index).textbox('getValue');
                        let start = $("#stationStartTime"+index).timespinner('getValue');
                        let arrive = $("#stationEndTime"+index).timespinner('getValue');
                        let lon = $("#Longitude"+index).timespinner('getValue');
                        let lat = $("#Latitude"+index).timespinner('getValue');
                        if(xmlParam.stops[index]==null){
                            let stop = {"name":name,"start":start,"arrive":arrive,"lon":lon,"lat":lat};
                            xmlParam.stops[index] = stop;
                        }else{
                            xmlParam.stops[index].name = name;
                            xmlParam.stops[index].start = start;
                            xmlParam.stops[index].arrive = arrive;
                            xmlParam.stops[index].lon = lon;
                            xmlParam.stops[index].lat = lat;
                        }
                    });
                    selectLocationType = 1;
                    $("#saveButton").linkbutton('enable');
                    $('#nextButton').linkbutton('disable');
                    $("#addLonLats").linkbutton('select');
                    mapIframe.window.stationLocationDisplay(xmlParam.stops);
                    $("#stationButton").css('display','none');
                    $("#pointButton").css('display','block');
                }
            }
        }

        function saveXml(){

            $('#stationInfo td .iconfont').each(function(){
                let index = $(this).parent().parent().children("#tdSid")[0].innerHTML;
                let name = $("#stationName"+index).textbox('getValue');
                let pointGraphicArry = mapIframe.window.getTrainPoint(index);
                xmlParam.stops[index].ponitList = pointGraphicArry;
            });
            let xmlParamJson = JSON.parse(JSON.stringify(xmlParam));
            let data = {};
            data["line.name"] = xmlParamJson["name"];
            data["line.startStation"] = xmlParamJson["startStation"];
            data["line.startTime"] = xmlParamJson["startTime"];
            data["line.destStation"] = xmlParamJson["destStation"];
            data["line.arriveTime"] = xmlParamJson["arriveTime"];
            for (let i = 0; i < xmlParamJson["stops"].length; i++) {
                data["line.stops["+i+"].name"] = xmlParamJson["stops"][i].name;
                data["line.stops["+i+"].arrive"] = xmlParamJson["stops"][i].arrive;
                data["line.stops["+i+"].start"] = xmlParamJson["stops"][i].start;
                data["line.stops["+i+"].lon"] = xmlParamJson["stops"][i].lon;
                data["line.stops["+i+"].lat"] = xmlParamJson["stops"][i].lat;
                for (let j = 0; j < xmlParamJson["stops"][i].ponitList.length; j++) {
                    data["line.stops["+i+"].points["+j+"].lon"] = xmlParamJson["stops"][i].ponitList[j].lon;
                    data["line.stops["+i+"].points["+j+"].lat"] = xmlParamJson["stops"][i].ponitList[j].lat;
                }
            }
            $.ajax({
                type : "GET",
                url : "${pageContext.request.contextPath}/railwayLine/manualAddTrainXml",
                data : data,
                dataType : "json", //服务器响应的数据类型
                success : function(data) {
                    if (data != undefined && data != null) {
                        if (data.errorMsg) {
                            $.messager.alert("提示",data.errorMsg,'error');
                        }else{
                            $.messager.alert("提示","保存成功!",'info');
                        }
                    }
                }
            });
        }

        function addStation(){
            let trElement = $("#stationInfo tbody tr")[sid];
            sid++;
            var inHtml ='<tr>' +
                '<td><input class=\'iconfont\' type="checkbox" /></td>' +
                '<td id="tdSid">'+sid+'</td>' +
                '<td><input id="stationName'+sid+'" name="stationName" class="easyui-textbox stationInput stationNameClass" data-options="required:true"/></td>' +
                '<td><input name="stationEndTime" id="stationEndTime'+sid+'" class="easyui-timespinner stationInput stationEndTimeClass" data-options="required:true,showSeconds:false"></td>'+
                '<td><input name="stationStartTime" id="stationStartTime'+sid+'" class="easyui-timespinner stationInput stationStartTimeClass" data-options="required:true,showSeconds:false"></td>' +
                ' <td><input id="Longitude'+sid+'" name="Longitude" class="easyui-numberbox stationInput longitudeClass" data-options="required:true,min:-180,max:180,precision:6"/></td>' +
                '<td><input id="Latitude'+sid+'" name="Latitude" class="easyui-numberbox stationInput latitudeClass" data-options="required:true,min:-90,max:90,precision:6"/></td>' +
                '<tr>';
            $(trElement).after(inHtml);
            $('.easyui-textbox').textbox();
            $('.easyui-timespinner').timespinner();
            $('.easyui-numberbox').numberbox();
            let i = 0;
            $('#stationInfo td .iconfont').each(function(){
                $(this).parent().parent().find("#tdSid")[0].innerHTML = i;
                $(this).parent().parent().find(".stationNameClass").attr('id','stationName'+i);
                $(this).parent().parent().find(".stationStartTimeClass").attr('id','stationStartTime'+i);
                $(this).parent().parent().find(".stationEndTimeClass").attr('id','stationEndTime'+i);
                $(this).parent().parent().find(".longitudeClass").attr('id','Longitude'+i);
                $(this).parent().parent().find(".latitudeClass").attr('id','Latitude'+i);
                i++;
            });
            addClick();
        }

        function deleteStation(){
            if(sidArr.length==0){
                $.messager.alert("提示","请选择站点!",'warning');
                return;
            }
            $.messager.confirm("系统提示", "此次操作会连带站间轨迹一起删除，并不可恢复，确认继续操作吗?", function(r) {
                if(r){
                    $('#stationInfo td .iconfont').each(function(){
                        if($(this).is(":checked")){
                            let index = $(this).parent().parent().children("#tdSid")[0].innerHTML;
                            if(xmlParam["stops"]!=null && xmlParam["stops"].length>index){
                                xmlParam["stops"].splice(index,1);
                            }
                            $(this).parent().parent().remove();
                            mapIframe.window.delStation(index);
                        }
                    });
                    let i = 0;
                    $('#stationInfo td .iconfont').each(function(){
                        $(this).parent().parent().find("#tdSid")[0].innerHTML = i;
                        $(this).parent().parent().find(".stationNameClass").attr('id','stationName'+i);
                        $(this).parent().parent().find(".stationStartTimeClass").attr('id','stationStartTime'+i);
                        $(this).parent().parent().find(".stationEndTimeClass").attr('id','stationEndTime'+i);
                        $(this).parent().parent().find(".longitudeClass").attr('id','Longitude'+i);
                        $(this).parent().parent().find(".latitudeClass").attr('id','Latitude'+i);
                        i++;
                    });
                }
            });
        }

        function getLineLoctionTye(){
            let length = sidArr.length;
            if(length>0){
                return selectLocationType;
            }else{
                $.messager.alert("提示","请选择站点!",'warning');
                return null;
            }
        }

        function getStationInfo(){
            let length = sidArr.length;
            return {"sid":sidArr[length-1],"stationName":stationArr[length-1]};
        }

        function inputSelctLocation(lon,lat){
            let selectSid = sidArr[sidArr.length-1];
            $("#Longitude"+selectSid).numberbox("setValue",lon);
            $("#Latitude"+selectSid).numberbox("setValue",lat);
        }


    </script>

</head>

<body class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">

	<!--地图界面 -->
	<div data-options="region:'west',border:false" style="width:70%;height:100%;">
	  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default3.html?toolbarType=106"
        scrolling="auto" frameborder="0"
        style="width:100%;height:100%;border:2px;"></iframe>
    </div>

    <div data-options="region:'east',title:'高铁线路设置',split:false,tools:'#tt3'" style="width:30%;height:100%;overflow: auto;">

        <form id="trainff" method="post">
            <div class="inputDivShow" >
                <input id="trainCode" name="trainCode" class="easyui-textbox" data-options="required:true,prompt:'请输入高铁车次号'" />
            </div>
            <div id="trainCodeInfo" style="display: block">
                <div style="height:2%;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;">
                    <div class="panel-title">添加车次信息</div>
                </div>
                <div style="width:100%;height:87%;overflow-y:auto;" class="tableClass">
                    <table style="width:100%;">
                        <thead>
                        <tr>
                            <th>字段</th>
                            <th>值</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>MetroLine LID</td>
                            <td>
                                <input id="metroLineLID" name="metroLineLID" value="0" class="easyui-numberbox" data-options="required:true" readonly="readonly"/>
                            </td>
                        </tr>
                        <tr>
                            <td>始发站</td>
                            <td>
                                <input id="startStation" name="startStation" class="easyui-textbox" data-options="required:true"/>
                            </td>
                        </tr>
                        <tr>
                            <td>终点站</td>
                            <td>
                                <input id="endStation" name="endStation" class="easyui-textbox" data-options="required:true"/>
                            </td>
                        </tr>
                        <tr>
                            <td>开始时间(StartTime)</td>
                            <td>
                                <input name="metroLineStartTime" id="metroLineStartTime" class="easyui-timespinner" data-options="required:true,showSeconds:false">
                            </td>
                        </tr>
                        <tr>
                            <td>终点时间(EndTime)</td>
                            <td>
                                <input name="metroLineEndTime" id="metroLineEndTime" class="easyui-timespinner" data-options="required:true,showSeconds:false">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>

        <form id="stationff" method="post">
            <div id="stationInfo" style="display: none">
                <div style="height:2%;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;">
                    <div class="panel-title" data-options="title:'tools:'#tt3'">添加各站点经纬度信息</div>
                </div>
                <div id="stationButton" style="display: block">
                    <a id="addStation" data-options="iconCls:'icon-add'" class="easyui-linkbutton" onclick="addStation();" >添加站点信息</a>
                    <a id="deleteStation" data-options="iconCls:'icon-remove'" class="easyui-linkbutton" onclick="deleteStation();" >删除站点信息</a>
                </div>
                <div id="pointButton" style="display: none">
                    <a id="addLonLats" data-options="iconCls:'icon-add'" class="easyui-linkbutton" onclick="addLonLats();" >添加站间经纬度</a>
                </div>
                <div style="width:100%;height:84%;overflow-y:auto;" class="tableClass">
                    <table style="width:100%;">
                        <thead>
                        <tr>
                            <th></th>
                            <th>序号</th>
                            <th>站点名称</th>
                            <th>到达时间(ArriveTime)</th>
                            <th>开始时间(StartTime)</th>
                            <th>经度(Longitude)</th>
                            <th>纬度(Latitude)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <input class='iconfont' type="checkbox" disabled/>
                            </td>
                            <td id="tdSid">0</td>
                            <td>
                                <input id="stationName0" name="stationName" class="easyui-textbox stationInput stationNameClass" data-options="required:true"/>
                            </td>
                            <td>
                                <input name="stationEndTime" id="stationEndTime0" class="easyui-timespinner stationInput stationEndTimeClass" data-options="required:true,showSeconds:false">
                            </td>
                            <td>
                                <input name="stationStartTime" id="stationStartTime0" class="easyui-timespinner stationInput stationStartTimeClass" data-options="required:true,showSeconds:false">
                            </td>
                            <td>
                                <input id="Longitude0" name="Longitude" class="easyui-numberbox stationInput longitudeClass" data-options="required:true,min:-180,max:180,precision:6"/>
                            </td>
                            <td>
                                <input id="Latitude0" name="Latitude" class="easyui-numberbox stationInput latitudeClass" data-options="required:true,min:-90,max:90,precision:6"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>

        <div data-options="region:'south',border:false" style="height:5%;">
            <table width="100%" style="border-top:1px solid #95b8e7;">
                <tr height="35px">
                    <td width="33;" align="right">
                        <a id="beforeButton" data-options="iconCls:'icon-undo',disabled:true" class="easyui-linkbutton" style="width:100px;" onclick="before();" >上一步</a>
                    </td>
                    <td width="33%;" align="right">
                        <a id="nextButton" iconCls="icon-redo" class="easyui-linkbutton" style="width:100px;" onclick="next();"  >下一步</a>
                    </td>
                    <td width="33%;">
                        <a id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-ok',disabled:true" style="width:100px;" onclick="saveXml();">保存</a>
                    </td>
                </tr>
            </table>
        </div>
<%--        <div class="inputDivShow" >--%>
<%--            测试点经度：<input id="testPointLon" class="easyui-numberbox"  name="testPointLon" data-options="required:true,min:-180,max:180,precision:6"/>--%>
<%--        </div>--%>
<%--        <div class="inputDivShow" >--%>
<%--            终端ID：--%>
<%--            <input id="terminalId" name="boxId" class="easyui-combobox" data-options="validType:'length[1,24]',required:true,editable:false" />--%>
<%--        </div>--%>
    </div>

    <div id="tt3">
        <a id="returnOld" class="easyui-linkbutton" onclick="javascript:goBack();" data-options="iconCls:'icon-undo'" style="width:60px;height:15px;" >返回</a>
    </div>

</body>
</html>
