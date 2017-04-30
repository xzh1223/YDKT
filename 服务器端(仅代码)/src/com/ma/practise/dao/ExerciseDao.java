package com.ma.practise.dao;

import java.sql.SQLException;
import java.util.List;

import com.ma.model.Exercise;
import com.ma.model.ExerciseContent;

/**
 * @ClassName: ExerciseDao  
 * @Description: 练习接口  
 * @author MZ  
 * @2017年3月6日上午10:48:23
 */
public interface ExerciseDao {
	
	/**
	 * @Title: queryExercise  
	 * @Description: 查询所有练习
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<Exercise>  
	 * @throws
	 */
	public List<Exercise> queryExercise()throws SQLException;
	
	/**
	 * @Title: queryExerciseContentById  
	 * @Description: 查询课程内容  
	 * @param @param exer_id
	 * @param @return
	 * @param @throws SQLException  
	 * @return List<ExerciseContend>  
	 * @throws
	 */
	public List<ExerciseContent> queryExerciseContentById(int exer_id)throws SQLException;
}
