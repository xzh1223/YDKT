package com.ma.controller.feedback;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ma.feedback.dao.impl.FeedbackImplDao;
import com.ma.model.feedback.Feedback;

/**
 * @ClassName: FeedbackServlet  
 * @Description: 用户反馈信息控制器  
 * @author MZ  
 * @2017年3月20日上午9:23:16
 */
@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	Feedback feed;
	FeedbackImplDao feedDao;
	@Override
	public void init() throws ServletException {
		feed = new Feedback();
		feedDao = new FeedbackImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//输出
		PrintWriter out = resp.getWriter();
		//接收参数
		String feedbackInf = req.getParameter("feedbackinf");
		String phone = req.getParameter("phone");
		System.out.println("反馈信息＝＝"+feedbackInf);
		int flag;
		
		try {
			//保存信息
			boolean tf= feedDao.saveFeedbackInf(feedbackInf, phone);
			if(tf){
				//保存成功
				flag = 200;
			}else{
				flag = 500;
			}
			out.print(flag);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
		
	}
}
