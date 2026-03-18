package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author unascribed
 * @version 1.0
 */

public class AssignRepresentativesModel implements Cloneable {
    private XhibitApplicationController xac;

    private FindLegalRepresentativeTableRowModel legalRep;

    private AssignRepresentativesTableModel assignRepresentativesTablemodel;

    public AssignRepresentativesModel() {
        // empty
    }

    // Getters
    public XhibitApplicationController getXac() {
        return xac;
    }

    public FindLegalRepresentativeTableRowModel getLegalRep() {
        return legalRep;
    }

    public AssignRepresentativesTableModel getAssignRepresentativesTableModel() {
        return assignRepresentativesTablemodel;
    }

    // Setters
    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public void setLegalRep(FindLegalRepresentativeTableRowModel param) {
        legalRep = param;
    }

    public void setAssignRepresentativesTableModel(AssignRepresentativesTableModel param) {
        assignRepresentativesTablemodel = param;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("AssignRepresentativesModel");
        XHIBITConstant.info("--------------------------");
        XHIBITConstant.info("XAC                 : " + getXac());
        if (getLegalRep() != null) {
            getLegalRep().printModel();
        }
    }

    public void printModel(boolean withAssignedReps) {
        this.printModel();

        if (withAssignedReps) {
            if (getAssignRepresentativesTableModel() != null) {
                for (int x = 0; x < getAssignRepresentativesTableModel().getData().size(); x++) {
                    AssignRepresentativesTableRowModel item = (AssignRepresentativesTableRowModel) getAssignRepresentativesTableModel()
                            .getDataAt(x);
                    item.printModel();
                }
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setXac(null);
        setLegalRep(null);
        setAssignRepresentativesTableModel(null);
    }
}
