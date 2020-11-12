package AccManager;

import CSVWork.CSVException;
import CSVWork.CSVWorker;
import DataOfBank.Account;
import DataOfBank.Deposit;
import DepManager.ReadDepositeRow;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class AccManager implements AccountManager {


    private static CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                new NotNull(),
                new NotNull()
        };
    }

    File file=new File("Accounts.csv");
    String[] AccountHeader=new String[]{"userName", "password"};


    /*
     Метод добавляет нового пользователя системы
    */
    public void addAccount(String userName, String password) throws AccException {

        Account newAcc= new Account(userName,password);

        try {
            CSVWorker.PrintInCVS(file, newAcc, AccountHeader);
        }
        catch (CSVException e){
            throw new AccException("Ошибка добавления в систему");
        }

    }


    /*
      Метод удаляет пользователя системы
     */
    public void removeAccount(String userName, String password) throws AccException {

        boolean flag=false;

        File tempFile=new File("temp.csv");

        List<String> NewAllAcc=getAllAccounts();

        Iterator<String> accIterator = NewAllAcc.iterator();

        while(accIterator.hasNext()) {

            String nextAcc = accIterator.next();

            if (nextAcc.equals(userName+","+password)) {
                accIterator.remove();
                flag=true;
            }
        }

        if(!flag)return;

        try (BufferedWriter bw=new BufferedWriter(new FileWriter(tempFile))){
            for (String acc : NewAllAcc) {
                bw.write(acc);
                bw.write("\n");
            }
        }
        catch (IOException e){
            throw new AccException("Ошибка удаления");
        }

        file.delete();
        tempFile.renameTo(file);

    }


    /*
      Метод возвращает строковый список всех аккаунтов
     */
    public List<String> getAllAccounts() throws AccException {

        List<String> Accounts=new ArrayList<String>();

        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);){

            ReadAccountRow row;
            final CellProcessor[] processors = getProcessors();

            while ((row=reader.read(ReadAccountRow.class,AccountHeader,processors))!=null){

                Accounts.add(row.getUserName()+","+row.getPassword());

            }
        }
        catch (IOException e){
            throw new AccException("Ошибка доступа к системе");
        }

        return Accounts;
    }


    /*
      Метод авторизирует пользователя и возвращает Token для доступа методам системы
     */
    public String authorize(String userName, String password, Date currentTime) throws AuthException, AccException {

        try (BufferedReader bw=new BufferedReader(new FileReader(file))){
            String AccountRow;

            while ((AccountRow=bw.readLine())!=null){

                if (AccountRow.equals(userName+","+password)){
                    return TokenManager.getInstance().setToken(currentTime);
                }
            }
        }
        catch (IOException e){
            throw new AccException("Ошибка доступа к системе");
        }

        throw  new AuthException();
    }

}
