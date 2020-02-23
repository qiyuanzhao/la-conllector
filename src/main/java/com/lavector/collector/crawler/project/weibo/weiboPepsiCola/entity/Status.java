package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Status {

	private User user = null;                            //作者信息
	private Date created_at;                              //status创建时间
	private String id;                                   //status id
	private String mid;                                  //微博MID
	private long idstr;                                  //保留字段，请勿使用                     
	private String text;                                 //微博内容
	private String source;                               //微博来源
	private boolean favorited;                           //是否已收藏
	private boolean truncated;
	private long in_reply_to_status_id;                      //回复ID
	private long in_reply_To_userId;                        //回复人ID
	private String in_reply_to_screen_name;                  //回复人昵称
	private String thumbnail_pic;                         //微博内容中的图片的缩略地址
	private String bmiddle_pic;                           //中型图片
	private String original_pic;                          //原始图片
	private Status retweeted_status = null;               //转发的博文，内容为status，如果不是转发，则没有此字段
	private Map geo;                                  //地理信息，保存经纬度，没有时不返回此字段
	private double latitude = -1;                        //纬度
	private double longitude = -1;                       //经度
	private int reposts_count;                            //转发数
	private int comments_count;                           //评论数
	private int attitudes_count;						  //点赞数
	private List<String> pic_ids;
	private int mlevel;
	@JsonDeserialize(using = DefinitionDeserializer.class)
	private List<Map> annotations;                          //元数据，没有时不返回此字段
	private Visible visible;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public long getIdstr() {
		return idstr;
	}

	public void setIdstr(long idstr) {
		this.idstr = idstr;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public long getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(long in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public long getIn_reply_To_userId() {
		return in_reply_To_userId;
	}

	public void setIn_reply_To_userId(long in_reply_To_userId) {
		this.in_reply_To_userId = in_reply_To_userId;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public Status getRetweeted_status() {
		return retweeted_status;
	}

	public void setRetweeted_status(Status retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	public Map getGeo() {
		return geo;
	}

	public void setGeo(Map geo) {
		this.geo = geo;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}

	public List<String> getPic_ids() {
		return pic_ids;
	}

	public void setPic_ids(List<String> pic_ids) {
		this.pic_ids = pic_ids;
	}

	public int getMlevel() {
		return mlevel;
	}

	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}

	public Visible getVisible() {
		return visible;
	}

	public void setVisible(Visible visible) {
		this.visible = visible;
	}

	public List<Map> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Map> annotations) {
		this.annotations = annotations;
	}

	public static class DefinitionDeserializer extends JsonDeserializer<List<Map>> {

		@Override
		public List<Map> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			List<Map> maps = new LinkedList<>();
			try {
				maps = jsonParser.readValueAs(new TypeReference<List<Map>>() {
				});
				return maps;
			} catch (IOException e) {
				return maps;
			}
		}
	}

	@Override
	public String toString() {
		return "Status{" +
				"user=" + user +
				", created_at=" + created_at +
				", id='" + id + '\'' +
				", mid='" + mid + '\'' +
				", idstr=" + idstr +
				", text='" + text + '\'' +
				", source='" + source + '\'' +
				", favorited=" + favorited +
				", truncated=" + truncated +
				", in_reply_to_status_id=" + in_reply_to_status_id +
				", in_reply_To_userId=" + in_reply_To_userId +
				", in_reply_to_screen_name='" + in_reply_to_screen_name + '\'' +
				", thumbnail_pic='" + thumbnail_pic + '\'' +
				", bmiddle_pic='" + bmiddle_pic + '\'' +
				", original_pic='" + original_pic + '\'' +
				", retweeted_status=" + retweeted_status +
				", geo=" + geo +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", reposts_count=" + reposts_count +
				", comments_count=" + comments_count +
				", attitudes_count=" + attitudes_count +
				", pic_ids=" + pic_ids +
				", mlevel=" + mlevel +
				", annotations=" + annotations +
				", visible=" + visible +
				'}';
	}
}
