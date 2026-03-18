package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;


import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;

import org.apache.log4j.Logger;


/**
 * <p>
 * Title: OffenceStartDateRule
 * </p>
 * <p>
 * Description: Validates the offence has the bichard mandatory field
 * start date.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 *
 * @version $Id: OffenceStartDateRule.java,v 1.2 2009/04/13 14:04:26 hewittm Exp $
 */

public class OffenceStartDateRule implements OffenceRule {
    private static final Logger log = CSServices.getLogger(OffenceStartDateRule.class);

    private static final OffenceStartDateRule instance = new OffenceStartDateRule();

    private OffenceStartDateRule() {
        // empty
    }

    public static OffenceStartDateRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        log.debug("process OffenceStartDateRule - BEGIN");
        
        if (offence.getOffenceStartDateTime() != null) {
                return new String[] {};
        } else {
            log.info("No offence start date:" + defendantOnOffence.getDefendantOnOffenceId());
            return new String[] { "authorise.offence.startdate" }; 
        }
    }
}

