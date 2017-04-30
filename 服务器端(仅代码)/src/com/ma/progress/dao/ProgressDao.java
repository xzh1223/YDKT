package com.ma.progress.dao;

import java.sql.SQLException;

import com.ma.model.Progress;

/**
 * @ClassName: ProgressDao  
 * @Description: 学习进度  
 * @author MZ  
 * @2017年3月17日下午1:59:39
 */
public interface ProgressDao {
	
	/**
	 * @Title: updateProgress  
	 * @Description: 更新学习进度  
	 * @param @param prog
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean updateProgress(Progress prog)throws SQLException;
}
