package uk.gov.courtservice.xhibit.client.listdistribution;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;

/**
 * <p>
 * Title: DistributeListLettersTableModel
 * </p>
 * <p>
 * Description: This table model has common functionality for maintaining
 * recipients.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: Will Fardell, Xdevelopment (2004)
 * </p>
 * 
 * @version $Id: AbstractMaintainListLetterRecipientsTableModel.java,v 1.2
 *          2005/02/22 09:25:32 bzjrnl Exp $
 */
public abstract class AbstractMaintainListLetterRecipientsTableModel extends AbstractListDistributionTableModel {

    // Recipient Types
    private static Map RECIPIENT_TYPE_MAP = new HashMap(2);
    static {
        RECIPIENT_TYPE_MAP.put("S", getResource("maintainlistletterrecipientstablemodel.recipienttype.S"));
        RECIPIENT_TYPE_MAP.put("O", getResource("maintainlistletterrecipientstablemodel.recipienttype.O"));
    }

    // Mime Types
    private static Map MIME_TYPE_MAP = new HashMap(2);
    static {
        MIME_TYPE_MAP.put("PDF", getResource("maintainlistletterrecipientstablemodel.mimetype.PDF"));
        MIME_TYPE_MAP.put("HTM", getResource("maintainlistletterrecipientstablemodel.mimetype.HTM"));
    }

    // Mime Types
    private static Map DISTRIBUTION_TYPE_MAP = new HashMap(3);
    static {
        DISTRIBUTION_TYPE_MAP.put("EMAIL", getResource("maintainlistletterrecipientstablemodel.deliverytype.EMAIL"));
        DISTRIBUTION_TYPE_MAP.put("POST", getResource("maintainlistletterrecipientstablemodel.deliverytype.POST"));
        DISTRIBUTION_TYPE_MAP.put("FAX", getResource("maintainlistletterrecipientstablemodel.deliverytype.FAX"));
    }

    /**
     * Contruct a new model with the specified data
     */
    public AbstractMaintainListLetterRecipientsTableModel(WllRecipientComplexValue[] data, String[] columnNames,
            Class[] columnClasses) {
        super(data, columnNames, columnClasses);
    }

    // Accessors

    protected String getName(int rowIndex) {
        return getWllRecipientComplexValue(rowIndex).getName();
    }

    protected String getAddress(int rowIndex) {
        return getWllRecipientComplexValue(rowIndex).getAddress();
    }

    protected String getEmail(int rowIndex) {
        return getWllRecipientComplexValue(rowIndex).getEmail();
    }

    protected String getFax(int rowIndex) {
        return getWllRecipientComplexValue(rowIndex).getFax();
    }

    protected String getRecipientType(int rowIndex) {
        return (String) RECIPIENT_TYPE_MAP.get(getWllRecipientComplexValue(rowIndex).getRecipientType());
    }

    protected String getMimeType(int rowIndex) {
        return (String) MIME_TYPE_MAP.get(getWllRecipientComplexValue(rowIndex).getMimeType());
    }

    protected String getDistributionType(int rowIndex) {
        return (String) DISTRIBUTION_TYPE_MAP.get(getWllRecipientComplexValue(rowIndex).getDistributionType());
    }

    /**
     * Get the value at the specified row.
     */
    public WllRecipientComplexValue getWllRecipientComplexValue(int rowIndex) {
        return (WllRecipientComplexValue) getDataAt(rowIndex);
    }

}
