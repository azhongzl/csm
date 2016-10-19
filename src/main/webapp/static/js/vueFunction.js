var url = "http://localhost:8080/csm/e/";

var vmSidebar = new Vue({
	el : '#sidebar_container',

	data : {
		categorys : [],
	},

	computed : {
		categorys : function() {
			var url1 = url + "Category" + "/find";
			var checkKey = "";
			var result = ajaxFind(checkKey, url1);
			return result.data.content;
		},
	},

	methods : {
		showCategoryDetail : function(id) {
			vmContent.faqs = [];
			vmPagecount.id = id;
			var url1 = url + "Faq" + "/find";
			var checkKey = {
				ff_categoryId : id,
				page_size : vmPagecount.pageSize,
				page_no : 1,
			};
			var result1 = ajaxFind(checkKey, url1);

			if (result1.length == 0) {
				vmPagecount.total = 0;
				vmPagecount.showKey = false;
			} else {
				vmPagecount.total = result1.data.totalElements;
				vmPagecount.pageSize = result1.data.size;
				vmPagecount.totalPage = result1.data.totalPages;
				vmPagecount.showKey = true;
				vmContent.faqs = result1.data.content;

			}
			;
		},

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
			if (result.data.content == undefined) {
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
	return (checkList);
}
var tr1 = Vue.extend({
	template : "#content1",

	props : [ 'faqs', 'answerKey', 'faq' ],
	computed : {
		attachments : function() {
			return this.faq.attachments.split(",")
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
	},
	components : {
		'my-faqs' : tr1,
	},

});

var vmPagecount = new Vue({
	el : '#pagecount',
	data : {
		curPage : 1,
		pageSize : 8,
		total : 0,
		totalPage : 0,
		id : "",
		showKey : false,
	},
	methods : {
		changePage : function(page) {
			if (this.curPage != page) {
				if (page > this.totalPage) {
					this.curPage = totalPage;
				} else if (page <= 0) {
					this.curPage = 1;
				} else {
					this.curPage = page;
				}
				vmContent.faqs = [];
				var url1 = url + "Faq" + "/find";
				var checkKey = {
					ff_categoryId : this.id,
					page_size : this.pageSize,
					page_no : this.curPage,
				};
				var result1 = ajaxFind(checkKey, url1);
				vmContent.faqs = result1.data.content;
			}
		},
	},
	computed : {},

})
