package uk.gov.courtservice.xhibit.client.originalcharges.tables;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class DefendantsTableModel extends OriginalChargesTableModel {

    public static final int NAME            = 0;
    public static final int HAS_INDICTMENTS = 1;
    public static final int ASN             = 2;

    private String[] columnNames = new String[] { 
        XHIBITConstant.getResource(XhibitBundles.OriginalCharges, "nameLbl"),
        XHIBITConstant.getResource(XhibitBundles.OriginalCharges, "hasIndictmentsLbl"),
        XHIBITConstant.getResource(XhibitBundles.OriginalCharges, "asnLbl")
    };

    public DefendantsTableModel() {
        super();
        setup(new ArrayList<OriginalChargesTableRowModel>(), columnNames);
    }
    
    public DefendantsTableModel(Collection<DefendantsTableRowModel> param) {
        super();
        setup(new ArrayList<OriginalChargesTableRowModel>(param), columnNames);
    }
    
    public Object getValueAt(int r, int c) {
        DefendantsTableRowModel trm = (DefendantsTableRowModel)getDataAt(r);
        switch (c) {
            case NAME:
                return trm.getDisplayableName();
            case HAS_INDICTMENTS:
                return new Boolean(trm.hasIndictments());
            case ASN:
                return trm.getAsn();
            default:
                return "";
        }
    }

    public Class getColumnClass(int col) {
        switch (col) {
            case NAME:
            case ASN:
                return String.class;
            case HAS_INDICTMENTS:
                return Boolean.class;
            default:
                return Object.class;
        }
    }
    
    public void printModel() {
        XHIBITConstant.debug("DefendantsOnCase");
        XHIBITConstant.debug("================");
        for( int x = 0; x < getRowCount(); x++ ) {
            DefendantsTableRowModel item = (DefendantsTableRowModel)getDataAt(x);
            XHIBITConstant.debug("defendantOnCaseId: "+item.getDefendantOnCaseVO().getDefendantOnCaseId());
            XHIBITConstant.debug("name             : "+item.getDisplayableName());
            XHIBITConstant.debug("hasIndictments   : "+item.hasIndictments());
            XHIBITConstant.debug("asn              : "+item.getAsn());
        }
    }
}
