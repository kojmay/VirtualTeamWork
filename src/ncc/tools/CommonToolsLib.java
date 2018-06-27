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
    
    
    /*  ��nccDB���ݿ��н��� ProvinceInfo
     */
    public static void createProvinceTable() throws EncryptedDocumentException, InvalidFormatException, IOException {
        
        /* 1����nccDB���ݿ��н���
         *      ������ProvinceInfo����¼ʡ�ݱ�����Ϣ
         *      �������ԣ�province_ID������, ʡ�ݱ��룩��province_name��ʡ������
         */
        DBTools dbTools = DBTools.getInstance();
        String sql = "CREATE TABLE Province_Info " +
                    "(provinceID varchar(20) NOT NULL, " + 
                    " provinceName varchar(64), " + 
                    " PRIMARY KEY ( provinceID )) default charset=utf8; "; 
        dbTools.nccDB.update(sql);
        insertProvinceInfo("./StdFileLib/ʡ�����.xls");
        dbTools.close();
    }
    
    /*
     * ��nccDB��ProvinceInfo���в���ʡ������ 
     */
    public static void insertProvinceInfo(String path) throws EncryptedDocumentException, InvalidFormatException, IOException {
        File provinceFile = new File(path);
        Workbook workbook = WorkbookFactory.create(provinceFile);
        
        DBTools dbTools = DBTools.getInstance();
        String sql = ""; 
        
        // ������һ��������
        Sheet sheet = workbook.getSheetAt(0);
        // ����������Ȼ��һ�У��ڵõ���������
        Row tmp = sheet.getRow(0);
        if (tmp == null){
            return;
        }
        
        // ��ȡ����
        for (int row = 0; row < sheet.getLastRowNum(); row++){
            Row r = sheet.getRow(row);
            r.getCell(0).setCellType(Cell.CELL_TYPE_STRING); // ��cellת��Ϊstring��ʽ���Ա��ڶ�ȡ
            String idCellValue = r.getCell(0).getStringCellValue(); // province id
            String nameCellValue = r.getCell(1).getStringCellValue(); // province name

            System.out.println(idCellValue+" "+ nameCellValue);
            sql = String.format("insert into Province_Info(provinceID, provinceName) values(\'%s\',  \'%s\')", idCellValue, nameCellValue);
            dbTools.nccDB.update(sql);
        }
        dbTools.close();
    }

    public static void main(String[] args) {
        //1����ʡ�ݱ���� ProvinceInfo������������
        try {
            createProvinceTable();
//            insertProvinceInfo("./StdFileLib/ʡ�����.xls");
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
//      createCheckInfoTable();


        
    }

}
