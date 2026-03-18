package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import java.util.Vector;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.DefendantHelper;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.UnauthorisedCaseDeftValue;


/**
 * <p>
 * Title: UnauthorisedCaseStatusTableModel
 * </p>
 * <p>
 * Description: The Table Model for the UnauthorisedCaseStatus table
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
public class UnauthorisedCaseStatusTableModel extends XHIBITTableModel{
    private static final long serialVersionUID = 1L;
    
    //Headers
    public static final int COURT_ROOM = 0;
    public static final int CASE_NUMBER = 1;
    public static final int DEFT_NAME = 2;
    public static final int CASE_CONCLUSION_DATE = 3;
    
    public UnauthorisedCaseStatusTableModel(){
        setup();
    }
    
    private void setup(){
        setColumnNames(new String[] {XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucColCourtRoom"),
                XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucColCaseNumber"),
                XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucColDeftNames"),
                XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "ucColConclusionDate")});
        this.setData(new Vector());
    }
    
    public Object getValueAt(int r, int c) {
        UnauthorisedCaseStatusTableRowModel myVO = (UnauthorisedCaseStatusTableRowModel) getData().elementAt(r);
        switch (c) {
        case COURT_ROOM:
            return myVO.getCourtRoom();
        case CASE_NUMBER:
            return myVO.getCaseValue().getCaseType()+myVO.getCaseValue().getCaseNumber();
        case DEFT_NAME:
            return constructDeftNames(myVO.getDefendants());
        case CASE_CONCLUSION_DATE:
            return XDateFormat.format(myVO.getCaseConclusionDate(), XDateFormat.DATEFORMAT);
        default:
            return "";
        }
    }
    
    /**
     * Method which takes an array of defendants and returns a string
     * containing a concatenation of all their names.
     * 
     * @param defs
     * @return String - All Defendant full names appended to one another
     */
    private String constructDeftNames(UnauthorisedCaseDeftValue[] defs){        
        String defName = "";
        for(int x=0;x <defs.length;x++){
            defName += DefendantHelper.getDefendantFullName(defs[x].getDefBasic()) + "\n";
        }
        
        return defName;
    }
    
    
    
}
