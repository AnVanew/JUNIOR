package ManageExeptions;

public class AccException extends ManageExeption {

    public AccException(){
        super("Ошибка аутентификации");
    }

    public AccException(String message) {
        super(message);
    }

}
