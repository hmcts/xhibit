package uk.gov.courtservice.xhibit.client.results.authorise;

import java.util.Vector;
import java.util.Date;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;

/**
 * <p>
 * Title: DisposalDateWarningTableModel
 * </p>
 * <p>
 * Description: The table model for DisposalDateWarning
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logoca
 * </p>
 */

public class DisposalDateWarningTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;

    public static final int DEFENDANT_NAME = 0;
    
    public static final int DISPOSAL_CODE = 1;

    public static final int DISPOSAL_DATE = 2;

    public static final int HEARING_DATE = 3;
    

    /**
     * Set up that will bring out the header information from resource bundles.
     * 
     * @param Collection
     */
    private void setup(AuthoriseWarning[] warnings) {
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        
        for (AuthoriseWarning warning : warnings) {
            final int columns = 4;
            Vector<Object> row = new Vector<Object>(columns);
            row.setSize(columns);
            row.setElementAt(getDefendantName(warning), DEFENDANT_NAME);
            row.setElementAt(warning.getDisposalCode(), DISPOSAL_CODE);
            row.setElementAt(warning.getDisposalResultDate(), DISPOSAL_DATE);
            row.setElementAt(warning.getHearingEndDate(), HEARING_DATE);
            data.add(row);
        }
        
        setColumnNames(new String[] { 
                lookupResource("results.authorise.warningdialog.column.defendantname"),
                lookupResource("results.authorise.warningdialog.column.disposalcode"),
                lookupResource("results.authorise.warningdialog.column.disposaldate"),                
                lookupResource("results.authorise.warningdialog.column.hearingdate") });
        
        setData(data);
    }

    
    public DisposalDateWarningTableModel(AuthoriseWarning[] warnings) {
        super();
        setup(warnings);
    }

    /**
     * The table is for information only and cannot be edited
     * @param r
     * @param c
     * @return boolean
     */
    public boolean isCellEditable(int r, int c) {
        return false;
    }
    
    
    /**
     * Return the formatted defendant name.
     * @param warning
     * @return The defendant name
     */
    private String getDefendantName(AuthoriseWarning warning) {
        if (warning != null) {
            return warning.getFirstName() 
                + " "
                + warning.getSurname();
        } else {
            return "";
        }
    }


    public Class getColumnClass(int col) {
        switch (col) {
        case DEFENDANT_NAME:
        case DISPOSAL_CODE:
            return String.class;
        case DISPOSAL_DATE:
        case HEARING_DATE:
            return Date.class;
        default:
            return Object.class;
        }
    }


    private String lookupResource(String key) {
        return ResourceBundleHelper.getResource(
                XhibitBundles.CaseProgressResources, key);
    }
}
