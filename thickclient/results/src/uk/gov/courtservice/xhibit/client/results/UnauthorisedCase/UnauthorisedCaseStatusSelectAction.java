package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.authorise.AuthoriseResultsDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: UnauthorisedCaseStatusSelectAction
 * </p>
 * <p>
 * Description: Action which is triggered when a user chooses an case in 
 * the Unauthorised Status Grid
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0 
 */

public class UnauthorisedCaseStatusSelectAction extends XAction{
    private static final long serialVersionUID = 1L;
    
    private UnauthorisedCaseStatusPanel parent;
    
    public UnauthorisedCaseStatusSelectAction(UnauthorisedCaseStatusPanel parent){
        populateFromBundle("ucsSelect");
        this.parent = parent;
    }
    
    @Override
    public void xActionPerformed(ActionEvent e) throws Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        
        //Get a reference to the table
        XTable caseTable = parent.getTable();
        UnauthorisedCaseStatusTableRowModel row = null;
        XHIBITTableModelInterface xtmi = (XHIBITTableModelInterface)caseTable.getModel();
        
        if(caseTable.getSelectedRowCount()>0){
            //Get selected row
            row = (UnauthorisedCaseStatusTableRowModel) xtmi.getDataAt(caseTable.getSelectedRow());            
            
            if(row!=null){
                //Open the 'Authorise Results' Dialog for the relevant case
                AuthoriseResultsDialog resultsDialog = new AuthoriseResultsDialog(xac,row.getCaseValue(),row.getScheduledHearingId());
                resultsDialog.setVisible(true);
            }
        }
        
        
    }

}
