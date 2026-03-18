<%@page import="uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue,
                 uk.gov.courtservice.xhibit.business.admin.services.UnauthorisedCaseAdminHelper"; @%>
				 
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
	
	UnauthorisedCaseAdminHelper model = new UnauthorisedCaseAdminHelper(
	request.getParameter("courtId"),
	request.getParameter("caseType"),
	request.getParameter("caseNumber"),
	request.getParameter("defendantId"),
	request.getParameter("whichButton"));

	String caseNumber = "";
	if (request.getParameter("caseNumber") != null) {
	    caseNumber = request.getParameter("caseNumber").toString();
	}
	
	String caseType = "T";
	if (request.getParameter("caseType") != null) {
	    caseType = request.getParameter("caseType").toString();
	}
	
	String defendantId = "";
	if (request.getParameter("defendantId") != null) {
	    defendantId = request.getParameter("defendantId").toString();
	}
	
	String courtId = "";
	if (request.getParameter("courtId") != null) {
	    courtId = request.getParameter("courtId").toString();
	}

	String message = "";
	if (model != null) {
		if (model.getReturnMessage() != null) {
		    message = model.getReturnMessage();
		}
	}
%>

<script>

	 function sortSelect(elem) {
	 	var tmpAry = [];
	 	elem = document.getElementById(elem);
	 	var selectedValue = elem[elem.selectedIndex].value;
	 	
	 	for (var i=0; i<elem.options.length; i++) {
	 		tmpAry.push(elem.options[i]);
	 	}
	 	tmpAry.sort(function(a,b) { return (a.text < b.text) ? -1 : 1 });

	 	while (elem.options.length > 0) {
	 		elem.options[0] = null;
	 	}

	 	var newSelectedIndex = 0;
	 	for (var i=0; i<tmpAry.length; i++) {
			elem.options[i] = tmpAry[i];
			if (elem.options[i].value == selectedValue) {
				newSelectedIndex = i;
			}
	 	}

	 	elem.selectedIndex = newSelectedIndex;
	 	
	 	return;
	}
	 
 	function validateCaseNumber() {
 		caseNo = document.getElementById('caseNumber').value;
 		allValid = true;
 		// Check length of caseNo
 		if (caseNo.length != 8) {
 			allValid = false;
 		}
 		
 		// Check all numerics
 		if (!isNumber(caseNo)) {
 			allValid = false;
 		} else {
 			// Check first 4 digits are greater than 1990 and less than or equal to current year
 			caseyear = caseNo.substring(0,4);
 			thisyear=new Date().getFullYear();

 			if (caseyear<1990) {
 				alert('Invalid year < 1990; must be after 1990 and less than or equal to current year');
	 			allValid = false;
 	 			return false;
 	 		} else if (caseyear>thisyear) {
 				alert('Invalid year > '+thisyear+'; must be after 1990 and less than or equal to current year');
 	 			allValid = false;
 	 			return false;
 			}
 		}
 		
 		if (allValid) {
 			// Now disable all buttons
 			document.getElementById('SetAsAuthorisedButton').disabled = true;
 			document.getElementById('CheckAuthStatusButton').disabled = true;
 			document.getElementById('FindDefendantsButton').disabled = true;
			return true;
 		} else {
 			alert('The case number is invalid - it must contain 8 digits.');
 			return false;
 		}
 	}
 	
 	function disableButtonsIfThereAreNoDefendants() {
		document.getElementById('SetAsAuthorisedButton').disabled = true;
		document.getElementById('CheckAuthStatusButton').disabled = true; 	
 	}
 	
 	function isNumber(n) {
 		return !isNaN(parseFloat(n)) && isFinite(n);
 	}

</script>
<%@page import="java.util.ArrayList"%>
<%@page import="uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue"%>
<%@page import="java.util.HashMap"%>
<html>
	<head>
		<title>Xhibit Unauthorised Case Status Change - Admin</title>
	</head>

	<body onload="sortSelect('court');<%if ((model != null) && (model.getDefendants() == null)) {%>disableButtonsIfThereAreNoDefendants();<%}%>">
		<h2>Xhibit Unauthorised Case Status Change - Admin</h2>
		
		<!--  Display any messages here -->	
		<p style="color:red;font-size:large">
			<%= message %>
		</p>
		
		<form name="unauthadmin" method="post" action="unauthorisedCasesAdministration.jsp" onSubmit="return validateCaseNumber();">
		<input type="hidden" name="whichButton" />
		<table>
			<tr>
				<!-- COURT -->
				<td><b>Court: <b></td>
	
				<td>
					<SELECT ID="court" NAME="courtId">			
					<%
						for(XhbCourtBasicValue s : model.getCourts()){
						    if (courtId.trim().equals(s.getCourtId().toString().trim())) {
								out.print("<OPTION VALUE="+s.getCourtId()+" selected>"+s.getCourtName()+"</OPTION>");
							} else {
								out.print("<OPTION VALUE="+s.getCourtId()+">"+s.getCourtName()+"</OPTION>");
							}
					    }
					%>
					</SELECT>
				</td>
			</tr>
			<tr>
				<td><b>Case Type: </b></td>
				<td>
					<select id="caseType" name="caseType">
						<option value="T" <% if (caseType.equals("T")) { %>selected<% } %>>T</option>
						<option value="S" <% if (caseType.equals("S")) { %>selected<% } %>>S</option>
						<option value="A" <% if (caseType.equals("A")) { %>selected<% } %>>A</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><b>Case Number: </b></td>
				<td>
					<input type="text" id="caseNumber" name="caseNumber"maxlength="8" value="<% if (caseNumber != null) { %><%=caseNumber.trim() %><% } %>" onkeypress="return ((event.keyCode || event.which) >=48 && (event.keyCode || event.which) <=57)" />
				</td>
				<td>
					<input id="FindDefendantsButton" type="submit" value="Find defendants" onClick="document.getElementById('whichButton').value='FindDefendants'" name="findDefendantsButton">
				</td>
			</tr>
			<tr>
				<td><b>Defendant(s): <b></td>
	
				<td>
					<SELECT ID="defendant" NAME="defendantId">
					<%
						if ((model != null) && (model.getDefendants() != null)) {
						    ArrayList a = model.getDefendants();
							for(int i=0; i<a.size(); i++) {
							    HashMap h = (HashMap) a.get(i);
							    if (h.get("defendantId") != null) {
							        if (defendantId.equals(h.get("defendantId"))) {
										out.print("<OPTION VALUE="+h.get("defendantId")+" selected>"+h.get("defendantName")+"</OPTION>");							        
								    } else {
										out.print("<OPTION VALUE="+h.get("defendantId")+">"+h.get("defendantName")+"</OPTION>");
								    }
							    } else {
									out.print("<OPTION VALUE="+h.get("defendantId")+">"+h.get("defendantName")+"</OPTION>");							        
							    }
						    }

						}
					%>

					</SELECT>
				</td>
				<td><input id="CheckAuthStatusButton" type="submit" onClick="document.getElementById('whichButton').value='CheckAuthStatus'" value="Check Auth Status" name="checkAuthStatusButton"></td>
			</tr>
			<tr>
				<td>
					<input id="SetAsAuthorisedButton" type="submit" onClick="document.getElementById('whichButton').value='SetAsAuthorised'" value="Set as Authorised" name="SetAsAuthorisedButton">
				</td>
			</tr>
		</table>
		</form>
		<hr>
		<a href="/Admin">Back to Administration Website Home </a>
	</body>
</html>