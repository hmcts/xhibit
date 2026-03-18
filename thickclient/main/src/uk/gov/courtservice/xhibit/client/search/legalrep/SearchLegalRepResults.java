package uk.gov.courtservice.xhibit.client.search.legalrep;

import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
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
 * @version 1.0
 */

public class SearchLegalRepResults extends XHIBITSearchResults {

    public SearchLegalRepResults() {
        super();
        setResultsValueObjectClass(new RefLegalRepresentativeBasicValue());
    }

    public void setResultFields() {
        addResultsField("firstName", "legalRep.results.firstName");
        addResultsField("middleName", "legalRep.results.middleName");
        addResultsField("surname", "legalRep.results.surName");
    }

    public String getStepTitleResourceKey() {
        return "legalRep.criteria.xxtitle";
    }

    public String getStepDescriptionResourceKey() {
        return "legalRep.criteria.xxdescription";
    }

    public int getColumnSortModus() {
        return getColumnSortModus("legalRep.results.sortTableColumnModus");
    }

    public int getColumnIndexToSort() {
        return getColumnIndexToSort("legalRep.results.sortTableColumn");
    }

}