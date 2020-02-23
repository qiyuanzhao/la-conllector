//获取应用实例
const util = require('../../utils/util');
const app = getApp();
Page({
  onLoad() {
    console.log('load list page');

    // if (app.globalData.nearestPlaza) {
    //   console.log(app.globalData.nearestPlaza);
    //   // this.data.curPlaza = app.globalData.nearestPlaza;
    //   this.data.curPlaza = {id:1,name:""};
    //   this.loadArticleList(this.data.curPlaza.id, -1).then(data => {
    //     this.addArticleList(data);
    //   });
    // } else {
    //   console.log('**********1');
    //   app.nearestPlazaReadyCallback = plaza => {
    //     console.log('**********2');
    //     console.log(plaza);
    //     // this.data.curPlaza = app.globalData.nearestPlaza;
    //   this.data.curPlaza = {id:1,name:""};
    //     this.loadArticleList(this.data.curPlaza.id, -1).then(data => {
    //       this.addArticleList(data);
    //     });
    //   };
    // }
    console.log('plazas : ');
    console.log(app.globalData.plazas);
    this.loadArticleList(this.data.curPlaza.id, -1).then(data => {
      this.addArticleList(data);
    });
    app.getAllPlaza(plazas => {
      this.setData({ plazas: app.globalData.plazas });
    });
  },
  onPullDownRefresh: function () {
    this.data.curPage = 0;
    this.data.refresh = true;
    this.loadArticleList(
      this.data.curPlaza.id,
      this.data.currentNavbar,
      this.data.curPage,
      this.data.oncePageSize
    ).then(data => {
      this.setData({
        articleList: data.content,
        refresh: false
      });
      if (data.numberOfElements === 0) {
        this.setData({
          noData: true
        });
      }
    });
  },
  data: {
    plazas: [],
    curPlaza: { id: 1, name: '' },
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    navbar: [
      {
        id: -1,
        name: '精选'
      },
      {
        id: 23,
        name: '美食'
      },
      {
        id: 24,
        name: '购物'
      },
      {
        id: 25,
        name: '娱乐'
      }
    ],
    currentNavbar: '-1',
    articleList: [
      // {
      //   id: 1,
      //   shopId: 11,
      //   brandId: 111,
      //   title: '科比球衣退役',
      //   image:
      //     'https://wx4.sinaimg.cn/mw690/61e36371ly1fmm3m924skj20sg0iy78y.jpg',
      //   address: '一层b110'
      // },
      // {
      //   id: 2,
      //   shopId: 11,
      //   brandId: 111,
      //   title: '科比球衣退役',
      //   image:
      //     'https://wx4.sinaimg.cn/mw690/61e36371ly1fmm3m924skj20sg0iy78y.jpg',
      //   address: '一层b110'
      // }
    ],
    curPage: 0,
    oncePageSize: 5,
    loadMore: false,
    noData: false,
    refresh: false
  },
  changeIndicatorDots: function (e) {
    this.setData({
      indicatorDots: !this.data.indicatorDots
    });
  },
  changeAutoplay: function (e) {
    this.setData({
      autoplay: !this.data.autoplay
    });
  },
  intervalChange: function (e) {
    this.setData({
      interval: e.detail.value
    });
  },
  durationChange: function (e) {
    this.setData({
      duration: e.detail.value
    });
  },
  /**
   * 切换 navbar
   */
  switchNav(e) {
    console.log('switch navbar to ' + e.currentTarget.dataset.id);

    this.setData({
      currentNavbar: e.currentTarget.dataset.id
    });
    this.setData({
      articleList: []
    });
    this.data.curPage = 0;
    this.loadArticleList(
      this.data.curPlaza.id,
      e.currentTarget.dataset.id
    ).then(data => {
      this.addArticleList(data);
    });

    // if (e.currentTarget.dataset.idx == 1 && this.data.latest_list.length == 0) {
    //   this.pullUpLoadLatest()
    // }
  },
  tapArticle(event) {
    const articleId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/detail/detail?id=' + articleId
    });
  },
  tapShop(event) {
    const articleId = event.currentTarget.dataset.shopId;
    wx.navigateTo({
      url: '/pages/shop/shop?id=' + articleId
    });
  },
  tapFavorite(event) {
    console.log(util.author('scope.userInfo'));

    app.authorUserInfo().then(
      data => {
        console.log(data);
      },
      err => {
        console.log('error');
      }
    );
    const articleId = event.currentTarget.dataset.brandId;
    wx.showToast({
      title: '收藏成功',
      icon: 'success',
      duration: 1000
    });
  },
  loadArticleList(plaza, category, page = 0, size = 5) {
    console.log(
      'load plaza : ' + plaza + '  category : ' + category + '   page:' + page
    );
    var that;

    this.setData({ loadMore: true });
    return new Promise((resolve, reject) => {
      wx.request({
        url:
        'http://read.lavector.com/api/wsa/article/' +
        plaza +
        '/' +
        category +
        '/type?page=' +
        page +
        '&size=' +
        size,
        data: {},
        header: {
          'content-type': 'application/json' // 默认值
        },
        success: res => {
          this.setData({ loadMore: false });
          console.log('load article count : ' + res.data.numberOfElements);
          if (res.data.numberOfElements == 0) {
            // wx.showToast({
            //   title: '没有更多数据',
            //   icon: 'info',
            //   duration: 2000
            // });
          } else {
            resolve(res.data);
            // let newList;
            // if (Array.isArray(this.data.articleList)) {
            //   newList = this.data.articleList.concat(res.data.content);
            // } else {
            //   newList = res.data.content;
            // }
            // this.setData({ articleList: newList });

            // console.log(this.data.articleList);
          }
        }
      });
    });
  },
  addArticleList(data) {
    let newList = [];
    if (Array.isArray(this.data.articleList)) {
      newList = newList.concat(this.data.articleList);
      newList = newList.concat(data.content);
    } else {
      newList = data.content;
    }
    this.setData({ articleList: newList });
  },
  loadMore() {
    console.log('load more');
    console.log(this.data.currentNavbar);

    this.data.curPage += 1;
    this.loadArticleList(
      this.data.curPlaza.id,
      this.data.currentNavbar,
      this.data.curPage,
      this.data.oncePageSize
    ).then(data => {
      this.addArticleList(data);
      if (data.numberOfElements === 0) {
        this.setData({
          noData: true
        });
      }
    });
  },
  refreshArticle() { },
  changePlaza(e) {
    console.log(e);
  }
});
