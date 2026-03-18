package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

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

public class EditPostTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;
    
    public static final int FULL_NAME_COLUMN = 0;

    public static final int CHAMBERS_NAME_COLUMN = 1;

    public static final int CHAMBERS_ADDRESS_COLUMN = 2;
    
    public static final int UNAVAILABLE_FLAG = 3;
    
    public static final int WITHDRAWN_FLAG = 4;
    

    private void setup(Collection<FindInstructedAdvocateTableRowModel> data) {
        String[] columnNames = new String[] { 
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colName"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colChambers"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colAddress"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colUnavailable"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colWithdrawn")};

        this.setColumnNames(columnNames);
        this.setData(data);
    }

    public EditPostTableModel(Collection<FindInstructedAdvocateTableRowModel> param) {
        super();
        setup(param);
    }

    public void add(FindInstructedAdvocateTableRowModel trm) {
        Vector<Object> data = getData();

        if (!data.contains(trm)) {
            data.add(trm);
            this.fireTableDataChanged();
        }
    }
    
    public boolean isCellEditable(int r, int c) {
        FindInstructedAdvocateTableRowModel myVO = 
            (FindInstructedAdvocateTableRowModel) getDataAt(r);
        
        if (myVO == null) 
            return false;
        
        if (!myVO.isWithdrawn() && (c == UNAVAILABLE_FLAG || c == WITHDRAWN_FLAG)) {
            return true;
        }
        
        return false;
    }

    public Object getValueAt(int r, int c) {
        Object cellValue;
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
        case UNAVAILABLE_FLAG:
            cellValue = new Boolean(myVO.getAvailable() != null 
                    && myVO.getAvailable().equals(InstructedAdvocateHelper.UNAVAILABLE_FLAG));
            break;
        case WITHDRAWN_FLAG:
            cellValue = new Boolean(myVO.getAvailable() != null 
                    && myVO.getAvailable().equals(InstructedAdvocateHelper.WITHDRAWN_FLAG));
            break;
        default:
            cellValue = "";
            break;
        }

        return cellValue;
    }
    
    public void setValueAt(Object value, int r, int c) {
        if (c == UNAVAILABLE_FLAG) {
            this.fireTableRowsUpdated(r, r);
            this.fireTableDataChanged();
            Boolean selected = (Boolean)value;
            if (selected.booleanValue()) {
                ((FindInstructedAdvocateTableRowModel) getData().elementAt(r))
                    .setAvailable(InstructedAdvocateHelper.UNAVAILABLE_FLAG);
            } else {
                ((FindInstructedAdvocateTableRowModel) getData().elementAt(r))
                    .setAvailable(null);
            }
        } else if (c == WITHDRAWN_FLAG) {
            this.fireTableRowsUpdated(r, r);
            this.fireTableDataChanged();
            Boolean selected = (Boolean)value;
            if (selected.booleanValue()) {
                ((FindInstructedAdvocateTableRowModel) getData().elementAt(r))
                    .setAvailable(InstructedAdvocateHelper.WITHDRAWN_FLAG);
            } else {
                ((FindInstructedAdvocateTableRowModel) getData().elementAt(r))
                    .setAvailable(null);
            }
        }
    }
    
    public Class getColumnClass(int col) {
        switch (col) {
        case FULL_NAME_COLUMN:
        case CHAMBERS_NAME_COLUMN:
        case CHAMBERS_ADDRESS_COLUMN:
            return String.class;
        case UNAVAILABLE_FLAG:
        case WITHDRAWN_FLAG:
            return Boolean.class;
        default:
            return Object.class;
        }
    }
    
    public boolean noMoreThanOneAvailableAdvocate() {
        Collection<FindInstructedAdvocateTableRowModel> data = getData();
        
        int availableAdvocates = 0;
        
        Iterator<FindInstructedAdvocateTableRowModel> iter = data.iterator();
        while (iter.hasNext()) {
            FindInstructedAdvocateTableRowModel item = iter.next();
            if (item.isAvailable()) {
                ++availableAdvocates;
                if (availableAdvocates > 1) {
                    return false;
                }
            }
        }
        
        return true;
    }
}



