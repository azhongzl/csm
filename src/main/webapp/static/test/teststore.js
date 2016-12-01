const store = new Vuex.Store({
	  state: {
		  customers:[],
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
		  CategoryList: [],
		  FaqList:[],
		  pageSize: 4,
		  curPage: 1,
		  total:0,
		  totalPage:0,
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
						  getCategory:function(state) {
							   	let url1 = url + "CsmFaqCategory" + "/find";
						    	let checkKey = "";
						    	let result = ajaxFind(checkKey, url1);
						    	state.CategoryList = result.data.content;
						    },
						  getFaq:function(state,payload) {
								let url1 = url + "CsmFaq" + "/find";
								let checkKey = {
									ff_categoryId : payload.id,
									page_size : state.pageSize,
									page_no : state.curPage,
								};
						    	let result = ajaxFind(checkKey, url1);
								if (result.data.content !== undefined) {
							    	state.FaqList = result.data.content;
									state.total = result.data.totalElements;
									state.totalPage = result.data.totalPages;
								}else{
									state.FaqList =[];
									state.total = 0;
									state.totalPage = 0;
								}
							    },
	  			}
	});