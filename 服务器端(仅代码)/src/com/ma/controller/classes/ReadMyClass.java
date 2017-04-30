package com.ma.controller.classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.model.Project;
import com.ma.project.dao.impl.ProjectImplDao;
import com.ma.util.JsonUtil;

/**
 * @ClassName: ReadMyClass  
 * @Description: 读取我的课程  
 * @author MZ  
 * @2017年3月16日下午1:20:35
 */
@WebServlet("/readMyClass")
public class ReadMyClass extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Project p;
	ProjectImplDao proDao;
	@Override
	public void init() throws ServletException {
		p = new Project();
		proDao = new ProjectImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("读取我的课程！");
		//获取请求参数
		int user_id = Integer.valueOf(req.getParameter("user_id"));
		//输出
		PrintWriter out = resp.getWriter();
		List<Project> pro = new ArrayList<Project>();
		try {
			pro = proDao.readMyClass(user_id);
			//返回json数据
			String jsondata = JsonUtil.proToJson(pro);
			//向客户端输出
			out.print(jsondata);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 清空输出流缓冲区
		out.flush();
		out.close();
	}
}
