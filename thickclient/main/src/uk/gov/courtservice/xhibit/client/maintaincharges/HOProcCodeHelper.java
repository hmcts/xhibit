package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Component;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.util.SingleItemSelectionDialog;
import uk.gov.courtservice.xhibit.client.util.SingleItemSelectionPanel;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:Helper to determin behaviour when setting and selecting HO Proc Codes
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
 * @version 1.1 Updated for new search framework
 */

public class HOProcCodeHelper {

    public static final String TRIAL = "HO_PROC_TRIAL";

    public static final String S41 = "HO_PROC_S41";

    public static final String SENT = "HO_PROC_SENT";

    public static final String BREACH = "HO_PROC_BREACH";

    public static final String FAIL_2_APPEAR_HO_CODE = "09";
    
    private Component parent = new XFrame();

    public HOProcCodeHelper(java.awt.Window parent) {
        if ((parent instanceof java.awt.Frame) || (parent instanceof java.awt.Dialog))
            this.parent = parent;
    }

    public HOProcCodeHelper() {
        // Empty
    }

    /**
     * Method to return JPanel with proc code selection
     */
    public SingleItemSelectionPanel getUserSelectionPanel(XPanel parentPanel, Collection listToChooseFrom,
            Integer currentRefCode) {
        RefSystemCodeBasicValue defValue = null;

        // find the default and pass into the call
        if (currentRefCode != null && listToChooseFrom != null) {
            Iterator iter = listToChooseFrom.iterator();
            while (iter.hasNext()) {
                RefSystemCodeBasicValue item = (RefSystemCodeBasicValue) iter.next();
                if (item.getId() != null) {
                    if (item.getId().intValue() == currentRefCode.intValue()) {
                        defValue = item;
                        break;
                    }
                }
            }
        }
        return new SingleItemSelectionPanel(parentPanel, listToChooseFrom, defValue, getString("helpProcCode"));
    }

    /**
     * Check whether there are multiple HO Proc Codes for type.
     */
    public boolean isHOCodeList(String procType) throws BisRefControllerException {
        Collection hoCodes = getProcList(procType);
        if (hoCodes == null)
            return false;

        return (hoCodes.size() > 1 ? true : false);
    }

    /**
     * Return size of HO Proc Code selection
     */
    public int getHOCodeSize(String procType) throws BisRefControllerException {
        Collection hoCodes = getProcList(procType);
        if (hoCodes == null)
            return 0;
        return hoCodes.size();
    }

    
    public RefSystemCodeBasicValue getHoProcCodeFromProcCodeAndProcType(String hoProcCode, String hoProcType) 
    throws BisRefControllerException {
        Collection hoCodes = getProcListFromProcCode(hoProcCode, hoProcType);
        // Should be only one result
        if (hoCodes == null)
            return null;
        if (hoCodes.size() <= 0)
            return null;
        if (hoCodes.size() == 1) {
            Object[] arr = hoCodes.toArray();
            return (RefSystemCodeBasicValue) arr[0];
        }
        return null;
    }
    
    /**
     * Return RefSystemCodeBasicValue
     * 
     * @param procType
     * @param currentRefCode
     * @return
     * @throws BisRefControllerException
     */
    public RefSystemCodeBasicValue getHoProcCode(String procType, Integer currentRefCode)
            throws BisRefControllerException {
        Collection hoCodes = getProcList(procType);
        if (hoCodes == null)
            return null;
        if (hoCodes.size() <= 0)
            return null;
        if (hoCodes.size() == 1) {
            Object[] arr = hoCodes.toArray();
            return (RefSystemCodeBasicValue) arr[0];
        }
        Object procSel = getUserSelection(hoCodes, currentRefCode);
        return (procSel == null ? null : (RefSystemCodeBasicValue) procSel);
    }

    public RefSystemCodeBasicValue getHoProcCode(String procType) throws BisRefControllerException {
        return getHoProcCode(procType, null);
    }

    private Collection getProcListFromProcCode(String hoProcCode, String hoProcType) 
    throws BisRefControllerException {
        Integer courtId = XhibitSingleton.getInstance().getCourtId();
        RefSystemCodeCriteria rcc = new RefSystemCodeCriteria();
        rcc.setCourtId(courtId.toString());
        rcc.setCode(hoProcCode);
        rcc.setCodeType(hoProcType);
        Collection hoCodes = XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(rcc);
        return hoCodes;
    }
    
    /**
     * Find all system codes for a given code type. Use one of the statics
     * above, but can pass in any code type
     * 
     * @param procType
     * @return Collection of RefSystemCodeBasicValue
     * @throws BisRefControllerException
     */
    public Collection getProcList(String procType) throws BisRefControllerException {
        Integer courtId = XhibitSingleton.getInstance().getCourtId();
        RefSystemCodeCriteria rcc = new RefSystemCodeCriteria();
        rcc.setCourtId(courtId.toString());
        rcc.setCodeType(procType);
        Collection hoCodes = XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(rcc);
        return hoCodes;
    }

    private Object getUserSelection(Collection listToChooseFrom, Integer currentRefCode) {
        RefSystemCodeBasicValue defValue = null;

        /* find the default and pass into the call */
        if (currentRefCode != null) {
            Iterator iter = listToChooseFrom.iterator();
            while (iter.hasNext()) {
                RefSystemCodeBasicValue item = (RefSystemCodeBasicValue) iter.next();
                if (item.getId() != null) {
                    if (item.getId().intValue() == currentRefCode.intValue()) {
                        defValue = item;
                        break;
                    }
                }
            }
        }

        SingleItemSelectionDialog sis;
        if (parent instanceof java.awt.Frame) {
            sis = new SingleItemSelectionDialog((java.awt.Frame) parent, listToChooseFrom, defValue,
                    getString("titleProcCode"), getString("helpProcCode"));
        } else {
            sis = new SingleItemSelectionDialog((java.awt.Dialog) parent, listToChooseFrom, defValue,
                    getString("titleProcCode"), getString("helpProcCode"));
        }
        sis.setVisible(true);
        return sis.getSelectedItem();
    }

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.MaintainCharges, key);
    }
}