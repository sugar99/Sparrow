<template>
	<div class="cnt_back">
		<el-container class="cnt_width">
			<el-header>
				<div class="input-cnt">
					<el-autocomplete placeholder="请输入内容" class="input-with-select" v-model="keyword" style="width: 100%;"
					 :fetch-suggestions="querySearchAsync" @select="handleSelect">

						<el-dropdown slot="prepend" @command="dropdownClick">
							<el-button type="primary">
								{{typeOnshow}}<i class="el-icon-arrow-down el-icon--right"></i>
							</el-button>
							<el-dropdown-menu slot="dropdown">
								<el-dropdown-item v-for="(searchType, index) in searchTypes" :key="index" :command="searchType">{{searchType.value}}</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>
						<el-button slot="append" icon="el-icon-search" @click="search">搜索</el-button>
					</el-autocomplete>
				</div>
			</el-header>

			<el-main class="cnt_padding" v-loading="loading">
				<section class="selection_cnt">
					<el-row type="flex" class="row_bottom_line">
						<el-col :span="3">
							<span class="notication_padding">类目选择</span>
						</el-col>
						<el-col :span="21">
							<el-checkbox-group v-model="checkedCtgr" class="selection" @change="!loading && search()">
								<el-checkbox v-for="(showCategory, index) in showCategories" :key="index" :label="showCategory.id" border size="medium"
								 style="margin-right: 10px; margin-bottom: 10px;">{{showCategory.title}}</el-checkbox>
							</el-checkbox-group>
							<el-autocomplete :fetch-suggestions="queryCategories" @select="addCategories" v-model="category" placeholder="搜索类目"
							 size="medium" class="input">
							</el-autocomplete>
						</el-col>
					</el-row>

					<el-row type="flex" class="row_bottom_line">
						<el-col :span="3">
							<span class="notication_padding">标签选择</span>
						</el-col>
						<el-col :span="21">
							<el-checkbox-group v-model="checkedTags" class="selection" @change="!loading && search()">
								<el-checkbox v-for="(showTag, index) in showTags" :key="index" :label="showTag.id" border size="medium" style="margin-right: 10px; margin-bottom: 10px;">{{showTag.title}}</el-checkbox>
							</el-checkbox-group>
							<el-autocomplete :fetch-suggestions="queryTags" @select="addTags" v-model="tag" placeholder="搜索标签" size="medium"
							 class="input">
							</el-autocomplete>
						</el-col>
					</el-row>

					<el-row type="flex" class="row_bottom_line">
						<el-col :span="3">
							<span class="notication_padding">文件后缀</span>
						</el-col>
						<el-col :span="21">
							<el-checkbox style="margin-bottom: 10px;" v-model="allTypes" @change="handleCheckAllChange">全选</el-checkbox>
							<el-checkbox-group v-model="defaultTypes" @change="handleCheckedTypesChange" class="selection">
								<el-checkbox v-for="(type, index) in types" :label="type.key" :key="index" style="margin-bottom: 10px;">{{type.key + '(' + type.doc_count +')'}}</el-checkbox>
							</el-checkbox-group>
						</el-col>
					</el-row>

					<el-row type="flex" class="row_bottom_line">
						<el-col :span="3">
							<span class="notication_padding">创建时间</span>
						</el-col>
						<el-col :span="21">
							<el-radio-group @change="!loading && defaultCTime != CTimes.length - 1 && search()" v-model="defaultCTime">
								<el-radio v-for="(ct, index) in CTimes" :key="index" :label="index" style="margin-bottom: 10px;">{{ct.key + '(' + ct.doc_count +')'}}</el-radio>
							</el-radio-group>

							<el-date-picker v-show="defaultCTime == CTimes.length - 1" v-model="CTime" type="daterange" range-separator="-"
							 start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"
							 @change="!loading && search()">
							</el-date-picker>
						</el-col>
					</el-row>

					<el-row type="flex" class="row_top_line">
						<el-col :span="3">
							<span class="notication_padding">修改时间</span>
						</el-col>
						<el-col :span="21">
							<el-radio-group @change="!loading && defaultCTime != CTimes.length - 1 && search()" v-model="defaultETime">
								<el-radio v-for="(et, index) in ETimes" :key="index" :label="index" style="margin-bottom: 10px;">{{et.key + '(' + et.doc_count +')'}}</el-radio>
							</el-radio-group>

							<el-date-picker v-show="defaultETime == ETimes.length - 1" v-model="ETime" type="daterange" range-separator="-"
							 start-placeholder="开始日期" end-placeholder="结束日期" format="yyyy 年 MM 月 dd 日" value-format="yyyy-MM-dd"
							 @change="!loading && search()">
							</el-date-picker>
						</el-col>
					</el-row>
				</section>

				<section class="contain_cnt">
					<div style="height: 50px">
						<div style="float: left;">
							<span>共{{itemTotal}}个搜索结果</span>
						</div>

						<el-button-group style="float: right;">
							<el-button @click="isList = false" :type="isList ? 'normal' : 'primary'">图标</el-button>
							<el-button @click="isList = true" :type="!isList ? 'normal' : 'primary'">列表</el-button>
						</el-button-group>
					</div>

					<div v-if="!isList" class="card_cnt">
						<el-popover placement="right-end" trigger="hover" class="item" width=420 v-for="(item, index) in resultItems"
						 :key="index">
							<el-card slot="reference" @click.native="itemClicked">
								<div class="img_cnt">
									<el-image :src="item.thumbnail" fit="cover" class="img">
										<div slot="error">
											<i class="el-icon-picture-outline"></i>
										</div>
									</el-image>
								</div>
								<span class="card_text" v-html="item.title_highlight">{{item.title_highlight + '.' + item.ext}}</span>
							</el-card>

							<div class="popover_content">
								<el-row style="margin-top: 10px;">
									<el-col :offset="1" :span="5" style="text-align: right;">文件名：</el-col>
									<el-col :offset="2" :span="16">
										<span v-html="item.title_highlight">{{item.title_highlight + '.' + item.ext}}</span>
									</el-col>
								</el-row>
								<el-row style="margin-top: 10px;">
									<el-col :offset="1" :span="5" style="text-align: right;">创建时间：</el-col>
									<el-col :offset="2" :span="16">
										<span>{{item.created_time}}</span>
									</el-col>
								</el-row>
								<el-row style="margin-top: 10px;">
									<el-col :offset="1" :span="5" style="text-align: right;">修改时间：</el-col>
									<el-col :offset="2" :span="16">
										<span>{{item.modified_time}}</span>
									</el-col>
								</el-row>
								<el-row style="margin-top: 10px;">
									<el-col :offset="1" :span="5" style="text-align: right;">简介：</el-col>
									<el-col :offset="2" :span="16">
										<span v-for="(desc, index) in item.desc_highlights" :key="index" v-html="desc">
											{{desc}}
										</span>
									</el-col>
								</el-row>
								<el-row style="margin-top: 10px;" v-if="item.content_highlights != null">
									<el-col :offset="1" :span="5" style="text-align: right;">内容：</el-col>
									<el-col :offset="2" :span="16">
										<span v-for="(content, index) in item.content_highlights" :key="index" v-html="content">
											{{content}}
										</span>
									</el-col>
								</el-row>
							</div>
						</el-popover>
					</div>

					<div v-if="isList" class="card_cnt">
						<el-row v-for="(item, index) in resultItems" :key="index" style="height: 220px; width: 100%; padding: 10px; overflow: hidden;">
							<el-col :span="5">
								<el-image :src="item.thumbnail" fit="cover" style="max-height: 200px;">
									<div slot="error" class="image-slot">
										<i class="el-icon-picture-outline image-slot-icon"></i>
									</div>
								</el-image>
							</el-col>
							<el-col :offset="1" :span="18">
								<h3 v-html="item.title_highlight" style="color: #08c;">{{item.title_highlight + '.' + item.ext}}</h3>
								<span v-for="(desc, index) in item.desc_highlights" :key="index" class="list_text" v-html="desc" style="color: #666; font-weight: 300;">
									{{desc}}
								</span>
								<span v-for="(content, index) in item.content_highlights" :key="index" v-html="content">
									{{content}}
								</span>
							</el-col>
						</el-row>
					</div>

					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
					 :page-sizes="[10, 20, 30, 40]" :page-size="10" layout="sizes, prev, pager, next" :total="itemTotal" style="text-align: center;">
					</el-pagination>
				</section>
			</el-main>

		</el-container>
	</div>
</template>

<script>
	import * as Api from '../api/api'
	import * as DEFAULT from "../json/default"

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
				searchTypes: DEFAULT.typeOptions,
				typeOnshow: '全部',
				typeCommand: 'all',

				// 后缀名时间单多选
				types: DEFAULT.extOptions,
				allTypes: true,
				defaultTypes: [],
				CTimes: DEFAULT.timeOptions,
				ETimes: DEFAULT.timeOptions,
				defaultCTime: 0,
				defaultETime: 0,
				// 自定义时间
				CTime: '',
				ETime: '',
				// 结果展示
				resultItems: [],
				// 类目和标签搜索和展示
				category: '',
				tag: '',
				categorySuggestions: [],
				tagSuggestions: [],
				categoryBackup: [],
				tagBackup: [],

				checkedCtgr: [],
				showCategories: DEFAULT.cate,
				checkedTags: [],
				showTags: DEFAULT.tg,
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

				//图表、列表模式
				isList: false,

				// loading
				loading: false,
				// 是否正在搜索 用loading代替
				// Searching: false,
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
		watch: {},
		methods: {
			itemClicked() {},
			handleCheckAllChange(value) {
				// console.log(value);
				let temp = [];
				for (let i = 0; i < this.types.length; ++i) {
					temp.push(this.types[i].key);
				}
				this.defaultTypes = (value ? temp : []);

				this.search();
			},
			handleCheckedTypesChange(value) {
				let checkedCount = value.length;
				if (checkedCount < this.types.length) this.allTypes = false;

				this.search();
			},
			handleSizeChange(val) {
				this.currentPageSize = val;
				this.search();
			},
			handleCurrentChange(val) {
				this.currentPage = val;
				this.search();
			},
			// 搜索建议部分
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
				// loading动画
				this.loading = true;
				this.Searching = true;

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
							else _this.loading = false;
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

				this.search();
			},
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

							_this.loading = false;
						} else {
							alert(res.data.msg);
						}
					}).catch(err => {
					console.log(err);
				});
			},
			// 关键字不改变的情况下更新后缀名数组
			updateExts(arr) {
				let originLength = arr.length;
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
				console.log("originLength" + originLength);
				console.log(defaultKeys);
				console.log(temp);
				this.defaultTypes = temp;

				this.types = arr;
			},
			dataInit() {
				// 数据初始化
				this.allTypes = true;
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
				// 选中值是否在原数组里
				let isContain = false;
				let i;
				for (i = 0; i < this.showCategories.length; ++i)
					if (this.showCategories[i].title == item.value) {
						isContain = true;
						break;
					}

				// 在就将原数组的对应值选上（已经选上就不用选了
				// 不在就添加然后选上
				if (isContain) {
					if (this.checkedCtgr.indexOf(item.id) == -1) this.checkedCtgr.push(item.id);
				} else {
					this.showCategories.push(this.categoryBackup[item.index]);
					this.checkedCtgr.push(item.id);
				}

				this.search();
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
				// 选中值是否在原数组里
				let isContain = false;
				let i;
				for (i = 0; i < this.showTags.length; ++i)
					if (this.showTags[i].title == item.value) {
						isContain = true;
						break;
					}

				// 在就将原数组的对应值选上（已经选上就不用选了
				// 不在就添加然后选上
				if (isContain) {
					if (this.checkedTags.indexOf(item.id) == -1) this.checkedTags.push(item.id);
				} else {
					this.showTags.push(this.tagBackup[item.index]);
					this.checkedTags.push(item.id);
				}

				this.search();
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
		width: 80%;
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
		text-align: left;
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

	.list_text {
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 3;
		overflow: hidden;
		margin-top: 5px;
	}

	.card_cnt {
		display: flex;
		justify-content: flex-start;
		flex-wrap: wrap;
	}

	.card_cnt em,
	.popover_content em {
		color: #dd4b39 !important;
		font-style: normal;
		font-weight: normal;
	}

	.cnt_back {
		background: url("https://gw.alipayobjects.com/zos/rmsportal/TVYTbAXWheQpRcWDaDMu.svg") no-repeat 50%;
		background-size: 100%;
		background-origin: padding-box;
		background-repeat: repeat-y;
		position: relative;
		color: #000000;
	}

	.cnt_width {
		width: 70%;
		min-width: 800px;
		margin: 0 auto;
	}

	.image-slot {
		height: 150px;
		width: 150px;
		line-height: 150px;
		text-align: center;
	}

	.image-slot-icon {
		font-size: 50px;
	}
</style>
