package uk.gov.courtservice.xhibit.web.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>
 * Title: UrlUtil
 * </p>
 * <p>
 * Description: A bunch of utilities for manipulating urls
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
 * $Revision: 1.4 $ $Log: UrlUtil.java,v $
 * $Revision: 1.4 $ Revision 1.4  2006/06/05 12:30:26  bzjrnl
 * $Revision: 1.4 $ Change: TI901
 * $Revision: 1.4 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * $Revision: 1.4 $ $Revision: 1.4 $ Revision 1.3
 * 2006/05/31 14:23:59 bzjrnl $Revision: 1.4 $ Change: TI901 $Revision: 1.4 $
 * Comment: Weblogic Upgrade - Standadise code formatting $Revision: 1.4 $
 * Revision 1.2 2003/10/01 15:33:54 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.1 2003/03/24 11:32:41 fz0n8j Add code for fetching content of url.
 * 
 * 
 */
public class UrlUtil {
    /**
     * <p>
     * Loads the requested resource from the url.
     * </p>
     * 
     * @param url
     *            a String specifying the url of the requested resource
     * @throws FrameworkException
     *             if an error occurs
     */
    public static String getUrlResourceAsString(String url) throws FrameworkException {
        try {
            return getUrlResourceAsString(new URL(url));
        } catch (MalformedURLException murle) {
            throw new FrameworkException(murle);
        }
    }

    /**
     * <p>
     * Loads the requested resource from the url.
     * </p>
     * 
     * @param url
     *            a URL specifying the url of the requested resource
     * @throws FrameworkException
     *             if an error occurs
     */
    public static String getUrlResourceAsString(URL url) throws FrameworkException {
        try {
            StringBuffer buffer = new StringBuffer();

            Reader in = new BufferedReader(new InputStreamReader(url.openStream()));
            for (int c = in.read(); c != -1; c = in.read()) {
                buffer.append((char) c);
            }

            return buffer.toString();
        } catch (IOException ioe) {
            throw new FrameworkException(ioe);
        }
    }

    /**
     * Stops this class being constructed unnecessarily
     */
    private UrlUtil() {
    }

}
