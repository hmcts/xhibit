package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */

public class BailCustodyModel extends FreeTextModel {
    protected String subSchema;

    protected int selectedIndex;

    protected String selectedItem;

    protected String selectedItemCode;

    protected Calendar enteredDate;

    protected Collection defendantsArray;

    public BailCustodyModel() {
    }

    public String getSubSchema() {
        return subSchema;
    }

    public void setSubSchema(String s) {
        subSchema = s;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int s) {
        selectedIndex = s;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String s) {
        selectedItem = s;
    }

    public String getSelectedItemCode() {
        return selectedItemCode;
    }

    public void setSelectedItemCode(String s) {
        selectedItemCode = s;
    }

    public Calendar getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(Calendar c) {
        enteredDate = c;
    }

    public Collection getDefendantsArray() {
        return defendantsArray;
    }

    public void setDefendantsArray(Collection dArray) {
        defendantsArray = dArray;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("BailCustodyModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("In Edit Mode?           : " + isInEditMode());
        XHIBITConstant.info("DateTime                : " + getDateTime());
        XHIBITConstant.info("FreeText                : " + getFreeText());
        XHIBITConstant.info("EventId                 : " + getEventId());
        XHIBITConstant.info("EventType               : " + getEventType());
        XHIBITConstant.info("Schema                  : " + getSchema());
        XHIBITConstant.info("Sub Schema              : " + getSubSchema());
        XHIBITConstant.info("PanelText               : " + getPanelText());
        XHIBITConstant.info("XAC                     : " + getXac());
        XHIBITConstant.info("SelectedIndex           : " + getSelectedIndex());
        XHIBITConstant.info("SelectedItem            : " + getSelectedItem());
        XHIBITConstant.info("SelectedItem Code       : " + getSelectedItemCode());
        XHIBITConstant.info("enteredDate             : " + getEnteredDate());
        XHIBITConstant.info("Defendant Name          : " + getDefendantName());
        XHIBITConstant.info("------------------------- ");
    }

    public void printListOfDefendants(Collection defArray) {
        Iterator it = defArray.iterator();
        for (int i = 0; it.hasNext(); i++) {
            DefendantValue def = (DefendantValue) it.next();
            XHIBITConstant.info(" Defendant " + i + ": " + def.getSurName() + ", " + def.getFirstName());
        }
    }

    public void printListOfDefendants() {
        String[] dList = this.getXac().getApplicationCaseModel().getScheduledHearingValue().getDefendants();

        if (dList == null) {
            // NoAction
        } else {
            for (int i = 0; i < dList.length; i++) {
                XHIBITConstant.info(" Defendant " + i + ": " + dList[i]);
            }
        }
    }

    public void clearmodel() {
        setDateTime(null);
        setInEditMode(false);
        setFreeText(null);
        setEventId(null);
        setEventType(null);
        setSchema(null);
        setSubSchema(null);
        setPanelText(null);
        setXac(null);
        setSelectedIndex(0);
        setSelectedItem(null);
        setSelectedItemCode(null);
        setEnteredDate(null);
        setDefendantsArray(null);
    }
}