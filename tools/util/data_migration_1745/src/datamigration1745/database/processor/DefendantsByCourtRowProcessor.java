package datamigration1745.database.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

import datamigration1745.vos.DefendantByCourtVO;

/**
 * <p>
 * Title: DefendantsByCourtRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Defendants for a Court.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version 1.0
 */
public class DefendantsByCourtRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<DefendantByCourtVO> defendantsByCourtList = new ArrayList<DefendantByCourtVO>();

    /**
     * Returns the data
     *
     * @return Collection
     */
    public DefendantByCourtVO[] getDefendantsByCourtList() {
        return defendantsByCourtList.toArray(new DefendantByCourtVO[defendantsByCourtList.size()]);
    }

    /**
     * Implementation of row processor
     *
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        DefendantByCourtVO item = new DefendantByCourtVO();

        // Put the data into a VO
        item.setCourtId(row.getInteger("court_id"));
        item.setCaseId(row.getInteger("case_id"));
        item.setCaseType(row.getString("case_type"));
        item.setCaseNumber(row.getInteger("case_number"));
        item.setId(row.getInteger("defendant_on_case_id"));
        item.setDefendantId(row.getInteger("defendant_id"));
        item.setPtiurn(row.getString("ptiurn"));
        item.setFirstName(row.getString("first_name"));
        item.setSurname(row.getString("surname"));
        item.setCaseSubType(row.getString("case_sub_type"));
        item.setCrestDefendantId(row.getInteger("crest_defendant_id"));

        // Add the VO to the ArrayList
        defendantsByCourtList.add(item);
    }

}
