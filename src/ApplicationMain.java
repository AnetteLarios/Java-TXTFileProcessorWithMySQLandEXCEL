import java.io.IOException;
import processor.FileProcessorService;
public class ApplicationMain {
    public static void main(String[] args) {
       try {
           FileProcessorService.fileOpenAndRead("C:\\Users\\SpectrumByte\\Documents\\CódigosPaola\\" +
                                                        "Java-TXTFileProcessorWithMySQLandEXCEL\\src\\Resources\\" +
                                                        "UsersData.txt");
       } catch (Exception e){
           System.out.println(e.toString());
       }
    }
}