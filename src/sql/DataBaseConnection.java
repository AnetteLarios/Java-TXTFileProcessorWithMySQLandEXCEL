package sql;

import java.sql.PreparedStatement;

import java.sql.DriverManager;

import java.sql.SQLException;

import java.util.List;

import java.sql.Connection;

import javax.swing.JOptionPane;

import com.sun.jdi.request.ClassPrepareRequest;

import models.Client;

import sql.DataBaseConnectionToExcel;

import services.emailSenderGmail;
/*
DataBaseConnection class contains a constructor in charge to create objects that makes the connection
with the database.
@author Anette Larios
@since 20.06.2023
 */
public class DataBaseConnection {
    /*
    String variables that contains data to set the dataBase connection
     */
    private String driver = "com.mysql.cj.jdbc.Driver";
    protected String url = "jdbc:mysql://localhost:3306/bank_data_sqlxml";
    private String user = "root";
    private String pass = "12345678";
    //Declaring  a connection object called connection initialized as null
    Connection connection = null;
    /*
    Initializing a String variable that stores the SQL statement to insert clients data into the database.
     */
    String insertStatementSql = "INSERT INTO customers (name, phone_number, address, email, country, number_range," +
                                 "balance, rfc, tax) VALUES (?,?,?,?,?,?,?,?,?) ";

    /*
    DataBaseConnection constructor receives the clientList
    @param a list of type Client called clientsList.

     */
    public DataBaseConnection (List<Client> clientsList){

        try{
            Class.forName(driver);
            /*
            connection stores the object DriverManager that makes the connection with the database using
            the information of the user.
             */
            connection = DriverManager.getConnection(url, user, pass);
            /*
            If the connection with the database is made successfully, a message will be displayed
             */
            JOptionPane.showMessageDialog(null, "Connected successfully to database");
            /*
            An object of PreparedStatement called 'insertClientData' stores the connection object with the connection's
            information and also sends to the database the insert statement stored in a String variable.
             */
            PreparedStatement insertClientData = connection.prepareStatement(insertStatementSql);
            /*
            for each client object in the clientList received as a parameter...
             */
            for (Client clients : clientsList){
                /*
                the insertClientData object will be executed by every '?' in the statement. This symbols represents
                where the information of the client's Data will be located to be inserted in the database
                 */
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
            /*
            if all the clients within the client's list are successfully inserted into the database a message will be
            shown
             */
            JOptionPane.showMessageDialog(null, "Client List inserted in data base successfully.");
            /*
            insertClientData object closed to close the insertion action.
             */
            insertClientData.close();
            /*
            Connection with the dataBase is closed, as we will not interact more with the database at least with this
            specific action
             */
            connection.close();
            /*
            Calling insertDataFromDataBaseToExcel function.
             */
            DataBaseConnectionToExcel.insertDataFromDataBaseToExcel();

            /*
            If a problem is shown while interacting with the database a message will be displayed
             */
            }catch(SQLException s){
            System.out.println(s.toString());
            }catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
