package uk.gov.courtservice.xhibit.client.listings.createlist;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

public class CreateListOptionsPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;

	private static final String DATE_FORMAT = "dd-MMM-yyyy";
	
	private ButtonGroup radioButtonGroup;
	private JRadioButton createNewListRadioButton;
	private JRadioButton createFromPreviousDailyListRadioButton;
	private JRadioButton createFromPreviousFirmListRadioButton;

	private CreateListOptionsModel model;
	private XDialog parent;

	public CreateListOptionsPanel(XDialog parent, CreateListOptionsModel model)
			throws CSRecoverableException {
		this.model = model;
		this.parent = parent;

		stepInitialise();
		jbInit();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);		
		
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.add(scrollPane, gbc);
		
		// Setup the button Panel
		getButtonPanel().okButton.setText(getOkButtonText());		
		
		// Add the icon
		gbc.weightx = 0.2;
		mainPanel.add(getIcon(), gbc);
		gbc.gridx++;
		
		// Add the List specific panels
		gbc.weightx = 0.8;
		if (model.isRadioButtonDisplay()) {
			getButtonPanel().okButton.setEnabled(false);
			JPanel radioButtonPanel = initDailyRadioButtonPanel();
			mainPanel.add(radioButtonPanel, gbc);
		} else { 
			JPanel messagePanel = initMessagePanel();
			mainPanel.add(messagePanel, gbc);
		}
	}	

	private JLabel getIcon() {
		return new JLabel(UIManager.getDefaults().getIcon("OptionPane.questionIcon"));
	}
	
	private String getOkButtonText() {	
		return getResourceBundle(model.isRadioButtonDisplay() ? "createListOptionsContinue" : "createListOptionsOpenExisting");
	}
	
	private String getResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, resourceKey);
	}

	private String getResourceBundle(String resourceKey, Object[] objects) {
		return MessageFormat.format(getResourceBundle(resourceKey), objects);
	}

	private JPanel initDailyRadioButtonPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
 
		radioButtonGroup = new ButtonGroup();
					
		createNewListRadioButton = initNewOptionRadioButton(
				getResourceBundle("createListOptionCreateDaily"));
		panel.add(createNewListRadioButton, gbc);
		gbc.gridy++;
		
		if (model.getPreviousDailyList() != null) {		
			createFromPreviousDailyListRadioButton = initNewOptionRadioButton(
					getResourceBundle("createListOptionCreateDailyFromPreviousDaily",
							new Object[] {getFormattedDate(model.getPreviousDailyList().getListStartDate()) } ));
			panel.add(createFromPreviousDailyListRadioButton, gbc);
			gbc.gridy++;
		}
		
		if (model.getPreviousFirmList() != null) {
			createFromPreviousFirmListRadioButton = initNewOptionRadioButton(
					getResourceBundle("createListOptionCreateDailyFromPreviousFirm",
							new Object[] {getFormattedDate(model.getPreviousFirmList().getListStartDate(),
									model.getPreviousFirmList().getListEndDate()) } ));
			panel.add(createFromPreviousFirmListRadioButton, gbc);
		}
		
		return panel;
	}
	
	private JPanel initMessagePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel message = new MultiLineJLabel(getMessage());
		panel.add(message, gbc);
		
		return panel;
	}	
		
	private OkCancelPanel getButtonPanel() {
		return ((OkCancelPanel) parent.getButtonPanel());
	}
		
	private String getListDetails(ListBasicValue list) {		
		StringBuilder result = new StringBuilder();
		// Unpublished or Published...
		if (!list.isPublished()) {
			result.append(getResourceBundle("createListOptionsUnpublished"));
		} else {
			result.append(getResourceBundle("createListOptionsPublished"));
		}
		result.append(" ");
		// DRAFT or FINAL...
		if (list.isDraft()) {
			result.append(getResourceBundle("createListOptionsDraft").toUpperCase());
		} else {
			result.append(getResourceBundle("createListOptionsFinal").toUpperCase());
		}
		result.append(" ");
		// Daily/Firm/Warned...
		if (model.getListType().isDaily()) {
			result.append(getResourceBundle("createListOptionsDaily"));
		} else if (model.getListType().isFirm()) {
			result.append(getResourceBundle("createListOptionsFirm"));
		} else if (model.getListType().isWarned()) {
			result.append(getResourceBundle("createListOptionsWarn"));
		}
		result.append(" ");
		// List for <start> to <end>...
		String dateString;
		if (model.getListType().isDaily()) {
			dateString = getFormattedDate(list.getListStartDate());
		} else {
			dateString = getFormattedDate(list.getListStartDate(), list.getListEndDate());
		}
		result.append(getResourceBundle("createListOptionsListDetails", new Object[] { dateString }));
		return result.toString(); 	
	}
	
	private String getMessage() {
		if (model.isDailyListForDate()) {
			return MessageFormat.format(getResourceBundle("createListOptionDailyListExists"), 
					new Object[] { getListDetails(model.getPreviousDailyList()) } );
		} else if (model.getListType().isFirm()) { 
			return MessageFormat.format(getResourceBundle("createListOptionFirmListExists"), 
					new Object[] { getListDetails(model.getPreviousFirmList()) } );			
		} else if (model.getListType().isWarned()) {
			return MessageFormat.format(getResourceBundle("createListOptionWarnListExists"), 
					new Object[] { getListDetails(model.getPreviousWarnList()) } );			
		}
		return "";
	}
	
	private JRadioButton initNewOptionRadioButton(String text) {
		JRadioButton result = new JRadioButton(text);
		result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOkButtonEnabled();
			}
		});
		radioButtonGroup.add(result);
		return result;
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
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
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		setOkButtonEnabled();		
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (update) {
			if (getButtonPanel().okButton.equals(getDeinitialiseSource())) {
				if (model.isRadioButtonDisplay()) {
					if (isRadioSelected(createNewListRadioButton)) {
						model.setAction(CreateListOptionsModel.Action.CREATE);
						model.setSelectedListId(null);
					} else if (isRadioSelected(createFromPreviousDailyListRadioButton)) {
						model.setAction(CreateListOptionsModel.Action.CREATE);
						model.setSelectedListId(model.getPreviousDailyList().getListId());
					} else if (isRadioSelected(createFromPreviousFirmListRadioButton)) {
						model.setAction(CreateListOptionsModel.Action.CREATE);
						model.setSelectedListId(model.getPreviousFirmList().getListId());
					}
				} else if (model.isDailyListForDate()) {
					model.setAction(CreateListOptionsModel.Action.OPEN);
					model.setSelectedListId(model.getPreviousDailyList().getListId());
				} else if (model.getListType().isFirm()) {
					model.setAction(CreateListOptionsModel.Action.OPEN);
					model.setSelectedListId(model.getPreviousFirmList().getListId());
				} else if (model.getListType().isWarned()) {
					model.setAction(CreateListOptionsModel.Action.OPEN);
					model.setSelectedListId(model.getPreviousWarnList().getListId());
				}
			}				
		}
	}	

	private void setOkButtonEnabled() {
		if (model.getListType().isDaily()) {
			boolean optionSelected = 
					isRadioSelected(createNewListRadioButton) ||
					isRadioSelected(createFromPreviousDailyListRadioButton) ||
					isRadioSelected(createFromPreviousFirmListRadioButton);
			getButtonPanel().okButton.setEnabled(optionSelected);	
		}
	}
	
	private boolean isRadioSelected(JRadioButton radioButton) {
		return (radioButton != null && radioButton.isSelected());
	} 
	
	private String getFormattedDate(Date date) {
		return date != null ? new SimpleDateFormat(DATE_FORMAT).format(date.getTime()) : null;
	}
	
	private String getFormattedDate(Date startDate, Date endDate) {
		String startDateString = getFormattedDate(startDate);
		if (endDate != null) {			
			String endDateString = getFormattedDate(endDate);
			return getResourceBundle("createListOptionsListDateRange", new Object[] {startDateString, endDateString} );
		}		
		return startDateString;
	}
	
	/*
	 * Extend JLabel to include multiline functionality
	 */
	private static class MultiLineJLabel extends JLabel {
	
		private static final long serialVersionUID = 1L;
		
		private static final String LINEFEED = "\n";
		
		public MultiLineJLabel(String text) {
			super(text);
		}

		@Override
		public void setText(String text) {			
			if (StringUtils.contains(text, LINEFEED)) {				
				setMultiLineText(text);
			} else {
				super.setText(text);
			}
		}
		
		private void setMultiLineText(String text) {
			StringBuilder newText = new StringBuilder();
			newText.append("<html>");
			newText.append(StringUtils.replace(text, LINEFEED, "<br>"));
			newText.append("</html>");
			super.setText(newText.toString());
		}
	}
}
