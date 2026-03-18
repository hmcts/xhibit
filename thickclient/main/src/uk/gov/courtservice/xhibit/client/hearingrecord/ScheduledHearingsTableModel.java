package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
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
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ScheduledHearingsTableModel extends XHIBITTableModel {
    private HearingRecordModel model;

    public static final int DATE_COLUMN = 0;

    public ScheduledHearingsTableModel(HearingRecordModel model) {
        super();

        this.model = model;

        // Vector vCol = new Vector();
        // vCol.add(XHIBITConstant.getResource(XhibitBundles.HearingRecord,
        // "date"));
        // this.setColumnNames(vCol);
        this.setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "date") });

        Vector shData = new Vector();
        Collection shValues = model.getHearingRecordVal().getHearingRecordDisplayValue().getHrHearingDisplayValue()
                .getHrScheduledHearingValues();
        if ((shValues != null) && (shValues.size() > 0)) {
            for (int i = 0; i < shValues.size(); i++) {
                Vector v = new Vector();
                HRScheduledHearingValue shVal = (HRScheduledHearingValue) (((Vector) shValues).get(i));
                v.add(shVal.getOriginalTime());
                shData.add(v);
            }
        }
        this.setData(shData);

        /*
         * //data for testing Vector data = new Vector(); Vector v1 = new
         * Vector(); Vector v2 = new Vector();
         * 
         * v1.add("01/01/02"); v2.add("03/01/02");
         * 
         * data.add(v1); data.add(v2); this.setData(data);
         */
    }

    public Object getValueAt(int r, int c) {
        Vector myVector = (Vector) getData().elementAt(r);
        switch (c) {
        case DATE_COLUMN:
            StringBuffer buf = new StringBuffer();
            buf.append(XDateFormat.format((Date) myVector.get(0), XDateFormat.DATEFORMAT));
            buf.append(" ");
            buf.append(XDateFormat.format((Date) myVector.get(0), XDateFormat.TIMEFORMAT));
            return buf.toString();
        default:
            return "";
        }
    }
}