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
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CaseSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Cases;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Counsel;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendants;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Fixture;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FixtureSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtLists;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedListLetter;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDateSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDateSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.CaseSummaryType;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.CaseHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.FixtureHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.ProsecutionHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.WarnedCourtListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.WarnedListHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.WarnedListLetterHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.WithFixedDateHelper;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper.WithoutFixedDateHelper;

/**
 * <p>
 * Title: WarnedListProcessor
 * </p>
 * <p>
 * Description: Process the Warned list.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WarnedListProcessor.java,v 1.4 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class WarnedListProcessor extends AbstractListProcessor {

    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(WarnedListProcessor.class);

    /**
     * AbstractListProcessor Implementation
     */
    // Fully qualify to stop confilct with java.util.List!
    protected uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List readListImpl(
            ClassDescriptorResolver classDescriptorResolver, Reader reader) throws MarshalException,
            ValidationException {
        return WarnedListHelper.unmarshal(classDescriptorResolver, true, reader);
    }

    /**
     * AbstractListProcessor Implementation
     */
    // Fully qualify to stop confilct with java.util.List!
    protected ListLetter[] processListImpl(uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List list) {
        return process((WarnedList) list);
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected void writeLetterImpl(ClassDescriptorResolver classDescriptorResolver, Writer writer, ListLetter listLetter)
            throws MarshalException, ValidationException, IOException, ClassCastException {
        WarnedListLetterHelper.marshal(classDescriptorResolver, true, writer, (WarnedListLetter) listLetter);
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected String getLetterTypeImpl(ListLetter listLetter) {
        if (listLetter == null || !(listLetter instanceof WarnedListLetter)) {
            throw new IllegalArgumentException("listLetter: " + listLetter);
        }
        return "WLL";
    }

    /**
     * AbstractListProcessor Implementation
     */
    protected String getLetterTitleImpl(ListLetter listLetter) {
        if (listLetter == null || !(listLetter instanceof WarnedListLetter)) {
            throw new IllegalArgumentException("listLetter: " + listLetter);
        }
        return "Warned List Letter " + getFormatedTitleDate();
    }

    /**
     * Instantiate an instance and call its main method
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String args[]) {
        new WarnedListProcessor().main(args, "WL-", "WLL-");
    }

    /**
     * Process the warned list logging if required
     * 
     * @param warnedList
     *            the list to process
     * @return an array containing all the WarnedListLetter
     * @throws NullPointerException
     *             if warnedList is null
     */
    private static WarnedListLetter[] process(WarnedList warnedList) {
        if (log.isDebugEnabled()) {
            long starttime = System.currentTimeMillis();
            WarnedListLetter[] letters = _process(warnedList);
            log.debug("Generating " + letters.length + " warned list letters took "
                    + (System.currentTimeMillis() - starttime) + "ms.");
            for (int i = 0; i < letters.length; i++) {
                log.debug("    " + i + ": " + WarnedListLetterHelper.toDebug(letters[i]));
            }
            return letters;
        } else {
            return _process(warnedList);
        }
    }

    // Extracted to make logging efficient
    private static WarnedListLetter[] _process(WarnedList warnedList) {
        // Populate the maps

        Map solicitorRecipientMap = new HashMap();
        Map defendantRecipientMap = new HashMap();
        Map prosecutionRecipientMap = new HashMap();

        populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList);

        // Consolidate the letters into a single array.

        int size = solicitorRecipientMap.size() + defendantRecipientMap.size() + prosecutionRecipientMap.size();
        WarnedListLetter[] letters = new WarnedListLetter[size];

        int index = 0;

        Iterator i = solicitorRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (WarnedListLetter) i.next();
        }
        i = defendantRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (WarnedListLetter) i.next();
        }
        i = prosecutionRecipientMap.values().iterator();
        while (i.hasNext()) {
            letters[index++] = (WarnedListLetter) i.next();
        }

        return letters;
    }

    /**
     * Populate the recipient maps with the letters
     * 
     * @param warnedList
     *            the list to get the letters from
     * @param solicitorRecipientMap
     *            the map to populate with represented letters keyed by
     *            solicitor id
     * @param defendantRecipientMap
     *            the map to populate with unrepresented defendant letters keyed
     *            by solicitor id
     * @param prosecutionRecipientMap
     *            the map to populate with unrepresented prosecution letters
     *            keyed by solicitor id
     * @param unrepresentedList
     *            the list to populate with unrepresented letters
     */
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, WarnedList warnedList) {
        WarnedCourtLists courtLists = warnedList.getWarnedCourtLists();
        if (courtLists != null) {
            for (int i = 0, c = courtLists.getWarnedCourtListCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList, courtLists
                        .getWarnedCourtList(i));
            }
        }
    }

    // Create or update letters for all the withFixedDate and
    // withoutFixedDate
    // in the courtList
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, WarnedList warnedList, WarnedCourtList courtList) {
        // process withFixedDate (Fixed)
        for (int i = 0, c = courtList.getWithFixedDateCount(); i < c; i++) {
            populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList, courtList,
                    courtList.getWithFixedDate(i));
        }

        // process withoutFixedDate (Warned)
        for (int i = 0, c = courtList.getWithoutFixedDateCount(); i < c; i++) {
            populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList, courtList,
                    courtList.getWithoutFixedDate(i));
        }
    }

    // Create or update letters for all the fixtuers in the withFixedDate
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, WarnedList warnedList, WarnedCourtList courtList, WithFixedDate withFixedDate) {
        for (int i = 0, c = withFixedDate.getFixtureCount(); i < c; i++) {
            populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList, courtList,
                    withFixedDate, null, withFixedDate.getFixture(i));
        }
    }

    // Create or update letters for all the fixtuers in the withoutFixedDate
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, WarnedList warnedList, WarnedCourtList courtList,
            WithoutFixedDate withoutFixedDate) {
        for (int i = 0, c = withoutFixedDate.getFixtureCount(); i < c; i++) {
            populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList, courtList,
                    null, withoutFixedDate, withoutFixedDate.getFixture(i));
        }
    }

    // Create or update letters for all the cases in the fixture
    // In ALL populate methods
    // If withFixedDate is null then withoutFixedDate, else withFixedDate.
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, WarnedList warnedList, WarnedCourtList courtList, WithFixedDate withFixedDate,
            WithoutFixedDate withoutFixedDate, Fixture fixture) {
        Cases cases = fixture.getCases();
        if (cases != null) {
            for (int i = 0, c = cases.getCaseCount(); i < c; i++) {
                populate(solicitorRecipientMap, defendantRecipientMap, prosecutionRecipientMap, warnedList, courtList,
                        withFixedDate, withoutFixedDate, fixture, cases.getCase(i));
            }
        }
    }

    // Create or update letters for all the defendants on the caze
    private static final void populate(Map solicitorRecipientMap, Map defendantRecipientMap,
            Map prosecutionRecipientMap, WarnedList warnedList, WarnedCourtList courtList, WithFixedDate withFixedDate,
            WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze) {
        Defendants defendants = caze.getDefendants();
        if (defendants != null) {
            for (int i = 0, c = defendants.getDefendantCount(); i < c; i++) {
                populateDefendant(solicitorRecipientMap, defendantRecipientMap, warnedList, courtList, withFixedDate,
                        withoutFixedDate, fixture, caze, defendants.getDefendant(i));
            }
        }

        Prosecution prosecution = caze.getProsecution();
        if (prosecution != null) {
            populateProsecution(solicitorRecipientMap, prosecutionRecipientMap, warnedList, courtList, withFixedDate,
                    withoutFixedDate, fixture, caze, prosecution);
        }
    }

    // Create or update letters for all the prosecution representation or
    // If the defendant is unrepresented contact diectly.
    private static final void populateDefendant(Map solicitorRecipientMap, Map defendantRecipientMap,
            WarnedList warnedList, WarnedCourtList courtList, WithFixedDate withFixedDate,
            WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze, Defendant defendant) {
        boolean represented = false;
        // Create letters for all the defendants representation
        for (int i = 0, c = defendant.getCounselCount(); i < c; i++) {
            Counsel counsel = defendant.getCounsel(i);
            if (counsel != null) {
                for (int j = 0, d = counsel.getSolicitorCount(); j < d; j++) {
                    populateSolicitorRecipient(solicitorRecipientMap, warnedList, courtList, withFixedDate,
                            withoutFixedDate, fixture, caze, defendant, counsel.getSolicitor(j));
                    represented = true;
                }
            }
        }

        // The defendant is not represented so should be sent a letter directly
        if (!represented) {
            populateDefendantRecipient(defendantRecipientMap, warnedList, courtList, withFixedDate, withoutFixedDate,
                    fixture, caze, defendant);
        }
    }

    // Create or update letters for all the prosecution representation or
    // If the prosecution is unrepresented contact diectly.
    private static final void populateProsecution(Map solicitorRecipientMap, Map prosecutionRecipientMap,
            WarnedList warnedList, WarnedCourtList courtList, WithFixedDate withFixedDate,
            WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze, Prosecution prosecution) {
        // Create letters for all the prosecutions representation
        int c = prosecution.getSolicitorCount();
        if (0 < c) {
            populateSolicitorRecipient(solicitorRecipientMap, warnedList, courtList, withFixedDate, withoutFixedDate,
                    fixture, caze, null, prosecution.getSolicitor(0));
            for (int i = 1; i < c; i++) {
                populateSolicitorRecipient(solicitorRecipientMap, warnedList, courtList, withFixedDate,
                        withoutFixedDate, fixture, caze, null, prosecution.getSolicitor(i));
            }
        }
        // The prosecution is not represented so if not CPS should be sent a
        // letter directly
        else if (!ProsecutionHelper.isCps(prosecution)) {
            populateProsecutionRecipient(prosecutionRecipientMap, warnedList, courtList, withFixedDate,
                    withoutFixedDate, fixture, caze, prosecution);
        }
    }

    // Look for a WarnedListLetter for solicitor (keyed by solicitor id)
    // in the solicitorRecipientMap.
    // If not found create new WarnedCourtListSummary and add to
    // solicitorRecipientMap.
    // In ALL populate methods
    // If withFixedDate is null then withoutFixedDate, else withFixedDate.
    // If defendant is null then prosecution, else defendant.
    private static final void populateSolicitorRecipient(Map solicitorRecipientMap, WarnedList warnedList,
            WarnedCourtList courtList, WithFixedDate withFixedDate, WithoutFixedDate withoutFixedDate, Fixture fixture,
            Case caze, Defendant defendant, Solicitor solicitor) {
        String id = solicitor.getId();

        WarnedListLetter warnedListLetter = (WarnedListLetter) solicitorRecipientMap.get(id);
        if (warnedListLetter != null) {
            populate(courtList, withFixedDate, withoutFixedDate, fixture, caze, defendant, solicitor, warnedListLetter);
        } else {
            solicitorRecipientMap.put(id, WarnedListLetterHelper.create(warnedList, courtList, withFixedDate,
                    withoutFixedDate, fixture, caze, defendant, null, solicitor));
        }
    }

    // Look for a WarnedListLetter for defendant (keyed by defendant id)
    // in the defendantRecipientMap.
    // If not found create new WarnedCourtListSummary and add to
    // defendantRecipientMap.
    // In ALL populate methods
    // If withFixedDate is null then withoutFixedDate, else withFixedDate.
    // If defendant is null then defendant, else defendant.
    private static final void populateDefendantRecipient(Map defendantRecipientMap, WarnedList warnedList,
            WarnedCourtList courtList, WithFixedDate withFixedDate, WithoutFixedDate withoutFixedDate, Fixture fixture,
            Case caze, Defendant defendant) {
        String id = defendant.getId();

        WarnedListLetter warnedListLetter = (WarnedListLetter) defendantRecipientMap.get(id);
        if (warnedListLetter != null) {
            populate(courtList, withFixedDate, withoutFixedDate, fixture, caze, null, null, warnedListLetter);
        } else {
            defendantRecipientMap.put(id, WarnedListLetterHelper.create(warnedList, courtList, withFixedDate,
                    withoutFixedDate, fixture, caze, defendant, null, null));
        }
    }

    // Look for a WarnedListLetter for prosecution (keyed by prosecution id)
    // in the prosecutionRecipientMap.
    // If not found create new WarnedCourtListSummary and add to
    // prosecutionRecipientMap.
    // In ALL populate methods
    // If withFixedDate is null then withoutFixedDate, else withFixedDate.
    // If defendant is null then prosecution, else defendant.
    private static final void populateProsecutionRecipient(Map prosecutionRecipientMap, WarnedList warnedList,
            WarnedCourtList courtList, WithFixedDate withFixedDate, WithoutFixedDate withoutFixedDate, Fixture fixture,
            Case caze, Prosecution prosecution) {
        String id = prosecution.getId();

        WarnedListLetter warnedListLetter = (WarnedListLetter) prosecutionRecipientMap.get(id);
        if (warnedListLetter != null) {
            populate(courtList, withFixedDate, withoutFixedDate, fixture, caze, null, null, warnedListLetter);
        } else {
            prosecutionRecipientMap.put(id, WarnedListLetterHelper.create(warnedList, courtList, withFixedDate,
                    withoutFixedDate, fixture, caze, null, prosecution, null));
        }
    }

    // Look for a WarnedCourtListSummary for courtList in the
    // warnedListLetter.
    // If not found create new WarnedCourtListSummary and add to
    // warnedListLetter.
    private static final void populate(WarnedCourtList courtList, WithFixedDate withFixedDate,
            WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze, Defendant defendant, Solicitor solicitor,
            WarnedListLetter warnedListLetter) {
        WarnedCourtListSummary courtListSummary = WarnedListLetterHelper.getWarnedCourtListSummary(warnedListLetter,
                courtList);
        if (courtListSummary != null) {
            populate(withFixedDate, withoutFixedDate, fixture, caze, defendant, solicitor, courtListSummary);
        } else {
            courtListSummary = WarnedCourtListHelper.createSummary(courtList, withFixedDate, withoutFixedDate, fixture,
                    caze, defendant, solicitor);
            warnedListLetter.getWarnedCourtListSummaries().addWarnedCourtListSummary(courtListSummary);
        }
    }

    // If withFixedDate is null withoutFixedDate, else assume withFixedDate
    private static final void populate(WithFixedDate withFixedDate, WithoutFixedDate withoutFixedDate, Fixture fixture,
            Case caze, Defendant defendant, Solicitor solicitor, WarnedCourtListSummary courtListSummary) {
        if (withFixedDate != null) {
            populate(withFixedDate, fixture, caze, defendant, solicitor, courtListSummary);
        } else {
            populate(withoutFixedDate, fixture, caze, defendant, solicitor, courtListSummary);
        }
    }

    // Look for a WithFixedDate for withFixedDate in the courtListSummary.
    // If not found create new WithFixedDate and add to courtListSummary.
    private static final void populate(WithFixedDate withFixedDate, Fixture fixture, Case caze, Defendant defendant,
            Solicitor solicitor, WarnedCourtListSummary courtListSummary) {
        WithFixedDateSummary withFixedDateSummary = WarnedCourtListHelper.getWithFixedDateSummary(courtListSummary,
                withFixedDate);
        if (withFixedDateSummary != null) {
            populate(fixture, caze, defendant, solicitor, withFixedDateSummary);
        } else {
            withFixedDateSummary = WithFixedDateHelper
                    .createSummary(withFixedDate, fixture, caze, defendant, solicitor);
            courtListSummary.addWithFixedDateSummary(withFixedDateSummary);
        }
    }

    // Look for a WithoutFixedDateSummary for withoutFixedDate in the
    // courtListSummary.
    // If not found create new WithoutFixedDateSummary and add to
    // courtListSummary.
    private static final void populate(WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze,
            Defendant defendant, Solicitor solicitor, WarnedCourtListSummary courtListSummary) {
        WithoutFixedDateSummary withoutFixedDateSummary = WarnedCourtListHelper.getWithoutFixedDateSummary(
                courtListSummary, withoutFixedDate);
        if (withoutFixedDateSummary != null) {
            populate(fixture, caze, defendant, solicitor, withoutFixedDateSummary);
        } else {
            withoutFixedDateSummary = WithoutFixedDateHelper.createSummary(withoutFixedDate, fixture, caze, defendant,
                    solicitor);
            courtListSummary.addWithoutFixedDateSummary(withoutFixedDateSummary);
        }

    }

    // Look for a FixtureSummary for fixture in the withFixedDateSummary.
    // If not found create new FixtureSummary and add to
    // withFixedDateSummary.
    private static final void populate(Fixture fixture, Case caze, Defendant defendant, Solicitor solicitor,
            WithFixedDateSummary withFixedDateSummary) {
        FixtureSummary fixtureSummary = WithFixedDateHelper.getFixtureSummary(withFixedDateSummary, fixture);
        if (fixtureSummary != null) {
            populate(caze, defendant, solicitor, fixtureSummary);
        } else {
            fixtureSummary = FixtureHelper.createSummary(fixture, caze, defendant, solicitor);
            withFixedDateSummary.addFixtureSummary(fixtureSummary);
        }
    }

    // Look for a FixtureSummary for fixture in the withoutFixedDateSummary.
    // If not found create new FixtureSummary and add to
    // withoutFixedDateSummary.
    private static final void populate(Fixture fixture, Case caze, Defendant defendant, Solicitor solicitor,
            WithoutFixedDateSummary withoutFixedDateSummary) {
        FixtureSummary fixtureSummary = WithoutFixedDateHelper.getFixtureSummary(withoutFixedDateSummary, fixture);
        if (fixtureSummary != null) {
            populate(caze, defendant, solicitor, fixtureSummary);
        } else {
            fixtureSummary = FixtureHelper.createSummary(fixture, caze, defendant, solicitor);
            withoutFixedDateSummary.addFixtureSummary(fixtureSummary);
        }
    }

    // Look for a CaseSummary for caze in the fixtureSummary.
    // If not found create new CaseSummary and add to fixtureSummary.
    private static final void populate(Case caze, Defendant defendant, Solicitor solicitor,
            FixtureSummary fixtureSummary) {
        // if this fixture summary does not contain this case summary create it
        CaseSummary caseSummary;
        if (defendant != null) {
            caseSummary = FixtureHelper.getCaseSummary(fixtureSummary, caze, CaseSummaryType.DEFENDANT);
        } else {
            caseSummary = FixtureHelper.getCaseSummary(fixtureSummary, caze, CaseSummaryType.PROSECUTION);
        }

        if (caseSummary != null) {
            if (log.isDebugEnabled()) {
                log.debug("Found " + CaseHelper.toDebug(caseSummary) + ".");
            }
        } else {
            caseSummary = CaseHelper.createSummary(caze, defendant, solicitor);
            fixtureSummary.getCaseSummaries().addCaseSummary(caseSummary);
        }
    }

}
