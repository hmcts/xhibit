package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.charge.ResultsFoundException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeType;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.charges.AddWizardBreachOffenceAction;
import uk.gov.courtservice.xhibit.client.actions.charges.ChangeCountAction;
import uk.gov.courtservice.xhibit.client.actions.charges.ChangeOffenceAction;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.results.appealresults.AppealResultsController;
import uk.gov.courtservice.xhibit.client.results.pleas.MultiplePleasPanel;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaControllerModel;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCell;
import uk.gov.courtservice.xhibit.client.results.verdicts.VerdictsController;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: UncodedOffenceController
 * </p>
 * <p>
 * Description: The control component for Change Request 58.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version $Id: UncodedOffenceController.java,v 1.30 2005/07/08 11:30:28 rzvddy
 *          Exp $
 * 
 */
public class UncodedOffenceController {
    /**
     * Logger logger
     */
    private static final Logger LOG = CSServices.getLogger(UncodedOffenceController.class);

    /**
     * Xhibit Application Controller
     */
    XhibitApplicationController xac;

    /**
     * The View Component
     */
    private UncodedOffenceDialog uod;

    /**
     * The Desciption
     */
    private static String uncodedOffenceDesc;

    /**
     * The Model Component
     */
    private UncodedOffenceModel uom;

    /**
     * Resources at config.bundles.XHIBITChargesResources
     */
    private ResourceBundle myResources;

    /**
     * <init>
     */
    public UncodedOffenceController() {
        LOG.debug("UncodedOffenceController - BEGIN");
        uom = new UncodedOffenceModel();

        if (uom.refOffenceId == null) {
            RefOffenceBasicValue value = findReservedCodePK(XhibitSingleton.getInstance().getCourtId());
            uom.refOffenceId = value.getId();
            setUncodedOffenceDescription(value.getOffenceDesc());
        }
        LOG.debug("UncodedOffenceController - END");
    }

    /**
     * <init>
     * 
     * @param xac
     *            parameter for <init>
     */
    public UncodedOffenceController(XhibitApplicationController xac) {
        this();
        this.xac = xac;
        this.myResources = ResourceBundleHelper.getResourceBundle(XhibitBundles.MaintainCharges);
    }

    /**
     * findReservedCodePK
     * 
     * @param courtId
     *            the court id.
     * @return the returned Integer
     */
    private RefOffenceBasicValue findReservedCodePK(Integer courtId) {
        RefOffenceBasicValue rtn = null;

        RefOffenceCriteria criteria = new RefOffenceCriteria();
        criteria.setOffenceCode(uom.UNCODED_OFFENCE_REFERENCE_CODE);
        criteria.setCourtId(courtId.toString());

        Collection collection = null;
        try {
            collection = XhibitDelegateHelper.getBizRefDelegate().findOffences(criteria);
        } catch (CSUnrecoverableException ex) {
            LOG.error(ex.getMessage());

        } catch (BisRefControllerException ex) {
            LOG.error(ex.getMessage());
        }

        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            RefOffenceBasicValue value = (RefOffenceBasicValue) iter.next();
            rtn = value;
        }

        if (rtn == null) {
            LOG.error("The reserved offence code (" + uom.UNCODED_OFFENCE_REFERENCE_CODE
                    + ") for Uncoded Offences hasn't been set.");
        }

        return rtn;
    }

    /**
     * editModel
     * 
     * @param chargesController
     *            parameter for editModel
     * @throws CSRecoverableException -
     */
    public void editModel(ChargesController chargesController) throws CSRecoverableException {
        LOG.debug("editModel - BEGIN");
        ChargesControllerModel model = chargesController.getModel();

        ChargeType chargeType = convertChargeType(model.getSelectedChargeType());

        String caseType = model.getACM().getCaseType();

        validateModel(caseType, chargeType.getChargeType());

        OffenceValue offenceValue = model.getOffenceValue();

        setUncodedDetails(offenceValue);
        offenceValue.setCourtLogDate(Calendar.getInstance());
        offenceValue.setDeleteResults(false);

        closeDialog();

        try {
            XhibitDelegateHelper.getChargeDelegate().editOffence(offenceValue,
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        } catch (ChargeControllerException ex) {
            chargesController.loadCharges();
            throw ex;
        } catch (CSUnrecoverableException ex) {
            chargesController.loadCharges();
            throw ex;
        }

        if (chargeType.equals(ChargeTypes.INDICTMENT)) {
            // Set the offence decription to Uncoded Offence to be recorded
            // in the CREST Indictment Log
            offenceValue.setOffenceDescription(getUncodedOffenceDescription());

            CrestIndictmentLog.getInstance().editCountLog(
                    xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), offenceValue);
        }
        chargesController.loadCharges();
    }

    /**
     * @throws CSRecoverableException
     */
    public void saveModel(ChargesController controller) throws CSRecoverableException {
        LOG.debug("saveModel(ChargesController controller) - BEGIN");
        ChargesControllerModel model = controller.getModel();

        Integer caseID = model.getACM().getCaseId();
        Integer courtID = new Integer(model.getCourtId());

        ChargeType chargeType = convertChargeType(model.getSelectedChargeType());
        String caseType = model.getACM().getCaseType();

        validateModel(caseType, chargeType.getChargeType());

        OffenceValue offenceValue = new OffenceValue();

        offenceValue.setCaseID(caseID);
        offenceValue.setCourtID(courtID);

        setUncodedDetails(offenceValue);

        offenceValue.setCourtLogDate(java.util.Calendar.getInstance());

        closeDialog();

        // If Charge Type is C4S or S41 then we need to prompt the user
        // to add defendants to the offence
        if (!chargeType.equals(ChargeTypes.INDICTMENT)) {
            //TODO Check why the below method in the model has changed.
//            model.setOffenceValue(offenceValue, model);

            if (chargeType.equals(ChargeTypes.SECTION_41)) {
                AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                        AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, HOProcCodeHelper.S41,
                        ChargesControllerHelper.MODE.ADD);
                addOffenceDetailsDialog.setVisible(true);
                if (addOffenceDetailsDialog.isCancelClicked()) {
                    throw new UserCancelException();
                }
            } else if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE)) {
                AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                        AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, HOProcCodeHelper.SENT,
                        ChargesControllerHelper.MODE.ADD);
                addOffenceDetailsDialog.setVisible(true);
                if (addOffenceDetailsDialog.isCancelClicked()) {
                    throw new UserCancelException();
                }
            } else if (chargeType.equals(ChargeTypes.CRIMINAL_APPEAL)) {
            	AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                        AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, null,
                        ChargesControllerHelper.MODE.ADD);
                addOffenceDetailsDialog.setVisible(true);
                if (addOffenceDetailsDialog.isCancelClicked()) {
                    throw new UserCancelException();
                }
            }

            model.setOffenceValue(offenceValue);
            addDefendantsToOffence(offenceValue, model);
        }

        if (chargeType.equals(ChargeTypes.INDICTMENT)) {
            AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                    AddOffenceDetailsDialog.TITLE.COUNT, offenceValue, xac, HOProcCodeHelper.TRIAL, 
                    ChargesControllerHelper.MODE.ADD);
            addOffenceDetailsDialog.setVisible(true);
            if (addOffenceDetailsDialog.isCancelClicked()) {
                    throw new UserCancelException();
            }
        }
        
        // Check if a charge exists
        setOffenceOnCharge(caseID, offenceValue, model);

        if (chargeType.equals(ChargeTypes.INDICTMENT)) {
            // Set the offence decription to Uncoded Offence to be recorded
            // in the CREST Indictment Log
            offenceValue.setOffenceDescription(getUncodedOffenceDescription());

            CrestIndictmentLog.getInstance().addCountLog(
                    xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), offenceValue);
        }

        controller.loadCharges();

        LOG.debug("saveModel(ChargesController controller) - END");
    }

    /**
     * If there is no chareg value on the model we need to create a new one
     * 
     * @param chargesBD
     *            the delegate
     * @param caseID
     *            teh case id
     * @param offenceValue
     *            the offence value
     * @param model
     *            the model
     * @throws ChargeControllerException
     * @throws CSUnrecoverableException
     */
    private void setOffenceOnCharge(Integer caseID, OffenceValue offenceValue, ChargesControllerModel model)
            throws ChargeControllerException, CSUnrecoverableException {
        if (null != model.getChargeValue()) {
            offenceValue.setChargeID(model.getChargeValue().getChargeID());
            XhibitDelegateHelper.getChargeDelegate().addOffence(offenceValue,
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        } else {
            Vector offences = new Vector();
            offences.add(offenceValue);

            ChargeValue chargeValue = new ChargeValue(caseID, convertChargeType(model.getSelectedChargeType()));
            chargeValue.setOffenceValues(offences);
            chargeValue.setCourtLogDate(Calendar.getInstance());
            chargeValue.setCourtID(model.getCourtId());

            XhibitDelegateHelper.getChargeDelegate().addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        }
    }

    /**
     * Display the add defendanbts to offence dialog
     * 
     * @param offenceValue
     *            the offence value
     * @throws CSRecoverableException
     */
    private void addDefendantsToOffence(OffenceValue offenceValue, ChargesControllerModel ccm) throws CSRecoverableException {

        LOG.debug("addDefendantsToOffence - BEGIN");
        Collection notOnOffence = ChargesControllerHelper.getDefendantsNotOnOffence(ccm);
        if (notOnOffence.size() > 0) {
            AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, false,
                    AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, notOnOffence,
                    ChargesControllerHelper.MODE.ADD);

            addDefendantsToOffenceDialog.setVisible(true);
            
            if (addDefendantsToOffenceDialog.getDefendantIDs() == null) {
                throw new UserCancelException();
            }

            // Need to retrieve selected defendants from Dialog
            offenceValue.setDefendantIDs(addDefendantsToOffenceDialog.getDefendantIDs());

            // Set DefendantOnOffenceComplexValues on the OffenceValue.
            if (addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues() == null) {
                throw new UserCancelException();
            }
            offenceValue.setDefOnOffenceBasicValues(addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues());

        } else {
            boolean messageBoxReply = XMessageBox.alert(xac, getResource("addDefendantsToOffence.noDefendants.title"),
                    true, XMessageBox.ICONINFORMATION, getResource("addDefendantsToOffence.noDefendants.msg"),
                    XMessageBox.DEFAULTOK);
        }
        LOG.debug("addDefendantsToOffence - END");
    }

    /**
     * Update the Offence Value fields that are related to Uncoded Offences
     * 
     * @param offenceValue
     *            the offence value
     */
    private void setUncodedDetails(OffenceValue offenceValue) {
        offenceValue.setRefOffenceID(uom.refOffenceId);
        offenceValue.setOffenceDescription(uom.getRsDesc());
        offenceValue.setOffenceCode(uom.UNCODED_OFFENCE_REFERENCE_CODE);
        offenceValue.setCrestOffenceFreeText(uom.getCrestDesc());
        offenceValue.setCrestHOClass(uom.getHoClass());
        offenceValue.setCrestHOSubclass(uom.getHoSubclass());
    }

    /**
     * Indictment and Breach
     * 
     * @throws CSRecoverableException
     */
    public void saveModel(AddedOffencesPanelModel addedOffencesPanelModel) throws CSRecoverableException {
        LOG.debug("saveModel(AddedOffencesPanelModel addedOffencesPanelModel) - BEGIN");
        XPanel addedOffencesPanel = addedOffencesPanelModel.getAddedOffencesPanel();
        ChargeWizardModel wizardModel = addedOffencesPanelModel.getChargeWizardModel();
        
        Integer courtID = XhibitSingleton.getInstance().getCourtId();
        Integer caseID = wizardModel.getCaseID();

        ChargesController cc = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel model = cc.getModel();

        ChargeType chargeType = convertChargeType(model.getSelectedChargeType());
        String caseType = model.getACM().getCaseType();

        validateModel(caseType, chargeType.getChargeType());

        OffenceValue offenceValue = new OffenceValue();
        offenceValue.setCaseID(caseID);
        offenceValue.setCourtID(courtID);

        setUncodedDetails(offenceValue);

        offenceValue.setCourtLogDate(java.util.Calendar.getInstance());

        // The charge id will not exist for Indictments or Breaches that are
        // in the process of being created.
        if (wizardModel.getChargeID() != null) {
            offenceValue.setChargeID(wizardModel.getChargeID());
        }

        if (chargeType.equals(ChargeTypes.INDICTMENT)) {
            AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                    AddOffenceDetailsDialog.TITLE.COUNT, offenceValue, xac, HOProcCodeHelper.TRIAL, 
                    ChargesControllerHelper.MODE.ADD);
            addOffenceDetailsDialog.setVisible(true);
            if (addOffenceDetailsDialog.isCancelClicked()) {
                    throw new UserCancelException();
            }
        } else if (chargeType.equals(ChargeTypes.BREACH)) {
            //Launch Add Additional Breach Offence Defendants details functionality for Breach Wizard
            AddBreachOffenceDefendantDetailsDialog addBreachOffenceDefendantDetailsDialog = 
                new AddBreachOffenceDefendantDetailsDialog(xac, offenceValue, wizardModel, false, 
                        ChargesControllerHelper.MODE.ADD, model);
            
            addBreachOffenceDefendantDetailsDialog.setVisible(true);
            if (addBreachOffenceDefendantDetailsDialog.isCancelClicked()) {
                throw new UserCancelException();
            }            
        }        
        
        addedOffencesPanelModel.addOffence(offenceValue);
        addedOffencesPanel.stepActivate();
        addedOffencesPanel.stepUpdateViewState();

        LOG.debug("saveModel(AddedOffencesPanelModel addedOffencesPanelModel) - END");
    }
       
    /**
     * updateParentPanel - Moves the data entered by the user to the appropriate
     * fields on the MultiplePleasPanel.
     * 
     * @param pleaControllerModel -
     *            contains the reference to the MultiplePleasPanel
     * @throws CSRecoverableException
     */
    public void updateParentPanel(PleaControllerModel pleaControllerModel) throws CSRecoverableException {
        MultiplePleasPanel mpp = pleaControllerModel.getMultiplePleasPanel();

        mpp.getOffenceDescText().setText(uom.getCrestDesc());
        mpp.setOffenceId(uom.refOffenceId);
        mpp.setOffenceCode(uom.UNCODED_OFFENCE_REFERENCE_CODE);
        mpp.stepUpdateViewState();
    }

    /**
     * saveModel
     * 
     * @param info
     *            parameter for saveModel
     * @throws CSRecoverableException
     */
    public void saveModel(AdditionalInfoTableCell info) throws CSRecoverableException {
        LOG.debug("saveModel(AdditionalInfoTableCell info) - BEGIN");
        validateDescriptions();
        RefOffenceBasicValue value = new RefOffenceBasicValue();
        value.setId(uom.refOffenceId);
        value.setOffenceCode(uom.UNCODED_OFFENCE_REFERENCE_CODE);
        value.setOffenceDesc(uom.getCrestDesc());

        info.setRefOffenceBasicValue(value);
        info.setOffenceText(value.getOffenceDesc(), true);
        LOG.debug("saveModel(AdditionalInfoTableCell info) - END");
    }

    /**
     * updateModel
     * 
     * @param controller
     *            parameter for updateModel
     * @throws CSRecoverableException -
     */
    public void updateModel(ChargesController controller) throws CSRecoverableException {
        LOG.debug("updateModel - BEGIN");
        boolean offenceChanged = false;
        boolean resultsFoundMBReply = false;

        ChargesControllerModel model = controller.getModel();
        OffenceValue offenceValue = model.getOffenceValue();

        ChargeType chargeType = convertChargeType(model.getSelectedChargeType());
        String caseType = model.getACM().getCaseType();

        validateModel(caseType, chargeType.getChargeType());

        offenceValue.setActSection(null);
        offenceValue.setStatute(null);

        setUncodedDetails(offenceValue);

        offenceValue.setCourtLogDate(Calendar.getInstance());

        closeDialog();

        getHOProcCode(offenceValue, caseType, chargeType.getChargeType());

        LOG.debug("updateModel - about to call updateOffence");
        try {
            offenceValue.setDeleteResults(false);
            XhibitDelegateHelper.getChargeDelegate().updateOffence(offenceValue,
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

            offenceChanged = true;
        } catch (ResultsFoundException rfe) {
            String changeType = chargeType.equals(ChargeTypes.INDICTMENT) ? "ChangeCount" : "ChangeOffence";
            resultsFoundMBReply = XMessageBox.alert(xac, getResource(changeType + ".Results.Query.Title"), true,
                    XMessageBox.ICONQUESTION, getResource(changeType + ".Results.Query.Message"), XMessageBox.YESNO,
                    XMessageBox.DEFAULTNO);

            if (resultsFoundMBReply) {
                try {
                    offenceValue.setDeleteResults(true);
                    XhibitDelegateHelper.getChargeDelegate().updateOffence(offenceValue,
                    		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                    offenceChanged = true;
                } catch (ResultsFoundException rfe2) {
                    // Should not get this exception when passing
                    // true to the method deleteOffence
                    String errMsg = getResource("ChangeCount.ResultsFoundException2");
                    XHIBITErrorHandler.handleError(rfe2, null, errMsg);
                }
            }
        }
        LOG.debug("updateModel - done call to updateOffence");

        if (offenceChanged) {
            if (chargeType.equals(ChargeTypes.INDICTMENT)) {
                // Set the offence decription to Uncoded Offence to be recorded
                // in the CREST Indictment Log
                offenceValue.setOffenceDescription(getUncodedOffenceDescription());

                CrestIndictmentLog.getInstance().addCountLog(
                        xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), offenceValue);
            }
            controller.loadCharges();
        }
        LOG.debug("updateModel - END");
    }

    /**
     * getModel
     * 
     * @return the returned UncodedOffenceModel
     */
    public UncodedOffenceModel getModel() {
        return this.uom;
    }

    /**
     * openDialog
     * 
     * @param frame
     *            parameter for openDialog
     * @param createOffence
     *            true if adding an uncoded offence (false - editing)
     */
    public void openDialog(Frame frame, boolean createOffence) {
        String title = createOffence ? myResources.getString("AddUncodedOffence.title") : myResources
                .getString("EditUncodedOffence.title");
        if ((frame instanceof XhibitApplicationController) || (xac.getBodyPanel() instanceof ChargesController)) {
            ChargesController chargesController = (ChargesController) xac.getBodyPanel();
            ChargesControllerModel model = chargesController.getModel();
            OffenceValue offenceValue = model.getOffenceValue();

            uom = new UncodedOffenceModel(offenceValue);
            uod = new UncodedChargeDialog(frame, title, true, this, chargesController);
        } else {
            uod = new UncodedOffenceDialog(frame, title, true, this);
        }

        uod.setVisible(true);
    }

    /**
     * openDialog, This method contains the rules to determine which dialog to
     * open in relation with the functional area of the system
     * 
     * @param xSearch
     *            parameter for openDialog
     */
    public void openDialog(XHIBITSearch xSearch) {
        if (xSearch.getXSOpenSearchAction().getCaller() instanceof AddWizardBreachOffenceAction) {
            // Wizard
            AddWizardBreachOffenceAction first = (AddWizardBreachOffenceAction) xSearch.getXSOpenSearchAction()
                    .getCaller();

            AddedOffencesPanelModel model = first.getAddedOffencesPanelModel();
            if (model.getAddedOffencesPanel() instanceof AddedOffencesToBreachPanel) {
                LOG.debug("openDialog - AddedOffencesToBreachPanel");
                AddedOffencesToBreachPanel panel = (AddedOffencesToBreachPanel) model.getAddedOffencesPanel();
                uod = new UncodedBreachDialog((Dialog) panel.getController(), myResources
                        .getString("AddUncodedOffence.title"), true, this, model);

            } else if (model.getAddedOffencesPanel() instanceof AddedOffencesPanel) {
                LOG.debug("openDialog - AddedOffencesPanel (Add Indictment)");
                AddedOffencesPanel panel = (AddedOffencesPanel) model.getAddedOffencesPanel();
                uom.setHoClass(uom.DEFAULT_COUNT_CLASS);
                uom.setHoSubclass(uom.DEFAULT_COUNT_SUBCLASS);
                uod = new UncodedIndictmentDialog((Dialog) panel.getController(), myResources
                        .getString("AddUncodedOffence.title"), true, this, model);
            }
        } else if (xac.getBodyPanel() instanceof ChargesController) {
            boolean isCreate = true; // determine the mode of operattion
            if (xSearch.getXSOpenSearchAction().getCaller() instanceof ChangeOffenceAction
                    || xSearch.getXSOpenSearchAction().getCaller() instanceof ChangeCountAction) {
                isCreate = false;
            }

            ChargesController cc = (ChargesController) xac.getBodyPanel();
            Integer ct = new Integer(cc.getModel().getSelectedChargeType());

            if (ct.equals(new Integer(ChargesController.INDICTMENTS_TAB))) {
                LOG.debug("openDialog - ChargesController.INDICTMENTS_TAB(Add count)");
                uom.setHoClass(uom.DEFAULT_COUNT_CLASS);
                uom.setHoSubclass(uom.DEFAULT_COUNT_SUBCLASS);
                uod = new UncodedCountDialog(xac, myResources.getString("AddUncodedOffence.title"), true, this,
                        (ChargesController) xac.getBodyPanel(), isCreate);
            } else if (ct.equals(new Integer(ChargesController.COMMITTALS_TAB))) {
                LOG.debug("openDialog - ChargesController.COMMITTALS_TAB");
                uod = new UncodedCommittalDialog(xac, myResources.getString("AddUncodedOffence.title"), true, this,
                        (ChargesController) xac.getBodyPanel(), isCreate);
            } else {
                LOG.debug("openDialog - ChargesController TAB = ?");
                uod = new UncodedSummaryDialog(xac, myResources.getString("AddUncodedOffence.title"), true, this,
                        (ChargesController) xac.getBodyPanel(), isCreate);
            }
        } else if (xac.getBodyPanel() instanceof VerdictsController) {
            LOG.debug("openDialog - VerdictsController");
            AdditionalInfoTableCell.SearchObsoleteOffenceAction infoAction = (AdditionalInfoTableCell.SearchObsoleteOffenceAction) xSearch
                    .getXSOpenSearchAction().getCaller();

            AdditionalInfoTableCell info = infoAction.getOuter();

            uod = new UncodedAlternateLesserDialog(xac, myResources.getString("AddUncodedOffence.title"), true, this,
                    info);
        } else if (xac.getBodyPanel() instanceof AppealResultsController) {
            LOG.debug("openDialog - AppealResultsController");
            AdditionalInfoTableCell.SearchOffenceAction infoAction = (AdditionalInfoTableCell.SearchOffenceAction) xSearch
                    .getXSOpenSearchAction().getCaller();

            AdditionalInfoTableCell info = infoAction.getOuter();

            uod = new UncodedAlternateLesserDialog(xac, myResources.getString("AddUncodedOffence.title"), true, this,
                    info);
        } else if (xSearch.getXSOpenSearchAction().getCaller() instanceof AdditionalInfoTableCell.SearchObsoleteOffenceAction) {
            LOG.debug("openDialog - AdditionalInfoTableCell.SearchObsoleteOffenceAction");
            AdditionalInfoTableCell.SearchObsoleteOffenceAction infoAction = (AdditionalInfoTableCell.SearchObsoleteOffenceAction) xSearch
                    .getXSOpenSearchAction().getCaller();

            AdditionalInfoTableCell info = infoAction.getOuter();

            uod = new UncodedAlternateLesserDialog(xac, myResources.getString("AddUncodedOffence.title"), true, this,
                    info);
        } else if (xSearch.getXSOpenSearchAction().getCaller() instanceof MultiplePleasPanel.PleaSearchOffenceAction) {
            LOG.debug("openDialog - MultiplePleasPanel.PleaSearchOffenceAction");
            MultiplePleasPanel.PleaSearchOffenceAction invokingAction = (MultiplePleasPanel.PleaSearchOffenceAction) xSearch
                    .getXSOpenSearchAction().getCaller();

            PleaControllerModel pcm = (PleaControllerModel) invokingAction.getModel();

            uom.setHoClass(uom.DEFAULT_COUNT_CLASS);
            uom.setHoSubclass(uom.DEFAULT_COUNT_SUBCLASS);
            uod = new UncodedPleaDialog((Dialog) SwingUtilities.getWindowAncestor(pcm.getMultiplePleasPanel()),
                    myResources.getString("AddUncodedOffence.title"), true, this, pcm);
        } else {
            throw new CSUnrecoverableException("The identity of the invoking process is not specified or unknown: "
                    + xSearch.getXSOpenSearchAction().getCaller());
        }

        xSearch.dispose();

        uod.setVisible(true);
    }

    /**
     * closeDialog
     */
    public void closeDialog() {
        uod.setVisible(false);
        uod.dispose();
    }

    /**
     * setModel
     * 
     * @param model
     *            parameter for setModel
     */
    public void setModel(UncodedOffenceModel model) {
        this.uom = model;
    }

    /**
     * Present the user with a choice of proceddings code
     * 
     * @param offenceValue
     *            OffenceValue
     * @param caseType
     *            the case type. T = Trial, S = Sentence etc.
     * @param chargeType
     *            the charge type. I = Indictment etc.
     * @throws UserCancelException
     * @throws BisRefControllerException
     */
    private void getHOProcCode(OffenceValue offenceValue, String caseType, String chargeType)
            throws UserCancelException, BisRefControllerException {
        HOProcCodeHelper hop;
        RefSystemCodeBasicValue hoproc = null;

        // Breach offences do not have an HO Proc code.
        if (!chargeType.equals(ChargeTypes.BREACH.getChargeType())) {
            if (caseType.equals("T")) {
                hop = new HOProcCodeHelper(xac);
                // INDICTMENT
                if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
                    hoproc = hop.getHoProcCode(HOProcCodeHelper.TRIAL);
                }
                // SUMMARY OFFENCE
                else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
                    hoproc = hop.getHoProcCode(HOProcCodeHelper.S41);
                }

                if (hoproc == null) {
                    throw new UserCancelException();
                } else {
                    offenceValue.setRefSystemCodeID(hoproc.getId());
                }
            }
            // COMMITTAL FOR SENTENCE (S Case).
            else if (caseType.equals("S") && chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
                hop = new HOProcCodeHelper(xac);
                hoproc = hop.getHoProcCode(HOProcCodeHelper.SENT);

                if (hoproc == null) {
                    throw new UserCancelException();
                } else {
                    offenceValue.setRefSystemCodeID(hoproc.getId());
                }
            } else {
                LOG.info("Unknown combination: caseType: " + caseType + " chargeType: " + chargeType);
            }
        }
    }

    /**
     * Validate class and subclass rules from CREST screen validation rules
     * 
     * @param caseType
     *            the case type. T = Trial, S = Sentence etc.
     * @param chargeType
     *            the charge type. I = Indictment etc.
     * @throws CSValidationException
     */
    public void validateModel(String caseType, String chargeType) throws CSValidationException {
        validateDescriptions();

        if (uom.getHoClass() == null || uom.getHoClass().trim().equals("")) {
            throw new CSValidationException("gui.uncodedOffence.class0Validation", "HO Class invalid.");
        }

        if (uom.getHoSubclass() == null || uom.getHoSubclass().trim().equals("")) {
            throw new CSValidationException("gui.uncodedOffence.subclass0Validation", "HO Subclass invalid.");
        }

        int classValue = Integer.parseInt(uom.getHoClass());
        int subclassValue = Integer.parseInt(uom.getHoSubclass());

        if (caseType.equals("T")) {
            validateTrialCase(chargeType, classValue, subclassValue);
        } else if (caseType.equals("S")) {
            validateSentenceCase(chargeType, classValue, subclassValue);
        } else if (caseType.equals("A")) {
            validateAppealCase(classValue, subclassValue);
        } else {
            LOG.error("validateModel Unknown case type = " + caseType);
        }

        uom.setHoClass(paddingString(uom.getHoClass(), 3, '0', true));
        uom.setHoSubclass(paddingString(uom.getHoSubclass(), 2, '0', true));
    }

    /**
     * Validate class and subclass rules for Appeal Cases
     * 
     * @param classValue
     *            the HO Class value
     * @param subclassValue
     *            the HO Sub Class
     * @throws CSValidationException
     */
    private void validateAppealCase(final int classValue, final int subclassValue) throws CSValidationException {
        if (!((999 >= classValue) && (classValue >= 1))) {
            String[] validation = new String[3];
            validation[0] = Integer.toString(classValue);
            validation[1] = Integer.toString(1);
            validation[2] = Integer.toString(999);

            throw new CSValidationException("gui.uncodedOffence.class1Validation", validation, "HO Class invalid.");
        }

        if (!((99 >= subclassValue) && (subclassValue >= 0))) {
            String[] validation = new String[3];
            validation[0] = Integer.toString(subclassValue);
            validation[1] = Integer.toString(0);
            validation[2] = Integer.toString(99);

            throw new CSValidationException("gui.uncodedOffence.subclass1Validation", validation,
                    "HO Subclass invalid.");
        }
    }

    /**
     * Validate class and subclass rules for Sentence Cases
     * 
     * @param chargeType
     *            the type of charge
     * @param classValue
     *            the HO Class value
     * @param subclassValue
     *            the HO Sub Class
     * @throws CSValidationException
     */
    private void validateSentenceCase(final String chargeType, final int classValue, final int subclassValue)
            throws CSValidationException {
        if (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())) {
            if (!(((825 >= classValue) && (classValue >= 803)) || ((195 >= classValue) && (classValue >= 101)) || ((99 >= classValue) && (classValue >= 1)))) {
                String[] validation = new String[7];
                validation[0] = Integer.toString(classValue);
                validation[1] = Integer.toString(1);
                validation[2] = Integer.toString(99);
                validation[3] = Integer.toString(101);
                validation[4] = Integer.toString(195);
                validation[5] = Integer.toString(803);
                validation[6] = Integer.toString(825);

                throw new CSValidationException("gui.uncodedOffence.class3Validation", validation, "HO Class invalid.");
            }

            if (!((99 >= subclassValue) && (subclassValue >= 0))) {
                String[] validation = new String[3];
                validation[0] = Integer.toString(subclassValue);
                validation[1] = Integer.toString(0);
                validation[2] = Integer.toString(99);

                throw new CSValidationException("gui.uncodedOffence.subclass1Validation", validation,
                        "HO Subclass invalid.");
            }
        } else if (chargeType.equals(ChargeTypes.BREACH.getChargeType())) {
            if (!(((825 >= classValue) && (classValue >= 803)) || ((195 >= classValue) && (classValue >= 101)) || ((99 >= classValue) && (classValue >= 1)))) {
                String[] validation = new String[7];
                validation[0] = Integer.toString(classValue);
                validation[1] = Integer.toString(1);
                validation[2] = Integer.toString(99);
                validation[3] = Integer.toString(101);
                validation[4] = Integer.toString(195);
                validation[5] = Integer.toString(803);
                validation[6] = Integer.toString(825);

                throw new CSValidationException("gui.uncodedOffence.class3Validation", validation, "HO Class invalid.");
            }

            if (!((99 >= subclassValue) && (subclassValue >= 0))) {
                String[] validation = new String[3];
                validation[0] = Integer.toString(subclassValue);
                validation[1] = Integer.toString(0);
                validation[2] = Integer.toString(99);

                throw new CSValidationException("gui.uncodedOffence.subclass1Validation", validation,
                        "HO Subclass invalid.");
            }
        } else {
            LOG.error("validateSentenceCase unknown charge type = " + chargeType);
        }
    }

    /**
     * Validate class and subclass rules for Trial Cases
     * 
     * @param chargeType
     *            the type of charge
     * @param classValue
     *            the HO Class value
     * @param subclassValue
     *            the HO Sub Class
     * @throws CSValidationException
     */
    private void validateTrialCase(final String chargeType, final int classValue, final int subclassValue)
            throws CSValidationException {
        if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType())) {
            if (!(((825 >= classValue) && (classValue >= 803)) || ((195 >= classValue) && (classValue >= 101)) || ((99 >= classValue) && (classValue >= 1)))) {
                String[] validation = new String[7];
                validation[0] = Integer.toString(classValue);
                validation[1] = Integer.toString(1);
                validation[2] = Integer.toString(99);
                validation[3] = Integer.toString(101);
                validation[4] = Integer.toString(195);
                validation[5] = Integer.toString(803);
                validation[6] = Integer.toString(825);

                throw new CSValidationException("gui.uncodedOffence.class3Validation", validation, "HO Class invalid.");
            }

            if (!((99 >= subclassValue) && (subclassValue >= 0))) {
                String[] validation = new String[3];
                validation[0] = Integer.toString(subclassValue);
                validation[1] = Integer.toString(0);
                validation[2] = Integer.toString(99);

                throw new CSValidationException("gui.uncodedOffence.subclass1Validation", validation,
                        "HO Subclass invalid.");
            }
        } else if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())) {
            if (!(((825 >= classValue) && (classValue >= 803)) || ((195 >= classValue) && (classValue >= 101)) || ((99 >= classValue) && (classValue >= 1)))) {
                String[] validation = new String[7];
                validation[0] = Integer.toString(classValue);
                validation[1] = Integer.toString(1);
                validation[2] = Integer.toString(99);
                validation[3] = Integer.toString(101);
                validation[4] = Integer.toString(195);
                validation[5] = Integer.toString(803);
                validation[6] = Integer.toString(825);

                throw new CSValidationException("gui.uncodedOffence.class3Validation", validation, "HO Class invalid.");
            }

            if (!((99 >= subclassValue) && (subclassValue >= 0))) {
                String[] validation = new String[3];
                validation[0] = Integer.toString(subclassValue);
                validation[1] = Integer.toString(0);
                validation[2] = Integer.toString(99);

                throw new CSValidationException("gui.uncodedOffence.subclass1Validation", validation,
                        "HO Subclass invalid.");
            }
        } else if (chargeType.equals(ChargeTypes.BREACH.getChargeType())) {
            if (!(((825 >= classValue) && (classValue >= 803)) || ((195 >= classValue) && (classValue >= 101)) || ((99 >= classValue) && (classValue >= 1)))) {
                String[] validation = new String[7];
                validation[0] = Integer.toString(classValue);
                validation[1] = Integer.toString(1);
                validation[2] = Integer.toString(99);
                validation[3] = Integer.toString(101);
                validation[4] = Integer.toString(195);
                validation[5] = Integer.toString(803);
                validation[6] = Integer.toString(825);

                throw new CSValidationException("gui.uncodedOffence.class3Validation", validation, "HO Class invalid.");
            }

            if (!((99 >= subclassValue) && (subclassValue >= 0))) {
                String[] validation = new String[3];
                validation[0] = Integer.toString(subclassValue);
                validation[1] = Integer.toString(0);
                validation[2] = Integer.toString(99);

                throw new CSValidationException("gui.uncodedOffence.subclass1Validation", validation,
                        "HO Subclass invalid.");
            }
        } else {
            LOG.error("validateTrialCase unknown charge type = " + chargeType);
        }
    }

    private void validateDescriptions() throws CSValidationException {
        if (uom.getHoDesc() == null || uom.getHoDesc().trim().equals("")) {
            throw new CSValidationException("gui.uncodedOffence.hodescValidation", "HO Description invalid.");
        }

        if (uom.getRsDesc() == null || uom.getRsDesc().trim().equals("")) {
            throw new CSValidationException("gui.uncodedOffence.rsdescValidation", "RS Description invalid.");
        }
    }

    private ChargeType convertChargeType(int tab) {
        ChargeType rtn;

        switch (tab) {
        case ChargesController.INDICTMENTS_TAB:
            rtn = ChargeTypes.INDICTMENT;
            break;
        case ChargesController.SECTION41S_TAB:
            rtn = ChargeTypes.SECTION_41;
            break;
        case ChargesController.COMMITTALS_TAB:
            rtn = ChargeTypes.COMMITAL_FOR_SENTENCE;
            break;
        case ChargesController.BREACHES_TAB:
            rtn = ChargeTypes.BREACH;
            break;
        case ChargesController.APPEALOFFENCES_TAB:
        	rtn = ChargeTypes.CRIMINAL_APPEAL;
        	break;
        default:
            rtn = ChargeTypes.INDICTMENT;
            break;
        }

        return rtn;
    }

    private void setUncodedOffenceDescription(String desc) {
        this.uncodedOffenceDesc = desc;
    }

    private String getUncodedOffenceDescription() {
        return this.uncodedOffenceDesc;
    }

    /**
     * pad a string S with a size of N with char C on the left (true) or on the
     * right(false).
     * 
     * @param s
     *            the String to pad.
     * @param n
     *            number of chars to pad.
     * @param c
     *            the char used to do the padding.
     * @param paddingLeft
     *            true if padding on the left.
     * @return the padded String.
     */
    private static String paddingString(String s, int n, char c, boolean paddingLeft) {
        StringBuffer str = new StringBuffer(s);
        int strLength = str.length();
        if (n > 0 && n > strLength) {
            for (int i = 0; i <= n; i++) {
                if (paddingLeft) {
                    if (i < n - strLength) {
                        str.insert(0, c);
                    }
                } else {
                    if (i > strLength) {
                        str.append(c);
                    }
                }
            }
        }
        return str.toString();
    }

    private String getResource(final String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, key);
    }
}