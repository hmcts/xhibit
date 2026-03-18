package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithWitness;

/**
 * Implementation class of the <code>DailyListWithWitness</code> interface.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 * 
 * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
 *      .DailyListWithWitness
 * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
 */
public class DailyListWithWitnessImpl extends DailyListWithJudgeImpl implements DailyListWithWitness {
    private static final String SKELETON_SCHEDULE = "SKELETON_SCHEDULE";

    private static final String ISSUED = "ISSUED";

    private static final String WITNESSES = "WITNESSES";

    private boolean witnessOnCase; // does the case have witnesses???

    private boolean scheduled; // does it have a skeleton schedule???

    private boolean issued; // is the schedule issued???

    private static final long serialVersionUID =449670885952750834L;

    /**
     * Default constructor.
     */
    public DailyListWithWitnessImpl() {
        super();
    }

    /**
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
     *      #populate(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void populate(Row rset) {
        super.populate(rset);

        this.witnessOnCase = YES.equals(rset.getString(WITNESSES));
        this.issued = YES.equals(rset.getString(ISSUED));
        this.scheduled = YES.equals(rset.getString(SKELETON_SCHEDULE));
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyListWithWitness#isWitnessOnCase()
     */
    public boolean isWitnessOnCase() {
        return this.witnessOnCase;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyListWithWitness#isIssued()
     */
    public boolean isIssued() {
        return this.issued;
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyListWithWitness#isScheduled()
     */
    public boolean isScheduled() {
        return this.scheduled;
    }
}
