'use strict'

let log = console.log.bind(console),
  id = val => document.getElementById(val),
  mediaVideo = id('mediaVideo'),
  mediaAudio = id('mediaAudio'),
  start = id('start'),
  stop = id('stop'),
  recorder,
  counter=1,
  chunks,
  media,
  mediaOptions = {
	        video: {
	          tag: 'video',
	          type: 'video/webm',
	          ext: '.mp4',
	          gUM: {video: true, audio: true}
	        },
	        audio: {
	          tag: 'audio',
	          type: 'audio/ogg',
	          ext: '.ogg',
	          gUM: {audio: true}
	        }
	      };
  
  if(mediaVideo.checked){
	  media = mediaOptions.video;
  }else{
	  media = mediaOptions.audio;
  }
  init();

mediaVideo.onchange = e => {
	media = mediaOptions.video;
	init();
}

mediaAudio.onclick = e => {
	media = mediaOptions.audio;
	init();
} 
  
function init() {
  navigator.mediaDevices.getUserMedia(media.gUM).then(_stream => {
    id('btns').style.display = 'inherit';
    start.removeAttribute('disabled');
    stop.disabled = true;
    chunks=[];
    recorder = new MediaRecorder(_stream);
    recorder.ondataavailable = e => {
      chunks.push(e.data);
      if(recorder.state == 'inactive')  uploadAudio();
    };
    log('got media successfully');
  }).catch(log);
}

start.onclick = e => {
  start.disabled = true;
  chunks=[];
  recorder.start();
  stop.removeAttribute('disabled');
}

stop.onclick = e => {
  stop.disabled = true;
  recorder.stop();
  start.removeAttribute('disabled');
}

function upload(url, fd, callback) {
	let blob = new Blob(chunks, {type: media.type });
    fd.append("uploadFile", blob, `${counter++}${media.ext}`);
    var xhr = new XMLHttpRequest();
    if (callback) {
        xhr.upload.addEventListener("progress", function (e) {
            callback('uploading', e);
        }, false);
        xhr.addEventListener("load", function (e) {
            callback('ok', e);
        }, false);
        xhr.addEventListener("error", function (e) {
            callback('error', e);
        }, false);
        xhr.addEventListener("abort", function (e) {
            callback('cancel', e);
        }, false);
    }
    xhr.open("POST", url);
    xhr.send(fd);
}

function uploadAudio() {
	var fd = new FormData();
    fd.append("roomId", "5ab5e407-9a6a-4e3b-90db-2aa3dc267012");
    upload("/csm/admin/chat/upload", fd, function (state, e) {
        switch (state) {
            case 'uploading':
                // var percentComplete = Math.round(e.loaded * 100 / e.total) +
				// '%';
                break;
            case 'ok':
                // alert(e.target.responseText);
                alert("Upload successfully");
                break;
            case 'error':
                alert("Upload failed");
                break;
            case 'cancel':
                alert("Upload cancelled");
                break;
        }
    });
}