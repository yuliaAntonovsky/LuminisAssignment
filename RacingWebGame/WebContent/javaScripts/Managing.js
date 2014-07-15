/**
 * 
 */

var name = "";
var playerId = "";
var command = "";
var groundColor = "";
var annimationTimeout = 10000;
var isStarted = false;
var boomImg = new Image();
boomImg.src = "Images/boom.png";
var carImages = new Array();;
var maxPlayersNumber = 0;

function loadImages() {
	
	for (var i=0; i<maxPlayersNumber; i++) {
		carImages[i] = new Image();
		carImages[i].src = "Images/car" + i + ".png";
	}
}

$.ajax({
	 url:"CheckGameStarted",
	 contentType: "application/json; charset=utf-8",
     dataType: "json",
     cache: false,
	 success:function(result){
		 var playersNum = result.playersNumber;
		 annimationTimeout = result.annimationTimeout;
		 maxPlayersNumber = result.maxNumberOfPlayers;
		 loadImages();
		 if ( playersNum > 0 ) {
			 $('#startGame').hide(); 
			 $('#gameSartedMsg').show(); 
			 playerId = "0";
			 refreshPlayZone();
		 }
	 },
	 error:function(result){
	 	alert("Unknown error");
	 	location.reload(true);
	 }
});

	
function submitGameForm() {
	name = $('#playerName').val();
	if (name.length < 1) {
		$('#errorMsg').show();
	} else {
		 $.ajax({
			 url:"LoginToGame?playerName=" + name,
			 contentType: "application/json; charset=utf-8",
			 dataType: 'json',
			 cache: false,
			 success:function(result){
				 $('#startGame').hide();
				 playerId = result.playerId;
				 if ( playerId == "0" ) {
					 $('#gameSartedMsg').show(); 
				 } else {
					 var playerText = $('#helloPlayer').text();
					 playerText = playerText.replace("PlayerName",result.playerName);
					 $('#helloPlayer').text(playerText);
					 document.getElementById("playerName").innerHTML = result.playerName;
					 var imgSrc = "<p> Your car is: <img src='Images/car" + + result.orderNum + ".png'>" + "</img></p>";
					 $('#playerCarImg').html(imgSrc);
					 $('#gameWaitingToStartMsg').show(); 
				 }
				 refreshPlayZone();
			 },
		 	error:function(result){
		 		alert("Error during login");
		 		location.reload(true);
			 }
		 });
	}
}

function refreshPlayZone() {
	var urlParams = "";
	if ( isStarted ) {
		urlParams = "&command=" + command  + "&groundColor=" + groundColor;
	}
	$.ajax({
		url:"GameManager?playerId=" + playerId + urlParams, 
		contentType: "application/json; charset=utf-8",
		dataType: 'json',
		cache: false,
		success: function(result) {
			context.clearRect(0,0,ctxW,ctxH);
			context.drawImage(track, 0, 0);
			var gameStatus = result.isStarted;
			if ( typeof(gameStatus) != "undefined" && gameStatus == "Y" ) {
				var msg = result.message;
				if ( typeof(msg) != "undefined" && msg.length > 0 ) {
					letsStartTheGame(msg);
				} else {
					drawGameArea(result.players);
					updateScreenMsg(result.state);
					displayComment(result.comment);
					countGroundColor(result.xCoordinate, result.yCoordinate, result.rotation);
				}
			} else if ( typeof(gameStatus) != "undefined" && gameStatus == "F" ) {
				showResults(result.players);
				var timeOut = result.resultTimeout;
				if ( typeof(timeOut) == "undefined" ) timeOut = 30000;
				annimationTimeout = timeOut*2;
				setTimeout("location.reload(true);", timeOut*1);
			}
			
		},
		complete: function() {
			command="";
			setTimeout(refreshPlayZone, annimationTimeout);
		},
		error:function(result){
			alert(result.responseText);
			alert("Error during counting next step, you will have to wait for next round to start play again");
			location.reload(true);
		}
	});
}

function letsStartTheGame(msg) {
	 isStarted = true;
	 alert(msg);
	 $('#gameWaitingToStartMsg').hide();
	 $('#gamePlayMsg').show(); 
}

function countGroundColor(x, y, rotation) {
	if ( isStarted && typeof(x) != "undefined" && typeof(y) != "undefined" && typeof(rotation) != "undefined") {
		groundColor = hit.isHit(x, y, rotation);
	} else {
		groundColor = "";
	}
}

function drawGameArea(players) {
	if ( typeof(players) == "undefined" ) return;
	context.clearRect(0,0,ctxW,ctxH);
	context.drawImage(track, 0, 0);
	drawTheCars(players);
}

function drawTheCars(carsDetails) {
	for ( var i=0; i<carsDetails.length; i++ ) {
		var carDetails = carsDetails[i];
		if ( typeof(carDetails) != "undefined" ) {
			if ( carDetails.state == "PLAY" ) {
				drawRotatedImage(carImages[carDetails.orderNum], carDetails.xCoordinate, carDetails.yCoordinate, carDetails.rotation);
			} else if ( isStarted && carDetails.state == "CRASHED" ) {
				drawRotatedImage(boomImg, carDetails.xCoordinate, carDetails.yCoordinate, carDetails.rotation);
			}
			
		}
	}
}

function displayComment(comment) {
	if ( typeof(comment) != "undefined" && comment.length > 0) {
		alert(comment);
	}
}

function updateScreenMsg(state) {
	if ( typeof(state) != "undefined" && ( state == "FINISHED" || state == "CRASHED") ) {
		$('#gamePlayMsg').hide();
		$('#gameFinishedMsg').show();
		isStarted = false;
	}
}

function showResults(data) {
	$('#playZone').hide(); 
	$('#gameResults').show(); 
	if ( typeof(data) != "undefined" ) {
		var len = data.length;
        var txt = "";
        if( len > 0 ){
            for( var i=0; i<len; i++ ){
                if ( data[i].playerName && data[i].state && data[i].place && data[i].finishTime ) {
                    txt += "<tr><td>" + data[i].playerName + "</td><td>" + data[i].state + "</td>"
                    	+ "<td>" + data[i].place + "</td><td>" + data[i].finishTime + "</td></tr>";
                }
            }
            if(txt != ""){
                $("#resultTable").append(txt);
            }
        }
	}
	
}

//Keyboard event listeners
$(window).keydown(function(e){
	command = e.keyCode;
});

function drawRotatedImage(image, x, y, angle) {
	 
	// save the current co-ordinate system
	// before we screw with it
	context.save();
 
	// move to the middle of where we want to draw our image
	context.translate(x, y);
 
	// rotate around that point, converting our
	// angle from degrees to radians
	context.rotate(angle * TO_RADIANS);
 
	// draw it up and to the left by half the width
	// and height of the image
	context.drawImage(image, -(image.width/2), -(image.height/2));
 
	// and restore the co-ords to how they were when we began
	context.restore();
}

var ctrlDown = false;
var ctrlKey = 17, f5Key = 116, rKey = 82;

$(document).keydown(function( e ) {
    if( e.keyCode == f5Key )
    {
        e.preventDefault( );
    }

    if( e.keyCode == ctrlKey )
        ctrlDown = true;
    if( ctrlDown && ( e.keyCode == rKey ) )
    {
        e.preventDefault( );
    }

}).keyup(function(e) {
    if (e.keyCode == ctrlKey)
        ctrlDown = false;
});


