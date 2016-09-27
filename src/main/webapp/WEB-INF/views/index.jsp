<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
</head>

<body>
	<h1>
		Hello
		<shiro:principal property="username" />
	</h1>
	<fieldset>
		<a href="${ctx}/entity">实体</a> <br /> <a href="${ctx}/edit">编辑</a> <br />
		<a href="${ctx}/upload">上传</a>
	</fieldset>
	<br />
	<br />
	<fieldset>
		<a href="${ctx}/logout">登出</a>
	</fieldset>
</body>
</html>
