package com.ma.progress.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ma.dao.JdbcUtil;
import com.ma.model.Progress;
import com.ma.progress.dao.ProgressDao;

/**
 * @ClassName: ProgressImplDao  
 * @Description: 学习进度实现类  
 * @author MZ  
 * @2017年3月17日下午2:01:55
 */
public class ProgressImplDao implements ProgressDao{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	@Override
	public boolean updateProgress(Progress prog) throws SQLException {
		boolean flag = false;
		String updateSql = "UPDATE title_users_progress SET curPro = ?, allPro = ? "
				+ "WHERE title_id = ? AND user_id = ?";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setInt(1, prog.getCurPro());
			pstmt.setInt(2, prog.getAllPro());
			pstmt.setInt(3, prog.getTitle_id());
			pstmt.setInt(4, prog.getUser_id());
			int s = pstmt.executeUpdate();
			if(s>0){
				flag = true;
			}else{
				flag = false;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		
		return flag;
	}


}
