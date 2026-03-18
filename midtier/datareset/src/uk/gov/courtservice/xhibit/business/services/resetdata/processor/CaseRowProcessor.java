package uk.gov.courtservice.xhibit.business.services.resetdata.processor;

/**
 * <p>Title: QueueRowProcessor</p>
 * <p>Description: Class to process the DefendantOnCase rows.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.services.datareset.vo.ManagedCase;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;

public class CaseRowProcessor extends AbstractRowProcessor {

    private final ArrayList<ManagedCase> docList = new ArrayList<ManagedCase>();

    /**
     * Returns the data
     * 
     * @return DefendantOnCaseVO[]
     */
    public ManagedCase[] getManagedCasesByCourt() {
        return docList.toArray(new ManagedCase[docList.size()]);
    }
    
    public ArrayList<ManagedCase> getDocList() {
        return docList;
    }

    /**
     * Implementation of row processor
     * 
     * @param row from the resultset
     */
    public void processRow(Row row) {
    	ManagedCase doc = new ManagedCase();

        // Populate the vo
    	doc.setCaseId(row.getInteger("case_id"));
        doc.setCourtId(row.getInteger("court_id"));
        doc.setCaseNumber(row.getInteger("case_number"));
        doc.setCaseType(row.getString("case_type"));
        doc.setDefendantId(row.getInteger("defendant_id"));
        doc.setDefendantDOB(row.getDate("date_of_birth"));
        doc.setDefendantFirstName(row.getString("first_name"));
        doc.setDefendantMiddleName(row.getString("middle_name"));
        doc.setDefendantSurname(row.getString("surname"));
        doc.setCourtName(row.getString("court_name"));

        // Add the VO to the ArrayList
        docList.add(doc);
    }
}
