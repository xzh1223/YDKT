package com.ma.model.feedback;
/**
 * @ClassName: Feedback  
 * @Description: 用户反馈实体类  
 * @author MZ  
 * @2017年3月20日上午9:20:10
 */
public class Feedback {
	private int id;				//id
	private String feedbackinf;	//反馈信息
	private String phone;		//联系方式
	private String msg;			//成功信息
	public Feedback() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFeedbackinf() {
		return feedbackinf;
	}
	public void setFeedbackinf(String feedbackinf) {
		this.feedbackinf = feedbackinf;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", feedbackinf=" + feedbackinf + ", phone=" + phone + ", msg=" + msg + "]";
	}
	
	
}
