<%@ page import="uk.gov.courtservice.xhibit.business.services.userterminal.*,
                 uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue,
                 uk.gov.courtservice.xhibit.business.admin.services.UpdateTerminalHelper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="uk.gov.courtservice.xhibit.business.admin.services.EditTerminalHelper"%>

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
	
		String addedMessage="";
		String courtId ="";
		String courtText = "";
		String courtSiteOrRoom = "";
		EditTerminalHelper model = null;
		String terminalId= "";
		String previousCourtRoomId = "";
		String courtRoomId = "";
		
		String hidTerminalName = "";
		String terminalName = "";
		
		courtId = request.getParameter("courtId");
		courtText = request.getParameter("courtText");
		courtSiteOrRoom = request.getParameter("courtSiteOrRoom");
		previousCourtRoomId = request.getParameter("previousCourtRoomId");
		courtRoomId = request.getParameter("courtRoomId");
		
		terminalName = request.getParameter("terminalName");
		hidTerminalName = request.getParameter("hidTerminalName");
		
		if (courtSiteOrRoom == null){
		    courtSiteOrRoom = "";
		}
		if (previousCourtRoomId  ==null){
		    previousCourtRoomId = courtRoomId;
		}	
		
		// need some logic 
		if (hidTerminalName !=null && !hidTerminalName.equalsIgnoreCase("")){
		    terminalName =hidTerminalName;
		}
		
		final String errorOccured = "<b><font colour='red'>Error occurred adding a terminal, please ensure terminal is not already added</b></font>"; 
	
		if (request.getMethod() == "GET" && request.getParameter("added") !=null){
  			try{
  			    int iTerminalId = UpdateTerminalHelper.processRequest(
  				request.getParameter("terminalId").toLowerCase(),request.getParameter("court"), 
  				request.getParameter("courtSite"), request.getParameter("courtRoom"), 
  				terminalName.toLowerCase(), request.getParameter("siteText"));    
  			    terminalId= Integer.toString(iTerminalId);
  			} catch(Exception e){
  			    e.printStackTrace();
   				request.getSession().setAttribute("errorMessage", errorOccured);
   				terminalId = request.getParameter("terminalId");	
  			}
  			
   			model = new EditTerminalHelper(terminalId,
 	   					courtId,
  	   					request.getParameter("courtSiteId"),
  	   					courtRoomId,
  	   					request.getParameter("terminalName"));
   			// set the boolean added flag.
   			request.getSession().setAttribute("model", model);
   			request.getSession().setAttribute("courtText", courtText);	  
  			response.sendRedirect("terminalAdministration.jsp?courtId=" + courtId);
   			    
	} else {
	    
			model =  new EditTerminalHelper(request.getParameter("terminalId"),
			request.getParameter("courtId"),
			request.getParameter("courtSiteId"),
			request.getParameter("courtRoomId"),
			request.getParameter("terminalName"));
	}
	%>	
	
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%out.print(model.isInAddMode() ? "Add" : "Edit");%> XHIBIT Terminal</title>
</head>
<body>
	<script type="text/javascript">
		function getUrl(componentChanged){			
			var url = "editTerminal.jsp?terminalId="+document.getElementById('terminalId').value;	
			url += "&terminalName="+document.getElementById('terminalName').value;
			url += "&courtText="+document.getElementById('courtText').value;
			
			if(parseInt(document.getElementById('court').value) > 0){
				url += "&courtId="+document.getElementById('court').value;
				if(parseInt(document.getElementById('courtSite').value) > 0 && componentChanged != "court"){
					url += "&courtSiteId="+document.getElementById('courtSite').value;
			
					if(document.getElementById('courtRoom').value > 0 && componentChanged != "court" && componentChanged != "courtSite"){
						 url += "&courtRoomId="+document.getElementById('courtRoom').value;
						 url += "&previousCourtRoomId="+document.getElementById('previousCourtRoomId').value;
						 
					}
				}
			}
			document.getElementById('courtRoom').disabled = "false";
			return url;
		}
		
		function textDisableSelect(){
			var courtSiteIndex = document.getElementById('courtRoom').selectedIndex =0;	
		}
				
		function enableDisableOKButton(){
//			alert(document.getElementById('courtRoom').value);
			var enabled = false;
			if(
				document.getElementById('terminalName').value != ""
				&&parseInt(document.getElementById('court').value) > 0
				&&parseInt(document.getElementById('courtSite').value) > 0
				&& ((parseInt(document.getElementById('courtRoom').value) > 0 &&
				   parseInt(document.getElementById('courtRoom').value) != "<%= previousCourtRoomId%>") ||
					document.getElementById('siteText').value !="" && 	
					document.getElementById('siteText').value !="<%=courtSiteOrRoom%>"
			 )){
			 	enabled = true;			
			 }
			document.getElementById('submit').disabled = !enabled;
		}		
		
		function updateNewShortCode(){
			var code = "";
			if(parseInt(document.getElementById('court').value) > 0
			&&parseInt(document.getElementById('courtSite').value) > 0
			&&document.getElementById('siteText').value != ""){
				code = document.getElementById('shortLocation').value + document.getElementById('siteText').value;
			}
			document.getElementById('newTerminalLocation').value = code;
		}
		function checkCourtRoomAndClearLocation(){
  			if (parseInt(document.getElementById('courtRoom').value) == "<%= previousCourtRoomId%>"){
			 	document.getElementById('newTerminalLocation').value="";						
			}
		}
		
		function checkCourtName(){
			var hiddenCourtText = document.getElementById('courtText');
			var courtSiteIndex = document.getElementById('court').selectedIndex;
			var	courtText = document.getElementById('court').options[courtSiteIndex].text;
			hiddenCourtText.value = courtText;
		}
		function setFocus(){
		// 	document.getElementById('terminalName').focus();
		}
	</script>
	<body onload="enableDisableOKButton(); checkCourtRoomAndClearLocation()">
	<%
	if(model.isInAddMode() || model.getTerminal() != null){			
			%>
			<h1><%out.print(model.isInAddMode() ? "Add" : "Edit");%> Xhibit Terminal for Court: <%=courtText %> </h1>
			<%=addedMessage %>
				<FORM NAME="frmCourt" ACTION="editTerminal.jsp" METHOD="GET" onsubmit="checkCourtName();">
			<input type="hidden" name="added" value="yes">
			<input type="hidden" name="courtText" value="<%=courtText %>">
			<input type="hidden" name="courtId" value="<%=courtId %>">
			<input type="hidden" name="previousCourtRoomId" value="<%=previousCourtRoomId %>">			
			<input type="hidden" name="hidTerminalName" value="<%=model.getTerminalName()%>">			
	
			<table>
				<tr><td>Terminal Name</td><td><INPUT TYPE="TEXT" NAME="terminalName" <%out.print((model.isInAddMode()) ? "" : "DISABLED = \"TRUE\"");%> VALUE="<%out.print(model.getTerminalName());%>" ONKEYUP="enableDisableOKButton()"/></td></tr>
				<% if (!model.isInAddMode()){ %>
				<tr><td>Location</td><td><INPUT TYPE="TEXT" NAME="terminalLocation" SIZE="100" DISABLED="disabled" onFocus="this.blur();" VALUE="<%out.print(model.getTerminalLocation());%>"/></td></tr>
				<% } %>
			</table>			
			<br/>
			<table>
			<!-- COURT -->
		
			<tr><td></td><td>
			<div id="courtDiv" style="visibility:hidden">
			<SELECT ID="court" NAME="court" ONCHANGE="location=getUrl('court');">			
			<OPTION VALUE=0>Select Court...</OPTION>
			<%
			for(XhbCourtBasicValue s : model.getCourts()){
				String selected = (model.getCourtId() > 0 && s.getCourtId() == model.getCourtId()) ? "selected=\"yes\"" : "";
				out.print("<OPTION VALUE="+s.getCourtId()+" "+selected+">"+s.getCourtName()+"</OPTION>");
			}
			%>
			</SELECT>
			</div>
			</td></tr>
			
			<!--Court Site-->
			<%String disabled = (model.getCourtSites() == null) ? "disabled = \"true\"" : "";%>			
			<tr><td>Court Site</td><td><SELECT ID="courtSite" NAME="courtSite" ONCHANGE="location = getUrl('courtSite');"<%out.print(disabled);%>>			
			<OPTION VALUE=\"0\">Select Court Site...</OPTION>
			<%
			if(model.getCourtSites() != null){
				for(XhbCourtSiteBasicValue sbv : model.getCourtSites()){
					String selected = (model.getCourtSiteId() > 0 && sbv.getCourtSiteId() == model.getCourtSiteId()) ? "selected=\"yes\"" : "";
					out.print("<OPTION VALUE=\""+sbv.getCourtSiteId()+"\" "+selected+">"
							+sbv.getCourtSiteCode()+" - "+sbv.getCourtSiteName()+"</OPTION>");
				}
			}
			%>
			</SELECT></td></tr>
			
			<!--Court Room-->
			<%disabled = (model.getCourtRooms() == null) ? "disabled = \"true\"" : "";%>
			<tr><td>Court Room / Location</td><td><SELECT ID="courtRoom" NAME="courtRoom" ONCHANGE="location = getUrl('courtRoom');" <%out.print(disabled);%>>
			<OPTION VALUE="0">Select Court Room...</OPTION>
			<%
			if(model.getCourtRooms() != null){
				for(XhbCourtRoomBasicValue crbv : model.getCourtRooms()){
				    out.print("model" + model.getCourtRoomId());
				    out.print("crbv" + crbv.getCourtRoomName());
		            String selected = (model.getCourtRoomId() > 0 && crbv.getCourtRoomId() == model.getCourtRoomId()) ? "selected=\"yes\"" : "";
					out.print("<OPTION VALUE=\""+crbv.getCourtRoomId()+"\" "+selected+">"
							+crbv.getCourtRoomName()+"</OPTION>");
				}
			}
			%>
			</SELECT>
			<INPUT TYPE="TEXT"  size="50" MAXLENGTH="50" NAME="siteText" VALUE="<%=courtSiteOrRoom%>"
			  ONKEYUP="enableDisableOKButton();textDisableSelect();updateNewShortCode();" ID="siteText"/></td></tr>
			<tr><td>New Location</td><td><INPUT TYPE="TEXT" size="70" NAME="newTerminalLocation" DISABLED="true" VALUE="<%out.print(model.getLocationString());%>"ID="newTerminalLocation"/></td></tr>
			</table>
			<INPUT TYPE="HIDDEN" VALUE="<%out.print(model.getTerminalId());%>" NAME="terminalId" ID="terminalId"/>
			<INPUT TYPE="HIDDEN" VALUE="<%out.print(model.getLocationStringCourtSite());%>" NAME="shortLocation" ID="shortLocation"/>
			<INPUT TYPE="SUBMIT" <%out.print((model.isReadyToSave()) ? "" : "DISABLED = \"TRUE\"");%> VALUE="<%out.print(model.isInAddMode() ? "Add" : "Update");%>" ID="submit" onsubmit="checkCourtName()"/>
			</FORM>
			<%
		}else{
			out.print("<h2>Problem with Terminal ID Supplied</h2>");		
		}
		out.flush();
	%>

<br/><a href="terminalAdministration.jsp?action=cancelled&courtId=<%=courtId %>&courtText=<%=courtText %>">Back to Terminal Administration</a><br/>
</body>
</html> 