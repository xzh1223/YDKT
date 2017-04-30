package com.ma.project.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ma.dao.JdbcUtil;
import com.ma.model.ClassTitle;
import com.ma.model.Project;
import com.ma.model.ProjectUserPropress;
import com.ma.project.dao.ProjectDao;

/**
 * @ClassName: ProjectImplDao  
 * @Description:课程接口实现类  
 * @author MZ  
 * @2017年3月6日上午10:22:40
 */
public class ProjectImplDao implements ProjectDao{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	@Override
	public List<Project> queryProject() throws SQLException {
		
		String querySql = "select * from project";
		List<Project> pro = new ArrayList<Project>();
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Project p = new Project();
				p.setId(rs.getInt("id"));
				p.setPro_name(rs.getString("pro_name"));
				p.setPro_image( rs.getString("pro_image"));
				pro.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return pro;
	}
	@Override
	public boolean insertProject(ProjectUserPropress pup) throws SQLException {

		String insertSql =  "insert into project_user_progress (user_id,proj_id,sum_progress) values (?,?,?)";
		boolean flag = false;
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setInt(1, pup.getUser_id());
			pstmt.setInt(2, pup.getProj_id());
			pstmt.setInt(3, 0);
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
	public List<Project> readMyClass(int user_id) throws SQLException {
		String querySql = "SELECT * FROM project "
				+ "WHERE id IN(SELECT proj_id FROM project_user_progress WHERE user_id = ?) ";
		List<Project> pro = new ArrayList<Project>();
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Project p = new Project();
				p.setId(rs.getInt("id"));
				p.setPro_name(rs.getString("pro_name"));
				p.setPro_image( rs.getString("pro_image"));
				pro.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return pro;
	}
	@Override
	public boolean deleteMyClass(ProjectUserPropress pup) throws SQLException {
		//String delSql = "DELETE  FROM project_user_progress WHERE user_id = ? and proj_id = ?";
		String delSql = "DELETE t,p FROM title_users_progress t , project_user_progress p "
				+ "WHERE p.user_id = ? and t.user_id = ? AND p.proj_id = ? and t.project_id = ?";
		boolean flag = false;
		
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(delSql);
			pstmt.setInt(1, pup.getUser_id());
			pstmt.setInt(2, pup.getUser_id());
			pstmt.setInt(3, pup.getProj_id());
			pstmt.setInt(4, pup.getProj_id());
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
	public List<ClassTitle> queryTitleIdByProj_id(int proj_id) throws SQLException {
		List<ClassTitle> listTitle = new ArrayList<ClassTitle>();
		String querySql = "select id from class_title where proj_id = ?";//查询课程表
		
		try{
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, proj_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				ClassTitle ct = new ClassTitle();
				ct.setId(rs.getInt("id"));
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
	public boolean insertIntoProgress(List<ClassTitle> classTitle, ProjectUserPropress pup) 
			throws SQLException {
		String insertSql =  "insert into title_users_progress (curPro,allPro,user_id,title_id,project_id) values (?,?,?,?,?)";
		boolean flag = false;
		int s = 0;
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(insertSql);
			for (int i = 0; i < classTitle.size(); i++) {
				pstmt.setInt(1, 0);
				pstmt.setInt(2, 0);
				pstmt.setInt(3, pup.getUser_id());
				pstmt.setInt(4, classTitle.get(i).getId());
				pstmt.setInt(5, pup.getProj_id());
				s = pstmt.executeUpdate();
			}
			
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
