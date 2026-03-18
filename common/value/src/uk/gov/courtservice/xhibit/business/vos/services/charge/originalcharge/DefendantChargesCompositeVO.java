package uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge;

import java.io.Serializable;

/**
 * <p>Title: DefendantChargesCompositeVO</p>
 * <p>Description: A composite VO that describes a Defendant On Case and their original charge Charge records.</p>
 * <p>It is used to transport data from the mid-tier to the client in order to populate the Original Charges screen.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
public class DefendantChargesCompositeVO implements Serializable {
    private DefendantOnCaseVO defendantOnCase;
    private ChargeVO[] charges;
    private ChargeVO[] obsoleteCharges;
    private static final long serialVersionUID = 7300466894905958918L;
    
    public DefendantChargesCompositeVO() {
        // Default Constructor
    }
    
    public DefendantChargesCompositeVO(DefendantOnCaseVO defendantOnCase) {
        this(defendantOnCase, null, null);
    }
    
    public DefendantChargesCompositeVO(DefendantOnCaseVO defendantOnCase, ChargeVO[] charges) {
        this(defendantOnCase, charges, null);
    }
    
    public DefendantChargesCompositeVO(DefendantOnCaseVO defendantOnCase, ChargeVO[] charges, ChargeVO[] obsoleteCharges) {
        this.defendantOnCase = defendantOnCase;
        this.charges = charges;
        this.obsoleteCharges = obsoleteCharges;
    }
    
    public ChargeVO[] getCharges() {
        return charges;
    }
    public void setCharges(ChargeVO[] charges) {
        this.charges = charges;
    }
    public DefendantOnCaseVO getDefendantOnCase() {
        return defendantOnCase;
    }
    public void setDefendantOnCase(DefendantOnCaseVO defendantOnCase) {
        this.defendantOnCase = defendantOnCase;
    }
    public ChargeVO[] getObsoleteCharges() {
        return obsoleteCharges;
    }
    public void setObsoleteCharges(ChargeVO[] obsoleteCharges) {
        this.obsoleteCharges = obsoleteCharges;
    }
}
