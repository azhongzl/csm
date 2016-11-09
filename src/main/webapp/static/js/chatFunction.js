var userName = "user1";
var ctx="";
const store = new Vuex.Store({
	  state: {
		  customers:[
		             { name:'customer1'},
		             { name:'customer2'},
		             { name:'customer3'}
		             ],
		  service:[
		   		     { name:'service group1',department:'IT'},
		   		  ],
		 serviceList:[
					{ name:'service group2',department:'IT'},
					{ name:'service group3',department:'IT'},
				   	 { name:'service group4',department:'IT'},
				   	 { name:'service group5',department:'IT'},
				   	 { name:'service group6',department:'IT'},
				   	 { name:'service group7',department:'IT'},
				   	 { name:'service group8',department:'IT'},
				   	 { name:'service group9',department:'IT'},				   	 
				   ],
		   		  
		   		  
	  },
	  mutations: {
			  addUser(state,payload) {
              state.chatUser.push(payload);
              state.chatUser1.splice(payload.index,1)
		    },
			  removeUser(state,payload) {
		    	state.chatUser.splice(payload.index,1)
		    	state.chatUser1.push(payload);

			    },
	  }
	});



const customer={
	  template:'#customer',
	  computed:{
		  customers(){
			  return this.$store.state.customers;
		  }
	  },

	methods:{
		showUser(name){
	     alert(name);
		}
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
		  service(){
			  return this.$store.state.service;
		  },
		  serviceList(){
			  return this.$store.state.serviceList;
		  }
	  },

	methods:{
		showUser(name,department,index){
			alert(name+"          "+department+"          "+index);
		},
		
		test(res){
			this.$store.state.service.push(res);
		},
		
		}
	};

const content={
		  template:'#content',
		  data:function(){
			return {
				sentence:""
					}; 
		  },
			watch:{
				'$route'(to,from){
				let id=	this.$route.params.id;
				userName=id;
				$("#sentence").empty();
				}
			},
			created:function(){
				let id=	this.$route.params.id;
				userName=id;
				$("#sentence").empty();
			},
		  computed:{
			  
		  },

		methods:{
				send(res){
				if (res.length>0){
					$("#sentence").append (userName+":"+"<p>"+res+"<p>");
					this.sentence="";	
				}	
			}
			}
		};


const router = new VueRouter({
	  mode: 'history',
	  routes: [
		 	{ path: '/csm/admin/', 
			    	name : 'home',
					components:{
					   	customer : customer,
					   	service : service
					        	},
			},
		 	{ path: '/csm/admin/chat/content:id', 
		    	name : 'content',
				components:{
				   	customer : customer,
				   	service : service,
				   	content : content
				        	},
		 	},
		  ],
		  
		});



new Vue({
	  store,
	  router,
	  el:'#main',
	});


