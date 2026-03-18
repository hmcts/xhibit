package uk.gov.courtservice.xhibit.client.originalcharges.tables;

import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public abstract class OriginalChargesTableRowModel {
    private DefendantOnCaseVO defendantOnCaseVO;
    
    public OriginalChargesTableRowModel() {
        // Default constructor
    }
    
    public OriginalChargesTableRowModel(DefendantOnCaseVO data) {
        defendantOnCaseVO = data;
    }
    
    public DefendantOnCaseVO getDefendantOnCaseVO() {
        return defendantOnCaseVO;
    }
    
    public void setDefendantOnCaseVO(DefendantOnCaseVO defendantOnCaseVO) {
        this.defendantOnCaseVO = defendantOnCaseVO;
    }
}
