dojo.require("dojo.parser");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.layout.AccordionContainer");
dojo.require("dijit.layout.AccordionPane");
dojo.require("esri.layers.FeatureLayer");
dojo.require("esri.toolbars.navigation");
dojo.require("dojo.fx.easing");
dojo.require("esri.dijit.TimeSlider");
dojo.require("esri.dijit.Measurement");
dojo.require("esri.tasks.GeometryService");
dojo.require("esri.dijit.OverviewMap");
dojo.require("esri.tasks.find");
dojo.require("esri.geometry.Point");
dojo.require("esri.tasks.RouteTask");
dojo.require("esri.graphic");
dojo.require("esri.tasks.geometry");
dojo.require("esri.tasks.RouteParameters");
dojo.require("esri.tasks.FeatureSet");
dojo.require("esri.tasks.DistanceParameters");
dojo.require("extras.GoogleMapLayer");
var map;
var navToolbar;
var navOption;
var layer;
var layer_ditu;
var layer_gongdan;
var layer_image;
var featureLayer;
//点线面符号样式
var pointSymbol;
var lineSymbol;
var markerSymbol,polylineSymbol,font;
var polygonSymbol;
//模糊查询
var findTask;
var findParams;
var yewu_findTask;
var yewu_findParams;
var RouteTask;
var routeParameters;
var geometryService;
dojo.addOnLoad(init);
function init(){
    //var labels=[];
    var basemap = new GoogleMapLayer();
    //basemap.on("load",function(){
    //    var lods = layer_ditu.tileInfo.lods;
    //    for ( var i = 0, il = lods.length; i < il; i++ ) {
    //        if ( i % 2 ) {
    //            labels.push(parseInt(lods[i].scale.toFixed()));
    //        }
    //    }
    //});
    map = new esri.Map("map",
        { logo: false
        });

   // var layerAnoo = new GoogleAnooLayer();
    map.addLayer(basemap);
    markerSymbol = new esri.symbol.SimpleMarkerSymbol(esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE,6, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([204, 102, 51]), 1), new dojo.Color([158, 184, 71, 0.65]));
    polylineSymbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([204, 102, 51]),1);
    font = new esri.symbol.Font("18px", esri.symbol.Font.STYLE_NORMAL, esri.symbol.Font.VARIANT_NORMAL, esri.symbol.Font.WEIGHT_BOLDER);

    // map.addLayer(layerAnoo);
     setupNavBar();
}
function setupNavBar() {
    navToolbar = new esri.toolbars.Navigation(map);
    dojo.query(".navItem").onclick(function (evt) {
        navEvent(evt.target.id);
    });
}
function navEvent(id) {
    switch (id) {
        case 'Pan':
        {//漫游
            navToolbar.deactivate();
            map.enablePan();
            navToolbar.activate(esri.toolbars.Navigation.PAN);
            navOption = id;
            break;
        }
        case 'PrevExtent':
        {//前一视图
            navToolbar.zoomToPrevExtent();
            break;
        }
        case 'NextExtent':
        {//后一视图
                navToolbar.zoomToNextExtent();
            break;
        }
        case 'FullExtent':
            navToolbar.zoomToFullExtent();
            break;
        case 'zoom_in':
        {//放大
            navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);
            if (navOption) {
                dojo.anim(dojo.byId(navOption), {
                    backgroundColor: '#FFFFFF'
                });
            }
            navOption = id;
            break;
        }
        case 'zoom_out':
        {//缩小
            navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);
            if (navOption) {
                dojo.anim(dojo.byId(navOption), {
                    backgroundColor: '#FFFFFF'
                });
            }
            navOption = id;
            break;
        }
        case 'select':
            navToolbar.deactivate();
            tb.activate(esri.toolbars.Draw.EXTENT);
            break;
        case 'clear':
            mapclear();
            break;
        case 'deactivate':
            navToolbar.deactivate();
            if (navOption) {
                dojo.anim(dojo.byId(navOption), {
                    backgroundColor: '#FFFFFF'
                });
            }
            navOption = id;
            break;
        case 'DistanceMeasure':
            DistanceByTwopoints();
            break;
        case '加点':
            AddpointSymbol();
            break;
        case '聚类':
            addClusters();
            break;
        case '图层控制':
            var $a=$("#Layer_control");
            if($a.is(":hidden")){
                $a.css("display","block");
            }
            else{
                $a.css("display","none");
            }
            break;
        case '时间条':
            var $b=$("#slider");
            if($b.is(":hidden")){
                $b.css("display","block");
                SetOpacity();
            }
            else{
                $b.css("display","none");
            }
            break;
        case 'GPS轨迹':
            DisplayGpsHistory();
            break;
        case '小区绘制':
            GetCell();
            //MyDialog=new dijit.Dialog({
            //    title:"测试",
            //    content:"测试",
            //    style:"height:auto;width:200px;"
            //});
            //domStyle.set(MyDialog.containerNode, {
            //    position: 'relative'});
            //MyDialog.show();
            break;
        case '指标选择':
            //读取指标
            var $ab= $("#dropDownButtonContainer");
            if($ab.is(":hidden")){
                $ab.css("display","block");
                var myDialog = new dijit.TooltipDialog({
                    content:
                    '<label for="select_index">选择指标:</label>'+
                    '<select   data-dojo-type="dijit/form/Select"  id="select_index" name="name">'+
                    '<option value="MOS值">MOS值</option><option value="LTE网络RSRP" selected="selected">LTE网络RSRP</option></select><br>' +
                    '<button data-dojo-type="dijit/form/Button" type="submit">刷新</button>'+
                    '<button data-dojo-type="dijit/form/Button" type="button">清除</button>'
                });
                var myButton = new dijit.form.DropDownButton({
                    label: "地图显示指标",
                    dropDown: myDialog
                });
                dojo.byId("dropDownButtonContainer").remove();
                var myDiv= document.createElement('div');
                myDiv.setAttribute("id", "dropDownButtonContainer");
                dojo.byId("MapTool").appendChild(myDiv);
                dojo.byId("dropDownButtonContainer").appendChild(myButton.domNode);
                myButton.startup();
            }
            else{
                $ab.css("display","none");
            }
            break;
    }
}
//两点距离测算
var handle;
var handle_double;
var textSymbol;
function DistanceByTwopoints() {
    map.setMapCursor("url(images/distance.cur),auto");
    handle = dojo.connect(map, "onClick", mapClickHandler);
    handle_double=dojo.connect(map,"onDblClick",mapDoubleClickHandler);
}
var inputPoints = [];
var endGraphic; var totalDistance = 0;
function mapDoubleClickHandler(evt)
{
    map.setMapCursor("default");
    var inPoint = new esri.geometry.Point(evt.mapPoint.x, evt.mapPoint.y, map.spatialReference);
    inputPoints.push(inPoint);
    if(inputPoints.length>=2){
        var distParams = new esri.tasks.DistanceParameters();
        distParams.distanceUnit = esri.tasks.GeometryService.UNIT_METER;
        distParams.geometry1 = inputPoints[inputPoints.length - 2];
        distParams.geometry2 = inputPoints[inputPoints.length - 1];
        distParams.geodesic = true;
        var geometryService = new esri.tasks.GeometryService(geometry_url);
        geometryService.distance(distParams, function (distance) {
            if (isNaN(distance)) {
                distance = 0;
            }
            var s=dojo.number.format(distance, {
                places: 2
            });
            if(distance>1000)
            {
                s=dojo.number.format(distance/1000, {
                        places: 2
                    })+'km';
            }
            else
            {
                s=s+'m';
            }
            textSymbol = new esri.symbol.TextSymbol(s, font, new dojo.Color([204, 102, 51]));
            totalDistance += distance;
            //dojo.byId('distanceDetails').innerHTML = content;
        });
        //draw a polyline to connect the input points
        var polyline = new esri.geometry.Polyline(map.spatialReference);
        polyline.addPath([distParams.geometry1, distParams.geometry2]);
        map.graphics.add(new esri.Graphic(polyline, polylineSymbol));
        var total_dis;
        if(totalDistance>1000){
            total_dis=dojo.number.format(totalDistance/1000, {
                    places: 2
                })+"km";
        }
        else{
            total_dis=dojo.number.format(totalDistance, {
                    places: 2
                })+"m";
        }
        textSymbol = new esri.symbol.TextSymbol("总长:"+total_dis, font, new dojo.Color([204, 102, 51]));
        textSymbol.yoffset = 18;
        textSymbol.xoffset=20;
        map.graphics.add(new esri.Graphic(evt.mapPoint, textSymbol));
        map.graphics.add(new esri.Graphic(evt.mapPoint, markerSymbol));
        dojo.disconnect(handle);
        dojo.disconnect(handle_double);
        inputPoints=[];
    }
}
function mapClickHandler(evt) {
    var inPoint = new esri.geometry.Point(evt.mapPoint.x, evt.mapPoint.y, map.spatialReference);
    inputPoints.push(inPoint);
    //define the symbology for the graphics
    if (inputPoints.length === 1) { //start location label
        textSymbol = new esri.symbol.TextSymbol("起点", font, new dojo.Color([204, 102, 51]));
        textSymbol.yoffset =18;
        textSymbol.xoffset=10;
        map.graphics.add(new esri.Graphic(evt.mapPoint, textSymbol));
    }
    if (inputPoints.length >= 2) { //end location label
        var distParams = new esri.tasks.DistanceParameters();
        distParams.distanceUnit = esri.tasks.GeometryService.UNIT_METER;
        distParams.geometry1 = inputPoints[inputPoints.length - 2];
        distParams.geometry2 = inputPoints[inputPoints.length - 1];
        distParams.geodesic = true;
        //draw a polyline to connect the input points
        var polyline = new esri.geometry.Polyline(map.spatialReference);
        polyline.addPath([distParams.geometry1, distParams.geometry2]);
        map.graphics.add(new esri.Graphic(polyline, polylineSymbol));
        var geometryService = new esri.tasks.GeometryService(geometry_url);
        geometryService.distance(distParams, function (distance) {
            if (isNaN(distance)) {
                distance = 0;
            }
            var s=dojo.number.format(distance, {
                places: 2
            });
            if(distance>1000)
            {
                s=dojo.number.format(distance/1000, {
                        places: 2
                    })+'km';
            }
            else
            {
                s=s+'m';
            }
            textSymbol = new esri.symbol.TextSymbol(s, font, new dojo.Color([204, 102, 51]));
            totalDistance += distance;
            var content = "";
            textSymbol.yoffset = 8;
            textSymbol.xoffset = 8;
            endGraphic=new esri.Graphic(evt.mapPoint, textSymbol);
            map.graphics.add(endGraphic);
            //dojo.byId('distanceDetails').innerHTML = content;
        });

    }
    //add a graphic for the clicked location
    map.graphics.add(new esri.Graphic(evt.mapPoint, markerSymbol));
}