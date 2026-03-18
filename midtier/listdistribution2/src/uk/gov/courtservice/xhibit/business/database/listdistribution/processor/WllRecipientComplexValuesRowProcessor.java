package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.entities.xhb_document_distribution.XhbDocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;

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
    // True if the processor should create document distribution data
    private final boolean createDocumentDistribution;

    // The list of control summary records
    private List valueList = new ArrayList();

    /**
     * Construct a new RecipientSummaryRowProcessor
     */
    public WllRecipientComplexValuesRowProcessor(boolean createDocumentDistribution) {
        this.createDocumentDistribution = createDocumentDistribution;
    }

    /**
     * Get the values created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public WllRecipientComplexValue[] getWllRecipientComplexValues() {
        return (WllRecipientComplexValue[]) valueList.toArray(new WllRecipientComplexValue[valueList.size()]);
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
        valueList.add(createComplexValue(row));
    }

    private WllRecipientComplexValue createComplexValue(Row row) {
        if (createDocumentDistribution) {
            return new WllRecipientComplexValue(createRecipientBasicValue(row),
                    createDocumentDistributionBasicValue(row));
        } else {
            return new WllRecipientComplexValue(createRecipientBasicValue(row));
        }
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

    private XhbDocumentDistributionBasicValue createDocumentDistributionBasicValue(Row row) {
        XhbDocumentDistributionBasicValue basicValue = new XhbDocumentDistributionBasicValue();
        basicValue.setDocDistributionId(row.getInteger("doc_distribution_id"));
        basicValue.setDistributionType(row.getString("distribution_type"));
        basicValue.setDocumentType(row.getString("document_type"));
        basicValue.setMimeType(row.getString("mime_type"));
        basicValue.setLastUpdateDate(row.getTimestamp("distribution_last_update_date"));
        basicValue.setCreationDate(row.getTimestamp("distribution_creation_date"));
        basicValue.setCreatedBy(row.getString("distribution_created_by"));
        basicValue.setLastUpdatedBy(row.getString("distribution_last_updated_by"));
        basicValue.setVersion(row.getInteger("distribution_version"));
        basicValue.setRecipientId(row.getInteger("recipient_id"));
        basicValue.setWllRecipientId(row.getInteger("wll_recipient_id"));
        basicValue.setCourtId(row.getInteger("court_id"));
        basicValue.setUsePrefDistType(row.getString("use_pref_dist_type"));
        return basicValue;
    }

}
