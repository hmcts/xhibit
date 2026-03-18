package uk.gov.courtservice.xhibit.client.originalcharges;

import uk.gov.courtservice.xhibit.client.originalcharges.tables.ChargesTableModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.DefendantsTableModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import java.util.List;

public class OriginalChargesModel implements Cloneable {
    private DefendantsTableModel defendantsTableModel;
    private ChargesTableModel chargesTableModel;
    private XhibitApplicationController xac;
    private List<Integer> defsWithIndictmentsList;
    
    public ChargesTableModel getChargesTableModel() {
        return chargesTableModel;
    }
    public void setChargesTableModel(ChargesTableModel chargesTableModel) {
        this.chargesTableModel = chargesTableModel;
    }
    public DefendantsTableModel getDefendantsTableModel() {
        return defendantsTableModel;
    }
    public void setDefendantsTableModel(DefendantsTableModel defendantsTableModel) {
        this.defendantsTableModel = defendantsTableModel;
    }
    public XhibitApplicationController getXac() {
        return xac;
    }
    public void setXac(XhibitApplicationController xac) {
        this.xac = xac;
    }

    public void setDefsWithIndictmentsList(List defsWithIndictmentsList){
        this.defsWithIndictmentsList = defsWithIndictmentsList;
    }
    
    public List<Integer> getDefsWithIndictmentsList(){
        return this.defsWithIndictmentsList;
    }
    
    // Utility methods
    public void printModel() {
        XHIBITConstant.info("OriginalChargesModel");
        XHIBITConstant.info("====================");

        if( defendantsTableModel != null ) {
            defendantsTableModel.printModel();
        }
        if( chargesTableModel != null ) {
            chargesTableModel.printModel();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setDefendantsTableModel(null);
        setChargesTableModel(null);
        setXac(null);
    }
}
