package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: SelectDefendantsToRemoveRowModel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Krishna Pokala
 * 
 */

public class SelectDefendantsToRemoveRowModel {
    private Integer defendantId;

    private Integer defendantOnOffenceId;

    private String defendantName;

    private Boolean removeFromCount;

    // get methods
    public String getDefendantName() {
        return (defendantName);
    }

    // set methods
    public void setDefendantName(String defendantName) {
        this.defendantName = defendantName;
    }

    // utility
    public void printModel() {
        XHIBITConstant.info("SelectDefendantsToRemoveRowModel");
        XHIBITConstant.info("-----------------------------");
        XHIBITConstant.info("defendantId      : " + getDefendantOnOffenceId());
        XHIBITConstant.info("defendantName      : " + getDefendantName());
        XHIBITConstant.info("removeFromCount: " + isRemoveFromCount());

    }

    public Boolean isRemoveFromCount() {
        return removeFromCount;
    }

    public void setRemoveFromCount(Boolean removeFromCount) {
        this.removeFromCount = removeFromCount;
    }

    public Integer getDefendantOnOffenceId() {
        return defendantOnOffenceId;
    }

    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        this.defendantOnOffenceId = defendantOnOffenceId;
    }

    public Integer getDefendantId() {
        return defendantId;
    }

    public void setDefendantId(Integer defendantId) {
        this.defendantId = defendantId;
    }
}
