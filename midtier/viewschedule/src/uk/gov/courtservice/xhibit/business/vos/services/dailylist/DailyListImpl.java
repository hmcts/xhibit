package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList;

/**
 * Implementation class of the <code>DailyList</code> interface.
 * 
 * @author tz0d5m
 * @version $Revision: 1.4 $
 * 
 * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyList
 * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
 */
public class DailyListImpl implements DailyList, RowPopulatorInterface {
    /**
     * Constant used to show that a database field has been set to "Y" (true)
     */
    protected static final String YES = "Y";

    /** Constant used to show that a case is floating "1" (true) */
    protected static final String FLOATING_CASE = "1";

    private static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    private static final String DEF_NAME = "DEF_NAME";

    private static final String HEARING_TYPE = "HEARING_TYPE";

    private static final String CASE_STRING = "CASE_STRING";

    private static final String COURT_ROOM_NAME = "COURT_ROOM_NAME";

    private static final String NOT_BEFORE_TIME = "NOT_BEFORE_TIME";

    private static final String CASE_ID = "CASE_ID";

    private static final String FLOATING = "FLOATING";

    private final Collection defendantNames = new ArrayList();

    private String hearingType;

    private String caseString;

    private String courtRoomName;

    private Date notBeforeTime;

    private int caseId;

    private int scheduledHearingId;

    private boolean floating;

    private static final long serialVersionUID =77630640060472787L;
    
    /**
     * Default constructor.
     */
    public DailyListImpl() {
        super();
    }

    /**
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
     *      #populate(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void populate(Row rset) {
        this.hearingType = rset.getString(HEARING_TYPE);
        this.caseString = rset.getString(CASE_STRING);
        this.courtRoomName = rset.getString(COURT_ROOM_NAME);
        this.notBeforeTime = rset.getTimestamp(NOT_BEFORE_TIME);
        this.caseId = rset.getInt(CASE_ID);
        this.scheduledHearingId = rset.getInt(SCHEDULED_HEARING_ID);
        this.floating = FLOATING_CASE.equals(rset.getString(FLOATING));
    }

    /**
     * A DailyList entry is deemed as a duplicate if the previous value has the
     * same scheduled hearing id.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
     *      #isDuplicateRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public boolean isDuplicateRow(Row rset) {
        final int shId = rset.getInt(SCHEDULED_HEARING_ID);
        return (shId == getScheduledHearingId());
    }

    /**
     * The name of the defendant should be added for a new row, or for duplicate
     * rows.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
     *      #addRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void addRow(Row rset) {
        // if there is a name, add it to the array of names...
        final String defName = rset.getString(DEF_NAME);
        if (defName != null) {
            this.defendantNames.add(defName);
        }
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getDefendantNames()
     */
    public String[] getDefendantNames() {
        return (String[]) this.defendantNames.toArray(new String[this.defendantNames.size()]);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#isFloating()
     */
    public boolean isFloating() {
        return this.floating;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getHearingType()
     */
    public String getHearingType() {
        return this.hearingType;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getCase()
     */
    public String getCase() {
        return this.caseString;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getNotBeforeTime()
     */
    public Date getNotBeforeTime() {
        return this.notBeforeTime;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getCaseId()
     */
    public int getCaseId() {
        return this.caseId;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getScheduledHearingId()
     */
    public int getScheduledHearingId() {
        return this.scheduledHearingId;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyList#getCourtRoomName()
     */
    public String getCourtRoomName() {
        return this.courtRoomName;
    }
}
