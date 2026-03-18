package uk.gov.courtservice.xhibit.client.listings.createlist;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CreateListOptionsValue;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.daily.DailyListModel;
import uk.gov.courtservice.xhibit.client.listings.list.daily.DailyListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.firm.FirmListModel;
import uk.gov.courtservice.xhibit.client.listings.list.firm.FirmListPanel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListModel;
import uk.gov.courtservice.xhibit.client.listings.list.warned.WarnedListPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrAfterTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CreateListPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private CreateListModel createListModel;
    private ButtonGroup listTypeRadioButtonGroup = null;
    private JRadioButton dailyRb = null;
    private JRadioButton firmRb = null;
    private JRadioButton warnedRb = null;
    private JLabel selectDateError = null;
    private JLabel numOfWeeksCbLabel = null;
    private JComboBox numOfWeeksCb = null;
    private XDatePanel selectDatePanel = null;
    private DateValidationController selectDateValCon = null;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
    private ArrayList<RefCalendarBasicValue> workingDays;
    private XDialog parent;
	
    public CreateListPanel(XDialog parent, CreateListModel createListModel)throws CSRecoverableException {
    	this.parent = parent;
    	this.createListModel = createListModel;

    	jbInit();
    }
    
    @Override
	public void validationUpdatedView(ValidationController<?> validationController) {
    	boolean invalid = validationController.hasErrors() || !ValidationControllerFactory.validateComponents(validationControllers);
    	parent.setOkEnabled(!invalid);
	}

	@Override
    public void stepInitialise() throws CSRecoverableException {
    	stepUpdateViewState();
    }

    @Override
    public void stepActivate() throws CSRecoverableException {
    }

    @Override
    public void stepUpdateViewState() throws CSRecoverableException {
    }

    @Override
    public void stepValidate() throws CSValidationException, CSRecoverableException {
    	// Throw exception if failures to prevent navigation 
    	if (!ValidationControllerFactory.validateComponents(validationControllers)) {
    		throw new CSValidationException("validation.general", "Field validation failed");
    	}
    }

    @Override
    public void stepDeactivate() throws CSRecoverableException {
    }

    @Override
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    	// If the continue button has been pressed
    	if (update) {
    		Calendar startDate = getStartDate();
    		Calendar endDate = getSelectedListType().isDaily() ? startDate : getEndDate();
    		CreateListOptionsModel optionsModel = getOptionsModel(startDate.getTime(), endDate.getTime());
            // Check if we need to display the options screen
    		if (optionsModel.isOptional()) {
    			showListOptionsPanel(optionsModel);
    			optionsModel = processOptionModel(optionsModel, startDate, endDate);
    			if (optionsModel != null) {
    				showListOptionsPanel(optionsModel);
    				processOptionModel(optionsModel, startDate, endDate);
    			}
            } else {
            	// Create a new list
    			createOrOpenList(null, null, startDate, endDate);
    		}
    	}
    }

    private CreateListOptionsModel showListOptionsPanel(CreateListOptionsModel optionsModel) throws CSRecoverableException {
        // Check if we need to display the options screen
		if (optionsModel.isOptional()) {
        	CreateListOptionsDialog optionsDialog = new CreateListOptionsDialog(parent, optionsModel);
        	optionsDialog.setVisible(true);
		}
		return optionsModel;
    }

    private CreateListOptionsModel processOptionModel(CreateListOptionsModel optionsModel,final Calendar startDate,final Calendar endDate) throws CSRecoverableException {
    	// If the option screen is cancelled
    	if (CreateListOptionsModel.Action.CANCEL.equals(optionsModel.getAction())) {
			throw new UserCancelException();
			
		// If the user has chosen to open an existing list	
		} else if (CreateListOptionsModel.Action.OPEN.equals(optionsModel.getAction())) {
			createOrOpenList(optionsModel.getSelectedListId(), null, null, null);
			
		// If the user has chosen to create a new list (maybe based on a parent)	
		} else if (CreateListOptionsModel.Action.CREATE.equals(optionsModel.getAction())) {
			 try {
				createOrOpenList(null, optionsModel.getSelectedListId(), startDate, endDate);
			} catch (CSUnrecoverableException e) {	
				optionsModel = checkForNewOptionsModel(startDate, endDate);
				if (optionsModel == null) {
					throw e;
				}
				// We've got a new optionsModel so flag up that we've not finished processing
				return optionsModel;
			}
		}
    	return null;
    }

    private CreateListOptionsModel checkForNewOptionsModel(Calendar startDate, Calendar endDate) throws CSRecoverableException {
    	// Final check for a list that may have been created by another user whilst in the options screen
    	CreateListOptionsModel postOptionsModel = getOptionsModel(startDate.getTime(), endDate.getTime());
    	// Determine if a new list has been created since we tried
		boolean newListCreated = postOptionsModel.isOptional() && !postOptionsModel.isRadioButtonDisplay();
		// If a new list has been created then give the user the option to open it
		return newListCreated ? postOptionsModel : null;
    }

    private ListTypeEnum getSelectedListType() {
    	 if (firmRb.isSelected()) {
			return ListTypeEnum.Firm;
		} else if (warnedRb.isSelected()) {
			return ListTypeEnum.Warned;
		} else {
			return ListTypeEnum.Daily;
		}
    }
    
    private void createOrOpenList(Integer openListId, Integer parentListId, Calendar startDate, Calendar endDate)  throws CSRecoverableException {
    	if (getSelectedListType().isDaily()) {
    		createOrOpenDailyList(openListId, parentListId, startDate);
    	} else if (getSelectedListType().isFirm()) {
    		createOrOpenFirmList(openListId, startDate, endDate);
    	} else if (getSelectedListType().isWarned()) {
    		createOrOpenWarnedList(openListId, startDate, endDate);
    	}
    }
    
    private void createOrOpenDailyList(Integer openListId, Integer parentListId, 
    		Calendar startDate) throws CSRecoverableException {
    	final XhibitApplicationController xac = (XhibitApplicationController)getParentFrame(parent);
    	DailyListModel model = new DailyListModel(xac);
    	
    	if (openListId != null) {
			model.openList(openListId);
		} else if (parentListId != null ){
			model.createList(startDate, parentListId);
		} else {	
			model.createList(startDate);
		}
    	final DailyListPanel dailyListPanel = new DailyListPanel(parent, model);
    	dailyListPanel.showPanel();
    }
    
    private void createOrOpenFirmList(Integer openListId, Calendar startDate, Calendar endDate) throws CSRecoverableException {
    	final XhibitApplicationController xac = (XhibitApplicationController)getParentFrame(parent);
    	FirmListModel model = new FirmListModel(xac);
    	if (openListId != null) {
    		model.openList(openListId);
    	} else {
    		model.createList(startDate, endDate);
    	}	
    	FirmListPanel firmListPanel = new FirmListPanel(parent, model);
    	firmListPanel.showPanel();
    }
    
    private void createOrOpenWarnedList(Integer openListId, Calendar startDate, Calendar endDate) throws CSRecoverableException {
    	final XhibitApplicationController xac = (XhibitApplicationController)getParentFrame(parent);
    	WarnedListModel model = new WarnedListModel(xac);
    	if (openListId != null) {
    		model.openList(openListId);
    	} else {
    		model.createList(startDate, endDate);
    	}
    	WarnedListPanel warnedListPanel = new WarnedListPanel(parent, model);
    	warnedListPanel.showPanel();
    }
    
    /**
     * Retrieves a parent frame object which can be sourced from different locations based upon
     * entry into the screen
     * @return parent frame
     */
    private Frame getParentFrame(XDialog parentObj) {
    	Frame parentFrame = null;
    	if ( null != parentObj.getParent() ) {
    		// The parent object has a parent, try to access the parent frame
	    	if ( null != parentObj.getParentFrame() ) {
	    		parentFrame = parentObj.getParentFrame();
	    	}
	    	else {
	    		// Current parent frame is null, call getParentFrame to check the parent for it's parent frame
	    		parentFrame = getParentFrame(((XDialog)parentObj.getParent()));
	    	}
    	}
    	return parentFrame;
    }
    
    private Calendar getStartDate() throws CSRecoverableException {
    	return selectDatePanel.getDate();
    }
    
    private Calendar getEndDate() throws CSRecoverableException {
    	// Get the number of Weeks required
    	Integer noOfWks = Integer.valueOf((String) getNumOfWeeks().getSelectedItem());
    	// Using the start date, get the next available working week
    	Calendar endDate = getNextWorkingWeek(getStartDate(), noOfWks);
    	// Set the date to the last day of the week (in java its Saturday)
    	endDate = DateTimeUtilities.getDateOnDayOfWeek(endDate, Calendar.SATURDAY);
    	// Work backwards to find the last actual working day
    	endDate = getNextWorkingDate(endDate, -1);
    	return endDate;
    }
    
	private CreateListOptionsModel getOptionsModel(Date startDate, Date endDate) throws CSRecoverableException {
    	CreateListOptionsValue optionsValue = XhibitDelegateHelper.getListingsDelegate().
    			findCreateListOptions(XhibitSingleton.getInstance().getCourtId(), getSelectedListType().toString(), startDate, endDate);
    	 
    	 CreateListOptionsModel optionsModel = new CreateListOptionsModel(getSelectedListType(), startDate, endDate);
    	 optionsModel.setPreviousDailyList(optionsValue.getPreviousDailyList());
    	 optionsModel.setPreviousFirmList(optionsValue.getPreviousFirmList());
    	 optionsModel.setPreviousWarnList(optionsValue.getPreviousWarnList());    	 
    	 return optionsModel;
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
		
    	JPanel listTypePanel = initRadioButtonPanel();
    	mainPanel.add(listTypePanel, gbc);

    	gbc.gridy++;
    	mainPanel.add(initDatePanel(), gbc);
    	
	    // initialise Num of Weeks Visible flag
	    setNumOfWeeksVisible();

	    // ensure tab to cancel triggers validation
	    parent.setCancelVerifyInputWhenFocusTarget(true);
    }

    private JPanel initDatePanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setBorder(BorderFactory.createTitledBorder(getResourceBundle("createListDate")));
    	panel.setLayout(new GridBagLayout());
    	
    	gbc.insets = XHIBITConstant.errorLabelInsets;
        panel.add(getSelectDateError(), gbc);
    	gbc.insets = XHIBITConstant.nonContainerInsets;
    	
    	gbc.gridy++;
    	panel.add(getSelectDatePanel(), gbc);
	    
	    gbc.gridx++;
	    panel.add(getNumOfWeeksCbLabel(), gbc);
	    
	    gbc.gridx++;
	    panel.add(getNumOfWeeks(), gbc);
	    return panel;
    }
    
    private JPanel initRadioButtonPanel() {
    	GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel listTypePanel = new JPanel();
    	listTypePanel.setBorder(BorderFactory.createTitledBorder(getResourceBundle("createListType")));
    	listTypePanel.setLayout(new GridBagLayout());
    	listTypePanel.setPreferredSize(new Dimension(350, 60));

    	ActionListener radioButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNumOfWeeksVisible();
			}
		};
    	getListTypeRadioButtonGroup().add(getDailyRb());
    	getListTypeRadioButtonGroup().add(getFirmRb());
    	getListTypeRadioButtonGroup().add(getWarnedRb());
    	getDailyRb().addActionListener(radioButtonActionListener);
    	getFirmRb().addActionListener(radioButtonActionListener);
    	getWarnedRb().addActionListener(radioButtonActionListener);
    	getDailyRb().setSelected(true);

        listTypePanel.add(getDailyRb(), gbc);
    	
    	gbc.gridx++;
    	listTypePanel.add(getFirmRb(), gbc);
    	
    	gbc.gridx++;
    	listTypePanel.add(getWarnedRb(), gbc);
    	
    	return listTypePanel;
    }

    private void setNumOfWeeksVisible() {
    	boolean visible = firmRb.isSelected()  || warnedRb.isSelected();
    	getNumOfWeeksCbLabel().setVisible(visible);
    	getNumOfWeeks().setVisible(visible);
    }
    
    private ButtonGroup getListTypeRadioButtonGroup() {
    	if (listTypeRadioButtonGroup == null) {
    		listTypeRadioButtonGroup = new ButtonGroup();
    	}
    	return listTypeRadioButtonGroup;
    }

    private JRadioButton getDailyRb() {
    	if (dailyRb == null) {
    		dailyRb = new JRadioButton(getResourceBundle("createListDaily"));
    	}
    	return dailyRb;
    }

    private JRadioButton getFirmRb() {
    	if (firmRb == null) {
    		firmRb = new JRadioButton(getResourceBundle("createListFirm"));
    	}
    	return firmRb;
    }
    
    private JRadioButton getWarnedRb() {
    	if (warnedRb == null) {
    		warnedRb = new JRadioButton(getResourceBundle("createListWarned"));
    	}
    	return warnedRb;
    }  
    		
    private JLabel getSelectDateError() {
    	if (selectDateError == null) {
    		selectDateError = new JLabel(" ");
    	}
    	return selectDateError;
    }
        
    public XDatePanel getSelectDatePanel() {
        if (selectDatePanel == null) {	
        	Calendar nextWorkingDate = getNextWorkingDate(getTomorrowsDate(), 1);
            selectDatePanel = new XDatePanel(this, nextWorkingDate, true);
            selectDateValCon = ValidationControllerFactory.createDateRequired(this,
            		selectDatePanel, getSelectDateError(),
            		new DateEqualOrAfterTodayValidator(),
            		new AbstractDateValidator() {
		            	@Override
		            	public void validate(XDatePanel target, List<String> errors) {
		            		if (hasDate(target) && !ListingDropdownPopulation.isWorkingDay(getWorkingDays(), getDate(target))){
	            				errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.validation.notWorkingDay"));
		            		}
		            	}
		            });
            validationControllers.add(selectDateValCon);
        }
        return selectDatePanel;
    }
    
    private Calendar getTomorrowsDate() {
    	// Starting date is tomorrow with no time so comparisons will work
    	Calendar date = DateTimeUtilities.stripTimeToCalendar(new Date());
    	date.add(Calendar.DATE, 1);
    	return date;
    }
    
    private Calendar getNextWorkingDate(Calendar startDate, int increment) {
    	// Cycle through future dates until found working day
    	Calendar nextWorkingDate = startDate;
    	boolean isWorkingDay = ListingDropdownPopulation.isWorkingDay(getWorkingDays(), nextWorkingDate);
    	while (!isWorkingDay) {
    		nextWorkingDate.add(Calendar.DATE, increment);
    		isWorkingDay = ListingDropdownPopulation.isWorkingDay(getWorkingDays(), nextWorkingDate);
        }
        return nextWorkingDate;
    }

    private Calendar getNextWorkingWeek(Calendar startDate, int workingWeeksReq) {
    	Calendar nextWorkingDate = startDate;
    	boolean isWorkingDay = ListingDropdownPopulation.isWorkingDay(getWorkingDays(), nextWorkingDate);
    	int actualWorkingWeeks = 1;
    	int weekNo = getWeekNoFromDate(nextWorkingDate);   
    	
    	// Loop through the working days until we get to the correct week
    	while (!(isWorkingDay && actualWorkingWeeks >= workingWeeksReq)) {
    		nextWorkingDate.add(Calendar.DATE, 1);
    		isWorkingDay = ListingDropdownPopulation.isWorkingDay(getWorkingDays(), nextWorkingDate);
    		// If the week number has changed then increment the actual working weeks
    		if (isWorkingDay && weekNo != getWeekNoFromDate(nextWorkingDate)) {
    			weekNo = getWeekNoFromDate(nextWorkingDate);
    			actualWorkingWeeks++;
    		}
    	}
    	return nextWorkingDate;
    }
    
    private int getWeekNoFromDate(Calendar date) {
    	return date.get(Calendar.WEEK_OF_YEAR);
    }
    
    private ArrayList<RefCalendarBasicValue> getWorkingDays() {
    	if (workingDays == null) {
    		workingDays = ListingDropdownPopulation.getRefCalendar(DateTimeUtilities.stripTimeToCalendar(new Date()));       		
    	}
    	return workingDays;
    }
    
    private JLabel getNumOfWeeksCbLabel() {
    	if (numOfWeeksCbLabel == null) {
    		numOfWeeksCbLabel = new JLabel();
    		numOfWeeksCbLabel.setText(getResourceBundle("createListNumberOfWeeks"));

    	}
    	return numOfWeeksCbLabel;
    }
        
    public JComboBox getNumOfWeeks() {
        if (numOfWeeksCb == null) {
        	String[] NumOfWeeksList = {"1", "2", "3", "4"};
            numOfWeeksCb = new JComboBox(NumOfWeeksList);
        }
        return numOfWeeksCb;
    }
    
	protected String getResourceBundle(String key) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, key);
	}
}
