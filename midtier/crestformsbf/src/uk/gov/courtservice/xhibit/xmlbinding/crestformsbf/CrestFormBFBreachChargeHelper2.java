package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.CourtType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.YesNoType;
import uk.gov.courtservice.xhibit.xmlbinding.util.ChargeComparator2;
import uk.gov.courtservice.xhibit.xmlbinding.util.OffenceComparator2;

/**
 * <p/> Title: Utility class for populating Charge Information in a Crest Form
 * Schema.
 * </p>
 * <p/> Description: Charge information population + breaches
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
public class CrestFormBFBreachChargeHelper2 extends CrestFormBFChargeHelper2 {
    // set up logger
    protected static Logger log = CSServices.getLogger(CrestFormBFBreachChargeHelper2.class);

    private static final String CROWN = "C";

    private static final String MAGISTRATES = "M";

    public static uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.BreachCharges populateBreachCharges(
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.BreachCharges charges, XhbDefendantOnCase doc,
            String chargeType) throws CrestFormBFXMLException {

        log.debug("******************* CREST FORMS BF BREACH CHARGE HELPER ENTERED *********************** ");
        log.debug("Charge Type : " + chargeType);

        Integer caseId = doc.getXhbCase().getCaseId();
        log.debug("Case ID : " + caseId);

        // get the defendant_on_case_id
        Integer docID = doc.getDefendantOnCaseId();

        ResultsCompositeValue rcv = null;
        try {
            rcv = Results2WorkFlow.getResults(caseId);
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
            log.debug("Relevant Charges: " + noOfRelevantCharges);

            // loop through relevant charges and get their offences
            // check that the offences match the required defendant before
            // generating castor object

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
                    charges = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.BreachCharges();
                }

                if (validOffences.length > 0) {
                    // if there are offences that add the charge and offence
                    // castor info.
                    uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.BreachCharge chargeToAdd = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.BreachCharge();

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

                    // *********************************
                    // END OF COMMON CHARGE POPULATION
                    // BREACH CHARGE SPECIFIC POPULATION
                    // *********************************

                    // get the breach for the charge if any
                    BreachValue breach = relCharge.getBreachValue();

                    if (breach != null) {
                        log.debug("BREACH ID ::::::::::::::::::::::::::::::" + breach.getBreachID());

                        // ensure that there is a breach charge castor object to
                        // populate
                        if (chargeToAdd.getBreach() == null) {
                            chargeToAdd
                                    .setBreach(new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Breach());
                        }

                        // set the origianal sentence text
                        String originalSentence = breach.getOriginalSentence();
                        log.debug("original sentence ::::: " + originalSentence);
                        if (originalSentence != null) {
                            chargeToAdd.getBreach().setOriginalSentence(originalSentence);
                        }

                        // set the committal after breach and bringBack
                        // get the value from xhb_breach.BreachType
                        // if BreachType = 'B' then BringBack = 'Yes' and
                        // CommittalAfterBreach = 'No'
                        // if BreachType = 'C' then BringBack = 'No' and
                        // CommittalAfterBreach = 'Yes'
                        String breachType = breach.getBreachType();
                        log.debug("breach type ::::::  " + breachType);
                        if (breachType != null) {
                            if (breachType.equals("B")) {
                                chargeToAdd.getBreach().setBringBack(YesNoType.YES);
                                chargeToAdd.getBreach().setCommittalAfterBreach(YesNoType.NO);
                            }
                            if (breachType.equals("C")) {
                                chargeToAdd.getBreach().setBringBack(YesNoType.NO);
                                chargeToAdd.getBreach().setCommittalAfterBreach(YesNoType.YES);
                            }
                        }

                        // populate original court

                        // set up a default court type otherwise if null it will
                        // invalidate the xml
                        chargeToAdd.getBreach().setCourtHouseType(CourtType.VALUE_3); // blank

                        String courtType = breach.getOriginalCourtType();
                        log.debug("court type :::::::::  " + courtType);
                        if (courtType != null) {
                            if (courtType.equals(MAGISTRATES)) {
                                chargeToAdd.getBreach().setCourtHouseType(CourtType.VALUE_1); // magistrates
                            }

                            if (courtType.equals(CROWN)) {
                                chargeToAdd.getBreach().setCourtHouseType(CourtType.VALUE_2); // crown
                            }
                        }

                        Integer origCourtId = breach.getOriginalCourtID();
                        log.debug("origCourtId :::::::::  " + origCourtId);
                        if (origCourtId != null) {
                            chargeToAdd.getBreach().setCourtHouseCode(origCourtId.toString());
                        }

                        String courtName = breach.getOriginalCourtName();
                        log.debug("courtName :::::::::  " + courtName);
                        if (courtName != null) {
                            chargeToAdd.getBreach().setCourtHouseName(courtName);
                        }

                        // set the original sentence date
                        Calendar origSentenceDate = breach.getOriginalSentenceDate();
                        log.debug("origSentenceDate ::::::: " + origSentenceDate);
                        // check to ensure that the original sentence date is
                        // not null
                        if (origSentenceDate != null) {
                            chargeToAdd.getBreach().setOriginalDate(new Date(origSentenceDate.getTime()));
                        }

                        // set the breach put date
                        Calendar breachPutDate = breach.getDatePut();
                        // check to ensure that the breach put date is not null
                        if (breachPutDate != null) {
                            chargeToAdd.getBreach().setBreachPutDate(new Date(breachPutDate.getTime()));
                        }

                        // set up the default Admitted option
                        chargeToAdd.getBreach().setAdmitted("N/A");

                        Integer chargeId = relCharge.getChargeID();
                        if (chargeId != null) {
                            // pick up the defendant Charge record
                            log.debug("BREACH ADMITTED :: charge id :: " + chargeId);
                            log.debug("BREACH ADMITTED :: doc id :: " + docID);
                            XhbDefendantCharge defOnCharge = getDefendantCharge(chargeId, docID);
                            if (defOnCharge != null) {
                                Integer defOnChargeId = defOnCharge.getDefendantChargeId();
                                log.debug("BREACH ADMITTED :: def on charge id :: " + defOnChargeId);
                                // pick up plea record
                                XhbPlea pl = getPlea(defOnChargeId);
                                if (pl != null) {
                                    log.debug("BREACH ADMITTED :: plea id :: " + pl.getPleaId());
                                    String admitted = pl.getBreachAdmitted();
                                    if (admitted != null) {
                                        chargeToAdd.getBreach().setAdmitted(getAdmittedText(admitted));
                                    }
                                }
                            }
                        }

                        // set up HO Code
                        String hoCode = breach.getHoCode();
                        if (hoCode != null) {
                            int hoCodeIntVal = Integer.parseInt(hoCode);
                            chargeToAdd.setHOCode(hoCodeIntVal);
                        }
                    }
                    // add the charge to the charges castor object
                    charges.addBreachCharge(chargeToAdd);
                }
            }
        } catch (ResultsControllerException e) {
            e.printStackTrace();
        }
        return charges; // return the populated charges
    }

    /**
     * Util method to convert the Breach Admitted text
     *
     * @param admitted
     *            String to be converted
     * @return converted string
     */
    private static String getAdmittedText(String admitted) {

        String rtnAdmittedText = "N/A";

        if (admitted.equals("Y")) {
            rtnAdmittedText = "Yes";
        }
        if (admitted.equals("N")) {
            rtnAdmittedText = "No";
        }

        return rtnAdmittedText;
    }

    /**
     * Util method to return a plea record for the given defOnChargeId
     *
     * @param defOnChargeId
     * @return Plea
     */
    private static XhbPlea getPlea(Integer defOnChargeId) {
        XhbPlea pl = null;
        Iterator pleas = XhbPleaBeanHelper2.findByDefendantChargeId(defOnChargeId).iterator();
        while (pleas.hasNext()) {
            pl = (XhbPlea) pleas.next();
        }
        return pl;
    }

    /**
     * Util method to retieve a Defendant On Charge record for the given Charge
     * ID and doc
     *
     * @param chargeId
     * @param docID
     * @return DefendantOnCharge record
     */
    private static XhbDefendantCharge getDefendantCharge(Integer chargeId, Integer docID) {
        XhbDefendantCharge dc = null;
        try {
            //Iterator defOnCharges = defChargeHome.findByChargeIdAndDefOnCaseId(chargeId, docID).iterator();
            Iterator defOnCharges = XhbDefendantChargeBeanHelper2.findByChargeIdAndDefOnCaseId(chargeId, docID).iterator();
            while (defOnCharges.hasNext()) {
                dc = (XhbDefendantCharge) defOnCharges.next();
            }
        } catch (XhbDefendantChargeBeanNotFoundException e) {
            log.debug(e);

        }
        return dc;
    }

}
