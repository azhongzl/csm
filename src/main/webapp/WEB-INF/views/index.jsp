<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>

<head>
<title>ERP FAQ</title>
<meta name="description" content="website description" />
<meta name="keywords" content="website keywords, website keywords" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/style.css" />

</head>

<body>
	<div id="main">
		<header>
			<nav>
				<div id="menu_container">
					<ul class="sf-menu" id="nav">
						<li><a href="${ctx}/">Home</a></li>
						<li><a href="#">Contact Us</a></li>
						<li><shiro:principal property="id" /> <shiro:principal
								property="username" /></li>
					</ul>
				</div>
			</nav>
			<div id="logo">
				<div id="logo_text">
					<h1>Search</h1>
					<input type="search" id="search" placeholder="Search..." />
				</div>
			</div>
		</header>
		<div id="site_content">
			<div id="sidebar_container">
				<div class="sidebar">
					<h3>Categories</h3>
					<ul id="Category">
					</ul>
				</div>

			</div>
			<div id="content"></div>
			<div id="pagecount"></div>
		</div>
		<div id="scroll">
			<a title="Scroll to the top" class="top" href="#"><img
				src="${ctx}/static/images/top.png" alt="top" /></a>
		</div>
		<footer>
			<p>
				<img src="${ctx}/static/images/twitter.png" alt="twitter" />&nbsp;<img
					src="${ctx}/static/images/facebook.png" alt="facebook" />&nbsp;<img
					src="${ctx}/static/images/rss.png" alt="rss" />
			</p>
			<p>
				<a href="index.html">Home</a> | <a href="javascript:void(0)">Contact
					Us</a>
			</p>
			<p>
				Copyright &copy; <a href="http://www.kuzcolighting.com">design
					from Kuzcolighting</a>
			</p>
		</footer>
	</div>

	<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/function.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			showCategory();
		});
	</script>
</body>
</html>