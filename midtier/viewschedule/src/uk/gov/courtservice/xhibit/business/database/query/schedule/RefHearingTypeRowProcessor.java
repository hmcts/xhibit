package uk.gov.courtservice.xhibit.business.database.query.schedule;

import java.util.Properties;

import uk.gov.courtservice.framework.jdbc.core.ReflectionRowProcessor;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;

/**
 * <p>
 * Title: RefHearingTypeRowRrocessor
 * </p>
 * <p>
 * Description: Creates RefHearingTypeValue from a database row
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

class RefHearingTypeRowProcessor extends ReflectionRowProcessor {

    // The file that contains the binding for this object
    private static final String BINDING_FILE = "database/binding/schedule/RefHearingType.properties";

    // SQL used by this query
    private static final Properties BINDING = readBinding(BINDING_FILE);

    /**
     * Initializes the metadata
     */
    RefHearingTypeRowProcessor() {
        super(RefHearingTypeBasicValue.class);
        registerProperties(BINDING);
    }

    /**
     * Returns the ref hearing type
     * 
     * @return
     */
    RefHearingTypeBasicValue getRefHearingType() {
        return (RefHearingTypeBasicValue) getObject();
    }

}