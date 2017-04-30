package com.ma.forum.dao;

import java.sql.SQLException;
import java.util.List;

import com.ma.model.Forum;

/**
 * @ClassName: ForumDao  
 * @Description: 论坛发布实体类  
 * @author MZ  
 * @2017年3月6日下午4:33:23
 */
public interface ForumDao {
	
	/**
	 * @Title: queryForum  
	 * @Description: 读取论坛接口  
	 * @param @param forum
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<Forum>  
	 * @throws
	 */
	public List<Forum> queryForum()throws SQLException;
	
	/**
	 * @Title: insertForum  
	 * @Description: 保存论坛发布信息  
	 * @param @param forum
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean insertForum(Forum forum)throws SQLException;
	
	/**
	 * @Title: readForumCount  
	 * @Description: 读取帖子数  
	 * @param @return
	 * @param @throws SQLException  
	 * @return int  返回总条数
	 * @throws
	 */
	public int readForumCount()throws SQLException;
	
	/**
	 * @Title: readMyForumById  
	 * @Description: 读取与我相关的帖子  
	 * @param @param user_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return Forum  
	 * @throws
	 */
	public List<Forum> readMyForumById(int user_id)throws SQLException;
	
	/**
	 * @Title: deleteMyForum  
	 * @Description: 删除我的帖子  
	 * @param @param id
	 * @param @param user_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean deleteMyForum(int id,int user_id)throws SQLException;
}
