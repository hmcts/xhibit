package uk.gov.courtservice.xhibit.business.services.email;

import java.io.Serializable;

import javax.activation.DataSource;

/**
 * <p>
 * Title: Portable DataSource interface.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * To be implemented by all attachments to be passed to the EmailHelper as part
 * of any value objects used. Provides portable, simple implementation of a
 * compatible means of supplying attachments. The core
 * javax.activation.DataSource provides no guarnatees of serializability.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
public interface PortableDataSource extends Serializable, DataSource {
    /**
     * The default content type to be returned by getContentType().
     */
    public static final String DEFAULT_ATTACHMENT_TYPE = "text/plain";
}