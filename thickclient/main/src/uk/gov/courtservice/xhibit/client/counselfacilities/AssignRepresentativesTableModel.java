package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: AssignRepresentativesTableModel
 * </p>
 * <p>
 * Description: The table model for Assign Representatives
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class AssignRepresentativesTableModel extends XHIBITTableModel {
    
    private static final long serialVersionUID = 1L;

    public static final int COURTROOM = 0;

    public static final int CASE_NUMBER = 1;

    public static final int TIME_LISTED = 2;

    public static final int ROLE_IN_CASE = 3;

    public static final int PARTY = 4;

    public static final int SELECT = 5;

    public static final int REPRESENTATIVES = 6;


    /**
     * Set up that will bring out the header information from resource bundles.
     * 
     * @param Collection
     */
    private void setup(Collection data) {
        setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colCourtRoom"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colCaseNumber"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colTimeListed"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colRoleInCase"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colPartyName"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colSelect"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colLegalReps") });
        this.setData(data);
    }

    public AssignRepresentativesTableModel() {
        super();
        Collection noDataYet = new Vector();
        setup(noDataYet);
    }

    public AssignRepresentativesTableModel(Collection param) {
        super();
        setup(param);
    }

    /**
     * Method to check if the counsel can sign into this particular case and
     * defendant. A counsel can be signed in if the case type is A, S or T.
     * 
     * @param r
     * @param c
     * @return boolean
     */
    public boolean isCellEditable(int r, int c) {
        boolean result = true;

        if (c == SELECT) {
            AssignRepresentativesTableRowModel myVO = (AssignRepresentativesTableRowModel) getData().elementAt(r);

            if (myVO.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_A)
                    || myVO.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_S)
                    || myVO.getCaseType().equalsIgnoreCase(CounselFacilitiesHelper.CASE_TYPE_T)) {
                if ((myVO.getScheduledHearingDefendantId() != null && myVO.getScheduledHearingDefendantId().intValue() != 0)
                        || (myVO.getRole().equalsIgnoreCase(CounselFacilitiesHelper.ROLE_PROSECUTION)
                                || myVO.getRole().equalsIgnoreCase(CounselFacilitiesHelper.ROLE_RESPONDENT) || myVO
                                .getRole().equalsIgnoreCase(CounselFacilitiesHelper.ROLE_OBJECTOR))) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    public Object getValueAt(int r, int c) {
        AssignRepresentativesTableRowModel myVO = (AssignRepresentativesTableRowModel) getData().elementAt(r);
        switch (c) {
        case COURTROOM:
            StringBuffer buf = new StringBuffer();
            if (myVO.getCourtSiteShortName() != null && myVO.getCourtSiteShortName().length() > 0) {
                buf.append(myVO.getCourtSiteShortName());
                buf.append(" - ");
            }
            buf.append(myVO.getCourtRoom());
            return buf.toString();
        case CASE_NUMBER:
            return CounselFacilitiesHelper.getCaseTypeAndNumber(myVO.getCaseType(), myVO.getCaseNumber());
        case TIME_LISTED:
            return myVO.getTimeListed();
        case ROLE_IN_CASE:
            return (XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "dcd" + myVO.getRole()));
        case PARTY:
            return myVO.getParty();
        case SELECT:
            return myVO.isSelected();
        case REPRESENTATIVES:
            return myVO.getRepresentatives();
        default:
            return "";
        }
    }

    public void setValueAt(Object value, int r, int c) {
        if (c == SELECT) {
            ((AssignRepresentativesTableRowModel) getData().elementAt(r)).setSelected(((Boolean) value));
        }
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case COURTROOM:
        case CASE_NUMBER:
        case TIME_LISTED:
        case ROLE_IN_CASE:
        case PARTY:
        case REPRESENTATIVES:
            return String.class;
        case SELECT:
            return Boolean.class;
        default:
            return Object.class;
        }
    }
}
