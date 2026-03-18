package uk.gov.courtservice.xhibit.business.vos.services.counselfacilities;

import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: PrintPartyOnCaseValueList
 * </p>
 * <p>
 * Description: Class to be used for printing the list of counsels signed in.
 * This will only hold a Collection of PartyOnCaseValues.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class PrintPartyOnCaseValueList extends CSAbstractValue {
	
	private static final long serialVersionUID = 4529532392359260584L;

    private Collection partyOnCaseValueList;

    /**
     * Default, emtpy constructor
     */
    public PrintPartyOnCaseValueList() {
    }

    public void setPartyOnCaseValueList(Collection partyOnCaseValueList) {
        this.partyOnCaseValueList = partyOnCaseValueList;
    }

    public Collection getPartyOnCaseValueList() {
        return partyOnCaseValueList;
    }
}