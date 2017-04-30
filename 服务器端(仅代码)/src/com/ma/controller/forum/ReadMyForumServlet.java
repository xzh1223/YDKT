package com.ma.controller.forum;

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

import com.ma.forum.dao.impl.ForumImplDao;
import com.ma.model.Forum;
import com.ma.util.JsonUtil;

/**
 * @ClassName: ReadMyForumServlet  
 * @Description: 读取与我相关的帖子信息  
 * @author MZ  
 * @2017年3月23日下午8:09:45
 */
@WebServlet("/readMyForum")
public class ReadMyForumServlet extends HttpServlet{

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
		//输出
		PrintWriter out = resp.getWriter();
		
		//接收请求参数
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		
		List<Forum> forumList = new ArrayList<Forum>();
		try {
			//查询我的帖子
			forumList = forumDao.readMyForumById(user_id);
			//转换json
			String jsondata = JsonUtil.myForumToJson(forumList);
			//输出
			out.print(jsondata);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		out.flush();
		out.close();
	}
	
}
