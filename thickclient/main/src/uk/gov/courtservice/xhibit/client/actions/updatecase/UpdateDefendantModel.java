package uk.gov.courtservice.xhibit.client.actions.updatecase;

import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

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
 * @author Frederik Vandendriessche
 * @version 1.0 Updating a Defendant in XHIBIT 2 requires Case information to be
 *          passed to the XHIBIT 2 Mid Tier. This model holds all information
 *          passed around in the GUI to update a defendant.
 */

public class UpdateDefendantModel {
    private Integer defendantID;

    private DefendantValue defendantValue;

    private CaseStatusValue caseStatusValue;

    private DefendantOnCaseValue defendantOnCaseValue;

    private OpenAmendDefendantAction action;
    
    private boolean showPublicDisplayHidingControls = false;

    private boolean update;

    /**
     * Indicates the update mode (normal / only the Crest Form A specific. def.
     * data
     */
    private boolean crestFormAFields;

    public UpdateDefendantModel(Integer defendantID, CaseStatusValue caseStatusValue) {
        this(defendantID, caseStatusValue, false);
    }

    public UpdateDefendantModel(Integer defendantID, Integer caseID, boolean inCourt) {
        this(defendantID, new CaseStatusValue(caseID, inCourt), false);
    }

    public UpdateDefendantModel(Integer defendantID, Integer caseID, boolean inCourt, boolean onlyCrestFormA) {
        this(defendantID, new CaseStatusValue(caseID, inCourt), onlyCrestFormA);
    }

    public UpdateDefendantModel(Integer defendantID, CaseStatusValue caseStatusValue, boolean onlyCrestFormA) {
        this.defendantID = defendantID;
        this.caseStatusValue = caseStatusValue;
        this.crestFormAFields = onlyCrestFormA;
    }

    public Integer getDefendantID() {
        return this.defendantID;
    }

    public CaseStatusValue getCaseStatusValue() {
        return this.caseStatusValue;
    }

    public DefendantValue getDefendantValue() {
        return this.defendantValue;
    }

    public void setDefendantValue(DefendantValue defendantValue) {
        this.defendantValue = defendantValue;
    }

    public boolean isCrestFormAFieldsOnly() {
        return this.crestFormAFields;
    }

    public void setDefendantOnCaseValue(DefendantOnCaseValue x) {
        this.defendantOnCaseValue = x;
    }

    public DefendantOnCaseValue getDefendantOnCaseValue() {
        return this.defendantOnCaseValue;
    }

    public void setAction(OpenAmendDefendantAction param) {
        this.action = param;
    }

    public OpenAmendDefendantAction getAction() {
        return this.action;
    }

    public boolean getUpdate() {
        return this.update;
    }

    public void setUpdate(boolean param) {
        this.update = param;
    }
    
    public void setShowPublicDisplayHidingControls(boolean param) {
        this.showPublicDisplayHidingControls = param;
    }
    
    public boolean isShowPublicDisplayHidingControls() {
        return showPublicDisplayHidingControls;
    }
}
