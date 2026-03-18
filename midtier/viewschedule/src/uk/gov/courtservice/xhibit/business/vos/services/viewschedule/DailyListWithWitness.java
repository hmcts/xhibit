package uk.gov.courtservice.xhibit.business.vos.services.viewschedule;

/**
 * Type definition for a custom daily list entry that also contains details
 * about the witnesses on the case, whether it is issued and whether a skeleton
 * schedule exists or not.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 * @see DailyList
 * @see DailyListWithJudge
 */
public interface DailyListWithWitness extends DailyListWithJudge {
    /**
     * Does the scheduled hearing that this daily list object represent have any
     * witnesses on the case.
     * 
     * @return <i>true</i> if there are any witnesses, <i>false</i> otherwise.
     */
    public boolean isWitnessOnCase();

    /**
     * Is the skeleton schedule associated with this scheduled hearing marked as
     * issued.
     * 
     * @return <i>true</i> if issued, <i>false</i> otherwise.
     */
    public boolean isIssued();

    /**
     * Is there a skeleton schedule associated with this scheduled hearing.
     * 
     * @return <i>true</i> if a skeleton schedule exists, <i>false</i>
     *         otherwise.
     */
    public boolean isScheduled();
}
