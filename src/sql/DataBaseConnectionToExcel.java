package sql;
import java.sql.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sql.DataBaseConnection;
public class DataBaseConnectionToExcel {



    public static void insertDataFromDataBaseToExcel() throws SQLException {
        String selectInformationFromCustomersStatement = "SELECT * FROM customers";
        DataBaseConnection connectionData = null;
        Connection connectionToDatabase = DriverManager.getConnection(connectionData.getUrl(),
                                                                      connectionData.getUser(),
                                                                      connectionData.getPass());
        Statement extractInformationFromDataBase = connectionToDatabase.createStatement();
        ResultSet executeQuery = extractInformationFromDataBase.executeQuery(selectInformationFromCustomersStatement);
        //This will create an empty workbook on excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Creating a new sheet on the excel file called "customers"
        workbook.createSheet("customers");
    }
}
