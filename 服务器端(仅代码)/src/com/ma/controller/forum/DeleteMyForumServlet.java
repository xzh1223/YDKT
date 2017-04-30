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
 * @ClassName: DeleteMyForumServlet  
 * @Description: 删除我的帖子  
 * @author MZ  
 * @2017年3月24日上午10:27:23
 */
@WebServlet("/deleteMyForum")
public class DeleteMyForumServlet extends HttpServlet{

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
		System.out.println("删除我的帖子！");
		PrintWriter out = resp.getWriter();
		
		int id = Integer.parseInt(req.getParameter("id"));
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		System.out.println("id==="+id+"  user_id=="+user_id);
		int flag = 500;
		
		try {
			boolean is = forumDao.deleteMyForum(id, user_id);
			System.out.println(is);
			if(is){
				flag = 200;
			}else{
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
