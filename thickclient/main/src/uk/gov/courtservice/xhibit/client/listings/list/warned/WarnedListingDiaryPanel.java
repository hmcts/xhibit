package uk.gov.courtservice.xhibit.client.listings.list.warned;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DiaryNoteEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.FixtureDeftAttendingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.AbstractListingDiaryPanel;
import uk.gov.courtservice.xhibit.client.listings.list.common.DiaryTableRowModel;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingCaseContextPopupMenuHelper;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingUtils;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrAfterDateValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Panel for warned listing diary.
 * 
 * @author uphillj
 *
 */
public class WarnedListingDiaryPanel extends AbstractListingDiaryPanel<WarnedListingDiaryModel> {

	private static final long serialVersionUID = 1L;

	protected WarnedFixtureTableModel fixturesCasesModel;
	protected JTable fixturesCasesTable;
	protected WarnedDiaryTableModel diaryTableModel;

	private XDatePanel toDiaryDate;
	private boolean fromDiaryDateValid = true;
	private XDatePanel fromDiaryDate;
	
	private JPanel diaryDateListingPanel;

	public WarnedListingDiaryPanel(final WarnedListingDiaryModel listingDiaryModel) {
		super(listingDiaryModel);
	}

	@Override
	protected String getFixturesLabel() {
		return XHIBITConstant.getResource(XhibitBundles.Listings, "mainListingsFixturesWarned");
	}

	@Override
	protected void jbInit() {

		this.setBorder(BorderFactory.createTitledBorder("Listing Diary"));
		this.setPreferredSize(new Dimension(500, 300));

		final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0);

		gbc.weighty = 0.6;
		this.add(getFixturesListingPanel(), gbc);
		gbc.gridy++;

		// Note Diary Date Panel is in a different location for warned.
		gbc.weighty = 0.0;
		this.add(getDiaryDateListingPanel(), gbc);

		gbc.weighty = 0.4;
		gbc.gridy++;
		this.add(getDiaryListingPanel(), gbc);

	}

	@Override
	protected JPanel getDiaryDateListingPanel() {
		if (diaryDateListingPanel == null) {
			diaryDateListingPanel = new JPanel(new GridBagLayout());

			// Create empty label in centre to force remaining controls to the right
			diaryDateListingPanel.add(new JLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

			diaryDateListingPanel.add(new JLabel("From Diary Date:"), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

			final XDatePanel xDatePanelFrom = getFromDiaryDatePanel();
			diaryDateListingPanel.add(xDatePanelFrom, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

			diaryDateListingPanel.add(XHIBITConstant.getSpacer(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

			diaryDateListingPanel.add(new JLabel("To Diary Date:"), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

			final XDatePanel xDatePanelTo = getToDiaryDatePanel();
			diaryDateListingPanel.add(xDatePanelTo, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

		}
		return diaryDateListingPanel;
	}

	@Override
	protected void setPanelEnabled(boolean isEnabled) {
		getFromDiaryDatePanel().getDateComponent().setEnabled(isEnabled);
		getToDiaryDatePanel().getDateComponent().setEnabled(isEnabled);
	}
	
	private XDatePanel getFromDiaryDatePanel() {

		if (fromDiaryDate == null) {
			fromDiaryDate = new XDatePanelWithEvent(getDiaryDateListingPanel(), listingDiaryModel.getListDate(), true) {

				private static final long serialVersionUID = 1L;

				final DateInListValidator dateInListValidator = new DateInListValidator("From Diary Date");

				@Override
				protected void fireEvent() {
					if (ValidationUtils.hasDate(this)) {
						try {
							final Calendar cal = getDate();
							if (!cal.equals(listingDiaryModel.getListDate())) {
								// Update model first to prevent the same validation message
								// being displayed second time when the control loses focus
								listingDiaryModel.setListDate(cal);

								// Validate the date is within list dates
								final List<String> errors = new ArrayList<String>();
								dateInListValidator.validate(this, errors);

								// Validation failed.
								// We have an error to display.
								if (!errors.isEmpty()) {
									fromDiaryDateValid = false;
									XMessageBox.alert((JDialog)this.getRootPane().getParent(),
											XHIBITConstant.getResource(XhibitBundles.XhibitConstant,"exception.validation.title"),
											true, XMessageBox.ICONERROR, errors.get(0), XMessageBox.OK_ONLY,
											XMessageBox.DEFAULTOK);
								} else {
									fromDiaryDateValid = true;
									toDiaryDate.setDate(cal);
									listingDiaryModel.setListEndDate(cal);
									WarnedListingDiaryPanel.this.stepUpdateViewState();
								}
							}
						} catch (final CSValidationException e) {
							fromDiaryDateValid = false;
							XHIBITConstant.handleError(e);
						}
					} else {
						fromDiaryDateValid = false;
					}
				}
			};
		}
		return fromDiaryDate;
	}

	private XDatePanel getToDiaryDatePanel() {

		if (toDiaryDate == null) {
			toDiaryDate = new XDatePanelWithEvent(getDiaryDateListingPanel(), listingDiaryModel.getListEndDate(), true) {

				private static final long serialVersionUID = 1L;

				final DateEqualOrAfterDateValidator dateCompareWithFromValidator = new DateEqualOrAfterDateValidator("To Diary Date", "From Diary Date", getFromDiaryDatePanel());
			
				final DateInListValidator dateInListValidator = new DateInListValidator("To Diary Date");

				@Override
				protected void fireEvent() {
					if (ValidationUtils.hasDate(this)) {
						try {
							final Calendar cal = getDate();
							if (!cal.equals(listingDiaryModel.getListEndDate())) {
								// Update model first to prevent the same validation message
								// being displayed second time when the control loses focus
								listingDiaryModel.setListEndDate(cal);
								
								// Validate the date is after from date and within list dates
								final List<String> errors = new ArrayList<String>();
								dateCompareWithFromValidator.validate(this, errors);
								dateInListValidator.validate(this, errors);
								
								//Validation failed.
								//We have an error to display.
								if (!errors.isEmpty()) {
									XMessageBox.alert((JDialog)this.getRootPane().getParent(),
											XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"),
											true, XMessageBox.ICONERROR, errors.get(0), XMessageBox.OK_ONLY,
											XMessageBox.DEFAULTOK);
								} else if (fromDiaryDateValid) {
									WarnedListingDiaryPanel.this.stepUpdateViewState();
								}
							}
						} catch (final CSValidationException e) {
							XHIBITConstant.handleError(e);
						}
					}
				}
			};
		}
		return toDiaryDate;
	}
	
	private class DateInListValidator extends AbstractDateValidator {

		private String fieldName;
		
		public DateInListValidator(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public void validate(final XDatePanel target, final List<String> errors) {

			if (hasDate(target)) {
				final Calendar cal = getDate(target); 
			
				if (cal.after(listingDiaryModel.getListModel().getListEndDate())) {
					errors.add(fieldName + " is after the List End Date");
				} else if (cal.before(listingDiaryModel.getListModel().getListStartDate())) {
					errors.add(fieldName + " is before the List Start Date");
				}
			}
		}
	};

	/**
	 * Refresh fixtures displayed in table using a swing worker to retrieve all
	 * the fixtures in a background thread so all refreshes can run at same
	 * time.
	 */
	private void refreshFixturesTableModelData() {
		// Clear the current table model data as reloading everything
		fixturesCasesModel.setData((Object[]) null);

		SwingWorker worker = new SwingWorker<Void, WarnedFixtureTableRow>() {
			@Override
			protected Void doInBackground() throws Exception {

				final Map<Integer, String> courtSiteIdSiteCode = getCourtSiteIdSiteCodeMap();

				// Initialise our calendar
				final Calendar cal = listingDiaryModel.getListModel().getListStartDate();

				// Loop around whilst our cal date is not after the list end
				// date
				while (!cal.after(listingDiaryModel.getListModel().getListEndDate())) {

					// Get a collection of fixtures for the day / date.
					@SuppressWarnings("unchecked")
					final Collection<CaseDiaryFixtureComplexValue> caseDiaryFixtureComplexValues = XhibitDelegateHelper
							.getListingsDelegate().findCaseDiaryFixturesByDateAndCourtId(cal.getTime(),
									XhibitSingleton.getInstance().getCourtId());

					processFixturesForDay(courtSiteIdSiteCode, caseDiaryFixtureComplexValues);
					// Add a day.
					cal.add(Calendar.DAY_OF_YEAR, 1);
				}

				return null;
			}

			private void processFixturesForDay(final Map<Integer, String> courtSiteIdSiteCode,
					Collection<CaseDiaryFixtureComplexValue> caseDiaryFixtureComplexValues) {

				// Set some detail for each fixture.
				for (final CaseDiaryFixtureComplexValue item : caseDiaryFixtureComplexValues) {

					// Get the list of defendant on case ids, which are the ones
					// attending on the fixture
					final List<Integer> defendantOnCaseIds = new ArrayList<Integer>();
					for (FixtureDeftAttendingBasicValue fixtureDeftAttendingValue : item.getFixtureDeftAttending()) {
						if ("Y".equals(fixtureDeftAttendingValue.getAttending())) {
							defendantOnCaseIds.add(fixtureDeftAttendingValue.getDefendantOnCaseId());
						}
					}

					// Create table row for fixture
					final WarnedFixtureTableRow warnedRow = new WarnedFixtureTableRow();

					ListingUtils.populateListCaseTableRow(item.getCase(), item.getRefHearingType(),
							item.getDirectionsForCase(), defendantOnCaseIds, item.getListNotePreDefinedId(),
							item.getListNoteText(), warnedRow);

					final String courtCode = courtSiteIdSiteCode.get(item.getCourtSiteId());
					warnedRow.setCourtSiteCode(courtCode);
					warnedRow.setListingDate(item.getListingDate());
					warnedRow.setCaseDiaryFixtureId(item.getCaseDiaryFixtureId());

					// We have some data ready for the UI.
					// Call publish and this will call process.
					publish(warnedRow);

				}
			}

			@Override
			protected void process(List<WarnedFixtureTableRow> chunks) {
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
							XHIBITConstant.handleError((Exception) e.getCause());
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}

				// Ensure when refreshed for the first time, the panel
				// is now flagged as being ready for user interaction
				initialised = true;
			}

			/**
			 * Util method to get a map of CourtSideIds, CourtSiteCode
			 * 
			 * @return Map courtSiteId, courtSiteCode
			 */
			private Map<Integer, String> getCourtSiteIdSiteCodeMap() {
				final CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();
				final XhbCourtSiteBasicValue[] sites = courtStructure.getCourtSites();
				final Map<Integer, String> courtSiteIdSiteCode = new HashMap<Integer, String>();

				// Add all the court Site Id and Codes to a map.
				for (final XhbCourtSiteBasicValue site : sites) {
					courtSiteIdSiteCode.put(site.getCourtSiteId(), site.getCourtSiteCode());
				}
				return courtSiteIdSiteCode;
			}

		};
		worker.execute();
	}

	@Override
	protected JTable getFixturesCasesTable() {
		fixturesCasesModel = new WarnedFixtureTableModel();
		fixturesCasesTable = XTableFactory.getInstance().createDefaultTable(fixturesCasesModel);
		TableUtils.setupDefaultsOnJTable(fixturesCasesTable);
		TableUtils.setupColumnHeaderToolTips(fixturesCasesTable, fixturesCasesModel.getColumnHeaderToolTips());
		TableUtils.setupColumnHorizontalAlignment(fixturesCasesTable, WarnedFixtureTableModel.COL_SITE_CODE, SwingConstants.CENTER);
		TableUtils.setupColumnHorizontalAlignment(fixturesCasesTable, WarnedFixtureTableModel.COL_GROUP_NO, SwingConstants.LEFT);
		fixturesCasesModel.setColumnWidths(fixturesCasesTable);

		setDateRendererOnFixturesCasesTable();

		// We don't set a transfer handler since warned list does not support
		// drag and drop.

		setTableRowFilter(fixturesCasesTable);

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

		// Set background colour as disabled.
		fixturesCasesTable.setBackground(UIManager.getColor("control"));
		return fixturesCasesTable;
	}

	protected void refreshFixturesCaseTable() {
		refreshFixturesTableModelData();
	}
	
	private void setDateRendererOnFixturesCasesTable() {
		final TableColumnModel columModel = fixturesCasesTable.getColumnModel();
		final TableColumn tableComumn = columModel.getColumn(WarnedFixtureTableModel.COL_FIX_DATE);
		tableComumn.setCellRenderer(new DateTableCellRender());
	}

	// We have to create a different row filter for Warned.
	private void setTableRowFilter(final JTable table) {

		final RowFilter<? super WarnedFixtureTableModel, ? super Integer> filter = new WarnedListCaseTableModelRowFilter();

		final TableRowSorter<WarnedFixtureTableModel> sorter = new TableRowSorter<WarnedFixtureTableModel>(
				(WarnedFixtureTableModel) table.getModel());

		sorter.setSortKeys(
				Arrays.asList(new RowSorter.SortKey(WarnedFixtureTableModel.COL_GROUP_NO, SortOrder.ASCENDING),
						new RowSorter.SortKey(WarnedFixtureTableModel.COL_CASE_NUMBER, SortOrder.ASCENDING)));

		sorter.setRowFilter(filter);
		table.setRowSorter(sorter);
	}

	private class DateTableCellRender extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

		@Override
		protected void setValue(final Object value) {
			if (value instanceof Date) {
				setText((value == null) ? "" : sdf.format(value));
			} else {
				// Any other type than the date we are expecting use the default
				// format.
				super.setValue(value);
			}
		}
	}
	
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
						.findGeneralDiaryNotesByCourtIdAndDates(XhibitSingleton.getInstance().getCourtId(),
								listingDiaryModel.getListDate().getTime(),listingDiaryModel.getListEndDate().getTime());
				
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
		diaryTableModel = new WarnedDiaryTableModel();
		diaryTable = XTableFactory.getInstance().createDefaultTable(diaryTableModel);
		TableUtils.setupDefaultsOnJTable(diaryTable);
		setDateRendererOnDiaryTable();
		
		diaryTable.setAutoCreateRowSorter(true);
		diaryTable.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(WarnedDiaryTableModel.COL_DATE, SortOrder.ASCENDING)));
		
		refreshDiaryTableModelData();

		diaryTable.getColumnModel().getColumn(WarnedDiaryTableModel.COL_DATE).setMinWidth(80);
		diaryTable.getColumnModel().getColumn(WarnedDiaryTableModel.COL_DATE).setMaxWidth(80);
		diaryTable.getColumnModel().getColumn(WarnedDiaryTableModel.COL_NOTE).setPreferredWidth(400);
		diaryTable.getColumnModel().getColumn(WarnedDiaryTableModel.COL_USER).setPreferredWidth(80);
		diaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		return diaryTable;
	}
	
	private void setDateRendererOnDiaryTable() {
		final TableColumnModel columModel = diaryTable.getColumnModel();
		final TableColumn tableComumn = columModel.getColumn(WarnedDiaryTableModel.COL_DATE);
		tableComumn.setCellRenderer(new DateTableCellRender());
	}

}
