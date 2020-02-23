const util = require('utils/util.js');
const logs = [];
//app.js
App({
  onLaunch: function () {
    logs.unshift('start app');
    // 展示本地存储能力
    this.globalData.logs = logs;
    // wx.setStorageSync('logs', logs);

    // 登录
    wx.login({
      success: res => {
        console.log(res);
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    });
    // this.authorUserInfo().then(data => {});
    this.authorLocation();
  },
  authorUserInfo() {
    // 获取用户信息
    return new Promise((resolve, reject) => {
      const getUserInfo = (resolve, reject) => {
        logs.unshift('start get user info ');
        wx.getUserInfo({
          success: res => {
            logs.unshift('get user info success');
            logs.unshift(JSON.stringify(res.userInfo));
            console.log(res);
            // 可以将 res 发送给后台解码出 unionId
            this.globalData.userInfo = res.userInfo;
            resolve(res.userInfo);

            // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
            // 所以此处加入 callback 以防止这种情况
            if (this.userInfoReadyCallback) {
              this.userInfoReadyCallback(res);
            }
          },
          fail: res => {
            logs.unshift('get user info fail');
            reject(res);
          }
        });
      };
      if (this.globalData.userInfo) {
        resolve(this.globalData.userInfo);
      } else {
        const scopeName = 'scope.userInfo';
        wx.getSetting({
          success: res => {
            logs.unshift(scopeName + ' author :' + res.authSetting[scopeName]);
            if (res.authSetting[scopeName]) {
              // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
              getUserInfo(resolve, reject);
            } else {
              wx.authorize({
                scope: scopeName,
                success(errMsg) {
                  logs.unshift(scopeName + ' author success~~');
                  getUserInfo(resolve, reject);
                },
                fail(errMsg) {
                  logs.unshift(scopeName + ' author fail');
                  console.log('author user info error');
                  console.log(errMsg);
                  reject(errMsg);
                },
                complete: function () {
                  console.log('comm');
                }
              });
            }
          }
        });
      }
    });
  },
  authorLocation() {
    var self = this;
    const scopeName = 'scope.userLocation';
    // 获取用户信息
    wx.getSetting({
      success: res => {
        logs.unshift(
          'location author :' + res.authSetting[scopeName]
        );
        if (res.authSetting[scopeName]) {
          self.getLocation(position => {
            console.log(position);
            self.getNearestPlaza(position.latitude, position.longitude);
          });
        } else {
          console.log('author location');
          wx.authorize({
            scope: scopeName,
            success() {
              // 用户已经同意小程序使用录音功能，后续调用 wx.startRecord 接口不会弹窗询问
              self.getLocation(position => {
                console.log(position);
                self.getNearestPlaza(position.latitude, position.longitude);
              });
            }
          });
        }
      }
    });
  },
  globalData: {
    userInfo: null,
    plazas: [],
    nearestPlaza: null
  },
  getNearestPlaza(lat, lng) {
    this.getAllPlaza(plazas => {
      let nearest = Number.MAX_VALUE;
      let nearestPlaza = null;
      plazas.forEach(plaza => {
        const distance = util.getDistance(
          lat,
          lng,
          plaza.latitude,
          plaza.longitude
        );
        if (distance < nearest) {
          nearest = distance;
          nearestPlaza = plaza;
        }
      });
      this.confirmNearestPlaza(nearestPlaza);
    });
  },
  confirmNearestPlaza(nearestPlaza) {
    this.globalData.nearestPlaza = nearestPlaza;
    console.log('nearest plaza : ' + nearestPlaza.name);
    console.log(nearestPlaza);
    console.log(this.nearestPlazaReadyCallback);

    if (this.nearestPlazaReadyCallback) {
      this.nearestPlazaReadyCallback(nearestPlaza);
    }
  },
  getAllPlaza(callback) {
    wx.request({
      url: 'http://read.lavector.com/api/wsa/plaza',
      data: {},
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: res => {
        logs.unshift(JSON.stringify(res));
        console.log(res);
        
        this.globalData.plazas = res.data;
        if (callback) {
          callback(res.data);
        }
      }
    });
  },
  getLocation(callback) {
    console.log(this.globalData);

    wx.getLocation({
      type: 'wgs84',
      success: function (res) {
        // var latitude = res.latitude;
        // var longitude = res.longitude;
        // var speed = res.speed;
        // var accuracy = res.accuracy;
        if (callback) {
          callback(res);
        }
        // wx.request({
        //   url:
        //     'http://read.lavector.com/api/wsa/plaza/nearest?latitude=' +
        //     latitude +
        //     '&longitude=' +
        //     longitude, //仅为示例，并非真实的接口地址
        //   data: {},
        //   header: {
        //     'content-type': 'application/json' // 默认值
        //   },
        //   success: function(res) {
        //     console.log(res.data);
        //   }
        // });
      }
    });
  }
});
