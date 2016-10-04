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

function showAdminCategory() {
	var url1 = url + "Category" + "/search";
	var checkKey = "";
	var checkResult1 = ajaxSearch(checkKey, url1);
	$("#admin_sidebar").empty();
	$.each(checkResult1, function(i, n) {
		$("#admin_sidebar").append(
				"<input type='radio' name='id' value=" + "'" + n.id + "'" + ">"
						+ n.name + "</input><br>");
	});
	$("#admin_sidebar")
			.append(
					"<input type='button' value = 'ADD NEW' onclick='addNewUi()'></input><br>");
	$("#admin_sidebar")
			.append(
					"<input type='button' value = 'DELETE' onclick='Categorydelete()'></input><br>");
	$("#admin_sidebar")
			.append(
					"<input type='button' value = 'MODIFY' onclick='alert()'></input><br>");
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
					li += "<a href='#' onclick='showAnswer(" + '"' + n.id + '"'
							+ "," + '"' + n.answer + '"' + ")'>" + n.question
							+ "</a>";
					li += "<span id=" + '"' + n.id + '"' + "></span>";
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

function showAnswer(id, answer) {
	if ($("#" + id).html() == "") {
		$('#' + id).hide();
		$('#' + id).html(answer);
		$('#' + id).slideDown();
	} else {
		$('#' + id).html("");
		$('#' + id).slideUp();
	}
}

function Categorydelete() {

	var id = $("input[name='id']:checked").val();
	alert(id);
	var url1 = url + "Category" + "/delete/" + id;

	$.ajax({
		type : "GET",
		url : url1,
		async : false,
		success : function(result) {
			alert("DELETE OK ");
			showAdminCategory();
		},
		timeout : 3000,
		error : function(xhr) {
			var obj = jQuery.parseJSON(xhr.responseText);
			alert("delete message:" + obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},
	});
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

function addNewUi() {
	var time = getNowFormatDate();
	$("#content").empty();
	var li = "";
	li += "NAME:<br><input type =text name='name' size='100'></input><br>";
	li += "DESCRIPTION:<br><textarea name='describe' rows='4' cols='75'></textarea><br>";
	li += "MODIFY_ACCOUNT_ID:<br><input type ='text' name='modify_account_id' size='25' disabled='disabled'></input><br>";
	li += "MODIFY_DATE:<br><input type ='datetime-local' name='modify_date' size='25' disabled='disabled'></input><br>";
	li += "<input type ='submit' value='submit' onclick='addNew()'></input>"
	li += "<input type ='reset' onclick='reset()' ></input>"
	$("#content").append(li);
	$("input[name='modify_date']").val(time);
}

function reset() {
	$("input[name='name']").val("");
	$("textarea[name='describe']").val("");
	$("input[name='modify_date']").val(getNowFormatDate());
}

function ajaxcreate(savedata, url1) {
	alert();
	$.ajax({
		type : "POST",
		url : url1,
		data : savedata,
		async : false,
		success : function(result) {
			alert(result);
			alert("ADD NEW SUCCESS");
		},
		timeout : 3000,
		error : function(xhr) {
			alert("error");
			var obj = jQuery.parseJSON(xhr.responseText);
			alert(obj.message);
			alert("error： " + xhr.status + " " + xhr.statusText);
		},

	});
}

function addNew() {
	var id= getCookie();
	var url1 = url + "Category" + "/post";
	var name1 = $("input[name='name']").val();
	var describe1 = $("textarea[name='describe']").val();
	var modify_date1 = getNowFormatDate();
	var savedata = {
		name : name1,
		descrption : describe1,
		active : "1",
		createAccountId : "liu",
		createDate : modify_date1,
		modifyAccountId : "liu",
		modifyDate : modify_date1,
	}

	ajaxcreate(savedata, url1);
	showAdminCategory();
}


function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=")
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 
    c_end=document.cookie.indexOf(";",c_start)
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end))
    } 
  }
return ""
}