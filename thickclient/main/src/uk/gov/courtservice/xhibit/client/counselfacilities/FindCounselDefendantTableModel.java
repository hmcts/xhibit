package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: FindCounselDefendantTableModel
 * </p>
 * <p>
 * Description: The table Model for finding a counsel/defendant
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

public class FindCounselDefendantTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;

    // Counsel columns
    public static final int COURTROOM = 0;

    public static final int CASE_NUMBER = 1;

    public static final int TIME = 2;

    public static final int DEFENDANT = 3;

    public static final int LEGAL_ROLE = 4;

    public static final int COUNSEL = 5;

    public static final String DCD = "dcd";

    // type of search, either counsel or defendant
    private String searchType;


    // the resource bundle for counsel facility
    private String resources = XhibitBundles.CounselFacilities;

    /**
     * This constructor will set the radio button to default if the passed in
     * boolean is true else it will remain as it was.
     * 
     * @param setSearchtype -
     *            boolean
     */
    public FindCounselDefendantTableModel(boolean setSearchtype) {
        super();
        Collection noDataYet = new Vector();
        if (setSearchtype) {
            this.searchType = CounselFacilitiesHelper.COURADIO;
        }
        setup(noDataYet);
    }

    public FindCounselDefendantTableModel(Collection param, String searchType) {
        super();
        this.searchType = searchType;
        setup(param);
    }

    private void setup(Collection data) {
        setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colCourtRoom"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colCaseNumber"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colTimeListed"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colDefendant"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colRoleInCase"),
                XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "colCounsel") });

        // helper.sortByCourtRoomAssignLegalRep(data);
        CounselFacilitiesHelper.sortByCourtRoom(data);
        this.setData(new Vector(data));
    }

    public boolean isCellEditable(int r, int c) {
        return false;
    }

    /**
     * Build up the data for each cell.
     * 
     * @param r
     *            the row in the table
     * @param c
     *            the cell to populate the data for
     * @return Object - random object
     */
    public Object getValueAt(int r, int c) {
        Object cell = "";

        try {
            PartyOnCaseValue myVO = (PartyOnCaseValue) getData().elementAt(r);
            switch (c) {
            case COURTROOM:
                StringBuffer buf = new StringBuffer();
                if (myVO.getCourtSiteShortName() != null && myVO.getCourtSiteShortName().length() > 0) {
                    buf.append(myVO.getCourtSiteShortName());
                    buf.append(" - ");
                }
                // if the case if floating we only want to disply it as
                // unassigned.
                if (myVO.getIsFloating().equalsIgnoreCase(CounselFacilitiesHelper.UNASSIGNED)) {
                    buf.append(XHIBITConstant.getResource(resources, "dcdUnassigned"));
                } else {
                    buf.append(myVO.getCourtRoomDisplayName());
                }
                return buf.toString();
            case CASE_NUMBER:
                return CounselFacilitiesHelper.getCaseTypeAndNumber(myVO.getCaseType(), myVO.getCaseNumber());
            case TIME:
                return CounselFacilitiesHelper.getDateTime(myVO.getTimeListed(), XDateFormat.TIMEFORMAT);
            case DEFENDANT:
                return CounselFacilitiesHelper.getDefendantNames(myVO.getDefendants());
            case LEGAL_ROLE:
                return (XHIBITConstant.getResource(XhibitBundles.CounselFacilities, FindCounselDefendantTableModel.DCD
                        + myVO.getPartyRole()));
            case COUNSEL:
                return CounselFacilitiesHelper.getFullNamesAndChamberAndAddress(myVO.getRepresentatives());
            default:
                return cell;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            XHIBITConstant.error("XHIBITTableModel : cell[" + r + "," + c
                    + "] caused an ArrayIndexOutOfBoundsException");
            return cell;
        }
    }
}
