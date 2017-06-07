/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlog16;

import java.util.List;

/**
 *
 * @author lysharnie
 */
public class TLOG16 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Task t1 = new Task("LT-1234", 12, 45, 17, 30, "cc");
        Task t2 = new Task("4567", "11:15", "12:45", "bb");
        //Util.roundToMultipleQuarterHour(t1.getStartTime(), t1.getEndTime());
        //if (Util.isMultipleQuarterHour(t1.getStartTime(), t1.getEndTime())) System.out.println("jo");
       // else System.out.println("nem nem");
        WorkDay w1 = new WorkDay(700, 2017, 06, 06);
        WorkDay w2 = new WorkDay(750, 2017, 06, 11);
        if(Util.isWeekday(w2)) System.out.println("booom");
        w1.addTask(t1);
        w1.addTask(t2);
        List<Task> l = w1.getTasks();
        System.out.println(w1.getTasks().size());
        for (Task i : l){
            System.out.println(i.getTaskId());
        }
        if(Util.isSeparatedTime(t2, w1.getTasks())) System.out.println("hey ho");
        else System.out.println("lul");
        
        /*WorkMonth wm = new WorkMonth(2011, 05);
        WorkMonth wm2 = new WorkMonth(2000, 11);
        WorkMonth wm3 = new WorkMonth(2011, 05);
        if (wm.isSameMonth(w1)) System.out.println("nyert");
        wm.addWorkDay(w1);
        wm.addWorkDay(w2, true);
        TimeLogger tl = new TimeLogger();
        tl.addMonth(wm);
        tl.addMonth(wm2);
        tl.addMonth(wm3);*/
    }
    
}
