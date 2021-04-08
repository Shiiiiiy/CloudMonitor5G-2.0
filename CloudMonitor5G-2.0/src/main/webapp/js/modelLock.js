/**
 * 不同制式类型对应的不同网络制式锁定下拉框内容
 */
var modelLocks={
	GSM:[],
	CDMA:[],
	FG:[],
	TDS:[{text:"T/G双模切换",value:2,selected:true},{text:"锁定G网",value:1},{text:"锁定T网",value:0}],
	WCDMA:[{text:"W/G双模切换",value:2,selected:true},{text:"锁定G网",value:1},{text:"锁定W网",value:0}],
	EVDO:[{text:"1X/EVDO混合模式",value:2,selected:true},{text:"锁定EVDO",value:1},{text:"锁定1X网",value:0}],
	TD_LTE:[{text:"TDL/TDS双模切换",value:2,selected:true},{text:"锁定TD-SCDMA网",value:1},{text:"锁定TD-LTE网",value:0},{text:"锁F频",value:7},{text:"锁D频",value:8},{text:"锁E频",value:9}],
	FDD_LTE:[{text:"TDL/TDS双模切换",value:2,selected:true},{text:"锁定TD-SCDMA网",value:1},{text:"锁定TD-LTE网",value:0}],
	NB_IoT:[{text:"TDL/TDS双模切换",value:2,selected:true},{text:"锁定TD-SCDMA网",value:1},{text:"锁定TD-LTE网",value:0}],
	eMTC:[{text:"TDL/TDS双模切换",value:2,selected:true},{text:"锁定TD-SCDMA网",value:1},{text:"锁定TD-LTE网",value:0}],
	APP单站验证:[],
	APP_FG:[]
};
