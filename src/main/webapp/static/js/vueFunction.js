var ctx;
var vmSidebar = new Vue({
	el : '#sidebar_container',
	data : {
		categorys : [],
		cssId:"",
	},
	created :function() {
		this.getCategory();
	},

	methods : {
		showCategoryDetail : function(id) {
			if(this.cssId.length>0){
				$("#" + this.cssId).css("color", "#555");
			}
	        $("#" + id).css("color", "red");
	        this.cssId = id;
			vmContent.faqs = [];
			vmPagecount.id = id;
			var url1 =  ctx + "/faq/listForm/"+id;
			var checkKey = {
				page_no : 1,
			};
			var result1 = ajaxFind(checkKey, url1);
			if (result1.data.faqList.content == undefined) {
				vmPagecount.total = 0;
				vmPagecount.showKey = false;
			} else {
				vmPagecount.total = result1.data.faqList.totalElements;
				vmPagecount.totalPage = result1.data.faqList.totalPages;
				vmPagecount.showKey = true;
				vmSearch.label = "";
				vmContent.label = "Faq";
				vmContent.faqs = result1.data.faqList.content;
				
			};
		},
		getCategory : function(){
			var url1 = ctx + "/faqCategory/listForm";
			var checkKey = "";
			var result = ajaxFind(checkKey, url1);
			if (result.length==0){
				return false;
			}
			this.categorys = result.data.faqCategoryList;
		}
	}
})

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
	return (checkList);
}
var tr1 = Vue.extend({
	template : "#content1",
	props : [ 'faqs', 'answerKey', 'faq' ],
	computed : {
		attachments : function() {
			if (this.faq.attachments!=undefined){
				return this.faq.attachments.split(",")	;
			}else{
				return [];
			}
			
		}
	},
	methods : {
		showAnswer : function() {
			this.answerKey = !this.answerKey;
		},
	},
});

var vmContent = new Vue({
	el : '#content',
	data : {
		faqs : [],
		answerKey : false,
		label : "",
	},
	components : {
		'my-faqs' : tr1,
	},

});

var vmPagecount = new Vue({
	el : '#pagecount',
	data : {
		curPage : 1,
		total : 0,
		totalPage : 0,
		id : "",
		showKey : false,
	},
	methods : {
		changePage : function(page) {
			if (page<=0){
				page=1;
			}
			if(page>this.totalPage){
				page=this.totalPage;
			}
			if (this.curPage != page) {
				this.curPage = page;
				vmContent.faqs = [];
				if(vmContent.label=="Faq"){
					var url1 =  ctx + "/faq/listForm/"+this.id;
					var checkKey = {
						page_no : this.curPage,
					};
					var result1 = ajaxFind(checkKey, url1);
					vmContent.faqs = result1.data.faqList.content;
				}
				if(vmContent.label=="Search"){
		    		vmContent.faqs=[];
					var url1 ="http://localhost:8080/csm/search/faq";
					var checkKey = {
							ss : this.search,
							page_no : this.curPage,
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
		    			},
		    		});
					if (checkList.data.faqList.content == undefined) {
						vmPagecount.total = 0;
						vmPagecount.showKey = false;
					} else {
						vmPagecount.total = checkList.data.totalElements;
						vmPagecount.totalPage = checkList.data.totalPages;
						vmPagecount.showKey = true;
						vmContent.label = "";
						this.label = "Search";
			    		vmContent.faqs = checkList.data.content;
					}
				}
			}
		}
	},
});
var vmSearch = new Vue({
	el:'#logo_text',
	data: {
		search :"",
		label:""
	},
    methods:{
    	ajaxSearch:function(){
    		vmContent.faqs=[];
 			var url1 ="http://localhost:8080/csm/search/faq";
			var checkKey = {
					ss : this.search,
					page_no : vmPagecount.curPage,
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
				vmPagecount.total = 0;
				vmPagecount.showKey = false;
			} else {
				vmPagecount.total = checkList.data.searchList.totalElements;
				vmPagecount.totalPage = checkList.data.searchList.totalPages;
				vmPagecount.showKey = true;
				vmContent.label = "";
				this.label = "Search";
	    		vmContent.faqs = checkList.data.searchList.content;
			};

    		
    	}
    }
})
