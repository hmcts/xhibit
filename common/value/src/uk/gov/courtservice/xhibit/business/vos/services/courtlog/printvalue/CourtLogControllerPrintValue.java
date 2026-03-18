package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

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
public class CourtLogControllerPrintValue extends CSAbstractValue {
    private Collection courtLogControllerPrintCompositeValue;
    private static final long serialVersionUID = 255841842344161652L;

    public CourtLogControllerPrintValue() {
    }

    public Collection getCourtLogControllerPrintCompositeValue() {
        return courtLogControllerPrintCompositeValue;
    }

    public void setCourtLogControllerPrintCompositeValue(Collection param) {
        courtLogControllerPrintCompositeValue = param;
    }
}
