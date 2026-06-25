package uk.gov.courtservice.xhibit.web.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.gov.courtservice.xhibit.business.services.migration.CmLogSummaryValue;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogsControllerBeanBusinessDelegate;

public class DownloadLogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int cmLogsId = Integer.parseInt(request.getParameter("cmLogsId"));
		
		CmLogsControllerBeanBusinessDelegate delegate = CmLogsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
		
		CmLogSummaryValue logSummaryValue = delegate.getLogSummary(cmLogsId);
		String contents = delegate.getLogContents(cmLogsId);
		
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + logSummaryValue.getFileName() + ".txt\"");
		response.getWriter().write(contents);
	}
}