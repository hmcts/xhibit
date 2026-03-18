package uk.gov.courtservice.xhibit.client.originalcharges.tables;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class ChargesTableModel extends OriginalChargesTableModel {

    public static final int DEFENDANT_NAME  = 0;
    public static final int ORIGINAL_CHARGE = 1;
    public static final int SEQUENCE_NUMBER = 2;
    
    private String[] columnNames = new String[] { 
        XHIBITConstant.getResource(XhibitBundles.OriginalCharges, "defendantNameLbl"),
        XHIBITConstant.getResource(XhibitBundles.OriginalCharges, "originalChargeLbl"),
        XHIBITConstant.getResource(XhibitBundles.OriginalCharges, "seqNoLbl")
    };

    public ChargesTableModel() {
        super();
        setup(new ArrayList<OriginalChargesTableRowModel>(), columnNames);
    }

    public ChargesTableModel(Collection<ChargesTableRowModel> param) {
        super();
        setup(new ArrayList<OriginalChargesTableRowModel>(param), columnNames);
    }

    public Object getValueAt(int r, int c) {
        ChargesTableRowModel trm = (ChargesTableRowModel)getDataAt(r);
        switch (c) {
            case DEFENDANT_NAME:
                return trm.getDefendantOnCaseVO().getDisplayableName();
            case ORIGINAL_CHARGE:
                return trm.getChargeVO().getCrestOffenceFreetext();
            case SEQUENCE_NUMBER:
                return trm.getChargeVO().getSeqNo() == null ? 
                      "" : 
                       String.format("%1$03d", trm.getChargeVO().getSeqNo());
            default:
                return "";
        }
    }
    
    public void printModel() {
        XHIBITConstant.debug("OriginalCharges");
        XHIBITConstant.debug("===============");
        for( int x = 0; x < getRowCount(); x++ ) {
            ChargesTableRowModel item = (ChargesTableRowModel)getDataAt(x);
            XHIBITConstant.debug("defendantOnCaseId: "+item.getDefendantOnCaseVO().getDefendantOnCaseId());
            XHIBITConstant.debug("name             : "+item.getDefendantOnCaseVO().getDisplayableName());
            XHIBITConstant.debug("originalCharge   : "+item.getChargeVO().getCrestOffenceFreetext());
            XHIBITConstant.debug("seqNo            : "+item.getChargeVO().getSeqNo());
        }
    }
}
