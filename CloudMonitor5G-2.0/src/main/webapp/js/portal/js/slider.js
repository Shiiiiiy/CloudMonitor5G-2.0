var MySlider = {
	Config:{
		syncSourceId:'slider'
	},
};

MySlider.fn = function (a) {

	var syncOther =true;

	function getSliderTime(value,pattern){
		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		t = t*value/100;
		var nd =  new Date(	MyPlayer.Data.startTime);
		nd.setMilliseconds(nd.getMilliseconds()+t);
		return  nd.Format(pattern);
	}

	function setSliderValue(){
		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		var t2 =  new Date(MyPlayer.Data.currentTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;

		var value = t2/t*100;
		$('#timer').slider('setValue',value);
	}

	return a = {
		init: function (b) {

			b.slider({
				showTip:true,
				tipFormatter: function(value){
			//		return	new Date(MyPlayer.Data.currentTime).Format("hh:mm:ss");
						return  getSliderTime(value,"hh:mm:ss");
				},
				rule:[ new Date(MyPlayer.Data.startTime).Format("hh:mm:ss") ,new Date(MyPlayer.Data.endTime).Format("hh:mm:ss") ],
				onChange:function(newValue,oldValue){
					if(syncOther){
						MyPlayer.Data.currentTime = getSliderTime(newValue,"yyyy-MM-dd hh:mm:ss");
						var ct = MyPlayer.Data.currentTime;
						setTimeout(function(){
							if(ct === MyPlayer.Data.currentTime){
								MyPlayer.fn.sync(MySlider.Config.syncSourceId);
							}
						},500);
					}
					syncOther = true;
				}
			});
			//年月日 时分秒的样式
			//$(".slider-rulelabel").find("span:last-child").css("margin-left","-117px");
			//时分秒的样式
			$(".slider-rulelabel").find("span:last-child").css("margin-left","-27px");
			$(".slider-rulelabel").css('top',0);
			$(".slider-rulelabel span").css('height','20px').css('line-height','20px').css('top','10px');
		},synced:function(){
			syncOther = false;
			setSliderValue();
		}

	}
}();
