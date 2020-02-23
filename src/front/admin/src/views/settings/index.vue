<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <div class="grid-content">
          <el-tree v-loading="treeLoading" :data="treeData" :props="defaultProps" highlight-current node-key="id" @node-click="nodeClick"
          default-expand-all :expand-on-click-node="false" :render-content="renderContent">
          </el-tree>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content">
            <el-table :data="list" border fit highlight-current-row style="width: 100%">

              <el-table-column label="数据源" >
                <template scope="scope">
                  <el-input v-show="scope.row.edit" size="small" v-model="scope.row.name"></el-input>
                  <span v-show="!scope.row.edit">{{ scope.row.name }}</span>
                </template>
              </el-table-column>

              <el-table-column label="数据源链接">
                <template scope="scope">
                  <el-input v-show="scope.row.edit" size="small" v-model="scope.row.url"></el-input>
                  <span v-show="!scope.row.edit">{{ scope.row.url }}</span>
                </template>
              </el-table-column>

              <el-table-column align="center" label="编辑" >
                <template scope="scope">
                  <el-button v-show="!scope.row.edit" type="primary" @click='editTable(scope.row)' size="small" >{{'编辑'}}</el-button>
                  <el-button v-show="scope.row.edit" type="success" @click='saveTable(scope.row)' size="small" >{{'完成'}}</el-button>
                  <el-button size="small" type="danger" @click="removeSite(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="margin-top: 20px">
              <el-button @click="addNewSite()">新建数据源</el-button>
            </div>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-row>
          <div class="grid-content">
            <el-form :model="sendConfig" :label-width="formLabelWidth2" :label-position="labelPosition">
              <el-col :span="12">
                <el-form-item label="今日热点推送条数" >
                  <el-input v-model="sendConfig.hotSendNum" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="今日热点可选条数">
                  <el-input v-model="sendConfig.hotChooseNum" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发现热点推送条数">
                  <el-input v-model="sendConfig.trendSendNum" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发现热点可选条数" >
                  <el-input v-model="sendConfig.trendChooseNum" auto-complete="off"></el-input>
                </el-form-item>
              </el-col>
            </el-form> 
            <el-button type="primary" @click="saveHotSetting()">保 存</el-button>
          </div>
        </el-row>
      </el-col>
    </el-row>  
    <el-dialog title="修改频道" :visible.sync="editNodePopup" size="tiny">
      <el-form :model="node">
        <el-form-item label="频道名称" :label-width="formLabelWidth">
          <el-input v-model="node.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="频道关键词" :label-width="formLabelWidth">
          <el-input v-model="node.code" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editNodePopup = false">取 消</el-button>
        <el-button type="primary" v-if="editNode" @click="saveNode()">确 定</el-button>
        <el-button type="primary" v-else="editNode" @click="saveNewNode()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import Vue from "vue";

let id = 1000;

export default {
  data() {
    return {
      editNodePopup: false,
      editNode: false,
      form: {
        type: "",
        title: "",
        url: ""
      },
      sendConfig: {
        hotSendNum: '',
        hotChooseNum: '',
        trendSendNum: '',
        trendChooseNum: ''
      },
      node: {
        name: "",
        code: ""
      },
      nodeCopy: {},
      formLabelWidth: "100px",
      labelPosition: "left",
      formLabelWidth2: "140px",
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      list: [],
      store: {},
      currentNode: {},
      treeLoading: false,
      sendConfigCopy: {}
    };
  },
  created: function() {
    this.treeLoading = true;
    axios.get("/api/category/all").then(respone => {
      let root = {
        name: "频道",
        id: 0
      };
      function tree(node) {
        respone.data.forEach(d => {
          if (d.pid === node.id) {
            if (!Array.isArray(node.children)) {
              node.children = [];
            }
            node.children.push(d);
            tree(d);
          }
        });
      }
      tree(root);
      this.treeData = [root];
      this.treeLoading = false;
    });
    axios.get("/api/config/sendNum").then(respone => {
      this.sendConfigCopy = respone.data;
      this.sendConfig = JSON.parse(respone.data.configData);
    });
  },
  methods: {
    addNode(store) {
      this.node = {};
      this.editNodePopup = true;
      this.editNode = false;
      this.store = store;
    },

    edit(data) {
      this.editNodePopup = true;
      this.editNode = true;
      this.nodeCopy = data;
      this.node = JSON.parse(JSON.stringify(this.nodeCopy));
    },

    saveNode() {
      this.editNodePopup = false;
      if (this.nodeCopy.id) {
        this.node.id = this.nodeCopy.id;
        let url = "/api/category";
        axios.put(url, this.node).then(respone => {
          this.nodeCopy.name = respone.data.name;
          this.nodeCopy.code = respone.data.code;
          this.$message({
            type: "success",
            message: "修改成功!"
          });
        });
      }
    },

    saveNewNode() {
      this.node.pid = this.store.currentNode.data.id;
      axios.post("/api/category", this.node).then(respone => {
        if (!this.store.currentNode.data.children) {
          this.store.currentNode.data.children = [];
        }
        this.store.currentNode.data.children.push(respone.data);
        this.$message({
          type: "success",
          message: "添加成功!"
        });
        this.editNodePopup = false;
      });
    },

    remove(store, data) {
      this.$confirm("是否删除该频道?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let url = "/api/category/" + data.id;
        axios.delete(url).then(respone => {
          store.remove(data);
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        });
      });
    },

    renderContent(h, { node, data, store }) {
      return (
        <span>
          <span>
            <span>{node.label}</span>
          </span>
          <span style="float: right; margin-right: 20px">
            {data.id > 0 ? (
              <span>
                <el-button size="mini" on-click={() => this.edit(data)}>
                  修改
                </el-button>
                <el-button
                  size="mini"
                  on-click={() => this.remove(store, data)}
                >
                  删除
                </el-button>
              </span>
            ) : (
              <div />
            )}
            <span style="margin-left: 10px">
              <el-button size="mini" on-click={() => this.addNode(store)}>
                添加
              </el-button>
            </span>
          </span>
        </span>
      );
    },

    nodeClick(node, data, store) {
      this.currentNode = node;
      let url = "/api/origin/list?cid=" + node.id;
      axios.get(url).then(respone => {
        this.list = respone.data;
      });
    },

    editTable(row) {
      Vue.set(row, "edit", true);
    },

    saveTable(row) {
      if (row.name.length < 1) {
        this.$message({
          type: "warning",
          message: "请输入数据源的名称！"
        });
        return;
      }
      if (row.url.length < 1) {
        this.$message({
          type: "warning",
          message: "请输入数据源的链接！"
        });
        return;
      }
      row.categoryId = this.currentNode.id;
      if (row.id) {
        axios.put("/api/origin", row).then(respone => {
          Vue.set(row, "edit", false);
          row = respone.data;
          this.$message({
            type: "success",
            message: "编辑成功!"
          });
        });
      } else {
        axios.post("/api/origin", row).then(respone => {
          Vue.set(row, "edit", false);
          row = respone.data;
          this.$message({
            type: "success",
            message: "添加成功!"
          });
        });
      }
    },

    addNewSite() {
      this.list.push({
        name: "",
        url: "",
        edit: true
      });
    },

    removeSite(index, row) {
      this.$confirm("是否删除该数据源?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let url = "/api/origin/" + row.id;
        axios.delete(url).then(respone => {
          this.list.splice(index, 1);
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        });
      });
    },

    saveHotSetting() {
      this.sendConfigCopy.configData = JSON.stringify(this.sendConfig);
      axios({
          method: 'put',
          url: '/api/config',
          data: this.sendConfigCopy
      }).then(respone => {
        this.$message({
          type: "success",
          message: "保存成功!"
        });
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-col-12 {
  padding: 10px;
}
</style>
