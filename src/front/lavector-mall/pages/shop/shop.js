const app = getApp();
const shopData = {
  id: 1234,
  name: '汉堡王',
  position: '王小二',
  phone: '10086',
  brand: {
    name: '',
    logo:
      'https://tva2.sinaimg.cn/crop.1.14.547.547.180/7747a91djw8fdgef9qielj20fk0fxjt9.jpg',
    category: {
      name: '快餐'
    }
  },
  picList: [
    'http://img02.tooopen.com/images/20150928/tooopen_sy_143912755726.jpg',
    'http://img06.tooopen.com/images/20160818/tooopen_sy_175866434296.jpg',
    'http://img06.tooopen.com/images/20160818/tooopen_sy_175833047715.jpg'
  ],
  introduction: '超好吃的汉堡',
  rate: 4,
  address: '一楼b123'
};
Page({
  data: {
    shop: shopData,
    brandArticleList: []
  },
  onLoad: function(option) {
    console.log(option);
    this.loadShopInfo(option.id, (data) => {
        this.setData({ shop: data });
        this.loadBrandArticleList(data.brand.id)
    });
    var that = this;
  },
  call() {
    console.log();
    wx.makePhoneCall({
      phoneNumber: this.data.shop.phone //仅为示例，并非真实的电话号码
    });
  },
  loadShopInfo(id, callback) {
    wx.request({
      url: 'http://read.lavector.com/api/wsa/shop/' + id,
      data: {},
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: res => {
        console.log(res.data);
        callback(res.data)

        // console.log(this.articleList);
      }
    });
  },
  loadBrandArticleList(brandId, page = 0, size = 2) {
    wx.request({
      url:
        'http://read.lavector.com/api/wsa/article/brand/' + brandId + '?page=' + page + '&size=' + size,
      data: {},
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: res => {
        this.setData({ brandArticleList: res.data.content });
        console.log(this.data.brandArticleList);
      }
    });
  },
  tapArticle(e) {
    const articleId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/detail/detail?id=' + articleId
    });
  }
  
});
