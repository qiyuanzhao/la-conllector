var WxParse = require('../../wxParse/wxParse.js');
const app = getApp();
const articleData = {
        title: '我是标题',
        author: '王小二',
        date: '2017-12-20',
        readNum: 111,
        likeNum: 22,
        content: `<div data-note-content="" class="show-content">
        <blockquote>
<p>为API生，为框架死，为debug奋斗一生</p>
</blockquote>
<h2>前言</h2>
<p>最近公司有个项目需要用到小程序的map组件，要实现一个类似摩拜红包车的信息弹框,简单说就是在map组件上添加自定义view。<br>
</p><div class="image-package">
<div class="image-container" style="max-width: 700px; max-height: 906px; background-color: transparent;">
<div class="image-container-fill" style="padding-bottom: 129.54000000000002%;"></div>
<div class="image-view" data-width="1080" data-height="1399"><img data-original-src="//upload-images.jianshu.io/upload_images/6602491-c003cfab3f8fafb9.jpg" data-original-width="1080" data-original-height="1399" data-original-format="image/jpeg" data-original-filesize="119635" class="" style="cursor: zoom-in;" src="//upload-images.jianshu.io/upload_images/6602491-c003cfab3f8fafb9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/700"></div>
</div>
<div class="image-caption">摩拜红包车</div>
</div>    <br>
直接在map上使用view，image等是不行的，因为查看<a href="https://link.jianshu.com?t=https://mp.weixin.qq.com/debug/wxadoc/dev/component/map.html#map" target="_blank" rel="nofollow">微信小程序API</a>底部Bug&amp;Tip有一句话，<strong>map 组件是由客户端创建的原生组件，它的层级是最高的。</strong>所以直接在map上使用view等没有效果的，即使使用z-index改变z轴也是没用的，网上也查了很多，都说是没办法使用这种布局的。<p></p>
<p>后来仔细查看微信小程序API文档，发现一个控件<a href="https://link.jianshu.com?t=https://mp.weixin.qq.com/debug/wxadoc/dev/component/cover-view.html" target="_blank" rel="nofollow">cover-view</a>,没错，就是今天的主角。查看文档第一行发现有这么一句话，<strong>覆盖在原生组件之上的文本视图，可覆盖的原生组件包括map、video、canvas、camera，只支持嵌套cover-view、cover-image。</strong>有了这句话我就心安了，看来是可以实现了。</p>
<h2>代码实现</h2>
<p>.wxml</p>
<pre class="hljs undefined"><code>  &lt;map id="map" longitude="{{point.longitude}}" latitude="{{point.latitude}}" 
markers="{{markers}}" scale="{{mapScale}}" show-location  bindregionchange="regionchange" 
bindmarkertap="markertap" controls="{{controls}}" bindcontroltap="controltap" 
style="width: {{mapWidth}}; height: {{mapHeight}};top: {{mapTop}}"&gt; 
&lt;cover-view class="place_info"&gt;
&lt;cover-view class="place_info_parking"&gt;cover-view&lt;/cover-view&gt;
&lt;cover-view class="place_info_surplus"&gt;可覆盖在原生组件的组件&lt;/cover-view&gt;
&lt;cover-view class="place_info_order"&gt;66666&lt;/cover-view&gt;
&lt;/cover-view&gt;
&lt;/map&gt;    
</code></pre>
<p>.wxss</p>
<pre class="hljs undefined"><code>  .place_info {
position: relative;
width: 90%;
margin-left: 20rpx; 
margin-top: 10rpx; 
border-radius: 5rpx;
background: white;
padding: 10rpx;
}
.place_info_parking{
font-size: 50rpx
}
.place_info_surplus{
color: #999999
}
.place_info_order{
margin-top: 10rpx;
margin-bottom: 10rpx; 
margin-left: 20rpx;   
background-color: #ff5722;
color: #FFFFFF;
padding: 10rpx;
width: 90%;
border-radius: 5rpx;
text-align: center;
}
</code></pre>
<h2>效果图</h2>
<div class="image-package">
<div class="image-container" style="max-width: 700px; max-height: 498px; background-color: transparent;">
<div class="image-container-fill" style="padding-bottom: 67.12%;"></div>
<div class="image-view" data-width="742" data-height="498"><img data-original-src="//upload-images.jianshu.io/upload_images/6602491-eb576bfaebc78ab9.png" data-original-width="742" data-original-height="498" data-original-format="image/png" data-original-filesize="212509" class="" style="cursor: zoom-in;" src="//upload-images.jianshu.io/upload_images/6602491-eb576bfaebc78ab9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700"></div>
</div>
<div class="image-caption">map上添加view</div>
</div>

      </div>`,
    }
Page({
  data: {
    article: articleData,
    parseContent: 'xxxxxx'
  },
  onLoad: function(option) {
    console.log(option);
    const articleId = option.id;
    var that = this;
WxParse.wxParse('parseContent', 'html', articleData.content, that, 5);
    
  }
});
