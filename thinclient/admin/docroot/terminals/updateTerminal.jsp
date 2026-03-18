<%@ page import="uk.gov.courtservice.xhibit.business.services.userterminal.*,
                 uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="uk.gov.courtservice.xhibit.business.admin.services.UpdateTerminalHelper"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Updating Terminal...</title>
</head>
<body>
<%	//Security Check
	boolean isAdmin = false;
	if (request.getSession() != null) {
		if ((request.getSession().getAttribute("isAdmin") != null) && (request.getSession().getAttribute("isAdmin").equals("true"))) { // Check if user is a CPS user so that they can see these 2 menu options
		    isAdmin = true;
		}
	}
	
	// Redirect if not admin
	if (!isAdmin) {
	    response.sendRedirect("/Admin");
	}
	
	int terminalId = UpdateTerminalHelper.processRequest(
			request.getParameter("terminalId"),request.getParameter("court"), 
			request.getParameter("courtSite"), request.getParameter("courtRoom"), 
			request.getParameter("terminalName"), request.getParameter("siteText"));
	
	out.println("<h1>Created/Updated terminal </h1>"+terminalId);
	out.println("<a href=\"editTerminal.jsp?terminalId="+terminalId+"\">View Terminal</a>");
%>
</body>
</html>