package com.ma.forum.dao;

import java.sql.SQLException;
import java.util.List;

import com.ma.model.Discuss;


/**
 * @ClassName: DiscussDao  
 * @Description: 论坛接口  
 * @author MZ  
 * @2017年3月5日上午11:36:42
 */
public interface DiscussDao {
	
	/**
	 * @Title: queryDiscussByF_id  
	 * @Description: 论坛查询  
	 * @param @param f_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<Discuss>  
	 * @throws
	 */
	public List<Discuss> queryDiscussByF_id(int f_id)throws SQLException;
	
	/**
	 * @Title: insertDiscussContent  
	 * @Description: 保存论坛信息  
	 * @param @param dis 论坛实体
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean insertDiscussContent(Discuss dis)throws SQLException;
	
}
