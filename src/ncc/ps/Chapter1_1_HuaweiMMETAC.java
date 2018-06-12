package ncc.ps;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.*;

import ncc.mysql_connection.DBTools;

/**  
 *  System： NCC VirtualTeamWork
 *  Title： Chapter1_1_HuaweiMMETAC.java
 *  Description： 对功能点的描述
 *  @author：  梅海峰
 *  @date： 2018年6月12日 上午11:19:01 
 *  Copyright (c) 2018 CMCC.
 *   
 */

public class Chapter1_1_HuaweiMMETAC {
    
    /* 1、在stdDB标准表数据库中建表
     *      表名：PS_LACandTAC
     *      表中属性：id（自增，主键），province（省份名），type（LAC/TAC)，l1、l2、l3、l4
     */
    public static void createLACandTACTable() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE PS_LACandTAC " +
                    "(id int NOT NULL AUTO_INCREMENT, " + 
                    " province VARCHAR(255), " + 
                    " type INTEGER, " + 
                    " l1 VARCHAR(4), " + 
                    " l2 VARCHAR(4), " + 
                    " l3 VARCHAR(4) DEFAULT NULL, " + // 默认为NULL,即l3\l4各省自主分配
                    " l4 VARCHAR(4) DEFAULT NULL, " + 
                    " PRIMARY KEY ( id ))"; 
        dbTools.stdDB.update(sql);
    }
    
    /* 2、读取标准表数据，并插入数据库表中，此阶段可手动插入
     *    标准表位置：./StdFileLib/Chapter1_1_LAC原始分配与NB TAC分配明细表.xls
     *    标准数据库表名：PS_LACandTAC
     */
    public static void insertIntoPS_LACandTAC(String path) throws IOException, InvalidFormatException{
        if (path.endsWith(".xls")) {
            File stdFile = new File(path);
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(stdFile);
            // 获得工作表个数
            int sheetCount = workbook.getNumberOfSheets();
            // 遍历整个工作表
            for(int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                // 获得行数
                int rows = sheet.getLastRowNum() + 1;
                // 获得列数，先获得一行，在得到改行列数
                Row tmp = sheet.getRow(0);
                if (tmp == null){
                   continue;
                }
                int cols = tmp.getPhysicalNumberOfCells();
                // 读取数据
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
    
    
    
    
    // 3.现网数据表
    
    // 4。查表对比
    
    
    public static void runCheck() throws InvalidFormatException, IOException {
        // 1.在标准表数据库中建表
        //createLACandTACTable();
        
        // 2.读取标准表，并插入标准表数据库
        insertIntoPS_LACandTAC("./StdFileLib/Chapter1_1_LAC原始分配与NB TAC分配明细表.xls");

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
