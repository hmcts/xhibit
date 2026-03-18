package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.client.actions.AbstractPreviewList;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class PreviewRecordSheetAction extends AbstractPreviewList {

	private static final long serialVersionUID = 1L;
	private Integer defendantOnCaseId;
	private String caseType;
	private XDialog parent;

	public PreviewRecordSheetAction(Integer defendantOnCaseId, String caseType, XDialog parent) {
		super("PreviewList");
		this.defendantOnCaseId = defendantOnCaseId;
		this.caseType = caseType;
		this.parent = parent;
	}

	@Override
	protected String getTransformationStylesheetURL() {
		String transformationStylesheetURL = null;
		if("A".equals(caseType)){
			transformationStylesheetURL = "config/xsl/results/authorise/AppealRecordSheet-v2-9.xsl";
		}else if ("S".equals(caseType)){
			transformationStylesheetURL = "config/xsl/results/authorise/CommittalRecordSheet-v2-13.xsl";
		}else if ("T".equals(caseType)){
			transformationStylesheetURL = "config/xsl/results/authorise/TrialRecordSheet-v2-12.xsl";
		}		
		return transformationStylesheetURL;
	}

	@Override
	public String getXml() throws Exception {
		return XhibitDelegateHelper.getResults2Delegate().getGeneratedPreviewXML(defendantOnCaseId);
	}

	@Override
    /**
     * Preview the rec sheet
     * 
     * @param e
     *            the event that caused this method to be called
     * @throws Exception
     *             if an error occurs
     */
    public void xActionPerformed(ActionEvent e) throws Exception {       
		String formattedXml = getFormatedXml();
		
		XMLServicesImpl xmlService = XMLServicesImpl.getInstance();
		Document doc = xmlService.createDocFromString(formattedXml);
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression xpathExpression = xpath.compile("//*[@class='HearingBlock' and text()]");
		
		NodeList hearingBlocks = (NodeList) xpathExpression.evaluate(doc, XPathConstants.NODESET);
		if(hearingBlocks != null && hearingBlocks.getLength() > 0){
			if(JOptionPane.showConfirmDialog(parent, "Edit Hearings and Important Dates?", "Edit Hearings?", JOptionPane.YES_NO_OPTION) == 0) {
				//pass node list to GUI
				EditHearingsXmlDialog dialog = new EditHearingsXmlDialog(parent, hearingBlocks);
				dialog.setVisible(true);	
				formattedXml = xmlService.getStringXML(doc);
			}
		}
		// Preview the formated list xml
        FOPFactory.getFOPRenderer(true).printDocument(formattedXml, true, "Record Sheet - Print Preview");
    }
}
