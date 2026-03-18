package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Apply Action to activate authorise
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author parthibanj
 * @version $Id: AuthoriseSyncAction.java,v 1.6 2006/06/05 12:32:15 bzjrnl Exp $
 */

public class AuthorisePreviewAction extends XAction {

	private static final long serialVersionUID = 1L;
	private AuthoriseResultsDialog authorisePanel;
	private Vector<AuthoriseResultsTableRowModel> defendantsModel;
	private JTable defendantsTable;
	private String caseType;

    public AuthorisePreviewAction(AuthoriseResultsDialog authorisePanel) {
        super("btnPreview");
        this.authorisePanel = authorisePanel;
        this.enabled = false;
    }

	@Override
	public void xActionPerformed(ActionEvent e) throws Exception {
		AuthoriseResultsTableRowModel row = defendantsModel.get(defendantsTable.getSelectedRow());
        Integer selectedDefendantOnCaseId = row.getAuthorisationValue().getDefendantOnCaseId();
       
        String generatePreviewTitle = ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "results.authorise.generatepreviewdialog.title");
		GenerateAuthorisePreviewDialog generateAuthorisePreviewDialog= new GenerateAuthorisePreviewDialog(authorisePanel, generatePreviewTitle, selectedDefendantOnCaseId, caseType);
		generateAuthorisePreviewDialog.setVisible(true);
	}

	public void setDefendantsTable(JTable defendantsTable) {
		this.defendantsTable = defendantsTable;
	}

	public void setDefendantsModel(Vector<AuthoriseResultsTableRowModel> defendantDetails) {
		this.defendantsModel = defendantDetails;
	}
	
	public void setCaseType(String caseType){
		this.caseType = caseType;
	}
}