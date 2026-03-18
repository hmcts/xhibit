package uk.gov.courtservice.xhibit.client.admin.courtofappeal;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Vector;

import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
  
 
public class CourtOfAppealTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;
    
    public static final int DEFENDANT_NAME = 0;

    public static final int DATE_OF_RECEIPT = 1;
    
    public static final int DATE_PAPERS_SENT = 2;

    public static final int DATE_OF_RESULT = 3;
    
    public static final int APPEAL_RESULT = 4;
    
    public static final int STATUS = 5;

    /**
     * Set up that will bring out the header information from resource bundles.
     * 
     * @param Collection
     */
    private void setup(Collection data) {
        setColumnNames(new String[] { 
                lookupResource("admin.courtofappeal.defendantsTbl.defendantName"),
                lookupResource("admin.courtofappeal.defendantsTbl.dateOfReceipt"),  
                lookupResource("admin.courtofappeal.defendantsTbl.datePapersSent"),
                lookupResource("admin.courtofappeal.defendantsTbl.appealHearing"), 
                lookupResource("admin.courtofappeal.defendantsTbl.appealResult"),
                lookupResource("admin.courtofappeal.defendantsTbl.status")});
   
        this.setData(data);
    }

    public CourtOfAppealTableModel() {
        super();
        Collection noDataYet = new Vector();
        setup(noDataYet);
    }

    public CourtOfAppealTableModel(Collection param) {
        super();
        setup(param);
    }

    /**
     * Method to check if the results may be authorised for the current
     * defendant. Authorisations cannot be performed for statuses R, U and E.
     * 
     * @param r
     * @param c
     * @return boolean
     */
    public boolean isCellEditable(int r, int c) {
        boolean result = false;
 
        return result;
    }

	public Object getValueAt(int r, int c) {
		CourtOfAppealTableRowModel myVO = (CourtOfAppealTableRowModel) getData().elementAt(r);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		switch (c) {
		case DEFENDANT_NAME:
			return myVO.getDefendantName();
		case DATE_PAPERS_SENT:
			if (myVO.getDatePapersSent() != null) {
				return sdf.format(myVO.getDatePapersSent()).toUpperCase();
			}
			break;
		case DATE_OF_RECEIPT:
			if (myVO.getDateOfReceipt() != null) {
				return sdf.format(myVO.getDateOfReceipt()).toUpperCase();
			}
			break;
		case DATE_OF_RESULT:
			if (myVO.getDateOfResult() != null) {
				return sdf.format(myVO.getDateOfResult()).toUpperCase();
			}
			break;
		case APPEAL_RESULT:
			if (myVO.getAppealResult() != null) {
				return myVO.getAppealResult();
			} else {
				return "";
			}
		case STATUS:
			if ("S".equals(myVO.getStatus())) {
				return "Sent to Portal";
			} else if ("P".equals(myVO.getStatus()) || "E".equals(myVO.getStatus())) {
				return "Send in Progress";
			} else {
				return "Not Sent to Portal";
			}
		default:
			return "";
		}
		return "";
	}

    public void setValueAt(Object value, int r, int c) {
         
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case DEFENDANT_NAME:
        case DATE_PAPERS_SENT:
        case DATE_OF_RECEIPT:
            return String.class;
        case APPEAL_RESULT:
            return String.class;
        case DATE_OF_RESULT:
            return String.class;
        case STATUS:
        default:
            return Object.class;
        }
    }
 

    private String lookupResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key);
    }
}