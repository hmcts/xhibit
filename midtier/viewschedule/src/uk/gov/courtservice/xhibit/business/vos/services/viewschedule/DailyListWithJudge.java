package uk.gov.courtservice.xhibit.business.vos.services.viewschedule;

/**
 * Type definition for a custom daily list entry that also contains details
 * about the judge on the case.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 * @see DailyList
 */
public interface DailyListWithJudge extends DailyList {
    /**
     * The name of the judge.
     * 
     * @return The judge name.
     */
    public String getJudgeName();
}
