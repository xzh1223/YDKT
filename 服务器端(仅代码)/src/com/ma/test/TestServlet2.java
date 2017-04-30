package com.ma.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ma.dao.impl.PersonImplDao;
import com.ma.model.Person;

public class TestServlet2 extends HttpServlet{

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
		resp.setContentType("text/html;charset=utf-8");
		List<Person> listPerson = new ArrayList<Person>();
		int id = 123;
        try {
        	per = pi.findById(id);
    		if(!(per.getMsg()!=null)){//
			System.out.println(per);
    		}else{
    			System.out.println("没有此人");
    		}
    		listPerson = pi.findAll();
    		System.out.println(listPerson);
    		int result = pi.delete(id);
    		if(result==-1){
    			System.out.println("删除失败！");
    		}else if(result>0){
    			System.out.println("删除成功！");
    		}else if(result==0){
    			System.out.println("没有数据");
    		}
		}catch (SQLException e) {
        	e.printStackTrace();
        } 
	}	
}
