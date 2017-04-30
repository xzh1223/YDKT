package com.ma.classes.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ma.classes.dao.ClassTitleDao;
import com.ma.dao.JdbcUtil;
import com.ma.model.ClassContent;
import com.ma.model.ClassTitle;

/**
 * @ClassName: ClassTitleImplDao  
 * @Description:课程接口实现类  
 * @author MZ  
 * @2017年3月4日下午1:09:23
 */
public class ClassTitleImplDao implements ClassTitleDao{
	Connection conn=null;
    Statement stmt=null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    
	@Override
	public List<ClassTitle> queryTitleByProjId(int proj_id) throws SQLException {
		
		List<ClassTitle> listTitle = new ArrayList<ClassTitle>();
		String querySql = "select * from class_title where proj_id = ?";//查询课程表
		
		try{
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, proj_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				ClassTitle ct = new ClassTitle();
				ct.setId(rs.getInt("id"));
				ct.setC_name(rs.getString("c_name"));
				ct.setC_img(rs.getString("c_img"));
				listTitle.add(ct);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return listTitle;
	}
	
	
	
	@Override
	public ClassContent queryContentByTit_id(int tit_id) throws SQLException {
		ClassContent cc = new ClassContent();
		String querySql = "select * from class_content where tit_id = ?";
		try{
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, tit_id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				cc.setId(rs.getInt("id"));
				cc.setC_content(rs.getString("c_content"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return cc;
	}



	@Override
	public String queryProjectProgress(int proj_id, int user_id) throws SQLException {
		String msg = "";
		String querySql = "SELECT * FROM project_user_progress "		//查询是否存在于我的课程里
				+ "WHERE proj_id = ?  AND user_id = ?";
		try{
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, proj_id);
			pstmt.setInt(2, user_id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				msg = "200";
			}else{
				msg = "500";
			}
			
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
			}
		return msg;
	}



	@Override
	public List<ClassTitle> queryTitleByProjIdAndUserId(int proj_id, int user_id) throws SQLException {
		List<ClassTitle> listTitle = new ArrayList<ClassTitle>();
		String querySql = "SELECT c.id,c.c_name,c.c_img,t.curPro,t.allPro "
				+ "FROM class_title c "
				+ "LEFT JOIN title_users_progress t ON t.title_id = c.id "
				+ "WHERE t.user_id = ? AND t.project_id = ?";//查询课程表
		
		try{
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, proj_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				ClassTitle ct = new ClassTitle();
				ct.setId(rs.getInt("id"));
				ct.setC_name(rs.getString("c_name"));
				ct.setC_img(rs.getString("c_img"));
				ct.setCurPro(rs.getInt("curPro"));
				ct.setAllPro(rs.getInt("allPro"));
				listTitle.add(ct);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return listTitle;
	}

}
