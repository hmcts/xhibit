package uk.gov.courtservice.xhibit.client.results.NHA;

import org.apache.fop.apps.FOUserAgent;

import uk.gov.courtservice.xhibit.client.print.viewer.XhibitPreviewDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.results.vos.ISingleRunLetterReport;

public class PrintOnceLetterReportPreviewDialog extends XhibitPreviewDialog {

	private static final long serialVersionUID = 1L;
	private ISingleRunLetterReport reportList;

	public PrintOnceLetterReportPreviewDialog(FOUserAgent foUserAgent, ISingleRunLetterReport reportValue) {
		super(foUserAgent);
		this.reportList = reportValue;
	}
	
	/**
     * This is an override of print() and it does the same things, but also updates the NHA records in the database
     */
    protected void print() {
    	super.print();
    	
    	//After successful printing, flag these cases as reported
		try {
			XhibitDelegateHelper.getResults2Delegate().setLettersSentFlagOnCases(reportList);
		} catch (Exception e) {
			String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.NHA.alert.saveRecordsErrorTitle");
			String errorMessage = XHIBITConstant.getResource(XhibitBundles.ErrorText, "Reports.NHA.alert.saveRecordsErrorMessage");
			log.error(errorMessage, e);
			XMessageBox.alert(this, messageBoxTitle, true, XMessageBox.ICONERROR, errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
    }
}
