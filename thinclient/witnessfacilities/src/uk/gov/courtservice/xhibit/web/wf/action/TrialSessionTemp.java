/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Apr 14, 2003
 * Time: 1:39:47 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.web.wf.action;

import java.util.Date;

public class TrialSessionTemp {
    String dayNumber;

    Date appearanceDate;

    String sessionType;

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setAppearanceDate(Date appearanceDate) {
        this.appearanceDate = appearanceDate;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getDayNumber() {
        return this.dayNumber;
    }

    public Date getAppearanceDate() {
        return this.appearanceDate;
    }

    public String getSessionType() {
        return this.sessionType;
    }

    /*
     * public static void main(String[] args){ SimpleDateFormat f = new
     * SimpleDateFormat("dd-MM-yy"); try { Date d = f.parse("2003-04-15
     * 00:00:00.0"); } catch (ParseException e) { e.printStackTrace(); } }
     */
}
