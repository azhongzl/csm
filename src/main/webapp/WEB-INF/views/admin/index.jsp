<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>

<head>
<title>DATA Managment</title>

<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/adminStyle.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/js/vue.js"></script>
<script type="text/javascript" src="${ctx}/static/js/vue-router.js"></script>
<script type="text/javascript" src="${ctx}/static/js/vuex.min.js"></script>
</head>

<body>
	<div id="main">
		<header>
			<nav>
				<div id="menu_container">
					<ul class="sf-menu" id="nav">
						<li><router-link :to="{name:'home'}">Home</router-link></li>
						<li><a href="#">Database</a>
							<ul>
								<li><router-link :to="{name:'faq1'}">Faq</router-link></li>
								<li><router-link :to="{name:'category'}">Category</router-link></li>
							</ul></li>
						<li><a href="#">Logout</a></li>
					</ul>
				</div>
			</nav>
		</header>
		<div id="site_content">
			<div id="sidebar_container">

				<router-view></router-view>


			</div>
			<div id="content">
				<router-view name="content"></router-view>
			</div>
			<div id="pagecount">
				<router-view name="pagecount"></router-view>
			</div>
		</div>
		<div id="scroll">
			<a title="Scroll to the top" class="top" href="#"><img src="${ctx}/static/images/top.png" alt="top" /></a>
		</div>
		<footer>
			<p>
				<img src="${ctx}/static/images/twitter.png" alt="twitter" />&nbsp;<img src="${ctx}/static/images/facebook.png"
					alt="facebook" />&nbsp;<img src="${ctx}/static/images/rss.png" alt="rss" />
			</p>
			<p>
				<a href="${ctx}/">Home</a> | <a href="javascript:void(0)">Contact Us</a>
			</p>
			<p>
				Copyright &copy; <a href="http://www.kuzcolighting.com">design from Kuzcolighting</a>
			</p>
		</footer>
	</div>




	<template id="category">
	<div id="admin_sidebar">
		<h3>Categories</h3>
		<template v-for="n in list"> <input type="radio" name="id" :value="n.id" v-model="picked">{{n.name}}</input>
		<br>
		</template>
		<div id="router">
			<router-link :to="{name:'addNew'}">ADD NEW</router-link>
			<br>
			<router-link :to="{ name: 'categoryDelete', query: { id: picked }}">DELETE</router-link>
			<br>
			<router-link :to="{name: 'modify', query: { id: picked }}">MODIFY</router-link>
		</div>
	</div>
	</template>

	<template id="addNew">
	<div>
		NAME:<br> <input type=text name='name' size='75' v-model='name'></input><br> DESCRIPTION:<br>
		<textarea name='describe' rows='4' cols='58' v-model='describe'></textarea>
		<br> <input type='button' value='submit' v-on:click='addNew()'></input> <input type='button' value='cancel'
			v-on:click='reset()'></input>
	</div>
	</template>



	<template id="FaqCategory">
	<div id="admin_sidebar">
		<h3>Categories</h3>
		<ul id="adminCategory">
			<li v-for="n in list"><router-link :to="{name: 'showFaqList', params: { id: n.id }}">{{n.name}}</router-link></li>
		</ul>
	</div>
	</template>

	<template id="FaqDetail">
	<div>
	  	<ul>
		<li v-for="n in list1"><router-link :to="{name: 'faqModify', params: { id: n.id }}">{{n.question}}</router-link></li>
		</ul>
		<input type="button" v-on:click="faqAddNewUi()" value="ADD NEW">
	</div>
	</template>

	<template id="FaqAddNewUi">
	<div>
		QUESTION:<br>
		<textarea name='question' rows='4' cols='75' v-model='question'></textarea>
		<br> ANSWER:<br>
		<textarea name='answer' rows='8' cols='75' v-model='answer'></textarea>		
		<br> Upload File: <input type='file' name='uploadFile' multiple='multiple' v-on:change='showUploadFile()' /> <br>
		<textarea name='attachments' rows='3' cols='75' disabled='disabled' v-model='attachements'></textarea>
		<br> <input type='button' value='submit' v-on:click='faqAddNew()'></input> <input type='button' value='cancel'
			v-on:click='faqCancel()'></input>
	</div>
	</template>


	<template id="FaqModifyUi">
	<div>
		QUESTION:<br>
		<textarea name='question' rows='4' cols='75' v-model='question'></textarea>
		<br> ANSWER:<br>
		<textarea name='answer' rows='8' cols='75' v-model='answer'></textarea>
		<br>
		<p v-show="attachments.length === 0?false:true">
		Attachments: 
		</p>		
		<template v-for='attachment in attachments'  >
			<input type='checkbox' :checked='checked' name='attach' :value='attachment' 
			v-model='attach' v-show="attachments.length === 0?false:true">{{attachment}} </input>  
		</template>
		 <br><br>
		Upload File: <input type='file' name='uploadFile' multiple='multiple' v-on:change='showUploadFile()' /><br>
		<textarea name='attachments1' rows='3' cols='75' disabled='disabled' v-model='attachements1'></textarea> <br>
		<input type='button' value='submit' v-on:click='adminFaqPut()'></input>
		<input type='button' value='Delete' v-on:click='adminFaqDelete()'></input> 
		<input type='button' value='cancel' v-on:click='faqCancel()'></input>
	</div>
	</template>
	 
	 
	<template id="pageChange">
		<div  v-show="showKey" >
				<span><a href="javascript:void(0)" v-on:click="changePage(1)"> &laquo; </a></span> 
				<span><a href="javascript:void(0)" v-on:click="changePage(curPage - 1)"> &lsaquo; </a></span>
			    <span v-for="n in totalPage" :class="curPage === n ? 'current' : ''">
			    <a href="javascript:void(0)" v-on:click="changePage(n)">{{ n }}</a>
			    </span> 
			    <span><a href="javascript:void(0)" v-on:click="changePage(curPage + 1)"> &rsaquo; </a></span> 
			    <span><a href="javascript:void(0)" v-on:click="changePage(totalPage)"> &raquo; </a></span>
			</div>
	</template>

	<script type="text/javascript" src="${ctx}/static/js/adminVueFunction.js"></script>
	<script type="text/javascript">
		userId = "<shiro:principal property="id" />";
		userName = "<shiro:principal property="username" />"
	</script>

</body>

</html>