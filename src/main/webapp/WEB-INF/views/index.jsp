<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>

<head>
<title>ERP FAQ</title>
<meta name="description" content="website description" />
<meta name="keywords" content="website keywords, website keywords" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />

</head>

<body>
	<div id="main">
		<header>
			<nav>
				<div id="menu_container">
					<ul class="sf-menu" id="nav">
						<li><a href="index.html">Home</a></li>
						<li><a href="javascript:void(0)" onclick="test()">A Page</a></li>
						<li><a href="javascript:void(0)">Contact Us</a></li>
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
					<ul>
						<li><a href="#">First Link</a></li>
						<li><a href="#">Another Link</a></li>
						<li><a href="#">And Another</a></li>
						<li><a href="#">Last One</a></li>
					</ul>
				</div>
				<div class="sidebar">
					<h3>More Useful Links</h3>
					<ul>
						<li><a href="#">First Link</a></li>
						<li><a href="#">Another Link</a></li>
						<li><a href="#">And Another</a></li>
						<li><a href="#">Last One</a></li>
					</ul>
				</div>
			</div>
			<div id="content">
				<h1>Welcome to the kuzcolighting FAQ</h1>
				<p>CSS Syntax A CSS rule-set consists of a selector and a
					declaration block: CSS selector The selector points to the HTML
					element you want to style. The declaration block contains one or
					more declarations separated by semicolons. Each declaration
					includes a CSS property name and a value, separated by a colon. A
					CSS declaration always ends with a semicolon, and declaration
					blocks are surrounded by curly braces. In the following example all
				<p>elements will be center-aligned, with a red text color:<h2>Browser Compatibili
				ty</h2>
				<p>This template has been tested in the following browsers:</p>
				<ul>
					<li>Internet Explorer 8</li>
					<li>Internet Explorer 7</li>
					<li>FireFox 10</li>
					<li>Google Chrome 17</li>
					<li>Safari 4</li>
				</ul>
			</div>
		</div>
		<div id="scroll">
			<a title="Scroll to the top" class="top" href="#"><img
				src="../../images/top.png" alt="top" /></a>
		</div>
		<footer>
			<p>
				<img src="../../images/twitter.png" alt="twitter" />&nbsp;<img
					src="../../images/facebook.png" alt="facebook" />&nbsp;<img
					src="../../images/rss.png" alt="rss" />
			</p>
			<p>
				<a href="index.html">Home</a> | <a href="javascript:void(0)">Contact Us</a>
			</p>
			<p>
				Copyright &copy; <a	href="http://www.kuzcolighting.com">design from
					Kuzcolighting</a>
			</p>
		</footer>
	</div>

	<script type="text/javascript" src="../../js/jquery.js"></script>
	<script type="text/javascript" src="../../js/founction.js"></script>
</body>
</html>