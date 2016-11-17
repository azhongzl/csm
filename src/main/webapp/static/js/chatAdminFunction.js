
var stompClient = null;
var subscribeList ;
//$(document).ready(function() {
//	connect1();
//router.push({
//		name : 'home'
//	});
//
//});


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
		   		  
		   		  
	  },
	  mutations: {
			  addUser:function(state,payload) {
			  state.service.push(payload.id);
		    },
			  removeUser:function(state,payload) {
		    	state.service.splice(payload.id,1);
			    },
			 closeChat:function(state,payload) {
			    state.customers.splice(payload.index,1);
			    
  
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
		closeChat:function(index,name){	
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
					let customerId=	this.$route.params.id;
					stompClient.send('/app/chatASendMessage', {}, JSON.stringify({
						'message' : this.sentence,
						'roomId' : customerId,
					}));
					this.sentence="";
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
	if (message.fromAdmin){
	$("#sentence").append("<p class='user1 right'>" + message.senderName+"</p></br>"+"<p class='speech1 right'>"+ message.message +"&nbsp;&nbsp;&nbsp;&nbsp;" + message.createDateTime+"</p><hr>");
	}else{
		$("#sentence").append("<p class='user2 left'>" + message.senderName+"</p></br>"+"<p class='speech2 left'>"+ message.message +"&nbsp;&nbsp;&nbsp;&nbsp;" + message.createDateTime+"</p><hr>");
	
	}

	
	}

