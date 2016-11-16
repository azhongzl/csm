<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="${ctx}/static/js/lib/vue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vue-router.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vuex.js"></script>
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
<link href="${ctx}/static/css/chat.css" rel="stylesheet">
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>
</head>
<body>
	<div id="main" class="container">
		<div class="row clearfix">
			<div class="col-md-12 column" style="background-color: #82c0ff">
				<nav class="navbar navbar-default " role="navigation" style="background-color: #82c0ff">
					<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
						<h2>KUZCOLIGHTING</h2>
						<ul class="nav navbar-nav navbar-right" style="font-size: 15px">
							<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">About<strong
									class="caret"></strong></a>
								<ul class="dropdown-menu">
									<li><a href="#">managment</a></li>
									<li><a href="#">about</a></li>
									<li><a href="#">Logout</a></li>
									<li class="divider"></li>
									<li><a href="#">about</a></li>
								</ul></li>
						</ul>
					</div>
				</nav>
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-md-3 column " style="background-color: #d5eaff; height: 800px">
				<h3>Customer</h3>
				<router-view name="customer"></router-view>
				<h3>Customer Service</h3>
				<router-view name="service"></router-view>
			</div>
			<router-view name="content"></router-view>

		</div>

	</div>
	<script type="text/javascript" src="${ctx}/static/js/lib/jquery.js"></script>
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/chatAdminFunction.js"></script>
	<script>
		$(document).ready(function() {
			connect();
			router.push({
				name : 'home'
			});

		});
	</script>
	<!------------------ template------------------- -->
	<template id="customer">
	<div class="list-group" style="height: 250px; overflow: auto">
		<ul class="list-group">
			<li class="list-group-item" v-for="(customer,index) in customers"><router-link
					:to="{name:'content',params :{id:customer.userId},query:{name:customer.username}}" :id="customer.userId" style="color:#838383">{{customer.username}}</router-link></li>
		</ul>
	</div>
	</template>

	<template id="service">
	<div class="list-group" style="height: 360px; overflow-y: auto">
		<ul class="list-group">
			<li class="list-group-item" v-for="(user,index) in service"><span class="badge" style="background-color: blue;">online</span><a
				href="#" v-on:click="showUser(user.name,index)">{{user.name}}</a></li>
		</ul>
		<label for="name">Select service group</label><br> <select class="form-control" v-model="selected"
			v-on:change="select(selected)">
			<option v-for="service in serviceList" :value="service">{{service.name}}</option>
		</select>
	</div>
	</template>

	<template id="content">
	<div class="col-md-9 column">
		<div class="row clearfix">
			<div id="sentence" class="col-md-12 column"
				style="background-color: #f7f7f7; height: 600px; padding-top: 10px; overflow-y: auto;overflow-x: hidden; "></div>
		</div>
		<div class="row clearfix">
			<div class="col-md-12 column"
				style="border: 1px solid #c0c0c0; background-color: #95caff; height: 190px; margin-top: 10px">
				<p>Talk to : {{userName}}</p>
				<div class="form-inline" style="margin-top: 20px">
					<input type="text" size="100" v-on:keyup.enter="send" v-model="sentence" />
					<button type="button" class="btn btn-default" v-on:click="send(sentence)">Send</button>
				</div>
			</div>
		</div>
	</div>
	</template>


	<!------------------ template------------------- -->
</body>
</html>