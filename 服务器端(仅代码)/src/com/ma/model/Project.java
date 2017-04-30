package com.ma.model;
/**
 * @ClassName: Project  
 * @Description: 课程实体类  
 * @author MZ  
 * @2017年3月4日下午2:18:40
 */
public class Project {
	private String pro_name;	//课程名
	private String pro_image;	//图片
	private int id;				//表id
	public Project() {
		super();
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	public String getPro_image() {
		return pro_image;
	}
	public void setPro_image(String pro_image) {
		this.pro_image = pro_image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Project [pro_name=" + pro_name + ", pro_image=" + pro_image + ", id=" + id + "]";
	}
	
	
	
}
