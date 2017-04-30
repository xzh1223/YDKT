package com.ma.user.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.ma.dao.JdbcUtil;
import com.ma.model.User;
import com.ma.model.UserInfo;
import com.ma.user.dao.UserDao;

/**
 * @ClassName: UserImplDao
 * @Description: 用户接口实现类
 * @author MZ
 * @2017年3月2日下午6:47:06
 */
public class UserImplDao implements UserDao {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;

	/**
	 * 根据用户名查询用户
	 */
	public User queryUserByUserName(String username, String password) throws SQLException {
		User user = new User();
		String querySql = "select * from users where username=? and password=?";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setImg(rs.getString("user_img"));
				user.setMsg("1");
			} else {
				user.setMsg("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return user;
	}

	@Override
	public UserInfo queryUserInfById(int user_id) throws SQLException {
		UserInfo userInfo = new UserInfo();
		// 查询用户信息sql
		String queryInfoSql = "select * from user_info where user_id =?";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(queryInfoSql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				userInfo.setSex(rs.getString("i_sex") == null ? "" : rs
						.getString("i_sex"));
				userInfo.setAge(rs.getInt("i_age"));
				userInfo.setProfession(rs.getString("i_profession") == null ? "" : rs
						.getString("i_profession"));
				userInfo.setPhone(rs.getString("i_phone") == null ? "" : rs
						.getString("i_sex"));
				userInfo.setEmail(rs.getString("i_email") == null ? "" : rs
						.getString("i_phone"));
				userInfo.setConstellation(rs.getString("i_constellation") == null ? "" : rs
						.getString("i_constellation"));//i_constellation
				userInfo.setHome(rs.getString("i_home") == null ? "" : rs
						.getString("i_home"));
				userInfo.setDescription(rs.getString("i_description") == null ? "" : rs
						.getString("i_description"));
				userInfo.setMsg("1");
			} else {
				// 设置未查到用户的提示信息
				userInfo.setMsg("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}

		return userInfo;
	}

	@Override
	public boolean updateOrAddUserInfo(UserInfo userInfo, int user_id) throws SQLException {
		boolean flag = false; // 判断更新或插入是否成功
		String querysql = "select * from user_info where user_id =?";
		String insertUserInfoSql = "";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querysql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery(); // 根据id查询用户信息

			if (rs.next()) { // 如果信息已经存在，则更新
				// 更新语句
				insertUserInfoSql = "update user_info set i_sex =? ,i_age = ?,i_profession =? ,"
						+ "i_phone =? ,i_email=	?,i_constellation =?," + "i_home =? ,i_description =? "
						+ " where user_id = ?";
			} else { // 若不存在，则插入新的用户信息
				// 插入语句
				insertUserInfoSql = "insert into user_info (i_sex,i_age,i_profession,i_phone,i_email,i_constellation,i_home,i_description,user_id) "
						+ "values(?,?,?,?,?,?,?,?,?)";
			}
			pstmt = conn.prepareStatement(insertUserInfoSql);
			pstmt.setString(1, userInfo.getSex());
			pstmt.setInt(2, userInfo.getAge());
			pstmt.setString(3, userInfo.getProfession());
			pstmt.setString(4, userInfo.getPhone());
			pstmt.setString(5, userInfo.getEmail());
			pstmt.setString(6, userInfo.getConstellation());
			pstmt.setString(7, userInfo.getHome());
			pstmt.setString(8, userInfo.getDescription());
			pstmt.setInt(9, user_id);
			int num = pstmt.executeUpdate();	//根据返回结果
			if (num > 0) {
				System.out.println("插入或更新成功");
				flag = true;
			} else {
				System.out.println("插入失败！");
				flag = false;
			}
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
			throw new SQLException("插入或更新数据失败！");
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}

		return flag;
	}

	@Override
	public boolean updateUserPass(String newpassword, int user_id) throws SQLException {
		String updateSql = "UPDATE users SET PASSWORD = ? WHERE id = ?";
		boolean flag = false;
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setString(1, newpassword);
			pstmt.setInt(2,user_id);
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

	@Override
	public boolean registerUser(String username, String password, String phone) throws SQLException {
		boolean flag = false;
		String insertSql = "INSERT INTO users(username,password,phone,user_img) VALUES(?,?,?,?)";
		String insertSql2 = "INSERT INTO user_info (user_id) SELECT id FROM users WHERE phone = ?";
		String querySql = "SELECT * FROM users WHERE phone = ?";
		String imgurl = "img/img_proj/img.png";
		try {
			conn = JdbcUtil.getConnection();
			//判断用户是否存在 
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, phone);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return flag = false;
			}
			//注册
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, phone);
			pstmt.setString(4, imgurl);
			int s = pstmt.executeUpdate();
			//将数据插入用户信息表
			pstmt = conn.prepareStatement(insertSql2);
			pstmt.setString(1, phone);
			int i = pstmt.executeUpdate();
			if(s>0 && i>0){
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

	@Override
	public boolean updateUserImgById(int id, String img_path) throws SQLException {
		boolean flag = false;
		String updateSql = "UPDATE users SET user_img = ? WHERE id = ?";
		
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(updateSql);
			pstmt.setString(1, img_path);
			pstmt.setInt(2,id);
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

	@Override
	public User recoverPasswordByUsernameAndPhone(String username, String phone) throws SQLException {
		User user = new User();
		String querySql = "SELECT password FROM users WHERE username = ? AND phone = ?";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setString(1, username);
			pstmt.setString(2, phone);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user.setPassword(rs.getString("password"));
				user.setMsg("1");
			} else {
				user.setMsg("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return user;
	}

}
