<%@ page import="uk.gov.courtservice.xhibit.business.services.admin.*,
	         uk.gov.courtservice.xhibit.business.entities.xhb_formatting.*,
	         uk.gov.courtservice.xhibit.business.entities.xhb_court.*,
	         java.util.*"%>
	         
<html>
	<head/>
	<body>
	<h1>Internet Web Pages</h1>
	<p><a href="/Admin	">Back to Admin Home</a></p>	         
	         
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
	
	String selectedDate =  null;
	Integer selectedCourt = new Integer(0);
	Integer firstCourt = null;

	/* retrieve request variables */
	if (request.getParameter("DATE") != null)
	{
		selectedDate = request.getParameter("DATE");
	}
	if (request.getParameter("COURT") != null)
	{
		selectedCourt = new Integer(Integer.parseInt(request.getParameter("COURT")));
	}
	
	Integer selectedDay = new Integer(0);
	Integer selectedMonth = new Integer(0);
	Integer selectedYear = new Integer(0);
	
	/* create a calendar object */
	GregorianCalendar myCal = new GregorianCalendar();
	
	if (selectedDate != null)
	{
		try
		{
			selectedDay = Integer.valueOf(selectedDate.substring(0,2));
			selectedMonth = Integer.valueOf(selectedDate.substring(3,5));
			selectedYear = Integer.valueOf(selectedDate.substring(6));

			myCal.set(Calendar.DAY_OF_MONTH,selectedDay.intValue());
			myCal.set(Calendar.MONTH,selectedMonth.intValue()-1);
			myCal.set(Calendar.YEAR,selectedYear.intValue());
		}
		catch (Exception e)
		{
			out.print("<h1>Invalid Date - " + selectedDate + "</h1>");
		}
	}
	         
	selectedDay = new Integer(myCal.get(Calendar.DAY_OF_MONTH));
	selectedMonth = new Integer(myCal.get(Calendar.MONTH));
	selectedYear = new Integer(myCal.get(Calendar.YEAR));
	
	String displayDay = selectedDay.toString();
	if (displayDay.length()==1)
	{	displayDay = "0" + displayDay;	}
	Integer correctMonth = new Integer(selectedMonth.intValue()+1);
	String displayMonth = correctMonth.toString();
	if (displayMonth.length()==1)
	{	displayMonth = "0" + displayMonth;	}
	String displayYear = selectedYear.toString();
		
		
	RefDataAdminControllerBeanBusinessDelegate controller = RefDataAdminControllerBeanBusinessDelegate.DelegateFactory.getInstance();
			
	XhbCourtBasicValue[] courts = controller.getCourts();

%>
	<form name="ListForm" method="post" action="listInternetWebPages.jsp" target="_self" ID="Form1">
		<p>
			<table cellSpacing="1" cellPadding="1" border="1">
				<tr>
					<td align="center">Required Date<br/>dd/mm/yyyy</td>
					<td><input id=DATE type="text" maxLength="10" size="10" name="DATE" value="<%= displayDay %>/<%= displayMonth %>/<%= displayYear %>"></TD>
					<td align="center">Required<br/>Court</td>
					<td>
						<select size="1" id="COURT" name="COURT">
<%

	String selectedText;
	
	for (int i=0; i < courts.length; i++)
	{
		selectedText = "";
	
		XhbCourtBasicValue court = courts[i];
		
		if (i == 0)
		{
			firstCourt = court.getCourtId();
		}
		
		if (court.getCourtId().equals(selectedCourt))
		{
			selectedText = "selected";
		}
		
		out.print("<option " + selectedText + " value='" + court.getCourtId() + "'>" + court.getCourtName() + "</option>");
	}
	
	if (selectedCourt.intValue() == 0)
	{
		selectedCourt = firstCourt;
	}
	
%>

						</select></td>
					<td>
						<Input id="Submit" type="submit" value="Refresh" name="Submit"></td>
				</tr>
			</table>
		</p>
	</form>	<table cellSpacing="2" cellPadding="2" border="1">
<%

	XhbFormattingBasicValue[] docs = controller.getIWPDocuments(selectedDay.intValue(),selectedMonth.intValue(),selectedYear.intValue(),selectedCourt);

	out.print("<tr><th>Formatting<br/>Id</th><th>Court<br/>Id</th><th>Format<br/>Status</th><th>Creation<br/>Date</th><th>Last Update<br/>Date</th><th>Language<br/>&nbsp;</th><th>Blob<br/>Id</th></tr>");
	
	for (int i=0; i < docs.length; i++)
	{
	
	XhbFormattingBasicValue doc = docs[i];
	
	out.print("<tr>");
	
		out.print("<td align='right'>" + doc.getFormattingId() + "</td>");
	
		out.print("<td align='right'>" + doc.getCourtId() + "</td>");
	
		out.print("<td>" + doc.getFormatStatus() + "</td>");
	
		out.print("<td>" + doc.getCreationDate().toLocaleString() + "</td>");
	
		out.print("<td>" + doc.getLastUpdateDate().toLocaleString() + "</td>");
	
		out.print("<td>" + doc.getLanguage() + "</td>");
	
		out.print("<td align='right'><a href='onlineservices\\xhibit\\viewInternetWebPage.jsp?BLOB_ID=" + doc.getFormattedDocumentBlobId() + "' target='_blank'>"	+ doc.getFormattedDocumentBlobId() + "</a></td>");
	
	out.print("</tr>");
	
	}

%>
</table>
<p><a href="/Admin">Back to Admin Home</a></p>
</body>
</html>
