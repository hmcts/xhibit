package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineCellRenderer;
import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineHelper;

/**
 * <p>
 * Title: CourtLogCaseCellRenderer
 * </p>
 * <p>
 * Description: Custom cell renderer to set the text in a column to red if the string contains the text 'Migrated'. Used for the Case Number column in the CourtLogController in this case.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2026
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Owain Greener
 * @version 1.0
 */
public class CourtLogCaseCellRenderer extends MultiLineCellRenderer {
	
	private static final long serialVersionUID = 1L;
	
	private static final String MIGRATED = "Migrated";
	
	public CourtLogCaseCellRenderer(JTable table, MultiLineHelper helper) {
		super(table, helper);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (value != null) {
			String text = value.toString();
			
			if (!isSelected && text.contains(MIGRATED)) {
				setForeground(Color.RED);
			}
		}
		
		return c;
	}

}