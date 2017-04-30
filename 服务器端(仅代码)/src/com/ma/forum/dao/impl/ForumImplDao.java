package com.ma.forum.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ma.dao.JdbcUtil;
import com.ma.forum.dao.ForumDao;
import com.ma.model.Forum;

/**
 * @ClassName: ForumImplDao  
 * @Description: 论坛接口实现类  
 * @author MZ  
 * @2017年3月6日下午4:37:49
 */
public class ForumImplDao implements ForumDao{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	@Override
	public List<Forum> queryForum() throws SQLException {
		String querySql = "SELECT u.username,u.user_img,f.id,f.f_time,f.f_title,f.f_content,f.f_times,f.user_id,f.f_img "
				+ "FROM forum f,users u "
				+ "WHERE u.id=f.user_id "
				+ "ORDER BY f.id DESC";
		List<Forum> list = new ArrayList<Forum>();
		
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Forum f = new Forum();
				f.setId(rs.getInt("id"));
				f.setUsername(rs.getString("username")== null ? "" : rs.getString("username"));
				f.setUser_img(rs.getString("user_img")== null ? "" : rs.getString("user_img"));
				f.setF_time(rs.getString("f_time")== null ? "" : rs.getString("f_time"));
				f.setF_title(rs.getString("f_title")== null ? "" : rs.getString("f_title"));
				f.setF_content(rs.getString("f_content")== null ? "" : rs.getString("f_content"));
				f.setF_times(rs.getInt("f_times"));
				f.setUser_id(rs.getInt("user_id"));
				f.setF_img(rs.getString("f_img")== null ? "" : rs.getString("f_img"));
				list.add(f);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		
		return list;
	}
	
	
	@Override
	public boolean insertForum(Forum forum) throws SQLException {
		String insertSql =  "insert into forum (user_id,f_time,f_title,f_content,f_img,f_times) values (?,?,?,?,?,?)";
		boolean flag = false;
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setInt(1, forum.getUser_id());
			pstmt.setString(2, forum.getF_time());
			pstmt.setString(3, forum.getF_title());
			pstmt.setString(4, forum.getF_content());
			pstmt.setString(5, forum.getF_img());
			pstmt.setInt(6, forum.getF_times());
			int s = pstmt.executeUpdate();
			if(s>0){
				flag = true;
			}else{
				flag = false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return flag;
	}


	@Override
	public int readForumCount() throws SQLException {
		int countOfForum = 0;
		String querySql = "SELECT COUNT(*) FROM forum";
		
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				countOfForum = rs.getInt("count(*)");
				//System.out.println("条数 ＝ "+countOfForum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return countOfForum;
	}


	@Override
	public List<Forum> readMyForumById(int user_id) throws SQLException {
		String querySql = "SELECT f.id,f.f_time,f.f_title,f.f_times,f.f_content,f.f_img,u.username,u.user_img FROM forum f "
				+ "LEFT JOIN users u ON u.id = f.user_id "
				+ "WHERE f.user_id = ? ORDER BY (f.f_time) DESC";
		List<Forum> forumList = new ArrayList<Forum>();
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				Forum forum = new Forum();
				forum.setId(rs.getInt("id"));
				forum.setF_time(rs.getString("f_time")== null ? "" : rs.getString("f_time"));
				forum.setF_title(rs.getString("f_title")== null ? "" : rs.getString("f_title"));
				forum.setF_content(rs.getString("f_content")== null ? "" : rs.getString("f_content"));
				forum.setF_times(rs.getInt("f_times"));
				forum.setUsername(rs.getString("username")== null ? "" : rs.getString("username"));
				forum.setUser_img(rs.getString("user_img")== null ? "" : rs.getString("user_img"));
				forum.setF_img(rs.getString("f_img")== null ? "" : rs.getString("f_img"));
				forumList.add(forum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return forumList;
	}


	@Override
	public boolean deleteMyForum(int id, int user_id) throws SQLException {
		String delSql = "DELETE FROM forum WHERE id = ? AND user_id = ?";
		boolean flag = false;
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(delSql);
			pstmt.setInt(1, id);
			pstmt.setInt(2, user_id);
			int s = pstmt.executeUpdate();
			if(s>0){
				flag = true;
			}else{
				flag = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return flag;
	}

}
