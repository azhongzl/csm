var stompClient = null;
$(document).ready(function() {
	connect();
});

var vm = new Vue({
    el: '#chatContainer',
    data: {
         sentence: "",
         modalTitle: "",
         videostart: true,
         videostop: true,
         messages:[],
         historyMessages:[],
         curPage:1,
         totalPage: 0,
         lastNum:5,
         column:5
    },
    watch:{
    	messages:function(){
            setTimeout(function() {
           	 document.getElementById('sentence').scrollTop = document.getElementById('sentence').scrollHeight;
           }, 100);
    	}
    },
    
    methods: {
        showUploadFile: function(){
        	var fileData = $("input[name='uploadFile1']").get(0);
        	url1 = ctx + "/chat/upload";
        	var form_data = new FormData();
        	for (var i = 0; i < (fileData.files.length); i++) {
        		form_data.append("uploadFile", fileData.files[i]);
        	}
        	if (this.sentence.trim().length > 0) {
        		form_data.append("message", this.sentence);
				this.sentence="";
        	}
        	ajaxcreateUpload(form_data, url1);

        },
        sendMessage: function(){
        	if (this.sentence.trim().length > 0) {
        		stompClient.send('/app/chatCSendMessage', {}, JSON.stringify({
        			'message' : this.sentence
        		}));
        		this.sentence="";
        	}
        },
        
        listHistory: function() {
        	let beginDate = $("#datepicker001").val();
        	let endDate = $("#datepicker002").val();
        	if((beginDate == endDate )||(beginDate == "")||(endDate == "")){
        		alert("Please enter correct date ...");
        		return false;
        	}
        	beginDate = beginDate+"T00:00:00";
        	endDate = endDate+"T23:59:59.999999999"
            let url = ctx + "/chat/listHistory/" ;
            let checkKey = {
            		beginDateTime:beginDate,
            		endDateTime:endDate,
            		page_no:this.curPage,
            };
            let result = ajaxFind(checkKey, url);
            if (!result.data.historyPage.content){
            	alert("No result ...");
            	return false;
            }
            let messageList = result.data.historyPage.content;
            this.totalPage = result.data.historyPage.totalPages;
            if (this.totalPage<=5){
            	this.column=this.totalPage;
            }else{
            	this.column=5;
            }
            for (var i = 0; i < messageList.length; i++) {
                let timeStr = "";
                let num = messageList[i].createDateTime.indexOf("T");
                timeStr = messageList[i].createDateTime.substring(0, num) + " " + messageList[i].createDateTime.substring(num + 1, 19);
                messageList[i].createDateTime=timeStr;
                if (messageList[i].attachments != undefined){
                	 let attachment = messageList[i].attachments.split(",");
                	 if(messageList[i].message){
                	 let mess = html_encode(messageList[i].message);
                	 messageList[i].message = mess;
                	 }
                	 messageList[i].attachments = attachment;
                }else{
                	let mess = html_encode(messageList[i].message);
                	messageList[i].message = mess;
                }
            }
            this.historyMessages=messageList.reverse();
            setTimeout(function() {
           	 document.getElementById('historySentence').scrollTop = document.getElementById('historySentence').scrollHeight;
           }, 100);
        },
        changePage : function(n){
        	if ((n==0)||(n>this.totalPage)){
        		return false;
        	}
        	if (this.curPage==n){
        		return false;
        	}
        	this.curPage=n;
        	if (n<=5){
        		this.lastNum=5;
        	}else{
        		if ((n+2)>this.totalPage){
        			this.lastNum=this.totalPage;
        		}else{
        			this.lastNum=n+2;
        		}
        	}
        	this.listHistory();
        },
        
         video: function() {
            this.modalTitle = "VIDEO"
            $("#myVideoModal").modal("show");
            myMediaRecorder.initVideo(this.processStream, this.processBlob, this.processError);
            this.videostart = false;
            this.videostop = true;

        },
        videoStart: function() {
            this.videostart = true;
            myMediaRecorder.start();
            this.videostop = false;

        },
        videoCancel: function() {
            myMediaRecorder.cancel();
            $("#myVideoModal").modal("hide");
        },
        videoStop: function() {
            myMediaRecorder.stop();
            $("#myVideoModal").modal("hide");
        },
        audio: function() {
            this.modalTitle = "AUDIO"
            $("#myAudioModal").modal("show");
            myMediaRecorder.initAudio(this.processStream1, this.processBlob, this.processError);
            this.videostart = false;
            this.videostop = true;

        },
        audioStart: function() {
            this.videostart = true;
            myMediaRecorder.start();
            this.videostop = false;

        },
        audioStop: function() {
            myMediaRecorder.stop();
            $("#myAudioModal").modal("hide");
        },
        audioCancel: function() {
            myMediaRecorder.cancel();
            $("#myAudioModal").modal("hide");
        },
        processStream: function(stream) {
            var video = document.getElementById('myVideo');
            video.srcObject = stream;
            video.onloadedmetadata = function(e) {
                video.play();
            };

        },
        processStream1: function(stream) {
            var video = document.getElementById('myAudio');
            video.srcObject = stream;
            video.onloadedmetadata = function(e) {
                video.play();
            };
        },
        processBlob: function(blob, media) {
        	var url = ctx + "/chat/upload";
            var fd = new FormData();
            fd.append("uploadFile", blob, "media" + media.ext);
            if (this.sentence.trim().length>0){
                fd.append("message", this.sentence);
                this.sentence="";
            }
            var xhr = new XMLHttpRequest();
            xhr.addEventListener("load", function(e) {
// myAlert("Upload successfully");
            }, false);
            xhr.addEventListener("error", function(e) {
// myAlert("Upload failed");
            }, false);
            xhr.addEventListener("abort", function(e) {
// myAlert("Upload cancelled");
            }, false);
            xhr.open("POST", url);
            xhr.send(fd);
        },
        processError: function(e) {
        	let text="";
        	if(e.name=="NotAllowedError"){
        		text="Camera Not Allowed";
        		}else{
        			if(e.name=="NotFoundError"){
                		text="Camera Not Found";
            		}else{
            			text=e.name+":"+e.message;
            		}
        		}
        	alert(text);
            $("#myVideoModal").modal("hide");
            $("#myAudioModal").modal("hide");
        },
        
    },

})

function connect() {
	var socket = new SockJS(ctx + '/ws');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/topic/chat/message/' + userId,
				function(message) {
					showMessage(JSON.parse(message.body));
//					document.getElementById('sentence').scrollTop = document
//							.getElementById('sentence').scrollHeight;
				});
		stompClient.subscribe('/app/chatCInitMessage', function(message) {
			showMessages(JSON.parse(message.body).data.messageList);
		});
	});
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
//	document.getElementById('sentence').scrollTop = document
//			.getElementById('sentence').scrollHeight;
}

function showMessage(message) {
    var timeStr = "";
    var num = message.createDateTime.indexOf("T");
    timeStr = message.createDateTime.substring(0, num) + " " + message.createDateTime.substring(num + 1, 19);
    message.createDateTime=timeStr;
    if (message.attachments != undefined){
    	 let attachment = message.attachments.split(",");
    	 if(message.message){
    	 let mess = html_encode(message.message);
    	 message.message = mess;
    	 }
    	 message.attachments = attachment;
    }else{
    	let mess = html_encode(message.message);
   	 	message.message = mess;
    }
    vm.messages.push(message);
}





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