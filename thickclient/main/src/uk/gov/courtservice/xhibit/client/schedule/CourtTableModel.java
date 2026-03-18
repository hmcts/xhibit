package uk.gov.courtservice.xhibit.client.schedule;

import java.util.Date;
import java.util.ResourceBundle;

import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitHelper;

public class CourtTableModel extends XHIBITDefaultTableModel {

    public static final int COURT_COLUMN = 0;

    public static final int CASE_COLUMN = 1;

    public static final int JUDGE_COLUMN = 2;

    public static final int DEFENDANT_COLUMN = 3;

    public static final int HEARING_COLUMN = 4;

    public static final int TIME_COLUMN = 5;

    public static final int STATUS_COLUMN = 6;

    private String unassigned;

    private ResourceBundle rb;

    public CourtTableModel(Object[] arraySHV) {
        super();
        setData(arraySHV);
        rb = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");
        setColumnNames(new String[] { rb.getString("courtColumn"), rb.getString("caseColumn"),
                rb.getString("judgeColumn"), rb.getString("defendantColumn"), rb.getString("hearingTypeColumn"),
                rb.getString("notBeforeColumn"), rb.getString("caseStatusColumn") });
        unassigned = XHIBITConstant.getResource(rb, "unassigned");
    }

    public Class getColumnClass(int c) {
        return String.class;
    }

    public Object getValueAt(int r, int c) {

        String cellValue;

        if (_data.length <= 0)
            return null;
        ScheduledHearingValue shv = (ScheduledHearingValue) _data[r];
        switch (c) {
        case 0:
            Boolean isFloating = shv.getIsFloating();
            if (isFloating != null && isFloating.booleanValue()) {
                cellValue = unassigned;
            } else {
                if (shv.getCourtSiteShortName() != null && shv.getCourtSiteShortName().length() > 0) {
                    cellValue = shv.getCourtSiteShortName() + " - " + checkNull(shv.getCourtRoomDisplayName());
                } else {
                    cellValue = checkNull(shv.getCourtRoomDisplayName());
                }
            }
            break;
        case 1:
            cellValue = checkNull(shv.getCaseType()) + checkNull(shv.getCaseNumber().toString());
            break;
        case 2:
            cellValue = checkNull(shv.getJudge());
            break;
        case 3:
            cellValue = ScheduleHelper.getDefendantString(shv.getDefendants(), shv.getCaseBasicValue());
            break;
        case 4:
            cellValue = checkNull(shv.getHearingTypeDesc());
            break;
        case 5:
            cellValue = getFormattedTime(shv.getNotBeforeTime());
            break;
        case 6:
            cellValue = getStatusColumn(shv.getHearingProgress(), shv.getMovedFromCourtRoomName());
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

    private String getFormattedTime(Date t) {
        if (t == null) {
            return "";
        } else {
            return XDateFormat.format(t, XDateFormat.TIMEFORMAT);
        }
    }

    public String getStatusColumn(Integer intStatus, String strMoved) {
        String tempStatus = XhibitHelper.getHearingProgress(intStatus);
        String tempMoved = strMoved;
        if (tempMoved == null)
            tempMoved = "";

        if (tempMoved.length() <= 0) {
            return tempStatus;
        } else {
            StringBuffer s = new StringBuffer(tempStatus);
            s.append("\n");
            s.append(rb.getString("movedCourt"));
            s.append(' ');
            s.append(tempMoved);
            return s.toString();
        }
    }

    public boolean isCellEditable(int r, int c) {
        return false;
    }
}
