package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: DeleteDocumentAction
 * </p>
 * <p>
 * Description: Delete the selected documents
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: DeleteDocumentAction.java,v 1.10 2006/06/05 12:31:00 bzjrnl Exp $
 */
public class DeleteDocumentAction extends XAction {
    public DeleteDocumentAction() {
        super("DeleteDocument");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        throw new CSRecoverableException(new UnsupportedOperationException("Under Construction"));
    }
}
