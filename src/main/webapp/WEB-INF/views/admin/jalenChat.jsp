<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Hello WebSocket</title>
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
<link href="${ctx}/static/css/jalenMain.css" rel="stylesheet">
<script src="${ctx}/static/js/lib/jquery.js"></script>
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>
<script src="${ctx}/static/js/jalenAdminChat.js"></script>
</head>
<body>
	<noscript>
		<h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
			enabled. Please enable Javascript and reload this page!</h2>
	</noscript>
	<div id="main-content" class="container">
		<div class="row">
			<div class="col-md-12">
				<form class="form-inline">
					<div class="form-group">
						<label for="connect">WebSocket connection (<shiro:principal property="username" />):
						</label>
						<button id="connect" class="btn btn-default" type="submit">Connect</button>
						<button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Disconnect</button>
					</div>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="customerSetTable" class="table table-striped">
					<thead>
						<tr>
							<th>Customers</th>
						</tr>
					</thead>
					<tbody id="customerSet">
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<form class="form-inline">
					<div class="form-group">
						<label for="message">To:</label> <input type="text" id="roomId" class="form-control" placeholder=""><label
							for="message">Message:</label><input type="text" id="message" class="form-control"
							placeholder="Your message here...">
					</div>
					<button id="send" class="btn btn-default" type="submit">Send</button>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table id="conversation" class="table table-striped">
					<thead>
						<tr>
							<th>Messages</th>
						</tr>
					</thead>
					<tbody id="messages">
					</tbody>
				</table>
			</div>
		</div>
		</form>
	</div>
	<script type="text/javascript">
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />"
	</script>
</body>
</html>