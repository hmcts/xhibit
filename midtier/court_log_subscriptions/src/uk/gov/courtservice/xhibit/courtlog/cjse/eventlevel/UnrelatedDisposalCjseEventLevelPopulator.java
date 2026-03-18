package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

public class UnrelatedDisposalCjseEventLevelPopulator implements CjseEventLevelPopulator {
    private static final Logger log = CSServices.getLogger(UnrelatedDisposalCjseEventLevelPopulator.class);

    public EventLevelAndIdentifier populate(EventParameters eventParameters, XhbCase theCase,
            CourtLogSubscriptionValue eventInfo) {
        // log start of method, we do not need to check the parameters,
        // as that will be performed by the populator we delegate to
        log.debug("populate() start");

        // find the populator
        CjseEventLevelPopulator populator;
        if ((eventInfo != null) && (eventInfo.getDefendantOnOffenceId() != null)) {
            // use a CRN level populator
            populator = CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
                    CjseEventLevelPopulatorFactory.CRN_EVENT_LEVEL);
        } else {
            // use a DEFENDANT level populator
            populator = CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
                    CjseEventLevelPopulatorFactory.DEFENDANT_EVENT_LEVEL);
        }

        // attempt population
        return populator.populate(eventParameters, theCase, eventInfo);
    }
}