package com.ma.controller.progress;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ma.model.Progress;
import com.ma.progress.dao.impl.ProgressImplDao;

/**
 * @ClassName: UpdateProgressServlet  
 * @Description: 更新学习进度  
 * @author MZ  
 * @2017年3月17日下午2:04:32
 */
@WebServlet("/updateProg")
public class UpdateProgressServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	Progress prog;
	ProgressImplDao progDao;
	@Override
	public void init() throws ServletException {
		prog = new Progress();
		progDao = new ProgressImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		int user_id = Integer.valueOf(req.getParameter("user_id"));
		int title_id = Integer.valueOf(req.getParameter("title_id"));
		int curPro = Integer.valueOf(req.getParameter("curPro"));
		int allPro = Integer.valueOf(req.getParameter("allPro"));
		int flag;	//返回标志
		prog.setUser_id(user_id);
		prog.setTitle_id(title_id);
		prog.setCurPro(curPro);
		prog.setAllPro(allPro);
		boolean tf;
		try {
			tf = progDao.updateProgress(prog);
			if (tf) {
				flag = 200;
			} else {
				flag = 500;
			}
			out.print(flag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
