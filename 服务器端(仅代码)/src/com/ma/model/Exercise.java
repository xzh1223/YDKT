package com.ma.model;
/**
 * @ClassName: Exercise  
 * @Description: 练习实体类  
 * @author MZ  
 * @2017年3月6日上午10:46:14
 */
public class Exercise {
	private int id;				//课程id
	private String e_name;		//课程名
	private String e_image;		//图片
	private String msg;
	public Exercise() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getE_name() {
		return e_name;
	}
	public void setE_name(String e_name) {
		this.e_name = e_name;
	}
	public String getE_image() {
		return e_image;
	}
	public void setE_image(String e_image) {
		this.e_image = e_image;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Exercise [id=" + id + ", e_name=" + e_name + ", e_image=" + e_image + ", msg=" + msg + "]";
	}
	
	
	
}
