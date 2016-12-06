<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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
	<form action="#" method="post">
		<fieldset>
			<table>
				<tr>
					<td>Id:</td>
					<td><input type="text" id="userId" name="id" value="11d5f5f7-d6eb-1034-a268-c6b53a0158b7" /></td>
				</tr>
				<tr>
					<td>Username:</td>
					<td><input type="text" name="username" value="username" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="text" name="password" value="" /></td>
				</tr>
				<tr>
					<td>Salt:</td>
					<td><input type="text" name="salt" value="" /></td>
				</tr>
				<tr>
					<td>Active:</td>
					<td><input type="radio" name="active" value="1" checked />Active <input type="radio" name="active" value="0" />Inactive</td>
				</tr>
				<tr>
					<td>userGroupId:</td>
					<td><input type="text" name="userGroupId" value="5A0452FE-D6EA-1034-A268-C6B53A0158B7" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Update"
						onclick="this.form.action='${ctx}/profile'" /></td>
				</tr>
			</table>
		</fieldset>
	</form>

	<br />
	<hr />
	<br />

	<a href="${ctx}/e/Category/find">View Category</a>
	<form action="${ctx}/e/Category/post" method="post">
		<input type="hidden" name="createAccountId" value="<shiro:principal property="id" />" /> <input type="hidden"
			name="modifyAccountId" value="<shiro:principal property="id" />" />
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
					<td>Active:</td>
					<td><input type="radio" name="active" value="1" checked />Active <input type="radio" name="active" value="0" />Inactive</td>
				</tr>
				<tr>
					<td>createDateTime:</td>
					<td><input type="text" name="createDate"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td>modifyDateTime:</td>
					<td><input type="text" name="modifyDate"
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
		<input type="hidden" name="modifyAccountId" value="<shiro:principal property="id" />" />
		<fieldset>
			<table>
				<tr>
					<td>Id:</td>
					<td><input type="text" id="categoryId" name="id" value="11d5f5f7-d6eb-1034-a268-c6b53a0158b7" /></td>
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
					<td>Active:</td>
					<td><input type="radio" name="active" value="1" checked />Active <input type="radio" name="active" value="0" />Inactive</td>
				</tr>
				<tr>
					<td>modifyDateTime:</td>
					<td><input type="text" name="modifyDate"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Update"
						onclick="this.form.action='${ctx}/e/Category/put/' + document.getElementById('categoryId').value" /></td>
				</tr>
			</table>
		</fieldset>
	</form>

	<br />
	<hr />
	<br />

	<a href="${ctx}/e/Faq/find">View Faq</a>
	<form action="${ctx}/e/Faq/postUpload" method="post" enctype="multipart/form-data">
		<input type="hidden" name="createAccountId" value="<shiro:principal property="id" />" /> <input type="hidden"
			name="modifyAccountId" value="<shiro:principal property="id" />" /> <input type="hidden" name="keywords" value="111" />
		<fieldset>
			<table>
				<tr>
					<td>CategoryId:</td>
					<td><input type="text" name="categoryId" value="5a0452fe-d6ea-1034-a268-c6b53a0158b7" /></td>
				</tr>
				<tr>
					<td>Question:</td>
					<td><input type="text" name="question" value="question" /></td>
				</tr>
				<tr>
					<td>Answer:</td>
					<td><input type="text" name="answer" value="answer" /></td>
				</tr>
				<tr>
					<td>UploadFile:</td>
					<td><input type="file" name="uploadFile" multiple="multiple" /></td>
				</tr>
				<tr>
					<td>Active:</td>
					<td><input type="radio" name="active" value="1" checked />Active <input type="radio" name="active" value="0" />Inactive</td>
				</tr>
				<tr>
					<td>createDateTime:</td>
					<td><input type="text" name="createDate"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td>modifyDateTime:</td>
					<td><input type="text" name="modifyDate"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Create" /></td>
				</tr>
			</table>
		</fieldset>
	</form>
	<br />
	<form action="#" method="post" enctype="multipart/form-data">
		<input type="hidden" name="modifyAccountId" value="<shiro:principal property="id" />" /> <input type="hidden"
			name="keywords" value="111" />
		<fieldset>
			<table>
				<tr>
					<td>Id:</td>
					<td><input type="text" id="faqId" name="id" value="5a0452fe-d6ea-1034-a268-c6b53a0158b7" /></td>
				</tr>
				<tr>
					<td>CategoryId:</td>
					<td><input type="text" name="categoryId" value="5a0452fe-d6ea-1034-a268-c6b53a0158b7" /></td>
				</tr>
				<tr>
					<td>Question:</td>
					<td><input type="text" name="question" value="General Ledger question for Kuzcolighting 1" /></td>
				</tr>
				<tr>
					<td>Answer:</td>
					<td><input type="text" name="answer" value="General Ledger answer 1" /></td>
				</tr>
				<tr>
					<td>Attachments:</td>
					<td><input type="text" name="attachments" value="" /></td>
				</tr>
				<tr>
					<td>UploadFile:</td>
					<td><input type="file" name="uploadFile" multiple="multiple" /></td>
				</tr>
				<tr>
					<td>Active:</td>
					<td><input type="radio" name="active" value="1" checked />Active <input type="radio" name="active" value="0" />Inactive</td>
				</tr>
				<tr>
					<td>modifyDateTime:</td>
					<td><input type="text" name="modifyDate"
						value="<%=java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)%>" /></td>
				</tr>
				<tr>
					<td><input type="submit" value="Update"
						onclick="this.form.action='${ctx}/e/Faq/putUpload/' + document.getElementById('faqId').value" /></td>
				</tr>
			</table>
		</fieldset>
	</form>

	<br />
	<hr />
	<br />
</body>
</html>
