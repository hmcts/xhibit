package uk.gov.courtservice.xhibit.courtlog.darts;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.services.darts.DartsConfiguration;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;


/**
 * <p>
 * Title: DartsSubscriber
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * The class performs Darts related business logic.  Is called by the the CourtLogWork 
 * class when events appear on the court log that Darts has defined an interest in.
 * This class performs actions when court log entries are created and updated.
 * </p>
 * <p>
 * Company: logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.1 20081010 
 *  
 */
public class DartsSubscriber extends Subscriber {

    /* Logger */
    private static Logger LOG = Logger.getLogger(DartsSubscriber.class);

    /* DARTS Helper */
    private DartsClHelper hlpr = new DartsClHelper();
      
    
    /**
     * Do post create processing for DARTS
     */
    public void postCreate(OperationContext ctx) {
    	log.debug("postCreate()");
        
    	boolean enabled = isDartsEnabled();
    	
        // Only process Event if Darts is Active.  Default mode is on if Config failed to instantiate.
        if ( enabled ) {
            CourtLogSubscriptionValue subVals[] = ctx.getNewSubscriptionValues();
            // Free text needed from the original CRUDval so pulled from the Operational context
            String freeText = ctx.getCrudValue().getEntryFreeText();
            Integer eventID = ctx.getCrudValue().getId();
            
            for (int i = 0; i < subVals.length; i++) {
                try {
                    hlpr.transformClEvent(subVals[i], freeText, eventID);
                } catch (Throwable e) {
                    // We don't want this to abort the court log
                    LOG.fatal("DARTS post create event error :" + e.getMessage(), e);
                }
            }
        }// end IF        
    }// end postCreate
    
    
    /**
     * Do post update processing for DARTS
     */
    public void postUpdate(OperationContext ctx){
    	log.debug("postUpdate()");
    	boolean enabled = isDartsEnabled();
    	
        // Only process Event if Darts is Active.  Default mode is on if Config failed to instantiate.
        if ( enabled ) {
            CourtLogSubscriptionValue subVals[] = ctx.getNewSubscriptionValues();
            // Free text needed from the original CRUDval so pulled from the Operational context
            String freeText = ctx.getCrudValue().getEntryFreeText();
            Integer eventID = ctx.getCrudValue().getId();
            for (int i = 0; i < subVals.length; i++) {
                try {
                    hlpr.transformClEvent(subVals[i], freeText, eventID);
                } catch (Throwable e) {
                    // We don't want this to abort the court log
                    LOG.fatal("DARTS post update event error :" + e.getMessage(), e);
                }
            }
        }// end IF        
    }// end postUpdate

    /**
     * Do post delete processing for DARTS
     */
    public void postDelete(OperationContext ctx) throws CourtLogBusinessException {
    	log.debug("postDelete()");
    	boolean enabled = isDartsEnabled();
    	
        // Only process Event if Darts is Active.  Default mode is on if Config failed to instantiate.
        if ( enabled ) {
        	Integer caseId = ctx.getCrudValue().getCaseId();
        	Integer xhibitEventType = ctx.getCrudValue().getEventType();
            try {
            	hlpr.deleteClEvent(xhibitEventType, caseId);
            } catch (Throwable e) {
                // We don't want this to abort the court log
                LOG.fatal("DARTS post delete event error :" + e.getMessage(), e);
            }
        }// end IF
    }
    
    private boolean isDartsEnabled() {
    	boolean enabled = true;
        // Instantiate the config object 
        DartsConfiguration _dartsConfig = null;
        try {
            _dartsConfig = DartsConfiguration.getInstance();
            enabled = _dartsConfig.isDartsActive(); 
        } catch (Throwable e) {
            enabled = true;
            LOG.warn("DartsConfiguration failed instantiation : " + e.getMessage());
        }
        return enabled;
    }
}// end class
