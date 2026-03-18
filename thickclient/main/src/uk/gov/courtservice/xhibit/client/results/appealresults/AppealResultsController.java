package uk.gov.courtservice.xhibit.client.results.appealresults;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: AppealResultsController
 * </p>
 * <p>
 * Description: Main controller panel for appeal results
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version $Revision: 1.61 $
 */
public class AppealResultsController extends XPanel implements SaveFunction {
    public static final int JUDGES_COMMENTS_MAX_SIZE = 78;

    private final AppealResultsModel appealResultsModel;

    private final ApplicationCaseModel acm;

    private ResultsSaveValue resultsSaveValue;

    private XPanel appealPanel;

    /**
     * Constructor
     * 
     * @param acm
     *            the application case model
     * @throws CSRecoverableException
     *             thrown whenever
     */
    public AppealResultsController(ApplicationCaseModel acm) throws CSRecoverableException {
        this.acm = acm;
        appealResultsModel = new AppealResultsModel(acm);
        stepInitialise();
        jbInit();
    }

    /**
     * gui inits
     * 
     * @throws CSRecoverableException
     *             thrown whenever
     */
    private void jbInit() throws CSRecoverableException {
        setLayout(new BorderLayout());

        ScheduledHearingValue scheduledHearingValue = acm.getScheduledHearingValue();

        if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(scheduledHearingValue)) {
            appealPanel = new MiscellaneousAppealPanel(acm, appealResultsModel);
        } else if (CaseTypeHelper.isCriminalAppeal_CaseType(scheduledHearingValue)) {
            appealPanel = new CriminalAppealPanel(acm.getXhibitApplicationController(), appealResultsModel);
        } else {
            throw new CSRecoverableException("appealresults.launcherror", "occurred at AppealResultsController::jbInit");
        }

        add(appealPanel, BorderLayout.CENTER);
        // Listen for changes to the Modified variable
        appealPanel.addPropertyChangeListener(XPanel.property_modified, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                setModified(((Boolean) e.getNewValue()).booleanValue());
            }
        });
    }

    /**
     * the initialise framework method
     * 
     * @throws CSRecoverableException
     *             thrown for whatever reason
     */
    public void stepInitialise() throws CSRecoverableException {

    }

    /**
     * the deactivate framework method
     * 
     * @throws CSRecoverableException
     *             thrown for whatever reason
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * the validation framework method
     * 
     * @throws CSRecoverableException
     *             thrown for whatever reason
     * @throws CSValidationException
     *             thrown for whatever reason
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
    }

    /**
     * the update view state framework method
     * 
     * @throws CSRecoverableException
     *             thrown for whatever reason
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        appealPanel.stepUpdateViewState();
    }

    /**
     * the deinitialise framework method
     * 
     * @throws CSRecoverableException
     *             thrown for whatever reason
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update && getModified()) {
            int rc = JOptionPane.showConfirmDialog(acm.getXhibitApplicationController(), ResourceBundleHelper
                    .getResource(XhibitBundles.AppealResults, "SaveMessage"), ResourceBundleHelper.getResource(
                    XhibitBundles.AppealResults, "SaveTitle"), JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (rc == JOptionPane.YES_OPTION) {
                save();
            } else if (rc == JOptionPane.NO_OPTION) {
                setModified(false);
            } else {
                throw new UserCancelException();
            }
        }
    }

    /**
     * the activate framework method
     * 
     * @throws CSRecoverableException
     *             thrown for whatever reason
     */
    public void stepActivate() throws CSRecoverableException {
        appealPanel.stepActivate();
        stepUpdateViewState();
    }

    /**
     * SaveFunctions implementation. When the user clicks the save button, the
     * action will call these methods.
     */
    public void save() throws CSRecoverableException {
        try {
            savePreSynchAction();
            saveSynchAction();
            savePostSynchAction();
        } catch (CSRecoverableException e) {
            throw e;
        } catch (CSUnrecoverableException e) {
            throw e;
        } catch (Exception ex) {
            throw new CSUnrecoverableException(ex);
        }
    }

    public void savePreSynchAction() throws Exception {
        setModified(false);
        try {
            resultsSaveValue = new ResultsSaveValue(XhibitSingleton.getInstance().getCourtId());
            ((AppealSaver) appealPanel).populateResultsSaveValue(resultsSaveValue);
        } catch (Exception e) {
            setModified(true);
            throw e;
        }
    }

    public void saveSynchAction() throws Exception {
    	String username = XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME);
        try {
            if (resultsSaveValue.getResultSaveValueCount() > 0) {
                XhibitDelegateHelper.getResults2Delegate().setVerdicts(resultsSaveValue, username);
                appealPanel.stepDeactivate();
                appealResultsModel.reloadResults();
            }
        } catch (Exception e) {
            // Need to ensure can save again
            setModified(true);
            throw e;
        }
    }

    public void savePostSynchAction() throws Exception {
        // Activate regardless of whether resultsSaveValue contains items.
        // This is to ensure modifeid is false and to refresh the document
        // listner
        // on the judges comments panel.
        appealPanel.stepActivate();
    }

}
