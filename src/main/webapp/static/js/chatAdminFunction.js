var ctx;
var stompClient = null;
var subscribeList ;
var plainPassword="kuzco123";
$(function() {

	connect1();

	router.push({
			name : 'home'
		});
});

const store = new Vuex.Store({
	  state: {
		  customers:[ ],
		  service:[
		   		     { name:'service group1',department:'IT',customer:"any"},
		   		  ],
		 serviceList:[
					 { name:'service group1',department:'IT',custormer:""},	
					 { name:'service group2',department:'IT',custormer:""},
					 { name:'service group3',department:'IT',custormer:""},
				   	 { name:'service group4',department:'IT',custormer:""},
				   	 { name:'service group5',department:'IT',custormer:""},
				   	 { name:'service group6',department:'IT',custormer:""},
				   	 { name:'service group7',department:'IT',custormer:""},
				   	 { name:'service group8',department:'IT',custormer:""},
			   	 
				   ],
		  permissions:[],
		  roles:[],
		  role_permissions:[],
		  groups:[],
		  group_roles:[],
		  users:[],
	  },
	  mutations: {
			  addUser:function(state,payload) {
			  state.service.push(payload.id);
		    },
			  removeUser:function(state,payload) {
		    	state.service.splice(payload.id,1);
			    },
			  permission:function(state) {
				   	let url = ctx+ "/e/CsmPermission/find";
			    	let checkKey = "";
		    	let result = ajaxFind(checkKey, url);
		    	if (result.data.content!=undefined){
			    	state.permissions = result.data.content;    		
		    	}
				},
			  role:function(state) {
					let url = ctx+ "/e/CsmRole/find";
				    let checkKey = "";
			    	let result = ajaxFind(checkKey, url);
			    	if (result.data.content!=undefined){
				    	state.roles = result.data.content;    		
			    	}
					},
				role_permissions:function(state,payload){
					var temp;
					let url = ctx+ "/e/CsmRolePermission/find";
				    let checkKey = {
				    		ff_roleId:payload.id,
				    };
			    	let result = ajaxFind(checkKey, url);

			    	if (result.data.content!=undefined){
			    		temp=result.data.content;
				    	$.each(temp,function(i,n){
							url= ctx + "/e/CsmPermission/get/" + n.permissionId;
							var result = ajaxGet(url);
							n.permissionName=result.name;
				    	});
				    	state.role_permissions = temp;
			    	}
				},
				  groups:function(state) {
						let url = ctx+ "/e/CsmUserGroup/find";
					    let checkKey = "";
				    	let result = ajaxFind(checkKey, url);
				    	if (result.data.content!=undefined){
					    	state.groups = result.data.content;    		
				    	}
					},
					
					group_role:function(state,payload){
						var temp;
						let url = ctx+ "/e/CsmUserGroupRole/find";
					    let checkKey = {
					    		ff_userGroupId:payload.id,
					    };
				    	let result = ajaxFind(checkKey, url);

				    	if (result.data.content!=undefined){
				    		temp=result.data.content;
					    	$.each(temp,function(i,n){
								url= ctx + "/e/CsmRole/get/" + n.roleId;
								var result = ajaxGet(url);
								n.roleName=result.name;
					    	});
					    	state.group_roles = temp;
				    	}
					},
					
					  users:function(state) {
							let url = ctx+ "/e/CsmUser/find";
						    let checkKey = "";
					    	let result = ajaxFind(checkKey, url);
					    	if (result.data.content!=undefined){
						    	state.users = result.data.content;    		
					    	}
						},
	  			}
	});


const customer={
	  template:'#customer',
	  computed:{
		  customers:function(){
			  return this.$store.state.customers;
		  }
	  },

	methods:{

	}
	};


const service={
	  template:'#service',
	  data: function(){
		  return{
			  selected:"",  
		  }
	  },
	  computed:{
		  service:function(){
			  let id = this.$route.params.id;
			  let service1 = [];
			  let service2 = this.$store.state.service;
			  $.each(service2,function(i,n){
				  if(n.customer==id ||n.customer=="any" ){
					  service1.push(n);
				  }
			  });
			  return service1;
		  },
		  serviceList:function(){
			  return this.$store.state.serviceList;
		  }
	  },
	methods:{
		showUser:function(name,index){
			store.commit('removeUser',{id:index});
			
		},
		select:function(res){
			let id=	this.$route.params.id;
			res.customer = id;
		   	store.commit('addUser',{id:res});
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
					let customerId=	this.$route.params.id;
					this.userName=this.$route.query.name;
						showMsg(customerId);
					}
			},
			created:function(){
				let customerId=	this.$route.params.id;
				this.userName=this.$route.query.name;
				showMsg(customerId);
			},
		  computed:{
		  },

		methods:{
				send:function(){
					if(this.sentence.length>0){
						let customerId=	this.$route.params.id;
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
			  router.push({name:'role'});
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
				  router.push({name:'group'});
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

const router = new VueRouter({

	  routes: [
		 	{ path: '/home', 
			    	name :'home',
					components:{
					 customer : customer,
					        	},
			},
		 	{ path: '/content:id', 
		    	name : 'content',
				components:{
				   	customer : customer,
				   	service : service,
				   	content : content
				        	},
		 	},
		 	{ path: '/permission', 
		    	name :'permission',
				components:{
				   	customer : customer,
				   	content : permission,
				        	},
		},
		 { path: '/role', 
		      name :'role',
		      components:{
		      customer : customer,
			content : role,
				        	},
		},
		{ path: '/role_permission', 
	    	name :'role_permission',
	    	components:{
		   	customer : customer,
		   	content : role_permission,
		        	},
	},
	 { path: '/group', 
	      name :'group',
	      components:{
	      customer : customer,
		content : group,
			        	},
	},
	{ path: '/group_role', 
    	name :'group_role',
    	components:{
	   	customer : customer,
	   	content : group_role,
	        	},
},
{ path: '/user', 
    name :'user',
    components:{
    customer : customer,
	content : user,
		        	},
},

			  ],
		  
		});


new Vue({
	  store:store,
	  router:router,
	  el:'#main',
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
	$("#sentence").append("<p class='user1 right'>" + message.senderName+"</p></br>"+"<p class='speech1 right'>"+ message.message +"&nbsp;&nbsp;&nbsp;&nbsp;" + timeStr+"</p><hr>");
	}else{
		$("#sentence").append("<p class='user2 left'>" + message.senderName+"</p></br>"+"<p class='speech2 left'>"+ message.message +"&nbsp;&nbsp;&nbsp;&nbsp;" + timeStr+"</p><hr>");
	
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
