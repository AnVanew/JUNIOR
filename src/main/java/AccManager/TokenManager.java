package AccManager;

/*
Создание класса Singleton
*/

import ManageExeptions.NoTokenExeption;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class TokenManager {

    private static final TokenManager  singleton=new TokenManager ();

    private  TokenManager (){}

    public static TokenManager  getInstance() {
        return singleton;
    }

    /*
    Токены хранятся в Map, где ключем является сам токен, а значением является время его создания
     */
    private Map <String, Date>  tokenStorage=new HashMap();


    /*
    Мето реализует элементарную генерацию токена.
     */
    private String GenerateToken(){
        StringBuilder str=new StringBuilder();
        for (int i=0;i<4;i++) {
            str.append((int) (Math.random() * 270));
        }
        return str.toString();
    }


    /*
    Метод реализующий размещение токена в TokenStorage
     */
     String setToken(Date currentTime){

        String token= GenerateToken();
        tokenStorage.put(token,currentTime);
        return token;

    }

    /*
    Проверяется необходимое условие токена, переданного в качестве параметра.
    Необходимое условие валидации токена: время существования менее 30 мин.
    Время измеряется с точностью до миллисекунд.

    При отсутствии токена выбрасывается исключение NoTokenException
    */
    public boolean checkToken(String token) throws NoTokenExeption{

        Date currentDate=new Date();
        Date sessionDate=tokenStorage.get(token);
        if (sessionDate==null) throw new NoTokenExeption();
        if(currentDate.getTime()-sessionDate.getTime() <= 30*60*1000)return true;
        tokenStorage.remove(token);
        return false;
    }




}