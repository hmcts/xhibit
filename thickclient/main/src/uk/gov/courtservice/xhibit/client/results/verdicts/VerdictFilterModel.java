package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.TableModel;

//import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelDecoratorInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

public class VerdictFilterModel extends XHIBITDefaultTableModel implements Observer, XHIBITTableModelDecoratorInterface {
	private static final long serialVersionUID = 1L;
	//private static final Logger log = Logger.getLogger(VerdictFilterModel.class);
	private XHIBITTableModelInterface model;
    private VerdictFilterSelectionModel vfs;

    private List<Integer> rows;
    private String selectedDefendant;
    private int filter = 0;
    
    public VerdictFilterModel(XHIBITTableModelInterface m, VerdictFilterSelectionModel vfs) {
    	model = m;
    	this.vfs = vfs;
    	
    	vfs.addObserver(this);
    	rows = new ArrayList<Integer>();
    	
    	for (int i = 0; i < model.getRowCount(); i++) {
            rows.add(new Integer(i));
        }
    	
    }
    
    public void update(Observable obs, Object obj) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("VerdictFilterModel: update");
        filter = vfs.getFilter();
        selectedDefendant = vfs.getDefendantFilter();
        filterVerdicts();
    }
    
    public void filterVerdicts() {
        String defendant;
        rows = new ArrayList<Integer>();

        for (int i = 0; i < model.getRowCount(); i++) {
            ResultsRowValue verdictTableRow = (ResultsRowValue) model.getDataAt(i);
            
            if (filter == VerdictFilterSelectionModel.FILTER_DEFENDANT) {
                XHIBITConstant.debug("[VerdictFilterModel] FILTER_DEFENDANT");

                defendant = ResultsHelper.getName(verdictTableRow.getDefendantValue());
                if (defendant.equals(selectedDefendant)) {
                    rows.add(new Integer(i));
                }
            } else {
                rows.add(new Integer(i));
            }
        }
        fireTableDataChanged();
    }

    public Object getDataAt(int r) {
        int row = ((Integer) rows.get(r)).intValue();
        return model.getDataAt(row);
    }

    /*
     * compute the moved row for the three methods that access model elements
     */
    public Object getValueAt(int r, int c) {
        // fix for buggy code in multi line table.
        if ((r < 0) || (r >= rows.size())) {
            return "";
        }

        int row;
        // XTable needs to reference first row of table before data exists
        // and if List "rows" is empty we get NullPointerException
        if (rows.size() == 0) {
            row = 0;
        } else {
            row = ((Integer) rows.get(r)).intValue();
        }
        return model.getValueAt(row, c);
    }

    public boolean isCellEditable(int r, int c) {
        int row = ((Integer) rows.get(r)).intValue();
        return model.isCellEditable(row, c);
    }

    public void setValueAt(Object aValue, int r, int c) {
        int row = ((Integer) rows.get(r)).intValue();
        model.setValueAt(aValue, row, c);
    }

    public int getRowCount() {
        // return model.getRowCount();
        return rows.size();
    }

    /* delegate all remaining methods to the model */
    public int getColumnCount() {
        return model.getColumnCount();
    }

    public String getColumnName(int c) {
        return model.getColumnName(c);
    }

    public Class getColumnClass(int c) {
        return model.getColumnClass(c);
    }

    /**
     * Return the model that is being filtered
     * 
     * @return
     */
    public TableModel getModel() {
        return model;
    }
}
