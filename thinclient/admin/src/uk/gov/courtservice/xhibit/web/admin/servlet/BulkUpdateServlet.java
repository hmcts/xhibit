package uk.gov.courtservice.xhibit.web.admin.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.admin.services.BulkUpdateHelper;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogSummaryValue;

public class BulkUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String ACTION_FIELD_NAME = "action";
	private static final String FILE_FIELD_NAME = "csvFile";
	private static final String FORWARD_JSP = "/crimemigration/bulkUpdate.jsp";
	
	private static final Logger log = CSServices.getLogger(BulkUpdateServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BulkUpdateHelper helper = new BulkUpdateHelper();

		List<CmLogSummaryValue> logs = helper.getLogs();
		request.setAttribute("logs", logs);
		
		forward(request,response, FORWARD_JSP);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			String action = "";
			FileItem csvFile = null;
			
			if (ServletFileUpload.isMultipartContent(request)) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				
				List<FileItem> items = upload.parseRequest(request);
				
				for (FileItem item : items) {
					if (item.isFormField()) {
						if (ACTION_FIELD_NAME.equals(item.getFieldName())) {
							action = item.getString();
						}
					} else if(FILE_FIELD_NAME.equals(item.getFieldName())) {
						csvFile = item;
					}
				}
			} else {
				action = request.getParameter(ACTION_FIELD_NAME);
			}
			
			BulkUpdateHelper helper = new BulkUpdateHelper();
			
			if ("validate".equals(action)) {
				List<String> errors = new ArrayList<String>();
				
				if (csvFile == null) {
					errors.add("No file supplied"); 
				} else {
					errors = helper.validate(csvFile, request.getRemoteUser());
					request.setAttribute("fileName", csvFile.getName());
				}
				
				request.setAttribute("errors", errors);
				request.setAttribute("valid", errors.isEmpty());
			}
			else if ("process".equals(action)) {
				List<String> processResponse = helper.process(request.getRemoteUser());
				request.setAttribute("processResponse", processResponse);
			}
			else if("showLogsForm".equals(action)) {
				List<CmLogSummaryValue> logs = helper.getLogs();
				request.setAttribute("logs", logs);
				request.setAttribute("showLogs", true);
			}
			else if("hideLogsForm".equals(action)) {
				request.setAttribute("showLogs", false);
			}
			else if ("viewLog".equals(action)) {
				String selectedLogIdParam = request.getParameter("selectedLogId");
				
				if (selectedLogIdParam != null && selectedLogIdParam.trim().length() > 0) {
					int selectedLogId = Integer.valueOf(request.getParameter("selectedLogId"));
					CmLogSummaryValue selectedLog = helper.getSelectedLog(selectedLogId);
					request.setAttribute("selectedLog", selectedLog);
				}
				
				List<CmLogSummaryValue> logs = helper.getLogs();

				request.setAttribute("logs", logs);
				request.setAttribute("showLogs", true);
			}
			
			forward(request, response, FORWARD_JSP);
		}
		catch(Exception ex) {
			log.error("BulkUpdateServlet: Unable to process request." , ex);
		}
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);
	}
}