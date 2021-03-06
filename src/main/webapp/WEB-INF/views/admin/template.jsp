
<template id="chatMainPage">
<div>
	<div class="row">
		<div class="col-lg-10">
			<h3 >Chat</h3>
		</div>
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default" style="background-color: #d8d8d8">
				<div class="panel-body">

					<!-- /.row (nested) -->

					<div class="row clearfix" style="min-height: 720px" >
						<div class="col-md-2 column ">
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
	<div class="list-group" style="height: 450px; overflow: auto;">
		<ul class="list-group" >
			<li class="list-group-item" v-for="(customer,index) in customers" :id="customer.username"><router-link
					:to="{path:'/admin/chat/content',query:{name:customer.username,id:customer.userId}}" :id="customer.userId" style="display:block">{{customer.username}}</router-link></li>
		</ul>
	</div>
</div>
</template>

<template id="service">
<div>
	<h4>Customer Service</h4>
	<div class="list-group" style="height: 250px; overflow-y: auto">
		<ul class="list-group" >
			<li class="list-group-item" v-for="(user,index) in service"><a href="javascript:void(0)"
				v-on:click="removeGroup(user.customerUserGroup.id,index)" style="display:block">{{user.userGroup.name}}</a></li>
		</ul>
		<label>Select service group</label><br> <select class="form-control" v-model="selected"
			v-on:change="selectFunction(selected)">
			<option v-for="service in serviceList" :value="service">{{service.name}}</option>
		</select>
	</div>
</div>
</template>

<template id="content">
<div>

	<div class="tabbable" >
		<div class="row clearfix">
			<ul class="nav nav-tabs tab1" style="margin-left: -1px;">
				<li class="active"><a href="#panel-100113" data-toggle="tab">Chat</a></li>
				<li><a href="#panel-586596" data-toggle="tab">History</a></li>
			</ul>
		</div>
		<div class="tab-content">
			<div class="tab-pane  fade in active" id="panel-100113">
				<div class="row clearfix">
					<div id="sentence"
						style="background-color: #ECEDF1; max-width: 1050px; height: 550px; margin-top: 0px; overflow-y: auto; overflow-x: hidden; padding-left: 20px; padding-right: 20px">
						<template v-for="message in messages">
						<div v-if="message.fromAdmin">
							<div class='row'>
								<div class=' pull-right' style='margin-top: 10px'>
									{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
							</div>
							<div class='row'>
								<div class='popover fade bottom in pull-right' role='tooltip'
									style='position: relative; display: block; max-width: 600px; word-wrap: break-word; background-color: #B7DFF8; z-index: 0'>
									<div class='arrow arrowcolor' style='left: 70%;'></div>
									<div class='popover-content' v-html="message.message"></div>
									<div v-if="message.attachments != undefined">
										<div v-for="attachment in message.attachments">
											<video v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												style="height: 280px; width: 350px" v-if="attachment.indexOf('mp4')!=-1">{{attachment}}
											</video>
											<audio v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												v-if="attachment.indexOf('ogg')!=-1">{{attachment}}
											</audio>
											<p style="padding-left: 10px; padding-right: 10px;">
												<a v-bind:href="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment"
													v-if="(attachment.indexOf('mp4')===-1)&&(attachment.indexOf('ogg')===-1)" style="color: red">{{attachment}}</a>
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div v-if="!message.fromAdmin">
							<div class='row'>
								<div class=' pull-left' style='margin-top: 10px'>
									{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
							</div>
							<div class='row'>
								<div class='popover fade bottom in pull-left' role='tooltip'
									style='position: relative; display: block; max-width: 600px; word-wrap: break-word; background-color: #FFFFFF; z-index: 0'>
									<div class='arrow arrowcolor1' style='left: 30%;'></div>
									<div class='popover-content' v-html="message.message"></div>
									<div v-if="message.attachments != undefined">
										<div v-for="attachment in message.attachments">
											<video v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												style="height: 280px; width: 350px" v-if="attachment.indexOf('mp4')!=-1">{{attachment}}
											</video>
											<audio v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												v-if="attachment.indexOf('ogg')!=-1">{{attachment}}
											</audio>
											<p style="padding-left: 10px; padding-right: 10px;">
												<a v-bind:href="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment"
													v-if="(attachment.indexOf('mp4')===-1)&&(attachment.indexOf('ogg')===-1)" style="color: red">{{attachment}}</a>
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						</template>
					</div>
				</div>

				<div class="row clearfix">
					<div
						style="max-width: 1050px; margin-top: 5px; border-style: solid; border-width: 0.2px; border-color: white; background-color: white">
						<input class="btn btn-default" id="inputFile" type="file" name='uploadFile1' multiple='multiple'
							v-on:change='showUploadFile()' style="display: none"> </input>
						<div class="row">
							<div class="btn-group col-md-3  column ">
								<button class="btn btn-default" type="button" v-on:click="document.getElementById('inputFile').click();"
									data-toggle="tooltip" title="Upload">
									<i class="fa fa-paperclip" aria-hidden="true"></i>
								</button>
								<button class="btn btn-default" type="button" v-on:click="video();" data-toggle="tooltip" title="Video">
									<i class="fa fa-video-camera" aria-hidden="true"></i>
								</button>
								<button class="btn btn-default" type="button" v-on:click="audio();" data-toggle="tooltip" title="Audio">
									<i class="fa fa-volume-up" aria-hidden="true"></i>
								</button>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12 column ">
								<textarea placeholder="Please enter message here..." class="form-control" v-model="sentence" rows="4"
									style="resize: none" maxlength="500"> </textarea>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12 column " style="margin-bottom: 10px">
								<button class="btn btn-defaut btn-block" type="button" v-on:click="send(sentence)"
									style="background-color: #c0c0c0">
									<i class="fa fa-share-square-o " aria-hidden="true">Send</i>
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="tab-pane" id="panel-586596">
				<div class="row clearfix">
					<div
						style="background-color: #ECEDF1; max-width: 1050px; height: 80px; margin-top: 0px; padding-left: 20px; padding-right: 20px; padding-top: 20px">
						<div class="input-group input-daterange " data-provide="datepicker" style="max-width: 750px; margin: auto">
							<span class="input-group-addon" style="border-left-width: 1px">Start date:</span> <input type="text"
								id="datepicker001" class="form-control  datepicker" data-date-format="yyyy-mm-dd" readonly="readonly"
								placeholder="Select date..." style="background-color: white"> <span class="input-group-addon">End
								date:</span> <input type="text" id="datepicker002" class="form-control datepicker" data-date-format="yyyy-mm-dd"
								readonly="readonly" placeholder="Select date..." style="background-color: white"> <span
								class="input-group-btn">
								<button class="btn btn-default" type="button" v-on:click="listHistory()">
									<i class="fa fa-search" aria-hidden="true"></i>
								</button>
							</span>
						</div>

					</div>
					<div id="historySentence"
						style="background-color: #ECEDF1; max-width: 1050px; height: 600px; margin-top: 0px; overflow-y: auto; overflow-x: hidden; padding-left: 20px; padding-right: 20px">
						<template v-for="message in historyMessages">
						<div v-if="message.fromAdmin">
							<div class='row'>
								<div class=' pull-right' style='margin-top: 10px'>
									{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
							</div>
							<div class='row'>
								<div class='popover fade bottom in pull-right' role='tooltip'
									style='position: relative; display: block; max-width: 600px; word-wrap: break-word; background-color: #B7DFF8; z-index: 0'>
									<div class='arrow arrowcolor' style='left: 70%;'></div>
									<div class='popover-content' v-html="message.message"></div>
									<div v-if="message.attachments != undefined">
										<div v-for="attachment in message.attachments">
											<video v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												style="height: 280px; width: 350px" v-if="attachment.indexOf('mp4')!=-1">{{attachment}}
											</video>
											<audio v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												v-if="attachment.indexOf('ogg')!=-1">{{attachment}}
											</audio>
											<p style="padding-left: 10px; padding-right: 10px;">
												<a v-bind:href="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment"
													v-if="(attachment.indexOf('mp4')===-1)&&(attachment.indexOf('ogg')===-1)" style="color: red">{{attachment}}</a>
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div v-if="!message.fromAdmin">
							<div class='row'>
								<div class=' pull-left' style='margin-top: 10px'>
									{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
							</div>
							<div class='row'>
								<div class='popover fade bottom in pull-left' role='tooltip'
									style='position: relative; display: block; max-width: 600px; word-wrap: break-word; background-color: #FFFFFF; z-index: 0'>
									<div class='arrow arrowcolor1' style='left: 30%;'></div>
									<div class='popover-content' v-html="message.message"></div>
									<div v-if="message.attachments != undefined">
										<div v-for="attachment in message.attachments">
											<video v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												style="height: 280px; width: 350px" v-if="attachment.indexOf('mp4')!=-1">{{attachment}}
											</video>
											<audio v-bind:src="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment" controls
												v-if="attachment.indexOf('ogg')!=-1">{{attachment}}
											</audio>
											<p style="padding-left: 10px; padding-right: 10px;">
												<a v-bind:href="'${ctx}/uploads/CsmChatMessage/'+message.id+'/'+attachment"
													v-if="(attachment.indexOf('mp4')===-1)&&(attachment.indexOf('ogg')===-1)" style="color: red">{{attachment}}</a>
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						</template>
					</div>
					<div class="text-center"
						style="background-color: #ECEDF1; max-width: 1050px; height: 40px; margin-top: 0px; padding-left: 20px; padding-right: 20px;">
						<ul class="pagination" style="margin-top: 2px" v-if="totalPage>1">
							<li v-bind:class="curPage===1?'disabled':''"><a href="javascript:void(0)" v-on:click="changePage(1)"><i
									class="fa fa-fast-backward"></i></a></li>
							<li v-bind:class="curPage===1?'disabled':''"><a href="javascript:void(0)" v-on:click="changePage(curPage-1)"><i
									class="fa fa-step-backward fa-large"></i></a></li>
							<li v-for="i in column" v-bind:class="curPage===lastNum-5+i?'active':''" ><a
								href="javascript:void(0)" v-on:click="changePage(lastNum-5+i)">{{lastNum-5+i}}</a></li>
							<li v-bind:class="curPage===totalPage?'disabled':''"><a href="javascript:void(0)"
								v-on:click="changePage(curPage+1)"><i class="fa fa-step-forward "></i></a></li>
							<li v-bind:class="curPage===totalPage?'disabled':''"><a href="javascript:void(0)"
								v-on:click="changePage(totalPage)"><i class="fa fa-fast-forward "></i></a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myVideoModal" style="margin-top: 200px" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header " style="background-color: #337ab7; color: white">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">{{modalTitle}}</h4>
				</div>
				<div class="modal-body" style="text-align: center">
					<video id="myVideo" autoplay muted width="400" height="280"></video>
				</div>
				<div class="modal-footer">
					<button type="button" v-bind:disabled="videostart" class="btn btn-primary pull-left" v-on:click="videoStart">Record</button>
					<button type="button" v-bind:disabled="videostop" class="btn btn-primary" v-on:click="videoCancel">Cancel</button>
					<button type="button" v-bind:disabled="videostop" class="btn btn-primary" v-on:click="videoStop">Send</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	<div class="modal fade" id="myAudioModal" style="margin-top: 200px" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header " style="background-color: #337ab7; color: white">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">{{modalTitle}}</h4>
				</div>
				<div class="modal-body" style="text-align: center">
					<audio id="myAudio" controls> Your browser does not support the audio tag.
					</audio>
				</div>
				<div class="modal-footer">
					<button type="button" v-bind:disabled="videostart" class="btn btn-primary pull-left" v-on:click="audioStart">Record</button>
					<button type="button" v-bind:disabled="videostop" class="btn btn-primary" v-on:click="audioCancel">Cancel</button>
					<button type="button" v-bind:disabled="videostop" class="btn btn-primary" v-on:click="audioStop">Send</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
</div>
</div>
</div>
</template>



<template id="permission">
<div>
	<h3>Permission</h3>
	<div class="list-group ">
		<div class=" list-group-item active ">
			<div class="row">
				<span class="col-sm-2"><b>Permission Name</b></span> <a href="javascript:void(0)" class="pull-right"
					style="color: white; margin-right: 15px" v-on:click="addNew"><i class="fa fa-plus fa-fw"></i><b>ADD NEW
						PERMISSION</b></a>
			</div>
		</div>

		<div class="modal fade" id="myPermissionModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header " style="background-color: #337ab7; color: white">
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
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>

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
		<div class=" list-group-item active ">
			<div class="row">
				<span class="col-sm-2"><b>Role Name</b></span> <a href="javascript:void(0)" class="pull-right"
					style="color: white; margin-right: 15px" v-on:click="addNew"><i class="fa fa-plus fa-fw"></i><b>ADD NEW
						ROLE</b></a>
			</div>
		</div>

		<div class="modal fade" id="roleModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header " style="background-color: #337ab7; color: white">
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
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>

					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>

		<template v-for="role in roles">
		<div class="list-group-item">
			<span><router-link :to="{path:'/role_permission',query:{id:role.id,name:role.name}}">{{role.name}}</router-link></span>
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
	<div class="row clearfix ">
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
		<div class=" list-group-item active ">
			<div class="row">
				<span class="col-sm-2"><b>User Group Name</b></span> <span class="col-sm-3 col-sm-offset-2"><b>Super User
						Group Name</b></span> <a href="javascript:void(0)" class="pull-right" style="color: white" v-on:click="addNew"><i
					class="fa fa-plus fa-fw"></i><b>ADD NEW GROUP</b></a>
			</div>
		</div>
		<div class="modal fade" id="myModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header " style="background-color: #337ab7; color: white">
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
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>
		<template v-for="group in groups">
		<div class="list-group-item" style="padding: 5px 10px">
			<div class="row">
				<span class="col-sm-2 "><router-link
						:to="{path:'/group_role',query:{id:group.userGroup.id,name:group.userGroup.name}}">{{group.userGroup.name}}</router-link></span>
				<span class="col-sm-3 col-sm-offset-2">{{group.superUserGroup==undefined?"":group.superUserGroup.name}}</span>
				<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
					v-on:click="del(group.userGroup.id)">delete</button>
				<button type="button" class="btn btn-primary btn-link" style="float: right; padding-top: 0px;"
					v-on:click="mod(group.userGroup.id)">modify</button>
			</div>
		</div>
		</template>
	</div>
</div>
</template>

<template id="group_role">
<div style="margin-top: 10px">
	<div class="row clearfix ">
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
		<div class=" list-group-item active ">
			<div class="row">
				<span class="col-sm-2"><b>Username</b></span> <a href="javascript:void(0)" class="pull-right"
					style="color: white; margin-right: 15px" v-on:click="addNew"> <i class="fa fa-plus fa-fw"></i> <b>ADD NEW
						USER</b></a>
			</div>
		</div>

		<div class="modal fade" id="myModal1" style="margin-top: 200px;" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header " style="background-color: #337ab7; color: white">
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
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>

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
	<a class="list-group-item  active"><b>Categories</b></a>
	<ul class="list-group-item">
		<li v-for="n in categoryList" class="list-group-item"><router-link
				:to="{path: '/faq/showFaqList', query: { id: n.id }}" v-bind:id=" n.id " style="display:block"> {{n.name}} </router-link></li>
	</ul>
</div>
</template>

<template id="FaqDetail">
<div class="list-group">
	<a href="javascript:void(0)" class="list-group-item  active text-right" v-on:click="faqAddNewUi()"><i
		class="fa fa-plus fa-fw"></i><b>ADD NEW FAQ</b></a>

	<div v-for="n in faqList" class="list-group-item">
		<span><router-link :to="{path: '/faq/faqModify', query: { id: n.id }}">{{n.question}}</router-link></span>
	</div>

</div>
</template>

<template id="FaqAddNewUi">
<div class=" form-group">
	<div class=" form-group">

		<label>QUESTION:</label>
		<textarea name='question' class="form-control" rows="3" v-model='question' maxlength="255"></textarea>
	</div>
	<div class=" form-group">
		<label>ANSWER:</label>
		<textarea name='answer' class="form-control" rows="5" v-model='answer' maxlength="500"></textarea>
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
		<textarea name='question' class="form-control" rows="3" v-model='question' maxlength="255"></textarea>
	</div>
	<div class=" form-group">
		<label>ANSWER:</label>
		<textarea name='answer' class="form-control" rows="5" v-model='answer' maxlength="500"></textarea>
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
		<a href="javascript:void(0)" class="list-group-item  active text-right" v-on:click="addNew"><i
			class="fa fa-plus fa-fw"></i><b>ADD NEW CATEGORY</b></a>

		<div class="modal fade" id="categoryModal" style="margin-top: 200px" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header " style="background-color: #337ab7; color: white">
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
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>

					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal -->
		</div>
		<template v-for="n in categoryList">
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
				<div class="modal-header " style="background-color: #337ab7; color: white">
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
					<button type="button" class="btn btn-default" v-on:click="closeModal">Cancel</button>

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

