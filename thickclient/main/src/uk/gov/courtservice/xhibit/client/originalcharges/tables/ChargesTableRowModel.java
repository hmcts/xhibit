package uk.gov.courtservice.xhibit.client.originalcharges.tables;

import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public class ChargesTableRowModel extends OriginalChargesTableRowModel {
    
    public static final String CREATE = "C";
    public static final String READ   = "R";
    public static final String UPDATE = "U";
    public static final String DELETE = "D";
    
    private String trxCode;
    private ChargeVO chargeVO;
    private boolean hasAnyIndictments;
    private boolean isNew;
    
    public ChargesTableRowModel() {
        super();
    }

    public ChargesTableRowModel(DefendantOnCaseVO defendantOnCaseVO) {
        this(
            defendantOnCaseVO, 
            new ChargeVO(
                defendantOnCaseVO.getDefendantOnCaseId(), 
                ChargeTypes.ORIGINAL_CHARGE.getChargeType()
            ), 
            ChargesTableRowModel.CREATE);
    }
    
    public ChargesTableRowModel(DefendantOnCaseVO defendantOnCaseVO, ChargeVO chargeVO, String trxCode) {
        super(defendantOnCaseVO);
        this.chargeVO = chargeVO;
        this.trxCode = trxCode;
        
    }
    
    public String getTrxCode() {
        return trxCode;
    }
    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode;
    }

    public ChargeVO getChargeVO() {
        return chargeVO;
    }

    public void setChargeVO(ChargeVO chargeVO) {
        this.chargeVO = chargeVO;
    }
    
    public void setHasAnyIndictments(boolean hasAnyIndictments){
        this.hasAnyIndictments = hasAnyIndictments;
    }
    
    public boolean getHasAnyIndictments(){
        return this.hasAnyIndictments;
    }
    
    public void setIsNew(boolean isNew){
        this.isNew = isNew;
    }
    
    public boolean getIsNew(){
        return isNew;
    }
    
    /**
     * Convenience method for determining whether or not the transaction is persistable
     * @return true if the transaction code is one of CREATE, UPDATE or DELETE
     */
    public boolean databaseUpdateRequired() {
        return  getTrxCode().equalsIgnoreCase(ChargesTableRowModel.CREATE)
            ||  getTrxCode().equalsIgnoreCase(ChargesTableRowModel.UPDATE)
            || (getTrxCode().equalsIgnoreCase(ChargesTableRowModel.DELETE)
            &&  getChargeVO().getDefendantOnOffenceId() != null);
    }
    
    /**
     * Convenience method for determining whether or not the record is displayable
     * @return true if the transaction code is one of CREATE, UPDATE or READ
     */
    public boolean displayableRecord() {
        return getTrxCode().equalsIgnoreCase(ChargesTableRowModel.CREATE)
            || getTrxCode().equalsIgnoreCase(ChargesTableRowModel.UPDATE)
            || getTrxCode().equalsIgnoreCase(ChargesTableRowModel.READ);
    }
}
