package uk.gov.courtservice.xhibit.client.originalcharges.maintainoriginalcharge;

import java.util.TreeSet;
import java.util.List;

import uk.gov.courtservice.xhibit.client.originalcharges.tables.ChargesTableRowModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

public class MaintainOriginalChargeModel implements Cloneable {
    private String mode;
    private ChargesTableRowModel chargesTableRowModel;
    private String originalCharge;
    private String seqNo;
    private List usedSequenceNumbers;
    
    
    
    public enum MODE {
        ADD("ADD"),
        EDIT("EDIT"),
        DELETE("DELETE");
        
        String mode;
        
        MODE(String mode) {
            this.mode = mode;
        }
        
        public String getMode() {
            return mode;
        }
    }

    public String getOriginalCharge() {
        return originalCharge;
    }
    public void setOriginalCharge(String originalCharge) {
        this.originalCharge = originalCharge;
    }
    public String getSeqNo() {
        return seqNo;
    }
    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }
    public boolean inAddMode() {
        return getMode().equalsIgnoreCase("ADD");
    }
    public boolean inEditMode() {
        return getMode().equalsIgnoreCase("EDIT");
    }
    public boolean inDeleteMode() {
        return getMode().equalsIgnoreCase("DELETE");
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public ChargesTableRowModel getChargesTableRowModel() {
        return chargesTableRowModel;
    }
    public void setChargesTableRowModel(ChargesTableRowModel chargesTableRowModel) {
        this.chargesTableRowModel = chargesTableRowModel;
    }
    public List getUsedSequenceNumbers() {
        return usedSequenceNumbers;
    }
    public void setUsedSequenceNumbers(List usedSequenceNumbers) {
        this.usedSequenceNumbers = usedSequenceNumbers;
    }

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("MaintainOriginalChargeModel");
        XHIBITConstant.info("===========================");
        XHIBITConstant.info("mode                : " + getMode());
        XHIBITConstant.info("defendantOnCaseId   : " + getChargesTableRowModel().getDefendantOnCaseVO().getDefendantOnCaseId());
        XHIBITConstant.info("defendantName       : " + getChargesTableRowModel().getDefendantOnCaseVO().getDisplayableName());
        XHIBITConstant.info("defendantOnOffenceId: " + getChargesTableRowModel().getChargeVO().getDefendantOnOffenceId());
        XHIBITConstant.info("originalCharge      : " + getChargesTableRowModel().getChargeVO().getCrestOffenceFreetext());
        XHIBITConstant.info("seqNo               : " + getChargesTableRowModel().getChargeVO().getSeqNo());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setChargesTableRowModel(null);
        setOriginalCharge(null);
        setSeqNo(null);
        setUsedSequenceNumbers(null);
    }
}
