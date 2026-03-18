package uk.gov.courtservice.xhibit.client.xhibitapplication;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.SavePropertyListener;
import uk.gov.courtservice.xhibit.client.util.SelectAllPropertyListener;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XToolBarHelper;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SelectAllFunction;

/**
 * <p>
 * Title: XAC Container Listener
 * </p>
 * <p>
 * Description: This listener is added to every Xhibit Application Controller.
 * When a new XPanel is added to the body area this listener picks up the event
 * and performs various actions, eg setting the title, displaying toolbars and
 * enabling actions.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class XACContainerListener implements ContainerListener {
    private XhibitApplicationController xac;

    public XACContainerListener(XhibitApplicationController myController) {
        xac = myController;
    }

    public void componentAdded(ContainerEvent e) {
        XPanel thisPanel;
        XToolBarHelper toolBarHelper = this.xac.getToolBarHelper();
        if (e.getChild() instanceof XPanel) {
            thisPanel = (XPanel) e.getChild();

            // Title
            XhibitHelper.setTitle(xac, thisPanel);

            // Toolbar stuff
            if (thisPanel instanceof CourtLogController) {
                String hearingType = xac.getApplicationCaseModel().getScheduledHearingValue().getHearingTypeDesc();
                toolBarHelper.setHearingType(hearingType);
            }

            if (thisPanel instanceof TodaysScheduleController) {
                toolBarHelper.setHearingType(null);
            }

            if (thisPanel instanceof SaveFunction) {
                XhibitActions.getAction(xac, XhibitActions.Save).setEnabled(false);
                thisPanel.addPropertyChangeListener(XPanel.property_modified, new SavePropertyListener(xac));
            } else {
                XhibitActions.getAction(xac, XhibitActions.Save).setEnabled(false);
            }
            if (thisPanel instanceof SelectAllFunction) {
                XhibitActions.getAction(xac, XhibitActions.EditSelectAll).setEnabled(false);
                thisPanel.addPropertyChangeListener(XPanel.property_selectAll, new SelectAllPropertyListener(xac));
            } else {
                XhibitActions.getAction(xac, XhibitActions.EditSelectAll).setEnabled(false);
            }
            boolean canPrint = (thisPanel instanceof PrintFunction);
            XhibitActions.getAction(xac, XhibitActions.Print).setEnabled(canPrint);
            XhibitActions.getAction(xac, XhibitActions.PrintToolbar).setEnabled(canPrint);
            XhibitActions.getAction(xac, XhibitActions.PrintPreview).setEnabled(canPrint);

        }
        toolBarHelper.refreshToolBarOnLoad(e.getChild().getClass());
    }

    public void componentRemoved(ContainerEvent e) {
        this.xac.getToolBarHelper().refreshToolBarOnUnload(e.getChild().getClass());
    }
}