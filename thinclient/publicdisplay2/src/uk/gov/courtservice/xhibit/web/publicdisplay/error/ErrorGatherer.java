/*
 * Created on 10-Feb-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.web.publicdisplay.error;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author pznwc5 <p/> To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ErrorGatherer implements Serializable {

    private static final int DEFAULT_SIZE = 100;

    private static ErrorGatherer INSTANCE = new ErrorGatherer();

    private LinkedList errors = new LinkedList();

    private int size = DEFAULT_SIZE;

    private ErrorGatherer() {
    }

    public static final ErrorGatherer getInstance() {
        return INSTANCE;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public synchronized void addError(ProcessingError error) {
        if (errors.size() == size) {
            errors.removeLast();
        }
        errors.addFirst(error);
    }

    public synchronized ProcessingError[] getErrors() {
        return (ProcessingError[]) errors.toArray(new ProcessingError[errors.size()]);
    }

    public synchronized void flush() {
        errors.clear();
    }

}
