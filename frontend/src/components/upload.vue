<template>
	<a-drawer title="上传文件" width=420 :closable="false" @close="close" :visible="uploadVisiable">
		<el-upload style="text-align: center;" drag :action="url" :on-success="afterUpload" :before-upload="beforeUpload"
		 :http-request="uploadFile">
			<i class="el-icon-upload"></i>
			<div>将文件拖到此处，或<span class="em">点击上传</span>
			</div>
		</el-upload>
	</a-drawer>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	import * as Api from '../api/api'
	import * as DEFAULT from "../json/default"

	export default {
		name: "Upload",
		computed: {
			...mapState([
				'uploadVisiable'
			])
		},
		props: [
			'currentResourceId'
		],
		data() {
			return {
				// 上传
				url: "",
				objectName: "",
				file_id: "",
				fileExtension: "",
				currentUserId: ""
			}
		},
		methods: {
			// 上传函数
			afterUpload(response, file, fileList) {
				let _this = this;
				// 上传成功后更新meta
				let data = JSON.stringify({
					'title': file.name.substring(0, file.name.lastIndexOf(".")),
					'store_key': _this.objectName,
					'doc_id': _this.currentResourceId,
					'parentId': '',
					'ext': _this.fileExtension,
					'creator': _this.currentUserId,
					'size': Math.floor(file.size.toFixed(1))
				})

				$.ajax({
					type: 'post',
					// url: 'http://192.168.43.211:8089/v1/files/' + _this.file_id.substring(0, _this.file_id.lastIndexOf(".")),
					// url: 'http://39.108.210.48:8089/v1/files/' + _this.file_id.substring(0, _this.file_id.lastIndexOf(".")),
					url: Api.baseUrl + '/files/' + _this.file_id.substring(0, _this.file_id.lastIndexOf(".")),
					data: data,
					contentType: 'application/json',
					xhrFields: {
						withCredentials: true
					},
					crossDomain: true,
					success: function(datas) {
						_this.$parent.itemDBClicked(-1);
						setTimeout(function() {
							_this.close();
						}, 500)
					}
				})
			},
			beforeUpload(file) {
				let _this = this;
				_this.fileExtension = file.name.split('.')[file.name.split('.').length - 1]

				// 从服务器获取一个URL
				this.policy(file);
			},
			policy(file) {
				let _this = this;
				var data = JSON.stringify({
					'ext': this.fileExtension,
					'cur_id': this.currentResourceId
				})
				$.ajax({
					type: 'post',
					// url: 'http://192.168.43.211:8089/v1/files/url',
					// url: 'http://39.108.210.48:8089/v1/files/url',
					url: Api.baseUrl + '/files/url',
					data: data,
					async: false,
					contentType: 'application/json',
					xhrFields: {
						withCredentials: true
					},
					crossDomain: true,
					success: function(datas) {
						_this.url = datas.data.url;
						_this.currentUserId = datas.data.creator;

						let strings = _this.url.split('/')
						_this.objectName = ""
						for (var i = 4; i < strings.length - 1; i++) {
							_this.objectName += strings[i] + '/'
						}
						_this.file_id = strings[strings.length - 1].split('?')[0]
						// 存储在oss里的key
						_this.objectName += _this.file_id;
					}
				})
			},
			uploadFile(params) {
				let file = params.file;
				let _this = this;
				let xhr = new XMLHttpRequest()
				xhr.upload.addEventListener("progress", function(evt) {
					var percentComplete = Math.round(evt.loaded * 100 / evt.total);
					params.onProgress({
						percent: percentComplete
					});
				}, false)
				xhr.open('PUT', this.url, true)
				xhr.send(file)
				xhr.onload = () => {
					if (xhr.status == 200) {
						params.onSuccess("上传成功");
					}
				}
			},
			close() {
				this.$store.commit({
					type: "uploadH"
				})
			}
		}
	}
</script>

<style>
	.em {
		color: #409eff;
		font-style: normal;
	}
</style>
