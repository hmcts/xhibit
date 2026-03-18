package uk.gov.courtservice.xhibit.client.listdistribution;

// jdk

import java.util.Collection;

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: ManageListTableModel
 * </p>
 * <p>
 * Description: Data managed by the <code>ManageListTablePanel</code>
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
 * @version $Id: ManageListTableModel.java,v 1.27 2006/06/05 12:31:25 bzjrnl Exp $
 * @editor Sarah Tong
 */

public class ManageListTableModel extends XHIBITTableModel {
    protected Integer courtId;

    private String documentType;

    private String listType;

    public ManageListTableModel() {
        super();
    }

    // Getters
    public void getTableColumnNames() {
        if (getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            String[] col = new String[] { XHIBITConstant.getResource(XhibitBundles.ManageLists, "recipientNameCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "postalAddressCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "emailAddressCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "contactMethodCol") };
            this.setColumnNames(col);
        } else {
            String[] col = new String[] { XHIBITConstant.getResource(XhibitBundles.ManageLists, "recipientNameCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "emailAddressCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "faxNumberCol"),
                    XHIBITConstant.getResource(XhibitBundles.ManageLists, "contactMethodCol") };
            this.setColumnNames(col);
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
                WLLRecipientComplexValue rowv = (WLLRecipientComplexValue) getDataAt(row);
                if (col == 0) {
                    cell = rowv.getSolicitorFirmName();
                } else if (col == 1) {
                    cell = rowv.getSolicitorFirmAddress();
                } else if (col == 2) {
                    cell = rowv.getSolicitorFirmEmail();
                } else if (col == 3) {
                    cell = rowv.getSolicitorFirmFax();
                } else if (col == 4) {
                    cell = rowv.getDocumentDistribution().getDistributionType();
                }
            } else {
                RecipientComplexValue rowv = (RecipientComplexValue) getDataAt(row);
                if (col == 0) {
                    cell = rowv.getRecipientName();
                } else if (col == 1) {
                    cell = rowv.getEmailAddress();
                } else if (col == 2) {
                    cell = rowv.getFaxNumber();
                } else if (col == 3) {
                    // there will only be one record in this Collection as
                    // the mid tier
                    // returns only the distribution for this list
                    Collection docDists = rowv.getDocumentDistribution();
                    DocumentDistributionBasicValue docDist = (DocumentDistributionBasicValue) docDists.iterator()
                            .next();
                    cell = docDist.getDistributionType();
                }
            }
            XHIBITConstant.debug("XHIBITTableModel : cell[" + row + "," + col + "] = [" + cell.toString() + "].");
        } catch (Exception e) {
            cell = "";
            XHIBITConstant.debug("XHIBITTableModel : cell[" + row + "," + col + "] is null!");
        }
        return cell;
    }

    // Setters
    public void setCourtId(Integer cId) {
        courtId = cId;
    }

    // public void setDocumentDistributionId(Integer docDistId ) {
    // documentDistributionId = docDistId; }
    // public void setDistributionType(String distType ) { distributionType
    // = distType; }
    public void setListType(String lType) {
        listType = lType;
    }

    // public void setMimeType(String mType ) { mimeType = mType; }

    public void setDocumentType(String param) {
        if (param.equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "dailyList"))) {
            documentType = "DL";
        } else if (param.equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "runningList"))) {
            documentType = "RL";
        } else if (param.equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "criminalFirmList"))) {
            documentType = "FL";
        } else if (param.equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedList"))) {
            documentType = "WL";
        } else if (param.equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            documentType = "WLL";
        } else if (param.equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "dailyListPrison"))) {
            documentType = "DLP";
        }
    }

    /**
     * Add a new list recipient to the table
     * 
     * @param temp
     *            The recipient to add
     */
    public void setData(Collection data) {
        super.setData(data);
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Remove a recipient from the table
     * 
     * @param x
     *            The position of the recipient in the table rows
     */
    public void deleteData(Object x) {
        data.remove(x);
        fireTableChanged(new TableModelEvent(this));
    }
}