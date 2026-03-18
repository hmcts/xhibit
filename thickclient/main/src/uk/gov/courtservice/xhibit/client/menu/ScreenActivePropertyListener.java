package uk.gov.courtservice.xhibit.client.menu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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

public class ScreenActivePropertyListener implements PropertyChangeListener {
    private ArrayList components = new ArrayList();

    public ScreenActivePropertyListener() {
    }

    public void addComponent(AbstractButton comp) {
        components.add(comp);
    }

    public void removeComponent(AbstractButton comp) {
        components.remove(comp);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Iterator iter = components.iterator();
        while (iter.hasNext()) {
            boolean newState = ((Boolean) evt.getNewValue()).booleanValue();
            AbstractButton item = (AbstractButton) iter.next();
            item.setSelected(newState);
            XAction a = (XAction) item.getAction();
            if (newState) {
                XhibitSingleton.getInstance().setUserDeactivedScreen(false);
                a.setIcon(XHIBITConstant.imageRoot + "tactivatepublicdisplay.gif");
                a.setName(XHIBITConstant
                        .getResource(XhibitBundles.XhibitActionResources, "ActivatePublicDisplayActive"));
            } else {
                XhibitSingleton.getInstance().setUserDeactivedScreen(true);
                a.setIcon(XHIBITConstant.imageRoot + "tdeactivatepublicdisplay.gif");
                a.setName(XHIBITConstant.getResource(XhibitBundles.XhibitActionResources,
                        "ActivatePublicDisplayInactive"));
            }
        }
    }
}