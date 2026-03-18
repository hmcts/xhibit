//package uk.gov.courtservice.xhibit.client.test;
//
//
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Iterator;
//import java.util.Vector;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JScrollPane;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.client.listeners.XhibitListeners;
//import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
//import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
//import uk.gov.courtservice.xhibit.client.util.XPanel;
//import uk.gov.courtservice.xhibit.client.util.XTableFactory;
//import uk.gov.courtservice.xhibit.client.util.table.XTable;
//import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;
//
///**
// * <p>Title: TestXTable </p>
// * <p>Description: Testbed for XHIBIT Table and XHIBIT Table models.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Frederik Vandendriessche
// * @version 1.0
// * $Log: TestXTable.java,v $
// * Revision 1.6  2006/07/13 12:58:05  xzfdtb
// * Unit test work - a broken unit test will now fail the build.
// *
// * Revision 1.5  2003/10/08 17:02:29  sz0t7n
// * splitting out the thickclient_framework
// *
// * Revision 1.4  2003/10/06 14:41:46  sz0t7n
// * remove deprecations
// *
// * Revision 1.3  2003/08/18 07:02:47  bzw8gp
// * Jon Powell
// *
// * organise imports (remove unused)
// * unused imports cause misleading dependencies
// * remove unused variables / associated imports
// *
// * Revision 1.2  2003/05/11 15:51:12  nz5zpz
// * extended test
// *
// * Revision 1.1  2003/04/30 10:57:56  nz5zpz
// * test construct for table(sorting)
// * *
// */
//
//public class TestXTable extends JFrame
//{
//	Logger log;
//
//	int maxRows = 5;
//	int maxCols = 5;
//
//    public TestXTable()
//    {
//		super("Test XTable Frame");
//		try
//		{
//			log = CSServices.getLogger(TestUpdateCase.class);
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating logger");
//			e.printStackTrace();
//		}
//		XhibitApplicationController xac = null;
//		try
//		{
//			xac = new XhibitApplicationControllerImpl();
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble creating xhibitapplicationcontroller");
//			e.printStackTrace();
//		}
//	    try
//		{
//			this.getContentPane().add(new XTableTestPanel());
//			setSize(600,600);
//			show();
//		}
//		catch(Exception e)
//		{
//			XHIBITConstant.debug("trouble using the XTableTestPanel instance");
//			e.printStackTrace();
//		}
//    }
//
//	private class XTableTestPanel extends XPanel implements ActionListener
//	{
//		private String action_TestTableSelection1 = "001";
//		XHIBITTableModel xTableModel;
//		//XSortableTableModel xSortTableModel;
//		XTable xTable;
//
//		public XTableTestPanel()
//		{
//			this.setLayout(new GridBagLayout());
//			GridBagConstraints gbc = new GridBagConstraints();
//			gbc.fill = GridBagConstraints.BOTH;
//			gbc.gridx = 0;
//			gbc.gridy = 0;
//
//			String[] cols = new String[maxCols];
//			Vector rows = new Vector();
//
//			for (int c=0; c < maxCols; c++) { cols[c] = "Column " + c ; }
//			for (int r=0; r < maxRows; r++)
//			{
//				Vector aRow = new Vector();
//				for (int c=0; c < maxCols; c++) { aRow.add(" [" + r + ", " + c + "] "); }
//				rows.add(aRow);
//			}
//
//			xTableModel = new XHIBITTableModel();
//			xTableModel.setColumnNames(cols);
//			xTableModel.setLongValues(cols);
//			xTableModel.setData(rows);
//			xTable = XTableFactory.getInstance().createDefaultTable(xTableModel);
//			xTable.makeSortable();
//
//			JScrollPane jsp = new JScrollPane(xTable);
//			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
//
//			this.add(jsp, gbc);
//
//
//			JButton testSelectionButton =  new JButton("SelectedItem");
//			testSelectionButton.setActionCommand(this.action_TestTableSelection1);
//			testSelectionButton.addActionListener(this);
//			gbc.gridy = 1;
//			gbc.fill = GridBagConstraints.NONE;
//			gbc.anchor = GridBagConstraints.CENTER;
//			this.add(testSelectionButton, gbc);
//
//			XhibitListeners.setDefaultListeners(this);
//		}
//
//
//		public void actionPerformed(ActionEvent actionEvent)
//		{
//			String action = actionEvent.getActionCommand();
//			if (action_TestTableSelection1.equals(action))
//			{
//				Vector row = (Vector)((XSortableTableModel)this.xTable.getModel()).getDataAt(xTable.getSelectedRow());
//				log.debug("Selection made at " + System.currentTimeMillis());
//				log.debug("got the vector at selected table index " + xTable.getSelectedRow());
//				log.debug("-----------------");
//				if (row == null)
//				{
//					log.debug("null row returned!");
//				}
//				else
//				{
//					StringBuffer sb = new StringBuffer("\n");
//					Iterator i = row.iterator();
//					while (i.hasNext()) sb.append("\tcell : "+ i.next());
//					log.debug(sb.toString());
//				}
//				log.debug("-----------------");
//			}
//		}
//		public void stepInitialise() {}
//		public void stepDeactivate() {}
//		public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException {}
//		public void stepUpdateViewState() {}
//		public void stepDeinitialise(boolean update) {}
//		public void stepActivate() {}
//	}
//
//	public static void main(String[] args)
//	{
//		try
//		{
//			TestXTable xx = new TestXTable();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//}