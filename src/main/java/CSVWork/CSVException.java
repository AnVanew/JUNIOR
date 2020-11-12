package CSVWork;

public class CSVException extends Exception{

    public CSVException(){
        super();
    }

    public CSVException(String message){
        System.err.println(message);
    }

}
