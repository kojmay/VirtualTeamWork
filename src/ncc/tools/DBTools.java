package ncc.tools;

/**  
 *  System�� NCC
 *  Title�� DB_Tools.java
 *  Description�� �Թ��ܵ������
 *  @author��  ÷����<br/>
 *  @date�� 2018��6��8�� ����3:16:12 
 *  Copyright (c) 2018 CMCC.   
 *   
 */

import java.sql.*;

import org.apache.poi.wp.usermodel.CharacterRun;

import com.mysql.cj.SimpleQuery;

public class DBTools {
    
    public MySQLConnection nccDB; // log ���ݿ�����
//    public MySQLConnection stdDB; // standard ���ݿ�����
    
    //����ģʽ
    private DBTools() { 
        System.out.println("init dbtools");
        nccDB = new MySQLConnection("ncc_db", "root", "");
    }
    private static final DBTools dbtools = new DBTools();  
    //��̬��������   
    public static DBTools getInstance() {  
        return dbtools;  
    }
    
    public void close() {
        nccDB.closeConnection();
    }
    
    /*
     * ʹ��ʾ��
     */
    public static void main(String[] args) {
        //1����ȡ���ݿ����ʵ��
        DBTools dbTool = DBTools.getInstance();//dbtool �У� logDBΪ��־���ݿ�����ӣ� stdDB Ϊ��׼�����ݿ������
        
        //2���½�һ�ű�
        String sql = "CREATE TABLE student " +
                "(id INTEGER not NULL, " +
                " first VARCHAR(255), " + 
                " last VARCHAR(255), " + 
                " age INTEGER, " + 
                " PRIMARY KEY ( id ))"; 
        dbTool.nccDB.update(sql);
        
        //3����������
      sql = "INSERT INTO student " +
      "VALUES (100, 'C++', 'Li', 18)";
      dbTool.nccDB.update(sql);
      sql = "INSERT INTO student " +
      "VALUES (101, 'Python', 'Py', 25)";
      dbTool.nccDB.update(sql);
      
      //4��ɾ������
      sql = "delete from student where id=100";
      dbTool.nccDB.update(sql);
      
      //5����������
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
      
//      //6��ɾ����
////      sql = "drop table student";
////      dbTool.stdDB.update(sql);
//      
      //7������֮����ر�
      dbTool.close();
      
     //todo ����ʡ�ж�Ӧ��
        
    }
    
}

