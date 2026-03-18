package uk.gov.courtservice.xhibit.business.vos.services.charge;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: JoinderChargeInfoValue
 * </p>
 * <p>
 * Description: JoinderChargeInfoValue is intended to represent minimal info
 * about all joinder charges. It is intended for use in as an array in
 * ChargeValue for charges that have been joindered. It is immutable
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: JoinderChargeInfoValue.java,v 1.2 2004/06/09 17:22:10 czvsws
 *          Exp $
 */

public class JoinderChargeInfoValue extends CSAbstractValue {
    private String caseType;

    private Integer caseNumber;

    private Integer crestChargeSeqNo;

    private Integer chargeId;
    
    private static final long serialVersionUID = -7953284360805375037L;

    public JoinderChargeInfoValue(String caseType, Integer caseNumber, Integer crestChargeSeqNo, Integer chargeId) {
        this.caseType = caseType;
        this.caseNumber = caseNumber;
        this.crestChargeSeqNo = crestChargeSeqNo;
        this.chargeId = chargeId;
    }

    public String getCaseType() {
        return caseType;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public Integer getCrestChargeSeqNo() {
        return crestChargeSeqNo;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getChargeId() {
        return chargeId;
    }
}