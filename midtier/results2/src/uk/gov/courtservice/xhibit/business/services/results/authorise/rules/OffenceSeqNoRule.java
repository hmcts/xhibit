package uk.gov.courtservice.xhibit.business.services.results.authorise.rules;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.services.results.authorise.OffenceRule;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import org.apache.log4j.Logger;


/**
 * <p>
 * Title: OffenceSeqNoRule
 * </p>
 * <p>
 * Description: Validates Seq Nos are present on all the Defendant On Offences
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version $Id: OffenceSeqNoRule.java,v 1.4 2009/04/13 13:51:07 hewittm Exp $
 */

public class OffenceSeqNoRule implements OffenceRule {
    private static final Logger log = CSServices.getLogger(OffenceSeqNoRule.class);

    private static final OffenceSeqNoRule instance = new OffenceSeqNoRule();

    private OffenceSeqNoRule() {
        // empty
    }

    public static OffenceSeqNoRule getInstance() {
        return instance;
    }

    public String[] process(ResultsCompositeValue rcv, OffenceValue offence, DefendantOnOffenceComplexValue defendantOnOffence) {
        log.debug("process OffenceSeqNoRule - BEGIN");

        if (defendantOnOffence.getSeqNo()!=null && defendantOnOffence.getSeqNo().intValue()!=0) {
            return new String[] {};
        } else {
            log.info("Seq No not recorded for:" + defendantOnOffence.getDefendantOnOffenceId());
            return new String[] { "authorise.seqno.notrecorded" }; 
        }
    }
}
