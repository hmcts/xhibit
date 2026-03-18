/**
 * <p>
 * Title: ListResultsDialog
 * </p>
 * <p>
 * Description: ListResultsDialog represents the Dialog for the List Results screen
 * CTX-1314
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Vincent
 * @version 1.0
 */

package uk.gov.courtservice.xhibit.client.listings.listresults;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class ListResultsDialog extends XDialog { 
	
	private static final long serialVersionUID = 1L;

	private ListResultsPanel bodyPanel;
    private ListResultsModel model;

    /**
     * Constructor
     * @param frame
     * @param model
     * @throws CSRecoverableException
     */
	public ListResultsDialog(Frame frame, ListResultsModel model) throws CSRecoverableException {
        super(frame, XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsTitle"), 
        		true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);
        
        this.model = model;
        this.bodyPanel = new ListResultsPanel(this, this.model);
        
        addBodyPanel(bodyPanel);
        setResizable(true);
        pack();
	}
	
	@Override
	protected OkCancelPanel createButtonPanel(int panelType, int defaultButton){
		OkCancelPanel buttonPanel = super.createButtonPanel(panelType, defaultButton);
		buttonPanel.okButton.setVisible(false);
		buttonPanel.cancelButton.setToolTipText("Close");
		buttonPanel.cancelButton.setText("Close");
		
		return buttonPanel;
	}
}
