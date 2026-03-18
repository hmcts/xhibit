package uk.gov.courtservice.xhibit.client.results.NHA;

import java.awt.event.ActionEvent;
import java.util.Map;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.common.results.vos.ISingleRunLetterReport;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class PreviewPrintOnceLetterReportAction extends PreviewReportAction {

	private static final long serialVersionUID = 1L;

	public PreviewPrintOnceLetterReportAction(ReportAbsttractValue reportValue, String location, String report, Map map ) {
		super(reportValue, location, report, map);
	}
	
	/**
     * Preview the list
     * 
     * @param e
     *            the event that caused this method to be called
     * @throws Exception
     *             if an error occures
     */
    public void xActionPerformed(ActionEvent e) throws Exception {
        // Preview the formated list xml
    	PrintOnceLetterReportPrintPreviewHelper helper = new PrintOnceLetterReportPrintPreviewHelper((ISingleRunLetterReport) reportValue);
        helper.printDocument(getFormatedXml(), true, getShortDescription());
    } 
}
