package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.LookupHelper;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.SubscriberChain;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.SubscriberChainFactory;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * Creates basic values for linked cases
 * 
 * @author pznwc5
 * @version $Revision: 1.15 $
 */
public class LinkedCaseCreateHelper extends CreateHelper {
    private static final Logger LOG = CSServices.getLogger(LinkedCaseCreateHelper.class);

    /**
     * Initializes the crud value
     * 
     * @param crudValue
     */
    protected LinkedCaseCreateHelper(OperationContext context) {
        super(context);
    }

    /**
     * Creates multiple basic values from a CRUD value.
     * 
     * @todo Current logic returns the last created basic value. Not sure
     *       whether it is right.
     * 
     * @param logEntryXml
     *            Court log entry XML
     * @return Basic value
     * @throws CourtLogBusinessException
     */
    protected CourtLogViewValue[] createEntry(String logEntryXml) throws CourtLogBusinessException {
        CourtLogViewValue[] mainViewValues = super.createEntry(logEntryXml);
        CourtLogCRUDValue linkedCaseCrudVals[] = getLinkedCrudValues();

        // if no linked cases to process, simply return parents returned values
        if ((linkedCaseCrudVals == null) || (linkedCaseCrudVals.length == 0)) {
            LOG.debug("No linked cases to process, so return mainViewValues");
            return mainViewValues;
        }

        // otherwise we need to process the linked cases...

        // first, create a collection for us to return later...
        final List viewVals = new ArrayList();

        // and add the original entry to it...
        viewVals.add(mainViewValues[0]);

        SubscriberChainFactory factory = SubscriberChainFactory.getInstance();
        for (int i = 0; i < linkedCaseCrudVals.length; i++) {
            LOG.debug("Linked caseId = " + linkedCaseCrudVals[i].getCaseId() + "; shId = "
                    + linkedCaseCrudVals[i].getScheduledHearingId());

            try {
                SubscriberChain chain = factory.getSubscriberChain(linkedCaseCrudVals[i]);
                viewVals.add(chain.processCreateLinked()[0]);
            } catch (CourtLogBusinessException e) {
                // if the error should be aborted, then bubble-up...
                if (e.isAborted()) {
                    throw e;
                }

                // otherwise, just log it for information purposes...
                LOG.info("Error can be ignored...", e);
            }
        }

        // convert our collection to the required array...
        return (CourtLogViewValue[]) viewVals.toArray(new CourtLogViewValue[viewVals.size()]);
    }

    /**
     * Helper method used to acquire the defendant id for the defendant on case
     * id that is passed in.
     * 
     * @param defendantOnCaseId
     *            The defendant on case primary key we want to find the
     *            defendant id for.
     * @return The primary key for the defendant. Or <i>null</i> if the passed
     *         in defendant on case id is <i>null</i>.
     */
    private Integer getDefendantId(Integer defendantOnCaseId) {
        LOG.debug("getDefendantId() - defendantOnCaseId = " + defendantOnCaseId);
        Integer defendantId = null;

        if (defendantOnCaseId != null) {
            defendantId = EntityHelper.getXhbDefendantOnCase(defendantOnCaseId).getDefendantId();
        }

        LOG.debug("getDefendantId() - Returning " + defendantId);
        return defendantId;
    }

    /**
     * Helper method used to acquire the defendant on case id of the passed in
     * defendant if it is on the passed in scheduled hearings defendant on
     * cases.
     * 
     * @param xhbScheduledHearing
     *            The scheduled hearing that the defendants on case should be
     *            iterated through to find a match
     * @param defendantId
     *            The defendant primary key that should
     * @return
     */
    private Integer getDefendantOnCaseId(XhbScheduledHearing xhbScheduledHearing, Integer defendantId) {
        final Integer scheduledHearingId = ((xhbScheduledHearing != null) ? xhbScheduledHearing.getScheduledHearingId()
                : null);
        Integer docId = null;

        LOG.debug("getDefendantOnCaseId() - defendantId: " + defendantId + "; scheduledHearingId = "
                + scheduledHearingId);

        if ((defendantId != null) && (xhbScheduledHearing != null)) {
            Iterator docIterator = xhbScheduledHearing.getXhbHearing().getXhbCase().getXhbDefendantOnCases().iterator();

            while (docIterator.hasNext()) {
                XhbDefendantOnCase doc = (XhbDefendantOnCase) docIterator.next();

                if (doc.getDefendantId().equals(defendantId)) {
                    docId = doc.getDefendantOnCaseId();
                    break;
                }
            }
        }

        LOG.debug("getDefendantOnCaseId() - Returning " + docId);
        return docId;
    }

    /**
     * Determines whether or not the case type parameter passed is at case level
     * 
     * @param caseType
     * @return true if the case type is of type B or U
     */
    private boolean isCaseLevelCaseType(XhbCase xhbCase) {
        final String caseType = xhbCase.getCaseType();
        final boolean isCaseLevel = "U".equalsIgnoreCase(caseType) || "B".equalsIgnoreCase(caseType);

        LOG.debug("isCaseLevelCaseType() - caseType = " + caseType + " - Returning = " + isCaseLevel);

        return isCaseLevel;
    }

    /**
     * Utility method used to create a copy of the passed in CRUD value and
     * update it with the relevant details passed in of the linked case.
     * 
     * @param courtLogCRUDValue -
     *            The original <code>CourtLogCRUDValue</code> that the new
     *            instance should be a copy of
     * @param caseId
     *            The case id of the linked case
     * @param defendantOnCaseId
     *            The id for the defendant on case for the linked case
     * @param scheduledHearingId
     *            The scheduled hearing id for the linked case
     * @return The cloned CRUD value, with the case id, defendant on case id and
     *         scheduled hearing id changed to the passed in parameters
     */
    private CourtLogCRUDValue createLinkedCRUDValue(CourtLogCRUDValue courtLogCRUDValue, Integer caseId,
            Integer defendantOnCaseId, Integer scheduledHearingId) {
        CourtLogCRUDValue linkedCrud = (CourtLogCRUDValue) courtLogCRUDValue.clone();

        // update the fields that are always set (and change for linked cases)
        linkedCrud.setCaseId(caseId);
        // do not need to do defendant on offence as must be non-null here
        linkedCrud.setDefendantOnCaseId(defendantOnCaseId);
        linkedCrud.setScheduledHearingId(scheduledHearingId);

        // ensure we do not try to process the linked cases, otherwise we could
        // get
        // into an infinite loop situation!
        linkedCrud.setProcessLinkedCases(false);

        return linkedCrud;
    }

    /**
     * TODO
     * 
     * @return
     */
    private CourtLogCRUDValue[] getLinkedCrudValues() {
        final XhbScheduledHearing xhbScheduledHearing = LookupHelper.getXhbScheduledHearing(crudVal);

        if (xhbScheduledHearing != null) {
            LOG.debug("shId = " + xhbScheduledHearing.getScheduledHearingId() + "; linkedShId = "
                    + xhbScheduledHearing.getLinkedShId());

            if (xhbScheduledHearing.getLinkedShId() != null) {
                final Collection linkedScheduledHearings = XhbScheduledHearingBeanHelper2
                        .findLinkedScheduledHearings(xhbScheduledHearing.getScheduledHearingId());

                LOG.debug("linkedScheduledHearings.size() = " + linkedScheduledHearings.size());

                if (linkedScheduledHearings.size() > 0) {
                    // find the defendant id for the passed in crud ...
                    final Integer defendantId = getDefendantId(super.crudVal.getDefendantOnCaseId());
                    final Iterator it = linkedScheduledHearings.iterator();

                    final List linkedCruds = new ArrayList();

                    LOG.debug("linkedDefendantId = " + defendantId);

                    while (it.hasNext()) {
                        XhbScheduledHearing xsh = (XhbScheduledHearing) it.next();
                        XhbCase xc = xsh.getXhbHearing().getXhbCase();


                        // only allow processing of cases of the same type...
                            // and only process the linked case if the event
                            // is
                            // case level, or the defendant is also on the
                            // linked case
                            final Integer defOnCaseId = getDefendantOnCaseId(xsh, defendantId);

                            if ((defendantId == null) || (defOnCaseId != null)) {
                                linkedCruds.add(createLinkedCRUDValue(super.crudVal, xc.getCaseId(), defOnCaseId, xsh
                                        .getScheduledHearingId()));
                            }
                    }

                    if (linkedCruds.size() > 0) {
                        CourtLogCRUDValue[] returnArray = (CourtLogCRUDValue[]) linkedCruds
                                .toArray(new CourtLogCRUDValue[linkedCruds.size()]);
                        LOG.debug("getLinkedCrudValues() - Returning " + returnArray.length + " CRUD values");
                        return returnArray;
                    }
                }
            }
        }

        LOG.debug("getLinkedCrudValues() - Returning null");
        return null;
    }
}