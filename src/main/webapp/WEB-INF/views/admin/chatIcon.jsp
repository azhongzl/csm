<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<script src="${ctx}/static/js/lib/jquery.js"></script>
</head>
<body>
	<div>
		<a id="chatLink" href="${ctx}/admin/chat" target="_blank"><img id="imgId" src="${ctx}/static/images/002.png"
			width="${iconWidth == null ? 20 : iconWidth}" height="${iconHeight == null ? 20 : iconHeight}" alt="Chat Icon" /></a>
	</div>
</body>
<script type="text/javascript">
	var timer = window.setInterval(check, 3000);

	function check() {
		$.ajax({
			type : "GET",
			url : "${ctx}/admin/chat/hasUnhandledCustomer",
			async : false,
			success : function(result) {
				if (result.data) {
					setImg("001.gif");
				} else {
					setImg("002.png");
				}
			},
			timeout : 3000,
			error : handleError,
		});
	}

	function handleError(xhr) {
		if (xhr.status == 499) {
			window.clearInterval(timer);

			$("#chatLink").removeAttr("href");
			setImg("002.png");
			window
					.open(
							'${ctx}/login?username=${username}&successUrl=/loginRefresh',
							'_blank');
		} else {
			alert(" error: " + xhr.status + " " + xhr.statusText);
		}
	}

	function setImg(img) {
		$("#imgId").attr({
			src : "${ctx}/static/images/" + img
		});
	}
</script>
</html>