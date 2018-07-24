package ncc.ps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PublicKey;
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


/*
 * 数据库中的数据类
 */
class HuaweiMMETACObject{
    
    public int id;
    public int checkID;
    public String provinceID;
    public String type;
    public String l1, l2, l3, l4;
    
    public HuaweiMMETACObject(int id, int checkID, String provinceID, String type, String l1, String l2, String l3, String l4) {
        this.id = id;
        this.checkID = checkID;
        this.provinceID = provinceID;
        this.type = type;
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
        this.l4 = l4;
    }
    
    public int getId() {
        return id;
    }
    
    public int getCheckID() {
        return checkID;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public String getType() {
        return type;
    }

    public String getL1() {
        return l1;
    }

    public String getL2() {
        return l2;
    }

    public String getL3() {
        return l3;
    }

    public String getL4() {
        return l4;
    }
    
    public String getInfo() {
        return String.format("id:%d \t checkID:%d \t provinceID:%s \t type:%s \t l1:%s \t l2:%s \t l3:%s \t l4:%s ", this.id, this.checkID, this.provinceID, this.type, this.l1, this.l2, this.l3, this.l4);
    }

}


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
//        dbTools.close();
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


//                dbTools.close();
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
        analysisLACandTACFile(provinceId ,filePath + "S1PAGING_1.TXT", checkId, "(\\w{9})\\s+NB-IoT", "TAC");
        // 2.解析  华为现网LAC数据的  日志 LSTTAILAI，并存入cu_ps_lacandtac
        analysisLACandTACFile(provinceId ,filePath + "LSTTAILAI_1.txt", checkId, "\\s(\\w{9})$", "LAC");
        // 3.解析  华为现网LAC数据的  日志 LST_LAIVLR_1 ，并存入cu_ps_lacandtac
        analysisLACandTACFile(provinceId ,filePath + "LST_LAIVLR_1.txt", checkId, "^\\s(\\w{9})", "LAC");
    }
    
    
    /* 4: 解析 华为现网NB TAC的 日志，并存入 cu_ps_lacandtac中 
     *    filePath：日志文件位置
     *    provinceId：省公司ID
     *    checkId： 核查操作的ID，此ID唯一标识一次核查任务
     *    regex: 使用的正则表达式
     *    type: LAC / TAC
     */
    public static void analysisLACandTACFile(String provinceID, String fileName, int checkId, String regex, String type) {
        
        try {
            File logFile = new File(fileName);
            if (logFile.isFile() && logFile.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(logFile));
                BufferedReader bReader = new BufferedReader(reader);
                
                Pattern pat = Pattern.compile(regex);
                Matcher mat ;
                String line = bReader.readLine();
                DBTools dbTool = DBTools.getInstance();
                ArrayList<String> tacList = new ArrayList<>();
                while (line != null) {
//                    System.out.println(line);
                    mat = pat.matcher(line);
                    if(mat.find()) {
//                        System.out.println(mat.group(1));
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
                    insertSQL += String.format("( %d, \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\" ),", checkId, provinceID, type, l1, l2, l3, l4);
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
                
//                dbTool.close();
                
            } else {
                System.out.println(fileName + " 文件不存在！");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("文件读取错误！"+ e.getMessage());
        }
    }
    
    
    /* 5.在数据库中建表
     *      表名：check_result_huawei_mme_tac
     *      表中属性：id（自增，主键）, checkID(核查任务id)，correctNum(正确条数)， wrongNum(错误条数), lossNum（漏做条数）  
     */
    public static void createTable_check_result() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE check_result " +
                    "(id int NOT NULL AUTO_INCREMENT PRIMARY KEY, " + 
                    " checkID int NOT NULL, " + 
                    " provinceID VARCHAR(20), " + 
                    " totalCounts int, " + 
                    " correctNum int, " + 
                    " wrongNum int, " + 
                    " lossNum int, " + 
                    " FOREIGN KEY (provinceID) REFERENCES province_info(provinceID)) charset utf8;";
        dbTools.nccDB.update(sql);
    }
    
    
    /* 6.对比标准表和现网数据，获得少做数据； 对比现网数据和标准数据，获得多做数据。
     *      并将结果插入核查结果表 check_result
     */
    public static void compareData(String provinceID, int checkID ) throws SQLException {
        DBTools dbTool = DBTools.getInstance();
        
        // 获取标准库数据
        ResultSet stdRS = dbTool.nccDB.query(String.format("select * from std_ps_lacandtac where provinceID = \'%s\'", provinceID));
        Set<HuaweiMMETACObject> stdLibSet = new HashSet<HuaweiMMETACObject>();
        while(stdRS.next()){

            int id = stdRS.getInt("id");
            String type = stdRS.getString("type");
            String province_ID = stdRS.getString("provinceID");
            String l1 = stdRS.getString("l1");
            String l2 = stdRS.getString("l2");
            String l3 = stdRS.getString("l3");
            String l4 = stdRS.getString("l4");
            stdLibSet.add(new HuaweiMMETACObject(id, -1, province_ID, type, l1, l2, l3, l4));
        }
        
        System.out.println("stdLibSet len: " + stdLibSet.size());
        for (HuaweiMMETACObject item : stdLibSet) {
            System.out.println(item.getInfo());
        }
        stdRS.close();

        // 获取现网数据, 并存入cuLibList
        ResultSet cuRS = dbTool.nccDB.query(String.format("select * from cu_ps_lacandtac where provinceID = \'%s\'", provinceID));
        ArrayList<HuaweiMMETACObject> cuLibList = new ArrayList<HuaweiMMETACObject>();

        while(cuRS.next()){

            int id = cuRS.getInt("id");
            String type = cuRS.getString("type");
            int check_ID = cuRS.getInt("checkID");
            String province_ID = cuRS.getString("provinceID");
            String l1 = cuRS.getString("l1");
            String l2 = cuRS.getString("l2");
            String l3 = cuRS.getString("l3");
            String l4 = cuRS.getString("l4");
            cuLibList.add(new HuaweiMMETACObject(id, check_ID, province_ID, type, l1, l2, l3, l4));
        }

        System.out.println("cuLibSet len: " + cuLibList.size());
        for (HuaweiMMETACObject item : cuLibList) {
            System.out.println(item.getInfo());
        }
        cuRS.close();


    }

    
    public static void runCheck() throws InvalidFormatException, IOException, SQLException {
        
        // 1.在标准表数据库中建表
//        createStdLACandTACTable();
        // 2.在标准表中插入标准数据
//        insertIntoSTD_PS_LACandTAC("./StdFileLib/Chapter1_1_LAC原始分配与NB TAC分配明细表.xls");
        
        // 3.在建现网数据表   表名：cu_ps_lacandtac
//        createCUTACandLAC();
        
        // 4.分析现网数据文件，并存入cu_ps_lacandtac
//        analysisAndInsertLog("./CuFileLib/Chapter1_1_HuaweiMMETAC/", "731", 1);
        
        // 5.新建核查结果表
//        createTable_check_result();
        
        // 6.比较数据
        compareData("731", 1);
        

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
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
