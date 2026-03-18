/*
 * Created on 10-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.web.publicdisplay.initialization;

import java.io.Serializable;

/**
 * @author pznwc5 <p/> To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ProcessingInstance implements Serializable {
    /**
     * During start up
     */
    public static final ProcessingInstance STARTUP = new ProcessingInstance();

    /**
     * During asynchronous JMS
     */
    public static final ProcessingInstance ASYNCHRONOUS = new ProcessingInstance();

    /**
     * Private constructor
     */
    private ProcessingInstance() {
    }

}
