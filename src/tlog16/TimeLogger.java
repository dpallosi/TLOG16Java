package tlog16;

import java.util.ArrayList;
import java.util.List;
import timelogger.exceptions.NotNewMonthException;

/**
 *
 * @author Dániel Pallósi
 */
public class TimeLogger {
    // -------------------------- Variables ---------------------------
    private List<WorkMonth> months = new ArrayList<WorkMonth>();
    private static TimeLogger instance = null;
    // ----------------------------------------------------------------
    
    // ---------------------------- Constructors ----------------------

    /**
     * singleton class, it contains the list of the workmonths
     */
    private TimeLogger(){}
    public static TimeLogger getInstance(){
        if (instance == null)
            instance = new TimeLogger();
        return instance;
    }
    
    // ----------------------------------------------------------------
    
    // --------------------------- Methods ------------------------------

    /**
     *
     * @param wm is a new month or not
     * @return true if wm is a new month
     */
    public boolean isNewMonth(WorkMonth wm){
        boolean exist = false;
        for (WorkMonth m : months){
            if (m.getDate().equals(wm.getDate()))
                exist = true;
        }
        return !exist;
    }
    
    /**
     *
     * @param wm will be added to the workmonth list if it's new
     * @throws NotNewMonthException if wm is already exists
     */
    public void addMonth(WorkMonth wm) throws NotNewMonthException{
        if (isNewMonth(wm))
            months.add(wm);
        else throw new NotNewMonthException();
    }
    // --------------------------------------------------------------
    
    // --------------------------- Getters --------------------------

    /**
     *
     * @return the list of the workMonths
     */
    public List<WorkMonth> getMonths(){
        return months;
    }
    // --------------------------------------------------------------
}
