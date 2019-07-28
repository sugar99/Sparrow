import Vue from 'vue'
import Router from 'vue-router'
import App from '../App'
import Doc from '../pages/doc'
import Search from '../pages/search'
import File from '../subpage/SingleFile'

Vue.use(Router)

export default new Router({
	mode: "hash",
// 	base: '/tool/',
	routes: [
		{
			path: '/doc',
			name: 'doc',
			component: Doc
		},
		{ //需要跳转的页面
			path: '/search',
			name: 'search',
			component: Search
		},
		{ //需要跳转的页面
			path: '/file',
			name: 'file',
			component: File
		}
	]
})
