package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Counsel;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendants;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtLists;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearings;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ReserveList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ReserveListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sittings;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.HearingSummaryType;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.FirmCourtListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.FirmListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.FirmListLetterHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.HearingHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.ProsecutionHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.ReserveListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.SittingHelper;

/**
 * <p>
 * Title: FirmListProcessor
 * </p>
 * <p>
 * Description: Process the Firm list.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: FirmListProcessor.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class FirmListProcessor extends AbstractListProcessor {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(FirmListProcessor.class);

    /**
     * AbstractListProcessor Implementation
     */
    // Fully qualify to stop confilct with java.util.List!
    protected uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List readListImpl(
            ClassDescriptorResolver classDescriptorResolver, Reader reader) throws MarshalException,
            ValidationException {
        return FirmListHelper.unmarshal(classDescriptorResolver, true, reader);
    }

    /**
     * AbstractListProcessor Implementation
     */
    // Fully qualify to stop confilct with java.util.List!
    protected ListLetter[] processListImpl(uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List list) {
        return process((FirmList) list);
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected void writeLetterImpl(ClassDescriptorResolver classDescriptorResolver, Writer writer, ListLetter listLetter)
            throws MarshalException, ValidationException, IOException, ClassCastException {
        FirmListLetterHelper.marshal(classDescriptorResolver, true, writer, (FirmListLetter) listLetter);
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected String getLetterTypeImpl(ListLetter listLetter) {
        if (listLetter == null || !(listLetter instanceof FirmListLetter)) {
            throw new IllegalArgumentException("listLetter: " + listLetter);
        }
        return "FLL";
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected String getLetterTitleImpl(ListLetter listLetter) {
        if (listLetter == null || !(listLetter instanceof FirmListLetter)) {
            throw new IllegalArgumentException("listLetter: " + listLetter);
        }
        return "Firm List Letter " + getFormatedTitleDate();
    }

    /**
     * Instantiate an instance and call its main method
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        new FirmListProcessor().main(args, "FL-", "FLL-");
    }

    /**
     * Process the firm list logging if required
     * 
     * @param firmList
     *            the list to process
     * @return an array containing all the FirmListLetter
     * @throws NullPointerException
     *             if firmList is null
     */
    private static FirmListLetter[] process(FirmList firmList) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            FirmListLetter[] letters = _process(firmList);
            log.debug("Generating " + letters.length + " firm list letters took "
                    + (System.currentTimeMillis() - starttime) + "ms.");
            for (int i = 0; i < letters.length; i++) {
                log.debug("    " + i + ": " + FirmListLetterHelper.toDebug(letters[i]));
            }
            return letters;
        } else {
            return _process(firmList);
        }
    }

    // Extracted to make logging efficient
    private static FirmListLetter[] _process(FirmList firmList) {
        // Populate the maps

        Map solicitorRecipientMap = new HashMap();
        Map defendantRecipientMap = new HashMap();
        Map prosecutionRecipientMap = new HashMap();

        populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, firmList);

        // Consolidate the letters into a single array.

        int size = solicitorRecipientMap.size() + defendantRecipientMap.size() + prosecutionRecipientMap.size();
        FirmListLetter[] letters = new FirmListLetter[size];

        int index = 0;

        Iterator i = solicitorRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (FirmListLetter) i.next();
        }
        i = defendantRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (FirmListLetter) i.next();
        }
        i = prosecutionRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (FirmListLetter) i.next();
        }

        return letters;
    }

    /**
     * Populate the maps with the FirmList letters from the firmList.
     * 
     * @param firmList
     *            the list to get the letters from
     * @param solicitorRecipientMap
     *            the map to populate with represented letters keyed by
     *            solicitor id
     * @param defendantRecipientMap
     *            the map to populate with unrepresented defendant letters keyed
     *            by defendant id
     * @param prosecutionRecipientMap
     *            the map to populate with unrepresented prosecution letters
     *            keyed by prosecution id
     */
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, FirmList firmList) {
        // Process all the court lists and the reserve list
        FirmCourtLists courtLists = firmList.getFirmCourtLists();
        if (courtLists != null) {
            for (int i = 0, c = courtLists.getFirmCourtListCount(); i < c; i++) {
                FirmCourtList courtList = courtLists.getFirmCourtList(i);
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, firmList, courtList);
            }
        }

        // If we have a reserve list process it
        ReserveList reserveList = firmList.getReserveList();
        if (reserveList != null) {
            populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, firmList, reserveList);
        }
    }

    // Process each sitting in the court list.
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, FirmList firmList, FirmCourtList courtList) {
        Sittings sittings = courtList.getSittings();
        if (sittings != null) {
            for (int i = 0, c = sittings.getSittingCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, firmList, courtList,
                        sittings.getSitting(i));
            }
        }
    }

    // Process each hearing in the reserve list.
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, FirmList firmList, ReserveList reserveList) {
        for (int i = 0, c = reserveList.getHearingCount(); i < c; i++) {
            populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, firmList, null, null,
                    reserveList, reserveList.getHearing(i));
        }
    }

    // Process each hearing in a sitting
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, FirmList firmList, FirmCourtList courtList, Sitting sitting) {
        Hearings hearings = sitting.getHearings();
        if (hearings != null) {
            for (int i = 0, c = hearings.getHearingCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, firmList, courtList,
                        sitting, null, hearings.getHearing(i));
            }
        }
    }

    // Either courtList & sitting must be not null and reserveList must be
    // null
    // or
    // reserveList is not null and sitting and courtList are null.
    // If courtList is null then reserveList!
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, FirmList firmList, FirmCourtList courtList, Sitting sitting,
            ReserveList reserveList, Hearing hearing) {
        Defendants defendants = hearing.getDefendants();
        if (defendants != null) {
            for (int i = 0, c = defendants.getDefendantCount(); i < c; i++) {
                populateDefendant(solicitorRecipientMap, defendantRecipientMap, firmList, courtList, sitting,
                        reserveList, hearing, defendants.getDefendant(i));
            }
        }

        Prosecution prosecution = hearing.getProsecution();
        if (prosecution != null) {
            populateProsecution(solicitorRecipientMap, prosecutionRecipientMap, firmList, courtList, sitting,
                    reserveList, hearing, prosecution);
        }
    }

    // If the defendant is represented create letters for all the solicitors
    // else send the defendant a letter directly.
    private static final void populateDefendant(Map solicitorRecipientMap, Map defendantRecipientMap,
            FirmList firmList, FirmCourtList courtList, Sitting sitting, ReserveList reserveList, Hearing hearing,
            Defendant defendant) {
        boolean represented = false;
        // Create letters for all the defendants representation
        for (int i = 0, c = defendant.getCounselCount(); i < c; i++) {
            Counsel counsel = defendant.getCounsel(i);
            for (int j = 0, d = counsel.getSolicitorCount(); j < d; j++) {
                populateSolicitorRecipient(solicitorRecipientMap, firmList, courtList, sitting, reserveList, hearing,
                        defendant, counsel.getSolicitor(j));
                represented = true;
            }
        }

        // The defendant is not represented so should be sent a letter directly
        if (!represented) {
            populateDefendantRecipient(defendantRecipientMap, firmList, courtList, sitting, reserveList, hearing,
                    defendant);
        }
    }

    // If the prosecution is represented create letters for all of the
    // solicitors
    // else send the prosecution a letter directly.
    private static final void populateProsecution(Map solicitorRecipientMap, Map prosecutionRecipientMap,
            FirmList firmList, FirmCourtList courtList, Sitting sitting, ReserveList reserveList, Hearing hearing,
            Prosecution prosecution) {
        // Create letters for all the prosecutions representation
        int c = prosecution.getSolicitorCount();
        if (0 < c) {
            populateSolicitorRecipient(solicitorRecipientMap, firmList, courtList, sitting, reserveList, hearing, null,
                    prosecution.getSolicitor(0));
            for (int i = 1; i < c; i++) {
                populateSolicitorRecipient(solicitorRecipientMap, firmList, courtList, sitting, reserveList, hearing,
                        null, prosecution.getSolicitor(i));
            }
        }
        // The prosecution is not represented if not CPS should be sent a letter
        // directly
        else if (!ProsecutionHelper.isCps(prosecution)) {
            populateProsecutionRecipient(prosecutionRecipientMap, firmList, courtList, sitting, reserveList, hearing,
                    prosecution);
        }
    }

    // If courtlist is null then reserve list.
    // If courtList is not null then sitting must not be null.
    // If reserveList is null then court list.
    // If defendant is null then prosecution
    private static final void populateSolicitorRecipient(Map solicitorRecipientMap, FirmList firmList,
            FirmCourtList courtList, Sitting sitting, ReserveList reserveList, Hearing hearing, Defendant defendant,
            Solicitor solicitor) {
        String id = solicitor.getId();

        FirmListLetter firmListLetter = (FirmListLetter) solicitorRecipientMap.get(id);
        if (firmListLetter != null) {
            populate(courtList, sitting, reserveList, hearing, defendant, solicitor, firmListLetter);
        } else {
            solicitorRecipientMap.put(id, FirmListLetterHelper.create(firmList, courtList, sitting, reserveList,
                    hearing, defendant, null, solicitor));
        }
    }

    // courtlist and sitting, and reservelist are mutually exclusive.
    // if reserveList is null then courtList and sitting.
    private static final void populateDefendantRecipient(Map defendantRecipientMap, FirmList firmList,
            FirmCourtList courtList, Sitting sitting, ReserveList reserveList, Hearing hearing, Defendant defendant) {
        String id = defendant.getId();

        FirmListLetter firmListLetter = (FirmListLetter) defendantRecipientMap.get(id);
        if (firmListLetter != null) {
            populate(courtList, sitting, reserveList, hearing, defendant, null, firmListLetter);
        } else {
            defendantRecipientMap.put(id, FirmListLetterHelper.create(firmList, courtList, sitting, reserveList,
                    hearing, defendant, null, null));
        }
    }

    // courtlist and sitting, and reservelist are mutually exclusive.
    // if reserveList is null then courtList and sitting.
    private static final void populateProsecutionRecipient(Map prosecutionRecipientMap, FirmList firmList,
            FirmCourtList courtList, Sitting sitting, ReserveList reserveList, Hearing hearing, Prosecution prosecution) {
        String id = prosecution.getId();

        FirmListLetter firmListLetter = (FirmListLetter) prosecutionRecipientMap.get(id);
        if (firmListLetter != null) {
            populate(courtList, sitting, reserveList, hearing, null, null, firmListLetter);
        } else {
            prosecutionRecipientMap.put(id, FirmListLetterHelper.create(firmList, courtList, sitting, reserveList,
                    hearing, null, prosecution, null));
        }
    }

    // If court list is not null process the sitting in the list
    // else assume it is a reserve list entry and process that
    private static final void populate(FirmCourtList courtList, Sitting sitting, ReserveList reserveList,
            Hearing hearing, Defendant defendant, Solicitor solicitor, FirmListLetter firmListLetter) {
        if (courtList != null) {
            populate(courtList, sitting, hearing, defendant, solicitor, firmListLetter);
        } else {
            populate(reserveList, hearing, defendant, solicitor, firmListLetter);
        }
    }

    // Check if the court list summary exists in the letter,
    // if yes process child else add.
    private static final void populate(FirmCourtList courtList, Sitting sitting, Hearing hearing, Defendant defendant,
            Solicitor solicitor, FirmListLetter firmListLetter) {
        FirmCourtListSummary firmCourtListSummary = FirmListLetterHelper.getFirmCourtListSummary(firmListLetter,
                courtList);
        if (firmCourtListSummary != null) {
            populate(sitting, hearing, defendant, solicitor, firmCourtListSummary);
        } else {
            firmCourtListSummary = FirmCourtListHelper.createSummary(courtList, sitting, hearing, defendant, solicitor);
            firmListLetter.getFirmCourtListSummaries().addFirmCourtListSummary(firmCourtListSummary);
        }
    }

    // Check if the sitting summary exists in the court list summary,
    // if yes process child else add.
    private static final void populate(Sitting sitting, Hearing hearing, Defendant defendant, Solicitor solicitor,
            FirmCourtListSummary firmCourtListSummary) {
        SittingSummary sittingSummary = FirmCourtListHelper.getSittingSummary(firmCourtListSummary, sitting);
        if (sittingSummary != null) {
            populate(hearing, defendant, solicitor, sittingSummary);
        } else {
            sittingSummary = SittingHelper.createSummary(sitting, hearing, defendant, solicitor);
            // The firmCourtListSummary allready contains at least 1 sitting
            // summary
            firmCourtListSummary.getSittingSummaries().addSittingSummary(sittingSummary);
        }
    }

    // Check if the hearing summary (of the correct type) exists in the
    // sitting summary, if yes log success else add.
    private static final void populate(Hearing hearing, Defendant defendant, Solicitor solicitor,
            SittingSummary sittingSummary) {
        HearingSummary hearingSummary;
        if (defendant != null) {
            hearingSummary = SittingHelper.getHearingSummary(sittingSummary, hearing, HearingSummaryType.DEFENDANT);
        } else {
            hearingSummary = SittingHelper.getHearingSummary(sittingSummary, hearing, HearingSummaryType.PROSECUTION);
        }

        if (hearingSummary != null) {
            if (log.isDebugEnabled()) {
                log.debug("Found " + HearingHelper.toDebug(hearingSummary) + ".");
            }
        } else {

            hearingSummary = HearingHelper.createSummary(hearing, defendant, solicitor);
            // The sittingSummary allready contains at least 1 hearing
            // summary
            sittingSummary.getHearingSummaries().addHearingSummary(hearingSummary);
        }
    }

    // Check if the hearing summary (of the correct type) exists in the
    // reserve list, if yes log success else add.
    private static final void populate(ReserveList reserveList, Hearing hearing, Defendant defendant,
            Solicitor solicitor, FirmListLetter firmListLetter) {
        ReserveListSummary reserveListSummary = firmListLetter.getReserveListSummary();
        if (reserveListSummary != null) {
            populate(hearing, defendant, solicitor, reserveListSummary);
        } else {
            firmListLetter.setReserveListSummary(ReserveListHelper.createSummary(reserveList, hearing, defendant,
                    solicitor));
        }
    }

    // Check if the hearing summary (of the correct type) exists in the
    // Reserve list, if yes log success else add.
    private static final void populate(Hearing hearing, Defendant defendant, Solicitor solicitor,
            ReserveListSummary reserveListSummary) {
        HearingSummary hearingSummary;
        if (defendant != null) {
            hearingSummary = ReserveListHelper.getHearingSummary(reserveListSummary, hearing,
                    HearingSummaryType.DEFENDANT);
        } else {
            hearingSummary = ReserveListHelper.getHearingSummary(reserveListSummary, hearing,
                    HearingSummaryType.PROSECUTION);
        }

        if (hearingSummary != null) {
            if (log.isDebugEnabled()) {
                log.debug("Found " + HearingHelper.toDebug(hearingSummary) + ".");
            }
        } else {
            hearingSummary = HearingHelper.createSummary(hearing, defendant, solicitor);
            reserveListSummary.addHearingSummary(hearingSummary);
        }
    }

}
