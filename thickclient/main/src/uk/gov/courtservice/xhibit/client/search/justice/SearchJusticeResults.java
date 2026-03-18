package uk.gov.courtservice.xhibit.client.search.justice;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
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
public class SearchJusticeResults extends XHIBITSearchResults implements TableCellRenderer {

    public SearchJusticeResults() {
        super();
        setResultsValueObjectClass(new RefJusticeBasicValue());
    }

    public void setResultFields() {
        // addResultsField( ResultValueObjectAttributeName,
        // XHIBITSearchResourceKey, optional cellRenderer)
        addResultsField("title", "justice.results.title"); // you may wish
        // to use
        // valueobject.results.attriblabel.
        addResultsField("initials", "justice.results.initials");
        addResultsField("justiceName", "justice.results.name");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        XHIBITConstant.debug("getTableCellRendererComponent - row : " + row + " col : " + column);
        Component component = new JLabel();
        return component;

    }

    public Class getColumnClass(int columnIndex) {
        return RefJusticeBasicValue.class;
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
        return "justice.results.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "justice.results.xxdescription";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("justice.results.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("justice.results.sortTableColumn");
    }

}