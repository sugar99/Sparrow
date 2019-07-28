import Vue from 'vue'
import SingleFile from './SingleFile.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from '../router/router'

import Antd from 'ant-design-vue'
import "ant-design-vue/dist/antd.css";
import axios from 'axios'
import store from '../store/store'

Vue.prototype.axios = axios
axios.defaults.withCredentials = true;

Vue.config.productionTip = false
Vue.use(ElementUI);

Vue.use(Antd);

new Vue({
	router,
	store,
	render: h => h(SingleFile),
}).$mount('#app')
