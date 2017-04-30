package com.ma.model;
/**
 * 测试使用
 * @author Administrator
 *
 */
public class Person {
	private int id;
	private String name;
	private int password;
	
	private String msg;
	
	public Person(){
		
	}
	public Person(String name,int password,String birthday){
		this.name = name;
		this.password = password;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPassword() {
		return password;
	}
	public void setPassword(int password) {
		this.password = password;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + password
			   + "]";
	}
}
