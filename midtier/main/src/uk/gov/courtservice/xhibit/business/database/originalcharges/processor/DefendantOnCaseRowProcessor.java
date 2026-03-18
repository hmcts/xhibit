package uk.gov.courtservice.xhibit.business.database.originalcharges.processor;

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
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public class DefendantOnCaseRowProcessor extends AbstractRowProcessor {

    private final ArrayList<DefendantOnCaseVO> docList = new ArrayList<DefendantOnCaseVO>();

    /**
     * Returns the data
     * 
     * @return DefendantOnCaseVO[]
     */
    public DefendantOnCaseVO[] getDefendantsOnCase() {
        return docList.toArray(new DefendantOnCaseVO[docList.size()]);
    }
    
    public ArrayList<DefendantOnCaseVO> getDocList() {
        return docList;
    }

    /**
     * Implementation of row processor
     * 
     * @param row from the resultset
     */
    public void processRow(Row row) {
        DefendantOnCaseVO doc = new DefendantOnCaseVO();

        // Populate the vo
        doc.setAsn(row.getString("asn"));
        doc.setCaseId(row.getInteger("case_id"));
        doc.setCaseNumber(row.getInteger("case_number"));
        doc.setCaseType(row.getString("case_type"));
        doc.setCourtId(row.getInteger("court_id"));
        doc.setDefendantId(row.getInteger("defendant_id"));
        doc.setDefendantOnCaseId(row.getInteger("defendant_on_case_id"));
        doc.setFirstName(row.getString("first_name"));
        doc.setMiddleName(row.getString("middle_name"));
        doc.setSurname(row.getString("surname"));
        doc.setTotalIndictments(row.getInteger("total_indictments"));
        doc.setTotalOriginalCharges(row.getInteger("total_original_charges"));

        // Add the VO to the ArrayList
        docList.add(doc);
    }
}
