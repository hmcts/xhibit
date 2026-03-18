package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.RightJustifyTextRenderer;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class JuryDischargedPanel extends CourtLogEventPanel {
    private final JuryDischargedModel model;

    private JuryDischargedTableModel juryDischargedTableModel = new JuryDischargedTableModel(new Juror[] {});

    private XTable jurorTable = null;

    private JLabel panelTitleLabel;

    private Dimension juryTableSize = new Dimension(350, (XHIBITConstant.getLineHeight() + 1) * 13);

    public JuryDischargedPanel(XDialog parent, JuryDischargedModel model) throws CSRecoverableException {
        super(parent, model);

        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        final JScrollPane jurorTableScrollPane = new JScrollPane();

        jurorTableScrollPane.setMinimumSize(juryTableSize);
        jurorTableScrollPane.setPreferredSize(juryTableSize);
        jurorTableScrollPane.getViewport().add(getJurorTable(), null);

        this.add(getPanelTitleLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(jurorTableScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0));
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        Juror[] jArray = new Juror[12];
        HashMap jsOptionsType = (HashMap) propertyMap.get("E20915_Jury_Discharged");
        for (int i = 0; i < 12; i++) {
            Juror juror = new Juror((String) jsOptionsType.get("E20915_JSIO_juror" + (i + 1)), (String) jsOptionsType
                    .get("E20915_JSIO_jurorNo" + (i + 1)));
            jArray[i] = juror;
        }

        juryDischargedTableModel = new JuryDischargedTableModel(jArray);
    }

    public void stepInitialise() throws CSRecoverableException {
        if (!model.isInEditMode()) {
            Juror[] jArray = new Juror[12];
            for (int i = 0; i < 12; i++) {
                Juror j = new Juror("", "");
                jArray[i] = j;
            }

            juryDischargedTableModel = new JuryDischargedTableModel(jArray);
        }

        super.stepInitialise();
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();

        setEditingValue();
        for (int i = 0; i < 12; i++) {
            String jurorId = (String) getJurorTable().getModel().getValueAt(i, 0);
            String jurorName = (String) getJurorTable().getModel().getValueAt(i, 1);
            String jurorNo = (String) getJurorTable().getModel().getValueAt(i, 2);

            try {
                log.debug("JuryDischargedPanel.stepValidate jurorNo = |" + jurorNo + "|");
                // Changed from int to long as we must allow for 9999999999
                long number = Long.parseLong(jurorNo);
                log.debug("JuryDischargedPanel.stepValidate number = |" + number + "|");
                if (number <= 0) {
                    // Jury Number validation exception - must be > 0

                    throw new CSValidationException("validation.minexclusive", new Object[] { jurorNo, "0" },
                            "Jury number <= 0");
                }
            } catch (NumberFormatException ex) {
                if ("".equals(jurorName.trim()) && "".equals(jurorNo.trim())) {
                    // NoAction
                } else {
                    throw new CSValidationException("Court_Log.Jury_Discharged.Juror_Number_For_Juror",
                            new Object[] { ("".equals(jurorName.trim()) ? jurorId : jurorName) },
                            "Court_Log.Jury_Discharged.Juror_Number_For_Juror: Invalid juror number", ex);
                }
            }
            // Juror name not entered
            if (jurorNo.trim().length() > 0 && jurorName.trim().length() <= 0) {
                throw new CSValidationException("Court_Log.Jury_Discharged.Juror_Name", new Object[] { new Integer(
                        i + 1) }, "Court_Log.Jury_Discharged.Juror_Name: No name entered");
            }
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        // Specific fields
        HashMap jsOptionsType = new HashMap();

        XHIBITTableModelInterface tModel = (XHIBITTableModelInterface) getJurorTable().getModel();
        for (int i = 0; i < 12; i++) {
            Juror juror = (Juror) tModel.getDataAt(i);
            jsOptionsType.put("E20915_JSIO_juror" + (i + 1), juror.getName());
            jsOptionsType.put("E20915_JSIO_jurorNo" + (i + 1), juror.getJurorId());
        }

        propertyMap.put("E20915_Jury_Discharged", jsOptionsType);
    }

    protected boolean isMandatoryFieldsCompleted() {
        boolean incompleteRow = false;
        int rowsWithData = 0;
        setEditingValue();
        for (int i = 0; i < 12; i++) {

            String column1 = checkNull((String) getJurorTable().getValueAt(i, 1));
            String column2 = checkNull((String) getJurorTable().getValueAt(i, 2));
            if ((column1.length() == 0) && (column2.length() == 0)) {
                // NoAction
            } else {
                if ((column1.length() > 0) && (column2.length() > 0)) {
                    rowsWithData++;
                } else {
                    incompleteRow = true;
                }
            }
        }
        return ((rowsWithData > 0) && (incompleteRow == false));
    }

    private String checkNull(String toCheck) {
        if (toCheck == null) {
            return "";
        } else {
            return toCheck.trim();
        }
    }

    // This method checks if the table is still in edit mode and forces it
    // to
    // stop editting
    private void setEditingValue() {
        JTable table = getJurorTable();
        Component c = table.getEditorComponent();
        if (c instanceof JTextComponent) {
            table.getModel()
                    .setValueAt(((JTextComponent) c).getText(), table.getEditingRow(), table.getEditingColumn());
        }
    }

    private JLabel getPanelTitleLabel() {
        if (panelTitleLabel == null) {
            panelTitleLabel = new PanelTitleLabel(ResourceBundleHelper.getResource(XhibitBundles.JuryDischarged,
                    "panelTitle"));
        }
        return panelTitleLabel;
    }

    private JTable getJurorTable() {
        if (jurorTable == null) {
            jurorTable = XTableFactory.getInstance().createDefaultTable(juryDischargedTableModel);
            jurorTable.setMinimumSize(juryTableSize);
            jurorTable.setRowHeight(XHIBITConstant.getLineHeight());

            jurorTable.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }

                public void keyReleased(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
            TableColumnModel columnModel = jurorTable.getColumnModel();
            TableColumn jurorNameColumn = columnModel.getColumn(JuryDischargedTableModel.NAME_COLUMN);
            jurorNameColumn.setCellEditor(new KeyListenerCellEditor(new JTextField()));

            TableColumn jurorNoColumn = columnModel.getColumn(JuryDischargedTableModel.JUROR_NO_COLUMN);
            jurorNoColumn.setCellEditor(new KeyListenerCellEditor(new JTextField()));

            jurorTable.initColumnSizes(new String[] { "99 ", XTableFactory.COLUMN_WIDTH_UNDEFINED, " Juror No.  " },
                    (int) juryTableSize.getWidth());

            // Tab action added to JuryTable so that the user cannot
            // tab or shift tab into the index column.
            // Handles tab action
            String tabActionName = new String("Tab");
            jurorTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), tabActionName);
            jurorTable.getActionMap().put(tabActionName, new JuryTableTabAction(true));

            // Handles shift tab action
            String shiftTabActionName = "ShiftTab";
            jurorTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK),
                    shiftTabActionName);
            jurorTable.getActionMap().put(shiftTabActionName, new JuryTableTabAction(false));

            // Add listener to set cursor to second column in table when
            // the table gains focus
            jurorTable.addFocusListener(new JuryTableFocusListener());
            jurorTable.getColumnModel().getColumn(JuryDischargedTableModel.JUROR_NO_COLUMN).setCellRenderer(
                    new RightJustifyTextRenderer());
        }
        return jurorTable;
    }

    /**
     * <p>
     * Title: JurorCellEditor used to enable the OK button when the relevant
     * table cells have been populated.
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
     * @author unascribe
     * @version 1.0
     */
    private class KeyListenerCellEditor extends DefaultCellEditor {
        /**
         * Create a JurorCellEditor
         * 
         * @param text
         *            A text field
         */
        public KeyListenerCellEditor(JTextField text) {
            super(text);
            getComponent().addKeyListener(new KeyAdapter() {
                /**
                 * Override KeyAdapter method - update the view state when a key
                 * is typed
                 * 
                 * @param e
                 *            KeyEvent
                 */
                public void keyTyped(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }

                /**
                 * Override KeyAdapter method - update the view state when a key
                 * is released
                 * 
                 * @param e
                 *            KeyEvent
                 */
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
    }
}
