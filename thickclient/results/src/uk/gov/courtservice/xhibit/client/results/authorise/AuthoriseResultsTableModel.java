package uk.gov.courtservice.xhibit.client.results.authorise;

import java.util.Collection;
import java.util.Vector;

import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;

/**
 * <p>
 * Title: AAuthoriseResultsTableModel
 * </p>
 * <p>
 * Description: The table model for AuthoriseResults
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.1
 * @history v1.1 Kelvin Davies 03/04/2009 For CCN1263
 *          Updated to include the new dateRecordSheetSent field.
 *          v1.2 Brian Hingston/Scott Atwell May 2011 for CCN 2878
 *          Updated to include new re-auth checkbox and amended reason combobox
 */

public class AuthoriseResultsTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;

    public static final int WARNING_FLAG = 0;
    
    public static final int DEFENDANT_NAME = 1;

    public static final int AUTHORISATION_STATE = 2;
    
    public static final int DATE_RECORDSHEET_SENT = 3;

    public static final int SELECT = 4;
    
    public static final int RESELECT = 5;
    
    public static final int AMENDED_REASON = 6;
    
    private AuthoriseResultsPanel panel = null;

    /**
     * Set up that will bring out the header information from resource bundles.
     * 
     * @param Collection
     */
    private void setup(Collection data) {
        setColumnNames(new String[] { 
                " ",
                lookupResource("results.authorise.defendantsTbl.defendantName"),
                lookupResource("results.authorise.defendantsTbl.status"),
                lookupResource("results.authorise.defendantsTbl.date"),  
                lookupResource("results.authorise.defendantsTbl.select"), 
                lookupResource("results.authorise.defendantsTbl.reselect"),
                lookupResource("results.authorise.defendantsTbl.reason")});
        this.setData(data);
    }

    public AuthoriseResultsTableModel() {
        super();
        Collection noDataYet = new Vector();
        setup(noDataYet);
    }
    
    public AuthoriseResultsTableModel(AuthoriseResultsPanel panel) {
    	this();
    	this.panel = panel;
    }

    public AuthoriseResultsTableModel(Collection param) {
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
        
    	if (isOutOfBounds(r)) {
    		return false;
    	}

        if (c == SELECT) {
            AuthoriseResultsTableRowModel myVO = (AuthoriseResultsTableRowModel) getData().elementAt(r);
            String temp = myVO.getAuthorisationStatus();
            boolean tempStatus = myVO.getPreviouslyAuthorised();

            if (temp == null) {
                result = true;
            } else {
                if ((temp.equalsIgnoreCase(AuthorisationValue.AUTHORISED_INITIAL_STATUS) &&
                        tempStatus == true)
                        || temp.equalsIgnoreCase(AuthorisationValue.AUTHORISED_READY)
                        || temp.equalsIgnoreCase(AuthorisationValue.AUTHORISED_EXPORTING)
                        || temp.equalsIgnoreCase(AuthorisationValue.AUTHORISED_EXPORT_SUCCESS)) {
                    result = false;
                } else {
                    result = true;
                }
            }
        
        }else if (c == RESELECT) {
            AuthoriseResultsTableRowModel myVO = (AuthoriseResultsTableRowModel) getData().elementAt(r);
            String temp = myVO.getAuthorisationStatus();
            boolean tempStatus = myVO.getPreviouslyAuthorised();

            if (temp == null) {
                result = false;
            } else {
                if (temp.equalsIgnoreCase(AuthorisationValue.AUTHORISED_INITIAL_STATUS) &&
                        tempStatus == true) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } else if (c == AMENDED_REASON) {
            AuthoriseResultsTableRowModel myVO = (AuthoriseResultsTableRowModel) getData().elementAt(r);
            String temp = myVO.getAuthorisationStatus();
            boolean tempStatus = myVO.getPreviouslyAuthorised();
            result = false;

            if (temp != null) {
                if (temp.equalsIgnoreCase(AuthorisationValue.AUTHORISED_INITIAL_STATUS) &&
                        tempStatus == true) {
                    if ((getValueAt(r, RESELECT) != null) && (((Boolean)getValueAt(r, RESELECT)).booleanValue() == true)) {
                        result = true;
                    }
                }
            }
        }
        
        return result;
    }

    public Object getValueAt(int r, int c) {
    	if (isOutOfBounds(r)) {
    		return "";
    	}
        AuthoriseResultsTableRowModel myVO = (AuthoriseResultsTableRowModel) getData().elementAt(r);
        switch (c) {
        case WARNING_FLAG:
            return myVO.getWarningFlag();
        case DEFENDANT_NAME:
            return myVO.getDefendantName();
        case DATE_RECORDSHEET_SENT:
            return myVO.getDateRecordSheetSent();
        case AUTHORISATION_STATE:
            return getAuthStatusMessage(myVO.getAuthorisationStatus());
        case SELECT:
            return myVO.isSelected();
        case RESELECT:
            return myVO.isReSelected();
        case AMENDED_REASON:
            return myVO.getAmendedReason();
        default:
            return "";
        }
    }

    public void setValueAt(Object value, int r, int c) {
        if (c == SELECT) {
            ((AuthoriseResultsTableRowModel) getData().elementAt(r)).setSelected(((Boolean) value));
            if(panel != null) { 
            	panel.stepUpdateViewState();
            }
        }else if (c == RESELECT) {
            ((AuthoriseResultsTableRowModel) getData().elementAt(r)).setReSelected(((Boolean) value));
            if(panel != null) { 
            	panel.stepUpdateViewState();
            }
        }else if (c == AMENDED_REASON) {
            ((AuthoriseResultsTableRowModel) getData().elementAt(r)).setAmendedReason(((String) value));
            if(panel != null) { 
            	panel.stepUpdateViewState();
            }
        }
    }

    public Class getColumnClass(int col) {
        switch (col) {
        case WARNING_FLAG:
        case DEFENDANT_NAME:
        case DATE_RECORDSHEET_SENT:
        case AUTHORISATION_STATE:
            return String.class;
        case SELECT:
            return Boolean.class;
        case RESELECT:
            return Boolean.class;
        case AMENDED_REASON:
            return JComboBox.class;
        default:
            return Object.class;
        }
    }
    
    private boolean isOutOfBounds(int rowNum) {
    	if (rowNum >= data.size()) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public String getAuthStatusMessage(String param) {
        StringBuffer buf = new StringBuffer("results.authorise.status.");
        buf.append(param == null || param.equals("") ? "default" : param);
        return lookupResource(buf.toString());
    }

    private String lookupResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key);
    }
}
