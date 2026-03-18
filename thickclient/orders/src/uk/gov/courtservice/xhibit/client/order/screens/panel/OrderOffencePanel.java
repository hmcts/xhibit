package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.bea.staxb.runtime.internal.util.collections.BooleanArrayIterator;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrderOffenceDialog;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * @author guthriec
 * Panel for slecting offences for the D20 Order
 */
/**
 * Order Offence Panel that contains a panel with a list of offences to add to the D20 with checkboxes and details.
 * This also includes a button that allows you to select all the checkbox in the list.
 * 
 * @author guthriec
 *
 */
public class OrderOffencePanel extends XPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderOffenceModel model;
	private OrderOffenceDialog parent;
	

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel newD20Label;
	private JButton newD20Button;
	private OrderOffenceListPanel orderOffencePanelList;
	
	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param model
	 * @throws CSRecoverableException
	 */
	public OrderOffencePanel(OrderOffenceDialog parent, OrderOffenceModel model) throws CSRecoverableException{
		this.parent = parent;
		this.model = model;
		stepInitialise();
		init();
		
	}
	
	/**
	 * This sets the button for marking all the checkboxes and the list of offences
	 * 
	 * @throws CSRecoverableException
	 */
	public void init() throws CSRecoverableException {
		
		orderOffencePanelList = new OrderOffenceListPanel(this, model);
		model.setCaseID(parent.getModel().getCaseID());
		model.setDefendantID(parent.getModel().getDefendantID());
		this.setLayout(gridBagLayout1);

        // Add Labels
        this.add((getNewD20Label()), new GridBagConstraints(0, 0, 1, 1, 0, 0.05, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        
     // Add Entry fields
        this.add(getNewD20Button(), new GridBagConstraints(1, 0, 2, 1, 0, 0.05, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        
        //Panel
        this.add(new JScrollPane(orderOffencePanelList, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ), new GridBagConstraints(0, 1, 3, 1, 1.0, 0.95, GridBagConstraints.CENTER,
        		GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));

	}
	
    private JLabel getNewD20Label() {
        if (newD20Label == null) {
        	newD20Label = new JLabel(getString("newD20Label"));
        }
        return newD20Label;
    }
    
    /**
     * @return A D20 button that when pressed will enable all the checkboxes
     */
    private JButton getNewD20Button(){
    	if(newD20Button == null){
    		newD20Button = new JButton(getString("newD20Button"));
    		newD20Button.setBounds(50, 100, 95, 30);
    		newD20Button.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
   				 orderOffencePanelList.enableAllCheckboxes();
				 model.newD20();
    			}
    	});
    	}
    	return newD20Button;
    }

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
		stepUpdateViewState();
	}

	private void moveModelToScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
			parent.setOkEnabled(model.isOkEnabled());
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		moveModelToScreen();
		
	}

	/* (non-Javadoc)
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeinitialise(boolean)
	 * 
	 * When pressing the ok button the models finished value is set to be true 
	 * i.e. the offences have been selected and ready to move on to the next page
	 * 
	 * When pressing the cancel button there will be a another dialog to ask if the user
	 * is sure if they want to cancel. They click yes and will be sent to main xhbit application.
	 * Or select No and go back to the order offence dialog.
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update == false) {
            int rc = JOptionPane.showConfirmDialog(this.getParent(), ResourceHelper
                    .getResourceString(AbstractOrdersPanel.CANCEL_CONFIRM), ResourceHelper
                    .getResourceString(AbstractOrdersPanel.CANCEL_TITLE), JOptionPane.YES_NO_OPTION);
            if (rc != 0) {
                throw new UserCancelException();
            }
        }
		this.model.setFinished(update);
	}
	
	 /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.OrderOffenceResources, key);
    }
    
    public OrderOffenceDialog getParentDialog() {
		return parent;
	}

}
