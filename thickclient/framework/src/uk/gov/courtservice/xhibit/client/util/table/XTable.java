package uk.gov.courtservice.xhibit.client.util.table;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelDecoratorInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.sort.SortButtonRenderer;
import uk.gov.courtservice.xhibit.client.util.table.model.sort.SortHeaderListener;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineHelper;
import uk.gov.courtservice.xhibit.client.util.table.xml.XTableXmlHelper;

/**
 * <p>
 * Title: XHIBIT2 XTable
 * </p>
 * <p>
 * Description: This JTable subclass provides support to make this table and its
 * model sortable.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @editor Rakesh Lakhani
 * @version 2.0
 */

public class XTable extends JTable {
	public static final String XTABLESIZES = "XTableSizes.properties";

	/**
	 * This constant is used for undefined column widths when initialising the
	 * column widths.
	 */
	public static final String COLUMN_WIDTH_UNDEFINED = "";

	private static Logger log = CSServices.getLogger(XTable.class);

	private boolean isTableChanged = false;

	private int lastRowCount = -1;

	/*
	 * Single instance of multiline helper for the table. Each renderer wishing
	 * to be a multi-line component should get a handle on this for resizing the
	 * rows.
	 */
	private MultiLineHelper _multiLineHelper = null;

	public XTable() {
		super();
		addListeners();
	}

	public XTable(Object[][] data, Object[] headers) {
		super(data, headers);
		addListeners();
	}

	/**
	 * passing an xsortabletablemodel will automatically make the table
	 * sortable.
	 * 
	 * @param tableModel
	 */
	public XTable(XSortableTableModel tableModel) {
		super(tableModel);
		this.makeSortable();
		addListeners();
	}

	public XTable(Vector data, Vector headers) {
		super(data, headers);
		addListeners();
	}

	public XTable(TableModel tableModel) {
		super(tableModel);
		addListeners();
	}

	private void addListeners() {
		TableHeaderResizeListener listen = new TableHeaderResizeListener(getTableHeader(), getBaseModelName(this));
		// getTableHeader().addHierarchyBoundsListener(listen);
		getTableHeader().addMouseListener(listen);
	}

	public static String getBaseModelName(XTable tableName) {
		TableModel tm = tableName.getModel();
		while (tm instanceof XHIBITTableModelDecoratorInterface) {
			tm = ((XHIBITTableModelDecoratorInterface) tm).getModel();
		}
		String name;
		if (tm instanceof XHIBITDefaultTableModel) {
			name = ((XHIBITDefaultTableModel) tm).getNameForPropertyFile();
		} else {
			name = tm.getClass().getName();
		}
		return name;
	}

	/**
	 * This method returns the mutli-line helper that the renderers can use for
	 * automatically resizing rows in the table.
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.table.multiline.MultiLineHelper
	 *      for more information.
	 * @return Single instance of MultiLineHelper for the table
	 */
	public MultiLineHelper getMultiLineHelper() {
		if (_multiLineHelper == null) {
			_multiLineHelper = new MultiLineHelper(this);
		}
		return _multiLineHelper;
	}

	/**
	 * only to be called after putting an XHIBITTableModel in the XTable.
	 */
	public void makeSortable() {
		this.makeSortable(true);
	}

	/**
	 * only to be called after putting an XHIBITTableModel in the XTable.
	 * 
	 * @param makeSortable
	 *            only the true value works
	 */
	public void makeSortable(boolean makeSortable) {
		if (makeSortable) {
			this.makeTableSortable();
		} else {
			this.makeTableUnSortable();
		}
	}

	public void initColumnSizes(Object[] longValues, int tableViewableWidth, boolean useMemorisedWidths) {
		boolean hasMemorisedWidths = false;
		StringTokenizer widths = null;
		if (useMemorisedWidths) {
			// check if property is in file.
			try {
				Properties localProps = PropertyHelper.getUserHomeProperties(XTABLESIZES);
				if (localProps != null) {
					String key = getBaseModelName(this);
					if (localProps.containsKey(key)) {
						widths = new StringTokenizer(localProps.getProperty(key), ",", false);
						hasMemorisedWidths = true;
					}
				}
			} catch (Exception ex) {
				// continue without properties from file.
				log.error("Error occurred getting widths from file: " + ex.getMessage());
			}
		}

		TableColumn column = null;

		if (hasMemorisedWidths && useMemorisedWidths && widths != null && widths.countTokens() > 0) {
			// make sure that the num of cols in the table and num of cols
			// in property is the same
			// set the preferred sized based on the properties
			int i = 0;
			while (widths.hasMoreTokens()) {
				if (i > (getColumnCount() - 1))
					break;

				int colWidth = Integer.valueOf(widths.nextToken()).intValue();
				column = this.getColumnModel().getColumn(i);
				column.setPreferredWidth(colWidth);

				i++;
			}
		} else {
			// calculate based on long values
			java.awt.Component comp = null;
			int headerWidth = 0;
			int cellWidth = 0;
			int columnCount = this.getColumnModel().getColumnCount();
			ArrayList undefinedIndexs = new ArrayList();

			// Represents the total width of all column widths that have a
			// column mask.
			int totalDefinedWidth = 0;

			for (int i = 0; i < longValues.length; i++) {
				// If the column mask is specified as undefined, then continue,
				// but
				// keep the index for later calculation
				if (longValues[i].equals(XTable.COLUMN_WIDTH_UNDEFINED)) {
					undefinedIndexs.add(new Integer(i));
					continue;
				}
				column = this.getColumnModel().getColumn(i);

				try {
					if (column.getHeaderRenderer() == null) {
						comp = this.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(this,
								column.getHeaderValue(), false, false, 0, 0);
					} else {
						comp = column.getHeaderRenderer().getTableCellRendererComponent(this, column.getHeaderValue(),
								false, false, 0, 0);
					}
					headerWidth = comp.getPreferredSize().width;
				} catch (NullPointerException e) {
					log.error("Unable to get header renderer component");
					// Just continue
				}

				// RL: 29/8/03: Removed this code as does not seem to be
				// required.
				// Can still size columns even if there is no data in the table.
				// if (this.getModel().getValueAt (0,i)==null)
				// {
				// continue;
				// }

				comp = this.getDefaultRenderer(this.getModel().getColumnClass(i)).getTableCellRendererComponent(this,
						longValues[i], false, false, 0, i);
				if (comp == null) {
					continue;
				}

				cellWidth = comp.getPreferredSize().width;

				// XXX: Before Swing 1.1 Beta 2, use setMinWidth instead.
				cellWidth = Math.max(headerWidth, cellWidth);
				column.setPreferredWidth(cellWidth);
				totalDefinedWidth += cellWidth;
			}

			if (undefinedIndexs.size() == 0) {
				return;
			}

			// Traverse the indexes of the undefined column masks and set
			// equal width to each column.
			int undefinedColWidth = (tableViewableWidth - totalDefinedWidth) / undefinedIndexs.size();
			for (int i = 0; i < undefinedIndexs.size(); i++) {
				int columnIndex = ((Integer) undefinedIndexs.get(i)).intValue();
				this.getColumnModel().getColumn(columnIndex).setPreferredWidth(undefinedColWidth);
			}
		}
	}

	/**
	 * Initialises the column sizes from given column masks for the table. If
	 * there is a column that does not have a particular mask, then it must be
	 * passed through as <code>XTable.COLUMN_WIDTH_UNDEFINED</code>
	 * 
	 * 
	 * @param table
	 *            the table that the columns belong to
	 * @param longValues
	 *            the column masks to determine the column widths
	 * @param tableViewableWidth
	 *            the view width of the table.
	 */
	public void initColumnSizes(Object[] longValues, int tableViewableWidth) {
		initColumnSizes(longValues, tableViewableWidth, true);
	}

	private void makeTableSortable() {
		if (!(this.getModel() instanceof XSortableTableModel)) {
			XSortableTableModel sortableTableModel = new XSortableTableModel(
					(XHIBITTableModelInterface) this.getModel());
			this.setModel(sortableTableModel);
		}
		addMouseListenerToHeaderInTable(this);
	}

	private boolean runOnce = false;

	public void addMouseListenerToHeaderInTable(XTable table) {
		if (!runOnce) {
			table.setColumnSelectionAllowed(false);
			TableColumnModel model = table.getColumnModel();
			SortButtonRenderer renderer = new SortButtonRenderer();
			int n = model.getColumnCount();
			for (int i = 0; i < n; i++) {
				model.getColumn(i).setHeaderRenderer(renderer);
			}
			JTableHeader header = table.getTableHeader();
			header.addMouseListener(new SortHeaderListener(header, (XSortableTableModel) table.getModel(), renderer));
			runOnce = true;
		}
	}

	private void makeTableUnSortable() {
		/** @todo */
	}

	/**
	 * Fix to ensure internal row count is updated for newly created models ie
	 * when the model is replaced within the table.
	 */
	public void setModel(TableModel model) {
		TableModel oldModel = getModel();

		int oldCount = oldModel == null ? 0 : oldModel.getRowCount();
		int newCount = model == null ? 0 : model.getRowCount();

		super.setModel(model);

		// Notify the table if the number of rows has changed
		// As we dont know where insert or delete occured perform at end,
		// this works as the table is subsequently notified that the data
		// has changed.
		if (oldCount < newCount) {
			tableChanged(new TableModelEvent(model, oldCount, newCount - 1, TableModelEvent.ALL_COLUMNS,
					TableModelEvent.INSERT));
		} else if (oldCount > newCount) {
			tableChanged(new TableModelEvent(model, newCount, oldCount - 1, TableModelEvent.ALL_COLUMNS,
					TableModelEvent.DELETE));
		}
		tableChanged(new TableModelEvent(model, TableModelEvent.HEADER_ROW));
	}

	/**
	 * Render the table as XML with the given title.
	 * 
	 * @return
	 */
	public String toXml(String title) {
		StringWriter buffer = new StringWriter();
		XTableXmlHelper.marshal(this, title, buffer);
		return buffer.toString();
	}

	/**
	 * 
	 * 
	 * 
	 * @author bzjrnl
	 * 
	 *         TODO To change the template for this generated type comment go to
	 *         Window - Preferences - Java - Code Style - Code Templates
	 */
	public String toFop(String title) {
		return XSLServices.getInstance().transform(toXml(title), "config/xsl/framework/xtable.xsl", Locale.getDefault(),
				null);
	}

	public boolean isEmpty() {
		if (this != null && this.getModel() != null) {
			return this.getModel().getRowCount() <= 0 ? true : false;
		}

		return false;
	}

}
