package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.entities.xhb_document_control.XhbDocumentControlBasicValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.DocumentControlComplexValue;

/**
 * <p>
 * Title: WllDocumentComplexValuesRowProcessor
 * </p>
 * <p>
 * Description: Create an object representing the document control data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DocumentControlComplexValuesRowProcessor.java,v 1.4 2005/02/22
 *          09:25:32 bzjrnl Exp $
 */
public final class DocumentControlComplexValuesRowProcessor extends AbstractRowProcessor {

    // The map of document control records
    private Map valueMap = new HashMap();

    /**
     * Get the values created when the last row was processed
     * 
     * @throws IllegalStateException
     *             if the summary has not been created
     */
    public DocumentControlComplexValue[] getDocumentControlComplexValues() {
        return (DocumentControlComplexValue[]) valueMap.values().toArray(
                new DocumentControlComplexValue[valueMap.size()]);
    }

    /**
     * Create a Document Control Complex Value if not already created and add
     * the recipient. Note the document control
     * 
     * @param row
     *            The row to process.
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowProcessor
     *      #processRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void processRow(final Row row) {
        // Note getDocumentControlBasicValue stores the
        // DocumentControlComplexValue objects in the map.
        getDocumentControlComplexValue(row).addRecipientName(row.getString("doc_recipient_name"));
    }

    /**
     * Check to see if we have already created this document control record.
     */
    private DocumentControlComplexValue getDocumentControlComplexValue(Row row) {
        Integer docControlId = row.getInteger("doc_control_id");
        DocumentControlComplexValue value = (DocumentControlComplexValue) valueMap.get(docControlId);
        if (value == null) {
            value = createDocumentControlComplexValue(docControlId, row);
            valueMap.put(docControlId, value);
        }
        return value;
    }

    private DocumentControlComplexValue createDocumentControlComplexValue(Integer docControlId, Row row) {
        return new DocumentControlComplexValue(createDocumentControlBasicValue(docControlId, row));
    }

    private XhbDocumentControlBasicValue createDocumentControlBasicValue(Integer docControlId, Row row) {
        XhbDocumentControlBasicValue basicValue = new XhbDocumentControlBasicValue();
        basicValue.setDocControlId(docControlId);
        basicValue.setStatus(row.getString("status"));
        basicValue.setExpiryDate(row.getTimestamp("expiry_date"));
        basicValue.setDistributionType(row.getString("distribution_type"));
        basicValue.setMimeType(row.getString("mime_type"));
        basicValue.setDocumentType(row.getString("document_type"));
        basicValue.setLastUpdateDate(row.getTimestamp("last_update_date"));
        basicValue.setCreationDate(row.getTimestamp("creation_date"));
        basicValue.setCreatedBy(row.getString("created_by"));
        basicValue.setLastUpdatedBy(row.getString("last_updated_by"));
        basicValue.setVersion(row.getInteger("version"));
        basicValue.setFormattingId(row.getInteger("formatting_id"));
        basicValue.setCourtId(row.getInteger("court_id"));
        basicValue.setDistributedDate(row.getTimestamp("distributed_date"));
        basicValue.setXmlDocumentId(row.getInteger("xml_document_id"));
        basicValue.setFormattedDocumentBlobId(row.getWrappedLong("formatted_document_blob_id"));
        return basicValue;
    }

}
