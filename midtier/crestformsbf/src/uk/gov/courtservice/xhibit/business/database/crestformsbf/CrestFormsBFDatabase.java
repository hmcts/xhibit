package uk.gov.courtservice.xhibit.business.database.crestformsbf;

import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.CaseQuery;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.ConsolidatedCaseQuery;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.CourtClerkNameQuery;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.CrestFormsBtoFOffenceQueries;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.DefendantOnCaseIdQuery;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.DefendantsOnCaseQuery;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.query.LinkedCasesQuery;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;

/**
 * <p>
 * Title: Crest Forms B-F fastdb
 * </p>
 * <p>
 * Description: The facade interface for fast lane readers.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003)
 * 
 */
public class CrestFormsBFDatabase {

    /**
     * Charge Types
     */
    private static final String BREACH_TYPE = "B";

    private static final String CRIM_APPEAL_TYPE = "C";

    private static final String INDICTMENT_TYPE = "I";

    private static final String S41_TYPE = "O";

    private static final String C4S_TYPE = "S";

    /**
     * Stop unnecisary production of this interface class
     */
    private CrestFormsBFDatabase() {
    }

    /**
     * Get a case given an id
     * 
     * @param caseId
     *            the case to search against
     * @return the case object or null if it cant be found
     */
    public static CrestFormsBFCase getCase(Integer caseId) {
        if (caseId == null) {
            throw new IllegalArgumentException("caseId");
        }
        return new CaseQuery().getCase(caseId);
    }

    /**
     * Get the id's of all cases which have been linked to this one
     * 
     * @param caze
     * @return an array of <code>Intger</code> objects containg the ids of the
     *         linked cases
     * @throws IllegalArgumentException
     *             if caseId is null
     */
    public static CrestFormsBFCase[] getLinkedCases(CrestFormsBFCase caze) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        return new LinkedCasesQuery().getLinkedCases(caze.getId());
    }

    /**
     * Get the id's of all the cases which have Consolidated cases
     * 
     * @param caze
     * @return an array of <code>Integer</code> objects containing the ids of
     *         the consolidated cases
     * @throws IllegalArgumentException
     *             if caseid is null
     */

    public static CrestFormsBFCase[] getConsolidatedCases(CrestFormsBFCase caze) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        return new ConsolidatedCaseQuery().getConsolidatedQuery(caze.getId());
    }

    /**
     * Get defendants on a case
     * 
     * @param caze
     *            the case to search against
     * @return an <code>int</code> containing the number Criminal Appeal
     *         Offences a defendant has on a given case
     */
    public static CrestFormsBFDefendant[] getDefendantsOnCase(CrestFormsBFCase caze) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        return new DefendantsOnCaseQuery().getDefendantsOnCase(caze.getId());
    }

    /**
     * Determine if a defendant has any Unrelated Disposals
     * 
     * @param defendant
     * @return true if the defendant has Unrelated Disposals, false otherwise
     * @throws IllegalArgumentException
     *             if defendant is null
     */
    public static boolean hasUnrelatedDisposals(CrestFormsBFDefendant defendant, CrestFormsBFCase caze) {
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        Integer docId = getDefendantOnCaseId(caze, defendant);
        return getNumberOfUnrelatedDisposals(docId) > 0;
    }

    /**
     * Get the number of Unrelated Disposals for a given defendant
     * 
     * @param docID
     * @return an <code>int</code> containing the number of Unrelated
     *         Disposals for a defendant
     */
    public static int getNumberOfUnrelatedDisposals(Integer docID) {
        if (docID == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new CrestFormsBtoFOffenceQueries().getNumberOfUnrelatedDisposals(docID);
    }

    /**
     * Determine if a defendant has any Indictment Counts on a given case
     * 
     * @param caze
     * @param defendant
     * @return true if the defendant has Indictment Counts on a given case,
     *         false otherwise
     * @throws IllegalArgumentException
     *             if case or defendant is null
     */
    public static boolean hasIndictmentCounts(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return getNumberOfIndictmentCounts(caze, defendant) > 0;
    }

    /**
     * Get the number of Indictment Counts a defendant has for a given case
     * 
     * @param caze
     * @param defendant
     * @return an <code>int</code> containing the number Indictment Counts a
     *         defendant has on a given case
     */
    public static int getNumberOfIndictmentCounts(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new CrestFormsBtoFOffenceQueries().getNumberOfOffences(caze.getId(), defendant.getId(), INDICTMENT_TYPE);

    }

    /**
     * Determine if a defendant has any Section 41 Offences on a given case
     * 
     * @param caze
     * @param defendant
     * @return true if the defendant has Section 41 Offences on a given case,
     *         false otherwise
     * @throws IllegalArgumentException
     *             if case or defendant is null
     */
    public static boolean hasSection41Offences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return getNumberOfSection41Offences(caze, defendant) > 0;
    }

    /**
     * Get the number of Section 41 Offences a defendant has for a given case
     * 
     * @param caze
     * @param defendant
     * @return an <code>int</code> containing the number Section 41 Offences a
     *         defendant has on a given case
     */
    public static int getNumberOfSection41Offences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new CrestFormsBtoFOffenceQueries().getNumberOfOffences(caze.getId(), defendant.getId(), S41_TYPE);
    }

    /**
     * Determine if a defendant has any Committal for Sentence Offences on a
     * given case
     * 
     * @param caze
     * @param defendant
     * @return true if the defendant has Committal for Sentence Offences on a
     *         given case, false otherwise
     * @throws IllegalArgumentException
     *             if case or defendant is null
     */
    public static boolean hasCommittalForSentenceOffences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return getNumberOfCommittalForSentenceOffences(caze, defendant) > 0;
    }

    /**
     * Get the number of Committal for Sentence Offences a defendant has for a
     * given case
     * 
     * @param caze
     * @param defendant
     * @return an <code>int</code> containing the number Committal for
     *         Sentence Offences a defendant has on a given case
     */
    public static int getNumberOfCommittalForSentenceOffences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new CrestFormsBtoFOffenceQueries().getNumberOfOffences(caze.getId(), defendant.getId(), C4S_TYPE);
    }

    /**
     * Determine if a defendant has any Breach Offences on a given case
     * 
     * @param caze
     * @param defendant
     * @return true if the defendant has Breach Offences on a given case, false
     *         otherwise
     * @throws IllegalArgumentException
     *             if case or defendant is null
     */
    public static boolean hasBreachOffences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return getNumberOfBreachOffences(caze, defendant) > 0;
    }

    /**
     * Get the number of Breach Offences a defendant has for a given case
     * 
     * @param caze
     * @param defendant
     * @return an <code>int</code> containing the number Breach Offences a
     *         defendant has on a given case
     */
    public static int getNumberOfBreachOffences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new CrestFormsBtoFOffenceQueries().getNumberOfOffences(caze.getId(), defendant.getId(), BREACH_TYPE);
    }

    /**
     * Determine if a defendant has any Criminal Appeal Offences on a given case
     * 
     * @param caze
     * @param defendant
     * @return true if the defendant has Criminal Appeal Offences on a given
     *         case, false otherwise
     * @throws IllegalArgumentException
     *             if case or defendant is null
     */
    public static boolean hasCriminalAppealOffences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return getNumberOfCriminalAppealOffences(caze, defendant) > 0;
    }

    /**
     * Get the number of Criminal Appeal Offences a defendant has for a given
     * case
     * 
     * @param caze
     * @param defendant
     * @return an <code>int</code> containing the number Criminal Appeal
     *         Offences a defendant has on a given case
     */
    public static int getNumberOfCriminalAppealOffences(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new CrestFormsBtoFOffenceQueries()
                .getNumberOfOffences(caze.getId(), defendant.getId(), CRIM_APPEAL_TYPE);
    }

    /**
     * Get the defendant on case id for the specified case and defendant
     * 
     * @param caze
     * @param defendant
     * @return an <code>Integer</code> containing the defendant on case id or
     *         null if it cant be found
     * @throws IllegalArgumentException
     *             if cazeId or defendantId is null
     */
    public static Integer getDefendantOnCaseId(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        if (caze == null) {
            throw new IllegalArgumentException("caze");
        }
        if (defendant == null) {
            throw new IllegalArgumentException("defendant");
        }
        return new DefendantOnCaseIdQuery().getDefendantOnCaseId(caze.getId(), defendant.getId());
    }

    /**
     * Returns the name of the LATEST Court Clark for a given scheduled hearing.
     * 
     * <p>
     * This method is used as a workaround as you cant get full name information
     * about the logged on user, this will not always return what the user is
     * expecting and a better solution needs to be found. William Fardell
     * 2003/07/16
     * </p>
     * 
     * @return the Court Clark name or null if it cant be found
     * @throws IllegalArgumentException
     *             if scheduledHearingId is null
     */
    public static String getCourtClerkName(Integer scheduledHearingId) {
        if (scheduledHearingId == null) {
            throw new IllegalArgumentException("scheduledHearingId");
        }
        return new CourtClerkNameQuery().getCourtClerkName(scheduledHearingId);
    }

}
