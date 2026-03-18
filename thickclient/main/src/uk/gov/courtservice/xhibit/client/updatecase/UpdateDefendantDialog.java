package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenAmendDefendantAction;
import uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantModel;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: Xhibit 2 Client Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 * @version 1.1 - created for bug_release_X010102-030113 - handling of
 *          null/empty values - more debug statements to validate GUI
 *          functioning using logs
 * 
 * <Change History/> *
 * <P>
 * MH - 20030428 - amended to get set and save defendant main values.
 * </P>
 * <P>
 * MH - 20030503 - amended to get set and save defendant driving values.
 * </P>
 * <P>
 * MH - 20030603 - added check so that we don't store an invalid driving license
 * number.
 * </P>
 * 
 * $Log: UpdateDefendantDialog.java,v $
 * Revision 1.44  2015/06/11 17:53:09  atwells
 * Extra check to stop it bombing out if its a B case
 *
 * Revision 1.43  2010/03/16 16:37:26  lois
 * CCN1332 - Updated code to comply with coding standards
 *
 * Revision 1.42  2010/02/09 14:34:18  hewittm
 * CCN1332 Add method stateChanged to determine if the data editable via the dialog is editable
 *
 * Revision 1.41  2007/09/12 13:58:31  gzw0qg
 * RFC: 1745
 * the ScheduledHearingValue is required in the UpdateDefendantPanel so
 * a getShv() method was added.
 *
 * Revision 1.40  2006/06/05 12:31:42  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.39 2006/05/31 14:26:00 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.38 2003/09/05 14:00:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused) unused imports cause misleading dependencies
 * remove unused variables/methods change non-static access to statics
 * 
 * Revision 1.37 2003/08/05 15:28:23 szn20z Changes to support principle that
 * when the case properties screen is opened in view mode, all fields plus the
 * Apply/OK command buttons are disabled.
 * 
 * This is to fix defect #X53963.
 * 
 * Revision 1.36 2003/07/18 16:19:31 szn20z Case Properties refactoring: -
 * extracted UpdateDefendantXPanel inner class
 * 
 * Revision 1.35 2003/07/04 09:35:32 szn20z Check for nulled-out dateOfBirth and
 * lastConvDate values
 * 
 * Revision 1.34 2003/06/23 13:18:34 zzc872 Removed old comments that were no
 * longer valid.
 * 
 * Revision 1.33 2003/06/20 15:08:54 zzc872 Bugfix 53589 : Added check so that
 * last conviction date cannot be in the future.
 * 
 * Revision 1.32 2003/06/17 10:17:07 zzc872 Bugfix 53535: display "Amend
 * Appellant" when appeal cases.
 * 
 * Revision 1.31 2003/06/16 15:31:45 zzc872 Added validation defendant values
 * 
 * Revision 1.30 2003/06/09 13:09:45 szn20z Remove validation that defendant
 * must be between 12 and 69
 * 
 * Revision 1.29 2003/06/03 16:59:47 szn20z Validate that the defendant DOB is
 * in the past and their age is between 12 and 69 years
 * 
 * Revision 1.28 2003/06/03 12:21:53 zzc872 BUG-FIX : 53389 - added check so
 * that an invalid driving license is not being saved to the database.
 * 
 * Revision 1.27 2003/05/28 18:18:36 zzc872 BUG-FIX 53186
 * 
 * Revision 1.26 2003/05/24 13:11:20 zzc872 BUG-FIX: 53266 (consequence of this
 * bug)
 * 
 * Revision 1.25 2003/05/24 12:33:02 zzc872 BUG-FIX: 53266 (consequence of this
 * bug)
 * 
 * Revision 1.24 2003/05/21 17:06:07 nz5zpz fixing 52949
 * 
 * Revision 1.23 2003/05/21 16:48:32 nz5zpz fixing 52949
 * 
 * Revision 1.22 2003/05/18 16:03:12 nz5zpz 53186 Defenant initials are now
 * displayed iso defendant middle name
 * 
 */

public class UpdateDefendantDialog extends XDialog {
    
    private static final long serialVersionUID = 1L;

    private ScheduledHearingValue shv = null;

    private Logger log = CSServices.getLogger(UpdateDefendantDialog.class);

    private XhibitApplicationController xac;
    
    UpdateDefendantPanel updateDefendantPanel;
    
    
    public UpdateDefendantDialog(OpenAmendDefendantAction action) {
        super((JFrame) action.getController(), "Update Defendant", true, OKCANCEL, DEFAULTOK);
        // the 'Update Defendant' title will be overwritten, once the resource
        // bundle has been loaded for this component.
        try {
            log.debug("Created Logger instance, continuing instantiation.");

            this.xac = (XhibitApplicationController) action.getController();
            log.debug("action.getController() got XhibitApplicationController");

            // Get the scheduled hearing value that is required to determine
            // the
            // case type later.
            if (xac.getBodyPanel() instanceof TodaysScheduleController) {
                TodaysScheduleController tsc = (TodaysScheduleController) xac.getBodyPanel();
                if (tsc.getShv() != null) {
                    shv = tsc.getShv();
                }
            } else if (xac.getApplicationCaseModel() != null
                    && xac.getApplicationCaseModel().getScheduledHearingValue() != null) {
                shv = xac.getApplicationCaseModel().getScheduledHearingValue();
            }

            String name = XHIBITConstant.getResource(XhibitBundles.UpdateDefendant, "name");
            if (name == null) {
                throw (new CSConfigurationException(
                        "Exception during instantiation of UpdateCase! 'name' property not set.", new Exception()));
            } else {
                super.setTitle(name);
            }

            int width = 350;
            try {
                width = Integer.parseInt(XHIBITConstant.getProperty(XhibitProperties.UpdateDefendant, "width"));
            } catch (Exception e) {
                throw (new CSConfigurationException(
                        "Exception during instantiation of UpdateCase! 'width' property not parseable to int.", e));
            }

            int height = 300;
            try {
                width = Integer.parseInt(XHIBITConstant.getProperty(XhibitProperties.UpdateDefendant, "height"));
            } catch (Exception e) {
                throw (new CSConfigurationException(
                        "Exception during instantiation of UpdateCase! 'height' property not parseable to int.", e));
            }

            this.setSize(width, height);
            this.setResizable(false);
            this.setModal(true);

            if (action.getModel() != null) {
                ((UpdateDefendantModel) action.getModel()).setAction(action);
                updateDefendantPanel = new UpdateDefendantPanel(this, (UpdateDefendantModel) action.getModel());
                this.addBodyPanel(updateDefendantPanel);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = this.getSize();
                if (frameSize.height > screenSize.height)
                    frameSize.height = screenSize.height;
                if (frameSize.width > screenSize.width)
                    frameSize.width = screenSize.width;
    
                this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
                this.pack();
                this.setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Message userMessage = new Message("gui.updateDefendantDialog.ConstructorFailed");
            String logMessage = "UpdateCaseDialog Constructor Failed.";
            CSRecoverableException CSre = new CSRecoverableException(userMessage.getMessage(), logMessage, ex);
            XHIBITConstant.handleError(CSre);
        }
    }

    /**
     * stepActivate()
     */
    public void stepActivate() {
        // RL & MH - changed to display appellant instead of defendant for
        // appeals.
        // in the title
        try {
            if (shv != null
                    && (XHIBITConstant.isCriminalAppeal_CaseType(shv) || XHIBITConstant
                            .isMiscelleanousAppeal_CaseType(shv))) {
                this.setTitle(XHIBITConstant.getResource(XhibitBundles.UpdateDefendant, "statusBarTextAppeal"));
            } else {
                this.setTitle(XHIBITConstant.getResource(XhibitBundles.UpdateDefendant, "statusBarText"));
            }
        } catch (UnknownCaseTypeException ex) {
            this.setTitle(XHIBITConstant.getResource(XhibitBundles.UpdateDefendant, "statusBarText"));
        }
    }
    
    public ScheduledHearingValue getShv() { 
        return shv;
    }
    
    public boolean stateChanged() {
        return updateDefendantPanel.isStateChanged();
    }
}
