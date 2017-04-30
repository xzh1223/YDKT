package com.ma.controller.forum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ma.forum.dao.impl.ForumImplDao;
import com.ma.model.Forum;
import com.ma.util.DateUtils;

/**
 * @ClassName: SaveForumServlet  
 * @Description: 保存论坛发布信息  有图片
 * @author MZ  
 * @2017年3月6日下午6:51:12
 */
@WebServlet("/insertForumListpic")
public class SaveForumServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	Forum forum;
	ForumImplDao forDao;
	@Override
	public void init() throws ServletException {
		forum = new Forum();
		forDao = new ForumImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("保存帖子！");
		int flag;	//返回标志
		
		// 输出流
		PrintWriter out = resp.getWriter();
		//接收请求参数
		int user_id = 0;							
		String f_time = DateUtils.getDateTime();	
		String f_title = req.getParameter("f_title");
		String f_content = "";
		String filename = "";
		int f_times = 0;
		String filen = "";//截取的文件名
		try {
			FileItemFactory factory = new DiskFileItemFactory(); 		// 建立FileItemFactory对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			List<FileItem> items = upload.parseRequest(req);			// 分析请求，并得到上传文件的FileItem对象
			
			//String filename = ""; 									// 上传文件保存到服务器的文件名
			InputStream is = null; 										// 当前上传文件的InputStream对象
												
			for (FileItem item : items) {								// 循环处理上传文件
				// 处理普通的表单域
				if (item.isFormField()) {
					String filedName = item.getFieldName(); 
					//String value = item.getString();
					String filedValue = item.getString("UTF-8");
					System.out.println("name=="+filedName+"  value=="+filedValue);
					if (item.getFieldName().equals("user_id")) {
						user_id = Integer.valueOf(filedValue);
						}
					if (item.getFieldName().equals("f_title")) {
						f_title = filedValue;
						}
					if (item.getFieldName().equals("f_content")) {
						f_content = filedValue;
						}
				
				}
				// 处理上传文件
				else if (item.getName() != null && !item.getName().equals("")) {
					// 从客户端发送过来的上传文件路径中截取文件名
					filen = user_id+DateUtils.getStringToday()+item.getName().substring(item.getName().lastIndexOf("\\") + 1);
					System.out.println("截取filename=="+filen);
					
					is = item.getInputStream(); // 得到上传文件的InputStream对象
				}
			}
			String uploadPath = super.getServletContext().getRealPath("/img/img_forum/");
			System.out.println("当前项目路径＝＝"+uploadPath);
			
			File file = new File(uploadPath);
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			// 将路径和上传文件名组合成完整的服务端路径
			filename = uploadPath + filen;
			//System.out.println("filename==="+filename+"   uploadPath==="+uploadPath);
			// 如果服务器已经存在和上传文件同名的文件，则输出提示信息
			if (new File(filename).exists()) {
				new File(filename).delete();
			}
			// 开始上传文件
			if (!filename.equals("")) {
				// 用FileOutputStream打开服务端的上传文件
				FileOutputStream fos = new FileOutputStream(filename);
				
				byte[] buffer = new byte[8192]; // 每次读8K字节
				
				int count = 0;
				
				// 开始读取上传文件的字节，并将其输出到服务端的上传文件输出流中
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count); // 向服务端文件写入字节流
				}
				
				System.out.println("上传成功！");
				fos.close(); // 关闭FileOutputStream对象
				is.close(); // InputStream对象
				
			}
			
			String filePath = uploadPath+"/"+filen;
			System.out.println("保存的照片路径＝＝＝"+filePath);
			
			String saveFilePath = "img/img_forum/"+filen;
			System.out.println("要保存的"+saveFilePath);
//-------------------------------------------------------------------------------
		
		forum.setUser_id(user_id);
		forum.setF_time(f_time);
		forum.setF_title(f_title);
		forum.setF_content(f_content);
		forum.setF_img(saveFilePath);
		forum.setF_times(f_times);
		System.out.println("所有信息＝＝＝"+forum);
		boolean tf;
		try {
			//插入数据
			tf = forDao.insertForum(forum);
			//根据返回结果判断是否插入成功
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
	} catch (Exception e) {
		e.printStackTrace();
	}
}
}
