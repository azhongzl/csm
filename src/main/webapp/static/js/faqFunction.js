var ctx;

const
store = new Vuex.Store({
	state : {
		faqs : [],
		curPage : 1,
		totalPage : 0,
		lastNum : 5,
		column : 5,
		key : 0,
		sentence:"",
	},
	mutations : {
		getFaqs : function(state, payload) {
			state.faqs = payload.id;
		},
		searchFaqs : function(state) {
			$("*").collapse('hide');
			state.faqs = [];
			var url1 = ctx + "/search/faq";
			var checkKey = {
				ss : state.sentence,
				page_no : state.curPage,
			};
			var checkList = [];
			$.ajax({
				type : "GET",
				url : url1,
				data : checkKey,
				async : false,
				success : function(result) {
					if (result.data.searchList.content == undefined) {
						alert("No Result");
					} else {
						checkList = result;
					}
				},
				timeout : 3000,
				error : function(xhr) {
					alert(" error： " + xhr.status + " " + xhr.statusText);
				}
			});
			
			if (checkList.length == 0) {
				state.totalPage = 0;
			} else {
				state.totalPage = checkList.data.searchList.totalPages;
				if ( state.totalPage <= 5) {
					 state.column =  state.totalPage;
				} else {
					 state.column = 5;
				}
				let
				result2 = checkList.data.searchList.content;
				let
				temp = [];
				$.each(result2, function(i, n) {
					if (n.attachments != undefined) {
						temp = n.attachments.split(",");
						n.attachments = temp;
					}
				});
				state.key=1;
				store.commit("getFaqs", {
					id : result2
				});
			}
		},
	}
});

const
faqPage = {
	template : "#faqPage",
	data : function() {
		return {
			categories : [],
			cssId : "",
		}
	},
	computed : {
		faqs : function() {
			return this.$store.state.faqs;
		},
	    totalPage:function(){
	    	return this.$store.state.totalPage;
	    },
	    lastNum:function(){
	    	return this.$store.state.lastNum;
	    },
	    column:function(){
	    	return this.$store.state.column;
	    },
	},
	created : function() {
		this.getCategory();
	},
	methods : {
		getCategory : function() {
			var url1 = ctx + "/faqCategory/listForm";
			var checkKey = "";
			var result = ajaxFind(checkKey, url1);
			if (result.length == 0) {
				return false;
			}
			this.categories = result.data.faqCategoryList;
		},

		showCategoryDetail : function(id) {
			this.$store.state.faqs=[];
			$("*").collapse('hide');
			if (this.cssId.length > 0) {
				$("#" + this.cssId).css("background-color", "white");
			}
			$("#" + id).css("background-color", "#dfdfdf");
			this.cssId = id;
			var url1 = ctx + "/faq/listForm/" + id;
			var checkKey = {
				page_no : this.$store.state.curPage,
			};
			var result1 = ajaxFind(checkKey, url1);
			if (result1.data.faqList.content == undefined) {
				this.$store.state.totalPage = 0;
				store.commit("getFaqs", {
					id : []
				});
			} else {
				this.$store.state.totalPage = result1.data.faqList.totalPages;
				if (this.$store.state.totalPage <= 5) {
					this.$store.state.column = this.$store.state.totalPage;
				} else {
					this.$store.state.column = 5;
				}
				let
				result2 = result1.data.faqList.content;
				let
				temp = [];
				$.each(result2, function(i, n) {
					if (n.attachments != undefined) {
						temp = n.attachments.split(",");
						n.attachments = temp;
					}
				});
				this.$store.state.key=0;
				store.commit("getFaqs", {
					id : result2
				});
			}
			;
		},

		changePage : function(n) {
			if ((n == 0) || (n > this.$store.state.totalPage)) {
				return false;
			}
			if (this.$store.state.curPage == n) {
				return false;
			}
			this.$store.state.curPage = n;
			if (n <= 5) {
				this.$store.state.lastNum = 5;
			} else {
				if ((n + 2) > this.$store.state.totalPage) {
					this.$store.state.lastNum = this.$store.state.totalPage;
				} else {
					this.$store.state.lastNum = n + 2;
				}
			}
			if (this.$store.state.key==0){
				this.showCategoryDetail();				
			}
			if (this.$store.state.key==1){
				store.commit("searchFaqs");				
			}
		},
	}
};

const
contact = {
	template : "#contact",
};

const
router = new VueRouter({
	routes : [ {
		path : '/',
		component : faqPage
	}, {
		path : '/home',
		component : faqPage
	}, {
		path : '/contact',
		component : contact
	} ]
});

new Vue({
	store : store,
	router : router,
	el : '#mainPage',
	data : {
		searchSentence : "",
	},
	methods : {
		ajaxSearch : function() {
			if (!this.searchSentence) {
				return false;
			}
			this.$store.state.sentence=this.searchSentence;
			store.commit("searchFaqs");
		},
	},
});

function ajaxFind(checkKey, url1) {
	var checkList = [];
	$.ajax({
		type : "GET",
		url : url1,
		data : checkKey,
		async : false,
		success : function(result) {
			checkList = result;
		},
		timeout : 3000,
		error : function(xhr) {
			alert(" error： " + xhr.status + " " + xhr.statusText);
		},
	});
	return checkList;
};