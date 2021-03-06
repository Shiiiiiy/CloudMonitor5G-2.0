messageType = "";
var mousePositionControl = new ol.control.MousePosition({
    className: 'custom-mouse-position',
    projection: 'EPSG:4326',
    target: document.getElementById('location10'),
    coordinateFormat: ol.coordinate.createStringXY(4),
    undefinedHTML: ''
});

var view = new ol.View({
    center: ol.proj.transform([121.486848, 31.232137], 'EPSG:4326', 'EPSG:3857'),
    zoom: 10,
    //minResolution: 0.3
});

/*加载离线地图图层*/
var gdVector = new ol.layer.Tile({
    title: "离线地图",
    name: 'GDlayer',
    type: 99,

    source: new ol.source.XYZ({
        crossOrigin: "anonymous",
        url: gd_url
    })
});

/*加载天地图图层*/
var imgVector = new ol.layer.Tile({
    title: "天地图",
    name: 'imgLlayer',
    type: 99,

    source: new ol.source.XYZ({
        crossOrigin: "Anonymous",
        url: img_url
    })
});
/*天地图标注图层*/
var labelVector = new ol.layer.Tile({
    name: 'labelLayer',
    //visible: false,
    type: 99,
    source: new ol.source.XYZ({
        crossOrigin: "Anonymous",
        url: label_url
    })
});
/*道路图层*/
var roadVector = new ol.layer.Tile({
    name: 'roadLayer',
    source: new ol.source.TileWMS({
        crossOrigin: "Anonymous",
        url: wms_url,
        params: {
            LAYERS: dataBaseName + ":" + baseLayer,
        },
        transition: 0
    })
});

/* 测量图层 */
var measureSource = new ol.source.Vector();
var measureVector = new ol.layer.Vector({
    source: measureSource,
    style: new ol.style.Style({
        fill: new ol.style.Fill({
            color: 'rgba(255, 255, 255, 0.2)'
        }),
        stroke: new ol.style.Stroke({
            color: 'rgba(0, 0, 0, 0.5)',
            lineDash: [10, 10],
            width: 2
        }),
        image: new ol.style.Circle({
            radius: 7,
            fill: new ol.style.Fill({
                color: '#ffcc33'
            })
        })
    })
});
/* 日志同步当前点 */
var synchSource = new ol.source.Vector({ wrapX: false });
var synchVector = new ol.layer.Vector({
    name: 'SynchronizeVector',
    type: 99,
    source: synchSource,
    style: function (feature) {
        return new ol.style.Style({
            image: new ol.style.RegularShape({
                fill: new ol.style.Fill({
                    color: 'red'
                }),
                stroke: new ol.style.Stroke({
                    color: 'magenta',
                    width: 1
                }),
                points: 5,
                radius: 18,
                radius2: 9,
                angle: 0,
            }),
        })
        les;
    }
});
/* 查询数据源 */
var querySource = new ol.source.Vector({ wrapX: false });
var queryVector = new ol.layer.Vector({
    name: 'queryVector',
    type: 99,
    source: querySource
    // style: function(feature) {
    //     return getPntStyle('NR SS-RSRP',feature);
    // }
});

/*轨迹数据*/
var logVector = new ol.layer.Tile({
    name: "logLayer",
    type: 1,
    visible: false,
    source: new ol.source.TileWMS({
        url: wms_url,
        params: {
            'FORMAT': 'image/png',
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: dataBaseName + ':' + logLayer
        }
    })
});


var image = new ol.style.Icon({
    anchor: [0.5, 1],
    size: 50,
    src: './images/position.png'
});
var pntStyle = new ol.style.Circle({
    radius: 5,
    fill: new ol.style.Fill({
        color: 'magenta'
    }),
    stroke: new ol.style.Stroke({
        color: 'magenta',
        width: 2
    })
});
var styles = {
    'Point': new ol.style.Style({
        image: image
    }),
    'LineString': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'green',
            width: 1
        })
    }),
    'MultiLineString': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'green',
            width: 1
        })
    }),
    'MultiPoint': new ol.style.Style({
        image: image
    }),
    'MultiPolygon': new ol.style.Style({
        stroke: new ol.style.Stroke({
            // color: 'rgba(85,221,248,1)',
            color: 'rgba(30,53,162,1)',
            width: 2
        })/*
         * , fill: new ol.style.Fill({ color: 'rgba(255, 255, 0, 0.6)' })
         */
    }),
    'Polygon': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: '#15F6E2',
            width: 2
        }),
        fill: new ol.style.Fill({
            color: 'rgba(255, 0, 0,0.1)'
        })
    }),
    'GeometryCollection': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'magenta',
            width: 2
        }),
        fill: new ol.style.Fill({
            color: 'magenta'
        }),
        image: new ol.style.Circle({
            radius: 10,
            fill: null,
            stroke: new ol.style.Stroke({
                color: 'magenta'
            })
        })
    }),
    'Circle': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'red',
            width: 2
        }),
        fill: new ol.style.Fill({
            color: 'rgba(255,0,0,0.2)'
        })
    })
};

if (gd_url) {
    map = new ol.Map({
        layers: [synchVector, gdVector, logVector,/* queryVector, */ measureVector],
        interactions: ol.interaction.defaults({ doubleClickZoom: false })
            .extend([new ol.interaction.DragRotateAndZoom()]),
        controls: ol.control.defaults({
            attributionOptions: ({
                collapsible: false
            })
        }).extend([
            new ol.control.Rotate(), mousePositionControl
        ]),
        target: 'map',
        view: view,
        logo: null
    });
}
else {
    map = new ol.Map({
        layers: [synchVector, roadVector, logVector, /* queryVector, */ measureVector],
        interactions: ol.interaction.defaults({ doubleClickZoom: false })
            .extend([new ol.interaction.DragRotateAndZoom()]),
        controls: ol.control.defaults({
            attributionOptions: ({
                collapsible: false
            })
        }).extend([
            new ol.control.Rotate(), mousePositionControl
        ]),
        target: 'map',
        view: view,
        logo: null
    });
}




/* 移除放大缩小控件 */
map.removeControl(map.getControls().array_[0]);

//鼠标移动事件
map.on('pointermove', function (evt) {
    if (evt.dragging) {
        return;
    }
});

map.on('dblclick', function (evt) {	 // singleclick单击
    var view = map.getView();
    var viewResolution = view.getResolution();
    if (draw) {
        map.removeInteraction(draw);
    }
})

var iniCenter = view.getCenter();
var iniZoom = view.getZoom();

/* 放大 */
function zoominMap() {
    var view = map.getView();
    var zoom = view.getZoom();
    view.setZoom(zoom + 1);
}
/* 缩小 */
function zoomoutMap() {
    var view = map.getView();
    var zoom = view.getZoom();
    view.setZoom(zoom - 1);
}
/* 切换全图 */
function fullscreen() {

    //map.getView().fit(querySource.getExtent());
    /* var viewExtent = map.getView().calculateExtent();
    map.getView().fit(viewExtent); */

    // view.fit(new ol.proj.transformExtent(initBounds, 'EPSG:4326', 'EPSG:900913'));
    view.fit(querySource.getExtent());
}

/* 导出图片 */
function saveMap() {
    html2canvas($("#map"), {  //指定区域
        allowTaint: true,
        taintTest: false,
        onrendered: function (canvas) {
            canvas.toBlob(function (blob) {
                saveAs(blob, 'map.png');
            });
        }
    });

    /* map.once('postcompose', function (event) {
        //获取map中的canvas,并转换为图片
        var canvas = event.context.canvas;
        if (navigator.msSaveBlob) {
            navigator.msSaveBlob(canvas.msToBlob(), 'map.png');
        } else {
            // var strDataURI = canvas.toDataURL();
            canvas.toBlob(function (blob) {
                saveAs(blob, 'map.png');

            });
        }
    });
    map.renderSync(); */

    /* domtoimage.toBlob(document.getElementById('map'))
        .then(function (blob) {
            window.saveAs(blob, 'map.png');
        }); */
}

function showTraceByServer(logIds, dataType) {
    console.log(logIds);
    if (!logIds)
        return;
    mapLogIds = logIds;

    var reg = new RegExp(",", "g")
    var strLogIds = "'" + logIds.replace(reg, "','") + "'";
    //显示图例
    var legendInfo = [];
    var legendName = dataType ? dataType : "NR SS-RSRP";

    var pointStyle = 'point_rsrp';
    if (dataType == "NR SS-RSRP") {
        legendInfo = [
            {
                name: "[-Inf,-105)",
                color: "#FF0500"
            }, {
                name: "[-105,-100)",
                color: "#C9C307"
            }, {
                name: "[-100,-80)",
                color: "#008C00"
            }, {
                name: "[-80,+Inf)",
                color: "#0400FD"
            }
        ];
    } else if (dataType == "NR SS-SINR") {
        pointStyle = 'point_sinr';
        legendInfo = [
            {
                name: "[-Inf,-3)",
                color: "#F20400"
            }, {
                name: "[-3,0)",
                color: "#C9C307"
            }, {
                name: "[0,15)",
                color: "#008200"
            }, {
                name: "[15,+Inf)",
                color: "#0309FB"
            }
        ];
    } else if (dataType == "LTE PCC_RSRP") {
        pointStyle = 'point_lte_rsrp';
        legendInfo = [
            {
                name: "[-Inf,-105)",
                color: "#FF0500"
            }, {
                name: "[-105,-100)",
                color: "#C9C307"
            }, {
                name: "[-100,-80)",
                color: "#008C00"
            }, {
                name: "[-80,+Inf)",
                color: "#0400FD"
            }
        ];
    } else if (dataType == "LTE PCC_SINR") {
        pointStyle = 'point_lte_sinr';
        legendInfo = [
            {
                name: "[-Inf,-3)",
                color: "#F20400"
            }, {
                name: "[-3,0)",
                color: "#C9C307"
            }, {
                name: "[0,15)",
                color: "#008200"
            }, {
                name: "[15,+Inf)",
                color: "#0309FB"
            }
        ];
    }
    showLegendControl(legendName, legendInfo);

    var logIdField = "logcode";
    var filterParam = {
        'FILTER': null,
        'CQL_FILTER': logIdField + " in (" + logIds + ")",
        'FEATUREID': null
    };

    logVector.getSource().params_.STYLES = pointStyle;
    logVector.getSource().updateParams(filterParam);
    logVector.getSource().refresh();
    logVector.setVisible(true);

    /* querySource.clear();
    var promiseArr = [];
    var logArr = logIds.split(',');
    logArr.forEach(logId => {
        var equalFilter = new ol.format.filter.equalTo(logIdField, logId);
        var featureRequest = new ol.format.WFS().writeGetFeature({
            srsName: 'EPSG:900913',
            featureTypes: [logLayer],
            outputFormat: 'application/json',
            filter: equalFilter
        });
        // then post the request
        var tracePromise = fetch(yewu_url, {
            method: 'POST',
            body: new XMLSerializer().serializeToString(featureRequest)
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            var features = new ol.format.GeoJSON().readFeatures(json);
            if (features.length == 0) {
                return;
            } else {
                querySource.addFeatures(features);
            }
        });
        promiseArr.push(tracePromise);
    });

    Promise.all(promiseArr).then(() => {
        // //显示轨迹数据
        // if (tracePntLayer)
        //     map.removeLayer(tracePntLayer);
        // tracePntLayer = new ol.layer.WebGLPoints({
        //     source: querySource,
        //     style: getStyleBytype(dataType),
        //     disableHitDetection: true,
        // })
        // map.addLayer(tracePntLayer);

        var dataExtent  = querySource.getExtent();
        querySource.clear();
        map.getView().fit(dataExtent);
    }).catch(() => {

    }) */

}


var mapTraceData;
var tracePntLayer = undefined;
function showTrace(dataInfo, dataType) {
    if (!dataInfo || !dataInfo.mapData)
        return;

    mapTraceData = dataInfo;
    querySource.clear();
    for (var k = 0; k < dataInfo.mapData.length; k++) {
        var logName = dataInfo.mapData[k].logName;
        var traceData = dataInfo.mapData[k].gpsData;
        var curNum = k + 1;
        var tracePromise = dealTraceData(curNum, logName, dataType, traceData);
        tracePromise.then(function (resultData) {
            if (resultData)
                progress.addLoaded(resultData.currentNum, dataInfo.mapData.length, resultData.logName);
        })
    }
    //地图居中显示并导出图片
    view.fit(querySource.getExtent());

    if (tracePntLayer)
        map.removeLayer(tracePntLayer);
    tracePntLayer = new ol.layer.WebGLPoints({
        source: querySource,
        style: getStyleBytype(dataType),
        disableHitDetection: true,
    })
    map.addLayer(tracePntLayer);

    //显示图例
    var legendInfo = [];
    var legendName = dataType ? dataType : "NR SS-RSRP";

    if (dataType == "NR SS-RSRP") {
        legendInfo = [
            {
                name: "[-Inf,-105)",
                color: "#FF0500"
            }, {
                name: "[-105,-100)",
                color: "#C9C307"
            }, {
                name: "[-100,-80)",
                color: "#008C00"
            }, {
                name: "[-80,+Inf)",
                color: "#0400FD"
            }
        ];
    } else if (dataType == "NR SS-SINR") {
        legendInfo = [
            {
                name: "[-Inf,-3)",
                color: "#F20400"
            }, {
                name: "[-3,0)",
                color: "#C9C307"
            }, {
                name: "[0,15)",
                color: "#008200"
            }, {
                name: "[15,+Inf)",
                color: "#0309FB"
            }
        ];
    } else if (dataType == "LTE PCC_RSRP") {
        legendInfo = [
            {
                name: "[-Inf,-105)",
                color: "#FF0500"
            }, {
                name: "[-105,-100)",
                color: "#C9C307"
            }, {
                name: "[-100,-80)",
                color: "#008C00"
            }, {
                name: "[-80,+Inf)",
                color: "#0400FD"
            }
        ];
    } else if (dataType == "LTE PCC_SINR") {
        legendInfo = [
            {
                name: "[-Inf,-3)",
                color: "#F20400"
            }, {
                name: "[-3,0)",
                color: "#C9C307"
            }, {
                name: "[0,15)",
                color: "#008200"
            }, {
                name: "[15,+Inf)",
                color: "#0309FB"
            }
        ];
    }
    showLegendControl(legendName, legendInfo);

    /* var tileStart = 0
    var tileEnd = 0;
    roadVector.getSource().on("tileloadstart", function () {
        ++tileStart;
    })
    roadVector.getSource().on("tileloadend", function () {
        setTimeout(() => {
            ++tileEnd;
            if (tileStart == tileEnd) {
                saveMap();
            }
        }, 50);
    }) */

}

function dealTraceData(curNum, logName, dataType, traceData) {
    var tracePromise = new Promise((resolve, reject) => {

        for (var i = 0; i < traceData.length; i++) {
            var gpsLon = parseFloat(traceData[i].Long);
            var gpsLat = parseFloat(traceData[i].Lat);
            var nrRsrp = traceData[i].NR_SS_RSRP;
            var nrSinr = traceData[i].NR_SS_SINR;
            var lteRsrp = traceData[i].LTE_PCC_RSRP;
            var lteSinr = traceData[i].LTE_PCC_SINR;
            if (transCoor) {
                var transLonLat = transform(gpsLat, gpsLon);
                gpsLon = transLonLat.lon;
                gpsLat = transLonLat.lat;
            }


            var traceFeat = new ol.Feature({
                nr_rsrp: nrRsrp,
                nr_sinr: nrSinr,
                lte_rsrp: lteRsrp,
                lte_sinr: lteSinr,
                geometry: new ol.geom.Point(ol.proj.transform([gpsLon, gpsLat], 'EPSG:4326', 'EPSG:3857'))
            });
            /* var featColor = "#A9ACAE";
            if (dataType == "NR SS-RSRP" && nrRsrp) {
                featColor = getGpsColor("5grsrp", nrRsrp);
            }
            else if (dataType == "NR SS-SINR" && nrSinr) {
                featColor = getGpsColor("5gsinr", nrSinr);
            }
            else if (dataType == "LTE PCC_RSRP" && lteRsrp) {
                featColor = getGpsColor("ltersrp", lteRsrp);
            }
            else if (dataType == "LTE PCC_SINR" && lteSinr) {
                featColor = getGpsColor("ltesinr", lteSinr);
            }
 
            traceFeat.setStyle(new ol.style.Style({
                image: new ol.style.Circle({
                    radius: 3,
                    fill: new ol.style.Fill({
                        color: featColor
                    }),
                    stroke: new ol.style.Stroke({
                        color: featColor,
                        width: 1
                    })
                })
            })); */
            querySource.addFeature(traceFeat);
            resolve({
                currentNum: curNum,
                logName: logName
            });
        }
    })
    return tracePromise;
}

function getGpsColor(type, dataValue) {
    //需要根据不同类别设置GPS点颜色
    let color = '#A9ACAE';
    if (type == '5grsrp') {
        // let dataValue = feat['NR_SS_RSRP'];
        if (dataValue >= -Infinity && dataValue < -105) {
            color = '#FF0500';
        } else if (dataValue >= -105 && dataValue < -100) {
            color = '#C9C307';
        } else if (dataValue >= -100 && dataValue < -80) {
            color = '#008C00';
        } else if (dataValue >= -80 && dataValue < +Infinity) {
            color = '#0400FD';
        }
        return color;
    } else if (type == '5gsinr') {
        // let dataValue = feat['NR_SS_SINR'];
        if (dataValue >= -Infinity && dataValue < -3) {
            color = '#F20400';
        } else if (dataValue >= -3 && dataValue < 0) {
            color = '#C9C307';
        } else if (dataValue >= 0 && dataValue < 15) {
            color = '#008200';
        } else if (dataValue >= 15 && dataValue < +Infinity) {
            color = '#0309FB';
        }
        return color;
    } else if (type == 'ltersrp') {
        // let dataValue = feat['LTE_RSRP'];
        if (dataValue >= -Infinity && dataValue < -105) {
            color = '#FF0500';
        } else if (dataValue >= -105 && dataValue < -100) {
            color = '#C9C307';
        } else if (dataValue >= -100 && dataValue < -80) {
            color = '#008C00';
        } else if (dataValue >= -80 && dataValue < +Infinity) {
            color = '#0400FD';
        }
        return color;
    } else if (type == 'ltesinr') {
        // let dataValue = feat['LTE_SINR'];
        if (dataValue >= -Infinity && dataValue < -3) {
            color = '#F20400';
        } else if (dataValue >= -3 && dataValue < 0) {
            color = '#C9C307';
        } else if (dataValue >= 0 && dataValue < 15) {
            color = '#008200';
        } else if (dataValue >= 15 && dataValue < +Infinity) {
            color = '#0309FB';
        }
        return color;
    }

}

function getPntStyle(type, feat) {
    //需要根据不同类别设置GPS点颜色
    let color = '#A9ACAE';
    if (type == 'NR SS-RSRP') {
        let dataValue = feat.values_['nr_rsrp'];
        if (dataValue >= -Infinity && dataValue < -105) {
            color = '#FF0500';
        } else if (dataValue >= -105 && dataValue < -100) {
            color = '#C9C307';
        } else if (dataValue >= -100 && dataValue < -80) {
            color = '#008C00';
        } else if (dataValue >= -80 && dataValue < +Infinity) {
            color = '#0400FD';
        }
    } else if (type == 'NR SS-SINR') {
        let dataValue = feat.values_['nr_sinr'];
        if (dataValue >= -Infinity && dataValue < -3) {
            color = '#F20400';
        } else if (dataValue >= -3 && dataValue < 0) {
            color = '#C9C307';
        } else if (dataValue >= 0 && dataValue < 15) {
            color = '#008200';
        } else if (dataValue >= 15 && dataValue < +Infinity) {
            color = '#0309FB';
        }
    } else if (type == 'LTE PCC_RSRP') {
        let dataValue = feat.values_['lte_rsrp'];
        if (dataValue >= -Infinity && dataValue < -105) {
            color = '#FF0500';
        } else if (dataValue >= -105 && dataValue < -100) {
            color = '#C9C307';
        } else if (dataValue >= -100 && dataValue < -80) {
            color = '#008C00';
        } else if (dataValue >= -80 && dataValue < +Infinity) {
            color = '#0400FD';
        }
    } else if (type == 'LTE PCC_SINR') {
        let dataValue = feat.values_['lte_sinr'];
        if (dataValue >= -Infinity && dataValue < -3) {
            color = '#F20400';
        } else if (dataValue >= -3 && dataValue < 0) {
            color = '#C9C307';
        } else if (dataValue >= 0 && dataValue < 15) {
            color = '#008200';
        } else if (dataValue >= 15 && dataValue < +Infinity) {
            color = '#0309FB';
        }
    }
    return new ol.style.Style({
        image: new ol.style.Circle({
            radius: 3,
            fill: new ol.style.Fill({
                color: color
            }),
            stroke: new ol.style.Stroke({
                color: color,
                width: 1
            })
        })
    })
}

function getStyleBytype(type) {
    //需要根据不同类别设置GPS点颜色
    if (type == 'NR SS-RSRP') {
        var lyrStyle = {
            symbol: {
                symbolType: 'circle',
                size: 8,
                color: ['case', ['between', ['get', 'nr_rsrp'], -10000, -105], '#FF0500',
                    ['between', ['get', 'nr_rsrp'], -105, -100], '#C9C307',
                    ['between', ['get', 'nr_rsrp'], -100, -80], '#008C00',
                    ['between', ['get', 'nr_rsrp'], -80, +10000], '#0400FD',
                    '#A9ACAE'
                ],
                offset: [0, 0],
                opacity: 0.95,
            }
        }
        return lyrStyle;
    } else if (type == 'NR SS-SINR') {
        var lyrStyle = {
            symbol: {
                symbolType: 'circle',
                size: 8,
                color: ['case', ['between', ['get', 'nr_sinr'], -10000, -3], '#F20400',
                    ['between', ['get', 'nr_sinr'], -3, 0], '#C9C307',
                    ['between', ['get', 'nr_sinr'], 0, 15], '#008200',
                    ['between', ['get', 'nr_sinr'], 15, +10000], '#0309FB',
                    '#A9ACAE'
                ],
                offset: [0, 0],
                opacity: 0.95,
            }
        }
        return lyrStyle;
    } else if (type == 'LTE PCC_RSRP') {
        var lyrStyle = {
            symbol: {
                symbolType: 'circle',
                size: 8,
                color: ['case', ['between', ['get', 'lte_rsrp'], -10000, -105], '#FF0500',
                    ['between', ['get', 'lte_rsrp'], -105, -100], '#C9C307',
                    ['between', ['get', 'lte_rsrp'], -100, -80], '#008C00',
                    ['between', ['get', 'lte_rsrp'], -80, +10000], '#0400FD',
                    '#A9ACAE'
                ],
                offset: [0, 0],
                opacity: 0.95,
            }
        }
        return lyrStyle;
    } else if (type == 'LTE PCC_SINR') {
        var lyrStyle = {
            symbol: {
                symbolType: 'circle',
                size: 8,
                color: ['case', ['between', ['get', 'lte_sinr'], -10000, -3], '#F20400',
                    ['between', ['get', 'lte_sinr'], -3, 0], '#C9C307',
                    ['between', ['get', 'lte_sinr'], 0, 15], '#008200',
                    ['between', ['get', 'lte_sinr'], 15, +10000], '#0309FB',
                    '#A9ACAE'
                ],
                offset: [0, 0],
                opacity: 0.95,
            }
        }
        return lyrStyle;
    }

}

function changeLegend() {
    var dataType = document.getElementById("dataType").value;
    if (messageType == "logreplay")
        showLogTrace(mapLogIds, dataType);
    else
        showTraceByServer(mapLogIds, dataType);
    //showTrace(mapTraceData, dataType);
}

function showLegendControl(legendName, legendInfo) {
    var mapDiv = document.getElementById("map");
    if (document.getElementById("mapLegend")) {
        mapDiv.removeChild(document.getElementById("mapLegend"));
    }

    // 创建一个DOM元素
    var div = document.createElement("div");
    div.id = "mapLegend";
    div.style.background = "White";
    div.style.padding = "5px";
    div.style.opacity = " 0.8";
    div.style.borderRadius = "5px 5px 5px 5px";
    div.style.width = "100px";
    div.style.height = "108px";
    div.style.position = "absolute";
    $("<div style='border-radius:5px 5px 5px 5px;text-align:center;height:24px;width:99%;font-size:14px;line-height:25px;color:black'>" + legendName + "</div>").appendTo(div);
    legendInfo.forEach(element => {
        $("<div style='border-radius:5px 5px 5px 5px;background-color:" + element.color + ";text-align:center;height:24px;width:99%;font-size:10px;line-height:25px;color:white'>" + element.name + "</div>").appendTo(div);
    });
    // 添加DOM元素到地图中
    mapDiv.appendChild(div);
    //最后微调自定义控件位置
    $("#mapLegend").css('right', "30px");
    $("#mapLegend").css('bottom', "70px");
}



function Progress(el) {
    this.el = el;
}

/**
 * Increment the count of loaded tiles.
 */
Progress.prototype.addLoaded = function (curLen, totalLen, logName) {
    var this_ = this;
    setTimeout(function () {
        this_.update(curLen, totalLen, logName);
    }, 100);
};

/**
 * Update the progress bar.
 */
Progress.prototype.update = function (curLen, totalLen, logName) {
    var width = ((curLen / totalLen) * 100).toFixed(1) + '%';
    this.el.style.width = "100%";
    this.el.innerHTML = logName + "(" + curLen + "/" + totalLen + ")  ";

    /* if (curLen === totalLen) {
        var this_ = this;
        setTimeout(function () {
            this_.hide(curLen, totalLen);
        }, 500);
    } */
};

/**
 * Show the progress bar.
 */
Progress.prototype.show = function () {
    this.el.style.visibility = 'visible';
};

/**
 * Hide the progress bar.
 */
Progress.prototype.hide = function (curLen, totalLen) {
    if (curLen === totalLen) {
        this.el.style.visibility = 'hidden';
        this.el.style.width = 0;
    }
};

var progress = new Progress(document.getElementById('progress'));


var mapLogIds;
var pointLayer = undefined;
var nullPointLayer = undefined;
var pntSource = new ol.source.Vector({ wrapX: false });
var nullPntSource = new ol.source.Vector({ wrapX: false });
var logSource = new ol.source.Vector({ wrapX: false });

function showLogTrace(logIds, dataType) {
    console.log(logIds);
    if (!logIds)
        return;

    mapLogIds = logIds;
    pntSource.clear();
    nullPntSource.clear();
    logSource.clear();
    synchSource.clear();
    var logIdField = "logcode";
    var ieField = "lte_rsrp";
    if (dataType == "NR SS-RSRP") {
        ieField = "nr_rsrp";
    } else if (dataType == "NR SS-SINR") {
        ieField = "nr_sinr";
    } else if (dataType == "LTE PCC_RSRP") {
        ieField = "lte_rsrp";
    } else if (dataType == "LTE PCC_SINR") {
        ieField = "lte_sinr";
    }


    var promiseArr = [];
    var logArr = logIds.split(',');
    logArr.forEach(logId => {
        var equalFilter = new ol.format.filter.equalTo(logIdField, logId);
        var notNullFilter = new ol.format.filter.not(new ol.format.filter.isNull(ieField));
        var andFilter = new ol.format.filter.and(equalFilter, notNullFilter);
        var featureRequest = new ol.format.WFS().writeGetFeature({
            srsName: 'EPSG:900913',
            featureTypes: [logLayer],
            outputFormat: 'application/json',
            filter: andFilter
        });
        // then post the request
        var tracePromise = fetch(yewu_url, {
            method: 'POST',
            body: new XMLSerializer().serializeToString(featureRequest)
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            var features = new ol.format.GeoJSON().readFeatures(json);
            if (features.length == 0) {
                return;
            } else {
                pntSource.addFeatures(features);
                logSource.addFeatures(features);
            }
        });
        promiseArr.push(tracePromise);
    });

    //处理空值GPS数据
    logArr.forEach(logId => {
        var equalFilter = new ol.format.filter.equalTo(logIdField, logId);
        var nullFilter = new ol.format.filter.isNull(ieField);
        var andFilter = new ol.format.filter.and(equalFilter, nullFilter);
        var featureRequest = new ol.format.WFS().writeGetFeature({
            srsName: 'EPSG:900913',
            featureTypes: [logLayer],
            outputFormat: 'application/json',
            filter: andFilter
        });
        // then post the request
        var tracePromise = fetch(yewu_url, {
            method: 'POST',
            body: new XMLSerializer().serializeToString(featureRequest)
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            var features = new ol.format.GeoJSON().readFeatures(json);
            if (features.length == 0) {
                return;
            } else {
                nullPntSource.addFeatures(features);
                logSource.addFeatures(features);
            }
        });
        promiseArr.push(tracePromise);
    });


    Promise.all(promiseArr).then(() => {
        if (pointLayer)
            map.removeLayer(pointLayer);
        pointLayer = new ol.layer.WebGLPoints({
            source: pntSource,
            style: getStyleBytype(dataType),
            disableHitDetection: true,
        })
        map.addLayer(pointLayer);

        if (nullPointLayer)
            map.removeLayer(nullPointLayer);
        nullPointLayer = new ol.layer.WebGLPoints({
            source: nullPntSource,
            style: {
                symbol: {
                    symbolType: 'circle',
                    size: 8,
                    color: '#A9ACAE',
                    offset: [0, 0],
                    opacity: 0.95,
                }
            },
            disableHitDetection: true,
        })
        map.addLayer(nullPointLayer);
        //地图居中显示
        view.fit(logSource.getExtent());
    }).catch(() => {

    })


    //显示图例
    var legendInfo = [];
    var legendName = dataType ? dataType : "NR SS-RSRP";

    if (dataType == "NR SS-RSRP") {
        legendInfo = [
            {
                name: "[-Inf,-105)",
                color: "#FF0500"
            }, {
                name: "[-105,-100)",
                color: "#C9C307"
            }, {
                name: "[-100,-80)",
                color: "#008C00"
            }, {
                name: "[-80,+Inf)",
                color: "#0400FD"
            }
        ];
    } else if (dataType == "NR SS-SINR") {
        legendInfo = [
            {
                name: "[-Inf,-3)",
                color: "#F20400"
            }, {
                name: "[-3,0)",
                color: "#C9C307"
            }, {
                name: "[0,15)",
                color: "#008200"
            }, {
                name: "[15,+Inf)",
                color: "#0309FB"
            }
        ];
    } else if (dataType == "LTE PCC_RSRP") {
        legendInfo = [
            {
                name: "[-Inf,-105)",
                color: "#FF0500"
            }, {
                name: "[-105,-100)",
                color: "#C9C307"
            }, {
                name: "[-100,-80)",
                color: "#008C00"
            }, {
                name: "[-80,+Inf)",
                color: "#0400FD"
            }
        ];
    } else if (dataType == "LTE PCC_SINR") {
        legendInfo = [
            {
                name: "[-Inf,-3)",
                color: "#F20400"
            }, {
                name: "[-3,0)",
                color: "#C9C307"
            }, {
                name: "[0,15)",
                color: "#008200"
            }, {
                name: "[15,+Inf)",
                color: "#0309FB"
            }
        ];
    }
    showLegendControl(legendName, legendInfo);

    // var logLyr = new ol.layer.Tile({
    //     source: new ol.source.TileWMS({
    //       url: wms_url,
    //           params: { 
    //               LAYERS: dataBaseName+":"+logLayer		
    //           }
    //       })
    //   });
    //   map.addLayer(logLyr);


    /* var url = "";
    $.ajax({
        url: url,
        dataType: "json",
        type: "post",
        contentType: "application/json;charset=utf-8",
        data: dataInfo,
        success: function (gpsData) {
            if (null == gpsData) return;
            querySource.clear();
            for (var i = 0; i < gpsData.length; i++) {
                var gpsLon = gpsData[i].lon;
                var gpsLat = gpsData[i].lat;
                var gpsValue = gpsData[i].NR_SS_RSRP;
                var traceFeat = new ol.Feature({
                    geometry: new ol.geom.Point(ol.proj.transform([gpsLon, gpsLat], 'EPSG:4326', 'EPSG:3857'))
                });
                traceFeat.setStyle(new ol.style.Style({
                    image: new ol.style.Circle({
                        radius: 3,
                        fill: new ol.style.Fill({
                            color: getGpsColor("5grsrp", gpsValue)
                        }),
                        stroke: new ol.style.Stroke({
                            color: getGpsColor("5grsrp", gpsValue),
                            width: 1
                        })
                    })
                }));
                querySource.addFeature(traceFeat);
            }
            //地图居中显示
            view.fit(querySource.getExtent());
 
        },
        error: function (e) {
            console.log(e)
        }
    }); */
}

function SynchronizeLog(logId, timestamp) {
    console.log(logId + " " + timestamp);
    if (!logId)
        return;

    var logIdField = "logcode";
    var timeField = "timestamp";
    var idFilter = new ol.format.filter.equalTo(logIdField, logId);
    var timeFilter = new ol.format.filter.greaterThanOrEqualTo(timeField, timestamp);
    var andFilter = new ol.format.filter.and(idFilter, timeFilter);
    var featureRequest = new ol.format.WFS().writeGetFeature({
        srsName: 'EPSG:900913',
        featureTypes: [logLayer],
        outputFormat: 'application/json',
        filter: andFilter
    });
    // then post the request
    var pntPromise = fetch(yewu_url, {
        method: 'POST',
        body: new XMLSerializer().serializeToString(featureRequest)
    }).then(function (response) {
        return response.json();
    }).then(function (json) {
        var features = new ol.format.GeoJSON().readFeatures(json);
        if (features.length == 0) {
            return;
        } else {
            var timeIndex = 0;
            var dateTime=new Date();
            dateTime=dateTime.setDate(dateTime.getDate()+1);
            dateTime=new Date(dateTime);
            var minTimeStamp =dateTime.getTime();
            for (var i = 0; i < features.length; i++) {
                var timeStamp = features[i].values_["timestamp"];
                if(timeStamp<=minTimeStamp){
                    minTimeStamp=timeStamp;
                    timeIndex = i;
                }  
            }

            var synchFeat = features[timeIndex];
            synchSource.clear();
            synchSource.addFeature(synchFeat);
            var pntCoor = synchFeat.getGeometry().getCoordinates()
            var viewExtent = new ol.geom.Polygon.fromExtent(map.getView().calculateExtent());
            if (!viewExtent.intersectsCoordinate(pntCoor)) {
                view.setCenter(pntCoor);
            }
            //view.fit(synchSource.getExtent());
        }
    });
}

var draw;
var sketch;
var helpTooltipElement;
var helpTooltip;
var measureTooltipElement;
var measureTooltip;

/* 测量 */
function measureMap() {
    if (draw) {
        map.removeInteraction(draw);
    }
    addInteraction();
}

var formatLength = function (line) {
    var length = ol.sphere.getLength(line);
    var output;
    if (length > 100) {
        output = (Math.round(length / 1000 * 100) / 100) +
            ' ' + 'km';
    } else {
        output = (Math.round(length * 100) / 100) +
            ' ' + 'm';
    }
    return output;
};

function addInteraction() {
    var type = 'LineString';
    draw = new ol.interaction.Draw({
        source: measureSource,
        type: type,
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(0, 0, 0, 0.5)',
                lineDash: [10, 10],
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 5,
                stroke: new ol.style.Stroke({
                    color: 'rgba(0, 0, 0, 0.7)'
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                })
            })
        })
    });
    map.addInteraction(draw);

    createMeasureTooltip();
    createHelpTooltip();

    var listener;
    draw.on('drawstart',
        function (evt) {
            // set sketch
            sketch = evt.feature;

            /** @type {module:ol/coordinate~Coordinate|undefined} */
            var tooltipCoord = evt.coordinate;

            listener = sketch.getGeometry().on('change', function (evt) {
                var geom = evt.target;
                var output;
                if (geom instanceof ol.geom.Polygon) {
                    output = formatArea(geom);
                    tooltipCoord = geom.getInteriorPoint().getCoordinates();
                } else if (geom instanceof ol.geom.LineString) {
                    output = formatLength(geom);
                    tooltipCoord = geom.getLastCoordinate();
                }
                measureTooltipElement.innerHTML = output;
                measureTooltip.setPosition(tooltipCoord);
            });
        }, this);

    draw.on('drawend',
        function () {
            measureTooltipElement.className = 'tooltip tooltip-static';
            measureTooltip.setOffset([0, -7]);
            // unset sketch
            sketch = null;
            // unset tooltip so that a new one can be created
            measureTooltipElement = null;
            createMeasureTooltip();
            ol.Observable.unByKey(listener);
        }, this);
}

/**
 * Creates a new help tooltip
 */
function createHelpTooltip() {
    if (helpTooltipElement) {
        helpTooltipElement.parentNode.removeChild(helpTooltipElement);
    }
    helpTooltipElement = document.createElement('div');
    helpTooltipElement.className = 'tooltip hidden';
    helpTooltip = new ol.Overlay({
        element: helpTooltipElement,
        offset: [15, 0],
        positioning: 'center-left'
    });
    map.addOverlay(helpTooltip);
}

/**
 * Creates a new measure tooltip
 */
function createMeasureTooltip() {
    if (measureTooltipElement) {
        measureTooltipElement.parentNode.removeChild(measureTooltipElement);
    }
    measureTooltipElement = document.createElement('div');
    measureTooltipElement.className = 'tooltip tooltip-measure';
    measureTooltip = new ol.Overlay({
        element: measureTooltipElement,
        offset: [0, -15],
        positioning: 'bottom-center'
    });
    map.addOverlay(measureTooltip);
}

/* 清除 */
function clearMap() {
    //gpsSource.clear();          //GPS图层
    //cellLineSource.clear();     //小区连线
    querySource.clear();
    measureSource.clear();
    var measureItems = document.getElementsByClassName("tooltip-static");
    for (var i = measureItems.length - 1; i >= 0; --i) {
        measureItems[i].parentElement.remove();
    }
}

/*地图初始化*/
function initMap() {

}

var displayFeatureInfo = function (pixel) {
    var feature = map.forEachFeatureAtPixel(pixel, function (feature) {
        return feature;
    });
    return feature;
}

/*公共反查*/
function publicQuery(vec, evt, srs) {
    var view = map.getView();
    var viewResolution = view.getResolution();
    var source = vec.get('visible') ? vec.getSource() : null;
    if (source) {
        var url = source.getGetFeatureInfoUrl(
            evt.coordinate, viewResolution, view.getProjection(), { 'INFO_FORMAT': 'application/json', 'FEATURE_COUNT': 55 });
        if (url) {
            url += ',' + srs;
            var features;
            $.ajax({
                url: url,
                dataType: "json",
                type: "post",
                async: false,
                data: null,
                success: function (r) {
                    features = new ol.format.GeoJSON().readFeatures(r);
                }
            })
            return features;
        }
    }
}

//分辨率变化事件(鼠标滚轮缩放)
map.getView().on('change:resolution', checkZoom);//checkZoom为调用的函数

function checkZoom() {
    if (map.getView().getZoom() <= 14) {

    }
}

let pi = 3.14159265358979324;
let a = 6378245.0;
let ee = 0.00669342162296594323;
let x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//WGS84 Geodetic System==>Mars Geodetic System
function transform(wgLat, wgLon) {
    let mgLat, mgLon;
    if (outOfChina(wgLat, wgLon)) {
        mgLat = wgLat;
        mgLon = wgLon;
        return {
            lon: 0,
            lat: 0
        };
    }
    let dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
    let dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
    let radLat = wgLat / 180.0 * pi;
    let magic = Math.sin(radLat);
    magic = 1 - ee * magic * magic;
    let sqrtMagic = Math.sqrt(magic);
    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    mgLat = wgLat + dLat;
    mgLon = wgLon + dLon;
    return {
        lon: mgLon,
        lat: mgLat
    }
}


// Mars Geodetic System ==> Baidu Geodetic System
function bd_encrypt(gg_lat, gg_lon) {
    let x = gg_lon, y = gg_lat;
    let z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
    let theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
    let bd_lon = z * Math.cos(theta) + 0.0065;
    let bd_lat = z * Math.sin(theta) + 0.006;
    return {
        lon: bd_lon,
        lat: bd_lat
    }
}

function bd_decrypt(bd_lat, bd_lon) {
    let x = bd_lon - 0.0065, y = bd_lat - 0.006;
    let z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
    let theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
    let gg_lon = z * Math.cos(theta);
    let gg_lat = z * Math.sin(theta);
    return {
        lon: gg_lon,
        lat: gg_lat
    }
}

function outOfChina(lat, lon) {
    if (lon < 72.004 || lon > 137.8347)
        return true;
    if (lat < 0.8293 || lat > 55.8271)
        return true;
    return false;
}

function transformLat(x, y) {
    let ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
    return ret;
}

function transformLon(x, y) {
    let ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
    return ret;
}
