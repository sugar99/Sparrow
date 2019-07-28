<template>
	<el-card :body-style="{ padding: '0px'}">
		<el-image :src="doc.thumbnail_url" fit="scale-down" alt="fileImage" class="image"></el-image>

		<div class="divider" style="margin: 0 0 5px 0;"></div>

		<div style="height: 50px; line-height: 50px; text-align: center;" @click="edit">
			<span style="font-size: 14px; height: 50px; line-height: 50px;" v-show="!doc.isEditStatus">
				{{doc.ext != null? doc.title + '.' + doc.ext: doc.title}}
			</span>
			<el-input ref="changeName" v-show="doc.isEditStatus" class="changeName" size="medium" v-model="doc.title"
			 @keyup.enter.native.stop="enter" @blur.stop="changeName" @click.native.stop="getFocus"></el-input>
		</div>
	</el-card>
</template>

<script>
	import * as Api from '../api/api'
	import * as DEFAULT from "../json/default"

	export default {
		name: "ResourceCard",
		props: [
			'doc',
			'index'
		],
		data() {
			return {
				fileEditMeta: {},
				docEditMeta: {},
				primaryName: ""
			}
		},
		watch: {
			editStatus(newOne, oldOne) {
				if (newOne) {
					let _this = this;
					setTimeout(function() {
						_this.$refs.changeName.focus();
						_this.primaryName = _this.doc.title;
					}, 1)
				}
			}
		},
		computed: {
			editStatus() {
				return this.doc.isEditStatus;
			}
		},
		methods: {
			edit() {
				if (!this.doc.isEditStatus && this.$parent.isIndexClicked(this.index)) {
					this.$parent.callChangeName();
				}
			},
			getFocus() {

			},
			enter() {
				this.$refs.changeName.blur();
			},
			changeName() {
				if (this.doc.type == "doc") {
					this.changeDocName();
				} else if (this.doc.type == "dir") {
					this.editResources();
				} else {
					this.changeFileName();
				}
			},
			editResources() {
				let _this = this;
				Api.editDirMeta(this.doc.id, this.doc.title).then(
					res => {
						if (res.data.status === 200) {
							_this.changeSuccess();
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.changeError(err);
				});
			},
			changeDocName() {
				let _this = this;
				Api.getDocMeta(this.doc.id).then(
					res => {
						if (res.data.status === 200) {
							_this.docEditMeta = res.data.data;
							_this.docEditMeta.title = _this.doc.title;
							_this.updateDocMeta();
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.changeError(err);
				});
			},
			changeFileName() {
				let _this = this;
				Api.getFileMeta(this.doc.id).then(
					res => {
						if (res.data.status === 200) {
							_this.fileEditMeta = res.data.data;
							_this.fileEditMeta.title = _this.doc.title;
							_this.updateFileMeta();
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.changeError(err);
				});
			},
			updateFileMeta() {
				let _this = this;
				let temp = this.fileEditMeta;
				Api.editFileMeta(temp.id, temp.title, temp.desc, temp.categories, temp.tags).then(
					res => {
						if (res.data.status === 200) {
							_this.changeSuccess();
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.changeError(err);
				});
			},
			updateDocMeta() {
				let _this = this;
				Api.editDocMeta(this.doc.id, this.docEditMeta.title, this.docEditMeta.desc).then(
					res => {
						if (res.data.status === 200) {
							_this.changeSuccess();
						} else {
							_this.$message.error(res.data.msg);
						}
					}).catch(err => {
					_this.changeError(err);
				});
			},
			changeSuccess() {
				this.$message.success("改名成功");
				this.$parent.stopEditNameByIndex(this.index);
			},
			changeError(err) {
				console.log(err);
				this.$message.warning(DEFAULT.defaultNetwordError);
				this.$parent.stopEditNameByIndex(this.index);
			}
		}
	}
</script>

<style>
	.changeName {
		width: 80%;
	}
</style>
