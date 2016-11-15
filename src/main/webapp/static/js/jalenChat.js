var stompClient = null;

$(document).ready(function() {
	connect();
});

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$("#messages").html("");
}

function connect() {
	var socket = new SockJS('/csm/ws');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		stompClient.subscribe('/topic/chat/message/' + userId,
				function(message) {
					showMessage(JSON.parse(message.body));
				});
		stompClient.subscribe('/app/chatCInitMessage', function(message) {
			showMessages(JSON.parse(message.body));
		});
	});
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
}

function sendMessage() {
	stompClient.send('/app/chatCSendMessage', {}, JSON.stringify({
		'message' : $("#message").val()
	}));
}

function showMessages(messageList) {
	for (var i = 0; i < messageList.length; i++) {
		showMessage(messageList[i]);
	}
}

function showMessage(message) {
	if (message.fromAdmin) {
		$("#messages").append(
				"<tr><td>" + message.senderName + "</td><td>"
						+ message.dateTime + "</td></tr><tr><td>"
						+ message.message + "</td></tr>");
	} else {
		$("#messages").append(
				"<tr><td align=right>" + message.senderName
						+ "</td><td align=right>" + message.dateTime
						+ "</td></tr><tr><td align=right>" + message.message
						+ "</td></tr>");
	}
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendMessage();
	});
});