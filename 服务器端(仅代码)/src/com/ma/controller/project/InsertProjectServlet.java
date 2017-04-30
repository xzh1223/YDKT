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

import com.ma.model.ClassTitle;
import com.ma.model.ProjectUserPropress;
import com.ma.project.dao.impl.ProjectImplDao;

/**
 * @ClassName: InsertProjectServlet  
 * @Description: 保存我的课程  
 * @author MZ  
 * @2017年3月15日下午7:56:02
 */
@WebServlet("/saveMyProject")
public class InsertProjectServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	ProjectUserPropress pup;
	ProjectImplDao projDao;
	@Override
	public void init() throws ServletException {
		pup = new ProjectUserPropress();
		projDao = new ProjectImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("保存我的课程！");
		int flag;	//返回标志
		int proj_id = Integer.valueOf(req.getParameter("proj_id"));
		int user_id = Integer.valueOf(req.getParameter("user_id"));
		
		PrintWriter out = resp.getWriter();
		
		pup.setProj_id(proj_id);
		pup.setUser_id(user_id);
		List<ClassTitle> classTitle = new ArrayList<ClassTitle>();
		try {
			//根据proj_id在class_title查询章节id
			classTitle = projDao.queryTitleIdByProj_id(proj_id);
			//将章节id插入到title_users_progress proj_id, user_id,title_id
			projDao.insertIntoProgress(classTitle, pup);
			//插入课程
			boolean tf = projDao.insertProject(pup);
			if (tf) {
				flag = 200;
			} else {
				flag = 500;
			}
			out.print(flag);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
		
	}
}
