<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  <head>
    <title>统计任务</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
		$(function(){
			initTable();
		});
		function initTable(){
			$("#mainTable").datagrid({
				// 表头
				columns:[[
					{field:'id',width:20,align:'center',checkbox:true},
					{field:'createrName',width:60,align:'center',title:'创建人',formatter:showTooltip},
					{field:'name',width:80,align:'center',title:'任务名称',formatter:showTooltip},
					//{field:'terminalGroup',width:20,align:'center',title:'域',formatter:showTooltip},
					{field:'creatDate',width:80,align:'center',title:'创建时间',formatter:showTooltip},
					{field:'nam',width:80,align:'center',title:'操作',
						formatter:function(value,row,index){
							if(row.taskStatus == 2 || row.taskStatus == 3 || row.taskStatus == null || row.taskStatus == -1 ){
								return '<a href="#" title="报表下载" onclick="batchDownload('+row.id+');" >报表下载</a>';
							}else{
								return '';
								//	return '<a style="opacity: 0.2" href="javascript:return false;" title="查看详情" onclick="return false;" >查看详情</a>';
							}
							//return '<a href="#" title="查看详情" onclick="seeInfo('+row.id+');" >查看详情</a>';
						}
					},
					{field:'taskStatus',width:75,align:'center',title:'预统计状态',
						formatter: function(value,row,index){
							if(0==value||1==value){
								return '统计中';
							}else if(2==value || null==value){
								return '未查看';
							}else if(3==value){
								return '已查看';
							}else{
								return '统计失败';
							}
							return value;
						},
						styler: function(value,row,index){
							if(2!=value && 3!=value && null!=value){
								return 'background-color:#34aaf1;';
							}
						}
					}
/* 					{field:'na',width:40,align:'center',title:'移动',
						formatter:function(value,row,index){
							return '<a href="#" title="下载" onclick="alertAttention();" >移动</a>';
						}
					}, 
					{field:'n',width:40,align:'center',title:'电信',
						formatter:function(value,row,index){
							return '<a href="#" title="下载" onclick="alertAttention();" >电信</a>';
						}
					}, 
					{field:'am',width:40,align:'center',title:'联通',
						formatter:function(value,row,index){
							return '<a href="#" title="下载" onclick="alertAttention();" >联通</a>';
						}
					} */
				]],
				url:'${pageContext.request.contextPath}/customeLogReport/doPageListJson.action',
				title:'统计任务列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				pagination:true,
				pageSize:20,
				loadMsg:0,
				pageList:[10,20,50,100,200,500,1000],
				scrollbarSize:0,
				checkOnSelect:false,
				toolbar:'#tb',
				onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
		            if (data.rows.length > 0) {
		            	var checkAllDisable = false;
		                //循环判断操作为新增的不能选择
		                for (var i = 0; i < data.rows.length; i++) {
		                    //根据operate让某些行不可选
		                    if (2 != data.rows[i].taskStatus && 3!= data.rows[i].taskStatus && null != data.rows[i].taskStatus) {
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
		        }
				
			});
			
		}
		/*查看详情*/
		function seeInfo(id){
			parent.addTab(null,"${pageContext.request.contextPath}/customeLogReport/goSee.action?idLong="+id,"报表统计",null);
			//goToPage("${pageContext.request.contextPath}/customeLogReport/seeInfo.action?idLong="+id+"&dPage=0");
		}

		function batchDownload(id){

			window.open("${pageContext.request.contextPath}/customeLogReport/batchDownloadExcel.action?idLong="+id);
			//goToPage("${pageContext.request.contextPath}/customeLogReport/seeInfo.action?idLong="+id+"&dPage=0");
		}

		/* 删除任务*/
		function delReport(){
			var row = $('#mainTable').datagrid('getChecked');
			if (row.length!=0) {
				$.messager.confirm("系统提示", "确定要删除选中任务？", function(r) {
					if (r) {
						var idss=new Array();
						for(i=0;i<row.length;i++){
							idss.push(row[i].id);
						}
						var idsString = idss.join(",");
						$.post('${pageContext.request.contextPath}/customeLogReport/delReport.action', {ids : idsString}, 
							function(result) {
								if (result.errorMsg) {
									$.messager.alert("系统提示", result.errorMsg,'error');
								} else {
									$.messager.alert("系统提示", "已成功删除选中任务记录!");
									$("#mainTable").datagrid("reload");
								}
							},
						'json');
					}
				});
			}else{
				$.messager.alert("系统提示", "请选择某个任务!");
			}
		}
		/* 多条件查询 */
		function query(){
			if($("#queryForm").form('validate')){
				$("#mainTable").datagrid('load',{
					createrName:$("#createrName").textbox('getValue'),
					name:$("#name").textbox('getValue'),
					beginDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue'),
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
		
		function add(){
			goToPage('${pageContext.request.contextPath}/customeLogReport/goAdd.action?dPage=0');
		}
	</script>
  </head>
  
  <body  style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	
    	<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">
	 
	    		<!-- 数据列表 -->
			<div  data-options="region:'center',border:false" >
				<table id="mainTable" ></table>
			
		 	</div>
		 	
	    	<div style="width:100%;height:10%;" data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
		    		//收束的时候显示title
		    		var title = $('#ly').layout('panel','north').panel('options').title;
		    		$('.layout-expand-north .panel-title').html(title);
		    	}" style="height:160px;overflow-y:auto;">
	    			<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
				    	<div class="inputDivShow">创建人
				    		<input id="createrName" name="createrName"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
				    	</div>
				    	<div class="inputDivShow">任务名称
				    		<input id="name" name="name"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
				    	</div>
				    	<div class="inputDivShow">创建起始时间
				    		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:false,editable:false" />
				    	</div>
				    	<div class="inputDivShow">创建截至时间
				    		<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="required:false,editable:false" />
				    	</div>
				    	
						<td width="50%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;margin-left: 5px;" data-options="iconCls:'icon-search'" >查找</a></td>
					    
					   	<td width="50%" align="left"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;margin-left: 5px;" data-options="iconCls:'icon-reload'" >重置</a></td>
			
			    	</form>
   			</div>
    		<div id="tb">
    			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-add',plain:true" onclick="add();">新增</a>
				<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-remove',plain:true" onclick="delReport();">删除</a>
			</div>
    	</div>
    	
  </body>
</html>
