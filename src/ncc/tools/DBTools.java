package ncc.tools;

/**  
 *  System： NCC
 *  Title： DB_Tools.java
 *  Description： 对功能点的描述
 *  @author：  梅海峰<br/>
 *  @date： 2018年6月8日 下午3:16:12 
 *  Copyright (c) 2018 CMCC.   
 *   
 */

import java.sql.*;

import org.apache.poi.wp.usermodel.CharacterRun;

import com.mysql.cj.SimpleQuery;

public class DBTools {
    
    public MySQLConnection nccDB; // log 数据库连接
//    public MySQLConnection stdDB; // standard 数据库连接
    
    //单例模式
    private DBTools() { 
        System.out.println("init dbtools");
        nccDB = new MySQLConnection("ncc_db", "root", "");
    }
    private static final DBTools dbtools = new DBTools();  
    //静态工厂方法   
    public static DBTools getInstance() {  
        return dbtools;  
    }
    
    public void close() {
        nccDB.closeConnection();
    }
    
    /*
     * 使用示例
     */
    public static void main(String[] args) {
        //1、获取数据库操作实例
        DBTools dbTool = DBTools.getInstance();//dbtool 中， logDB为日志数据库的连接； stdDB 为标准版数据库的连接
        
        //2、新建一张表
        String sql = "CREATE TABLE student " +
                "(id INTEGER not NULL, " +
                " first VARCHAR(255), " + 
                " last VARCHAR(255), " + 
                " age INTEGER, " + 
                " PRIMARY KEY ( id ))"; 
        dbTool.nccDB.update(sql);
        
        //3、插入数据
      sql = "INSERT INTO student " +
      "VALUES (100, 'C++', 'Li', 18)";
      dbTool.nccDB.update(sql);
      sql = "INSERT INTO student " +
      "VALUES (101, 'Python', 'Py', 25)";
      dbTool.nccDB.update(sql);
      
      //4、删除数据
      sql = "delete from student where id=100";
      dbTool.nccDB.update(sql);
      
      //5、查找数据
      sql = "select * from student";
      ResultSet rs = dbTool.nccDB.query(sql);
       //Extract data from result set
      try {
          while(rs.next()){
             //Retrieve by column name
             int id  = rs.getInt("id");
             int age = rs.getInt("age");
             String first = rs.getString("first");
             String last = rs.getString("last");

             //Display values
             System.out.print("ID: " + id);
             System.out.print(", Age: " + age);
             System.out.print(", First: " + first);
             System.out.println(", Last: " + last);
             System.out.println(rs);
          }
          rs.close();
      } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      
//      //6、删除表
////      sql = "drop table student";
////      dbTool.stdDB.update(sql);
//      
      //7、用完之后需关闭
      dbTool.close();
      
     //todo 插入省市对应表
        
    }
    
}

