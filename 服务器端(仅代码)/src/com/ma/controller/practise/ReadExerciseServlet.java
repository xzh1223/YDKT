package com.ma.controller.practise;

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

import com.ma.model.ExerciseContent;
import com.ma.practise.dao.impl.ExerciseImplDao;
import com.ma.util.JsonUtil;

/**
 * @ClassName: ReadExerciseServlet  
 * @Description: 根据课程id查询对应的练习题  
 * @author MZ  
 * @2017年3月20日下午2:22:15
 */
@WebServlet("/readExercise")
public class ReadExerciseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	ExerciseContent exerContent;
	ExerciseImplDao exerDao;
	@Override
	public void init() throws ServletException {
		exerContent = new ExerciseContent();
		exerDao = new ExerciseImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("读取习题内容！");
		PrintWriter out = resp.getWriter();
		//接收请求参数
		int exer_id = Integer.valueOf(req.getParameter("exer_id"));
		
		List<ExerciseContent> cont = new ArrayList<ExerciseContent>();
		
		try {
			//根据id查询习题内容
			cont = exerDao.queryExerciseContentById(exer_id);
			//转换数据格式
			String jsondata = JsonUtil.contentToJson(cont);
			System.out.println("习题"+jsondata);
			//输出
			out.print(jsondata);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
