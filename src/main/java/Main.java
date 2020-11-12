import AccManager.*;
import DataOfBank.*;
import DepManager.*;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;
import ManageExeptions.DepositeException;
import ManageExeptions.ManageExeption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        DepManager depManager=new DepManager();
        AccManager accManager=new AccManager();
        Date currentDate=new Date();
        long date= (long)currentDate.getTime() - (long)1000*60*60*24*364;




        Client Ivan=new Client("Ivan", "Andreev", "4356783456");
        Date IvanDate=new Date(1573716754375L); //Вкла был оформлен 364 дня назад
        String IvanToken=null;
        Deposit IvanDeposit= new Deposit(Ivan, 100000, 0.05, 0.03, 364, IvanDate, false);

        Client Irina=new Client("Irina","Vturina","4587239854");

        Client Jon=new Client("Jon","Smit","9845678745");

        Client Mary=new Client("Mary","Necheeva","2398674343");

        Client Nik=new Client("Nik","Coll","5656097845");

        Client Ernest=new Client("Ernest","Korgy","43434767676");

        Client Igor=new Client("Igor","VKuzmin","2387560923");


        List<Client> clientsList=new ArrayList<>(Arrays.asList(Ivan, Irina, Jon, Mary, Nik, Ernest, Igor));




        try {
            //accManager.addAccount("IvanEv", "password12");
            IvanToken=accManager.authorize("IvanEv","password12", new Date());
        }
        catch (ManageExeption e){
            System.out.println(e);
        }

        try {
            //depManager.addDeposit(Ivan, 100000,0.05,0.03,364,IvanDate,true, IvanToken);
            //depManager.addDeposit(Jon, 600000,0.07,0.04,730,IvanDate,true, IvanToken);
            System.out.println(depManager.removeDeposit(IvanDeposit, currentDate ,IvanToken));
        }
        catch (DepositeException e){
            System.out.println(e);
        }





    }
}
