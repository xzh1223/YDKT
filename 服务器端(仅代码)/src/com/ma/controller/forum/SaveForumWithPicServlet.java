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
import com.ma.util.DateUtils;

/**
 * @ClassName: SaveForumServlet  
 * @Description: 保存论坛发布信息 没有图片 
 * @author MZ  
 * @2017年3月6日下午6:51:12
 */
@WebServlet("/insertForumList")
public class SaveForumWithPicServlet extends HttpServlet{
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
		System.out.println("保存帖子！");
		int flag;	//返回标志
		
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// 输出流
		PrintWriter out = resp.getWriter();
		//接收请求参数
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		String f_time = DateUtils.getDateTime();//df.format(new Date());
		String f_title = req.getParameter("f_title");
		String f_content = req.getParameter("f_content");
		int f_times = 0;
			
		forum.setUser_id(user_id);
		forum.setF_time(f_time);
		forum.setF_title(f_title);
		forum.setF_content(f_content);
		forum.setF_times(f_times);
		
		boolean tf;
		try {
			//插入数据
			tf = forDao.insertForum(forum);
			//根据返回结果判断是否插入成功
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