package tlog16;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import timelogger.exceptions.*;

/**
 *
 * @author Dániel Pallósi
 */
public class WorkDay {
    // ------------------------ Variables --------------------------------
    private List<Task> tasks = new ArrayList<Task>();
    private long requiredMinPerDay = 450;
    private LocalDate actualDay = LocalDate.now();
    private long sumPerDay; //sum of the minPerTask values every day
    // -------------------------------------------------------------------
    
    // ------------------------ Construtors ------------------------------

    /**
     *Normal constructor with exception handling
     * 
     * @param requiredMinPerDay -
     * @param year for date
     * @param month for date
     * @param day for date
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     * @throws FutureWorkException if this WorkDay's date is in the future
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     */
    public WorkDay(long requiredMinPerDay, int year, int month, int day) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException {
        if (LocalDate.of(year, month, day).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        if (requiredMinPerDay < 0)
            throw new NegativeMinutesOfWorkException();
        actualDay = LocalDate.of(year, month, day);
        this.requiredMinPerDay = requiredMinPerDay;
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }
    
    /*
     *
     * @param requiredMinPerDay
     * @throws NegativeMinutesOfWorkException
     * @throws EmptyTimeFieldException
     *
    public WorkDay(long requiredMinPerDay) throws NegativeMinutesOfWorkException, EmptyTimeFieldException{
        if (requiredMinPerDay < 0)
            throw new NegativeMinutesOfWorkException();
        this.requiredMinPerDay = requiredMinPerDay;
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }*/

    /**
     *
     * @param requiredMinPerDay -
     * @param actualDay for date
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     * @throws FutureWorkException if this WorkDay's date is in the future
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     */

    
    public WorkDay(long requiredMinPerDay, String actualDay) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException{
        if (LocalDate.parse(actualDay).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        if (requiredMinPerDay < 0)
            throw new NegativeMinutesOfWorkException();
        this.actualDay = LocalDate.parse(actualDay);
        this.requiredMinPerDay = requiredMinPerDay;
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }
    
    /**
     *
     * @param requiredMinPerDay -
     * @param year for date
     * @param month for date
     * @param day for date 
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     * @throws FutureWorkException if this WorkDay's date is in the future
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     */
    public WorkDay(long requiredMinPerDay, int year, Month month, int day) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException{
        if (requiredMinPerDay < 0)
            throw new NegativeMinutesOfWorkException();
        if (LocalDate.of(year, month, day).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        this.requiredMinPerDay = requiredMinPerDay;
        actualDay = LocalDate.of(year, month, day);
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }
    
    /**
     *
     * @param year for date 
     * @param month for date 
     * @param day for date
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     * @throws FutureWorkException if this WorkDay's date is in the future
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     */
    public WorkDay(int year, int month, int day) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException {
        if (LocalDate.of(year, month, day).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        actualDay = LocalDate.of(year, month, day);
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }
    
    /**
     *
     * @param actualDay for date
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     * @throws FutureWorkException if this WorkDay's date is in the future
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     */
    public WorkDay(String actualDay) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException{
        if (LocalDate.parse(actualDay).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        this.actualDay = LocalDate.parse(actualDay);
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }
    
    /**
     *
     * @param year for date
     * @param month for date
     * @param day for date
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     * @throws FutureWorkException if this WorkDay's date is in the future
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     */
    public WorkDay(int year, Month month, int day) throws NegativeMinutesOfWorkException, FutureWorkException, EmptyTimeFieldException{
        if (LocalDate.of(year, month, day).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        actualDay = LocalDate.of(year, month, day);
        for (Task t : tasks)
            sumPerDay += t.getMinPerTask();
    }
    // --------------------------------------------------------------------
    
    // -------------------------- Getters ---------------------------------

    /**
     *
     * @return long requiredMinPerDay
     */
    public long getRequiredMinPerDay(){
        return requiredMinPerDay;
    }
    
    /**
     *
     * @return long sumPerDay
     */
    public long getSumPerDay(){
        return sumPerDay;
    }
    
    /**
     * 
     * @return LocalDate actualDay
     */
    public LocalDate getActualDay(){
        return actualDay;
    }
    
    /**
     *
     * @return the different of sum of the worked minutes of the days and the required minutes to work
     */
    public long getExtraMinPerDay(){
        return sumPerDay - requiredMinPerDay;
    }
    
    /**
     *
     * @return the list of tasks
     */
    public List<Task> getTasks(){
        return tasks;
    }
    // ---------------------------------------------------------------------
    
    // --------------------------- Methods --------------------------------

    /**
     *
     * @param t will be added to the day's task list if it's separated and takes for x minutes, where x is multiple of the quarter hour
     * @throws EmptyTimeFieldException if any tasks of the workday dont have a time parameter
     * @throws NotSeparatedTimesException if the period of the task t is not a multiple of quarter hour
     * @throws NotExpectedTimeOrderException  if the endTime is less then startTime
     */
    public void addTask(Task t) throws EmptyTimeFieldException, NotSeparatedTimesException, NotExpectedTimeOrderException{
        if (Util.isMultipleQuarterHour(t.getStartTime(), t.getEndTime()))
            if (t.getStartTime().equals(t.getEndTime())){
                tasks.add(t);
            } else if (Util.isSeparatedTime(t, tasks)){
                tasks.add(t);
            } else throw new NotSeparatedTimesException();
    }
    
    /*public LocalTime latestEndTime(){
        LocalTime max = null;
        if (tasks.isEmpty())
            return max;
        else{
            for (Task i : tasks)
                if (i.getEndTime().compareTo(max) == 1) 
                    max = i.getEndTime();
        }
        
        return max;
    }*/
    // -------------------------------------------------------------------
    
    // -------------------------- Setters --------------------------------

    /**
     *
     * @param v the requiredMinPerDay's value
     * @throws NegativeMinutesOfWorkException if the requiredMinPerDay is negative
     */
    public void setRequiredMinPerDay(long v) throws NegativeMinutesOfWorkException{
        if (v<0)
            throw new NegativeMinutesOfWorkException();
        requiredMinPerDay = v;
    }
    
    /**
     *
     * @param year for date
     * @param month for date
     * @param day for date
     * @throws FutureWorkException if the date is in the future
     */
    public void setActualDay(int year, int month, int day) throws FutureWorkException{
        if (LocalDate.of(year, month, day).compareTo(LocalDate.now()) > 0)
            throw new FutureWorkException();
        actualDay = LocalDate.of(year, month, day);
    }
    
    /**
     *
     * @param minPerTask minutes per task increase the day's sumPerDay
     */
    public void setSumPerDay(long minPerTask){
        sumPerDay += minPerTask;
    }
    // ------------------------------------------------------------------
}
