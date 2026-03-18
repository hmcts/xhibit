package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class ChargeSummaryTableRow {
    private OffenceValue offenceValue;

    private DefendantValue defendantValue;

    private String count;

    private String offenceDescription;

    private String defendant;

    private String status;

    private Integer sortCount;
    
    private ChargeValue chargeValue;
    
    private String offenceCode;

    public ChargeSummaryTableRow(OffenceValue ov, DefendantValue dv, String count, String offenceDescription,
            String defendant, String status, ChargeValue chargeValue) {
        this.offenceValue = ov;
        this.defendantValue = dv;
        this.count = count;
        this.offenceDescription = offenceDescription;
        this.defendant = defendant;
        this.status = status;
        this.setChargeValue(chargeValue);
        this.sortCount = new Integer(count);
    }
    
    /**
     * For use with IndictementSummary on charge screen which needs the ChargeValue.
     * 
     */
    public ChargeSummaryTableRow(OffenceValue ov, DefendantValue dv, String count, String offenceDescription,
            String defendant, String status, ChargeValue chargeValue, String offenceCode) {
        this.offenceValue = ov;
        this.defendantValue = dv;
        this.count = count;
        this.offenceDescription = offenceDescription;
        this.defendant = defendant;
        this.status = status;
        this.setChargeValue(chargeValue);
        this.sortCount = new Integer(count);
    	this.offenceCode = offenceCode;
    }

    public OffenceValue getOffenceValue() {
        return offenceValue;
    }

    public DefendantValue getDefendantValue() {
        return defendantValue;
    }

    public String getCount() {
        return count;
    }

    public String getOffenceDescription() {
        return offenceDescription;
    }
    
    public String getOffenceCode() {
    	return offenceCode;
    }

    public String getDefendant() {
        return defendant;
    }

    public String getStatus() {
        return status;
    }
    
    public void setOffenceCode(String offenceCode) {
    	this.offenceCode = offenceCode;
    }

    public void setOffenceValue(OffenceValue offenceValue) {
        this.offenceValue = offenceValue;
    }

    public void setDefendantValue(DefendantValue defendantValue) {
        this.defendantValue = defendantValue;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setOffenceDescription(String offenceDescription) {
        this.offenceDescription = offenceDescription;
    }

    public void setDefendant(String defendant) {
        this.defendant = defendant;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSortCount() {
        return sortCount;
    }

    public void setSortCount(Integer sortCount) {
        this.sortCount = sortCount;
    }

    public void setChargeValue(ChargeValue chargeValue) {
        this.chargeValue = chargeValue;
    }

    public ChargeValue getChargeValue() {
        return chargeValue;
    }

}