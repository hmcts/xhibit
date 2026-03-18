package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: The model for counsel sign in.
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

public class CounselSignInModel implements Cloneable {

    private Collection assignedReps;

    private XhibitApplicationController xac;

    private FindLegalRepresentativeTableRowModel legalRep;

    public CounselSignInModel() {
        // empty
    }

    // Getters
    public Collection getAssignedReps() {
        return assignedReps;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public FindLegalRepresentativeTableRowModel getLegalRep() {
        return legalRep;
    }

    // Setters
    public void setAssignedReps(Collection param) {
        assignedReps = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public void setLegalRep(FindLegalRepresentativeTableRowModel param) {
        legalRep = param;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("CounselSignInModel");
        XHIBITConstant.info("------------------");
        XHIBITConstant.info("XAC                 : " + getXac());
        if (getLegalRep() != null) {
            getLegalRep().printModel();
        }
    }

    public void printModel(boolean withAssignedReps) {
        this.printModel();

        if (withAssignedReps) {
            if (getAssignedReps() != null) {
                Iterator iter = getAssignedReps().iterator();
                while (iter.hasNext()) {
                    AssignRepresentativesTableRowModel item = (AssignRepresentativesTableRowModel) iter.next();

                    item.printModel();
                }
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setAssignedReps(null);
        setXac(null);
        setLegalRep(null);
    }
}
