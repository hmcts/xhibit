package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.FontHelper;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public abstract class AbstractDailyFirmDiaryPanel extends AbstractListingDiaryPanel<ListingDiaryModel> {


	private static final long serialVersionUID = 1357115184390573385L;
	private static final String YES = "Y";
	protected Integer nonFixedListingPanelPos;
	protected GridBagConstraints nonFixedListingPanelGbc;
	protected ListBasicValue listFromFirmOrWarned;
	protected JPanel nonFixedListingPanel;
	
	protected ListCaseTableModel nonFixedCasesModel;
	protected JTable nonFixedCasesTable;
	protected ListCaseTableModel fixturesCasesModel;
	protected JTable fixturesCasesTable;
	protected DiaryTableModel diaryTableModel;

	private JPanel diaryDateListingPanel;
	private XDatePanel diaryDatePanel;

	public AbstractDailyFirmDiaryPanel(ListingDiaryModel listingDiaryModel) {
		super(listingDiaryModel);
	}
	
	@Override
	protected void stepUpdateViewState() {
		super.stepUpdateViewState();
		refreshNonFixedListingPanel();
		refreshFixturesTableModelData();
	}
	
	@Override
	protected void jbInit() {

		this.setBorder(BorderFactory.createTitledBorder("Listing Diary"));
		this.setPreferredSize(new Dimension(300, 300));

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0);

		gbc.weighty = 0.0;
		this.add(getDiaryDateListingPanel(), gbc);

		gbc.gridy++;
		gbc.weighty = 0.35;
		this.add(getFixturesListingPanel(), gbc);

		this.nonFixedListingPanel = getNonFixedListingPanel();
		this.nonFixedListingPanelPos = this.getComponentCount();

		gbc.gridy++;
		gbc.weighty = getNonFixedListingPanelWeightY();
		this.add(nonFixedListingPanel, gbc);
		this.nonFixedListingPanelGbc = (GridBagConstraints) gbc.clone();

		gbc.gridy++;
		gbc.weighty = 0.3;
		this.add(getDiaryListingPanel(), gbc);
	}
	
	@Override
	protected  JPanel getDiaryDateListingPanel() {
		if (diaryDateListingPanel == null) {
			diaryDateListingPanel = new JPanel(new GridBagLayout());

			// Create empty label in centre to force remaining controls to the right
			diaryDateListingPanel.add(new JLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

			diaryDateListingPanel.add(new JLabel("Diary Date:"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
			
			final XDatePanel xDatePanel = getDiaryDatePanel();
			diaryDateListingPanel.add(xDatePanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

		}
		return diaryDateListingPanel;
	}
	
	@Override
	protected void setPanelEnabled(boolean isEnabled) {
		getDiaryDatePanel().getDateComponent().setEnabled(isEnabled);
	}

	private XDatePanel getDiaryDatePanel() {

		if (diaryDatePanel == null) {
			diaryDatePanel = new XDatePanelWithEvent(diaryDateListingPanel, listingDiaryModel.getListDate(), true) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void fireEvent() {
					if (ValidationUtils.hasDate(this)) {
						try {
							final Calendar cal = getDate();
							if (!cal.equals(listingDiaryModel.getListDate())) {
								listingDiaryModel.setListDate(cal);
								AbstractDailyFirmDiaryPanel.this.stepUpdateViewState();
							}
						} catch (final CSValidationException e) {
							XHIBITConstant.handleError(e);
						}
					}
				}
			};
		}
		return diaryDatePanel;
	}
	
	
	@Override
	protected String getFixturesLabel() {
		return XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingsFixturesForToday");  
	}
	
	/**
	 * Refresh fixtures displayed in table using a swing worker to retrieve all
	 * the fixtures in a background thread so all refreshes can run at same time.
	 */
	private void refreshFixturesTableModelData() {
		// Clear the current table model data as reloading everything
		fixturesCasesModel.setData((Object[])null);
		
		SwingWorker worker = new SwingWorker<Void, ListCaseTableRow>() {
			@Override
			protected Void doInBackground() throws Exception {
				@SuppressWarnings("unchecked")
				final Collection<CaseDiaryFixtureComplexValue> caseListings = XhibitDelegateHelper.getListingsDelegate()
						.findCaseDiaryFixturesByDateAndCourtId(listingDiaryModel.getListDate().getTime(),
								XhibitSingleton.getInstance().getCourtId());

				// Must deal with the scenario when the dialog is being opened and the list model
				// is also being populated asynchronously, so wait for that to complete
				while (!listingDiaryModel.getListModel().isListPopulated()) {
					Thread.sleep(100);
				}

				for (final CaseDiaryFixtureComplexValue item : caseListings) {
					// get the list of defendant on case ids, which are the ones attending on the fixture
					List<Integer> defendantOnCaseIds = new ArrayList<Integer>();
					for (FixtureDeftAttendingBasicValue fixtureDeftAttendingValue : item.getFixtureDeftAttending()) {
						if ("Y".equals(fixtureDeftAttendingValue.getAttending())) {
							defendantOnCaseIds.add(fixtureDeftAttendingValue.getDefendantOnCaseId());
						}
					}

					// Create table row for case.
					final ListCaseTableRow row = new GeneralFixtureTableRow();
					
					ListingUtils.populateListCaseTableRow(
							item.getCase(), item.getRefHearingType(), item.getDirectionsForCase(), defendantOnCaseIds,
							item.getListNotePreDefinedId(), item.getListNoteText(), row);
					row.setCaseDiaryFixtureId(item.getCaseDiaryFixtureId());

					// Check the list model to see if the fixture is already listed
					final boolean listed = listingDiaryModel.getListModel().isFixtureListed(
							item.getCase().getCaseId(), item.getCaseDiaryFixtureId());
					if (listed) {
						row.setListedOnRight(true);
					}
					
					//We have data ready for the UI.
					publish(row);
				}

				return null;
			}
			
			@Override
			 protected void process(List<ListCaseTableRow> chunks) {
				fixturesCasesModel.addCases(chunks);	
		    }

			@Override
			protected void done() {
				// If the background task completed, call the get method to
				// check if an exception occurred, which if it did will be
				// re-thrown wrapped in an ExecutionException exception.
				if (!isCancelled()) {
					try {
						get();
					} catch (ExecutionException e) {
						Throwable cause = e.getCause();
						if (cause instanceof Exception) {
							XHIBITConstant.handleError((Exception)e.getCause());
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}

				// Ensure when refreshed for the first time, the panel
				// is now flagged as being ready for user interaction
		    	initialised = true;
			}
		};
		worker.execute();
	}
	
	@Override
	protected JTable getFixturesCasesTable() {
		fixturesCasesModel = new ListCaseTableModel();
		fixturesCasesTable = XTableFactory.getInstance().createDefaultTable(fixturesCasesModel);
		TableUtils.setupDefaultsOnJTable(fixturesCasesTable);
		
		final TransferHandler transferHandler = new ExcludeListedCaseTransferHandler(fixturesCasesModel,
				listingDiaryModel.getListModel().getTreeNodeFactory());
		TableUtils.setupDragAndDropOnJTable(fixturesCasesTable, transferHandler);
		TableUtils.setupColumnHeaderToolTips(fixturesCasesTable, fixturesCasesModel.getColumnHeaderToolTips());
		TableUtils.setupColumnHorizontalAlignment(fixturesCasesTable, ListCaseTableModel.COL_GROUP_NO, SwingConstants.LEFT);
		TableUtils.setTableRowFilter(fixturesCasesTable, new ExcludeListedCaseTableModelRowFilter());
		fixturesCasesModel.setColumnWidths(fixturesCasesTable);

		final ListingCaseContextPopupMenuHelper helper = new ListingCaseContextPopupMenuHelper(
				listingDiaryModel.getListModel(), fixturesCasesTable, fixturesCasesModel);
		helper.addCaseListingEntryPopupMenu(new XAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void xActionPerformed(ActionEvent e) throws Exception {
				refreshFixturesTableModelData();
			}
		});
		helper.addCaseSummaryPopupMenu();
		refreshFixturesTableModelData();

		return fixturesCasesTable;
	}
	
	
	protected void refreshFixturesCaseTable() {
		refreshFixturesTableModelData();
		if (nonFixedCasesModel != null && listFromFirmOrWarned != null) {
			refreshNonFixedCasesTableModelData();
		}
	}
	
	private void refreshNonFixedListingPanel() {
		this.remove(nonFixedListingPanel);
		this.nonFixedListingPanel = getNonFixedListingPanel();
		this.nonFixedListingPanelGbc.weighty = getNonFixedListingPanelWeightY();
		this.add(this.nonFixedListingPanel, nonFixedListingPanelGbc, nonFixedListingPanelPos);
		this.nonFixedListingPanel.revalidate();
	}
	
	private JPanel getNonFixedListingPanel() {
		final JPanel nonFixedListingPanel = new JPanel(new GridBagLayout());
		
		this.listFromFirmOrWarned = getNonFixedList();

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.CENTER, XHIBITConstant.nonContainerInsets, 0, 0);

		final ListTypeEnum listTypeEnum = listFromFirmOrWarned == null ? null : ListingDropdownPopulation.getListType(listFromFirmOrWarned.getListTypeId());
		
		// If there is no firm or warned list available for the diary date, only display a label
		if (listTypeEnum == null) {
			gbc.fill = GridBagConstraints.CENTER;
			final JLabel label = new JLabel(getNoListTypeMessage());
			label.setFont(FontHelper.getItalicFontFromFont(label.getFont()));
			nonFixedListingPanel.add(label, gbc);
		}
		// Else there is a firm or warned list that covers the diary date, so display cases from it
		else {
			// Get the start and end dates of the list being displayed
			final String startDateString = getDateString(listFromFirmOrWarned.getListStartDate());
			final String endDateString = getDateString(listFromFirmOrWarned.getListEndDate());
			
			// Get the labels detailing the list and add them and its cases to panel
			if (listTypeEnum.equals(ListTypeEnum.Firm)) {
				final String title1 = "Non-fixed Firm List Cases - from Reserve";
				final String title2 = "From Firmed List: " + startDateString + " to " + endDateString;
				addPanelFirmedOrWarned(title1, title2, nonFixedListingPanel, gbc);
			} else if (listTypeEnum.equals(ListTypeEnum.Warned)) {
				final String title1 = "Non-fixed Warned Cases";
				final String title2 = "From Warned List: " + startDateString + " to " + endDateString;
				addPanelFirmedOrWarned(title1, title2, nonFixedListingPanel, gbc);
			}
		}

		return nonFixedListingPanel;
	}
	
	private double getNonFixedListingPanelWeightY() {
		double weighty = 0.0;
		if (this.listFromFirmOrWarned != null) {
			weighty = 0.35;
		}
		return weighty;
	}
	
	/**
	 * Daily and Firm Lists display a different message when there is
	 * no List to return.
	 * @return The message to display when there is no list.
	 */
	protected abstract String getNoListTypeMessage();
	
	
	private void addPanelFirmedOrWarned(final String title1, final String title2, final JPanel nonFixedListingPanel,
			final GridBagConstraints gbc) {

		// set the title
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.NONE;

		nonFixedListingPanel.add(new JLabel(title1), gbc);
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.NORTH;

		JLabel label = new JLabel(title2);
		label.setFont(FontHelper.getItalicFontFromFont(label.getFont()));

		nonFixedListingPanel.add(label, gbc);

		gbc.gridy++;
		gbc.weighty = 1.0;

		addNonFixedListingPanel(gbc, nonFixedListingPanel);
	}
	
	private void addNonFixedListingPanel(GridBagConstraints gbc, final JPanel panel) {
		gbc.fill = GridBagConstraints.BOTH;
		JScrollPane scrollPane = new JScrollPane(getNonFixedCasesTable());
		panel.add(scrollPane, gbc);
	}

	private JTable getNonFixedCasesTable() {
		nonFixedCasesModel = new ListCaseTableModel();
		nonFixedCasesTable = XTableFactory.getInstance().createDefaultTable(nonFixedCasesModel);
		TableUtils.setupDefaultsOnJTable(nonFixedCasesTable);

		final TransferHandler transferHandler = new ExcludeListedCaseTransferHandler(nonFixedCasesModel,
				listingDiaryModel.getListModel().getTreeNodeFactory());
		TableUtils.setupDragAndDropOnJTable(nonFixedCasesTable, transferHandler);
		TableUtils.setupColumnHeaderToolTips(nonFixedCasesTable, nonFixedCasesModel.getColumnHeaderToolTips());
		TableUtils.setupColumnHorizontalAlignment(nonFixedCasesTable, ListCaseTableModel.COL_GROUP_NO, SwingConstants.LEFT);
		TableUtils.setTableRowFilter(nonFixedCasesTable, new ExcludeListedCaseTableModelRowFilter());
		nonFixedCasesModel.setColumnWidths(nonFixedCasesTable);

		final ListingCaseContextPopupMenuHelper helper = new ListingCaseContextPopupMenuHelper(
				listingDiaryModel.getListModel(), nonFixedCasesTable, nonFixedCasesModel);
		helper.addCaseListingEntryPopupMenu();
		helper.addCaseSummaryPopupMenu();
		
		refreshNonFixedCasesTableModelData();
		
		return nonFixedCasesTable;
	}
	
	/**
	 * Refresh non fixed cases displayed in table using a swing worker to retrieve all
	 * the non fixed cases in a background thread so all refreshes can run at same time.
	 */
	private void refreshNonFixedCasesTableModelData() {
		// Clear the current table model data as reloading everything
		nonFixedCasesModel.setData((Object[])null);
		
		SwingWorker worker = new SwingWorker<Void, ListCaseTableRow>() {
			@SuppressWarnings("unchecked")
			@Override
			protected Void doInBackground() throws Exception {
				// Get the list of cases to display in the non fixed table,
				// which for a Firm list
				// is only cases that are marked as reserved, but for a Warned
				// list is all of them
				final ListTypeEnum listTypeEnum = ListingDropdownPopulation
						.getListType(listFromFirmOrWarned.getListTypeId());
				final String reserved = listTypeEnum.equals(ListTypeEnum.Firm) ? YES : null;
				Collection<CaseOnListComplexValue> caseOnListComplexValues = XhibitDelegateHelper.getListingsDelegate()
							.getNonFixtureCasesOnList(listFromFirmOrWarned.getListId(), reserved);

				// Must deal with the scenario when the dialog is being opened
				// and the list model
				// is also being populated asynchronously, so wait for that to
				// complete
				while (!listingDiaryModel.getListModel().isListPopulated()) {
					Thread.sleep(100);
				}

				// Add all non fixed cases to the list of rows to add to the non
				// fixed table
				for (final CaseOnListComplexValue item : caseOnListComplexValues) {

					// Get the list of defendant on case ids, which are the
					// ones attending on the case on list
					final List<Integer> defendantOnCaseIds = new ArrayList<Integer>();
					final Collection<DefOnCaseOnListBasicValue> defendants = item.getDefOnCaseOnLists();
					for (final DefOnCaseOnListBasicValue defendant : defendants) {
						defendantOnCaseIds.add(defendant.getDefendantOnCaseId());
					}

					// Create table row for case and mark it as being
					// created from a non fixed case
					final ListCaseTableRow row = new NonFixedCaseTableRow();

					ListingUtils.populateListCaseTableRow(item.getCase(), item.getHearingType(),
							item.getDirectionsForCase(), defendantOnCaseIds, item.getListNotePredefinedId(),
							item.getListNoteText(), row);
					row.setParentCaseOnListId(item.getCaseOnListId());

					// Check the list model to see if the non fixed case is
					// already listed
					final boolean listed = listingDiaryModel.getListModel().isNonFixedListed(item.getCase().getCaseId(),
							item.getCaseOnListId());
					if (listed) {
						row.setListedOnRight(true);
					}

					//We have data ready for the UI.
					publish(row);
				}

				return null;
			}
			
			@Override
			 protected void process(List<ListCaseTableRow> chunks) {
				nonFixedCasesModel.addCases(chunks);
		    }

			@Override
			protected void done() {
				// If the background task completed, call the get method to
				// check if an exception occurred, which if it did will be
				// re-thrown wrapped in an ExecutionException exception.
				if (!isCancelled()) {
					try {
						get();
					} catch (ExecutionException e) {
						Throwable cause = e.getCause();
						if (cause instanceof Exception) {
							XHIBITConstant.handleError((Exception)e.getCause());
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		worker.execute();
	}
	
	/**
	 * Returns a ListBasicValue containing a list ID if a 
	 * suitable one exists.
	 * 
	 * @return ListBasicValue list data.
	 */
	protected abstract ListBasicValue getNonFixedList();

	/**
	 * Try to get a warned list for diary date which is null if none exits.
	 * 
	 * @return
	 */
	protected ListBasicValue getWarnedList() {
		final ListBasicValue warnedList = XhibitDelegateHelper.getListingsDelegate()
				.findWarnedListByDateAndCourtId(XhibitSingleton.getInstance().getCourtId(),
						listingDiaryModel.getListDate().getTime());
		return warnedList; 
	}
	
	/**
	 * Try to get a firm list for diary date which is null if none exists.
	 * 
	 * @return
	 */
	protected ListBasicValue getFirmList() {
		final ListBasicValue firmList = XhibitDelegateHelper.getListingsDelegate()
				.findFirmListByDateAndCourtId(XhibitSingleton.getInstance().getCourtId(),
						listingDiaryModel.getListDate().getTime());
		return firmList; 
	}
	
	/**
	 * Listener which receives case removal events from main listing panel
	 * so that cases that were previously dragged from the fixture or the
	 * firm/warned list tables can be un-hidden when they are no longer
	 * listed in the tree/table outline control.
	 */
	protected class CaseRemovedListener implements CaseRemovalListener {

		@Override
		public void actionPerformed(final Integer caseId, final Integer caseDiaryFixtureId, final Integer parentCaseOnListId) {
			if (fixturesCasesTable != null) {
				checkDataItemsOnTable(fixturesCasesTable, caseId, caseDiaryFixtureId, parentCaseOnListId);
			}
			if (nonFixedCasesTable != null) {
				checkDataItemsOnTable(nonFixedCasesTable, caseId, caseDiaryFixtureId, parentCaseOnListId);
			}
		}

		private void checkDataItemsOnTable(final JTable table, final Integer caseId, final Integer caseDiaryFixtureId,
				final Integer parentCaseOnListId) {
			final ListCaseTableModel model = (ListCaseTableModel) table.getModel();
			final Collection<Object> dataItems = model.getDataAsCollection();
			updateItemListed(caseId, caseDiaryFixtureId, parentCaseOnListId, dataItems);
			model.fireTableDataChanged();
		}

		private void updateItemListed(final Integer caseId, final Integer caseDiaryFixtureId, final Integer parentCaseOnListId,
				final Collection<Object> dataItems) {

			// Cycle through all data items to see if case deleted from list
			// was dragged from one of them which now needs to be un-hidden
			for (final Object dataItem : dataItems) {
				final ListCaseTableRow row = (ListCaseTableRow) dataItem;
				
				if (row.getCaseId().equals(caseId)) {
					// If case removed from list came from a fixture and the row is
					// that fixture, then mark that row as no longer being listed
					if (caseDiaryFixtureId != null && row.getCaseDiaryFixtureId() != null
							&& caseDiaryFixtureId.equals(row.getCaseDiaryFixtureId())) {
						row.setListedOnRight(false);
					}
					// Else if case removed from list came from a firm/warned list and the row
					// is for that list entry, then mark that row as no longer being listed
					else if (parentCaseOnListId != null && row.getParentCaseOnListId() != null
							&& parentCaseOnListId.equals(row.getParentCaseOnListId())) {
						row.setListedOnRight(false);
					}
				}
			}
		}

	};
	
	/**
	 * Refresh dairy notes displayed in table using a swing worker to retrieve all
	 * the notes in a background thread so all refreshes can run at same time.
	 */
	@Override
	final protected void refreshDiaryTableModelData() {
		// Clear the current table model data as reloading everything
		diaryTableModel.setData((Object[])null);
		
		SwingWorker worker = new SwingWorker<Void, DiaryTableRowModel>() {
			@Override
			protected Void doInBackground() throws Exception {
				@SuppressWarnings("unchecked")
				final Collection<DiaryNoteEntryComplexValue> notes = XhibitDelegateHelper.getListingsDelegate()
						.findGeneralDiaryNotesByCourtIdAndDate(XhibitSingleton.getInstance().getCourtId(),
								listingDiaryModel.getListDate().getTime());
				
				// Add all the notes to a list to add to table
				for (final DiaryNoteEntryComplexValue note : notes) {
					final DiaryTableRowModel data = new DiaryTableRowModel(note.getDiaryNoteEntryId(),
							note.getDisplayDiaryText(), note.getLastUpdatedBy(), note.getDiaryDate());
					//We have data ready for the UI.
					publish(data);
				}

				return null;
			}
			
			@Override
			protected void process(List<DiaryTableRowModel> chunks) {
				diaryTableModel.addDiaryNotes(chunks);
		    }

			@Override
			protected void done() {
				// If the background task completed, call the get method to
				// check if an exception occurred, which if it did will be
				// re-thrown wrapped in an ExecutionException exception.
				if (!isCancelled()) {
					try {
						get();
					} catch (ExecutionException e) {
						Throwable cause = e.getCause();
						if (cause instanceof Exception) {
							XHIBITConstant.handleError((Exception)e.getCause());
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		worker.execute();
	}

	@Override
	final protected JTable getDiaryTable() {
		diaryTableModel = new DiaryTableModel();
		diaryTable = XTableFactory.getInstance().createDefaultTable(diaryTableModel);
		TableUtils.setupDefaultsOnJTable(diaryTable);
		
		refreshDiaryTableModelData();
		
		diaryTable.getColumnModel().getColumn(DiaryTableModel.COL_NOTE).setPreferredWidth(400);
		diaryTable.getColumnModel().getColumn(DiaryTableModel.COL_USER).setPreferredWidth(80);
		diaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		return diaryTable;
	}
}
