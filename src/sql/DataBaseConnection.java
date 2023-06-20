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
    private String url = "jdbc:mysql://localhost:3306/bank_data_sqlxml";
    private String user = "root";
    private String pass = "12345678";
    Connection connection = null;
    String insertStatementSql = "INSERT INTO customers (name, phone_number, address, email, country, number_range," +
                                 "balance, rfc, tax) VALUES (?,?,?,?,?,?,?,?,?) ";

    public DataBaseConnection (List<Client> clientsList){

        try{
            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, pass);

            JOptionPane.showMessageDialog(null, "Connected successfully to database");

            PreparedStatement insertClientData = connection.prepareStatement(insertStatementSql);

            for (Client clients : clientsList){
                insertClientData.setString(1, clients.getName());
                insertClientData.setString(2, clients.getPhoneNumber());
                insertClientData.setString(3, clients.getAddress());
                insertClientData.setString(4, clients.getEmail());
                insertClientData.setString(5, clients.getCountry());
                insertClientData.setInt(6, clients.getNumberRange());
                insertClientData.setFloat(7, clients.getBalance());
                insertClientData.setString(8, clients.getRfc());
                insertClientData.setFloat(9, clients.getTax());
                insertClientData.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "Client List inserted in data base successfully.");
            insertClientData.close();
            connection.close();

            }catch(SQLException s){
            System.out.println(s.toString());
            }catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
