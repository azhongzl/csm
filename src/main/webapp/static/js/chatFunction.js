var stompClient = null;
$(document).ready(function() {
	connect();
});

function connect() {
	var socket = new SockJS('/csm/ws');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/topic/chat/message/' + userId,
				function(message) {
					showMessage(JSON.parse(message.body));
					document.getElementById('sentence').scrollTop = document
							.getElementById('sentence').scrollHeight;
				});
		stompClient.subscribe('/app/chatCInitMessage', function(message) {
			showMessages(JSON.parse(message.body));
		});
	});
}

function sendMessage() {
	if($("#message").val().length>0){
	stompClient.send('/app/chatCSendMessage', {}, JSON.stringify({
		'message' : $("#message").val()
	}));
	$("#message").val("");
	}
}

function showMessages(messageList) {
	for (var i = 0; i < messageList.length; i++) {
		showMessage(messageList[i]);
	}
	document.getElementById('sentence').scrollTop = document
			.getElementById('sentence').scrollHeight;
}

function showMessage(message) {
	if (message.fromAdmin) {
		$("#sentence").append(
				"<p class='user1 left'>" + message.senderName + "</p></br>"
						+ "<p class='speech1 left'>" + message.message
						+ "&nbsp;&nbsp;&nbsp;&nbsp;" + message.createDateTime
						+ "</p><hr>");
	} else {
		$("#sentence").append(
				"<p class='user2 right'>" + message.senderName + "</p></br>"
						+ "<p class='speech2 right'>" + message.message
						+ "&nbsp;&nbsp;&nbsp;&nbsp;" + message.createDateTime
						+ "</p><hr>");
	}
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});

});