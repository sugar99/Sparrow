<template>
	<div class="pdf" v-show="fileType === 'pdf'">
		<p class="arrow">
			<el-button type="text" @click="changePdfPage(0)" class="turn" :class="{grey: currentPage==1}">Preview</el-button>
			{{currentPage}} / {{pageCount}}
			<el-button type="text" @click="changePdfPage(1)" class="turn" :class="{grey: currentPage==pageCount}">Next</el-button>
		</p>
		<pdf :src="src" :page="currentPage" @num-pages="pageCount=$event" 
		@page-loaded="currentPage=$event" @loaded="loadPdfHandler">
		</pdf>
<!-- 		src需要展示的PDF地址
		当前展示的PDF页码
		PDF文件总页码
		一开始加载的页面
		加载事件 -->
	</div>
</template>

<script>
	// pdf预览
	// 暂时没有用上
	
	import pdf from 'vue-pdf'
	export default {
		name: 'ShowPdf',
		components: {
			pdf
		},
		props: {
			src: String, // pdf文件地址
			pageCount: Number // pdf文件总页数
		},
		data() {
			return {
				currentPage: 0, // pdf文件页码
				fileType: 'pdf', // 文件类型
			}
		},
		created: {
			// 有时PDF文件地址会出现跨域的情况,这里最好处理一下
			// this.src = pdf.createLoadingTask(this.src);
		},
		methods: {
			// 改变PDF页码,val传过来区分上一页下一页的值,0上一页,1下一页
			changePdfPage(val) {
				// console.log(val)
				if (val === 0 && this.currentPage > 1) {
					this.currentPage--
					// console.log(this.currentPage)
				}
				if (val === 1 && this.currentPage < this.pageCount) {
					this.currentPage++
					// console.log(this.currentPage)
				}
			},
			// pdf加载时
			loadPdfHandler(e) {
				this.currentPage = 1 // 加载的时候先加载第一页
			}
		}
	}
</script>	

<style>
</style>
