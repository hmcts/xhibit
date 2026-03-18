package uk.gov.courtservice.xhibit.courtlog.publicdisplay;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_scheduled_hearing.XhbScheduledHearingBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.crlivestatus.CrLiveStatusHelper;

/**
 * Subscriber to update CR live status for the public display status and for
 * sending JMS messages to public display
 * 
 * @author pznwc5
 * @version $Revision: 1.8 $
 */
public class PublicDisplaySubscriber extends Subscriber {
    /** Logger */
    private static final Logger log = Logger.getLogger(PublicDisplaySubscriber.class);

    /**
     * Logic after a public display event is created
     */
    public void postCreate(OperationContext ctx) {
    	if (log.isDebugEnabled()) {
    		log.debug("postCreate() entered");
    	}
    	
        if (CrLiveStatusHelper.updatePublicDisplayStatus(ctx.getNewViewValues()[0])) {
            CourtLogSubscriptionValue subVal = ctx.getNewSubscriptionValues()[0];
            if (subVal != null) {
            	log.debug("subVal not null.");
            	log.debug("subVal courtSiteId: " + subVal.getCourtSiteId());
            	log.debug("subVal hearingId: " + subVal.getHearingId());
            	log.debug("subVal pnEventType: " + subVal.getPnEventType());
            	log.debug("subVal courtRoomId: " + subVal.getCourtRoomId());
            } else {
            	log.debug("subVal null.");
            }
            PublicDisplayHelper.sendMessage(subVal);
            log.debug("Message sent in post create");
        }
        
    	if (log.isDebugEnabled()) {
    		log.debug("postCreate() exited");
    	}
    }

    /**
     * Logic after a public display event is deleted
     */
    public void postDelete(OperationContext ctx) {
    	if (log.isDebugEnabled()) {
    		log.debug("postDelete() entered");
    	}
    	
        if (CrLiveStatusHelper.deletePublicDisplayStatus(ctx.getOriginalViewValue())) {
            // ctx.getLastSubscriptionValue()
            XhbScheduledHearing xsh = XhbScheduledHearingBeanHelper2.findByPrimaryKey(ctx.getCrudValue()
                    .getScheduledHearingId());
            
            if (xsh != null) {
            	log.debug("xsh not null.");
            	if (xsh.getXhbSitting() != null) {
            		log.debug("xsh.getXhbSitting() not null."); 
            		log.debug("xsh.getXhbSitting().getCourtRoomId():" + xsh.getXhbSitting().getCourtRoomId());
            	} else {
            		log.debug("xsh.getXhbSitting() null.");
            	}
            } else {
            	log.debug("xsh null.");
            }

            PublicDisplayHelper.sendMessage(xsh.getXhbSitting().getCourtRoomId());
            log.debug("Message sent in post delete");
        }
        
    	if (log.isDebugEnabled()) {
    		log.debug("postDelete() entered");
    	}
    }

    /**
     * Logic after a public display event is updated
     */
    public void postUpdate(OperationContext ctx) {
    	if (log.isDebugEnabled()) {
    		log.debug("postUpdate() entered");
    	}
    	
        if (CrLiveStatusHelper.updatePublicDisplayStatus(ctx.getNewViewValues()[0])) {
            CourtLogSubscriptionValue subVal = ctx.getNewSubscriptionValues()[0];
            if (subVal != null) {
            	log.debug("subVal not null.");
            	log.debug("subVal courtSiteId: " + subVal.getCourtSiteId());
            	log.debug("subVal hearingId: " + subVal.getHearingId());
            	log.debug("subVal pnEventType: " + subVal.getPnEventType());
            	log.debug("subVal courtRoomId: " + subVal.getCourtRoomId());
            } else {
            	log.debug("subVal null.");
            }
            PublicDisplayHelper.sendMessage(subVal);
            log.debug("Message sent in post update");
        }
        
    	if (log.isDebugEnabled()) {
    		log.debug("postUpdate() entered");
    	}
    }
}