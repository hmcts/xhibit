
package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.bwhistory.BwHistoryValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author D Rath
 * @version 1.0
 */

public class EndBWCourtLogPanel extends CourtLogEventPanel{
	
	 private static final String END_BW_ENDDATE = "E20101_END_DATE";

	protected final EndBWMediumEventMLModel model;

	    private Dimension cbDim1;
	 
	    private int cbSize = 0;
	    
	    private XDatePanel datePanel = null;
	    
	    private GridBagConstraints gbc;
	    
	    private BwHistoryControllerBeanBusinessDelegate bwHistoryDelegate;	    
 		 
	    private Vector pullDownList01 = new Vector();
	    
	    private JComboBox combo = null;

	    public EndBWCourtLogPanel(final XDialog parent, final EndBWMediumEventMLModel model) throws CSRecoverableException {
	        super(parent, model);

	        this.model = model;

	        stepInitialise();
	        jbInit();
	        stepActivate();
	    }

	    private void jbInit() {
	        double minCbSize = cbSize * 6.5;
	        minCbSize = (minCbSize >= 350 ? minCbSize : 350);
	        cbDim1 = new Dimension((int) minCbSize, 5);
	     
	        GridBagConstraints gb = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
	                GridBagConstraints.HORIZONTAL,XHIBITConstant.nonContainerInsets, 0, 20);
	        
	       
	        
	        this.add(getEndBWPanel(), gb);
	        
	        gb.gridy++;
	        
	        this.add(getLogAuditPanel(), gb);
	    }
	    
	    public JPanel getEndBWPanel(){
	    	
	    	JPanel panel = new JPanel(new FlowLayout());
	        panel.setLayout(new GridBagLayout());
	        
	         gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
	                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20);
	         
	       
	        panel.add(getDefendantPanelLabel(),gbc);
	        gbc.gridx++;
	        panel.add(getDefendantPanelLabelValue(),gbc);
	        gbc.gridy++;
	        
	        gbc.gridx--;
	        panel.add(getDatePanelLabel(),gbc);
	        gbc.gridx++;
	        panel.add(getDatePanel(),gbc);
	        gbc.gridy++;
	        
	        gbc.gridx--;
	        panel.add( getOptionsPanelLabel(),gbc);
	        gbc.gridx++;
	        panel.add( getOptionsPanel(),gbc);
	        gbc.gridy++;
	    	
	        return panel;
	    }
	    
	    private JPanel getDatePanel() {
	    	
	        if (datePanel == null) {
	            datePanel = new XDatePanel(this);
	            datePanel.getDateComponent().setEditable(true);
	        }
	        
	        datePanel.setDate(Calendar.getInstance().getTime());	   
	       
	        return datePanel;
	    }	    
	        
	 
	    private JLabel getDatePanelLabel(){
	    	return   new JLabel(XHIBITConstant.getResource(XhibitBundles.BailCustody, "lblBCEndDate"), JLabel.LEFT);
	    	
	    }
	    private JLabel getDefendantPanelLabelValue() {
	    	return new JLabel(model.getDefendantName(), JLabel.LEFT);
	    }
	    
	    private JLabel getDefendantPanelLabel() {
	    	return  new JLabel(XHIBITConstant.getResource(XhibitBundles.BailCustody, "lblBCDefendants"), JLabel.LEFT);
	    }
	    
	    private JLabel getOptionsPanelLabel() {
	    	return new JLabel(XHIBITConstant.getResource(XhibitBundles.BailCustody, "lblBCOption"), JLabel.LEFT);
	    }
	    
	    @SuppressWarnings("unchecked")
		private JComboBox getOptionsPanel() { 
	        
	        Vector meTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", model.getSubSchema());
	        pullDownList01.add(new PullDownListObject(0, "select", ResourceBundleHelper.getResource(
	                    XhibitBundles.SimpleEvent, "select")));

	            // Populate pullDownList01 from xsd
	            for (int x = 0; x < meTypes.size(); x++) {
	                String name = (String) meTypes.get(x);
	                log.debug("name: " + name);
	                String value = ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, name);

	                pullDownList01.add(new PullDownListObject(x + 1, name, value));
	            }
	        
	        return getOptionCb();
	    }
	    
	    private JComboBox getOptionCb() {
	    
	    	if (combo == null) {
	        	combo = new JComboBox(pullDownList01);
	        	combo.setMinimumSize(cbDim1);
	        	combo.setPreferredSize(cbDim1);
	         	combo.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
 	                    stepUpdateViewStateHandleExceptions();
	                }
	            });
	        }
	        return combo;
	    }
	    
	    protected boolean isMandatoryFieldsCompleted() {
	        if (model.isSelectionRequired()) {
	        	return getOptionCb().getSelectedIndex() > 0;
	        }

	        return true;
	    }
	    
	    @SuppressWarnings("unchecked")
		protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
	    	HashMap endBwMap = new HashMap();	    	  
	    	endBwMap.put(END_BW_ENDDATE, model.getEndDate().getTime());
	    	
	        if (model.isSelectionRequired() || getOptionCb().getSelectedIndex() > 0) {	            
	            endBwMap.put(model.getSubSchema(), model.getSelectedItemCode());	         
	        }
	        propertyMap.put(model.getSchema(), endBwMap);
	    }
	    
	    @Override
		public void stepValidate() throws CSValidationException, CSRecoverableException {	    	
	    	// Validation of End Date
	    	Date issueDate = getIssueDate();
	    	Date endDate=datePanel.getDate().getTime();
	    	
	    	if(issueDate ==null || endDate ==null){
	    		JOptionPane.showMessageDialog(new JFrame(), XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.benchwarrant.invalidissueenddates"), "Invalid End Date or Issue Date", JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
	    	}
	    	
	    	if(!validateEndDate(endDate, issueDate)){
	    		JOptionPane.showMessageDialog(new JFrame(), XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.benchwarrant.invalidenddate"), "Invalid End Date", JOptionPane.ERROR_MESSAGE);
				throw new UserCancelException();
	    	}	    	

	    }
	    
	    private boolean validateEndDate(Date endDate, Date issueDate){
	    	Calendar endDateCal = Calendar.getInstance();
	    	endDateCal.setTime(endDate);
	    	endDateCal.set(Calendar.HOUR_OF_DAY,23);
	    	endDateCal.set(Calendar.MINUTE, 59);
	    	endDateCal.set(Calendar.SECOND, 59);
	    	endDateCal.set(Calendar.MILLISECOND, 999);
	    	
	    	if(!endDateCal.getTime().before(issueDate) && !endDate.after(Calendar.getInstance().getTime())){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    @SuppressWarnings("unchecked")
		private Date getIssueDate() throws BwHistoryControllerException{
	    	
	    	bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();	
	    	ArrayList<BwHistoryBasicValue> bwHistoryColl = (ArrayList<BwHistoryBasicValue>) bwHistoryDelegate.findOutstandingBenchWarrantsForDefOnCaseId(model.getDefendantOnCaseId());
	    	if(bwHistoryColl != null && !bwHistoryColl.isEmpty()) {	
	    		return bwHistoryColl.get(0).getBwIssueDate();
	    	}
			return null;
	    	
	    
		}
	    
	    public void stepDeinitialise(boolean update) throws CSRecoverableException {
	    	 
	    	bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();	
			BwHistoryValue bwBV = new BwHistoryValue();			
	    	
	        if (update) {
	        
	 		          CourtLogCRUDValue crudArray = new CourtLogCRUDValue();	        	    	  
		                         
		              bwBV.setBwEndDate(model.getEndDate().getTime());		              
		             
	                  if(model.getSelectedItem().contains("Withdrawn")){
	                	  // Bench Warrant Withdrawn
	                	  bwBV.setWithdrawn("Y");
	                  }
	                  else {
	                	  // Bench Warrant Executed
	                	  // Take anything after "Bench Warrant Executed - Absconding - " in the option
	                	  String absonding = model.getSelectedItem().substring( 
	                			  "Bench Warrant Executed - Absconding - ".length());
	                	  bwBV.setAbsconding(absonding);
	                  }
		              crudArray = createCRUDFromModel();	              
		            
		              bwBV.setBwHistoryId(model.getbWHistoryId());
 		              bwBV.setVersion(model.getVersion());
		              bwHistoryDelegate.updateBwHistory(bwBV,
		            		  XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			         
	                  getCLCDelegate().newEntry(crudArray);
	        }
	        
	    }
	    
	    	    
	    protected void moveScreenToModel() throws CSRecoverableException {
	        super.moveScreenToModel();

	        PullDownListObject plo = (PullDownListObject) pullDownList01.get(getOptionCb().getSelectedIndex());
	        model.setSelectedItem(plo.toString());
	        model.setSelectedIndex(plo.getId());
	        model.setSelectedItemCode(plo.getCode());
	        model.setEndDate(datePanel.getDate());
	    }
	    
}
