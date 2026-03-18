package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.admin.security.rolemapping.StripedTableCellRenderer;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellEditor;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellRenderer;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationControllerImpl;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * The Panel that displays a scrollable list of offences that can be added to the D20 order.
 * Checkbox for each order to be include
 * Details such as the offence, dvla offence, a description of the offence and the date of the offence included
 * 
 * @author guthriec
 *
 */
public class OrderOffenceListPanel extends XPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderOffenceModel model;
	private OrderOffencePanel parent;
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private TitledBorder titledBorder;
	private JLabel dvlaOffenceLabel;
	private JLabel offenceLabel;
	private JLabel convictionDateLabel;
	private JLabel descriptionOffenceLabel;
	private JLabel dateOffenceLabel;
	private JCheckBox offenceCheckBoxButton;
	private ArrayList<JCheckBox> offenceCheckBoxes;
	private XTable offenceSelectorTable;

	/**
	 * Constructer and initialises the panel
	 * 
	 * @param parent
	 * @param model
	 * @throws CSRecoverableException
	 */
	public OrderOffenceListPanel(OrderOffencePanel parent, OrderOffenceModel model) throws CSRecoverableException {
		this.parent = parent;
		this.model = model;
		this.offenceCheckBoxes = new ArrayList<JCheckBox>();
		stepInitialise();
		init();
	}

	/**
	 * Create the panels for the checkbox list and the offence details
	 * It uses and OrderOffenceModel to populate the list
	 */
	public void init() {
		
		this.setLayout(new BorderLayout());
		titledBorder = new TitledBorder(
				BorderFactory.createEtchedBorder(SystemColor.controlHighlight, SystemColor.BLUE),
				getString("orderOffenceList.border.title"));
		titledBorder.setTitleColor(Color.BLUE);
		this.setBorder(titledBorder);
		
		this.add(new JScrollPane(this.getOffenceSelTableModel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		
	

	}
	
	private XTable getOffenceSelTableModel()
	{
		if(this.offenceSelectorTable==null)
		{
			
			OffenceSelectorModel tableModel= new OffenceSelectorModel(parent, model);
			offenceSelectorTable = XTableFactory.getInstance().createMultiLineTable(tableModel);
			TableUtils.setupDefaultsOnJTable(offenceSelectorTable);
			
			offenceSelectorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			offenceSelectorTable.getTableHeader().setReorderingAllowed(false);
			offenceSelectorTable.setCellSelectionEnabled(true);
			offenceSelectorTable.getColumnModel().getColumn(OffenceSelectorModel.CHECKBOX_COL).setCellRenderer(new OffSelectionCellRenderer(offenceSelectorTable,parent,model ));
			offenceSelectorTable.getColumnModel().getColumn(OffenceSelectorModel.CHECKBOX_COL).setCellEditor( new CheckBoxTableCellEditor(new JCheckBox()));
			offenceSelectorTable.getColumnModel().getColumn(0).setMaxWidth(20);
			offenceSelectorTable.getColumnModel().getColumn(1).setMaxWidth(110);
			offenceSelectorTable.getColumnModel().getColumn(2).setMaxWidth(75);
			offenceSelectorTable.getColumnModel().getColumn(3).setMaxWidth(110);
			offenceSelectorTable.getColumnModel().getColumn(0).setMinWidth(20);
			offenceSelectorTable.getColumnModel().getColumn(1).setMinWidth(110);
			offenceSelectorTable.getColumnModel().getColumn(2).setMinWidth(75);
			offenceSelectorTable.getColumnModel().getColumn(3).setMinWidth(110);
			offenceSelectorTable.getColumnModel().getColumn(4).setPreferredWidth(150);
			DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
			tableRenderer.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
			((DefaultTableCellRenderer)offenceSelectorTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
			offenceSelectorTable.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
			offenceSelectorTable.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
			offenceSelectorTable.getColumnModel().getColumn(3).setCellRenderer(tableRenderer);
			offenceSelectorTable.getColumnModel().getColumn(5).setCellRenderer(tableRenderer);
			
			
			
			
			offenceSelectorTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			
			
			
			
			
		}
		
		return offenceSelectorTable;
	}

	
	

	/**
	 * @return DVLA Offence Label. Creates Label if there is none.
	 */
	private JLabel getDvlaOffenceLabel() {
		if (dvlaOffenceLabel == null) {
			dvlaOffenceLabel = new JLabel(getString("dvlaOffenceLabel"));
			dvlaOffenceLabel.setForeground(Color.BLUE);
		}
		return dvlaOffenceLabel;

	}

	/**
	 * @return Offence Label. Creates Label if there is none.
	 */
	private JLabel getOffenceLabel() {
		if (offenceLabel == null) {
			offenceLabel = new JLabel(getString("offenceLabel"));
			offenceLabel.setForeground(Color.BLUE);
		}
		return offenceLabel;

	}
	
	/**
	 * @return Conviction Date Label
	 */
	private JLabel getConvictionDateLabel() {
		if (convictionDateLabel == null) {
			convictionDateLabel = new JLabel(getString("convictionDateLabel"));
			convictionDateLabel.setForeground(Color.BLUE);
		}
		return convictionDateLabel;

	}

	/**
	 * @return Offence Description Label. Creates Label if there is none.
	 */
	private JLabel getDescriptionOffenceLabel() {
		if (descriptionOffenceLabel == null) {
			descriptionOffenceLabel = new JLabel(getString("descriptionOffenceLabel"));
			descriptionOffenceLabel.setForeground(Color.BLUE);
		}
		return descriptionOffenceLabel;

	}

	/**
	 * @return Gets the date of the offence label. Creates Label if there is none.
	 */
	private JLabel getDateOffenceLabel() {
		if (dateOffenceLabel == null) {
			dateOffenceLabel = new JLabel(getString("dateOffenceLabel"));
			dateOffenceLabel.setForeground(Color.BLUE);
		}
		return dateOffenceLabel;

	}
	
	/**
	 * @return Checkbox for the offence
	 */
	public JCheckBox getOffenceCheckBox() {
		if (offenceCheckBoxButton == null) {
			offenceCheckBoxButton = new JCheckBox();
			offenceCheckBoxButton.setSelected(false);
			offenceCheckBoxButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						parent.stepUpdateViewState();
					} catch (CSRecoverableException csre) {
						XHIBITErrorHandler.handleError(csre);
					}
				}
			});
		}
		return offenceCheckBoxButton;

	}
	
	/**
	 * Method that enables all the D20 Order Offence Checkboxes
	 */
	public void enableAllCheckboxes() {
		this.getOffenceSelTableModel().selectAll();
		
	}

	private void moveModelToScreen() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
		stepUpdateViewState();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	/**
	 * @param key key for the resource string
	 * @return the resource based on the key
	 */
	private String getString(String key) {
		return ResourceBundleHelper.getResource(XhibitBundles.OrderOffenceResources, key);
	}

}
