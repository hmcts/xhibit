package uk.gov.courtservice.xhibit.client.util;

import java.io.IOException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class HTMLHelper {

    static String os = System.getProperty("os.name");

    public HTMLHelper() {
    }

    static public Process runCommand(String command) {
        String os = System.getProperty("os.name");
        if (isWindowsSystem()) {
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(command);
                return p;
            } catch (IOException ex) {
                System.out.println("Error running command :" + command+"; OS="+os);
                XHIBITConstant.handleError(ex);
            }
            XHIBITConstant.debug("Native runtime execution exit code: " + p.exitValue());
        }
        return null;
    }

    static public String getWindowsURLLauncher(String link) {
        if ((link.indexOf(".htm") > 0) && (link.indexOf("http:") == 0)) {
            // rundll32 does not work on 98 with non local .html files, we
            // use "start" instead
            if (isWindows7System() || isWindows8System() || isWindows10System() ||isWindows2000System() || isWindows2003System())
                return "cmd /c start " + link;
            else if (isWindowsNTSystem() || isWindowsXPSystem())
                return "rundll32 url.dll,FileProtocolHandler " + link;
            else
                return "start " + "\"" + link + "\"";
        } else {
            if (isWindows7System() || isWindows8System() || isWindows10System()||isWindows2000System() || isWindowsNTSystem() || isWindows98System() || isWindowsXPSystem())
                return "rundll32 url.dll,FileProtocolHandler " + link;
            else
                return "start " + "\"" + link + "\"";
        }
    }

    private static boolean isWindowsSystem() {
        if (os.indexOf("Windows") > -1)
            return true;
        return false;
    }

    private static boolean isWindows2000System() {
        if (isWindowsSystem() && (os.indexOf("2000") > -1))
            return true;
        return false;
    }
    
    private static boolean isWindows7System() {
        if (isWindowsSystem() && (os.indexOf("7") > -1))
            return true;
        return false;
    }
    
    private static boolean isWindows8System() {
        if (isWindowsSystem() && (os.indexOf("8") > -1))
            return true;
        return false;
    }
    private static boolean isWindows10System() {
        if (isWindowsSystem() && (os.indexOf("10") > -1))
            return true;
        return false;
    }
    
    private static boolean isWindows2003System() {
        if (isWindowsSystem() && (os.indexOf("2003") > -1))
            return true;
        return false;
    }

    private static boolean isWindowsNTSystem() {
        if (isWindowsSystem() && (os.indexOf("NT") > -1))
            return true;
        return false;
    }

    private static boolean isWindowsXPSystem() {
        if (isWindowsSystem() && (os.indexOf("XP") > -1))
            return true;
        return false;
    }

    private static boolean isWindows98System() {
        if (isWindowsSystem() && (os.indexOf("98") > -1))
            return true;
        return false;
    }

}