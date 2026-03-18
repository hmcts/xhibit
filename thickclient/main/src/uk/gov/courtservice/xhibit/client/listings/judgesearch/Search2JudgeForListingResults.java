package uk.gov.courtservice.xhibit.client.listings.judgesearch;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
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
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */
public class Search2JudgeForListingResults extends XHIBITSearchResults implements TableCellRenderer {

    public Search2JudgeForListingResults() {
        super();
        setResultsValueObjectClass(new RefJudgeComplexValue());

    }

    public void setResultFields() {
        // addResultsField( ResultValueObjectAttributeName,
        // XHIBITSearchResourceKey, optional cellRenderer)
    	addResultsField("court.courtName", "judge.court.results.courtSite");
    	addResultsField("judgeType", "judge.court.results.judgeType");
        addResultsField("title", "judge.court.results.title");
        addResultsField("firstName", "judge.court.results.firstName"); // you
        // may
        // wish
        // to
        // use
        // valueobject.results.attriblabel.
        addResultsField("middleName", "judge.court.results.middleName");
        addResultsField("surname", "judge.court.results.surName");
        addResultsField("honours", "judge.court.results.honours");
        addResultsField("fullListTitle", "judge.court.results.listTitle");
        addResultsField("allTicketTypes", "judge.court.results.ticketType");

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
        return getColumnSortModus("judge.court.results.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("judge.court.results.sortTableColumn");
    }

    public Dimension getResultsTableDimension() {
        int width = 900;
        int height = 300;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }
}