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
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>

</head>
<body>


	<div id="main" class="container" style="width: 630px; height: 740px;">
		<div id="sentence" class="well"
			style="width: 600px; height: 600px; overflow-y: auto; overflow-x: hidden; background-color: #ebebeb; margin-top: 5px">
		</div>
		<div class="well" style="width: 600px; height: 100px; background-color: #dddcd9; margin: auto;">

			<form class="form-inline">
				<div class="form-group">
					<input type="text" id="message" class="form-control" placeholder="Your message here..." style="width: 480px;">
				</div>
				<button id="send" class="btn btn-default" type="submit" onclick="sendMessage()">Send</button>
			</form>

			<!-- /input-group -->
		</div>

	</div>
	<script src="${ctx}/static/js/lib/jquery.js"></script>
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/chatFunction.js"></script>
	<script type="text/javascript">
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />"
	</script>
</body>
</html>