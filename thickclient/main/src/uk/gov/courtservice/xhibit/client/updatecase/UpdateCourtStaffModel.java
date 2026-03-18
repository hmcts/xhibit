package uk.gov.courtservice.xhibit.client.updatecase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.AttendeeHistoryValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva, Frederik Vandendriessche
 * @version 1.0
 */
public class UpdateCourtStaffModel {
    private final Logger log = CSServices.getLogger(UpdateCourtStaffModel.class);

    private final Hashtable personTypeHash = new Hashtable();

    private final Collection courtClerkListData = new Vector();

    private final Collection usherListData = new Vector();

    protected final Collection addedCourtClerks = new Vector();

    protected final Collection removedCourtClerks = new Vector();

    protected final Collection addedUshers = new Vector();

    protected final Collection removedUshers = new Vector();

    private XCourtStaffHistoryTableModel xCourtStaffHistoryTableModel;

    private XCourtClerkUsherListModel xCourtClerkListModel;

    private XCourtClerkUsherListModel xUsherListModel;

    private Calendar schedHearingCal;

    private long midnight;

    private long endofday;

    public UpdateCourtStaffModel(Collection staffValues, Collection attendeeHistoryValues, Date scheduleHearingStartTime) {
        xCourtStaffHistoryTableModel = new XCourtStaffHistoryTableModel();

        List list = createCourtStaffHistoryRows(attendeeHistoryValues);

        Sorter.sort(list, new String[] { "startDate", "schedHearingAttendeeId" }, new Boolean(false));

        xCourtStaffHistoryTableModel.setData(list);

        final Enumeration prekeys = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainHearingHeader).getKeys();
        while (prekeys.hasMoreElements()) {
            final String key = (String) prekeys.nextElement();
            if (key.startsWith("persontype_")) {
                String value = XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, key);
                this.personTypeHash.put(key.substring(11, key.length()), value);
            }
        }

        synchronized (staffValues) {
            final Iterator it = staffValues.iterator();
            while (it.hasNext()) {
                final PersonValue pv = (PersonValue) it.next();
                if (pv.getPersonType().equals(PersonValue.COURT_CLERK)) {
                    courtClerkListData.add(pv);
                    XHIBITConstant.debug("CourtStaffModel added court clerk : " + pv.getFullName());
                } else if (pv.getPersonType().equals(PersonValue.USHER)) {
                    usherListData.add(pv);
                    XHIBITConstant.debug("CourtStaffModel added usher : " + pv.getFullName());
                }
            }
        }
    }

    public XCourtStaffHistoryTableModel getCourtStaffHistoryTableModel() {
        return xCourtStaffHistoryTableModel;
    }

    public XCourtClerkUsherListModel getCourtClerkModel() {
        if (xCourtClerkListModel == null) {
            xCourtClerkListModel = new XCourtClerkUsherListModel();
            synchronized (courtClerkListData) {
                Iterator it = courtClerkListData.iterator();
                while (it.hasNext()) {
                    xCourtClerkListModel.addElement(it.next());
                }
            }
        }
        return xCourtClerkListModel;
    }

    public XCourtClerkUsherListModel getUsherModel() {
        if (xUsherListModel == null) {
            xUsherListModel = new XCourtClerkUsherListModel();
            synchronized (usherListData) {
                Iterator it = usherListData.iterator();
                while (it.hasNext()) {
                    xUsherListModel.addElement(it.next());
                }
            }
        }
        return xUsherListModel;
    }

    public class XCourtClerkUsherListModel extends DefaultListModel {
        public Object getElementAt(int row) {
            PersonValue pv = (PersonValue) get(row);
            return pv.getFullName() == null ? "" : pv.getFullName();
        }
    }

    public class XCourtStaffHistoryTableModel extends XHIBITTableModel {
        public XCourtStaffHistoryTableModel() {
            this.setColumnNames(new String[] {
                    XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "columnNameCourtStaffName"),
                    XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "columnNameCourtStaffType"),
                    XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "columnNameCourtStaffStart") });
        }

        public Object getValueAt(int rowIdx, int colIdx) {
            Object cell = null;
            try {
                CourtStaffHistoryRow row = (CourtStaffHistoryRow) getData().get(rowIdx);

                switch (colIdx) {
                case 0:
                    cell = row.person.getFullName();
                    break;
                case 1:
                    // the court staff / person type code needs resolving
                    cell = personTypeHash.get(row.person.getPersonType());
                    break;
                case 2:
                    cell = XDateFormat.format(row.startDate, XDateFormat.DATEFORMAT);
                    break;
                }
            } catch (final Exception e) {
                XHIBITConstant.debug("XHIBITTableModel : cell[" + rowIdx + "," + colIdx + "] is null!");
            }

            return ((cell != null) ? cell : "");
        }

        public void addRow(CourtStaffHistoryRow obj) {
            getData().add(obj);
            fireTableRowsInserted(getData().size() - 1, getData().size());
        }

        public void removeRow(int idx) {
            getData().remove(idx);
            fireTableRowsDeleted(idx, idx);
        }
    }

    public void addCourtClerk(String name) {
        final PersonValue pv = new PersonValue();
        pv.setPersonType(PersonValue.COURT_CLERK);
        pv.setFullName(name);
        addCourtClerk(pv);
    }

    public void addUsher(String name) {
        PersonValue pv = new PersonValue();
        pv.setPersonType(PersonValue.USHER);
        pv.setFullName(name);
        addUsher(pv);
    }

    public void addCourtClerk(PersonValue pv) {
        this.addedCourtClerks.add(pv);
        addStaffToHistory(pv);
        this.xCourtClerkListModel.addElement(pv);
    }

    public void removeCourtClerk(PersonValue pv) {
        if (!addedCourtClerks.remove(pv))
            removedCourtClerks.add(pv);
        removeStaffFromHistory(pv);
        this.xCourtClerkListModel.removeElement(pv);
    }

    public void addUsher(PersonValue pv) {
        this.addedUshers.add(pv);
        addStaffToHistory(pv);
        this.xUsherListModel.addElement(pv);
    }

    public void removeUsher(PersonValue pv) {
        if (!addedUshers.remove(pv))
            removedUshers.add(pv);
        this.xUsherListModel.removeElement(pv);
        removeStaffFromHistory(pv);
    }

    private void addStaffToHistory(PersonValue pv) {
        // The schdedule hearing date is entered here.
        // This is always set to the current system date.
        // The current system date updates the SH date when opening a case.
        // The introduction of Java 1.5 highlighted an issue where Timestamp and
        // Date
        // can't be compared using Timestamp.compareTo(Date d). The timestamp
        // comes frm the database, but
        // we then tried to compare it to a new instance of Date. Changing this
        // to two instances of Timestamp resolves the problem.
        // Neil Entwistle 16th May 2006
        xCourtStaffHistoryTableModel.addRow(new CourtStaffHistoryRow(pv, new Timestamp(Calendar.getInstance()
                .getTimeInMillis())));

        List list = xCourtStaffHistoryTableModel.getData();
        Sorter.sort(list, new String[] { "startDate", "schedHearingAttendeeId" }, new Boolean(false));
        xCourtStaffHistoryTableModel.setData(list);
    }

    private boolean removeStaffFromHistory(PersonValue pv) {
        final Iterator it = xCourtStaffHistoryTableModel.getData().iterator();
        long rowDate;

        for (int i = 0; it.hasNext(); i++) {
            final CourtStaffHistoryRow row = (CourtStaffHistoryRow) it.next();

            if ((row.person == pv)
                    || (((row.person.getParentId() != null) && (pv.getParentId() != null) && (row.person.getParentId()
                            .equals(pv.getParentId()))))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param attendeeHistoryValues
     * @return
     */
    private List createCourtStaffHistoryRows(Collection attendeeHistoryValues) {
        final List allRows = new ArrayList();

        try {
            final Iterator it = attendeeHistoryValues.iterator();
            while (it.hasNext()) {
                final AttendeeHistoryValue ahv = (AttendeeHistoryValue) it.next();
                final Date startTime = ahv.getScheduledHearingBasicValue().getStartTime();
                final Iterator personIt = ahv.getPersonValueList().iterator();
                while (personIt.hasNext()) {
                    final PersonValue pv = (PersonValue) personIt.next();
                    if (showInHistoryTable(pv)) {
                        allRows.add(new CourtStaffHistoryRow(pv, startTime));
                    }
                }
            } // end iterating the getAttendeeHistoryValues() Collection.
        } catch (final Exception e) {
            log.error(e);
        }

        return allRows;
    }

    /**
     * Private helper method used to determine whether the passed in person
     * should be displayed in the court staff history table, currently only
     * court clerks, ushers and court reporters will be displayed (return
     * <i>true</i> from this method.
     * 
     * @param pv
     *            A <code>PersonValue</code>
     * @return <code>boolean</code> of <i>true</i> if the person should be
     *         displayed in the history table
     */
    private boolean showInHistoryTable(PersonValue pv) {
        boolean show = false;
        if ((pv != null) && (pv.getPersonType() != null)) {
            if (pv.getPersonType().equals(PersonValue.COURT_CLERK))
                show = true;
            else if (pv.getPersonType().equals(PersonValue.USHER))
                show = true;
            else if (pv.getPersonType().equals(PersonValue.COURT_REPORTER))
                show = true;
        }
        return show;
    }
}