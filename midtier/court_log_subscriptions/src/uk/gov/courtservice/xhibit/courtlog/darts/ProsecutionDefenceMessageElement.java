package uk.gov.courtservice.xhibit.courtlog.darts;
import java.util.Collection;

import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_legal_representative.XhbRefLegalRepresentative;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_legal_representative.XhbRefLegalRepresentativeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_legal_representative.XhbRefLegalRepresentativeBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanNotFoundException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_sh_leg_rep.XhbShLegRep;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;

/**
 * <p>
 *
 * Title: ProsecutionDefenceMessageElement
 * </p>
 * <p>
 * Description: Returns the names of the registered Defence or Prosecution advocates during the current
 * hearing. Prosecution events are treated as CASE level while the Defence is at DEFENDANT level. It
 * should be noted that individuals are not indentifiable so a list of all advocates
 * registered against a CASE or DEFENDANT for the current hearing will be returned.
 * 
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version $Id:
 * 
 */
public class ProsecutionDefenceMessageElement {
 
    private static final Logger log = CSServices.getLogger(ProsecutionDefenceMessageElement.class);
    private static final String BARRISTER = "A";
    private static final String SOLICTOR = "S";

    public ProsecutionDefenceMessageElement() {
      // constructor
    }

    
    /**
     * Returns the Prosecution names for the hearing that the courtlog event was created in.
     * 
     * @param value
     *            Contains the getScheduledHearingId to retrieve the prosecution data for.
     *            
     * @return The a comma separated String of Prosecution advocates names in the form of 
     *          Title FirstName MiddleName Surname.
     */
     public String getProsData(CourtLogSubscriptionValue value) {
        
        StringBuffer sb = new StringBuffer();
        try{
            /* Get List of Legal representatives associated with hearing */
            XhbScheduledHearing  schedHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(value.getScheduledHearingId());
            Collection<XhbShLegRep> legReps = schedHearing.getXhbShLegReps();
            
            boolean firstLoop = true;  // used to ommit the comma from first element
            for (XhbShLegRep shedLegRepVal : legReps ) 
            {
                if (shedLegRepVal.getLegalRole().equals(PersonValue.PROSECUTION))
                {
                    XhbRefLegalRepresentative refLegRep  = 
                        XhbRefLegalRepresentativeBeanHelper2.findByPrimaryKey(shedLegRepVal.getRefLegalRepId());
                    populateLegRep(firstLoop, sb,refLegRep );
                    firstLoop = false;
                }                    
            } // end of for loop
            return sb.toString();
            
        } catch (XhbScheduledHearingBeanNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass());
                log.error("Could not find scheduled hearing for ID : " + value.getScheduledHearingId());
        } catch ( XhbRefLegalRepresentativeBeanNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass());
                log.debug("Could not find legal representative information for scheduled hearing ID : " + value.getScheduledHearingId());
        }    
        return sb.toString();
    }
    
    
    /**
     * Returns the Defence names for the Defendant in the hearing that the courtlog event was created for.
     * 
     * @param value
     *            The court log value that contains the getScheduledHearingId and Defendant information 
     *            to retrieve the Defence data for.
     *            
     * @return The a comma separated String of Defence advocates names in the form of 
     *          Title FirstName MiddleName Surname.
     */
    public String getDefenData(CourtLogSubscriptionValue value) {
       
        StringBuffer sb = new StringBuffer();
        try{
            
            /* Get List of Legal representatives associated with hearing */
            XhbScheduledHearing  schedHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(value.getScheduledHearingId());
            Collection<XhbShLegRep> legReps = schedHearing.getXhbShLegReps();
            
            boolean firstLoop = true;  // used to omit the comma from first element
            for (XhbShLegRep shedLegRepVal : legReps ) 
            {
                if (shedLegRepVal.getLegalRole().equals(PersonValue.DEFENCE))
                {   
                    /* Check defendant on Case on court log against the scheduled legal
                     * representative as multiple defendants may exist on same case */
                    Integer courtLogDefOnCaseId = getDefendantOnCaseId(value);
                    if ( shedLegRepVal.getXhbSchedHearingDefendant().getDefendantOnCaseId().equals(courtLogDefOnCaseId ))
                    {
                        XhbRefLegalRepresentative refLegRep = XhbRefLegalRepresentativeBeanHelper2
                                .findByPrimaryKey(shedLegRepVal.getRefLegalRepId());
                        populateLegRep(firstLoop, sb, refLegRep);
                        firstLoop = false;
                    }                    
                }
            } // end of for loop
            return sb.toString();
            
        } catch (XhbScheduledHearingBeanNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass());
                log.error("Could not find scheduled hearing for ID : " + value.getScheduledHearingId());
        } catch ( XhbRefLegalRepresentativeBeanNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass());
                log.debug("Could not find legal representative information for scheduled hearing ID : " + value.getScheduledHearingId());
        }    
        return sb.toString();
    }
    
    
    /**
     * Returns the Defence names for the hearing that the courtlog event was created in, to be used when
     *  a defence event is not specific to a single Defendant.
     * 
     * @param value
     *            Contains the getScheduledHearingId to retrieve the prosecution data for.
     *            
     * @return The a comma separated String of Prosecution advocates names in the form of 
     *          Title FirstName MiddleName Surname.
     */
    public String getAllDefenData(CourtLogSubscriptionValue value) {
        
        StringBuffer sb = new StringBuffer();
        try{
            /* Get List of Legal representatives associated with hearing */
            XhbScheduledHearing  schedHearing = XhbScheduledHearingBeanHelper2.findByPrimaryKey(value.getScheduledHearingId());
            Collection<XhbShLegRep> legReps = schedHearing.getXhbShLegReps();
            
            boolean firstLoop = true;  // used to ommit the comma from first element
            for (XhbShLegRep shedLegRepVal : legReps ) 
            {
                if (shedLegRepVal.getLegalRole().equals(PersonValue.DEFENCE))
                {
                    XhbRefLegalRepresentative refLegRep  = 
                        XhbRefLegalRepresentativeBeanHelper2.findByPrimaryKey(shedLegRepVal.getRefLegalRepId());
                    populateLegRep(firstLoop, sb,refLegRep );
                    firstLoop = false;
                }                    
            } // end of for loop
            return sb.toString();
            
        } catch (XhbScheduledHearingBeanNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass());
                log.error("Could not find scheduled hearing for ID : " + value.getScheduledHearingId());
        } catch ( XhbRefLegalRepresentativeBeanNotFoundException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass());
                log.debug("Could not find legal representative information for scheduled hearing ID : " + value.getScheduledHearingId());
        }    
        return sb.toString();
    }
    
    
    // ---------------------------- Private Methods
    // -----------------------------//
    
    private Integer getDefendantOnCaseId(CourtLogSubscriptionValue value) {
        
        Integer defOnCaseId = null;
        if(value.getDefendantOnCaseId() != null){
            return  value.getDefendantOnCaseId();
        } else {
            defOnCaseId = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(value.getDefendantOnOffenceId())
                .getXhbDefendantOnCase().getDefendantOnCaseId();
            return defOnCaseId;
        }
    }

    private void populateLegRep (boolean firstLoopFlag, StringBuffer strBuff, XhbRefLegalRepresentative refLegRep){
        if(!firstLoopFlag){ strBuff.append(", "); } 
        strBuff.append((refLegRep.getTitle() != null )     ? (refLegRep.getTitle() + " ") : "" );
        strBuff.append((refLegRep.getFirstName()!= null )  ? (refLegRep.getFirstName() + " "): "" );
        strBuff.append((refLegRep.getMiddleName()!= null ) ? (refLegRep.getMiddleName()+ " "): "" );
        strBuff.append((refLegRep.getSurname()!= null )    ? (refLegRep.getSurname()): "" );
        if (refLegRep.getLegalRepType().equals(BARRISTER) ){
            strBuff.append(" (Barrister)" );
        } else if ( refLegRep.getLegalRepType().equals(SOLICTOR)) {
            strBuff.append(" (Solicitor)" );
        }
                
    }
    
} // end of class
