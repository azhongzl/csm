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
		stompClient.subscribe('/topic/chat/unhandledCustomer',
				function(message) {
					showUnhandledCustomer(JSON.parse(message.body));
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
		'recipient' : $("#recipient").val()
	}));
	var customer = $("#recipient").val();
	$("#customer-" + customer).stop();
	if(intervalMap.has(customer)){
		clearInterval(intervalMap.get(customer));
		intervalMap.delete(customer);
	}
}

function showMessages(messageList) {
	$("#messages").html("");
	for (var i = 0; i < messageList.length; i++) {
		showMessage(messageList[i]);
	}
}

function showMessage(message) {
	$("#messages").append(
			"<tr><td>" + message.sender + "</td><td>" + message.dateTime
					+ "</td></tr><tr><td>" + message.message + "</td></tr>");
}

function showCustomerSet(customerSet) {
	for (var i = 0; i < customerSet.length; i++) {
		showCustomer(customerSet[i]);
	}
}

function showCustomer(customer) {
	var customerName = customer.username;
	$("#customerSet").append(
			"<div id='customer-" + customerName + "'><tr><td>" + customerName
					+ "</td></tr></div>");
	$("#customer-" + customerName).click(
			function() {
				for (var i = 0; i < subscribeList.length; i++) {
					subscribeList[i].unsubscribe();
				}
				subscribeList = [];

				var subApp = stompClient.subscribe('/app/chatAInitMessage/'
						+ customerName, function(message) {
					showMessages(JSON.parse(message.body));
				});
				var subTopic = stompClient.subscribe('/topic/chat/message/'
						+ customerName, function(message) {
					showMessage(JSON.parse(message.body));
				});
				subscribeList.push(subApp);
				subscribeList.push(subTopic);

				$("#recipient").val(customerName);
			});

	if (customer.online) {
		showOnlineCustomer(customer);
	}
	if (customer.unhandled) {
		showUnhandledCustomer(customer);
	}
}

function showOnlineCustomer(customer) {
	$("#customer-" + customer.username).css('color', 'red');
}

function removeOnlineCustomer(customer) {
	$("#customer-" + customer.username).css('color', 'black');
}

var intervalMap = new Map();

function showUnhandledCustomer(customer) {
	var username = customer.username;
	if (!intervalMap.has(username)) {
		var interval = setInterval(function() {
			$("#customer-" + username).fadeOut(100).fadeIn(100);
		}, 200);
		intervalMap.set(username, interval);
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