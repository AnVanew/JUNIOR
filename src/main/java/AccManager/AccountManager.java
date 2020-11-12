package AccManager;

import ManageExeptions.AccException;
import ManageExeptions.AuthException;

import java.util.Date;
import java.util.List;

public interface AccountManager {

    /*
      Метод добавляет нового пользователя системы
     */
    void addAccount(String userName, String password) throws AccException;

    /*
      Метод удаляет пользователя системы
     */
    void removeAccount(String userName, String password) throws AccException;

    /*
      Метод возвращает список всех аккаунтов
     */
    List<String> getAllAccounts() throws AccException;

    /*
      Метод авторизирует пользователя и возвращает Token для доступа методам системы
     */
    String authorize(String userName, String password, Date currentTime) throws AuthException, AccException;

}
