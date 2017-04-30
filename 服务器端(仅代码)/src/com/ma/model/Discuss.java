package com.ma.model;
/**
 * @ClassName: Discuss  
 * @Description: 评论实体类  
 * @author MZ  
 * @2017年3月4日下午2:13:06
 */
public class Discuss {
	private int id;
	private String d_content; 	// 发帖内容
	private String d_time; 		// 发帖时间
	private String username; 	// 发帖用户名
	private String user_img; 	// 用户头像
	private int f_id;
	private int user_id;
	private String msg;
	public Discuss() {

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getD_content() {
		return d_content;
	}
	public void setD_content(String d_content) {
		this.d_content = d_content;
	}
	public String getD_time() {
		return d_time;
	}
	public void setD_time(String d_time) {
		this.d_time = d_time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public int getF_id() {
		return f_id;
	}
	public void setF_id(int f_id) {
		this.f_id = f_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Discuss [id=" + id + ", d_content=" + d_content + ", d_time=" + d_time + ", username=" + username
				+ ", user_img=" + user_img + ", f_id=" + f_id + ", user_id=" + user_id + ", msg=" + msg + "]";
	}
	
	
	
}
