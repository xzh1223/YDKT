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
 * @ClassName: RecoverPasswordServlet  
 * @Description: 找回密码  
 * @author MZ  
 * @2017年3月21日下午4:45:15
 */
@WebServlet("/recoverPassword")
public class RecoverPasswordServlet extends HttpServlet{
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
		PrintWriter out = resp.getWriter();
		String username = req.getParameter("username");
		String phone = req.getParameter("phone");
		String password = "";
		try {
			//找回密码
			user = userDao.recoverPasswordByUsernameAndPhone(username, phone);
			if(user.getMsg().equals("1")){
				password = user.getPassword();
				//解密
				byte result2[] = Jiami.decryptBASE64(password);
				password = new String(result2);
			}else{
				password = "500";
			}
			out.print(password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
}
