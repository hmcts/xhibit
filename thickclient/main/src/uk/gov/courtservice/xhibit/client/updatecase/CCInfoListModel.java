package uk.gov.courtservice.xhibit.client.updatecase;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class CCInfoListModel implements ComboBoxModel {
    private Object selectedObject = null;

    private int selectedCCInfoIndex = 0;

    private Vector ccInfos;

    public CCInfoListModel() {
        this.setup();
    }

    public void setSelectedItem(Object o) {
        XHIBITConstant.debug("CCInfoListModel.setSelectedObject(" + o + ").");
        this.selectedCCInfoIndex = this.ccInfos.indexOf(o);
        this.selectedObject = o;
    }

    public void setSelectedIndex(int index) {
        this.selectedCCInfoIndex = index;
    }

    public void addCCInfo(Object CCInfo) {
        this.ccInfos.addElement(CCInfo);
    }

    private void setup() {
        this.ccInfos = new Vector();

        ResourceBundle rsc = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainHearingHeader);

        Enumeration keys = rsc.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith("CCInfoCode_")) {
                String id = key.substring(11, key.length());
                CCInfoHolder ccInfo = new CCInfoHolder();
                ccInfo.id = id;
                ccInfo.text = rsc.getString(key);
                this.ccInfos.addElement(ccInfo);
                // XHIBITConstant.debug("Added CCInfo object " + ccInfo.id + ",
                // " + ccInfo.text + ".");
            }
        }
    }

    public Object getElementAt(int index) {
        return ((CCInfoHolder) this.ccInfos.elementAt(index));
    }

    public int getSize() {
        return this.ccInfos.size();
    }

    public Object getSelectedItem() {
        Object returnObject = null;
        try {
            // returnObject =
            // this.ccInfos.elementAt(this.selectedCCInfoIndex);
            returnObject = this.selectedObject;
            XHIBITConstant.debug("CCInfoListModel.getSelectedItem returns:" + this.selectedObject.toString());
        } catch (Exception e) {
            XHIBITConstant.debug("ArrayOutOfBounds?.");
            e.printStackTrace();
        }
        return returnObject;
    }

    public void removeListDataListener(ListDataListener ldl) {
    }

    public void addListDataListener(ListDataListener ldl) {
    }

    public class CCInfoHolder {
        public String id = "-1";

        public String text = "n/aaa";

        public String toString() {
            String x = "CCInfo:";
            x = x.concat(" id:" + this.id);
            x = x.concat(" text:" + this.text);
            return x;
        }

    }

    public String getId(CCInfoHolder ccInfo) {
        return ccInfo.id;
    }

    public String getText(CCInfoHolder ccInfo) {
        return ccInfo.text;
    }
}