var url = "http://localhost:8080/csm/facade/"
var curPage = 1;
var pageSize = 2;
var total, totalPage;

function showCategory() {
	var url1 = url + "Category" + "/search";
	var checkKey = "";
	var checkResult1 = ajaxSearch(checkKey, url1);
	$.each(checkResult1, function(i, n) {
		$("#Category").append(
				"<li><a href='#'   onclick='showCategoryDetail(" + '"' + n.id
						+ '"' + ")'>" + n.name + "</a></li>");
	});
}

function showCategoryDetail(id1) {
	var url1 = url + "Faq" + "/search";
	var checkKey = {
		ff_categoryId : id1,
		page_size : pageSize,
		page_no : curPage,
	};
	getCategoryDetail(checkKey, url1);
}

function ajaxSearch(checkKey, url1) {
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
				$.each(result.data.content, function(i, n) {
					checkList.push(n);
				});
			}
		},
		timeout : 3000,
		error : function(xhr) {
			var obj = jQuery.parseJSON(xhr.responseText);
			alert(obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},
	});
	return (checkList);
}


function getCategoryDetail(checkKey, url1) {
	$.ajax({
		type : "GET",
		url : url1,
		data : checkKey,
		async : false,
		beforeSend : function() {
			$("#content").empty();
			$("#content").append("<li >loading...</li>");
		},
		success : function(json) {
			$("#content").empty();
			if (json.data.content == undefined) {
				alert("No Result");
				total = 0;
			} else {
				total = json.data.totalElements;

				pageSize = json.data.size;

				totalPage = json.data.totalPages;
				var li = "";
				var list = json.data.content;
				$.each(list, function(i, n) {
					li += "<a href='#' onclick='showAnswer("+'"'+n.id+'"'+","+'"'+n.answer+'"'+")'>" + n.question + "</a>";
					li += "<span id="+'"'+n.id+'"'+"></span>";
				});

				$("#content").append(li);
			}
			;
		},
		complete : function() {
			var id = checkKey.ff_categoryId;
			getPageBar(id);
		},
		error : function() {
			alert("data error");
		},
	});
}

function getPageBar(id1) {
	var pageStr = "";
	if (total == 0) {
		pageStr = "<span>No Result</span>"
	} else {
		if (curPage == 1) {
			pageStr += "<span><<</span><span><</span>";
		} else {
			pageStr += "<span><a href='javascript:void(0)' onclick='changePage(1,"
					+ '"'
					+ id1
					+ '"'
					+ ")'><<</a></span><span><a href='javascript:void(0)' onclick='changePage("
					+ (curPage - 1) + "," + '"' + id1 + '"' + ")'><</a></span>";
		}
		for (i = 1; i <= totalPage; i++) {
			if (i == curPage) {
				pageStr += "<span class='current'>" + i + "</span>";
			} else {
				pageStr += "<span><a href='javascript:void(0)' onclick='changePage("
						+ i + ',"' + id1 + '"' + ")'>" + i + "</a></span>";
			}
		}

		if (curPage >= totalPage) {
			pageStr += "<span>></span><span>>></span>";
		} else {
			pageStr += "<span><a href='javascript:void(0)' onclick='changePage("
					+ (curPage + 1)
					+ ","
					+ '"'
					+ id1
					+ '"'
					+ ")'>></a></span><span><a href='javascript:void(0)' onclick='changePage("
					+ totalPage + "," + '"' + id1 + '"' + ")'>>></a></span>";
		}
	}
	$("#pagecount").html(pageStr);
}

function changePage(page, id1) {
	curPage = page;
	showCategoryDetail(id1);
}

function showAnswer(id,answer){


	if($("#"+id).html()==""){
		$('#'+id).hide();
		$('#'+id).html(answer);
		$('#'+id).slideDown();
	}else{
		$('#'+id).html("");
		$('#'+id).slideUp();
	}

}






