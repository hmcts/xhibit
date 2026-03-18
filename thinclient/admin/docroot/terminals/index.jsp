<%@page import="uk.gov.courtservice.xhibit.business.services.userterminal.*,
                 uk.gov.courtservice.xhibit.business.entities.xhb_terminal.XhbTerminalBasicValue,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue,
                 uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue,
                 uk.gov.courtservice.xhibit.business.admin.services.UpdateTerminalHelper,
				 uk.gov.courtservice.xhibit.business.admin.services.EditTerminalHelper"; @%>
				 
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
	
	String addedMessage = "";
	EditTerminalHelper model = new EditTerminalHelper(request.getParameter("terminalId"),
	request.getParameter("courtId"),
	request.getParameter("courtSiteId"),
	request.getParameter("courtRoomId"),
	request.getParameter("terminalName"));
	// sort
	
%>	

<script>

function sortSelect(selElem) {   
			  selElem = document.getElementById(selElem);
              var tmpAry = new Array();                 
              for (var i=0;i<selElem.options.length;i++) {                         
                                        tmpAry[i] = new Array();                         
                                        tmpAry[i][0] = selElem.options[i].text;                         
                                        tmpAry[i][1] = selElem.options[i].value;                 
              }                 
              tmpAry.sort();    
              while (selElem.options.length > 0) {
                     selElem.options[0] = null; 
              }                 

                      for (var i=0;i<tmpAry.length;i++) {                         
                           var op = new Option(tmpAry[i][0], tmpAry[i][1]);                         
                           selElem.options[i] = op;                 
                      }           
                      return;    
	 } 


	function checkVal(){
		courtSel = document.getElementById('courtId').selectedIndex;
		showCourt = document.getElementById('showCourt');
		if (courtSel>0){
			showCourt.disabled = false;
		}
		else{
			showCourt.disabled = true;

		}	

	}
	function getCourtName(){
		courtId = document.getElementById('courtId').selectedIndex;
		courtText = document.getElementById('courtId').options[courtId].text;
//		alert(courtText);
		jsForm = document.getElementById('court');
		jsForm = jsForm.action = "terminalAdministration.jsp?courtText=" + courtText;
		jsForm.submit();
 	}
</script>
<html>
	<head>
		<title>Xhibit Administration Homepage</title>
	</head>
<body onload="sortSelect('courtId')">
		<!-- Author Neil Ellis (C) 2004 EDS -->
		<!--  Updated by Ian Simmons, Logica - 13:02 17/11/2010 -->
		<h1>XHIBIT Terminal Administration Homepage</h1>
		<table>
			<!-- COURT -->
			<tr><td><h2>Court<h2></td>
			<td>
				<form name="court" method="post" onsubmit="getCourtName()">
				<SELECT ID="court" NAME="courtId">			
				<%
					for(XhbCourtBasicValue s : model.getCourts()){
					String selected = (model.getCourtId() > 0 && s.getCourtId() == model.getCourtId()) ? "selected=\"yes\"" : "";
					out.print("<OPTION VALUE="+s.getCourtId()+" "+selected+">"+s.getCourtName()+"</OPTION>");
				    }
				%>
				</SELECT>
				<input type="submit" value="Show Court" name="showCourt">
				</form>
			</td>
			</tr>
		
		</table>
		<hr>
		<a href="/Admin">Back to Administration Website Home </a>
		</body>
</html>
	