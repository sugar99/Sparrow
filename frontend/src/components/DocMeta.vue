<template>
	<a-drawer width=420 :closable="false" @close="close" :visible="docMetaVisible">
		<div slot="title">
			<span>文档属性</span>
			<el-button style="position: absolute; top: 10px; right: 10px;" size="small" @click="docEditMetaVisible = true" icon="el-icon-edit" type="primary" plain></el-button>
		</div>
		<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">文档名：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{docMeta.title}}</span>
			</el-col>
		</el-row>
		<!-- 			<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">创建者：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{docMeta.creator}}</span>
			</el-col>
		</el-row> -->
		<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">创建时间：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{docMeta.created_time.substr(0,19)}}</span>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">修改时间：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{docMeta.modified_time.substr(0,19)}}</span>
			</el-col>
		</el-row>
		<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">简介：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{docMeta.desc}}</span>
			</el-col>
		</el-row>

		<!-- 文档meta修改 -->
		<a-drawer width=420 :closable="false" @close="docEditMetaVisible = false" :visible="docEditMetaVisible">
			<div slot="title">
				<span>修改文档信息</span>
				<el-button style="position: absolute; top: 10px; right: 10px;" size="small" @click="updateDocMeta" type="primary" plain>保存修改</el-button>
			</div>
			<el-row style="margin-top: 10px;">
				<el-col :offset="1" :span="5" style="text-align: right;">文档名：</el-col>
				<el-col :offset="2" :span="16">
					<el-input type="text" v-model="docEditMeta.title" size="small">
					</el-input>
				</el-col>
			</el-row>
			<el-row style="margin-top: 10px;">
				<el-col :offset="1" :span="5" style="text-align: right;">简介：</el-col>
				<el-col :offset="2" :span="16">
					<el-input type="textarea" :rows="8" show-word-limit v-model="docEditMeta.desc" size="small">
					</el-input>
				</el-col>
			</el-row>
		</a-drawer>

	</a-drawer>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	import * as DEFAULT from "../json/default"
	import * as Api from '../api/api'

	export default {
		name: "DocMeta",
		data() {
			return {
				// 文档meta
				docEditMetaVisible: false,
				docMeta: {
					"id": "1",
					"title": "code",
					"desc": "代码仓库",
					"creator": "green",
					"files": [
						"1",
						"2",
						"ABC"
					],
					"meta_state": 1,
					"created_time": "2019-07-05 23:09:00",
					"modified_time": "2019-07-05 23:10:00"
				},
				docEditMeta: {
					"title": "code",
					"desc": "代码仓库"
				},
			}
		},
		computed: {
			...mapState([
				'docMetaVisible'
			])
		},
		props: [
			'clickedItemId'
		],
		watch: {
			docMetaVisible(newOne, oldOne) {
				if (newOne && this.clickedItemId != '') {
					this.getDocMeta();
				}
			}
		},
		methods: {
			updateDocMeta() {
				let _this = this;
				Api.editDocMeta(this.clickedItemId, this.docEditMeta.title, this.docEditMeta.desc).then(
					res => {
						if (res.data.status === 200) {
							_this.docMeta = JSON.parse(JSON.stringify(_this.docEditMeta));
							_this.docEditMetaVisible = false;
							_this.$parent.itemDBClicked(-1);
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.handleError(err);
				});
			},
			getDocMeta() {
				let _this = this;
				Api.getDocMeta(this.clickedItemId).then(
					res => {
						if (res.data.status === 200) {
							_this.docMeta = res.data.data;
							_this.docEditMeta = JSON.parse(JSON.stringify(_this.docMeta));
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.handleError(err);
				});
			},
			close() {
				this.$store.commit({
					type: "docMetaH"
				})
			},
			handleError(err) {
				console.log(err);
				_this.$message.warning(DEFAULT.defaultNetwordError);
			}
		}
	}
</script>

<style>
</style>
