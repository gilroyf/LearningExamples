/**
 * 
 */

var restServiceChannel = {
		url: "rest/intercomserver",
		sendOffer: function(desc, group) {
			$.ajax({
				  url:this.url,
				  type:"POST",
				  data:JSON.stringify( {"type":"initiateCall", "group":group, "sdp":desc}),
				  contentType:"application/json",
				  dataType:"json",
				  success: function(){
					  console.log("done");
				  }, 
				  error: function(a,b,c) {
					  console.log("error"+b+" "+c );
				  }
				});
		},
		onOffer: function(video, cb, groupName) {
			_vid=video;
			$.ajax({
				  url:this.url+'/'+groupName,
				  type:"GET",
				  dataType:"json",
				  success: 	function(data){ 
					  			cb.call(_vid,data);//_vid.__restSuccessCB(data);
					  		},
				  error: function(a,b,c) {
					  console.log("error"+b+" "+c );
				  }
				}
			);
			
		},
		sendAnswer: function(desc, group) {
			$.ajax({
				  url:this.url,
				  type:"POST",
				  data:JSON.stringify( {"type":"answerResponse", "group":group, "sdp":desc}),
				  contentType:"application/json",
				  dataType:"json",
				  success: function(){
					  console.log("done");
				  }, 
				  error: function(a,b,c) {
					  console.log("error"+b+" "+c );
				  }
				});
		},
		onAnswer: function(video, cb, groupName) {
			id = setInterval( function(u, _vid, cb, groupName) {
				$.ajax({
					  url:u+'/answer/'+groupName,
					  type:"GET",
					  dataType:"json",
					  success: 	function(data){ 
//						  			var obj = JSON.parse(data[0]);
						  			if (data[0]['error']!==undefined)
						  				clearInterval(id);
						  			cb.call(_vid,data);//_vid.__restSuccessCB(data);
						  		},
					  error: function(a,b,c) {
						  console.log("error"+b+" "+c );
					  }
					}
				);
			},3000, this.url, video, cb, groupName);
			
		}
}