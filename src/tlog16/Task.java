package tlog16;


import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lysharnie
 */
public class Task {
    // ----------------------------- Variables -------------------------------
    private String taskId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;
    private static final String REGEX1 = "\\d{4}";
    private static final String REGEX2 = "LT-\\d{4}";
    // ----------------------------------------------------------------------
    
    // ----------------------------- Constructors ---------------------------
    public Task(String taskId, int startHour, int startMin, 
            int endHour, int endMin, String comment){
        this.taskId = taskId;
        startTime = LocalTime.of(startHour, startMin);
        endTime = LocalTime.of(endHour, endMin);
        this.comment = comment;
    }
    
    public Task(String taskId, String startTime, String endTime,
            String comment){
        this.taskId = taskId;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.comment = comment;
    }
    
    public Task(String taskId){
        this.taskId = taskId;
    }
    // ----------------------------------------------------------------------
    
    // ---------------------------- Getters --------------------------------
    public String getTaskId(){
        return taskId;
    }
    
    public LocalTime getStartTime(){
        return startTime;
    }
    
    public LocalTime getEndTime(){
        return endTime;
    }
    
    public String getComment(){
        return comment;
    }
    
    public long getMinPerTask(){
        return (long) (( endTime.getHour() - startTime.getHour()) * 60
                + endTime.getMinute() - startTime.getMinute());
    }
    // ---------------------------------------------------------------------
    
    // ----------------------------- Methods -------------------------------
    private boolean isValidRedmineTaskId(){
        Pattern p  = Pattern.compile(REGEX1);
        Matcher m = p.matcher(taskId);
        
        return m.matches();
    }
    
    private boolean isValidLTTaskId(){
        Pattern p  = Pattern.compile(REGEX2);
        Matcher m = p.matcher(taskId);
        
        return m.matches();
    }
    
    public boolean isValidTaskId(){
        return (isValidRedmineTaskId() || isValidLTTaskId());
    }
    // ---------------------------------------------------------------------
    
    // ------------------------------ Setters -----------------------------
    public void setTaskId(String id){
        taskId = id;
    }
    
    public void setStartTime(int hour, int min){
        startTime = LocalTime.of(hour, min);
    }
    
    public void setStartTime(String time){
        startTime = LocalTime.parse(time);
    }
    
    public void setEndTime(int hour, int min){
        endTime = LocalTime.of(hour, min);
    }
    
    public void setEndTime(String time){
        endTime = LocalTime.parse(time);
    }
    
    public void setComment(String comment){
        this.comment = comment;
    }
    // ------------------------------------------------------------------
}
