package tlog16;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lysharnie
 */
public class WorkDay {
    // ------------------------ Variables --------------------------------
    private List<Task> tasks = new ArrayList<Task>();
    private long requiredMinPerDay = 450;
    private LocalDate actualDay = LocalDate.now();
    private long sumPerDay; //sum of the minPerTask values every day
    // -------------------------------------------------------------------
    
    // ------------------------ Construtors ------------------------------
    public WorkDay(long requiredMinPerDay, int year, int month, int day) {
        actualDay = LocalDate.of(year, month, day);
        this.requiredMinPerDay = requiredMinPerDay;
    }
    
    public WorkDay(long requiredMinPerDay){
        this.requiredMinPerDay = requiredMinPerDay;
    }
    
    public WorkDay(long requiredMinPerDay, String actualDay){
        this.actualDay = LocalDate.parse(actualDay);
    }
    
    public WorkDay(long requiredMinPerDay, int year, Month month, int day){
        this.requiredMinPerDay = requiredMinPerDay;
        actualDay = LocalDate.of(year, month, day);
    }
    // --------------------------------------------------------------------
    
    // -------------------------- Getters ---------------------------------
    public long getRequiredMinPerDay(){
        return requiredMinPerDay;
    }
    
    public long getSumPerDay(){
        return sumPerDay;
    }
    
    public LocalDate getActualDay(){
        return actualDay;
    }
    
    public long getExtraMinPerDay(){
        return sumPerDay - requiredMinPerDay;
    }
    
    public List<Task> getTasks(){
        return tasks;
    }
    // ---------------------------------------------------------------------
    
    // --------------------------- Methods --------------------------------
    public void addTask(Task t){
        if (tasks.isEmpty()){
            tasks.add(t);
            sumPerDay += t.getMinPerTask();
        } else
            if (Util.isMultipleQuarterHour(t.getStartTime(), t.getEndTime())
                    && Util.isSeparatedTime(t, tasks)){
                tasks.add(t);
                sumPerDay += t.getMinPerTask();
            }
    }
    // -------------------------------------------------------------------
    
    // -------------------------- Setters --------------------------------
    public void setRequiredMinPerDay(long v){
        requiredMinPerDay = v;
    }
    
    public void setActualDay(int year, int month, int day){
        actualDay = LocalDate.of(year, month, day);
    }
    // ------------------------------------------------------------------
}
