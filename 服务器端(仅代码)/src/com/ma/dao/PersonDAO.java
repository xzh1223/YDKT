package com.ma.dao;

import java.sql.SQLException;
import java.util.List;

import com.ma.model.Person;
/**
 * 测试使用
 * @author Administrator
 *
 */
public interface PersonDAO {
	 //添加方法
    public void add(Person p)throws SQLException;
    
    //更新方法
    public void update(Person p)throws SQLException;
    
    //删除方法
    public int delete(int id)throws SQLException;
    
    //查找方法
    public Person findById(int id)throws SQLException;
    
    //查找所有
    public List<Person> findAll()throws SQLException;
    
    public boolean insert(Person p)throws SQLException;
    
}
