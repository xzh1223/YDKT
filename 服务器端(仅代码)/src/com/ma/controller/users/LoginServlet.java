package com.ma.controller.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ma.model.User;
import com.ma.user.dao.impl.UserImplDao;
import com.ma.util.Jiami;
import com.ma.util.JsonUtil;

/**
 * @ClassName: LoginServlet
 * @Description: 用户登录
 * @author MZ
 * @2017年3月2日下午6:28:07
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	User user;
	UserImplDao userDao;

	@Override
	public void init() throws ServletException {
		user = new User();
		userDao = new UserImplDao();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 获取客户端请求的用户名和密码
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println(username+"登录！");
		/*
		 * String username = "admin"; String password = "admin";
		 */

		// 输出流
		PrintWriter out = resp.getWriter();
		try {
			password = Jiami.encryptBASE64(password.getBytes());
			System.out.println("加密后的登录密码＝"+password);
			// 根据用户名和密码查询用户
			user = userDao.queryUserByUserName(username, password);
			// 如果结果为空，向客户端输出错误信息 500
			if (user.getMsg().equals("0")) {
				out.print("500");
			} else {
				// 将数据转换为json格式
				String jsonData = JsonUtil.dateToJson(user);
				// 向客户端输出json数据
				out.print(jsonData);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
