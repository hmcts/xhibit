package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Fixture;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtListSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDate;

/**
 * <p>
 * Title: WarnedListLetterHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating WarnedListLetter XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WarnedListLetterHelper.java,v 1.4 2005/02/08 13:56:48 bzjrnl
 *          Exp $
 */
public class WarnedListLetterHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(WarnedListLetterHelper.class);

    /**
     * Create a new letter from the warned list for the recipient
     * 
     * @param warnedList
     *            the list
     * @param courtList
     *            the court list must not be null
     * @param withFixedDate
     *            the withFixedDate, if null withoutFixedDate
     * @param withoutFixedDate
     *            the withoutFixedDate, if null withFixedDate
     * @param fixture
     *            the fixture must not be null
     * @param caze
     *            the case must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the new WarnedListLetter
     */
    public static WarnedListLetter create(WarnedList warnedList, WarnedCourtList courtList,
            WithFixedDate withFixedDate, WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze,
            Defendant defendant, Prosecution prosecution, Solicitor solicitor) {
        WarnedListLetter warnedListLetter = new WarnedListLetter();

        warnedListLetter.setRecipient(RecipientHelper.create(defendant, prosecution, solicitor));

        warnedListLetter.setListHeaderSummary(ListHeaderHelper.createSummary(warnedList.getListHeader()));
        warnedListLetter.setCrownCourt(warnedList.getCrownCourt());
        warnedListLetter.setWarnedListDetail(warnedList.getWarnedListDetail());
        warnedListLetter.setWarnedCourtListSummaries(new WarnedCourtListSummaries());

        WarnedCourtListSummary courtListSummary = WarnedCourtListHelper.createSummary(courtList, withFixedDate,
                withoutFixedDate, fixture, caze, defendant, solicitor);

        warnedListLetter.getWarnedCourtListSummaries().addWarnedCourtListSummary(courtListSummary);

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(warnedListLetter) + ".");
        }

        return warnedListLetter;
    }

    /**
     * Get the court list summary for the court list or null if it does not
     * exist
     * 
     * @param warnedListLetter
     *            the letter to look for the court list in
     * @param courtList
     *            the detailed court list for which we are looking for the
     *            summary
     */
    public static WarnedCourtListSummary getWarnedCourtListSummary(WarnedListLetter warnedListLetter,
            WarnedCourtList courtList) {
        WarnedCourtListSummaries courtListSummaries = warnedListLetter.getWarnedCourtListSummaries();
        for (int i = 0, c = courtListSummaries.getWarnedCourtListSummaryCount(); i < c; i++) {
            WarnedCourtListSummary courtListSummary = courtListSummaries.getWarnedCourtListSummary(i);
            if (WarnedCourtListHelper.summaryOf(courtListSummary, courtList)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + WarnedCourtListHelper.toDebug(courtListSummary) + " in "
                            + toDebug(warnedListLetter) + ".");
                }
                return courtListSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + WarnedCourtListHelper.toDebug(courtList) + " in "
                    + toDebug(warnedListLetter) + ".");
        }

        return null;
    }

    /**
     * Get the number of cases on the letter
     * 
     * @param warnedListLetter
     * @return the number of cases
     */
    public static int getCaseCount(WarnedListLetter warnedListLetter) {
        return warnedListLetter != null ? WarnedCourtListHelper.getCaseCount(warnedListLetter
                .getWarnedCourtListSummaries()) : 0;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param warnedListLetter
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WarnedListLetter warnedListLetter) {
        return "WarnedListLetter[recipient=" + RecipientHelper.toDebug(warnedListLetter.getRecipient())
                + ", caseCount=" + getCaseCount(warnedListLetter) + ", valid=" + warnedListLetter.isValid() + "]";
    }

    /**
     * Marshal the warnedListLetter setting up the namespaces to reduce the size
     * of the output.
     * 
     * @param cdr
     *            the Class Descriptor Resolver, reusing this stops castor
     *            reloading descriptions.
     * @param validation
     *            pass in true to validate
     * @param warnedListLetter
     *            the letter to marshal
     * @param out
     *            the writer to marshal to
     */
    public static void marshal(ClassDescriptorResolver cdr, boolean validation, Writer out,
            WarnedListLetter warnedListLetter) throws MarshalException, ValidationException, IOException {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            _marshal(cdr, validation, out, warnedListLetter);
            log.debug("Marshaling warned list letter took " + (System.currentTimeMillis() - startTime) + "ms.");
        } else {
            _marshal(cdr, validation, out, warnedListLetter);
        }
    }

    private static final void _marshal(ClassDescriptorResolver cdr, boolean validation, Writer out,
            WarnedListLetter warnedListLetter) throws MarshalException, ValidationException, IOException {
        Marshaller marshaller = new Marshaller(out);
        marshaller.setResolver(cdr);
        marshaller.setValidation(validation);
        /*
         * No Longer Required As Moved Into Same Namespace
         * marshaller.setNamespaceMapping("bs7666",
         * "http://www.govtalk.gov.uk/people/bs7666");
         * marshaller.setNamespaceMapping("apd",
         * "http://www.govtalk.gov.uk/people/AddressAndPersonalDetails");
         * marshaller.setNamespaceMapping("cs",
         * "http://www.courtservice.gov.uk/schemas/courtservice");
         */
        marshaller.marshal(warnedListLetter);
    }
}