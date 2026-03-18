package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteria;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchCriteriaPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class SolicitorFirmSearchCriteriaPanel extends XHIBITSearchCriteriaPanel {

	private static final long serialVersionUID = -1950660625606010515L;
	private SolicitorFirmSearch myParentSearchControl;

	/**
	 * Criteria fields.
	 */
	private JTextField[] fields;
	private JButton addSolicitorFirmBtn;
	private JButton searchButton;
	private JLabel orLbl;

	public SolicitorFirmSearchCriteriaPanel(XHIBITSearchCriteria xsCriteria, XHIBITSearch xsSearch) {
		super(xsCriteria, xsSearch);
		this.myParentSearchControl = (SolicitorFirmSearch) xsSearch;
	}

	@Override
	public void jbInit(XHIBITSearchCriteria xsCriteria) {
		// iterate on the criteria and add them to panel
		stepTitle = new JLabel(xsCriteria.getStepTitle());
		stepDescription = new JLabel(xsCriteria.getStepDescription());
		addSolicitorFirmBtn = new JButton("");
		addSolicitorFirmBtn.setAction(new TheAddAction());

		orLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitSearch, "xs.gen.or"));

		searchButton = new JButton();
		searchButton.setAction(new SearchAction());
		searchButton.setMnemonic(((XAction) searchButton.getAction()).getMnemonicKey().intValue());
		searchButton.setEnabled(false);

		Enumeration keysEnum = xsCriteria.getTheCriteriaKeys().elements();

		int noOfCriteria = xsCriteria.getTheCriteria().size();
		if (this.xsSearch.isInternalDebug())
			log.debug("found " + noOfCriteria + " criteria keys.");

		int criteriaCounter = 0;

		setLayout(new GridBagLayout());
		setName(XHIBITConstant.getResource(XhibitBundles.XhibitSearch, "xs.gen.name"));

		gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		stepTitle.setFont(stepTitle.getFont().deriveFont(java.awt.Font.BOLD));
		doAdd(stepTitle, gbc);

		gbc = new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(stepDescription, gbc);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		int noFields = xsCriteria.getTheCriteriaKeys().size();
		fields = new JTextField[noFields];

		while (keysEnum.hasMoreElements()) {
			// get the key (always a Criteria Attribute Name
			String key = (String) keysEnum.nextElement();
			criteriaCounter++;
			final int i = criteriaCounter - 1;
			if (this.xsSearch.isInternalDebug())
				log.debug("Processing criteria no " + criteriaCounter + " named '" + key + "'.");

			final JLabel jLabel = new JLabel((String) xsCriteria.getTheCriteriaLabels().get(key) + ":");
			fields[i] = (JTextField) xsCriteria.getTheCriteriaComponents().get(key);
			fields[i].addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					validateSearchCriteria();
				}
			});
			fields[i].getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					validateSearchCriteria();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					validateSearchCriteria();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					validateSearchCriteria();
				}
			});

			if (!(fields[i] == null)) {
				// put the label
				gbc.fill = GridBagConstraints.NONE;
				gbc.gridy = gbc.gridy + 1;
				gbc.gridx = 0;
				gbc.weightx = 0.0;
				doAdd(jLabel, gbc);

				// put the component
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridx = 1;
				gbc.weightx = 1.0;
				doAdd(fields[i], gbc);
			}
		}

		gbc = new GridBagConstraints(1, gbc.gridy + 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(addSolicitorFirmBtn, gbc);

		gbc = new GridBagConstraints(2, gbc.gridy, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(orLbl, gbc);

		gbc = new GridBagConstraints(3, gbc.gridy, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(searchButton, gbc);

		gbc = new GridBagConstraints(0, gbc.gridy + 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(XHIBITConstant.getSpacer(), gbc);

		this.xsSearch.setSearchScreenName("Criteria");
	}

	/**
	 * Validates the search criteria field/s and enables/disables Search button
	 * accordingly.
	 */
	private void validateSearchCriteria() {
		boolean enable = false;
		for (JTextField textField : fields) {
			String text = textField.getText();

			if (text != null) {
				if (!(text.trim().equals(""))) {
					enable = true;
					break;
				}
			}
		}
		searchButton.setEnabled(enable);
	}

	/**
	 * SearchAction class.
	 * 
	 * @author grewalg
	 *
	 */
	private class SearchAction extends XAction {

		private static final long serialVersionUID = -1300293392863408332L;

		public SearchAction() {
			populateFromBundle("btnSearch");
		}

		public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
			try {
				myParentSearchControl.doSearch();
			} catch (Exception e) {
				CSRecoverableException csre = new CSRecoverableException("gui.user.search.genericFailedMessage",
						"myParentSeachControl.doSearch() returned exception to XHIBITSearchCriteriaPanel's SearcAction.",
						e);
				throw csre;
			}
		}
	}

	private class TheAddAction extends XAction {
		private static final long serialVersionUID = 1L;

		public TheAddAction() {
			populateFromBundle("SolicitorFirmAdd");
		}

		public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
			// Open new Dialog with bigger panel for the update
			myParentSearchControl.showAddSolicitorFirmPanel(new RefSolicitorFirmComplexValue());
		}
	}
}
