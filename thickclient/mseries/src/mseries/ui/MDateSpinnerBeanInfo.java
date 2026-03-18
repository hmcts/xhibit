/*
 *   Copyright (c) 2001 Martin Newstead (seth_brundell@bigfoot.com).  All Rights Reserved.
 *
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 *
 *   The author requests that he be notified of any application, applet, or other binary that
 *   makes use of this code and that some acknowedgement is given. Comments, questions and
 *   requests for change will be welcomed.
 */
package mseries.ui;

import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import mseries.Calendar.MDateTimeValueEditor;

/**
 * Bean Info class for MDateSpinner. Not all attributes are editable in the
 * beanbox but most are.
 */
public class MDateSpinnerBeanInfo extends SimpleBeanInfo {
    Class mdsClass = MSpinner.class;

    public EventSetDescriptor[] getEventSetDescriptors() {
        EventSetDescriptor changeEvent = null;
        EventSetDescriptor fieldEvent = null;
        try {
            changeEvent = new EventSetDescriptor(mdsClass, "MMonthEvent", mseries.Calendar.MMonthListener.class,
                    "dataChanged");

            String listenerMethods[] = { "fieldEntered", "fieldExited" };
            fieldEvent = new EventSetDescriptor(mdsClass, "MFieldEvent", mseries.Calendar.MFieldListener.class,
                    listenerMethods, "addMFieldListener", "removeMFieldListener");

        } catch (IntrospectionException e) {
        }
        EventSetDescriptor[] events = { changeEvent, fieldEvent };
        return events;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor value = new PropertyDescriptor("value", mdsClass), foreground = new PropertyDescriptor(
                    "foreground", mdsClass), background = new PropertyDescriptor("background", mdsClass), db = new PropertyDescriptor(
                    "doubleBuffered", mdsClass), opaque = new PropertyDescriptor("opaque", mdsClass), autoscrolls = new PropertyDescriptor(
                    "autoscrolls", mdsClass), alx = new PropertyDescriptor("alignmentX", mdsClass), aly = new PropertyDescriptor(
                    "alignmentY", mdsClass), dgo = new PropertyDescriptor("debugGraphicsOptions", mdsClass), rfe = new PropertyDescriptor(
                    "requestFocusEnabled", mdsClass), ps = new PropertyDescriptor("preferredSize", mdsClass), maxS = new PropertyDescriptor(
                    "maximumSize", mdsClass), minS = new PropertyDescriptor("minimumSize", mdsClass), b = new PropertyDescriptor(
                    "border", mdsClass), nfc = new PropertyDescriptor("nextFocusableComponent", mdsClass), font = new PropertyDescriptor(
                    "font", mdsClass), ttext = new PropertyDescriptor("toolTipText", mdsClass), format = new PropertyDescriptor(
                    "format", mdsClass);

            foreground.setHidden(true);
            background.setHidden(true);
            ps.setHidden(true);
            maxS.setHidden(true);
            minS.setHidden(true);
            b.setHidden(true);
            db.setHidden(true);
            opaque.setHidden(true);
            autoscrolls.setHidden(true);
            alx.setHidden(true);
            aly.setHidden(true);
            dgo.setHidden(true);
            rfe.setHidden(true);
            nfc.setHidden(true);

            value.setPropertyEditorClass(MDateTimeValueEditor.class);
            value.setShortDescription("Current Value");

            PropertyDescriptor[] pd = { value, foreground, background, font, opaque, autoscrolls, alx, aly, dgo, rfe,
                    db, ps, maxS, minS, b, nfc, format, ttext };
            return pd;
        } catch (IntrospectionException e) {
            System.out.println(e.getMessage());
            return super.getPropertyDescriptors();
        }
    }
}
