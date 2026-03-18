package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: EditAdvocateWizardModel
 * </p>
 * <p>
 * Description: The model for the edit advocate panel.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class EditAdvocateWizardModel implements Cloneable {

    private FindInstructedAdvocateTableRowModel trm;

    private XhibitApplicationController xac;
  
    private String barristerType;
    
    private String newInstructedAdvocateDefenceCategory;
    
    private Integer newInstructedAdvocateCrestPostNumber;
    

    public EditAdvocateWizardModel() {
        // empty
    }

    // Getters
    public String getBarristerType() {
        return barristerType;
    }
    
    public FindInstructedAdvocateTableRowModel getSubstitutedInstructedAdvocateTableRowModel() {
        return trm;
    }
    
    public XhibitApplicationController getXac() {
        return xac;
    }
    
    public String getNewInstructedAdvocateDefenceCategory() {
        return newInstructedAdvocateDefenceCategory;
    }
    
    public Integer getNewInstructedAdvocateCrestPostNumber() {
        return newInstructedAdvocateCrestPostNumber;
    }

    // Setters
    public void setBarristerType(String param) {
        barristerType = param;
    }
    
    public void setSubstitutedInstructedAdvocateTableRowModel(FindInstructedAdvocateTableRowModel param) {
        trm = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }
    
    public void setNewInstructedAdvocateDefenceCategory(String newInstructedAdvocateDefenceCategory) {
        this.newInstructedAdvocateDefenceCategory = newInstructedAdvocateDefenceCategory;
    }
    
    public void setNewInstructedAdvocateCrestPostNumber(Integer newInstructedAdvocateCrestPostNumber) {
        this.newInstructedAdvocateCrestPostNumber = newInstructedAdvocateCrestPostNumber;
    }

    // Utility methods
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setBarristerType(null);
        setSubstitutedInstructedAdvocateTableRowModel(null);
        setXac(null);
    }
    
    public boolean isBarristerTypeSet() {
        return getBarristerType() != null;
    }
    
    public boolean isInstructedAdvocate() {
        return getBarristerType() != null 
            && getBarristerType().equals(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG);
    }
    
    public boolean isSubstituteAdvocate() {
        return getBarristerType() != null 
            && getBarristerType().equals(InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG);
    }
}

