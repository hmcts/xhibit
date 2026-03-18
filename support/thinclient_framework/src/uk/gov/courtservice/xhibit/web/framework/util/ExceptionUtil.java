package uk.gov.courtservice.xhibit.web.framework.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;

import uk.gov.courtservice.framework.exception.CSException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.validation.CSValidationException;

/**
 * <p>
 * Title: Exception Util
 * </p>
 * <p>
 * Description: A bunch of utilities for manipulating exceptions
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
 * @version V1.0.0 $Log: ExceptionUtil.java,v $
 * @version V1.0.0 Revision 1.7  2006/06/05 12:30:26  bzjrnl
 * @version V1.0.0 Change: TI901
 * @version V1.0.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version V1.0.0
 * @version V1.0.0 Revision 1.6 2006/05/31 14:23:57 bzjrnl
 * @version V1.0.0 Change: TI901
 * @version V1.0.0 Comment: Weblogic Upgrade - Standadise code formatting
 * @version V1.0.0 Revision 1.5 2004/12/09 15:05:47 sz0t7n Make thin client
 *          exceptions use standard CSExceptions and report messages correctly
 * 
 * Revision 1.4 2003/10/01 15:33:53 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.3 2003/04/02 11:00:47 fz0n8j Added Reflection Exception and
 * improved CS exception handling in get cause
 * 
 * Revision 1.2 2003/03/25 10:24:46 fz0n8j Added get root cuse
 * 
 * Revision 1.1 2003/03/21 11:48:29 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.2 2003/03/19 11:30:52 fz0n8j Added framework exception wrapper.
 * 
 * Revision 1.1 2003/03/18 13:28:53 fz0n8j Factored exception handling.
 * 
 * Revision 1.2 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 */
public class ExceptionUtil {

    /**
     * This method is used to get the stack traces from the exception and its
     * causes.
     * 
     * @param wrapper
     *            the wrapper exception
     * @return the string containing the stack traces
     * 
     */
    public static final String getStackTraces(Throwable wrapper) {
        StringWriter buffer = new StringWriter();
        printStackTraces(wrapper, new PrintWriter(buffer));
        return buffer.toString();
    }

    /**
     * This method is used to print the stack traces from the exception and its
     * causes.
     * 
     * @param wrapper
     *            the wrapper exception
     * @param out
     *            the stream to write to
     */
    public static final void printStackTraces(Throwable wrapper, OutputStream out) {
        if (out instanceof PrintStream) {
            printStackTraces(wrapper, (PrintStream) out);
        } else {
            printStackTraces(wrapper, new PrintStream(out));
        }
    }

    /**
     * This method is used to print the stack traces from the exception and its
     * causes.
     * 
     * @param wrapper
     *            the wrapper exception
     * @param out
     *            the stream to write to
     */
    public static final void printStackTraces(Throwable wrapper, PrintStream out) {
        Throwable[] exceptions = getExceptions(wrapper);
        if (0 < exceptions.length) {
            out.println("Cause:-");
            exceptions[0].printStackTrace(out);
            out.println();
            for (int i = 1; i < exceptions.length; i++) {
                out.print("Wrapper ");
                out.print(i);
                out.println(":-");
                exceptions[i].printStackTrace(out);
                out.println();
            }
        }
    }

    /**
     * This method is used to print the stack traces from the exception and its
     * causes.
     * 
     * @param wrapper
     *            the wrapper exception
     * @param out
     *            the writer to write to
     */
    public static final void printStackTraces(Throwable wrapper, Writer out) {
        if (out instanceof PrintWriter) {
            printStackTraces(wrapper, (PrintWriter) out);
        } else {
            printStackTraces(wrapper, new PrintWriter(out));
        }
    }

    /**
     * This method is used to print the stack traces from the exception and its
     * causes.
     * 
     * @param wrapper
     *            the wrapper exception
     * @param out
     *            the writer to write to
     */
    public static final void printStackTraces(Throwable wrapper, PrintWriter out) {
        Throwable[] exceptions = getExceptions(wrapper);
        if (0 < exceptions.length) {
            out.println("Cause:-");
            exceptions[0].printStackTrace(out);
            out.println();
            for (int i = 1; i < exceptions.length; i++) {
                out.print("Wrapper ");
                out.print(i);
                out.println(":-");
                exceptions[i].printStackTrace(out);
                out.println();
            }
        }
    }

    /**
     * Unrolls the causal exception from the types reconised by the getCause
     * method
     * 
     * @param wrapper
     *            the top level exception wrapper
     * @return an iterator over the list of Throwable objects that represent the
     *         chained exception
     */
    public static final Iterator getExceptionIterator(Throwable wrapper) {
        return getExceptionList(wrapper).iterator();
    }

    /**
     * Unrolls the causal exception from the types reconised by the getCause
     * method
     * 
     * @param wrapper
     *            the top level exception wrapper
     * @return a list of Throwable objects that represent the chained exception
     */
    public static final List getExceptionList(Throwable wrapper) {
        return Arrays.asList(getExceptions(wrapper));
    }

    /**
     * Unrolls the causal exception from the types reconised by the getCause
     * method
     * 
     * @param wrapper
     *            the top level exception wrapper
     * @return an array of Throwable objects that represent the chained
     *         exception
     */
    public static final Throwable[] getExceptions(Throwable wrapper) {
        return getExceptions(wrapper, 0);
    }

    /**
     * This method is used to produce the exception array, it counts the
     * exceptions (storing the size) on the way along the list, it then
     * populates the array as it falls back down.
     * 
     * @param wrapper
     *            the wrapper exception
     * @return an array of Throwable objects that represent the chained
     *         exception
     */
    private static final Throwable[] getExceptions(Throwable wrapper, int index) {
        if (wrapper == null) {
            return new Throwable[index];
        } else {
            Throwable[] exceptions = getExceptions(getCause(wrapper), ++index);
            exceptions[exceptions.length - index] = wrapper;
            return exceptions;
        }
    }

    /**
     * Returns the root cause from the wrapper exception
     * 
     * @param wrapper
     *            the top level exception wrapper
     * @return the Throwable object that represent the chained exception
     */
    public static final Throwable getRootException(Throwable wrapper) {
        Throwable current = wrapper;
        for (Throwable next = getCause(current); next != null; next = getCause(current)) {
            current = next;
        }
        return current;
    }

    /**
     * Gets the causal exception from the wrapper exception, recognises the
     * following exception types.
     * 
     * <ul>
     * <li>javax.servlet.ServletException</li>
     * <li>javax.servlet.jsp.JspException</li>
     * <li>javax.ejb.EJBException</li>
     * <li>java.rmi.RemoteException</li>
     * <li>org.exolab.castor.xml.MarshalException</li>
     * <li>org.exolab.castor.xml.ValidationException</li>
     * <li>uk.gov.courtservice.framework.exception.CSUnrecoverableException</li>
     * <li>uk.gov.courtservice.xhibit.web.framework.util.FrameworkException</li>
     * </ul>
     * 
     * @param wrapper
     *            the wrapper exception
     * @return the causal Throwable object.
     */

    private static Throwable getCause(Throwable wrapper) {
        if (wrapper instanceof ServletException) {
            return ((ServletException) wrapper).getRootCause();
        } else if (wrapper instanceof JspException) {
            return ((JspException) wrapper).getRootCause();
        } else if (wrapper instanceof ServletException) {
            return ((ServletException) wrapper).getRootCause();
        } else if (wrapper instanceof CSException) {
            return ((CSException) wrapper).getCause();
        } else if (wrapper instanceof EJBException) {
            return ((EJBException) wrapper).getCausedByException();
        } else if (wrapper instanceof RemoteException) {
            return ((RemoteException) wrapper).detail;
        } else if (wrapper instanceof FrameworkException) {
            return ((FrameworkException) wrapper).getCause();
        } else if (wrapper instanceof UndeclaredThrowableException) {
            return ((UndeclaredThrowableException) wrapper).getUndeclaredThrowable();
        } else {
            return null;
        }
    }

    public static String buildMessageString(Throwable ex) {
        if (ex instanceof CSException) {
            return buildMessageString((CSException) ex);
        } else {
            return ex.getMessage();
        }
    }

    public static String buildMessageString(CSException ex) {
        StringBuffer sb = new StringBuffer();

        String[] messages = ex.getUserMessages();
        for (int i = 0; i < messages.length; i++) {
            String message = messages[i];
            sb.append(message);
            if (i < messages.length - 1) {
                sb.append("\n");
            }
        }
        if (ex instanceof CSValidationException) {
            sb.append(buildValidationMessageString((CSValidationException) ex));
        }
        return sb.toString();
    }

    private static String buildValidationMessageString(CSValidationException cv) {
        StringBuffer messageText = new StringBuffer();
        if (cv.getUserValidationErrorList() != null && !cv.getUserValidationErrorList().isEmpty()) {
            Iterator iter = cv.getUserValidationErrorList().iterator();
            while (iter.hasNext()) {
                Message item = (Message) iter.next();
                messageText.append("\n" + item.getMessage());
            }
        }
        return messageText.toString();
    }

    /**
     * Stops this class being constructed unnecessarily
     */
    private ExceptionUtil() {
    }
}
