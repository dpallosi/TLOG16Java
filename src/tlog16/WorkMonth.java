package tlog16;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import timelogger.exceptions.NotNewDateException;
import timelogger.exceptions.NotTheSameMonthException;
import timelogger.exceptions.WeekendNotEnabledException;

/**
 *
 * @author Dániel Pallósi
 */
public class WorkMonth {
    // -------------------------------- Variables -------------------------
    private List<WorkDay> days = new ArrayList<WorkDay>(); 
    private YearMonth date;
    private long sumPerMonth; //sum of the sumPerDay values for every day of the month
    private long requiredMinPerMonth; // sum of the requiredMinPerDay values
    // --------------------------------------------------------------------
    
    // -------------------------- Constructors --------------------------

    /**
     *
     * @param year for date
     * @param month for date
     */
    public WorkMonth(int year, int month){
        date = YearMonth.of(year, month);
    }
    
    /**
     *
     * @param year for date
     * @param month for date
     */
    public WorkMonth(int year, Month month){
        date = YearMonth.of(year, month);
    }
    // ----------------------------------------------------------------
    
    // ---------------------------- Getters ---------------------------

    /**
     *
     * @return with the date
     */
    public YearMonth getDate(){
        return date;
    }
    
    /**
     *
     * @return long sum of worked minutes per of this month
     */
    public long getSumPerMonth(){
        return sumPerMonth;
    }
    
    /**
     *
     * @return long required minutes to work of this month
     */
    public long getRequiredMinPerMonth(){
        return requiredMinPerMonth;
    }
    
    /**
     *
     * @return the extra worked minutes of the month. can be negative
     */
    public long getExtraMinPerMonth(){
        long sum=0;
        for (WorkDay d: days){
            sum += d.getExtraMinPerDay();
        }
        return sum;
    }
    
    /**
     *
     * @return the days list of this month
     */
    public List<WorkDay> getDays(){
        return days;
    }
    // --------------------------------------------------------------
    
    // -------------------------- Methods -------------------------------

    /**
     *
     * @param day is a new date if there isn't any day what has the same ActualDay paramater
     * @return true if day is a new day in this month
     */
    public boolean isNewDate(WorkDay day){
        boolean ret = true;
        for(WorkDay d: days){
            if (day.getActualDay().equals(d.getActualDay()))
                ret = false;
        }
        return ret;
    }
    
    /**
     *
     * @param day is in this workMonth or not
     * @return true if the day is in this year and month
     */
    public boolean isSameMonth(WorkDay day){
        return (day.getActualDay().getYear() == date.getYear() &&
                day.getActualDay().getMonth() == date.getMonth());
    }
    
    /**
     *
     * @param wd wanna be a new workDay in this month
     * @param isWeekendEnabled : if it's false, and the day is a weekday, that day won't be add to the month's day list
     * @throws WeekendNotEnabledException if isWeekendEnabled is false
     * @throws NotNewDateException if the day already exists
     * @throws NotTheSameMonthException if the day isn't in this month
     */
    public void addWorkDay(WorkDay wd, boolean isWeekendEnabled) throws WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
        if (isNewDate(wd)){
            if (isSameMonth(wd)){
                if (isWeekendEnabled){
                    days.add(wd);
                    requiredMinPerMonth += wd.getRequiredMinPerDay();
                }
                else if (!Util.isWeekday(wd)){
                    days.add(wd);
                    requiredMinPerMonth += wd.getRequiredMinPerDay();
                } else throw new WeekendNotEnabledException();
            } else throw new NotTheSameMonthException();
        } else throw new NotNewDateException();
    }
    
    /**
     * Overloaded method, because the isWeekendEnabled paramater should be false as default
     *
     * @param wd wanna be the new workDay in this month
     * @throws WeekendNotEnabledException if isWeekendEnabled is false
     * @throws NotNewDateException if the day already exists
     * @throws NotTheSameMonthException if the day isn't in this month
     */
    public void addWorkDay(WorkDay wd) throws WeekendNotEnabledException, NotNewDateException, NotTheSameMonthException{
        addWorkDay(wd, false);
    }
    // -------------------------------------------------------------------
    
    // ------------------------------ Setters ----------------------------

    /**
     *
     * @param minPerTask minutes per task increase the month's sumPerMonth
     */
    public void setSumPerMonth(long minPerTask){
        sumPerMonth += minPerTask;
    }
}
