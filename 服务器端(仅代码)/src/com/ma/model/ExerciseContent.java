package com.ma.model;
/**
 * @ClassName: ExerciseContend  
 * @Description: 习题内容实体类  
 * @author MZ  
 * @2017年3月20日下午2:24:27
 */
public class ExerciseContent {
	private int id;				//表id
	private String quest;		//题目
	private String a_choose;	//选项A
	private String b_choose;	//选项B
	private String c_choose;	//选项C
	private String d_choose;	//选项D
	private String answer;		//答案
    private int exer_id;		//课程id
	public ExerciseContent() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuest() {
		return quest;
	}
	public void setQuest(String quest) {
		this.quest = quest;
	}
	public String getA_choose() {
		return a_choose;
	}
	public void setA_choose(String a_choose) {
		this.a_choose = a_choose;
	}
	public String getB_choose() {
		return b_choose;
	}
	public void setB_choose(String b_choose) {
		this.b_choose = b_choose;
	}
	public String getC_choose() {
		return c_choose;
	}
	public void setC_choose(String c_choose) {
		this.c_choose = c_choose;
	}
	public String getD_choose() {
		return d_choose;
	}
	public void setD_choose(String d_choose) {
		this.d_choose = d_choose;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getExer_id() {
		return exer_id;
	}
	public void setExer_id(int exer_id) {
		this.exer_id = exer_id;
	}
	@Override
	public String toString() {
		return "ExerciseContend [id=" + id + ", quest=" + quest + ", a_choose=" + a_choose + ", b_choose=" + b_choose
				+ ", c_choose=" + c_choose + ", d_choose=" + d_choose + ", answer=" + answer + ", exer_id=" + exer_id
				+ "]";
	}
    
    
}
