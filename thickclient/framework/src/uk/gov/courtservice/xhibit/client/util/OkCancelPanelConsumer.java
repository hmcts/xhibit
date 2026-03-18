package uk.gov.courtservice.xhibit.client.util;

import java.awt.event.ActionEvent;

/**
 * <p>
 * Title: Xhibit
 * </p>
 * <p>
 * Description: Xhibit 2 Client Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS UK Solutions Consulting
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public interface OkCancelPanelConsumer {
    public abstract void okClicked(ActionEvent e) throws Exception;

    public abstract void cancelClicked(ActionEvent e) throws Exception;
}