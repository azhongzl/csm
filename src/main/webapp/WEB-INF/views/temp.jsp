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
	<div id='gUMArea'>
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
		function uploadCallback() {
			alert("my upload callback");
		}
	</script>
</body>
</html>