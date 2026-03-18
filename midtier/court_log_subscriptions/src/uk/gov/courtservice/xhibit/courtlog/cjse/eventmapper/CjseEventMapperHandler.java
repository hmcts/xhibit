package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

import org.xml.sax.ContentHandler;

/**
 * <p>
 * Title: Interface specifying the responsibility of a CJSE Event Mapper Handler
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This interface specifies the methods to be implemented by a class that
 * creates and configures an instance of CjseEventMapper. Using a combination of
 * reflection and the decorator pattern, this allows new types of
 * CjseEventMapper to be created and configured using XML that was not defined
 * at the time of the framework.
 * </p>
 * <p>
 * Instances of this interface should be written to be reusable, after the
 * reset() method is called.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public interface CjseEventMapperHandler extends ContentHandler {

    /**
     * Get the successfully configured mapper.
     * 
     * @return the configured mapper.
     */
    public CjseEventMapper getMapper();

    /**
     * This method will be called to reset the instance for reuse.
     */
    public void reset();

}