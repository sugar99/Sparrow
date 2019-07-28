import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
	state: {
		// 登陆获得的初始信息,仅存储
		pathBackup: [],
		currentResourceBackup: null,

		// 各种抽屉的visible变量
		groupVisible: false,
		docMetaVisible: false,
		dirMetaVisible: false,
		fileMetaVisible: false,
		changeNameVisible: false,
		uploadVisiable: false,
	},
	mutations: {
		setPathBackup(state, payload) {
			state.pathBackup = payload.pathBackup;
		},
		setCurrentResourceBackup(state, payload) {
			state.currentResourceBackup = payload.currentResourceBackup;
		},

		// visible
		groupV(state) {
			state.groupVisible = true;
		},
		// hidden
		groupH(state) {
			state.groupVisible = false;
		},

		docMetaV(state) {
			state.docMetaVisible = true;
		},
		docMetaH(state) {
			state.docMetaVisible = false;
		},

		dirMetaV(state) {
			state.dirMetaVisible = true;
		},
		dirMetaH(state) {
			state.dirMetaVisible = false;
		},

		fileMetaV(state) {
			state.fileMetaVisible = true;
		},
		fileMetaH(state) {
			state.fileMetaVisible = false;
		},

		changeNameV(state) {
			state.changeNameVisible = true;
		},
		changeNameH(state) {
			state.changeNameVisible = false;
		},

		uploadV(state) {
			state.uploadVisiable = true;
		},
		uploadH(state) {
			state.uploadVisiable = false;
		},
	}
});
