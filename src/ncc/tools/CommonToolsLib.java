package ncc.tools;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class CommonToolsLib {
    
    public static void createCheckInfoTable() {
        
        /* 1、在stdDB标准表数据库中建表
         *      表名：CheckInfo，记录核查信息
         *      表中属性：checkId（自增，主键），checkDescribe（核查既简单描述），checkDate（检查日期，默认为当前时间）
         */
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE CheckInfo " +
                    "(checkId int NOT NULL AUTO_INCREMENT, " + 
                    " checkDescribe varchar(256), " + 
                    " checkDate DATETIME DEFAULT NOW(),"+
                    " PRIMARY KEY ( checkId )) default charset=utf8; "; 
        dbTools.nccDB.update(sql);
        dbTools.close();
    }
    
    public static int insertNewLine(String checkDescribe) {
        DBTools dbTools = DBTools.getInstance();
        String sql = String.format("insert into CheckInfo(checkDescribe) values(\'%s\')", checkDescribe);
        dbTools.nccDB.update(sql);
        sql = "select max(checkId) as checkId from CheckInfo"; 
        ResultSet rs = dbTools.nccDB.query(sql);
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
    
    
    /*  在nccDB数据库中建表 ProvinceInfo
     */
    public static void createProvinceTable() throws EncryptedDocumentException, InvalidFormatException, IOException {
        
        /* 1、在nccDB数据库中建表
         *      表名：ProvinceInfo，记录省份编码信息
         *      表中属性：province_ID（主键, 省份编码），province_name（省份名）
         */
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE Province_Info " +
                    "(provinceID varchar(20) NOT NULL, " + 
                    " provinceName varchar(64), " + 
                    " PRIMARY KEY ( provinceID )) default charset=utf8; "; 
        dbTools.nccDB.update(sql);
        insertProvinceInfo("./StdFileLib/省份码表.xls");
        dbTools.close();
    }
    
    /*
     * 在nccDB的ProvinceInfo表中插入省份数据 
     */
    public static void insertProvinceInfo(String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
        File provinceFile = new File(path);
        Workbook workbook = WorkbookFactory.create(provinceFile);
        
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
        for (int row = 0; row < sheet.getLastRowNum(); row++){
            Row r = sheet.getRow(row);
            r.getCell(0).setCellType(Cell.CELL_TYPE_STRING); // 将cell转换为string格式，以便于读取
            String idCellValue = r.getCell(0).getStringCellValue(); // province id
            String nameCellValue = r.getCell(1).getStringCellValue(); // province name

            System.out.println(idCellValue+" "+ nameCellValue);
            sql = String.format("insert into Province_Info(provinceID, provinceName) values(\'%s\',  \'%s\')", idCellValue, nameCellValue);
            dbTools.nccDB.update(sql);
        }
        dbTools.close();
    }

    public static void main(String[] args) {
        //1、建省份编码表： ProvinceInfo，并插入数据
        try {
            createProvinceTable();
//            insertProvinceInfo("./StdFileLib/省份码表.xls");
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
//      createCheckInfoTable();


        
    }

}
