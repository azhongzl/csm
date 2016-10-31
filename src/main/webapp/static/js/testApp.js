
const Home = { template: '<div>This is Home</div>' }
const Foo = { template: '<div>This is Foo</div>' }
const Bar = { template: '<div>This is Bar {{ $route.params.id }}</div>' }

const router = new VueRouter({
  mode: 'history',
  base: __dirname,
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/foo', name: 'foo', component: Foo },
    { path: '/bar/:id', name: 'bar', component: Bar }
  ]
})

new Vue({
  router,
  template: `
    <div id="app">
      <h1>Named Routes</h1>
      <p>Current route name: {{ $route.name }}</p>
      <ul>
        <li><router-link :to="{ name: 'home' }">home</router-link></li>
        <li><router-link :to="{ name: 'foo' }">foo</router-link></li>
        <li><router-link :to="{ name: 'bar', params: { id: 123 }}">bar</router-link></li>
      </ul>
      <router-view class="view"></router-view>
    </div>
  `
}).$mount('#app')


const Baz = {
    template:"#test",

    data: function() {
        return {
            textFields: [
                {id: 1, value: "sss"},
                {id: 2, value: "qqq"},
                {id: 3, value: "eee"}
            ]
        }
    },
    computed: {
        textValues: function() {
            values = []
            this.textFields.forEach(textField => {
                values.push(textField.value);
            })
            return values;
        }
    }
    
}

const Foo = { template: '#category',
		  data:function(){ 
			  return {
            list : [
                {id: "sdaf", name: "sss"},
                {id: "asf", name: "qqq"},
               {id: "asdf", name: "eee"} 
            ]
		  }
        },
        methods:{
        	addNewUi : function(){
        		this.list.push({id: "asdf", name: "eee"});
        	}
        },
        
        };

const Bar = { 
		template: `<div>BAR<div> `

		}
//const Baz = { template: '<div>baz</div>' }

const router = new VueRouter({
  mode: 'history',
  routes: [
    { path: '/',

      components: {
        default: Foo,
        a: Bar,
        b: Baz
      }
    },
    {
      path: '/other',
      components: {

        a: Bar,

      }
    }
  ]
})


new Vue({
	
data: {
	picked: " ",
	list:[{id:'0000123123123123',name:'uuuuuu'},
	      {id:'00001231sdf3',name:'sdfsdfsdf'}
	      ]
},
	router,
  el: '#app'
})



const test = {
    template: `
        <div class="my-text-input-fields">
            
                <input type="text" v-for="textField in textFields" v-model="textField.value"><br>
           
            <div style="margin-top:30px;">
                {{textValues}}
            </div>
        </div>
    `,

    data: function() {
        return {
            textFields: [
                {id: 1, value: "sss"},
                {id: 2, value: "qqq"},
                {id: 3, value: "eee"}
            ]
        }
    },
    computed: {
        textValues: function() {
            values = []
            this.textFields.forEach(textField => {
                values.push(textField.value);
            })
            return values;
        }
    }
    
}



new Vue({
    el: '#app1',
components:{
	'my-test': test,
}
 
  
})
