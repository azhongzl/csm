<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<title>ERP FAQ</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/style.css" />
<script type="text/javascript" src="http://static.runoob.com/assets/vue/1.0.11/vue.min.js"></script>
</head>
<body>
	<div id="main">
		<header>
			<nav>
				<div id="menu_container">
					<ul class="sf-menu" id="nav">
						<li><a href="${ctx}/">Home</a></li>
						<li><a href="#">Contact Us</a></li>
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
						<li v-for="category in categorys"><a href="#" v-on:click="showCategoryDetail(category.id)">{{category.name }}</a></li>
					</ul>
				</div>
			</div>
			<div id="content">
				<my-faqs :faqs='faqs' :answer-key='answerKey' v-for="faq in faqs" :faq='faq'></my-faqs>
			</div>
			<div id="pagecount" v-show="showKey">
				<span><a href="#" v-on:click="changePage(1)">  &laquo; </a></span> <span><a href="#"
					v-on:click="changePage(curPage - 1)"> &lsaquo;</a></span> <span v-for="n in totalPage" :class="curPage === n ? 'current' : ''">
					<a href="#" v-on:click="changePage(n)">{{ n}}</a>
				</span> <span><a href="#" v-on:click="changePage(curPage + 1)">&rsaquo;</a></span> <span><a href="#"
					v-on:click="changePage(totalPage)"> &raquo; </a></span>
			</div>
		</div>
		<div id="scroll">
			<a title="Scroll to the top" class="top" href="#"><img src="${ctx}/static/images/top.png" alt="top" /></a>
		</div>
		<footer>
			<p>
				<img src="${ctx}/static/images/twitter.png" alt="twitter" />&nbsp;<img src="${ctx}/static/images/facebook.png"
				alt="facebook" />&nbsp;<img src="${ctx}/static/images/rss.png" alt="rss" />
			</p>
			<p>
				<a href="index.html">Home</a> | <a href="javascript:void(0)">Contact Us</a>
			</p>
			<p>
				Copyright &copy; <a href="http://www.kuzcolighting.com">design from Kuzcolighting</a>
			</p>
		</footer>
	</div>

	<template id="content1"> 
		<a href="#" v-on:click="showAnswer(faq.id)">{{faq.question}} </a>
		<div v-show="answerKey">{{faq.answer}}<br> 
			<a href="${ctx}/uploads/Faq/{{faq.id}}/{{attachment}}" v-for="attachment in attachments">{{attachment}}	</a>
		</div>
	</template>

	<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/vueFunction.js"></script>


</body>
</html>