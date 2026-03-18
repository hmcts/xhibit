package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;


import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddress;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import org.apache.log4j.Logger;


/**
 * <p>
 * Title: OffenceAddressRule
 * </p>
 * <p>
 * Description: Validates the offence has the bichard mandatory fields
 * address line 1 and address line 2.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 *
 * @version $Id: OffenceAddressRule.java,v 1.3 2009/04/16 12:20:08 hewittm Exp $
 */

public class OffenceAddressRule implements OffenceRule {
    private static final Logger log = CSServices.getLogger(OffenceAddressRule.class);

    private static final OffenceAddressRule instance = new OffenceAddressRule();

    private OffenceAddressRule() {
        // empty
    }

    public static OffenceAddressRule getInstance() {
        return instance;
    }

    
    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        log.debug("process OffenceAddressRule - BEGIN");
        
        if (hasValidAddress(offence)) {
            return new String[] {};
        } else {
            log.info("No offence address:" + defendantOnOffence.getDefendantOnOffenceId());
            return new String[] { "authorise.offence.address.missingdetails" }; 
        }
    }
    
    
    /*
     *  Return true if the offence has all the mandatory Bichard address details.
     */
    private boolean hasValidAddress(OffenceValue offence) {

        if (offence.getAddressId() == null) {
            return false;
        }
        
        /*
         * The AddressValue is left empty by
         * GetChargesHelper.populateOffenceValue
         * 
        AddressValue addressValue = offence.getAddressValue();
        if (addressValue == null) {
            return false;
        }
        if (addressValue.getAddress1() == null 
                || addressValue.getAddress1().equals("")) {
            return false;
        }
        if (addressValue.getAddress2() == null 
                || addressValue.getAddress2().equals("")) {
            return false;
        }
        */
        try {
            XhbAddress address =
                XhbAddressBeanHelper2.findByPrimaryKey(offence.getAddressId());
            
            if (address == null) {
                return false;
            }
            if (address.getAddress1() == null 
                    || address.getAddress1().equals("")) {
                return false;
            }
            if (address.getAddress2() == null 
                    || address.getAddress2().equals("")) {
                return false;
            }
            
        } catch (XhbAddressBeanNotFoundException e) {
            return false;
        }
        
        return true;
    }
}
