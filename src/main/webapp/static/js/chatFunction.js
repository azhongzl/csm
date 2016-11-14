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
		   		  
		   		  
	  },
	  mutations: {
			  addUser(state,payload) {
			  state.service.push(payload.id);
		    },
			  removeUser(state,payload) {
		    	state.service.splice(payload.id,1);
			    },
			 closeChat(state,payload) {
			    state.customers.splice(payload.index,1);
			    
  
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
		closeChat(index,name){	
			router.push({name: 'home'});
			store.commit('closeChat',{index:index,name:name});
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
		  serviceList(){
			  return this.$store.state.serviceList;
		  }
	  },

	methods:{
		showUser(name,index){
		   	store.commit('removeUser',{id:index});
			
		},
		
		select(res){
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
					$("#sentence").append ("<div>"+userName+" : "+"<p class='speech'>"+res+"</p></div>");
					this.sentence="";	
				}	
			}
		  }
		};


const router = new VueRouter({
	  mode: 'history',
	  routes: [
		 	{ path: '/csm/admin/chat/home', 
			    	name : 'home',
					components:{
					   	customer : customer,

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


