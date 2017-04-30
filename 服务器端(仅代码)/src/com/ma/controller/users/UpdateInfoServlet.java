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

/**
 * @ClassName: UpdateInfoServlet  
 * @Description: 更新或添加用户信息  
 * @author MZ  
 * @2017年3月3日下午12:19:26
 */
@WebServlet("/insertUserInfo")
public class UpdateInfoServlet extends HttpServlet{
		
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
		System.out.println("更新用户信息！");
		// 输出流
		PrintWriter out = resp.getWriter();
		int msg = 500;
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		//String i_age = req.getParameter("i_age");
		String i_sex = req.getParameter("i_sex");
		int i_age =Integer.parseInt( req.getParameter("i_age")==""? "0" : req.getParameter("i_age"));
		String i_profession = req.getParameter("i_profession");
		String i_phone = req.getParameter("i_phone");
		String i_email = req.getParameter("i_email");
		String i_constellation = req.getParameter("i_constellatioin");
		String i_home = req.getParameter("i_home");
		String i_description = req.getParameter("i_description");
		userInfo.setSex(i_sex);
		userInfo.setAge(i_age);
		userInfo.setProfession(i_profession);
		userInfo.setPhone(i_phone);
		userInfo.setEmail(i_email);
		userInfo.setConstellation(i_constellation);
		userInfo.setHome(i_home);
		userInfo.setDescription(i_description);
		/*int user_id = 2;
		String i_sex = "男";
		int i_age = 23;
		String i_profession = "1";
		String i_phone = "1";
		String i_email = "1";
		String i_constellation = "1";
		String i_home = "1";
		String i_description ="1"; 
		userInfo.setSex(i_sex);
		userInfo.setAge(i_age);
		userInfo.setProfession(i_profession);
		userInfo.setPhone(i_phone);
		userInfo.setEmail(i_email);
		userInfo.setConstellation(i_constellation);
		userInfo.setHome(i_home);
		userInfo.setDescription(i_description);*/
		
		try {
			//根据用户id更新或插入用户信息
			boolean flag = userDao.updateOrAddUserInfo(userInfo, user_id);
			if (flag) {
				//成功返回200
				msg = 200;
			} else {
				//失败返回500
				msg = 500;
			}
			/*out.print(msg);
			out.flush();
			out.close();*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.print(msg);
		out.flush();
		out.close();
	}
}
