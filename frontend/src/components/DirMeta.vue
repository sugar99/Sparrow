<template>
	<a-drawer title="目录属性" width=420 :closable="false" @close="close" :visible="dirMetaVisible">
		<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">目录名：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{dirMeta.title}}</span>
			</el-col>
		</el-row>
		<!-- 			<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">创建者：</el-col>
			<el-col :offset="2" :span="16">
				<el-input type="textarea" :rows="5" show-word-limit v-model="docEditMeta.desc" size="small">
				</el-input>
			</el-col>
		</el-row> -->
		<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">创建时间：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{showDirCreatTime}}</span>
			</el-col>
		</el-row>
	</a-drawer>
</template>

<script>
	import { mapState } from 'vuex'
	
	export default {
		name: "DirMeta",
		data() {
			return {
				showDirCreatTime:"yyyy-mm-dd xx:xx:xx"
			}
		},

		computed: {
			...mapState([
				'clickedItemId',
				'dirMetaVisible'
			])
		},
		props:[
			'dirMeta'
		],
		watch:{
			dirMetaVisible(){
				//时间里面有个T,去除一下
				var year = this.dirMeta.created_time.substr(0,10);
				var hoursT = this.dirMeta.created_time.substr(10,3);
				var minAndSecon = this.dirMeta.created_time.substr(13,6);
				var newHours = parseInt(hoursT.substr(1,2))+8;
				this.showDirCreatTime = year +" "+newHours.toString()+minAndSecon;
			}
		},
		methods:{
			close(){
				this.$store.commit({
					type: "dirMetaH"
				});
			}
		}
	}
</script>

<style>
</style>
