package uk.gov.courtservice.xhibit.web.framework.response;

import java.util.HashMap;
import java.util.Properties;

import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.PDFUtil;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

/**
 * <p>
 * Title: PDF Response
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         PDFResponse.java,v $ Revision 1.3 2003/10/01 15:27:48 bzw8gp Jon
 *         Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.2 2003/04/29 16:11:50 fz0n8j Merged devBranch-2b-030404
 * 
 * Revision 1.1.2.1 2003/04/09 16:14:30 fz0n8j Added pdf functionality to
 * framework.
 * 
 * 
 */
public class PDFResponse extends AbstractResponse {

    public static final String dataKey = "DATAKEY";

    /**
     * Empty normal constructor
     */
    public PDFResponse() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested
     * response.
     * </p>
     * 
     * @param responseEnvironment
     *            the environment to evaluate the response in
     */
    public void internalPerformResponse(ResponseEnvironment responseEnvironment) throws FrameworkException {
        try {
            HashMap data = (HashMap) responseEnvironment.getRequestParameter(dataKey);
            Properties messages = new Properties();
            messages.load(ResourceUtil.getResourceAsStream("Messages.properties"));
            data.putAll(messages); // add the messages to the data hashmap
            // . . .
            byte[] content = PDFUtil.getPDF(getParameter("xslStyleSheet"), data);
            responseEnvironment.sendBytes("application/pdf", content);
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }
}
