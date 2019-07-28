<template>
	<div>
		<el-container>
			<el-header>
				<div class="input-cnt-phone">
					<el-autocomplete placeholder="请输入内容" size="small" class="input-with-select" v-model="keyword" style="width: 100%;"
					 :fetch-suggestions="querySearchAsync" @select="handleSelect">

						<el-dropdown slot="prepend" @command="dropdownClick">
							<el-button type="primary">
								{{typeOnshow}}<i class="el-icon-arrow-down el-icon--right"></i>
							</el-button>
							<el-dropdown-menu slot="dropdown">
								<el-dropdown-item v-for="(searchType, index) in searchTypes" :key="index" :command="searchType">{{searchType.value}}</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>
						<el-button slot="append" icon="el-icon-search" @click="test"></el-button>
					</el-autocomplete>
				</div>
			</el-header>
			<el-main class="cnt_padding">
				<div style="height: 35px">
					<el-button @click="phoneSVisible = true" icon="el-icon-set-up el-icon--right" type="primary" size="small" style="float: right;">筛选条件</el-button>
					<h2>搜索结果</h2>
				</div>
				<div class="dd"></div>

				<section>
					<div style="display: flex; justify-content: flex-start; flex-wrap: wrap;">
						<el-card @click.native="itemClicked" v-for="(item, index) in resultItems" :key="index" class="item_phone">
							<el-image :src="item.thumbnail" fit="cover" style="width:100%; height: ;">
							</el-image>
							<span>{{item.title}}</span>
						</el-card>
					</div>
					<el-pagination small @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
					 :page-size="10" layout="prev, pager, next" :total="400" style="text-align: center;">
					</el-pagination>

				</section>
			</el-main>
		</el-container>

		<!-- 筛选条件抽屉 -->
		<a-drawer placement="right" :closable="false" @close="phoneSVisible = false" :visible="phoneSVisible">

			<el-collapse v-model="activeNames">
				<el-collapse-item title="类目" name="类目">
					<el-checkbox-group v-model="checkedCtgr" class="checkbox_group_phone">
						<el-checkbox v-for="(showCategory, index) in showCategories" :key="index" :label="showCategory.id" border size="medium"
						 class="checkbox_phone">{{showCategory.title}}</el-checkbox>
					</el-checkbox-group>
					<el-input v-model="category" placeholder="类目搜索框" size="medium" class="input"></el-input>
				</el-collapse-item>
				<el-collapse-item title="标签" name="标签">
					<el-checkbox-group v-model="checkedTags" class="checkbox_group_phone">
						<el-checkbox v-for="(showTag, index) in showCategories" :key="index" :label="showTag.id" border size="medium"
						 class="checkbox_phone">{{showTag.title}}</el-checkbox>
					</el-checkbox-group>
					<el-input v-model="tag" placeholder="标签搜索框" size="medium" class="input"></el-input>
				</el-collapse-item>
				<el-collapse-item title="文件后缀" name="文件后缀">
					<el-checkbox :indeterminate="isIndeterminate" class="checkbox_phone" border v-model="allTypes" @change="handleCheckAllChange">全选</el-checkbox>
					<el-checkbox-group v-model="defaultTypes" @change="handleCheckedTypesChange" class="checkbox_group_phone">
						<el-checkbox v-for="(type, index) in types" border :label="type.key" :key="index" class="checkbox_phone"></el-checkbox>
					</el-checkbox-group>
				</el-collapse-item>
				<el-collapse-item title="创建时间" name="创建时间">
					<el-radio-group v-model="defaultCTime" class="checkbox_group_phone">
						<el-radio v-for="(ct, index) in CTimes" border :key="index" :label="ct.key" class="checkbox_phone"></el-radio>
					</el-radio-group>

					<el-date-picker v-model="CTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
					</el-date-picker>
				</el-collapse-item>
				<el-collapse-item title="修改时间" name="修改时间">
					<el-radio-group v-model="defaultETime" class="checkbox_group_phone">
						<el-radio v-for="(et, index) in ETimes" border :key="index" :label="et.key" class="checkbox_phone"></el-radio>
					</el-radio-group>

					<el-date-picker v-model="ETime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
					</el-date-picker>
				</el-collapse-item>
			</el-collapse>
		</a-drawer>
	</div>
</template>

<script>
	import * as Api from '../api/api'

	const extOptions = [{
			"key": "jpg",
			"doc_count": 50
		},
		{
			"key": "png",
			"doc_count": 30
		},
		{
			"key": "gif",
			"doc_count": 20
		}
	];
	const timeOptions = [{
			"key": "全部",
			"doc_count": 100
		},
		{
			"key": "三天内",
			"doc_count": 6
		},
		{
			"key": "一周内",
			"doc_count": 15
		},
		{
			"key": "一个月内",
			"doc_count": 23
		},
		{
			"key": "三个月内",
			"doc_count": 25
		},
		{
			"key": "半年内",
			"doc_count": 27
		},
		{
			"key": "一年内",
			"doc_count": 50
		},
		{
			"key": "一年前",
			"doc_count": 50
		}
	];
	const typeOptions = [{
		value: '全部',
		command: 'all'
	}, {
		value: '图片',
		command: 'image'
	}, {
		value: '文档',
		command: 'doc'
	}, {
		value: '视频',
		command: 'video'
	}, {
		value: '音频',
		command: 'audio'
	}, {
		value: '其他文件',
		command: 'others'
	}];
	const cate = [{
			"id": 6,
			"title": "科技"
		},
		{
			"id": 1,
			"title": "文学"
		},
		{
			"id": 2,
			"title": "流行"
		},
		{
			"id": 3,
			"title": "文化"
		},
		{
			"id": 4,
			"title": "生活"
		},
		{
			"id": 5,
			"title": "经管"
		}
	];
	const tg = [{
			"id": 137,
			"title": "算法"
		},
		{
			"id": 2998,
			"title": "计算机"
		},
		{
			"id": 2697,
			"title": "计算机科学"
		},
		{
			"id": 66,
			"title": "哲学"
		},
		{
			"id": 133,
			"title": "编程"
		}
	]

	export default {
		name: "Search",
		data() {
			return {
				// header搜索栏
				searchType: [],
				keyword: '',
				keyword_backup: '',
				isKeywordChanged: false,
				searchSuggestions: [],
				searchTypes: typeOptions,
				typeOnshow: '全部',
				typeCommand: 'all',

				// 后缀名时间单多选
				types: extOptions,
				isIndeterminate: false,
				allTypes: true,
				defaultTypes: [],
				CTimes: timeOptions,
				ETimes: timeOptions,
				defaultCTime: 0,
				defaultETime: 0,
				// 自定义时间
				CTime: '',
				ETime: '',
				// 结果展示
				resultItems: [{
						title: '文件文件文件文件文件文件文件文件文件文件文件文件文件文件文件文件文件',
						thumbnail: "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg"
					},
					{
						title: '文件1',
						thumbnail: "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg"
					},
					{
						title: '文件1',
						thumbnail: "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg"
					},
					{
						title: '文件1',
						thumbnail: "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg"
					},
					{
						title: '文件1',
						thumbnail: "http://douban-test.oss-cn-beijing.aliyuncs.com/img/10432347.jpeg"
					}
				],
				// 类目和标签搜索和展示
				category: '',
				tag: '',
				categorySuggestions: [],
				tagSuggestions: [],
				categoryBackup: [],
				tagBackup: [],

				checkedCtgr: [],
				showCategories: cate,
				checkedTags: [],
				showTags: tg,
				// 已选条件展示（废弃
				selectedOptions: [
					"XXXXX",
					"AAAAA",
					"GGGGGGGGGG"
				],
				// 分页
				currentPage: 1,
				currentPageSize: 10,
				itemTotal: 0,

				// 路由
				currentResourceId: null,

				// 以下是手机端专属参数
				// 筛选条件抽屉
				phoneSVisible: false,
				activeNames: ['类目', '标签']
			}
		},
		mounted: function() {
			// 路由检测
			if (this.$route.query.keyword != null) {
				this.keyword = this.$route.query.keyword;
				this.currentResourceId = this.$route.query.resourceId;
				this.search();
			}
		},
		methods: {
			itemClicked() {
				alert("itemClicked");
			},
			handleCheckAllChange(value) {
				// console.log(value);
				let temp = [];
				for (let i = 0; i < this.types.length; ++i){
					temp.push(this.types[i].key);
				}
				this.defaultTypes = (value ? temp : []);
				this.isIndeterminate = false;
			},
			handleCheckedTypesChange(value) {
				let checkedCount = value.length;
				this.isIndeterminate = (checkedCount > 0 && checkedCount <= this.types.length);
				if (checkedCount < this.types.length) this.allTypes = false;
			},
			handleSizeChange(val) {
				this.currentPageSize = val;
				this.search();
			},
			handleCurrentChange(val) {
				this.currentPage = val;
				this.search();
			},
			// 搜索部分
			querySearchAsync(queryString, cb) {
				// cb(results);网络请求部分
				let _this = this;
				Api.Suggestions(this.typeCommand, this.keyword, 10).then(
					res => {
						if (res.data.status === 200) {
							// 数组清空
							_this.searchSuggestions.splice(0, _this.searchSuggestions.length);
							for (let i = 0; i < res.data.data.length; ++i) {
								let temp = {
									value: res.data.data[i]
								};
								_this.searchSuggestions.push(temp);
							}
							cb(_this.searchSuggestions);
						} else {
							alert(res.data.msg);
						}
					}).catch(err => {
					console.log(err);
				});
			},
			handleSelect(item) {
				// 点击后处理
				this.keyword = item.value;
				this.search();
			},
			search() {
				if (this.keyword != this.keyword_backup) this.isKeywordChanged = true;
				else this.isKeywordChanged = false;
				this.keyword_backup = this.keyword;

				if (this.isKeywordChanged) this.dataInit();

				// 点击后处理
				let _this = this;
				let exts = [];
				if (this.allTypes) exts = ['all'];
				else exts = this.defaultTypes;

				let created_time;
				if (this.defaultCTime == this.CTimes.length - 1) {
					created_time = {
						from: this.CTime[0],
						to: this.CTime[1]
					};
				} else {
					created_time = {
						from: (this.CTimes[this.defaultCTime] != null ? this.CTimes[this.defaultCTime].from_as_string : null),
						to: this.CTimes[this.defaultCTime] != null ? this.CTimes[this.defaultCTime].to_as_string : null
					};
				}

				let modified_time;
				if (this.defaultETime == this.ETimes.length - 1) {
					modified_time = {
						from: this.ETime[0],
						to: this.ETime[1]
					};
				} else {
					modified_time = {
						from: this.ETimes[this.defaultETime] != null ? this.ETimes[this.defaultETime].from_as_string : null,
						to: this.ETimes[this.defaultETime] != null ? this.ETimes[this.defaultETime].to_as_string : null
					};
				}


				Api.Results(this.typeCommand, this.keyword, this.checkedTags, this.checkedCtgr, exts, created_time, modified_time,
					"+8", this.currentPage, this.currentPageSize).then(
					res => {
						if (res.data.status === 200) {
							// 结果数组
							_this.itemTotal = res.data.data.total;
							_this.resultItems.splice(0, _this.resultItems.length);
							_this.resultItems = res.data.data.results;

							// 后缀名数组更新
							_this.types.splice(0, _this.types.length);
							if (_this.isKeywordChanged) {
								_this.defaultTypes.splice(0, _this.defaultTypes.length);
								_this.isIndeterminate = false;
								_this.allTypes = true;
								_this.types = res.data.data.group_by_ext.slice(1);

								for (let i = 0; i < _this.types.length; ++i)
									_this.defaultTypes.push(_this.types[i].key);
							} else {
								_this.updateExts(res.data.data.group_by_ext);
							}

							// 创建时间数组更新
							let reset = true;
							for (let j = 0; j < res.data.data.group_by_created_time.length; ++j)
								if (!_this.isKeywordChanged && _this.CTimes[_this.defaultCTime] != null && _this.CTimes[_this.defaultCTime].key ==
									res.data.data.group_by_created_time[j].key) {
									_this.defaultCTime = j;
									reset = false;
									break;
								}
							if (reset) _this.defaultCTime = 0;
							_this.CTimes.splice(0, _this.CTimes.length);
							_this.CTimes = res.data.data.group_by_created_time;

							// 修改时间数组更新
							reset = true;
							for (let j = 0; j < res.data.data.group_by_modified_time.length; ++j)
								if (!_this.isKeywordChanged && _this.ETimes[_this.defaultETime] != null && this.ETimes[_this.defaultETime].key ==
									res.data.data.group_by_modified_time[j].key) {
									_this.defaultETime = j;
									reset = false;
									break;
								}
							if (reset) _this.defaultETime = 0;
							_this.ETimes.splice(0, _this.ETimes.length);
							_this.ETimes = res.data.data.group_by_modified_time;

							if (_this.isKeywordChanged) _this.getTAG();
						} else {
							alert(res.data.msg);
						}
					}).catch(err => {
					console.log(err);
				});
			},
			// 下拉框选择
			dropdownClick(searchType) {
				this.typeOnshow = searchType.value;
				this.typeCommand = searchType.command;
			},
			test() {

			},
			// UTC转GMC+8 已废弃
			// 			changeTime(dt) {
			// 				if (dt == null) return;
			// 				return [
			// 					[dt.getFullYear(), dt.getMonth() + 1, dt.getDate()].join('-'),
			// 					[dt.getHours(), dt.getMinutes(), dt.getSeconds()].join(':')
			// 				].join(' ').replace(/(?=\b\d\b)/g, '0');
			// 			},
			getTAG() {
				let _this = this;
				// 获取类目和标签
				Api.Associations(this.keyword, 5, 5).then(
					res => {
						if (res.data.status === 200) {
							// 类目数组清空
							_this.showCategories.splice(0, _this.showCategories.length);
							_this.checkedCtgr.splice(0, _this.checkedCtgr.length);
							_this.showCategories = res.data.data.categories;

							// 标签数组清空
							_this.checkedTags.splice(0, _this.checkedTags.length);
							_this.showTags.splice(0, _this.showTags.length);
							_this.showTags = res.data.data.tags;
						} else {
							alert(res.data.msg);
						}
					}).catch(err => {
					console.log(err);
				});
			},
			// 关键字不改变的情况下更新后缀名数组
			updateExts(arr) {
				arr = arr.slice(1);

				let temp = [];
				let defaultKeys = this.defaultTypes;
				let arrKeys = [];
				for (let i = 0; i < arr.length; ++i)
					arrKeys.push(arr[i].key);
				for (let i = 0; i < defaultKeys.length; ++i) {
					if (arrKeys.indexOf(defaultKeys[i]) != -1) {
						temp.push(this.defaultTypes[i]);
					}
				}
				let allChecked = (defaultKeys.length == temp.length);
				this.defaultTypes = temp;

				// 全选按钮更新
				if (arrKeys.length == 0 || allChecked) {
					this.isIndeterminate = false;
					this.allTypes = true;
				} else {
					this.isIndeterminate = true;
					this.allTypes = false;
				}
				this.types = arr;
			},
			dataInit() {
				// 数据初始化
				this.allTypes = true;
				this.isIndeterminate = false
				this.defaultTypes = [];
				this.CTimes = [];
				this.ETimes = [];
				this.defaultCTime = null;
				this.defaultETime = null;
				this.CTime = [];
				this.ETime = [];

				this.showCategories = [];
				this.showTags = [];
				this.checkedCtgr = [];
				this.checkedTags = [];
			},
			queryCategories(queryString, cb) {
				// 网络请求部分
				let _this = this;
				Api.Categories(this.category, 5).then(
					res => {
						if (res.data.status === 200) {
							_this.categoryBackup = res.data.data;
							// 数组清空
							_this.categorySuggestions.splice(0, _this.searchSuggestions.length);
							for (let i = 0; i < res.data.data.length; ++i) {
								let temp = {
									value: res.data.data[i].title,
									id: res.data.data[i].id,
									desc: res.data.data[i].desc,
									index: i
								};
								_this.categorySuggestions.push(temp);
							}
							cb(_this.categorySuggestions);
						} else {
							alert(res.data.msg);
						}
					}).catch(err => {
					console.log(err);
				});
			},
			addCategories(item) {
				this.showCategories.push(this.categoryBackup[item.index]);
			},
			queryTags(queryString, cb) {
				// 网络请求部分
				let _this = this;
				Api.Tags(this.tag, 5).then(
					res => {
						if (res.data.status === 200) {
							_this.tagBackup = res.data.data;
							// 数组清空
							_this.tagSuggestions.splice(0, _this.searchSuggestions.length);
							for (let i = 0; i < res.data.data.length; ++i) {
								let temp = {
									value: res.data.data[i].title,
									id: res.data.data[i].id,
									desc: res.data.data[i].desc,
									index: i
								};
								_this.tagSuggestions.push(temp);
							}
							cb(_this.tagSuggestions);
						} else {
							alert(res.data.msg);
						}
					}).catch(err => {
					console.log(err);
				});
			},
			addTags(item) {
				this.showTags.push(this.tagBackup[item.index]);
			},
			turnToDoc() {
				this.$router.push({
					path: '/doc',
				})
			}
		},
	}
</script>

<style>
	.input-with-select .el-input-group__prepend {
		background-color: #fff;
	}

	.input-cnt {
		width: 60%;
		margin: 10px auto 0 auto;
	}

	.input-cnt-phone {
		width: 100%;
		margin: 10px auto 0 auto;
	}

	.el-select .el-input {
		width: 130px;
	}

	.block {
		display: block !important;
	}

	.marT {
		margin-top: 20px;
		padding: 10px;
		padding-left: 50px;
	}

	.line {
		border: #303133 1px solid;
		padding: 10px;
		margin-top: 10px;
	}

	.line_pure {
		border: #303133 1px solid;
	}

	.item {
		width: 16%;
		display: inline-block;
		margin: 2%;
		cursor: pointer;
		text-align: center;
	}

	.item_phone {
		width: 46%;
		display: inline-block;
		margin: 2%;
		cursor: pointer;
		text-align: center;
	}

	.selection_cnt {
		border: 1px solid #e8e8e8;
	}

	.input {
		display: inline-block;
		width: 150px !important;
	}

	.selection {
		display: inline-block !important;
	}

	.cnt_padding {
		padding-left: 5% !important;
		padding-right: 5% !important;
	}

	.notication_padding {
		padding-left: 10%;
	}

	.row_bottom_line {
		border-bottom: 1px dashed #e8e8e8;
		padding-top: 10px;
		padding-bottom: 10px;
		margin-bottom: 10px;
	}

	.row_top_line {
		padding-top: 10px;
		padding-bottom: 10px;
	}

	.selection_header {
		margin-bottom: 20px;
	}

	.contain_cnt {
		margin-top: 20px;
	}

	.checkbox_phone {
		margin: 0 10px 5px 10px !important;
	}

	.checkbox_group_phone {
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
	}

	.ant-drawer-body {
		padding: 12px !important;
	}

	.dd {
		display: block;
		height: 1px;
		width: 100%;
		margin: 10px 0;
		background-color: #DCDFE6;
		position: relative;
		box-sizing: border-box;
	}

	.img {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
	}

	.img_cnt {
		position: relative;
		width: 100%;
		height: 0px;
		padding-top: 100%;
	}

	.card_text {
		margin-top: 5%;
		font-size: 1.25vw;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		overflow: hidden;
	}
</style>
