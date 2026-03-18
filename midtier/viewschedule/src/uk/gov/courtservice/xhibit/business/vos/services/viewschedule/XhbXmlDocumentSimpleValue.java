package uk.gov.courtservice.xhibit.business.vos.services.viewschedule;

import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface;

/**
 * <p>
 * Title: Basic value object to hold xml document id
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: XhbXmlDocumentSimpleValue.java,v 1.1 2005/02/15 17:54:23 sz0t7n
 *          Exp $
 */

public class XhbXmlDocumentSimpleValue implements RowPopulatorInterface {
    private static final String XML_DOCUMENT_ID = "XML_DOCUMENT_ID";

    private static final String XML_DOCUMENT_CLOB_ID = "XML_DOCUMENT_CLOB_ID";

    private Integer xmlDocumentId;

    private Long xmlDocumentClobId;

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface#populate(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void populate(Row row) {
        setXmlDocumentId(row.getInteger(XML_DOCUMENT_ID));
        setXmlDocumentClobId(new Long(row.getLong(XML_DOCUMENT_CLOB_ID)));
    }

    /**
     * @return Returns the xmlDocumentId.
     */
    public Integer getXmlDocumentId() {
        return xmlDocumentId;
    }

    /**
     * @param xmlDocumentId
     *            The xmlDocumentId to set.
     */
    public void setXmlDocumentId(Integer xmlDocumentId) {
        this.xmlDocumentId = xmlDocumentId;
    }

    /**
     * @return Returns the xmlDocumentClobId.
     */
    public Long getXmlDocumentClobId() {
        return xmlDocumentClobId;
    }

    /**
     * @param xmlDocumentClobId
     *            The xmlDocumentClobId to set.
     */
    public void setXmlDocumentClobId(Long xmlDocumentClobId) {
        this.xmlDocumentClobId = xmlDocumentClobId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface#isDuplicateRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public boolean isDuplicateRow(Row row) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.framework.jdbc.core.RowPopulatorInterface#addRow(uk.gov.courtservice.framework.jdbc.core.Row)
     */
    public void addRow(Row row) {
    }

}
