package tlog16;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.NotExpectedTimeOrderException;

/**
 *
 * @author Dániel Pallósi
 */
public final class Util {
    
    private Util(){
        System.out.println("If u can see this message, you are a programmer");
    }
    
    /**
     * calculates the different between 2 LocalTime parameter
     * @param startTime - 
     * @param endTime -
     * @return as long 
     */
    private static long getMinPerTask(LocalTime startTime, LocalTime endTime){
        return (long) (( endTime.getHour() - startTime.getHour()) * 60
                + endTime.getMinute() - startTime.getMinute());
    }

    /**
     *this method rounds mostly down a task's endTime to make that period a multiple of quarter hour
     * 
     * @param startTime -
     * @param endTime -
     * @return with the changed endTime
     * @throws EmptyTimeFieldException if any time param missing
     * @throws NotExpectedTimeOrderException if endTime less then startTime
     */
    public static LocalTime roundToMultipleQuarterHour(LocalTime startTime, 
            LocalTime endTime) throws EmptyTimeFieldException, NotExpectedTimeOrderException{
        int hour, min, v;
        if (!isMultipleQuarterHour(startTime, endTime)){
            v = (int) getMinPerTask(startTime, endTime);
            while (v%15 != 0)
                v--;
            if (v == 0){
                if (startTime.getMinute() + 15 > 59)
                    endTime = LocalTime.of(startTime.getHour() + 1, startTime.getMinute() - 45);
                else endTime = LocalTime.of(startTime.getHour(), startTime.getMinute() + 15);
            }
            else if (v < 0){
                endTime = LocalTime.of(startTime.getHour() + 1, startTime.getMinute() - v - 60);
            } 
            else{
                hour = v / 60;
                min = v - hour * 60;
                if (startTime.getMinute() + min > 59){
                    hour++;
                    min -= 60;
                }
                endTime = LocalTime.of(startTime.getHour() + hour, startTime.getMinute() + min);
            }
        }
        return endTime;
    }
    
    /**
     *
     * @param startTime -
     * @param endTime -
     * @return true if the period is multiple of quarter hour
     * @throws EmptyTimeFieldException if any time param missing
     * @throws NotExpectedTimeOrderException if endTime less then startTime
     */
    public static boolean isMultipleQuarterHour(LocalTime startTime, LocalTime endTime) throws EmptyTimeFieldException, NotExpectedTimeOrderException{
        if (startTime == null || endTime == null)
            throw new EmptyTimeFieldException();
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
        return getMinPerTask(startTime, endTime)%15 == 0;
    }
    
    /**
     *
     * @param t is the observed task
     * @param tasks list of tasks
     * @return true if t is separated from the other tasks
     */
    public static boolean isSeparatedTime(Task t, List<Task> tasks){
        boolean ret = false;
        if (tasks.isEmpty())
            return true;
        for(Task i: tasks){
            if (i != t){
                if (t.getStartTime().compareTo(i.getEndTime()) >= 0 || t.getEndTime().compareTo(i.getStartTime()) <= 0)
                    ret = true;
                else return false;
            }
        }
        return ret;
    }
    
    /**
     *
     * @param d is the observed workday
     * @return true if d is a weekday, false if not
     */
    public static boolean isWeekday(WorkDay d){
        return (d.getActualDay().getDayOfWeek() == DayOfWeek.SATURDAY 
                || d.getActualDay().getDayOfWeek() == DayOfWeek.SUNDAY);
    }
}
