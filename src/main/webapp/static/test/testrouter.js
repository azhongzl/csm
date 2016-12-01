
var ctx;
var stompClient = null;
var subscribeList ;
var plainPassword="kuzco123";
var url = "http://localhost:8080/csm/e/";
var userId = "";
var userName = "";

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
			selected : "",
		}
	},
	computed : {
		service : function() {
			let
			id = this.$route.params.id;
			let
			service1 = [];
			let
			service2 = this.$store.state.service;
			$.each(service2, function(i, n) {
				if (n.customer == id || n.customer == "any") {
					service1.push(n);
				}
			});
			return service1;
		},
		serviceList : function() {
			return this.$store.state.serviceList;
		}
	},
	methods : {
		showUser : function(name, index) {
			store.commit('removeUser', {
				id : index
			});

		},
		select : function(res) {
			let
			id = this.$route.params.id;
			res.customer = id;
			store.commit('addUser', {
				id : res
			});
		},

	}
};

const content={
		  template:'#content',
		  data:function(){
			return {
				userName:"",
				sentence:"",
					}; 
		  },
			watch:{
				'$route':function(to,from){
					let customerId=	this.$route.query.id;
					this.userName=this.$route.query.name;
						showMsg(customerId);
					}
			},
			created:function(){
				let customerId=	this.$route.query.id;
				this.userName=this.$route.query.name;
				showMsg(customerId);
			},
		  computed:{
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
				  permissionName:"",
				  permissionValue:"",
				  permissionId:"",
			  }
		  },

		  created:function(){
			  store.commit('permission');
		  },

		  computed:{
			  permissions:function(){
				  return this.$store.state.permissions;
			  },

		  },
		methods:{
			addNew:function(){
				$("#myPermissionModal").modal('show');
			},
			addNewSubmit:function(){
             if(this.permissionId.trim().length==0){
				if (this.permissionName.trim().length>0){
					var url=ctx+"/e/CsmPermission/post"
					var savedata = {
							name : this.permissionName,
							permission : this.permissionValue,
	  				};
	  				ajaxcreate(savedata, url);
	  				  store.commit('permission');
	  				  this.permissionName="";
	  				  this.permissionValue="";
	  				$("#myPermissionModal").modal('hide');
				}else{
					alert("Please enter permission name ");
				};
             }else{
          	   if(this.permissionName.trim().length>0){
    				var url = ctx + "/e/CsmPermission/put/" + this.permissionId;
  				var putdata = {
  					name : this.permissionName,
  					permission : this.permissionValue,
  			    				}
  				ajaxPut(putdata, url);
					store.commit('permission');
	  				  this.permissionName="";
	  				  this.permissionValue="";
	  				  this.permissionId="";
	  				$("#myPermissionModal").modal('hide');
          	   }else{
          		   alert("Please enter permission name ");  
          	   }
          	   
             }
			},
			del:function(id){
				let url = ctx+ "/e/CsmRolePermission/find";
			    let checkKey = {
			    		ff_permissionId:id,
			    };
		    	let result = ajaxFind(checkKey, url);
		    	if (result.data.totalElements!=0){
		    		alert("some role use this permission,you can't delete it");
		    	}else{
				var conf=confirm("are you sure?");
				if (conf){
					url=ctx+ "/e/CsmPermission/delete/" + id;
					$.ajax({
						type : "GET",
						url : url,
						async : false,
						success : function(result) {
							alert("DELETE OK ");
						},
						timeout : 3000,
						error :handleError
					});
					store.commit('permission');
				}
		      }
			},
			mod:function(id,name,per){
				this.permissionName=name;
				this.permissionValue=per;
				this.permissionId=id;
				$("#myPermissionModal").modal('show');
					
			},
			}
		};


const role={
		  template:'#role',

		  created:function(){
			  store.commit('role');
			  
		  },
		  computed:{
			  roles:function(){
				  return this.$store.state.roles;
			  },

		  },
		methods:{
			addNew:function(){
				var name=prompt("please enter a Role:"," ");
				if (name.trim().length>0){
				var url=ctx+"/e/CsmRole/post"
				var savedata = {
						name:name
				};

				ajaxcreate(savedata, url);
				  store.commit('role');
				}
			},
			del:function(id){
				var conf=confirm("are you sure?");
				if (conf){
					url=ctx+ "/e/CsmRole/delete/" + id;
					$.ajax({
						type : "GET",
						url : url,
						async : false,
						success : function(result) {
							alert("DELETE OK ");
						},
						timeout : 3000,
						error :handleError
					});
					store.commit('role');
				}
			},
			mod:function(id,per){
				var name=prompt("input:",per);
					if (name.trim().length>0){
				var url = ctx + "/e/CsmRole/put/" + id;
				var putdata = {
					name : name,
			    				}
				ajaxPut(putdata, url);
					store.commit('role');
					}
					
			},
			}
		};

const role_permission={
	template:"#role_permission",
	  created:function(){
		  store.commit('permission');
		  store.commit('role_permissions',{id:this.$route.query.id})
	  },
	  
	  computed:{
		  permissions:function(){
			  return this.$store.state.permissions;
		  },
		  role_permissions:function(){
			 return this.$store.state.role_permissions; 
		  }
	  },
	  methods:{
		  add:function(id){
			  var each=true;
			  $.each(this.$store.state.role_permissions,function(i,n){
				 if (n.permissionId==id){
					 alert("you already have this permission");
					 each=false;
					 return false;
				 }
			  });
			  if(each){
					var url=ctx+"/e/CsmRolePermission/post";
					var savedata = {
							roleId:this.$route.query.id,
							permissionId:id,
	    				};
	    				ajaxcreate(savedata, url);
	    				store.commit('role_permissions',{id:this.$route.query.id});
			  };

		  },
		  remove:function(id){
				url=ctx+ "/e/CsmRolePermission/delete/" + id;
				$.ajax({
					type : "GET",
					url : url,
					async : false,
					success : function(result) {
						alert("DELETE OK ");
					},
					timeout : 3000,
					error :handleError
				});
				store.commit('role_permissions',{id:this.$route.query.id});
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
				  groupId:"",
				  groupText:"",
				  sele:"",
				  groupOptions: [ ],
				  checked:false, 
			  }
		  },
		  created:function(){
			  store.commit('groups');
			  
		  },
		  computed:{
			  groups:function(){
				  return this.$store.state.groups;
			  },

		  },
		methods:{

			addNew:function(){
		    	this.groupOptions = this.$store.state.groups;
				this.groupText="";
				this.checked=false;
				this.sele="";

				$("#myModal").modal('show');
				},
			

			addNewSubmit:function(){
				if(this.groupId.length==0){
					if(this.groupText.length==0){
					alert('Please enter group name');
					}else{
						var url=ctx+"/e/CsmUserGroup/post";
						var savedata = {
							name:this.groupText,
							admin:this.checked,
							superId:this.sele,
							};

						ajaxcreate(savedata, url);
						this.groupText="";
						this.checked=false;
						this.sele="";
						this.groupOptions= [];
						$("#myModal").modal('hide')
						store.commit('groups');
					}
				}
				if(this.groupId.length>0){
					 var url = ctx + "/e/CsmUserGroup/put/" + this.groupId;
					 var putdata = {
								name:this.groupText,
								admin:this.checked,
								superId:this.sele,
					 }
					 ajaxPut(putdata, url);
					$("#myModal").modal('hide')
						this.groupText="";
						this.checked=false;
						this.sele="";
						this.groupOptions= [];
					store.commit('groups');
				}
			},

			del:function(id,name){
				if(name.trim()=="System"){
					alert("you can't delete it")
				}else{
					let url = ctx+ "/e/CsmUser/find";
					let checkKey = {
			    		ff_userGroupId:id,
					};
					let result = ajaxFind(checkKey, url);
					 url = ctx+ "/e/CsmUserGroup/find";
					 checkKey = {
			    		ff_superId:id,
					};
					let result1 = ajaxFind(checkKey, url);
					if (result.data.totalElements!=0 || result1.data.totalElements!=0){
						alert("some user use this group,you can't delete it");
					}else{
						var conf=confirm("are you sure?");
						if (conf){
							url=ctx+ "/e/CsmUserGroup/delete/" + id;
							$.ajax({
								type : "GET",
								url : url,
								async : false,
								success : function(result) {
									alert("DELETE OK ");
								},
								timeout : 3000,
								error :handleError
							});
							store.commit('groups');
						}
					}
				}
			},
			mod:function(id,name,admin,superId){
		    	this.groupOptions = this.$store.state.groups;
				this.groupText=name;
				this.checked=admin;
				this.sele=superId;
				this.groupId=id;
				$("#myModal").modal('show');
				
				

					
			},
			}
		};

const group_role={
		template:"#group_role",

		  created:function(){
			  store.commit('role');
			  store.commit('group_role',{id:this.$route.query.id})
		  },
		  
		  computed:{
			  roles:function(){
				  return this.$store.state.roles;
			  },
			  group_roles:function(){
				  return this.$store.state.group_roles;
			  },
		  },
		  methods:{
			  add:function(id){
				  var each=true;
				  $.each(this.$store.state.group_roles,function(i,n){
					 if (n.roleId==id){
						 alert("you already have this role");
						 each=false;
						 return false;
					 }
				  });
				  if(each){
						var url=ctx+"/e/CsmUserGroupRole/post";
						var savedata = {
								userGroupId:this.$route.query.id,
								roleId:id,
		    				};
		    				ajaxcreate(savedata, url);
		    				store.commit('group_role',{id:this.$route.query.id});
				  };

			  },
			  remove:function(id){
					url=ctx+ "/e/CsmUserGroupRole/delete/" + id;
					$.ajax({
						type : "GET",
						url : url,
						async : false,
						success : function(result) {
							alert("DELETE OK ");
						},
						timeout : 3000,
						error :handleError
					});
					store.commit('group_role',{id:this.$route.query.id});
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
				  showKey:false,
				  username:"",
				  userId:"",
				  sele:"",
				  groupOptions: [ ],
				  checked:false, 
				  
			  }
		  },
		  created:function(){
			  store.commit('users');
			  
		  },
		  computed:{
			  users:function(){
				  return this.$store.state.users;
			  },

		  },
		methods:{

			addNew:function(){
				store.commit('groups');
		    	this.groupOptions = this.$store.state.groups;
				this.showKey=false;
				this.username="";
				this.checked=false;
				this.sele="";
				this.userId="";
				$("#myModal1").modal('show');
				},
			

			addNewSubmit:function(){
				if(this.userId.length==0){
					if(this.username.length==0||this.sele.length==0){
					alert('Please enter name and select group');
					}else{
						var url=ctx+"/e/CsmUser/post";
						var savedata = {
							username:this.username,
							active:true,
							userGroupId:this.sele,
							plainPassword:plainPassword,
							};

						ajaxcreate(savedata, url);
						this.username="";
						this.sele="";
						this.groupOptions= [];
						$("#myModal1").modal('hide')
						store.commit('users');
					}
				}
				if(this.userId.length>0){
					 var url = ctx + "/e/CsmUser/put/" + this.userId;
					 var putdata = {
								active:this.checked,
								userGroupId:this.sele,
					 }
					 ajaxPut(putdata, url);
					$("#myModal").modal('hide')
						this.username="";
						this.checked=false;
						this.sele="";
						this.groupOptions= [];
						$("#myModal1").modal('hide')
					store.commit('users');
				}
			},

			del:function(id){
						var conf=confirm("are you sure?");
						if (conf){
							url=ctx+ "/e/CsmUser/delete/" + id;
							$.ajax({
								type : "GET",
								url : url,
								async : false,
								success : function(result) {
									alert("DELETE OK ");
								},
								timeout : 3000,
								error :handleError
							});
							store.commit('users');
					}

			},
			mod:function(id,name,active,groupId){
				store.commit('groups');
		    	this.groupOptions = this.$store.state.groups;
				this.username=name;
				this.checked=active;
				this.sele=groupId;
				this.userId=id;
				this.showKey=true;
				$("#myModal1").modal('show');
			},
			}
		};

const
faqMain = {
	template : '#faqMain',
};

const faq = {
		template : '#FaqCategory',
		computed:{
			list :function() {
				return this.$store.state.CategoryList
			}
		},

	    created:function(){
	    	store.commit('getCategory');
	    },
	};

const showFaqList = {
		template : '#FaqDetail',
		watch:{
			'$route':function(to,from){
				var id=this.$route.query.id;
		    	$("#"+id).css("color","red");
				this.$store.state.curPage=1;
		    	store.commit('getFaq',{id : id});
			}
		},
		computed:{
			list1:function() {
				return this.$store.state.FaqList
			}
		},
	    created:function(){
	    	var id=this.$route.query.id;
	    	$("#"+id).css("color","red");
            store.commit('getFaq',{id : id});
	    },

	    methods:{
	    	faqAddNewUi:function(){
	    		router.push({path:'/faq/faqAddNew',query:{id:this.$route.query.id}})
	    	}
	    }
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
	    		let url1 = url + "CsmFaq" + "/post";
	    		if ($.trim(this.question).length == 0) {
	    			alert("Please input question");
	    		} else {
	    			var fileData = $("input[name='uploadFile']").get(0);
	    			if (fileData.files.length == 0) {
	    				var savedata = {
	    					categoryId : id,
	    					question : this.question,

	    					answer : this.answer,
	    					active : "1",
	    					createAccountId : userId,
	    					createDateTime : getNowFormatDate(),
	    					modifyAccountId : userId,
	    					modifyDateTime : getNowFormatDate(),
	    				};

	    				ajaxcreate(savedata, url1);
	    				router.push({path:'/faq/showFaqList', query:{id:id}})
	    			} else {
	    				url1 = url + "CsmFaq" + "/postUpload";
	    				var form_data = new FormData();
	    				form_data.append("categoryId", id);
	    				form_data.append("question", this.question);
	    				form_data.append("answer", this.answer);

	    				form_data.append("active", "1");
	    				form_data.append("createAccountId", userId);
	    				form_data.append("createDateTime", getNowFormatDate());
	    				form_data.append("modifyAccountId", userId);
	    				form_data.append("modifyDateTime", getNowFormatDate());
	    				for (var i = 0; i < (fileData.files.length); i++) {
	    					form_data.append("uploadFile", fileData.files[i]);
	    				}
	    				ajaxcreateUpload(form_data, url1);
	    				router.push({path:'/faq/showFaqList', query:{id:id}})

	    			}

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
			var id1=this.$route.query.id;
			var url1 = url + "CsmFaq" + "/get/" + id1;
			var checkResult1 = ajaxGet(url1);
			if (checkResult1.attachments != undefined) {
            	this.attachments = checkResult1.attachments.split(",");
			}
			this.attach = this.attachments;
			this.question = checkResult1.question;
			this.answer = checkResult1.answer;
			this.id = checkResult1.categoryId;
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
	    			alert("Please input question");
	    		} else {
	    			var fileData = $("input[name='uploadFile']").get(0);
	    			
	    			if ((fileData.files.length == 0) && (this.attach.length == 0)) {
	    				var url1 = url + "CsmFaq" + "/put/" + id1;
	    				var putdata = {
	    					question : this.question,
	    					answer : this.answer,
	    					attachments:"",
	    					modifyAccountId : userId,
	    					modifyDateTime : getNowFormatDate(),
	    				}
	    				ajaxPut(putdata, url1);
	    				router.push({path:'/faq/showFaqList', query:{id:this.id}});
	    			} else {
	    				if (this.attach.length != 0) {
	    					var temp=this.attach
	    					$.each(temp,function(i, n) {
	    						if (i == (temp.length - 1)) {
	    							attachs += n;
	    						} else {
	    							attachs += n + ",";
	    						}
	    					});
	    				}
	    				url1 = url + "CsmFaq" + "/putUpload/"+id1;
	    				var form_data = new FormData();
	    				form_data.append("question", this.question);
	    				form_data.append("answer", this.answer);
	    				form_data.append("attachments", attachs);
	    				form_data.append("modifyAccountId", userId);
	    				form_data.append("modifyDateTime", getNowFormatDate());
	    				for (var i = 0; i < (fileData.files.length); i++) {
	    					form_data.append("uploadFile", fileData.files[i]);
	    				}
	    				ajaxPutUpload(form_data, url1);
	    				router.push({path:'/faq/showFaqList', query:{id:this.id}});
	    			}
	    		}
	    	},

	    	adminFaqDelete:function() {
	    		let id1=this.$route.query.id;
	    		var url1 = url + "CsmFaq" + "/delete/" + id1;
	    		$.ajax({
	    			type : "GET",
	    			url : url1,
	    			async : false,
	    			success : function(result) {
	    				alert("DELETE OK ");
	    			},
	    			timeout : 3000,
	    			error : function(xhr) {
	    				alert("error： " + xhr.status + " " + xhr.statusText);
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
			}
		},
		computed:{
			list:function() {
				return this.$store.state.CategoryList
			}
		},
	    created:function(){
	    	store.commit('getCategory');
	    },
	    methods:{
			addNew:function(){
		    	this.name = "";
				this.description="";
				$("#categoryModal").modal('show');
				},

			addNewSubmit:function(){
				if(this.id.length==0){
					var url1 = url + "CsmFaqCategory" + "/post";
					if ($.trim(this.name).length == 0) {
						alert("Please input name");
					} else {
						var modify_date1 = getNowFormatDate();
						var savedata = {
							name : this.name,
							description :  this.description,
							active : "1",
							createAccountId : userId,
							createDateTime : modify_date1,
							modifyAccountId : userId,
							modifyDateTime : modify_date1,
						}
						ajaxcreate(savedata, url1);
					}
				};
				if(this.id.length>0){
					if ($.trim(this.name).length == 0) {
						alert("Please input name");
					} else {
						var url1 = url + "CsmFaqCategory" + "/put/" + this.id;
						var putdata = {
							name : this.name,
							description : this.describe,
							modifyAccountId : userId,
							modifyDateTime : getNowFormatDate(),
						}
						ajaxPut(putdata, url1);
					}
				};
				$("#categoryModal").modal('hide');	
		    	store.commit('getCategory');
				router.push({path: '/category'});
			
				},

				del:function(id){
					var conf=confirm("Are you sure?");
					if (conf==true){
						var url1 = url + "CsmFaq" + "/find";
						var checkKey = {
							ff_categoryId : id,
						};
						var checkResult = ajaxFind(checkKey, url1);
						if (checkResult.data.content == undefined) {
							url1 = url + "CsmFaqCategory" + "/delete/" + id;
							$.ajax({
								type : "GET",
								url : url1,
								async : false,
								success : function(result) {
									alert("DELETE OK ");
								},
								timeout : 3000,
								error : function(xhr) {
									alert("error： " + xhr.status + " " + xhr.statusText);
								},
							});
							store.commit('getCategory');
						} else {
							alert("you can't delete this category");
						}
					}
				},
				mod:function(id,name,description){
			    	this.id = id;
					this.name=name;
					this.description=description;
					$("#categoryModal").modal('show');
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
	],
});



new Vue({
	store : store,
	router : router,
	el : '#wrapper',
});



function connect1() {
	var socket = new SockJS('/csm/ws');
	stompClient = Stomp.over(socket);
	var headers = {
		admin : true
	};

	stompClient.connect(headers, function(frame) {
		subscribeList = [];
		stompClient.subscribe('/app/chatAInit', function(message) {
			showCustomerSet(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/chat/login', function(message) {
			showOnlineCustomer(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/chat/logout', function(message) {
			removeOnlineCustomer(JSON.parse(message.body));
		});
		stompClient.subscribe('/topic/chat/addUnhandledCustomer',
				function(message) {
					showUnhandledCustomer(JSON.parse(message.body));
				});
		stompClient.subscribe('/topic/chat/removeUnhandledCustomer',
				function(message) {
					removeUnhandledCustomer(JSON.parse(message.body));
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
	var customerName = customer.userId;
	store.state.customers.push(customer);
	if (customer.online) {
		setTimeout(function(){showOnlineCustomer(customer);}, 200);
	}
	if (customer.unhandled) {
		setTimeout(function(){showUnhandledCustomer(customer);}, 200);
	}
}



function showOnlineCustomer(customer) {
	$("#" + customer.userId).css('color', 'red');
}

function removeOnlineCustomer(customer) {
	$("#" + customer.userId).css('color', '#838383');
}

var intervalMap = new Map();

function showUnhandledCustomer(customer) {
	var userId = customer.userId;
	if (!intervalMap.has(userId)) {
		var interval = setInterval(function() {
			$("#" + userId).fadeOut(100).fadeIn(100);
		}, 200);
		intervalMap.set(userId, interval);
	}
}

function removeUnhandledCustomer(customer) {
	var roomId = customer.userId;
	if(intervalMap.has(roomId)){
		clearInterval(intervalMap.get(roomId));
		intervalMap.delete(roomId);
	}
}

function showMsg(customerId){
	$("#sentence").empty();

	for (var i = 0; i < subscribeList.length; i++) {
		subscribeList[i].unsubscribe();
	}
	subscribeList = [];
	var subApp = stompClient.subscribe('/app/chatAInitMessage/'
			+ customerId, function(message) {
		showMessages(JSON.parse(message.body));
	});
	var subTopic = stompClient.subscribe('/topic/chat/message/'
			+ customerId, function(message) {
		showMessage(JSON.parse(message.body));
		document.getElementById ( 'sentence').scrollTop=document.getElementById ( 'sentence').scrollHeight ;
	});
	subscribeList.push(subApp);
	subscribeList.push(subTopic);
}

function showMessages(messageList) {
	for (var i = 0; i < messageList.length; i++) {
		showMessage(messageList[i]);
	}
    document.getElementById ( 'sentence').scrollTop=document.getElementById ( 'sentence').scrollHeight ;  
}

function showMessage(message) {
	var timeStr="";
	var num=message.createDateTime.indexOf("T");
	timeStr=message.createDateTime.substring(0,num)+" "+message.createDateTime.substring(num+1,19);
	if (message.fromAdmin){
	$("#sentence").append("<div class='panel panel-primary' style='clear:both;float:right;width:600px'><div class='panel-heading' style='padding: 2px 0px 2px 300px' >"+message.senderName+"</div><div class='panel-body'>"+message.message+" </div><div class='panel-footer' style='padding: 2px 0px 2px 250px' >"+timeStr+"</div></div>"
);
	}else{
		$("#sentence").append("<div class='panel panel-info' style='clear:both;float:left;width:600px'><div class='panel-heading' style='padding: 2px 0px ' >"+message.senderName+"</div><div class='panel-body'>"+message.message+" </div><div class='panel-footer' style='padding:2px 0px' >"+timeStr+"</div></div>"
		);
	}

}

function ajaxcreate(savedata, url1) {
	$.ajax({
		type : "POST",
		url : url1,
		data : savedata,
		async : false,
		success : function(result) {
			alert("ADD NEW SUCCESS");
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
		async : false,
		success : function(result) {
			if (result.data.content == undefined) {
				// alert("No Result");
			} 
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
		async : false,
		success : function(result) {
			alert("PUT OK ");
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
		success : function(result) {
			if (result.data == undefined) {
				alert("No Result");
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
		window.location.replace("/csm" + xhr.statusText);
	}else{
		alert(" error： " + xhr.status + " " + xhr.statusText);
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
			alert("ADD NEW SUCCESS");
		},
		timeout : 3000,
		error : function(xhr) {
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

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
			alert("PUT OK ");
		},
		timeout : 3000,
		error : function(xhr) {
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

	});
}