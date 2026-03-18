<%@ page import="uk.gov.courtservice.xhibit.business.services.userterminal.*,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue"%>
<html>
<head/>
<body>
<%  String trx = request.getParameter("TRX");
    if (trx != null)
    {
        trx = trx.trim();
        String[] errors = new String[]{};
        if (trx.equals("all") )
        {
            out.print("<h1>Synchronizing with All Terminals with Active Directory ... </h1>");
            out.flush();
            errors = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().synchronizeLocations();
        } else if (trx.equals("single")) {
            String terminalName = request.getParameter("terminalName");
            if (terminalName != null 
                && terminalName.trim().length()>0)
            {
                out.print("<h1>Synchronizing with terminals named " + terminalName + " Active Directory ... </h1>");
                out.flush();
                errors = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().synchronizeLocation(terminalName);
            }
        } else if (trx.equals("court")) {
            String courtShortName = request.getParameter("courtShortName");
            if (courtShortName != null 
                && courtShortName.trim().length()>0)
            {
                errors = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().synchronizeByCourt(courtShortName.toUpperCase());
            }
        }
        if (errors.length > 0)
        {
            out.print("The following terminals failed to synchronise:<br>");
            for (int i=0;i<errors.length;i++)
            {
                out.print("&nbsp;&nbsp;&nbsp;" + errors[i] + "<br>");
            }
        }
%>      
        <h1>Done</h1>
        <br/><a href="synchronizeTerminals.jsp">Back to Synchronise Terminals</a><br/>
<%  } else {
%>
        <h1>Synchronise Terminals</h1>
        <form method="GET" action="synchronizeTerminals.jsp">
            <input type="HIDDEN" name="TRX" value="single">
            Enter terminal name: <input type="text" name="terminalName"> <input type="submit" value="Synchronise">
        </form>
        <form method="GET" action="synchronizeTerminals.jsp">
            <input type="HIDDEN" name="TRX" value="all">
            Synchronise All Terminals: <input type="submit" value="Synchronise All">
        </form>
        <table cellspacing="0" callpadding="1" border="1">
        <tr><th>Synchronise Terminals By Court</th></tr>
        <% 
            XhbCourtBasicValue[] courts = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().getAllCourts();
            for (int i=0;i<courts.length;i++)
            { %>
                <tr><td><a href="synchronizeTerminals.jsp?TRX=court&courtShortName=<%= courts[i].getShortName()%>"><%= courts[i].getDisplayName() %></a></td></tr>
        <%  }
        %>
                <tr><td><a href="synchronizeTerminals.jsp?TRX=court&courtShortName=ROAM">Terminals not associated with a Court</a></td></tr>
        </table>
<%  } %>
<form action="viewTerminalTable.jsp" method="GET">
<input type="submit" value="View Terminal Table"/>
</form>

<br/><a href="..">Back to Admin Home</a><br/>
</body>
</html>

