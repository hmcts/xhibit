package uk.gov.courtservice.xhibit.client.courtlog;


import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.bwhistory.BwHistoryControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.BwHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XFrame;
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
 * @author Deepak Rath
 * 
 */
public class EndBWEventPanel extends CourtLogEventPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final EndBWMediumEventMLModel model;

	private int cbSize = 0;
	
	private XDialog parent = null;

	
	private BwHistoryControllerBeanBusinessDelegate bwHistoryDelegate;
 	 
	public EndBWEventPanel(final XDialog parent, final EndBWMediumEventMLModel model) throws CSRecoverableException {
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

		if (outStandingBWCheck(getCourtLogEventLevelPanel().findDefOnCase(model.getDefendantId()).getId())) {	
				
			String name = dbv.getFirstName() + dbv.getMiddleName() + dbv.getSurname();
			
			if (name != null) {
				name = name.replace("null", "");
			}
		 
			model.setDefendantName(name);
 
			model.setDefendantOnCaseId(getCourtLogEventLevelPanel().findDefOnCase(model.getDefendantId()).getId());
			new EndBWCourtLogDialog(this.getParentFrame(), model);
			
		} 
		else 
		{
			JOptionPane.showMessageDialog(new XFrame(), XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.benchwarrant.cannotbeended"), "No Bench Warrant exists", JOptionPane.ERROR_MESSAGE);
			throw new UserCancelException();
		}
 	}
	
	private boolean outStandingBWCheck(Integer defOnCaseId) throws DefendantControllerException, BwHistoryControllerException {
 
		bwHistoryDelegate = XhibitDelegateHelper.getBwHistoryDelegate();	
		 		
		// Return any entries in XHB_BW_HISTORY where an outstanding bench warrant exists for the given def on case
		ArrayList<BwHistoryBasicValue> bwHistoryColl = (ArrayList<BwHistoryBasicValue>) bwHistoryDelegate.findOutstandingBenchWarrantsForDefOnCaseId(defOnCaseId);
		if(bwHistoryColl != null && !bwHistoryColl.isEmpty()) {	
			log.debug("End BW, outStandingBWCheck: Returned Entries No.: " + bwHistoryColl.size()
		    								  + ", Def On Case Id: " + defOnCaseId);
			
			model.setbWHistoryId(bwHistoryColl.get(0).getId());
			model.setVersion(bwHistoryColl.get(0).getVersion());
			
	 		return true;
	 		
 		} else {
 			
 			return false;
		}
	 
	}


	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		
	}

}
