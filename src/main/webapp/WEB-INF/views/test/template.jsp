



<template id="chatMainPage">
<div>
	<div class="row">
		<div class="col-lg-12">
			<h3 class="page-header">Chat</h3>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">

					<!-- /.row (nested) -->

					<div class="row clearfix">
						<div class="col-md-2 column " style="height: 650px">
							<router-view name="customer"></router-view>
							<router-view name="service"></router-view>
						</div>
						<div class="col-md-10 column">
							<router-view name="content"></router-view>
						</div>
					</div>
				</div>
				<!-- /.panel-body -->
			</div>
			<!-- /.panel -->
		</div>
		<!-- /.col-lg-12 -->
	</div>
</div>
<!-- /.row --> </template>

<!------------------ template------------------- -->
<template id="customer">
<div>
	<h4>Customer</h4>
	<div class="list-group" style="height: 250px; overflow: auto">
		<ul class="list-group">
			<li class="list-group-item" v-for="(customer,index) in customers" ><router-link
					:to="{path:'/admin/chat/content',query:{name:customer.username,id:customer.userId}}" :id="customer.userId">{{customer.username}}</router-link></li>
		</ul>
	</div>
</div>
</template>

<template id="service">
<div>
	<h4>Customer Service</h4>
	<div class="list-group" style="height: 250px; overflow-y: auto">
		<ul class="list-group">
			<li class="list-group-item" v-for="(user,index) in service"><a href="javascript:void(0)"
				v-on:click="removeGroup(user.customerUserGroup.id,index)">{{user.userGroup.name}}</a></li>
		</ul>
		<label>Select service group</label><br> <select class="form-control" v-model="selected"
			v-on:change="select(selected)">
			<option v-for="service in serviceList" :value="service">{{service.name}}</option>
		</select>
	</div>
</div>
</template>

<template id="content">
<div>
	<div class="row clearfix">
		<div id="sentence"
			style="background-color: #f7f7f7; max-width: 1000px; height: 600px; padding-top: 10px; overflow-y: auto; overflow-x: hidden; padding-left: 20px; padding-right: 20px"></div>
	</div>
	<div class="row clearfix">
		<div style="max-width: 1000px; height: 60px; margin-top: 2px; margin-left: 50px">
			<p>
			<h3>Talk to : {{customerName}}</h3>
			</p>
			<div class="form-group input-group" style="max-width: 900px; margin-left: 10px">
				<input type="text" class="form-control" v-on:keyup.enter="send" v-model="sentence"> <span
					class="input-group-btn">
					<button class="btn btn-default" type="button" v-on:click="send(sentence)">
						<i class="fa fa-share">send</i>
					</button>
				</span>
			</div>
		</div>
	</div>
</div>
</template>



<template id="permission">
<div>
	<h3>Permission</h3>
	<div class="list-group">
		<a href="javascript:viod(0)" class="list-group-item active" v-on:click="addNew">ADD NEW PERMISSION</a>

		<div class="modal fade" id="myPermissionModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">{{modalTitle}}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="inputEmail31" class="col-sm-2 control-label">Name</label>
								<div class="col-sm-10">
									<input class="form-control" id="inputEmail31" type="text" v-model="permissionName" />
								</div>
								<label for="inputEmail311" class="col-sm-2 control-label" style="margin-top: 10px">Permission</label>
								<div class="col-sm-10" style="margin-top: 10px">
									<input class="form-control" id="inputEmail311" type="text" v-model="permissionValue" />
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



		<template v-for="permission in permissions">
		<div class="list-group-item">
			<span>{{permission.name}}</span>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="del(permission.id)">delete</button>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="mod(permission.id,permission.name,permission.permission)">modify</button>
		</div>
		</template>
	</div>
</div>
</template>

<template id="role">
<div>
	<h3>Role</h3>
	<div class="list-group">
		<a href="javascript:viod(0)" class="list-group-item active" v-on:click="addNew">ADD NEW ROLE</a>

		<div class="modal fade" id="roleModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">{{modalTitle}}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="inputEmail3" class="col-sm-2 control-label">Name</label>
								<div class="col-sm-10">
									<input class="form-control" id="inputEmail3" type="text" v-model="name" />
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



		<template v-for="role in roles">
		<div class="list-group-item">
			<span><router-link :to="{path:'/role_permission',query:{id:role.id}}">{{role.name}}</router-link></span>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="del(role.id)">delete</button>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="mod(role.id)">modify</button>
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
						<button type="button" class="btn btn-primary btn-link" v-on:click="add(permission.id)">{{permission.name}}</button>
					</div>
				</div>

			</div>
		</div>
		<div class="col-md-6 column">
			<div class="list-group">
				<a class="list-group-item active"><span style="color: red">{{this.$route.query.name}}</span> : selected
					permission(click to remove)</a>

				<div style="height: 700px; overflow-y: auto">
					<div class="list-group-item" v-for="per in rolePermissions">
						<button type="button" class="btn btn-primary btn-link" v-on:click="remove(per.rolePermission.id)">{{per.permission.name}}</button>
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
		<a href="javascript:viod(0)" class="list-group-item  active" v-on:click="addNew">ADD NEW GROUP</a>

		<div class="modal fade" id="myModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">{{modalTitle}}</h4>
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
										<label><input type="checkbox" v-model="checked1" />admin</label> <label v-show="checked1"><input
											type="checkbox" v-model="checked2" />chat</label>
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
			<span><router-link :to="{path:'/group_role',query:{id:group.id,name:group.name}}">{{group.name}}</router-link></span>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="del(group.id)">delete</button>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="mod(group.id)">modify</button>
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
						<button type="button" class="btn btn-primary btn-link" v-on:click="remove(group_role.userGroupRole.id)">{{group_role.role.name}}</button>
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
		<a href="javascript:viod(0)" class="list-group-item active " v-on:click="addNew">ADD NEW USER</a>

		<div class="modal fade" id="myModal1" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">{{modalTitle}}</h4>
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
						v-on:click="mod(user.id)">modify</button>
		</div>
		</template>
	</div>

	<div id="pagecount" class="row" v-show="pageShowKey">
		<div align="center">
			<span><a href="javascript:void(0)" v-on:click="changePage(1)"> &laquo; </a></span> <span><a
				href="javascript:void(0)" v-on:click="changePage(curPage - 1)"> &lsaquo; </a></span> <span v-for="n in totalPage"
				:class="curPage === n ? 'current' : ''"> <a href="javascript:void(0)" v-on:click="changePage(n)">{{ n }}</a>
			</span> <span><a href="javascript:void(0)" v-on:click="changePage(curPage + 1)"> &rsaquo; </a></span> <span><a
				href="javascript:void(0)" v-on:click="changePage(totalPage)"> &raquo; </a></span>
		</div>

	</div>

</div>
</template>

<template id="faqMain">
<div>
	<div class="row">
		<div class="col-lg-12">
			<h3 class="page-header">FAQ</h3>
		</div>
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-body">
					<div class="row clearfix">
						<div class="col-md-3 column ">
							<router-view></router-view>
						</div>
						<div class="col-md-9 column">
							<div id="content" class="col-lg-12">
								<router-view name="content"></router-view>
							</div>
							<div class="col-md-5 column"></div>
							<div id="pagecount" class="col-md-2 column">
								<router-view name="pagecount"></router-view>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</template>

<template id="FaqCategory">
<div class="list-group" style="max-height: 650px; overflow: auto">
	<a class="list-group-item  active">Categories</a>
	<ul class="list-group-item">
		<li v-for="n in list" class="list-group-item"><router-link :to="{path: '/faq/showFaqList', query: { id: n.id }}"
				v-bind:id=" n.id "> {{n.name}} </router-link></li>
	</ul>
</div>
</template>

<template id="FaqDetail">
<div class="list-group">
	<a href="javascript:void(0)" class="list-group-item  active" v-on:click="faqAddNewUi()">ADD NEW FAQ</a>

	<div v-for="n in list1" class="list-group-item">
		<span><router-link :to="{path: '/faq/faqModify', query: { id: n.id }}">{{n.question}}</router-link></span>
	</div>

</div>
</template>

<template id="FaqAddNewUi">
<div class=" form-group">
	<div class=" form-group">

		<label>QUESTION:</label>
		<textarea name='question' class="form-control" rows="3" v-model='question'></textarea>
	</div>
	<div class=" form-group">
		<label>ANSWER:</label>
		<textarea name='answer' class="form-control" rows="5" v-model='answer'></textarea>
	</div>
	<div class=" form-group">
		<label>Upload File:</label> <input type='file' name='uploadFile' multiple='multiple' v-on:change='showUploadFile()' />
	</div>
	<div class=" form-group">
		<label>Uploaded files:</label>
		<textarea name='attachments' rows='3' class="form-control" disabled='disabled' v-model='attachements'></textarea>
	</div>
	<button class="btn btn-default" v-on:click='faqAddNew()' style="width: 160px;">Submit</button>
	<button class="btn btn-default" v-on:click='faqCancel()' style="width: 160px;">Cancel</button>
</div>
</template>

<template id="FaqModifyUi">

<div>
	<div class=" form-group">
		<label>QUESTION:</label>
		<textarea name='question' class="form-control" rows="3" v-model='question'></textarea>
	</div>
	<div class=" form-group">
		<label>ANSWER:</label>
		<textarea name='answer' class="form-control" rows="5" v-model='answer'></textarea>
	</div>
	<div class=" form-group">
		<lable v-show="attachments.length === 0?false:true">Attachments:</lable>
		<template v-for='attachment in attachments'> <lable> <input type='checkbox' checked='checked'
			v-bind:value='attachment' v-model='attach' v-show='attachments.length === 0?false:true' />{{attachment}} </lable> </template>
	</div>
	<div class=" form-group">
		<label>Upload File:</label> <input type='file' name='uploadFile' multiple='multiple' v-on:change='showUploadFile()' />
	</div>
	<div class=" form-group">
		<label>Uploaded files:</label>
		<textarea name='attachments1' rows='3' class="form-control" disabled='disabled' v-model='attachements1'></textarea>
	</div>
	<button class="btn btn-default" v-on:click='adminFaqPut()' style="width: 160px;">Submit</button>
	<button class="btn btn-default" v-on:click='adminFaqDelete()' style="width: 160px;">Delete</button>
	<button class="btn btn-default" v-on:click='faqCancel()' style="width: 160px;">Cancel</button>
</div>
</template>

<template id="category">
<div>
	<h3>Categories</h3>
	<div class="list-group">
		<a href="javascript:viod(0)" class="list-group-item  active" v-on:click="addNew">ADD NEW CATEGORY</a>

		<div class="modal fade" id="categoryModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">ADD NEW CATEGORY</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="input1" class="col-sm-2 control-label">Name</label>
								<div class="col-sm-10">
									<input class="form-control" id="input1" type="text" v-model="name" />
								</div>
								<label for="input2" class="col-sm-2 control-label"> Description</label>
								<div class="col-sm-10" style="margin-top: 10px">
									<input class="form-control" id="input2" type="text" v-model="description" />
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
		<template v-for="n in list">
		<div class="list-group-item">
			<span>{{n.name}}</span>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;" v-on:click="del(n.id)">delete</button>
			<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
				v-on:click="mod(n.id,n.name,n.description)">modify</button>
		</div>
		</template>
	</div>
</div>
</template>
<template id="profile">
<div>
	<div class="modal fade" id="profileModal" style="margin-top: 200px" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Change Password</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="input1" class="col-sm-3 control-label">Name</label>
							<div class="col-sm-9">
								<input class="form-control" id="input1" type="text" v-model="name" disabled />
							</div>
							<label for="input2" class="col-sm-3 control-label" style="margin-top: 10px">New Password</label>
							<div class="col-sm-9" style="margin-top: 10px">
								<input class="form-control" placeholder="New password" id="input2" type="password" v-model="password1" autofocus />
							</div>
							<label for="input3" class="col-sm-3 control-label">New Password</label>
							<div class="col-sm-9" style="margin-top: 10px">
								<input class="form-control" placeholder="Retype new password" id="input3" type="password" v-model="password2" />
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
</div>
</template>

<template id="pageChange">
<div v-show="showKey">
	<span><a href="javascript:void(0)" v-on:click="changePage(1)"> &laquo; </a></span> <span><a
		href="javascript:void(0)" v-on:click="changePage(curPage - 1)"> &lsaquo; </a></span> <span v-for="n in totalPage"
		:class="curPage === n ? 'current' : ''"> <a href="javascript:void(0)" v-on:click="changePage(n)">{{ n }}</a>
	</span> <span><a href="javascript:void(0)" v-on:click="changePage(curPage + 1)"> &rsaquo; </a></span> <span><a
		href="javascript:void(0)" v-on:click="changePage(totalPage)"> &raquo; </a></span>
</div>
</template>

<!------------------ template------------------- -->
<!-- myAlert -->
<div class="modal fade" id="myAlertModal" style="margin-top: 200px" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width: 300px">
		<div class="modal-content">
			<div class="modal-header " style="padding: 5px; background-color: #0066cc">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel" style="color: white">{{modalTitle}}</h4>
			</div>
			<div class="modal-body " style="padding: 25px;" align="center">
				<span style="font-size: 16px">{{message}}</span>
			</div>
			<div class="modal-footer" style="padding: 3px">
				<button type="button" class="btn btn-default" data-dismiss="modal" style="padding: 6px">Close</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
</div>

