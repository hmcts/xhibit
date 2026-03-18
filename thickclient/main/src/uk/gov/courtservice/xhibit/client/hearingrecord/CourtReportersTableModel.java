package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.util.Date;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCourtReporterValue;
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
 * @author Sherie De Silva
 * @version 1.0
 * 
 * <Change History/>
 * 
 * <P>
 * 02/06/03 - MH - Added Initials.
 * </P>
 * 
 */

public class CourtReportersTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;
    
    public CourtReportersTableModel(HearingRecordModel model) {
        super();

        setColumnNames(new String[] { 
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "name"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "startDate"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "endDate") });

        Vector<Vector<Object>> vec = new Vector<Vector<Object>>();

        Vector<HRScheduledHearingValue> schedHearingsVec = 
            (Vector<HRScheduledHearingValue>) model.getHearingRecordVal().getHearingRecordDisplayValue()
                .getHrHearingDisplayValue().getHrScheduledHearingValues();

        // Use crest court reporter Ids to determine if
        // two reporters are the same reporter.
        boolean useCrestCourtReporterIds = true;
        
        for (int i = 0; i < schedHearingsVec.size(); i++) {
            Vector<Object> v = new Vector<Object>();
            HRScheduledHearingValue schedHearingVal = schedHearingsVec.get(i);

            if (schedHearingVal != null) {
                if ((schedHearingVal.getHrCourtReporter() != null) && (schedHearingVal.getHrCourtReporter().size() > 0)) {
                    // Although court reporter value objects are in a
                    // collection, there should only be one court reporter
                    // per scheduled hearing
                    HRCourtReporterValue crVal = (HRCourtReporterValue) (((Vector) (schedHearingVal
                            .getHrCourtReporter())).get(0));
                    v.add(crVal.getRefCourtReporterID());
                    v.add(schedHearingVal.getOriginalTime());

                    // getting court reporter name as one string
                    String crName = "";
                    if ((crVal.getFirstName() != null) && (!crVal.getFirstName().equals(""))) {
                        crName = crName + crVal.getFirstName() + " ";
                    }
                    if ((crVal.getMiddleName() != null) && (!crVal.getMiddleName().equals(""))) {
                        crName = crName + crVal.getMiddleName() + " ";
                    }
                    // MH - added initials
                    if ((crVal.getInitials() != null) && (!crVal.getInitials().equals(""))) {
                        crName = crName + crVal.getInitials() + " ";
                    }
                    if ((crVal.getSurname() != null) && (!crVal.getSurname().equals(""))) {
                        crName = crName + crVal.getSurname();
                    }
                    v.add(crName);
                    
                    if (crVal.getCrestCourtReporterId() != null) {
                        v.add(crVal.getCrestCourtReporterId());
                    } else {
                        // If any are null then fall back to using Xhibit court reporter ids.
                        // I dont think this can ever happen, but the crestCourtReporter field
                        // is nullable in the database.
                        useCrestCourtReporterIds = false;
                    }

                    vec.add(v);
                }
            }
        }

        Vector<Vector<String>> crData = new Vector<Vector<String>>();
        Vector<Integer> addedIds = new Vector<Integer>();

        for (int i = 0; i < vec.size(); i++) {
            Integer crId;
            if (useCrestCourtReporterIds) {
                crId = (Integer) (((Vector) vec.get(i)).get(3));
            } else {
                crId = (Integer) (((Vector) vec.get(i)).get(0));
            }
            
            Date time = (Date) (((Vector) vec.get(i)).get(1));
            Date startDate = (Date)time.clone();
            Date endDate = (Date)time.clone();
            String name = (String) (((Vector) vec.get(i)).get(2));

            for (int j = i; j < vec.size(); j++) {
                Integer crId2;
                if (useCrestCourtReporterIds) {
                    crId2 = (Integer) (((Vector) vec.get(j)).get(3));
                } else {
                    crId2 = (Integer) (((Vector) vec.get(j)).get(0));
                }
                
                Date time2 = (Date) (((Vector) vec.get(j)).get(1));

                if (crId.equals(crId2)) {
                    if (time2.before(startDate)) {
                        startDate = (Date)time2.clone();
                    }

                    if (time2.after(endDate)) {
                        endDate = (Date)time2.clone();
                    }
                }
            }

            // creating a vector representing a row in table
            Vector<String> v2 = new Vector<String>();
            v2.add(name);
            v2.add(XDateFormat.format(startDate, XDateFormat.DATEFORMAT));
            v2.add(XDateFormat.format(endDate, XDateFormat.DATEFORMAT));

            // checking to make sure that a court reporter is only added to
            // table data once.

            boolean alreadyAdded = addedIds.contains(crId);

            if (!alreadyAdded) {
                crData.add(v2);
                addedIds.add(crId);
            }
        }

        this.setData(crData);
    }
}