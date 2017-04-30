package com.ma.controller.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ma.model.UserInfo;
import com.ma.user.dao.impl.UserImplDao;
import com.ma.util.JsonUtil;
/**
 * @ClassName: ReadUserInfServlet  
 * @Description: 读取用户信息  
 * @author MZ  
 * @2017年3月3日上午10:40:24
 */
@WebServlet("/readUserInfo")
public class ReadUserInfServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	UserInfo userInfo;
	UserImplDao userDao;
	@Override
	public void init() throws ServletException {
		userInfo = new UserInfo();
		userDao = new UserImplDao();
		
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("读取用户信息！");
		//获取客户端参数
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		//int user_id = 2;
		// 输出流
		PrintWriter out = resp.getWriter();

		try {
			//根据用户id查询用户信息
			userInfo = userDao.queryUserInfById(user_id);
			
			//如果没有查到信息，返回500
			if(userInfo.getMsg().equals("0")){
				out.print("500");
			}else{
				//将数据转换为json格式
				String jsonData = JsonUtil.infoToJson(userInfo);
				//向客户端输出json数据
				out.print(jsonData);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
		
	}
}
