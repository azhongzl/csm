var test=1;
var stompClient = null;
var subscribeList ;
var plainPassword="kuzco123";
var root = "root";
const
chat = {
	template : '#chatMainPage',
};

const
customer = {
	template : '#customer',
	computed : {
		customers : function() {
			return this.$store.state.customers;
		}
	},
	created: function(){
		connect1();
	}
};

const
service = {
	template : '#service',
	data : function() {
		return {
			selected : '',

		}
	},
	created: function(){
		store.commit('getService',{id:this.$route.query.id});
	},
	computed : {
		service : function() {
			return this.$store.state.service;
		},
		serviceList : function() {
			return this.$store.state.serviceList;
		}
	},
	methods : {
		removeGroup : function(id, index) {
			var canSendMsg=false;
			if(store.state.currentUserGroup.chatOrSuper){
				canSendMsg=true;
			}else{
				if(store.state.customerUserGroupList.indexOf(this.$route.query.id)!=-1){
					canSendMsg=true;
				}
			}
			if(!canSendMsg){
				return;
			}
		   	let url1=ctx+ "/admin/chat/deleteCustomerUserGroup/" + id;
			$.ajax({
				type : "GET",
				url : url1,
				async : false,
				success : function(result) {
					myAlert("DELETE OK ");
				},
				timeout : 3000,
				error :handleError
			});
			//store.commit('getService',{id:this.$route.query.id});
		},
		
	 selectFunction : function(service) {
		 	if (service==undefined){
		 		return;
		 	}
		 	var serviceId=service.id;
			let	id = this.$route.query.id;
			var canSendMsg=false;
			if(store.state.currentUserGroup.chatOrSuper){
				canSendMsg=true;
			}else{
				if(store.state.customerUserGroupList.indexOf(id)!=-1){
					canSendMsg=true;
				}
			}
			if(!canSendMsg){
				return;
			}
			var url1=ctx+"/admin/chat/postCustomerUserGroup";

			var savedata = {
					customerUserId:id,
					userGroupId:serviceId,
					operatorUserId:userId,
			};

			ajaxcreate(savedata, url1);
			//store.commit('getService',{id:id});
			
		},

	}
};

const content={
		  template:'#content',
		  data:function(){
			return {

				sentence:"",
					}; 
		  },
			watch:{
				'$route':function(to,from){
					store.commit('getService',{id:this.$route.query.id});
					store.commit('getCustomerName',{id:this.$route.query.id,name:this.$route.query.name});
					let customerId=	this.$route.query.id;
						showMsg(customerId);
					}
			},
			created:function(){
				let customerId=	this.$route.query.id;
				store.commit('getCustomerName',{id:customerId,name:this.$route.query.name});
				showMsg(customerId);
			},
		  computed:{
			  customerName : function(){
				  return this.$store.state.customerName;
			  }
		  },

		methods:{
				send:function(){
					if(this.sentence.length>0){
						let customerId=	this.$route.query.id;
						stompClient.send('/app/chatASendMessage', {}, JSON.stringify({
							'message' : this.sentence,
							'roomId' : customerId,
						}));
						this.sentence="";
					}
			}
		  }
		};


const permission={
		  template:'#permission',
		  data:function(){
			  return{
				  permissions:[],
				  permissionName:"",
				  permissionValue:"",
				  permissionId:"",
				  modalTitle:""
			  }
		  },

		  created:function(){
			 this.getPermission();
		  },

		methods:{
			addNew:function(){
				this.modalTitle="ADD NEW PERMISSION";
				$("#myPermissionModal").modal('show');
				
			},
			addNewSubmit:function(){
             if(this.permissionId.trim().length==0){
				if (this.permissionName.trim().length>0){
					var url1=ctx+"/admin/permission/post"
					var savedata = {
							name : this.permissionName,
							permission : this.permissionValue,
	  				};
	  				ajaxcreate(savedata, url1);
	  				 this.getPermission();
	  				  this.permissionName="";
	  				  this.permissionValue="";
	  				$("#myPermissionModal").modal('hide');
				}else{
					myAlert("Please enter permission name ");
				};
             }else{
          	   if(this.permissionName.trim().length>0){
    				var url1 = ctx + "/admin/permission/put" ;
  				var putdata = {
  					id:this.permissionId,
  					name : this.permissionName,
  					permission : this.permissionValue,
  			    				}
  				ajaxPut(putdata, url1);
  				 this.getPermission();
	  				  this.permissionName="";
	  				  this.permissionValue="";
	  				  this.permissionId="";
	  				$("#myPermissionModal").modal('hide');
          	   }else{
          		   myAlert("Please enter permission name ");  
          	   }
          	   
             }
			},
			del:function(id){


				var conf=confirm("are you sure?");
				if (conf){
					let url1 = ctx+ "/admin/permission/delete/"+id;
					$.ajax({
						type : "GET",
						url : url1,
						async : false,
						success : function(result) {
							if (result.code===0){
								myAlert("DELETE OK ");	
							}
							if (!result.success){
								myAlert(result.message+" ,you can't delete it ");	
							}

						},
						timeout : 3000,
						error :handleError
					});
					 this.getPermission();
				}
			},
			mod:function(id,name,per){
				this.permissionName=name;
				this.permissionValue=per;
				this.permissionId=id;
				this.modalTitle="MODIFY PERMISSION";
				$("#myPermissionModal").modal('show');
					
			},
			getPermission:function(){
			   	let url = ctx+ "/admin/permission/listForm";
		    	let checkKey = {};
	    	let result = ajaxFind(checkKey, url);
	    	if(result.length==0){
	    		return false;
	    	}
	    	if (result.data.permissionList!=undefined){
		    	this.permissions = result.data.permissionList;    		
	    	}
			},
			}
		};


const role={
		  template:'#role',
		  data:function(){
			return{
				modalTitle:"",
				name:"",
				id:"",
				roles:[],

			}  
		  },
		  created:function(){
			  this.getRoles();
			  
		  },
		methods:{
			addNew:function(){
				this.modalTitle="ADD NEW ROLE";
				this.name="";
				$("#roleModal").modal("show");

			},
			addNewSubmit:function(){
				if (this.name.trim().length>0 && this.id.length==0){
					var url1=ctx+"/admin/role/post"
					var savedata = {
							name:this.name
					};
					ajaxcreate(savedata, url1);
					  this.getRoles();
					}
				if (this.name.trim().length>0 && this.id.length>0){
					var url1 = ctx + "/admin/role/put/" ;
					var putdata = {
						name : this.name,
						id : this.id,
				    				}
					ajaxPut(putdata, url1);
					  this.getRoles();
						}

				$("#roleModal").modal('hide');
			},
			del:function(id){
				var conf=confirm("are you sure?");
				if (conf){
					url1=ctx+ "/admin/role/delete/" + id;
					$.ajax({
						type : "GET",
						url : url1,
						async : false,
						success : function(result) {
							if (result.code===0){
								myAlert("DELETE OK ");	
							}
							if (!result.success){
								myAlert(result.message+" ,you can't delete it ");	
							}
						},
						timeout : 3000,
						error :handleError
					});
					  this.getRoles();
				}
			},
			mod:function(id){
				let url1 = ctx+ "/admin/role/putForm/"+id;
		    	let checkKey = { 	};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.role!=undefined){
					this.id=id;
					this.name=result.data.role.name;
		    	}
				this.modalTitle="MODIFY ROLE";
				$("#roleModal").modal('show');
			},
			getRoles:function(){
				let url1 = ctx+ "/admin/role/listForm";
		    	let checkKey = { 	};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.roleList!=undefined){
		    		this.roles = result.data.roleList;    		
		    	}
			},
			}
		};

const role_permission={
	template:"#role_permission",
	data:function(){
		return{
			rolePermissions:[],
			permissions:[],
		}
	},
	  created:function(){
		  this.getPermissions(this.$route.query.id);
	  },

	  methods:{
		  add:function(id){
			  var each=true;
			  $.each(this.rolePermissions,function(i,n){
				 if (n.permission.id==id){
					 myAlert("you already have this permission");
					 each=false;
					 return false;
				 }
			  });
			  if(each){
					var url1=ctx+"/admin/role/postRolePermission";
					var savedata = {
							roleId:this.$route.query.id,
							permissionId:id,
	    				};
	    				ajaxcreate(savedata, url1);
	    				  this.getPermissions(this.$route.query.id);
			  };

		  },
		  remove:function(id){
				url1=ctx+ "/admin/role/deleteRolePermission/" + id;
				$.ajax({
					type : "GET",
					url : url1,
					async : false,
					success : function(result) {
						myAlert("DELETE OK ");
					},
					timeout : 3000,
					error :handleError
				});
				this.getPermissions(this.$route.query.id);
		  },
		  
		  getPermissions:function(id){
				let url = ctx+ "/admin/role/listRolePermissionForm/"+id;
		    	let checkKey = {};
		    	let result = ajaxFind(checkKey, url);
		    	if(result.length==0){
		    		return false;
		    	}
			    this.rolePermissions = result.data.rolePermissionList;    		
		    	this.permissions = result.data.roleList;
		  },
		  
		  back:function(){
			  router.push({path:'/role'});
		  }
	  }
};

const group={
		  template:'#group',
		  data:function(){
			  return{
				  
				  groups:"",
				  group:"",
				  groupId:"",
				  groupText:"",
				  sele:"",
				  groupOptions: [ ],
				  checked1:false, 
				  checked2:false,
				  modalTitle:"",
			  }
		  },
		  created:function(){
			  this.getGroups();
		  },

		methods:{

			addNew:function(){
		    	this.getGroupsOptions();
				this.groupText="";
				this.checked1=false;
				this.checked2=false;
				this.sele="";
				this.modalTitle="ADD NEW GROUP";
				$("#myModal").modal('show');
				},
			

			addNewSubmit:function(){
				if(this.groupId.length==0){
					if(this.groupText.length==0){
					myAlert('Please enter group name');
					}else{
						var url1=ctx+"/admin/userGroup/post";
						var savedata = {
							name:this.groupText,
							admin:this.checked1,
							chat:this.checked2,
							superId:this.sele,
							};

						ajaxcreate(savedata, url1);
						this.groupText="";
						this.checked1=false;
						this.checked2=false;
						this.sele="";
						this.groupOptions= [];
						$("#myModal").modal('hide')
						
					}
				}
				if(this.groupId.length>0){
					 var url1 = ctx + "/admin/userGroup/put" ;
					 var putdata = {
							 	id:this.groupId,
								name:this.groupText,
								admin:this.checked1,
								chat:this.checked2,
								superId:this.sele,
					 }
					 ajaxPut(putdata, url1);
					$("#myModal").modal('hide')
						this.groupText="";
						this.checked1=false;
						this.checked2=false;
						this.sele="";
						this.groupOptions= [];
				}
				this.getGroups();
			},

			del:function(id){

						var conf=confirm("are you sure?");
						if (conf){
							url1=ctx+ "/admin/userGroup/delete/" + id;
							$.ajax({
								type : "GET",
								url : url1,
								async : false,
								success : function(result) {
									if (result.code===0){
										myAlert("DELETE OK ");	
									}
									if (!result.success){
										myAlert(result.message+" ,you can't delete it ");	
									}
								},
								timeout : 3000,
								error :handleError
							});
							this.getGroups();
						}
			},
			mod:function(id){
		    	this.getGroup(id);
				this.groupText=this.group.name;
				this.checked1=this.group.admin;
				this.checked2=this.group.chat;
				this.sele=this.group.superId;
				this.groupId=id;
				this.modalTitle="MODIFY GROUP";
				$("#myModal").modal('show');
			},
			getGroups:function(){
				let url1 = ctx+ "/admin/userGroup/listForm";
		    	let checkKey = { 	};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.userGroupList!=undefined){
		    		this.groups = result.data.userGroupList;    		
		    	}
			},
			getGroup:function(id){
				let url1 = ctx+ "/admin/userGroup/putForm/"+id;
		    	let checkKey = {};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.userGroup!=undefined){
		    		this.group = result.data.userGroup; 
		    		this.groupOptions = result.data.superUserGroupList;
		    	}
			},
			getGroupsOptions:function(){
				let url1 = ctx+ "/admin/userGroup/postForm";
		    	let checkKey = { 	};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.superUserGroupList!=undefined){
		    		this.groupOptions = result.data.superUserGroupList;    		
		    	}
			},
		}
		};

const group_role={
		template:"#group_role",
		data:function(){
			return{
				roles:[],
				group_roles:[],
			}
		},
 		  created:function(){
 			 this.getRoles(this.$route.query.id);
		  },

		  methods:{
			  add:function(id){
				  var each=true;
				  $.each(this.group_roles,function(i,n){
					 if (n.roleId==id){
						 myAlert("you already have this role");
						 each=false;
						 return false;
					 }
				  });
				  if(each){
						var url1=ctx+"/admin/userGroup/postUserGroupRole";
						var savedata = {
								userGroupId:this.$route.query.id,
								roleId:id,
		    				};
		    				ajaxcreate(savedata, url1);
		    				 this.getRoles(this.$route.query.id);
				  };

			  },
			  remove:function(id){
					url1=ctx+ "/admin/userGroup/deleteUserGroupRole/" + id;
					$.ajax({
						type : "GET",
						url : url1,
						async : false,
						success : function(result) {
							myAlert("DELETE OK ");
						},
						timeout : 3000,
						error :handleError
					});
   				 this.getRoles(this.$route.query.id);
			  },
			  getRoles:function(id){
					let url = ctx+ "/admin/userGroup/listUserGroupRoleForm/"+id;
			    	let checkKey = {};
			    	let result = ajaxFind(checkKey, url);
			    	if(result.length==0){
			    		return false;
			    	}
			    	this.group_roles = result.data.userGroupRoleList;    		
			    	this.roles = result.data.roleList;
			  },
			  back:function(){
				  router.push({path:'/group'});
			  }
		  }
	};

const user={
		  template:'#user',
		  data:function(){
			  return{
				  users:[],
				  user:[],
				  showKey:false,
				  username:"",
				  userId:"",
				  sele:"",
				  groupOptions: [ ],
				  checked:false, 
				  modalTitle:"",
				  pageSize: 10,
				  curPage: 1,
				  total:0,
				  totalPage:0,
				  pageShowKey:false,
  
			  }
		  },
		  created:function(){
			 this.getUsers();
		  },

		methods:{
			addNew:function(){
				this.getGroups();
				this.showKey=false;
				this.username="";
				this.checked=false;
				this.sele="";
				this.userId="";
				this.modalTitle="ADD NEW USER";
				$("#myModal1").modal('show');
				},

			addNewSubmit:function(){
				if(this.userId.length==0){
					if(this.username.length==0||this.sele.length==0){
					myAlert('Please enter name and select group');
					}else{
						var url1=ctx+"/admin/user/post";
						var savedata = {
							username:this.username,
							active:true,
							userGroupId:this.sele,
							plainPassword:plainPassword,
							};

						ajaxcreate(savedata, url1);
						this.username="";
						this.sele="";
						this.groupOptions= [];
						$("#myModal1").modal('hide');
						 this.getUsers();
					}
				}
				if(this.userId.length>0){
					 var url1 = ctx + "/admin/user/put" ;
					 var putdata = {
								active:this.checked,
								userGroupId:this.sele,
								id:this.userId
					 }
					 ajaxPut(putdata, url1);
					$("#myModal").modal('hide')
						this.username="";
						this.checked=false;
						this.sele="";
						this.groupOptions= [];
						$("#myModal1").modal('hide')
						this.getUsers();
				}
			},

			del:function(id){
						var conf=confirm("are you sure?");
						if (conf){
							url1=ctx+ "/admin/user/delete/" + id;
							$.ajax({
								type : "GET",
								url : url1,
								async : false,
								success : function(result) {
									myAlert("DELETE OK ");
								},
								timeout : 3000,
								error :handleError
							});
							this.getUsers();
					}

			},
			mod:function(id){
				this.getUser(id)
				this.username=this.user.username;
				this.checked=this.user.active;
				this.sele=this.user.userGroupId;
				this.userId=id;
				this.showKey=true;
				this.modalTitle="MODIFY USER";
				$("#myModal1").modal('show');
			},
			getUsers:function(){
				let url1 = ctx+ "/admin/user/listForm";
		    	let checkKey = {
		    			page_no:this.curPage,
		    			page_size:this.pageSize
		    			
		    	};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.userList.content!=undefined){
			    	this.users = result.data.userList.content;
			    	this.totalPage=result.data.userList.totalPages;
			    	if (this.totalPage>0){
			    		this.pageShowKey=true;
			    	}
		    	}
			},
			
			getUser:function(id){
				let url1 = ctx+ "/admin/user/putForm/"+id;
		    	let checkKey = {};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
			    	this.user = result.data.user;
			    	this.groupOptions = result.data.userGroupList;
			},
			getGroups:function(){
				let url1 = ctx+ "/admin/user/postForm";
		    	let checkKey = { 	};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if (result.data.userGroupList!=undefined){
		    		this.groupOptions = result.data.userGroupList;    		
		    	}
			},
         	changePage:function(page){
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
	template : '#faqMain',
};

const faq = {
		template : '#FaqCategory',
		data:function(){
			return{
				categoryList:[],	
			}
		},
	    created:function(){
	    	this.getCategory();
	    },
	    methods:{
	    	getCategory:function(){
			   	let url1 = ctx + "/admin/faqCategory/listForm";
		    	let checkKey = "";
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
		    	if(result.data.faqCategoryList!=undefined){
			    	this.categoryList = result.data.faqCategoryList;		    		
		    	}
	    	},
	    },
	};

const showFaqList = {
		template : '#FaqDetail',
		data:function(){
			return{
				cssId:"",
				faqList:[],
			}
		},
		watch:{
			'$route':function(to,from){
				var id=this.$route.query.id;
				$("#"+this.cssId).css("color","#337ab7");
		    	$("#"+id).css("color","red");
		    	this.cssId=id;
				this.$store.state.curPage=1;
	            this.getFaq(id);
			}
		},

	    created:function(){
	    	var id=this.$route.query.id;
	    	$("#"+id).css("color","red");
	    	this.cssId=id;
            this.getFaq(id);
	    },

	    methods:{
	    	faqAddNewUi:function(){
	    		router.push({path:'/faq/faqAddNew',query:{id:this.$route.query.id}})
	    	},
	    	getFaq:function(id){
				let url1 = ctx + "/admin/faq/listForm/"+id;
				let checkKey = {
					page_size : this.$store.state.pageSize,
					page_no : this.$store.state.curPage,
				};
		    	let result = ajaxFind(checkKey, url1);
		    	if(result.length==0){
		    		return false;
		    	}
				if (result.data.faqList.content !== undefined) {
			    	this.faqList = result.data.faqList.content;
					this.$store.state.total = result.data.faqList.totalElements;
					this.$store.state.totalPage = result.data.faqList.totalPages;
				}else{
					this.faqList =[];
					this.$store.state.total = 0;
					this.$store.state.totalPage = 0;
				}
	    	},
	    },
	};

const faqAddNew = {
		template : '#FaqAddNewUi',
		data :function(){
			return{
				question:"",
				answer: "",
				attachements:"",
			}
		},
	    methods:{
	    	faqAddNew:function(){
	    		let id=this.$route.query.id;
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
	    				router.push({path:'/faq/showFaqList', query:{id:id}})

	    		}
		    	},
	    	faqCancel:function(){
	    		router.push({path:'/faq/showFaqList',query:{id:this.$route.query.id}})
	    	},
		    showUploadFile:function(){
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
	    		this.attachements=txt;
	    	}
	    }
	};

const faqModify = {
		template : '#FaqModifyUi',
		data :function(){
			return{
				question:"",
				answer: "",
				attachments:[],
				attach:[],
			    attachements1:"",
			    id:"",
			}
		},
		created:function(){
			var id=this.$route.query.id;
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
		methods:{
		    showUploadFile:function(){
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
	    		this.attachements1=txt;
	    	},
	    	adminFaqPut:function(){

	    		let id1=this.$route.query.id;
	    		let attachs="";
	    		if (this.question.trim().length == 0) {
	    			myAlert("Please input question");
	    		} else {
	    			var fileData = $("input[name='uploadFile']").get(0);
	    			
	    					var temp=this.attach
	    					$.each(temp,function(i, n) {
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
	    				router.push({path:'/faq/showFaqList', query:{id:this.id}});

	    		}
	    	},

	    	adminFaqDelete:function() {
	    		let id1=this.$route.query.id;
	    		var url1 = ctx + "/admin/faq/delete/" + id1;
	    		$.ajax({
	    			type : "GET",
	    			url : url1,
	    			async : false,
	    			success : function(result) {
	    				myAlert("DELETE OK ");
	    			},
	    			timeout : 3000,
	    			error : function(xhr) {
	    				myAlert("error： " + xhr.status + " " + xhr.statusText);
	    			},
	    		});

				router.push({path:'/faq/showFaqList', query:{id:this.id}});
	    	},
	    	faqCancel:function(){
				router.push({path:'/faq/showFaqList', query:{id:this.id}});
	    	}
	    	
		}
	};

const category = {
		template : '#category',
		data : function(){
			return{ 
				id:"",
				name:"",
				description:"",
				categoryList:[],
			}
		},
	    created:function(){
	    	this.getCategory();
	    },
	    methods:{
			addNew:function(){
		    	this.name = "";
				this.description="";
				$("#categoryModal").modal('show');
				},

			addNewSubmit:function(){
				if(this.id.length==0){
					var url1 = ctx + "/admin/faqCategory/post";
					if ($.trim(this.name).length == 0) {
						myAlert("Please input name");
					} else {
						var savedata = {
							name : this.name,
							description :  this.description,
						}
						ajaxcreate(savedata, url1);
					}
				};
				if(this.id.length>0){
					if ($.trim(this.name).length == 0) {
						myAlert("Please input name");
					} else {
						var url1 = ctx + "/admin/faqCategory/put";
						var putdata = {
							id:this.id,
							name : this.name,
							description : this.describe,
						}
						ajaxPut(putdata, url1);
					}
				};
				$("#categoryModal").modal('hide');	
		    	this.getCategory();
				router.push({path: '/category'});
			
				},

				del:function(id){
					var conf=confirm("Are you sure?");
					if (conf==true){
							url1 = ctx + "/admin/faqCategory/delete/" + id;
							$.ajax({
								type : "GET",
								url : url1,
								async : false,
								success : function(result) {
									if (result.code===0){
										myAlert("DELETE OK ");	
									}
									if (!result.success){
										myAlert(result.message+" ,you can't delete it ");	
									}
								},
								timeout : 3000,
								error : function(xhr) {
									myAlert("error： " + xhr.status + " " + xhr.statusText);
								},
							});
					    	this.getCategory();
					}
				},
				mod:function(id,name,description){
			    	this.id = id;
					this.name=name;
					this.description=description;
					$("#categoryModal").modal('show');
				},
				
		    	getCategory:function(){
				   	let url1 = ctx + "/admin/faqCategory/listForm";
			    	let checkKey = "";
			    	let result = ajaxFind(checkKey, url1);
			    	if(result.length==0){
			    		return false;
			    	}
			    	if(result.data.faqCategoryList!=undefined){
				    	this.categoryList = result.data.faqCategoryList;		    		
			    	}
		    	},
				
	    }
	};



const pageChange = {
		template:'#pageChange',
		data :function(){
			return{
				curPage:0,
				totalPage:0,
				showKey:false,
			}
		},
		watch:{
			'$route':function(to,from){
			this.curPage = this.$store.state.curPage;
			this.totalPage = this.$store.state.totalPage;
			this.showKey ="this.$store.state.total==0 ? 'false':'true'";
			}
		},
        computed:{
    	    curPage:function(){
    	    	return this.$store.state.curPage
     	    },
    	    totalPage:function(){
    	    	return this.$store.state.totalPage
    	    },
    	    showKey:function(){
    	    	if  (this.$store.state.total==0){
    	    		return false
    	    	}else{
    	    		return true
    	    	}
     	    },
        },
        methods:{
         	changePage:function(page){
  			if (this.curPage != page) {
    				if (page > this.totalPage) {
    					this.$store.state.curPage = this.totalPage;
    				} else if (page <= 0) {
    					this.$store.state.curPage = 1;
    				} else {
    					this.$store.state.curPage = page;
    				}
    				var id=this.$route.params.id;

     		    	store.commit('getFaq',{id : id});
    			}
         	}
        }
		
};

const profile={
		template:"#profile",
		data:function(){
			return{
			name:"",
			password1:"",
			password2:"",
			}
		},
		computed:{
			name:function(){
				return username;
			}
		},
		mounted:function(){

 			$("#profileModal").modal('show');

		},
		methods:{
			addNewSubmit:function(){
				if(this.password1===this.password2 && this.password1.trim().length>0 ){
					 var url1 = ctx + "/profile/put" ;
					 var putdata = {
							 plainPassword:this.password1,
					 }
					 ajaxPut(putdata, url1);

					$("#profileModal").modal('hide')
						this.password1="";
						this.password2="";
				}else{
					if(this.password1.trim().length==0 && this.password2.trim().length==0){
						myAlert("NO password ")
					}else{
						myAlert("The two password is different,please try again")
					}
				
					this.password1="";
					this.password2="";
				}
			},
		},
}


const
router = new VueRouter({
	routes : [ {
		path : '/admin/chat',
		components : {
			mainRouter : chat,
		},
		children : [ {
			path : '',
			components : {
				customer : customer,
			},
		}, {
			path : 'content',
			components : {
				customer : customer,
				service : service,
				content : content,
			},
		},
		],
	}, 
	{
		path : '/permission',
		components :{mainRouter:permission},
	},
	
	 { path: '/role', 
	      components:{mainRouter : role	},
	},
	{ path: '/role_permission', 
  	components:{mainRouter : role_permission},
	},
	 { path: '/group', 
	      components:{mainRouter : group,},
	},
	 { path: '/group_role', 
	      components:{mainRouter : group_role,},
	},
	 { path: '/user', 
	      components:{mainRouter : user,},
	      children : [{
				path : '',
				components : {
				default : faq,
				},
			} 
	                  ]
	},
	
	 { path: '/faq', 
	   components:{mainRouter : faqMain,},
	   children : [ {
					path : '',
					components : {
					default : faq,
					},
				}, 
				{
					path : 'showFaqList',
					components : {
					default : faq,
					content : showFaqList,
					pagecount:pageChange,
					},
				}, 
				{ path: 'FaqAddNew',
					components:{
				   	default : faq,
				   	content : faqAddNew
				        	},
				 },
				{ path: 'FaqModify',
					components:{
					default : faq,
					content : faqModify
						     	},
					 },
			],
	},
	 { path: '/category', 
	      components:{mainRouter : category,},
	},
	 { path: '/profile', 
	      components:{mainRouter:profile, },
	},
	
	],
});



new Vue({
	store : store,
	router : router,
	el : '#wrapper',
});

var vm=new Vue({
	el : '#myAlertModal',
	data :{
		message:"",
		modalTitle:"",
	},
	methods:{
		showMsg:function(){
			this.modalTitle=username;
			$("#myAlertModal").modal("show");
		},
	},

})



function connect1() {
	var socket = new SockJS('/csm/ws');
	stompClient = Stomp.over(socket);
	var headers = {
		admin : true
	};

	stompClient.connect(headers, function(frame) {
		subscribeList = [];
		stompClient.subscribe('/app/chatAInit', function(message) {
			showCustomerSet(JSON.parse(message.body).data.customerList);
			store.state.currentUserGroup=JSON.parse(message.body).data.currentUserGroup;
			$.each(JSON.parse(message.body).data.customerUserGroupList,function(i,n){
				store.state.customerUserGroupList.push(n.customerUserId);
			});
				stompClient.subscribe('/topic/chat/addCustomerUserGroup',
						function(message) {
							var data=JSON.parse(message.body);
							store.commit('getService',{id:data.data.customerId});
							if (data.data.userGroupId==store.state.currentUserGroup.userGroup.id){
								store.state.customerUserGroupList.push(data.data.customerId);
								showUnhandledCustomer(data.data.customerId);
								if(store.state.customerId==data.data.customerId){
									showMsg(data.data.customerId);
								}
							}

							});
				stompClient.subscribe('/topic/chat/removeCustomerUserGroup',
						function(message) {
					var data=JSON.parse(message.body);
					store.commit('getService',{id:data.data.customerId});
					if (data.data.userGroupId==store.state.currentUserGroup.userGroup.id){
							var indexNo=store.state.customerUserGroupList.indexOf(data.data.customerId);
							store.state.customerUserGroupList.splice(indexNo,1);
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
	store.state.customers=[];
	for (var i = 0; i < customerSet.length; i++) {
		showCustomer(customerSet[i]);
	}
}

function showCustomer(customer) {
	var customerId = customer.userId;
	store.state.customers.push(customer);
	if (customer.online) {
		setTimeout(function(){showOnlineCustomer(customer);}, 200);
	}
	if (customer.unhandled) {
		setTimeout(function(){showUnhandledCustomer(customerId);}, 200);
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
	var canSendMsg=false;
	if(store.state.currentUserGroup.userGroup.chat){
		canSendMsg=true;
	}else{
		if(store.state.customerUserGroupList.indexOf(userId)!=-1){
			canSendMsg=true;
		}
	}
	if(!canSendMsg){
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
	if(intervalMap.has(roomId)){
		clearInterval(intervalMap.get(roomId));
		intervalMap.delete(roomId);
	}
}

function showMsg(customerId){
	$("#sentence").empty();
	var canSendMsg=false;
	if(store.state.currentUserGroup.chatOrSuper){
		canSendMsg=true;
	}else{
		if(store.state.customerUserGroupList.indexOf(customerId)!=-1){
			canSendMsg=true;
		}
	}
	if (canSendMsg)	{
		for (var i = 0; i < subscribeList.length; i++) {
			subscribeList[i].unsubscribe();
		}
		subscribeList = [];
		var subApp = stompClient.subscribe('/app/chatAInitMessage/'
				+ customerId, function(message) {
			showMessages(JSON.parse(message.body).data.messageList);
	
		});
		var subTopic = stompClient.subscribe('/topic/chat/message/'
				+ customerId, function(message) {
			showMessage(JSON.parse(message.body));
			document.getElementById ( 'sentence').scrollTop=document.getElementById ( 'sentence').scrollHeight ;
		});
		subscribeList.push(subApp);
		subscribeList.push(subTopic);
	}
}

function showMessages(messageList) {
	for (var i = 0; i < messageList.length; i++) {
		showMessage(messageList[i]);
	}
    document.getElementById ( 'sentence').scrollTop=document.getElementById ( 'sentence').scrollHeight ;  
}

function showMessage(message) {
	var canSendMsg=false;
	if(store.state.currentUserGroup.chatOrSuper){
		canSendMsg=true;
	}else{
		if(store.state.customerUserGroupList.indexOf(store.state.customerId)!=-1){
			canSendMsg=true;
		}
	}
	if(!canSendMsg){
		return;
	}
	
	var timeStr="";
	var num=message.createDateTime.indexOf("T");
	timeStr=message.createDateTime.substring(0,num)+" "+message.createDateTime.substring(num+1,19);
	if (message.fromAdmin){
	$("#sentence").append("<div class='panel panel-primary' style='clear:both;float:right;width:500px'><div class='panel-heading' style='padding: 2px 0px 2px 300px' >"+message.senderName+"&nbsp;&nbsp;&nbsp;&nbsp;"+timeStr+"</div><div class='panel-body'>"+message.message+" </div></div>"
);
	}else{
		$("#sentence").append("<div class='panel panel-info' style='clear:both;float:left;width:500px'><div class='panel-heading' style='padding: 2px 0px ' >"+message.senderName+"&nbsp;&nbsp;&nbsp;&nbsp;"+timeStr+"</div><div class='panel-body'>"+message.message+" </div></div>"
		);
	}

}

function ajaxcreate(savedata, url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : savedata,
		cache : false,
		async : false,
		success : function(result) {
			myAlert("ADD NEW SUCCESS");
		},
		timeout : 3000,
		error :handleError

	});
}

function ajaxFind(checkKey, url1) {
	var checkList = [];
	$.ajax({
		type : "GET",
		url : url1,
		data : checkKey,
		cache : false,
		async : false,
		success : function(result) {
				checkList = result;
		},
		timeout : 3000,
		error : handleError,
	});

	return (checkList);
}



function ajaxPut(putdata, url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : putdata,
		cache : false,
		async : false,
		success : function(result) {
			myAlert("PUT OK ");
		},
		timeout : 3000,
		error : handleError,

	});
}

function ajaxGet(url1) {
	var checkList;
	$.ajax({
		type : "GET",
		url : url1,
		async : false,
		cache : false,
		success : function(result) {
			if (result.data == undefined) {
				myAlert("No Result");
			} else {
				checkList = result.data;
			}
		},
		timeout : 3000,
		error :handleError,
	});
	return checkList;
}

function handleError(xhr){

	if(xhr.status == 499){
		window.location.replace("${ctx}" + xhr.statusText);
	}else{
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
	var currentdate = date.getFullYear() + seperator1 + month + seperator1
			+ strDate + "T" + checkTime(date.getHours()) + seperator2
			+ checkTime(date.getMinutes()) + seperator2
			+ checkTime(date.getSeconds());
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
		type : "POST",
		url : url1,
		data : savedata,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(result) {
			myAlert("ADD NEW SUCCESS");
		},
		timeout : 3000,
		error : handleError,

	});
}

function ajaxPutUpload(putdata, url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : putdata,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(result) {
			myAlert("PUT OK ");
		},
		timeout : 3000,
		error : handleError,
	});
}

function myAlert(msg){
	vm.message=msg;
	vm.showMsg();
}


