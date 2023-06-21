package sql;
import java.sql.*;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataBaseConnectionToExcel {



    public static void insertDataFromDataBaseToExcel() throws SQLException {
        String selectInformationFromCustomersStatement = "SELECT * FROM customers";
        DataBaseConnection connectionData = null;
        Connection connectionToDatabase = DriverManager.getConnection(connectionData.getUrl(),
                                                                      connectionData.getUser(),
                                                                      connectionData.getPass());
        Statement extractInformationFromDataBase = connectionToDatabase.createStatement();
        ResultSet executeQuery = extractInformationFromDataBase.executeQuery(selectInformationFromCustomersStatement);

        createExcelWorkbook();


        while(executeQuery.next()){
            int id_user = executeQuery.getInt("id_user");
            String name = executeQuery.getString("name");
            String phone_number = executeQuery.getString("phone_number");
            String address = executeQuery.getString("address");
            String email = executeQuery.getString("email");
            String country = executeQuery.getString("country");
            int numberRange = executeQuery.getInt("number_range");
            float balance = executeQuery.getFloat("balance");
            String rfc = executeQuery.getString("rfc");
            float tax = executeQuery.getFloat("tax");



        }
    }

    public static void createExcelWorkbook(){
        //This will create an empty workbook on excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Creating a new sheet on the excel file called "customers"
        XSSFSheet sheet = workbook.createSheet("customers");
        //Creating a row in the sheet
        XSSFRow row = sheet.createRow(0);
        //Creasting Cells in that row, these will be the titles of the columns.
        row.createCell(0).setCellValue("Id User");
        row.createCell(1).setCellValue("Full name");
        row.createCell(2).setCellValue("Phone number");
        row.createCell(3).setCellValue("Address");
        row.createCell(4).setCellValue("Email");
        row.createCell(5).setCellValue("Country");
        row.createCell(6).setCellValue("Number Range");
        row.createCell(7).setCellValue("Balance");
        row.createCell(8).setCellValue("RFC");
        row.createCell(9).setCellValue("Tax");


    }
}
