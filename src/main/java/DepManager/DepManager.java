package DepManager;

import AccManager.TokenManager;
import CSVWork.CSVException;
import CSVWork.CSVWorker;
import DataOfBank.*;


import ManageExeptions.DepositeException;
import ManageExeptions.NoTokenExeption;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;


public class DepManager implements DepositManager{

    //Был статик
    private CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull(),
        };
    }
    String FileName="Clients.csv";
    String[] headerDeposit=new String[]{"Passport", "FirstName", "LastName" , "Ammount", "Percent", "PretermPercent", "TermDays", "StartDate", "WithPercentCapitalization"};
    File file=new File(FileName);


    @Override
    public Deposit addDeposit(Client client, double ammount, double percent, double pretermPercent, int termDays, Date startDate, boolean withPercentCapitalization, String token) throws DepositeException {

        try {
            TokenManager.getInstance().checkToken(token);
        }
        catch (NoTokenExeption e){
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }

       Deposit deposit= new Deposit(client,ammount,percent,pretermPercent,termDays,startDate,withPercentCapitalization);

       try {
           CSVWorker.PrintInCVS(file, deposit,headerDeposit);
       }
       catch (CSVException e){
           System.out.println("Ошибка создания депозита. Повторите попытку. ");
           return null;
       }


        return deposit;

    }

    @Override
    public List<Deposit> getClientDeposits(Client client, String token) throws DepositeException {

        try {
            TokenManager.getInstance().checkToken(token);
        }
        catch (NoTokenExeption e){
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }

        List<Deposit> Deposits=new ArrayList<Deposit>();
        String ClientPassport=client.getPassport();


        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(FileName),CsvPreference.STANDARD_PREFERENCE);){
            ReadDepositeRow row;
            final CellProcessor[] processors = getProcessors();

            while ((row=reader.read(ReadDepositeRow.class,headerDeposit,processors))!=null){

                if(row.getPassport().equals(ClientPassport)){
                        Deposits.add(CreareRowDeposit(row));
                }

            }

        }
        catch (IOException e){
            System.out.println(e);
        }


        return Deposits;
    }

    @Override
    public List<Deposit> getAllDeposits() {

        List<Deposit> Deposits=new ArrayList<Deposit>();

        try (ICsvBeanReader reader = new CsvBeanReader(new FileReader(FileName),CsvPreference.STANDARD_PREFERENCE);){

            ReadDepositeRow row;
            final CellProcessor[] processors = getProcessors();

            while ((row=reader.read(ReadDepositeRow.class,headerDeposit,processors))!=null){

                    Deposits.add(CreareRowDeposit(row));

                }
            }
        catch (IOException e){
            System.out.println(e);
        }

        return Deposits;

    }

    /*
    Следует учитывать, что вклад может быть с выплатой прибыли по процентам
    или же процент по вкладу может добавляться к сумме вклада.
    За это отвечает поле withPercentCapitalization класса Deposit
    */
    @Override
    public double getEarnings(Deposit deposit, Date currentDate, String token) throws DepositeException {

        try {
            TokenManager.getInstance().checkToken(token);
        }
        catch (NoTokenExeption e){
            System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
            throw new DepositeException();
        }

            if(!deposit.isWithPercentCapitalization()){
                return GetSimpleSum(currentDate, deposit, deposit.getPercent());
            }
            else {
                return GetProgressSum(currentDate, deposit, deposit.getPercent());
            }

    }

    /*
    Вклад считатется завершенным в срок, если он закрыт в срок удерржания.
    При этом последний день вклада не учитывается.
    TermDays является полем, хранящее в себе количество дней действия вклада.
     */
    @Override
    public double removeDeposit(Deposit deposit, Date closeDate, String token) throws DepositeException {

    try {
        TokenManager.getInstance().checkToken(token);
    }
    catch (NoTokenExeption e){
        System.out.println("Операция сейчас не доступна. Выполните повторную авторизацию");
        throw new DepositeException();
    }

        File tempFile=new File("temp.csv");

        List<Deposit> NewAllDep=getAllDeposits();

        boolean flag= NewAllDep.remove(deposit);

        if(!flag){
            System.out.println("Депозит отсутствует");
            throw  new DepositeException();
            }

        try {
            tempFile.createNewFile();
        }
        catch (IOException e){
            System.out.println("Удаление невозможно");
            throw new DepositeException();
        }


        try {
    for (Deposit dep : NewAllDep) CSVWorker.PrintInCVS(tempFile, dep, headerDeposit);
}

catch (CSVException e){
    System.out.println("Ошибка записи в систему.");
    tempFile.delete();
    return 0;
}

    file.delete();
    tempFile.renameTo(file);



    if (DaysGone(deposit.getStartDate(), closeDate) < deposit.getTermDays()) {
        System.out.println("Срок не вышел, прошло "+DaysGone(deposit.getStartDate(), closeDate)+" дней, а срок "+deposit.getTermDays());
        if (deposit.isWithPercentCapitalization())
            return Math.ceil((deposit.getAmmount() + GetProgressSum(closeDate, deposit, deposit.getPercent()))*100)/100;
        else return Math.ceil((deposit.getAmmount() + GetSimpleSum(closeDate, deposit, deposit.getPercent()))*100)/100;
    } else {
        System.out.println("Срок вышел");
        if (deposit.isWithPercentCapitalization())
            return Math.ceil((deposit.getAmmount() + GetProgressSum(closeDate, deposit, deposit.getPretermPercent()))*100)/100;
        else return Math.ceil((deposit.getAmmount() + GetSimpleSum(closeDate, deposit, deposit.getPretermPercent()))*100)/100;
    }



    }





    /*
    withPercentCapitalization is FALSE

    Если процент по вкладу выплачивается, то прибыль до определенного момента можно вычислить по формуле
    Sп=(Sв*%*Nд)/Nг, где
    Sп – размер компенсации по вкладу
    Sв – размер депозита
    % — размер процентной ставки
    Nд – количество дней , за которые банком начисляются проценты
    Nг – количество дней в календарном году

    Схема начислений:
    Процентная ставка является годовой.
    Процент начинает начислятся с момента открытия, то есть процент за первый год будет не целым.
    Процент за год на момент запроса так же не будет целым.

    Чтобы посчитать прибыль по вкладу при неокончании срока
    необхоимо узнать прибыли за первым и последний годы вклада
    и, еси имеются, за промежуточные года.

     */
    private double GetSimpleSum(Date currentDate, Deposit deposit,double percent){

        GregorianCalendar CurrentDate=new GregorianCalendar();
        GregorianCalendar DepositeDate= new GregorianCalendar();
        CurrentDate.setTime(currentDate);
        DepositeDate.setTime(deposit.getStartDate());

        int FYDays,AllFYDays, AllLYDays=0;
        int LYDays=CurrentDate.get(Calendar.DAY_OF_YEAR);
        double Summary=0;


        /*
        От того, являются ли первый и последний года вклада високосными, зависят:
        общее количество дней в первом и последних годах, количество дней вкладад первого года
         */

        if(CurrentDate.get(Calendar.YEAR)==DepositeDate.get(Calendar.YEAR)) {

            int DaysDeposit=DaysGone(deposit.getStartDate(),currentDate);
            if (DepositeDate.isLeapYear(DepositeDate.get(Calendar.YEAR))) {
                Summary += (deposit.getAmmount() * DaysDeposit * deposit.getPercent()) / 366;
                return Math.ceil(Summary*100)/100;
            }
            else {
                Summary += (deposit.getAmmount() * DaysDeposit * deposit.getPercent()) / 365;
                return Math.ceil(Summary*100)/100;
            }
        }

        if (CurrentDate.isLeapYear(CurrentDate.get(Calendar.YEAR)))AllLYDays=366;
        else AllLYDays=365;

        if(DepositeDate.isLeapYear(DepositeDate.get(Calendar.YEAR))) {
            FYDays=366-DepositeDate.get(Calendar.DAY_OF_YEAR)+1;
            AllFYDays=366;
        }
        else {
            FYDays=365-DepositeDate.get(Calendar.DAY_OF_YEAR)+1;
            AllFYDays=365;
        }

        int LYears =CountYear.LeapYears(deposit.getStartDate(),currentDate);
        int SYears =CountYear.SimpleYears(deposit.getStartDate(),currentDate);

        Summary=deposit.getAmmount()*percent*(CurrentDate.get(Calendar.YEAR)-DepositeDate.get(Calendar.YEAR)-1+(float)FYDays/AllFYDays + (float)LYDays/AllLYDays);


        return Math.ceil(Summary*100)/100;
    }


    /*
    withPercentCapitalization is TRUE

    Если процент по вклау суммируется, то прибыль вычисляется по формуле
    Sп=Sв*(1+%)п-Sв, где
    Sп – сумма дохода по депозиту
    Sв – размер вклада
    n – число периодов капитализации
    % — размер процентной ставки в периоде капитализации

    Схема начислений:
    Каждый прошедший период капитализации процент добавляется к вкладу.
    Если вклад изымается до завершения расчетного периода, то сумма остается той же,
    что и была на момент завершения прошлого рассчетного периода.
    Процент percent является годовым
    Периодизацию будем считать ежемесечной.

    Для вычисления дохада используется эффективная процентная ставка, вычисляемая по формуле
    ( (1+ Процентная Ставка/12)^T -1)*12/T, где
    Т - срок размещения вклаа в месяцах.
    При помощи данной ставки можно посчитать прибыль с использованием метода
    нахождения прибыли без капитаизации процентов.

     */
    private double GetProgressSum(Date currentDate, Deposit deposit,double percent){

        GregorianCalendar CurrentDate=new GregorianCalendar();
        GregorianCalendar DepositeDate= new GregorianCalendar();
        CurrentDate.setTime(currentDate);
        DepositeDate.setTime(deposit.getStartDate());

        int mounthDeposit=(int) ( (CurrentDate.getTimeInMillis()-DepositeDate.getTimeInMillis()) / ((long)1000*60*60*24*30) );

        percent=( Math.pow( (1+percent/12),mounthDeposit)-1 )*12 / mounthDeposit;

        return GetSimpleSum(currentDate,deposit,percent);

    }


    private int DaysGone(Date start, Date finish){
        return (int) ( (finish.getTime()-start.getTime() ) / ( (long) 1000*60*60*24) );
    }

    /*
 Создание депозита из прочитанной CVS строки
  */
    public Deposit CreareRowDeposit(ReadDepositeRow row){

          /*
          Необхоимо преобразовать строку даты из CVS файла в обьект типа Date.
          Для этого парсим строку в соотвествтвующем формате.
          */

        Date ParseDate=null;
        try {
            ParseDate=new SimpleDateFormat("E MMM d hh:mm:ss z yyyy").parse(row.getStartDate());
        }
        catch (ParseException e){}

        return new Deposit(
                new Client(row.getFirstName(), row.getLastName(), row.getPassport()),
                Double.parseDouble(row.getAmmount()),
                Double.parseDouble(row.getPercent()),
                Double.parseDouble(row.getPretermPercent()),
                Integer.parseInt(row.getTermDays()),
                ParseDate,   //т.к используется Date, а не календарь, нужно парсировать с опредленным форматом
                Boolean.parseBoolean(row.getWithPercentCapitalization())
        );
    }

}
