//获取应用实例
const app = getApp();
Page({
  onLoad() {
    console.log('load list page');

    if (app.globalData.nearestPlaza) {
      console.log(app.globalData.nearestPlaza);
      this.curPlaza = app.globalData.nearestPlaza
      this.loadArticleList(this.curPlaza.id,-1);
    } else {
      console.log('**********1');
      app.nearestPlazaReadyCallback = plaza => {
        console.log('**********2');
        console.log(plaza);
      this.curPlaza = app.globalData.nearestPlaza
      this.loadArticleList(this.curPlaza.id,-1);
      };
    }
  },
  data: {
    curPlaza: null,
    indicatorDots: true,
    autoplay: true,
    interval: 5000,
    duration: 1000,
    navbar: [
     {
      id: -1,
      name:'精选'
    },
     {
      id: 23,
      name:'美食'
    },
     {
      id: 24,
      name:'购物'
    },
     {
      id: 25,
      name:'娱乐'
    },
      ],
    currentNavbar: '-1',
    articleList: [
      {
        id: 1,
        shopId: 11,
        brandId: 111,
        title: '科比球衣退役',
        image:
          'https://wx4.sinaimg.cn/mw690/61e36371ly1fmm3m924skj20sg0iy78y.jpg',
        address: '一层b110'
      },
      {
        id: 2,
        shopId: 11,
        brandId: 111,
        title: '科比球衣退役',
        image:
          'https://wx4.sinaimg.cn/mw690/61e36371ly1fmm3m924skj20sg0iy78y.jpg',
        address: '一层b110'
      }
    ]
  },
  changeIndicatorDots: function(e) {
    this.setData({
      indicatorDots: !this.data.indicatorDots
    });
  },
  changeAutoplay: function(e) {
    this.setData({
      autoplay: !this.data.autoplay
    });
  },
  intervalChange: function(e) {
    this.setData({
      interval: e.detail.value
    });
  },
  durationChange: function(e) {
    this.setData({
      duration: e.detail.value
    });
  },
  /**
   * 切换 navbar
   */
  switchNav(e) {
    console.log(e);
    
    this.setData({
      currentNavbar: e.currentTarget.dataset.id
    });
      this.loadArticleList(this.curPlaza.id,e.currentTarget.dataset.id);
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
    const articleId = event.currentTarget.dataset.brandId;
    wx.showToast({
      title: '收藏成功',
      icon: 'success',
      duration: 1000
    });
  },
  loadArticleList(plaza, category, page = 0, size = 5) {

    wx.request({
      url: 'http://read.lavector.com/api/wsa/article/'+plaza+'/'+category+'/type?page=' + page + '&size=' + size,
      data: {},
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: (res) =>{
        console.log(res.data);
        this.setData({articleList: res.data.content})

        console.log(this.articleList);
      }
    });
  }
});
