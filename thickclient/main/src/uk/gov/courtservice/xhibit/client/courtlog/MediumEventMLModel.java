package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
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
 * @author unascribed
 * @version 1.0
 */
public class MediumEventMLModel extends FreeTextModel {
    protected String subSchema;

    protected int selectedIndex;

    protected String selectedItem;

    protected String selectedItemCode;

    private boolean selectionRequired;
    
    private DefendantOnCaseBasicValue defOnCase;

    public MediumEventMLModel() {
        super();
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

    public void setSelectionRequired(boolean selectionRequired) {
        this.selectionRequired = selectionRequired;
    }

    public boolean isSelectionRequired() {
        return selectionRequired;
    }

    public DefendantOnCaseBasicValue getDefOnCase() {
		return defOnCase;
	}

	public void setDefOnCase(DefendantOnCaseBasicValue defOnCase) {
		this.defOnCase = defOnCase;
	}

	// Utility methods
    public void printModel() {
        XHIBITConstant.info("MediumEventMLModel");
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
        XHIBITConstant.info("SelectedItemCode        : " + getSelectedItemCode());
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
    }
}