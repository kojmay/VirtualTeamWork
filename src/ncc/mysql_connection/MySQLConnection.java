/**  
 *  System： NCC
 *  Title： MySQLConnection.java
 *  Description： 对功能点的描述
 *  @author：  梅海峰
 *  @date： 2018年6月6日 下午4:43:08 
 *  Copyright (c) 2018 CMCC.   
 *   
 */

package ncc.mysql_connection;

import java.sql.*;


public class MySQLConnection {
    //JDBC driver name and database url
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static String DB_URL = "jdbc:mysql://localhost/";

    //Database credentials
    public static String username = "";
    public static String password = "";
    public static String db_name = "";
    public static Connection conn = null;
    public static Statement stmt = null;

    // 单例方法
    public MySQLConnection(String dbname, String uname, String pwd) {
        db_name = dbname;
        DB_URL = String.format("%s%s?serverTimezone=UTC", DB_URL, db_name);
        username = uname;
        password = pwd;
        getStmt();
    }
   /*
    * update 为数据库表的增、删、改
    */
    public void update(String sql) {
        try {
            stmt.executeUpdate(sql);
            System.out.println(sql + " execute successfully!");
        } catch (SQLException se) {
            System.out.println(sql + " 执行错误！");
            se.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet query(String sql) {
        ResultSet resultSet = null;
        try {
             resultSet = stmt.executeQuery(sql);
             System.out.println(sql + " query successfully!");
        }  catch (SQLException se) {
            System.out.println(sql + " 查询执行错误！");
            se.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // 获取数据库连接
    public static Statement getStmt() {
        if(stmt == null) {
            try {
                Class.forName(JDBC_DRIVER);
                System.out.println("Connecting to database。。。");
                conn = DriverManager.getConnection(DB_URL, username, password);
                stmt = conn.createStatement();
                System.out.println("Connect database successfully!");
            }catch(SQLException se) {
                System.out.println(db_name + " 数据库连接错误");
                se.printStackTrace();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return stmt;
    }

    /** 关闭数据库连接
     *  conn: 需要关闭的连接
     *  stmt：  需要关闭的表连接
     */
    public void closeConnection() {
        try {
            if(stmt != null) {
                stmt.close();
            }
        }catch(SQLException se2) {
            System.out.println(db_name + " 关闭stmt错误");
            se2.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        }catch(SQLException se) {
            System.out.println(db_name + " 关闭conn错误");
            se.printStackTrace();
        }
    }
}
