package uk.gov.courtservice.xhibit.web.publicdisplay.error;

import java.io.Serializable;
import java.util.Date;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.ProcessingInstance;

/**
 * @author pznwc5 <p/> To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ProcessingError implements Serializable {

    private PublicDisplayEvent event;

    private Throwable exception;

    private ProcessingInstance processingInstance;

    private Date time = new Date();

    public ProcessingError(PublicDisplayEvent event, Throwable exception, ProcessingInstance processingInstance) {
        this.event = event;
        this.exception = exception;
        this.processingInstance = processingInstance;
    }

    public PublicDisplayEvent getEvent() {
        return event;
    }

    public Throwable getException() {
        return exception;
    }

    public ProcessingInstance getProcessingInstance() {
        return processingInstance;
    }

    public Date getTime() {
        return time;
    }

}
