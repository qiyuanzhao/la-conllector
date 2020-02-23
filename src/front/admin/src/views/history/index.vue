
<template>
  <div class="app-container">
    <div class="">
      <el-button :plain="true" type="info">今日热点</el-button>
      <el-button :plain="true" type="info">发现热点</el-button>
    </div>
    <div class="padding-top">
      <el-date-picker v-model="date" type="date" placeholder="选择日期" :editable="false">
      </el-date-picker>
    </div>
    <div class="padding-top">
      <div class="left">
        <el-tree v-loading="treeLoading" :data="treeData" :props="defaultProps" :current-node-key="currentNode" highlight-current node-key="id" @node-click="nodeClick"
        default-expand-all :expand-on-click-node="false">
        </el-tree>
      </div>
      <div class="right">
        <el-table :data="tableData.topNewsList" style="width: 100%" v-loading="tableLoading">
          <el-table-column prop="title" label="标题">
            <template scope="scope">
                <div slot="reference" class="name-wrapper">
                  {{ scope.row.title }}
                  <el-tag type="danger" v-if="scope.row.hot">hot</el-tag>
                </div>
            </template>
          </el-table-column>
          <el-table-column prop="origin.name" label="来源">
          </el-table-column>
          <el-table-column prop="timePublish" label="日期">
            <template scope="scope">
              <span>
                {{getDisplayName(scope.row.timePublish)}}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import momont from "moment";

export default {
  data() {
    return {
      currentNode: '',
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      date: Date.now(),
      tableData: {},
      treeLoading: false,
      tableLoading: false
    };
  },
  filters: {},
  created: function() {
    this.treeLoading = true;
    this.tableLoading = true;
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
      this.treeData = root.children;
      this.currentNode = this.treeData[0].id;
      this.treeLoading = false;
      let date = momont(this.date).format("YYYYMMDD");
      let url = "/api/news-top/" + this.currentNode + "/HOT/" + date;
      axios.get(url).then(respone => {
        this.tableData = respone.data;
        this.tableLoading = false;
      });
    });
  },
  watch: {
    date: function (newDate) {
      this.tableLoading = true;
      let date = momont(this.date).format("YYYYMMDD");
      let url = "/api/news-top/" + this.currentNode + "/HOT/" + date;
      axios.get(url).then(respone => {
        this.tableData = respone.data;
        this.tableLoading = false;
      });
    }
  },
  methods: {
    nodeClick(node, data, store) {
      this.tableLoading = true;
      this.currentNode = node.id;
      let date = momont(this.date).format("YYYYMMDD");
      let url = "/api/news-top/" + node.id + "/HOT/" + date;
      axios.get(url).then(respone => {
        this.tableData = respone.data;
        this.tableLoading = false;
      });
    },
    getDisplayName(date) {
      return momont(date).format("YYYY-MM-DD");
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.text-right {
  text-align: right;
}
.padding-top {
  padding-top: 15px;
}
.left {
  width: 200px;
  float: left;
}
.right {
  margin-left: 200px;
}
</style>
