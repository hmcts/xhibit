package uk.gov.courtservice.xhibit.client.search.courtreporter;

import java.awt.Dimension;

import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterComplexValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchResults;

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
 * @version 1.0 $Log: SearchCourtReporterResults.java,v $
 * @version 1.0 Revision 1.10  2006/06/05 12:31:38  bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version 1.0
 * @version 1.0 Revision 1.9 2006/05/31 14:25:49 bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting
 * @version 1.0 Revision 1.8 2003/10/14 09:06:25 bzw8gp Jon Powell
 * 
 * organise imports remove commented out statements
 * 
 * Revision 1.7 2003/06/16 13:53:58 nz5zpz X52217 - GUI part of the fix -
 * http://gbspsiad002:8888/Xhibit/646
 * 
 */
/*
 * Ref Date Author Description
 * 
 * 83,52539 28-04-2003 AW Daley Firm name results field added
 * 
 */
public class SearchCourtReporterResults extends XHIBITSearchResults // implements
// TableCellRenderer
{

    public SearchCourtReporterResults() {
        super();
        setResultsValueObjectClass(new RefCourtReporterComplexValue(new Integer(0), new Integer(0)));
        // RefCourtReporterComplexValue x = new RefCourtReporterComplexValue(new
        // Integer(0), new Integer(0));
    }

    public void setResultFields() {
        addResultsField("initials", "personvalue.results.initials");
        addResultsField("surname", "personvalue.results.surName");
        addResultsField("refCourtReporterFirm.firmName", "courtreporter.DetailsCard.firmName");
    }

    public Class getColumnClass(int columnIndex) {
        return RefCourtReporterComplexValue.class;
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
        return "courtreporter.ResultsCard.title";
    }

    public String getStepDescriptionResourceKey() {
        return "courtreporter.ResultsCard.description";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("courtreporter.ResultsCard.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("courtreporter.ResultsCard.sortTableColumn");
    }

    public Dimension getResultsTableDimension() {
        int width = 450;
        int height = 200;
        Dimension dimension = new Dimension(width, height);
        return dimension;
    }
}