package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtListSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ReserveList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: FirmListLetterHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating FirmListLetter XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: FirmListLetterHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class FirmListLetterHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(FirmListLetterHelper.class);

    /**
     * Create a new letter from the firm list for the recipient
     * 
     * @param firmList
     *            the list
     * @param courtList
     *            the court list, if the court list is null then reserve list
     * @param sitting
     *            the sitting must not be null if the court list is not null
     * @param reserveList
     *            the reserve list, if the reserve list is null then court list
     * @param hearing
     *            the case must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the new FirmListLetter
     */
    public static FirmListLetter create(FirmList firmList, FirmCourtList courtList, Sitting sitting,
            ReserveList reserveList, Hearing hearing, Defendant defendant, Prosecution prosecution, Solicitor solicitor) {
        FirmListLetter firmListLetter = new FirmListLetter();

        firmListLetter.setRecipient(RecipientHelper.create(defendant, prosecution, solicitor));

        firmListLetter.setListHeaderSummary(ListHeaderHelper.createSummary(firmList.getListHeader()));
        firmListLetter.setCrownCourt(firmList.getCrownCourt());

        if (courtList != null) {
            firmListLetter.setFirmCourtListSummaries(createFirmCourtListSummaries(courtList, sitting, hearing,
                    defendant, solicitor));
        } else {
            firmListLetter.setReserveListSummary(ReserveListHelper.createSummary(reserveList, hearing, defendant,
                    solicitor));
        }

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(firmListLetter) + ".");
        }

        return firmListLetter;
    }

    /**
     * Create a new firm court list summaries object for the court list summary
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
    private static FirmCourtListSummaries createFirmCourtListSummaries(FirmCourtList courtList, Sitting sitting,
            Hearing hearing, Defendant defendant, Solicitor solicitor) {
        FirmCourtListSummaries firmCourtListSummaries = new FirmCourtListSummaries();

        firmCourtListSummaries.addFirmCourtListSummary(FirmCourtListHelper.createSummary(courtList, sitting, hearing,
                defendant, solicitor));

        return firmCourtListSummaries;
    }

    /**
     * Get the court list summary for the court list or null if it does not
     * exist
     * 
     * @param firmListLetter
     *            the letter to look for the court list in
     * @param courtList
     *            the detailed court list for which we are looking for the
     *            summary
     */
    public static FirmCourtListSummary getFirmCourtListSummary(FirmListLetter firmListLetter, FirmCourtList courtList) {
        FirmCourtListSummaries courtListSummaries = firmListLetter.getFirmCourtListSummaries();
        for (int i = 0, c = courtListSummaries.getFirmCourtListSummaryCount(); i < c; i++) {
            FirmCourtListSummary courtListSummary = courtListSummaries.getFirmCourtListSummary(i);
            if (FirmCourtListHelper.summaryOf(courtListSummary, courtList)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + FirmCourtListHelper.toDebug(courtListSummary) + " in "
                            + toDebug(firmListLetter) + ".");
                }
                return courtListSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + FirmCourtListHelper.toDebug(courtList) + " in "
                    + toDebug(firmListLetter) + ".");
        }

        return null;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param firmListLetter
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(FirmListLetter firmListLetter) {
        return "FirmListLetter[recipient=" + RecipientHelper.toDebug(firmListLetter.getRecipient()) + ", hearingCount="
                + getHearingCount(firmListLetter) + ", valid=" + firmListLetter.isValid() + "]";
    }

    /**
     * Count the number of hearings on the court lists and the reserve list
     * 
     * @param firmListLetter
     *            the letter to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(FirmListLetter firmListLetter) {
        if (firmListLetter != null) {
            return FirmCourtListHelper.getHearingCount(firmListLetter.getFirmCourtListSummaries())
                    + ReserveListHelper.getHearingCount(firmListLetter.getReserveListSummary());
        } else {
            return 0;
        }
    }

    /**
     * Marshal the firmListLetter setting up the namespaces to reduce the size
     * of the output.
     * 
     * @param cdr
     *            the Class Descriptor Resolver, reusing this stops castor
     *            reloading descriptions.
     * @param validation
     *            pass in true to validate
     * @param firmListLetter
     *            the letter to marshal
     * @param out
     *            the writer to marshal to
     */
    public static void marshal(ClassDescriptorResolver cdr, boolean validation, Writer out,
            FirmListLetter firmListLetter) throws MarshalException, ValidationException, IOException {
        if (log.isDebugEnabled()) {
            long startTime = System.currentTimeMillis();
            _marshal(cdr, validation, out, firmListLetter);
            log.debug("Marshaling firm list letter took " + (System.currentTimeMillis() - startTime) + "ms.");
        } else {
            _marshal(cdr, validation, out, firmListLetter);
        }
    }

    private static final void _marshal(ClassDescriptorResolver cdr, boolean validation, Writer out,
            FirmListLetter firmListLetter) throws MarshalException, ValidationException, IOException {
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
        marshaller.marshal(firmListLetter);
    }
}
