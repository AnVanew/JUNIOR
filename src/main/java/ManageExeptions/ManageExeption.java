package ManageExeptions;

public abstract class ManageExeption extends Exception{

    ManageExeption (){
        super("Ошибка менеджмента");
    }

    ManageExeption (String message){
        System.err.println(message);
    }

}





