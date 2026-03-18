package uk.gov.courtservice.xhibit.business.services.systemadmin;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;

/**
 * <p>
 * Title: WllRecipientComplexValuesRowProcessor
 * </p>
 * <p>
 * Description: Create an object representing the distribution list control
 * data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WllRecipientComplexValuesRowProcessor.java,v 1.3 2005/02/22
 *          09:25:32 bzjrnl Exp $
 */
public final class WllRecipientComplexValuesRowProcessor extends AbstractRowProcessor {

    // The list of control summary records
    private List valueList = new ArrayList();

    /**
     * Get the values created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public XhbWllRecipientBasicValue[] getWllRecipientBasicValues() {
        return (XhbWllRecipientBasicValue[]) valueList.toArray(new XhbWllRecipientBasicValue[valueList.size()]);
    }

    /**
     * Create a Recipient Complex Value for each row including distribution data
     * if required
     * 
     * @param row
     *            The row to process.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        valueList.add(createRecipientBasicValue(row));
    }

    private XhbWllRecipientBasicValue createRecipientBasicValue(Row row) {
        XhbWllRecipientBasicValue basicValue = new XhbWllRecipientBasicValue();
        basicValue.setWllRecipientId(row.getInteger("wll_recipient_id"));
        basicValue.setCrestSolicitorFirmId(row.getInteger("crest_solicitor_firm_id"));
        basicValue.setSolicitorFirmName(row.getString("solicitor_firm_name"));
        basicValue.setSolictiorFirmAddress(row.getString("solictior_firm_address"));
        basicValue.setSolicitorFirmFax(row.getString("solicitor_firm_fax"));
        basicValue.setSolicitorFirmEmail(row.getString("solicitor_firm_email"));
        basicValue.setLastUpdateDate(row.getTimestamp("recipient_last_update_date"));
        basicValue.setCreationDate(row.getTimestamp("recipient_creation_date"));
        basicValue.setCreatedBy(row.getString("recipient_created_by"));
        basicValue.setLastUpdatedBy(row.getString("recipient_last_updated_by"));
        basicValue.setVersion(row.getInteger("recipient_version"));
        basicValue.setCourtId(row.getInteger("court_id"));
        basicValue.setRecipientType(row.getString("recipient_type"));
        return basicValue;
    }
}
