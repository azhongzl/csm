<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>KUZCOLIGHTING CHAT</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link href="${ctx}/static/css/lib/bootstrap.css" rel="stylesheet">
<link href="${ctx}/static/css/lib/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx}/static/css/lib/bootstrap-datepicker3.standalone.min.css" rel="stylesheet">
<link href="${ctx}/static/css/chat.css" rel="stylesheet">
<script src="${ctx}/static/js/lib/jquery.js"></script>
<script src="${ctx}/static/js/lib/bootstrap.js"></script>
<script src="${ctx}/static/js/lib/jquery.slidereveal.min.js"></script>
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>
<script type="text/javascript" src="${ctx}/static/js/lib/vue.js"></script>
</head>

<script type="text/javascript">
	$(function() {

		$('#slider').slideReveal({
			trigger : $("#trigger"),
			position : "right",
			push : false,
			width : 640,
			top : 200
		});
		$("#uploadFile").hide();
	});
</script>
<body>
	<div id="chatContainer">
		<div id="slider" class="slider">
			<div class="well" style="padding-top: 0px">
				<div class="row clearfix" style="margin-top: 2px">
					<ul class="nav nav-tabs tab1" style="margin-left: -1px;">
						<li class="active"><a href="#panel-100113" data-toggle="tab">Chat</a></li>
						<li><a href="#panel-586596" data-toggle="tab">History</a></li>
					</ul>
				</div>
				<div class="tab-content ">
					<div class="tab-pane  fade in active" id="panel-100113">
						<div class="row clearfix">
							<div id="sentence" class="well"
								style="max-width: 650px; height: 580px; overflow-y: auto; overflow-x: hidden; background-color: #ebebeb; margin-bottom: 0px">

								<template v-for="message in messages">
								<div v-if="!message.fromAdmin">
									<div class='row'>
										<div class=' pull-right' style='margin-top: 10px'>
											{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
									</div>
									<div class='row'>
										<div class='popover fade bottom in pull-right' role='tooltip'
											style='position: relative; display: block; max-width: 500px; word-wrap: break-word; background-color: #B7DFF8; z-index: 0'>
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
								<div v-if="message.fromAdmin">
									<div class='row'>
										<div class=' pull-left' style='margin-top: 10px'>
											{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
									</div>
									<div class='row'>
										<div class='popover fade bottom in pull-left' role='tooltip'
											style='position: relative; display: block; max-width: 500px; word-wrap: break-word; background-color: #FFFFFF; z-index: 0'>
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
							<div class="panel panel-default" style="width: 650px; background-color: #dddcd9; margin-top: 0">

								<input class="btn btn-default" id="inputFile" type="file" name='uploadFile1' multiple='multiple'
									v-on:change='showUploadFile()' style="display: none"> </input>
								<div class="row">
									<div class="btn-group col-md-5  column">
										<button class="btn btn-default" type="button" v-on:click="listHistory()" data-toggle="tooltip"
											title="Display history">
											<i class="fa fa-history" aria-hidden="true"></i>
										</button>
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
										<textarea placeholder="Please enter message here..." id="message" class="form-control" rows="2"
											style="resize: none" v-model="sentence"></textarea>
									</div>
								</div>

								<div class="row">
									<div class="col-md-12 column " style="margin-bottom: 5px">
										<button class="btn btn-defaut btn-block" type="button" v-on:click="sendMessage()"
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
							<div class="well" style="max-width: 650px; height: 60px; background-color: #ebebeb; margin-bottom: 0px">
								<div class="input-group input-daterange " data-provide="datepicker" style="max-width: 600px; margin: auto;">
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
								style="max-width: 650px; height: 580px; overflow-y: auto; overflow-x: hidden; background-color: #ebebeb; margin-top: 5px; padding-left: 20px; padding-right: 20px;">
								<template v-for="message in historyMessages">
								<div v-if="!message.fromAdmin">
									<div class='row'>
										<div class=' pull-right' style='margin-top: 10px;'>
											{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
									</div>
									<div class='row'>
										<div class='popover fade bottom in pull-right' role='tooltip'
											style='position: relative; display: block; max-width: 500px; word-wrap: break-word;; background-color: #B7DFF8; z-index: 0'>
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
								<div v-if="message.fromAdmin">
									<div class='row'>
										<div class=' pull-left' style='margin-top: 10px;'>
											{{message.senderName}}&nbsp;&nbsp;&nbsp;&nbsp;{{message.createDateTime}}</div>
									</div>
									<div class='row'>
										<div class='popover fade bottom in pull-left' role='tooltip'
											style='position: relative; display: block; max-width: 500px; word-wrap: break-word; background-color: #FFFFFF; z-index: 0'>
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
							<div id="pageCount"
								style="max-width: 650px; height: 60px; background-color: #ebebeb; margin-top: 0px;padding-top:10px; padding-left: 20px; padding-right: 20px;">
								<div class="text-center"
									style="background-color: #ECEDF1; max-width: 1050px; height: 40px; margin-top: 0px; padding-left: 20px; padding-right: 20px;">
									<ul class="pagination" style="margin-top: 2px" v-if="totalPage>0">
										<li v-bind:class="curPage===1?'disabled':''"><a href="javascript:void(0)" v-on:click="changePage(1)"><i
												class="fa fa-fast-backward"></i></a></li>
										<li v-bind:class="curPage===1?'disabled':''"><a href="javascript:void(0)"
											v-on:click="changePage(curPage-1)"><i class="fa fa-step-backward fa-large"></i></a></li>
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

	<button id="trigger" class="trigger pull-right" style="margin-top: 30px;">CHAT</button>


	<script src="${ctx}/static/js/myMediaRecorder.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/lib/bootstrap-datepicker.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/chatFunction.js"></script>
	<script type="text/javascript">
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />"
		ctx = "${ctx}";
		$('.datepicker').datepicker({
			startDate : '-300d',
			todayHighlight : true,
			clearBtn : true,
			orientation : "bottom right",
		});
		$('.input-daterange input').each(function() {
			$(this).datepicker('clearDates');
		});
	</script>
</body>
</html>