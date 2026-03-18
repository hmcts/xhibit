package terminal_id_chooser;

import uk.gov.courtservice.xhibit.client.util.security.TerminalIDFactory;
import org.apache.log4j.Logger;

/*
The purpose of this class is to allow the Xhbit thick client to behave as if it were
running on a different workstation.  This should be useful for testing.  A tester should be
able to run many Xhibit thick clients on the same workstation and they will appear as if logged in
from different workstations.  This will allow a tester to have different clients logged in to 
different courts on the same workstation.
*/

public class SystemPropertyTerminalIDFactory extends TerminalIDFactory {

    private static final Logger log = Logger.getLogger(SystemPropertyTerminalIDFactory.class);
    
    public static final String SYSTEM_PROPERTY_NAME = "xhibit.terminalid";

    public static final String DEFAULT_TERMINAL_ID = "terminalid";

    public String getTerminalID() {
        log.debug("looking up terminal name");

        String hostname = System.getProperty(SYSTEM_PROPERTY_NAME);
	if (hostname == null || hostname.equals("")) {
		hostname = DEFAULT_TERMINAL_ID;
	}

        log.debug("terminal name=" + hostname);
        return hostname.toLowerCase();
    }
}
