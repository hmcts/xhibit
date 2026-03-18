package uk.gov.courtservice.xhibit.client.admin.courtofappeal;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;


public class CourtOfAppealDialog extends XDialog {
	
	private CourtOfAppealPanel courtPanel;
	private static final String TITLE_SUFFIX_DEFAULT = " Court of Appeal - Case ";
	private static final long serialVersionUID = 1L;
   
	public CourtOfAppealDialog(XhibitApplicationController xac, CaseBasicValue caseBV) throws CSRecoverableException {
 	    super(xac, "Court Of Appeal", true,  XDialog.CUSTOM, XDialog.DEFAULTOK);
  	 	courtPanel = new CourtOfAppealPanel(this, xac, caseBV);
  	 	String titleSuffix = TITLE_SUFFIX_DEFAULT + caseBV.getCaseType() + caseBV.getCaseNumber();
        initialise(titleSuffix);
    
	}

	 private void initialise(String titleSuffix){
		 
	     this.setTitle(titleSuffix);  	        
	     addBodyPanel(courtPanel);
	     pack();
 	 }
	 

	 
}
