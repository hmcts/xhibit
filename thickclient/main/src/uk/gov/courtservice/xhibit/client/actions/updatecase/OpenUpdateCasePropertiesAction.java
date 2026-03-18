package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateCaseDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @editors Rakesh Lakhani
 * 
 *          2002.11.23 Do not get the model from the controller like
 *          ApplicationCaseModel acm = xac.getApplicationCaseModel(); Rather
 *          ApplicationCaseModel acm = (ApplicationCaseModel)getModel();
 * @version 1.0
 */

public class OpenUpdateCasePropertiesAction extends XAction {
	private Logger log;

	/* This argument is only set by xxxx */
	private Integer shID = null;

	private HearingRecordModel hrRecordModel;

	private boolean editable;

	/**
	 * This method must be used if an other Scheduled hearing should be openend
	 * than the one currently in the Application Case Model of the Controller.
	 * 
	 * @param id
	 */
	public void setScheduledHearingID2BOpened(Integer id) {
		this.shID = id;
	}

	public Integer getScheduledHearingID2BOpened() {
		return this.shID;
	}

	public void setHearingRecordModel(HearingRecordModel hrRecordModel) {
		this.hrRecordModel = hrRecordModel;
	}

	public HearingRecordModel getHearingRecordModel() {
		return this.hrRecordModel;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean param) {
		editable = param;
	}

	public OpenUpdateCasePropertiesAction() {
		this.log = CSServices.getLogger(OpenUpdateCasePropertiesAction.class);
		populateFromBundle("OpenUpdateCasePropertiesAction");
		setIcon(XHIBITConstant.imageRoot + "properties.gif");
	}

	public void xActionPerformed(ActionEvent e) throws Exception {
		log.debug("OpenUpdateCasePropertiesAction: initialising update case dialog.");

		try {
			XhibitApplicationController xac = (XhibitApplicationController) getController();

			if (xac == null || xac.getApplicationCaseModel() == null) {
				setEditable(false);
			} else {
				setEditable(xac.getApplicationCaseModel().isInEditMode(FunctionList.ECaseProperty));
			}

			log.debug("OpenUpdateCasePropertiesAction: opening update case dialog.");

			UpdateCaseDialog x = new UpdateCaseDialog(this); // clhv,

			// refreshing today's schedule
			log.debug("XAC = " + getController());

			// if the cancel button was clicked, then do not reload
			if (x.isApplyClicked() || !x.isCancelClicked()) {
				// Reload todays schedule
				if (xac.getBodyPanel() instanceof TodaysScheduleController) {
					((TodaysScheduleController) xac.getBodyPanel()).reloadView();
				}

				if (xac.getApplicationCaseModel() != null) {
					xac.getApplicationCaseModel().refresh();
				}
				if (xac.getBodyPanel() instanceof CourtLogController) {
					CourtLogController cc = (CourtLogController) xac.getBodyPanel();
					cc.loadCourtLogHeaderTableModel();
					cc.getCourtLogHeaderTable().repaint();
				}
			}

			log.debug("OpenUpdateCasePropertiesAction: now closed update case dialog.");
		} catch (Exception dce) {
			CSRecoverableException csse = new CSRecoverableException("gui.user.TodaysScheduleControllerText",
					"gui.log.TodaysScheduleControllerText", dce);
			throw (csse);
		}
	}
}