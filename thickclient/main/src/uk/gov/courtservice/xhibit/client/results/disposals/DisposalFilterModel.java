package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelDecoratorInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

public class DisposalFilterModel extends XHIBITDefaultTableModel implements Observer, XHIBITTableModelDecoratorInterface {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DisposalFilterModel.class);
	private XHIBITTableModelInterface model;
    private DisposalFilterSelectionModel dfs;

    private java.util.List<Integer> rows;
    
    private String selectedDefendant;
    private int filter = 0;
    
    public DisposalFilterModel(XHIBITTableModelInterface m, DisposalFilterSelectionModel dfs) {
        model = m;
        this.dfs = dfs;

        dfs.addObserver(this);
        m.addTableModelListener(null);
        rows = new ArrayList<Integer>();

        for (int i = 0; i < model.getRowCount(); i++) {
            rows.add(new Integer(i));
        }
    }

    public void update(Observable obs, Object obj) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("DisposalFilterModel: update");
        filter = dfs.getFilter();
        selectedDefendant = dfs.getDefendantFilter();
        filterDisposals();
    }
    
	public void filterDisposals() {
    	rows = new ArrayList<Integer>();
        for (int i = 0; i < model.getRowCount(); i++) {
            ResultsRowValue disposalTableRow = (ResultsRowValue) model.getDataAt(i);
            if (matchesFilter(disposalTableRow)) {
            	rows.add(new Integer(i));
            }
        }
        fireTableDataChanged(); //redraws data on screen ;
    }
    
    public Boolean matchesFilter(ResultsRowValue disposalTableRow){
    	String defendant;
    	Boolean matchesFilter = true;
    	if (filter == DisposalFilterSelectionModel.FILTER_DEFENDANT) {
            XHIBITConstant.debug("[DisposalFilterModel] FILTER_DEFENDANT");

            defendant = ResultsHelper.getName(disposalTableRow.getDefendantValue());
            matchesFilter = defendant.equals(selectedDefendant);
    	}
    	return matchesFilter;
    }

    public Object getDataAt(int r) {
    	if (r >= rows.size()) {
    		log.error("Cannot retrieve data for element at row "+r);
    		return null;
    	}
    	int row = ((Integer) rows.get(r)).intValue();
        return model.getDataAt(row);
    }

    /*
     * compute the moved row for the methods that access model elements
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
        //log.debug("value at row " + row + ", " + c + " is " 
        //		+ model.getValueAt(row, c));
        return model.getValueAt(row, c);
    }

    public boolean isCellEditable(int r, int c) {
        int row = ((Integer) rows.get(r)).intValue();
        return model.isCellEditable(row, c);
    }

    public void setValueAt(Object aValue, int r, int c) {
        int row = ((Integer) rows.get(r)).intValue();
        model.setValueAt(aValue, row, c);
        fireTableCellUpdated(r, c);
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
    
    //added method
    public String getNameForPropertyFile() {
        String chargeType = "";
        if (_data.length != 0) {
            chargeType = ((ResultsRowValue) _data[0]).getChargeType();
        }
        return super.getNameForPropertyFile() + chargeType;
    }

    /**
     * Return the model that is being filtered
     * 
     * @return
     */
    public TableModel getModel() {
        return model;
    }
    
    public void addRow() {
    	rows.add(new Integer(rows.size()));
    }
    
    /**
     * If data is added the rows could get misaligned, so reorder
     */
    public void fixRowsAfterAdd(int dataInsertedAt) {
    	boolean insertedValue = false;
    	// Keep looping till we get the element with the value just the next highest after "dataInsertedAt"
    	// If no value then just leave as is
    	// If we find a value then insert "dataInsertedAt" here
    	int i = 0;
    	while (i<rows.size() && !insertedValue) {
    		
    		if ((rows.get(i) > dataInsertedAt) && (!insertedValue)) {
    			rows.add(i, dataInsertedAt);
    			insertedValue = true;
    		}
    		
    		i++;
    	}
    	if (!insertedValue) {
    		rows.add(rows.size()-1, dataInsertedAt);
    	}
    	
    	// Remove the last entry in rows
    	rows.remove(rows.size()-1);
    	
    	// Finally, check if any items need to be right-shifted; i.e. look for duplicates
    	// Duplicates could occur if we have multiple unsaved disposals for the same offence
    	i = 0;
    	int prevVal = 0;
    	int currVal = 0;
    	while (i<rows.size()) {
    		if (i > 0) {
    			currVal = rows.get(i);
    			if (prevVal == currVal) {
    				rows.set(i, rows.get(i)+1);
    				i = 0;
    			}
    		}
    		prevVal = rows.get(i);
    		i++;
    	}
    	
    	return;
    }
    
    /**
     * Gets the actual row number from the filtered list where the given row number from the unfiltered list appears
     *  
     * @param dataInsertedAt
     * @return
     */
    public int getFilteredRowNoAt(int dataInsertedAt) {
    	int i=0;
    	while (i<rows.size()) {
    		if (dataInsertedAt == rows.get(i)) {
    			return i;
    		}
    		i++;
    	}
    	return 0;
    }
}
