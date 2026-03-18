package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.Calendar.MFieldListener;
import mseries.ui.MSimpleDateFormat;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.util.XDateField;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: OrderDate. Generic date component - uses an XDateField to determine
 * the date.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Desmond Johnston
 * @version 1.0
 */
public class OrderDate extends AbstractOrderComponent {
    // Default date as held on blank schema
    private static final Logger log = CSServices.getLogger(OrderDate.class);

    private static final String DEFAULT_DATE = "0001-01-01";

    private static final String XSL_DATE_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat standardDateFormat;

    private static SimpleDateFormat xslDateFormat;

    // if you change MAX_DATE, change test in setDate method below
    private static final String MIN_PERMITTED_DATE = "01-01-1800";

    private static final String MAX_PERMITTED_DATE = "31-12-9999";

    private static SimpleDateFormat minMaxDateFormatter = null;

    // setup the date formats used for parsing.
    static {
        standardDateFormat = new SimpleDateFormat(XDateFormat.simpleDateFormat);
        xslDateFormat = new SimpleDateFormat(XSL_DATE_FORMAT);
        standardDateFormat.setLenient(true);
        minMaxDateFormatter = new SimpleDateFormat("dd-mm-yyyy");
        minMaxDateFormatter.setLenient(false);
    }

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    final XDateField entryField = new XDateField();

    private ResourceBundle datePanelResources;
    
    private JPanel panel;

    /**
     * Used to record the last valid date entered.
     */
    private Date oldDate = new Date();

    /**
     * Creates a new JPanel containing the date fields and sets it as a
     * VisualComponent for display.
     */

    public void initComponent() {
    	panel = this.createPanel();
        setVisualComponent(panel);
    }

    /**
     * 
     * @return JPanel with date fields.
     */
    private JPanel createPanel() {
        this.setLayout(gridBagLayout1);

        // Set up Pulldown constraints.
        MDefaultPullDownConstraints mdefaultpulldownconstraints = new MDefaultPullDownConstraints();
        mdefaultpulldownconstraints.firstDay = 2;
        mdefaultpulldownconstraints.changerStyle = 4;
        mdefaultpulldownconstraints.hasShadow = true;
        mdefaultpulldownconstraints.selectionClickCount = 1;

        // Setup entry Field
        entryField.setConstraints(mdefaultpulldownconstraints);
        entryField.setDateFormatter(new MSimpleDateFormat(XDateFormat.simpleDateFormat));
        entryField.setEditable(true);
        Dimension dM = new Dimension(100, XHIBITConstant.getLineHeight());
        Dimension dP = new Dimension(150, XHIBITConstant.getLineHeight());
        entryField.setPreferredSize(dP);
        entryField.setMinimumSize(dM);

        // reset the default limits of the DatePicker,
        // since by default it only allows 1-1-1970 to 31-12-2037
        try {
            entryField.setMinimum(minMaxDateFormatter.parse(MIN_PERMITTED_DATE));
            entryField.setMaximum(minMaxDateFormatter.parse(MAX_PERMITTED_DATE));
        } catch (ParseException pe) {
            log.warn("caught exception setting min/max date on MDateEntryField : " + pe);
        }

        // Setup tool tip text
        datePanelResources = XHIBITConstant.getResourceBundle(XhibitBundles.UtilResources);
        // include permitted date range in tool tip
        entryField.setToolTipText(XHIBITConstant.getResource(datePanelResources, "CalendarComponent")
                + " (Jan 1800 to Jan 9999)");

        // Add entry field.
        this.add(entryField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        // Get the date ready for display
        String storedDate = getHelper().getValue();
        
       
        if (storedDate != null ){
            storedDate = storedDate.trim();
        } else {
            log.debug("Date returned from data  for ref:" + getHelper().getOrderDataReference() + " was null.");
        }
        

        // Check that the date retrieved from xml is not the default date, as
        // set in the
        // blank schema (0000-01-01). Set the date accordingly. Will default to
        // today's
        // date from oldDate otherwise.
        if ((storedDate != null) && (storedDate.equalsIgnoreCase(DEFAULT_DATE) == false) && (storedDate.length() > 0) ) {
            java.util.Date xmlDate;
            try {
                xmlDate = xslDateFormat.parse(storedDate);
            } catch (ParseException ex) {
                log.error("Failure in Instantiation.", ex);
                xmlDate = new Date();
            }

            entryField.setValue(xmlDate);
            oldDate = xmlDate;
        } else if (isNullable()){
        	entryField.setValue(null);
        	getHelper().setValue("");
        }
        
        try {
        	setDate();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        createListeners();
        // Do nothing as part of XLC3-320; may be reinstated later
        /*if(isD20Order() && storedDate!=null && !storedDate.equals("0001-01-01")) {//} 
        	entryField.setEditable(false);
        }*/
        
        return this;
    }

    /**
     * Setup the listeners.
     */
    private void createListeners() {
        entryField.addMFieldListener(new MFieldListener() {
            public void fieldEntered(FocusEvent focusevent) {
                // set the focus to the beginning of the field
                entryField.getDisplay().setCaretPosition(0);
            }

            public void fieldExited(FocusEvent focusevent) {
            	if (isInitialised() && isReadOnly()) {
            		if (showConfirmOverrideMsg(getFrame())) {
            			setDate();
            		} else {
            			revertDate();
            		}
            	} else {
            		setDate();
            	}
            }
        });
    }
    
    public Frame getFrame() {
    	return getParentFrame(panel.getParent());
    }

    /**
     * Sets date correctly.
     */
    private void setDate() {
        String dateString = (entryField.getText());
        if (dateString != null && !"".equals(dateString)) {
	        Date trueDate = new Date();
	        try {
	            // Attempt to set new date.
	            trueDate = standardDateFormat.parse(dateString);
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(trueDate);
	            // we want to limit the user to dates between MIN and
	            // MAX_PERMITTED
	            // (test -100/8100 since Date class returns year relative to
	            // 1900)
	            // if changed, also change MIN/MAX_PERMITTED_DATE
	            if ((cal.get(Calendar.YEAR) < -100) || (cal.get(Calendar.YEAR) >= 8100)) {
	                log.error("date outside valid range (" + MIN_PERMITTED_DATE + "->" + MAX_PERMITTED_DATE + ")");
	                trueDate = oldDate;
	            }
	        } catch (ParseException ex) {
	            log.error("Failed to set date to: " + dateString, ex);
	            // Fall back to old date.
	            trueDate = oldDate;
	        }
	        entryField.setValue(trueDate);
	        oldDate = (Date) trueDate.clone();
	        getHelper().setValue(xslDateFormat.format(trueDate));
        }
    }
    
    private void revertDate() {
    	boolean isInitialised = isInitialised();
    	setInitialised(false);
    	entryField.setValue(oldDate);
    	setInitialised(isInitialised);
    }
    
	private boolean isNullable() {
		String readOnlyAttrib = getHelper().getAttribute("nullable");
        if (readOnlyAttrib != null) {
        	return "true".equalsIgnoreCase(readOnlyAttrib);
        }
        return false;
	}
	
	public boolean isD20DateOfSentenceIfDifferent() {
        return isD20Order() && 
        		getHelper().getOrderDataReference().contains("ord:OffenceDateOfSentence");	
	}
	
	public boolean isD20AppealCase() {
		String caseNumber = getHelper().getValue("//ord:D20/ord:OrderHeader/ord:CaseNumber");
		boolean isAppealCase = caseNumber != null && caseNumber.startsWith("A");
		return isD20Order() && isAppealCase;
	}
}
