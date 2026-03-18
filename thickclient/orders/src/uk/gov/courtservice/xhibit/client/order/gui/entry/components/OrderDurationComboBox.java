package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomDurationComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.helpers.DateFieldHelper;
import uk.gov.courtservice.xhibit.client.util.ComboBoxRendererRightJustify;

/**
 * <p>
 * Title: OrderDurationComboBox. Class that utilises the CustomDurationComboBox
 * class to create 3 JComboBox components.
 * </p>
 * <p>
 * Description: This class will display all or a combination of Years, Day,
 * Month comboboxes. This is determined by checking xml attributes that are set
 * to either true or false in DataEntryTemplate xml file. Each combobox is
 * populated with values by calling appropriate methods on the DateFieldHelper
 * class.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Des Johnston & David Duncan
 * @version 1.0
 */
public class OrderDurationComboBox extends AbstractOrderComponent implements ItemListener {
    private static final Logger log = CSServices.getLogger(OrderDurationComboBox.class);

    // Component to display the day combobox.
    private CustomDurationComboBox dayCb;
    
    //Component to display the week combobox.
    private CustomDurationComboBox weekCb;

    // Component to display the month combobox.
    private CustomDurationComboBox monthCb;

    // Component to display the year combobox.
    private CustomDurationComboBox yearCb;

    // Instantiate this class so comboboxes can be populated with appopriate
    // court name lists.
    private DateFieldHelper dateHelper = new DateFieldHelper();

    // String array that holds the combobox reference.
    private String initialYear = new String("");

    private String initialMonth = "";
    
    private String initialWeek = new String("");

    private String initialDay = new String("");

    // Create four JLabels.
    private JLabel yearLabel = new JLabel("Years:");

    private JLabel monthLabel = new JLabel("Months:");
    
    private JLabel weekLabel = new JLabel("Weeks:");

    private JLabel dayLabel = new JLabel("Days:");

    private String dayRef;
    
    private String weekRef;

    private String monthRef;

    private String yearRef;

    /**
     * Create three JComboBox components to represent Day, Week, Month and Year
     * Duration values.
     */
    
    public void initComponent() {
        log.debug("[initComponent]");
        setReferences();
        String years = getHelper().getAttribute("yearsMax");
        if (years != null) {
            dateHelper.setYearsMax(Integer.parseInt(years));
        }
        String months = getHelper().getAttribute("monthsMax");
        if (months != null) {
            dateHelper.setMonthsMax(Integer.parseInt(months));
        }
        String weeks = getHelper().getAttribute("weeksMax");
        if (weeks != null) {
            dateHelper.setWeeksMax(Integer.parseInt(weeks));
        }
        String days = getHelper().getAttribute("daysMax");
        if (days != null) {
            dateHelper.setDaysMax(Integer.parseInt(days));
        }
        
        
        
        yearCb = new CustomDurationComboBox(dateHelper.getYear(), yearRef, getHelper().getValue(yearRef));
        monthCb = new CustomDurationComboBox(dateHelper.getMonth(), monthRef, getHelper().getValue(monthRef));
        weekCb = new CustomDurationComboBox(dateHelper.getWeek(), weekRef, "0");
        dayCb = new CustomDurationComboBox(dateHelper.getDay(), dayRef,getHelper().getValue(dayRef));
        
        dayCb.addItemListener(this);
        weekCb.addItemListener(this);
        monthCb.addItemListener(this);
        yearCb.addItemListener(this);

        ComboBoxRendererRightJustify renderer = new ComboBoxRendererRightJustify();
        dayCb.setRenderer(renderer);
        weekCb.setRenderer(renderer);
        monthCb.setRenderer(renderer);
        yearCb.setRenderer(renderer);
        setVisualComponent(this.createPanel());
        
        
    }

    /**
     * Create a JPanel to display combobox components. Find the value of xml
     * attributes, these are set as either true or false. If false the related
     * combobox will not be displayed.
     * 
     * @return Return a JPanel
     */
    private JPanel createPanel() {
        this.setLayout(new FlowLayout());
        if (getHelper().getAttribute("yearattribute").equals("true")) {
            this.add(yearLabel);
            this.add(yearCb);
            initialYear = getHelper().getValue(yearRef);
            yearCb.setSelectedItem(initialYear);
        }
        if (getHelper().getAttribute("monthattribute").equals("true")) {
            this.add(monthLabel);
            this.add(monthCb);
            if (getHelper().getAttribute("defaultrequired").equals("true")) {
                String duration = getHelper().getValue(monthRef);
                if (duration == null) {
                    duration = (getHelper().getAttribute("defaultdurationvalue"));
                }
                monthCb.setSelectedItem(duration);

            } else if (getHelper().getAttribute("defaultrequired").equals("false")) {
                initialMonth = getHelper().getValue(monthRef);
                monthCb.setSelectedItem(initialMonth);
            }
        }
        if (getHelper().getAttribute("weekattribute").equals("true")) {
            this.add(weekLabel);
            this.add(weekCb);
            initialWeek = getHelper().getValue(weekRef);
            weekCb.setSelectedItem(initialWeek);
        }
        if (getHelper().getAttribute("dayattribute").equals("true")) {
            this.add(dayLabel);
            this.add(dayCb);
            initialDay = getHelper().getValue(dayRef);
            dayCb.setSelectedItem(initialDay);
        }

        return this;
    }

    /**
     * Place the xml duration field references into a string array.
     */
    private void setReferences() {
        String ref = getHelper().getAttribute("ref1");
        if (ref != null) {
            dayRef = ref + "/ord:Days";
            weekRef = ref + "/ord:Weeks";
            monthRef = ref + "/ord:Months";
            yearRef = ref + "/ord:Years";
        }
    }

    /**
     * Update the xml values on the preview screen when duartion combobox values
     * are changed.
     * 
     * @param e
     *            Invoked when an item has been selected or deselected.
     */
    public void itemStateChanged(ItemEvent e) {
        CustomDurationComboBox b = ((CustomDurationComboBox) e.getSource());
        String ref = b.getRef();

        getHelper().setValues(ref, e.getItem().toString());
    }

    /**
     * Sets the values on the dom from the three combo boxes within the panel.
     * This ensures that the values on the order are updated correctly as the
     * user selects/deselects the various options.
     * 
     * @param enabled
     *            to indicate that the component has been enabled
     */
    public void setEnabled(boolean enabled) {
    	if (isD20Order() && !isReadOnly() && isSwitchedOn()) {
    		enabled = true;
    	}
    	
        if (enabled) {
            // Days combo box
            String dayRef = dayCb.getRef();
            String defaultValue = "";
            if (dayCb.getSelectedItem() != null) {
            	getHelper().setValues(dayRef, dayCb.getSelectedItem().toString());
            } else {
            	getHelper().setValues(dayRef, defaultValue);
            }
            
            // Weeks combo box
            String weekRef = weekCb.getRef();
            // Ignore this step for D20 orders and Period of Compliance on YRO
            if (dayCb.getSelectedItem() != null) {
            	if ((weekRef.indexOf("OffenceDisqualifiedPeriod") <= 0) && (weekRef.indexOf("PeriodOfCompliance") <= 0)
	            		&& (weekRef.indexOf("OffencePPSCO") <= 0)) {
	                getHelper().setValues(weekRef, weekCb.getSelectedItem().toString());
	            }
            } else {
            	if ((weekRef.indexOf("OffenceDisqualifiedPeriod") <= 0) && (weekRef.indexOf("PeriodOfCompliance") <= 0)
	            		&& (weekRef.indexOf("OffencePPSCO") <= 0)) {
	                getHelper().setValues(weekRef, defaultValue);
	            }
            }

            // Months combo box
            String monRef = monthCb.getRef();
            if (monthCb.getSelectedItem() != null) {
            	getHelper().setValues(monRef, monthCb.getSelectedItem().toString());
            } else {
            	getHelper().setValues(monRef, defaultValue);
            }

            // Years combo box
            String yearRef = yearCb.getRef();
            if (yearCb.getSelectedItem() != null) {
            	getHelper().setValues(yearRef, yearCb.getSelectedItem().toString());
            } else {
            	getHelper().setValues(yearRef, defaultValue);
            }
        }
        
        yearCb.setEnabled(enabled);
        monthCb.setEnabled(enabled);
        weekCb.setEnabled(enabled);
        dayCb.setEnabled(enabled);
        // set the labels on the comboboxes
        yearLabel.setEnabled(enabled);
        monthLabel.setEnabled(enabled);
        weekLabel.setEnabled(enabled);
        dayLabel.setEnabled(enabled);
        
        this.validateTree();
    }
}
