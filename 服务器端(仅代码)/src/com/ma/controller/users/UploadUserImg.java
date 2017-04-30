package com.ma.controller.users;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import com.ma.user.dao.impl.UserImplDao;

/**
 * @ClassName: UploadUserImg  
 * @Description: 用户上传头像  
 * @author MZ  
 * @2017年3月21日上午9:38:43
 */
@WebServlet("/uploadUserImg")
public class UploadUserImg extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	UserImplDao userDao;
	@Override
	public void init() throws ServletException {
		userDao = new UserImplDao();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("上传头像");
		String filen = "";		//截取的文件名
		PrintWriter out = resp.getWriter();
		int id = 0;

		try {
			FileItemFactory factory = new DiskFileItemFactory(); // 建立FileItemFactory对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			List<FileItem> items = upload.parseRequest(req);	// 分析请求，并得到上传文件的FileItem对象
			
			String filename = ""; 								// 上传文件保存到服务器的文件名
			InputStream is = null; 								// 当前上传文件的InputStream对象
												
			for (FileItem item : items) {						// 循环处理上传文件
				// 处理普通的表单域
				if (item.isFormField()) {
					String name = item.getFieldName(); 
					String value = item.getString();
					//System.out.println("name=="+name+"value=="+value);
					if(item.getFieldName().equals("id"))
					{
						if(!item.getString().equals(""))
							id = Integer.valueOf(value);
					}
					if (item.getFieldName().equals("image")) {
						// 如果新文件不为空，将其保存在filename中
						if (!item.getString().equals(""))
							filename = item.getString("UTF-8");
					}
				}
				// 处理上传文件
				else if (item.getName() != null && !item.getName().equals("")) {
					// 从客户端发送过来的上传文件路径中截取文件名
					filen = id+item.getName().substring(item.getName().lastIndexOf("\\") + 1);
					//System.out.println("截取filename=="+filen);
					is = item.getInputStream(); // 得到上传文件的InputStream对象
				}
			}
			// 从web.xml文件中的参数中得到上传文件的路径
			//String uploadPath = req.getRealPath("/") +"\\"+"upload"+"\\" + author +"\\";
			String uploadPath = super.getServletContext().getRealPath("/img/img_user/");
			//System.out.println("当前项目路径＝＝"+uploadPath);
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
			
			//String url = "Class/upload/"+ author + "/" + filen;
			String filePath = uploadPath+"/"+filen;
			//System.out.println("保存的照片路径＝＝＝"+filePath);
			String saveFilePath = "img/img_user/"+filen;
			//System.out.println("要保存的"+saveFilePath);
			
			boolean flag = userDao.updateUserImgById(id, saveFilePath);
			if(flag){
				System.out.println("更新成功");
				out.print(200);
			}else{
				out.print(500);
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
