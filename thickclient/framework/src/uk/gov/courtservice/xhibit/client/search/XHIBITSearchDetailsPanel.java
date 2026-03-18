package uk.gov.courtservice.xhibit.client.search;

import java.awt.GridBagConstraints;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.util.ReflectionHelper;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT2 XHIBITSearchDetailsPanel
 * </p>
 * <p>
 * Description: Generic construct to display a value object.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version $Revision: 1.3 $
 */
public class XHIBITSearchDetailsPanel extends XHIBITSearchPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5489373139233968035L;

	protected XHIBITSearch myParentSearchControl;

    protected XHIBITSearchDetails xsDetails;
    
    protected CSValueObject valueObject;
    
    protected XDialog parentContainer;

    protected JButton backButton;
    
    private final String FIELD_PATH_DELIMITER = ".";

    public XHIBITSearchDetailsPanel(XHIBITSearchDetails xsDetails, XHIBITSearch xsSearch) {
        super(xsSearch);
        this.myParentSearchControl = xsSearch;
        jbInit(xsDetails);
    }
    
    /**
     * 
     * @return
     */
    public XHIBITSearch getMyParentSearchControl() {
		return myParentSearchControl;
	}
 
    /**
	 * @param myParentSearchControl the myParentSearchControl to set
	 */
	public void setMyParentSearchControl(XHIBITSearch myParentSearchControl) {
		this.myParentSearchControl = myParentSearchControl;
	}

	/**
	 * 
	 * @return
	 */
	public CSValueObject getValueObject() {
    	return valueObject;
    }
	
	/**
	 * 
	 * @param valueObject the valueObject to set
	 */
    public void setValueObject(CSValueObject valueObject) {
        this.valueObject = valueObject;

        if (myParentSearchControl.isInternalDebug())
            log.debug("received value object " + valueObject);

        // iterate over the display attributes

        Iterator attribIterator = this.xsDetails.theDetailsKeys.iterator();
        while (attribIterator.hasNext()) {
            String attribKey = (String) attribIterator.next();
            // Field field =
            // (Field)this.xsDetails.theDetails.get(attribKey);
            // if (myParentSeachControl.internalDebug)
            // log.debug(".setValueObject() key["+attribKey+"] field :" +
            // field);

            JComponent jcomponent = (JComponent) this.xsDetails.theDetailsComponents.get(attribKey);
            if (myParentSearchControl.isInternalDebug())
                log.debug(".setValueObject() key[" + attribKey + "] component :" + jcomponent);

            Object attributeValue = "";
            try {
                Object[] parameterValues = {};
                Method[] getterMethods = ReflectionHelper.getGetterMethodForFieldPath(valueObject.getClass(),
                        attribKey, FIELD_PATH_DELIMITER);

                Object targetObject = valueObject;

                for (int i = 0; i < getterMethods.length; i++) {
                    attributeValue = getterMethods[i].invoke(targetObject, parameterValues);
                    targetObject = attributeValue;
                }

                if (attributeValue == null) {
                    attributeValue = "";
                }
            } catch (Exception e) {
                log.error("Exception whilst getting value for attribute with key " + attribKey);
                log.error(e);
            }

            if (jcomponent instanceof JTextComponent) {
                JTextComponent jtc = (JTextComponent) jcomponent;
                jtc.setText((String) attributeValue);
            } else if (jcomponent instanceof JLabel) {
                JLabel jtc = (JLabel) jcomponent;
                jtc.setText((String) attributeValue);
            }
        }
    }

    public void jbInit(XHIBITSearchDetails ixsDetails) {
        // iterate on the criteria and add them to panel
        stepTitle = new JLabel(ixsDetails.getStepTitle());
        stepDescription = new JLabel(ixsDetails.getStepDescription());
        super.jbInit();

        this.xsDetails = ixsDetails;

        Enumeration keysEnum = xsDetails.theDetailsKeys.elements();

        int noOfCriteria = xsDetails.theDetails.size();
        if (myParentSearchControl.isInternalDebug())
            log.debug("found " + noOfCriteria + " detail attribute keys.");

        int criteriaCounter = 0;

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        while (keysEnum.hasMoreElements()) {
            criteriaCounter++;
            // get the key (always a Criteria Attribute Name
            String key = (String) keysEnum.nextElement();
            if (myParentSearchControl.isInternalDebug())
                log.debug("Processing detail attribute no " + criteriaCounter + " named '" + key + "'.");

            // get the label
            JLabel jLabel = new JLabel((String) xsDetails.theDetailsLabels.get(key) + ":");

            gbc.gridy = gbc.gridy + 1;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.weightx = 0.0;
            doAdd(jLabel, gbc);

            // get the component
            JComponent jComponent = (JComponent) xsDetails.theDetailsComponents.get(key);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            doAdd(jComponent, gbc);
        }

        gbc = new GridBagConstraints(0, gbc.gridy + 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        doAdd(XHIBITConstant.getSpacer(), gbc);
        this.backButton = new JButton();
        backButton.setAction(getBackAction());
        backButton.setMnemonic(((XAction) backButton.getAction()).getMnemonicKey().intValue());

        gbc = new GridBagConstraints(1, gbc.gridy + 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        doAdd(backButton, gbc);

        this.xsSearch.setSearchScreenName("Details");
    }

    protected BackAction getBackAction() {
    	return new BackAction(this);
    }
}