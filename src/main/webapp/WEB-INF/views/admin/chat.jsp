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
<script type="text/javascript" src="${ctx}/static/js/lib/vuex.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/polyfill.js"></script>


<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
<link href="${ctx}/static/css/adminChat.css" rel="stylesheet">
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
							<li class="dropdown"><a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown"
								style="background-color: #82c0ff">PROFILE<strong class="caret"></strong></a>
								<ul class="dropdown-menu">
									<li><a href="javascript:void(0)">OPTION1</a></li>
									<li class="divider"></li>
									<li><a href="javascript:void(0)">OPTION2</a></li>
									<li class="divider"></li>
									<li><a href="javascript:void(0)">OPTION3</a></li>
								</ul></li>
							<li class="dropdown"><a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown"
								style="background-color: #82c0ff">MENU<strong class="caret"></strong></a>
								<ul class="dropdown-menu">

									<li><router-link :to="{name:'user'}">User</router-link></li>
									<li class="divider"></li>
									<li><router-link :to="{name:'group'}">Group</router-link></li>
									<li class="divider"></li>
									<li><router-link :to="{name:'role'}">Role</router-link></li>
									<li class="divider"></li>
									<li><router-link :to="{name:'permission'}">Permission</router-link></li>
									<li class="divider"></li>
									<li><a href="javascript:void(0)">Logout</a></li>
								</ul></li>
						</ul>
					</div>
				</nav>
			</div>
		</div>
		<div class="row clearfix">
			<div class="col-md-3 column " style="background-color: #d5eaff; height: 800px">
				<router-view name="customer"></router-view>
				<router-view name="service"></router-view>
			</div>
			<div class="col-md-9 column">
				<router-view name="content"></router-view>
			</div>
		</div>

	</div>


	<!------------------ template------------------- -->
	<template id="customer">
	<div>
		<h3>Customer</h3>
		<div class="list-group" style="height: 250px; overflow: auto">
			<ul class="list-group">
				<li class="list-group-item" v-for="(customer,index) in customers"><router-link
						:to="{name:'content',params :{id:customer.userId},query:{name:customer.username}}" :id="customer.userId"
						style="color:#838383">{{customer.username}}</router-link></li>
			</ul>
		</div>
	</div>
	</template>

	<template id="service">
	<div>
		<h3>Customer Service</h3>
		<div class="list-group" style="height: 360px; overflow-y: auto">
			<ul class="list-group">
				<li class="list-group-item" v-for="(user,index) in service"><span class="badge" style="background-color: blue;">online</span><a
					href="javascript:void(0)" v-on:click="showUser(user.name,index)">{{user.name}}</a></li>
			</ul>
			<label for="name">Select service group</label><br> <select class="form-control" v-model="selected"
				v-on:change="select(selected)">
				<option v-for="service in serviceList" :value="service">{{service.name}}</option>
			</select>
		</div>
	</div>
	</template>

	<template id="permission">
	<div>
		<h3>Permission</h3>
		<div class="list-group">
			<a href="javascript:viod(0)" class="list-group-item " v-on:click="addNew">ADD NEW PERMISSION</a>
			<template v-for="permission in permissions">
			<div class="list-group-item">
				<span>{{permission.permission}}</span>
				<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
					v-on:click="del(permission.id)">delete</button>
				<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
					v-on:click="mod(permission.id,permission.permission)">modify</button>
			</div>
			</template>
		</div>
	</div>
	</template>

	<template id="role">
	<div>
		<h3>Role</h3>
		<div class="list-group">
			<a href="javascript:viod(0)" class="list-group-item " v-on:click="addNew">ADD NEW ROLE</a>
			<template v-for="role in roles">
			<div class="list-group-item">
				<span><router-link :to="{name:'role_permission',query:{id:role.id,name:role.name}}">{{role.name}}</router-link></span>
				<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
					v-on:click="del(role.id)">delete</button>
				<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
					v-on:click="mod(role.id,role.name)">modify</button>
			</div>
			</template>
		</div>
	</div>
	</template>

	<template id="role_permission">
	<div style="margin-top: 10px">
		<div class="row clearfix">
			<div class="col-md-6 column">
				<div class="list-group">
					<a class="list-group-item active">Please select permission(click to selected)</a>
					<div style="height: 700px; overflow-y: auto">
						<div class="list-group-item" v-for="permission in permissions">
							<button type="button" class="btn btn-primary btn-link" v-on:click="add(permission.id)">{{permission.permission}}</button>
						</div>
					</div>

				</div>
			</div>
			<div class="col-md-6 column">
				<div class="list-group">
					<a class="list-group-item active"><span style="color: red">{{this.$route.query.name}}</span> : selected
						permission(click to remove)</a>

					<div style="height: 700px; overflow-y: auto">
						<div class="list-group-item" v-for="permission in role_permissions">
							<button type="button" class="btn btn-primary btn-link" v-on:click="remove(permission.id)">{{permission.permissionName}}</button>
						</div>
						<a href="javascript:void(0)" v-on:click="back" class="list-group-item active"> Back</a>
					</div>


				</div>
			</div>
		</div>
	</div>
	</template>

	<template id="group">
	<div>
		<h3>Group</h3>
		<div class="list-group">
			<a href="javascript:viod(0)" class="list-group-item " v-on:click="addNew">ADD NEW GROUP</a>

			<div class="modal fade" id="myModal" style="margin-top: 200px" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">ADD NEW GROUP</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal" role="form">
								<div class="form-group">
									<label for="inputEmail3" class="col-sm-2 control-label">Group</label>
									<div class="col-sm-10">
										<input class="form-control" id="inputEmail3" type="text" v-model="groupText" />
									</div>
									<label for="inputEmail4" class="col-sm-2 control-label"> Super Group</label>
									<div class="col-sm-10">
										<select class="form-control" v-model="sele" id="inputEmail4" style="margin-top: 10px">
											<option v-for="opt in groupOptions" v-bind:value="opt.id">{{opt.name}}</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10">
										<div class="checkbox">
											<label><input type="checkbox" v-model="checked" />admin</label>
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" v-on:click="addNewSubmit">submit</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Cancle</button>

						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal -->
			</div>
			<template v-for="group in groups">
			<div class="list-group-item">
				<span><router-link :to="{name:'group_role',query:{id:group.id,name:group.name}}">{{group.name}}</router-link><span>
						<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
							v-on:click="del(group.id,group.name)">delete</button>
						<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
							v-on:click="mod(group.id,group.name,group.admin,group.superId)">modify</button>
			</div>
			</template>
		</div>
	</div>
	</template>

	<template id="group_role">
	<div style="margin-top: 10px">
		<div class="row clearfix">
			<div class="col-md-6 column">
				<div class="list-group">
					<a class="list-group-item active">Please select role(click to selected)</a>
					<div style="height: 700px; overflow-y: auto">
						<div class="list-group-item" v-for="role in roles">
							<button type="button" class="btn btn-primary btn-link" v-on:click="add(role.id)">{{role.name}}</button>
						</div>
					</div>

				</div>
			</div>
			<div class="col-md-6 column">
				<div class="list-group">
					<a class="list-group-item active"><span style="color: red">{{this.$route.query.name}}</span> : selected
						roles(click to remove)</a>

					<div style="height: 700px; overflow-y: auto">
						<div class="list-group-item" v-for="group_role in group_roles">
							<button type="button" class="btn btn-primary btn-link" v-on:click="remove(group_role.id)">{{group_role.roleName}}</button>
						</div>
						<a href="javascript:void(0)" v-on:click="back" class="list-group-item active"> Back</a>
					</div>


				</div>
			</div>
		</div>
	</div>
	</template>

	<template id="user">
	<div>
		<h3>User</h3>
		<div class="list-group">
			<a href="javascript:viod(0)" class="list-group-item " v-on:click="addNew">ADD NEW USER</a>

			<div class="modal fade" id="myModal1" style="margin-top: 200px" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">ADD NEW USER</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal" role="form">
								<div class="form-group">
									<label for="inputEmail5" class="col-sm-3 control-label">Username</label>
									<div class="col-sm-9" v-if="showKey">
										<input class="form-control" id="inputEmail5" type="text" v-model="username" disabled="disabled" />
									</div>
									<div class="col-sm-9" v-else>
										<input class="form-control" id="inputEmail5" type="text" v-model="username" />
									</div>
									<label for="inputEmail6" class="col-sm-3 control-label">Group</label>
									<div class="col-sm-9">
										<select class="form-control" v-model="sele" id="inputEmail6" style="margin-top: 10px">
											<option v-for="opt in groupOptions" v-bind:value="opt.id">{{opt.name}}</option>
										</select>
									</div>
								</div>
								<div class="form-group" v-show="showKey">
									<div class="col-sm-offset-3 col-sm-9">
										<div class="checkbox">
											<label><input type="checkbox" v-model="checked" />Active</label>
										</div>
									</div>
								</div>
							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" v-on:click="addNewSubmit">submit</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">Cancle</button>

						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal -->
			</div>
			<template v-for="user in users">
			<div class="list-group-item">
				<span>{{user.username}}<span>
						<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
							v-on:click="del(user.id)">delete</button>
						<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
							v-on:click="mod(user.id,user.username,user.active,user.userGroupId)">modify</button>
			</div>
			</template>
		</div>
	</div>
	</template>

	<template id="content">
	<div>
		<div class="row clearfix">
			<div id="sentence"
				style="background-color: #f7f7f7; height: 600px; padding-top: 10px; overflow-y: auto; overflow-x: hidden;"></div>
		</div>
		<div class="row clearfix">
			<div style="border: 1px solid #c0c0c0; background-color: #95caff; height: 190px; margin-top: 10px">
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
	<script type="text/javascript" src="${ctx}/static/js/lib/vue-router.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/lib/jquery.js"></script>
	<script src="${ctx}/static/js/lib/bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/chatAdminFunction.js"></script>
	<script type=text/javascript>
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />";
		ctx = "${ctx}";
	</script>
</body>
</html>