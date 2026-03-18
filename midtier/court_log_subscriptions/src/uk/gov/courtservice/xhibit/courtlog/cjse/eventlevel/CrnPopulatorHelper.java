package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.ASNs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.CRNIDs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

/**
 * <p>
 * Title: CrnPopulatorHelper
 * </p>
 * <p>
 * Description: Populates the CRN level attriubtes. The ASNs are also populated
 * at this level, despite the fact that in business terms ASNs are considered
 * defendant level. This is because the ASNs are directly related to the CRNs
 * and it makes sense to populate the two together. The defendant level
 * populator will call this helper to populate the CRNs and the ASNs will
 * therefore always be populated for defendant level events.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: CrnPopulatorHelper.java,v 1.4 2006/06/05 12:28:55 bzjrnl Exp $
 */

public class CrnPopulatorHelper {
    private static final Logger log = CSServices.getLogger(CrnPopulatorHelper.class);

    // need to make sure we don't have duplicates in these groups so use
    // HashSet
    private HashSet _asns = new HashSet();

    private HashSet _crns = new HashSet();

    public CrnPopulatorHelper() {
    }

    /**
     * Retrieves the CRNs for the given XhbDefendantOnCase
     * 
     * @param defOnCase
     *            XhbDefendantOnCase
     * @return a String array of CRNs
     */
    public static String[] retrieveCRNs(final XhbDefendantOnCase defOnCase) {
        final Set crns = getCRNs(defOnCase);
        return (String[]) crns.toArray(new String[crns.size()]);
    }

    /**
     * Retrieves the CRNs for the given Collection of XhbDefendantOnCase
     * 
     * @param defOnCases
     *            Collection of XhbDefendantOnCase
     * @return a String array of CRNs
     */
    public static String[] retrieveCRNs(final Collection defOnCases) {
        final Set crns = new HashSet();
        for (Iterator i = defOnCases.iterator(); i.hasNext();) {
            XhbDefendantOnCase defOnCase = (XhbDefendantOnCase) i.next();
            crns.addAll(getCRNs(defOnCase));
        }
        return (String[]) crns.toArray(new String[crns.size()]);
    }

    /**
     * Retrieves the CRNs for the given XhbDefendantOnCase
     * 
     * @param defOnCase
     *            A XhbDefendantOnCase
     * @return a Set of CRNs
     */
    private static Set getCRNs(final XhbDefendantOnCase defOnCase) {
        final Collection defOnOffences = defOnCase.getXhbDefendantOnOffences();
        final Set crns = new HashSet();
        for (Iterator i = defOnOffences.iterator(); i.hasNext();) {
            crns.add(((XhbDefendantOnOffence) i.next()).getCrnId());
        }
        return crns;
    }

    /**
     * Populates the crn level attriubtes. It is important to set attributes for
     * all CRNs as the same time i.e. pass all CRNs as the check for duplicate
     * values is performed only on attributes for the ids passed.
     * 
     * @param eventParameters
     *            The event parameters to populate.
     * @param crns
     *            A String array of CRNs
     */
    public void populateCrnAttributes(EventParameters eventParameters, String[] crns) {
        logStart(crns);

        // check parameters
        if (eventParameters == null) {
            throw new IllegalArgumentException("An EventParameters instance must be passed to the "
                    + "populateCrnAttributes() method");
        }
        if (crns == null || crns.length == 0) {
            // nothing to populate from, daft to call method with no
            // crns but not really worth throwing an exception...
            return;
        }

        // populate attributes for each crn
        for (int i = 0; i < crns.length; i++) {
            String crnId = crns[i];
            if (crnId != null && !crnId.equals("")) {
                if (crnId.length() != 23) {
                    throw new CSUnrecoverableException("Invalid crnId, must be 23 characters long. crnId: " + crnId);
                } else {
                    _crns.add(crnId);
                    // trim off the last 3 chars to get the asn
                    // NOTE Actually a defendant level attribute, see class
                    // documentation for further explanation
                    _asns.add(crnId.substring(0, crnId.length() - 3));
                }
            }
        }

        // for each set of attributes:
        // remove any null value from the HashSet
        // check contains values
        // convert to a String[]
        // add to the EventParameters instance
        copyAttribuesToEventParameters(eventParameters);
    }

    // ---------------------------- Private Methods
    // -----------------------------//

    // log start of method
    private void logStart(Object[] crns) {
        if (log.isDebugEnabled()) // check debug is on before executing
        // this loop
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < crns.length; i++) {
                sb.append(crns[i] + " ");
            }
            log.debug("populateCrnAttributes() with crns: " + sb);
        }
    }

    // for each set of attributes:
    // remove any null value from the HashSet
    // check contains values
    // convert to a String[]
    // add to the EventParameters instance
    private void copyAttribuesToEventParameters(EventParameters eventParameters) {
        _asns.remove(null);
        _asns.remove("");
        _crns.remove(null);
        _crns.remove("");

        if (_asns.size() > 0) {
            ASNs theAsns = new ASNs();
            theAsns.setASN((String[]) _asns.toArray(new String[] {}));
            eventParameters.setASNs(theAsns);
        }

        if (_crns.size() > 0) {
            CRNIDs theCrns = new CRNIDs();
            theCrns.setCRNID((String[]) _crns.toArray(new String[] {}));
            eventParameters.setCRNIDs(theCrns);
        }
    }
}