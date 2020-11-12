package ManageExeptions;

/*
Ошибка неверной авторизации
 */
public class AuthException extends ManageExeption {

    public AuthException(){
        super("Ошибка аутентификации");
    }

    public AuthException(String message) {
        super(message);
    }

}