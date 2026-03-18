package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_def_hearing_record.XhbDefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_reference.XhbDefendantReference;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.BailStatuses;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.CRONumbers;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.Genders;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.IsJuveniles;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.PNCIDs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.PTIURNs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.PrisonerIDs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.Prisons;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.types.types.BusinessGender;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Populates the defendant level attriubtes. It is important to set
 * attributes for all defendants as the same time i.e. pass all defendant ids as
 * the check for duplicate values is performed only on attributes for the ids
 * passed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DefendantPopulatorHelper.java,v 1.4 2006/02/10 15:58:07 rzvddy
 *          Exp $
 */
public class DefendantPopulatorHelper {
    private static final Logger log = CSServices.getLogger(DefendantPopulatorHelper.class);

    // Defendant Reference names
    private static String PRISONER_NO = "PRISONER_NO";

    private static String CRO_NO = "CRO_NO";

    private static int CRO_NO_LENGTH = 9;

    // Gender values
    private static Byte COMPANY = new Byte("0");

    private static Byte MALE = new Byte("1");

    private static Byte FEMALE = new Byte("2");

    // need to make sure we don't have duplicates in these groups so use
    // HashSet
    private HashSet _prisons = new HashSet();

    private HashSet _pncIds = new HashSet();

    private HashSet _prisonerIds = new HashSet();

    private HashSet _ptiUrns = new HashSet();

    private HashSet _croNumbers = new HashSet();

    private HashSet _genders = new HashSet();

    private HashSet _bailStatuses = new HashSet();

    private HashSet _isJuveniles = new HashSet();

    /**
     * Populates the defendant level attributes.
     * 
     * @param eventParameters
     *            The event parameters to populate.
     * @param defendantOnCases
     *            A Collection of XhbDefendantOnCase DefHearingRecord used to
     *            find bail status.
     */
    public void populateDefendantAttributes(EventParameters eventParameters, Collection defendantOnCases) {
        logStartAndCheckParams(defendantOnCases, eventParameters);

        if (defendantOnCases == null || defendantOnCases.size() == 0) {
            // nothing to populate from, daft to call method with no
            // defendants
            // but not really worth throwing an exception...
            return;
        }

        // populate attributes for each defendant
        for (Iterator i = defendantOnCases.iterator(); i.hasNext();) {
            XhbDefendantOnCase theDefOnCase = (XhbDefendantOnCase) i.next();
            log.debug("populateDefendantAttributes() processing defendantOnCase: "
                    + theDefOnCase.getDefendantOnCaseId());

            populateAttributes(theDefOnCase);
        }

        // for each set of attributes:
        // remove any null value from the HashSet
        // check contains values
        // convert to a String[]
        // add to the EventParameters instance
        copyAttribuesToEventParameters(eventParameters);
    }

    /**
     * Populates the defendant level attributes.
     * 
     * @param eventParameters
     *            The event parameters to populate.
     * @param defendantOnCase
     *            An XhbDefendantOnCase
     */
    public void populateDefendantAttributes(EventParameters eventParameters, XhbDefendantOnCase defendantOnCase) {
        populateAttributes(defendantOnCase);
        copyAttribuesToEventParameters(eventParameters);
    }

    /**
     * Populates the defendant level attributes.
     * 
     * @param theDefOnCase
     */
    private void populateAttributes(XhbDefendantOnCase theDefOnCase) {
        // find the defendant entity, the defendant will exist since
        // the defendantId in a not null fk on XhbDefendantOnCase
        XhbDefendant theDefendant = theDefOnCase.getXhbDefendant();

        // add the attributes to the HashSets for this defendant
        if (theDefendant.getPrisonId() != null) {
            _prisons.add(theDefendant.getPrisonId().toString());
        }
        _pncIds.add(convertCrestPncId(theDefOnCase.getPncId()));
        if (theDefOnCase.getPtiurn() != null) {
            _ptiUrns.add(theDefOnCase.getPtiurn().trim());
        }
        setGender(theDefendant);
        setBailStatus(theDefOnCase);
        setIsJuvenile(theDefOnCase);

        // prisonerId and CRONumber are retrieved from the Defendant References
        setDefendantReferenceAttributes(theDefendant);
    }

    // ---------------------------- Private Methods
    // -----------------------------//

    // log start of method
    private void logStartAndCheckParams(Collection defendantOnCases, EventParameters eventParameters) {
        if (log.isDebugEnabled()) // check debug is on before executing
        // this loop
        {
            log.debug("populateDefendantAttributes() with " + defendantOnCases.size() + " defendantOnCaseIds");
        }
        // check parameters
        if (eventParameters == null) {
            throw new IllegalArgumentException("An EventParameters instance must be passed to the "
                    + "populateDefendantAttributes() method");
        }
    }

    /**
     * set the prisoner number and cro number if specified.
     * 
     * @param theDefendant
     *            a local reference to the defendant entity.
     */
    private void setDefendantReferenceAttributes(XhbDefendant theDefendant) {
        Collection references = theDefendant.getXhbDefendantReferences();
        Iterator refIt = references.iterator();
        while (refIt.hasNext()) {
            XhbDefendantReference reference = (XhbDefendantReference) refIt.next();
            if (reference.getReferenceName().equals(PRISONER_NO)) {
                _prisonerIds.add(reference.getReferenceValue());
            } else if (reference.getReferenceName().equals(CRO_NO)) {
                // The CRO Number is 9 characters which consist of 8 digits
                // followed by 1 letter which is the checksum.
                // CJIT does not want the checksum. i.e. only send first 8
                // chars.
                String eightDigitCRO = null;
                if (reference.getReferenceValue() != null) {
                    if (reference.getReferenceValue().length() != CRO_NO_LENGTH) {
                        eightDigitCRO = reference.getReferenceValue();
                        log.warn("Defendant " + theDefendant.getDefendantId() + " has a CRO of "
                                + reference.getReferenceValue() + ". Should contain " + CRO_NO_LENGTH + " characters!");
                    } else {
                        eightDigitCRO = reference.getReferenceValue().substring(0, 8);
                    }
                }
                _croNumbers.add(eightDigitCRO);
            }
        }
    }

    /**
     * Converst the crest pncId to the format expected by CJSE. pnc format:
     * [0-9]{4}[0-9]{7}[A-Za-z0-9]{1} where the first 4 digits are the year. In
     * crest this is [0-9]{2}[0-9]{7}[A-Za-z0-9]{1} - the century has been
     * trimmed from the year - we need to add this back on. The final digit is a
     * 'check digit' calculated from the preceeding numbers, we are not
     * currently recalculating the check digit after adding back the century,
     * however this may be required in future.
     * 
     * @param crestPncId
     *            the PNC Id
     * @return PNC ID in CJSE format
     */
    public String convertCrestPncId(String crestPncId) {
        log.debug("convertCrestPncId() start with crestPncId: " + crestPncId);

        if (crestPncId == null) {
            // nothing to convert
            log.debug("convertCrestPncId() called with a null crestPncId");
            return null;
        }
        String pncYear = crestPncId.substring(0, 2);

        Calendar today = Calendar.getInstance();
        int fullYearToday = today.get(Calendar.YEAR);
        int century = fullYearToday / 100;
        int pncFullYear = century * 100 + Integer.parseInt(pncYear);

        // check the pnc year is this year or earlier
        if (pncFullYear > fullYearToday) {
            // if not take one century from the full year
            pncFullYear -= 100;
        }

        // now convert the crestPncId to the CJSE format
        log.debug("convertCrestPncId() returning: " + pncFullYear + crestPncId.substring(2));
        return pncFullYear + crestPncId.substring(2);
    }

    // set gender if specified
    private void setGender(XhbDefendant theDefendant) {
        Byte gender = theDefendant.getGender();
        if (gender != null) {
            if (gender.equals(MALE)) {
                _genders.add(BusinessGender.MALE);
            } else if (gender.equals(FEMALE)) {
                _genders.add(BusinessGender.FEMALE);
            } else if (gender.equals(COMPANY)) {
                _genders.add(BusinessGender.UNKNOWN);
            }
        }
    }

    private void setBailStatus(XhbDefendantOnCase theDefOnCase) {
        // find the most recent DefHearingRecord for this defendant, if one
        // exists
        Collection defHearingRecs = theDefOnCase.getXhbDefHearingRecords();
        if (defHearingRecs.size() > 0) {
            ArrayList defHearingRecArray = new ArrayList(defHearingRecs);
            // sort in reverse chronological order so the first element in
            // the
            // list will be the most recent record
            Collections.sort(defHearingRecArray, CREATION_DATE_ORDER);
            XhbDefHearingRecord theDefHearingRecord = (XhbDefHearingRecord) defHearingRecArray.get(0);
            // use, in order of preference, end bail status, new bail status
            // or
            // start bail status
            if (theDefHearingRecord.getEndBailStatus() != null) {
                _bailStatuses.add(theDefHearingRecord.getEndBailStatus());
            } else if (theDefHearingRecord.getNewBailStatus() != null) {
                _bailStatuses.add(theDefHearingRecord.getNewBailStatus());
            } else if (theDefHearingRecord.getStartBailStatus() != null) {
                _bailStatuses.add(theDefHearingRecord.getStartBailStatus());
            }
            // otherwise no bail status set
        }
    }

    // set isJuvenile if specified
    private void setIsJuvenile(XhbDefendantOnCase theDefOnCase) {
        if (theDefOnCase.getIsJuvenile() != null) {
            if (theDefOnCase.getIsJuvenile().equals("Y"))
                _isJuveniles.add(new Boolean(true));
            else if (theDefOnCase.getIsJuvenile().equals("N"))
                _isJuveniles.add(new Boolean(false));
        }
    }

    // for each set of attributes:
    // remove any null value from the HashSet
    // check contains values
    // convert to a String[]
    // add to the EventParameters instance
    private void copyAttribuesToEventParameters(EventParameters eventParameters) {
        removeNullsEmptyStrings();
        if (_prisons.size() > 0) {
            Prisons thePrisons = new Prisons();
            thePrisons.setPrison((String[]) _prisons.toArray(new String[] {}));
            eventParameters.setPrisons(thePrisons);
        }

        if (_pncIds.size() > 0) {
            PNCIDs thePNCIDs = new PNCIDs();
            thePNCIDs.setPNCID((String[]) _pncIds.toArray(new String[] {}));
            eventParameters.setPNCIDs(thePNCIDs);
        }

        if (_prisonerIds.size() > 0) {
            PrisonerIDs thePrisonerIDs = new PrisonerIDs();
            thePrisonerIDs.setPrisonerID((String[]) _prisonerIds.toArray(new String[] {}));
            eventParameters.setPrisonerIDs(thePrisonerIDs);
        }

        if (_ptiUrns.size() > 0) {
            PTIURNs thePTIURNs = new PTIURNs();
            thePTIURNs.setPTIURN((String[]) _ptiUrns.toArray(new String[] {}));
            eventParameters.setPTIURNs(thePTIURNs);
        }

        if (_croNumbers.size() > 0) {
            CRONumbers theCRONumbers = new CRONumbers();
            theCRONumbers.setCRONumber((String[]) _croNumbers.toArray(new String[] {}));
            eventParameters.setCRONumbers(theCRONumbers);
        }

        if (_genders.size() > 0) {
            Genders theGenders = new Genders();
            theGenders.setGender((BusinessGender[]) _genders.toArray(new BusinessGender[] {}));
            eventParameters.setGenders(theGenders);
        }

        if (_bailStatuses.size() > 0) {
            BailStatuses theBailStatuses = new BailStatuses();
            theBailStatuses.setBailStatusCode((String[]) _bailStatuses.toArray(new String[] {}));
            eventParameters.setBailStatuses(theBailStatuses);
        }

        if (_isJuveniles.size() > 0) {
            IsJuveniles theIsJuveniles = new IsJuveniles();
            theIsJuveniles.setIsJuvenile(copyJuvenilesToArray());
            eventParameters.setIsJuveniles(theIsJuveniles);
        }
    }

    // creates an array of boolean values from the isJuveniles HashSet
    // containing Boolean objects
    private boolean[] copyJuvenilesToArray() {
        boolean[] juvenilesArray = new boolean[_isJuveniles.size()];
        Iterator it = _isJuveniles.iterator();

        int i = 0;
        while (it.hasNext()) {
            juvenilesArray[i] = ((Boolean) it.next()).booleanValue();
            i++;
        }

        return juvenilesArray;
    }

    // remove any null or empty String objects from the HashSets
    private void removeNullsEmptyStrings() {
        _prisons.remove(null);
        _pncIds.remove(null);
        _prisonerIds.remove(null);
        _ptiUrns.remove(null);
        _croNumbers.remove(null);
        _genders.remove(null);
        _bailStatuses.remove(null);
        _isJuveniles.remove(null);
        _prisons.remove("");
        _pncIds.remove("");
        _prisonerIds.remove("");
        _ptiUrns.remove("");
        _croNumbers.remove("");
        _genders.remove("");
        _bailStatuses.remove("");
        // no need to remove empty string from juveniles as it contains Boolean
        // objects not strings
    }

    /**
     * Comparator used to order def hearing records by creation date in reverse
     * chronological order
     */
    static final Comparator CREATION_DATE_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            XhbDefHearingRecord entry1 = (XhbDefHearingRecord) o1;
            XhbDefHearingRecord entry2 = (XhbDefHearingRecord) o2;
            // Timestamp implements Comparable so fine to do this, we want
            // the results in reverse chronological order so the latest
            // event
            // is first
            return entry2.getCreationDate().compareTo(entry1.getCreationDate());
        }
    };

}