package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
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
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
public class DefendantOnOffenceComplexValue extends XhbDefendantOnOffenceBasicValue {

    private static final long serialVersionUID = 1L;
    private DefendantOnCaseBasicValue defendantOnCase;

    public DefendantOnOffenceComplexValue() {
        // Empty
    }

    public DefendantOnOffenceComplexValue(Integer id, Integer version) {
        super();
        setDefendantOnOffenceId(id);
        setVersion(version);
    }

    public DefendantOnCaseBasicValue getDefendantOnCase() {
        return defendantOnCase;
    }

    public void setDefendantOnCase(DefendantOnCaseBasicValue defendantOnCase) {
        this.defendantOnCase = defendantOnCase;
    }


    /*
     * // WDF: original Results Now Stored in Disposal Table
     *
     * public void setOriginalResultComplexValue(OriginalResultComplexValue
     * originalResult) { this.originalResult = originalResult; }
     *
     * public OriginalResultComplexValue getOriginalResultComplexValue() {
     * return originalResult; }
     */

}