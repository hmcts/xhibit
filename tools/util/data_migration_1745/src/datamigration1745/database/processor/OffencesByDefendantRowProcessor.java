package datamigration1745.database.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

import datamigration1745.vos.OffenceByDefendantVO;

/**
 * <p>
 * Title: OffencesByDefendantRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Offence rows for a Defendant.
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
public class OffencesByDefendantRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<OffenceByDefendantVO> offenceByDefendantList = new ArrayList<OffenceByDefendantVO>();

    /**
     * Returns the data
     *
     * @return Collection
     */
    public OffenceByDefendantVO[] getOffencesByDefendant() {
        return offenceByDefendantList.toArray(new OffenceByDefendantVO[offenceByDefendantList.size()]);
    }

    /**
     * Implementation of row processor
     *
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        OffenceByDefendantVO item = new OffenceByDefendantVO();

        // Put the data into a VO
        item.setCaseType(row.getString("case_type"));
        item.setCaseNumber(row.getInteger("case_number"));
        item.setCaseSubType(row.getString("case_sub_type"));
        item.setChargeType(row.getString("charge_type"));
        item.setCrnId(row.getString("crn_id"));
        item.setAsn(row.getString("asn"));
        item.setCrestOffenceSeqNo(row.getInteger("crest_offence_seq_no"));
        item.setCrestChargeSeqNo(row.getInteger("crest_charge_seq_no"));
        item.setChargeId(row.getInteger("charge_id"));
        item.setCrestChargeId(row.getInteger("crest_charge_id"));
        item.setOffenceId(row.getInteger("offence_id"));
        item.setCrestOffenceId(row.getInteger("crest_offence_id"));
        item.setSeqNo(row.getInteger("seq_no"));
        item.setId(row.getInteger("defendant_on_offence_id"));

        // Add the VO to the ArrayList
        offenceByDefendantList.add(item);
    }

}
