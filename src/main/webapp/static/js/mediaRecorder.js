'use strict'

let recorder,
    counter = 1,
    chunks,
    media,
    mediaOptions = {
        video: {
            tag: 'video',
            type: 'video/webm',
            ext: '.mp4',
            gUM: {
                video: true,
                audio: true
            }
        },
        audio: {
            tag: 'audio',
            type: 'audio/ogg',
            ext: '.ogg',
            gUM: {
                audio: true
            }
        }
    };

function init(isVideo, processData) {
    if (isVideo) {
        media = mediaOptions.video;
    } else {
        media = mediaOptions.audio;
    }

    navigator.mediaDevices.getUserMedia(media.gUM).then(_stream => {
        recorder = new MediaRecorder(_stream);
        recorder.ondataavailable = e => {
            chunks.push(e.data);
            if (recorder.state == 'inactive') {
                let blob = new Blob(chunks, {
                    type: media.type
                });
                let fileExt = `${media.ext}`;
                processData(blob, fileExt);
            }
        };
    }).catch(log);
}

function start() {
    chunks = [];
    recorder.start();
}

function stop() {
    recorder.stop();
}