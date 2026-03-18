package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class LookupForceLocationTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;
    
    public static final int LOCATION_CODE_COLUMN = 0;

    public static final int FORCE_NAME_COLUMN = 1;


    private void setup(List<RefSystemCodeBasicValue> data) {
        String[] columnNames = new String[] { 
                XHIBITConstant.getResource(XhibitBundles.AddCountsDefendantsResources, "forceCodeColumn"),
                XHIBITConstant.getResource(XhibitBundles.AddCountsDefendantsResources, "forceNameColumn") };

        this.setColumnNames(columnNames);
        this.setData(data);
    }

    public LookupForceLocationTableModel(List<RefSystemCodeBasicValue> param) {
        super();
        setup(param);
    }
    
    public boolean isCellEditable(int r, int c) {
        return false;
    }

    public Object getValueAt(int r, int c) {
        Object cellValue;
        RefSystemCodeBasicValue myVO = 
            (RefSystemCodeBasicValue) getDataAt(r);
        
        switch (c) {
        case LOCATION_CODE_COLUMN:
            cellValue = myVO.getCode();
            break;
        case FORCE_NAME_COLUMN:
            cellValue = myVO.getDecode();
            break;
        default:
            cellValue = "";
            break;
        }

        return cellValue;
    }
    
    public Class getColumnClass(int col) {
        switch (col) {
        case LOCATION_CODE_COLUMN:
        case FORCE_NAME_COLUMN:
            return String.class;
        default:
            return Object.class;
        }
    }
}

