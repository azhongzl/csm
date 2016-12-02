<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="com.itdoes.common.core.shiro.Shiros"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap Core CSS -->
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
</head>

<body>
	<div class="container" style="margin-top: 140px">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Please Sign In</h3>
					</div>
					<div class="panel-body">
						<form role="form" id="loginForm" action="${ctx}/login" method="post">
							<%
								String successUrl = (String) request.getAttribute(Shiros.SUCCESS_URL_KEY);
								if (successUrl != null) {
							%>
							<input type="hidden" id="successUrl" name="successUrl" value="${successUrl}" />
							<%
								}

								String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
								if (error != null) {
							%>
							<div class="alert alert-error controls input-large">
								<button class="close" data-dismiss="alert">×</button>
								<%
									if (error.contains("DisabledAccountException")) {
											out.print("Inactive user");
										} else {
											out.print("Login failed");
										}
								%>
							</div>
							<%
								}
							%>

							<fieldset>
								<div class="form-group">
									<input class="form-control input-medium required" placeholder="username" name="username" type="text"
										value="${username}" autofocus>
								</div>
								<div class="form-group">
									<input class="form-control input-medium required" placeholder="password" name="password" type="password">
								</div>
								<div class="checkbox">
									<label> <input name="rememberMe" type="checkbox" checked>Remember Me
									</label>
								</div>
								<input id="submit_btn" class="btn btn-lg btn-success btn-block" type="submit" value="Login" />
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="${ctx}/static/js/lib/jquery.js"></script>
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<script>
		$(document).ready(function() {
			$("#loginForm").validate();
		});
	</script>
</body>
</html>
