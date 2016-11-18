<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script src="${ctx}/static/js/lib/jquery.js"></script>
</head>
<body>
	<div>
	<a href="${ctx}/admin/chat" target="_blank"><img id="imgId" src="${ctx}/static/images/002.png" width="${iconWidth==null?20:iconWidth}" height="${iconHeight==null?20:iconHeight}" alt="chat button" /></a>
	</div>
</body>
<script type="text/javascript">


window.setInterval(check, 3000)

function check(){
	var flash;
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/csm/admin/chat/hasUnhandledCustomer",
		async : false,
		success : function(result) {
			if (result.data == true) {

				$("#imgId").attr({ src: "${ctx}/static/images/001.gif"});
			} else {

				$("#imgId").attr({ src: "${ctx}/static/images/002.png"});
			}
		},
		timeout : 3000,
		error : function(xhr) {
			alert(" error： " + xhr.status + " " + xhr.statusText);
		},
	});


}




</script>
</html>