package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * Base class for Listing Diary Panel.
 * Sets up the basic components but does not implement a display.
 * @author westalll
 *
 */
public abstract class AbstractListingDiaryPanel<T extends ListingDiaryModel> extends JPanel {
	

	private static final long serialVersionUID = -2720312143862896136L;
	protected T listingDiaryModel;
	protected JTable diaryTable;
    
    protected boolean initialised;

	public AbstractListingDiaryPanel(final T listingDiaryModel) {
		super(new GridBagLayout());
		this.listingDiaryModel = listingDiaryModel;

		jbInit();
	}
	
	public boolean isInitialised() {
		return initialised;
	}
	
    protected abstract void setPanelEnabled(boolean isEnabled);

    protected abstract void refreshFixturesCaseTable();

	/**
	 * Method to create the panel to return.
	 * Put your code here.
	 */
	protected abstract void jbInit();

	protected abstract JPanel getDiaryDateListingPanel();


	protected void stepUpdateViewState() {
		refreshDiaryTableModelData();
	}
	
	final protected JPanel getFixturesListingPanel() {
		final JPanel fixturesForTodayListingPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.CENTER, XHIBITConstant.nonContainerInsets, 0, 0);

		// Set the title
		gbc.gridy = 0;
		fixturesForTodayListingPanel.add(new JLabel(getFixturesLabel()), gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		JScrollPane scrollPane = new JScrollPane(getFixturesCasesTable());
		fixturesForTodayListingPanel.add(scrollPane, gbc);
		return fixturesForTodayListingPanel;
	}
	
	/**
	 * Warned has a different label from Daily and Firm for the Fixtures table.
	 * @return The label for fixtures.
	 */
	protected abstract String getFixturesLabel();
	
	// This may well be common. I didn't want to tie all implementations to the
	// same data model.
	protected abstract JTable getFixturesCasesTable();
	
	final protected String getDateString(final Date date) {
		final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}
	
	final protected JPanel getDiaryListingPanel() {
		final JPanel diaryListingPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.CENTER, XHIBITConstant.nonContainerInsets, 0, 0);

		// Set the title
		diaryListingPanel.add(new JLabel("Diary"), gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy++;
		gbc.weighty = 1.0;
		JScrollPane scrollPane = new JScrollPane(getDiaryTable());
		diaryListingPanel.add(scrollPane, gbc);
		return diaryListingPanel;
	}
	
	protected abstract JTable getDiaryTable();
	
	/**
	 * Refresh dairy notes displayed in table.
	 */
	 protected abstract void refreshDiaryTableModelData();

}
