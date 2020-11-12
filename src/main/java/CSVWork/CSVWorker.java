package CSVWork;

import DataOfBank.Deposit;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWorker {

   public static <T> void PrintInCVS(File file, T obj, String[] header) throws CSVException {

        ICsvBeanWriter writer = null;

        try(FileWriter fw=new FileWriter(file,true)) {
            // final CellProcessor[] processors = getProcessors();
            writer = new CsvBeanWriter(fw, CsvPreference.STANDARD_PREFERENCE);

            try {
                writer.write(obj, header);
                writer.close();
            } catch (IOException e) {
                throw new CSVException();
            }
        }
        catch (Exception e){
            throw new CSVException();
        }

    }

}
