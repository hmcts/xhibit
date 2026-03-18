package uk.gov.courtservice.xhibit.client.results.pleas;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.TableModel;

import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelDecoratorInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Plea Filter Model
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class PleaFilterModel extends XHIBITDefaultTableModel implements Observer, XHIBITTableModelDecoratorInterface {
    private XHIBITTableModelInterface model;

    private PleaFilterSelectionModel pfs;

    private java.util.List rows;

    private String selectedIndictment;

    private String selectedCount;

    private String selectedDefendant;

    private int filter = 0;

    public PleaFilterModel(XHIBITTableModelInterface m, PleaFilterSelectionModel pfs) {
        model = m;
        this.pfs = pfs;

        pfs.addObserver(this);

        rows = new ArrayList();

        for (int i = 0; i < model.getRowCount(); i++) {
            rows.add(new Integer(i));
        }
    }

    public void update(Observable obs, Object obj) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("PleaFilterModel: update");
        filter = pfs.getFilter();
        selectedIndictment = pfs.getIndictmentFilter();
        selectedCount = pfs.getCountFilter();
        selectedDefendant = pfs.getDefendantFilter();
        filterPleas();
    }

    public void filterPleas() {
        String charge = null;
        String count;
        String defendant;

        rows = new ArrayList();

        for (int i = 0; i < model.getRowCount(); i++) {
            ResultsRowValue pleaTableRow = (ResultsRowValue) model.getDataAt(i);

            if (filter == PleaFilterSelectionModel.FILTER_INDICTMENT) {
                XHIBITConstant.debug("[PleaFilterModel] FILTER_INDICTMENT");

                charge = pleaTableRow.getChargeSequenceNumber().toString();
                XHIBITConstant.debug("PleaFilterModel: filterPleas: charge = [" + charge + "] selectedIndictment = ["
                        + selectedIndictment + "]");
                if (charge.equals(selectedIndictment)) {
                    rows.add(new Integer(i));
                    XHIBITConstant.debug("PleaFilterModel: filterPleas: i = [" + i + "]");
                }
            } else if (filter == PleaFilterSelectionModel.FILTER_INDICTMENT_COUNT) {
                XHIBITConstant.debug("[PleaFilterModel] FILTER_INDICTMENT_COUNT");

                charge = pleaTableRow.getChargeSequenceNumber().toString();
                if (charge.equals(selectedIndictment)) {
                    count = pleaTableRow.getOffenceSequenceNumber().toString();
                    if (count.equals(selectedCount)) {
                        rows.add(new Integer(i));
                    }
                }
            } else if (filter == PleaFilterSelectionModel.FILTER_INDICTMENT_COUNT_DEFENDANT) {
                XHIBITConstant.debug("[PleaFilterModel] FILTER_INDICTMENT_COUNT_DEFENDANT");

                if (pleaTableRow.getChargeType().equals(PleaHelper.INDICTMENT_CHARGE_TYPE)) {
                    charge = pleaTableRow.getChargeSequenceNumber().toString();
                } else if (pleaTableRow.getChargeType().equals(PleaHelper.SECTION41_CHARGE_TYPE)) {
                    charge = pleaTableRow.getChargeValue().getChargeID().toString();
                }

                if (charge.equals(selectedIndictment)) {
                    count = pleaTableRow.getOffenceSequenceNumber().toString();
                    if (count.equals(selectedCount)) {
                        defendant = ResultsHelper.getName(pleaTableRow.getDefendantValue());
                        if (defendant.equals(selectedDefendant)) {
                            rows.add(new Integer(i));
                        }
                    }
                }
            } else if (filter == PleaFilterSelectionModel.FILTER_COUNT) {
                XHIBITConstant.debug("[PleaFilterModel] FILTER_COUNT");

                if (pleaTableRow.getChargeType().equals(PleaHelper.INDICTMENT_CHARGE_TYPE)) {
                    charge = pleaTableRow.getChargeSequenceNumber().toString();
                } else if (pleaTableRow.getChargeType().equals(PleaHelper.SECTION41_CHARGE_TYPE)) {
                    charge = pleaTableRow.getChargeValue().getChargeID().toString();
                }

                if (charge.equals(selectedIndictment)) {
                    count = pleaTableRow.getOffenceSequenceNumber().toString();
                    if (count.equals(selectedCount)) {
                        rows.add(new Integer(i));
                    }
                }
            } else if (filter == PleaFilterSelectionModel.FILTER_INDICTMENT_DEFENDANT) {
                XHIBITConstant.debug("[PleaFilterModel] FILTER_INDICTMENT_DEFENDANT");

                charge = pleaTableRow.getChargeSequenceNumber().toString();
                if (charge.equals(selectedIndictment)) {
                    defendant = ResultsHelper.getName(pleaTableRow.getDefendantValue());
                    if (defendant.equals(selectedDefendant)) {
                        rows.add(new Integer(i));
                    }
                }
            }
            // else if (filter ==
            // PleaFilterSelectionModel.FILTER_COUNT_DEFENDANT)
            // {
            // defendant =
            // PleaHelper.getName(pleaTableRow.getDefendantValue());
            // if (defendant.equals(selectedDefendant))
            // {
            // rows.add(new Integer(i));
            // }
            // }
            else if (filter == PleaFilterSelectionModel.FILTER_DEFENDANT) {
                XHIBITConstant.debug("[PleaFilterModel] FILTER_DEFENDANT");

                defendant = ResultsHelper.getName(pleaTableRow.getDefendantValue());
                if (defendant.equals(selectedDefendant)) {
                    rows.add(new Integer(i));
                }
            } else {
                rows.add(new Integer(i));
            }
        }
        fireTableDataChanged();
    }

    public Object getDataAt(int r) {
        int row = ((Integer) rows.get(r)).intValue();
        return model.getDataAt(row);
    }

    /*
     * compute the moved row for the three methods that access model elements
     */
    public Object getValueAt(int r, int c) {
        // fix for buggy code in multi line table.
        if ((r < 0) || (r >= rows.size())) {
            return "";
        }

        int row;
        // return model.getValueAt(rows[r].index, c);
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("\ngetValueAt:
        // 1: row = " + r + " col = " + c);

        // XTable needs to reference first row of table before data exists
        // and if List "rows" is empty we get NullPointerException
        if (rows.size() == 0) {
            row = 0;
        } else {
            row = ((Integer) rows.get(r)).intValue();
        }
        return model.getValueAt(row, c);
    }

    public boolean isCellEditable(int r, int c) {
        int row = ((Integer) rows.get(r)).intValue();
        return model.isCellEditable(row, c);
    }

    public void setValueAt(Object aValue, int r, int c) {
        int row = ((Integer) rows.get(r)).intValue();
        model.setValueAt(aValue, row, c);
    }

    public int getRowCount() {
        // return model.getRowCount();
        return rows.size();
    }

    /* delegate all remaining methods to the model */
    public int getColumnCount() {
        return model.getColumnCount();
    }

    public String getColumnName(int c) {
        return model.getColumnName(c);
    }

    public Class getColumnClass(int c) {
        return model.getColumnClass(c);
    }

    /**
     * Return the model that is being filtered
     * 
     * @return
     */
    public TableModel getModel() {
        return model;
    }
}
