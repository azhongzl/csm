const store = new Vuex.Store({
	  state: {
		  customerName:"",
		  customerId:"",
		  customers:[],
		  service:[],
		  serviceList:[],
		  currentUserGroup:[],
		  customerUserGroupList:[],
		  pageSize: 10,
		  curPage: 1,
		  total:0,
		  totalPage:0,

	  },
	  mutations: {
						  getService:function(state,payload) {

							   	let url1 = ctx + "/admin/chat/listCustomerUserGroups/"+payload.id;
						    	let checkKey = {};
						    	let result = ajaxFind(checkKey, url1);
							    	state.service = result.data.customerUserGroupList;
									state.serviceList=result.data.userGroupList;
						    },
							  getCustomerName:function(state,payload) {
								  		state.customerId=payload.id;
										state.customerName=payload.name;
							    },

	  			}
	});