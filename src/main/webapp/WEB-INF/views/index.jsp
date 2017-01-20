<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>FAQ</title>
<!-- Bootstrap Core CSS -->
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${ctx}/static/css/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/style.css" />
<link href="${ctx}/static/css/admin.css" rel="stylesheet">
<script type="text/javascript" src="${ctx}/static/js/lib/polyfill.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vuex.js"></script>

</head>

<body>
	<div id="mainPage" class="container-fluit" style="background: #f9f9f9">
		<div class="row clearfix">
			<div class="col-md-12 column">
				<nav class="navbar navbar-default navbar-fixed-top "
					style="background: #444 url(${ctx}/static/images/logo.jpg) no-repeat; height: 100px">
					<div class="container"
						style="background: transparent url(${ctx}/static/images/menubar.png); height: 60px; margin-top: 5px">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
								<span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span
									class="icon-bar"></span>
							</button>
							<a class="navbar-brand " href="www.kuzcolighting"
								style="font-size: 35px; margin-top: 5px; text-shadow: 2px 2px 4px #0067ce"><strong><i>KUZCO</i></strong></img></a>
						</div>
						<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="font-size: 18px; margin-top: 5px;">
							<form class="navbar-form navbar-left" role="search">
								<div class="input-group">
									<input type="search" class="form-control" v-model="searchSentence" placeholder="Search..." style="width: 400px; margin-left: 20px" v-on:keyup.enter = "ajaxSearch"> <span
										class="input-group-btn">
										<button class="btn btn-default" type="button" v-on:click="ajaxSearch">
											<i class="fa fa-search" aria-hidden="true"></i>
										</button>
									</span>
								</div>
							</form>
							<ul class="nav navbar-nav navbar-right">
								<li><router-link to="/home"> <i class="fa fa-home" aria-hidden="true"></i> Home</router-link></li>
								<li><router-link to="/contact"> <i class="fa fa-phone" aria-hidden="true"></i> Contact Us</router-link></li>
							</ul>
						</div>
					</div>
				</nav>
			</div>
		</div>

		<div class=" container " style="margin: 115px auto 10px auto; min-height: 720px">
			<router-view></router-view>
		</div>

		<div class="row clearfix  ">
			<div class="col-md-12 column">
				<footer class="text-center" style="width: 100%">
					<p>
						<img src="${ctx}/static/images/twitter.png" alt="twitter" />&nbsp;<img src="${ctx}/static/images/facebook.png"
							alt="facebook" />&nbsp;<img src="${ctx}/static/images/rss.png" alt="rss" />
					</p>
					<p>
						Copyright &copy; <a href="http://www.kuzcolighting.com" style="color: white">design from Kuzcolighting</a>
					</p>
				</footer>
			</div>
		</div>
	</div>

	<template id="faqPage">
	<div class="row clearfix">
		<div class="col-md-3 column" style="padding-left: 0px;">
			<div class="list-group" style="box-shadow: 0 1px 4px rgba(0, 0, 0, .115)">
				<a class="list-group-item " style="background-color: #f5f5f5">Categories</a> <a href="javascript:void(0)"
					class="list-group-item" v-for="category in categories " v-on:click="showCategoryDetail(category.id)"
					v-bind:id="category.id">{{category.name}} <i class="fa fa-angle-right pull-right" aria-hidden="true"></i>
				</a>
			</div>

		</div>

		<div class="col-md-9 column" style="padding-right: 0px">
			<div class="panel-group" id="panel-747372">
				<div class="panel panel-default" v-for="faq in faqs">
					<div class="panel-heading" style="box-shadow: 0 1px 4px rgba(0, 0, 0, .115);padding:8px 10px">
						<a class="panel-title" data-toggle="collapse" data-parent="#panel-747372" v-bind:href="'#'+faq.id" style="display:block">{{faq.question}}</a>
					</div>
					<div v-bind:id="faq.id" class="panel-collapse collapse">
						<div class="panel-body" >{{faq.answer}}</div>
						<hr v-if="faq.attachments" style="margin-top: 5px; margin-bottom: 5px">
						<template v-for="attachment in faq.attachments"> <a
							v-bind:href="'${ctx}/uploads/CsmFaq/'+faq.id+'/'+attachment"
							style="margin-left: 15px; margin-bottom: 10px; color: red;">{{attachment}}</a> </template>
					</div>

				</div>
			</div>
			<div class="text-center"
				style=" max-width: 1050px; height: 40px; margin-top: 0px; padding-left: 20px; padding-right: 20px;">
				<ul class="pagination" style="margin-top: 2px;box-shadow: 1px 1px 4px rgba(0, 0, 0, .115)" v-if="totalPage>1">
					<li v-bind:class="curPage===1?'disabled':''"><a href="javascript:void(0)" v-on:click="changePage(1)"><i
							class="fa fa-fast-backward"></i></a></li>
					<li v-bind:class="curPage===1?'disabled':''"><a href="javascript:void(0)" v-on:click="changePage(curPage-1)"><i
							class="fa fa-step-backward fa-large"></i></a></li>
					<li v-for="i in column" v-bind:class="curPage===lastNum-5+i?'active':''"><a href="javascript:void(0)"
						v-on:click="changePage(lastNum-5+i)">{{lastNum-5+i}}</a></li>
					<li v-bind:class="curPage===totalPage?'disabled':''"><a href="javascript:void(0)"
						v-on:click="changePage(curPage+1)"><i class="fa fa-step-forward "></i></a></li>
					<li v-bind:class="curPage===totalPage?'disabled':''"><a href="javascript:void(0)"
						v-on:click="changePage(totalPage)"><i class="fa fa-fast-forward "></i></a></li>
				</ul>
			</div>
		</div>
	</div>
	</template>

	<template id="contact">
	<div class="row clearfix">Contact Us</div>
	</template>

	<script type=text/javascript>
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />";
		ctx = "${ctx}";
	</script>
	<script src="${ctx}/static/js/lib/jquery.js"></script>
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/lib/vue-router.js"></script>
	<script src="${ctx}/static/js/faqFunction.js"></script>


</body>
</html>