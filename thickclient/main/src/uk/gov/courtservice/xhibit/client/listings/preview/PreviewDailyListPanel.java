package uk.gov.courtservice.xhibit.client.listings.preview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * An Xpanel to display the Preview Daily List popup dialog box.
 * 
 */
public class PreviewDailyListPanel extends XPanel {

	private static final long serialVersionUID = 1L;
		
	private PreviewListModel previewListModel;	
	private ButtonGroup listTypeRadioButtonGroup = null;
    private JRadioButton dailyListRb = null;
    private JRadioButton courtRoomListRb = null;    
    private JRadioButton prisonListRb = null;
    
    /**
     * constructor passing in the PreviewListModel.
     * 
     * @param PreviewListModel previewDailyListModel to store the data.
     */
    public PreviewDailyListPanel(PreviewListModel previewDailyListModel) throws CSRecoverableException {
		this.previewListModel = previewDailyListModel;
		stepInitialise();
		jbInit();
	}
    
    /**
     * Initialises variables and model.
     * 
     * @throws CSRecoverableException
     */
	@Override
	public void stepInitialise() throws CSRecoverableException {
		stepUpdateViewState();
	}

	/**
     * Moves data from model to the screen
     * 
     * @throws CSRecoverableException
     */
	@Override
	public void stepActivate() throws CSRecoverableException {

	}

	/**
     * Updates screen components from user input
     * 
     * @throws CSRecoverableException
     */
	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	/**
     * Validates User input
     * 
     * @throws CSRecoverableException
     */
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	/**
     * Moves data from screen to model
     * 
     * @throws CSRecoverableException
     */
	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}
	
	/**
     * Called when the dialogue closes. Preview/Close (OK/Cancel) processing
     * 
     * @param boolean update - true if Preview button clicked. False if Close.
     * @throws CSRecoverableException
     */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if(update){
			PreviewUnpublishedDailyListAction previewDailyListAction = new PreviewUnpublishedDailyListAction(previewListModel.getListID(), this.courtRoomListRb.isSelected(), this.prisonListRb.isSelected());
			previewDailyListAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		}
	}
	
	/**
     * Creates the swing controls for the Preview Daily list panel
     * 
     */
	private void jbInit() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.add(scrollPane, gbc);
		
        // Create list type panel 			
 		final JPanel listTypePanel = initListTypePanel();		
        mainPanel.add(listTypePanel, gbc);
	}

	
	/**
     * Creates the list type panel
     * 
     * @return JPanel the list type panel
     */
	private JPanel initListTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		// create panel
		final JPanel listTypePanel = new JPanel();
		
		//set border and layout for this panel
		listTypePanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Listings,"listPrintPreviewListType")));
		listTypePanel.setLayout(new GridBagLayout());
		
		// Add the panel elements
		// add radio buttons on same row and within a button group
		getListTypeRadioButtonGroup().add(getDailyListRb());
		listTypePanel.add(getDailyListRb(), gbc);
        gbc.gridx++;

		getListTypeRadioButtonGroup().add(getCourtRoomListRb());
		listTypePanel.add(getCourtRoomListRb(), gbc);		 

		gbc.gridx++;
		getListTypeRadioButtonGroup().add(getPrisonListRb());
		listTypePanel.add(getPrisonListRb(), gbc);

		return listTypePanel;
	}
			   
    /**
	 * get the list type radio button group
	 *@return ButtonGroup
	 */
    private ButtonGroup getListTypeRadioButtonGroup() {
	    if (listTypeRadioButtonGroup == null) {
	    	listTypeRadioButtonGroup = new ButtonGroup();
	    }
	    return listTypeRadioButtonGroup;
	}   
	
	/**
	 * get the daily list radio button group
	 *@return JRadioButton
	 */
	private JRadioButton getDailyListRb() {
	    if (dailyListRb == null) {
	    	dailyListRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "listPrintPreviewDailyListButton"));
	    	dailyListRb.setSelected(true);
	    	dailyListRb.addActionListener(new XAction() {
				private static final long serialVersionUID = 1L;
				public void xActionPerformed(ActionEvent e) {
	            	// left here to add any necessary action required later
	            }
	        });
	    }
	    return dailyListRb;
	}
	
	/**
	 * get the court room list radio button group
	 *@return JRadioButton
	 */
	private JRadioButton getCourtRoomListRb() {
	    if (courtRoomListRb == null) {
	    	courtRoomListRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "listPrintPreviewCourtRoomList"));
	    	courtRoomListRb.addActionListener(new XAction() {
				private static final long serialVersionUID = 1L;
				public void xActionPerformed(ActionEvent e) {
	            	// left here to add any necessary action required later
	            }
	        });
	    }
	    return courtRoomListRb;
	}
	
	/**
	 * get the prison list radio button group
	 *@return JRadioButton
	 */
	private JRadioButton getPrisonListRb() {
	    if (prisonListRb == null) {
	    	prisonListRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "listPrintPreviewPrisonList"));
	    	prisonListRb.addActionListener(new XAction() {
	    		private static final long serialVersionUID = 1L;
	            public void xActionPerformed(ActionEvent e) {
	            	// left here to add any necessary action required later
	            }
	        });
	    }
	    return prisonListRb;
	}
}