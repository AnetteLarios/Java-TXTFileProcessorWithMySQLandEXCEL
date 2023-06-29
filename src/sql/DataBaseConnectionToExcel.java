package sql;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

import org.apache.poi.ddf.NullEscherSerializationListener;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.JOptionPane;
import services.emailSenderGmail;
import javax.xml.transform.Result;

public class DataBaseConnectionToExcel {
    private String driver = "com.mysql.cj.jdbc.Driver";
    protected static String url = "jdbc:mysql://localhost:3306/bank_data_sqlxml";
    private static String user = "root";
    private static String pass = "12345678";
    static Connection connectionToDatabase = null;


    public static void insertDataFromDataBaseToExcel() throws SQLException, IOException {
        String selectInformationFromCustomersStatement = "SELECT * FROM customers";

        try  {
            connectionToDatabase = DriverManager.getConnection(url, user, pass);
            JOptionPane.showMessageDialog(null, "Successfully connected to Database"
                                                            + " and initializing the 'Creating Excel Document Process'");

            Statement extractInformationFromDataBase = connectionToDatabase.createStatement();
            ResultSet executeQuery = extractInformationFromDataBase.executeQuery(selectInformationFromCustomersStatement);

            createExcelWorkbook(executeQuery);
            connectionToDatabase.close();
            JOptionPane.showMessageDialog(null, "Excel file created successfully");

        } catch(SQLException | IOException s){
            System.out.println(s.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void createExcelWorkbook(ResultSet executeQuery) throws Exception {
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

        int numberOfRow = 1;

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

            row = sheet.createRow(numberOfRow++);

            row.createCell(0).setCellValue(id_user);
            row.createCell(1).setCellValue(name);
            row.createCell(2).setCellValue(phone_number);
            row.createCell(3).setCellValue(address);
            row.createCell(4).setCellValue(email);
            row.createCell(5).setCellValue(country);
            row.createCell(6).setCellValue(numberRange);
            row.createCell(7).setCellValue(balance);
            row.createCell(8).setCellValue(rfc);
            row.createCell(9).setCellValue(tax);
        }

        FileOutputStream customersFile = new FileOutputStream("C:\\Users\\SpectrumByte\\Documents" +
                                           "\\CódigosPaola\\Java-TXTFileProcessorWithMySQLandEXCEL\\src\\Resources\\" +
                                           "customers.xlsx");
        workbook.write(customersFile);
        workbook.close();
        customersFile.close();

        emailSenderGmail.callHttpTransport();
    }
}
