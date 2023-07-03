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
/*
DataBaseConnectionToExcel class contains methods that connects with the database, extracts the information
within it and then sends it to an Excel File.
@author Anette Larios
@since 27.06.2023
 */
public class DataBaseConnectionToExcel {
    /*
    String variables that contains data to make the dataBaseConnection
     */
    private String driver = "com.mysql.cj.jdbc.Driver";
    protected static String url = "jdbc:mysql://localhost:3306/bank_data_sqlxml";
    private static String user = "root";
    private static String pass = "12345678";
    /*
    A connection object called 'connectionToDatabase' initialized as null
     */
    static Connection connectionToDatabase = null;

    /*
    insertDataFromDataBaseToExcel function is in charge to make the connection with the database, extract the
    information and send it to an Excel file.
    @author Anette Larios
    @since 27.06.2023
     */
    public static void insertDataFromDataBaseToExcel() throws SQLException, IOException {
        /*
        Declaring a String object called 'selectInformationFromCustomersStatement', contains the sql
        statement to select all the information within customers table in the database.
         */
        String selectInformationFromCustomersStatement = "SELECT * FROM customers";

        try  {
            /*
            The connection object stores the data to make the connection with the database
             */
            connectionToDatabase = DriverManager.getConnection(url, user, pass);
            /*
            If the connection with the database is properly made, a message will be shown
             */
            JOptionPane.showMessageDialog(null, "Successfully connected to Database"
                                                            + " and initializing the 'Creating Excel Document Process'");

            /*
            Inserts the statement previously declared in the database
             */
            Statement extractInformationFromDataBase = connectionToDatabase.createStatement();
            ResultSet executeQuery = extractInformationFromDataBase.executeQuery(selectInformationFromCustomersStatement);
            /*
            Calls the function createExcelWorkbook and sends as a parameter the information obtained from the database
            that is stored in the ResultSet object called 'executeQuery'.
            */
            createExcelWorkbook(executeQuery);
            //Close the connection with the database
            connectionToDatabase.close();
            //If the createExcelWorkbook function is executed without problems, a message will be shown.
            JOptionPane.showMessageDialog(null, "Excel file created successfully");

        /*
        If a problem is shown while making the connection with the database or while creating the Excel file a
        message will be displayed.
         */
        } catch(SQLException | IOException s){
            System.out.println(s.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*
    createExcelWorkbook function is in charge to create the excel file.
    @param ResultSet executeQuery is an object that stores the information extracted from the database.
    @author Anette Larios
    @since 27.06.2023
     */
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
        /*
        Declaring an int variable called numberOfRow and initializing it as 1
         */
        int numberOfRow = 1;
        /*
        while there is more information in the executeQuery object, the information will be inserted in the excel file
        per row
         */
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
            /*
            Here the iterator of row is incremented to do not insert the information within the same row, instead
            the information will be inserted consecutively.
             */
            row = sheet.createRow(numberOfRow++);
            /*
            Setting information within the cells.
             */
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
        /*
        Once all the information is inserted a file will be created in the next path with that name
         */
        FileOutputStream customersFile = new FileOutputStream("C:\\Users\\SpectrumByte\\Documents" +
                                           "\\CódigosPaola\\Java-TXTFileProcessorWithMySQLandEXCEL\\src\\Resources\\" +
                                           "customers.xlsx");
        /*
        Writing all the information in the workbook
         */
        workbook.write(customersFile);
        /*
        Closing the Excel Workbook
         */
        workbook.close();
        /*
        closing the new customers Excel file
         */
        customersFile.close();
        /*
        Calling the function callHttpTransport.
         */
        emailSenderGmail.callHttpTransport();
    }
}
