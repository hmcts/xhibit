/**
 * <p>
 * Title: NonAvailableDaysDialog
 * </p>
 * <p>
 * Description: NonAvailableDaysDialog represents the Dialog for the Non Available Days screen
 * CTX-1313
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */

package uk.gov.courtservice.xhibit.client.listings.nonavailabledays;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class NonAvailableDaysDialog extends XDialog { 
	
	private static final long serialVersionUID = 1L;

	private NonAvailableDaysPanel bodyPanel;
    private NonAvailableDaysModel model;

    /**
     * Action Constructor (called from menu)
     * @param frame Frame object
     * @param model NonAvailableDaysModel model
     * @throws CSRecoverableException
     */
	public NonAvailableDaysDialog(Frame frame, NonAvailableDaysModel model) throws CSRecoverableException{
		super(frame, XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysTitle"), 
        		true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);	
		initDialog(model);
	}
	
	/**
	 * Modal Screen (called from a parent dialog)
	 * @param parent XDialog parent object
	 * @param model NonAvailableDaysModel model
	 * @throws CSRecoverableException
	 */
	public NonAvailableDaysDialog(XDialog parent, NonAvailableDaysModel model) throws CSRecoverableException {
        super(parent, XHIBITConstant.getResource(XhibitBundles.Listings, "nonAvailableDaysTitle"), 
        		true, XDialog.CUSTOM, XDialog.DEFAULTCANCEL);
        initDialog(model);
	}
	
	/**
	 * Method to initialise the Dialog
	 * @param model NonAvailableDaysModel model
	 * @throws CSRecoverableException
	 */
	private void initDialog(NonAvailableDaysModel model) throws CSRecoverableException {
		this.model = model;
        this.bodyPanel = new NonAvailableDaysPanel(this, this.model);
        
        addBodyPanel(bodyPanel);
        pack();
	}	
}