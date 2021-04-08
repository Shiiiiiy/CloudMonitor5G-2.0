var map;
var xmin = $.cookie("GIS_xmin");
var ymin = $.cookie("GIS_ymin");
var xmax = $.cookie("GIS_xmax");
var ymax = $.cookie("GIS_ymax");
var LodsLength;
//ҵ������
var layer;
// ���ߵ�ͼ����
var layer_diTu;//���ߵ�ͼ
var layer_online_diTu;//��ͼ
var layer_online_annotation;//ע��
var layer_online_img;//Ӱ��
var clusterLayer;
var  anoCtrl;var gs;
require([
    "dojo/parser",
    "dojo/ready",
    "dojo/_base/array",
    "esri/Color",
    "dojo/dom-style",
    "dojo/query",
    "esri/map",
    "esri/request",
    "esri/graphic",
    "esri/geometry/Extent",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/SimpleFillSymbol",
    "esri/symbols/PictureMarkerSymbol",
    "esri/renderers/ClassBreaksRenderer",
    "esri/layers/GraphicsLayer",
    "esri/SpatialReference",
    "esri/dijit/PopupTemplate",
    "esri/geometry/Point",
    "esri/geometry/webMercatorUtils",
    "extras/ClusterLayer",
    "esri/layers/FeatureLayer",
    "dijit/layout/BorderContainer",
    "dijit/layout/ContentPane",
    "esri/toolbars/navigation",
    "dojo/number",
    "esri/tasks/GeometryService",
    "esri/tasks/DistanceParameters",
    "esri/renderers/SimpleRenderer",
    "esri/InfoTemplate",
    "esri/dijit/Legend",
    "esri/symbols/SimpleLineSymbol",
    "esri/layers/WFSLayer",
    "extras/BDAnoLayer","extras/BDimgLayer","extras/BDVecLayer",
    "dijit/form/NumberTextBox",
    "dojo/domReady!"
], function(
    parser, ready, arrayUtils, Color, domStyle, query,
    Map, esriRequest, Graphic, Extent,
    SimpleMarkerSymbol, SimpleFillSymbol, PictureMarkerSymbol, ClassBreaksRenderer,
    GraphicsLayer, SpatialReference, PopupTemplate, Point, webMercatorUtils,
    ClusterLayer,SimpleRenderer,InfoTemplate,Legend
) {
    ready(function() {
        parser.parse();
        var popupOptions = {
            "markerSymbol": new SimpleMarkerSymbol("circle", 20, null, new Color([0, 0, 0, 0.25])),
            "marginLeft": "20",
            "marginTop": "20"
        };
        var labels = [],OnlineLabels=[];
        gs = new esri.tasks.GeometryService(geometry_url);
        //���ߵ�ͼ
        layer_diTu = new esri.layers.ArcGISTiledMapServiceLayer(diTu_url);
        layer_diTu.on("load",function(){
            var lods = layer_diTu.tileInfo.lods;
            LodsLength=lods.length;
            for ( var i = 0, il = LodsLength; i < il; i++ ) {
                if ( i % 2 ) {
                    labels.push(parseInt(lods[i].scale.toFixed()));
                }
            }
        });
        //���ߵ�ͼ
        //�������ߵ�ͼ
        if(xmin!=null){
            var extent = new esri.geometry.Extent({ "xmin": parseFloat(xmin), "ymin": parseFloat(ymin), "xmax": parseFloat(xmax), "ymax": parseFloat(ymax) });
            map = new Map("map",
                { logo: false,
                    extent: extent,
                    sliderPosition: "bottom-right",
                    sliderStyle: "large",
                    sliderLabels: labels,
                    showLabels : true
                });
        }
        else{
            map = new Map("map",
                { logo: false,
                    sliderPosition: "bottom-right",
                    sliderStyle: "large",
                    sliderLabels: labels,
                    showLabels : true
                });
        }
        map.addLayer(layer_diTu);
        //map.addLayers([layer_online_diTu,layer_online_img,layer_diTu,layer_online_annotation]);
        map.on("extent-change", function () {
            $.cookie("GIS_xmin", map.extent.xmin, { expires: 365 });
            $.cookie("GIS_ymin", map.extent.ymin, { expires: 365 });
            $.cookie("GIS_xmax", map.extent.xmax, { expires: 365 });
            $.cookie("GIS_ymax", map.extent.ymax, { expires: 365 });
        });

        showMap= function(layer){
            //���ð�ť��ʽ
            layer_online_annotation=new GoogleAnooLayer();
            layer_online_diTu=new GoogleMapLayer();
            //if(layer_online_diTu){
            //
            //    var lods = layer_online_diTu.tileInfo.lods;
            //    for ( var i = 0, il = lods.length; i < il; i++ ) {
            //        if ( i % 2 ) {
            //            OnlineLabels.push(parseInt(lods[i].scale.toFixed()));
            //        }
            //    }
            //}
            layer_online_img =new GoogleMapImgLayer();
            var baseMap = ["vec","img","offline"];
            for(var i= 0, dl=baseMap.length;i<dl;i++){
                $("#"+baseMap[i]).removeClass("base-map-switch-active");
            }
            $("#"+layer).addClass("base-map-switch-active");
            //������ʾ��ͼ
            var diTu=null;
            switch(layer){
                case "img":{//Ӱ��
                    diTu=[layer_online_img,layer_online_annotation];
                    buildMap(map.extent,diTu);
                    break;
                }
                case "vec":{//���ߵ�ͼ
                    diTu=[layer_online_diTu];
                    buildMap(map.extent,diTu);
                    break;
                }
                default :{//���ߵ�ͼ
                    diTu=[layer_diTu];
                    buildMap(map.extent,diTu);
                }
            }
        };
        function buildMap(b,diTu) {
            if (map) { // kill the map, if it already exists
                map.destroy();
                map = null;
            }
            var extent= b || map.extent;
            labels=[];
            var lods = diTu[0].tileInfo.lods;
            LodsLength=lods.length;
            map = new Map("map", {
                extent: extent,
                logo:false,
                showAttribution: false,
                sliderPosition: "bottom-right",
                "lods":lods,
                sliderStyle: "large"
                //sliderLabels: OnlineLabels
            });
            map.addLayers(diTu);
        }

        //SetLayersVisibility();
//������

        //С������
        var CellLayer;
        function GetCell(){
            //CellLayer=new WFSLayer(yewu_url+"/0");
            CellLayer=new esri.layers.FeatureLayer(yewu_url+"/0",{
                mode: esri.layers.FeatureLayer.MODE_ONDEMAND,
                outFields: ["*"],
                infoTemplate:new InfoTemplate("${SITENAME}","<table><tr><td>С������</td><td>${CELLNAME}</td></tr><tr><td>PCI</td><td>${PCI}</td></tr></table>")
            });
            map.addLayer(CellLayer);
            //svg��ʽ
            //var Sector="M 379 220.375 C 315.4436 220.375 255.85809 237.34672 204.53125 267.03125 L 382 600.375 L 568.25 276.09375 C 513.72728 240.83432 448.76224 220.375 379 220.375 z";
            var  Sector="M 16.944445 4.444442 C 14.352888 4.444442 11.948866 5.247371 9.967574 6.6180334 C 9.967578  6.6180334 9.967578 6.6180334 9.967578 6.6180334 C 16.666666 16.666666 16.666666 16.666666 16.666666 16.666666 C 23.53629 6.3622336 23.53629 6.3622336 23.53629 6.3622336 C 23.536291 6.3622336 23.536291 6.3622336 23.536291  6.3622336 C 21.63195 5.148012 19.370361 4.444442 16.944445 4.444442 z";
            //var marker=new esri.symbol.SimpleMarkerSymbol().setPath(Sector).setOutline(new esri.symbol.SimpleLineSymbol().setColor(new dojo.Color([255,255,0])).setWidth(0.5));
            //marker.setSize("7");
            var markerSymbol = new esri.symbol.SimpleMarkerSymbol();
            markerSymbol.setPath(Sector);
            markerSymbol.setOutline(null);
            markerSymbol.setSize("29");
            markerSymbol.setOffset(0,9);
            var  render=new esri.renderer.SimpleRenderer(markerSymbol);
            render.setRotationInfo({
                field: "AZIMUTH"
            });
            //render.setSizeInfo({
            //    field: "AHEIGHT",
            //    minSize: 20,
            //    maxSize: 25,
            //    minDataValue: 0,
            //    maxDataValue: 30,
            //    valueUnit: "unknown"
            //});
            render.setColorInfo({
                field: "PCI",
                minDataValue: 0,
                maxDataValue: 480,
                colors: [
                    new dojo.Color([0,104,214]), new dojo.Color([20,120,220]), new dojo.Color([39,136,226]),
                    new dojo.Color([59,152,232]), new dojo.Color([78,169,237]), new dojo.Color([98,185,243]),
                    new dojo.Color([131,197,181]), new dojo.Color([164,210,120]), new dojo.Color([197,222,58]),
                    new dojo.Color([205,188,80]), new dojo.Color([212,155,102]), new dojo.Color([220,121,124]),
                    new dojo.Color([216,87,115]), new dojo.Color([211,53,106]), new dojo.Color([206,19,97])
                ]
            });
            CellLayer.setRenderer(render);
        }
        //����
        function addClusters(resp) {
            var photoInfo = {};
            photoInfo.data = arrayUtils.map(resp, function(p) {
                var latlng = new  Point(parseFloat(p.x), parseFloat(p.y), map.spatialReference);
                var webMercator = webMercatorUtils.geographicToWebMercator(latlng);
                var attributes = {
                    "x": p.x,
                    "y": p.y,
                    "values":parseFloat(Math.random()+ 0.2).toFixed(2)
                };
                return {
                    "x": p.x,
                    "y": p.y,
                    "attributes": attributes
                };
            });
            // popupTemplate to work with attributes specific to this dataset
            var popupTemplate = new PopupTemplate({
                "title": "",
                "fieldInfos": [{
                    "fieldName": "x",
                    visible: true
                }, {
                    "fieldName": "y",
                    "label": "By",
                    visible: true
                }, {
                    "fieldName": "values",
                    "label": "On Instagram",
                    visible: true
                }]
            });
            // cluster layer that uses OpenLayers style clustering
            var picBaseUrl = "images/";
            var defaultSym = new SimpleMarkerSymbol().setSize(4);
            var renderer1 = new ClassBreaksRenderer(defaultSym, "values");
            var blue = new PictureMarkerSymbol(picBaseUrl + "BluePin1LargeB.png", 32, 32).setOffset(0, 15);
            var green = new PictureMarkerSymbol(picBaseUrl + "GreenPin1LargeB.png", 64, 64).setOffset(0, 15);
            var red = new PictureMarkerSymbol(picBaseUrl + "RedPin1LargeB.png", 72, 72).setOffset(0, 15);

            clusterLayer = new ClusterLayer({
                "data": photoInfo.data,
                "distance": 100,
                "spatialReference":map.spatialReference,
                "id": "clusters",
                "labelColor": "#fff",
                "labelOffset": 10,
                "resolution": map.extent.getWidth() / map.width,
                "singleColor": "#888",
                "singleTemplate": popupTemplate,
                "Renderer":renderer1
            });
            var renderer = new ClassBreaksRenderer(defaultSym, "clusterCount");
            renderer.addBreak(5, 10, blue);
            renderer.addBreak(10, 200, green);
            renderer.addBreak(200, 5001, red);
            clusterLayer.setRenderer(renderer);
            map.addLayer(clusterLayer);
            // close the info window when the map is clicked
            map.on("click", cleanUp);
            // close the info window when esc is pressed
            map.on("key-down", function(e) {
                if (e.keyCode === 27) {
                    cleanUp();
                }
            });
        }
        function ProjectPoints(){
            var n=5000;
            var x = 112.11;
            var y = 23.31;
            var wgs = new SpatialReference({
                "wkid": 4326
            });
            var points=[];//������
            for (var i = 0; i < n; i++) {
                var cc={};
                cc.x= x+Math.random();
                cc.y=y+Math.random();
                var latlng = new  Point(parseFloat(cc.x), parseFloat(cc.y), wgs);
                points.push(latlng);
            }
            if(map.spatialReference.wkid!=4326)
            {
                var pp = gs.project(points, map.spatialReference);
                pp.then(ProjectSuccess, ProjectFailure);
            }
            else{
                addClusters(points);
            }
        }
        function ProjectSuccess(res){
            addClusters(res);
        }
        function ProjectFailure(err){
            alert(err);
        }
        function cleanUp() {
            map.infoWindow.hide();
            clusterLayer.clearSingles();
        }
        function error(err) {
            console.log("something failed: ", err);
        }
// show cluster extents...
// never called directly but useful from the console
        window.showExtents = function() {
            var extents = map.getLayer("clusterExtents");
            alert("ִ����");
            if ( extents ) {
                map.removeLayer(extents);
            }
            extents = new GraphicsLayer({ id: "clusterExtents" });
            var sym = new SimpleFillSymbol().setColor(new Color([205, 193, 197, 0.5]));

            arrayUtils.forEach(clusterLayer._clusters, function(c, idx) {
                var e = c.attributes.extent;
                extents.add(new Graphic(new Extent(e[0], e[1], e[2], e[3], map.spatialReference), sym));
            }, this);
            map.addLayer(extents, 0);
        };
    });
});/**
 * Created by zhaobenfu on 2015/9/6.
 */
/**
 * Created by zhaobenfu on 2015/10/30.
 */
