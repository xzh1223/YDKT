package com.ma.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ma.dao.JdbcUtil;

public class TestDDL {
	public static void main(String[] args) {
         Connection conn=null;
         Statement stmt=null;
         conn=JdbcUtil.getConnection();//连接数据库
         String createTableSql= " create table user_test1( "+//记住引号和单词间一定要有空格
                                " id int, "+
                                " name varchar(32) , "+
                                " password varchar(32) , "+
                                " birthday date "+
                                " ) ";  
         try {
             stmt=conn.createStatement();
             stmt.execute(createTableSql);
         } catch (SQLException e) {
            e.printStackTrace();
         }
         JdbcUtil.close(null, stmt, conn);//关闭数据库
     }
}
