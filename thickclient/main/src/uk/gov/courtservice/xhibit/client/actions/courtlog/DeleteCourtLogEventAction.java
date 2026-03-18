package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Revision: 1.16 $
 */
public class DeleteCourtLogEventAction extends XAction {

	private static final long serialVersionUID = 1L;
    private static final Logger log = CSServices.getLogger(DeleteCourtLogEventAction.class);
    private static final String SentenceForBroadcastEventType = "20937";

    public DeleteCourtLogEventAction() {
        populateFromBundle("clDeleteCourtLog");
    }

    public void xActionPerformed(ActionEvent e) throws CourtLogBusinessException, CSRecoverableException {
        String deleteConfirmationMessage = ResourceBundleHelper.getResource(XhibitBundles.CourtLogResources,
                "DeleteSure");

        JFrame parent = getController() == null ? null : (JFrame) this.getController();


        XhibitApplicationController xac = (XhibitApplicationController) getController();
        if (xac.getBodyPanel() instanceof CourtLogController) {
            CourtLogController courtLogController = (CourtLogController) xac.getBodyPanel();
            CourtLogViewValue clv = (CourtLogViewValue) getModel();
            
            // ctx-3838, disable ability to edit/delete a bench warrant court log event
            if (clv.isBWHistoryIssueWarrantEvent() || clv.isBWHistoryEndWarrantEvent()) {
            	JOptionPane.showMessageDialog((JFrame) this.getController(), "Can't delete Bench Warrant court log events.",
            			"Delete Court Log Event", JOptionPane.INFORMATION_MESSAGE);
            	return;
            }
            
            boolean selectedOption = XMessageBox.alert(parent, ResourceBundleHelper.getResource(
                    XhibitBundles.CourtLogResources, "ConfirmDeleteTitle"), true, XMessageBox.ICONWARNING,
                    deleteConfirmationMessage, XDialog.YESNO, XDialog.DEFAULTNO);
            
            if (selectedOption) {
            	deleteLogEntry(clv.getLogEntryId());
            	
            	updateSpecificEventData(xac, clv);            	
            	
                // Reload the court log event table.
                courtLogController.refreshEvents();
            }
            JPopupMenu popup = courtLogController.getEventsTablePopup();
            if (popup.isVisible()) {
                popup.setVisible(false);
            }
        }
    }

	private void updateSpecificEventData(XhibitApplicationController xac, CourtLogViewValue clv) {
		log.debug("Going to reset the case.setTelevisedRemarksFilmed() with eventID: " + xac.getApplicationCaseModel().getCaseId());
		String eventType = clv.getEventType().toString();
		if(null != eventType && eventType.equals(SentenceForBroadcastEventType)){
			try {
				CaseBasicValue caseBV = XhibitDelegateHelper.getCaseDelegate().getCase(xac.getApplicationCaseModel().getCaseId());			
		        if(null != caseBV && null != caseBV.getTelevisedRemarksFilmed() ){
		        	caseBV.setTelevisedRemarksFilmed(null);
		        	XhibitDelegateHelper.getCaseDelegate().updateCase(caseBV, "XHIBIT");
		        }
			} catch (CaseControllerException ex) {
				log.error("updateSpecificEventData - ERROR retrieving/updating case - " + xac.getApplicationCaseModel().getCaseId() + " " + clv.getEventType());
			}
		}
	}

    private void deleteLogEntry(Long logEntryId) throws CourtLogBusinessException {
        log.debug("Going to Delete the selected row! with eventID: " + logEntryId);

        final boolean inCourt = XhibitSingleton.getInstance().isUserInCourtroom();
        XhibitDelegateHelper.getCourtLogDelegate2().deleteEntry(logEntryId, inCourt);

        log.debug("Deleted the selected row! with eventID: " + logEntryId);
    }
}
