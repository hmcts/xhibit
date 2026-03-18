package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.RightJustifyTextRenderer;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XLimitedLengthCellEditor;

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
public class JurySwornInPanel extends CourtLogEventPanel {
    private final JurySwornInModel model;

    private JurySwornInTableModel jurySwornInTableModel = new JurySwornInTableModel(new Juror[] {});

    private XTable jurorTable = null;

    private JLabel panelTitleLabel;

    private Dimension juryTableSize = new Dimension(350, (XHIBITConstant.getLineHeight() + 1) * 13);

    // Bug X54067
    private final static int JUROR_NUMBER_MAX_LENGTH = 9;

    public JurySwornInPanel(XDialog parent, JurySwornInModel model) throws CSRecoverableException {
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
        HashMap jsOptionsType = new HashMap();

        jsOptionsType = (HashMap) propertyMap.get("E20902_Jury_Sworn_In");
        for (int i = 0; i < 12; i++) {
            jArray[i] = new Juror((String) jsOptionsType.get("E20902_JSIO_juror" + (i + 1)), (String) jsOptionsType
                    .get("E20902_JSIO_jurorNo" + (i + 1)));
        }

        jurySwornInTableModel = new JurySwornInTableModel(jArray);
    }

    public void stepInitialise() throws CSRecoverableException {
        super.stepInitialise();

        if (!model.isInEditMode()) {
            Juror[] jArray = new Juror[12];
            for (int i = 0; i < 12; i++) {
                Juror j = new Juror("", "");
                jArray[i] = j;
            }

            jurySwornInTableModel = new JurySwornInTableModel(jArray);
        }

        setFocusEditCell(getJurorTable(), 0, 1); // Set the focus of the
        // first name cell
    }

    private void setFocusEditCell(final JTable table, final int row, final int col) {
        // Gui alteration made, so keep it thread-safe and executed on the
        // event-dispatching thread.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                table.setRowSelectionInterval(row, row);
                table.setColumnSelectionInterval(col, col);
                // table.editCellAt(row,col); Uncomment to make the cell
                // editable straight away.
            }
        });
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        super.stepValidate();
        setEditingValue();

        for (int i = 0; i < 12; i++) {
            String jurorId = (String) getJurorTable().getModel().getValueAt(i, 0);
            String jurorName = (String) getJurorTable().getModel().getValueAt(i, 1);
            String jurorNo = (String) getJurorTable().getModel().getValueAt(i, 2);

            try {
                log.debug("JurySwornInPanel.stepValidate jurorNo = |" + jurorNo + "|");
                // changed from into to long as we must allow for 9999999999
                long number = Long.parseLong(jurorNo);
                log.debug("JurySwornInPanel.stepValidate number = |" + number + "|");
                if (number <= 0) {
                    // Jury Number validation exception - must be > 0

                    throw new CSValidationException("validation.minexclusive", new Object[] { jurorNo, "0" },
                            "Jury number <= 0");
                }
            } catch (NumberFormatException ex) {
                if ("".equals(jurorName.trim()) && "".equals(jurorNo.trim())) {
                    // NoAction
                } else {
                    throw new CSValidationException("Court_Log.Jury_Sworn.Juror_Number_For_Juror", new Object[] { (""
                            .equals(jurorName.trim()) ? jurorId : jurorName) },
                            "Court_Log.Jury_Sworn.Juror_Number_For_Juror: Invalid juror number", ex);
                }
            }
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        // Specific fields
        HashMap jsOptionsType = new HashMap();
        XHIBITTableModelInterface tModel = (XHIBITTableModelInterface) getJurorTable().getModel();
        for (int i = 0; i < 12; i++) {
            Juror juror = (Juror) tModel.getDataAt(i);
            String jurorName = juror.getName();
            String jurorNo = juror.getJurorId();

            jsOptionsType.put("E20902_JSIO_juror" + (i + 1), jurorName);
            jsOptionsType.put("E20902_JSIO_jurorNo" + (i + 1), jurorNo);
        }
        propertyMap.put("E20902_Jury_Sworn_In", jsOptionsType);
    }

    protected boolean isMandatoryFieldsCompleted() {
        setEditingValue();
        // D52731: Make screen OK-able by default
        // int rowsWithData = 0;
        // for( int i = 0; i < 12; i++ )
        // {
        // String column1 = checkNull((String) getJurorTable().getValueAt(i,
        // 1));
        // String column2 = checkNull((String) getJurorTable().getValueAt(i,
        // 2));
        // if (column1.length() > 0 || column2.length() > 0 )
        // {
        // rowsWithData++;
        // }
        // }
        // return ( rowsWithData > 0 );
        return true;
    }

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
            panelTitleLabel = new PanelTitleLabel(model.getPanelText());
        }
        return panelTitleLabel;
    }

    private JTable getJurorTable() {
        if (jurorTable == null) {
            jurorTable = XTableFactory.getInstance().createDefaultTable(jurySwornInTableModel);
            // Bug X54067 - restrict number of characters allowed for juror
            // number
            jurorTable.getColumnModel().getColumn(JurySwornInTableModel.JUROR_NO_COLUMN).setCellEditor(
                    new XLimitedLengthCellEditor(JUROR_NUMBER_MAX_LENGTH));
            jurorTable.setMinimumSize(juryTableSize);
            jurorTable.setRowHeight(XHIBITConstant.getLineHeight());
            jurorTable.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });

            jurorTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });

            // Tab action added to JuryTable so that the user cannot
            // tab or shift tab into the index column.
            // Handles tab action
            String tabActionName = "Tab";
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

            jurorTable.initColumnSizes(new String[] { "99 ", XTableFactory.COLUMN_WIDTH_UNDEFINED, " Juror No.  " },
                    (int) juryTableSize.getWidth());

        }
        // Ensure number is right justified
        jurorTable.getColumnModel().getColumn(JurySwornInTableModel.JUROR_NO_COLUMN).setCellRenderer(
                new RightJustifyTextRenderer());
        return jurorTable;
    }
}
