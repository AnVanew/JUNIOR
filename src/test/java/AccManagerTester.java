import java.util.*;

import ManageExeptions.EqualAccException;
import org.junit.Assert;
import org.junit.Test;
import AccManager.AccManager;
import AccManager.AccountManager;
import ManageExeptions.AccException;
import ManageExeptions.AuthException;

/**
 * Класс представляет собой тестирование класса AccManager пакета AccManager
 * <p>
 * Тесты необходимо запускать последовательно.
 * Перед тестированием очистить файл Accounts.csv
 */
public class AccManagerTester {
    AccountManager accManager = new AccManager();

    /**
     * Тестирование метода добавления пользователя в систему.
     * <p>
     * При попытке добавления уже существующего пользователя генерируется исключение
     * В завершении в файл Accounts.csv добавятся записи об аккаунтах
     */
    @Test
    public void addAccTest() throws AccException {
        try {
            accManager.addAccount("UnitOne", "qwerty");
            accManager.addAccount("ClillBro", "cllie12");
            accManager.addAccount("Super", "super");
            accManager.addAccount("Barber100", "password");
            accManager.addAccount("Nikata", "atakiN");
        } catch (AccException | EqualAccException e) {
            throw new AccException();
        }
    }

    /**
     * Тестирование метода удаления пользователя из системы.
     *
     * @throws AccException
     */
    @Test
    public void removeTest() throws AccException {
        accManager.removeAccount("Barber100", "password");
        accManager.removeAccount("Nikata", "atakiN");
        getAllTest();
    }

    /**
     * Тестирование метода получения всех пользователей из системы.
     * <p>
     * Тестирование проходят размеры списков и значения, находящиеся на соответствующих местах.
     * Сравнение производится со списком строк, которые останутся в CSV файле в результате двух предыдущих тестов.
     *
     * @throws AccException
     */
    @Test
    public void getAllTest() throws AccException {
        String[] accArr = new String[]{"UnitOne,qwerty", "ClillBro,cllie12", "Super,super",};
        List<String> testList = accManager.getAllAccounts();
        List<String> trueList = new ArrayList<>(Arrays.asList(accArr));
        Assert.assertEquals(testList.size(), trueList.size());
        for (int i = 0; i < testList.size(); i++) {
            Assert.assertEquals(trueList.get(i), testList.get(i));
        }
    }

    /**
     * Тестирование метода авторизации пользователя.
     * <p>
     * В данном тесте методу autorize() передается два различных пользователя.
     * В первом случае существующий пользователь, а во втором несуществующий.
     * В результате теста будет выведена строка со значение токена первого пользователя
     * и сообщение об отсутствии второго пользователя
     */
    @Test
    public void authorizeTest() {
        try {
            String haveToken = accManager.authorize("UnitOne", "qwerty", new Date());
            System.out.println(haveToken); //выведется значение токена
            String noToken = accManager.authorize("noUser", "noPassword", new Date()); //сгенерируется исключение AuthException
        } catch (AccException e) {
            System.out.println("Ошибка авторизации");
        } catch (AuthException e) {
            System.out.println("Второй пользователь не существует"); //выведется данное сообщение
        }

    }

}
