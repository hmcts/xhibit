package uk.gov.courtservice.xhibit.web.messaging.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.messaging.IMSessionInfo;
import uk.gov.courtservice.xhibit.web.messaging.bean.DisplayLineNode;
import uk.gov.courtservice.xhibit.web.messaging.bean.FlattenedTree;

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
 * @version 1.0
 */

public class SendInstantMessageAction extends AbstractAction {
    protected void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {

        FlattenedTree tree = ((IMSessionInfo) actionEnvironment
                .getSessionParameter(IMSessionInfo.INSTANT_MESSAGE_SESSION)).getDestinationTree();

        if (tree == null) {
            throw new FrameworkException("im.jms.nodestinations",
                    "Unable to build tree of instant message destinations.");
        }
        DisplayLineNode[] lines = tree.getLines();
        // sorting has been removed from this class, now performed in
        // InstantMessageServices where it is customised by court
        actionEnvironment.setRequestParameter("lines", lines);

        actionEnvironment.setResponseName("sendinstantmessagecomplete");
    }
}
