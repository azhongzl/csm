var userName = "user1";

const store = new Vuex.Store({
	  state: {
		  customers:[
		             { name:'customer1'},
		             { name:'customer2'},
		             { name:'customer3'}
		             ],
		  service:[
		   		     { name:'service group1',department:'IT'},
		   		     { name:'service group2',department:'IT'},
		   		     { name:'service group3',department:'IT'},
		   		
		   	             
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
	  computed:{
		  service(){
			  return this.$store.state.service;
		  }
	  },

	methods:{
		showUser(name,department,index){
 	  alert(name+"          "+department+"          "+index);
		},
		}
	};

const content={
		  template:'#content',
			watch:{
				'$route'(to,from){
				let id=	this.$route.params.id;
				alert(id);
				}
			},
			created:function(){
				alert(this.$route.params.id);
			},
		  computed:{
			  
		  },

		methods:{
				send(sentence){
					alert(sentence);
				}
			}
		};


const router = new VueRouter({
	  mode: 'history',
	  routes: [
		         
		 	{ path: '/', 
			    	name : 'home',
					components:{
					   	customer : customer,
					   	service : service
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
		  ],
		  
		});



new Vue({
	  store,
	  router,
	  el:'#main',
	});


function test1(){
	alert();
}
