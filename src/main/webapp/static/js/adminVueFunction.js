var url = "http://localhost:8080/csm/e/";
var userId = "";
var userName = "";


const store = new Vuex.Store({
	  state: {
	    CategoryList: [],
	    FaqList:[],
	    pageSize: 5,
	    curPage: 1,
	    total:0,
	    totalPage:0,
	  },
	  mutations: {
		  getCategory:state=> {
		   	let url1 = url + "Category" + "/find";
	    	let checkKey = "";
	    	let result = ajaxFind(checkKey, url1);
	    	state.CategoryList = result.data.content;
	    },
		  getFaq(state,payload) {
			let url1 = url + "Faq" + "/find";
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

const category = {
	template : '#category',
	data : function(){
		return{ 
			picked:""
		}
	},
	computed:{
		list () {
			return this.$store.state.CategoryList
		}
	},
    created:function(){
    	store.commit('getCategory');
    },
};


const addNew = {
		template : '#addNew',
		data : function(){
			return{ 
			     name:"",
			     describe:"",
			     modify_account_id:userId,
			     modify_date:getNowFormatDate()
			}
		},
		methods: {
			addNew(){
				var url1 = url + "Category" + "/post";
				if ($.trim(this.name).length == 0) {
					alert("Please input name");
				} else {
					var modify_date1 = getNowFormatDate();
					var savedata = {
						name : this.name,
						description :  this.describe,
						active : "1",
						createAccountId : userId,
						createDate : modify_date1,
						modifyAccountId : userId,
						modifyDate : modify_date1,
					}
					alert(url1);
					ajaxcreate(savedata, url1);
					store.commit('getCategory');
					router.push({name : 'category'});
				}
			},
			reset(){
	
				router.push({name : 'category'});

			},
			},
	};

const modify = {
		template : '#addNew',
		data: function(){
			return{
				name : "",
				describe : ""
			}
		},
		created: function(){
			var id = this.$route.query.id;
			if (id.trim().length !== 0) {
				var url1 = url + "Category" + "/get/" + id;
				var checkResult1 = ajaxGet(url1);

				if (checkResult1.description == undefined) {
					checkResult1.description = "";
				};
				
					this.name = checkResult1.name;
					this.describe = checkResult1.description;
				
			} else {
				alert("Please select category");
				router.push({name: 'category'});
			}
		},
		methods: {
			addNew(){
				var id = this.$route.query.id;
				if ($.trim(this.name).length == 0) {
					alert("Please input name");
				} else {
					var url1 = url + "Category" + "/put/" + id;
					var putdata = {
						name : this.name,
						description : this.describe,
						modifyAccountId : userId,
						modifyDate : getNowFormatDate(),
					}
					ajaxPut(putdata, url1);
			    	store.commit('getCategory');
					router.push({name: 'category'});
					
				}
			},
			reset(){
				router.push({name: 'category'});

			},
			},
	};


const categoryDelete = {
		template : '<div>delete function</div>',
		created: function(){
			   
				var id = this.$route.query.id;
				if (id.trim().length !== 0) {
					var r=confirm("Are you sure?");
					if (r==true){
						var url1 = url + "Faq" + "/find";
						var checkKey = {
							ff_categoryId : id,
						};
						var checkResult = ajaxFind(checkKey, url1);
						if (checkResult.data.content == undefined) {
							url1 = url + "Category" + "/delete/" + id;
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
				} else {
					alert("Please select category ");
				};
				router.push({name: 'category'});
		},
	
	};

const faq = {
		template : '#FaqCategory',
		computed:{
			list () {
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
			'$route'(to,from){
				var id=this.$route.params.id;
				this.$store.state.curPage=1;
		    	store.commit('getFaq',{id : id});
		    	
			}
		},
		computed:{
			list1 () {
				return this.$store.state.FaqList
			}
		},
	    created:function(){
	    	var id=this.$route.params.id;
	    	store.commit('getFaq',{id : id});
	    },
	    methods:{
	    	faqAddNewUi(){
	    		router.push({name:'faqAddNew',query:{id:this.$route.params.id}})
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
			var id=this.$route.params.id;
			var url1 = url + "Faq" + "/get/" + id;
			var checkResult1 = ajaxGet(url1);
			if (checkResult1.attachments !== undefined) {
            	this.attachments = checkResult1.attachments.split(",");
			}
			this.attach = this.attachments;
			this.question = checkResult1.question;
			this.answer = checkResult1.answer;
			this.id = checkResult1.categoryId;
		},
		methods:{
		    showUploadFile(){
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
	    	adminFaqPut(){
	    		let id1=this.$route.params.id;
	    		let attachments="";
	    		if (this.question.trim().length == 0) {
	    			alert("Please input question");
	    		} else {
	    			var fileData = $("input[name='uploadFile']").get(0);
	    			
	    			if ((fileData.files.length == 0) && (this.attach.length == 0)) {
	    				var url1 = url + "Faq" + "/put/" + id1;
	    				var putdata = {
	    					question : this.question,
	    					answer : this.answer,
	    					modifyAccountId : userId,
	    					modifyDate : getNowFormatDate(),
	    				}
	    				ajaxPut(putdata, url1);
	    				router.push({name:'showFaqList', params:{id:this.id}});
	    			} else {
	    				if (this.attach.length != 0) {
	    					var temp=this.attach
	    					$.each(temp,function(i, n) {
	    						if (i == (temp.length - 1)) {
	    							attachments += n;
	    						} else {
	    							attachments += n + ",";
	    						}
	    					});
	    				}
	    				url1 = url + "Faq" + "/putUpload/"+id1;
	    				var form_data = new FormData();
	    				form_data.append("question", this.question);
	    				form_data.append("answer", this.answer);
	    				form_data.append("attachments", attachments);
	    				form_data.append("modifyAccountId", userId);
	    				form_data.append("modifyDate", getNowFormatDate());
	    				for (var i = 0; i < (fileData.files.length); i++) {
	    					form_data.append("uploadFile", fileData.files[i]);
	    				}
	    				ajaxPutUpload(form_data, url1);
	    				router.push({name:'showFaqList', params:{id:this.id}});
	    			}
	    		}
	    	},

	    	adminFaqDelete() {
	    		let id1=this.$route.params.id;
	    		var url1 = url + "Faq" + "/delete/" + id1;
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

				router.push({name:'showFaqList', params:{id:this.id}});
	    	},
	    	faqCancel(){
				router.push({name:'showFaqList', params:{id:this.id}});
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
	    	faqAddNew(){
	    		let id=this.$route.query.id;
	    		let url1 = url + "Faq" + "/post";
	    	
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
	    					createDate : getNowFormatDate(),
	    					modifyAccountId : userId,
	    					modifyDate : getNowFormatDate(),
	    				};

	    				ajaxcreate(savedata, url1);
	    				router.push({name:'showFaqList', params:{id:id}})
	    			} else {
	    				url1 = url + "Faq" + "/postUpload";
	    				var form_data = new FormData();
	    				form_data.append("categoryId", id);
	    				form_data.append("question", this.question);
	    				form_data.append("answer", this.answer);

	    				form_data.append("active", "1");
	    				form_data.append("createAccountId", userId);
	    				form_data.append("createDate", getNowFormatDate());
	    				form_data.append("modifyAccountId", userId);
	    				form_data.append("modifyDate", getNowFormatDate());
	    				for (var i = 0; i < (fileData.files.length); i++) {
	    					form_data.append("uploadFile", fileData.files[i]);
	    				}
	    				ajaxcreateUpload(form_data, url1);
	    				router.push({name:'showFaqList', params:{id:id}})

	    			}

	    		}
		    	},
	    	faqCancel(){
	    		router.push({name:'faq'})
	    	},
		    showUploadFile(){
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
			'$route'(to,from){
			this.curPage = this.$store.state.curPage;
			this.totalPage = this.$store.state.totalPage;
			this.showKey ="this.$store.state.total==0 ? 'false':'true'";
			}
		},
        computed:{
    	    curPage(){
    	    	return this.$store.state.curPage
     	    },
    	    totalPage(){
    	    	return this.$store.state.totalPage
    	    },
    	    showKey(){
    	    	if  (this.$store.state.total==0){
    	    		return false
    	    	}else{
    	    		return true
    	    	}
    	    	
    	    },
        },
        methods:{
         	changePage(page){
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
		
}


const router = new VueRouter({
  mode: 'history',
	routes: [
	         
	 	{ path: '/csm/admin//', 
		    	name : 'home',
			    	},
	
		{ path: '/csm/admin/FaqAddNew',
			 	name : 'faqAddNew',
				components:{
			   	default : faq,
			   	content : faqAddNew
			        	},
			 },
	    { path: '/csm/admin/Category',
		    	name : 'category',
		    	components:{
	        	default : category
	        	},
	    },
	    { path: '/csm/admin/addNew', 
	    	name : 'addNew',
	    	components:{
	    		default: category,
		    	content : addNew},
		    },
		{ path: '/csm/admin/del', 				    	
		name : 'categoryDelete',
		components:{
 		    default: category,
			content : categoryDelete},
		    },

		{ path: '/csm/admin/modify',
		    	name : 'modify',
		    	components:{
	    		default: category,
		    	content : modify},
		    },
		    { path: '/csm/admin/Faq', 
		    	name : 'faq1',
		    	components:{
		    		 default: faq,
		      	    	}
			    	},
			{ path: '/csm/admin/showFaqList:id',
		    	name : 'showFaqList',
		    	components:{
	    			default: faq,
	    			content : showFaqList,
	    			pagecount:pageChange
	    			},
			},
	 
		    
			{ path: '/csm/admin/FaqModify',
				name : 'faqModify',
				components:{
				default : faq,
				content : faqModify
				     	},
			 },
				{ path: '/csm/admin/pageChange',
					name : 'pageChange',
					components:{
						pagecount:pageChange
					     	},
				 },
	  ],
	  
	})

new Vue({
	  store,
	  router,
	  el:'#main',
	})



function ajaxFind(checkKey, url1) {
	var checkList = [];
	$.ajax({
		type : "GET",
		url : url1,
		data : checkKey,
		async : false,
		success : function(result) {
			if (result.data.content == undefined) {
				alert("No Result");
				checkList = result;
			} else {
				checkList = result;
			}
		},
		timeout : 3000,
		error : function(xhr) {
			alert(" error： " + xhr.status + " " + xhr.statusText);
		},
	});
	return (checkList);
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
		error : function(xhr) {
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

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
		error : function(xhr) {
			alert("error： " + xhr.status + " " + xhr.statusText);
		},
	});
	return checkList;
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
		error : function(xhr) {

			alert("error： " + xhr.status + " " + xhr.statusText);
		},

	});
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