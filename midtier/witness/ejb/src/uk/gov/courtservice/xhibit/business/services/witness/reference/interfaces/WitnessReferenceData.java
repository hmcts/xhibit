package uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces;

/**
 * <p>
 * Title: Witness Reference Data
 * </p>
 * <p>
 * Description: Provides access to the various pieces of reference data used
 * within witness.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * <p>
 * <b>To obtain an instance</b><br/>
 * 
 * <pre>
 *   WitnessReferenceDataFactory.getWitnessReferenceData()
 *   &lt;pre&gt;
 * </p>
 *  
 *   @author Neil Ellis
 *   @version $Revision: 1.6 $
 *  
 *   @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary
 *   @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession
 * 
 */
public interface WitnessReferenceData {

    /**
     * Return the valid witness types. (Defence and Prosecution).
     * 
     * @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary#DEFENCE_TYPE
     * @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSummary#PROSECUTION_TYPE
     */
    public String[] getWitnessTypes();

    /**
     * Return the valid witness statuses. eg. (Profesional, Expert etc).
     */
    public String[] getWitnessStatuses();

    /**
     * Return the valid pager networks.
     */
    public String[] getPagerNetworks();

    /**
     * Return the valid trial session types.
     * 
     * @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession#AFTERNOON
     * @see uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession#MORNING
     * @return a string array of valid session types. These are simple keys of
     *         one letter.
     */
    public String[] getTrialSessionTypes();

}
