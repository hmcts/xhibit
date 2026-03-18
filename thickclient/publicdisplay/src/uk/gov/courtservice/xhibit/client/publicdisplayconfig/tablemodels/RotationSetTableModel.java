package uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description: List the rotation sets in a court
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: RotationSetTableModel.java,v 1.4 2006/06/05 12:32:07 bzjrnl Exp $
 */

public class RotationSetTableModel extends XHIBITDefaultTableModel {
    private static final String TABLECOL_KEY1 = "pd.tablecol.rotationSets";

    public RotationSetTableModel(Object[] data) {
        super(data);
        setColumnNames(new String[] { PublicDisplayUtils.getResource(TABLECOL_KEY1) });
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        RotationSetComplexValue row = (RotationSetComplexValue) _data[rowIndex];
        return row.getRotationSetBasicValue().getDescription();
    }
}