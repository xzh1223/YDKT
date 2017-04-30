package com.ma.practise.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.ma.dao.JdbcUtil;
import com.ma.model.Exercise;
import com.ma.model.ExerciseContent;
import com.ma.practise.dao.ExerciseDao;

/**
 * @ClassName: ExerciseImplDao  
 * @Description:练习接口实现类  
 * @author MZ  
 * @2017年3月6日上午10:50:40
 */
public class ExerciseImplDao implements ExerciseDao{
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	@Override
	public List<Exercise> queryExercise() throws SQLException {
		
		String querySql = "select * from exercise";
		List<Exercise> exe = new ArrayList<Exercise>();
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Exercise exercise = new Exercise();
				exercise.setId(rs.getInt("id"));
				exercise.setE_name(rs.getString("e_name"));
				exercise.setE_image(rs.getString("e_image"));
				exe.add(exercise);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return exe;
	}
	@Override
	public List<ExerciseContent> queryExerciseContentById(int exer_id) throws SQLException {
		String querySql = "SELECT * FROM exer_content WHERE exer_id = ? ORDER BY RAND() LIMIT 1";
		
		List<ExerciseContent> content = new ArrayList<ExerciseContent>();
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(querySql);
			pstmt.setInt(1, exer_id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ExerciseContent cont = new ExerciseContent();
				cont.setId(rs.getInt("id"));
				cont.setQuest(rs.getString("quest"));
				cont.setA_choose(rs.getString("a_choose"));
				cont.setB_choose(rs.getString("b_choose"));
				cont.setC_choose(rs.getString("c_choose"));
				cont.setD_choose(rs.getString("d_choose"));
				cont.setAnswer(rs.getString("answer"));
				content.add(cont);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstmt, conn);// 关闭数据库
		}
		return content;
	}

}
