package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Fatal;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storeable;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class DocumentNotYetRenderedException extends StoreException implements Fatal {
    /**
     * Creates a new DocumentNotYetRenderedException object.
     * 
     * @param doc
     *            the Storeable document.
     */
    public DocumentNotYetRenderedException(final Storeable doc) {
        super("No rendered string for document '" + doc.getUri() + "'.");
    }
}
