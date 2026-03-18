package uk.gov.courtservice.xhibit.client.search.judge;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue;
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
public class SearchJudgeResults extends XHIBITSearchResults implements TableCellRenderer {

    public SearchJudgeResults() {
        super();
        setResultsValueObjectClass(new RefJudgeBasicValue());

    }

    public void setResultFields() {
        // addResultsField( ResultValueObjectAttributeName,
        // XHIBITSearchResourceKey, optional cellRenderer)
        addResultsField("title", "judge.results.title");
        addResultsField("firstName", "judge.results.firstName"); // you
        // may
        // wish
        // to
        // use
        // valueobject.results.attriblabel.
        addResultsField("middleName", "judge.results.middleName");
        addResultsField("surname", "judge.results.surName");

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        XHIBITConstant.debug("getTableCellRendererComponent - row : " + row + " col : " + column);
        Component component = new JLabel();
        return component;

    }

    public Class getColumnClass(int columnIndex) {
        return PersonValue.class;
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
        return "judge.results.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "judge.results.xxdescription";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("judge.results.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("judge.results.sortTableColumn");
    }

    public Dimension getResultsTableDimension() {
        int width = 600;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }
}