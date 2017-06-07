/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlog16;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lysharnie
 */
public class TimeLogger {
    // -------------------------- Variables ---------------------------
    private List<WorkMonth> months = new ArrayList<WorkMonth>();
    // ----------------------------------------------------------------
    
    // --------------------------- Methods ------------------------------
    public boolean isNewMonth(WorkMonth wm){
        boolean exist = false;
        for (WorkMonth m : months){
            if (m.getDate().equals(wm.getDate()))
                exist = true;
        }
        return !exist;
    }
    
    public void addMonth(WorkMonth wm){
        if (isNewMonth(wm))
            months.add(wm);
    }
    // --------------------------------------------------------------
    
    // --------------------------- Getters --------------------------
    public List<WorkMonth> getMonths(){
        return months;
    }
    // --------------------------------------------------------------
}
