/**  
 *  System： NCC
 *  Title： first.java
 *  Description： 对功能点的描述
 *  @author：  梅海峰<br/>
 *  @date： 2018年6月6日 下午4:42:23 
 *  Copyright (c) 2018 CMCC.   
 *   
 */
package ncc.ps;


import java.sql.ResultSet;
import java.sql.SQLException;

import ncc.mysql_connection.DBTools;
import ncc.mysql_connection.MySQLConnection;


public class first {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DBTools dbTools = DBTools.getInstance();
        
        String sql = "CREATE TABLE student " +
                    "(id INTEGER not NULL, " +
                    " first VARCHAR(255), " + 
                    " last VARCHAR(255), " + 
                    " age INTEGER, " + 
                    " PRIMARY KEY ( id ))"; 
        
//        sql = "INSERT INTO student " +
//                "VALUES (100, 'C++', 'Li', 18)";
//        log_db_connection.update(sql);
//        sql = "INSERT INTO student " +
//                "VALUES (101, 'Python', 'Py', 25)";
//        log_db_connection.update(sql);
//        sql = "INSERT INTO student " +
//                "VALUES (102, 'Ruby', 'Ru', 30)";
//        log_db_connection.update(sql);
//        sql = "INSERT INTO student " +
//                "VALUES(103, 'Java', 'Ja', 28)";
//        log_db_connection.update(sql);
        
//        sql = "select * from student";
//        ResultSet rs = dbTools.logDB.query(sql);
//        //STEP 5: Extract data from result set
//        try {
//            while(rs.next()){
//               //Retrieve by column name
//               int id  = rs.getInt("id");
//               int age = rs.getInt("age");
//               String first = rs.getString("first");
//               String last = rs.getString("last");
//
//               //Display values
//               System.out.print("ID: " + id);
//               System.out.print(", Age: " + age);
//               System.out.print(", First: " + first);
//               System.out.println(", Last: " + last);
//               System.out.println(rs);
//            }
//            rs.close();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        sql = "delete from student where id=100";
        dbTools.logDB.update(sql);
        
        
    }

}
