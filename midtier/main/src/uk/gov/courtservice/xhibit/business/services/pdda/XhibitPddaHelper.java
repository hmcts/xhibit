package uk.gov.courtservice.xhibit.business.services.pdda;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayNotifier;

/**
 * <p>
 * Title: Xhibit PDDAHelper
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public abstract class XhibitPddaHelper {
	private static final Logger LOG = CSServices.getLogger(XhibitPddaHelper.class);
	
	private LocalPublicDisplayNotifier publicDisplayNotifier;
	private String methodName;

	public XhibitPddaHelper() {
	}

	/**
     * Send messages to PDDA (new way)
     */
	protected boolean isSendToPDDA(String pddaSwitcher) {
		return "1".equals(pddaSwitcher) || "2".equals(pddaSwitcher);
	}
	
	/**
     * Send messages to Xhibit (old way)
     */
	protected boolean isSendToXhibit(String pddaSwitcher) {
		return "2".equals(pddaSwitcher) || "3".equals(pddaSwitcher);
	}
	
	/**
     * Sends a public display event
     * 
     * @param event
     *            Public display event
     */
    public void sendMessage(PublicDisplayEvent event, String userDisplayName) {
    	getPublicDisplayNotifier().sendMessage(event);
    	PddaHelper pddaHelper = new PddaHelper();
        pddaHelper.sendMessage(event, "Pdda", true); // Skip sending to XHIBIT as its handled above
    }
    
    /**
     * Close the notifier connection
     */
    public void close() {
    	getPublicDisplayNotifier().close();
    }
    
    protected ObjectMessage getMessage() {
    	if (isPublicNotifierInUse()) {
    		return getPublicDisplayNotifier().getObjectMessage();    		
    	}
    	return null;
    }
    
    private boolean isPublicNotifierInUse() {
    	return publicDisplayNotifier != null;
    }
    
	private LocalPublicDisplayNotifier getPublicDisplayNotifier() {
		methodName = "getPublicDisplayNotifier()";
		if (!isPublicNotifierInUse()) {
			LOG.debug(methodName + " - Create PublicDisplayNotifier");
			publicDisplayNotifier = new LocalPublicDisplayNotifier();
		}
		return publicDisplayNotifier;
	}
	
	public class LocalPublicDisplayNotifier extends PublicDisplayNotifier {
		
		 public ObjectMessage localMessage;
		
		 protected void setMessageHeader(ObjectMessage msg) throws JMSException {
			 super.setMessageHeader(msg);
			 localMessage = msg;
		 }
		 
		 public ObjectMessage getObjectMessage() {
			 return localMessage;
		 }
	}
}