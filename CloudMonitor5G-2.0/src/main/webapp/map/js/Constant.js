/*地图参数配置 */
var img_url="http://t4.tianditu.com/DataServer?T=vec_w&x={x}&y={y}&l={z}&random_id="+Math.random()+"&tk=a070cfb878381b191e96a47ef96ef5e1";
var label_url="http://t3.tianditu.com/DataServer?T=cva_w&x={x}&y={y}&l={z}&random_id="+Math.random()+"&tk=a070cfb878381b191e96a47ef96ef5e1";
var initBounds=[120.3882153, 30.7677578, 122.5854809, 31.6942464]            //边界大小

/* geoserver配置 */
var transCoor = false;                                             //坐标偏移-添加离线地图URL需要修改为 true
var gd_url='';                                                     //高德离线地图URL   http://172.30.100.183:8080/mapTiles/{z}/{x}/{y}.png
var yewu_url='http://172.30.4.191:8080/geoserver/egcgeoserver/wfs'  //geoserver中业务服务
var wms_url='http://172.30.4.191:8080/geoserver/egcgeoserver/wms'   //geoserver中wms服务
var dataBaseName='egcgeoserver'              //工作空间名称
var baseLayer = 'quanguo_polyline'    //道路图层名
var logLayer = 'iads_cucc_playbackinfo'                     //日志数据图层名

/* //本机测试
var yewu_url='http://127.0.0.1:8080/geoserver/baseMap/wfs'  //geoserver中业务服务
var wms_url='http://127.0.0.1:8080/geoserver/baseMap/wms'   //geoserver中wms服务
var dataBaseName='baseMap'              //工作空间名称
var baseLayer = 'city_polyline_Clip'    //道路图层名 */

