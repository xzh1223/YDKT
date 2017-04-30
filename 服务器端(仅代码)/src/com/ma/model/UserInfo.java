package com.ma.model;
/**
 * @ClassName: UserInfo  
 * @Description: 用户信息实体类  
 * @author MZ  
 * @2017年3月3日上午10:35:08
 */
public class UserInfo {
	private String sex; 			// 性别
	private int age; 				// 年龄
	private String profession; 		// 专业
	private String phone; 			// 手机号
	private String email; 			// 邮箱
	private String constellation; 	// 星座
	private String home; 			// 家乡
	private String description; 	// 个人说明
	private String msg;				//错误信息
	public UserInfo() {
	}


	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	@Override
	public String toString() {
		return "UserInfo [sex=" + sex + ", age=" + age + ", profession=" + profession + ", phone=" + phone + ", email="
				+ email + ", constellation=" + constellation + ", home=" + home + ", description=" + description
				+ ", msg=" + msg + "]";
	}

}
