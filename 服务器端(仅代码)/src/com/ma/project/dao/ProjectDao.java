package com.ma.project.dao;

import java.sql.SQLException;
import java.util.List;

import com.ma.model.ClassTitle;
import com.ma.model.Project;
import com.ma.model.ProjectUserPropress;

/**
 * @ClassName: ProjectDao  
 * @Description: 课程接口  
 * @author MZ  
 * @2017年3月6日上午10:20:00
 */
public interface ProjectDao {
	
	/**
	 * @Title: queryProject  
	 * @Description: 查询所有课程  
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<Project>  
	 * @throws
	 */
	public List<Project> queryProject()throws SQLException;
	
	/**
	 * @Title: insertProject  
	 * @Description: 保存课程  
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean insertProject(ProjectUserPropress pup)throws SQLException;
	
	/**
	 * @Title: readMyClass  
	 * @Description: 读取我的课程  
	 * @param @param user_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<Project>  
	 * @throws
	 */
	public List<Project> readMyClass(int user_id)throws SQLException;
	
	/**
	 * @Title: deleteMyClass  
	 * @Description: 删除我的课程  
	 * @param @param pup
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean deleteMyClass(ProjectUserPropress pup)throws SQLException;
	
	
	/**
	 * @Title: queryTitleIdByProj_id  
	 * @Description: 查询章节id  
	 * @param @param proj_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<Integer>  
	 * @throws
	 */
	public List<ClassTitle> queryTitleIdByProj_id(int proj_id)throws SQLException;  
	
	/**
	 * @Title: insertIntoProgress  
	 * @Description: 插入到进度表中  
	 * @param @param classTitle
	 * @param @param pup
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean insertIntoProgress(List<ClassTitle> classTitle,ProjectUserPropress pup)throws SQLException;  
	
}
