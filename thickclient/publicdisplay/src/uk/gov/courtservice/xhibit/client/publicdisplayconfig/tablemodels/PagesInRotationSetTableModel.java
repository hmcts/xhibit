package uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PagesInRotationSetTableModel.java,v 1.4 2005/11/25 15:07:11
 *          szfnvt Exp $
 */

public class PagesInRotationSetTableModel extends AbstractSelectRowTableModel {
    private static final String TABLECOL_KEY1 = "pd.tablecol.pagesInRotationSet";

    private static final String TABLECOL_HIDDEN = "HiddenSort";

    private XTable dataTable;

    public PagesInRotationSetTableModel(XTable rotationSetTable) {
        dataTable = rotationSetTable;
        setColumnNames(new String[] { PublicDisplayUtils.getResource(TABLECOL_KEY1), TABLECOL_HIDDEN });
    }

    /**
     * Get the RotationSetComplexValue for the selected row in the table model
     * passed in and extracts the description
     * 
     * @param rowIndex
     * @param columnIndex
     * @return String for document name
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        RotationSetDDComplexValue[] pages = getDisplayDocuments();
        switch (columnIndex) {
        case 0:
            String documentName = PublicDisplayUtils.getResource(PublicDisplayUtils.PRE_DISPLAYDOCUMENT.concat(
                    nulltoString(pages[rowIndex].getDisplayDocumentBasicValue().getDescriptionCode())).concat(
                    nulltoString(pages[rowIndex].getDisplayDocumentBasicValue().getLanguage())).concat(
                    nulltoString(pages[rowIndex].getDisplayDocumentBasicValue().getCountry())));
            return documentName;
        case 1:
            return pages[rowIndex].getRotationSetDDBasicValue().getOrdering();
        default:
            return "";
        }
    }

    private String nulltoString(Object value) {
        return value != null ? (String) value : "";
    }

    /**
     * @return The number of display documents for the selected rotation set
     */
    public int getRowCount() {
        if (getSelectedRow() < 0) {
            return 0;
        } else {
            RotationSetDDComplexValue[] pages = getDisplayDocuments();
            return pages.length;
        }
    }

    /**
     * Return a VO for required position
     * 
     * @param row
     *            required item from array
     * @return RotationSetDDComplexValue
     */
    public Object getDataAt(int row) {
        RotationSetDDComplexValue[] pages = getDisplayDocuments();
        if (row > pages.length)
            throw new CSUnrecoverableException("The selected row " + row
                    + " is not available in the list (ArrayIndexOutOfBounds)");
        return pages[row];
    }

    /**
     * @return get an array of display documents for the selected rotation set
     */
    private RotationSetDDComplexValue[] getDisplayDocuments() {
        RotationSetComplexValue rsComplex = (RotationSetComplexValue) ((XHIBITTableModelInterface) dataTable.getModel())
                .getDataAt(getSelectedRow());
        RotationSetDDComplexValue[] pages = rsComplex.getRotationSetDDComplexValues();
        return pages;
    }

}