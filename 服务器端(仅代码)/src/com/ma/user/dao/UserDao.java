package com.ma.user.dao;

import java.sql.SQLException;

import com.ma.model.User;
import com.ma.model.UserInfo;

/**
 * 用户接口
 * @ClassName: UserDao  
 * @Description: TODO  
 * @author MZ  
 * @2017年3月2日下午6:42:21
 */
public interface UserDao {
	
	/**
	 * @Title: queryUserByUserNamethrows  
	 * @Description: 根据用户名和密码查询用户  
	 * @param @param user
	 * @param @return
	 * @param @throws SQLException  
	 * @return User  
	 * @throws
	 */
	public User queryUserByUserName(String username,String password)throws SQLException;
	
	/**
	 * @Title: queryUserInfById  
	 * @Description: 根据用户Id查询用户信息  
	 * @param @param user_id 
	 * @param @return
	 * @param @throws SQLException  
	 * @return UserInfo  
	 * @throws
	 */
	public UserInfo queryUserInfById(int user_id) throws SQLException;
	
	/**
	 * @Title: updateOrAddUserInfo  
	 * @Description: 更新或添加用户信息  
	 * @param @param userInfo
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean updateOrAddUserInfo(UserInfo userInfo,int user_id)throws SQLException;
	
	/**
	 * @Title: updateUserPass  
	 * @Description: 修改密码  
	 * @param @param newpassword
	 * @param @param user_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean updateUserPass(String newpassword,int user_id)throws SQLException;
	
	/**
	 * @Title: registerUser  
	 * @Description: 用户注册  
	 * @param @param username
	 * @param @param password
	 * @param @param phone
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean registerUser(String username,String password,String phone)throws SQLException;
	
	/**
	 * @Title: updateUserImgById  
	 * @Description: 上传用户头像  
	 * @param @param id
	 * @param @param img_path
	 * @param @return
	 * @param @throws SQLException  
	 * @return boolean  
	 * @throws
	 */
	public boolean updateUserImgById(int id,String img_path)throws SQLException;
	
	/**
	 * @Title: recoverPasswordByUsernameAndPhone  
	 * @Description: TODO  
	 * @param @param 密码找回
	 * @param @param phone
	 * @param @return
	 * @param @throws SQLException  
	 * @return User  
	 * @throws
	 */
	public User recoverPasswordByUsernameAndPhone(String username, String phone)throws SQLException;
}
