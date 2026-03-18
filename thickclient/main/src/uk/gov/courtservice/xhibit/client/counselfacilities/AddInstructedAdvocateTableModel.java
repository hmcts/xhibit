package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Vector;

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
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class AddInstructedAdvocateTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;
    
    public static final int FULL_NAME_COLUMN = 0;

    public static final int CHAMBERS_NAME_COLUMN = 1;

    public static final int CHAMBERS_ADDRESS_COLUMN = 2;

    private void setup(Collection data) {
        String[] columnNames = new String[] { 
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colName"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colChambers"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colAddress") };

        this.setColumnNames(columnNames);
        this.setData(data);
    }

    public AddInstructedAdvocateTableModel() {
        super();
        Collection noDataYet = new Vector();
        setup(noDataYet);
    }

    public AddInstructedAdvocateTableModel(Collection param) {
        super();
        setup(param);
    }

    public boolean isCellEditable(int r, int c) {
        return false;
    }

    public Object getValueAt(int r, int c) {
        String cellValue;
        FindInstructedAdvocateTableRowModel myVO = 
            (FindInstructedAdvocateTableRowModel) getDataAt(r);
        
        switch (c) {
        case FULL_NAME_COLUMN:
            cellValue = (myVO.getFullName() == null || myVO.getFullName().equals("") ? "---" : myVO.getFullName());
            break;
        case CHAMBERS_NAME_COLUMN:
            cellValue = (myVO.getChambersName() == null || myVO.getChambersName().equals("") ? "---" : myVO.getChambersName());
            break;
        case CHAMBERS_ADDRESS_COLUMN:
            cellValue = (myVO.getAddressLine01() == null || myVO.getAddressLine01().equals("") ? "---" : myVO.getAddressLine01());
            break;
        default:
            cellValue = "";
            break;
        }

        return cellValue;
    }
}


