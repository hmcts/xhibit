package uk.gov.courtservice.xhibit.client.actions;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment (2003)
 * @version $Id: AbstractPreviewList.java,v 1.8 2006/06/05 12:31:01 bzjrnl Exp $
 */
public abstract class AbstractPreviewList extends XAction {    
    
    /**
     * Construct a new AbstractPreviewList action populating from the specified
     * resource bundle
     * 
     * @param key
     *            the bundle key
     */
    public AbstractPreviewList(String key) {
        super(key);
    }
    
    protected abstract String getTransformationStylesheetURL();

    /**
     * Preview the list
     * 
     * @param e
     *            the event that caused this method to be called
     * @throws Exception
     *             if an error occures
     */
    public void xActionPerformed(ActionEvent e) throws Exception {
        // Preview the formated list xml
        FOPFactory.getFOPRenderer(true).printDocument(getFormatedXml(), true, getShortDescription());
    }
    
    
    /**
     * Get todays dailylist xml for the currrent court formated for printing
     * 
     * @return String the xml
     * @throws Exception
     */
    protected String getFormatedXml() throws Exception {
        return XSLServices.getInstance().transform(getXml(), getTransformationStylesheetURL(), Locale.getDefault(), null);
    }

    /**
     * Implementations of this class use this method to determine what list xml
     * to preview
     * 
     * @return String the xml
     * @throws Exception
     */
    public abstract String getXml() throws Exception;

    /**
     * Get the current court id
     * 
     * @return Integer
     */
    protected static Integer getCurrentCourtId() {
        return XhibitSingleton.getInstance().getCourtId();
    }

    /**
     * Get the timestamp for today's date with the time set to 00:00:00.0000
     * 
     * @return Timestamp the populated time.
     */
    protected static Timestamp getTodaysTimeStamp() {
        Date today = DateTimeUtilities.stripTimeToUtilDate(Calendar.getInstance().getTime());
        return new Timestamp(today.getTime());
    }
}