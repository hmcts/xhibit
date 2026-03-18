package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class XDatePickerPanel extends XPanel {
    protected final Logger log = CSServices.getLogger(getClass());

    protected JPanel dateContainer;

    protected Date defaultDate = null;

    protected List collectionOfDates;

    protected Object[] listOfValidDates;

    protected final Dimension panelDim = new Dimension(100, XHIBITConstant.getLineHeight());

    protected JComboBox dropDownDate;

    private boolean required = true;

    private boolean constructed = false;

    public XDatePickerPanel(JPanel containingPanel, List collectionOfDates) {
        this(containingPanel, collectionOfDates, null);
    }

    public XDatePickerPanel(JPanel containingPanel, List collectionOfDates, Date defaultDate) {
        this.dateContainer = containingPanel;
        this.collectionOfDates = collectionOfDates;
        this.defaultDate = defaultDate;
        stepInitialise();
        jbInit();
        stepActivate();
        // to indicate that the constructor has finished (see
        // stepUpdateViewState)
        constructed = true;
    }

    public void stepInitialise() {
        setListOfValidDates(collectionOfDates);
    }

    public void stepActivate() {
        if (defaultDate != null)
            setDate(defaultDate);
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        if (dateContainer instanceof XPanel) {
            ((XPanel) dateContainer).stepUpdateViewState();
        }
    }

    public void stepValidate() {
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean save) {
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(getDimension());
        this.add(getDropDownDate(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
    }

    public JComboBox getDropDownDate() {
        if (dropDownDate == null) {
            dropDownDate = new JComboBox(listOfValidDates);
            dropDownDate.setPreferredSize(getDimension());
            dropDownDate.setToolTipText(XHIBITConstant.getResource(XhibitBundles.UtilResources, "LogDateMonths"));
            dropDownDate.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dropDownDate_ActionPerformed();
                }
            });
        }
        return dropDownDate;
    }

    void dropDownDate_ActionPerformed() {
        // only perform if not triggered indirectly by the constructor
        if (constructed) {
            try {
                stepUpdateViewState();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
        }
    }

    protected void setListOfValidDates(List dates) {
        Object[] datesArray = new Object[dates.size()];
        Iterator iter = dates.iterator();
        int x = 0;
        while (iter.hasNext()) {
            Date i = (Date) iter.next();
            datesArray[x] = XDateFormat.format(i, XDateFormat.DATEFORMAT);
            x++;
        }

        setListOfValidDates(datesArray);
    }

    protected void setListOfValidDates(Object[] dates) {
        this.listOfValidDates = dates;
    }

    //
    // getters and setters to match those of XDatePanel
    //

    /**
     * Return the text in the field. This does not guarantee the validity of the
     * date
     * 
     * @return
     */
    public String getText() {
        return getDropDownDate().getSelectedItem().toString();
    }

    /**
     * Set a new date for the field
     * 
     * @param newDate
     */
    public void setDate(Calendar newDate) {
        if (newDate == null) {
            return;
        } else {
            setDate(newDate.getTime());
        }
    }

    /**
     * Set a new date for the field
     * 
     * @param newDate
     */
    public void setDate(Date newDate) {
        int i = 0;
        boolean found = false;
        String matchDate = XDateFormat.format(newDate, XDateFormat.DATEFORMAT);
        for (i = 0; i < listOfValidDates.length; i++) {
            if (listOfValidDates[i].equals(matchDate)) {
                found = true;
                break;
            }
        }
        if (found)
            dropDownDate.setSelectedIndex(i);
    }

    /**
     * Set if the user is required to complete the field Defaults to true
     * 
     * @param newValue
     */
    public void setRequired(boolean newValue) {
        required = newValue;
    }

    /**
     * Set whether the display of the field is enabled. Defaults to true
     * 
     * @param newValue
     */
    public void setDateEnabled(boolean newValue) {
        dropDownDate.setEnabled(newValue);
    }

    /**
     * Set whether the field can be editted Defaults to true
     * 
     * @param newValue
     */
    public void setDateEditable(boolean newValue) {
        dropDownDate.setEditable(newValue);
    }

    public Date getDate() {
        return (Date) (collectionOfDates.get(getDropDownDate().getSelectedIndex()));
    }

    /**
     * Check if the user has populated text in the field. This does not check
     * the validity of the date.
     * 
     * @return true if there is data. If the field is not required, also returns
     *         true.
     */
    public boolean isMandatoryFieldsCompleted() {
        if (required) {
            if (getText().length() <= 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    protected Dimension getDimension() {
        return panelDim;
    }
}