var stompClient = null;
$(document).ready(function() {
	connect();
});

function connect() {
	var socket = new SockJS(ctx + '/ws');
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
	if ($("#message").val().trim().length > 0) {
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
	url1 = ctx + "/chat/upload";
	var form_data = new FormData();
	for (var i = 0; i < (fileData.files.length); i++) {
		form_data.append("uploadFile", fileData.files[i]);
	}
	if ($("#message").val().trim().length > 0) {
		form_data.append("message", $("#message").val());
		$("#message").val("");
	}
	ajaxcreateUpload(form_data, url1);

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
			// myAlert("ADD NEW SUCCESS");
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
							"<div class='row'><div class=' pull-right' style='margin-top:10px'>"
									+ message.senderName
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ timeStr
									+ "</div></div><div class='row'><div class='popover fade bottom in pull-right' role='tooltip'  style='position:relative;display:block;max-width:500px;word-wrap:break-word;background-color:#B7DFF8;z-index:0'><div class='arrow arrowcolor' style='left: 70%;'></div><div class='popover-content'>"
									+ html_encode(message.message)
									+ " </div></div></div>");
		} else {
			$("#sentence")
					.append(
							"<div class='row'><div class=' pull-left' style='margin-top:10px'>"
									+ message.senderName
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ timeStr
									+ "</div></div><div class='row'><div class='popover fade bottom in pull-left' role='tooltip'  style='position:relative;display:block;max-width:500px;word-wrap:break-word;background-color:#FFFFFF;z-index:0'><div class='arrow arrowcolor1' style='left: 30%;'></div><div class='popover-content'>"
									+ html_encode(message.message)
									+ " </div></div></div>");
		}
	} else {
		let
		attachment = message.attachments.split(",");
		let
		files = "";
		if (message.message) {
			files += "<p>" + html_encode(message.message) + "</p>";
		}
		$
				.each(
						attachment,
						function(i, n) {
							if (n.indexOf("mp4") != -1) {
								files += "<video src="
										+ ctx
										+ "/uploads/CsmChatMessage/"
										+ message.id
										+ "/"
										+ n
										+ " controls  style='height:280px;width:350px'>"
										+ n + "</video>";
							}
							if (n.indexOf("ogg") != -1) {
								files += "<audio src=" + ctx
										+ "/uploads/CsmChatMessage/"
										+ message.id + "/" + n + " controls >"
										+ n + "</audio>";
							}
							if ((n.indexOf("mp4") === -1)
									&& (n.indexOf("ogg") === -1)) {
								files += "<p><a href=" + ctx
										+ "/uploads/CsmChatMessage/"
										+ message.id + "/" + n + ">" + n
										+ "</a></p>";
							}
						});

		if (message.fromAdmin) {
			$("#sentence")
					.append(
							"<div class='row'><div class=' pull-right' style='margin-top:10px'>"
									+ message.senderName
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ timeStr
									+ "</div></div><div class='row'><div class='popover fade bottom in pull-right' role='tooltip'  style='position:relative;display:block;max-width:500px;word-wrap:break-word;background-color:#B7DFF8;z-index:0'><div class='arrow arrowcolor' style='left: 70%;'></div><div class='popover-content'>"
									+ files + " </div></div></div>");
		} else {
			$("#sentence")
					.append(
							"<div class='row'><div class=' pull-left' style='margin-top:10px'>"
									+ message.senderName
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ timeStr
									+ "</div></div><div class='row'><div class='popover fade bottom in pull-left' role='tooltip'  style='position:relative;display:block;max-width:500px;word-wrap:break-word;background-color:#FFFFFF;z-index:0'><div class='arrow arrowcolor1' style='left: 30%;'></div><div class='popover-content'>"
									+ files + " </div></div></div>");
		}

	}
}

function video() {
	$("#myVideoModal").modal("show");
	myMediaRecorder.initVideo(processStream, processBlob, processError);
	$("#videostart").attr("disabled", false);
	$("#videostop").attr("disabled", true);
	$("#videocancel").attr("disabled", true);
}

function videoStart() {
	$("#videostart").attr("disabled", true);
	myMediaRecorder.start();
	$("#videostop").attr("disabled", false);
	$("#videocancel").attr("disabled", false);
}

function videoCancel() {
	myMediaRecorder.cancel();
	$("#myVideoModal").modal("hide");
}

function videoStop() {
	myMediaRecorder.stop();
	$("#myVideoModal").modal("hide");
}

var processStream = function(stream) {
	var video = document.getElementById('myVideo');
	video.srcObject = stream;
	video.onloadedmetadata = function(e) {
		video.play();
	};
}

var processStream1 = function(stream) {
	var video = document.getElementById('myAudio');
	video.srcObject = stream;
	video.onloadedmetadata = function(e) {
		video.play();
	};
}

function audio() {
	$("#myAudioModal").modal("show");
	myMediaRecorder.initAudio(processStream1, processBlob, processError);
	$("#audiostart").attr("disabled", false);
	$("#audiostop").attr("disabled", true);
	$("#audiocancel").attr("disabled", true);
}

function audioStart() {
	$("#audiostart").attr("disabled", true);
	myMediaRecorder.start();
	$("#audiostop").attr("disabled", false);
	$("#audiocancel").attr("disabled", false);
}

function audioCancel() {
	myMediaRecorder.cancel();
	$("#myAudioModal").modal("hide");
}

function audioStop() {
	myMediaRecorder.stop();
	$("#myAudioModal").modal("hide");
}

var processError = function(e) {
	alert("b");
	let
	text = "";
	if (e.name == "NotAllowedError") {
		text = "Camera Not Allowed";
	} else {
		if (e.name == "NotFoundError" || e.name == "devicesNotFoundError") {
			text = "Camera Not Found";
		} else {
			text = e.message;
		}
	}
	myAlert(text);
	$("#myVideoModal").modal("hide");
	$("#myAudioModal").modal("hide");
}
var processBlob = function(blob, media) {
	var url = ctx + "/chat/upload";
	var fd = new FormData();
	fd.append("uploadFile", blob, "media" + media.ext);
	if ($("#message").val().trim().length > 0) {
		fd.append("message", $("#message").val());
		$("#message").val("");
	}
	var xhr = new XMLHttpRequest();
	xhr.addEventListener("load", function(e) {
		// alert("Upload successfully");
	}, false);
	xhr.addEventListener("error", function(e) {
		// alert("Upload failed");
	}, false);
	xhr.addEventListener("abort", function(e) {
		// alert("Upload cancelled");
	}, false);
	xhr.open("POST", url);
	xhr.send(fd);
};

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
function html_encode(str)   
{   
  var s = "";   
  if (str.length == 0) return "";   
  s = str.replace(/&/g, "&gt;");   
  s = s.replace(/</g, "&lt;");   
  s = s.replace(/>/g, "&gt;");   
  s = s.replace(/ /g, "&nbsp;");   
  s = s.replace(/\'/g, "&#39;");   
  s = s.replace(/\"/g, "&quot;");   
  s = s.replace(/\n/g, "<br>");   
  return s;   
}  