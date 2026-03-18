package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public abstract class CaseSummaryTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<T> dataList = new ArrayList<T>();

	@Override
	public int getRowCount() {
		return dataList.size();
	}

	@Override
	public int getColumnCount() {
		return getColumnHeaders().length;
	}
	
	public String getColumnName(int columnIndex)
	{
		return getColumnHeaders()[columnIndex];
	}

	protected String getFormattedDate(Timestamp date) {
		return XDateFormat.format(date, XDateFormat.DATEFORMAT);
	}
	
	public void setTableSource(ArrayList<T> data)
	{
		this.dataList = data;
		this.fireTableDataChanged();
	}
	
	public void addRow(T rowData)
	{
		this.dataList.add(rowData);
		this.fireTableDataChanged();
	}
	
	public T getRow(int rowIndex)
	{
		return this.dataList.get(rowIndex);
	}
	
	protected String getResource(String key) {
		return XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,key);
	}
	
	protected abstract String[] getColumnHeaders();
	
	protected Integer[] getColumnWidths() {
		Integer noOfColumns = getColumnHeaders().length;
		Integer[] columnWidths = new Integer[noOfColumns];
		return columnWidths;
	}
}
