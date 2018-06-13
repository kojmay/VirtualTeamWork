package ncc.ps;

import java.io.File;
import java.io.IOException;
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
    public static void createLACandTACTable() {
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE PS_LACandTAC " +
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
            int sheetCount = workbook.getNumberOfSheets();
            
            
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
     *    ��׼��λ�ã�./StdFileLib/Chapter1_1_LACԭʼ������NB TAC������ϸ��.xls
     *    ��־���ݿ������PS_TACandLAC
     */
    public static void analysisAndInsertLog(String path) {
        // 1.��stdDB��CheckInfo���в���˲���Ϣ������checkId ��Ϊ�������ݵ�һ������
        int checkId = CommonToolsLib.insertNewLine("first check");
        System.out.println(checkId);
        
        // 2.������־��������logDB�е�PS_TACandLAC
        
        
    }
    
    
    public static void runCheck() throws InvalidFormatException, IOException {
        // 1.�ڱ�׼�����ݿ��н���
//        createLACandTACTable();
        
        // 2.��ȡ��׼���������׼�����ݿ�
//        insertIntoPS_LACandTAC("./StdFileLib/Chapter1_1_LACԭʼ������NB TAC������ϸ��.xls");
        
        // 3.��logDB��־���ݿ��н���   ������PS_TACandLAC
//        createLogTACandLAC();
        
        // 4.
        analysisAndInsertLog("");

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
