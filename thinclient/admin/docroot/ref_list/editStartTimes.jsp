<%@ page import="uk.gov.courtservice.framework.services.CSServices,
                 uk.gov.courtservice.xhibit.business.services.admin.*,
                 uk.gov.courtservice.xhibit.business.entities.xhb_crest_import.XhbCrestImportBasicValue,
                 uk.gov.courtservice.xhibit.web.framework.util.ExceptionUtil,
                 java.util.ArrayList,
                 java.util.Calendar,
                 java.util.Date,
                 java.util.HashMap,
                 java.text.SimpleDateFormat,
                 java.sql.Timestamp"%>
<%!	private SimpleDateFormat formatter = new SimpleDateFormat("H:mm");

	private String getTimeToRun(Date timeToRun)
	{
		if (timeToRun==null) return "";
        return formatter.format(timeToRun);
	}
%>
<html>
<head>
<script type="text/javascript" src="js/TimeValidation.js">
</script>
<script type="text/javascript">
function validateAndSubmit()
{
//    alert("validating");
    var isValidated = true;
    var numElements = startTimes.elements.length;
    for (var i=0; i<startTimes.elements.length; i++)
    {
    	var elementName = new String(startTimes.elements[i].name);
    	if (startTimes.elements[i].name.substr(0,4) == "TIME")
    	{
 			var courtId = parseInt(elementName.substr(4));
// 			alert(courtId);
 			var courtChanged = eval("startTimes.SAVE" + courtId + ".checked");
// 			alert (courtChanged);
    		if (courtChanged)
    		{
				if (!isTime(startTimes.elements[i].value))
				{
					if (isValidated) startTimes.elements[i].focus();
					isValidated = false;
					startTimes.elements[i].style.backgroundColor="FFFF33";
				} else {
				startTimes.elements[i].style.backgroundColor="FFFFFF";
				}
    		} else {
				startTimes.elements[i].style.backgroundColor="FFFFFF";
    		}
    	}
    }

    if (isValidated) {
    	document.startTimes.submit();
    } else {
    	alert("One or more times entered are not valid.\nPlease enter a time in the format [h]h:mm in 24 hour clock format.\nonly times after 18:30 or before 06:00 are valid.\n\nPlease correct highlighted fields");
    }
}
</script>
</head>
<body>
<h1>Edit Overnight Process Start Times</h1>
<a href="..">Back to Admin Home</a>
<p>This page will allow you to modify the start times of the reference data loads.
<br/>The time input is 24 hour clock format and only permits start times after 18:30 or before 06:00.<br/>
<%
	out.flush();
	RefDataAdminControllerBeanBusinessDelegate controller = 
		RefDataAdminControllerBeanBusinessDelegate.DelegateFactory.getInstance();
%>

<% 	if (request.getParameter("TRX") != null && request.getParameter("TRX").equals("save"))
	{
		XhbCrestImportBasicValue[] crestImports = controller.getAllCrestImportCourts();
		ArrayList startTimeSaveValues = new ArrayList();
		for (int i=0; i < crestImports.length; i++)
		{
			Integer thisCourtId = crestImports[i].getCourtId();
			String  thisImportType = crestImports[i].getImportType();
//			out.write("SAVE VALUE=" + request.getParameter("SAVE" + thisCourtId));
			if (request.getParameter("SAVE" + thisCourtId) != null
			    && request.getParameter("SAVE" + thisCourtId).equals("on"))
			{
				try {
  					String refDataStartTime = request.getParameter("TIME" + thisCourtId + thisImportType);
// 						out.write("Requesting " + ("TIME" + thisCourtId + thisImportType) + "=" + refDataStartTime);
  					if (refDataStartTime == null
  						|| refDataStartTime.trim().equals(""))
  					{
  						if (crestImports[i].getTimeToRun() != null)
  						{
							out.write("Saving court id: " + thisCourtId + ", " + thisImportType + "<br>");
  							crestImports[i].setTimeToRun(null);
							startTimeSaveValues.add(crestImports[i]);
						}
					} else {
//  						out.write("refDataStartTime(hh)=" + refDataStartTime.substring(0, refDataStartTime.indexOf(":")) );
//  						out.write("refDataStartTime(mm)=" + refDataStartTime.substring(refDataStartTime.indexOf(":")+1)  );
						int hh = Integer.valueOf(refDataStartTime.substring(0, refDataStartTime.indexOf(":")) ).intValue();
						int mm = Integer.valueOf(refDataStartTime.substring(refDataStartTime.indexOf(":")+1) ).intValue();
						Calendar c = Calendar.getInstance();
						c.set(Calendar.HOUR_OF_DAY, hh);
						c.set(Calendar.MINUTE, mm);
						c.set(Calendar.SECOND, 0); 
						// Check if old time and new time are different
						if (!getTimeToRun(c.getTime()).equals(getTimeToRun(crestImports[i].getTimeToRun())))
						{
							out.write("Saving court id: " + thisCourtId + ", " + thisImportType + "<br>");
							crestImports[i].setTimeToRun(new Timestamp(c.getTime().getTime()));
							startTimeSaveValues.add(crestImports[i]);
						}
					}
				} catch (Exception e) {
  					out.write("ERROR Saving court id: " + thisCourtId + ". NOT SAVED<br>");
  					ExceptionUtil.printStackTraces(e, out);
				}
			}
			out.flush();
		}
		if (!startTimeSaveValues.isEmpty())
		{
			controller.setCrestImportValues(
				(XhbCrestImportBasicValue[])startTimeSaveValues.toArray(new XhbCrestImportBasicValue[startTimeSaveValues.size()])
			);
		}
%>
<h2>Data Saved</h2>
<%		out.flush();
	}
%>
<center>
		<form name="startTimes" method="POST" action="editStartTimes.jsp">
			<input type="button" onclick="validateAndSubmit();" value="Save">
			<input type="hidden" name="TRX" value="save">
			<table width="90%" border="1" cellspacing="0" bordercolor="#000000">
				<tr>
				<th>Court Id</th>
				<th>Court Name</th>
				<th>Ref Data Start Times</th>
				<th>Save</th>
				</tr>
<% 	 

	XhbCrestImportBasicValue[] importValues = controller.getAllCrestImportCourts();
	HashMap courtNamesMap = controller.getCourtNames();

	for (int i=0; i < importValues.length; )
	{
		int currCourtId = importValues[i].getCourtId().intValue();
%>
				<tr>
				<td><%= currCourtId %></td>
				<td><%= courtNamesMap.get(importValues[i].getCourtId()) %></td>
                <td>
                    <table cellpadding="2" cellspacing="0"><tr>
<%				do { %>
                    <td><%= importValues[i].getImportType() %>:&nbsp;<input type="text" size="5" maxlength="5" name="TIME<%= currCourtId %><%= importValues[i].getImportType() %>" value="<%= getTimeToRun(importValues[i].getTimeToRun()) %>" onchange="document.startTimes.SAVE<%= currCourtId %>.checked=true;" /></td>
<%					i++;
				} while (i < importValues.length && (importValues[i].getCourtId().intValue()==currCourtId));
%>
                    </tr></table>                    
                </td>
				<td align="center"><input name="SAVE<%= currCourtId %>" type="checkbox"></td>
				</tr>
<%	} %>
			</table>
		<input type="button" onclick="validateAndSubmit();" value="Save">
		</form>
</center>

<br/><a href="..">Back to Admin Home</a><br/>
</body>
</html>

