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
	$("#customerSet").html("");
}

var subscribeList;

function connect() {
	var socket = new SockJS('/csm/ws');
	stompClient = Stomp.over(socket);
	var headers = {
		admin : true
	};
	stompClient.connect(headers, function(frame) {
		subscribeList = [];
		setConnected(true);
		stompClient.subscribe('/app/chatAInit', function(message) {
			showCustomerSet(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/chat/login', function(message) {
			showOnlineCustomer(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/chat/logout', function(message) {
			removeOnlineCustomer(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/chat/addUnhandledCustomer',
				function(message) {
					showUnhandledCustomer(JSON.parse(message.body));
				});
		stompClient.subscribe('/topic/chat/removeUnhandledCustomer',
				function(message) {
					removeUnhandledCustomer(JSON.parse(message.body));
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
	stompClient.send('/app/chatASendMessage', {}, JSON.stringify({
		'message' : $("#message").val(),
		'roomId' : $("#roomId").val()
	}));
}

function showMessages(messageList) {
	$("#messages").html("");
	for (var i = 0; i < messageList.length; i++) {
		showMessage(messageList[i]);
	}
}

function showMessage(message) {
	if(message.fromAdmin){
	$("#messages").append(
			"<tr><td align=right>" + message.senderName + "</td><td align=right>" + message.createDateTime
					+ "</td></tr><tr><td align=right>" + message.message + "</td></tr>");
	}else{
		$("#messages").append(
				"<tr><td>" + message.senderName + "</td><td>" + message.createDateTime
						+ "</td></tr><tr><td>" + message.message + "</td></tr>");
	}
}

function showCustomerSet(customerSet) {
	for (var i = 0; i < customerSet.length; i++) {
		showCustomer(customerSet[i]);
	}
}

function showCustomer(customer) {
	var customerId = customer.userId;
	$("#customerSet").append(
			"<div id='customer-" + customerId + "'><tr><td>" + customer.username
					+ "</td></tr></div>");
	$("#customer-" + customerId).click(
			function() {
				for (var i = 0; i < subscribeList.length; i++) {
					subscribeList[i].unsubscribe();
				}
				subscribeList = [];

				var subApp = stompClient.subscribe('/app/chatAInitMessage/'
						+ customerId, function(message) {
					showMessages(JSON.parse(message.body));
				});
				var subTopic = stompClient.subscribe('/topic/chat/message/'
						+ customerId, function(message) {
					showMessage(JSON.parse(message.body));
				});
				subscribeList.push(subApp);
				subscribeList.push(subTopic);

				$("#roomId").val(customerId);
			});

	if (customer.online) {
		showOnlineCustomer(customer);
	}
	if (customer.unhandled) {
		showUnhandledCustomer(customer);
	}
}

function showOnlineCustomer(customer) {
	$("#customer-" + customer.userId).css('color', 'red');
}

function removeOnlineCustomer(customer) {
	$("#customer-" + customer.userId).css('color', 'black');
}

var intervalMap = new Map();

function showUnhandledCustomer(customer) {
	var userId = customer.userId;
	if (!intervalMap.has(userId)) {
		var interval = setInterval(function() {
			$("#customer-" + userId).fadeOut(100).fadeIn(100);
		}, 200);
		intervalMap.set(userId, interval);
	}
}

function removeUnhandledCustomer(customer) {
	var roomId = customer.userId;
	if(intervalMap.has(roomId)){
		clearInterval(intervalMap.get(roomId));
		intervalMap.delete(roomId);
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