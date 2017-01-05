function connect1() {
    var socket = new SockJS(ctx+'/ws');
    stompClient = Stomp.over(socket);
    var headers = {
        admin: true
    };
    stompClient.connect(headers, function(frame) {
        stompClient.subscribe('/app/chatAInit', function(message) {
            showCustomerSet(JSON.parse(message.body).data.customerList);
            $.each(JSON.parse(message.body).data.customerUserGroupList, function(i, n) {
                store.state.customerUserGroupList.push(n.customerUserId);
            });
            stompClient.subscribe('/topic/chat/addCustomerUserGroup',
                function(message) {
                    var data = JSON.parse(message.body);
                    store.commit('getService', {
                        id: data.data.customerId
                    });
                    if (data.data.userGroupId == store.state.currentUserGroup.userGroup.id) {
                        store.state.customerUserGroupList.push(data.data.customerId);
                        showUnhandledCustomer(data.data.customerId);
                        if (store.state.customerId == data.data.customerId) {
                            showMsg(data.data.customerId);
                        }
                    }

                });
            stompClient.subscribe('/topic/chat/removeCustomerUserGroup',
                function(message) {
                    var data = JSON.parse(message.body);
                    store.commit('getService', {
                        id: data.data.customerId
                    });
                    if (data.data.userGroupId == store.state.currentUserGroup.userGroup.id) {
                        var indexNo = store.state.customerUserGroupList.indexOf(data.data.customerId);
                        store.state.customerUserGroupList.splice(indexNo, 1);
                        removeUnhandledCustomer(data.data.customerId);

                    }
                });
        });
        stompClient.subscribe('/topic/chat/login', function(message) {
            showOnlineCustomer(JSON.parse(message.body).data);
        });
        stompClient.subscribe('/topic/chat/logout', function(message) {
            removeOnlineCustomer(JSON.parse(message.body).data);
        });
        stompClient.subscribe('/topic/chat/addUnhandledCustomer',
            function(message) {
                showUnhandledCustomer(JSON.parse(message.body).data.userId);
            });
        stompClient.subscribe('/topic/chat/removeUnhandledCustomer',
            function(message) {
                removeUnhandledCustomer(JSON.parse(message.body).data.userId);
            });
    });
}

function showCustomerSet(customerSet) {
    store.state.customers = [];
    for (var i = 0; i < customerSet.length; i++) {
        showCustomer(customerSet[i]);
    }
}

function showCustomer(customer) {
    var customerId = customer.userId;
    store.state.customers.push(customer);
    if (customer.online) {
        setTimeout(function() {
            showOnlineCustomer(customer);
        }, 200);
    }
    if (customer.unhandled) {
        setTimeout(function() {
            showUnhandledCustomer(customerId);
        }, 200);
    }
}

function showOnlineCustomer(customer) {
    $("#" + customer.userId).css('color', 'red');
}

function removeOnlineCustomer(customer) {
    $("#" + customer.userId).css('color', '#838383');
}

var intervalMap = new Map();

function showUnhandledCustomer(customerId) {
    var userId = customerId;
    var canSendMsg = false;
    if (store.state.currentUserGroup.userGroup.chat) {
        canSendMsg = true;
    } else {
        if (store.state.customerUserGroupList.indexOf(userId) != -1) {
            canSendMsg = true;
        }
    }
    if (!canSendMsg) {
        return;
    }
    if (!intervalMap.has(userId)) {
        var interval = setInterval(function() {
            $("#" + userId).fadeOut(100).fadeIn(100);
        }, 200);
        intervalMap.set(userId, interval);
    }
}

function removeUnhandledCustomer(customerId) {
    var roomId = customerId;
    if (intervalMap.has(roomId)) {
        clearInterval(intervalMap.get(roomId));
        intervalMap.delete(roomId);
    }
}

function showMsg(customerId) {
    if (!stompClient.connected) {
        setTimeout(function() {
            showMsg(customerId);
        }, 200);
        return;
    }

    $("#sentence").empty();
    var canSendMsg = false;
    if (store.state.currentUserGroup.chatOrSuper) {
        canSendMsg = true;
    } else {
        if (store.state.customerUserGroupList.indexOf(customerId) != -1) {
            canSendMsg = true;
        }
    }
    if (canSendMsg) {
        for (var i = 0; i < subscribeList.length; i++) {
            subscribeList[i].unsubscribe();
        }
        subscribeList = [];
        var subApp = stompClient.subscribe('/app/chatAInitMessage/' +
            customerId,
            function(message) {
                showMessages(JSON.parse(message.body).data.messageList);

            });
        var subTopic = stompClient.subscribe('/topic/chat/message/' +
            customerId,
            function(message) {
                showMessage(JSON.parse(message.body));
                document.getElementById('sentence').scrollTop = document.getElementById('sentence').scrollHeight;
            });
        subscribeList.push(subApp);
        subscribeList.push(subTopic);
    }
}

function showMessages(messageList) {
    for (var i = 0; i < messageList.length; i++) {
        showMessage(messageList[i]);
    }
    document.getElementById('sentence').scrollTop = document.getElementById('sentence').scrollHeight;
}

function showMessage(message) {
    var canSendMsg = false;
    if (store.state.currentUserGroup.chatOrSuper) {
        canSendMsg = true;
    } else {
        if (store.state.customerUserGroupList.indexOf(store.state.customerId) != -1) {
            canSendMsg = true;
        }
    }
    if (!canSendMsg) {
        return;
    }
    var timeStr = "";
    var num = message.createDateTime.indexOf("T");
    timeStr = message.createDateTime.substring(0, num) + " " + message.createDateTime.substring(num + 1, 19);
    if (message.attachments == undefined) {
        if (message.fromAdmin) {
            $("#sentence").append("<div class='panel panel-primary' style='clear:both;float:right;width:500px'><div class='panel-heading' style='padding: 2px 0px 2px 300px' >" + message.senderName + "&nbsp;&nbsp;&nbsp;&nbsp;" + timeStr + "</div><div class='panel-body'>" + message.message + " </div></div>");
        } else {
            $("#sentence").append("<div class='panel panel-info' style='clear:both;float:left;width:500px'><div class='panel-heading' style='padding: 2px 0px ' >" + message.senderName + "&nbsp;&nbsp;&nbsp;&nbsp;" + timeStr + "</div><div class='panel-body'>" + message.message + " </div></div>");
        }
    } else {
        let attachment = message.attachments.split(",");
        let files = "";
        if (message.message){
        	files +="<p>"+message.message+"</p>";
        }
        $.each(attachment, function(i, n) {
				if(n.indexOf("mp4")!=-1){
				files+="<video src="+ctx+"/uploads/CsmChatMessage/"+message.id+"/"+n+" controls  style='height:280px;width:350px'>"+n+"</video>";	
				}
	            if (n.indexOf("ogg")!=-1) {
	                files += "<audio src=" + ctx + "/uploads/CsmChatMessage/" + message.id + "/" + n + " controls >" + n + "</audio>";
	            } 
	            if ((n.indexOf("mp4")===-1)&&(n.indexOf("ogg")===-1)){
	                files +="<p><a href="+ctx+"/uploads/CsmChatMessage/"+message.id+"/"+n+">"+n+"</a></p>";
	            }
        });
        
        if (message.fromAdmin) {
            $("#sentence").append("<div class='panel panel-primary' style='clear:both;float:right;width:500px'><div class='panel-heading' style='padding: 2px 0px 2px 300px' >" + message.senderName + "&nbsp;&nbsp;&nbsp;&nbsp;" + timeStr + "</div><div class='panel-body'>" + files + " </div></div>");
        } else {
            $("#sentence").append("<div class='panel panel-info' style='clear:both;float:left;width:500px'><div class='panel-heading' style='padding: 2px 0px ' >" + message.senderName + "&nbsp;&nbsp;&nbsp;&nbsp;" + timeStr + "</div><div class='panel-body'>" + files + " </div></div>");
        }
    }
}

function ajaxcreate(savedata, url1) {
    $.ajax({
        type: "POST",
        url: url1,
        data: savedata,
        cache: false,
        async: false,
        success: function(result) {
//            myAlert("ADD NEW SUCCESS");
        },
        timeout: 3000,
        error: handleError

    });
}

function ajaxFind(checkKey, url1) {
    var checkList = [];
    $.ajax({
        type: "GET",
        url: url1,
        data: checkKey,
        cache: false,
        async: false,
        success: function(result) {
            checkList = result;
        },
        timeout: 3000,
        error: handleError,
    });

    return (checkList);
}



function ajaxPut(putdata, url1) {
    $.ajax({
        type: "POST",
        url: url1,
        data: putdata,
        cache: false,
        async: false,
        success: function(result) {
//            myAlert("PUT OK ");
        },
        timeout: 3000,
        error: handleError,
    });
}

function ajaxGet(url1) {
    var checkList;
    $.ajax({
        type: "GET",
        url: url1,
        async: false,
        cache: false,
        success: function(result) {
            if (result.data == undefined) {
//                myAlert("No Result");
            } else {
                checkList = result.data;
            }
        },
        timeout: 3000,
        error: handleError,
    });
    return checkList;
}

function handleError(xhr) {
    if (xhr.status == 499) {
        window.location.replace("${ctx}" + xhr.statusText);
    } else {
        myAlert("error： " + xhr.status + " " + xhr.statusText);
    }
}


function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 +
        strDate + "T" + checkTime(date.getHours()) + seperator2 +
        checkTime(date.getMinutes()) + seperator2 +
        checkTime(date.getSeconds());
    return currentdate;
};

function checkTime(i) {
    if (i < 10) {
        i = "0" + i
    }
    return i
}

function ajaxcreateUpload(savedata, url1) {
    $.ajax({
        type: "POST",
        url: url1,
        data: savedata,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function(result) {
//            myAlert("ADD NEW SUCCESS");
        },
        timeout: 3000,
        error: handleError,

    });
}

function ajaxPutUpload(putdata, url1) {
    $.ajax({
        type: "POST",
        url: url1,
        data: putdata,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function(result) {
//            myAlert("PUT OK ");
        },
        timeout: 3000,
        error: handleError,
    });
}

function myAlert(msg) {
    vm.message = msg;
    vm.showMsg();
}