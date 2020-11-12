package DepManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CountYear {

    public static int LeapYears(Date start, Date finish){

        Calendar Start=new GregorianCalendar();
        Calendar Finish=new GregorianCalendar();
        Start.setTime(start);
        Finish.setTime(finish);
        int count=0;

        for(int i=Start.get(Calendar.YEAR);i<=Finish.get(Calendar.YEAR);i++){
            if (i%4==0 && i%400!=0)count++;
        }
        return count;
    }

    public static int SimpleYears(Date start, Date finish){

        Calendar Start=new GregorianCalendar();
        Calendar Finish=new GregorianCalendar();
        Start.setTime(start);
        Finish.setTime(finish);

        return Finish.get(Calendar.YEAR)-Start.get(Calendar.YEAR)+1-LeapYears(start,finish);
    }

}
