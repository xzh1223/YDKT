package com.ma.controller.classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.model.ProjectUserPropress;
import com.ma.project.dao.impl.ProjectImplDao;

/**
 * @ClassName: DeleteMyClass  
 * @Description: 删除我的课程  
 * @author MZ  
 * @2017年3月16日下午3:02:44
 */
@WebServlet("/deleteMyClass")
public class DeleteMyClass extends HttpServlet{

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
		System.out.println("删除我的课程！");
		PrintWriter out = resp.getWriter();
		
		int flag;	//返回标志
		int proj_id = Integer.valueOf(req.getParameter("proj_id"));
		int user_id = Integer.valueOf(req.getParameter("user_id"));
		
		System.out.println("proj_id=="+proj_id+"  user_id==="+user_id);
		pup.setProj_id(proj_id);
		pup.setUser_id(user_id);
		
		try {
			boolean tf = projDao.deleteMyClass(pup);
			if (tf) {
				flag = 200;
			} else {
				flag = 500;
			}
			out.print(flag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
