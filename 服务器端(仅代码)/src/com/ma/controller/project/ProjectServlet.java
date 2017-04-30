package com.ma.controller.project;

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
 * @ClassName: ProjectServlet  
 * @Description: 课程  
 * @author MZ  
 * @2017年3月6日上午10:18:05
 */
@WebServlet("/readProj")
public class ProjectServlet extends HttpServlet{
	
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
		
		// 输出流
		PrintWriter out = resp.getWriter();
		List<Project> pro = new ArrayList<Project>();
		
		try {
			//查询所有课程
			pro = proDao.queryProject();
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
