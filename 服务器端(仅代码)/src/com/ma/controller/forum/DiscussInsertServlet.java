package com.ma.controller.forum;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.forum.dao.impl.DiscussImplDao;
import com.ma.model.Discuss;
import com.ma.util.DateUtils;

/**
 * @ClassName: DiscussServlet  
 * @Description: 评论控制器  
 * @author MZ  
 * @2017年3月4日下午2:21:06
 */
@WebServlet("/insertDiscussList")
public class DiscussInsertServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	Discuss dis;
	DiscussImplDao disDao;
	@Override
	public void init() throws ServletException {
		dis = new Discuss();
		disDao = new DiscussImplDao();
	}
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("插入评论！");
		int flag = 500;
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		
		//接收请求参数
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		int f_id = Integer.parseInt(req.getParameter("f_id"));
		String d_time = DateUtils.getDateTime();//df.format(new Date());
		String d_content = req.getParameter("d_content");
		/*int user_id = 1;
		int f_id = 1;
		String d_time = "201404404";
		String d_content = "大";*/
		dis.setUser_id(user_id);
		dis.setF_id(f_id);
		dis.setD_time(d_time);
		dis.setD_content(d_content);
		// 输出流
		PrintWriter out = resp.getWriter();
		
		try {
			//将数据插入到数据库
			boolean tf = disDao.insertDiscussContent(dis);
			if (tf) {
				//成功返回200
				flag  = 200;
			} else {
				//失败返回500
				flag = 500;
			}
			//向客户端输出
			out.print(flag);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			out.flush();
			out.close();
	}
}
