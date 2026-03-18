package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.xmlbinding.util.ChargeComparator2;
import uk.gov.courtservice.xhibit.xmlbinding.util.OffenceComparator2;

/**
 * <p/> Title: Utility class for populating Charge Information in a Crest Form
 * Schema.
 * </p>
 * <p/> Description: Charge information population
 * </p>
 * <p/> This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Surtar Bachra
 * @version 1.0
 */

public class CrestFormBFChargeHelper2 {

    // set up logger
    protected static Logger log = CSServices.getLogger(CrestFormBFChargeHelper2.class);

    /**
     * Method populating an castor bound xml schema from an entity bean.
     *
     * @param charges
     *            The castor bound charges object to populate.
     * @param doc
     *            The Defendant On Case entity bean to use for population.
     * @param chargeType
     *            The type of charges to pick up for defendant
     */
    public static uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Charges populateCharges(
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Charges charges, XhbDefendantOnCase doc,
            String chargeType) throws CrestFormBFXMLException {

        log.debug("******************* CREST FORMS BF CHARGE HELPER ENTERED *********************** ");
        log.debug("Charge Type : " + chargeType);
        Integer caseId = doc.getXhbCase().getCaseId();
        log.debug("Case ID : " + caseId);

        // get the defendant_on_case_id
        Integer docID = doc.getDefendantOnCaseId();

        try {
            ResultsCompositeValue rcv = Results2WorkFlow.getResults(caseId);
            Collection caseCharges = rcv.getChargeCompositeValue().getCharges();

            log.debug("******************** Total Charges: " + caseCharges.size());

            // used to hold those charges that of the required charge type
            ArrayList relevantCharges = new ArrayList();

            // loop through charges to pick up those that are of the
            // required charge type
            for (Iterator it = caseCharges.iterator(); it.hasNext();) {
                ChargeValue chr = (ChargeValue) it.next();
                if (chr.getChargeType().equals(chargeType)) {
                    // add to ArrayList if the charge is of the required
                    // charge_type
                    relevantCharges.add(chr);
                }
            }

            // sort the Charges
            Collections.sort(relevantCharges, ChargeComparator2.getInstance());

            int noOfRelevantCharges = relevantCharges.size();
            log.debug("********************* Relevant Charges: " + noOfRelevantCharges);

            // loop through all the relevant charges
            for (int i = 0; i < noOfRelevantCharges; i++) {
                ChargeValue relCharge = (ChargeValue) relevantCharges.get(i);
                // get the offences for each charge
                Collection allOffences = relCharge.getOffenceValues();

                // get the relevant offences for the given docID
                ArrayList relOffences = getRelevantOffences(allOffences, docID);

                // sort the offences by CrestOffenceSeqNo
                Collections.sort(relOffences, OffenceComparator2.getInstance());

                // convert arraylist to array of offences
                OffenceValue[] validOffences = new OffenceValue[relOffences.size()];
                if (relOffences.size() > 0) {
                    relOffences.toArray(validOffences);
                }

                if (charges == null && validOffences.length > 0) {
                    // will ensure that there is a charges castor object to
                    // populate
                    charges = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Charges();
                }

                if (validOffences.length > 0) {
                    // if there are offences that add the charge and offence
                    // castor info.
                    uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Charge chargeToAdd = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Charge();

                    // set up the charge information
                    Integer crestseqNo = relCharge.getCrestChargeSeqNo();
                    if (crestseqNo != null) {
                        // only set if there is a value
                        chargeToAdd.setIndictmentNumber(crestseqNo.intValue());
                    }

                    // for each relevant offence add offence information
                    int noOfRelOffences = validOffences.length;

                    ArrayList offAl = new ArrayList();
                    for (int j = 0; j < noOfRelOffences; j++) {
                        // set up a new offence castor object for each relevant
                        // offence
                        uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence castorOffence = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence();

                        OffenceValue offence = validOffences[j];

                        // populate castor offence
                        CrestFormBFOffenceHelper2.populateOffence(castorOffence, offence, docID, rcv);

                        offAl.add(castorOffence);
                    }

                    uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence[] offToAdd = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence[offAl
                            .size()];
                    offAl.toArray(offToAdd);
                    // add all the offences to the charge castor object
                    chargeToAdd.setOffence(offToAdd);

                    // add the charge to the charges castor object
                    charges.addCharge(chargeToAdd);
                }
            }
        } catch (ResultsControllerException e) {
            e.printStackTrace();
        }
        return charges;
    }

    /**
     * Util method to return those offences that match the given defendant using
     * the defedant_on_offence table
     *
     * @param offences
     *            Offences to be checked
     * @param docID
     *            The defendant to which the offences should relate
     * @return Offences that match the given defendant
     */
    protected static ArrayList getRelevantOffences(Collection offences, Integer docID) {

        log.debug("No of offences to check : " + offences.size());

        // used to hold the return list of offences
        ArrayList relevantOffences = new ArrayList();

        // loop through the collection of offences
        for (Iterator it = offences.iterator(); it.hasNext();) {
            OffenceValue off = (OffenceValue) it.next();

            // only return those offences which are valid
            // if (!"Y".equals(off.getObsInd())) {
            try {
                // get all the Defendants on the offence
                XhbDefendantOnOffence defOnOffence = XhbDefendantOnOffenceBeanHelper2.findByDefOnCaseAndOffence(docID, off.getOffenceID());
                if (defOnOffence != null) {
                    relevantOffences.add(off);
                }

            } catch (XhbDefendantOnOffenceBeanNotFoundException e) {
                // do nothing
                log.debug(e);
            }
        }
        return relevantOffences;
    }
}
