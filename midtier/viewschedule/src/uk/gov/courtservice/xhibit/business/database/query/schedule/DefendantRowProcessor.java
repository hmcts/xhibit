package uk.gov.courtservice.xhibit.business.database.query.schedule;

import java.util.Properties;

import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;

/**
 * <p>
 * Title: DefendantRowProcessor
 * </p>
 * <p>
 * Description: Creates DefendantBasicValue from a database row
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

class DefendantRowProcessor extends ReflectionRowProcessor {

    // The file that contains the binding for this object
    private static final String BINDING_FILE = "database/binding/schedule/Defendant.properties";

    // SQL used by this query
    private static final Properties BINDING = readBinding(BINDING_FILE);

    /**
     * Initializes the metadata
     */
    DefendantRowProcessor() {
        super(DefendantBasicValue.class);
        registerProperties(BINDING);
    }

    /**
     * Returns the ref hearing type
     * 
     * @return
     */
    DefendantBasicValue getDefendant() {
        return (DefendantBasicValue) getObject();
    }

}