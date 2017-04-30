package com.ma.model;
/**
 * @ClassName: ProjectUserPropress  
 * @Description: 我的课程  
 * @author MZ  
 * @2017年3月15日下午7:16:35
 */
public class ProjectUserPropress {
	private int id; 			//学习进度id
	private int user_id;		//用户id
	private int proj_id; 		//课程id
	private int sum_progress;	//总进度
	private String msg;			//提示信息
	public ProjectUserPropress() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getProj_id() {
		return proj_id;
	}
	public void setProj_id(int proj_id) {
		this.proj_id = proj_id;
	}
	public int getSum_progress() {
		return sum_progress;
	}
	public void setSum_progress(int sum_progress) {
		this.sum_progress = sum_progress;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "ProjectUserPropress [id=" + id + ", user_id=" + user_id + ", proj_id=" + proj_id + ", sum_progress="
				+ sum_progress + ", msg=" + msg + "]";
	}
	
	
}
