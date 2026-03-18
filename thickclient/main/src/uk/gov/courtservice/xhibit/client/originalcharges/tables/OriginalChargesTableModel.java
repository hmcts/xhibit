package uk.gov.courtservice.xhibit.client.originalcharges.tables;

import java.util.Collection;

import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

public abstract class OriginalChargesTableModel extends XHIBITDefaultTableModel {

    public OriginalChargesTableModel() {
        super();
    }
    
    public boolean isCellEditable(int r, int c) {
        return false;
    }

    protected void setup(Collection<OriginalChargesTableRowModel> data, String[] columnNames) {
        this.setColumnNames(columnNames);
        this.setData(data);
    }
    
    public Object[] getData() {
        return _data;
    }
}
