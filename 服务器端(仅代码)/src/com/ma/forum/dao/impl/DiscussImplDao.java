package com.ma.forum.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ma.dao.JdbcUtil;
import com.ma.forum.dao.DiscussDao;
import com.ma.model.Discuss;

/**
 * @ClassName: DiscussImplDao  
 * @Description: 论坛接口实现类  
 * @author MZ  
 * @2017年3月5日上午11:44:00
 */
public class DiscussImplDao implements DiscussDao{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	@Override
	public List<Discuss> queryDiscussByF_id(int f_id) throws SQLException {
		
		String querySql = "select u.username,u.user_img,d.d_content,d.d_time,d.id "
				+ "from discuss d,Users u "
				+ "where u.id = d.user_id "
				+ "and d.f_id = ? "
				+ "ORDER BY (id) DESC";
		List<Discuss> list = new ArrayList<Discuss>();
		try{
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, f_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Discuss dis = new Discuss();
				dis.setId(rs.getInt("id"));
				dis.setUsername(rs.getString("username"));
				dis.setUser_img(rs.getString("user_img"));
				dis.setD_time(rs.getString("d_time"));
				dis.setD_content(rs.getString("d_content"));
				
				list.add(dis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		
		return list;
	}
	@Override
	public boolean insertDiscussContent(Discuss dis) throws SQLException {
		boolean flag = false;
		String saveSql = "insert into discuss (d_content,d_time,f_id,user_id) values (?,?,?,?)";
		
		String updateSql = "UPDATE forum SET f_times = (f_times+1) WHERE id = ?";
			try{	
				conn = JdbcUtil.getConnection();
				pstmt = conn.prepareStatement(saveSql);
				pstmt.setString(1, dis.getD_content());
				pstmt.setString(2, dis.getD_time());
				pstmt.setInt(3, dis.getF_id());
				pstmt.setInt(4, dis.getUser_id());
				int i = pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setInt(1,dis.getF_id());
				//pstmt.setInt(2, dis.getUser_id());
				int s = pstmt.executeUpdate();
				
				if(i>0 && s>0){
					flag = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
			}
		return flag;
	}

}
