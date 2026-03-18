package uk.gov.courtservice.xhibit.common.publicdisplay.data;

import uk.gov.courtservice.xhibit.common.publicdisplay.data.exceptions.DataRetrievalException;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * 
 * A DataSource can be used more than once for a given uri.
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
 * @version $Revision: 1.5 $
 * 
 * @invariant uri != null
 * @invariant data != null
 */
public abstract class DataSource {
    private Data data = new Data();

    private DataContext context;

    private DisplayDocumentURI uri;

    /**
     * Creates a new DataSource object.
     * 
     * @param uri
     *            the URI of the DisplayDocument we are retrieving data for.
     * @pre uri != null
     */
    public DataSource(DisplayDocumentURI uri) {
        this.uri = uri;
    }

    /**
     * Sets the URI of the DisplayDocument we are retrieving data for.
     * 
     * @param uri
     *            the URI of the DisplayDocument we are retrieving data for.
     * @deprecated use the constructor instead.
     */
    public void setUri(DisplayDocumentURI uri) {
        this.uri = uri;
    }

    /**
     * Returns the URI of the DisplayDocument we are retrieving data for.
     * 
     * @return the URI of the DisplayDocument we are retrieving data for.
     * @post return != null
     */
    public DisplayDocumentURI getUri() {
        return uri;
    }

    /**
     * Retrieve the data and store internally. Use getData() to access the data
     * afterwards.
     * 
     * @pre uri != null
     * @post getData() != null
     * @see #getData
     */
    public abstract void retrieve() throws DataRetrievalException;

    /**
     * Sets the optional DataContext.
     * 
     * @param context
     *            the DataContext.
     * @pre context != null
     */
    public void setContext(DataContext context) {
        this.context = context;
    }

    /**
     * Returns the optional data context.
     * 
     * @return the optional data context.
     */
    public DataContext getContext() {
        return context;
    }

    /**
     * Returns the retrieved data.
     * 
     * @return the retrieved data.
     * @post return != null
     */
    public Data getData() {
        return data;
    }

    /**
     * Reset the DataSource so it can be used again. This should be called by
     * the subclasses retrieve() implementation before retrieving data.
     * 
     * @post data.isEmpty()
     */
    public void reset() {
        if (!data.isEmpty()) {
            data.clear();
        }
    }
}
