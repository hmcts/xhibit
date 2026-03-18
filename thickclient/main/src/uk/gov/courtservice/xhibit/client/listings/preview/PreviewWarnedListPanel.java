package uk.gov.courtservice.xhibit.client.listings.preview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class PreviewWarnedListPanel extends XPanel {

	private static final long serialVersionUID = 1L;

	private PreviewListModel previewListModel;	
	private ButtonGroup listTypeRadioButtonGroup = null;
    private JRadioButton warnedListRb = null;
    private JRadioButton annotatedWarnedListRb = null;    
	private XCheckBox standardNoteCheckbox;
	private XCheckBox priorityNoteCheckbox;
	private XCheckBox restrictedNoteCheckbox;
	private JPanel mainPanel;

	private XDialog parentDialog;

	private JPanel notesToDisplayPanel;
	
    public PreviewWarnedListPanel(PreviewListModel previewDailyListModel, XDialog parent) throws CSRecoverableException {
		this.previewListModel = previewDailyListModel;
		this.parentDialog = parent;
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
     * Creates the swing controls for the Preview Daily list panel
     * 
     */
	private void jbInit() {
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		mainPanel = new JPanel();
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
		getListTypeRadioButtonGroup().add(getWarnedListRb());
		listTypePanel.add(getWarnedListRb(), gbc);
        gbc.gridx++;

		getListTypeRadioButtonGroup().add(getAnnotatedWarnedListRb());
		listTypePanel.add(getAnnotatedWarnedListRb(), gbc);		 

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
	private JRadioButton getWarnedListRb() {
	    if (warnedListRb == null) {
	    	warnedListRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "listPrintPreviewWarnedListButton"));
	    	warnedListRb.setSelected(true);
	    	warnedListRb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
		            	mainPanel.remove(getNotesToDisplayPanel());
		            	mainPanel.validate();
		            	mainPanel.repaint();
		            	validate();
		            	parentDialog.pack();
					}
				}
	        });
	    }
	    return warnedListRb;
	}
	
	/**
	 * get the court room list radio button group
	 *@return JRadioButton
	 */
	private JRadioButton getAnnotatedWarnedListRb() {
	    if (annotatedWarnedListRb == null) {
	    	annotatedWarnedListRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.Listings, "listPrintPreviewAnnotatedWarnedList"));
	    	annotatedWarnedListRb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
		            	GridBagConstraints gbc = new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		            	mainPanel.add(getNotesToDisplayPanel(), gbc);
		            	resetNotesToDisplayPanel();
		            	mainPanel.validate();
		            	mainPanel.repaint();
		            	validate();
		            	parentDialog.pack();
					}
				}
	        });
	    }
	    return annotatedWarnedListRb;
	}
	
	private JPanel getNotesToDisplayPanel(){
		
		if(notesToDisplayPanel == null){
			GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
			// create panel
			notesToDisplayPanel = new JPanel();
			
			//set border and layout for this panel
			notesToDisplayPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Listings,"listPrintPreviewNotesToDisplay")));
			notesToDisplayPanel.setLayout(new GridBagLayout());
			
			gbc.gridx++;
			priorityNoteCheckbox = new XCheckBox();
			notesToDisplayPanel.add(priorityNoteCheckbox, gbc);
			
			gbc.gridx++;
			JLabel priorityNoteLabel = new JLabel((XHIBITConstant.getResource(XhibitBundles.Listings,"listPrintPreviewPriorityNote")));
			notesToDisplayPanel.add(priorityNoteLabel, gbc);
			
			gbc.gridx++;
			restrictedNoteCheckbox = new XCheckBox();
			notesToDisplayPanel.add(restrictedNoteCheckbox, gbc);
			
			gbc.gridx++;
			JLabel restrictedNoteLabel = new JLabel((XHIBITConstant.getResource(XhibitBundles.Listings,"listPrintPreviewRestrictedNote")));
			notesToDisplayPanel.add(restrictedNoteLabel, gbc);
			
			gbc.gridx++;		
			standardNoteCheckbox = new XCheckBox();
			notesToDisplayPanel.add(standardNoteCheckbox, gbc);
			
			gbc.gridx++;
			JLabel standardNoteLabel = new JLabel((XHIBITConstant.getResource(XhibitBundles.Listings,"listPrintPreviewStandardNote")));
			notesToDisplayPanel.add(standardNoteLabel, gbc);
			
			// reset state of check boxes
			resetNotesToDisplayPanel();
		}	
		
		return notesToDisplayPanel;
	}
    
	private void resetNotesToDisplayPanel() {
		if(notesToDisplayPanel != null) {
			priorityNoteCheckbox.setSelected(true);
			restrictedNoteCheckbox.setSelected(false);
			standardNoteCheckbox.setSelected(false);
		}
	}
	
	@Override
	public void stepActivate() throws CSRecoverableException {
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

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
			if(this.getWarnedListRb().isSelected()){
				PreviewUnpublishedWarnedListAction previewWarnedListAction = new PreviewUnpublishedWarnedListAction(previewListModel.getListID());
				previewWarnedListAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		
			}else{
				PreviewUnpublishedAnnotatedWarnedListAction previewWarnedListAction = new PreviewUnpublishedAnnotatedWarnedListAction(previewListModel.getListID(), this.standardNoteCheckbox.isSelected(), this.priorityNoteCheckbox.isSelected(), this.restrictedNoteCheckbox.isSelected());
				previewWarnedListAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		
			}
		}
	}

}
