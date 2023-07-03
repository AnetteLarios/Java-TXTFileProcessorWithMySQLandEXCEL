package processor;

import java.io.IOException;

import java.io.FileReader;

import java.io.BufferedReader;

import javax.swing.JOptionPane;

import utils.StringsUtils;

import models.Client;

import sql.DataBaseConnection;

import utils.MathUtils;

import java.util.ArrayList;

import java.util.List;

import services.emailSenderGmail;
/*
FileProcessorService class contains the methods in charge to process and display the clients data
extracted from TXT File. Once all this information is processed, creates a list that stores this data and
tries to send this information to the dataBaseConnection function.
@Author Anette Larios
@since 20.06.2023
 */public class FileProcessorService {

    /*
    fileOpenAndRead creates an object to read line per line the txt file and calls the function
    'fileReaderProcessor' sending reference the bufferedReader object called "linePerlineReader".
    @param filePath is a String that stores the localization of the TXT file, this file contains customers' data
    that is going to be processed.
    @author Anette Larios
    @since 20.06.2023
     */

    public static void fileOpenAndRead(String filePath) throws IOException{
         BufferedReader linePerLineReader;

         try{
             /*
             Creates a BufferedReader object and send as parameter the filepath of the TXT file
              */
             linePerLineReader = new BufferedReader(new FileReader(filePath));
             /*
             If this action is executed successfully, a message will be shown.
              */
             JOptionPane.showMessageDialog(null, "Txt File Opened Successfully");
             /*
             Trying to make a connection with dataBase sending as parameter the bufferedReader object.
              */
             DataBaseConnection connectToDataBase = new DataBaseConnection(fileReaderProcessor(linePerLineReader));
         /*
         If a problem is shown while trying to open the txt file, a message will be displayed.
          */
         }catch(IOException e){
             System.out.println(e.toString());
         }
    }
    /*
    fileReaderProcessor receives BufferedReader that contains the TXT FilePath
    @param BufferedReader linePerLineReader is a bufferedReader object that contains the filePath of the TXT file
    @return Returns a list of Client objects
    @author Anette Larios
    @since 20.06.2023
     */
    public static List<Client> fileReaderProcessor(BufferedReader linePerLineReader)throws IOException{
        //Declaring a list of Client called Client list. This list will have the behavior of an ArrayList
        List<Client> clientList = new ArrayList<>();
        //Declaring a String called line in charge to store the lines of the TXT file
        String line;
        //Declaring an array of Strings called "dataFromFile" which will store the processed clients' data.
        String [] dataFromFile;
        //Declaring a variable float initialized in 0, used to make calculation later according to customers data.
        float tax = 0;

        /*
        While there is more lines in the txt file to read, line will be equal to and object in charge to read
        every single line in the file and will store it
         */
        while((line = linePerLineReader.readLine()) != null){
            /*
            dataFromFile will store every single line in the file, while reading the line will divide the information
            if a "," is found, every division will be stored in a position of the array.
             */
            dataFromFile = line.split(",");
            /*
            Calling function 'printClientsFronTXTFile' and sending as a parameter dataFromFile.
             */
            printClientsFromTxtFile(dataFromFile);
            /*
            Declaring a float called 'numberRange' that will store the return of the 'convertStringToFloat' function
            that receives as a parameter the String stored in the position five of the array
            The data that is stored in this position is the numberRange, but as it is read as a String,
            it needs to be converted to float with the purpose to make the calculation of the tax based on clients'
            data.
             */
            float numberRange = StringsUtils.convertStringToFloat(dataFromFile[5]);
            /*
            Declaring a float variable called 'balance' that will store the return of the function
            'convertStringToFloatAndRemoveSpecialCharacter' which receives as a parameter the String stored in
            position 6 of the array. The String stored in this position is the balance, but as it is read like
            a String, it needs to be converted to float with the purpose to make the calculation of the tax based on
            each client information.
             */
            float balance = convertStringToFloatAndRemoveSpecialCharacter(dataFromFile[6]);
            /*
            Float variable tax will store the return of the 'taxCalculator' function that receives as parameters
            The floats numberRange and balance.
             */
            tax = MathUtils.taxCalculator(numberRange, balance);

            /*
            Adding every single client processed to the list.
             */
            clientList.add(new Client(dataFromFile[0],
                                      dataFromFile[1],
                                      dataFromFile[2],
                                      dataFromFile[3],
                                      dataFromFile[4],
                                      Integer.parseInt(dataFromFile[5]),
                                      balance,
                                      dataFromFile[7],
                                      tax));

        }
        /*
        Closing the linePerLineReader object.
         */
        linePerLineReader.close();

        /*
        Once every single client is stored in the list, the function will return the clientList
         */
        return clientList;
    }

    /*
    printClientsFromTXTFile is in charge to print clients data received from the txt file to prove that the file
    was opened successfully and the information is correct
    @param dataFromFile is an array of Strings that contains clients data.
    @author Anette Larios
    @since 20.06.2023
     */
    public static void printClientsFromTxtFile(String [] dataFromFile){
        /*Making one String with all Clients data*/
        String clientData = String.format("Name: %s " +
                                          "Phone number: %s " +
                                          "Address: %s " +
                                          "Email: %s " +
                                          "Country: %s " +
                                          "Number range: %s " +
                                          "Balance: %s " +
                                          "RFC: %s",
                                          dataFromFile[0],
                                          dataFromFile[1],
                                          dataFromFile[2],
                                          dataFromFile[3],
                                          dataFromFile[4],
                                          dataFromFile[5],
                                          dataFromFile[6],
                                          dataFromFile[7]);
        System.out.println(clientData);
    }
    public static float convertStringToFloatAndRemoveSpecialCharacter(String balance){
        balance = balance.replace("$","");
        float balanceFloat = Float.parseFloat(balance);
        return balanceFloat;
    }
}
