package uk.gov.courtservice.xhibit.client.search.court;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
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
 * @version 1.0
 */
public class SearchCourtResults extends XHIBITSearchResults implements TableCellRenderer {

    public SearchCourtResults() {
        super();
        setResultsValueObjectClass(new RefCourtBasicValue());
    }

    public void setResultFields() {
        // addResultsField( ResultValueObjectAttributeName,
        // XHIBITSearchResourceKey, optional cellRenderer)
        addResultsField("courtFullName", "court.ResultsCard.courtName"); // you
        // may
        // wish
        // to
        // use
        // valueobject.results.attriblabel.
        // addResultsField("courtType", "court.ResultsCard.courtType");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        XHIBITConstant.debug("getTableCellRendererComponent - row : " + row + " col : " + column);
        Component component = new JLabel();
        return component;

    }

    public Class getColumnClass(int columnIndex) {
        return RefCourtBasicValue.class;
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
        return "court.ResultsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "court.ResultsCard.description";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("court.ResultsCard.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("court.ResultsCard.sortTableColumn");
    }

    public Dimension getResultsTableDimension() {
        int width = 450;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }
}