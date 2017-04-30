package com.ma.controller.classes;

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

import com.ma.classes.dao.impl.ClassTitleImplDao;
import com.ma.model.ClassTitle;
import com.ma.util.JsonUtil;
/**
 * @ClassName: ClassTitleServlet  
 * @Description: 课程题控制器  
 * @author MZ  
 * @2017年3月4日下午1:11:50
 */
@WebServlet("/readClassTitle")
public class ClassTitleServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	ClassTitle classTitle;
	ClassTitleImplDao ctDao;
	@Override
	public void init() throws ServletException {
		classTitle = new ClassTitle();
		ctDao = new ClassTitleImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("读取课程章节！");
		//接受请求参数
		int proj_id = Integer.valueOf(req.getParameter("proj_id"));
		int user_id = Integer.valueOf(req.getParameter("user_id"));
//		int proj_id = 7;
//		int user_id = 1;
		// 输出流
		PrintWriter out = resp.getWriter();
		List<ClassTitle> listTitle = new ArrayList<ClassTitle>();	
		String jsondata = null;
		try {
			//判断是否存在于我课程表里
			String msg = ctDao.queryProjectProgress(proj_id, user_id);
			//classTitle.setMsg(msg);
			
			if(msg.equals("500")){
				//根据id查询课程名
				listTitle = ctDao.queryTitleByProjId(proj_id);
				//将数据转换成json格式
				jsondata = JsonUtil.titleToJson(listTitle);
				
			}else if(msg.equals("200")){
				listTitle = ctDao.queryTitleByProjIdAndUserId(proj_id, user_id);
				jsondata = JsonUtil.titleToJson2(listTitle);
			}
			//向客户端输出数据
			out.print("{"+"\""+"flag"+"\""+":"+msg+","+jsondata+"}");
			//根据id查询课程名
			//listTitle = ctDao.queryTitleByProjId(proj_id);
			//listTitle.add(classTitle);
			//将数据转换成json格式
			//String jsondata = JsonUtil.titleToJson(listTitle);
			//String flag = JsonUtil.flagToJson(classTitle);
			//向客户端输出数据
			//out.print("{"+"\""+"flag"+"\""+":"+msg+","+jsondata+"}");
			//String s = "{"+"\""+"flag"+"\""+":"+msg+","+jsondata+"}";
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
