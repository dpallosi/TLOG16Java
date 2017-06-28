package tlog16;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.InvalidTaskIdException;
import timelogger.exceptions.NoTaskIdException;
import timelogger.exceptions.NotExpectedTimeOrderException;

/**
 *
 * @author Dániel Pallósi
 */
public class Task {
    // ----------------------------- Variables -------------------------------
    private String taskId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;
    private final String REGEX1 = "\\d{4}";
    private final String REGEX2 = "LT-\\d{4}";
    // ----------------------------------------------------------------------
    
    // ----------------------------- Constructors ---------------------------

    /**
     * Normal constructor with exception handling
     *
     * @param taskId -
     * @param startHour -
     * @param startMin -
     * @param endHour -
     * @param endMin -
     * @param comment -
     * @throws InvalidTaskIdException if the taskId is not valid
     * @throws NotExpectedTimeOrderException if the endTime is less than startTime
     * @throws NoTaskIdException if there is no taskId
     * @throws EmptyTimeFieldException if any time parameter missing
     */
    public Task(String taskId, int startHour, int startMin, 
            int endHour, int endMin, String comment) throws InvalidTaskIdException, NotExpectedTimeOrderException, NoTaskIdException, EmptyTimeFieldException{
        if (taskId.isEmpty())
            throw new NoTaskIdException();
        this.taskId = taskId;
        if (!isValidTaskId())
            throw new InvalidTaskIdException();
        startTime = LocalTime.of(startHour, startMin);
        endTime = LocalTime.of(endHour, endMin);
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        this.comment = comment;
        if (endTime.compareTo(startTime) < 0 )
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *Normal constructor with exception handling
     * 
     * @param taskId -
     * @param startTime -
     * @param endTime -
     * @param comment -
     * @throws InvalidTaskIdException if the taskId is not valid
     * @throws NotExpectedTimeOrderException if the endTime is less than startTime
     * @throws NoTaskIdException if there is no taskId
     * @throws EmptyTimeFieldException if any time parameter missing
     */
    public Task(String taskId, String startTime, String endTime,
            String comment) throws InvalidTaskIdException, NotExpectedTimeOrderException, NoTaskIdException, EmptyTimeFieldException{
        if (taskId.isEmpty())
            throw new NoTaskIdException();
        this.taskId = taskId;
        if (!isValidTaskId())
            throw new InvalidTaskIdException();
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.endTime = Util.roundToMultipleQuarterHour(this.startTime, this.endTime);
        this.comment = comment;
        if (this.endTime.compareTo(this.startTime) < 0 )
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *Constructor with a missing time parameter throws exception
     * 
     * @param taskId -
     * @param startHour -
     * @param startMin -
     * @param comment -
     * @throws EmptyTimeFieldException if any time parameter missing
     */
    public Task(String taskId, int startHour, int startMin, String comment) throws EmptyTimeFieldException{
        throw new timelogger.exceptions.EmptyTimeFieldException();
    }

    /**
     *Constructor with a missing time parameter throws exception
     * 
     * @param taskId -
     * @param startTime -
     * @param comment -
     * @throws EmptyTimeFieldException if any time parameter missing
     */
    public Task(String taskId, String startTime, String comment) throws EmptyTimeFieldException{
        throw new EmptyTimeFieldException();
    }
    
    /**
     *
     * Constructor with no taskID just throws an exception
     * @param i -
     * @param i0 -
     * @param i1 -
     * @param i2 -
     * @param cc -
     * @throws NoTaskIdException if there is no taskId
     */
    public Task(int i, int i0, int i1, int i2, String cc) throws NoTaskIdException {
        throw new NoTaskIdException();
    }
    
    /**
     *This constructor creates an empty comment parameter
     * 
     * @param taskId -
     * @param startHour -
     * @param startMin -
     * @param endHour -
     * @param endMin -
     * @throws InvalidTaskIdException if the taskId is not valid
     * @throws NotExpectedTimeOrderException if the endTime is less than startTime
     * @throws NoTaskIdException if there is no taskId
     * @throws EmptyTimeFieldException if any time parameter missing
     */
    public Task(String taskId, int startHour, int startMin, 
            int endHour, int endMin) throws NoTaskIdException, InvalidTaskIdException, NotExpectedTimeOrderException, EmptyTimeFieldException{
        if (taskId.isEmpty())
            throw new NoTaskIdException();
        this.taskId = taskId;
        if (!isValidTaskId())
            throw new InvalidTaskIdException();
        startTime = LocalTime.of(startHour, startMin);
        endTime = LocalTime.of(endHour, endMin);
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        this.comment = "";
        if (endTime.compareTo(startTime) < 0 )
            throw new NotExpectedTimeOrderException();
    }
    // ----------------------------------------------------------------------
    
    // ---------------------------- Getters --------------------------------

    /**
     *
     * @return taskId
     */
    public String getTaskId(){
        return taskId;
    }
    
    /**
     *
     * @return startTime
     */
    public LocalTime getStartTime(){
        return startTime;
    }
    
    /**
     *
     * @return endTime
     */
    public LocalTime getEndTime(){
        return endTime;
    }
    
    /**
     *
     * @return comment
     */
    public String getComment(){
        return comment;
    }
    
    /**
     *
     * @return with a long number (minutes) what calculated from the different of startTime and endTime 
     * @throws EmptyTimeFieldException if any time parameter missing
     */
    public long getMinPerTask() throws EmptyTimeFieldException{
        if (startTime == null || endTime == null)
            throw new EmptyTimeFieldException();
        return (long) (( endTime.getHour() - startTime.getHour()) * 60
                + endTime.getMinute() - startTime.getMinute());
    }
    // ---------------------------------------------------------------------
    
    // ----------------------------- Methods -------------------------------

    /**
     * 
     * @return true if taskId consists of 4 digits
     */
    private boolean isValidRedmineTaskId(){
        Pattern p  = Pattern.compile(REGEX1);
        Matcher m = p.matcher(taskId);
        
        return m.matches();
    }
    
    /**
     * 
     * @return true if taskId consists of "LT-" and 4 digits
     */
    private boolean isValidLTTaskId(){
        Pattern p  = Pattern.compile(REGEX2);
        Matcher m = p.matcher(taskId);
        
        return m.matches();
    }
    
    /**
     *
     * @return true if the taskId is a valid LT TaskId or a valid Redmine TaskId
     */
    private boolean isValidTaskId(){
        return (isValidRedmineTaskId() || isValidLTTaskId());
    }
    
    @Override
    public String toString(){
        return taskId + " " + startTime + " " + endTime + " " + comment;
    }
    // ---------------------------------------------------------------------
    
    // ------------------------------ Setters -----------------------------

    /**
     *
     * @param id's value will be the value of taskId
     * @throws NoTaskIdException if there is no taskId
     * @throws InvalidTaskIdException if the taskId isn't valid
     */
    public void setTaskId(String id) throws NoTaskIdException, InvalidTaskIdException{
        if (id == null)
            throw new NoTaskIdException();
        taskId = id;
        if (!isValidTaskId())
            throw new InvalidTaskIdException();
    }
    
    /**
     *
     * @param hour startTime's hour paramater
     * @param min startTime's minute paramater
     * @throws NotExpectedTimeOrderException if endTime is less then startTime
     * @throws EmptyTimeFieldException if any time paramater doesn't exist
     */
    public void setStartTime(int hour, int min) throws NotExpectedTimeOrderException, EmptyTimeFieldException{
        startTime = LocalTime.of(hour, min);
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *
     * @param time startTime's value
     * @throws NotExpectedTimeOrderException if endTime is less then startTime
     * @throws EmptyTimeFieldException if any time paramater doesn't exist
     */
    public void setStartTime(String time) throws NotExpectedTimeOrderException, EmptyTimeFieldException{
        startTime = LocalTime.parse(time);
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *
     * @param time startTime's value
     * @throws NotExpectedTimeOrderException if endTime is less then startTime
     * @throws EmptyTimeFieldException if any time paramater doesn't exist
     */
    public void setStartTime(LocalTime time) throws NotExpectedTimeOrderException, EmptyTimeFieldException{
        startTime = time;
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *
     * @param hour endTime's hour parameter
     * @param min endTime's minute paramater
     * @throws NotExpectedTimeOrderException if endTime is less then startTime
     * @throws EmptyTimeFieldException if any time paramater doesn't exist
     */
    public void setEndTime(int hour, int min) throws NotExpectedTimeOrderException, EmptyTimeFieldException{
        endTime = LocalTime.of(hour, min);
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *
     * @param time endTime's value
     * @throws NotExpectedTimeOrderException if endTime is less then startTime
     * @throws EmptyTimeFieldException if any time paramater doesn't exist
     */
    public void setEndTime(String time) throws NotExpectedTimeOrderException, EmptyTimeFieldException{
        endTime = LocalTime.parse(time);
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *
     * @param time endTime's value
     * @throws NotExpectedTimeOrderException if endTime is less then startTime
     * @throws EmptyTimeFieldException if any time paramater doesn't exist
     */
    public void setEndTime(LocalTime time) throws NotExpectedTimeOrderException, EmptyTimeFieldException{
        endTime = time;
        endTime = Util.roundToMultipleQuarterHour(startTime, endTime);
        if (startTime.compareTo(endTime) > 0)
            throw new NotExpectedTimeOrderException();
    }
    
    /**
     *
     * @param comment's value
     */
    public void setComment(String comment){
        this.comment = comment;
    }
    // ------------------------------------------------------------------
}
