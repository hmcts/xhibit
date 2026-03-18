Purpose
=======

The purpose of the terminal_id_chooser.jar is to allow the Xhbit thick client to behave as if it were
running on a different workstation.  This should be useful for testing.  A tester should be
able to run many Xhibit thick clients on the same workstation and they will appear as if logged in
from different workstations.  This will allow a tester to have different clients logged in to 
different courts on the same workstation.


To Build
========

cd <to the base of the XHIBIT files>
xhbenv.bat
cd tools\util\terminal_id_chooser
ant


Instructions for use
====================

To use the terminal id chooser, edit your xhibit.bat file and add the following lines:

SET JAVA_OPTIONS=%JAVA_OPTIONS% -Duk.gov.courtservice.xhibit.client.util.security.TerminalIDFactory=terminal_id_chooser.SystemPropertyTerminalIDFactory
SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dxhibit.terminalid=xhibit_terminal_name

in the above text xhibit.terminalid should be set to a value from the TERMINAL_NAME column of the
DB table XHB_TERMINAL.

Then edit the XHIBIT_CLASSPATH setting and add the terminal_id_chooser.jar file to 
it (use a full explicit file path if you need to), e.g.

  SET XHIBIT_CLASSPATH=F:\Projects\XHIBIT\XHIBIT\tools\util\terminal_id_chooser\dist\terminal_id_chooser.jar;..\properties;......

To run many clients at once, each of which appear to be from a different court, then
create many xhibit.bat files and set the -Dxhibit.terminalid=xhibit_terminal_name to specify a
terminal from the appropriate court.



