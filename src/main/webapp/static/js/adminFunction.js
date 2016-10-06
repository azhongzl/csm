var url = "http://localhost:8080/csm/facade/"
var curPage = 1;
var pageSize = 8;
var total, totalPage;
var userId = "";
var userName = "";

function showAdminCategory() {
	var url1 = url + "Category" + "/find";
	var checkKey = "";
	var checkResult1 = ajaxFind(checkKey, url1);
	$("#admin_sidebar").empty();
	$("#content").empty();
	$("#pagecount").empty();
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
					"<input type='button' value = 'MODIFY' onclick='modify()'></input><br>");
}

function showCategoryDetail(id1) {
	var url1 = url + "Faq" + "/find";
	var checkKey = {
		ff_categoryId : id1,
		page_size : pageSize,
		page_no : curPage,
	};
	getCategoryDetail(checkKey, url1);
}

function ajaxFind(checkKey, url1) {
	var checkList = [];
	$.ajax({
		type : "GET",
		url : url1,
		data : checkKey,
		async : false,
		success : function(result) {
			if (result.data.content == undefined) {
				//alert("No Result");
			} else {
				$.each(result.data.content, function(i, n) {
					checkList.push(n);
				});
			}
		},
		timeout : 3000,
		error : function(xhr) {
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
				var li = "";
				li += "<br><input type='button' onclick='faqAddNewUi(" + '"'
						+ checkKey.ff_categoryId + '"' + ")' value='ADD NEW'>";
				$("#content").append(li);

				total = 0;
			} else {
				total = json.data.totalElements;
				pageSize = json.data.size;
				totalPage = json.data.totalPages;
				var li = "";
				var list = json.data.content;
				$.each(list, function(i, n) {
					li += "<a href='#' onclick='adminModifyUi(" + '"' + n.id
							+ '"' + ")'>" + n.question + "</a>";
				});
				li += "<br><input type='button' onclick='faqAddNewUi(" + '"'
						+ checkKey.ff_categoryId + '"' + ")' value='ADD NEW'>";
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

function Categorydelete() {

	var id = $("input[name='id']:checked").val();
	if (id !== undefined) {
		var url1 = url + "Faq" + "/find";
		var checkKey = {
			ff_categoryId : id,
		};
		var checkResult = ajaxFind(checkKey, url1);
		if (checkResult.length == 0) {
			url1 = url + "Category" + "/delete/" + id;
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
					alert("error： " + xhr.status + " " + xhr.statusText);
				},
			});
		} else {
			alert("you can't delete this category");
		}
	} else {
		alert("Please select category");
	}
	;
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
	li += "MODIFY_ACCOUNT_ID:<br><input type ='text' name='modify_account_id'  size='25' disabled='disabled'></input><br>";
	li += "MODIFY_DATE:<br><input type ='datetime-local' name='modify_date' size='25' disabled='disabled'></input><br>";
	li += "<input type ='button' value='submit' onclick='addNew()'></input>"
	li += "<input type ='reset' onclick='reset()' ></input>"
	$("#content").append(li);
	$("input[name='modify_account_id']").val(userName);
	$("input[name='modify_date']").val(time);
}

function reset() {
	$("#content").empty();
	showAdminCategory();
}

function addNew() {
	var url1 = url + "Category" + "/post";
	var name1 = $("input[name='name']").val();
	if ($.trim(name1) == "") {
		alert("Please input name");
	} else {
		var describe1 = $("textarea[name='describe']").val();
		var modify_date1 = getNowFormatDate();

		var savedata = {
			name : name1,
			description : describe1,
			active : "1",
			createAccountId : userId,
			createDate : modify_date1,
			modifyAccountId : userId,
			modifyDate : modify_date1,
		}

		ajaxcreate(savedata, url1);
		showAdminCategory();
		$("input[name='name']").val("");
		$("textarea[name='describe']").val("");
	}
}

function modify() {

	var id = $("input[name='id']:checked").val();
	if (id !== undefined) {
		var url1 = url + "Category" + "/get/" + id;
		var checkResult1 = ajaxGet(url1);
		if (checkResult1.description == undefined) {
			checkResult1.description = "";
		}
		;
		$("#content").empty();
		var li = "";
		li += "NAME:<br><input type =text name='name' size='100'></input><br>";
		li += "DESCRIPTION:<br><textarea name='describe' rows='4' cols='75'></textarea><br>";
		li += "<input type ='button' value='submit' onclick='dataPut(" + '"'
				+ id + '"' + ")'></input>"
		li += "<input type ='button' value='Cancel' onclick='reset()' ></input>"
		$("#content").append(li);
		$("input[name='name']").val(checkResult1.name);
		$("input[name='describe']").val(checkResult1.description);
	} else {
		alert("Please select category")
	}
	;
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

function dataPut(id) {

	var name1 = $("input[name='name']").val();
	var description1 = $("textarea[name='describe']").val()
	if ($.trim(name1) == "") {
		alert("Please input name");
	} else {
		var url1 = url + "Category" + "/put/" + id;
		var putdata = {
			name : name1,
			description : description1,
			modifyAccountId : userId,
			modifyDate : getNowFormatDate(),
		}
		ajaxPut(putdata, url1);
		$("#content").empty();
		showAdminCategory();

	}
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

function showAdminCategory1() {
	var url1 = url + "Category" + "/find";
	var checkKey = "";
	var checkResult1 = ajaxFind(checkKey, url1);
	$("#admin_sidebar").empty();
	$("#content").empty();
	$("#pagecount").empty();
	$("#admin_sidebar").append("<h3>Categories</h3><ul id='adminCategory'>");
	$.each(checkResult1, function(i, n) {
		$("#adminCategory").append(
				"<li><a href='#'   onclick='showCategoryDetail(" + '"' + n.id
						+ '"' + ")'>" + n.name + "</a></li>");
	});
	$("#adminCategory").append("</ul>");
}

function faqAddNewUi(id) {
	$("#pagecount").empty()
	$("#content").empty();
	var li = "";
	li += "QUESTION:<br><textarea name='question' rows='4' cols='75'></textarea><br>";
	li += "ANSWER:<br><textarea name='answer' rows='8' cols='75'></textarea><br>";
	li += "Upload File:<input type='file' id='myFile' name='uploadFile' multiple='multiple'  onchange='showUploadFile()' />";
	li += "<br><textarea name='keywords' rows='3' cols='75'></textarea><br>";
	li += "<input type ='button' value='submit' onclick='faqAddNew(" + '"' + id
			+ '"' + ")'></input>"
	li += "<input type ='button' value='Cancel' onclick=faqCancel(" + '"' + id
			+ '"' + ")></input>"
	$("#content").append(li);

}

function faqAddNew(id) {
	var url1 = url + "Faq" + "/post";
	var name1 = $("textarea[name='question']").val();
	if ($.trim(name1) == "") {
		alert("Please input question");
	} else {
		var answer1 = $("textarea[name='answer']").val();
		var keywords1 = $("textarea[name='keywords']").val();
		var savedata = {
			categoryId : id,
			question : name1,
			keywords : keywords1,
			answer : answer1,
			active : "1",
			createAccountId : userId,
			createDate : getNowFormatDate(),
			modifyAccountId : userId,
			modifyDate : getNowFormatDate(),
		};

		ajaxcreate(savedata, url1);
		showCategoryDetail(id)

	}
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

function adminModifyUi(id) {
	$("#pagecount").empty();
	$("#content").empty();
	var url1 = url + "Faq" + "/get/" + id;
	var checkResult1 = ajaxGet(url1);
	var li = "";
	li += "QUESTION:<br><textarea name='question' rows='4' cols='75'></textarea><br>";
	li += "ANSWER:<br><textarea name='answer' rows='8' cols='75'></textarea><br>";
	li += "KEYWORDS:<br><textarea name='keywords' rows='4' cols='75'></textarea><br>";
	li += "<input type ='button' value='Submit' onclick='adminFaqPut(" + '"'
			+ id + '"' + ',' + '"' + checkResult1.categoryId + '"'
			+ ")'></input>"
	li += "<input type ='button' value='Delete' onclick='adminFaqDelete(" + '"'
			+ id + '"' + ',' + '"' + checkResult1.categoryId + '"'
			+ ")'></input>"
	li += "<input type ='button' value='Cancel' onclick='faqCancel(" + '"'
			+ checkResult1.categoryId + '"' + ")' ></input>"
	$("#content").append(li);
	$("textarea[name='question']").val(checkResult1.question);
	$("textarea[name='answer']").val(checkResult1.answer);
	$("textarea[name='keywords']").val(checkResult1.keywords);
}

function adminFaqPut(id, categoryId1) {
	var question1 = $("textarea[name='question']").val();
	var answer1 = $("textarea[name='answer']").val()
	var keywords1 = $("textarea[name='keywords']").val()
	if ($.trim(question1) == "") {
		alert("Please input question");
	} else {
		var url1 = url + "Faq" + "/put/" + id;
		var putdata = {
			question : question1,
			answer : answer1,
			categoryId : categoryId1,
			modifyAccountId : userId,
			modifyDate : getNowFormatDate(),
		}
		ajaxPut(putdata, url1);
		$("#content").empty();
		showCategoryDetail(categoryId1);

	}
}

function adminFaqDelete(id, categoryId) {
	var url1 = url + "Faq" + "/delete/" + id;
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

	showCategoryDetail(categoryId);
}

function faqCancel(id) {
	showCategoryDetail(id);
}

function showUploadFile(){
   var x = $("input[name='uploadFile']")[0];
   //var x = $("input[name='uploadFile']")[0]
    var txt = "";

 
    alert(x.files.length);
    if ('files' in x) {
        if (x.files.length == 0) {
            txt = "Select one or more files.";
        } else {
        	
            for (var i = 0; i < x.files.length; i++) {
                txt +=  (i+1) + ". file ";
                var file = x.files[i];
                if ('name' in file) {
                    txt += "name: " + file.name ;
                }
                if ('size' in file) {
                    txt += "  file size: " + file.size + " bytes \n";
                }
            }
        }
    } 
    else {
        if (x.value == "") {
            txt += "Select one or more files.";
        } else {
            txt += "The files property is not supported by your browser!";
            txt  += "<br>The path of the selected file: " + x.value; // If the browser does not support the files property, it will return the path of the selected file instead. 
        }
    }
   
    $("textarea[name='keywords']").val(txt);
}
