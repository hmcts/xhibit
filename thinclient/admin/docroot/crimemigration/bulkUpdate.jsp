<%@page import="java.util.List"; %>
<%@page import="java.text.SimpleDateFormat"; %>
<%@page import="uk.gov.courtservice.xhibit.business.services.migration.CmLogSummaryValue"; %>

<%	// Security Check
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
	
	String fileName = (String) request.getAttribute("fileName");
	List<String> errors = (List<String>) request.getAttribute("errors"); 
	Boolean validObj = (Boolean) request.getAttribute("valid");
	boolean valid = validObj != null && validObj.booleanValue();
	Boolean showLogsObj = (Boolean) request.getAttribute("showLogs");
	boolean showLogs = showLogsObj != null && showLogsObj.booleanValue();
	List<CmLogSummaryValue> logs = (List<CmLogSummaryValue>) request.getAttribute("logs"); 
	CmLogSummaryValue selectedLog = (CmLogSummaryValue) request.getAttribute("selectedLog");
	List<String> processResponse = (List<String>) request.getAttribute("processResponse");
%>

<script>
	function selectCsv() {
		document.getElementById('csvFile').click();
	}
	
	function uploadCsv() {
		document.forms['validateForm'].submit();
	}
	
	function processCsv() {
		if(confirm("Are you sure you want to process pending files?")){
			document.forms['processForm'].submit();
		}
	}
	
	function viewLogs() {
		document.forms['viewLogsForm'].submit();
	}
</script>

<!DOCTYPE html>
<html>
<head>
	<title>Bulk Update</title>
</head>
<body>

	<h2>Bulk Update</h2>
	
  	<div class="button-container">
  		<form name="validateForm"
		  method="post"
		  enctype="multipart/form-data"
		  action="<%= request.getContextPath() %>/crimemigration/bulkUpdate">
			<input type="hidden" name="action" id="action" value="validate" />
			<input type="file" name="csvFile" id="csvFile" accept=".csv,text/csv,application/csv" onchange="uploadCsv();" style="display:none;" />
			<button type="button" onclick="selectCsv()">Upload CSV</button>
		</form>
		
		<form name="processForm"
		  method="post"
		  action="<%= request.getContextPath() %>/crimemigration/bulkUpdate">
			<input type="hidden" name="action" id="action" value="process" />
			<button type="button" onClick="processCsv()">Process CSV</button>
		</form>
		
		<form name="showLogsForm"
		  method="post"
		  action="<%= request.getContextPath() %>/crimemigration/bulkUpdate">
		  	<input type="hidden" name="action" value="showLogsForm"/>
		  	<button type="submit">View Logs</button>
		</form>
		
		<% if (showLogs) { %>
			<form name="hideLogsForm"
			  method="post"
			  action="<%= request.getContextPath() %>/crimemigration/bulkUpdate">
			  	<input type="hidden" name="action" value="hideLogsForm"/>
			  	<button type="submit">Hide Logs</button>
			</form>
		<% } %>

	</div>
	
	<% if ((fileName != null && fileName.length() > 0) || (errors != null && !errors.isEmpty())) { %>
		<div class="bulkUpdateInfoBox">
			<ul> 
				<% if (fileName != null && fileName.length() > 0) { %>
					<li>You have uploaded the file: <b><%= fileName %></b></li>
				<% } %>
				<% if (errors != null && !errors.isEmpty()) { %>
						<li>The file is <b>invalid</b> as it does not conform to the format required:
							<ul>
								<% for (String err : errors) { %>
									<li><%= err %></li>
								<% } %>
							</ul>
						</li>
				<% } else {%>
						<li>The file is <b>valid</b> and you can now press the "Process CSV" button</li>
				<% } %>	
			</ul>
		</div>
	<% } %>
	
	<% if (processResponse != null) { %>
		<div class="bulkUpdateInfoBox">
			<ul> 
				<% if (processResponse != null && !processResponse.isEmpty()) { %>
					<ul>
						<% for (String res : processResponse) { %>
							<li><%= res %></li>
						<% } %>
					</ul>
				<% } %>
			</ul>
		</div>
	<% } %>
	
	<% if (selectedLog != null) { %>
		<div class="bulkUpdateInfoBox">
			<ul>
				<li>Log: <%= selectedLog.getFileName() %></li>
				<li>Status: <%= selectedLog.getStatus() %></li>
				<li>Uploaded: <%= new SimpleDateFormat("dd/MM/yyyy").format(selectedLog.getUploadDate()) %></li>
			</ul>
		</div>
	<% } %>
	
	<div class="button-container">
		<% if (showLogs) { %>
			<form name="viewLogsForm"
		      method="post"
			  action="<%= request.getContextPath() %>/crimemigration/bulkUpdate">
			  	<input type="hidden" name="action" id="action" value="viewLog" />	 
			  	
			  	<label for="selectedLogId"><b>Select a log:</b></label> 
			  	<select name="selectedLogId" id="selectedLogId" onchange="viewLogs()">
			  		<option value="">-- choose a log --</option>
			  		<% if (logs != null) { %>
			  			<% for (CmLogSummaryValue log : logs) { %>
			  				<option value="<%= log.getCmLogsId() %>"
			  					<%= selectedLog != null && log.getCmLogsId() == selectedLog.getCmLogsId() ? "selected=\"selected\"" : "" %>>
			  					<%= log.getFileName() %> (<%= log.getStatus() %>)
			  				</option>
			  			<% } %>
			  		<% } %>
  				<% } %>
		  	</select>
		</form>
		
		<% if (selectedLog != null && showLogs) { %>
			<form name="downloadLogForm"
			  method="get" 
			  action="<%= request.getContextPath() %>/crimemigration/downloadLog">
			  	<input type="hidden" name="cmLogsId" value="<%= selectedLog.getCmLogsId()  %>" />
			  	<button type="submit">Download Log</button>
			</form>
		<% } %>
	</div>	
	
	<hr>
	<a href="/Admin">Back to Administration Website Home </a>

</body>
</html>

<style>
.bulkUpdateInfoBox {
	margin-top: 16px;
	padding: 12px 16px;
	background-color: #e7eef9;
	border-left: 4px solid #1f6fb2;
	color: #000000;
	display: inline-block;
	width: auto;
	max-width: 700px;
}
.bulkUpdateInfoBox ul {
	margin: 0;
	padding-left: 20px;
}
.button-container {
	display: flex;
	gap: 10px;
	align-items: center;
	padding-top: 20px;
}
</style>