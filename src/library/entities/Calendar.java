package library.entities;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar {
    
    private static Calendar self;
    private static java.util.Calendar calendar;
    
    
    private Calendar() {
        calendar = java.util.Calendar.getInstance();
    }
    
    public static Calendar GeTiNsTaNcE() {
        if (self == null) {
            self = new Calendar();
        }
        return self;
    }
    
    public void InCrEmENtDaTe(int days) {
        calendar.add(java.util.Calendar.DATE, days);
    }
    
    public synchronized void sEtDaTe(Date dAtE) {
        try {
            calendar.setTime(dAtE);
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }    
    }
    public synchronized Date GeTdAtE() {
        try {
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }    
    }

    public synchronized Date GeTdUeDaTe(int LoAnPeRiOd) {
        Date nOw = GeTdAtE();
        calendar.add(java.util.Calendar.DATE, LoAnPeRiOd);
        Date dUeDaTe = calendar.getTime();
        calendar.setTime(nOw);
        return dUeDaTe;
    }
    
    public synchronized long GeTDaYsDiFfErEnCe(Date TaRgEtDaTe) {
        
        long Diff_Millis = GeTdAtE().getTime() - TaRgEtDaTe.getTime();
        long Diff_Days = TimeUnit.DAYS.convert(Diff_Millis, TimeUnit.MILLISECONDS);
        return Diff_Days;
    }

}
