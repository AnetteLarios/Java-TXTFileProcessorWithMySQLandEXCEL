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


public class FileProcessorService {



    public static void fileOpenAndRead(String filePath) throws IOException{
         BufferedReader linePerLineReader;

         try{

             linePerLineReader = new BufferedReader(new FileReader(filePath));

             JOptionPane.showMessageDialog(null, "Txt File Opened Successfully");

             DataBaseConnection connectToDataBase = new DataBaseConnection(fileReaderProcessor(linePerLineReader));

         }catch(IOException e){
             System.out.println(e.toString());
         }
    }

    public static List<Client> fileReaderProcessor(BufferedReader linePerLineReader)throws IOException{

        List<Client> clientList = new ArrayList<>();

        String line;

        String [] dataFromFile;

        float tax = 0;

        while((line = linePerLineReader.readLine()) != null){
            dataFromFile = line.split(",");

            printClientsFromTxtFile(dataFromFile);

            float numberRange = StringsUtils.convertStringToFloat(dataFromFile[5]);
            float balance = convertStringToFloatAndRemoveSpecialCharacter(dataFromFile[6]);
            tax = MathUtils.taxCalculator(numberRange, balance);



        }
        linePerLineReader.close();

        return clientList;
    }

    public static void printClientsFromTxtFile(String [] dataFromFile){
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
