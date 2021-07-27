var MyPlayer = {

	Config:{

	},
	Data : {

		speed:1,
		startTime:'',
		currentTime:'',
		endTime:'',
		playList:[

		],
		logId:'',
		playIndex:0,
		playingStatus:false,
		task:{},

		layoutArr:[],
		layoutId:''
	}
};



MyPlayer.fn = function (a) {

	function calcNextFrameTime(){


		var date =  new Date(MyPlayer.Data.currentTime);
		date.setSeconds(date.getSeconds()+MyPlayer.Data.speed);
		MyPlayer.Data.currentTime = date.Format("yyyy-MM-dd hh:mm:ss");

		if(MyPlayer.Data.currentTime<MyPlayer.Data.startTime){
			MyPlayer.Data.currentTime = MyPlayer.Data.startTime;
		}
		if(MyPlayer.Data.currentTime>MyPlayer.Data.endTime){
			MyPlayer.Data.currentTime = MyPlayer.Data.endTime;
		}
		return MyPlayer.Data.currentTime;

	}



	return a = {
		init: function () {

		},sync:function(sourceId){
			//需要等其他组件初始化好了之后，动态设置该方法
		},playOnce:function(){
			Jh.fn.playOneFrame();
			MyChart.fn.synced();
			MySlider.fn.synced();
			syncMap();
		},nextFrame:function (){
			calcNextFrameTime();
			this.playOnce();
			this.pause();
		},restart:function(){
			this.stop();
			this.play();
		},play:function(){
			MyChart.fn.disableClick();
			var $this =this;
			$("#tplay").hide();
			$("#tpause").show();

			if(!MyPlayer.Data.playingStatus){
				MyPlayer.Data.playingStatus = true;
				MyPlayer.Data.task = setInterval(function(){
					calcNextFrameTime();
					$this.playOnce();
					if(MyPlayer.Data.currentTime >= MyPlayer.Data.endTime && MyPlayer.Data.speed >0){
						MyPlayer.Data.currentTime = MyPlayer.Data.endTime;
						$this.pause();
						$("#tplay").show();
						$("#tpause").hide();
						return;
					}
					if(MyPlayer.Data.currentTime<=MyPlayer.Data.startTime && MyPlayer.Data.speed < 0){
						MyPlayer.Data.currentTime = MyPlayer.Data.startTime;
						$this.pause();
						$("#tplay").show();
						$("#tpause").hide();
					}

				},1000);
			}
		},pause:function(){
			MyChart.fn.enableClick();
			$("#tplay").show();
			$("#tpause").hide();
			if(MyPlayer.Data.playingStatus){
				MyPlayer.Data.playingStatus = false;
				clearInterval(MyPlayer.Data.task);
			}
		},stop:function(){
			MyPlayer.Data.currentTime = MyPlayer.Data.startTime;
			this.pause();
			this.playOnce();
		},fastForward:function(){

			if(MyPlayer.Data.speed >=16){

			}else if(MyPlayer.Data.speed>0){
				MyPlayer.Data.speed = MyPlayer.Data.speed*2;
			}else if(MyPlayer.Data.speed <-1){
				MyPlayer.Data.speed = MyPlayer.Data.speed/2;
			}else{
				MyPlayer.Data.speed = 1;
			}

			$("#speedSpan").html(MyPlayer.Data.speed +"X");

		},rewind:function(){

			if(MyPlayer.Data.speed <=-16){
				return;
			}else if(MyPlayer.Data.speed<0){
				MyPlayer.Data.speed = MyPlayer.Data.speed*2;
			}else if(MyPlayer.Data.speed >1){
				MyPlayer.Data.speed = MyPlayer.Data.speed/2;
			}else{
				MyPlayer.Data.speed = -1;
			}
			$("#speedSpan").html(MyPlayer.Data.speed +"X");
		}
	}
}();
