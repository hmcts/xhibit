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
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtLists;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendants;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearings;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sittings;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.HearingSummaryType;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.DailyCourtListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.DailyListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.DailyListLetterHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.HearingHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.ProsecutionHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.SittingHelper;

/**
 * <p>
 * Title: DailyListProcessor
 * </p>
 * <p>
 * Description: Process the daily list.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DailyListProcessor.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class DailyListProcessor extends AbstractListProcessor {

    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(DailyListProcessor.class);

    /**
     * AbstractListProcessor Implementation
     */
    // Fully qualify to stop confilct with java.util.List!
    protected uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List readListImpl(
            ClassDescriptorResolver classDescriptorResolver, Reader reader) throws MarshalException,
            ValidationException {
        return DailyListHelper.unmarshal(classDescriptorResolver, true, reader);
    }

    /**
     * AbstractListProcessor Implementation
     */
    // Fully qualify to stop confilct with java.util.List!
    protected ListLetter[] processListImpl(uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List list) {
        return process((DailyList) list);
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected void writeLetterImpl(ClassDescriptorResolver classDescriptorResolver, Writer writer, ListLetter listLetter)
            throws MarshalException, ValidationException, IOException, ClassCastException {
        DailyListLetterHelper.marshal(classDescriptorResolver, true, writer, (DailyListLetter) listLetter);
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected String getLetterTypeImpl(ListLetter listLetter) {
        if (listLetter == null || !(listLetter instanceof DailyListLetter)) {
            throw new IllegalArgumentException("listLetter: " + listLetter);
        }
        return "DLL";
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected String getLetterTitleImpl(ListLetter listLetter) {
        if (listLetter == null || !(listLetter instanceof DailyListLetter)) {
            throw new IllegalArgumentException("listLetter: " + listLetter);
        }
        return "Daily List Letter " + getFormatedTitleDate();
    }

    /**
     * Instantiate an instance and call its main method
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        new DailyListProcessor().main(args, "DL-", "DLL-");
    }

    /**
     * Process the daily list logging if required
     * 
     * @param dailyList
     *            the list to process
     * @return an array containing all the DailyListLetter
     * @throws NullPointerException
     *             if dailyList is null
     */
    private static DailyListLetter[] process(DailyList dailyList) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            DailyListLetter[] letters = _process(dailyList);
            log.debug("Generating " + letters.length + " daily list letters took "
                    + (System.currentTimeMillis() - starttime) + "ms.");
            for (int i = 0; i < letters.length; i++) {
                log.debug("    " + i + ": " + DailyListLetterHelper.toDebug(letters[i]));
            }
            return letters;
        } else {
            return _process(dailyList);
        }
    }

    // Extracted to make logging efficient
    private static DailyListLetter[] _process(DailyList dailyList) {
        // Populate the maps

        Map solicitorRecipientMap = new HashMap();
        Map defendantRecipientMap = new HashMap();
        Map prosecutionRecipientMap = new HashMap();

        populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, dailyList);

        // Consolidate the letters into a single array.

        int size = solicitorRecipientMap.size() + defendantRecipientMap.size() + prosecutionRecipientMap.size();
        DailyListLetter[] letters = new DailyListLetter[size];

        int index = 0;

        Iterator i = solicitorRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (DailyListLetter) i.next();
        }
        i = defendantRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (DailyListLetter) i.next();
        }
        i = prosecutionRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (DailyListLetter) i.next();
        }

        return letters;
    }

    /**
     * Populate the maps with the recipients
     * 
     * @param dailyList
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
            Map prosecutionRecipientMap, DailyList dailyList) {
        // Process all the court lists
        DailyCourtLists courtLists = dailyList.getDailyCourtLists();
        if (courtLists != null) {
            for (int i = 0, c = courtLists.getDailyCourtListCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, dailyList, courtLists
                        .getDailyCourtList(i));
            }
        }
    }

    // Process each sitting in the court list.
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, DailyList dailyList, DailyCourtList courtList) {
        Sittings sittings = courtList.getSittings();
        if (sittings != null) {
            for (int i = 0, c = sittings.getSittingCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, dailyList, courtList,
                        sittings.getSitting(i));
            }
        }
    }

    // Process each hearing in a sitting
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, DailyList dailyList, DailyCourtList courtList, Sitting sitting) {
        Hearings hearings = sitting.getHearings();
        if (hearings != null) {
            for (int i = 0, c = hearings.getHearingCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, dailyList, courtList,
                        sitting, hearings.getHearing(i));
            }
        }
    }

    // Process each defendant in a hearing and the hearing prosecutor
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, DailyList dailyList, DailyCourtList courtList, Sitting sitting, Hearing hearing) {
        Defendants defendants = hearing.getDefendants();
        if (defendants != null) {
            for (int i = 0, c = defendants.getDefendantCount(); i < c; i++) {
                populateDefendant(solicitorRecipientMap, defendantRecipientMap, dailyList, courtList, sitting, hearing,
                        defendants.getDefendant(i));
            }
        }

        Prosecution prosecution = hearing.getProsecution();
        if (prosecution != null) {
            populateProsecution(solicitorRecipientMap, prosecutionRecipientMap, dailyList, courtList, sitting, hearing,
                    prosecution);
        }
    }

    // If the defendant is represented create letters for all the solicitors
    // else send the defendant a letter directly.
    private static final void populateDefendant(Map solicitorRecipientMap, Map defendantRecipientMap,
            DailyList dailyList, DailyCourtList courtList, Sitting sitting, Hearing hearing, Defendant defendant) {
        boolean represented = false;
        // Create letters for all the defendants representation
        for (int i = 0, c = defendant.getCounselCount(); i < c; i++) {
            Counsel counsel = defendant.getCounsel(i);
            for (int j = 0, d = counsel.getSolicitorCount(); j < d; j++) {
                populateSolicitorRecipient(solicitorRecipientMap, dailyList, courtList, sitting, hearing, defendant,
                        counsel.getSolicitor(j));
                represented = true;
            }
        }

        // The defendant is not represented so should be sent a letter directly
        if (!represented) {
            populateDefendantRecipient(defendantRecipientMap, dailyList, courtList, sitting, hearing, defendant);
        }
    }

    // If the prosecution is represented create letters for all of the
    // solicitors else send the prosecution a letter directly.
    private static final void populateProsecution(Map solicitorRecipientMap, Map prosecutionRecipientMap,
            DailyList dailyList, DailyCourtList courtList, Sitting sitting, Hearing hearing, Prosecution prosecution) {
        // Create letters for all the prosecutions representation
        int c = prosecution.getSolicitorCount();
        if (0 < c) {
            populateSolicitorRecipient(solicitorRecipientMap, dailyList, courtList, sitting, hearing, null, prosecution
                    .getSolicitor(0));
            for (int i = 1; i < c; i++) {
                populateSolicitorRecipient(solicitorRecipientMap, dailyList, courtList, sitting, hearing, null,
                        prosecution.getSolicitor(i));
            }
        }
        // The prosecution is not represented so if not CPS should be sent a
        // letter directly
        else if (!ProsecutionHelper.isCps(prosecution)) {
            populateProsecutionRecipient(prosecutionRecipientMap, dailyList, courtList, sitting, hearing, prosecution);
        }
    }

    // Check if the solicitor already has a letter
    private static final void populateSolicitorRecipient(Map solicitorRecipientMap, DailyList dailyList,
            DailyCourtList courtList, Sitting sitting, Hearing hearing, Defendant defendant, Solicitor solicitor) {
        String id = solicitor.getId();

        DailyListLetter dailyListLetter = (DailyListLetter) solicitorRecipientMap.get(id);
        if (dailyListLetter != null) {
            populate(courtList, sitting, hearing, defendant, solicitor, dailyListLetter);
        } else {
            solicitorRecipientMap.put(id, DailyListLetterHelper.create(dailyList, courtList, sitting, hearing,
                    defendant, null, solicitor));
        }
    }

    // Check if the defendant already has a letter
    private static final void populateDefendantRecipient(Map defendantRecipientMap, DailyList dailyList,
            DailyCourtList courtList, Sitting sitting, Hearing hearing, Defendant defendant) {
        String id = defendant.getId();

        DailyListLetter dailyListLetter = (DailyListLetter) defendantRecipientMap.get(id);
        if (dailyListLetter != null) {
            populate(courtList, sitting, hearing, defendant, null, dailyListLetter);
        } else {
            defendantRecipientMap.put(id, DailyListLetterHelper.create(dailyList, courtList, sitting, hearing,
                    defendant, null, null));
        }
    }

    // Check if the prosecution already has a letter
    private static final void populateProsecutionRecipient(Map prosecutionRecipientMap, DailyList dailyList,
            DailyCourtList courtList, Sitting sitting, Hearing hearing, Prosecution prosecution) {
        String id = prosecution.getId();

        DailyListLetter dailyListLetter = (DailyListLetter) prosecutionRecipientMap.get(id);
        if (dailyListLetter != null) {
            populate(courtList, sitting, hearing, null, null, dailyListLetter);
        } else {
            prosecutionRecipientMap.put(id, DailyListLetterHelper.create(dailyList, courtList, sitting, hearing, null,
                    prosecution, null));
        }
    }

    // Check if the court list summary exists in the letter,
    // if yes process child else add.
    private static final void populate(DailyCourtList courtList, Sitting sitting, Hearing hearing, Defendant defendant,
            Solicitor solicitor, DailyListLetter dailyListLetter) {
        DailyCourtListSummary dailyCourtListSummary = DailyListLetterHelper.getDailyCourtListSummary(dailyListLetter,
                courtList);
        if (dailyCourtListSummary != null) {
            populate(sitting, hearing, defendant, solicitor, dailyCourtListSummary);
        } else {
            dailyCourtListSummary = DailyCourtListHelper.createSummary(courtList, sitting, hearing, defendant,
                    solicitor);
            dailyListLetter.getDailyCourtListSummaries().addDailyCourtListSummary(dailyCourtListSummary);
        }
    }

    // Check if the sitting summary exists in the court list summary,
    // if yes process child else add.
    private static final void populate(Sitting sitting, Hearing hearing, Defendant defendant, Solicitor solicitor,
            DailyCourtListSummary dailyCourtListSummary) {
        SittingSummary sittingSummary = DailyCourtListHelper.getSittingSummary(dailyCourtListSummary, sitting);
        if (sittingSummary != null) {
            populate(hearing, defendant, solicitor, sittingSummary);
        } else {
            sittingSummary = SittingHelper.createSummary(sitting, hearing, defendant, solicitor);
            // The dailyCourtListSummary allready contains at least 1
            // sitting
            // summary
            dailyCourtListSummary.getSittingSummaries().addSittingSummary(sittingSummary);
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
}
