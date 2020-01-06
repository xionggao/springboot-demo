package com.xxy.sfl.pub.utils;

import java.io.Serializable;

/**
 * 后端返回前端的JSON格式数据模型
 * 
 * @author xg
 */
public class JsonBackData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4679600279829055665L;

	private boolean success;

	private Object backData;

	private String backMsg;

	public JsonBackData() {
		this.success = true;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getBackData() {
		return backData;
	}

	public void setBackData(Object backData) {
		this.backData = backData;
	}

	public String getBackMsg() {
		return backMsg;
	}

	public void setBackMsg(String backMsg) {
		this.backMsg = backMsg;
	}
}
