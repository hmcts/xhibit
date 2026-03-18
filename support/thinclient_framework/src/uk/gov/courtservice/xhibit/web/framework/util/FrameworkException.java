package uk.gov.courtservice.xhibit.web.framework.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: An exception wrapper that can be thrown by the framework.
 * </p>
 * <p>
 * Description: Thrown to wrap exceptions that want to be handled in the same
 * way
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * 
 * $Revision: 1.4 $ $Log: FrameworkException.java,v $
 * $Revision: 1.4 $ Revision 1.4  2006/06/05 12:30:26  bzjrnl
 * $Revision: 1.4 $ Change: TI901
 * $Revision: 1.4 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * $Revision: 1.4 $ $Revision: 1.4 $ Revision
 * 1.3 2006/05/31 14:23:57 bzjrnl $Revision: 1.4 $ Change: TI901 $Revision: 1.4 $
 * Comment: Weblogic Upgrade - Standadise code formatting $Revision: 1.4 $
 * Revision 1.2 2004/12/09 15:05:47 sz0t7n Make thin client exceptions use
 * standard CSExceptions and report messages correctly
 * 
 * Revision 1.1 2003/03/21 11:48:29 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.1 2003/03/19 11:30:52 fz0n8j Added framework exception wrapper.
 * 
 * 
 */

public class FrameworkException extends CSRecoverableException {

    // public FrameworkException() {
    // super();
    // }

    // public FrameworkException(String message) {
    // super(message);
    // }

    // public FrameworkException(Throwable wrapped) {
    // this.wrapped = wrapped;
    // }
    //
    // public FrameworkException(String message, Throwable wrapped) {
    // super(message);
    // this.wrapped = wrapped;
    // }
    //
    // public Throwable getNested() {
    // return wrapped;
    // }

    public FrameworkException(Throwable throwable) {
        super(throwable);
    }

    public FrameworkException(String key, String message, Throwable throwable) {
        super(key, message, throwable);
    }

    public FrameworkException(String key, String message) {
        super(key, message);
    }

    public FrameworkException(String key, Object[] paramArray, String message) {
        super(key, paramArray, message);
    }

    public FrameworkException(String key, Object[] paramArray, String message, Throwable throwable) {
        super(key, paramArray, message, throwable);
    }

    public Throwable getRoot() {
        Throwable wrapped = getCause();
        if (wrapped == null) {
            return this;
        } else {
            if (wrapped instanceof CSException) {
                return ((CSException) wrapped).getCause();
            } else {
                return wrapped;
            }
        }
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(StringBuffer buffer) {
        StringWriter out = new StringWriter();
        printStackTrace(new PrintWriter(out));
        buffer.append(out.toString());
    }

    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        if (getCause() != null) {
            out.println();
            out.println("Contains: ");
            out.println();
            getCause().printStackTrace(out);
        }
    }

    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        if (getCause() != null) {
            out.println();
            out.println("Contains: ");
            out.println();
            getCause().printStackTrace(out);
        }
    }

    public String getMessage() {
        String message = super.getMessage();
        if (message != null) {
            return message;
        } else {
            if (getCause() != null) {
                message = getCause().getMessage();
                if (message != null) {
                    return message;
                } else {
                    return getClass().getName();
                }
            } else {
                return getClass().getName();
            }
        }
    }

}
