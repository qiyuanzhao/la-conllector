const app = getApp();
const storeData = {
  id: 1234,
  name: '汉堡王',
  position: '王小二',
  phone: '10086',
  category: '快餐',
  pics: [
    'http://img02.tooopen.com/images/20150928/tooopen_sy_143912755726.jpg',
    'http://img06.tooopen.com/images/20160818/tooopen_sy_175866434296.jpg',
    'http://img06.tooopen.com/images/20160818/tooopen_sy_175833047715.jpg'
  ],
  logo: 'https://tva2.sinaimg.cn/crop.1.14.547.547.180/7747a91djw8fdgef9qielj20fk0fxjt9.jpg',
  introduction: '超好吃的汉堡',
  rate: 4,
  address: '一楼b123',
};
Page({
  data: {
    shop: storeData
  },
  onLoad: function(option) {
    console.log(option.query);
    var that = this;
  },
  call () {
      console.log();
    wx.makePhoneCall({
      phoneNumber: this.data.shop.phone //仅为示例，并非真实的电话号码
    })
  }
});
