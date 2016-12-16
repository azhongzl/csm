(function(myMediaRecorder, $, undefined) {
    let log = console.log.bind(console);
    let recorder;
    let chunks;
    let mediaOptions = {
        video: {
            tag: 'video',
            type: 'video/webm',
            ext: '.mp4',
            constraints: {
                video: true,
                audio: false
            }
        },
        audio: {
            tag: 'audio',
            type: 'audio/ogg',
            ext: '.ogg',
            constraints: {
                audio: true
            }
        }
    };

    myMediaRecorder.initVideo = function(processStream, processBlob) {
        init(mediaOptions.video, processStream, processBlob);
    }

    myMediaRecorder.initAudio = function(processStream, processBlob) {
        init(mediaOptions.audio, processStream, processBlob);
    }

    myMediaRecorder.start = function() {
        chunks = [];
        recorder.start();
    }

    myMediaRecorder.stop = function() {
        recorder.stop();
    }

    init = function(media, processStream, processBlob) {
        navigator.mediaDevices.getUserMedia(media.constraints).then(stream => {
            processStream(stream);

            recorder = new MediaRecorder(stream);
            recorder.ondataavailable = e => {
                chunks.push(e.data);
            };
            recorder.onstop = e => {
                let blob = new Blob(chunks, {
                    type: media.type
                });
                processBlob(blob, media);
            };
            recorder.onerror = e => {
                log('Error: ' + e);
            };
            log('Get user media successfully');
        }).catch(e => {
            log('Error: ' + e);
        });
    }

}(window.myMediaRecorder = window.myMediaRecorder || {}, jQuery));