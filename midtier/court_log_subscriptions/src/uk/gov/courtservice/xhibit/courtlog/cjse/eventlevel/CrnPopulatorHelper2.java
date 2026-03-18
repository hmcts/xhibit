package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.ASNs;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.CRNIDs;

/**
 * <p>
 * Title: CrnPopulatorHelper
 * </p>
 * <p>
 * Description: Populates the CRN and ASN level attriubtes. The ASNs are 
 * directly related to the CRNs and it makes sense to populate the two together
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Simon Gilmore
 * @version $Id: CrnPopulatorHelper2.java,v 1.1 2007/09/26 09:29:30 rzvddy Exp $
 */
public class CrnPopulatorHelper2 {
    
    private static final Logger log = CSServices.getLogger(CrnPopulatorHelper2.class);

    /**
     * Compiled representation of a regular expression representing an
     * Arrest Summons Number (ASN).
     */
    private static final Pattern asnPattern = Pattern.compile("[0-9]{2}[A-Za-z0-9]{4}[0-9]{2}[0-9]{11}[A-Za-z0-9]{1}");
    
    /**
     * The Arrest Summons Numbers - there should not be any duplicates.
     */ 
    private final Set<String> _asns = new HashSet<String>();

    /**
     * The Common Reference Numbers - there should not be any duplicates.
     */ 
    private final Set<String> _crns;

    /**
     * Creates a CrnPopulatorHelper2 using the given XhbDefendantOnCase 
     * EJBLocalObject.
     * @param defOnCase An XhbDefendantOnCase EJBLocalObject
     */
    public CrnPopulatorHelper2(final XhbDefendantOnCase defOnCase) {
        String asn = defOnCase.getAsn();
        if (isValidAsn(asn)) {
            _asns.add(asn);
            _crns = getCRNs(defOnCase);
        } else {
            _crns = new HashSet<String>();
        }
    }

    /**
     * Creates a CrnPopulatorHelper2 using the given collection of 
     * XhbDefendantOnCase EJBLocalObjects.
     * @param defOnCases Collection of XhbDefendantOnCase EJBLocalObjects
     */
    public CrnPopulatorHelper2(final Collection defOnCases) {
        _crns = new HashSet<String>();
        String asn;
        for (Iterator i = defOnCases.iterator(); i.hasNext();) {
            XhbDefendantOnCase defOnCase = (XhbDefendantOnCase) i.next();
            asn = defOnCase.getAsn();
            if (isValidAsn(asn)) {
                _asns.add(asn);
                _crns.addAll(getCRNs(defOnCase));
		    }
        }
    }

    /**
     * Creates a CrnPopulatorHelper2 using the given XhbDefendantOnCase and 
     * XhbDefendantOnOffence EJBLocalObjects.
     * @param defOnCase An XhbDefendantOnCase EJBLocalObject
     * @param defOnOffence An XhbDefendantOnOffence EJBLocalObject
     */
    public CrnPopulatorHelper2(final XhbDefendantOnCase defOnCase, final XhbDefendantOnOffence defOnOffence) {
        _crns = new HashSet<String>();
        final String asn = defOnCase.getAsn();
        if (isValidAsn(asn)) {
            _asns.add(defOnCase.getAsn());
            if (defOnOffence.getSeqNo() != null) {
            	_crns.add(defOnCase.getAsn() + getPaddedSequenceNo(defOnOffence.getSeqNo()));
			}
        }
    }

    /**
     * Checks if the given Arrest Summons Number(ASN) is valid.
     * The ASN must match the regular expression.  The method matcher of the
     * class Pattern does not accept nulls.
     *  
     * @param asn The Arrest Summons Number as a String.
     * @return true if the ASN is valid.
     */
    private boolean isValidAsn(String asn) {

        if (asn == null || asn.length() == 0) {
            log.debug("isValidAsn: ASN pattern=" + asnPattern.pattern() + " asn=" + asn);
            return false;
        } else {
            Matcher asnMatcher = asnPattern.matcher(asn);
            if (asnMatcher.matches()) {
                return true;
            } else {
                log.debug("isValidAsn: ASN pattern=" + asnPattern.pattern() + " asn=" + asn);
                return false;
            }            
        }                        
    }

    /**
     * Checks id the given sequence number is valid.
     * @param seqNo The sequence number.
     * @return true if the sequence number is valid.
     */
    private boolean isValidSeqNo(Integer seqNo) {
        return (seqNo == null) ? false : true;
    }

    /**
     * Retrieves the CRNs for the given XhbDefendantOnCase EJBLocalObject.
     *
     * @param defOnCase
     *            An XhbDefendantOnCase EJBLocalObject.
     * @return a Set of CRNs
     */
    private Set<String> getCRNs(final XhbDefendantOnCase defOnCase) {
        final Set<String> crns = new HashSet<String>();

        final String asn = defOnCase.getAsn();

        Integer seqNo;
        String crn;

        final Collection defOnOffences = defOnCase.getXhbDefendantOnOffences();
        for (Iterator i = defOnOffences.iterator(); i.hasNext();) {
            //crns.add(((XhbDefendantOnOffence) i.next()).getCrnId());
            seqNo = ((XhbDefendantOnOffence) i.next()).getSeqNo();
            if (isValidSeqNo(seqNo)) {
                crn = asn + getPaddedSequenceNo(seqNo);
                if (crn.length() != 23) {
                    log.error("Invalid length of CRN: " + crn);
                } else {
                    crns.add(crn);
                }
            }
        }
        return crns;
    }
    
    /**
     * Gets the ASNs
     * @return ASNs
     */
    public ASNs getAsns() {
        ASNs theAsns = null;
        if (_asns.size() > 0) {
            theAsns = new ASNs();
            theAsns.setASN(_asns.toArray(new String[] {}));
        }
        return theAsns;
    }

    /**
     * Gets the CRNs
     * @return CRNIDs
     */
    public CRNIDs getCrns() {
        CRNIDs theCrnIds = null;
        if (_crns.size() > 0) {
            theCrnIds = new CRNIDs();
            theCrnIds.setCRNID(_crns.toArray(new String[] {}));
        }
        return theCrnIds;
    }
    
    private String getPaddedSequenceNo(final Integer seqNo) {
        final StringBuilder theSeqNo = new StringBuilder("000");
        final String convertedSeqNo = seqNo.toString();
        theSeqNo.replace(3 - convertedSeqNo.length(), 3, convertedSeqNo);
        return theSeqNo.toString();        
    }
   
}