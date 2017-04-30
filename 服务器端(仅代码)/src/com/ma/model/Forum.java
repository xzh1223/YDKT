package com.ma.model;
/**
 * @ClassName: Forum  
 * @Description: 论坛发贴实体类  
 * @author MZ  
 * @2017年3月6日下午2:31:17
 */
public class Forum {
	private int id;					//表id
	private String username;		//用户名
	private String user_img;		//图像
	private String f_time;			//时间
	private String f_title;			//标题	
	private String f_content;		//内容
	private int f_times;			//用时
	private int user_id;			//用户id
	private String f_img;			//上传图片
	
	public Forum() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getF_time() {
		return f_time;
	}
	public void setF_time(String f_time) {
		this.f_time = f_time;
	}
	public String getF_title() {
		return f_title;
	}
	public void setF_title(String f_title) {
		this.f_title = f_title;
	}
	public String getF_content() {
		return f_content;
	}
	public void setF_content(String f_content) {
		this.f_content = f_content;
	}
	public int getF_times() {
		return f_times;
	}
	public void setF_times(int f_times) {
		this.f_times = f_times;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public String getF_img() {
		return f_img;
	}
	public void setF_img(String f_img) {
		this.f_img = f_img;
	}
	@Override
	public String toString() {
		return "Forum [id=" + id + ", username=" + username + ", user_img=" + user_img + ", f_time=" + f_time
				+ ", f_title=" + f_title + ", f_content=" + f_content + ", f_times=" + f_times + ", user_id=" + user_id
				+ ", f_img=" + f_img + "]";
	}
	
	
	
}
