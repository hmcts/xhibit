package uk.gov.courtservice.xhibit.client.results.PRLIS;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import sun.swing.DefaultLookup;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_pub_running_list.XhbPubRunningListBasicValue;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XRadioButton;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDate;
import uk.gov.courtservice.xhibit.common.results.vos.LFIXRunDateValue;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISCaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.PRLISReport;

public class PRLISReportPanel extends XPanel {

	private static final long serialVersionUID = 1L;
	private XComboBox publishedRunningLists;
	private JLabel errorLabel = new JLabel("   ");
	private PublishPRLISAction publishAction = new PublishPRLISAction();
	
	public PRLISReportPanel(){
		jbInit();
	}
	
	private void jbInit() {
		try {
			LFIXRunDate lastRunDate = XhibitDelegateHelper.getResults2Delegate().getReportRunDate(XhibitSingleton.getInstance().getCourtId(), XHIBITConstant.getResource(XhibitBundles.XhibitActionResources, "PRLISReportActionName"));
			String displayRunDateFormatted = "";
			
			LFIXRunDateValue displayRunDate = lastRunDate.getLfixRunDateValues().get(0);
			
			if (displayRunDate != null) 
				displayRunDateFormatted = formatDateWithNewFormat(displayRunDate.getRundate());
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
	
			JLabel descriptonLabel = new JLabel();
			descriptonLabel.setText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.dialog_text"));
			this.add(descriptonLabel, gbc);
			
			ButtonGroup radioButtonGroup = new ButtonGroup();
			
			XRadioButton showAllCasesRadioButton = new XRadioButton();
			showAllCasesRadioButton.setText(String.format(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.show_all_cases"), displayRunDateFormatted));
			gbc.gridy++;
			this.add(showAllCasesRadioButton, gbc);
			
			XRadioButton showPreviousRadioButton = new XRadioButton();
			showPreviousRadioButton.setText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.show_previous_report"));
			gbc.gridy++;
			
			this.add(showPreviousRadioButton, gbc);
			
			radioButtonGroup.add(showAllCasesRadioButton);
			radioButtonGroup.add(showPreviousRadioButton);
			radioButtonGroup.setSelected(showAllCasesRadioButton.getModel(), true);
			
			publishedRunningLists = new XComboBox();
			publishedRunningLists.setRenderer(new PublishedListComboBoxRenderer());
			publishedRunningLists.setEnabled(false);
			
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 1.5;
			this.add(publishedRunningLists, gbc);
			
			showPreviousRadioButton.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
						publishedRunningLists.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
						publishedRunningLists.setSelectedIndex(-1);
						errorLabel.setText("   ");
				}
			});
			
			gbc.gridy++;
			gbc.weighty = 0;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.WEST;
			this.errorLabel.setForeground(Color.RED);
			this.add(errorLabel, gbc);
			
		} catch (Exception e) {
			Message newUserMessage = new Message("sysadmin.bisref.finderexception");
			throw new CSUnrecoverableException(newUserMessage, e, "Error whilst retrieving last run date of PRLIS report");
		}
	}

	private void moveModelToScreen() {
		try {
			publishedRunningLists.removeAllItems();
			XhbPubRunningListBasicValue[] previouslyRunListsValues = XhibitDelegateHelper.getResults2Delegate().getPreviouslyPublishedRunningLists(XhibitSingleton.getInstance().getCourtId());
			for(int i = 0; i < previouslyRunListsValues.length && i < 6; i++){
				XhbPubRunningListBasicValue value = previouslyRunListsValues[i];
				publishedRunningLists.addItem(value);
			}
			publishedRunningLists.setSelectedIndex(-1);
		} catch (ResultsControllerException e) {
			Message newUserMessage = new Message("sysadmin.bisref.finderexception");
			throw new CSUnrecoverableException(newUserMessage, e, "Error whilst retrieving previously published Running Lists");
		}
	}
	
	public XComboBox getPublishedListComboBox(){
		return this.publishedRunningLists;
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
	}

	@Override
	public void stepActivate() throws CSRecoverableException { 
		moveModelToScreen();
		stepUpdateViewState();
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {

	}
	
	private class PublishedListComboBoxRenderer extends DefaultListCellRenderer
	{

		private static final long serialVersionUID = 1L;
		String title = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.published_lists");
		
		private Border getNoFocusBorder() {
	        Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
	        if (System.getSecurityManager() != null) {
	            if (border != null) return border;
	            return new EmptyBorder(1, 1, 1, 1);
	        } else {
	            if (border != null &&
	                    (noFocusBorder == null ||
	                    noFocusBorder == new EmptyBorder(1, 1, 1, 1))) {
	                return border;
	            }
	            return noFocusBorder;
	        }
	    }
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			setComponentOrientation(list.getComponentOrientation());

	        Color bg = null;
	        Color fg = null;

	        JList.DropLocation dropLocation = list.getDropLocation();
	        if (dropLocation != null
	                && !dropLocation.isInsert()
	                && dropLocation.getIndex() == index) {

	            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
	            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

	            isSelected = true;
	        }

			if (isSelected) {
		            setBackground(bg == null ? list.getSelectionBackground() : bg);
			    setForeground(fg == null ? list.getSelectionForeground() : fg);
			}
			else {
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
			
			if(index == -1 && value == null){
				setText(title);
			}else{
				XhbPubRunningListBasicValue pubRunningList = (XhbPubRunningListBasicValue) value;
				setText(XDateFormat.format(pubRunningList.getPublishedDate(), XDateFormat.DATETIMEFORMAT));
			}
			
			setEnabled(list.isEnabled());
			setFont(list.getFont());
		        
		        Border border = null;
		        if (cellHasFocus) {
		            if (isSelected) {
		                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
		            }
		            if (border == null) {
		                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
		            }
		        } else {
		            border = getNoFocusBorder();
		        }
			setBorder(border);
			
			return this;
		}
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
	}

	public JLabel getMandatoryErrorMessage() {
		return this.errorLabel;
	}
	
	public PublishPRLISAction getPublishAction() {
		return publishAction;
	}

	public class PublishPRLISAction extends XAction {

		/**
		 * @param reportList the reportList to set
		 */
		public void setReportList(PRLISReport reportList) {
			this.reportList = reportList;
		}

		/**
		 * @param parent the parent to set
		 */
		public void setParent(XDialog parent) {
			this.parent = parent;
		}

		private static final long serialVersionUID = 1L;
		private PRLISReport reportList;
		private XDialog parent;
		
		public PublishPRLISAction() {
			populateFromBundle("PRLISReportPublish"); 
		}
		

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
	       	ArrayList<Integer> completeReportedCases = new ArrayList<Integer>();
	    	for(int i = 0; i < reportList.getPrlisCaseValues().size(); i++)
	    	{
	    		PRLISCaseValue cv =  (PRLISCaseValue)reportList.getPrlisCaseValues().get(i);
	    		completeReportedCases.add(cv.getCaseId());
	    	}
	    	Integer[] completedReportedCasesArray = new Integer[completeReportedCases.size()];
			XhibitDelegateHelper.getResults2Delegate().publishRunningList(XhibitSingleton.getInstance().getCourtId(), completeReportedCases.toArray(completedReportedCasesArray));
			
			JMenuItem publishButton = (JMenuItem) e.getSource();
			publishButton.setEnabled(false);
			
			String title = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.publish_successful_title");
			String message = String.format(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "PRLISReport.publish_successful"), XDateFormat.format(new Date(),XDateFormat.DATETIMEFORMAT));
	    	XMessageBox.alert(parent, title, true, XMessageBox.ICONINFORMATION, message, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	    	moveModelToScreen();
		}
	}
	
	private String formatDateWithNewFormat(String date) throws CSRecoverableException {
		try {
			String dateParts[] = date.split("-");
			if (dateParts != null) {
				Date newDate = new SimpleDateFormat("MMM",Locale.ENGLISH).parse(dateParts[1]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(newDate);
				int month = cal.get(Calendar.MONTH)+1;
				return dateParts[0]+"/"+month+"/"+dateParts[2];
			}
			return date;
		} catch (ParseException e) {
			throw new CSRecoverableException(e);
		}
	}
}

