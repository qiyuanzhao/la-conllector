package com.lavector.collector.crawler.project.weibo.weiboPepsiCola.entity;

import java.util.List;

public class StatusWapper {

	private List<Status> statuses;

	private List<Status> reposts;

	private long previous_cursor;

	private long next_cursor;

	private long total_number = -1;
	
	private String hasvisible;

	private String request;

	private String error_code;

	private String error;

	public List<Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<Status> statuses) {
		this.statuses = statuses;
	}

	public List<Status> getReposts() {
		return reposts;
	}

	public void setReposts(List<Status> reposts) {
		this.reposts = reposts;
	}

	public long getPrevious_cursor() {
		return previous_cursor;
	}

	public void setPrevious_cursor(long previous_cursor) {
		this.previous_cursor = previous_cursor;
	}

	public long getNext_cursor() {
		return next_cursor;
	}

	public void setNext_cursor(long next_cursor) {
		this.next_cursor = next_cursor;
	}

	public long getTotal_number() {
		return total_number;
	}

	public void setTotal_number(long total_number) {
		this.total_number = total_number;
	}

	public String getHasvisible() {
		return hasvisible;
	}

	public void setHasvisible(String hasvisible) {
		this.hasvisible = hasvisible;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPrintError() {
		return "weibo-api-returnerror,{ request : " + this.request + ",\n" + "error_code: " + this.error_code + ", \n" + "error: " + this.error + "}";
	}


	@Override
	public String toString() {
		return "StatusWapper{" +
				"statuses=" + statuses +
				", reposts=" + reposts +
				", previous_cursor=" + previous_cursor +
				", next_cursor=" + next_cursor +
				", total_number=" + total_number +
				", hasvisible='" + hasvisible + '\'' +
				", request='" + request + '\'' +
				", error_code='" + error_code + '\'' +
				", error='" + error + '\'' +
				'}';
	}
}
