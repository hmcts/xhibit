package uk.gov.courtservice.xhibit.business.services.publicnotice;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.services.pdda.PddaHelper;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: Thin delgation layer to the PublicNoticeWorkFlow
 * </p>
 * <p>
 * Description: see title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @ejb.bean name="PublicNoticeController" description="Public Notice Session
 *           Bean" type="Stateless" view-type="remote"
 *           jndi-name="PublicNoticeControllerHome"
 * @ejb.transaction type="Required"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author Pat Fox, RLakhani
 * @created 20 February 2003
 */
public class PublicNoticeControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	private PddaHelper pddaHelper;
	
    /**
     * Gets the allPublicNoticesForCourtRoom attribute of the
     * PublicNoticeControllerBean object
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtRoomID
     *            Description of the Parameter
     * @return The allPublicNoticesForCourtRoom value
     * @exception PublicNoticeCourtRoomUnknownException
     *                Description of the Exception
     */
    public DisplayablePublicNoticeValue[] getAllPublicNoticesForCourtRoom(int courtRoomID)
            throws PublicNoticeCourtRoomUnknownException {

        log.info("Entering getAllPublicNoticesForCourtRoom()");

        try {
            return PublicNoticeWorkFlow.getAllPublicNoticesForCourtRoom(courtRoomID);
        } catch (PublicNoticeCourtRoomUnknownException ex) {

            log.info("Marking the Transaction for RollBack");
            this.ctx.setRollbackOnly();
            throw ex;
        }
    }

    /**
     * Sets the allPublicNoticesForCourtRoom attribute of the
     * PublicNoticeControllerBean object
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param publicNotices
     *            The new allPublicNoticesForCourtRoom value
     * @param courtRoomID
     *            The new allPublicNoticesForCourtRoom value
     * @exception PublicNoticeCourtRoomUnknownException
     *                Description of the Exception
     * @throws PublicNoticeInvalidSelectionException
     * @exception PublicNoticeInvalidSelectionException
     *                Description of the Exception
     */
    public void setAllPublicNoticesForCourtRoom(DisplayablePublicNoticeValue[] publicNotices, int courtRoomID,
    		String userDisplayName)
            throws PublicNoticeCourtRoomUnknownException, PublicNoticeInvalidSelectionException {
        log.info("Entered setAllPublicNoticesForCourtRoom");
        try {
            PublicNoticeWorkFlow.setAllPublicNoticesForCourtRoom(publicNotices, courtRoomID, userDisplayName);
        } catch (PublicNoticeCourtRoomUnknownException ex) {
            log.info("Marking the Transaction for RollBack");
            this.ctx.setRollbackOnly();
            // rethrow the exception
            throw ex;
        } catch (PublicNoticeInvalidSelectionException ex) {
            log.info("Marking the Transaction for RollBack");
            this.ctx.setRollbackOnly();
            // rethrow the exception
            throw ex;
        }
        log.info("Exit setAllPublicNoticesForCourtRoom");
    }

    /**
     * Sets the publicNoticeforCourtRoom attribute of the
     * PublicNoticeControllerBean object
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param courtLogSubscriptionValue
     *            The new publicNoticeforCourtRoom value
     * @exception PublicNoticeInvalidSelectionException
     *                Description of the Exception
     * @exception PublicNoticeException
     *                Description of the Exception
     */
    public void setPublicNoticeforCourtRoom(CourtLogSubscriptionValue courtLogSubscriptionValue,
    		String userDisplayName)
            throws PublicNoticeException, PublicNoticeInvalidSelectionException {
        log.info("Entered setPublicNoticeforCourtRoom");

        try {
            PublicNoticeWorkFlow.setPublicNoticeforCourtRoom(courtLogSubscriptionValue, userDisplayName);
        } catch (PublicNoticeInvalidSelectionException ex) {
            log.info("Marking the Transaction for RollBack");
            this.ctx.setRollbackOnly();
            // rethrow the exception
            throw ex;
        }

        log.info("Exit setPublicNoticeforCourtRoom");
    }

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process.
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
	 * 
     */
    public void doTask(String taskName) {  
    	// Only fire if the xhbConfigProp is set to send to PDDA
    	if (getPddaHelper().isSendToPDDA()) {
	    	// Scheduler has no userDisplayName passed
	    	String userDisplayName = null;
	    	getPddaHelper().sendBatchesToPDDA(userDisplayName);
	    	getPddaHelper().resendBatchesToPDDA(userDisplayName);
	    	getPddaHelper().sendIwpDataToPDDA(userDisplayName);
    	}
    }
    
    private PddaHelper getPddaHelper() {
    	if (pddaHelper == null) {
    		pddaHelper = new PddaHelper();
    	}
    	return pddaHelper;
    }
}