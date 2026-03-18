package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class AddEditWitnessModel implements Cloneable {
    private boolean saveClicked;

    private String witnessName;

    private WitnessSwornModel witSwornModel;
    
    private WitnessReadModel witReadModel;

    public AddEditWitnessModel() {
        super();
    }

    // Getters
    public boolean isSaveClicked() {
        return saveClicked;
    }

    public WitnessSwornModel getWitSwornModel() {
        return witSwornModel;
    }
    
    public WitnessReadModel getWitReadModel() {
        return witReadModel;
    }

    public String getWitnessName() {
        return witnessName;
    }

    // Setters
    public void setSaveClicked(boolean param) {
        saveClicked = param;
    }

    public void setWitSwornModel(WitnessSwornModel m) {
        witSwornModel = m;
    }
    
    public void setWitReadModel(WitnessReadModel m) {
        witReadModel = m;
    }

    public void setWitnessName(String text) {
        witnessName = text;
    }

    public void printModel() {
        XHIBITConstant.info("AddEditWitnessModel");
        XHIBITConstant.info("-------------------");
        XHIBITConstant.info("Save Clicked?   : " + isSaveClicked());
        XHIBITConstant.info("Name            : " + getWitnessName());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setSaveClicked(false);
        setWitSwornModel(null);
        setWitnessName(null);
    }
}
