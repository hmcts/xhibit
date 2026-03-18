package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT2
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
public class ChargesSelectionModel implements Cloneable {
    public static final int CHARGE_SELECTION_TYPE = 1;

    public static final int OFFENCE_SELECTION_TYPE = 2;

    public static final int DEFENDANT_SELECTION_TYPE = 3;

    private uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue cv = null;

    private OffenceValue ov = null;

    private uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue dv = null;

    private int selectionType = -1;

    public ChargesSelectionModel() {
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue getChargeValue() {
        return cv;
    }

    public void setChargeValue(uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue cv) {
        this.cv = cv;
    }

    public OffenceValue getOffenceValue() {
        return ov;
    }

    public void setOffenceValue(OffenceValue ov) {
        this.ov = ov;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue getDefendantValue() {
        return dv;
    }

    public void setDefendantValue(uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue dv) {
        this.dv = dv;
    }

    public int getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public void printModel() {
        try {
            XHIBITConstant.debug("==== S T A R T   P R I N T    M O D E L ====");
            XHIBITConstant.debug("Charge value     = " + cv.getChargeType());
            XHIBITConstant.debug("Offence value    = " + ov.getOffenceDescription());
            XHIBITConstant.debug("Defendant value  = " + dv.getFirstName() + " " + dv.getSurName());
            XHIBITConstant.debug("selectionType    = " + selectionType);
            XHIBITConstant.debug("====   E N D    P R I N T    M O D E L  ====");
        } catch (Exception e) {
            XHIBITConstant.debug("ChargesSelectionModel: printModel: " + e + " : " + e.getMessage());
        }
    }
}