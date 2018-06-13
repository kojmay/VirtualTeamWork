package ncc.tools;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonToolsLib {
    
    public static void createCheckInfoTable() {
        
        /* 1����stdDB��׼�����ݿ��н���
         *      ������CheckInfo����¼�˲���Ϣ
         *      �������ԣ�checkId����������������checkDescribe���˲�ȼ���������checkDate��������ڣ�Ĭ��Ϊ��ǰʱ�䣩
         */
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE CheckInfo " +
                    "(checkId int NOT NULL AUTO_INCREMENT, " + 
                    " checkDescribe varchar(256), " + 
                    " checkDate DATETIME DEFAULT NOW(),"+
                    " PRIMARY KEY ( checkId )) default charset=utf8; "; 
        dbTools.stdDB.update(sql);
        dbTools.close();
    }
    
    public static int insertNewLine(String checkDescribe) {
        DBTools dbTools = DBTools.getInstance();
        String sql = String.format("insert into CheckInfo(checkDescribe) values(\'%s\')", checkDescribe);
        dbTools.stdDB.update(sql);
        sql = "select max(checkId) as checkId from CheckInfo"; 
        ResultSet rs = dbTools.stdDB.query(sql);
        System.out.println(rs);
        int checkId = -1;
        try {
            while(rs.next()){
                //Retrieve by column name
                checkId  = rs.getInt("checkId");
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbTools.close();
        return checkId;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        createCheckInfoTable();
//        System.out.println(insertNewLine("first check!"));
    }

}
