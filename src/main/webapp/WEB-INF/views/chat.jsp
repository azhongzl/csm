<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>KUZCOLIGHTING CHAT</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
<link href="${ctx}/static/css/chat.css" rel="stylesheet">
<script src="${ctx}/static/js/lib/jquery.js"></script>
<script src="${ctx}/static/js/lib/bootstrap.js"></script>
<script src="${ctx}/static/js/lib/jquery.slidereveal.min.js"></script>
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>

</head>

<script type="text/javascript">
	$(function() {

		$('#slider').slideReveal({
			trigger : $("#trigger"),
			position : "right",
			push : false,
			width : 640,
			top : 200
		});
		$("#uploadFile").hide();
	});
</script>
<body>

	<div id="slider" class="slider">
		<div id="main" class="container" style="width: 630px; height: 740px;">
			<div id="sentence" class="well"
				style="width: 600px; height: 600px; overflow-y: auto; overflow-x: hidden; background-color: #ebebeb; margin-top: 5px">
			</div>
			<div class="well" style="width: 600px; height: 100px; background-color: #dddcd9; padding: 0px; padding-top: 30px">
				<div class="btn-group dropup pull-right">
					<button type="button" aria-hidden="true" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="caret">
					</button>
					<ul class="dropdown-menu" role="menu">
						<li><a href="javascript:void(0)" onclick="listHistory()">Display history </a></li>
						<li><a href="javascript:void(0)" onclick="uploadFile()">Upload file</a></li>
						<li><a href="javascript:void(0)">other</a></li>
						<li class="divider"></li>
						<li><a href="javascript:void(0)" onclick="switchChat()">Chat</a></li>
					</ul>
				</div>
				<form class="form-inline" id="chatForm">
					<div class="form-group">
						<input type="text" id="message" class="form-control" placeholder="Your message here..." style="width: 480px;">
					</div>
					<button id="send" class="btn btn-default" type="submit" onclick="sendMessage()">Send</button>
				</form>
				<div id="uploadFile" class="col-md-2 " >
					<input class="btn btn-default" id="inputFile" type="file" name='uploadFile1' multiple='multiple'
						onchange='showUploadFile()' />
				</div>
				<!-- /input-group -->
			</div>
		</div>
	</div>
	<button id="trigger" class="trigger">try</button>
	<script type="text/javascript">
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />"
		ctx = "${ctx}";
	</script>
	<script type="text/javascript" src="${ctx}/static/js/chatFunction.js"></script>

</body>
</html>