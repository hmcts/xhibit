package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;

/**
 * <p>
 * Title: PrintWLLAction
 * </p>
 * <p>
 * Description: Print WLL Action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: PrintWLLAction.java,v 1.9 2006/06/05 12:31:00 bzjrnl Exp $
 */
public class PrintWLLAction extends XAction {
    public PrintWLLAction() {
        super("PrintWLL");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        throw new CSRecoverableException(new UnsupportedOperationException("Under Construction"));
    }
}
