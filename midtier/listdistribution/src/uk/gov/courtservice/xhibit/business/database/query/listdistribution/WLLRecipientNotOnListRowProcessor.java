package uk.gov.courtservice.xhibit.business.database.query.listdistribution;

import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: WLLRecipientNotOnListRowProcessor
 * </p>
 * <p>
 * Description: Processes the results from the query to retireve WLL Recipients
 * not already subscribed to the WLL for this court.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: WLLRecipientNotOnListRowProcessor.java,v 1.2 2004/03/30
 *          10:12:43 qzd3k3 Exp $
 */

public class WLLRecipientNotOnListRowProcessor extends ReflectionRowProcessor {

    // The logger for this class
    private static Logger log = CSServices.getLogger(WLLRecipientNotOnListRowProcessor.class);

    // The Vector that will contain a collection of all
    // WLLRecipientBasicValue objects
    private Vector wllRecipientVector = new Vector();

    // The file that contains the binding for this object
    private static final String BINDING_FILE = "config/database/binding/listdistribution/WLLRecipient.properties";

    // Bindings used by this row processor
    private static final Properties BINDING = readBinding(BINDING_FILE);

    /**
     * Initialised the metadata
     */
    WLLRecipientNotOnListRowProcessor() {
        super(uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue.class);
        registerProperties(BINDING);
    }

    /**
     * @return The <code>WLLRecipientBasicValue</code> object for the row.
     */
    uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue getWLLRecipient() {
        return (uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue) getObject();
    }

    /**
     * Called after the <code>ReflectionRowProcessor</code> has processed the
     * row, stores the value retrieved.
     * 
     * @param row
     */
    public void postProcessRow(Row row) {
        wllRecipientVector.add(getWLLRecipient());
    }

    /**
     * Returns the list of WLL Recipients not already subscribed to the list.
     * 
     * @return Collection of <code>WLLRecipientBasicValue</code> objects
     */
    Collection findWLLRecipientsNotOnList() {
        log.debug("findWLLRecipientsNotOnList() called");

        return wllRecipientVector;
    }
}