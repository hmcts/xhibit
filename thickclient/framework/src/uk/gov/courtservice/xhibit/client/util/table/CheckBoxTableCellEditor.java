package uk.gov.courtservice.xhibit.client.util.table;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

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
 * @author unascribed
 * @version 1.0
 */
public class CheckBoxTableCellEditor extends DefaultCellEditor {
    public CheckBoxTableCellEditor(JCheckBox checkBox) {
        super(checkBox);

        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.setVerticalAlignment(SwingConstants.CENTER);
    }
}
