const app = getApp();
const favoriteData = [
  {
    floor: '商场1层',
    stores: [{
      id: 12,
      brand: {
        logo: "https://ws3.sinaimg.cn/large/006tNc79ly1fmva7zzr8aj306e03qaad.jpg"
      }
    },
    {
      id: 8,
      brand: {
        logo: "https://ws2.sinaimg.cn/large/006tNc79ly1fmva7zvmwtj306103q0tr.jpg"
      }
    },
    // { 
    //   id: 4,
    //     logo: "https://ws1.sinaimg.cn/large/006tNc79ly1fmva7zjutvj306x03qwew.jpg"
    //   }
    //   brand: {
    // },
    // { 
    //   id: 11,
    //   brand: {
    //     logo: "https://ws4.sinaimg.cn/large/006tNc79ly1fmva7zc3ogj306s03qgmd.jpg"
    //   }
    // },
    // { 
    //   id: 6,
    //   brand: {
    //     logo: "https://ws4.sinaimg.cn/large/006tNc79ly1fmva7z8bhij304k03q0t9.jpg"
    //   }
    // },
    // { 
    //   id: 10,
    //   brand: {
    //     logo: "https://ws3.sinaimg.cn/large/006tNc79ly1fmva7z5ayij309q03qmxw.jpg"
    //   }
    // },
    // { 
    //   id: 8,
    //   brand: {
    //     logo: "https://ws3.sinaimg.cn/large/006tNc79ly1fmva7ywuo0j308603q0t4.jpg"
    //   }
    // },
    // {
    //   id: 9,
    //   brand: {
    //     logo: "https://ws2.sinaimg.cn/large/006tNc79ly1fmva7ys4o8j30b503qabk.jpg"
    //   }
    // },
    {
      id: 9,
      brand: {
        logo: "https://ws2.sinaimg.cn/large/006tNc79ly1fmva7yohcoj304z03qglx.jpg"
      }
    }]
  },
  {
    floor: '商场2层',
    stores: [
      {
        id: 18,
        brand: {
          logo: "https://ws4.sinaimg.cn/large/006tNc79ly1fmvasc2b2pj308c02sdg7.jpg"
        }
      },
      {
        id: 16,
        brand: {
          logo: "https://ws4.sinaimg.cn/large/006tNc79ly1fmvasc9e6jj308c02s3yn.jpg"
        }
      },
      {
        id: 17,
        brand: {
          logo: "https://ws1.sinaimg.cn/large/006tNc79ly1fmvasc5spcj308c02swef.jpg"
        }
      }
    ]
  },
  {
    floor: '商场3层',
    stores: [
      {
        id: 21,
        brand: {
          logo: "https://ws1.sinaimg.cn/large/006tNc79ly1fmvb7xprkej306803qdfx.jpg"
        }
      },
      {
        id: 20,
        brand: {
          logo: "https://ws4.sinaimg.cn/large/006tNc79ly1fmvb05rjnbj304y03q0sx.jpg"
        }
      },
      {
        id: 13,
        brand: {
          logo: "https://ws1.sinaimg.cn/large/006tNc79ly1fmvasbqsufj308c02smx6.jpg"
        }
      }

    ]
  }];
Page({
  data: {
    favoriteList: favoriteData,
    navbar: ['全部', '美食', '购物', '娱乐']
  },
  onLoad: function (option) {
    console.log(option.query);
    var that = this;
  },
  call() {
    console.log();
    wx.makePhoneCall({
      phoneNumber: this.data.shop.phone //仅为示例，并非真实的电话号码
    });
  },
  tapShop(event) {
    const articleId = event.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/shop/shop?id=' + articleId
    })
  }
});
