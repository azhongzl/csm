<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hi Jalen, this is a temp test html</title>
</head>
<body>
	<a href="${ctx}/">Go Back</a>
	<br />
	<a href="${ctx}/facade/Category/find">View Type</a>
	<form action="${ctx}/facade/Category/post" method="post">
		<fieldset>
			<table>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value="name" /></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><input type="text" name="description" value="" /></td>
				</tr>
				<tr>
					<td>createTime:</td>
					<td><input type="text" name="createTime"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Create" /></td>
				</tr>
			</table>
		</fieldset>
	</form>
	<br />
	<form action="#" method="post">
		<fieldset>
			<table>
				<tr>
					<td>Id:</td>
					<td><input type="text" id="id" name="id" value="" /></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value="name" /></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><input type="text" name="description" value="" /></td>
				</tr>
				<tr>
					<td>modifyTime:</td>
					<td><input type="text" name="modifyTime"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Update"
						onclick="this.form.action='${ctx}/facade/Category/put' + '/' + document.getElementById('id').value" /></td>
				</tr>
			</table>
		</fieldset>
	</form>

	<br />
	<hr />
	<br />
</body>
</html>
