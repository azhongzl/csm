var subscribeList = [];
var stompClient = null;
var plainPassword = "kuzco123";
var root = "root";
const
    chat = {
        template: '#chatMainPage',
        data:function(){
        	return {
        		customerName:"",
            }
        	},
        created : function(){
            store.commit('getCustomerName', {
                id: "",
                name: ""
            });
        },
        computed : {
        	customerName: function(){
        		return this.$store.state.customerName
        	}
        },

    };

const
    customer = {
        template: '#customer',
        watch: {
            '$route': function(to, from) {
if(from.query.name){
	$("#"+from.query.name).css("background-color","white");
}
$("#"+this.$route.query.name).css("background-color","#B7DFF8");
            	if (to.path=="/admin/chat"){
                    store.commit('getCustomerName', {
                        id: "",
                        name: "",
                    });
            	}
            }
        },
        computed: {
            customers: function() {
                return this.$store.state.customers;
            }
        },
        
        created: function() {
            connect1();
            store.commit("getCurrentUserGroup");
        }
    };

const
    service = {
        template: '#service',
        data: function() {
            return {
                selected: '',
            }
        },
        created: function() {
            store.commit('getService', {
                id: this.$route.query.id
            });
        },
        computed: {
            service: function() {
                return this.$store.state.service;
            },
            serviceList: function() {
                return this.$store.state.serviceList;
            }
        },
        methods: {
            removeGroup: function(id, index) {
                var canSendMsg = false;
                if (store.state.currentUserGroup.chatOrSuper) {
                    canSendMsg = true;
                } else {
                    if (store.state.customerUserGroupList.indexOf(this.$route.query.id) != -1) {
                        canSendMsg = true;
                    }
                }
                if (!canSendMsg) {
                    return;
                }
                let url1 = ctx + "/admin/chat/deleteCustomerUserGroup/" + id;
                $.ajax({
                    type: "GET",
                    url: url1,
                    async: false,
                    success: function(result) {
 // myAlert("DELETE OK ");
                    },
                    timeout: 3000,
                    error: handleError
                });
                // store.commit('getService',{id:this.$route.query.id});
            },

            selectFunction: function(service) {
                if (service == undefined) {
                    return;
                }
                var serviceId = service.id;
                let id = this.$route.query.id;
                var canSendMsg = false;
                if (store.state.currentUserGroup.chatOrSuper) {
                    canSendMsg = true;
                } else {
                    if (store.state.customerUserGroupList.indexOf(id) != -1) {
                        canSendMsg = true;
                    }
                }
                if (!canSendMsg) {
                    return;
                }
                var url1 = ctx + "/admin/chat/postCustomerUserGroup";

                var savedata = {
                    customerUserId: id,
                    userGroupId: serviceId,
                    operatorUserId: userId,
                };

                ajaxcreate(savedata, url1);
                // store.commit('getService',{id:id});

            },

        }
    };

const content = {
    template: '#content',
    data: function() {
        return {
            uploadKey: true,
            sentence: "",
            modalTitle: "",
            videostart: true,
            videostop: true,
            messages:[],
            historyMessages:[],
            curPage:1,
            totalPage: 0,
            lastNum:5,
            column:5
        };
    },
    watch: {
        '$route': function(to, from) {

            store.commit('getService', {
                id: this.$route.query.id
            });
            store.commit('getCustomerName', {
                id: this.$route.query.id,
                name: this.$route.query.name
            });
            let customerId = this.$route.query.id;
            showMsg(customerId);
            this.historyMessages=[];
            this.totalPage=0;
            this.curPage=1;
            this.lastNum=5;
            this.column=5;
        	$("#datepicker001").val("");
        	$("#datepicker002").val("");
        	$('.datepicker').datepicker({
        	    startDate: '-300d',
        	    todayHighlight:true,
        	    clearBtn:true,
        	    orientation:"bottom right",
        	});
        	$('.input-daterange input').each(function() {
        	    $(this).datepicker('clearDates');
        	});
        }
    },
    mounted: function() {
    	$('.datepicker').datepicker({
    	    startDate: '-300d',
    	    todayHighlight:true,
    	    clearBtn:true,
    	    orientation:"bottom right",
    	});
    	$('.input-daterange input').each(function() {
    	    $(this).datepicker('clearDates');
    	});
        let customerId = this.$route.query.id;
        let username = this.$route.query.name
        store.commit('getCustomerName', {
            id: customerId,
            name: username,
        });
        showMsg(customerId);
        setTimeout(function() {
        	$("#"+username).css("background-color","#B7DFF8");
       }, 500);
    },
    computed: {
        messages: function() {
            setTimeout(function() {
            	 document.getElementById('sentence').scrollTop = document.getElementById('sentence').scrollHeight;
            }, 100);
            return this.$store.state.messages;
        },
    },

    methods: {
        send: function() {
            if (this.sentence.trim().length > 0) {
                let customerId = this.$route.query.id;
                stompClient.send('/app/chatASendMessage', {}, JSON.stringify({
                    'message': this.sentence,
                    'roomId': customerId,
                }));
                this.sentence = "";
            }
        },

        listHistory: function() {
        	let beginDate = $("#datepicker001").val();
        	let endDate = $("#datepicker002").val();
        	if((beginDate == endDate )||(beginDate == "")||(endDate == "")){
        		myAlert("Please enter correct date ...");
        		return false;
        	}
        	beginDate = beginDate+"T00:00:00";
        	endDate = endDate+"T23:59:59.999999999"
            let customerId = this.$route.query.id;
            let url = ctx + "/admin/chat/listHistory/" + customerId;
            let checkKey = {
            		beginDateTime:beginDate,
            		endDateTime:endDate,
            		page_no:this.curPage,
            };
            let result = ajaxFind(checkKey, url);
            if (!result.data.historyPage.content){
            	myAlert("No result ...");
            	return false;
            }
            let messageList = result.data.historyPage.content;
               this.totalPage = result.data.historyPage.totalPages;
            if (this.totalPage<=5){
            	this.column=this.totalPage;
            }else{
            	this.column=5;
            }
            for (var i = 0; i < messageList.length; i++) {
                let timeStr = "";
                let num = messageList[i].createDateTime.indexOf("T");
                timeStr = messageList[i].createDateTime.substring(0, num) + " " + messageList[i].createDateTime.substring(num + 1, 19);
                messageList[i].createDateTime=timeStr;
                if (messageList[i].attachments != undefined){
                	 let attachment = messageList[i].attachments.split(",");
                	 if(messageList[i].message){
                	 let mess = html_encode(messageList[i].message);
                	 messageList[i].message = mess;
                	 }
                	 messageList[i].attachments = attachment;
                }else{
                	let mess = html_encode(messageList[i].message);
                	messageList[i].message = mess;
                }
            }
            this.historyMessages=messageList.reverse();
            setTimeout(function() {
           	 document.getElementById('historySentence').scrollTop = document.getElementById('historySentence').scrollHeight;
           }, 100);
        },
        
        changePage : function(n){
        	if ((n==0)||(n>this.totalPage)){
        		return false;
        	}
        	if (this.curPage==n){
        		return false;
        	}
        	this.curPage=n;
        	if (n<=5){
        		this.lastNum=5;
        	}else{
        		if ((n+2)>this.totalPage){
        			this.lastNum=this.totalPage;
        		}else{
        			this.lastNum=n+2;
        		}
        	}
        	this.listHistory();
        },
        showUploadFile: function() {
            var fileData = $("input[name='uploadFile1']").get(0);
            url1 = ctx + "/admin/chat/upload";
            var form_data = new FormData();
            form_data.append("roomId", this.$route.query.id);
            for (var i = 0; i < (fileData.files.length); i++) {
                form_data.append("uploadFile", fileData.files[i]);
            }
            if (this.sentence.trim().length>0){
            	form_data.append("message", this.sentence);
                this.sentence="";
            }
            ajaxcreateUpload(form_data, url1);

        },
        video: function() {
            this.modalTitle = "VIDEO"
            $("#myVideoModal").modal("show");
            myMediaRecorder.initVideo(this.processStream, this.processBlob, this.processError);
            this.videostart = false;
            this.videostop = true;

        },
        videoStart: function() {
            this.videostart = true;
            myMediaRecorder.start();
            this.videostop = false;

        },
        videoCancel: function() {
            myMediaRecorder.cancel();
            $("#myVideoModal").modal("hide");
        },
        videoStop: function() {
            myMediaRecorder.stop();
            $("#myVideoModal").modal("hide");
        },
        audio: function() {
            this.modalTitle = "AUDIO"
            $("#myAudioModal").modal("show");
            myMediaRecorder.initAudio(this.processStream1, this.processBlob, this.processError);
            this.videostart = false;
            this.videostop = true;

        },
        audioStart: function() {
            this.videostart = true;
            myMediaRecorder.start();
            this.videostop = false;

        },
        audioStop: function() {
            myMediaRecorder.stop();
            $("#myAudioModal").modal("hide");
        },
        audioCancel: function() {
            myMediaRecorder.cancel();
            $("#myAudioModal").modal("hide");
        },
        processStream: function(stream) {
            var video = document.getElementById('myVideo');
            video.srcObject = stream;
            video.onloadedmetadata = function(e) {
                video.play();
            };
        },
        processStream1: function(stream) {
            var video = document.getElementById('myAudio');
            video.srcObject = stream;
            video.onloadedmetadata = function(e) {
                video.play();
            };
        },
        processBlob: function(blob, media) {
            var url = ctx + "/admin/chat/upload";
            var fd = new FormData();
            fd.append("roomId", this.$route.query.id);
            fd.append("uploadFile", blob, "media" + media.ext);
            if (this.sentence.trim().length>0){
                fd.append("message", this.sentence);
                this.sentence="";
            }
            var xhr = new XMLHttpRequest();
            xhr.addEventListener("load", function(e) {
// myAlert("Upload successfully");
            }, false);
            xhr.addEventListener("error", function(e) {
// myAlert("Upload failed");
            }, false);
            xhr.addEventListener("abort", function(e) {
// myAlert("Upload cancelled");
            }, false);
            xhr.open("POST", url);
            xhr.send(fd);
        },
        processError: function(e) {
        	let text="";
        	if(e.name=="NotAllowedError"){
        		text="Camera Not Allowed";
        		}else{
        			if(e.name=="NotFoundError"){
                		text="Camera Not Found";
            		}else{
            			text=e.name+":"+e.message;
            		}
        		}
        	myAlert(text);
            $("#myVideoModal").modal("hide");
            $("#myAudioModal").modal("hide");
        }
    }
};


const permission = {
    template: '#permission',
    data: function() {
        return {
            permissions: [],
            permissionName: "",
            permissionValue: "",
            permissionId: "",
            modalTitle: ""
        }
    },

    created: function() {
        this.getPermission();
    },

    methods: {
        addNew: function() {
            this.permissionName = "";
            this.permissionValue = "";
            this.modalTitle = "ADD NEW PERMISSION";
            $("#myPermissionModal").modal('show');

        },
        addNewSubmit: function() {
            if (this.permissionId.trim().length == 0) {
                if (this.permissionName.trim().length > 0) {
                    var url1 = ctx + "/admin/permission/post"
                    var savedata = {
                        name: this.permissionName,
                        permission: this.permissionValue,
                    };
                    ajaxcreate(savedata, url1);
                    this.getPermission();
                    this.permissionName = "";
                    this.permissionValue = "";
                    $("#myPermissionModal").modal('hide');
                } else {
                    myAlert("Please enter permission name ");
                };
            } else {
                if (this.permissionName.trim().length > 0) {
                    var url1 = ctx + "/admin/permission/put";
                    var putdata = {
                        id: this.permissionId,
                        name: this.permissionName,
                        permission: this.permissionValue,
                    }
                    ajaxPut(putdata, url1);
                    this.getPermission();
                    this.permissionName = "";
                    this.permissionValue = "";
                    this.permissionId = "";
                    $("#myPermissionModal").modal('hide');
                } else {
                    myAlert("Please enter permission name ");
                }

            }
        },
        del: function(id) {


            var conf = confirm("are you sure?");
            if (conf) {
                let url1 = ctx + "/admin/permission/delete/" + id;
                $.ajax({
                    type: "GET",
                    url: url1,
                    async: false,
                    success: function(result) {
                        if (result.code === 0) {
                            myAlert("DELETE OK ");
                        }
                        if (!result.success) {
                            myAlert(result.message + " ,you can't delete it ");
                        }

                    },
                    timeout: 3000,
                    error: handleError
                });
                this.getPermission();
            }
        },
        mod: function(id, name, per) {
            this.permissionName = name;
            this.permissionValue = per;
            this.permissionId = id;
            this.modalTitle = "MODIFY PERMISSION";
            $("#myPermissionModal").modal('show');

        },
        getPermission: function() {
            let url = ctx + "/admin/permission/listForm";
            let checkKey = {};
            let result = ajaxFind(checkKey, url);
            if (result.length == 0) {
                return false;
            }
            if (result.data.permissionList != undefined) {
                this.permissions = result.data.permissionList;
            }
        },
    }
};


const role = {
    template: '#role',
    data: function() {
        return {
            modalTitle: "",
            name: "",
            id: "",
            roles: [],

        }
    },
    created: function() {
        this.getRoles();

    },
    methods: {
        addNew: function() {
            this.modalTitle = "ADD NEW ROLE";
            this.name = "";
            $("#roleModal").modal("show");

        },
        addNewSubmit: function() {
            if (this.name.trim().length > 0 && this.id.length == 0) {
                var url1 = ctx + "/admin/role/post"
                var savedata = {
                    name: this.name
                };
                ajaxcreate(savedata, url1);
                this.getRoles();
            }
            if (this.name.trim().length > 0 && this.id.length > 0) {
                var url1 = ctx + "/admin/role/put/";
                var putdata = {
                    name: this.name,
                    id: this.id,
                }
                ajaxPut(putdata, url1);
                this.getRoles();
            }

            $("#roleModal").modal('hide');
        },
        del: function(id) {
            var conf = confirm("are you sure?");
            if (conf) {
                url1 = ctx + "/admin/role/delete/" + id;
                $.ajax({
                    type: "GET",
                    url: url1,
                    async: false,
                    success: function(result) {
                        if (result.code === 0) {
                            myAlert("DELETE OK ");
                        }
                        if (!result.success) {
                            myAlert(result.message + " ,you can't delete it ");
                        }
                    },
                    timeout: 3000,
                    error: handleError
                });
                this.getRoles();
            }
        },
        mod: function(id) {
            let url1 = ctx + "/admin/role/putForm/" + id;
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.role != undefined) {
                this.id = id;
                this.name = result.data.role.name;
            }
            this.modalTitle = "MODIFY ROLE";
            $("#roleModal").modal('show');
        },
        getRoles: function() {
            let url1 = ctx + "/admin/role/listForm";
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.roleList != undefined) {
                this.roles = result.data.roleList;
            }
        },
    }
};

const role_permission = {
    template: "#role_permission",
    data: function() {
        return {
            rolePermissions: [],
            permissions: [],
        }
    },
    created: function() {
        this.getPermissions(this.$route.query.id);
    },

    methods: {
        add: function(id) {
            var each = true;
            $.each(this.rolePermissions, function(i, n) {
                if (n.permission.id == id) {
                    myAlert("you already have this permission");
                    each = false;
                    return false;
                }
            });
            if (each) {
                var url1 = ctx + "/admin/role/postRolePermission";
                var savedata = {
                    roleId: this.$route.query.id,
                    permissionId: id,
                };
                ajaxcreate(savedata, url1);
                this.getPermissions(this.$route.query.id);
            };

        },
        remove: function(id) {
            url1 = ctx + "/admin/role/deleteRolePermission/" + id;
            $.ajax({
                type: "GET",
                url: url1,
                async: false,
                success: function(result) {
                    myAlert("DELETE OK ");
                },
                timeout: 3000,
                error: handleError
            });
            this.getPermissions(this.$route.query.id);
        },

        getPermissions: function(id) {
            let url = ctx + "/admin/role/listRolePermissionForm/" + id;
            let checkKey = {};
            let result = ajaxFind(checkKey, url);
            if (result.length == 0) {
                return false;
            }
            this.rolePermissions = result.data.rolePermissionList;
            this.permissions = result.data.roleList;
        },

        back: function() {
            router.push({
                path: '/role'
            });
        }
    }
};

const group = {
    template: '#group',
    data: function() {
        return {

            groups: "",
            group: "",
            groupId: "",
            groupText: "",
            sele: "",
            groupOptions: [],
            checked1: false,
            checked2: false,
            modalTitle: "",
        }
    },
    created: function() {
        this.getGroups();
    },

    methods: {

        addNew: function() {
            this.getGroupsOptions();
            this.groupText = "";
            this.checked1 = false;
            this.checked2 = false;
            this.sele = "";
            this.modalTitle = "ADD NEW GROUP";
            $("#myModal").modal('show');
        },


        addNewSubmit: function() {
            if (this.groupId.length == 0) {
                if (this.groupText.length == 0) {
                    myAlert('Please enter group name');
                } else {
                    var url1 = ctx + "/admin/userGroup/post";
                    var savedata = {
                        name: this.groupText,
                        admin: this.checked1,
                        chat: this.checked2,
                        superId: this.sele,
                    };

                    ajaxcreate(savedata, url1);
                    this.groupText = "";
                    this.checked1 = false;
                    this.checked2 = false;
                    this.sele = "";
                    this.groupOptions = [];
                    $("#myModal").modal('hide')

                }
            }
            if (this.groupId.length > 0) {
                var url1 = ctx + "/admin/userGroup/put";
                var putdata = {
                    id: this.groupId,
                    name: this.groupText,
                    admin: this.checked1,
                    chat: this.checked2,
                    superId: this.sele,
                }
                ajaxPut(putdata, url1);
                $("#myModal").modal('hide')
                this.groupText = "";
                this.checked1 = false;
                this.checked2 = false;
                this.sele = "";
                this.groupOptions = [];
            }
            this.getGroups();
        },

        del: function(id) {

            var conf = confirm("are you sure?");
            if (conf) {
                url1 = ctx + "/admin/userGroup/delete/" + id;
                $.ajax({
                    type: "GET",
                    url: url1,
                    async: false,
                    success: function(result) {
                        if (result.code === 0) {
                            myAlert("DELETE OK ");
                        }
                        if (!result.success) {
                            myAlert(result.message + " ,you can't delete it ");
                        }
                    },
                    timeout: 3000,
                    error: handleError
                });
                this.getGroups();
            }
        },
        mod: function(id) {
            this.getGroup(id);
            this.groupText = this.group.name;
            this.checked1 = this.group.admin;
            this.checked2 = this.group.chat;
            this.sele = this.group.superId;
            this.groupId = id;
            this.modalTitle = "MODIFY GROUP";
            $("#myModal").modal('show');
        },
        getGroups: function() {
            let url1 = ctx + "/admin/userGroup/listForm";
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.userGroupList != undefined) {
                this.groups = result.data.userGroupList;
            }
        },
        getGroup: function(id) {
            let url1 = ctx + "/admin/userGroup/putForm/" + id;
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.userGroup != undefined) {
                this.group = result.data.userGroup;
                this.groupOptions = result.data.superUserGroupList;
            }
        },
        getGroupsOptions: function() {
            let url1 = ctx + "/admin/userGroup/postForm";
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.superUserGroupList != undefined) {
                this.groupOptions = result.data.superUserGroupList;
            }
        },
    }
};

const group_role = {
    template: "#group_role",
    data: function() {
        return {
            roles: [],
            group_roles: [],
        }
    },
    created: function() {
        this.getRoles(this.$route.query.id);
    },

    methods: {
        add: function(id) {
            var each = true;
            $.each(this.group_roles, function(i, n) {
                if (n.roleId == id) {
                    myAlert("you already have this role");
                    each = false;
                    return false;
                }
            });
            if (each) {
                var url1 = ctx + "/admin/userGroup/postUserGroupRole";
                var savedata = {
                    userGroupId: this.$route.query.id,
                    roleId: id,
                };
                ajaxcreate(savedata, url1);
                this.getRoles(this.$route.query.id);
            };

        },
        remove: function(id) {
            url1 = ctx + "/admin/userGroup/deleteUserGroupRole/" + id;
            $.ajax({
                type: "GET",
                url: url1,
                async: false,
                success: function(result) {
                    myAlert("DELETE OK ");
                },
                timeout: 3000,
                error: handleError
            });
            this.getRoles(this.$route.query.id);
        },
        getRoles: function(id) {
            let url = ctx + "/admin/userGroup/listUserGroupRoleForm/" + id;
            let checkKey = {};
            let result = ajaxFind(checkKey, url);
            if (result.length == 0) {
                return false;
            }
            this.group_roles = result.data.userGroupRoleList;
            this.roles = result.data.roleList;
        },
        back: function() {
            router.push({
                path: '/group'
            });
        }
    }
};

const user = {
    template: '#user',
    data: function() {
        return {
            users: [],
            user: [],
            showKey: false,
            username: "",
            userId: "",
            sele: "",
            groupOptions: [],
            checked: false,
            modalTitle: "",
            pageSize: 10,
            curPage: 1,
            total: 0,
            totalPage: 0,
            pageShowKey: false,

        }
    },
    created: function() {
        this.getUsers();
    },

    methods: {
        addNew: function() {
            this.getGroups();
            this.showKey = false;
            this.username = "";
            this.checked = false;
            this.sele = "";
            this.userId = "";
            this.modalTitle = "ADD NEW USER";
            $("#myModal1").modal('show');
        },

        addNewSubmit: function() {
            if (this.userId.length == 0) {
                if (this.username.length == 0 || this.sele.length == 0) {
                    myAlert('Please enter name and select group');
                } else {
                    var url1 = ctx + "/admin/user/post";
                    var savedata = {
                        username: this.username,
                        active: true,
                        userGroupId: this.sele,
                        plainPassword: plainPassword,
                    };

                    ajaxcreate(savedata, url1);
                    this.username = "";
                    this.sele = "";
                    this.groupOptions = [];
                    $("#myModal1").modal('hide');
                    this.getUsers();
                }
            }
            if (this.userId.length > 0) {
                var url1 = ctx + "/admin/user/put";
                var putdata = {
                    active: this.checked,
                    userGroupId: this.sele,
                    id: this.userId
                }
                ajaxPut(putdata, url1);
                $("#myModal").modal('hide')
                this.username = "";
                this.checked = false;
                this.sele = "";
                this.groupOptions = [];
                $("#myModal1").modal('hide')
                this.getUsers();
            }
        },

        del: function(id) {
            var conf = confirm("are you sure?");
            if (conf) {
                url1 = ctx + "/admin/user/delete/" + id;
                $.ajax({
                    type: "GET",
                    url: url1,
                    async: false,
                    success: function(result) {
                        myAlert("DELETE OK ");
                    },
                    timeout: 3000,
                    error: handleError
                });
                this.getUsers();
            }

        },
        mod: function(id) {
            this.getUser(id)
            this.username = this.user.username;
            this.checked = this.user.active;
            this.sele = this.user.userGroupId;
            this.userId = id;
            this.showKey = true;
            this.modalTitle = "MODIFY USER";
            $("#myModal1").modal('show');
        },
        getUsers: function() {
            let url1 = ctx + "/admin/user/listForm";
            let checkKey = {
                page_no: this.curPage,
                page_size: this.pageSize

            };
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.userList.content != undefined) {
                this.users = result.data.userList.content;
                this.totalPage = result.data.userList.totalPages;
                if (this.totalPage > 0) {
                    this.pageShowKey = true;
                }
            }
        },

        getUser: function(id) {
            let url1 = ctx + "/admin/user/putForm/" + id;
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            this.user = result.data.user;
            this.groupOptions = result.data.userGroupList;
        },
        getGroups: function() {
            let url1 = ctx + "/admin/user/postForm";
            let checkKey = {};
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.userGroupList != undefined) {
                this.groupOptions = result.data.userGroupList;
            }
        },
        changePage: function(page) {
            if (this.curPage != page) {
                if (page > this.totalPage) {
                    this.curPage = this.totalPage;
                } else if (page <= 0) {
                    this.curPage = 1;
                } else {
                    this.curPage = page;
                }
                this.getUsers();
            }
        },
    }
};

const
    faqMain = {
        template: '#faqMain',
    };

const faq = {
    template: '#FaqCategory',
    data: function() {
        return {
            categoryList: [],
        }
    },
    created: function() {
        this.getCategory();
    },
    methods: {
        getCategory: function() {
            let url1 = ctx + "/admin/faqCategory/listForm";
            let checkKey = "";
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.faqCategoryList != undefined) {
                this.categoryList = result.data.faqCategoryList;
            }
        },
    },
};

const showFaqList = {
    template: '#FaqDetail',
    data: function() {
        return {
            cssId: "",
            faqList: [],
        }
    },
    watch: {
        '$route': function(to, from) {
            var id = this.$route.query.id;
            $("#" + this.cssId).css("color", "#337ab7");
            $("#" + id).css("color", "red");
            this.cssId = id;
            this.$store.state.curPage = 1;
            this.getFaq(id);
        }
    },

    created: function() {
        var id = this.$route.query.id;
        $("#" + id).css("color", "red");
        this.cssId = id;
        this.getFaq(id);
    },

    methods: {
        faqAddNewUi: function() {
            router.push({
                path: '/faq/faqAddNew',
                query: {
                    id: this.$route.query.id
                }
            })
        },
        getFaq: function(id) {
            let url1 = ctx + "/admin/faq/listForm/" + id;
            let checkKey = {
                page_size: this.$store.state.pageSize,
                page_no: this.$store.state.curPage,
            };
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.faqList.content !== undefined) {
                this.faqList = result.data.faqList.content;
                this.$store.state.total = result.data.faqList.totalElements;
                this.$store.state.totalPage = result.data.faqList.totalPages;
            } else {
                this.faqList = [];
                this.$store.state.total = 0;
                this.$store.state.totalPage = 0;
            }
        },
    },
};

const faqAddNew = {
    template: '#FaqAddNewUi',
    data: function() {
        return {
            question: "",
            answer: "",
            attachements: "",
        }
    },
    methods: {
        faqAddNew: function() {
            let id = this.$route.query.id;
            if ($.trim(this.question).length == 0) {
                myAlert("Please input question");
            } else {
                var fileData = $("input[name='uploadFile']").get(0);
                url1 = ctx + "/admin/faq/post";
                var form_data = new FormData();
                form_data.append("categoryId", id);
                form_data.append("question", this.question);
                form_data.append("active", true);
                form_data.append("answer", this.answer);
                for (var i = 0; i < (fileData.files.length); i++) {
                    form_data.append("uploadFile", fileData.files[i]);
                }
                ajaxcreateUpload(form_data, url1);
                router.push({
                    path: '/faq/showFaqList',
                    query: {
                        id: id
                    }
                })

            }
        },
        faqCancel: function() {
            router.push({
                path: '/faq/showFaqList',
                query: {
                    id: this.$route.query.id
                }
            })
        },
        showUploadFile: function() {
            var fileData = $("input[name='uploadFile']").get(0);
            var txt = "";
            if ('files' in fileData) {
                for (var i = 0; i < fileData.files.length; i++) {
                    txt += (i + 1) + ". file ";
                    var file = fileData.files[i];
                    if ('name' in file) {
                        txt += "name: " + file.name;
                    }
                    if ('size' in file) {
                        txt += "  file size: " + file.size + " bytes \n";
                    }
                }
            }
            this.attachements = txt;
        }
    }
};

const faqModify = {
    template: '#FaqModifyUi',
    data: function() {
        return {
            question: "",
            answer: "",
            attachments: [],
            attach: [],
            attachements1: "",
            id: "",
        }
    },
    created: function() {
        var id = this.$route.query.id;
        var url1 = ctx + "/admin/faq/putForm/" + id;
        var result = ajaxGet(url1);
        if (result.faq.attachments != undefined) {
            this.attachments = result.faq.attachments.split(",");
        }
        this.attach = this.attachments;
        this.question = result.faq.question;
        this.answer = result.faq.answer;
        this.id = result.faq.categoryId;
    },
    methods: {
        showUploadFile: function() {
            var fileData = $("input[name='uploadFile']").get(0);
            var txt = "";
            if ('files' in fileData) {
                for (var i = 0; i < fileData.files.length; i++) {
                    txt += (i + 1) + ". file ";
                    var file = fileData.files[i];
                    if ('name' in file) {
                        txt += "name: " + file.name;
                    }
                    if ('size' in file) {
                        txt += "  file size: " + file.size + " bytes \n";
                    }
                }
            }
            this.attachements1 = txt;
        },
        adminFaqPut: function() {

            let id1 = this.$route.query.id;
            let attachs = "";
            if (this.question.trim().length == 0) {
                myAlert("Please input question");
            } else {
                var fileData = $("input[name='uploadFile']").get(0);

                var temp = this.attach
                $.each(temp, function(i, n) {
                    if (i == (temp.length - 1)) {
                        attachs += n;
                    } else {
                        attachs += n + ",";
                    }
                });
                url1 = ctx + "/admin/faq/put";
                var form_data = new FormData();
                form_data.append("id", id1);
                form_data.append("question", this.question);
                form_data.append("answer", this.answer);
                form_data.append("attachments", attachs);
                for (var i = 0; i < (fileData.files.length); i++) {
                    form_data.append("uploadFile", fileData.files[i]);
                }
                ajaxPutUpload(form_data, url1);
                router.push({
                    path: '/faq/showFaqList',
                    query: {
                        id: this.id
                    }
                });

            }
        },

        adminFaqDelete: function() {
            let id1 = this.$route.query.id;
            var url1 = ctx + "/admin/faq/delete/" + id1;
            $.ajax({
                type: "GET",
                url: url1,
                async: false,
                success: function(result) {
                    myAlert("DELETE OK ");
                },
                timeout: 3000,
                error: function(xhr) {
                    myAlert("error： " + xhr.status + " " + xhr.statusText);
                },
            });

            router.push({
                path: '/faq/showFaqList',
                query: {
                    id: this.id
                }
            });
        },
        faqCancel: function() {
            router.push({
                path: '/faq/showFaqList',
                query: {
                    id: this.id
                }
            });
        }

    }
};

const category = {
    template: '#category',
    data: function() {
        return {
            id: "",
            name: "",
            description: "",
            categoryList: [],
        }
    },
    created: function() {
        this.getCategory();
    },
    methods: {
        addNew: function() {
            this.name = "";
            this.description = "";
            $("#categoryModal").modal('show');
        },

        addNewSubmit: function() {
            if (this.id.length == 0) {
                var url1 = ctx + "/admin/faqCategory/post";
                if ($.trim(this.name).length == 0) {
                    myAlert("Please input name");
                } else {
                    var savedata = {
                        name: this.name,
                        description: this.description,
                    }
                    ajaxcreate(savedata, url1);
                }
            };
            if (this.id.length > 0) {
                if ($.trim(this.name).length == 0) {
                    myAlert("Please input name");
                } else {
                    var url1 = ctx + "/admin/faqCategory/put";
                    var putdata = {
                        id: this.id,
                        name: this.name,
                        description: this.description,
                    }
                    ajaxPut(putdata, url1);
                }
            };
            $("#categoryModal").modal('hide');
            this.getCategory();
            router.push({
                path: '/category'
            });
        },

        del: function(id) {
            var conf = confirm("Are you sure?");
            if (conf == true) {
                url1 = ctx + "/admin/faqCategory/delete/" + id;
                $.ajax({
                    type: "GET",
                    url: url1,
                    async: false,
                    success: function(result) {
                        if (result.code === 0) {
                            myAlert("DELETE OK ");
                        }
                        if (!result.success) {
                            myAlert(result.message + " ,you can't delete it ");
                        }
                    },
                    timeout: 3000,
                    error: function(xhr) {
                        myAlert("error： " + xhr.status + " " + xhr.statusText);
                    },
                });
                this.getCategory();
            }
        },
        mod: function(id, name, description) {
            this.id = id;
            this.name = name;
            this.description = description;
            $("#categoryModal").modal('show');
        },

        getCategory: function() {
            let url1 = ctx + "/admin/faqCategory/listForm";
            let checkKey = "";
            let result = ajaxFind(checkKey, url1);
            if (result.length == 0) {
                return false;
            }
            if (result.data.faqCategoryList != undefined) {
                this.categoryList = result.data.faqCategoryList;
            }
        },

    }
};



const pageChange = {
    template: '#pageChange',
    data: function() {
        return {
            curPage: 0,
            totalPage: 0,
            showKey: false,
        }
    },
    watch: {
        '$route': function(to, from) {
            this.curPage = this.$store.state.curPage;
            this.totalPage = this.$store.state.totalPage;
            this.showKey = "this.$store.state.total==0 ? 'false':'true'";
        }
    },
    computed: {
        curPage: function() {
            return this.$store.state.curPage
        },
        totalPage: function() {
            return this.$store.state.totalPage
        },
        showKey: function() {
            if (this.$store.state.total == 0) {
                return false
            } else {
                return true
            }
        },
    },
    methods: {
        changePage: function(page) {
            if (this.curPage != page) {
                if (page > this.totalPage) {
                    this.$store.state.curPage = this.totalPage;
                } else if (page <= 0) {
                    this.$store.state.curPage = 1;
                } else {
                    this.$store.state.curPage = page;
                }
                var id = this.$route.params.id;

                store.commit('getFaq', {
                    id: id
                });
            }
        }
    }

};

const profile = {
    template: "#profile",
    data: function() {
        return {
            name: "",
            password1: "",
            password2: "",
        }
    },
    computed: {
        name: function() {
            return username;
        }
    },
    mounted: function() {
        $("#profileModal").modal('show');

    },
    methods: {
        addNewSubmit: function() {
            if (this.password1 === this.password2 && this.password1.trim().length > 0) {
                var url1 = ctx + "/profile/put";
                var putdata = {
                    plainPassword: this.password1,
                }
                ajaxPut(putdata, url1);

                $("#profileModal").modal('hide')
                this.password1 = "";
                this.password2 = "";
            } else {
                if (this.password1.trim().length == 0 && this.password2.trim().length == 0) {
                    myAlert("NO password ")
                } else {
                    myAlert("The two password is different,please try again")
                }

                this.password1 = "";
                this.password2 = "";
            }
        },
       closeModal:function(){
           $("#profileModal").modal('hide')
           router.push({
               path: '/home'
           });
       }
    },
}