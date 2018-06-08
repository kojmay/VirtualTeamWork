/**  
 *  System�� NCC
 *  Title�� MySQLConnection.java
 *  Description�� �Թ��ܵ������
 *  @author��  ÷����
 *  @date�� 2018��6��6�� ����4:43:08 
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

    // ��������
    public MySQLConnection(String dbname, String uname, String pwd) {
        db_name = dbname;
        DB_URL = String.format("%s%s?serverTimezone=UTC", DB_URL, db_name);
        username = uname;
        password = pwd;
        getStmt();
    }
   /*
    * update Ϊ���ݿ�������ɾ����
    */
    public void update(String sql) {
        try {
            stmt.executeUpdate(sql);
            System.out.println(sql + " execute successfully!");
        } catch (SQLException se) {
            System.out.println(sql + " ִ�д���");
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
            System.out.println(sql + " ��ѯִ�д���");
            se.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // ��ȡ���ݿ�����
    public static Statement getStmt() {
        if(stmt == null) {
            try {
                Class.forName(JDBC_DRIVER);
                System.out.println("Connecting to database������");
                conn = DriverManager.getConnection(DB_URL, username, password);
                stmt = conn.createStatement();
                System.out.println("Connect database successfully!");
            }catch(SQLException se) {
                System.out.println(db_name + " ���ݿ����Ӵ���");
                se.printStackTrace();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return stmt;
    }

    /** �ر����ݿ�����
     *  conn: ��Ҫ�رյ�����
     *  stmt��  ��Ҫ�رյı�����
     */
    public void closeConnection() {
        try {
            if(stmt != null) {
                stmt.close();
            }
        }catch(SQLException se2) {
            System.out.println(db_name + " �ر�stmt����");
            se2.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        }catch(SQLException se) {
            System.out.println(db_name + " �ر�conn����");
            se.printStackTrace();
        }
    }
}
