package com.ma.model;
/**
 * @ClassName: ClassTitle  
 * @Description: 课程名  
 * @author MZ  
 * @2017年3月4日下午12:57:18
 */
public class ClassTitle {
	private int id;			//表id
	private String c_name;  //课程名
	private String c_img;   //课程图片
	private int proj_id;    //课程Id
	private int curPro;		//当前进度
	private int allPro;		//总进度
	private String msg;		//记录信息
	
	public ClassTitle() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getC_img() {
		return c_img;
	}

	public void setC_img(String c_img) {
		this.c_img = c_img;
	}

	public int getProj_id() {
		return proj_id;
	}

	public void setProj_id(int proj_id) {
		this.proj_id = proj_id;
	}

	public String getMsg() {
		return msg;
	}
	
	public int getCurPro() {
		return curPro;
	}

	public void setCurPro(int curPro) {
		this.curPro = curPro;
	}

	public int getAllPro() {
		return allPro;
	}

	public void setAllPro(int allPro) {
		this.allPro = allPro;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ClassTitle [id=" + id + ", c_name=" + c_name + ", c_img=" + c_img + ", proj_id=" + proj_id + ", curPro="
				+ curPro + ", allPro=" + allPro + ", msg=" + msg + "]";
	}

	
	
	
}
