/*
地图服务地址
 */
var diTu_url = "http://172.30.4.136:6080/arcgis/rest/services/ditu/MapServer";
var yewu_url = "http://172.30.4.136:6080/arcgis/rest/services/yewu/MapServer";
var geometry_url = "http://172.30.4.136:6080/arcgis/rest/services/Utilities/Geometry/GeometryServer";
var printTools_url  = "http://172.30.4.136:6080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task";

/* 地图服务地址 */
var WorkspaceIDpath = "D:\\zbf";
var WorkspaceID = "MyShapefileWorkspaceID";
var sqlcom = "select * from ltecell";
//var connectionstring = "Data Source=172.30.4.136:1521/iads;User Id=scott;Password=tiger";
// 小区与栅格图层
/*
 地图工具栏 
 */
var navToolbar;
var navOption;
/*
 控制界面
 */
/*
MOD_TAC颜色
*/
var TacColor=["#EB0000","#E66700","#EBD200","#00AC68","#1FC8C5","#1A8AD1","#0049B3","#7B2B95","#D11A95","#FC5474"];