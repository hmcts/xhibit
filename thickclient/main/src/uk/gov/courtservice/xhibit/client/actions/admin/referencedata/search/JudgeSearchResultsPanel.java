package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchResults;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchResultsPanel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;

/**
 * Panel class to contain Reference Data specific functionality by subclassing
 * standard class.
 * 
 * @author grewalg
 *
 */
public class JudgeSearchResultsPanel extends XHIBITSearchResultsPanel {

	private JudgeSearch myParentSearchController;

	private JButton theDetailsButton;
	private TheDetailsAction theDetailsAction = null;

	public JudgeSearchResultsPanel(XHIBITSearchResults xsSearchResults, JudgeSearch xsSearch) {
		super(xsSearchResults, xsSearch);
		this.myParentSearchController = xsSearch;
	}

	private static final long serialVersionUID = -1396148516789091741L;

	@Override
	protected JPanel getButtonPanel() {
		JPanel resultButtonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbcButtonPanel = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		resultButtonPanel.add(XHIBITConstant.getSpacer(), gbcButtonPanel);

        gbcButtonPanel = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        resultButtonPanel.add(getTheBackButton(), gbcButtonPanel);
        
        gbcButtonPanel = new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
		getDetailsAction().setEnabled(false); // by default false
		theDetailsButton = new JButton();

		theDetailsButton.setAction(new TheDetailsAction());
		theDetailsButton.setAction(getDetailsAction());
		theDetailsButton.setMnemonic(((XAction) theDetailsButton.getAction()).getMnemonicKey().intValue());
		resultButtonPanel.add(this.theDetailsButton, gbcButtonPanel);

		return resultButtonPanel;
	}

	@Override
	public void setNumberOfResultsSelected(int i) {
		boolean detailsEnabled = false;

		if (i > 0) {
			detailsEnabled = true;
		}
		getDetailsAction().setEnabled(detailsEnabled);
		theDetailsButton.setEnabled(detailsEnabled);
	}

	private Action getDetailsAction() {
		if (this.theDetailsAction == null) {
			this.theDetailsAction = new TheDetailsAction();
		}
		return this.theDetailsAction;
	}

	private class TheDetailsAction extends XAction {
		private static final long serialVersionUID = 1L;

		public TheDetailsAction() {
			populateFromBundle("btnDetails");
		}

		public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
			CSValueObject valueObject = (CSValueObject) (((XSortableTableModel) theResultsTable.getModel())
					.getDataAt(theResultsTable.getSelectedRow()));
			// Open new Dialog with bigger panel for the update
			myParentSearchController.showXSDetailsPanel(valueObject);
		}
	}
}
