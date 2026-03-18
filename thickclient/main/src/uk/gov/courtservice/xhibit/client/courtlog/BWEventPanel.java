package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.bwhistory.BwHistoryValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

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
public class BWEventPanel extends CourtLogEventPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 	 
	protected final MediumEventMLModel model;

	private int cbSize = 0;
	
	private XDialog parent = null;

	private BwHistoryControllerBeanBusinessDelegate bwHistoryDelegate;
	 	 
	
	public BWEventPanel(final XDialog parent, final MediumEventMLModel model) throws CSRecoverableException {
		super(parent, model);
		this.model = model;
		this.parent = parent;
   		jbInit();
  	}

	private void jbInit() {
		double minCbSize = cbSize * 6.5;
		minCbSize = (minCbSize >= 260 ? minCbSize : 260);
 
 		this.add(getCourtLogEventLevelPanel(), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
  	}
 
    
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
  
		DefendantBasicValue dbv = (DefendantBasicValue)this.getCourtLogEventLevelPanel().getDefendantCombo().getSelectedItem();
		 
		model.setDefendantId(dbv.getId());
		
		DefendantOnCaseBasicValue defOnCaseBV = getCourtLogEventLevelPanel().findDefOnCase(model.getDefendantId());
 		 
		DefendantOnCaseValue defOnCaseValue = XhibitDelegateHelper.getDefendantDelegate().getDefendantOnCaseDetails(model.getDefendantId(), defOnCaseBV.getCaseID());
	 	Integer defOnCaseId = defOnCaseValue.getDefendantOnCaseBVO().getDefendantOnCaseId();
	 	
		if (outStandingBWCheck(defOnCaseId)) {	
			JOptionPane.showMessageDialog(new JFrame(), XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.benchwarrant.cannotbeissued"), "Bench Warrant cannot be issued", JOptionPane.ERROR_MESSAGE);
			throw new UserCancelException();
		} 
 		else {
 			
 			String name = dbv.getFirstName() + dbv.getMiddleName() + dbv.getSurname();
 			
 			if (name != null) {
				name = name.replace("null", "");
			}
		 
			model.setDefendantName(name);
 			model.setDefendantOnCaseId(defOnCaseId);
			model.setDefOnCase(defOnCaseValue.getDefendantOnCaseBVO());
 			new IssueBWCourtLogDialog(this.getParentFrame(), model);
		}
   	}
	
 
	private boolean outStandingBWCheck(Integer defOnCaseId) throws DefendantControllerException, BwHistoryControllerException {
	
  		bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();	 
 		// Return any entries in XHB_BW_HISTORY where an outstanding bench warrant exists for the given def on case
		ArrayList<BwHistoryValue> bwHistoryColl = (ArrayList<BwHistoryValue>) bwHistoryDelegate.findOutstandingBenchWarrantsForDefOnCaseId(defOnCaseId);
 		if(bwHistoryColl != null && !bwHistoryColl.isEmpty()) {	// if any entries found then show pop-up
 			log.debug("Issue BW, outStandingBWCheck: Returned Entries No.: " + bwHistoryColl.size() + ", Def On Case Id: " + defOnCaseId);
			return true;
		} else {
 			return false;
		}
	}
	

	public void stepDeinitialise(boolean update) throws CSRecoverableException {
 	}

}
