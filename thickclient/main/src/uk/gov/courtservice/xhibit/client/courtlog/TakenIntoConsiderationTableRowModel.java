package uk.gov.courtservice.xhibit.client.courtlog;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;

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

public class TakenIntoConsiderationTableRowModel {

    private DefendantOnCaseValue defendantOnCase;

    private String fullName;

    private Integer originalNoOfTics;

    /**
     * Construct a TakenIntoConsiderationTableRowModel from the defendantOnCase
     * and fullName, this is used when adding a court log event
     */
    public TakenIntoConsiderationTableRowModel(DefendantOnCaseValue defendantOnCase, String fullName) {
        this.defendantOnCase = defendantOnCase;
        this.fullName = fullName;
        this.originalNoOfTics = defendantOnCase.getNoOfTics();
    }

    /**
     * Construct a TakenIntoConsiderationTableRowModel from the defendantOnCase,
     * fullName and noOfTICS (value recorded in event), this is used when
     * editing a court log event
     */
    public TakenIntoConsiderationTableRowModel(DefendantOnCaseValue defendantOnCase, String fullName, Integer noOfTics) {
        this.defendantOnCase = defendantOnCase;
        this.fullName = fullName;
        this.originalNoOfTics = noOfTics;
        defendantOnCase.setNoOfTics(noOfTics);
    }

    public Integer getDefendantId() {
        return defendantOnCase.getDefendantOnCaseBVO().getDefendantID();
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getOriginalNoOfTics() {
        return originalNoOfTics;
    }

    public Integer getNoOfTics() {
        return defendantOnCase.getNoOfTics();
    }

    public void setNoOfTics(Integer noOfTics) {
        defendantOnCase.setNoOfTics(noOfTics);
    }

    public DefendantOnCaseValue getDefendantOnCase() {
        return defendantOnCase;
    }
}