package com.ma.classes.dao;

import java.sql.SQLException;
import java.util.List;

import com.ma.model.ClassContent;
import com.ma.model.ClassTitle;

/**
 * @ClassName: ClassTitleDao  
 * @Description: 课程名接口  
 * @author MZ  
 * @2017年3月4日下午1:03:45
 */
public interface ClassTitleDao {
	/**
	 * @Title: queryTitleByProjId  
	 * @Description: 根据课程id查询课题  
	 * @param @param proj_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return ClassTitle  
	 * @throws
	 */
	public List<ClassTitle> queryTitleByProjId(int proj_id)throws SQLException;
	
	/**
	 * @Title: queryProjectProgress  
	 * @Description: 判断我的课程里是否添加此课程  
	 * @param @param proj_id
	 * @param @param user_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return String  
	 * @throws
	 */
	public String queryProjectProgress(int proj_id,int user_id)throws SQLException;
	
	/**
	 * @Title: queryContentByTit_id  
	 * @Description: 根据标题id查询课程内容  
	 * @param @param tit_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return ClassContent  
	 * @throws
	 */
	public ClassContent queryContentByTit_id(int tit_id)throws SQLException;
	
	/**
	 * @Title: queryTitleByProjIdAndUserId  
	 * @Description: 多查询两个进度  
	 * @param @param proj_id
	 * @param @param user_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<ClassTitle>  
	 * @throws
	 */
	public List<ClassTitle> queryTitleByProjIdAndUserId(int proj_id,int user_id)throws SQLException;	
}
