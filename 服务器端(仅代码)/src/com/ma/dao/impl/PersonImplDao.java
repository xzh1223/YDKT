package com.ma.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ma.dao.JdbcUtil;
import com.ma.dao.PersonDAO;
import com.ma.model.Person;
/**
 * 测试使用
 * @author Administrator
 *
 */
public class PersonImplDao implements PersonDAO{
	Connection conn=null;
    Statement stmt=null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;
   
	@Override
	public void add(Person p) throws SQLException {
		
		String insertSql="insert into user_test1(id,name,password)values(?,?,?)";  
		try {
			conn=JdbcUtil.getConnection();
        	pstmt = conn.prepareStatement(insertSql);
        	pstmt.setInt(1, p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getPassword());
            pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
        	JdbcUtil.close(rs, pstmt, conn);//关闭数据库
        }
	}

	@Override
	public void update(Person p) throws SQLException {
	
		String upstmtql = "update user_test1 set name=?,password=? where id = ?";
		try {
			conn = JdbcUtil.getConnection();
			pstmt = conn.prepareStatement(upstmtql);
			pstmt.setString(1, p.getName());
			pstmt.setInt(2, p.getPassword());
			pstmt.setInt(3, p.getId());
			pstmt.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("更新数据失败");
		}finally{
			JdbcUtil.close(rs, pstmt, conn);
		}
	}

	@Override
	public int delete(int id) throws SQLException {
		int result=0;
        String delSql = "delete from user_test1 where id = ?";
        try{
        	conn = JdbcUtil.getConnection();
        	pstmt = conn.prepareStatement(delSql);
        	pstmt.setInt(1, id);
        	result = pstmt.executeUpdate();
        }catch(SQLException e){
        	e.printStackTrace();
        	result = -1;
        	throw new SQLException("删除数据失败");
        }finally{
        	JdbcUtil.close(rs, pstmt, conn);
        }
        return result;
	}

	@Override
	public Person findById(int id) throws SQLException {

        Person p = null;
        String sql = "select name,password from user_test1 where id=?";
        try{
            conn = JdbcUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            p = new Person();
            if(rs.next()){
            	p.setId(id);
                p.setName(rs.getString(1));
                p.setPassword(rs.getInt(2));
            }else{
            	p.setMsg("没有此人");
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("根据ID查询数据失败");
        }finally{
       	 	JdbcUtil.close(rs, pstmt, conn);
        }
         return p;
	}

	@Override
	public List<Person> findAll() throws SQLException {
	
		Person p = null;
		List<Person> persons = new ArrayList<Person>();
		String sql = "select id,name,password from user_test1";
        try{
        	conn = JdbcUtil.getConnection();
        	pstmt = conn.prepareStatement(sql);
        	rs = pstmt.executeQuery();
        	while(rs.next()){
        		p = new Person();
        		p.setId(rs.getInt(1));
                p.setName(rs.getString(2));
                p.setPassword(rs.getInt(3));
                persons.add(p);
        	}
        }catch(SQLException e){
        	e.printStackTrace();
        	throw new SQLException("查询所有数据失败");
        }finally{
        	JdbcUtil.close(rs, pstmt, conn);
        }
        return persons;
	}

	@Override
	public boolean insert(Person p) throws SQLException {
		
		boolean flag = false;
        String insertSql="insert into user_test1(id,name,password)values(?,?,?)";  
        try {
        	conn=JdbcUtil.getConnection();
        	pstmt = conn.prepareStatement(insertSql);
        	pstmt.setInt(1, p.getId());
        	pstmt.setString(2, p.getName());
        	pstmt.setInt(3, p.getPassword());
            int result=pstmt.executeUpdate();
            if(result > 0){
            	flag = true;
            }
        }catch (SQLException e) {
        	e.printStackTrace();
        }finally{
        	JdbcUtil.close(rs, pstmt, conn);//关闭数据库
        }
        return flag;
	}

}
