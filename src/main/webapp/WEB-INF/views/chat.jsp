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
<link href="${ctx}/static/css/chat.css" rel="stylesheet">
<script src="${ctx}/static/js/lib/jquery.js"></script>
<script src="${ctx}/static/js/lib/bootstrap.js"></script>
<script src="${ctx}/static/js/lib/jquery.slidereveal.min.js"></script>
<script src="${ctx}/static/js/lib/sockjs.js"></script>
<script src="${ctx}/static/js/lib/stomp.js"></script>

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

	<div id="slider" class="slider">

		<div id="sentence" class="well"
			style="width: 600px; height: 600px; overflow-y: auto; overflow-x: hidden; background-color: #ebebeb; margin-top: 5px; margin-bottom: 0px">
		</div>
		<div class="panel panel-default" style="width: 600px; background-color: #dddcd9; margin-top: 0">

			<input class="btn btn-default" id="inputFile" type="file" name='uploadFile1' multiple='multiple'
				onchange='showUploadFile()' style="display: none"> </input>
			<div class="row">
				<div class="btn-group col-md-5  column">
					<button class="btn btn-default" type="button" onclick="listHistory()" data-toggle="tooltip" title="Display history">
						<i class="fa fa-history" aria-hidden="true"></i>
					</button>
					<button class="btn btn-default" type="button" onclick="document.getElementById('inputFile').click();"
						data-toggle="tooltip" title="Upload">
						<i class="fa fa-paperclip" aria-hidden="true"></i>
					</button>
					<button class="btn btn-default" type="button" onclick="video();" data-toggle="tooltip" title="Video">
						<i class="fa fa-video-camera" aria-hidden="true"></i>
					</button>
					<button class="btn btn-default" type="button" onclick="audio();" data-toggle="tooltip" title="Audio">
						<i class="fa fa-volume-up" aria-hidden="true"></i>
					</button>
				</div>

			</div>

			<div class="row">
				<div class="col-md-12 column ">
					<textarea placeholder="Please enter message here..." id="message" class="form-control"  rows="2" style="resize: none"  ></textarea>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12 column " style="margin-bottom: 5px">
					<button class="btn btn-defaut btn-block" type="button" onclick="sendMessage()" style="background-color: #c0c0c0">
						<i class="fa fa-share-square-o " aria-hidden="true">Send</i>
					</button>
				</div>
			</div>



			<!-- /input-group -->
		</div>

	</div>
	<div class="modal fade" id="myVideoModal" style="margin-top: 200px" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header " style="background-color: #337ab7; color: white">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">VIDEO</h4>
				</div>
				<div class="modal-body" style="text-align: center">
					<video id="myVideo" autoplay muted style="height: 280px;"></video>
				</div>
				<div class="modal-footer">
					<button type="button" id="videostart" class="btn btn-primary pull-left" onclick="videoStart()">Record</button>
					<button type="button" id="videocancel" class="btn btn-primary" onclick="videoCancel()">Cancel</button>
					<button type="button" id="videostop" class="btn btn-primary" onclick="videoStop()">Send</button>
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
					<h4 class="modal-title" id="myModalLabel">AUDIO</h4>
				</div>
				<div class="modal-body" style="text-align: center">
					<audio id="myAudio" controls> Your browser does not support the audio tag.
					</audio>
				</div>
				<div class="modal-footer">
					<button type="button" id="audiostart" class="btn btn-primary pull-left" onclick="audioStart()">Record</button>
					<button type="button" id="audiocancel" class="btn btn-primary" onclick="audioCancel()">Cancel</button>
					<button type="button" id="audiostop" class="btn btn-primary" onclick="audioStop()">Send</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
	</div>
	

	<button id="trigger" class="trigger pull-right"  style="margin-top: 30px; ">CHAT</button>

	<script type="text/javascript">
		userId = "<shiro:principal property="id" />";
		username = "<shiro:principal property="username" />"
		ctx = "${ctx}";
	</script>
	<script src="${ctx}/static/js/myMediaRecorder.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/chatFunction.js"></script>

</body>
</html>