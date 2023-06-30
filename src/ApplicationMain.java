
import java.io.IOException;
import processor.FileProcessorService;
/*
This is the main executor of all the program.
This programs extract information from a txt file, process it and then sends it into a Database.
Once the information is within the database, the information is process it again to put it into an Excel file.
If the creation of the Excel file is successful, then an email process is made.

@author Anette Larios
@since 21.06.2023
@version 1.8.0
 */
public class ApplicationMain {
    /*
    The main function executes a try-catch while the program is trying to localize the file, if the program
    does not localize it, an exception will be shown.

    @author Anette Larios
    @since 20.06.2023
     */
    public static void main(String[] args) {
       try {
           FileProcessorService.
                   fileOpenAndRead("C:\\Users\\SpectrumByte\\Documents\\CódigosPaola\\" +
                                                        "Java-TXTFileProcessorWithMySQLandEXCEL\\src\\Resources\\" +
                                                        "UsersData.txt");
       } catch (Exception e){
           System.out.println(e.toString());
       }
    }
}