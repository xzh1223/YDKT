package com.ma.test;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.dao.impl.PersonImplDao;
import com.ma.model.Person;

public class TestServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	PersonImplDao pi;
	Person per;

	@Override
	public void init() throws ServletException {
		pi = new PersonImplDao();
		per = new Person();
		
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");
		boolean flag=false;
		int id = 1234;
        try {
        	//pi.add(per);
        	per = pi.findById(id);
        	if(per.getMsg()!=null){
        		per.setId(id);
	  	        per.setName("马壮");
	  	        per.setPassword(1234567);
	  	        flag = pi.insert(per);
	        	if (flag) {
	        		System.out.println("插入成功");
					}else{
					System.out.println("插入失败");
	        		}
        	}else{
        		System.out.println("no repeat");
			}
		}catch (SQLException e) {
        	e.printStackTrace();
		} 
	}	
}

