package com.ma.test;

import java.sql.Connection;

import com.ma.dao.JdbcUtil;

public class Test {
	
	public int getNum(int forumCount){
		return forumCount = forumCount % 5 == 0 ? forumCount/5 : (forumCount/5)+1;
	}
	 public static void main(String[] args) throws Exception {
        /*Connection conn=JdbcUtil.getConnection();//利用封装好的类名来调用连接方法便可
        if(conn!=null){
        	System.out.println(conn+",成功连接数据库");
        }else{
        	System.out.println("连接失败");
        }
        JdbcUtil.close(null,null,conn);//同样利用类名调用关闭方法即可
*/        
        Test t = new Test();
        int s = t.getNum(17);
        System.out.println(s);
	 } 
}
