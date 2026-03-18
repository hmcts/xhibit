package uk.gov.courtservice.xhibit.client.schedule;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class OpenCaseCourtModel extends XHIBITDefaultTableModel // XVOTableModelHelper
{

    public OpenCaseCourtModel(Object[] objArray) {
        super();
        setData(objArray);
        ResourceBundle rb = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");
        setColumnNames(new String[] { rb.getString("courtColumn") });
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        String cellValue;

        switch (columnIndex) {
        case 0:
            cellValue = checkNull(_data[rowIndex]).toString();
            break;
        default:
            cellValue = "";
            break;
        }

        return cellValue;
    }

    private Object checkNull(Object toCheck) {
        if (toCheck == null) {
            return "";
        } else {
            return toCheck;
        }
    }
}