<template>
	<div>
		<video-player class="video-player-box" ref="videoPlayer" :options="playerOptions" :playsinline="true" customEventName="customstatechangedeventname"
		 @play="onPlayerPlay($event)" @pause="onPlayerPause($event)" @ended="onPlayerEnded($event)" @waiting="onPlayerWaiting($event)"
		 @playing="onPlayerPlaying($event)" @loadeddata="onPlayerLoadeddata($event)" @timeupdate="onPlayerTimeupdate($event)"
		 @canplay="onPlayerCanplay($event)" @canplaythrough="onPlayerCanplaythrough($event)" @statechanged="playerStateChanged($event)"
		 @ready="playerReadied">
		</video-player>
	</div>
</template>

<script>
	// 视频预览和音频预览
	// 暂时没有用上
	
	import 'video.js/dist/video-js.css'
	import {
		videoPlayer
	} from 'vue-video-player'

	export default {
		name: 'Video',
		props: {
			src: String,
			state: Number
		},
		data() {
			return {
				playerOptions: {
					// videojs options
					muted: false, // 默认情况下将会消除任何音频。
					language: 'zh-CN',
					playbackRates: [0.7, 1.0, 1.5, 2.0],
					sources: [{
						type: "video/mp4",
						src: ""
					}],
					width: document.documentElement.clientWidth,
					aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
					fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
					notSupportedMessage: '此视频暂无法播放，请稍后再试' // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
				}
			}
		},
		components: {
			videoPlayer
		},
		created: function() {
			this.playerOptions.sources[0].src = this.src;
		},
		computed: {},
		methods: {
			onPlayerPlay(player) {
				// console.log('player play!', player)
			},
			onPlayerPause(player) {
				// console.log('player pause!', player)
			},
			playerStateChanged(playerCurrentState) {
				// console.log('player current update state', playerCurrentState)
			},
			playerReadied(player) {

			},
			onPlayerWaiting(player) {

			},
			onPlayerPlaying(player) {

			},
			onPlayerLoadeddata(player) {

			},
			onPlayerTimeupdate(player) {

			},
			onPlayerCanplay(player) {

			},
			onPlayerCanplaythrough(player) {

			}
		},
		watch: {
			//更改视频源 videoUrl从弹出框组件传值
			src: function(val) {
				if (val !== '') {
					this.$refs.videoPlayer.player.src(val)
				}
			},
			//弹出框关闭后暂停 否则一直在播放 state从弹出框组件传值
			state: function(val) {
				if (val) {
					this.$refs.videoPlayer.player.pause()
				}
			}
		}
	}
</script>

<style>
</style>
