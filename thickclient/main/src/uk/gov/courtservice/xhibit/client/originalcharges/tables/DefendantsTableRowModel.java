package uk.gov.courtservice.xhibit.client.originalcharges.tables;

import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public class DefendantsTableRowModel extends OriginalChargesTableRowModel {

    private ChargeVO[] obsoleteCharges;
    
    private boolean hasAnyIndictments;
    
    public DefendantsTableRowModel() {
        super();
    }
    
    public DefendantsTableRowModel(DefendantOnCaseVO data) {
        this(data, new ChargeVO[] {});
    }
    
    public DefendantsTableRowModel(DefendantOnCaseVO data, ChargeVO[] obsoleteCharges) {
        super(data);
        this.obsoleteCharges = obsoleteCharges;
        this.hasAnyIndictments = hasAnyIndictments;
    }
    
    public String getAsn() {
        return getDefendantOnCaseVO().getAsn();
    }
    public void setAsn(String asn) {
        this.getDefendantOnCaseVO().setAsn(asn);
    }
    public boolean hasIndictments() {
        return getHasAnyIndictments();
    }
    public String getDisplayableName() {
        return getDefendantOnCaseVO().getDisplayableName();
    }
    public ChargeVO[] getObsoleteCharges() {
        return obsoleteCharges;
    }
    public void setObsoleteCharges(ChargeVO[] obsoleteCharges) {
        this.obsoleteCharges = obsoleteCharges;
    }
    public boolean getHasAnyIndictments(){
        return hasAnyIndictments;
    }
    public void setHasAnyIndictments(boolean hasAnyIndictments){
        this.hasAnyIndictments = hasAnyIndictments;
    }
}
