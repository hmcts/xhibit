package uk.gov.courtservice.xhibit.client.search.caseSearch;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
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
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark harris
 * @version 1.0
 */
public class SearchCaseResults extends XHIBITSearchResults implements TableCellRenderer {

    public SearchCaseResults() {
        super();
        setResultsValueObjectClass(new RefCourtBasicValue());
    }

    public void setResultFields() {
        addResultsField("caseNumber", "case.ResultsCard.caseNumber");
        addResultsField("defendantName", "case.ResultsCard.defendantName");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        XHIBITConstant.debug("getTableCellRendererComponent - row : " + row + " col : " + column);
        Component component = new JLabel();
        return component;

    }

    public Class getColumnClass(int columnIndex) {
        return CaseBasicValue.class;
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
        return "case.ResultsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "case.ResultsCard.description";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("case.ResultsCard.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("case.ResultsCard.sortTableColumn");
    }

    public Dimension getResultsTableDimension() {
        int width = 450;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }
}