package com.ma.model;
/**
 * @ClassName: User  
 * @Description: 用户实体类  
 * @author MZ  
 * @2017年3月2日下午6:30:46
 */
public class User {
	private int id;				//用户ID
	private String username;	//用户名
	private String password;	//登录密码
	private String phone;		//手机号
	private String img;			//头像
	private String msg;         //信息
	
	public User() {

	}
	public User(String username,String password,String img){
		this.username = username;
		this.password = password;
		this.img = img;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", phone=" + phone + ", img="
				+ img + ", msg=" + msg + "]";
	}
	
	
	
}
