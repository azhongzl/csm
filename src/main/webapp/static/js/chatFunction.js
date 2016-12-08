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
			showMessages(JSON.parse(message.body).data.messageList);
		});
	});
}

function sendMessage() {
	if ($("#message").val().length > 0) {
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
	var timeStr = "";
	var num = message.createDateTime.indexOf("T");
	timeStr = message.createDateTime.substring(0, num) + " "
			+ message.createDateTime.substring(num + 1, 19);
	if (message.fromAdmin) {
		$("#sentence")
				.append(
						"<div class='panel panel-primary' style='clear:both;float:right;width:400px'><div class='panel-heading' style='padding: 2px 0px ' >"
								+ "Customer Service"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;"
								+ timeStr
								+ "</div><div class='panel-body'>"
								+ message.message + " </div></div>");
	} else {
		$("#sentence")
				.append(
						"<div class='panel panel-info' style='clear:both;float:left;width:400px'><div class='panel-heading' style='padding: 2px 0px ' >"
								+ message.senderName
								+ "&nbsp;&nbsp;&nbsp;&nbsp;"
								+ timeStr
								+ "</div><div class='panel-body'>"
								+ message.message + " </div></div>");
	}
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});

});