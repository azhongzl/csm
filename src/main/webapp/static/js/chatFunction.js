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
			let temp="";
			temp="<p><a href="+ctx+"/uploads/CsmChatMessage/"+message.id+"/"+n+">"+n+"</a></p>";
			if(n.indexOf("mp4")>0){
			files+="<video src="+ctx+"/uploads/CsmChatMessage/"+message.id+"/"+n+" controls  style='height:280px;width:350px'>"+temp+"</video>";	
			}else{
			files+=temp;
			}
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

function video(){
	$("#myVideoModal").modal("show");
    myMediaRecorder.initVideo(processStream, processBlob);
    $("#videostart").attr("disabled",false);
    $("#videostop").attr("disabled",true);

}

function videoStart(){
    $("#videostart").attr("disabled",true);
    myMediaRecorder.start();
    $("#videostop").attr("disabled",false);
} 

function videoStop(){
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

var processBlob = function(blob, media) {
    var url = ctx+"/chat/upload";
    var fd = new FormData();
    fd.append("uploadFile", blob, "media" + media.ext);
    var xhr = new XMLHttpRequest();
    xhr.addEventListener("load", function(e) {
        alert("Upload successfully");
    }, false);
    xhr.addEventListener("error", function(e) {
        alert("Upload failed");
    }, false);
    xhr.addEventListener("abort", function(e) {
        alert("Upload cancelled");
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

