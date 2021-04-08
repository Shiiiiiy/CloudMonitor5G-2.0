<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  <head>
    <title>自定义报表上传日志列表页面</title>

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
		var testLogInfoColumns=[[
			{field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th> 
			{field:'fileName',width:150,align:'center',title:'文件名'},
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
			}
		]];
		$(function(){
			initTable();
		});
		function initTable(){
			$("#testLogInfoTable").datagrid({
				// 表头
				columns:testLogInfoColumns,
				url:'${pageContext.request.contextPath}/customeUploadTestLog/doPageListJson.action',
				title:'测试日志列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				pagination:true,
				pageSize:20,
				pageList:[10,20,50,100,200,500,1000],
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
		        onClickRow: function(rowIndex, rowData){
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
				$("#testLogInfoTable").datagrid('load',{
					fileName:$("#fileName").textbox('getValue'),
					testFileStatus:$("#testFileStatus").combobox('getValue')
				});
			}
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
		}
		
		//校验详单下载
		function verifacateDeatailDown(){
			var row = $('#testLogInfoTable').datagrid('getChecked');
			if (row.length!=0) {
				var ids=new Array();
				for(var i=0;i<row.length;i++){
					ids.push(row[i].id);
				}
				var idsString = ids.join(",");
				window.location= "${pageContext.request.contextPath}/customeUploadTestLog/downloadVerifacateDeatail.action?testLogItemIds="+idsString;
			}else{
				$.messager.alert("系统提示", "请选择某个任务!");
			}
		}
	
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
			    	<div class="inputDivShow">文件名
			    		<input id="fileName" name="fileName"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
			    	</div>
			    	<div class="inputDivShow">预统计状态
			    		<select id="testFileStatus" name="testFileStatus"  class="easyui-combobox" data-options="editable:false,multiple:false" >
			    			<option value="">&nbsp;</option>
			    			<option value="0">上传中</option>
			    			<option value="1">统计中</option>
							<option value="2">已完成</option>
			    		</select>
			    	</div>
			    	<table width="100%">
			    		<tr>
				    		<td width="47%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a></td>
				    		<td width="6%" align="center"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a></td>
			    			<td width="47%" align="left"><a class="easyui-linkbutton" onclick="verifacateDeatailDown();" style="width: 80px;margin-left: 5px;" data-options="iconCls:'icon-download'" >检验详单</a></td>
			    		</tr>
			    	</table>
		    	</form>
   			</div>
    	</div>
  </body>
</html>
