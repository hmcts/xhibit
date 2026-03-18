package uk.gov.courtservice.xhibit.business.database.query.counsel;

import java.util.Properties;

import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: DefendantRowProcessor
 * </p>
 * <p>
 * Description: Creates DefendantBasicValue from a database row for the counsel
 * sign in
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford, Marie Holmberg
 * @version 1.0
 */

class DefendantRowProcessor extends ReflectionRowProcessor {

    // The file that contains the binding for this object
    private static final String BINDING_FILE = "config/database/binding/counsel/Defendant.properties";

    private static final Properties BINDING = readBinding(BINDING_FILE);

    /**
     * Initializes the metadata
     */
    DefendantRowProcessor() {
        super(DefendantValue.class);
        registerProperties(BINDING);
    }

    /**
     * Returns the defendantValue object
     * 
     * @return
     */
    DefendantValue getDefendant() {
        return (DefendantValue) getObject();
    }

}