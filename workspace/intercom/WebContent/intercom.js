/**
 * 
 */

var vid = new Video($('.localVideo')[0]);//,$('.remoteVideo')[0]);
var vidr=null;

$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
       return null;
    }
    else{
       return results[1] || 0;
    }
}

var main = function() {

	if ($.urlParam('remote') != "yes")
		vid.initLocal();
	
	$('.getRemote').click(function() {
		console.log('getRemove click');
		vidr = new Video($('.remoteVideo')[0]);
		vidr.getCallInitiatorVideo();
	});
}

$(document).ready(main);