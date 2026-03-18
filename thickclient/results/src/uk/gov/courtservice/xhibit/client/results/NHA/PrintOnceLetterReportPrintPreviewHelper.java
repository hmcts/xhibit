package uk.gov.courtservice.xhibit.client.results.NHA;

import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.awt.viewer.PreviewDialog;

import uk.gov.courtservice.xhibit.client.print.helper.PrintPreviewHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.WindowBoundsHelper;
import uk.gov.courtservice.xhibit.common.results.vos.ISingleRunLetterReport;

public class PrintOnceLetterReportPrintPreviewHelper extends PrintPreviewHelper {

	private ISingleRunLetterReport report = null;
	
	public PrintOnceLetterReportPrintPreviewHelper(ISingleRunLetterReport nhaReport){
		this.report = nhaReport;
	}
	
	/**
     * Creates an instance of the preview dialog to control the printing process
     * The rendering is for AWT - this may be extended to PDF for furure
     * releases of Xhibit
     * 
     * @param renderer
     *            The AWTRenderer used to render the FO
     * @param res
     *            org.apache.fop.viewer.Translator (SecureResourceBundle)
     * @return An instance of XhibitPreviewDialog
     */
	@Override
    protected PreviewDialog createPreviewDialog(AWTRenderer renderer) {
		PrintOnceLetterReportPreviewDialog frame = new PrintOnceLetterReportPreviewDialog(getFOUserAgent(), report);
        frame.validate();

        // Set the window bounds to the last used value or center in default
        // display.
        // Store any changes to the window bounds on close
        WindowBoundsHelper.setBounds(frame, WindowBoundsHelper.PRINT_PREVIEW_FRAME_NAME, WindowBoundsHelper
                .getDefaultBounds(frame), true);

        frame.setVisible(true);
        return frame;
    }
}
