package com.ma.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
/**
 * 数据库连接工具类
 * @ClassName: JdbcUtil  
 * @Description: TODO  
 * @author MZ  
 * @2017年3月2日下午5:48:03
 */
public class JdbcUtil {
	private static String DRIVER_CLASS;
	      private static String URL;
          private static String USERRNAME;
	      private static String PASSWORD;
	      private static Properties p=new Properties();
	      static{
          try {
             //FileInputStream fis=new FileInputStream("db.properties");
             InputStream fis =  Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
             p.load(fis);
             DRIVER_CLASS=p.getProperty("driver");
             URL=p.getProperty("url");
             USERRNAME=p.getProperty("user");
             PASSWORD=p.getProperty("pass");
             Class.forName(DRIVER_CLASS);
             fis.close();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
         }
     }
      public static Connection getConnection(){
         Connection conn=null;
         try{
         conn=DriverManager.getConnection(URL, USERRNAME, PASSWORD);
         }
         catch (Exception e) {
                 e.printStackTrace();
             }
          return conn;
        }
     public static void close(ResultSet rs,Statement stmt,Connection conn) {
          
    	  try {
              if (rs != null)
            	  rs.close();
             } catch (Exception e) {
               e.printStackTrace();
             }
     	  try {
              if (stmt != null)
            	  stmt.close();
             } catch (Exception e) {
               e.printStackTrace();
             }
         	
         	 try {
                 if (conn != null)
                      conn.close();
                } catch (Exception e) {
                  e.printStackTrace();
               }
      }
}
             
     

