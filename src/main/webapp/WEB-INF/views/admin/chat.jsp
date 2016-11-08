<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title></title>
 <meta http-equi="Content-Type" content="text/html; charset=" utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/adminStyle.css" />

<script type="text/javascript" src="${ctx}/static/js/lib/vue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vue-router.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vuex.js"></script>
<link href="bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<div id="main" class="container">
		<div class="row clearfix">
			<div class="col-md-12 column" style="background-color: #82c0ff">
				<nav class="navbar navbar-default " role="navigation" style="background-color: #82c0ff">
					<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
						<h2>KUZCOLIGHTING</h2>
						<ul class="nav navbar-nav navbar-right" style="font-size: 15px">
							<li><router-link :to="{name:'home'}">HOME</router-link></li>
							<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown<strong
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
			<div class="col-md-3 column" style="background-color: #d5eaff; height: 800px">
				<h3>Customer</h3>
				<router-view name="customer"></router-view>
				<h3>Customer Service</h3>
				<router-view name="service"></router-view>
				<button class="btn btn-primary btn-lg btn-block" >Pull service group</button>
			</div>
			<router-view name="content"></router-view>
		</div>

	</div>
<script type="text/javascript" src="${ctx}/static/js/lib/jquery.js"></script>
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/chatFunction.js"></script>
	<script>
		$( document ).ready(function() {
		   router.push({name :  'home'});
		});
	</script>
	<!------------------ template------------------- -->
	<template id="customer">
	<div class="list-group" style="height: 250px; overflow: auto">
		<ul class="list-group">
			<li class="list-group-item" v-for="customer in customers"><span class="badge" style="background-color: blue;">online</span>
				<router-link :to="{name:'content',params :{id:customer.name}}">{{customer.name}}</router-link></li>
		</ul>
	</div>
	</template>

	<template id="service">
	<div class="list-group" style="height: 360px; overflow: auto">

		<ul class="list-group">
			<li class="list-group-item" v-for="(user,index) in service"><span class="badge" style="background-color: blue;">online</span><a
				href="#" v-on:click="showUser(user.name,user.department,index)">{{user.name}}</a></li>
		</ul>
	</div>
	</template>

	<template id="content">
	<div class="col-md-9 column">
		<div class="row clearfix">
			<div class="col-md-12 column" style="background-color: #f7f7f7; height: 600px; padding-top: 10px; overflow: auto">
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-md-12 column"
				style="border: 1px solid #c0c0c0; background-color: #95caff; height: 190px; margin-top: 10px">
				<div class="btn-group">
					<button class="btn btn-default" type="button">image</button>
					<button class="btn btn-default" type="button">files</button>
				</div>
					<div class="form-inline"  style="margin-top: 20px">
						<input  type="text" size="100" v-on:keyup.enter="send(sentence)" v-model="sentence"/>
						<button type="button" class="btn btn-default" v-on:click="send(sentence)">Send</button>
					</div>
			</div>
		</div>
	</div>
	</template>


		<!------------------ template------------------- -->
</body>
</html>