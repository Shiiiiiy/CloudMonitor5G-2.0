var MyPlayer = {
	
	Config:{
		
		
		
		
	},
	Data : {
		speed:1,
		startTime:'',
		currentTime:'',
		endTime:'',
		playList:[
			{logsId:'1',logName:'日志1'},
			{logsId:'2',logName:'日志2'},
			{logsId:'2',logName:'日志3'}
		],
		playIndex:0,
		playingStatus:false,
		task:{}
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
		},play:function(){
			
			MyPlayer.Data.playingStatus = true;
			
			MyPlayer.Data.task = setInterval(function(){
				
				var currentTime = a.calcCurrentTime();
				
			
				playFunction(currentTime);
			
			},1000);
			
			
			
			
		},pause:function(){
			
			MyPlayer.Data.playingStatus = false;
			
			clearInterval(MyPlayer.Data.task);
			
			pauseFunction();
			
			
		},stop:function(b){
			
		},fastForward:function(){
			
		},rewind:function(){
			
		},calcCurrentTime:function(){
			
			var date =  new Date(MyPlayer.Data.currentTime);
			date.setSeconds(date.getSeconds()+MyPlayer.Data.speed);
			MyPlayer.Data.currentTime = date.Format("yyyy-MM-dd hh:mm:ss");
			
			return MyPlayer.Data.currentTime;
		}
    }
}();
