package uk.gov.courtservice.xhibit.client.listdistribution;

// jdk

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: SelectRecipientTableModel
 * </p>
 * <p>
 * Description: Data managed by the <code>SelectRecipientTablePanel</code>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: G S Rajasekaran
 * </p>
 * 
 * @version $Id: SelectRecipientTableModel.java,v 1.20 2004/03/30 14:18:07
 *          qzd3k3 Exp $
 * @editor Sarah Tong
 */
public class SelectRecipientTableModel extends XHIBITTableModel {
    protected Integer courtId;

    private String documentType;

    private String listType;

    protected String panelTitle;

    protected RecipientBasicValue basicRecipient;

    /**
     * Calls constructor on super class
     */
    public SelectRecipientTableModel() {
        super();
    }

    // Getters
    public void getTableColumnNames() {
        if (getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.ManageLists, "recipientNameCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "postalAddressCol") });
        } else {
            String[] cols = new String[] { XHIBITConstant.getResource(XhibitBundles.ManageLists, "recipientNameCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "emailAddressCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberCol") };
            setColumnNames(cols);
        }
    }

    public Integer getCourtId() {
        return courtId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getListType() {
        return listType;
    }

    public Object getValueAt(int row, int col) {
        Object cell = "";
        try {
            if (this.getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
                WLLRecipientBasicValue rowv = (WLLRecipientBasicValue) data.elementAt(row);
                if (col == 0) {
                    cell = rowv.getSolicitorFirmName();
                } else if (col == 1) {
                    cell = rowv.getSolicitorFirmAddress();
                }
            } else {
                RecipientBasicValue rowv = (RecipientBasicValue) data.elementAt(row);
                if (col == 0) {
                    cell = rowv.getRecipientName();
                } else if (col == 1) {
                    cell = rowv.getEmailAddress();
                } else if (col == 2) {
                    cell = rowv.getFaxNumber();
                }
            }
            XHIBITConstant.debug("XHIBITTableModel : cell[" + row + "," + col + "] = [" + cell.toString() + "].");
        } catch (Exception e) {
            cell = new String("");
            XHIBITConstant.debug("XHIBITTableModel : cell[" + row + "," + col + "] is null!");
        }
        return cell;
    }

    // Setters
    public void setCourtId(Integer cId) {
        courtId = cId;
    }

    public void setDocumentType(String docType) {
        documentType = docType;
    }

    public void setListType(String lType) {
        listType = lType;
    }

    /**
     * Adds a list recipient to the table
     * 
     * @param temp
     *            The recipient to add
     */
    public void addData(RecipientBasicValue temp) {
        data.addElement(temp);
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Adds a Warned List Letter recipient to the table
     * 
     * @param temp
     *            The recipient to add
     */
    public void addData(WLLRecipientBasicValue temp) {
        data.addElement(temp);
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Remove a recipient from the table
     * 
     * @param x
     *            The position of the recipient in the table rows. # This must
     *            be the position in the data and not necessarily the row
     *            selected as the table may be sorted.
     */
    public void deleteData(Object x) {
        data.remove(x);
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Finds the first occurrance of the original element, then updates it with
     * the new elemnet.
     * 
     * @param oldElement
     *            the original recipient
     * @param newElement
     *            the modified recipient
     */
    public void updateElement(Object oldElement, Object newElement) {
        int pos = data.indexOf(oldElement);
        data.setElementAt(newElement, pos);
        fireTableChanged(new TableModelEvent(this, pos));
    }
}
