package uk.gov.courtservice.xhibit.client.search.solicitorfirm;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchResults;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * @author Frederik Vandendriessche
 * @version 1.0 $Log: SearchSolicitorFirmResults.java,v $
 * @version 1.0 Revision 1.9  2006/06/05 12:31:40  bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version 1.0
 * @version 1.0 Revision 1.8 2006/05/31 14:25:51 bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting
 * @version 1.0 Revision 1.7 2003/10/14 08:48:11 bzw8gp Jon Powell
 * 
 * organise imports
 * 
 * Revision 1.6 2003/06/16 14:22:12 nz5zpz X52217 - GUI part of the fix -
 * http://gbspsiad002:8888/Xhibit/646
 */
public class SearchSolicitorFirmResults extends XHIBITSearchResults implements TableCellRenderer {

    public SearchSolicitorFirmResults() {
        super();
        setResultsValueObjectClass(new RefSolicitorFirmComplexValue());
    }

    public void setResultFields() {
        // addResultsField( ResultValueObjectAttributeName,
        // XHIBITSearchResourceKey, optional cellRenderer)
        addResultsField("solicitorFirmName", "solicitorFirm.results.solicitorFirmName"); // you
                                                                                            // may
                                                                                            // wish
        // to use
        // valueobject.results.attriblabel.
        addResultsField("shortName", "solicitorFirm.results.shortName");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        XHIBITConstant.debug("getTableCellRendererComponent - row : " + row + " col : " + column);
        Component component = new JLabel();
        return component;

    }

    public Class getColumnClass(int columnIndex) {
        return RefSolicitorFirmComplexValue.class;
    }

    public int getMinimumResultsSelected() {
        return 1; // 1 = default value - method could be removed from this
        // class (the super does '1')
        // method still here to illustrate how to change cardinality of search
        // results to be selected
    }

    public int getMaximumResultsSelected() {
        return 1; // 1 = default value - method could be removed from this
        // class (the super does '1')
        // method still here to illustrate how to change cardinality of search
        // results to be selected
    }

    public String getStepTitleResourceKey() {
        return "solicitorFirm.results.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "solicitorFirm.results.xxdescription";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("solicitorFirm.ResultsCard.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("solicitorFirm.ResultsCard.sortTableColumn");
    }

    public Dimension getResultsTableDimension() {
        int width = 450;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }
}