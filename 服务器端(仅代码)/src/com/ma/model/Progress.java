package com.ma.model;
/**
 * @ClassName: Progress  
 * @Description: 学习进度实体类  
 * @author MZ  
 * @2017年3月15日上午10:47:29
 */
public class Progress {
	private int id; 		//学习进度id
	private int curPro;     //当前学习进度
	private int allPro;		//总进度
	private int user_id;	//用户id
	private int title_id;   //章节id
	private int project_id; //课程id
	private String msg;		//提示信息
	
	public Progress() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getTitle_id() {
		return title_id;
	}

	public void setTitle_id(int title_id) {
		this.title_id = title_id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Progress [id=" + id + ", curPro=" + curPro + ", allPro=" + allPro + ", user_id=" + user_id
				+ ", title_id=" + title_id + ", project_id=" + project_id + ", msg=" + msg + "]";
	}
	
	
	
}
