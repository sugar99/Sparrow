<template>
  <div id="app">
    <el-container>
      <el-header style="height:60px; line-height: 60px; box-shadow: 0px 5px 6px 0 rgba(0,0,0,.05);">
        <el-image
          :src="require('../assets/logo.png')"
          style="height: 50px; width: 240px; padding: 5px 0; float: left;"
        ></el-image>

        <div style="float: right; " v-if="(loginData.isLogin)">
          <el-popover trigger="hover" placement="bottom" width="150">
            <p>欢迎使用 {{loginData.currentUserName}}</p>
            <el-button @click="logout" style="float: right;">注销</el-button>
            <el-button
              slot="reference"
              style="font-size: 18px; color: #303133;"
              type="text"
              icon="el-icon-user-solid"
            >{{loginData.currentUserName}}</el-button>
          </el-popover>
        </div>

        <div style="float: right; " v-if="(!loginData.isLogin)">
          <el-button
            style="font-size: 16px;"
            size="small"
            type="danger"
            @click="loginData.visible = true"
          >登陆</el-button>
        </div>
      </el-header>

      <el-main :style="{minHeight: screenHeight - 120 + 'px'}" class="init">
        <div class="file_cnt" :style="{minHeight: screenHeight - 230 + 'px'}">
          <div style="height: 50px; line-height: 50px;">
            <div style="float: left; font-size: 18px; margin-left: 20px;">
              <i class="el-icon-document"></i>
              <span>{{fileMeta.title + "." + fileMeta.ext}}</span>
            </div>
            <el-button
              @click="download"
              style="float: right; margin-top: 8px; margin-right: 20px;"
              type="primary"
              plain
            >下载文件</el-button>
          </div>

          <div class="divider_cnt"></div>

          <div>
            <!-- 预览模块 除了图片都有问题-->
            <!-- <show-pdf
              v-if="fileMeta.type == 'doc'"
              :src="url"
              :page-count="count"
              style="height:auto; width: 100%; margin: 0 auto;"
            ></show-pdf> -->
            <Video
              v-if="fileMeta.type == 'video' || fileMeta.type == 'audio '"
              :src="url"
              :state="state"
              style="height:auto; width: 100%;"
            ></Video>
            <el-image v-else-if="fileMeta.type == 'image'" :src="url"></el-image>

            <div v-else style="margin: 150px auto 0 auto;">
              <el-image :src="require('../assets/logo.png')" style="width: 200px;"></el-image>
              <h1>该文件类型目前无法预览</h1>
            </div>

          </div>
        </div>
      </el-main>
      <el-footer
        style="height: 60px; line-height: 60px; box-shadow: 0px -5px 6px 0px rgba(0,0,0,.05);"
      >{{footerText}}</el-footer>
    </el-container>

    <el-dialog title="登陆" :visible.sync="loginData.visible" width="400px" style="text-align: left;">
      <el-image
        :src="require('../assets/logo.png')"
        style="height:40px; width: 193px; margin: 0 auto; display: block;"
      ></el-image>
      <el-form
        label-position="right"
        label-width="80px"
        style="margin-right: 50px; margin-top: 30px"
        :model="loginData"
        :rules="loginData.rules"
      >
        <el-form-item label="工号" prop="user">
          <el-input v-model="loginData.user"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pwd">
          <el-input v-model="loginData.pwd" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="loginData.visible = false">取 消</el-button>
        <el-button type="primary" @click="login">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as Api from "../api/api";
import * as DEFAULT from "../json/default";
// import ShowPdf from '../components/ShowPdf.vue'
// import Video from '../components/Video.vue'
import * as Download from "../util/download";

export default {
  name: "file",
  data() {
    return {
      // 手机端 还未完成
      phone: false,
      screenHeight: 0,
      // 注册 已废弃
      // 登陆
      loginData: {
        visible: false,
        logoutVisible: false,
        isLogin: false,
        currentUserName: "lintean",
        currentUserNo: "",
        currentUserId: "",
        currentUserEmail: "",
        user: "",
        pwd: "",
        rules: {
          user: [
            {
              required: true,
              message: "请输入工号",
              trigger: "blur"
            }
          ],
          pwd: [
            {
              required: true,
              message: "请输入密码",
              trigger: "blur"
            }
          ]
        }
      },

      // 文件
      fileId: "",
      fileMeta: DEFAULT.fm,
      url: "",
      state: 1,
      count: 1,

      // 页脚内容
      footerText: "华喜科技 @2019",

      // 侧边栏
      collapsed: false
    };
  },
  components: {
    // 'show-pdf': ShowPdf,
    // 'Video': Video
  },
  mounted: function() {
    // 判断是否是手机访问
    this.phone = this.isPhone();

    // 获取屏幕高度
    this.screenHeight = document.documentElement.clientHeight;

    let _this = this;
    Api.getUser()
      .then(res => {
        if (res.data.status === 200) {
          _this.recordLoginData(res);
        } else {
          _this.loginData.visible = true;
        }
      })
      .catch(err => {
        _this.loginData.visible = true;
        console.log(err);
        _this.$message.warning(DEFAULT.defaultNetwordError);
      });
  },
  methods: {
    isPhone() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
    recordLoginData(res) {
      let _this = this;
      // 登陆信息
      _this.loginData.currentUserName = res.data.data.userInfo.username;
      _this.loginData.currentUserNo = res.data.data.userInfo.work_no;
      _this.loginData.currentUserEmail = res.data.data.userInfo.email;
      _this.loginData.isLogin = true;

      // 检测路由
      if (this.$route.query.fileId != null) {
        this.fileId = this.$route.query.fileId;
        this.getFileMeta();
        console.log("fileId " + this.$route.query.fileId);
      }
    },
    login() {
      let _this = this;
      Api.Login(this.loginData.user, this.loginData.pwd)
        .then(res => {
          if (res.data.status === 200) {
            _this.recordLoginData(res);
            _this.loginData.visible = false;
          } else {
            _this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err);
          _this.$message.warning(DEFAULT.defaultNetwordError);
        });
    },
    logout() {
      let _this = this;
      Api.Logout()
        .then(res => {
          if (res.data.status === 200) {
            _this.loginData.isLogin = false;
            _this.loginData.visible = true;
          } else {
            _this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err);
          _this.$message.warning(DEFAULT.defaultNetwordError);
        });
    },
    getFileMeta() {
      let _this = this;
      Api.getFileMeta(this.fileId)
        .then(res => {
          if (res.data.status === 200) {
            _this.fileMeta = res.data.data;
            _this.getUrl();
          } else {
            _this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          _this.handleError(err);
        });
    },
    handleError(err) {
      console.log(err);
      this.$message.warning(DEFAULT.defaultNetwordError);
    },
    download() {
      let notify = this.$notify({
        title: "后台下载",
        message: "文件后台下载中，下载时间受网络影响，请耐心等候",
        type: "info",
        duration: 0
      });
      let _this = this;

      Download.download(
        this.url,
        this.fileMeta.title + "." + this.fileMeta.ext,
        () => {
          notify.close();
          setTimeout(function() {
            _this.$notify({
              title: "下载",
              message: "文件下载完成",
              type: "success"
            });
          }, 500);
        }
      );
    },
    getUrl() {
      let _this = this;
      Api.Download(this.fileId)
        .then(res => {
          if (res.data.status === 200) {
            _this.url = res.data.data.url;
            console.log(_this.fileMeta.type);
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.init {
  margin: 0;
  padding: 0 !important;
  position: relative;
  display: block !important;
  background-color: rgba(0, 0, 0, 0.01);
}

.file_cnt {
  width: 980px;
  margin: 20px auto 20px auto;
  /* height: ; */
  position: relative;
  background: #fff;
  border: 1px solid #dfe7f1;
  border-radius: 5px;
}

.divider_cnt {
  display: block;
  height: 1px;
  width: 100%;
  margin: 5px 0;
  background-color: #dcdfe6;
  position: relative;
}
</style>
