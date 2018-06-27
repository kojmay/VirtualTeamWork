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
    public static void createStdLACandTACTable() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE STD_PS_LACandTAC " +
                    "(id int NOT NULL AUTO_INCREMENT, " + 
                    " province VARCHAR(255), " + 
                    " type VARCHAR(4), " + // LAC��TAC
                    " l1 VARCHAR(4), " + 
                    " l2 VARCHAR(4), " + 
                    " l3 VARCHAR(4), " + // Ĭ��ΪNULL,��l3\l4��ʡ��������
                    " l4 VARCHAR(4), " + 
                    " PRIMARY KEY ( id )) default charset=utf8; "; 
        dbTools.stdDB.update(sql);
        dbTools.close();
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
//            int sheetCount = workbook.getNumberOfSheets();
            
            
            DBTools dbTools = DBTools.getInstance();
            String sql = ""; 
            
            /*// ������һ��������
            Sheet sheet = workbook.getSheetAt(0);
            // ����������Ȼ��һ�У��ڵõ���������
            Row tmp = sheet.getRow(0);
            if (tmp == null){
                return;
            }
            
            // ��ȡ����
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
                        System.out.println(Integer.toHexString(row-1).toUpperCase()+" "+ Integer.toHexString(col-1).toUpperCase()+" "+ cellValue);
                        String l1 = Integer.toHexString(row-1).toUpperCase(), l2 = Integer.toHexString(col-1).toUpperCase();
                        sql = String.format("insert into PS_LACandTAC(province, type, l1, l2) values(\'%s\', \'LAC\', \'%s\', \'%s\')", cellValue, l1, l2);
                        dbTools.stdDB.update(sql);
                    }
                }
            }*/
            
            // �����ڶ���������
            Sheet sheet = workbook.getSheetAt(1);
            // ����������Ȼ��һ�У��ڵõ���������
            Row tmp = sheet.getRow(0);
            if (tmp == null){
                return;
            }
            
            // ��ȡ����
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
                        System.out.println(Integer.toHexString(row-1).toUpperCase()+" "+ Integer.toHexString(col-1).toUpperCase()+" "+ cellValue);
                        String l1 = Integer.toHexString(row-1).toUpperCase(), l2 = Integer.toHexString(col-1).toUpperCase();
                        sql = String.format("insert into PS_LACandTAC(province, type, l1, l2) values(\'%s\', \'TAC\', \'%s\', \'%s\')", cellValue, l1, l2);
                        dbTools.stdDB.update(sql);
                    }
                }
            }
            
            
            dbTools.close();
            
        }
        
        
    }
    
    /* 3.��logDB��־���ݿ��н���
     *      ������PS_TACandLAC
     *      �������ԣ�id����������������province��ʡ��������type��LAC/TAC)��l1��l2��l3��l4��Date
     *      
     */
    public static void createLogTACandLAC() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE PS_TACandLAC " +
                    "(id int NOT NULL AUTO_INCREMENT, " + 
                    " checkId int NOT NULL, " + 
                    " province VARCHAR(255), " + 
                    " type VARCHAR(4), " + // LAC��TAC
                    " l1 VARCHAR(4), " + 
                    " l2 VARCHAR(4), " + 
                    " l3 VARCHAR(4), " + // Ĭ��ΪNULL,��l3\l4��ʡ��������
                    " l4 VARCHAR(4), " + 
                    " PRIMARY KEY ( id )) default charset=utf8; "; 
        dbTools.logDB.update(sql);
        dbTools.close();
    }
    
    /* 4����ȡlog�ļ�����ȡ���ݣ�������logDB���ݿ���У��˽׶α����Զ�
     *    ��־�ļ�λ�ã�./LogFileLib/Chapter1_1_HuaweiMMETAC/
     *    ��־���ݿ������PS_TACandLAC
     */
    public static void analysisAndInsertLog(String filePath, String province) throws IOException{
        // 1.��stdDB��CheckInfo���в���˲���Ϣ������checkId ��Ϊ�������ݵ�һ������
//        int checkId = CommonToolsLib.insertNewLine("first check");
//        System.out.println("check Id is: " + checkId);
        int checkId = 1;
        // 2.������־��������logDB�е�PS_TACandLAC
        
        analysisS1PAGIG(province ,filePath + "S1PAGING_1.TXT", checkId);
    }
    
    public static void analysisS1PAGIG(String province, String filePath, int checkId) {
        
        try {
            File logFile = new File(filePath);
            if (logFile.isFile() && logFile.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(logFile));
                BufferedReader bReader = new BufferedReader(reader);
                
                Pattern pat = Pattern.compile("(\\w{9})\\s+NB-IoT");
                Matcher mat ;
                String line = bReader.readLine();;
                DBTools dbTool = DBTools.getInstance();
                ArrayList<String> tacList = new ArrayList<>();
                while (line != null) {
//                    System.out.println(line);
                    mat = pat.matcher(line);
                    if(mat.find()) {
//                        System.out.println("#"+mat.group(1)+"#");
                        tacList.add(mat.group(1));
                    }
                    line = bReader.readLine();
                }
                bReader.close();
                reader.close();
                System.out.println(tacList);
                
//                String l1 = mat.group(1).charAt(5) + "";
//                String l2 = mat.group(1).charAt(6) + "";
//                String l3 = mat.group(1).charAt(7) + "";
//                String l4 = mat.group(1).charAt(8) + "";
//                tacList.add(mat.group(1));
//                dbTool.logDB.update(String.format("insert into PS_TACandLAC(checkId, province, type, l1, l2, l3, l4) values(%d, \'%s\', 'TAC', \'%s\', \'%s\', \'%s\', \'%s\')", checkId, province, l1, l2, l3, l4));
//                System.out.println(l1+ " " + l2 + " "+ l3 + " "  + l4);
                
                ResultSet rs = dbTool.stdDB.query(String.format("select * from PS_LACandTAC where province = \'%s\' and type = \'TAC\'", province));
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
                    
                    // ��ʼ�˲鲿��
                    Set<String> unecessarySet = new HashSet<>();
                    int rightNum = 0;
                    for (String tacItem : tacList) {
                        if (tacHeaderSet.contains(tacItem.substring(0, tacLen))) {
                            rightNum ++;
                        }else {
                            unecessarySet.add(tacItem);
                        }
                    }
                    System.out.printf("һ��  %d ���� ����  %d ��ȷ�� %d ���ࡣ", rightNum+unecessarySet.size(), rightNum, unecessarySet.size());
                    
                } catch (SQLException e) {
                    rs.close();
                    e.printStackTrace();
                }
                
                
                dbTool.close();
                
            } else {
                System.out.println(filePath + " �ļ������ڣ�");
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("�ļ���ȡ����"+ e.getMessage());
        }
        
        
    }
    
    
    public static void runCheck() throws InvalidFormatException, IOException {
        // 1.�ڱ�׼�����ݿ��н���
        createStdLACandTACTable();
        
        // 2.��ȡ��׼���������׼�����ݿ�
        insertIntoPS_LACandTAC("./StdFileLib/Chapter1_1_LACԭʼ������NB TAC������ϸ��.xls");
        
        // 3.��logDB��־���ݿ��н���   ������PS_TACandLAC
//        createLogTACandLAC();
        
        // 4.
//        analysisAndInsertLog("./LogFileLib/Chapter1_1_HuaweiMMETAC/", "����");

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
