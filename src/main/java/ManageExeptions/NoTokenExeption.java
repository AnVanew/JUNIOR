package ManageExeptions;

/*
Ошибка отсутствия токена
 */
public class NoTokenExeption extends ManageExeption {

    public NoTokenExeption(){
        super("Токен не найден");
    }

    public NoTokenExeption(String message){
        super(message);
    }

}
