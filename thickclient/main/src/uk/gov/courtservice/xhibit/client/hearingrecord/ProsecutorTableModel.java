package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.util.Vector;

import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCounselValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRStartEndDates;
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
 */

public class ProsecutorTableModel extends XHIBITTableModel {
    private HearingRecordModel model;

    private Vector data = new Vector();

    public ProsecutorTableModel(HearingRecordModel model) {
        super();
        this.setData(model);

    }

    public void setData(HearingRecordModel model) {
        // Remove all elements the data vector had before,then add the new set
        // of data.
        this.data.removeAllElements();
        this.model = model;

        setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "name"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "address"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "role"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "attendanceDates") });

        Vector counselVec = (Vector) (model.getHearingRecordVal().getHearingRecordDisplayValue().getHrCounselValue());

        if (counselVec != null) {
            for (int i = 0; i < counselVec.size(); i++) {
                Vector v = new Vector();
                HRCounselValue hrCounselVal = (HRCounselValue) counselVec.get(i);

                if ((hrCounselVal.getLegalRole().equals(HearingRecordConstants.LEGAL_ROLE_PROSECUTION))
                        || (hrCounselVal.getLegalRole().equals(HearingRecordConstants.LEGAL_ROLE_RESPONDANT))
                        || (hrCounselVal.getLegalRole().equals(HearingRecordConstants.LEGAL_ROLE_OBJECTOR))
                        || (hrCounselVal.getLegalRole().equals(HearingRecordConstants.LEGAL_ROLE_THIRD_PARTY))) {
                    String name = "";
                    if ((hrCounselVal.getFirstName() != null) && (!hrCounselVal.getFirstName().equals(""))) {
                        name = name + hrCounselVal.getFirstName() + " ";
                    }
                    if ((hrCounselVal.getMiddleName() != null) && (!hrCounselVal.getMiddleName().equals(""))) {
                        name = name + hrCounselVal.getMiddleName() + " ";
                    }
                    if ((hrCounselVal.getSurname() != null) && (!hrCounselVal.getSurname().equals(""))) {
                        name = name + hrCounselVal.getSurname();
                    }

                    v.add(name);

                    AddressBasicValue addressVal = hrCounselVal.getAddressBasicValue();
                    String address = "";

                    if (addressVal != null) {
                        if ((addressVal.getAddress1() != null) && (!addressVal.getAddress1().equals(""))) {
                            address = address + addressVal.getAddress1() + " ";
                        }
                        if ((addressVal.getAddress2() != null) && (!addressVal.getAddress2().equals(""))) {
                            address = address + addressVal.getAddress2() + " ";
                        }
                        if ((addressVal.getAddress3() != null) && (!addressVal.getAddress3().equals(""))) {
                            address = address + addressVal.getAddress3() + " ";
                        }
                        if ((addressVal.getAddress4() != null) && (!addressVal.getAddress4().equals(""))) {
                            address = address + addressVal.getAddress4() + " ";
                        }
                        if ((addressVal.getTown() != null) && (!addressVal.getTown().equals(""))) {
                            address = address + addressVal.getTown() + " ";
                        }
                        if ((addressVal.getCounty() != null) && (!addressVal.getCounty().equals(""))) {
                            address = address + addressVal.getCounty() + " ";
                        }
                        if ((addressVal.getPostcode() != null) && (!addressVal.getPostcode().equals(""))) {
                            address = address + addressVal.getPostcode() + " ";
                        }
                        if ((addressVal.getCountry() != null) && (!addressVal.getCountry().equals(""))) {
                            address = address + addressVal.getCountry() + " ";
                        }
                    }

                    v.add(address);
                    v.add(hrCounselVal.getLegalRepType());
                    v.add(buildStartEndDates(hrCounselVal.getStartEndDatesArray()));

                    data.add(v);
                }
            }
        }

        this.setData(data);
    }

    /**
     * Returns a String that shows all the dates( from and to ) on which the
     * legal representative attended hearings
     * 
     * @param startEndDates
     * @return String
     */
    private static String buildStartEndDates(final HRStartEndDates[] startEndDates) {
        StringBuffer allDates = new StringBuffer();
        boolean first = true;

        if (startEndDates != null) {
	        for (int x = 0; x < startEndDates.length; x++) {
	            if (first) {
	                first = false;
	            } else {
	                allDates.append(",\n");
	            }
	            allDates.append(XDateFormat.format(startEndDates[x].getStartDate(), XDateFormat.DATEFORMAT));
	            allDates.append(" - ");
	            allDates.append(XDateFormat.format(startEndDates[x].getEndDate(), XDateFormat.DATEFORMAT));
	        }
        }

        return allDates.toString();
    }
}