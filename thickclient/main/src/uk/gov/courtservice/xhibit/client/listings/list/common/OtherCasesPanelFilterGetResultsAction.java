package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseFilterResultComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseListingFilterCriteria;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class OtherCasesPanelFilterGetResultsAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    
	    private OtherCasesPanelFilterSelectionModel model;
	        
	    public OtherCasesPanelFilterGetResultsAction(OtherCasesPanelFilterSelectionModel model){
	        this.model = model;  
	    }
	    
		@Override
	    public void xActionPerformed(ActionEvent e) throws Exception {
			CaseListingFilterCriteria criteria = new CaseListingFilterCriteria();
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId());
			criteria.setCaseType(model.getCaseType() != null ? model.getCaseType(): null);
			criteria.setCaseClass(model.getCaseClass() != null ? model.getCaseClass():null);
			criteria.setBcStatus(model.getBcStatus() != null ? model.getBcStatus() :null);
			criteria.setHearingTypeCode(model.getDefaultHearingType() != null ? model.getDefaultHearingType().getHearingTypeCode() : null);
			criteria.setTimeEstFrom(model.getTimeFrom() !=null ? model.getTimeFrom() : null);
			criteria.setTimeEstTo(model.getTimeTo() !=null ? model.getTimeTo() : null);
			criteria.setUnits(model.getTimeUnits() !=null ? model.getTimeUnits() : null);
			criteria.setRefJudgeTypeId(model.getRequiredJudgeType() != null ? model.getRequiredJudgeType().getId() : null);
			criteria.setUnitsWeeks(model.getTimeEstWeeks() != null ? model.getTimeEstWeeks() : null);
			criteria.setSecureCourtRoom(model.getSecureCourtroom() != null ? model.getSecureCourtroom() : null);
			criteria.setJuvenileOnly(model.getJuvenileOnly() != null ? model.getJuvenileOnly() : null);
			
			//Call Midtier to get result set
			@SuppressWarnings("unchecked")
			List<CaseFilterResultComplexValue> caseFilterResults = 
	    		   XhibitDelegateHelper.getListingsDelegate().getCasesByFilter(criteria);

			this.model.setCaseFilterResults(caseFilterResults);
	    } 	
	}
