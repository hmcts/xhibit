package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
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
 * @author J Parthiban
 * 
 */
public class IssueBWCourtLogPanel extends CourtLogEventPanel {
  
	private static final Dimension labelDim = new Dimension(75, XHIBITConstant.getLineHeight());
	 
    protected final MediumEventMLModel model;

    private Dimension cbDim;
    
    private BwHistoryControllerBeanBusinessDelegate bwHistoryDelegate;
 
    private int cbSize = 0;
    
    private Vector pullDownList01 = new Vector();
    
    private JComboBox combo = null;
    
    private JLabel optionCbLabel;
    

    public IssueBWCourtLogPanel(final XDialog parent, final MediumEventMLModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }
    
    
    public void stepInitialise() throws CSRecoverableException {
        Vector meTypes = (Vector) RestrictionFinder.getRestrictingValues(model.getEventType() + ".xsd", model
                .getSubSchema());
        pullDownList01.add(new PullDownListObject(0, "select", ResourceBundleHelper.getResource(
                XhibitBundles.SimpleEvent, "select")));

        // Populate pullDownList01 from xsd
        for (int x = 0; x < meTypes.size(); x++) {
            String name = (String) meTypes.get(x);
            log.debug("name: " + name);
            String value = ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, name);

            pullDownList01.add(new PullDownListObject(x + 1, name, value));
        }
 
        super.stepInitialise();
    }

    private void jbInit() {
        double minCbSize = cbSize * 6.5;
        minCbSize = (minCbSize >= 260 ? minCbSize : 260);
        cbDim = new Dimension((int) minCbSize, 21);
        
        this.add(getPanelTitle(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getDefendantPanel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getOptionCbLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getOptionCb(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 20));
    }
   
    private JPanel getDefendantPanel() {
 
        final JLabel label = new JLabel(XHIBITConstant.getResource(XhibitBundles.BailCustody, "lblBCDefendants"), JLabel.LEFT);
        final JLabel labelValue = new JLabel(model.getDefendantName(), JLabel.RIGHT);
          
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        panel.add(labelValue);
        return panel;
    }
    
    private JLabel getOptionCbLabel() {
        if (optionCbLabel == null) {
            optionCbLabel = new JLabel();
            optionCbLabel.setMinimumSize(labelDim);
            optionCbLabel.setPreferredSize(labelDim);
            optionCbLabel
                    .setText(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent, "MediumEventMLOptionLbl"));
        }
        return optionCbLabel;
    }
    
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
 
    	bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();	
		BwHistoryBasicValue bwBV = new BwHistoryBasicValue();
    	
		
        if (update) {
     		 CourtLogCRUDValue crudArray = createCRUDFromModel();
    		 getCLCDelegate().newEntry(crudArray);
    	 
    	 	 bwBV.setAbsconding(null);
             bwBV.setObsInd(null);
             bwBV.setBcStatusBwEnded(null);
             bwBV.setBwEndDate(null);
             bwBV.setDefendantOnCaseId(model.getDefendantOnCaseId());
             bwBV.setBwIssueDate(model.getDateTime().getTime());
             bwBV.setBcStatusBwIssued(model.getDefOnCase().getCurrentBcStatus());
             
             if (model.getDefOnCase() != null) { 
	             log.debug("Issue BW, save to DB: DefOnCaseId: " + model.getDefendantOnCaseId() + 
	            		 					   ", BcStatus: " + model.getDefOnCase().getCurrentBcStatus());
             } else {
            	 log.debug("Issue BW, save to DB: DefOnCaseId: " + model.getDefendantOnCaseId() + 
	 					   ", BcStatus: null");
             }
             
         	 int bwId = bwHistoryDelegate.createBwHistory(bwBV, model.getDefendantOnCaseId(),
         			XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
  	    
         	 log.debug("Issue BW, save to DB: Created Bw ID: " + bwId);
        }
         
    }
    
    protected boolean isMandatoryFieldsCompleted() {
        if (model.isSelectionRequired()) {
            return getOptionCb().getSelectedIndex() > 0;
        }

        return true;
    }
       
    protected void moveModelToScreen() {
        super.moveModelToScreen();

        if (model.isInEditMode()) {
            getOptionCb().setSelectedIndex(model.getSelectedIndex());
        }
    }
    
    private JComboBox getOptionCb() {
        
        if (combo == null) {
        	combo = new JComboBox(pullDownList01);
        	combo.setMinimumSize(cbDim);
        	combo.setPreferredSize(cbDim);
        	combo.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.SimpleEvent,
                    "MediumEventMLOptionToolTip"));
         	combo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return combo;
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        HashMap hashMap = (HashMap) propertyMap.get(model.getSchema());
        if (hashMap != null) {
            model.setSelectedItemCode(((String) hashMap.get(model.getSubSchema())));
            model.setSelectedIndex(findSelectedEntry(pullDownList01, model.getSelectedItemCode()));
        }
    }
    
    private int findSelectedEntry(Vector itemList, String code) {
        int returnCode = 0;

        for (int x = 0; x < itemList.size(); x++) {
            PullDownListObject pdlo = (PullDownListObject) itemList.get(x);

            if (pdlo.getCode().equalsIgnoreCase(code))
                returnCode = x;
        }

        return returnCode;
    }
    
    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        if (model.isSelectionRequired() || getOptionCb().getSelectedIndex() > 0) {
            HashMap meMLOptionsType = new HashMap();
            meMLOptionsType.put(model.getSubSchema(), model.getSelectedItemCode());
            propertyMap.put(model.getSchema(), meMLOptionsType);
        }
    }
  
    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();

        PullDownListObject plo = (PullDownListObject) pullDownList01.get(getOptionCb().getSelectedIndex());
        model.setSelectedItem(plo.toString());
        model.setSelectedIndex(plo.getId());
        model.setSelectedItemCode(plo.getCode());
    }

}
