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

import com.ma.forum.dao.impl.DiscussImplDao;
import com.ma.model.Discuss;
import com.ma.util.JsonUtil;

/**
 * @ClassName: DiscussServlet  
 * @Description: 评论控制器  
 * @author MZ  
 * @2017年3月4日下午2:21:06
 */
@WebServlet("/readDiscussList")
public class DiscussReadServlet extends HttpServlet{

	/**  
	 * @Fields field:field:{todo} 
	 */
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
		System.out.println("读取评论！");
		//输出流
		PrintWriter out = resp.getWriter();
		//接收请求参数
		int f_id = Integer.valueOf(req.getParameter("f_id"));
//		int f_id = 1;
		//List集合，用于保存返回的论坛数据
		List<Discuss> list = new ArrayList<Discuss>();
		
		try {
			//根据f_id查询论坛信息
			list = disDao.queryDiscussByF_id(f_id);
			
			//返回json数据
			String jsondata = JsonUtil.disToJson(list);
			
			out.print(jsondata);
			//System.out.println(jsondata);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
