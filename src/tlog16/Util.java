/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlog16;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author lysharnie
 */
public final class Util {
    
    private Util(){
        System.out.println("If u can see this message, you are a programmer");
    }
    
    private static long getMinPerTask(LocalTime startTime, LocalTime endTime){
        return (long) (( endTime.getHour() - startTime.getHour()) * 60
                + endTime.getMinute() - startTime.getMinute());
    }
    public static LocalTime roundToMultipleQuarterHour(LocalTime startTime, 
            LocalTime endTime){
        int hour, min, v;
        if (!isMultipleQuarterHour(startTime, endTime)){
            v = (int) getMinPerTask(startTime, endTime);
            System.out.println(v);
            while (v%15 != 0){
                v--;
            }
            hour = v / 60;
            min = v - hour * 60;
            if (startTime.getMinute() + min > 59){
                hour++;
                min -= 60;
            }
            endTime = LocalTime.of(startTime.getHour() + hour, startTime.getMinute() + min);
        }
        return endTime;
    }
    
    public static boolean isMultipleQuarterHour(LocalTime startTime, LocalTime endTime){
        return getMinPerTask(startTime, endTime)%15 == 0;
    }
    
    public static boolean isSeparatedTime(Task t, List<Task> tasks){
        boolean ret = false;
        for(Task i: tasks){
            if (i != t){
                if (t.getStartTime().compareTo(i.getEndTime()) >= 0 || t.getEndTime().compareTo(i.getStartTime()) <= 0)
                    ret = true;
                else return false;
            }
        }
        return ret;
    }
    
    public static boolean isWeekday(WorkDay d){
        return (d.getActualDay().getDayOfWeek() == DayOfWeek.SATURDAY 
                || d.getActualDay().getDayOfWeek() == DayOfWeek.SUNDAY);
    }
}
