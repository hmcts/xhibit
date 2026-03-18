package uk.gov.courtservice.xhibit.business.vos.services.messaging;

import java.io.Serializable;

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
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version $Id: AdHocMessageValue.java,v 1.4 2006/06/05 12:29:13 bzjrnl Exp $
 */
public class AdHocMessageValue implements Serializable {
    private String message;

    private String senderLocation;

    private String receiverDeviceType;

    private String receiverNumber;

    private static final long serialVersionUID =5217786078274504139L;
    		
    public AdHocMessageValue(String message, String senderLocation, String receiverDeviceType, String receiverNumber) {
        this.message = message;
        this.senderLocation = senderLocation;
        this.receiverDeviceType = receiverDeviceType;
        this.receiverNumber = receiverNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiverDeviceType() {
        return receiverDeviceType;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public String getSenderLocation() {
        return senderLocation;
    }
}