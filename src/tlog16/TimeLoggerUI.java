package tlog16;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import timelogger.exceptions.*;

/**
 *singleton class, what handle the UI
 * @author Dániel Pallósi
 * 
 */
public class TimeLoggerUI {
    private static final String[] MAIN_MENU = 
            new String[]{
              "Exit", "List months", "List days", "List tasks for a specific day",
                "Add new month", "Add day to a specific month", "Start a task for a day",
                "Finish a specific task", "Delete a task", "Modify task", "Statistics"
            };
    private static final String[] MODIFY_MENU =
            new String[]{
              "Exit", "Task ID", "Start time", "End time", "Comment"  
            };
    private final TimeLogger tl = TimeLogger.getInstance();
    private static TimeLoggerUI instance = null;
    
    private TimeLoggerUI(){}
    public static TimeLoggerUI getInstance(){
        if (instance == null)
            instance = new TimeLoggerUI();
        return instance;
    }
    
    /**
     *the core. it operates the whole app
     */
    public void menu(){
        boolean finished = false;
        while(!finished){
            System.out.println();
            
            int cmd = selectCommand("Select function", MAIN_MENU);
            switch (cmd){
                case 0 : finished = true;
                    break;
                case 1 : listMonths();
                    break;
                case 2 : listDays();
                    break;
                case 3 : listTasks();
                    break;
                case 4 : addMonth();
                    break;
                case 5 : addDay();
                    break;
                case 6 : startTask();
                    break;
                case 7 : finishTask();
                    break;
                case 8 : deleteTask();
                    break;
                case 9 : modifyTask();
                    break;
                case 10 : stats();
                    break;
            }
        }
    }
    
    /**
     *when we want to modify a task, this menu will appear
     * 
     * @param month for the date what the user typed in
     * @param day for the date what the user typed in
     * @param taskIdx index of the chosen task
     */
    private void modifyMenu(int month, int day, int taskIdx){
        boolean finished = false;
        while(!finished){
            int cmd = selectCommand("Select the data you want to change", MODIFY_MENU);
            switch(cmd){
                case 0 : finished = true;
                    break;
                case 1 : modifyId(month, day, taskIdx); finished = true;
                    break;
                case 2 : modifyStart(month, day, taskIdx); finished = true;
                    break;
                case 3 : modifyEnd(month, day, taskIdx); finished = true;
                    break;
                case 4 : modifyComment(month, day, taskIdx); finished = true;
                    break;
            }
        }
    }
    
    /**
     *select a function from any menu
     * 
     * @param title describes what really want to express with this menu
     * @param menu String[] points of the menu
     * @return the number of the chosen function
     */
    private int selectCommand(String title, String[] menu){
        Scanner s = new Scanner(System.in);
        System.out.println(title);
        for (int i = 0; i<menu.length; i++)
            System.out.println(i + " - " + menu[i]);
        int cmd = 0;
        boolean exit = false;
        try{
            while (!exit){
                cmd = s.nextInt();
                if (cmd < 0 || cmd > 10)
                    System.out.println("This function doesn't exist: " + cmd + 
                            " type a valid number");
                else exit = true;
            }
        } catch (InputMismatchException e){
            System.out.println("You need to type a number of the list");
        }
        return cmd;
    }
    
    /**
     * list the months
     */
    private void listMonths(){
        if (tl.getMonths().isEmpty())
            System.out.println("There is no month added yet.");
        else{
            int i = 0;
            for (WorkMonth j : tl.getMonths())
                System.out.println((i++) + ". " + j.getDate());
        }
    }
    
    /**
     * list the days of a month
     */
    private void listDays(){
        System.out.println("Select a month: ");
        listMonths();
        if (tl.getMonths().isEmpty())
            return;
        if (tl.getMonths().get(0).getDays().isEmpty()){
            System.out.println("There is no day added yet.");
            return;
        }
        
        Scanner s = new Scanner(System.in);
        int cmd = 0;
        boolean exit = false;
        try{
            while(!exit){
                cmd = s.nextInt();
                if (cmd < 0 || cmd > tl.getMonths().size()-1)
                    System.out.println("This month doesn't exist " + cmd +
                            ", type a valid number!");
                else exit = true;
            }
        } catch (InputMismatchException e){
            System.out.println("You need to type a number of the list");
        }finally{
            for (WorkDay d : tl.getMonths().get(cmd).getDays())
                System.out.println(d.getActualDay());
        }
    }
    
    /**
     * list the tasks of the chosen day 
     */
    private void listTasks(){
        Scanner s = new Scanner(System.in);
        String cmd = null;
        boolean exit = false, exist = false;
        final String REGEX = "[0,1]?[0-9]:[0-3]?[0-9]";
        
        if (tl.getMonths().isEmpty()){
            System.out.println("There is no month added yet.");
            return;
        } else if (tl.getMonths().get(0).getDays().isEmpty()){
            System.out.println("There is no day added yet.");
            return;
        } else if (tl.getMonths().get(0).getDays().get(0).getTasks().isEmpty()){
            System.out.println("There is no task added yet.");
            return;
        } else{
            System.out.println("Type a month and a day (MM:DD)");
            try{
                Pattern p  = Pattern.compile(REGEX);
                Matcher m;
                while(!exit){
                    cmd = s.next();
                    m = p.matcher(cmd);
                    String[] helper = cmd.split(":");
                    int month = Integer.valueOf(helper[0]);
                    int day = Integer.valueOf(helper[1]);
                    for (WorkMonth wm : tl.getMonths())
                        for (WorkDay wd : wm.getDays())
                            if (wd.getActualDay().getMonthValue() == month && wd.getActualDay().getDayOfMonth() == day){
                                exist = true;
                                break;
                            }
                    if (!m.matches() || (month<0 || month>12) || (day<0 || day>31) || !exist){
                        System.out.println("Wrong format, or the given date doesn't exist!");
                    } else {
                        exit = true;
                        int i = 0;
                        for (WorkMonth wm : tl.getMonths())
                        if (wm.getDate().getMonthValue() == month)
                            for (WorkDay wd : wm.getDays())
                                if (wd.getActualDay().getDayOfMonth() == day)
                                    for (Task t : wd.getTasks())
                                        System.out.println((i++) + ". " + t.toString());

                    }
                }
            } catch(InputMismatchException e){
                System.out.println("You need to type a number of the list");
            }
        }
        
    }
    
    /**
     * add new month
     */
    private void addMonth(){
        Scanner s = new Scanner(System.in);
        int year = 0, month = 0;
        boolean exit = false, exit2 = false;
        System.out.println("Type a year: ");
        try{
            while(!exit){
                year = s.nextInt();
                if (year > LocalDate.now().getYear() || year < 0)
                    System.out.println("Wrong date");
                else{
                    exit = true;
                    System.out.println("Type a month: ");
                    while (!exit2){
                        month = s.nextInt();
                        if (month < 0 || month > 12)
                            System.out.println("This month doesn't exist: " + month);
                        else {
                            exit2 = true;
                            tl.addMonth(new WorkMonth(year, month));
                            System.out.println("Month added.");
                        }
                    }
                }
            }
        } catch(InputMismatchException e){
            System.out.println("You have to type a number!");
        } catch (NotNewMonthException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * add new day to a month
     */
    private void addDay(){
        System.out.println("Select a month by its index: ");
        listMonths();
        Scanner s = new Scanner(System.in);
        Scanner s2 = new Scanner(System.in);
        int monthIdx, day;
        double workingHours;
        boolean exit = false, exit2 = false, exit3 = false;
        
        try{
            while(!exit){
                monthIdx = s.nextInt();
                if (monthIdx < 0 || monthIdx > tl.getMonths().size()-1)
                    System.out.println("This month doesn't exist: " + monthIdx);
                else{
                    exit = true;
                    System.out.println("Type a day you want to add: ");
                    while(!exit2){
                        day = s.nextInt();
                        if (day < 0 || day > 31)
                            System.out.println("Wrong format! (0-31)");
                        else{
                            exit2 = true;
                            System.out.println("Type the required working hours for this day: (default value is 7.5)");
                            while(!exit3){
                                String line = s2.nextLine();
                                if (line.equals("") || line.equals(" ")){
                                    workingHours = 7.5;
                                } else{
                                    workingHours = Double.valueOf(line);
                                }
                                if (workingHours < 0 || workingHours > 24)
                                    System.out.println("The number should be between 0 and 24");
                                else{
                                    exit3 = true;
                                    System.out.println("Is the weekend enabled? (y/n)");
                                    line = s.next();
                                    if (line.equals("y") || line.equals("yes")){
                                        tl.getMonths().get(monthIdx).addWorkDay(new WorkDay((long) (workingHours * 60), 
                                            tl.getMonths().get(monthIdx).getDate().getYear(), 
                                            tl.getMonths().get(monthIdx).getDate().getMonthValue(), day), true);
                                        System.out.println("Day added.");
                                    }
                                    else{
                                        tl.getMonths().get(monthIdx).addWorkDay(new WorkDay((long) (workingHours * 60), 
                                            tl.getMonths().get(monthIdx).getDate().getYear(), 
                                            tl.getMonths().get(monthIdx).getDate().getMonthValue(), day));
                                        System.out.println("Day added.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (InputMismatchException | NumberFormatException e){
            System.out.println("You have to type a number!");
        } catch (WeekendNotEnabledException | NotNewDateException | NotTheSameMonthException | NegativeMinutesOfWorkException | FutureWorkException | EmptyTimeFieldException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * start a task with equal start/end time to a day. it means that task is unfinished
     */
    private void startTask(){
        System.out.println("Type a month and a day (MM:DD) : ");
        Scanner s = new Scanner(System.in);
        final String regex = "[0,1]?[0-9]:[0-3]?[0-9]";
        final String regex3 = "[0-2]?[0-9]:[0-5]?[0-9]";
        final String regex2 = ".*#.*";
        String ans = null;
        Pattern p = Pattern.compile(regex);
        Matcher m;
        int hour = 0, min = 0;
        boolean exit = false, exist = false, exit2 = false, exit3 = false;
        
        try{
            while(!exit){
                ans = s.nextLine();
                m = p.matcher(ans);
                String helper[] = ans.split(":");
                int month = Integer.valueOf(helper[0]);
                int day = Integer.valueOf(helper[1]);
                for (WorkMonth wm : tl.getMonths())
                    for (WorkDay wd : wm.getDays())
                        if (wd.getActualDay().getMonthValue() == month && wd.getActualDay().getDayOfMonth() == day){
                            exist = true;
                            break;
                        }
                if (!m.matches() || !exist || month < 0 || month > 12 || day < 0 || day > 31)
                    System.out.println("Wrong format, or the given date doens't exist!");
                else{
                    exit = true;
                    p = Pattern.compile(regex2);
                    System.out.println("Type a task id, and a comment (id#comment): ");
                    while(!exit2){
                        ans = s.nextLine();
                        m = p.matcher(ans);
                        helper = ans.split("#");
                        if (!m.matches())
                            System.out.println("Wrong format");
                        else{
                            exit2 = true;
                            System.out.println("Type the start time (HH:MM): ");
                            while(!exit3){
                                ans = s.nextLine();
                                p = Pattern.compile(regex3);
                                m = p.matcher(ans);
                                String helper2[] = ans.split(":");
                                if (ans.equals("") || ans.equals(" ")){
                                    hour = LocalTime.now().getHour();
                                    min = LocalTime.now().getMinute();
                                    exit3 = true;
                                    
                                    for (WorkMonth wm : tl.getMonths())
                                        if (wm.getDate().getMonthValue() == month)
                                            for (WorkDay wd : wm.getDays())
                                                if (wd.getActualDay().getDayOfMonth() == day){
                                                    wd.addTask(new Task(helper[0], hour, min, hour, min, helper[1]));
                                                }
                                } 
                                if (m.matches()){
                                    hour = Integer.valueOf(helper2[0]);
                                    min = Integer.valueOf(helper2[1]);
                                    exit3 = true;
                                    
                                    for (WorkMonth wm : tl.getMonths())
                                        if (wm.getDate().getMonthValue() == month)
                                            for (WorkDay wd : wm.getDays())
                                                if (wd.getActualDay().getDayOfMonth() == day)
                                                    wd.addTask(new Task(helper[0], hour, min, hour, min, helper[1]));
                                }
                                if (!m.matches() || hour < 0 || hour > 24 || min < 0 || min > 60)
                                    System.out.println("Wrong format!");
                            }
                        }
                    }
                    
                }
            }
        } catch (InputMismatchException e){
            e.printStackTrace();
        } catch (EmptyTimeFieldException | NotSeparatedTimesException | NotExpectedTimeOrderException | InvalidTaskIdException | NoTaskIdException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * finish any unfinished tasks
     */
    private void finishTask(){
        Scanner s = new Scanner(System.in);
        String cmd = null;
        boolean exit = false, exist = false;
        final String REGEX = "[0,1]?[0-9]:[0-3]?[0-9]";
        final String REGEX2 = "[0-2]?[0-9]:[0-5]?[0-9]";
        List<Task> unfinishedTasks = new ArrayList<Task>();
        
        System.out.println("Type a month and a day (MM:DD)");
        try{
            Pattern p  = Pattern.compile(REGEX);
            Matcher m;
            while(!exit){
                cmd = s.next();
                m = p.matcher(cmd);
                String[] helper = cmd.split(":");
                int month = Integer.valueOf(helper[0]);
                int day = Integer.valueOf(helper[1]);
                for (WorkMonth wm : tl.getMonths())
                    for (WorkDay wd : wm.getDays())
                        if (wd.getActualDay().getMonthValue() == month && wd.getActualDay().getDayOfMonth() == day){
                            exist = true;
                            break;
                        }
                if (!m.matches() || (month<0 || month>12) || (day<0 || day>31) || !exist){
                    System.out.println("Wrong format, or the given date doesn't exist!");
                    break;
                } else {
                    exit = true;
                    System.out.println("Choose an unfinished task by its index: ");
                    for (WorkMonth wm : tl.getMonths())
                        if (wm.getDate().getMonthValue() == month)
                            for (WorkDay wd : wm.getDays())
                                if (wd.getActualDay().getDayOfMonth() == day)
                                    for (Task t : wd.getTasks())
                                        if (t.getStartTime().equals(t.getEndTime()) || t.getStartTime().compareTo(t.getEndTime()) > 0)
                                            unfinishedTasks.add(t);
                    if (unfinishedTasks.isEmpty()){
                        System.out.println("There is no unfinished task!");
                        break;
                    } else{                 
                        int i = 0;
                        for (Task t : unfinishedTasks)
                            System.out.println((i++) + ". " + t.toString());
                        int taskId;
                        boolean exit2 = false;
                        while(!exit2){
                            taskId = s.nextInt();
                            if (taskId < 0 || taskId > i)
                                System.out.println("This task doesn't exist: " + taskId + "!");
                            else{
                                p = Pattern.compile(REGEX2);
                                boolean exit3 = false;
                                while(!exit3){
                                    System.out.println("Type the end time (HH:MM): ");
                                    cmd = s.next();
                                    m = p.matcher(cmd);
                                    helper = cmd.split(":");
                                    int hour = Integer.valueOf(helper[0]);
                                    int min = Integer.valueOf(helper[1]);
                                    if (!m.matches() || hour < 0 || hour > 24 || min < 0 || min > 59)
                                        System.out.println("Wrong format!");
                                    else{
                                        System.out.println("You typed: " + hour + ":" + min + ". Are you sure about this end time? (y/n)");
                                        boolean exit4 = false;
                                        while(!exit4){
                                            cmd = s.next();
                                            if (cmd.equals("yes") || cmd.equals("y")){
                                                exit4 = true;
                                                exit3 = true;
                                                exit2 = true;

                                                int k = 0;
                                                for (Task t : unfinishedTasks)
                                                    if (k++ == taskId)
                                                        t.setEndTime(hour, min);
                                                k = 0;
                                                for (WorkMonth wm : tl.getMonths())
                                                    for (WorkDay wd : wm.getDays())
                                                        for (Task t : wd.getTasks())
                                                            if (t.toString().equals(unfinishedTasks.get(taskId).toString())){
                                                                wd.setSumPerDay(wd.getTasks().get(k).getMinPerTask());
                                                                wm.setSumPerMonth(wd.getTasks().get(k).getMinPerTask());
                                                            } else k++;
                                            } else exit4 = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(InputMismatchException e){
            System.out.println("You need to type a number of the list");
        } catch (NotExpectedTimeOrderException | EmptyTimeFieldException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * delete a task 
     */
    private void deleteTask(){ 
        Scanner s = new Scanner(System.in);
        String cmd = null;
        boolean exit = false, exist = false, notExist = false;
        final String REGEX = "[0,1]?[0-9]:[0-3]?[0-9]";
        
        System.out.println("Type a month and a day (MM:DD)");
        try{
            Pattern p  = Pattern.compile(REGEX);
            Matcher m;
            while(!exit){
                cmd = s.next();
                m = p.matcher(cmd);
                String[] helper = cmd.split(":");
                int month = Integer.valueOf(helper[0]);
                int day = Integer.valueOf(helper[1]);
                for (WorkMonth wm : tl.getMonths())
                    for (WorkDay wd : wm.getDays())
                        if (wd.getActualDay().getMonthValue() == month && wd.getActualDay().getDayOfMonth() == day){
                            exist = true;
                            break;
                        }
                if (!m.matches() || (month<0 || month>12) || (day<0 || day>31) || !exist){
                    System.out.println("Wrong format, or the given date doesn't exist!");
                    break;
                } else {
                    exit = true;
                    int i = 0;
                    System.out.println("Choose a task by its index: ");
                    for (WorkMonth wm : tl.getMonths())
                        if (wm.getDate().getMonthValue() == month)
                            for (WorkDay wd : wm.getDays())
                                if (wd.getActualDay().getDayOfMonth() == day){
                                    if (wd.getTasks().isEmpty()){
                                        System.out.println("There is no task added yet.");
                                        notExist = true;
                                    } else{
                                        for (Task t : wd.getTasks()){
                                            i++;
                                            System.out.println((i-1) + ". " + t.toString());
                                        }
                                    }
                                }
                    if (notExist)
                        break;
                    else{
                        int taskId;
                        boolean exit2 = false;
                        while(!exit2){
                            taskId = s.nextInt();
                            if (taskId < 0 || taskId > i)
                                System.out.println("This task doesn't exist: " + taskId + "!");
                            else{
                                exit2 = true;
                                for (WorkMonth wm : tl.getMonths())
                                    if (wm.getDate().getMonthValue() == month)
                                        for (WorkDay wd : wm.getDays())
                                            if (wd.getActualDay().getDayOfMonth() == day){
                                                wd.setSumPerDay(wd.getTasks().get(taskId).getMinPerTask() * -1);
                                                wm.setSumPerMonth(wd.getTasks().get(taskId).getMinPerTask() * -1);
                                                wd.getTasks().remove(taskId);
                                            }
                                System.out.println("Task deleted.");
                            }
                        }
                    }
                }
            }
        }catch(InputMismatchException e){
            System.out.println("You need to type a number of the list");
        } catch (EmptyTimeFieldException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * modify any tasks by using a simple menu
     */
    private void modifyTask(){
        Scanner s = new Scanner(System.in);
        String cmd = null;
        boolean exit = false, exist = false, notExist = false;
        final String REGEX = "[0,1]?[0-9]:[0-3]?[0-9]";
        
        System.out.println("Type a month and a day (MM:DD)");
        try{
            Pattern p  = Pattern.compile(REGEX);
            Matcher m;
            while(!exit){
                cmd = s.next();
                m = p.matcher(cmd);
                String[] helper = cmd.split(":");
                int month = Integer.valueOf(helper[0]);
                int day = Integer.valueOf(helper[1]);
                for (WorkMonth wm : tl.getMonths())
                    for (WorkDay wd : wm.getDays())
                        if (wd.getActualDay().getMonthValue() == month && wd.getActualDay().getDayOfMonth() == day){
                            exist = true;
                            break;
                        }
                if (!m.matches() || (month<0 || month>12) || (day<0 || day>31) || !exist){
                    System.out.println("Wrong format, or the given date doesn't exist!");
                    break;
                } else {
                    exit = true;
                    int i = 0;
                    System.out.println("Choose a task by its index: ");
                    for (WorkMonth wm : tl.getMonths())
                        if (wm.getDate().getMonthValue() == month)
                            for (WorkDay wd : wm.getDays())
                                if (wd.getActualDay().getDayOfMonth() == day)
                                    if (wd.getTasks().isEmpty()){
                                        System.out.println("There is no task added yet.");
                                        notExist = true;
                                    } else{
                                        for (Task t : wd.getTasks()){
                                        i++;
                                        System.out.println((i-1) + ". " + t.toString());
                                        }
                                    }
                                    
                    if (notExist)
                        break;
                    else{
                        int taskIdx;
                        boolean exit2 = false;
                        while(!exit2){
                            taskIdx = s.nextInt();
                            if (taskIdx < 0 || taskIdx > i)
                                System.out.println("This task doesn't exist: " + taskIdx + "!");
                            else{
                                modifyMenu(month, day, taskIdx);
                                exit2 = true;
                            }
                        }
                    }
                    
                }
            }
        } catch(InputMismatchException e){
            System.out.println("You need to type a number of the list");
        }
    }
    
    /**
     * shows statistics for months, and their days
     */
    private void stats(){
        System.out.println("Select a month: ");
        if (tl.getMonths().isEmpty()){
            System.out.println("There is no month added yet.");
        }
        else{
            int i = 0;
            for (WorkMonth j : tl.getMonths())
                System.out.println((i++) + ". " + j.getDate());
            Scanner s = new Scanner(System.in);
            int monthIdx = 0;
            boolean exit = false;
            try{
                while(!exit){
                    monthIdx = s.nextInt();
                    if (monthIdx < 0 || monthIdx > tl.getMonths().size()-1)
                        System.out.println("This month doesn't exist " + monthIdx +
                                ", type a valid number!");
                    else exit = true;
                }
            } catch (InputMismatchException e){
                System.out.println("You need to type a number of the list");
            }finally{
                System.out.println("Required minutes per day of this month: " + tl.getMonths().get(monthIdx).getRequiredMinPerMonth());
                System.out.println("Sum of the worked minutes of the month: " + tl.getMonths().get(monthIdx).getSumPerMonth());
                System.out.println("Extra minutes per month: " + tl.getMonths().get(monthIdx).getExtraMinPerMonth());
                for (WorkDay d : tl.getMonths().get(monthIdx).getDays()){
                    System.out.println("Day: " + d.getActualDay());
                    System.out.println("Required minutes per day: " + d.getRequiredMinPerDay());
                    System.out.println("Sum of the worked minutes of the day: " + d.getSumPerDay());
                    System.out.println("Extra minutes per day: " + d.getExtraMinPerDay());
                }
            }
        }
    }
    
    // ------------------------------------------------------- Methods for the modify menu ----------------------------------------

    /**
     *
     * @param month chosen month
     * @param day chosen day
     * @param taskIdx chosen index of the task
     */
    private void modifyId(int month, int day, int taskIdx){
        String cmd = null, oldId = null;
        Scanner s = new Scanner(System.in);
        int i = 0;
        boolean exit = false;
        for (WorkMonth wm : tl.getMonths())
            if (wm.getDate().getMonthValue() == month)
                for (WorkDay wd : wm.getDays())
                    if (wd.getActualDay().getDayOfMonth() == day)
                            for (Task t : wd.getTasks())
                            if (i++ == taskIdx){
                                oldId = t.getTaskId();
                            }
        try{
            while(!exit){
                System.out.println("Type the task ID: Now it's " + "(" + oldId + ").");
                cmd = s.nextLine();
                if (cmd.equals("") || cmd.equals(" ")){
                    System.out.println("Nothing changed.");
                    exit = true;
                }
                else{
                        System.out.println("You typed: " + cmd +  ". Are you sure about this task ID? (y/n)");
                        boolean exit2 = false;
                        while(!exit2){
                            String sure = s.next();
                            if (sure.equals("yes") || sure.equals("y")){
                                exit2 = true;
                                exit = true;

                                i = 0;
                                for (WorkMonth wm : tl.getMonths())
                                    if (wm.getDate().getMonthValue() == month)
                                        for (WorkDay wd : wm.getDays())
                                            if (wd.getActualDay().getDayOfMonth() == day)
                                                for (Task t : wd.getTasks())
                                                    if (i++ == taskIdx)
                                                        t.setTaskId(cmd);
                            } else exit2 = true;
                        }
                    }
            }
        } catch (InputMismatchException e){
            e.printStackTrace();
        } catch (NoTaskIdException | InvalidTaskIdException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @param month chosen month
     * @param day chosen day
     * @param taskIdx chosen index of the task
     */
    private void modifyEnd(int month, int day, int taskIdx){
        final String REGEX2 = "[0-2]?[0-9]:[0-5]?[0-9]";
        Pattern p = Pattern.compile(REGEX2);
        Matcher m;
        String cmd = null;
        Scanner s = new Scanner(System.in);
        int oldHour = 0, oldMin = 0, i = 0;
        boolean exit = false;
        for (WorkMonth wm : tl.getMonths())
            if (wm.getDate().getMonthValue() == month)
                for (WorkDay wd : wm.getDays())
                    if (wd.getActualDay().getDayOfMonth() == day)
                            for (Task t : wd.getTasks())
                            if (i++ == taskIdx){
                                oldHour = t.getEndTime().getHour();
                                oldMin = t.getEndTime().getMinute();
                            }
        try{
            while(!exit){
                System.out.println("Type the end time (HH:MM): Now it's (" + oldHour + ":" + oldMin + ")");
                cmd = s.nextLine();
                if (cmd.equals("") || cmd.equals(" ")){
                    System.out.println("Nothing changed.");
                    exit = true;
                }
                else{
                    m = p.matcher(cmd);
                    String[] helper = cmd.split(":");
                    int hour = Integer.valueOf(helper[0]);
                    int min = Integer.valueOf(helper[1]);
                    if (!m.matches() || hour < 0 || hour > 24 || min < 0 || min > 59)
                        System.out.println("Wrong format!");
                    else{
                        System.out.println("You typed: " + hour + ":" + min + ". Are you sure about this end time? (y/n)");
                        boolean exit2 = false;
                        while(!exit2){
                            cmd = s.next();
                            if (cmd.equals("yes") || cmd.equals("y")){
                                exit2 = true;
                                exit = true;

                                i = 0;
                                for (WorkMonth wm : tl.getMonths())
                                    if (wm.getDate().getMonthValue() == month)
                                        for (WorkDay wd : wm.getDays())
                                            if (wd.getActualDay().getDayOfMonth() == day)
                                                for (Task t : wd.getTasks())
                                                    if (i++ == taskIdx){
                                                        long old = t.getMinPerTask();
                                                        t.setEndTime(hour, min);
                                                        wd.setSumPerDay(wd.getTasks().get(taskIdx).getMinPerTask() - old);
                                                        wm.setSumPerMonth(wd.getTasks().get(taskIdx).getMinPerTask() - old);
                                                    }
                            } else exit2 = true;
                        }
                    }
                }
                
            }
        } catch (InputMismatchException e){
            e.printStackTrace();
        } catch (NotExpectedTimeOrderException | EmptyTimeFieldException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @param month chosen month
     * @param day chosen day
     * @param taskIdx chosen index of the task
     */
    private void modifyStart(int month, int day, int taskIdx){
        final String REGEX2 = "[0-2]?[0-9]:[0-5]?[0-9]";
        Pattern p = Pattern.compile(REGEX2);
        Matcher m;
        String cmd = null;
        Scanner s = new Scanner(System.in);
        int oldHour = 0, oldMin = 0, i = 0;
        boolean exit = false;
        for (WorkMonth wm : tl.getMonths())
            if (wm.getDate().getMonthValue() == month)
                for (WorkDay wd : wm.getDays())
                    if (wd.getActualDay().getDayOfMonth() == day)
                            for (Task t : wd.getTasks())
                            if (i++ == taskIdx){
                                oldHour = t.getStartTime().getHour();
                                oldMin = t.getStartTime().getMinute();
                            }
        try{
            while(!exit){
                System.out.println("Type the start time (HH:MM): Now it's (" + oldHour + ":" + oldMin + ")");
                cmd = s.nextLine();
                if (cmd.equals("") || cmd.equals(" ")){
                    System.out.println("Nothing changed.");
                    exit = true;
                }
                else{
                    m = p.matcher(cmd);
                    String[] helper = cmd.split(":");
                    int hour = Integer.valueOf(helper[0]);
                    int min = Integer.valueOf(helper[1]);
                    if (!m.matches() || hour < 0 || hour > 24 || min < 0 || min > 59)
                        System.out.println("Wrong format!");
                    else{
                        System.out.println("You typed: " + hour + ":" + min + ". Are you sure about this start time? (y/n)");
                        boolean exit2 = false;
                        while(!exit2){
                            cmd = s.next();
                            if (cmd.equals("yes") || cmd.equals("y")){
                                exit2 = true;
                                exit = true;

                                i = 0;
                                for (WorkMonth wm : tl.getMonths())
                                    if (wm.getDate().getMonthValue() == month)
                                        for (WorkDay wd : wm.getDays())
                                            if (wd.getActualDay().getDayOfMonth() == day)
                                                for (Task t : wd.getTasks())
                                                    if (i++ == taskIdx){
                                                        long old = t.getMinPerTask();
                                                        t.setStartTime(hour, min);
                                                        wd.setSumPerDay(wd.getTasks().get(taskIdx).getMinPerTask() - old);
                                                        wm.setSumPerMonth(wd.getTasks().get(taskIdx).getMinPerTask() - old);
                                                    }
                            } else exit2 = true;
                        }
                    }
                }
                
            }
        } catch (InputMismatchException e){
            e.printStackTrace();
        } catch (NotExpectedTimeOrderException | EmptyTimeFieldException ex) {
            Logger.getLogger(TimeLoggerUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @param month chosen month
     * @param day chosen day
     * @param taskIdx chosen index of the task
     */
    private void modifyComment(int month, int day, int taskIdx){
        String cmd = null, oldComm = null;
        Scanner s = new Scanner(System.in);
        int i = 0;
        boolean exit = false;
        for (WorkMonth wm : tl.getMonths())
            if (wm.getDate().getMonthValue() == month)
                for (WorkDay wd : wm.getDays())
                    if (wd.getActualDay().getDayOfMonth() == day)
                            for (Task t : wd.getTasks())
                            if (i++ == taskIdx){
                                oldComm = t.getComment();
                            }
        try{
            while(!exit){
                System.out.println("Type the new comment: Now it's " + "(" + oldComm + ").");
                cmd = s.nextLine();
                if (cmd.equals("") || cmd.equals(" ")){
                    System.out.println("Nothing changed.");
                    exit = true;
                }
                else{
                        System.out.println("You typed: " + cmd +  ". Are you sure about this comment? (y/n)");
                        boolean exit2 = false;
                        while(!exit2){
                            String sure = s.next();
                            if (sure.equals("yes") || sure.equals("y")){
                                exit2 = true;
                                exit = true;

                                i = 0;
                                for (WorkMonth wm : tl.getMonths())
                                    if (wm.getDate().getMonthValue() == month)
                                        for (WorkDay wd : wm.getDays())
                                            if (wd.getActualDay().getDayOfMonth() == day)
                                                for (Task t : wd.getTasks())
                                                    if (i++ == taskIdx)
                                                        t.setComment(cmd);
                            } else exit2 = true;
                        }
                    }
            }
        } catch (InputMismatchException e){
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------------------------------------------------------
}
