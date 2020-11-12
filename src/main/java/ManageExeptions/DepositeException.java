package ManageExeptions;

public class DepositeException extends ManageExeption{

    public DepositeException(){
        super();
    }

    public DepositeException(String message){
        System.err.println(message);
    }

}
