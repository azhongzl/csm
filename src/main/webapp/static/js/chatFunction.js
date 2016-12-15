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
function listHistory() {

	var url = ctx + "/chat/listHistory";
	var checkKey = {};
	var result = ajaxFind(checkKey, url);

	showMessages(result.data.historyList);
}

function showUploadFile() {
	var fileData = $("input[name='uploadFile1']").get(0);
	var txt = "";
	// if ('files' in fileData) {
	// for (var i = 0; i < fileData.files.length; i++) {
	// txt += (i + 1) + ". file ";
	// var file = fileData.files[i];
	// if ('name' in file) {
	// txt += "name: " + file.name;
	// alert(file.name);
	// }
	// if ('size' in file) {
	// txt += " file size: " + file.size + " bytes \n";
	// }
	// }
	// }
	url1 = ctx + "/chat/upload";
	var form_data = new FormData();
	for (var i = 0; i < (fileData.files.length); i++) {
		form_data.append("uploadFile", fileData.files[i]);
	}
	ajaxcreateUpload(form_data, url1);

}

function uploadFile() {
	$("#chatForm").hide();
	$("#uploadFile").show();
}

function switchChat() {
	$("#uploadFile").hide();
	$("#chatForm").show();
}

function ajaxcreateUpload(savedata, url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : savedata,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(result) {
			myAlert("ADD NEW SUCCESS");
		},
		timeout : 3000,
		error : handleError,

	});
}

function ajaxFind(checkKey, url1) {
	var checkList = [];
	$.ajax({
		type : "GET",
		url : url1,
		data : checkKey,
		cache : false,
		async : false,
		success : function(result) {
			checkList = result;
		},
		timeout : 3000,
		error : handleError,
	});

	return (checkList);
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
	if (message.attachments == undefined) {
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
	} else {
		let attachment=message.attachments.split(",");
		let files="";
		$.each(attachment,function(i,n){
			files+="<p><a href="+ctx+"/uploads/CsmChatMessage/"+message.id+"/"+n+">"+n+"</a></p>";
		
		});
		if (message.fromAdmin) {
			$("#sentence")
					.append(
							"<div class='panel panel-primary' style='clear:both;float:right;width:400px'><div class='panel-heading' style='padding: 2px 0px ' >"
									+ "Customer Service"
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ timeStr
									+ "</div><div class='panel-body'>"
									+ files + " </div></div>");
		} else {
			$("#sentence")
					.append(
							"<div class='panel panel-info' style='clear:both;float:left;width:400px'><div class='panel-heading' style='padding: 2px 0px ' >"
									+ message.senderName
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ timeStr
									+ "</div><div class='panel-body'>"
									+ files + " </div></div>");
		}
		
	}
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});

});

function handleError(xhr) {

	if (xhr.status == 499) {
		window.location.replace("${ctx}" + xhr.statusText);
	} else {
		alert("error： " + xhr.status + " " + xhr.statusText);
	}
}