var tr1 = Vue.extend( {
	template : "#test",

	props : [ 'a', 'b','c' ],

});
var tr2 = Vue.extend( {
	template : "#test1",
	props : [ 'a', 'b','c' ],

});

var vm = new Vue({
	el : '#app',
	data : {
		a : 1,
	c:{text:"sdfsdfsdf",text1:"dddddddddd"},
		b : 2,
		test1: 5
		

		
	},
	components :{
		'my-test': tr1,
		'my-test1': tr2,
	},
computed : {
	test:function(){

		 this.test1=this.a+this.test1;
		 return this.test1
	}
}
})
