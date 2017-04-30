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
 * @ClassName: UpdatePassword  
 * @Description: 修改密码  
 * @author MZ  
 * @2017年3月16日下午3:47:52
 */
@WebServlet("/updatePassword")
public class UpdatePassword extends HttpServlet{

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
		System.out.println("更新密码！");
		// 输出流
		PrintWriter out = resp.getWriter();
		String newpassword = req.getParameter("new_password");
		int user_id = Integer.valueOf(req.getParameter("user_id"));
		int flag;
		try {
			newpassword = Jiami.encryptBASE64(newpassword.getBytes());
			//System.out.println("加密后的密码："+newpassword);
			boolean tf = userDao.updateUserPass(newpassword, user_id);
			if (tf) {
				flag = 200;
			} else {
				flag = 500;
			}
			out.print(flag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
