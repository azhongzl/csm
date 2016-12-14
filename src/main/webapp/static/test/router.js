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


