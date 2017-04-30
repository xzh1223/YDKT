package com.ma.controller.forum;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.forum.dao.impl.ForumImplDao;
import com.ma.model.Forum;

/**
 * @ClassName: ReadForumCountServlet  
 * @Description: 读取帖子数  
 * @author MZ  
 * @2017年3月20日下午1:35:58
 */
@WebServlet("/readForumCount")
public class ReadForumCountServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	Forum forum;
	ForumImplDao forumDao;
	@Override
	public void init() throws ServletException {
		forum = new Forum();
		forumDao = new ForumImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("读取帖子条数");
		//输出
		PrintWriter out = resp.getWriter();
		int forumCount;
		try {
			forumCount = forumDao.readForumCount();
			forumCount = forumCount % 5 == 0 ? forumCount/5 : (forumCount/5)+1;
			out.print(forumCount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
