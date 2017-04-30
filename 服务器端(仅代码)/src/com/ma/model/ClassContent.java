package com.ma.model;
/**
 * @ClassName: ClassContent  
 * @Description: 课程内容实体类  
 * @author MZ  
 * @2017年3月4日下午2:09:01
 */
public class ClassContent {
	
	private int id;				//表id
	private String c_content;	//课程内容
	private int tit_id;			//课程标题id
	private String msg;			//信息
	
	public ClassContent() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getC_content() {
		return c_content;
	}

	public void setC_content(String c_content) {
		this.c_content = c_content;
	}

	public int getTit_id() {
		return tit_id;
	}

	public void setTit_id(int tit_id) {
		this.tit_id = tit_id;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ClassContent [id=" + id + ", c_content=" + c_content + ", tit_id=" + tit_id + ", msg=" + msg + "]";
	}

}
