package uk.gov.courtservice.xhibit.client.util.table.renderers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;

import mseries.Calendar.MDateChanger;
import mseries.Calendar.MDefaultPullDownConstraints;
import mseries.ui.MDateCellEditor;
import mseries.ui.MDateFormat;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * PRE00166 30-10-2003 AW Daley Modified to display an error message if an
 * invalid date is entered
 * 
 */
public class XDateTableCellEditor extends MDateCellEditor {

	private final static String INVALID_DATE_TITLE_KEY = "date.invalid.title";
	
	private final static String INVALID_DATE_MESSAGE_KEY = "date.invalid.message";

	private ResourceBundle errorResources;

	private JPanel jp = new JPanel();

	private JPanel spacerPanel = new JPanel();

	private JComponent parent;

	private Date originalValue;

	/**
	 * Used to stop table model's setValueAt method being called when
	 * getTableCellEditorComponent is called.
	 */
	private boolean firstTime = true;

	public XDateTableCellEditor(MDateFormat formatter, JComponent parent) {
		super(formatter);
		this.parent = parent;

		MDefaultPullDownConstraints c = new MDefaultPullDownConstraints();
		c.changerStyle = MDateChanger.BUTTON;
		c.hasShadow = true;
		c.firstDay = Calendar.MONDAY;
		this.setConstraints(c);
		comp.setNullOnEmpty(true);

		jp.setLayout(new GridBagLayout());
		jp.setOpaque(true);
		spacerPanel.setOpaque(true);
		jp.add(getEditor(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		jp.add(spacerPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// Load in resource bundle that contains the message to be displayed
		errorResources = XHIBITConstant.getResourceBundle(XhibitBundles.ErrorText);

	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof Date) {
			comp.setValue((Date) value);
			// store the original date before any changes are made
			originalValue = (Date) value;
		} else {
			comp.setValue(null);
		}

		setCellBackground(table, isSelected);

		return jp;
	}

	/**
	 * If the date is invalid then display an error message and do not stop
	 * editing the cell. Focus will remain in the cell
	 * 
	 * @return
	 */
	public boolean stopCellEditing() {
		if (!isDateEnteredValid()) {
			JOptionPane.showMessageDialog(parent,
					XHIBITConstant.getResource(errorResources, INVALID_DATE_MESSAGE_KEY) + XDateFormat.simpleDateFormat,
					XHIBITConstant.getResource(errorResources, INVALID_DATE_TITLE_KEY), JOptionPane.ERROR_MESSAGE);
			// If date is invalid, reset to original value
			comp.setValue(originalValue);
			return false;
		}

		return super.stopCellEditing();
	}

	/**
	 * Method that overrides the super class method. This needs to be done
	 * because the super class method sets the date to todays date when an
	 * invalid data is entered.
	 * 
	 * @return
	 */
	public Object getCellEditorValue() {
		Object date = null;
		try {
			// Get value from component
			date = comp.getValue();
		} catch (ParseException e) {
			return date;
		}
		return date;
	}

	/**
	 * Determines if the date entered is valid.
	 * 
	 * @return true valid date
	 */
	private boolean isDateEnteredValid() {
		Object date = null;
		try {
			// Get the date value from the component
			date = comp.getValue();

			// If string entered is parsed successfully then return date
			// valid
			return true;
		} catch (ParseException e) {
			// If exception thrown when parsing the string entered then
			// return date invalid
			return false;
		}
	}

	public void setCellBackground(JTable table, boolean isSelected) {
		this.jp.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground()); // Color.white
		this.spacerPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

		jp.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		jp.setForeground(UIManager.getColor("Table.focusCellForeground"));
		jp.setBackground(UIManager.getColor("Table.focusCellBackground"));
		spacerPanel.setForeground(UIManager.getColor("Table.focusCellForeground"));
		spacerPanel.setBackground(UIManager.getColor("Table.focusCellBackground"));
	}

	protected JComponent getEditor() {
		return comp;
	}

}