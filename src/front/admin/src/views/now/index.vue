<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <div class="grid-content">
          <el-button :plain="true" type="info">今日热点</el-button>
          <el-button :plain="true" type="info">发现热点</el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content text-right">
          <el-button type="primary" icon="plus" @click="openAddHotEventPopup();">自添加</el-button>
          <el-button type="primary" icon="message" @click="sendEmail();">发送</el-button>
        </div>
      </el-col>
    </el-row>
    <!-- <div class="padding-top">
      <el-radio-group v-model="radio3">
        <el-radio-button label="体育"></el-radio-button>
        <el-radio-button label="电竞"></el-radio-button>
        <el-radio-button label="音乐"></el-radio-button>
        <el-radio-button label="美食"></el-radio-button>
        <el-radio-button label="生活"></el-radio-button>
        <el-radio-button label="节日"></el-radio-button>
      </el-radio-group>
    </div> -->
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
          <el-table-column label="操作">
            <template scope="scope">
              <el-button v-if="!scope.row.hot" size="small" @click="setTheHotEvent(scope.$index, scope.row, true)">设置热点</el-button>
              <el-button v-if="scope.row.hot" size="small" type="danger" @click="setTheHotEvent(scope.$index, scope.row, false)">取消热点</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog title="添加热点" :visible.sync="dialogFormVisible" size="tiny">
      <el-form :model="form">
        <el-form-item label="类型" :label-width="formLabelWidth">
          <el-select v-model="form.type" placeholder="请选择热点类型">
            <el-option label="今日热点" value="HOT"></el-option>
            <el-option label="发现热点" value="DISCOVER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据源" :label-width="formLabelWidth">
          <el-select v-model="form.site" placeholder="请选择数据源">
            <el-option v-for="item in options" :key="item.value" :label="item.name" :value="item" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="热点标题" :label-width="formLabelWidth">
          <el-input v-model="form.title" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="热点链接" :label-width="formLabelWidth">
          <el-input v-model="form.url" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addHotEvent()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import momont from "moment";

export default {
  data() {
    return {
      currentNode: '',
      options: [],
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      dialogFormVisible: false,
      tableData:{},
      form: {
        type: "",
        site: "",
        title: "",
        url: ""
      },
      formLabelWidth: "80px",
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
      let date = momont().format("YYYYMMDD");
      let url = "/api/news-top/" + this.currentNode + "/HOT/" + date;
      axios.get(url).then(respone => {
        this.tableLoading = false;
        this.tableData = respone.data;
      });
    });
  },
  methods: {
    openAddHotEventPopup() {
      let url = '/api/origin/list?cid=' + this.currentNode;
      axios.get(url)
      .then(respone => {
        this.dialogFormVisible = true;
        this.options = respone.data;
      })
    },
    sendEmail() {
      let url = '/api/news-top/send';
      axios.get(url)
      .then(respone => {
        this.$message({
          type: "success",
          message: "发送成功!"
        });
      })
    },
    addHotEvent() {
      let form = this.form;
      this.tableData.topNewsList.push({
        type: form.type,
        origin: form.site,
        title: form.title,
        url: form.url,
        hot: true
      });
      axios({
        method: 'put',
        url: '/api/news-top/update/' + this.tableData.id,
        data: this.tableData.topNewsList
      }).then(respone => {
        this.tableData = respone.data;
        this.dialogFormVisible = false;
        this.$message({
          type: "success",
          message: "添加热点成功!"
        });
      });
    },
    setTheHotEvent(index, row, hot) {
      let text1 = '';
      let text2 = '';
      if (hot) {
        text1 = '是否添加为热点?';
        text2 = '添加热点成功';
      } else {
        text1 = '是否取消为热点?';
        text2 = '取消热点成功';
      }
      this.$confirm(text1, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.tableData.topNewsList[index].hot = hot;
        axios({
          method: 'put',
          url: '/api/news-top/update/' + this.tableData.id,
          data: this.tableData.topNewsList
        }).then(respone => {
          this.tableData = respone.data;
          this.$message({
            type: "success",
            message: text2
          });
        });
      });
    },
    nodeClick(node, data, store) {
      this.currentNode = node.id;
      let date = momont().format("YYYYMMDD");
      let url = "/api/news-top/" + node.id + "/HOT/" + date;
      this.tableLoading = true;
      axios.get(url).then(respone => {
        this.tableLoading = false;
        this.tableData = respone.data;
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
