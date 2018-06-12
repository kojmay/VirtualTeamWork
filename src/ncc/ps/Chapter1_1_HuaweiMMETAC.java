package ncc.ps;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.*;

import ncc.mysql_connection.DBTools;

/**  
 *  System�� NCC VirtualTeamWork
 *  Title�� Chapter1_1_HuaweiMMETAC.java
 *  Description�� �Թ��ܵ������
 *  @author��  ÷����
 *  @date�� 2018��6��12�� ����11:19:01 
 *  Copyright (c) 2018 CMCC.
 *   
 */

public class Chapter1_1_HuaweiMMETAC {
    
    /* 1����stdDB��׼�����ݿ��н���
     *      ������PS_LACandTAC
     *      �������ԣ�id����������������province��ʡ��������type��LAC/TAC)��l1��l2��l3��l4
     */
    public static void createLACandTACTable() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE PS_LACandTAC " +
                    "(id int NOT NULL AUTO_INCREMENT, " + 
                    " province VARCHAR(255), " + 
                    " type INTEGER, " + 
                    " l1 VARCHAR(4), " + 
                    " l2 VARCHAR(4), " + 
                    " l3 VARCHAR(4) DEFAULT NULL, " + // Ĭ��ΪNULL,��l3\l4��ʡ��������
                    " l4 VARCHAR(4) DEFAULT NULL, " + 
                    " PRIMARY KEY ( id ))"; 
        dbTools.stdDB.update(sql);
    }
    
    /* 2����ȡ��׼�����ݣ����������ݿ���У��˽׶ο��ֶ�����
     *    ��׼��λ�ã�./StdFileLib/Chapter1_1_LACԭʼ������NB TAC������ϸ��.xls
     *    ��׼���ݿ������PS_LACandTAC
     */
    public static void insertIntoPS_LACandTAC(String path) throws IOException, InvalidFormatException{
        if (path.endsWith(".xls")) {
            File stdFile = new File(path);
            // ��ù�����
            Workbook workbook = WorkbookFactory.create(stdFile);
            // ��ù��������
            int sheetCount = workbook.getNumberOfSheets();
            // ��������������
            for(int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                // �������
                int rows = sheet.getLastRowNum() + 1;
                // ����������Ȼ��һ�У��ڵõ���������
                Row tmp = sheet.getRow(0);
                if (tmp == null){
                   continue;
                }
                int cols = tmp.getPhysicalNumberOfCells();
                // ��ȡ����
                for (int row = 0; row < rows; row++){
                   Row r = sheet.getRow(row);
                   for (int col = 0; col < cols; col++){
                       System.out.println("\n"+row+" "+ col);
                       System.out.printf("%10s", r.getCell(col));
                   }
                   System.out.println();
                }
            }
        }
    }
    
    
    
    
    // 3.�������ݱ�
    
    // 4�����Ա�
    
    
    public static void runCheck() throws InvalidFormatException, IOException {
        // 1.�ڱ�׼�����ݿ��н���
        //createLACandTACTable();
        
        // 2.��ȡ��׼���������׼�����ݿ�
        insertIntoPS_LACandTAC("./StdFileLib/Chapter1_1_LACԭʼ������NB TAC������ϸ��.xls");

    }
    
    public static void main(String[] args) {
        try {
            runCheck();
            
        } catch (InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("hel");
    }

}
