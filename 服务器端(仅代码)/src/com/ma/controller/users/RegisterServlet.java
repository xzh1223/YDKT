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

/**
 * @ClassName: RegisterServlet  
 * @Description: 用户注册  
 * @author MZ  
 * @2017年3月19日下午12:13:21
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	User user;
	UserImplDao userDao;

	@Override
	public void init() throws ServletException {
		user = new User();
		userDao = new UserImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//接收请求参数
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String phone = req.getParameter("phone");
		System.out.println("用户"+username+"注册！"+password+phone);
		PrintWriter out = resp.getWriter();
		boolean tf;
		int flag;
		try {
			password = Jiami.encryptBASE64(password.getBytes());
			tf = userDao.registerUser(username, password, phone);
			if (tf) {
				flag = 200;
			} else {
				flag = 500;
			}
			out.print(flag);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
