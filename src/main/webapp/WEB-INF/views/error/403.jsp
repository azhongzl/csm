<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	//Set return code 200，avoid browser uses its native error page
	//response.setStatus(200);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>403 - Authorization</title>
</head>

<body>
	<h2>403 - Authorization.</h2>
	<p>
		<a href="<c:url value="/"/>">Back</a>
	</p>
</body>
</html>
