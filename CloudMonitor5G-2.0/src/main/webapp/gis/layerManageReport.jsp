<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist/echarts.min.js"></script>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/easyui/themes/color.css" />
<link href="${pageContext.request.contextPath}/gis/css/Style.css" rel="stylesheet" type="text/css" />
<div id="cellBoshuAnalyse" class="easyui-window" title="小区波束分析" style="width:400px;height:500px"
        data-options="iconCls:'icon-save',modal:true,closed:'true',onClose:boshuDisconnect2">
   <div style="text-align:center;margin-top: 15px;padding:10px;">
   	显示的beamid<input id="beamId" style="width:60px;"> 
   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   	显示范围<input id="boshuLimits" style="width:60px;">
   </div>
   <div id="boshuAnalyseDiv" style="text-align:center;width: 350px;height:400px;"></div>
   <div style="text-align:center">
	   <a id="btn" href="#" class="easyui-linkbutton" data-options="" onclick="queryCellBoshuAnalyse()">确认</a>
	   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   <a id="btn" href="#" class="easyui-linkbutton" data-options="" onclick="clearCellBoshuAnalyse()">取消</a>
   </div>
</div>
<div id="cellBoshuCondition" class="easyui-window" title="全图特定波束统计条件设置" style="width:400px;height:150px;"
        data-options="iconCls:'icon-save',modal:true,closed:'true'">
    <div style="margin-left: 5%;width: 90%;">
    	<h3 style="color: red;text-align:center">请注意,该功能针对所有的路测采样点进行分析,可能需要耗费较多的系统资源</h3>
    </div>
   	<div style="text-align:center">
   		显示beamid=<input id="boshuScope" style="width:60px;">的波束的覆盖图
   	</div>
   	<div style="text-align:center">
		<label><input id="radioOne" type="radio" name="boshuChoose" value="1" checked>只考虑serving beam</label>
		<label><input id="radioTwo" type="radio" name="boshuChoose" value="2">考虑serving beam并且考虑邻区beam</label>
	</div>
	<div style="height: 10px;"></div>
   	<div style="text-align:center">
	   	<a id="btn" href="#" class="easyui-linkbutton" onclick="saveCellBoshuCondition()">&nbsp;保存&nbsp;</a>
	   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   	<a id="btn" href="#" class="easyui-linkbutton" onclick="clearCellBoshuCondition()">&nbsp;重置&nbsp;</a>
   	</div>
</div>
<div id="layerManagerWin" class="easyui-window" title="图层管理" style="width:600px;height:400px;"
        data-options="iconCls:'icon-save',modal:true,closed:'true'">
    <div class="mapTools" style="height: 3%;display: none;">
    	<ul>
    		<li><img class="navItem" id="saveLayer" onclick='saveLayerFunction()' src="<%=basePath%>/gis/images/save.png" title="图层另存为" alt="图层另存为" /></li>
    		<li><img class="navItem" id="addLayer" onclick='addLayerFunction()' src="<%=basePath%>/gis/images/addLayer.png" title="增加图层" alt="增加图层" /></li>
    		<li><img class="navItem" id="delLayer" onclick='delLayerFunction()' src="<%=basePath%>/gis/images/delLayer.png" title="删除图层" alt="删除图层" /></li>
    		<li><img class="navItem" id="upLayer" onclick='upLayerFunction()' src="<%=basePath%>/gis/images/upLayer.png" title="上移图层" alt="上移图层" /></li>
    		<li><img class="navItem" id="downLayer" onclick='downLayerFunction()' src="<%=basePath%>/gis/images/downLayer.png" title="下移图层" alt="下移图层" /></li>
    	</ul>
	    <div id="addLayerWin" class="easyui-window" title="添加图层" style="width:300px;height:150px;"
	        data-options="iconCls:'icon-save',modal:true,closed:'true'">
	        <div style="height: 60%;width: 100%;">
	        	<div>
	        		图层名称:<input class="easyui-textbox" id='addLayerFileName' name="addLayerFileName" style="width:70%">
	        	</div>
	        	<div style="margin-top: 10px;">
	        		文件:<input class="easyui-filebox" id='addLayerFile' name="addLayerFile"
	        		 data-options="prompt:'请选择文件...',buttonText:'选择文件',accept:'*.shp'" style="width:70%">
	        	</div>      
	        </div>
	        <div style="height: 40%;width: 100%;">
	        	<div style="text-align:center">
				   	<a id="btn" class="easyui-linkbutton" onclick="addLayerButtonFun()">确定</a>
			   	</div>
	        </div>
	    </div>
    </div>
    <div style="width: 99%;height: 30%;display: none;">
        <table id="layerChooseTable"></table>
    </div>
    <div style="width:99%;height: 80%;">
    	<div class="easyui-tabs" id="layerManagerTab" data-options="fit:true,plain:true,border:false" style="padding:4px;">
			<div id='tuliTab' data-options="closeable:'false',title:'图例',cache:false,selected:true" style="padding:4px;">
				<div style="width:100%;height: 100%;">
					<div style="width:50%;height: 100%;float:left;">
						<div style="width: 100%;height:10%;">
							指标选择:
					        <input id="targetChoose" class="easyui-combobox" style="width:200px;" data-options="valueField: 'label',textField: 'value'" />
						</div>
					    <div style="width: 100%;height: 90%">
				    	    <table id="targetChooseTable"></table>
					    </div>
					</div>
					<div style="width:50%;height: 100%;float:left;">
						<div style="width:100%;height: 50%;margin-left: 3px;display:none;">
							采样点显示范围:
							<div style="width:100%;height: 10%;">
								
							</div>
							<div style="width:100%;height: 32%;">
								<label>
									<input type="radio" name="sampleScope" value="1">只显示问题采样点
								</label>
							</div>
							<div style="width:100%;height: 32%;">
								<label>
									<input type="radio" name="sampleScope" value="2">显示问题采样点,同时提供测试轨迹
								</label>
							</div>
						</div>
						<div style="width:100%;height: 10%;margin-left: 3px;">
							采样点显示标签选择:
							<div style="width:100%;height: 80%;display:none; ">
								<label>
									<input type="radio" name="sampletarget" value="freq">频点
								</label>
								<label>
									<input type="radio" name="sampletarget" value="pci">PCI
								</label>
								<label>
									<input type="radio" name="sampletarget" value="beamNo">Beam ID
								</label>
							</div>
						</div>
						<div style="width:100%;height: 20%;">
							<div style="text-align:left;">
							   	<a id="btn" class="easyui-linkbutton" onclick="targetFoatFunc()" style="display: none">标签字体</a>
							   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							   	<a id="btn" class="easyui-linkbutton" onclick="sampleTypeFunc()">采样点样式</a>
						   	</div>
						</div>
						<div id="targetFoatWin" class="easyui-window" title="标签字体" style="width:400px;height:300px;"
       						data-options="iconCls:'icon-save',modal:true,closed:'true'">
       						 <div style="width:100%;height: 85%;">
       						 	<table id="targetFoatTable"></table>
       						 </div>
       						 <div style="width:100%;height: 15%;">
								<div style="text-align:center;margin-top: 5px;">
								   	<a id="btn" href="#" class="easyui-linkbutton" onclick="saveTargetFoatFunc()">&nbsp;&nbsp;确定&nbsp;&nbsp;</a>
								   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								   	<a id="btn" href="#" class="easyui-linkbutton" onclick="cancelTargetFoatFunc()">&nbsp;&nbsp;取消&nbsp;&nbsp;</a>
							   	</div>
							</div>
       					</div>
       					<div id="sampleTypeWin" class="easyui-window" title="采样点样式" style="width:300px;height:200px;"
       						data-options="iconCls:'icon-save',modal:true,closed:'true'">
       						 <div style="width:100%;height: 75%;">
       						 	形状选择:<input id="sampleTypeSelect" class="easyui-combobox" style="width:200px;" 
       						 	data-options="valueField:'label',textField:'value'"/>
       						 </div>
       						 <div style="width:100%;height: 25%;">
								<div style="text-align:center;margin-top: 5px;">
								   	<a id="btn" class="easyui-linkbutton" onclick="saveSampleTypeFunc()">&nbsp;&nbsp;刷新&nbsp;&nbsp;</a>
							   	</div>
							</div>
       					</div>
					</div>
				</div>
			</div>
			<div data-options="closeable:'false',title:'小区',cache:false" style="padding:4px;">
				<div style="heigth: 55%;">
					<div style="heigth: 20%;margin-top: 10px;">
						<input type="checkbox" id="showImg" name="showImg" value="1"  onclick="showImgFunc()">显示图源
					</div>
					<div style="heigth: 20%;margin-top: 10px;display:none;">
						&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id='network4G' name="networkType" onclick="choosePci(4)" value="4">4G
						&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id='network5G' name="networkType" onclick="choosePci(5)" value="5">5G
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio"  id="showNetwork" name="networkType" value="45"  onclick="choosePci(45)">同时显示45G
											
					</div>
					<br>
					<div style="heigth: 25%;margin-top: 10px;" id="unify4GRadioDiv" >
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id='unifyRadio4G' name="showImgColor4G" onclick="showImgCheckedFunc('showImg')" value="1">4G统一着色
								&nbsp;&nbsp;<input id='unifyColor4G' style="width: 40px;" type="color" value='#3F97F1'>
					</div>
					<div style="heigth: 25%;margin-top: 10px;" id="unify5GRadioDiv">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id='unifyRadio5G' name="showImgColor5G" onclick="showImgCheckedFunc('showImg')" value="1">5G统一着色
								&nbsp;&nbsp;<input id='unifyColor5G' style="width: 40px;" type="color" value='#3F97F1'>
					</div>
					<br>
					<div style="heigth: 25%;margin-top: 10px;" id='pciMo4RadioDiv'>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id='pciMo4Radio' name="showImgColor5G" onclick="showImgCheckedFunc('showImg')" value="2">PCI摸4
							&nbsp;&nbsp;
							<input name="pciMo4" style="width: 40px;" type="color" >&nbsp;&nbsp;
							<input name="pciMo4" style="width: 40px;" type="color" >&nbsp;&nbsp;
							<input name="pciMo4" style="width: 40px;" type="color" >&nbsp;&nbsp;
							<input name="pciMo4" style="width: 40px;" type="color" >&nbsp;&nbsp;
					</div>
					<div style="heigth: 25%;margin-top: 10px;" id='pciMo3RadioDiv'>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id='pciMo3Radio' name="showImgColor4G" onclick="showImgCheckedFunc('showImg')" value="2">PCI摸3
							&nbsp;&nbsp;
							<input name="pciMo3" style="width: 40px;" type="color" >&nbsp;&nbsp;
							<input name="pciMo3" style="width: 40px;" type="color" >&nbsp;&nbsp;
							<input name="pciMo3" style="width: 40px;" type="color" >&nbsp;&nbsp;
					</div>
					<div style="heigth: 25%;margin-top: 10px;">
							&nbsp;&nbsp;&nbsp;&nbsp;
							透明度&nbsp;不透明<input id='cellRange' type="range" value="0" name="points" min="0" max="100">100%  透明
					</div>
				</div>
				<div style="heigth: 45%;margin-top: 10px;">
					<div style="heigth: 30%;margin-top: 10px;">
						<input type="checkbox" name="showTarget" id="showTarget" value="1" checked="checked" onclick="showTargetFunc()">显示标签
					</div>
					<div style="heigth: 70%;margin-top: 10px;">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" name="showTargetOption" onclick="showImgCheckedFunc('showTarget')"
							 	value="CELLNAME">名称&nbsp;&nbsp;
							<input type="checkbox" name="showTargetOption" onclick="showImgCheckedFunc('showTarget')" value="CELLID" >编码&nbsp;&nbsp;
							<input type="checkbox" name="showTargetOption" onclick="showImgCheckedFunc('showTarget')" value="TAC" >TAC&nbsp;&nbsp;
							<input type="checkbox" name="showTargetOption" onclick="showImgCheckedFunc('showTarget')" value="FREQ1" >频点列表&nbsp;&nbsp;
							<input type="checkbox" name="showTargetOption" onclick="showImgCheckedFunc('showTarget')" value="PCI" >PCI&nbsp;&nbsp;
					</div>
				</div>
			</div>
	    </div>
    </div>
	<div style="width:99%;height: 10%;">
		<div style="text-align:center;margin-top: 10px;">
		   	<a id="btn" class="easyui-linkbutton" onclick="saveLayerManage()">&nbsp;&nbsp;确定&nbsp;&nbsp;</a>
		   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		   	<a id="btn" class="easyui-linkbutton" onclick="cancelLayerManage()">&nbsp;&nbsp;取消&nbsp;&nbsp;</a>
		   	
	   	</div>
	</div>
</div>

<script type="text/javascript">
     
    var myChart = echarts.init(document.getElementById('boshuAnalyseDiv'));
    var boshuLimits;
	var beamId;
	var boshuAnalyse;
    function analyseMap(boshuData){
    	boshuAnalyse = boshuData;
	    myChart.clear();
	    // 基于准备好的dom，初始化echarts实例
	    var data = [];
		/* for (var i = 0; i <= 360; i++) {
		    //var t = i / 180 * Math.PI;
		    var t = i / 180 * Math.PI;
		    var r = Math.sin(1 * t) * Math.cos(2 * t);
		    data.push([r, i]);
		} */
		if(beamId && boshuLimits){
			for(var i = 0;i < boshuData.length; i++){
				if(boshuData[i].beamId != beamId){
					continue;
				}
				if(boshuData[i].value > boshuLimits){
					continue;
				}
				data.push([boshuData[i].value.toFixed(2), boshuData[i].angle.toFixed(2)]);
			}
		}else if(beamId){
			for(var i = 0;i < boshuData.length; i++){
				if(boshuData[i].beamId != beamId){
					continue;
				}
				data.push([boshuData[i].value.toFixed(2), boshuData[i].angle.toFixed(2)]);
			}
		}else if(boshuLimits){
			for(var i = 0;i < boshuData.length; i++){
				if(boshuData[i].value > boshuLimits){
					continue;
				}
				data.push([boshuData[i].value.toFixed(2), boshuData[i].angle.toFixed(2)]);
			}
		}else{
			for(var i = 0;i < boshuData.length; i++){
				data.push([boshuData[i].value.toFixed(2), boshuData[i].angle.toFixed(2)]);
			}
		}
		
		var option = {
		    title: {
		        
		    },
		    legend: {
		    },
		    polar: {
		        center: ['54%', '50%']
		    },
		    tooltip: {
		        axisPointer: {
		            type: 'cross'
		        }
		    },
		    angleAxis: {
		        type: 'value',
		        startAngle: 90,
		        axisLabel: {
		            formatter: function(value, index) {
		                return value + "°";
		
		            }
		        },
		    },
		    radiusAxis: {
		        min: 0,
		        axisLabel: {
		            formatter: function(value, index) {
		            	if(value!=0){
		            		return value + "m";
		            	}else{
		            		return value;
		            	}
		            }
		        },
		    },
		    series: [{
		        coordinateSystem: 'polar',
		        name: 'line',
		        type: 'line',
	            itemStyle: {
                    normal: {
                        lineStyle:{
                            width:0.1//设置线条粗细
                        }
                    }
                },
		        data: data
		    }],
		    animationDuration: 2000
		};
	
	    // 使用刚指定的配置项和数据显示图表。
	    myChart.setOption(option);
    }
    
    
    var showRoadPointsData;
    //全图特定波束统计条件设置 保存按钮
    function saveCellBoshuCondition(){
    	var boshuScope = $("#boshuScope").val();
    	var boshuChoose = $('input[name=boshuChoose]:checked').val();
    	
    	if(boshuScope){
    		if(showRoadPointsData){
    	    	var data = JSON.parse(JSON.stringify(showRoadPointsData));
    	    	for(var i = 0; i < showRoadPointsData[0].length;i++){
    	    		for(var j = 0; j < showRoadPointsData[0][i][1].length;j++){
    	    			if(boshuChoose == 1){
	    	    			if(showRoadPointsData[0][i][1][j].beamId != boshuScope){
	    	    				data[0][i][1][j] = null;
	    	    			}
    	    			}else{
    	    				if(showRoadPointsData[0][i][1][j].beamId != boshuScope && showRoadPointsData[0][i][1][j].nbeamId != boshuScope){
	    	    				data[0][i][1][j] = null;
	    	    			}
    	    			}
    	    		}
    	    	}
    	    	mapIframe.window.ShowGPSTrackRender5g(data);
        	}
    	}else{
    		mapIframe.window.ShowGPSTrackRender5g(showRoadPointsData);
    	}
    	
    	$('#cellBoshuCondition').window('close');
    }
    
    //全图特定波束统计条件设置 重置按钮
    function clearCellBoshuCondition(){
    	$("#boshuScope").val('');
    	$("#radioOne").attr("checked","checked");
        $("#radioTwo").removeAttr("checked");
    }
    
    //小区波束分析window框关闭时,清除地图小区图层的点击事件
    function boshuDisconnect2(){
    	mapIframe.window.boshuDisconnect();
    }
    
    //小区波束分析确认按钮
    function queryCellBoshuAnalyse(){
    	boshuLimits = $("#boshuLimits").val();
    	beamId = $("#beamId").val();
    	analyseMap(boshuAnalyse);
    }
    
    //小区波束分析取消按钮
    function clearCellBoshuAnalyse(){
    	$("#boshuLimits").val('');
    	$("#beamId").val('');
    	$('#cellBoshuAnalyse').window('close');
    }
    
    
    /*-------------------------------------------图层管理js---------------------------------------------------*/
    
    var layerManageData = {};
	//初始化图层管理
	function initLayerManageFunc(data){
		layerManageData = data;
		initLayerChooseTable(data.layerChooseData);//初始化图层
		initConventionalSigns(data.conventionalSignsData);//初始化图例
		initCellTable();//初始化小区模块
		//initLineTable();//初始化连线模块
		//initEventTreeFunc(data.eventData.eventTreeData);//初始化事件
		//initEventTable(data.eventData.eventTableData);//初始化事件表格
	}
    
	//保存图层按钮函数
	function saveLayerFunction(){
		var checkedData = $("#layerChooseTable").datagrid('getChecked');
		if(checkedData.length == 0){
			//alert('请选择至少一行数据');
			$.messager.alert("系统提示", "请至少勾选一个需要保存的图层");
			return ;
		}
		for(var i=0; i<checkedData.length;i++){
			alert(checkedData[i]);
		}
	}
	
	//添加图层按钮函数
	function addLayerFunction(){
		$("#addLayerWin").window('open');
	}
	
	//添加图层确定按钮
	function addLayerButtonFun(){
		$("#layerChooseTable").datagrid('appendRow',{
			recSeqNo: true,
			name: $('#addLayerFileName').val(),
			mark:true,
			deviationX:'',
			deviationY:''
	    });
		$('#addLayerFileName').textbox('reset');
		$('#addLayerFile').filebox('reset');
		$("#addLayerWin").window('close');
	}
	
	//删除图层按钮函数
	function delLayerFunction(){
		var checkedData = $("#layerChooseTable").datagrid('getChecked');
		if(checkedData.length == 0){
			$.messager.alert("系统提示", "请至少勾选一个需要删除的图层");
			return ;
		}
		if(confirm("确定删除选中图层?")){
			for(var i = 0; i < checkedData.length; i++){
				$('#layerChooseTable').datagrid('deleteRow',$('#layerChooseTable').datagrid('getRowIndex',checkedData[i]));
			}
		}
	}
    
	//上移图层按钮函数
	function upLayerFunction(){
		var checkedData = $("#layerChooseTable").datagrid('getChecked');
		if(checkedData.length != 1){
			$.messager.alert("系统提示", "请勾选一个需要上移的图层");
			return ;
		}
		$('#layerChooseTable').datagrid('deleteRow',$('#layerChooseTable').datagrid('getRowIndex',checkedData[0]));
		$("#layerChooseTable").datagrid('insertRow',{
			index:0,
			row:{
				recSeqNo: checkedData[0].recSeqNo,
				name: checkedData[0].name,
				mark:checkedData[0].mark,
				deviationX:checkedData[0].deviationX,
				deviationY:checkedData[0].deviationY}
		});
	}
	
	//下移图层按钮函数
	function downLayerFunction(){
		var checkedData = $("#layerChooseTable").datagrid('getChecked');
		console.log(checkedData[0]);
		if(checkedData.length != 1){
			$.messager.alert("系统提示", "请勾选一个需要下移的图层");
			return ;
		}
		$('#layerChooseTable').datagrid('deleteRow',$('#layerChooseTable').datagrid('getRowIndex',checkedData[0]));
		$("#layerChooseTable").datagrid('insertRow',{
			row:{
				recSeqNo: checkedData[0].recSeqNo,
				name: checkedData[0].name,
				mark:checkedData[0].mark,
				deviationX:checkedData[0].deviationX,
				deviationY:checkedData[0].deviationY}
		});
	}
	
    var layerChooseTableColumns=[[
    	{field:'recSeqNo',checkbox:true},
        {field:'name',width:85,align:'center',title:'名称',sortable:true},
        {field:'mark',align:'center',title:'标签',sortable:true,formatter:showImg,width:'176px'},
        {field:'deviationX',width:150,align:'center',title:'X轴偏移(米)',sortable:true,editor:'text'},
        {field:'deviationY',width:90,align:'center',title:'Y轴偏移(米)',sortable:true,editor:'text'}
    ]];
    
    var editIndex = -1;
    //绘制图层表格
    function initLayerChooseTable(data){
        $("#layerChooseTable").datagrid({
            // 表头
            columns:layerChooseTableColumns,
            //url:'${pageContext.request.contextPath}/nasexceptionview/doPageListJson.action?',
            data: data,
            fitColumns:true,
            //填满区域
            fit:true,
            //奇偶变色
            striped:true,
            singleSelect:true,
            checkOnSelect:true,
            // 行号
            rownumbers:false,
            singleSelect:false,
            pagination:false,
            onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
            	if (data.rows.length > 0) {
	                //循环判断操作为新增的不能选择
	                for (var i = 0; i < data.rows.length; i++) {
	                    //根据operate让某些行不可选
	                    if (data.rows[i].recSeqNo) {
	                        $("#layerChooseTable").datagrid('checkRow', i);
	                    }
	                }
	            }
	        },
	        onClickRow: function(rowIndex, rowData){
	        	//保存之前修改的一行
	        	if(editIndex != -1 && editIndex != rowIndex){
					var edX = $('#layerChooseTable').datagrid('getEditor', {index:editIndex,field:'deviationX'});
					if(edX != null){
						$('#layerChooseTable').datagrid('getRows')[editIndex]['deviationX'] = edX.target[0].value;
					}
					
					var edY = $('#layerChooseTable').datagrid('getEditor', {index:editIndex,field:'deviationY'});
					if(edY != null){
						$('#layerChooseTable').datagrid('getRows')[editIndex]['deviationY'] = edY.target[0].value;
					}
					$('#layerChooseTable').datagrid('endEdit', editIndex);
	        	}
	        	editIndex = rowIndex;
	        	if(rowData.name == "测试数据"){
		        	//开启新的一行
		        	$("#layerChooseTable").datagrid('beginEdit',rowIndex);
	        	}
	        },
	        onSelect: function(rowIndex, rowData){
	        	$('#layerChooseTable').datagrid('getRows')[rowIndex]['recSeqNo'] = true;
	        },
	        onUnselect: function(rowIndex, rowData){
	        	$('#layerChooseTable').datagrid('getRows')[rowIndex]['recSeqNo'] = false;
	        }
        });
    }
    
    function showImg(value, row, index){
    	if(row.mark){
			return "<img style='width:24px;height:24px;' onClick=\"chanceImgFunc('img"+index+"1','img"+index+"2',"+index+",false)\" id=\"img" + index + "1\" border='1' src='<%=basePath%>/gis/images/标签1.png'/>"
				+ "<img style='width:24px;height:24px;display:none;' onClick=\"chanceImgFunc('img"+index+"2','img"+index+"1',"+index+",true)\" id=\"img" + index + "2\" border='1' src='<%=basePath%>/gis/images/标签.png'/>";
    	}else{
			return "<img style='width:24px;height:24px;display:none;' onClick=\"chanceImgFunc('img"+index+"1','img"+index+"2',"+index+",false)\" id=\"img" + index + "1\" border='1' src='<%=basePath%>/gis/images/标签1.png'/>"
				+ "<img style='width:24px;height:24px;' onClick=\"chanceImgFunc('img"+index+"2','img"+index+"1',"+index+",true)\" id=\"img" + index + "2\" border='1' src='<%=basePath%>/gis/images/标签.png'/>";
    	}
	}
	
	function chanceImgFunc(noneId,blockId,index,flag){
		document.getElementById(noneId).style.display = "none";
		document.getElementById(noneId).style.marginLeft = "72px";
		document.getElementById(blockId).style.display = "block";
		document.getElementById(blockId).style.marginLeft = "72px";
		
		$("#layerChooseTable").datagrid('beginEdit',index);
		$('#layerChooseTable').datagrid('getRows')[index]['mark'] = flag;
		$("#layerChooseTable").datagrid('endEdit',index);
	}
	
	var conventionalSignsData;
	//初始化图例
	function initConventionalSigns(data){
		$('#targetChoose').combobox({
	        valueField:'value',
	        textField:'label',
	        data:data.targetChooseData
	    });
	    conventionalSignsData = data;
	    for(var i = 0; i < data.targetChooseData.length;i++){
			if(data.targetChooseData[i].selected){
				initTargetChooseTable(data.targetChooseData[i].value);
			}
		}
	    //初始化采样点显示范围
	    if(conventionalSignsData.sampleScopeData.sampleScope != 0){
	    	//$("input[name=sampleScope]").val(conventionalSignsData.sampleScopeData.sampleScope);
	    	$("input[name=sampleScope]").each(function () {
	    		if(conventionalSignsData.sampleScopeData.sampleScope == $(this).val()){
				    $(this).prop('checked',true);
	    		}
			});
	    }
	  //初始化采样点显示标签选择
	   if(conventionalSignsData.sampletargetData.sampletarget != 0){
	    	$("input[name=sampletarget]").each(function () {
	    		if(conventionalSignsData.sampletargetData.sampletarget == $(this).val()){
				    $(this).prop('checked',true);
	    		}
			});
	    }
	}


    $("#targetChoose").combobox({
        onSelect: function () {
            var chooseVal = $("#targetChoose").combobox('getValue');
           	initTargetChooseTable(chooseVal);
        }
    })
    
	var targetChooseTableColumns=[[
        {field:'color',align:'center',title:'颜色',sortable:true,
        	formatter:function(value, row, index){
        		return "<input type='color' onchange='targetColorFunc(\"targetColor"+index+"\",\""+index+"\")' value='"+value+"' id='targetColor"+index+"'>";
        	}},
        {field:'scope',align:'center',title:' 范围  ',sortable:true},
        {field:'data',align:'center',title:'透明度',sortable:true,hidden:true}
    ]];
    
    //修改颜色
    function targetColorFunc(id,index){
    	var tc = $('#targetChoose').combobox('getValue');
    	/* if(tc == 1){
    		tc = 'rsrp';
    	}else if(tc == 9){
    		tc = 'rsrq';
    	}else if(val == 101){
    		tc = 'ibler';
    	}else if(val == 102){
    		tc = 'beam';
    	} */
    	if(tc == 109||tc == 110){
    		tc = 'rsrp';
    	}else if(tc == 111||tc == 112){
    		tc = 'sinr';
    	}else if(tc == 102){
    		tc = 'beam';
    	}else if(tc == 113){
    		tc = 'macthrputdl';
    	}else if(tc == 114){
    		tc = 'macthrputul';
    	}else if(tc == 115){
    		tc = 'pci';
    	}
    	conventionalSignsData.targetChooseTableData[tc][index].color = $("#"+id+"").val();
    }
    
    //初始化指标表格
	function initTargetChooseTable(val){
    	/* if(val == 1){
    		val = 'rsrp';
    	}else if(val == 9){
    		val = 'rsrq';
    	}else if(val == 101){
    		val = 'ibler';
    	}else if(val == 102){
    		val = 'beam';
    	} */
    	if(val == 109||val == 110){
    		val = 'rsrp';
    	}else if(val == 111||val == 112){
    		val = 'sinr';
    	}else if(val == 102){
    		val = 'beam';
    	}else if(val == 113){
    		val = 'macthrputdl';
    	}else if(val == 114){
    		val = 'macthrputul';
    	}else if(val == 115){
    		val = 'pci';
    	}
		var data = conventionalSignsData.targetChooseTableData[val];
		
		$("#targetChooseTable").datagrid({
            // 表头
            columns:targetChooseTableColumns,
            //url:'${pageContext.request.contextPath}/nasexceptionview/doPageListJson.action?',
            data: data,
            fitColumns:true,
            //填满区域
            fit:true,
            //奇偶变色
            striped:true,
            singleSelect:true,
            checkOnSelect:false,
            // 行号
            rownumbers:false,
            singleSelect:false,
            pagination:false,
            onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
            	
	        },
	        onClickCell: function(index,field,value){
	        	
	        }
        });
	}
	
	//显示颜色
	function showColorFunc(value, row, index){
		return "background-color:#"+value+";color:black";
	}
	
	//标签字体点击函数
	function targetFoatFunc(){
		initTargetFoatTable();
		$("#targetFoatWin").window('open');
	}
	
	//采样点样式点击函数
	function sampleTypeFunc(){
		$('#sampleTypeSelect').combobox({
	        valueField:'value',
	        textField:'label',
	        data:conventionalSignsData.sampleTypeData
	    });
		$("#sampleTypeWin").window('open');
	}
	
	//采样点样式
	function saveSampleTypeFunc(){
		for(var i = 0;i < conventionalSignsData.sampleTypeData.length;i++){
			if(conventionalSignsData.sampleTypeData[i].value == $("#sampleTypeSelect").combobox('getValue')){
				conventionalSignsData.sampleTypeData[i].selected = true;
			}else{
				conventionalSignsData.sampleTypeData[i].selected = false;
			}
		}
		$("#sampleTypeWin").window('close');
	}
	
	var targetFoatTableColumns=[[
        {field:'name',align:'center',title:'名称',sortable:true},
        {field:'fontColor',align:'center',title:'字体颜色',sortable:true,
        	formatter:function(value, row, index){
        		var list = value.split(',');
        		var str = "<select class='fontColorSelect' id=\"fontColorSelect"+index+"\" style='width:60px;'>";
        		for(var i = 0;i < list.length;i++){
        			if(list[i] == row.fontColorSelected){
	        			str = str + "<option value=\""+list[i]+"\" selected>"+list[i]+"</option>";
        			}else{
        				str = str + "<option value=\""+list[i]+"\">"+list[i]+"</option>";
        			}
        		}
        		return str;
        	}},
        {field:'fontSize',align:'center',title:'字号',sortable:true,
        	formatter:function(value, row, index){
        		var list = value.split(',');
        		var str = "<select class='fontSizeSelect' id=\"fontSizeSelect"+index+"\" style='width:60px;'>";
        		for(var i = 0;i < list.length;i++){
        			if(list[i] == row.fontSizeSelected){
	        			str = str + "<option value=\""+list[i]+"\" selected>"+list[i]+"</option>";
        			}else{
        				str = str + "<option value=\""+list[i]+"\">"+list[i]+"</option>";
        			}
        		}
        		str = str + "</select>";
        		return str;
        	}}
    ]];
    
    //初始化标签字体表格
	function initTargetFoatTable(){
		data = conventionalSignsData.targetFoatData;
		$("#targetFoatTable").datagrid({
            // 表头
            columns:targetFoatTableColumns,
            //url:'${pageContext.request.contextPath}/nasexceptionview/doPageListJson.action?',
            data: data,
            fitColumns:true,
            //填满区域
            fit:true,
            //奇偶变色
            striped:true,
            singleSelect:true,
            checkOnSelect:false,
            // 行号
            rownumbers:false,
            singleSelect:false,
            pagination:false,
            onLoadSuccess: function(dataa){//加载完毕后获取所有的checkbox遍历
	        },
	        onClickCell: function(index,field,value){
	        	
	        }
        });
	}
	
	//标签字体确定按钮
	function saveTargetFoatFunc(){
		for(var i = 0; i < $(".fontColorSelect").length;i++){
			conventionalSignsData.targetFoatData[i].fontColorSelected = $(".fontColorSelect")[i].value;
			conventionalSignsData.targetFoatData[i].fontSizeSelected = $(".fontSizeSelect")[i].value;
		}
		cancelTargetFoatFunc();
	}
	
	//标签字体取消按钮
	function cancelTargetFoatFunc(){
		$("#targetFoatWin").window('close');
	}
	
	//字体颜色下拉框
	function fontColorSelectFunc(val,id){
		$("#"+id+"").css("background-color",val);
		$("#"+id+"").value = val;
	}
	
	//显示图源函数
	function showImgFunc(){
		if($('input[name="showImg"]:checked').length == 0){
			$("input[name=showNetwork]").attr("checked",false);
			$("input[name=showImgColor4G]").attr("checked",false);
			$("input[name=showImgColor5G]").attr("checked",false);
			$("input[name=networkType]").attr("checked",false);
		}else{
			$("input[name=showImgColor4G]")[0].checked = true;
			$("input[name=showImgColor5G]")[0].checked = true;
			$("input[name=networkType]")[1].checked = true;
		}
	}
	
	
	//显示标签函数
	function showTargetFunc(){
		if($('input[name="showTarget"]:checked').length == 0){
			$("input[name=showTargetOption]").each(function () {
			    $(this).prop('checked',false);
			});
		}
	}
	
	//显示小区连线样式函数
	function cellCoverThresholdFunc(){
		if($('input[name="cellCoverThreshold"]:checked').length == 0){
			$("input[name=cellLinkColor]").each(function () {
			    $(this).prop('checked',false);
			});
		}else{
			$("input[name=cellLinkColor]")[0].checked = true;
		}
	}
	
	//显示数量函数
	function showNumberFunc(){
		if($('input[name="showNumber"]:checked').length == 0){
			$("input[name=ncellLinkColor]").each(function () {
			    $(this).prop('checked',false);
			});
		}else{
			$("input[name=ncellLinkColor]")[0].checked = true;
		}
	}
	
	//根据id勾选复选框
	function showImgCheckedFunc(id){
		$("#"+id+"").prop("checked",true);
	}
	
	//初始化事件库
	function initEventTreeFunc(data){
		$('#eventTree').tree({
	        //url: ...,
	        lines:true,
	        onlyLeafCheck:true,
	        checkbox:true,
	        data:data,
	        loadFilter: function(data){
	    		if (data.d){
	    			return data.d;
	    		} else {
	    			return data;
	    		}
	        }
	    });
	}
	
	var eventTableColumns=[[
		{field:'eventRecSeqNo',checkbox:true},
		{field:'parentNodeId',hidden:true},
        {field:'eventName',align:'center',title:'事件名称',sortable:true},
        {field:'imgUrl',align:'center',title:' 图标  ',sortable:true,
        	formatter:function(value, row, index){
        		return "<div style='width:60px;'><img id='imgUrl"+ index +"' src='${pageContext.request.contextPath}/gis/images/"+value+".png' /></div>";
        		//return "<input class='imgCombobox' style='width:100px;'>";
        }}
    ]];
    
	var imgColumnId = "";
	
    //初始化指标表格
	function initEventTable(data){

		$("#eventTable").datagrid({
            // 表头
            columns:eventTableColumns,
            //url:'${pageContext.request.contextPath}/nasexceptionview/doPageListJson.action?',
            data: data,
            fitColumns:true,
            //填满区域
            fit:true,
            //奇偶变色
            striped:true,
            singleSelect:true,
            checkOnSelect:false,
            // 行号
            rownumbers:false,
            singleSelect:false,
            pagination:false,
            onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
            	if (data.rows.length > 0) {
	                for (var i = 0; i < data.rows.length; i++) {
	                    //根据operate让某些行不可选
	                    if (!data.rows[i].eventRecSeqNo) {
	                        $("input[name='eventRecSeqNo']")[i].disabled = true;
	                    }
	                }
	            }
	        },
	        onClickCell: function(index,field,value){
	        	
	        }
        });
	}
    
	//添加一个或多个事件	
	function addOneEventFunc(){
		var eventTreeData = $('#eventTree').tree('getChecked');
		if(eventTreeData.length == 0){
			//alert('请选择至少一行数据');
			$.messager.alert("系统提示", "请选择至少一行数据");
			return ;
		}
		for(var i = 0;i<eventTreeData.length;i++){
		    $('#eventTable').datagrid('appendRow',{
		    	eventRecSeqNo: true,
		    	eventName: eventTreeData[i].text,
		    	parentNodeId:$('#eventTree').tree('getParent',eventTreeData[i].target).id,
		    	imgUrl:eventTreeData[i].text
		    });
		    $('#eventTree').tree('remove',eventTreeData[i].target);
		}
	}
	
	//移除一个或多个事件
	function removeOneEventFunc(){
		var eventTableSelections = $('#eventTable').datagrid('getChecked');
		if(eventTableSelections.length == 0){
			//alert('请选择至少一行数据');
			$.messager.alert("系统提示", "请选择至少一行数据");
			return ;
		}
		for(var i = 0;i<eventTableSelections.length;i++){
			//删除一个列表列
			if(eventTableSelections[i].parentNodeId == 0){
				continue;
			}
			$('#eventTable').datagrid('deleteRow',$('#eventTable').datagrid('getRowIndex',eventTableSelections[i]));
			var parentNode = $('#eventTree').tree('find', eventTableSelections[i].parentNodeId);
			$('#eventTree').tree('append', {
				parent: parentNode.target,
					data: [{
						text: eventTableSelections[i].eventName,
						state: 'open'//设置open时为叶节点
					}]
			}); 
		}
	}
	
	//添加所有事件到列表
	function addAllEventFunc(){
		var eventTreeRootsData = $('#eventTree').tree('getRoots');
		for(var i = 0;i<eventTreeRootsData.length;i++){
			var eventTreeData = $('#eventTree').tree('getChildren',eventTreeRootsData[i].target);
			for(var j = 0;j<eventTreeData.length;j++){
			    $('#eventTable').datagrid('appendRow',{
			    	eventRecSeqNo: true,
			    	eventName: eventTreeData[j].text,
			    	parentNodeId:eventTreeRootsData[i].id,
			    	imgUrl:eventTreeData[j].text
			    });
			    $('#eventTree').tree('remove',eventTreeData[j].target);
			}
		}
	}
	
	//移除所有事件到列表
	function removeAllEventFunc(){
		$('#eventTable').datagrid('selectAll');
		var eventTableSelections = $('#eventTable').datagrid('getSelections');
		for(var i = 0;i<eventTableSelections.length;i++){
			//删除一个列表列
			if(eventTableSelections[i].parentNodeId == 0){
				continue;
			}
			$('#eventTable').datagrid('deleteRow',$('#eventTable').datagrid('getRowIndex',eventTableSelections[i]));
			var parentNode = $('#eventTree').tree('find', eventTableSelections[i].parentNodeId);
			$('#eventTree').tree('append', {
				parent: parentNode.target,
					data: [{
						text: eventTableSelections[i].eventName,
						state: 'open'//设置open时为叶节点
					}]
			}); 
		}
		$('#eventTable').datagrid('unselectAll');
	}
	
	function choosePci(val){
		var imgeColor4G = $("input[name=showImgColor4G]");
		var imgeColor5G = $("input[name=showImgColor5G]");
		var pcimo4 = $("input[name=pciMo4]");
		var pcimo3 = $("input[name=pciMo3]");
    	if(val == 4){
    		$("#unifyColor5G").attr("disabled", false);
			$("#unifyColor4G").attr("disabled", false);
    		$("#pciMo4RadioDiv").attr("style","display:none;");
			$("#pciMo3RadioDiv").attr("style","display:block;");
			$("#unify4GRadioDiv").attr("style","display:block;");
			$("#unify5GRadioDiv").attr("style","display:none;");
			$("#unifyRadio5G").checked = true;
			$("#unifyRadio4G").checked = false;
			
			for(var i=0;i<imgeColor4G.length;i++){
				imgeColor4G[i].disabled = false;
			}
			for(var i=0;i<imgeColor5G.length;i++){
				imgeColor5G[i].disabled = false;
			}
			for(var i=0;i<pcimo4.length;i++){
				pcimo4[i].disabled = false;
			}
			for(var i=0;i<pcimo3.length;i++){
				pcimo3[i].disabled = false;
			}
    	}else if(val == 5){
    		$("#unifyColor5G").attr("disabled", false);
			$("#unifyColor4G").attr("disabled", false);
    		$("#pciMo4RadioDiv").attr("style","display:block;");
			$("#pciMo3RadioDiv").attr("style","display:none;");
			$("#unify4GRadioDiv").attr("style","display:none;");
			$("#unify5GRadioDiv").attr("style","display:block;");
			$("#unifyRadio5G").checked = false;
			$("#unifyRadio4G").checked = true;
			
			
			for(var i=0;i<imgeColor4G.length;i++){
				imgeColor4G[i].disabled = false;
			}
			for(var i=0;i<imgeColor5G.length;i++){
				imgeColor5G[i].disabled = false;
			}
			for(var i=0;i<pcimo4.length;i++){
				pcimo4[i].disabled = false;
			}
			for(var i=0;i<pcimo3.length;i++){
				pcimo3[i].disabled = false;
			}
    	}else if(val == 45){
    		$("#unifyColor5G").attr("disabled", true);
			$("#unifyColor4G").attr("disabled", true);
			for(var i=0;i<imgeColor4G.length;i++){
				imgeColor4G[i].disabled = true;
			}
			for(var i=0;i<imgeColor5G.length;i++){
				imgeColor5G[i].disabled = true;
			}
			for(var i=0;i<pcimo4.length;i++){
				pcimo4[i].disabled = true;
			}
			for(var i=0;i<pcimo3.length;i++){
				pcimo3[i].disabled = true;
			}
    	}
	    showImgCheckedFunc('showImg');
	    mapIframe.window.cellLayerSelect = val;
    }
    
	
	//初始化小区模块  layerManageData
	function initCellTable(){
		//显示图源复选框
		if(layerManageData.cellData.showImg.checked){
			$('#showImg')[0].checked = layerManageData.cellData.showImg.checked;
		}else{
			$('#showImg')[0].checked = false;
		}
		//显示图源下单选框和颜色渲染
		//制式
		if(layerManageData.cellData.showImg.networkType == 5){
			$("#pciMo4RadioDiv").attr("style","display:block;");
			$("#pciMo3RadioDiv").attr("style","display:none;");
			$("#unify5GRadioDiv").attr("style","display:block;");
			$("#unify4GRadioDiv").attr("style","display:none;");
			$("#network5G")[0].checked = true;
			$("#network4G")[0].checked = false;
		}else if(layerManageData.cellData.showImg.networkType == 4){
			$('#network5G')[0].checked = false;
			$('#network4G')[0].checked = true;
			$("#unify5GRadioDiv").attr("style","display:none;");
			$("#unify4GRadioDiv").attr("style","display:block;");
			$("#pciMo4RadioDiv").attr("style","display:none;");
			$("#pciMo3RadioDiv").attr("style","display:block;");
		}else{
			$('#network5G')[0].checked = false;
			$('#network4G')[0].checked = false;
			$("#showNetwork")[0].checked = true;
			$("#pciMo4RadioDiv").attr("style","display:block;");
			$("#pciMo3RadioDiv").attr("style","display:none;");
			$("#unify5GRadioDiv").attr("style","display:block;");
			$("#unify4GRadioDiv").attr("style","display:none;");
			
			var imgeColor4G = $("input[name=showImgColor4G]");
			var imgeColor5G = $("input[name=showImgColor5G]");
			var pcimo4 = $("input[name=pciMo4]");
			var pcimo3 = $("input[name=pciMo3]");
			for(var i=0;i<imgeColor4G.length;i++){
				imgeColor4G[i].disabled = true;
			}
			for(var i=0;i<imgeColor5G.length;i++){
				imgeColor5G[i].disabled = true;
			}
			for(var i=0;i<pcimo4.length;i++){
				pcimo4[i].disabled = true;
			}
			for(var i=0;i<pcimo3.length;i++){
				pcimo3[i].disabled = true;
			}
			$("#unifyColor5G").attr("disabled", true);
			$("#unifyColor4G").attr("disabled", true);
		}
		
		if(layerManageData.cellData.showImg.unifyRadio5G.checked){
			$("#unifyRadio5G")[0].checked = true;
		}else{
			$("#unifyRadio5G")[0].checked = false;
		}
		
		if(layerManageData.cellData.showImg.unifyRadio4G.checked){
			$("#unifyRadio4G")[0].checked = true;
		}else{
			$("#unifyRadio4G")[0].checked = false;
		}
		
		/* //着色选择框
		if(layerManageData.cellData.showImg.unifyRadio.checked){
			$("#unifyRadio")[0].checked = true;
		}else{
			$('#unifyRadio')[0].checked = false;
		} */
		if(layerManageData.cellData.showImg.pciMo4Radio.checked){
			$("#pciMo4Radio")[0].checked = true;
		}else{
			$('#pciMo4Radio')[0].checked = false;
		}
		if(layerManageData.cellData.showImg.pciMo3Radio.checked){
			$("#pciMo3Radio")[0].checked = true;
		}else{
			$('#pciMo3Radio')[0].checked = false;
		}
		
		/* $("#unifyColor")[0].value = "#" + layerManageData.cellData.showImg.unifyRadio.unifyColor;//颜色 */
		$("#unifyColor5G")[0].value = "#" + layerManageData.cellData.showImg.unifyRadio5G.unifyColor;;
		$("#unifyColor4G")[0].value = "#" + layerManageData.cellData.showImg.unifyRadio4G.unifyColor;;
		
		//mo4颜色
		var mo4list = layerManageData.cellData.showImg.pciMo4Radio.pciMo4Color.split(",");
		for(var i = 0;i < mo4list.length;i++){
			$("input[name=pciMo4]")[i].value = "#" + mo4list[i];
		}
		//模3颜色
		var mo3list = layerManageData.cellData.showImg.pciMo3Radio.pciMo3Color.split(",");
		for(var i = 0;i < mo3list.length;i++){
			$("input[name=pciMo3]")[i].value = "#" + mo3list[i];
		}
		
		$("#cellRange")[0].value = layerManageData.cellData.showImg.cellRange;
		
		//初始化显示标签 showTarget  showTargetOption
		if(layerManageData.cellData.showTarget.checked){
			//$('#showImg')[0].checked = true;
			list = layerManageData.cellData.showTarget.showTargetOption.split(",");
			for(var i = 0;i < list.length;i++){
				for(var j = 0; j < $("input[name=showTargetOption]").length;j++){
					if(list[i] == $("input[name=showTargetOption]")[j].value){
						$("input[name=showTargetOption]")[j].checked = true;
					}
				}
			}
		}else{
			$('#showImg')[0].checked = false;
		}
	}
	
	//初始化连线模块
	function initLineTable(){
		$("#cellCoverThreshold")[0].checked = layerManageData.line.cellCoverThreshold.checked;
		//$("#cellCoverThresholdVal").val(layerManageData.line.cellCoverThreshold.cellCoverThresholdVal);
		$("#cellLinkColorInput")[0].value = "#" + layerManageData.line.cellCoverThreshold.cellLinkColor.cellLinkColorInput;
		for(var i = 0; i < $("input[name=cellLinkColor]").length;i++){
			if($("input[name=cellLinkColor]")[i].value == layerManageData.line.cellCoverThreshold.cellLinkColor.value){
				$("input[name=cellLinkColor]")[i].checked = true;
			}
		}
		
		$("#showNumber")[0].checked = layerManageData.line.showNumber.checked;
		$("#showNumberSelect")[0].value = layerManageData.line.showNumber.showNumberSelect;
		$("#ncellLinkColorInput")[0].value = "#" + layerManageData.line.showNumber.ncellLinkColor.ncellLinkColorInput;
		for(var i = 0; i < $("input[name=ncellLinkColor]").length;i++){
			if($("input[name=ncellLinkColor]")[i].value == layerManageData.line.showNumber.ncellLinkColor.value){
				$("input[name=ncellLinkColor]")[i].checked = true;
			}
		}
		
		//top
		for(var i = 0; i < $("input[name=topColor]").length;i++){
			$("input[name=topColor]")[i].value = "#" + layerManageData.line.showNumber.topColor["top" +(i+1)];
		}
	}
	
	//确定保存按钮
	function saveLayerManage(){
		
		//layerManageData.layerChooseData
		//保存图层
		saveLayerData();
		//保存图例
		saveConventionalSignsData();
		//保存小区
		saveCellData();
		
		//保存连线
		//saveLineData();
		//保存事件
		//saveEventData();
		
		showCellLayer();//是否显示小区图层
		var LayerManageShowEventData = [[]]
		//mapIframe.window.LayerManageShowEventTrackRender(LayerManageShowEventData);//绘制事件图层
		cancelLayerManage();
	}
	//保存图层数据
	function saveLayerData(){
		//保存之前修改的一行
       	if(editIndex != -1){
			var edX = $('#layerChooseTable').datagrid('getEditor', {index:editIndex,field:'deviationX'});
			if(edX != null){
				$('#layerChooseTable').datagrid('getRows')[editIndex]['deviationX'] = edX.target[0].value;
			}
			
			var edY = $('#layerChooseTable').datagrid('getEditor', {index:editIndex,field:'deviationY'});
			if(edY != null){
				$('#layerChooseTable').datagrid('getRows')[editIndex]['deviationY'] = edY.target[0].value;
			}
			$('#layerChooseTable').datagrid('endEdit', editIndex);
       	}
		layerManageData.layerChooseData = $("#layerChooseTable").datagrid('getData').rows;
	}
	
	//保存图列数据
	function saveConventionalSignsData(){
		
		mapIframe.window.pointZBChoose = $("#targetChoose").combobox('getValue');
		//显示范围
		conventionalSignsData.sampleScopeData.sampleScope = $('input[name=sampleScope]:checked').val();
		conventionalSignsData.sampletargetData.sampletarget = $('input[name=sampletarget]:checked').val();
			//指标
		var chooseVal = $("#targetChoose").combobox('getValue');
		for(var i=0;i < conventionalSignsData.targetChooseData.length;i++){
			if(conventionalSignsData.targetChooseData[i].value == chooseVal){
				conventionalSignsData.targetChooseData[i].selected = true;
			}else{
				conventionalSignsData.targetChooseData[i].selected = false;
			}
		}
		layerManageData.conventionalSignsData = conventionalSignsData;
		for(var i = 0;i < layerManageData.conventionalSignsData.targetFoatData.length;i++){
    		var targetData = layerManageData.conventionalSignsData.targetFoatData[i];
    		if(targetData.name == "测试数据"){
    			mapIframe.window.pointsFontColorSelected = targetData.fontColorSelected;
    			mapIframe.window.pointsFontSizeSelected = targetData.fontSizeSelected;
    		}
    		if(targetData.name == "LTE小区"){
    			mapIframe.window.LTEFontColorSelected = targetData.fontColorSelected;
    			mapIframe.window.LTEFontSizeSelected = targetData.fontSizeSelected;
    		}
    		if(targetData.name == "5G小区"){
    			mapIframe.window.fgFontColorSelected = targetData.fontColorSelected;
    			mapIframe.window.fgFontSizeSelected = targetData.fontSizeSelected;
    		}
    	}
	}
	
	//保存小区数据
	function saveCellData(){
		//显示图源
		layerManageData.cellData.showImg.checked = $('input[name=showImg]')[0].checked;
		layerManageData.cellData.showImg.unifyRadio4G.checked = $('input[name=showImgColor4G]')[0].checked;
		layerManageData.cellData.showImg.unifyRadio5G.checked = $('input[name=showImgColor5G]')[0].checked;
		layerManageData.cellData.showImg.unifyRadio4G.unifyColor = $("#unifyColor4G")[0].value.replace("#", "");
		layerManageData.cellData.showImg.unifyRadio5G.unifyColor = $("#unifyColor5G")[0].value.replace("#", "");
		layerManageData.cellData.showImg.pciMo4Radio.checked = $('input[name=showImgColor5G]')[1].checked;
		layerManageData.cellData.showImg.pciMo3Radio.checked = $('input[name=showImgColor4G]')[1].checked;
		//保存制式
		for(var i = 0;i < $('input[name=networkType]').length;i++){
			if($('input[name=networkType]')[i].checked){
				layerManageData.cellData.showImg.networkType = $('input[name=networkType]')[i].value;
			}
		}
		//保存pci模4
		var str4 = '';
		for(var i = 0;i < $('input[name=pciMo4]').length;i++){
			if(i != 0){
				str4 = str4 + ",";
			}
			str4 = str4 + $('input[name=pciMo4]')[i].value.replace("#", "");
		}
		layerManageData.cellData.showImg.pciMo4Radio.pciMo4Color = str4;
		//保存pci模3
		var str3 = '';
		for(var i = 0;i < $('input[name=pciMo3]').length;i++){
			if(i != 0){
				str3 = str3 + ",";
			}
			str3 = str3 + $('input[name=pciMo3]')[i].value.replace("#", "");
		}
		layerManageData.cellData.showImg.pciMo3Radio.pciMo3Color = str3;
		layerManageData.cellData.showImg.cellRange = $("#cellRange")[0].value;
		//显示标签
		layerManageData.cellData.showTarget.checked = $('input[name=showTarget]')[0].checked;
		var str2 = '';
		for(var i = 0;i < $('input[name=showTargetOption]').length;i++){
			if($('input[name=showTargetOption]')[i].checked){
				str2 = str2 + $('input[name=showTargetOption]')[i].value+",";
			}
		}
		str2 = str2.substring(0, str2.lastIndexOf(","));
		layerManageData.cellData.showTarget.showTargetOption = str2;
	}
	
	//保存连线数据
	function saveLineData(){
		//小区连线样式
		layerManageData.line.cellCoverThreshold.checked = $('input[name=cellCoverThreshold]')[0].checked;
		//layerManageData.line.cellCoverThreshold.cellCoverThresholdVal = $('#cellCoverThresholdVal').val();
		layerManageData.line.cellCoverThreshold.cellLinkColor.value = $('input[name=cellLinkColor]:checked').val();
		layerManageData.line.cellCoverThreshold.cellLinkColor.cellLinkColorInput = $('#cellLinkColorInput')[0].value.replace("#", "");
			//邻小区连线样式
		layerManageData.line.showNumber.checked = $('input[name=showNumber]')[0].checked;
		layerManageData.line.showNumber.showNumberSelect = $("#showNumberSelect")[0].value;
		layerManageData.line.showNumber.ncellLinkColor.value = $('input[name=ncellLinkColor]:checked').val();
		layerManageData.line.showNumber.ncellLinkColor.ncellLinkColorInput = $('#ncellLinkColorInput')[0].value.replace("#", "");
		for(var i = 0;i < $("input[name=topColor]").length;i++){
			layerManageData.line.showNumber.topColor['top' + (i+1)] = $("input[name=topColor]")[i].value.replace("#", "");
		}
	}
	
	//保存事件模块数据
	function saveEventData(){
		//保存树
		for(var i = 0;i < $('#eventTree').tree('getRoots').length;i++){
			var children = [];
			for(var j = 0;j < $('#eventTree').tree('getRoots')[i].children.length;j++){
				children.push({text:$('#eventTree').tree('getRoots')[i].children[j].text});
			}
			layerManageData.eventData.eventTreeData[i].children = children;
		}
			//保存表格
		layerManageData.eventData.eventTableData = $('#eventTable').datagrid('getData').rows;
	}
	
	//取消按钮
	function cancelLayerManage(){
		$('#layerManagerWin').window('close');
	}
	
	//展示测试数据(采样点)图层
	function ShowTrackRender(){
		//获取采样点数据
		var targetChoose = $("#targetChoose").combobox('getValue')
		for(var i = 0;i < mapIframe.window.$("#KpiIndexTool").find("option").length;i++){
			if(mapIframe.window.$("#KpiIndexTool").find("option")[i].value == targetChoose){
				mapIframe.window.$("#KpiIndexTool").find("option")[i].selected = true;
			}else{
				mapIframe.window.$("#KpiIndexTool").find("option")[i].selected = false;
			}
		}
		//采样点颜色
		var val;
/* 		if(targetChoose == 1){
    		val = 'rsrp';
    	}else if(targetChoose == 9){
    		val = 'rsrq';
    	}else if(targetChoose == 101){
    		val = 'ibler';
    		val = 'rsrp';
    		targetChoose = 1;
    	}else if(targetChoose == 102){
    		val = 'beam';
    		val = 'rsrq';
    		targetChoose = 9;
    	} */
    	
    	if(targetChoose == 109||targetChoose == 110){
    		val = 'rsrp';
    		targetChoose = 1;
    	}else if(targetChoose == 111||targetChoose == 112){
    		val = 'sinr';
    		targetChoose = 1;
    	}else if(targetChoose == 102){
    		val = 'beam';
    		targetChoose = 9;
    	}else if(targetChoose == 113){
    		val = 'macthrputdl';
    	}else if(targetChoose == 114){
    		val = 'macthrputul';
    	}else if(targetChoose == 115){
    		val = 'pci';
    	}
    	
		//采样点样式
		if($("#sampleTypeSelect").combobox('getValue')){
			for(var i = 0;i < mapIframe.window.$("#PointStyle").find("option").length;i++){
				if(mapIframe.window.$("#PointStyle").find("option")[i].value == $("#sampleTypeSelect").combobox('getValue')){
					mapIframe.window.$("#PointStyle").find("option")[i].selected = true;
				}else{
					mapIframe.window.$("#PointStyle").find("option")[i].selected = false;
				}
			}
		}else{
			mapIframe.window.$("#PointStyle").find("option:selected").val();
		}
		//偏移量
		var deviationX = 0;
		var deviationY = 0;
		for(var i = 0; i < layerManageData.layerChooseData.length;i++){
			if(layerManageData.layerChooseData[i].name == '测试数据'){
				deviationX = layerManageData.layerChooseData[i].deviationX;
				deviationY = layerManageData.layerChooseData[i].deviationY;
			}
		}
		mapIframe.window.againGPSTrackRender(targetChoose,val,$('input[name=sampleScope]:checked').val(),deviationX,deviationY);
	}
	
	//是否显示小区图层
	function showCellLayer(){
		var cellShowFlag = null;
		var showTargetFlag = true;
		/* for(var i = 0;i < layerManageData.layerChooseData.length;i++){
			if(layerManageData.layerChooseData[i].name == '5G小区'){
				cellShowFlag = layerManageData.layerChooseData[i].recSeqNo;
				showTargetFlag = layerManageData.layerChooseData[i].mark;
			}
		} */
		if(layerManageData.cellData.showImg.checked){
			cellShowFlag = layerManageData.cellData.showImg.networkType;
		}
		var colors={};
		var colors4g = [];
		var colors5g = [];
		if(layerManageData.cellData.showImg.unifyRadio4G.checked){
			var color = layerManageData.cellData.showImg.unifyRadio4G.unifyColor;
			colors4g = ['#' + color,'#' + color,'#' + color,'#' + color];
		}else if(layerManageData.cellData.showImg.pciMo3Radio.checked == true){
			var colorList = layerManageData.cellData.showImg.pciMo3Radio.pciMo3Color.split(',');
			for(var i = 0;i < colorList.length;i++){
				colors4g.push('#' + colorList[i]);
			}
			colors4g.push('#' + colorList[0]);
		}
		colors["colors4g"] = colors4g;
		if(layerManageData.cellData.showImg.unifyRadio5G.checked){
			var color = layerManageData.cellData.showImg.unifyRadio5G.unifyColor;
			colors5g = ['#' + color,'#' + color,'#' + color,'#' + color];
		}else if(layerManageData.cellData.showImg.pciMo4Radio.checked == true){
			var colorList = layerManageData.cellData.showImg.pciMo4Radio.pciMo4Color.split(',');
			for(var i = 0;i < colorList.length;i++){
				colors5g.push('#' + colorList[i]);
			}
		}
		colors["colors5g"] = colors5g;
		
		var cellRange = layerManageData.cellData.showImg.cellRange;
		mapIframe.window.ReportLayerManageAddCell(cellShowFlag,colors,cellRange,layerManageData.cellData.showTarget.showTargetOption,showTargetFlag);
	}
	
	//连线模块
	function showLine(responses){
		//showCellTrackLine(responses);
		ShowCellToCell();
	}
	//小区采样点之间连线
	function showCellTrackLine(responses){
		var data = JSON.parse(JSON.stringify(responses));
		if(layerManageData.line.cellCoverThreshold.checked){//连线
			if(layerManageData.line.cellCoverThreshold.cellLinkColor.value == 1){//使用相同颜色
				for(var i = 0;i < data[1].colors.length;i++){
					data[1].colors[i].color = '#'+layerManageData.line.cellCoverThreshold.cellLinkColor.cellLinkColorInput;
				}
			}
			/* if(layerManageData.line.cellCoverThreshold.cellCoverThresholdVal){
				var ThresholdVal = parseFloat(layerManageData.line.cellCoverThreshold.cellCoverThresholdVal);
				for(var i = 0; i < responses[0].length;i++){
    	    		for(var j = 0; j < responses[0][i][1].length;j++){
						var indexValue = parseFloat(responses[0][i][1][j].indexValue);
    	    			if(indexValue < ThresholdVal){
    	    				data[0][i][1][j] = null;
    	    			}
    	    		}
    	    	}
			} */
			mapIframe.window.ShowCellLineCon(data);
		}else{//不连线
		}
	}
	
	//小区与小区连线
	function ShowCellToCell(){
		if($('#showNumber')[0].checked){
			var showNumberSelect = $('#showNumberSelect').val();
			//根据showNumber去后台获取数据
			/*
			ajax:
			*/
			var ncellLinkColor = $('input[name=ncellLinkColor]:checked')[0].value;
			var colors = [];
			if(ncellLinkColor == 1){
				var color = $('#ncellLinkColorInput').val();
				colors = [color,color,color,color,color,color];
			}else{
				for(var i = 0;i < $('input[name=topColor]').length;i++){
					colors.push($('input[name=topColor]')[i].value);
				}
			}
			var netType = layerManageData.cellData.showImg.networkType;
			mapIframe.window.LayerManageShowCellToCell(colors,netType);
		}
	}
	
	//图例(采样点)负责显示
	function showRoadPointsDataFunc(){
		var flag = true;//是否展示图层
    	var Showflag = false;//是否展示标签
    	if(layerManageData.layerChooseData){
			for(var i = 0;i < layerManageData.layerChooseData.length;i++){
				if(layerManageData.layerChooseData[i].name == '测试数据'){
					flag = layerManageData.layerChooseData[i].recSeqNo;
					Showflag = layerManageData.layerChooseData[i].mark;
				}
			}
    	}
    	if(Showflag){
    		mapIframe.window.gpsShowTarget = layerManageData.conventionalSignsData.sampletargetData.sampletarget;
    	}else{
    		mapIframe.window.gpsShowTarget = null;
    	}
    	//波束范围
    	var boshuScope = $("#boshuScope").val();
    	var boshuChoose = $('input[name=boshuChoose]:checked').val();
    	if(boshuScope){
    		if(showRoadPointsData){
    	    	var data = JSON.parse(JSON.stringify(showRoadPointsData));
    	    	for(var i = 0; i < showRoadPointsData[0].length;i++){
    	    		for(var j = 0; j < showRoadPointsData[0][i][1].length;j++){
    	    			if(boshuChoose == 1){
	    	    			if(showRoadPointsData[0][i][1][j].beamId != boshuScope){
	    	    				data[0][i][1][j] = null;
	    	    			}
    	    			}else{
    	    				if(showRoadPointsData[0][i][1][j].beamId != boshuScope && showRoadPointsData[0][i][1][j].nbeamId != boshuScope){
	    	    				data[0][i][1][j] = null;
	    	    			}
    	    			}
    	    		}
    	    	}
    	    	mapIframe.window.ShowGPSTrackRender5g(data);
        	}
    	}else{
    		mapIframe.window.ShowGPSTrackRender5g(showRoadPointsData);
    	}
    	
    	mapIframe.window.GPSTrackGraphicsLayer.setVisibility(flag);
    	showLine(showRoadPointsData);
	}
	
   	//获取地图图例url
   	function getParamColorUrl(){
   		var cityName = getCityName();
   		return "${pageContext.request.contextPath}/mapTrailLegendParam/displayLegnd.action?cityName="+cityName;
   	}
   	
   	//保存地图图例url
   	function getSaveMapLegendUrl(){
   		return "${pageContext.request.contextPath}/mapTrailLegendParam/saveMapLegend.action";
   	}
</script>