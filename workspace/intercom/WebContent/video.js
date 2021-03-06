/**
 * 
 */

function Video(localVideoDiv) {
	this.localVDiv = localVideoDiv;
	this.localMediaStream = null;
	var peerConnection = this.__getPeerConnection();
	this.pcr = new peerConnection();
	
}

Video.prototype.getCallInitiatorVideo = function() {
	var _vid = this;
	this.pcr.onaddstream = function (evt) {
		_vid.localVDiv.src = URL.createObjectURL(evt.stream);
    };
    this.__getUserMediaRemoteSuccessCB(null);
}


Video.prototype.initLocal= function() {
	if (!this.__hasUserMedia()) {
		console.log("No usermedia Returning");
	}
	var obj = this;
	navigator.getUserMedia = this.__getMedia();//navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;// _getMedia();
	navigator.getUserMedia({video:true, audio:true}, 
			function(ms) {
				obj.__getUserMediaSuccessfulCB(ms);
			}, 
			function(){
				obj.__getUserMediaErrorCB();
			}
	);

}

Video.prototype.__getUserMediaRemoteSuccessCB = function (stream) { 
	var _vid = this;
	//this.pcr.addStream(stream);
	restServiceChannel.onOffer(_vid, _vid.__restSuccessCB, 'group1');
}

Video.prototype.__getUserMediaSuccessfulCB = function(ms) {
	var video = this.localVDiv;
	video.src = window.URL.createObjectURL(ms);
	this.localMediaStream =  ms;
	
	//send offer to server
	this.pcr.addStream(ms);
	var obj = this;
	this.pcr.createOffer(
			function(desc) {
				obj.__gotDescription(desc);
			}, 
			function(e) {
				log.console(e);
			}
	);
	
}

Video.prototype.__gotDescription = function(desc) {
	this.pcr.setLocalDescription(desc);
	// send desciption to server
	restServiceChannel.sendOffer(desc, "group1");
	restServiceChannel.onAnswer(this,this.__gotAnswer, 'group1');
}

Video.prototype.__gotAnswer = function(data) {
	var desc = window.RTCSessionDescription || window.mozRTCSessionDescription || window.webkitRTCSessionDescription;
	var x = new desc(data[0]);
	this.pcr.setRemoteDescription(x);	
}

Video.prototype.__getUserMediaErrorCB = function() {
	
	
}

Video.prototype.__getPeerConnection = function() {
	return window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
}

Video.prototype.__hasUserMedia = function() {
	return !!this.__getMedia();//(navigator.getUserMedia || navigator.webkitGetUserMedia ||navigator.mozGetUserMedia || navigator.msGetUserMedia);
}

Video.prototype.__getMedia = function () {
	return 	navigator.getUserMedia || navigator.webkitGetUserMedia || 
			navigator.mozGetUserMedia || navigator.msGetUserMedia;	
}


var y=null;
Video.prototype.__restSuccessCB = function(data) {
	console.log("done data = ", data[0]);
	var desc = window.RTCSessionDescription || window.mozRTCSessionDescription || window.webkitRTCSessionDescription;
	var x = new desc(data[0]);
	var pcrObj = this.pcr;
	y=pcrObj;
	pcrObj.setRemoteDescription(x);
	pcrObj.createAnswer(
			  function(answer) {
				  y.setLocalDescription(answer);
				  // TODO remove next line
	//			  vid.pcr.setRemoteDescription(answer);
				  restServiceChannel.sendAnswer(answer, 'group1');
			  }  ,
			  function(err) {
				  console.log(err);
			  }
	);
	
}

