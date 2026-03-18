package uk.gov.courtservice.xhibit.business.database.originalcharges.processor;

/**
 * <p>Title: QueueRowProcessor</p>
 * <p>Description: Class to process the Charge rows.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;

public class ChargeRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final ArrayList<ChargeVO> chargeList = new ArrayList<ChargeVO>();

    /**
     * Returns the data
     * 
     * @return ChargeVO[]
     */
    public ChargeVO[] getCharges() {
        return chargeList.toArray(new ChargeVO[chargeList.size()]);
    }
    
    public ArrayList<ChargeVO> getChargeList() {
        return chargeList;
    }

    /**
     * Implementation of row processor
     * 
     * @param row from the resultset
     */
    public void processRow(Row row) {
        ChargeVO charge = new ChargeVO();

        // Populate the vo
        charge.setChargeId(row.getInteger("charge_id"));
        charge.setChargeType(row.getString("charge_type"));
        charge.setCrestChargeSeqNo(row.getInteger("crest_charge_seq_no"));
        charge.setCrestOffenceFreetext(row.getString("crest_offence_freetext"));
        charge.setCrestOffenceSeqNo(row.getInteger("crest_offence_seq_no"));
        charge.setDefendantOnCaseId(row.getInteger("defendant_on_case_id"));
        charge.setDefendantOnOffenceId(row.getInteger("defendant_on_offence_id"));
        charge.setOffenceId(row.getInteger("offence_id"));
        charge.setOffenceCode(row.getString("offence_code"));
        charge.setOffenceDesc(row.getString("offence_desc"));
        charge.setRefOffenceId(row.getInteger("ref_offence_id"));
        charge.setSeqNo(row.getInteger("seq_no"));

        // Add the VO to the ArrayList
        chargeList.add(charge);
    }
}
