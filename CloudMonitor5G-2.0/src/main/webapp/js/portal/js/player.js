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
	

	
	var playFunction;
	var pauseFunction;
	var playOnceFunction;
    return a = {
		init: function (b) {
			
			playOnceFunction = b.playOnce;
			playFunction = b.play;
			pauseFunction = b.pause;
	
	
        },playOnce:function(){
			playOnceFunction(MyPlayer.Data.currentTime);

		},next:function (){
			this.calcCurrentTime();
			this.playOnce();
			setSliderValue();
			this.pause();
		}
		,play:function(){

			var $this =this;
			$("#tplay").hide();
			$("#tpause").show();

			if(!MyPlayer.Data.playingStatus){

				MyPlayer.Data.playingStatus = true;

				MyPlayer.Data.task = setInterval(function(){

					var currentTime = a.calcCurrentTime();

					if(currentTime>MyPlayer.Data.endTime){

						currentTime = MyPlayer.Data.endTime;
						$this.playOnce();
						setSliderValue();
						$this.pause();

						$("#tplay").show();
						$("#tpause").hide();
						return;
					}
					if(currentTime<MyPlayer.Data.startTime){
						currentTime = MyPlayer.Data.startTime;
						$this.playOnce();
						setSliderValue();
						$this.pause();
						$("#tplay").show();
						$("#tpause").hide();
						return;
					}

					playFunction(currentTime);

				},1000);

			}

			
			
			
		},pause:function(){

			$("#tplay").show();
			$("#tpause").hide();

			if(MyPlayer.Data.playingStatus){
				MyPlayer.Data.playingStatus = false;

				clearInterval(MyPlayer.Data.task);

				pauseFunction();


			}
			
			
		},stop:function(){
			MyPlayer.Data.currentTime = MyPlayer.Data.startTime;
			this.pause();
			this.playOnce();
			setSliderValue();
		},fastForward:function(){

			if(MyPlayer.Data.speed >=16){

			}else if(MyPlayer.Data.speed>0){
				MyPlayer.Data.speed = MyPlayer.Data.speed*2;
			}else if(MyPlayer.Data.speed <-1){
				MyPlayer.Data.speed = MyPlayer.Data.speed/2;
			}else{
				MyPlayer.Data.speed = 1;
			}

			$("#speedSpan").html(	MyPlayer.Data.speed +"X");

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

			$("#speedSpan").html(	MyPlayer.Data.speed +"X");

		},calcCurrentTime:function(){
			
			var date =  new Date(MyPlayer.Data.currentTime);
			date.setSeconds(date.getSeconds()+MyPlayer.Data.speed);
			MyPlayer.Data.currentTime = date.Format("yyyy-MM-dd hh:mm:ss");
			
			return MyPlayer.Data.currentTime;
		}
    }
}();
