package uk.gov.courtservice.xhibit.client.util;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */

//
// This simple code generates a new Interenet Explorer window for the given URL
// This window fills the screen, has no toolbars, statusbars or frame and at the
// moment can only
// be killed by using Alt + F4
// The -k switch turns this instnace into 'kiosk' mode.
//
public class DisplaySimpleBrowser {

    private String iePath, ieCMD, ieSwitch, strURL;

    private static final Logger log = CSServices.getLogger(DisplaySimpleBrowser.class);

    public DisplaySimpleBrowser() {

        super();

        strURL = "file://w2lzjh1k01/courtdetail/DailyList.html";
        iePath = "C:\\Program Files\\Internet explorer\\";
        ieCMD = "IExplore.exe";
        ieSwitch = "-k";

        try {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(iePath + ieCMD + " " + ieSwitch + " " + strURL);
        } catch (Exception e) {
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);

        }
    }

    public static void main(String[] args) {
        DisplaySimpleBrowser sb = new DisplaySimpleBrowser();
        // Hitting Alt + F4 will close the window thus generated
    }

}