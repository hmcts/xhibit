package uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * 
 * @version $Revision: 1.7 $
 */
public interface WitnessSession extends WitnessDetail {

    public int getDayNumber();

    public String getSessionType();

    /**
     * @pre getMetaState() != REMOVED
     * @return
     */
    public TrialSession getTrialSession();

    /**
     * @pre getMetaState() != REMOVED
     * @param day
     */
    public void setTrialSession(TrialSession day);

    /**
     * Converts a WitnessSession to a WitnessDetail.
     * 
     * @return
     */
    public WitnessDetail getAssociatedWitnessDetail();

}
