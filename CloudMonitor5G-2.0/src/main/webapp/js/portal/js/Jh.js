var Jh = {

    Views:{
        'view1':'NR主邻区信息窗口',
        'view2':'NR主小区信息窗口',
        'view3':'LTE主小区信息窗口',
        'view4':'LTE主邻区信息窗口',
        'view5':'信令窗口',
        'view6':'事件窗口',
        'view7':'linechart窗口',
    },

    Data:{

        ieData:{},
        signData:[],
        eventData:[]
    },

    Config: {
        syncSourceId:"view12346",
        tableCls: "form-list",
        tdCls: "form-text",
        tdCls2: "single",
        ulCls: "tag-list",
        layCls: "layout-list",
        min: "min",
        mintext: "\u6536\u8d77",//收起
        max: "max",
        maxtext: "\u5c55\u5f00",//展开
        close: "close",
        closetext: "\u5173\u95ed",//关闭
        refreshtext: "\u5237\u65b0",//刷新
        refresh: "refresh",
        _groupItemContent: "itemContent",
        _groupItemHead: "itemHeader",
        _groupWrapperClass: "groupWrapper",
        _groupItemClass: "groupItem",
        _allIe:[],
        _listView:['view5','view6']
    },
    ViewColumns:{
        'view1':[[
            {field:'58000',title:'CellName',width:'11%'},
            {field:'58032',title:'Distance',width:'11%'},
            {field:'50165',title:'ARFCNSSB',width:'11%'},
            {field:'50101',title:'PCI',width:'11%'},
            {field:'70070',title:'BeamID',width:'11%'},
            {field:'50229',title:'SS-RSRP',width:'11%'},
            {field:'50293',title:'SS-RSRQ',width:'11%'},
            {field:'70525',title:'SS-SINR',width:'11%'},
            {field:'70005',title:'RSSI',width:'11%'}
        ]],
        'view2':[[
            {field:'50013',title:'SCell Name',width:'11%'},
            {field:'50006',title:'CellID',width:'11%'},
            {field:'53601',title:'ARFCNSSB',width:'11%'},
            {field:'50007',title:'PCI',width:'11%'},
            {field:'50015',title:'Band',width:'11%'},
            {field:'50055',title:'SS-RSRP',width:'11%'},
            {field:'50056',title:'SS-SINR',width:'17%'},
            {field:'50057',title:'SS-RSRQ',width:'17%'}
        ],[
            {field:'50013',title:'MCC',width:'11%'},
            {field:'50006',title:'MNC',width:'11%'},
            {field:'53601',title:'TAC',width:'11%'},
            {field:'50007',title:'UL BandWidth',width:'11%'},
            {field:'50015',title:'DL BandWidth',width:'11%'},
            {field:'50055',title:'DuplexMode',width:'11%'},
            {field:'50056',title:'subCarrierSpacingCommon',width:'17%'},
            {field:'50057',title:'SSB-subcarrierSpacing',width:'17%'}
        ]],


        'view3':[[
            {field:'40006',title:'Scell_Name',width:'11%'},
            {field:'40007',title:'Scell ID',width:'11%'},
            {field:'40010',title:'EARFCN DL',width:'11%'},
            {field:'40008',title:'PCI',width:'11%'},
            {field:'40028',title:'RSRP',width:'11%'},
            {field:'40035',title:'SINR',width:'11%'},
            {field:'40031',title:'RSRQ',width:'17%'},
            {field:'40034',title:'RSSI',width:'17%'}
        ],[
            {field:'40001',title:'MCC',width:'11%'},
            {field:'40002',title:'MNC',width:'11%'},
            {field:'40009',title:'TAC',width:'11%'},
            {field:'40014',title:'Band',width:'11%'},
            {field:'40019',title:'WorkMode',width:'11%'},
            {field:'40015',title:'PCC_Bandwidth_DL(M)',width:'11%'},
            {field:'40016',title:'SubframeAssignType',width:'17%'},
            {field:'40017',title:'SpecialSubFramePatterns',width:'17%'}
        ]],

        'view4':[[
            {field:'40075',title:'CellName',width:'15%'},
            {field:'44206',title:'Distance',width:'15%'},
            {field:'40139',title:'EARFCN',width:'14%'},
            {field:'40171',title:'PCI',width:'14%'},
            {field:'40203',title:'RSRP',width:'14%'},
            {field:'40299',title:'SINR',width:'14%'},
            {field:'40235',title:'RSRQ',width:'14%'}
        ]],

        'view5':[[
            {field:'time',title:'\u65f6\u95f4',width:'15%'},//时间
            {field:'Netmode',title:'\u5236\u5f0f',width:'15%',format:'Jh.Util.getNetCodeName'},//制式
            {field:'Dir',title:'\u65b9\u5411',width:'14%',format:'Jh.Util.getDirtName'},//方向
            {field:'signalName',title:'\u4fe1\u4ee4',width:'14%'}//信令
        ]],


        'view6':[[
            {field:'time',title:'\u65f6\u95f4',width:'33%'},//时间
            {field:'Netmode',title:'\u5236\u5f0f',width:'33%',format:'Jh.Util.getNetCodeName'},//制式
            {field:'evtName',title:'\u4e8b\u4ef6',width:'33%'}//事件
        ]],



    }

};
Jh.Layout = function () {
    return {
        location: {left: "portal_l", center: "portal_m", right: "portal_r"},
        locationId: {left: "#portal_l", center: "#portal_m", right: "#portal_r"},
        layoutText: {"0": "1", 1: "1:1", 2: "1:2", 3: "2:1", 4: "1:1:1"},
        layoutCss: {
            "0": "w1000 wnone wnone",
            1: "w500 w500 wnone",
            2: "w330 w660 wnone",
            3: "w660 w330 wnone",
            4: "w330 w330 w330"
        },
    }
}();
Jh.Util = {
    getNetCodeName:function(a){
        switch(a){
            case '0' :
            case '1' :
                return  a;
            case '2' :
                return "HSDPA";
            case '3' :
                return "GPRS";
            case '4' :
                return "HSDPA";
            case '5' :
                return "EVDO";
            case '6' :
                return "LTE";
            case '7' :
                return a;
            case '8' :
                return "NR";
            case '9' :
                return "HSR";
            default : return a;
        }

    },
    getDirtName:function(a){

        switch(a){
            case '1' :
                return "上行";
            case '0' :
                return  "下行";
            default : return a;
        }

    },
    dateSlice:function(val){

        if(val && val.length>=19 ){
            return val.slice(0,19).replace(/ /g,"").replace(/-/g,"").replace(/:/g,"")
        }else{
            return '';
        }
    },
    format: function (a, b) {
        for (var c in b) a = a.replace(RegExp("{" + c + "}", "g"), b[c]);
        return a
    }, refresh: function () {
        $("#" + Jh.Layout.left, "#" + Jh.Layout.center, "#" + Jh.Layout.right).sortable("refresh")
    }, toBody: function (a) {
        $("body").append(a)
    }
};
Jh.base = function (a) {
    return a = {
        init: function (b) {
            a._bindEvent(b);
        }, _bindEvent: function (b) {
            a._layoutAClick()
            a._bindAdd($("#addPanel"));
            a._bindSave($("#saveLayoutConfig"));
            $($(".layoutText")[b.layout]).addClass('active');
        },_bindAdd: function (b) {
            b.click(function () {
                $.fallr("show", {
                    buttons: {
                        button1: {
                            //确定
                            text: "\u786e\u5b9a", onclick: function () {
                                var mkey =  $("#modulekey").val();
                                var na = $("#modulekey").find('option:selected').text();
                                if(mkey){
                                    $.fallr("hide");
                                    Jh.fn._addNewPortal(mkey,na);
                                }

                            }
                            //取消
                        }, button2: {text: "\u53d6\u6d88"}
                    },
                    //模块名：   模块Code： 模块位置： 左中右
                    content: '<form style="margin-left:20px"><p>\u6a21\u5757Code\uff1a</p>' +
                        '<select id="modulekey">' +
                        '<option value="view1">NR主邻区信息窗口</option>'+
                        '<option value="view2">NR主小区信息窗口</option>'+
                        '<option value="view3">LTE主小区信息窗口</option>'+
                        '<option value="view4">LTE主邻区信息窗口</option>'+
                        '<option value="view5">信令窗口</option>'+
                        '<option value="view6">事件窗口</option>'+
                        '<option value="view7">linechart窗口</option>'+
                        '</select>'+
                        //     '<input type="text" size="15" id="modulekey" />' +

                        '</form>',
                    //			content: '<form style="margin-left:20px"><p>\u6a21\u5757\u540d\uff1a</p><input type="text" size="15" id="modulename" /><p>\u6a21\u5757Code\uff1a</p><input type="text" size="15" id="modulekey" /><p>\u6a21\u5757\u4f4d\u7f6e\uff1a</p>\u5de6:<input type="radio" name="modulelayout" checked="checked" value="left"/>&nbsp&nbsp\u4e2d:<input type="radio" name="modulelayout" value="center"/>&nbsp&nbsp\u53f3:<input type="radio" name="modulelayout" value="right"/></form>',

                    icon: "add",
                    position: "center"
                })
            })
        }, _bindSave: function (a) {
            a.click(function () {
                var a = $("#" + Jh.Layout.location.left).sortable("toArray"),
                    b = $("#" + Jh.Layout.location.center).sortable("toArray"),
                    d = $("#" + Jh.Layout.location.right).sortable("toArray"), f = "";
                $("." + Jh.Config.layCls + " a").each(function () {
                    $(this).hasClass("active") && (f = $(this).attr("rel"))
                });


                var result = {
                    appL:{},
                    appM:{},
                    appR:{}
                };

                $.each(a,function(k,v){
                    result.appL[v] = Jh.Views[v];
                });
                $.each(b,function(k,v){
                    result.appM[v] =  Jh.Views[v];
                });
                $.each(d,function(k,v){
                    result.appR[v] =  Jh.Views[v];
                });



                $.each(Jh.Layout.layoutText,function(k,v){
                    if(f==v){
                        result.layout = k;
                    }
                })

                saveLayoutConfig(JSON.stringify(result));

                return result;

            })
        }, _layoutAClick: function () {

            $.each($(".layoutText"),function(k,v){
                $(v).click(function () {

                    var b = $(this), c = b.attr("rel");
                    console.log(c);
                    a._ToLayout(c);
                    b.parent().parent().find("a").removeClass("active");
                    b.addClass("active");
                })

            })


        }, _ToLayout: function (a) {
            var c = Jh.Layout.layoutCss, e = Jh.Layout.locationId, d = 0, f = "";
            $.each(Jh.Layout.layoutText, function (c, j) {
                a == j && (d = c)
            });
            $.each(e, function (a, b) {
                var g = $(b), h = c[d].split(/\s+/);
                switch (a) {
                    case "left":
                        a = 0;
                        break;
                    case "center":
                        a = 1;
                        break;
                    case "right":
                        a = 2
                }
                "wnone" == h[a] && (f = g.sortable("toArray"), $.each(f, function (a, b) {
                    $("#" + Jh.Layout.location.left).append($("#" + b))
                }), g.empty());
                g.removeClass("w250 w750 w500 w1000 wnone w330 w660").addClass(h[a])
            })
        }
    }
}();
Jh.fn = function (a) {
    var b = "<div id='" + Jh.Layout.location.left + "' class='" + Jh.Config._groupWrapperClass + " '/>",
        c = "<div id='" + Jh.Layout.location.center + "' class='" + Jh.Config._groupWrapperClass + " '/>",
        e = "<div id='" + Jh.Layout.location.right + "' class='" + Jh.Config._groupWrapperClass + " '/>",
        h = "<div id='portal_hide' class='" + Jh.Config._groupWrapperClass + " wnone '/>",
        d = "<div id='{key}' class='" + Jh.Config._groupItemClass + "'/>",
        f = "<div class='" + Jh.Config._groupItemHead + "'><h3>{name}</h3></div>",
        i = "<div id='{key}'  class='" + Jh.Config._groupItemContent + "'/>";

    var resizeFunction;

    return a = {
        init: function (b) {
            resizeFunction = b.resize;

            delete b.resize;
            a._create(b);
            delete b.layout;
            a._bindData(b);
            a._bindEvent()
        }, _create: function (b) {
            a.box = $("<div id='portal'></div>");
            a._elements = {};
            a._createModulesWrap(b);
            Jh.Util.toBody(a.box)
        }, _bindData: function (b) {
            $.each(b, function (b, c) {
                a._createPortal(b, c)
            })
        }, _createModulesWrap: function (config) {
            var num = config.layout;
            var layoutArray =  Jh.Layout.layoutCss[num].split(/\s+/);
            a._elements.m_l = $(b);
            a._elements.m_m = $(c);
            a._elements.m_r = $(e);
            a._elements.m_h = $(h);

            a._elements.m_l.addClass(layoutArray[0]);
            a._elements.m_m.addClass(layoutArray[1]);
            a._elements.m_r.addClass(layoutArray[2]);
            a._addPanel(a._elements.m_l);
            a._addPanel(a._elements.m_m);
            a._addPanel(a._elements.m_r);
            a._addPanel(a._elements.m_h);

        }, _addPanel: function (b) {
            a.box.append(b)
        }, _createPortal: function (b, c) {
            var d;
            switch (b) {
                case "appL":
                    d = a._elements.m_l;
                    break;
                case "appM":
                    d = a._elements.m_m;
                    break;
                case "appR":
                    d = a._elements.m_r
            }
            $.each(c, function (b, c) {
                d.append(a._createPortalOne(b, c));

                if(Jh.ViewColumns[b]){
                    a._createViewTable(b,Jh.ViewColumns[b]);
                }else if(b== "view7"){





                }

            })

        }, _createPortalOne: function (b, c) {
            var e = a._createItemHeader(b,c), f = a._createItemContent(b);
            return $(Jh.Util.format(d, {key: b})).append(e).append(f)
        }, _createItemHeader: function (r,c) {
            var b = $(Jh.Util.format(f, {name: c})), c = a._createDiv("action").appendTo(b);

            if(r=='view7'){
                a._createA(Jh.Config.refresh, Jh.Config.refreshtext,r, !0).appendTo(c);
            }


            a._createA(Jh.Config.min, Jh.Config.mintext,r,!0).appendTo(c);
            a._createA(Jh.Config.max, Jh.Config.maxtext,r,!1).appendTo(c);
            a._createA(Jh.Config.close, Jh.Config.closetext,r,!0).appendTo(c);
            /**      b.hover(function () {
                $(this).find(".action").show()
            }, function () {
                $(this).find(".action").hide()
            });  **/
            return b
        }, _createItemContent: function (b) {
            var a = $(Jh.Util.format(i,{key:b+"Div"}));
            $(Jh.Util.format("<table id='{key}'   class='viewTable datagrid-htable'   border='0' cellspacing='0' cellpadding='0' />" ,{key:b+"Table"})).appendTo(a);
            return a
        },_createViewTable:function(b,c){

            var tbody=$("<tbody class='datagrid-body' ></tbody>");

            $.each(c,function(cind,cit){

                //======表格头
                var tr = $("<tr style='height:30px;line-height:30px;' class ='textCenter datagrid-header'></tr>");

                $.each(cit,function(index,item){
                    var td = item.width ?   $("<td style='width:"+item.width+"'>"+item.title+"</td>") :  $("<td>"+item.title+"</td>")   ;
                    tr.append(td);
                });

                tbody.append(tr);
                //==============

                var _data;
                var _prefix;

                if(Jh.Config._listView.includes(b)){

                    _prefix = '';
                    if('view5' == b){
                        _data = Jh.Data.signData;
                    }else{
                        _data = Jh.Data.eventData;
                    }

                }else{
                    _prefix = "ie_";
                    _data = [Jh.Data.ieData[MyPlayer.Data.currentTime]];
                }


                $.each(_data,function(key,value){

                    //========表格体
                    tr = $("<tr style='height:25px;' ref='"+key+"'  class='textCenter "+b+"_"+key+"'   ></tr>");

                    $.each(cit,function(ind,it){

                        var td;
                        if(value){
                            var val = value[_prefix+it.field];
                            if( val !=0 && !val ){
                                val = '';
                            }
                            //            var td = _prefix ?  $("<td id='ie_"+it.field+"'>"+val+"</td>") :  it.format ?   $("<td  class='" +  b +"_"   + eval( it.format+"('"+val+"');" )+ "' >"+val+"</td>")   :  $("<td>"+val+"</td>");
                            var td = _prefix ?  $("<td id='ie_"+it.field+"'>"+val+"</td>") :   it.format ?    $("<td>" + eval( it.format+"('"+val+"');") + "</td>" )  : $("<td>"+val+"</td>");
//
                        }else{
                            td = $("<td id='ie_"+it.field+"'></td>");
                        }
                        tr.append(td);
                    });
                    tr.hover(function () {
                        $(this).addClass("datagrid-row-over");
                    }, function () {
                        $(this).removeClass("datagrid-row-over");
                    });


                    if(!_prefix){
                        a._bindRowClick(b,tr);
                    }

                    tbody.append(tr);

                    //========表格体

                });


            });

            $("#"+b+"Table").append(tbody);

        },_bindRowClick:function(b,r){
            var $this = this;
            r.click(function () {
                if(!MyPlayer.Data.playingStatus){

                    $("#"+b+" .datagrid-row-selected").removeClass("datagrid-row-selected");
                    $(this).addClass("datagrid-row-selected");

                    var time = $(this).find("td:first").text();

                    MyPlayer.Data.currentTime = time;
                    $this.synced(b);
                    MyPlayer.fn.sync(Jh.Config.syncSourceId);
                }
            });

            //信令窗口
            if(b=="view5"){
                r.dblclick(function () {
                    if(!MyPlayer.Data.playingStatus){

                        $("#signDetailDiv").dialog('open');
                        $("#signDetailDiv").window('center');
                        $("#signDetail").html();
                        $("#signDetail").html(Jh.Data.signData[r.attr('ref')].DetailMsg);
                    }
                });
            }
        },playOneFrame:function(){
            this._refresh();
        }
        ,synced:function(id){
            syncViewData(id);
        }, _createDiv: function (a) {
            return $("<div/>").addClass(a)
        }, _createA: function (a, b, c,d) {
            a = $("<a href='javascript:void(0);' class='" + a + "' title='" + b + "'  ref='"+c+"'  />");
            d || a.hide();
            return a
        }, _eventMin: function () {
            $("." + Jh.Config.min).live("click", function () {
                var a = $(this), b = a.parent().parent().parent();
                b.find("." + Jh.Config._groupItemContent).hide();
                b.find("." + Jh.Config.max).show();
                a.hide()
            })
        }, _eventMax: function () {
            $("." + Jh.Config.max).live("click", function () {
                var a = $(this), b = a.parent().parent().parent();
                b.find("." + Jh.Config._groupItemContent).show();
                b.find("." + Jh.Config.min).show();
                a.hide()
            })
        }, _eventRemove: function () {

            $("." + Jh.Config.close).live("click", function () {

                var c = $(this), b = c.attr("ref");
                $("#"+b).remove();
                //		a._elements.m_h.append($("#"+b));
            })
        }, _eventRefresh: function () {
            $("." + Jh.Config.refresh).live("click", function () {
                resizeFunction();
            })
        }, _eventSortable: function () {
            $("." + Jh.Config._groupWrapperClass).sortable({
                handle: "." +Jh.Config._groupItemHead,
                connectWith: "." + Jh.Config._groupWrapperClass,
                opacity: "0.6",
                dropOnEmpty: !0
            }).disableSelection()
        }, _bindEvent: function () {
            a._eventSortable();
            a._eventRefresh();
            a._eventRemove();
            a._eventMax();
            a._eventMin()
        },_refresh:function(data,id){

            //同步信令、事件窗口
            $.each(Jh.Config._listView,function(i,view){
                if(id === view){
                }else{
                    var target;
                    var dataArray;

                    if( view==='view5' ){
                        dataArray = Jh.Data.signData.map(c=>c.time);
                    }else{
                        dataArray = Jh.Data.eventData.map(c=>c.time);
                    }
                    dataArray.push(MyPlayer.Data.currentTime);
                    dataArray.sort();
                    var position =  dataArray.indexOf(MyPlayer.Data.currentTime);

                    if(position == 0){
                        target = 0;
                    }else if(position == dataArray.length - 1){
                        target = dataArray.length -2;
                    }else{
                        var before = new Date(MyPlayer.Data.currentTime).getTime()  - new Date(dataArray[position-1]).getTime();
                        var after =  new Date(dataArray[position+1]).getTime() - new Date(MyPlayer.Data.currentTime).getTime();
                        if(before <after){
                            target = position -1;
                        }else{
                            target = position;
                        }
                    }
                    var obj = $("."+view+"_"+target)[0];
                    //				var obj = $("."+ view +"_"+Jh.Util.dateSlice(MyPlayer.Data.currentTime)+":first")[0];
                    if(obj){
                        $("#"+view+"Div").animate({
                            scrollTop:obj.offsetTop
                        });
                        $("#"+view+" .datagrid-row-selected").removeClass("datagrid-row-selected")
                        $(obj).addClass("datagrid-row-selected");
                    }else{
                        //console.log("."+ view +"_"+  Jh.Util.dateSlice(MyPlayer.Data.currentTime) +":first");
                    }
                }
            });

            var existData = true;
            //同步ie窗口
            if(!data){
                data = Jh.Data.ieData[new Date(MyPlayer.Data.currentTime).Format("yyyy-MM-dd hh:mm:ss")];
            }

            if(!data){
                //console.log(MyPlayer.Data.currentTime);
                existData = false;
            }

            if(Jh.Config._allIe.length>0){
                $.each(Jh.Config._allIe,function(iii,item){

                    if(existData){
                        $("#ie_"+item).html(data["ie_"+item]);
                    }else{
                        $("#ie_"+item).html('');
                    }

                });
            }else{

                $.each(Jh.ViewColumns,function(i,columns){
                    if(!Jh.Config._listView.includes(i)){
                        $.each(columns,function(ii,column){
                            $.each(column,function(iii,item){
                                Jh.Config._allIe.push(item.field);
                                if(existData){
                                    $("#ie_"+item.field).html(data["ie_"+item.field]);
                                }else{
                                    $("#ie_"+item.field).html('');
                                }
                            });
                        })
                    }

                });
            }



        },_addNewPortal:function(b,c){

            var d = a._elements.m_l;

            if($("#"+b).length>0){
                //          d.append($("#"+b));
                //           $("#"+b).show();
            }else{

                if(b=='view7'){
                    d.append(a._createPortalOne(b, c));
                    MyChart.fn.init();

                }else{
                    d.append(a._createPortalOne(b, c));
                    a._createViewTable(b,Jh.ViewColumns[b]);
                }


            }
        }
    }
}();
