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
 * @ClassName: ForumReadServlet  
 * @Description: 读取论坛内容  
 * @author MZ  
 * @2017年3月6日下午4:54:19
 */
@WebServlet("/readForumList")
public class ForumReadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	Forum forum;
	ForumImplDao forDao;
	@Override
	public void init() throws ServletException {
		forum = new Forum();
		forDao = new ForumImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("读取帖子内容！");
		// 输出流
		PrintWriter out = resp.getWriter();
		String limit = req.getParameter("limit");
		
		List<Forum> list = new ArrayList<Forum>();
		
		try {
			//查询论坛信息
			list = forDao.queryForum();
			//转换格式
			String jsondata = JsonUtil.forumToJson(list);
			//输出
			out.print(jsondata);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 清空输出流缓冲区
		out.flush();
		out.close();
	}
}
