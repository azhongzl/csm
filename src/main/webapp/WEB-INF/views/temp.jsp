<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>

<head>
<title>MediaRecorder API - Sample</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords" content="WebRTC getUserMedia MediaRecorder API">
<link type="text/css" rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<style>
button {
	margin: 10px 5px;
}

li {
	margin: 10px;
}

body {
	width: 90%;
	max-width: 960px;
	margin: 0px auto;
}

#btns {
	display: none;
}

h1 {
	margin: 100px;
}
</style>
</head>

<body>
	<h1>MediaRecorder API example</h1>

	<p>For now it is supported only in Firefox(v25+) and Chrome(v47+)</p>
	<div>
		<video id="myVideo" autoplay muted></video>
	</div>
	<div>
		<div>
			Record: <input type="radio" name="media" value="video" checked id='mediaVideo'>Video <input type="radio"
				name="media" value="audio" id='mediaAudio'>audio
		</div>
	</div>
	<div id='btns'>
		<button class="btn btn-default" id='start'>Start</button>
		<button class="btn btn-default" id='stop'>Stop</button>
	</div>
	<script src="https://code.jquery.com/jquery-2.2.0.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script src="${ctx}/static/js/myMediaRecorder.js"></script>
	<script>
                    var processStream = function(stream) {
                        var video = document.getElementById('myVideo');
                        video.srcObject = stream;
                        video.onloadedmetadata = function(e) {
                            video.play();
                        };
                    }

                    var processBlob = function(blob, media) {
                        var url = "${ctx}/admin/chat/upload";
                        var fd = new FormData();
                        fd.append("roomId", "5ab5e407-9a6a-4e3b-90db-2aa3dc267012");
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

                    var id = val => document.getElementById(val),
                        mediaVideo = id('mediaVideo'),
                        mediaAudio = id('mediaAudio'),
                        start = id('start'),
                        stop = id('stop');

                    myMediaRecorder.initVideo(processStream, processBlob);

                    id('btns').style.display = 'inherit';
                    start.removeAttribute('disabled');
                    stop.disabled = true;

                    mediaVideo.onchange = e => {
                        myMediaRecorder.initVideo(processStream, processBlob);
                    }

                    mediaAudio.onchange = e => {
                        myMediaRecorder.initAudio(processStream, processBlob);
                    }

                    start.onclick = e => {
                        start.disabled = true;
                        myMediaRecorder.start();
                        stop.removeAttribute('disabled');
                    }

                    stop.onclick = e => {
                        stop.disabled = true;
                        myMediaRecorder.stop();
                        start.removeAttribute('disabled');
                    }
                </script>
</body>
</html>