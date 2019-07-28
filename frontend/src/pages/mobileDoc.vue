<template>
	<!-- 手机端页面 暂时废弃 -->
	<!-- <el-container v-if="phone">
		<el-header style="height:60px; line-height: 60px; box-shadow: 0px 10px 6px 0 rgba(0,0,0,.05);">
			<el-image :src="require('./assets/logo.png')" style="height: 30px; width: 145px; padding: 15px 0 15px 0; float: left;"></el-image>
	
			<el-dropdown style="float: right;">
				<span style="display: inline-block; font-size: 18px;">
					<i class="el-icon-user-solid"></i>
					lintean
				</span>
	
				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item :command="0">账户</el-dropdown-item>
					<el-dropdown-item :command="1">退出</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</el-header>
	
		<el-main v-if="showDoc">
			<div>
				<el-button type="primary" plain @click="showDoc = false; doc = true;">新建</el-button>
				<el-button plain>搜索</el-button>
			</div>
			<div class="divider" style="margin-top: 10px;"></div>
	
			<el-card v-for="(doc, index) in docList" :key="index" shadow="always" :body-style="{ padding: '0px'}" style="line-height: 100px; padding: 0; margin-top: 10px;">
				<el-image :src="!doc.isDoc ? require('./assets/images/docCnt.png') : require('./assets/images/doc.png')" fit="scale-down"
				 alt="fileImage" style="height:80px; width:80px; float: left; padding: 10px;"></el-image>
				<span class="phoneText" :style="'max-width :' + phoneTextWt + 'px'" @click="showDoc = false">
					{{doc.docName}}</span>
				<el-dropdown style="float: right; margin-right: 10px">
					<span class="el-dropdown-link">
						<i class="el-icon-arrow-down"></i>
					</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item :command="[index,0]">下载</el-dropdown-item>
						<el-dropdown-item :command="[index,1]">重命名</el-dropdown-item>
						<el-dropdown-item :command="[index,2]">删除</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</el-card>
		</el-main>
	
		<el-main ref="phoneCnt" v-if="!showDoc">
			<div>
				<el-button type="primary" @click="showDoc = !showDoc" plain>返回</el-button>
				<el-button plain>上传</el-button>
				<el-button plain @click="doc = !doc">编辑</el-button>
			</div>
			<div class="divider" style="margin-top: 10px;"></div>
	
			<div v-if="!doc">
				<el-card v-for="(file, index) in fileList" :key="index" shadow="always" :body-style="{ padding: '0px'}" style="line-height: 100px; padding: 0; margin-top: 10px;">
					<el-image :src="file.fileImgUrl" fit="scale-down" alt="fileImage" style="height:80px; width:80px; float: left; padding: 10px;"></el-image>
					<span class="phoneText" :style="'max-width :' + phoneTextWt + 'px'">
						{{file.fileName}}</span>
					<el-dropdown style="float: right; margin-right: 10px">
						<span class="el-dropdown-link">
							<i class="el-icon-arrow-down"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item :command="[index,0]">预览</el-dropdown-item>
							<el-dropdown-item :command="[index,1]">删除</el-dropdown-item>
							<el-dropdown-item :command="[index,2]">下载</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</el-card>
			</div>
	
			<div v-if="doc">
				<el-row style="margin-top: 10px;">
					<el-col :offset="1" :span="3">标题</el-col>
					<el-col :offset="2" :span="18">
						<el-input type="text" placeholder="文档的性质或类型" maxlength="10" show-word-limit>
						</el-input>
					</el-col>
				</el-row>
				<el-row style="margin-top: 10px;">
					<el-col :offset="1" :span="3">介绍</el-col>
					<el-col :offset="2" :span="18">
						<el-input type="textarea" placeholder="文档的简介" maxlength="150" show-word-limit rows="10">
						</el-input>
					</el-col>
				</el-row>
				<el-row style="margin-top: 10px;">
					<el-col :offset="1" :span="3">标签</el-col>
					<el-col :offset="2" :span="18">
						<el-input type="text" placeholder="请输入一个标签" maxlength="10" show-word-limit>
						</el-input>
					</el-col>
				</el-row>
				<el-row style="margin-top: 10px;">
					<el-button type="primary" @click="doc = false">保存</el-button>
					<el-button @click="doc = false">取消</el-button>
				</el-row>
			</div>
		</el-main>
	</el-container> -->
</template>

<script>
</script>

<style>
</style>
