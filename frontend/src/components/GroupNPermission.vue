<template>
  <a-drawer width="520" :closable="false" @close="close" :visible="groupVisible">
    <!-- 下拉框：操作 -->
    <div slot="title">
      <span>查看群组</span>
      <el-dropdown style="position: absolute; top: 10px; right: 10px;" @command="operateGroup">
        <el-button size="small">操作</el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="0">添加群组</el-dropdown-item>
          <el-dropdown-item command="1">删除群组</el-dropdown-item>
          <el-dropdown-item command="2">新建群组</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <div style="border-bottom: #e8e8e8 1px solid;">
      <el-row
        style="height: 120px; border-top: #e8e8e8 1px solid; cursor: pointer; padding: 10px;"
        v-for="(group, index) in groupList"
        :key="index"
        @click.native="showGroupUser(index)"
      >
        <el-col :span="14" style="height: 100px">
          <h3>{{group.groupInfo.group_name}}</h3>
          <span
            style="color:rgba(0, 0, 0, 0.45); text-overflow: ellipsis; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 3; overflow: hidden;"
          >{{group.groupInfo.group_desc}}</span>
        </el-col>
        <el-col :offset="2" :span="8" style="height: 100px; line-height: 100px; text-align: right;">
          <span>{{permissionMap[group.permission]}}</span>
        </el-col>

        <el-button
          v-show="isDeleteScene"
          plain
          circle
          icon="el-icon-close"
          type="danger"
          style="position: absolute; right: 0px; top: 2px;"
          size="mini"
          @click.stop="deleteGroup(index)"
        ></el-button>
      </el-row>
    </div>

    <!-- 群组列表 含群组名、群组简介、权限选择器、删除button（隐藏）、点击进入群组中的用户 -->

    <!-- 查看群组中的用户(查看群组元数组) -->
    <a-drawer
      width="420"
      :closable="false"
      @close="groupDrawer.groupUserVisible = false"
      :visible="groupDrawer.groupUserVisible"
    >
      <div slot="title">
        <span>群组信息</span>
        <el-dropdown
          style="position: absolute; top: 10px; right: 10px;"
          @command="operateGroupMeta"
        >
          <el-button size="small">操作</el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="0">新增用户</el-dropdown-item>
            <el-dropdown-item command="1">删除用户</el-dropdown-item>
            <el-dropdown-item command="2">编辑信息</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>

      <el-row style="margin-top: 10px;">
        <el-col :offset="1" :span="5" style="text-align: right;">群组名：</el-col>
        <el-col :offset="2" :span="16">
          <span>{{groupList[groupClickedIndex].groupInfo.group_name}}</span>
        </el-col>
      </el-row>
      <!-- 			<el-row style="margin-top: 10px;">
			<el-col :offset="1" :span="5" style="text-align: right;">创建者：</el-col>
			<el-col :offset="2" :span="16">
				<span>{{docMeta.creator}}</span>
			</el-col>
      </el-row>-->
      <el-row style="margin-top: 10px;">
        <el-col :offset="1" :span="5" style="text-align: right;">创建时间：</el-col>
        <el-col :offset="2" :span="16">
          <span>{{groupList[groupClickedIndex].groupInfo.created_at}}</span>
        </el-col>
      </el-row>
      <el-row style="margin-top: 10px;">
        <el-col :offset="1" :span="5" style="text-align: right;">简介：</el-col>
        <el-col :offset="2" :span="16">
          <span>{{groupList[groupClickedIndex].groupInfo.group_desc}}</span>
        </el-col>
      </el-row>
      <el-row style="margin-top: 10px;">
        <el-col :offset="1" :span="5" style="text-align: right;">用户列表：</el-col>
        <el-col :offset="2" :span="16">
          <el-tag
            :closable="isDeleteGroupUserScene"
            @close="deleteGroupUser(index)"
            v-for="(groupUser, index) in groupUserList"
            :key="index"
            style="margin-right: 10px;"
          >{{groupUser.username}}</el-tag>
        </el-col>
      </el-row>

      <!-- 修改群组元数据 -->
      <a-drawer
        title="编辑群组信息"
        width="320"
        :closable="false"
        @close="groupDrawer.groupMetaVisible = false"
        :visible="groupDrawer.groupMetaVisible"
      >
        <el-row style="margin-top: 10px;">
          <el-col :offset="1" :span="5" style="text-align: right;">群组名：</el-col>
          <el-col :offset="2" :span="16">
            <el-input type="text" v-model="groupMeta.group_name" size="small"></el-input>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :offset="1" :span="5" style="text-align: right;">简介：</el-col>
          <el-col :offset="2" :span="16">
            <el-input
              type="textarea"
              :rows="8"
              show-word-limit
              v-model="groupMeta.group_desc"
              size="small"
            ></el-input>
          </el-col>
        </el-row>
      </a-drawer>

      <!-- 新增用户 -->
      <a-drawer
        title="新增用户"
        width="320"
        :closable="false"
        @close="groupDrawer.addUserVisible = false"
        :visible="groupDrawer.addUserVisible"
      >
        <el-input></el-input>
      </a-drawer>
    </a-drawer>

    <!-- 添加群组 -->
    <a-drawer
      title="添加群组"
      width="320"
      :closable="false"
      @close="groupDrawer.addGroupVisible = false"
      :visible="groupDrawer.addGroupVisible"
    >
      <el-input></el-input>
    </a-drawer>

    <!-- 新建群组 -->
    <a-drawer
      title="新增群组"
      width="320"
      :closable="false"
      @close="groupDrawer.newGroupVisible = false"
      :visible="groupDrawer.newGroupVisible"
    >
      <el-row style="margin-top: 10px;">
        <el-col :offset="1" :span="5" style="text-align: right;">群组名：</el-col>
        <el-col :offset="2" :span="16">
          <el-input type="text" v-model="groupMeta.group_name" size="small"></el-input>
        </el-col>
      </el-row>
      <el-row style="margin-top: 10px;">
        <el-col :offset="1" :span="5" style="text-align: right;">简介：</el-col>
        <el-col :offset="2" :span="16">
          <el-input
            type="textarea"
            :rows="8"
            show-word-limit
            v-model="groupMeta.group_desc"
            size="small"
          ></el-input>
        </el-col>
      </el-row>
      <el-button type="primary" style="position: relative;margin: 10px 0 0 20px;" @click="handleError('There No Ok')">确认</el-button>
    </a-drawer>
  </a-drawer>
</template>

<script>
// 此组件未完成
// 需要后台增添接口
import { mapState } from "vuex";
import * as DEFAULT from "../json/default";

export default {
  name: "GroupNPermission",
  data() {
    return {
      // 群组信息
      groupDrawer: {
        groupUserVisible: false,
        groupMetaVisible: false,
        newGroupVisible: false,
        addGroupVisible: false,
        addUserVisible: false
      },
      groupList: DEFAULT.gl,
      isCurrentResourceOwner: 1,
      isCurrentGroupOwner: 1,
      permissionMap: DEFAULT.pm,
      isDeleteScene: false,
      groupUserList: DEFAULT.ml,
      isDeleteGroupUserScene: false,
      groupMeta: {
        group_name: "",
        group_desc: ""
      },
      groupClickedIndex: 0
    };
  },
  computed: {
    ...mapState(["groupVisible", "currentResourceId"])
  },
  methods: {
    operateGroup(command) {
      switch (command) {
        case "0":
          this.groupDrawer.addGroupVisible = true;
          break;

        case "1":
          this.isDeleteScene = !this.isDeleteScene;
          break;

        case "2":
          this.groupDrawer.newGroupVisible = true;
          break;
      }
    },
    operateGroupMeta(command) {
      switch (command) {
        case "0":
          this.groupDrawer.addUserVisible = true;
          break;

        case "1":
          this.isDeleteGroupUserScene = !this.isDeleteGroupUserScene;
          break;

        case "2":
          this.groupDrawer.groupMetaVisible = true;
          break;
      }
    },
    addGroup() {},
    deleteGroup(index) {
      let _this = this;
      Api.deleteGroup(this.groupList[index].groupInfo.group_id)
        .then(res => {
          if (res.data.status === 200) {
            _this.getGroupsOfResource();
          } else {
            alert(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    newGroup() {
      let _this = this;
      Api.newGroup(this.groupMeta.group_name, this.groupMeta.group_desc)
        .then(res => {
          if (res.data.status === 200) {
            _this.$notify({
              title: "新建成功",
              message: "你已经新建群组，可以在添加群组中授予其权限",
              type: "success"
            });
            _this.groupDrawer.newGroupVisible = false;
          } else {
            alert(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    showGroupUser(index) {
      let _this = this;
      Api.getUserOfGroup(this.groupList[index].groupInfo.group_id)
        .then(res => {
          if (res.data.status === 200) {
            _this.groupClickedIndex = index;
            _this.groupUserList = res.data.data.memberList;
            _this.isCurrentGroupOwner = res.data.data.isOwner;
            this.groupDrawer.groupUserVisible = true;
          } else {
            alert(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    deleteGroupUser(index) {
      this.groupUserList.splice(index, 1);
    },
    getGroupsOfResource() {
      let _this = this;
      Api.getGroupOfResourceHadPermission(
        this.docList[this.currentItemClicked].id
      )
        .then(res => {
          if (res.data.status === 200) {
            // 数组清空
            _this.isCurrentResourceOwner = res.data.data.isOwner;
            _this.groupList = res.data.data.groupList;
            _this.groupDrawer.groupVisible = true;
          } else {
            alert(res.data.msg);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    close() {
      this.$store.commit({
        type: "groupH"
      });
    },
    handleError(err) {
      console.log(err);
      this.$message.warning(DEFAULT.defaultNetwordError);
    }
  }
};
</script>

<style>
</style>
