package sql;

import java.sql.PreparedStatement;

import java.sql.DriverManager;

import java.sql.SQLException;

import java.util.List;

import java.sql.Connection;

import javax.swing.JOptionPane;

import com.sun.jdi.request.ClassPrepareRequest;
import models.Client;

public class DataBaseConnection {

    private final String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://localhost::3306/bank_data_sqlXML";
    private String user = "root";
    private String pass = "12345678";
    Connection connection = null;
    String insertStatementSql = "INSERT INTO customers (name, phone_number, address, email, country, number_range, " +
            "                   rfc, tax VALUES (?,?,?,?,?,?,?,?,?)";

    public DataBaseConnection (List<Client> clientsList){

        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, pass);

            JOptionPane.showMessageDialog(null, "Connected successfully to database");

            //PreparedStatement insertClientData = connection.prepareStatement(insertStatementSql);


            }catch(SQLException s){
            System.out.println(s.toString());
        }
    }
}
