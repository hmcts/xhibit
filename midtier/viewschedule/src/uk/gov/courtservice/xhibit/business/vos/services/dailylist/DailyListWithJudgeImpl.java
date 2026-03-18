package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.viewschedule.DailyListWithJudge;

/**
 * Implementation class of the <code>DailyListWithJudge</code> interface.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 * 
 * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
 *      .DailyListWithJudge
 * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
 */
public class DailyListWithJudgeImpl extends DailyListImpl implements DailyListWithJudge {
    private static final String JUDGE_NAME = "JUDGE_NAME";

    private String judgeName;

    private static final long serialVersionUID =-1554234938220757001L;
    
    /**
     * Default constructor.
     */
    public DailyListWithJudgeImpl() {
        super();
    }

    /**
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface
     *      #populate(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void populate(Row rset) {
        super.populate(rset);
        this.judgeName = rset.getString(JUDGE_NAME);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.vos.services.viewschedule
     *      .DailyListWithJudge#getJudgeName()
     */
    public String getJudgeName() {
        return this.judgeName;
    }
}
