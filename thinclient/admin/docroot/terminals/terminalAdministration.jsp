		<%@ page import="uk.gov.courtservice.xhibit.business.services.userterminal.*,
		                 uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue,
		                 uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue,
		                 uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue,
		                 uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue,
		                 uk.gov.courtservice.xhibit.business.admin.services.EditTerminalHelper"%>
		 
		<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<html>
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>XHIBIT Terminal Administration </title>
		</head>
		<style type="text/css">
		 a:link { COLOR: #0645AD;}
		 a:visited { COLOR: #0645AD;}
		</style>		 		
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
	
			EditTerminalHelper model = null;
		 	String addedMessage =  "";
		 	String court = "";
		 	String courtText = "";
			String messageOut = "";
			String COURTSITECONSTANT = "S";
			String location = "";
			
			final String CANCELLEDMSG = "<b><font color='red'>No changes made.</b></font>";
		 	if (request.getSession().getAttribute("model") !=null){
				model = (EditTerminalHelper) request.getSession().getAttribute("model");
		 		request.getSession().removeAttribute("model");
		 		court = Integer.toString(model.getCourtId());
		 		addedMessage = "Terminal name <i><b>" + model.getTerminalName() + "</b></i> has been added/updated";
		 		
		 	} else{
		 	    model= new EditTerminalHelper(request.getParameter("terminalId"),
			 			request.getParameter("courtId"),
			 			request.getParameter("courtSiteId"),
			 			request.getParameter("courtRoomId"),
			 			request.getParameter("terminalName"));
		 	 	court = (String) request.getParameter("courtId");
		 	}
			// need to get court site name
			courtText = (String) request.getParameter("courtText");
			if (courtText ==null ||  courtText.equalsIgnoreCase("")){
			    courtText = (String) request.getSession().getAttribute("courtText");
				request.getSession().removeAttribute("courtText");    
			}
			String errorOccured = "";
			if (request !=null && (request.getSession() !=null && request.getSession().getAttribute("errorMessage") !=null )){
					errorOccured = (String) request.getSession().getAttribute("errorMessage");
					request.getSession().removeAttribute("errorMessage");    
			}
			if (!errorOccured.equalsIgnoreCase("")){
			    messageOut = "<b><font color=red>" + errorOccured + "</b></font>";
			} else{
			 	messageOut = addedMessage;   
			}
			// is edit cancelled
			if (request.getParameter("action") !=null){
			    messageOut = CANCELLEDMSG;
			}
			
			%>
			<h1> Terminal Administration for Court: <%=courtText %></h1>
			<p>
			  <%=messageOut	%>
			</p>
			<form name="getCourts" method="get" action="editTerminal.jsp">	
				<input type="hidden" value="<%=courtText %>" name="courtText">
				<table width="70%" border=0>
				<tr>
				   <td colspan="3" align="right">
					   <a href="index.jsp">Back to Terminal Administration Home</a><BR>
				   </td>
				</tr>
				<tr>
					<td width="20%"> <b>Terminal Name</b></td>
					<td width="60%"> <b>Terminal Location</b></td>
					<td width="20%">
						<a href="editTerminal.jsp?courtText=<%=courtText%>&courtId=<%=court%>"><b><i>Add a new terminal</b></i></a> 
					</td>
					
				<tr>	
				<% 	
						
				if (court != null && !court.equalsIgnoreCase("")){
				 	   
					 	XhbTerminalBasicValue[] terminalLocations = UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance().getTerminalsBySite(court);
					    for (int i = 0; i < terminalLocations.length; i++)
					    {
					        XhbTerminalBasicValue terminalLocation = terminalLocations[i];
					        String courtRoomId = "";
					        if (terminalLocation.getCourtRoomId() !=null){
					        	courtRoomId = Integer.toString(terminalLocation.getCourtRoomId());           
					        }
					        if (terminalLocation.getCourtroomOrSite().equalsIgnoreCase("S") &&
					                (terminalLocation.getLocation() !=null && !terminalLocation.getLocation().equalsIgnoreCase(""))){
					                
					          	String locations[] =  terminalLocation.getLocation().split("/");
					        	location = locations[3];
					             
							} else {
							    location = "";
							}
						 
					        out.println("<tr><td> "+terminalLocation.getTerminalName() +
					                 "</td>" 
					        + "<td>" +terminalLocation.getLocation() +"<td>"+"<td>"+
					        "<a href = \"editTerminal.jsp?&courtSiteId=" + terminalLocation.getCourtSiteId() + 
					                "&courtRoomId=" + courtRoomId +
					                "&courtId=" + court +"&courtText=" + courtText +
					                "&terminalId="+terminalLocation.getTerminalId()+
					                "&courtSiteOrRoom="+ location +"\">Edit</a></td></tr>");
					    }
		
				 	}
			    %>		
		
			<table>
			</form>
			
		<br/><a href="index.jsp">Back to Terminal Administration Home</a><br/>
		<br/><a href="/Admin">Back to XHIBIT Administration Home</a><br/>
		
		</body>
		</html>