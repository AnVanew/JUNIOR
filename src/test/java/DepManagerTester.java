import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ManageExeptions.EqualAccException;
import org.junit.Assert;
import org.junit.Test;
import AccManager.AccountManager;
import AccManager.AccManager;
import DataOfBank.Client;
import DataOfBank.Deposit;
import DepManager.DepositManager;
import DepManager.DepManager;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;
import ManageExeptions.DepositeException;

/**
 * Класс представляет собой тестирование класса DepManager пакета DepManager
 *
 * Тесты необходимо запускать последовательно.
 * Перед тестированием очистить файл Accounts.csv
 */
public class DepManagerTester {

    AccountManager accManager = new AccManager();
    DepositManager depManager = new DepManager();

    Client Irina = new Client("Irina","Vturina","4587239854");
    Date IDate = new Date( 1573716754375L );
    Date IDate2 = new Date( 1573716760000L);
    Client Jon=new Client("Jon","Smit","9845678745");
    Date JDate = new Date( 1573716750000L );
    Client Mary=new Client("Mary","Necheeva","2398674343");
    Date MDate = new Date( 1573716754000L);

    /**
     * Тестирование метода добавления в систему нового депозита.
     *
     * Для начала добавляются новые пользователи, которым даются токены для проверки доступа к методу.
     * Имеется еще один токен созданный 31 минуту назад.
     * В завершении в файл Client.csv добавятся 3 записи о новых депозитах.
     * @throws AuthException
     * @throws AccException
     */
    @Test
    public void addDepTest() throws AuthException, AccException, EqualAccException {
        accManager.addAccount("UnitOne","qwerty");
        accManager.addAccount("JoJon","12345");
        accManager.addAccount("BdMr","123657");
        String IToken = accManager.authorize("JoJon","12345", new Date()); //Токен создан только что
        String JToken = accManager.authorize("UnitOne", "qwerty", new Date()); //Токен создан только что
        String MToken = accManager.authorize("BdMr","123657", new Date()); //Токен создан только что
        String iraTokenBreak=accManager.authorize("UnitOne", "qwerty", new Date(new Date().getTime() - (long)(1000*60*31))); //Токен был создан 31 минуту назад
        try {
            depManager.addDeposit(Irina, 100000, 0.5, 0.3, 364, IDate, true, IToken);
            depManager.addDeposit(Irina, 323000, 0.47, 0.38, 365, IDate2, false, IToken);
            depManager.addDeposit(Jon, 24000, 0.65, 0.2, 365, JDate, false, JToken);
            depManager.addDeposit(Mary, 132000, 0.4, 0.4, 1000, MDate, true, MToken);
        } catch (DepositeException e) {
            System.out.println("Сообщение не выводится");
        }
        try {
            depManager.addDeposit(Irina, 100000, 0.5, 0.3, 364, new Date(), true, iraTokenBreak);
        } catch (DepositeException e) {
            System.out.println("Время жизни токена вышло.");
        }
        accManager.removeAccount("UnitOne","qwerty");
        accManager.removeAccount("JoJon","12345");
        accManager.removeAccount("BdMr","123657");
    }

    /**
     * Тестирование метода получения всех пользователей из системы.
     *
     * Для прохождения авторизации создается тестовй пользователь и ему присваивается актуальный токен.
     * Тестирование проходят размеры списков и значения, находящиеся на соответствующих местах.
     * Сравнение производится со списком депозитов, которые останутся в CSV файле в результате предыдущего теста.
     * @throws AccException
     * @throws AuthException
     */
    @Test
    public void getClDepTest() throws AccException, AuthException, EqualAccException {
        accManager.addAccount("UnitOne","qwerty");
        String IToken = accManager.authorize("UnitOne","qwerty", new Date()); //Токен создан только что
        Deposit dep1 = new Deposit(Irina, 100000, 0.5, 0.3, 364, IDate, true);
        Deposit dep2 = new Deposit(Irina, 323000, 0.47, 0.38, 365, IDate2, false);
        List <Deposit> trueList = new ArrayList<>(Arrays.asList(dep1, dep2));
        List <Deposit> testList = new ArrayList<>();
        try {
            testList = depManager.getClientDeposits(Irina, IToken);
        } catch (DepositeException e){
            System.out.println("Сообщение не выводится");
        }
        Assert.assertEquals(testList.size(),trueList.size());
        for (int i = 0; i < testList.size(); i++){
            Assert.assertEquals(trueList.get(i),testList.get(i));
        }
        accManager.removeAccount("UnitOne","qwerty");
    }

    /**
     * Тестирование метода получения дохода от вклада
     *
     * Для прохождения авторизации создается тестовый пользователь и ему присваивается актуальный токен.
     * Расчет производился по формулам, указанным в комментариях в методам GetSimpleSum и GetProgressSum.
     * @throws AuthException
     * @throws AccException
     */
    @Test
    public void gerEarningsTest() throws AuthException, AccException, EqualAccException {
        accManager.addAccount("UnitOne","qwerty");
        String IToken = accManager.authorize("UnitOne","qwerty", new Date()); //Токен создан только что
        Deposit dep1 = new Deposit(Irina, 100000, 0.5, 0.3, 364, IDate, true);
        Deposit dep2 = new Deposit(Irina, 323000, 0.47, 0.38, 365, IDate2, false);
        try {
            Assert.assertEquals(63577.5,depManager.getEarnings(dep1,new Date(),IToken), 0.05);
            Assert.assertEquals(152694.1,depManager.getEarnings(dep2,new Date(),IToken), 0.05);
        } catch (DepositeException e){
            System.out.println("Сообщение не выводится");
        }
        accManager.removeAccount("UnitOne","qwerty");
    }

    /**
     * Тестируется метод удаления депозита из системы
     *
     * Для начала добавляются новые пользователи, которым даются токены для проверки доступа к методу.
     * Расчет производился по формулам, указанным в комментариях в методам GetSimpleSum и GetProgressSum,
     * к которым прибавлялись суммы начальных вложений.
     * В результате из файла Client.csv удалятся строки с данными депозитами.
     * @throws AuthException
     * @throws AccException
     */
    @Test
    public void removeTest() throws AuthException, AccException, EqualAccException {
        accManager.addAccount("JoJon","12345");
        accManager.addAccount("BdMr","123657");
        String JToken = accManager.authorize("JoJon", "12345", new Date()); //Токен создан только что
        String MToken = accManager.authorize("BdMr","123657", new Date()); //Токен создан только что
        Deposit JDep = new Deposit(Jon, 24000, 0.65, 0.2, 365, JDate, false);
        Deposit MDep = new Deposit(Mary, 132000, 0.4, 0.4, 1000, MDate, true);
        try {
            Assert.assertEquals(39690.85, depManager.removeDeposit(JDep, new Date(), JToken), 0.5);
            Assert.assertEquals(196011.35, depManager.removeDeposit(MDep, new Date(), MToken), 0.5);
        } catch (DepositeException e){
            System.out.println("Сообщение не выводится");
        }
        accManager.removeAccount("JoJon","12345");
        accManager.removeAccount("BdMr","123657");
    }

    /**
     * Тестирование метода получения всех депозитов из системы.
     *
     * Тестирование проходят размеры списков и значения, находящиеся на соответствующих местах.
     * Сравнение производится со списком строк, которые останутся в CSV файле в результате всех предыдущих тестов.
     * @throws AuthException
     * @throws AccException
     */
    @Test
    public void getAllDepTest() throws AuthException, AccException {
        Deposit dep1 = new Deposit(Irina, 100000, 0.5, 0.3, 364, IDate, true);
        Deposit dep2 = new Deposit(Irina, 323000, 0.47, 0.38, 365, IDate2, false);
        List <Deposit> trueList = new ArrayList<>(Arrays.asList(dep1, dep2));
        List <Deposit> testList = new ArrayList<>();
        try {
            testList = depManager.getAllDeposits();
        } catch (DepositeException e){
            System.out.println("Сообщение не выводится");
        }
        Assert.assertEquals(testList.size(),trueList.size());
        for (int i = 0; i < testList.size(); i++){
            Assert.assertEquals(trueList.get(i),testList.get(i));
        }
    }

}
