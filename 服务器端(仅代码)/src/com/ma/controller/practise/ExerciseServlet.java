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

import com.ma.model.Exercise;
import com.ma.practise.dao.impl.ExerciseImplDao;
import com.ma.util.JsonUtil;

/**
 * @ClassName: ExerciseServlet  
 * @Description: 练习模块  
 * @author MZ  
 * @2017年3月6日上午11:03:04
 */
@WebServlet("/readExerName")
public class ExerciseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	Exercise exercise;
	ExerciseImplDao exeDao;
	@Override
	public void init() throws ServletException {
		exercise = new Exercise();
		exeDao = new ExerciseImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		
		List<Exercise> exe = new ArrayList<Exercise>();
		
		try {
			//查询所有课程练习
			exe = exeDao.queryExercise();
			//格式转换
			String jsondata = JsonUtil.ExeToJson(exe);
			//输出
			out.println(jsondata);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	
	}
}
