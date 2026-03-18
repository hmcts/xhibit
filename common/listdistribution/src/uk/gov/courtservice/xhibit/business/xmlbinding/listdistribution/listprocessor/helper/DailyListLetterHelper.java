package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtListSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: DailyListLetterHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating DailyListLetter XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DailyListLetterHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class DailyListLetterHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(DailyListLetterHelper.class);

    /**
     * Create a new letter from the daily list for the recipient
     * 
     * @param dailyList
     *            the list
     * @param courtList
     *            the court list must not be null
     * @param sitting
     *            the sitting must not be null
     * @param hearing
     *            the case must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the new DailyListLetter
     */
    public static DailyListLetter create(DailyList dailyList, DailyCourtList courtList, Sitting sitting,
            Hearing hearing, Defendant defendant, Prosecution prosecution, Solicitor solicitor) {
        DailyListLetter dailyListLetter = new DailyListLetter();

        dailyListLetter.setRecipient(RecipientHelper.create(defendant, prosecution, solicitor));

        dailyListLetter.setListHeaderSummary(ListHeaderHelper.createSummary(dailyList.getListHeader()));
        dailyListLetter.setCrownCourt(dailyList.getCrownCourt());

        dailyListLetter.setDailyCourtListSummaries(createDailyCourtListSummaries(courtList, sitting, hearing,
                defendant, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(dailyListLetter) + ".");
        }

        return dailyListLetter;
    }

    /**
     * Create a new daily court list summaries object for the court list summary
     * 
     * @param sitting
     *            the sitting must not be null
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the summaries object
     */
    private static DailyCourtListSummaries createDailyCourtListSummaries(DailyCourtList courtList, Sitting sitting,
            Hearing hearing, Defendant defendant, Solicitor solicitor) {
        DailyCourtListSummaries dailyCourtListSummaries = new DailyCourtListSummaries();

        dailyCourtListSummaries.addDailyCourtListSummary(DailyCourtListHelper.createSummary(courtList, sitting,
                hearing, defendant, solicitor));

        return dailyCourtListSummaries;
    }

    /**
     * Get the court list summary for the court list or null if it does not
     * exist
     * 
     * @param dailyListLetter
     *            the letter to look for the court list in
     * @param courtList
     *            the detailed court list for which we are looking for the
     *            summary
     */
    public static DailyCourtListSummary getDailyCourtListSummary(DailyListLetter dailyListLetter,
            DailyCourtList courtList) {
        DailyCourtListSummaries courtListSummaries = dailyListLetter.getDailyCourtListSummaries();
        for (int i = 0, c = courtListSummaries.getDailyCourtListSummaryCount(); i < c; i++) {
            DailyCourtListSummary courtListSummary = courtListSummaries.getDailyCourtListSummary(i);
            if (DailyCourtListHelper.summaryOf(courtListSummary, courtList)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + DailyCourtListHelper.toDebug(courtListSummary) + " in "
                            + toDebug(dailyListLetter) + ".");
                }
                return courtListSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + DailyCourtListHelper.toDebug(courtList) + " in "
                    + toDebug(dailyListLetter) + ".");
        }

        return null;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param dailyListLetter
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(DailyListLetter dailyListLetter) {
        return "DailyListLetter[recipient=" + RecipientHelper.toDebug(dailyListLetter.getRecipient())
                + ", hearingCount=" + getHearingCount(dailyListLetter) + ", valid=" + dailyListLetter.isValid() + "]";
    }

    /**
     * Count the number of hearings on the court lists and the reserve list
     * 
     * @param dailyListLetter
     *            the letter to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(DailyListLetter dailyListLetter) {
        if (dailyListLetter != null) {
            return DailyCourtListHelper.getHearingCount(dailyListLetter.getDailyCourtListSummaries());
        } else {
            return 0;
        }
    }

    /**
     * Marshal the dailyListLetter setting up the namespaces to reduce the size
     * of the output.
     * 
     * @param cdr
     *            the Class Descriptor Resolver, reusing this stops castor
     *            reloading descriptions.
     * @param validation
     *            pass in true to validate
     * @param dailyListLetter
     *            the letter to marshal
     * @param out
     *            the writer to marshal to
     */
    public static void marshal(ClassDescriptorResolver cdr, boolean validation, Writer out,
            DailyListLetter dailyListLetter) throws MarshalException, ValidationException, IOException {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            _marshal(cdr, validation, out, dailyListLetter);
            log.debug("Marshaling daily list letter took " + (System.currentTimeMillis() - startTime) + "ms.");
        } else {
            _marshal(cdr, validation, out, dailyListLetter);
        }
    }

    private static final void _marshal(ClassDescriptorResolver cdr, boolean validation, Writer out,
            DailyListLetter dailyListLetter) throws MarshalException, ValidationException, IOException {
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
        marshaller.marshal(dailyListLetter);
    }
}
