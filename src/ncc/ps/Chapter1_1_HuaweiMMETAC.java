package ncc.ps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.*;

import ncc.tools.CommonToolsLib;
import ncc.tools.DBTools;

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
    public static void createStdLACandTACTable() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE std_ps_lacandtac " +
                    "(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, " + 
                    " provinceID VARCHAR(20), " + 
                    " type VARCHAR(4), " + // LAC，TAC
                    " l1 VARCHAR(4), " + 
                    " l2 VARCHAR(4), " + 
                    " l3 VARCHAR(4), " + // 默认为NULL,即l3\l4各省自主分配
                    " l4 VARCHAR(4), " + 
                    " FOREIGN KEY (provinceID) REFERENCES province_info(provinceID)) charset utf8;";
        dbTools.nccDB.update(sql);
        dbTools.close();
    }
    
    /* 2、读取标准表数据，并插入数据库表中，此阶段可手动插入
     *    标准表位置：./StdFileLib/Chapter1_1_LAC原始分配与NB TAC分配明细表.xls
     *    标准数据库表名：STD_PS_LACandTAC
     */
    public static void insertIntoSTD_PS_LACandTAC(String path) throws IOException, InvalidFormatException{
        if (path.endsWith(".xls")) {
            File stdFile = new File(path);
            // 获得工作簿
            Workbook workbook = WorkbookFactory.create(stdFile);
           
            DBTools dbTools = DBTools.getInstance();
            String sql = ""; 
            // 遍历第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 获得列数，先获得一行，在得到改行列数
            Row tmp = sheet.getRow(0);
            if (tmp == null){
                return;
            }
            
            // 读取数据
            for (int row = 1; row < 17; row++){
                Row r = sheet.getRow(row);
                for (int col = 1; col < 17; col++){
                    String cellValue = r.getCell(col).getStringCellValue();
                    if(cellValue.length()>0) {
                        Pattern pat = Pattern.compile("^([\u4E00-\u9FA5]+)\\d+$");
                        Matcher mat = pat.matcher(cellValue);
                        if(mat.matches()) {
                            cellValue = mat.group(1);
                        }
                        
                        sql = String.format("select  * from Province_Info where provinceName like \'%s%%\'", cellValue);
                        ResultSet resultSet = dbTools.nccDB.query(sql);
                        
                        try {
                            while (resultSet.next()) {
                                String provinceID = resultSet.getString("provinceID");
                                String provinceName = resultSet.getString("provinceName");
                                System.out.println(provinceName+"  , "+ provinceID);

                                System.out.println(Integer.toHexString(row-1).toUpperCase()+" "+ Integer.toHexString(col-1).toUpperCase()+" "+ cellValue);
                                String l1 = Integer.toHexString(row-1).toUpperCase(), l2 = Integer.toHexString(col-1).toUpperCase();
                                sql = String.format("insert into STD_PS_LACandTAC(provinceID, type, l1, l2) values(\'%s\', \'LAC\', \'%s\', \'%s\')", provinceID, l1, l2);
                                dbTools.nccDB.update(sql);
                            }
                            resultSet.close();
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                    }
                }
            }
            
            
            // 遍历第二个工作表
            sheet = workbook.getSheetAt(1);
            // 获得列数，先获得一行，在得到改行列数
            tmp = sheet.getRow(0);
            if (tmp == null){
                return;
            }
            
            // 读取数据
            for (int row = 1; row < 17; row++){
                Row r = sheet.getRow(row);
                for (int col = 1; col < 17; col++){
                    String cellValue = r.getCell(col).getStringCellValue();
                    if(cellValue.length()>0) {
                        Pattern pat = Pattern.compile("^([\u4E00-\u9FA5]+)\\d+$");
                        Matcher mat = pat.matcher(cellValue);
                        if(mat.matches()) {
                            cellValue = mat.group(1);
                        }
                        
                        sql = String.format("select  * from Province_Info where provinceName like \'%s%%\'", cellValue);
                        ResultSet resultSet = dbTools.nccDB.query(sql);

                        try {
                            while (resultSet.next()) {
                                String provinceID = resultSet.getString("provinceID");
                                String provinceName = resultSet.getString("provinceName");
                                System.out.println(provinceName+"  , "+ provinceID);

                                System.out.println(Integer.toHexString(row-1).toUpperCase()+" "+ Integer.toHexString(col-1).toUpperCase()+" "+ cellValue);
                                String l1 = Integer.toHexString(row-1).toUpperCase(), l2 = Integer.toHexString(col-1).toUpperCase();
                                sql = String.format("insert into STD_PS_LACandTAC(provinceID, type, l1, l2) values(\'%s\', \'TAC\', \'%s\', \'%s\')", provinceID, l1, l2);

                            }
                            dbTools.nccDB.update(sql); 
                        }catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }


                dbTools.close();
            }


        }

    /* 3.在数据库中建表
     *      表名：cu_ps_lacandtac
     *      表中属性：id（自增，主键），province（省份名），type（LAC/TAC)，l1、l2、l3、l4，Date
     */
    public static void createCUTACandLAC() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE cu_ps_lacandtac " +
                    "(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, " + 
                    " checkID int NOT NULL, " + 
                    " provinceID VARCHAR(20), " + 
                    " type VARCHAR(4), " + // LAC，TAC
                    " l1 VARCHAR(4), " + 
                    " l2 VARCHAR(4), " + 
                    " l3 VARCHAR(4), " + // 默认为NULL,即l3\l4各省自主分配
                    " l4 VARCHAR(4), " + 
                    " FOREIGN KEY (provinceID) REFERENCES province_info(provinceID)) charset utf8;";
        dbTools.nccDB.update(sql);
//        dbTools.close();
    }
    
    /* 4、读取现网配置文件，提取数据，并插入数据库表中，此阶段必须自动
     *    filePath：日志文件位置
     *    provinceId：省公司ID
     *    checkId： 核查操作的ID，此ID唯一标识一次核查任务
     *    日志数据库表名：cu_ps_lacandtac
     */
    public static void analysisAndInsertLog(String filePath, String provinceId, int checkId) throws IOException{
        // 1.解析  华为现网NB TAC的  日志，并存入cu_ps_lacandtac
        analysisS1PAGIG(provinceId ,filePath + "S1PAGING_1.TXT", checkId);
        // 2.解析  华为现网NB TAC的  日志，并存入cu_ps_lacandtac
        analysisLSTAILAI(provinceId ,filePath + "S1PAGING_1.TXT", checkId);
        
    }
    
    
    /* 4-1: 解析 华为现网NB TAC的 日志，并存入 cu_ps_lacandtac中 
     *    filePath：日志文件位置
     *    provinceId：省公司ID
     *    checkId： 核查操作的ID，此ID唯一标识一次核查任务
     */
    public static void analysisS1PAGIG(String provinceID, String fileName, int checkId) {
        
        try {
            File logFile = new File(fileName);
            if (logFile.isFile() && logFile.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(logFile));
                BufferedReader bReader = new BufferedReader(reader);
                
                Pattern pat = Pattern.compile("(\\w{9})\\s+NB-IoT");
                Matcher mat ;
                String line = bReader.readLine();
                DBTools dbTool = DBTools.getInstance();
                ArrayList<String> tacList = new ArrayList<>();
                while (line != null) {
//                    System.out.println(line);
                    mat = pat.matcher(line);
                    if(mat.find()) {
                        tacList.add(mat.group(1));
                    }
                    line = bReader.readLine();
                }
                bReader.close();
                reader.close();
                System.out.println(tacList);
                
                String insertSQL = "insert into cu_ps_lacandtac(checkId, provinceId, type, l1, l2, l3, l4) values ";
                String l1, l2, l3, l4;
                for (String l_item : tacList) {
                    l1 = l_item.substring(5, 6);
                    l2 = l_item.substring(6, 7);
                    l3 = l_item.substring(7, 8);
                    l4 = l_item.substring(8, 9);
//                    System.out.println(l_item + " " + l1+ " " + l2 + " "+ l3 + " "  + l4);
                    insertSQL += String.format("( %d, \"%s\", \"TAC\", \"%s\", \"%s\", \"%s\", \"%s\" ),", checkId, provinceID, l1, l2, l3, l4);
                }
                insertSQL = insertSQL.substring(0, insertSQL.length()-1) + ";";
                System.out.println(insertSQL);
                DBTools dbTools = DBTools.getInstance();
                dbTool.nccDB.update(insertSQL);
                
//                dbTool.logDB.update(String.format("insert into PS_TACandLAC(checkId, province, type, l1, l2, l3, l4) values(%d, \'%s\', 'TAC', \'%s\', \'%s\', \'%s\', \'%s\')", checkId, province, l1, l2, l3, l4));
//                System.out.println(l1+ " " + l2 + " "+ l3 + " "  + l4);
                
                /*
                ResultSet rs = dbTool.nccDB.query(String.format("select * from STD_PS_LACandTAC where province = \'%s\' and type = \'TAC\'", province));
                Set<String> tacHeaderSet = new HashSet<String>();
                int tacLen = 5;
                try {
                    while(rs.next()){
                        String l1 = rs.getString("l1");
                        String l2 = rs.getString("l2");
                        String l3 = rs.getString("l3");
                        String l4 = rs.getString("l4");
                        
                        if (l3 == null ) {
                            tacHeaderSet.add("46000"+l1+l2);
                            tacLen = 7;
                        } else if(l4 == null){
                            tacHeaderSet.add("46000"+l1+l2+l3);
                            tacLen = 8;
                        } else {
                            tacHeaderSet.add("46000"+l1+l2+l3+l4);
                            tacLen = 9;
                        }
                    }
                    System.out.println(tacHeaderSet);
                    rs.close();
                    
                    // 开始核查部分
                    Set<String> unecessarySet = new HashSet<>();
                    int rightNum = 0;
                    for (String tacItem : tacList) {
                        if (tacHeaderSet.contains(tacItem.substring(0, tacLen))) {
                            rightNum ++;
                        }else {
                            unecessarySet.add(tacItem);
                        }
                    }
                    System.out.printf("一共  %d 条， 其中  %d 正确， %d 冗余。", rightNum+unecessarySet.size(), rightNum, unecessarySet.size());
                    
                } catch (SQLException e) {
                    rs.close();
                    e.printStackTrace();
                }
                */
                
                dbTool.close();
                
            } else {
                System.out.println(fileName + " 文件不存在！");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("文件读取错误！"+ e.getMessage());
        }
    }
    
    /* 4-1: 解析 华为现网NB TAC的 日志，并存入 cu_ps_lacandtac中 
     *    filePath：日志文件位置
     *    provinceId：省公司ID
     *    checkId： 核查操作的ID，此ID唯一标识一次核查任务
     */
    public static void analysisLSTAILAI(String provinceID, String fileName, int checkId) {
        
    }
    
    
    public static void runCheck() throws InvalidFormatException, IOException {
        
        // 1.在标准表数据库中建表
//        createStdLACandTACTable();
        // 2.在标准表中插入标准数据
//        insertIntoSTD_PS_LACandTAC("./StdFileLib/Chapter1_1_LAC原始分配与NB TAC分配明细表.xls");
        
        // 3.在建现网数据表   表名：cu_ps_lacandtac
//        createCUTACandLAC();
        
        // 4.分析现网数据文件，并存入cu_ps_lacandtac
        analysisAndInsertLog("./CuFileLib/Chapter1_1_HuaweiMMETAC/", "731", 1);

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
    }

}
