package com.ma.feedback.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ma.dao.JdbcUtil;
import com.ma.feedback.dao.FeedbackDao;

/**
 * @ClassName: FeedbackImplDao  
 * @Description: 反馈信息接口实现类  
 * @author MZ  
 * @2017年3月20日上午9:30:50
 */
public class FeedbackImplDao implements FeedbackDao{
	Connection conn=null;
    Statement stmt=null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
	@Override
	public boolean saveFeedbackInf(String feedbackInf, String phone) throws SQLException {
		boolean flag = false;
		String saveSql = "INSERT INTO feedback (feedbackinf,phone) VALUES(?,?)";
		
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(saveSql);
			pstmt.setString(1, feedbackInf);
			pstmt.setString(2, phone);
			int s = pstmt.executeUpdate();
			if(s>0){
				flag = true;
			}else{
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		
		return flag;
	}

}
