package com.ma.controller.classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.classes.dao.impl.ClassTitleImplDao;
import com.ma.model.ClassContent;
import com.ma.util.JsonUtil;

/**
 * @ClassName: ClassContentServlet  
 * @Description: 课程内容控制器  
 * @author MZ  
 * @2017年3月5日下午5:14:16
 */
@WebServlet("/readClassContent")
public class ClassContentServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	ClassContent cc;
	ClassTitleImplDao ctDao;
	@Override
	public void init() throws ServletException {
		cc = new ClassContent();
		ctDao = new ClassTitleImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//接收请求参数
		int tit_id = Integer.valueOf(req.getParameter("tit_id"));
//		int tit_id = 1;
		// 输出流
		PrintWriter out = resp.getWriter();
		
		try {
			//根据tit_id查询课程内容
			cc = ctDao.queryContentByTit_id(tit_id);
			//将课程内容转换成json数据
			String jsondata = JsonUtil.conToJson(cc);
			//向客户端输出数据
			out.print(jsondata);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 清空输出流缓冲区
		out.flush();
		out.close();
	}
}
