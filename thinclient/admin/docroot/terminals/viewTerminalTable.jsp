<%@ page import="uk.gov.courtservice.xhibit.business.services.userterminal.*,
                 uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue"%>
<html>
<head/>
<body>
<!-- Author Neil Ellis (C) 2004 EDS -->

<h1>Terminals in Xhibit</h1>

<form action="synchronizeTerminals.jsp" method="GET">
<input type="submit" value="synchronize"/>
</form>


<table>
<%

    XhbTerminalBasicValue[] terminalLocations = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().getXHIBITTerminalLocations();

    for (int i = 0; i < terminalLocations.length; i++)
    {
        XhbTerminalBasicValue terminalLocation = terminalLocations[i];
        out.println("<tr><td>"+terminalLocation.getTerminalName()+"</td><td>"+terminalLocation.getLocation()+"</td></tr>");
    }

%>
</table>

<br/><a href="..">Back to Admin Home</a><br/>
</body>
</html>

