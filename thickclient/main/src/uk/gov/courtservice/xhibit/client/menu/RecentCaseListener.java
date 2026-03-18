package uk.gov.courtservice.xhibit.client.menu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import uk.gov.courtservice.xhibit.client.actions.menu.OpenRecentCase;
import uk.gov.courtservice.xhibit.client.models.RecentCase;
import uk.gov.courtservice.xhibit.client.util.XAction;

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
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class RecentCaseListener implements PropertyChangeListener {
    JMenu recentCases;

    public RecentCaseListener(JMenu recentCaseMenu) {
        recentCases = recentCaseMenu;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        RecentCase model = (RecentCase) evt.getNewValue();
        JMenuItem newCase = new JMenuItem();
        OpenRecentCase xa = new OpenRecentCase(model);
        newCase.setAction(xa);
        recentCases.add(newCase, 0);
        for (int i = 1; i < (recentCases.getItemCount()); i++) {
            RecentCase thisModel = (RecentCase) ((XAction) recentCases.getItem(i).getAction()).getModel();
            if (thisModel.getCaseNumber().equals(model.getCaseNumber())) {
                recentCases.remove(i);
            }
        }
        recentCases.setEnabled(true);
        if (recentCases.getItemCount() > 5) {
            recentCases.remove(5);
        }
    }
}