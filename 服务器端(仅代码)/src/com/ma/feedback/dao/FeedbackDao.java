package com.ma.feedback.dao;

import java.sql.SQLException;

/**
 * @ClassName: FeedbackDao  
 * @Description: 用户反馈信息接口  
 * @author MZ  
 * @2017年3月20日上午9:27:49
 */
public interface FeedbackDao {
	
	/**
	 * @Title: saveFeedbackInf  
	 * @Description: 保存反馈信息  
	 * @param @param feedbackInf
	 * @param @param phone
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean saveFeedbackInf(String feedbackInf,String phone)throws SQLException;
}
