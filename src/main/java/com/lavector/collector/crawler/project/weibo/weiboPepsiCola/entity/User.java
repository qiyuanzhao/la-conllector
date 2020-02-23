
package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.entity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
	private String id;                      //用户UID
	private String screen_name;            //微博昵称
	private String name;                  //友好显示名称，如Bill Gates,名称中间的空格正常显示(此特性暂不支持)
	private int province;                 //省份编码（参考省份编码表）
	private int city;                     //城市编码（参考城市编码表）
	private String location;              //地址
	private String description;           //个人描述
	private String url;                   //用户博客地址
	private String profile_image_url;       //自定义图像
	private String cover_image;
	private String cover_image_phone;
	private String profile_url;
	private String domain;            //用户个性化URL
	private String gender;                //性别,m--男，f--女,n--未知
	private int followers_count;           //粉丝数
	private int friends_count;             //关注数
	private int pagefriends_count;
	private int statuses_count;            //微博数
	private int favourites_count;          //收藏数
	private Date created_at;               //创建时间
	private boolean following;            //保留字段,是否已关注(此特性暂不支持)
	private String allow_all_act_msg;
	private String geo_enabled;
	private boolean verified;             //加V标示，是否微博认证用户
	private int verified_type;             //认证类型
//	private boolean allow_all_actMsg;       //是否允许所有人给我发私信
	private boolean allow_all_comment;      //是否允许所有人对我的微博进行评论
	private boolean follow_me;             //此用户是否关注我
	private String avatar_large;           //大头像地址
	private String avatar_hd;
	private int online_status;             //用户在线状态
	private Status status = null;         //用户最新一条微博
	private int bi_followers_count;         //互粉数
	private String remark;                //备注信息，在查询用户关系时提供此字段。
	private String lang;                  //用户语言版本
	private long star;
	private String verified_reason;		  //认证原因
	private int verified_trade;
	private String verified_reason_url;
	private String verified_source;
	private String verified_source_url;
	private int verified_state;
	private int verified_level;
	private int verified_type_ext;
	private String verified_reason_modified;
	private String verified_contact_name;
	private String verified_contact_email;
	private String verified_contact_mobile;
	private String ability_tags; //技能标签
	private String weihao;				  //微號
	private String urank;					//等级
	private String mtype;
	private String mrank;
	private int block_word;
	private int block_app;
	private int credit_score;
	private int user_ability;
	private String cardid;
	private String avatargj_id;
	private String status_id;
	@JsonDeserialize(using = LastAtDeserializer.class)
	private Date last_at;
	private Double lon;
	private Double lat;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerified_type() {
		return verified_type;
	}

	public void setVerified_type(int verified_type) {
		this.verified_type = verified_type;
	}

	public boolean isAllow_all_comment() {
		return allow_all_comment;
	}

	public void setAllow_all_comment(boolean allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public boolean isFollow_me() {
		return follow_me;
	}

	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public int getOnline_status() {
		return online_status;
	}

	public void setOnline_status(int online_status) {
		this.online_status = online_status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(int bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public String getAbility_tags() {
		return ability_tags;
	}

	public void setAbility_tags(String ability_tags) {
		this.ability_tags = ability_tags;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public String getUrank() {
		return urank;
	}

	public void setUrank(String urank) {
		this.urank = urank;
	}

	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public String getCover_image_phone() {
		return cover_image_phone;
	}

	public void setCover_image_phone(String cover_image_phone) {
		this.cover_image_phone = cover_image_phone;
	}

	public String getProfile_url() {
		return profile_url;
	}

	public void setProfile_url(String profile_url) {
		this.profile_url = profile_url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getPagefriends_count() {
		return pagefriends_count;
	}

	public void setPagefriends_count(int pagefriends_count) {
		this.pagefriends_count = pagefriends_count;
	}

	public String getAllow_all_act_msg() {
		return allow_all_act_msg;
	}

	public void setAllow_all_act_msg(String allow_all_act_msg) {
		this.allow_all_act_msg = allow_all_act_msg;
	}

	public String getGeo_enabled() {
		return geo_enabled;
	}

	public void setGeo_enabled(String geo_enabled) {
		this.geo_enabled = geo_enabled;
	}

	public String getAvatar_hd() {
		return avatar_hd;
	}

	public void setAvatar_hd(String avatar_hd) {
		this.avatar_hd = avatar_hd;
	}

	public long getStar() {
		return star;
	}

	public void setStar(long star) {
		this.star = star;
	}

	public int getVerified_trade() {
		return verified_trade;
	}

	public void setVerified_trade(int verified_trade) {
		this.verified_trade = verified_trade;
	}

	public String getVerified_reason_url() {
		return verified_reason_url;
	}

	public void setVerified_reason_url(String verified_reason_url) {
		this.verified_reason_url = verified_reason_url;
	}

	public String getVerified_source() {
		return verified_source;
	}

	public void setVerified_source(String verified_source) {
		this.verified_source = verified_source;
	}

	public String getVerified_source_url() {
		return verified_source_url;
	}

	public void setVerified_source_url(String verified_source_url) {
		this.verified_source_url = verified_source_url;
	}

	public int getVerified_state() {
		return verified_state;
	}

	public void setVerified_state(int verified_state) {
		this.verified_state = verified_state;
	}

	public int getVerified_level() {
		return verified_level;
	}

	public void setVerified_level(int verified_level) {
		this.verified_level = verified_level;
	}

	public int getVerified_type_ext() {
		return verified_type_ext;
	}

	public void setVerified_type_ext(int verified_type_ext) {
		this.verified_type_ext = verified_type_ext;
	}

	public String getVerified_reason_modified() {
		return verified_reason_modified;
	}

	public void setVerified_reason_modified(String verified_reason_modified) {
		this.verified_reason_modified = verified_reason_modified;
	}

	public String getVerified_contact_name() {
		return verified_contact_name;
	}

	public void setVerified_contact_name(String verified_contact_name) {
		this.verified_contact_name = verified_contact_name;
	}

	public String getVerified_contact_email() {
		return verified_contact_email;
	}

	public void setVerified_contact_email(String verified_contact_email) {
		this.verified_contact_email = verified_contact_email;
	}

	public String getVerified_contact_mobile() {
		return verified_contact_mobile;
	}

	public void setVerified_contact_mobile(String verified_contact_mobile) {
		this.verified_contact_mobile = verified_contact_mobile;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public String getMrank() {
		return mrank;
	}

	public void setMrank(String mrank) {
		this.mrank = mrank;
	}

	public int getBlock_word() {
		return block_word;
	}

	public void setBlock_word(int block_word) {
		this.block_word = block_word;
	}

	public int getBlock_app() {
		return block_app;
	}

	public void setBlock_app(int block_app) {
		this.block_app = block_app;
	}

	public int getCredit_score() {
		return credit_score;
	}

	public void setCredit_score(int credit_score) {
		this.credit_score = credit_score;
	}

	public int getUser_ability() {
		return user_ability;
	}

	public void setUser_ability(int user_ability) {
		this.user_ability = user_ability;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getAvatargj_id() {
		return avatargj_id;
	}

	public void setAvatargj_id(String avatargj_id) {
		this.avatargj_id = avatargj_id;
	}

	public Date getLast_at() {
		return last_at;
	}

	public void setLast_at(Date last_at) {
		this.last_at = last_at;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}


	public static class LastAtDeserializer extends JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			try {
				String dateStr = jsonParser.readValueAs(String.class);
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				return dt.parse(dateStr);
			} catch (ParseException e) {
			    return null;
			}
		}
	}

}
