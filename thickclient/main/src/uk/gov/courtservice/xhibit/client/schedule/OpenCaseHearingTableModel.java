package uk.gov.courtservice.xhibit.client.schedule;

import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
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

public class OpenCaseHearingTableModel extends XHIBITDefaultTableModel // XVOTableModelHelper
{

    public OpenCaseHearingTableModel(Object[] objArray) {
        super();
        setData(objArray);
        ResourceBundle rb = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");
        setColumnNames(new String[] { rb.getString("caseColumn"), rb.getString("hearingTypeColumn"),
                rb.getString("defendantColumn") });
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        String cellValue;
        ScheduledHearingValue shv = ((ScheduledHearingValueHelper) _data[rowIndex]).getModel();
        // ((ScheduledHearingValueHelper)getRow(rowIndex)).getModel();

        switch (columnIndex) {
        case 0:
            cellValue = (shv.getCaseType()) + (shv.getCaseNumber());
            break;
        case 1:
            cellValue = checkNull(shv.getHearingTypeDesc());
            break;
        case 2:
            cellValue = getDefendantString(shv.getDefendants());
            break;
        default:
            cellValue = null;
            break;
        }

        return cellValue;
    }

    private String checkNull(String toCheck) {
        if (toCheck == null) {
            return "";
        } else {
            return toCheck;
        }
    }

    private String getDefendantString(String[] listDefendant) {
        if (listDefendant == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < listDefendant.length; i++) {
            sb.append(listDefendant[i]);
            if (i != (listDefendant.length - 1))
                sb.append(", ");
        }
        return sb.toString();
    }

    public boolean isCellEditable(int r, int c) {
        return false;
    }

    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
}