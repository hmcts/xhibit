package uk.gov.courtservice.xhibit.client.search.offence;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
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
public class SearchOffenceResults extends XHIBITSearchResults implements TableCellRenderer {

    public SearchOffenceResults() {
        super();
        setResultsValueObjectClass(new RefOffenceBasicValue());
    }

    public void setResultFields() {
        // addResultsField( ResultValueObjectAttributeName,
        // XHIBITSearchResourceKey, optional cellRenderer)
        // addResultsField("actSection", "offence.ResultsCard.actSection"); //
        // you may wish to use valueobject.results.attriblabel.
        addResultsField("offenceCode", "offence.ResultsCard.offenceCode");
        addResultsField("offenceDesc", "offence.ResultsCard.offenceDesc");
        addResultsField("offenceDesc2", "offence.ResultsCard.offenceDesc2");
        addResultsField("statute", "offence.ResultsCard.statute");
        addResultsField("actSection", "offence.ResultsCard.section");
        addResultsField("offenceClass", "offence.ResultDetailsCard.offenceClass");
        addResultsField("dvlcCode", "offence.ResultDetailsCard.dvlcCode");
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        XHIBITConstant.debug("getTableCellRendererComponent - row : " + row + " col : " + column);
        Component component = new JLabel();
        return component;
    }

    public Class getColumnClass(int columnIndex) {
        return RefOffenceBasicValue.class;
    }

    public String getStepTitleResourceKey() {
        return "offence.ResultsListCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "offence.ResultsListCard.description";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("offence.ResultsCard.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("offence.ResultsCard.sortTableColumn");
    }

    // SG - override from XHIBITSearchResults
    public Dimension getResultsTableDimension() {
        int width = 950;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }

}