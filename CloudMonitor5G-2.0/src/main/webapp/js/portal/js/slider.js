var MySlider = {
	Config:{
		syncSourceId:'slider'
	},
};

MySlider.fn = function (a) {
	
	var syncOther =true;
	
	function getSliderTime(value){
		var t =  new Date(	MyPlayer.Data.endTime).getTime() - new Date(MyPlayer.Data.startTime).getTime() ;
		t = t*value/100;
		var nd =  new Date(	MyPlayer.Data.startTime);
		nd.setMilliseconds(nd.getMilliseconds()+t);
		return  nd.Format("yyyy-MM-dd hh:mm:ss");
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
					return  getSliderTime(value);
				},
				rule:[MyPlayer.Data.startTime,'|',MyPlayer.Data.endTime],
				onChange:function(newValue,oldValue){
					MyPlayer.Data.currentTime = getSliderTime(newValue);
					if(syncOther){
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
			$(".slider-rulelabel").find("span:last-child").css("margin-left","-117px");
			
        },synced:function(){
			syncOther = false;
			setSliderValue();
		}
		
    }
}();
