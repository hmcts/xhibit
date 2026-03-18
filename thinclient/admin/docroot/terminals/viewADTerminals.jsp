<%@ page import="uk.gov.courtservice.xhibit.business.services.userterminal.*"%>
<html>
<head/>
<body>
<!-- Author Neil Ellis (C) 2004 EDS -->

<h1>Terminals in Active Directory</h1>

<form action="synchronizeTerminals.jsp" method="GET">
<input type="submit" value="synchronize"/>
</form>

<p>Click on terminal name below to synchronise.</p>
<table>
<%

    /*TerminalLocation[] terminalLocations = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().getActiveDirectoryTerminalLocations();

    for (int i = 0; i < terminalLocations.length; i++)
    {
        TerminalLocation terminalLocation = terminalLocations[i];
        out.println("<tr><td><a href='synchronizeTerminals.jsp?TRX=single&terminalName="+terminalLocation.getName()+"'>"+terminalLocation.getName()+"</a></td><td>"+terminalLocation.getLocation()+"</td></tr>");
    }*/

%>
</table>
<br/><a href="..">Back to Admin Home</a><br/>
</body>
</html>

