package tlog16;

/**
 *Application calls only one method from the TimeLoggerUI class
 * @author lysharnie
 */
public class TLOG16 {
    public static void main(String[] args)  {
        TimeLoggerUI ui = TimeLoggerUI.getInstance();
        ui.menu();
    }
}
