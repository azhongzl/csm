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
<title>KUZCOLIGHTING</title>
<!-- Bootstrap Core CSS -->
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
<!-- MetisMenu CSS -->
<link href="${ctx}/static/css/lib/metisMenu.min.css" rel="stylesheet">
<!-- Custom CSS -->
<link href="${ctx}/static/css/lib/sb-admin-2.css" rel="stylesheet">
<!-- Custom Fonts -->
<link href="${ctx}/static/css/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx}/static/css/admin.css" rel="stylesheet">
<script type="text/javascript" src="${ctx}/static/js/lib/polyfill.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vuex.js"></script>
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>


</head>

<body style="max-width: 1600px; margin:0 auto;">

	<div id="wrapper"  >
		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="http://www.kuzcolighting.com">KUZCOLIGHTING</a>
			</div>
			<!-- /.navbar-header -->

			<ul class="nav navbar-top-links navbar-right">

				<li style="color: blue; margin-right: 50px;"><i><b>Hello : <shiro:principal property="username" /></b></i></li>
				<li><router-link to="/admin/chat"> <i class="fa  fa-comments fa-fw"
						style="padding-right: 40px; color: red">chat</i> </router-link></li>
				<!-- /.dropdown -->
				<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i
						class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user">
						<li><router-link to="/profile"> <i class="fa  fa-user fa-fw">User Profile</i> </router-link></li>
						<li><a href="#"><i class="fa fa-gear fa-fw"></i> Settings</a></li>
						<li class="divider"></li>
						<li><a href="${ctx}/logout"><i class="fa fa-sign-out fa-fw"></i> Logout</a></li>
					</ul> <!-- /.dropdown-user --></li>
				<!-- /.dropdown -->
			</ul>
			<!-- /.navbar-top-links -->

			<div class="navbar-default sidebar" role="navigation" >
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<li><a href="#"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a></li>
						<li><a href="#"><i class="fa fa-edit fa-fw"></i> FAQ<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<li><router-link :to="{path:'/faq'}">FAQ</router-link></li>
								<li><router-link :to="{path:'/category'}">Category</router-link></li>
							</ul> <!-- /.nav-second-level --></li>

						<li><a href="#"><i class="fa fa-user  fa-fw"></i> USER<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<li><router-link to="/user">User</router-link></li>
								<li><router-link to="/group">Group</router-link></li>
								<li><router-link to="/role">Role</router-link></li>
								<li><router-link to="/permission">Permission</router-link></li>

							</ul> <!-- /.nav-second-level --></li>
						<li><a href="#"><i class="fa fa-files-o fa-fw"></i> Sample Pages<span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<li><a href="blank.html">Blank Page</a></li>
								<li><a href="login.html">Login Page</a></li>
							</ul> <!-- /.nav-second-level --></li>
					</ul>
				</div>
				<!-- /.sidebar-collapse -->
			</div>
			<!-- /.navbar-static-side -->
		</nav>

		<div id="page-wrapper">
			<router-view name="mainRouter"></router-view>
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->

	<%@include file="template.jsp"%>
	<script type=text/javascript>
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />";
		ctx = "${ctx}";
	</script>
	<!-- jQuery -->
	<script src="${ctx}/static/js/lib/jquery.js"></script>
	<!-- Bootstrap Core JavaScript -->
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<!-- Metis Menu Plugin JavaScript -->
	<script src="${ctx}/static/js/lib/metisMenu.min.js"></script>
	<script src="${ctx}/static/js/myMediaRecorder.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/lib/vue-router.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/common.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/store.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/component.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/router.js"></script>
	<!-- Custom Theme JavaScript -->
	<script src="${ctx}/static/js/lib/sb-admin-2.js"></script>
</body>
</html>