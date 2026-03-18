package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;


/**
 * <p>
 * Title: Authorise Results
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AuthoriseResultsDialog.java,v 1.3 2004/09/20 16:13:28 szfnvt
 *          Exp $
 * @history James Powell 13/03/2009 - Introduced a new constructor to allow this dialog
 *          to be called from The new Unauthorised Case Status Screen as well as from the
 *          action when the case is currently open. Also changed the title caption to
 *          include the case number to avoid confusion.
 * @history James Powell 18/03/2009 - Moved Sequence number check to here from the action
 *          class and also included this check when a case is not currently open in Xhibit.
 * @history Kelvin Davies 20/03/2009 - Updated prepareButtons() to include a additional Apply button.
 *          This button is used to apply/save the vulnerable victim indicator value to the specified case.
 */

public class AuthoriseResultsDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private static final String AUTH_RESULTS = XhibitBundles.CaseProgressResources;
    private static final String TITLE_SUFFIX_DEFAULT = " - Case ";
    
    private AuthoriseWarning[] warnings;
    private AuthoriseResultsPanel panel;
    private AuthoriseSyncAction authoriseSyncAction;   
    private AuthorisePreviewAction authorisePreviewAction;   
    private AuthoriseUpdateVictimIndicatorAction authoriseUpdateVictimIndicatorAction;
    
      
    public AuthoriseResultsDialog(XhibitApplicationController xac) throws CSRecoverableException { 
        super(xac, ResourceBundleHelper.getResource(AUTH_RESULTS, "authorise.dialog_title"), true,
                XDialog.APPLYOKCANCEL, XDialog.DEFAULTAPPLY);

        //Show warning message if Sequence Numbers aren't sequential
        checkSequenceNumbersOpen(xac);
        performPreAuthorisationChecks(xac, xac.getApplicationCaseModel().getCaseId());

        panel = new AuthoriseResultsPanel(xac, warnings);
        String titleSuffix = TITLE_SUFFIX_DEFAULT+xac.getApplicationCaseModel().getCaseType()+xac.getApplicationCaseModel().getCaseNumber();
        initialise(titleSuffix);
    }

    public AuthoriseResultsDialog(
            XhibitApplicationController xac,
            XhbCaseBasicValue caseValue, 
            Integer scheduledHearingId)
    throws CSRecoverableException{
        super(xac, ResourceBundleHelper.getResource(AUTH_RESULTS, "authorise.dialog_title"), true,
                XDialog.APPLYOKCANCEL, XDialog.DEFAULTAPPLY);
        
        //Show warning message if Sequence Numbers aren't sequential        
        checkSequenceNumbersClosed(caseValue.getCaseId());
        performPreAuthorisationChecks(xac, caseValue.getCaseId());
                
        panel = new AuthoriseResultsPanel(warnings, caseValue, scheduledHearingId);
        String titleSuffix = TITLE_SUFFIX_DEFAULT + caseValue.getCaseType() + caseValue.getCaseNumber();
        initialise(titleSuffix);
    }

    /**
     * This method is used to check that the Sequence numbers are sequential when the case is open in xhibit
     * @param xac
     * @throws CSRecoverableException
     */
    private void checkSequenceNumbersOpen(XhibitApplicationController xac) throws CSRecoverableException{
        HashMap <Integer,List> defOnCaseSeqNosMap = xac.getApplicationCaseModel().getDefOnCaseSeqNosMap();
        checkSequenceNumbers(defOnCaseSeqNosMap);
    }
    
    /**
     * This method is used to check that the Sequence numbers are sequential when the case is not open in xhibit
     * @param caseId
     * @throws CSRecoverableException
     */
    private void checkSequenceNumbersClosed(Integer caseId)throws CSRecoverableException{
            ChargeCompositeValue ccv = XhibitDelegateHelper.getResults2Delegate().getCharges(caseId);
            HashMap<Integer,List> defOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(ccv.getCharges(), ccv.getAllDefendants());
            checkSequenceNumbers(defOnCaseSeqNosMap);
            
    }
    
    /**
     * This method takes a Hashmap which contains a list of sequence numbers for each defendant. If there exists a defendant
     * where the sequence numbers are sequential, then the user is shown an error message where they can either continue or cancel the authorise
     * 
     * @param seqNoMap
     * @throws CSRecoverableException
     */
    private void checkSequenceNumbers(HashMap<Integer, List> seqNoMap) throws CSRecoverableException{
        if(!SeqNoHelper.areListsSequential(seqNoMap)){
            String message = XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,"SeqNosNotSequentialText1");
            message += "\n\n"+XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,"SeqNosNotSequentialText2");
            String title = XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,"SeqNosNotSequentialTitle");
            if(JOptionPane.showConfirmDialog(this,message , title,JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.OK_OPTION){
                throw new UserCancelException();
            }
        }
    }
    
    private void initialise(String titleSuffix){
        this.setTitle(ResourceBundleHelper.getResource(AUTH_RESULTS, "authorise.dialog_title")+titleSuffix);

//      create AuthoriseSyncAction & AuthoriseUpdateVictimIndicatorAction and set tp panel
        authoriseUpdateVictimIndicatorAction = new AuthoriseUpdateVictimIndicatorAction(panel);
        authoriseUpdateVictimIndicatorAction.setEnabled(false);
        panel.setAuthoriseUpdateVictimIndicatorAction(authoriseUpdateVictimIndicatorAction);
        
        authoriseSyncAction = new AuthoriseSyncAction(panel);
        panel.setAuthoriseSyncAction(authoriseSyncAction);
        
        authorisePreviewAction = new AuthorisePreviewAction(this);
        panel.setAuthorisePreviewAction(authorisePreviewAction);
        
        prepareButtons();
        addBodyPanel(panel);
        pack();
    }

   

	private void prepareButtons() {
    	
    	final JButton previewButton = new JButton();
    	
        ApplyOkCancelPanel buttonPanel = (ApplyOkCancelPanel) getButtonPanel();
        // Hide OK button
        //buttonPanel.okButton.setVisible(false);

        // Rename Cancel to Close
        buttonPanel.getCancelAction().populateFromBundle("Close");
        // Assign new action to Authorise
        buttonPanel.applyButton.setAction(authoriseSyncAction);
        // Assign new action to AuthorisePreview
         //CCN1263
        buttonPanel.okButton.setAction(authoriseUpdateVictimIndicatorAction);
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 700, 0, 0);
        buttonPanel.add(previewButton, gbc);
         
        previewButton.setAction(authorisePreviewAction);
   
	}
    
    private void performPreAuthorisationChecks(
            XhibitApplicationController xac,
            Integer caseId) 
    throws CSRecoverableException {
        
        warnings = 
            XhibitDelegateHelper.getResults2Delegate().getAuthoriseWarnings(caseId);
        
        if (warnings != null && warnings.length > 0) {
            
            String dlgTitle = 
                ResourceBundleHelper.getResource(
                        XhibitBundles.CaseProgressResources, 
                        "results.authorise.warningdialog.title");
            
            DisposalDateWarningDialog dialog = 
                new DisposalDateWarningDialog(
                        xac, dlgTitle, true, warnings);
            
            dialog.setVisible(true);
            if (!dialog.isOkClicked()) {
                throw new UserCancelException();
            }
        }
    }
}