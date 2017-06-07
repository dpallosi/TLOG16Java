/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlog16;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lysharnie
 */
public class WorkMonth {
    // -------------------------------- Variables -------------------------
    private List<WorkDay> days = new ArrayList<WorkDay>(); 
    private YearMonth date;
    private long sumPerMonth; //sum of the sumPerDay values for every day of the month
    private long requiredMinPerMonth; // sum of the requiredMinPerDay values
    // --------------------------------------------------------------------
    
    // -------------------------- Constructors --------------------------
    public WorkMonth(int year, int month){
        date = YearMonth.of(year, month);
    }
    
    public WorkMonth(int year, Month month){
        date = YearMonth.of(year, month);
    }
    // ----------------------------------------------------------------
    
    // ---------------------------- Getters ---------------------------
    public YearMonth getDate(){
        return date;
    }
    
    public long getSumPerMonth(){
        return sumPerMonth;
    }
    
    public long getRequiredMinPerMonth(){
        return requiredMinPerMonth;
    }
    
    public long getExtraMinPerMonth(){
        long sum=0;
        for (WorkDay d: days){
            sum += d.getExtraMinPerDay();
        }
        return sum;
    }
    // --------------------------------------------------------------
    
    // -------------------------- Methods -------------------------------
    public boolean isNewDate(WorkDay day){
        boolean ret = false;
        for(WorkDay d: days){
            if (day.getActualDay() == d.getActualDay())
                ret = true;
        }
        return ret;
    }
    
    public boolean isSameMonth(WorkDay day){
        return (day.getActualDay().getMonth() == date.getMonth());
    }
    
    public void addWorkDay(WorkDay wd, boolean isWeekendEnabled){
        boolean exist  = false;
        for (WorkDay d : days){
            if (wd.getActualDay().equals(d.getActualDay()))
                exist = true;
        }
        if (!exist && isSameMonth(wd)){
            if (isWeekendEnabled){
                days.add(wd);
                sumPerMonth += wd.getSumPerDay();
            }
            else if (!Util.isWeekday(wd)){
                days.add(wd);
                sumPerMonth += wd.getSumPerDay();
            }
        }
    }
    
    public void addWorkDay(WorkDay wd){
        addWorkDay(wd, false);
    }
    // -------------------------------------------------------------------
}
