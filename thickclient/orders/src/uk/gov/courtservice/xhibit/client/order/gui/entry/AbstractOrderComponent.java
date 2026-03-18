package uk.gov.courtservice.xhibit.client.order.gui.entry;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.exceptions.MalformedOrderDataException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderComponentException;
import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.components.validators.GuiValidator;
import uk.gov.courtservice.xhibit.client.order.gui.components.validators.ValidatorFactory;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderDate;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderOption;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderSection;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderTime;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

//todo : Use VetoableChangeListener

/**
 * <p>
 * Title: Abstract OrderComponent
 * </p>
 * <p>
 * Description: Abstract OrderComponent class containing useful helper methods
 * provided by the OrderComponent helper class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version
 * 
 */
public abstract class AbstractOrderComponent extends OrderPanel implements OrderComponent {
    private static final Logger log = CSServices.getLogger(AbstractOrderComponent.class);

    protected static final String ESCAPED_LINEFEED_TAG = "\\n";
    protected static final String HTML_START_TAG = "<html>";
    private static final String HTML_END_TAG = "</html>";
    private static final String HTML_LINEFEED_TAG = "<br>";
    private static final String HTML_DISABLED_TAG = "<font color=gray>";
    
    private JComponent visualComponent;

    private OrderComponentHelper helper;

    private GuiValidator val;

    private OrderComponent componentParent;

    private Vector allChildComponents = new Vector();
    
    private boolean initialised = false;

    /**
     * 
     * @return
     */
    protected OrderComponent getComponentParent() {
        return componentParent;
    }

    /**
     * Sets the parent of thic components
     * 
     * @param componentParent
     *            The parent component
     */
    public void setComponentParent(OrderComponent componentParent) {
        this.componentParent = componentParent;
    }

    /**
     * Returns all child components of this component
     * 
     * @return A valtor containg all child components
     */
    public Vector getAllChildComponents() {
        return allChildComponents;
    }

    /**
     * Validates the component and enables/disables sub components
     * 
     * @param b
     *            True if components are to be enabled
     */
    public void switchValidation(boolean b) {
        log.debug("AOC: Switch Validation");
        Vector comps = this.getAllChildComponents();
        Enumeration e = comps.elements();

        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof AbstractOrderComponent) {
                if (obj instanceof OrderDate) {
                    // ((OrderDate)obj).getOrdersDatePanel().getDateComponent().
                    // getDisplay().setValue(new Date());
                    // ((OrderDate)obj).getOrdersDatePanel().getDateComponent().
                    // getDisplay().setValue(new Date());

                } else if (obj instanceof OrderTime) {
                    // S.Bachra 16/5/03 Code commented out to stop the time
                    // component being re-set to the present time
                    // ((OrderTime)obj).getOrdersTimePanel().getTimeComponent().
                    // setValue(new Date());
                }

                GuiValidator validator = ((AbstractOrderComponent) obj).getValidator();
                if (validator != null) {
                    validator.setValidationOn(b);
                }
                if (((AbstractOrderComponent) obj).hasChildren()) {
                    ((AbstractOrderComponent) obj).switchValidation(b);
                }
            }
        }
    }

    /**
     * Adds a child component to this component
     * 
     * @param oc
     *            The OrderComponent to add
     */
    public void addChild(OrderComponent oc) {
        this.allChildComponents.add(oc);
    }

    /**
     * Returns a boolean to indicate if the component has any children.
     * 
     * @return true if yeas, otherwise false.
     */
    public boolean hasChildren() {
        if (this.allChildComponents.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the appropriate validator for this components.
     * 
     * @return The current GUIValidator
     */
    public GuiValidator getValidator() {
        return this.val;
    }

    /**
     * Initialises the component
     * 
     * @param helper
     *            The OrderComponentHelper to be used for the session
     * @param isParentAnOption
     *            boolean to indicate that the parent is an Option
     * @param opt
     *            The Option that the parent points to
     * @throws OrderComponentException
     */
    public void init(OrderComponentHelper helper, boolean isParentAnOption, OrderOption opt)
            throws OrderComponentException {
        setLookAndFeel();
        this.setHelper(helper);
        this.setLayout(new GridBagLayout());
        this.val = ValidatorFactory.getValidator(helper.getAttribute("type"));

        if (val != null) {

            this.val.setIsParentAnOrderOption(isParentAnOption);
            this.val.setOpt(opt);
        }

        initComponent();
        setInitialised(true);
    }

    /**
     * Initialises the component
     * 
     * @param helper
     *            The helper for the curremt component
     */
    public void init(OrderComponentHelper helper) {
        setLookAndFeel();
        this.setHelper(helper);
        this.setLayout(new GridBagLayout());
        this.val = ValidatorFactory.getValidator(helper.getAttribute("type"));
        initComponent();
        setInitialised(true);
    }

    /**
     * Returns the appropriate label for the compinent
     * 
     * @return
     */
    public JComponent getComponentLabel() {
        String labelName = getHelper().getAttribute("label");
        if (labelName == null)
            throw new MalformedOrderDataException("No label supplied for component: "
                    + getHelper().getAttribute("name"));
        JLabel labelLabel = new JLabel("    " + labelName);
        return labelLabel;
    }

    /**
     * Return the component that will be displayed
     * 
     * @return The OrderComponent to be displayed
     */
    public JComponent getVisualComponent() {
        return visualComponent;
    }

    /**
     * Return the component that will be displayed
     * 
     * @return The OrderComponent to be displayed
     */
    public JComponent getVisualLeafComponent() {
        return visualComponent;
    }

    /**
     * Sets the component that will be displayed
     * 
     * @param visualComponent
     *            to be displayed
     */
    public void setVisualComponent(JComponent visualComponent) {
        this.visualComponent = visualComponent;
    }

    /**
     * Return the helper for the component
     * 
     * @return The helper
     */
    public OrderComponentHelper getHelper() {
        return helper;
    }

    /**
     * Sets the helper for the component
     * 
     * @param helper
     */
    public void setHelper(OrderComponentHelper helper) {
        this.helper = helper;
    }

    /**
     * Indicates that the component has a label or not
     * 
     * @return always true
     */
    public boolean isLabelled() {
        return true;
    }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }

    private void logLookAndFeelError(Exception ex) {
        log.error("AbstractOrderComponent: setLookAndFeel: " + UIManager.getSystemLookAndFeelClassName() + " "
                + ex.getMessage());

    }

	public boolean showConfirmOverrideMsg(Frame parent) {
		return XMessageBox.alert(parent,
				XHIBITConstant.getResource(XhibitBundles.OrdersClient, "orders.message.confirmationTitle"), true,
				XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.OrdersClient, "orders.message.confirmationMessage.override"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);
	}
	
	public boolean isInitialised() {
		return initialised;
	}

	public void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}
	
	public boolean isInputLocked() {
		return isReadOnly() && !isAutoGenOverrideAllowed();
	}
	
	/* 
	 * Allow the locked fields to be overridden with a message
	 */
	private boolean isAutoGenOverrideAllowed() {
		return isD20Order();
	}
	
	public boolean isReadOnly() {
		String readOnlyAttrib = getHelper().getAttribute("readonly");
		if (isD20Order()) {
			// Check if false is specified in template otherwise true
			return readOnlyAttrib != null ? !"false".equalsIgnoreCase(readOnlyAttrib) : true;
		}
		// Check if true is specified in template otherwise false
		return readOnlyAttrib != null && "true".equalsIgnoreCase(readOnlyAttrib);
	}
	
	public boolean isD20Order() {
		return (getHelper().getOrderDataReference() != null &&
				getHelper().getOrderDataReference().contains("//ord:D20")) ||
			   (getHelper().getAttribute("ref1") != null &&
			    getHelper().getAttribute("ref1").contains("//ord:D20"));
	}
	
	public boolean isSwitchedOn() {
		if (this.getComponentParent() instanceof OrderSection) {
			OrderSection component = (OrderSection) this.getComponentParent();
			return component.isSwitchable() ? component.getjCheckBoxCbx().isSelected() : true;
		}
		return false;
	}
	
	protected Frame getParentFrame(Container parent) {
    	Frame frame = null;
    	if (parent.getParent() != null) {
	    	try {
	    		frame = (Frame) parent.getParent();
	    	} catch (Exception ex) {
	    		frame = getParentFrame(parent.getParent());
	    	}
    	}
    	return frame;
    }
	
	protected boolean isMultilineText(String text) {
		return text != null && text.contains(ESCAPED_LINEFEED_TAG);
	}
		
	protected String addLineBreaks(String text, boolean isEnabled) {
		// Add line feeds as html <br> statements
        if (isMultilineText(text) ) {
        	return HTML_START_TAG +
        			(isEnabled ? "" : HTML_DISABLED_TAG) +
        			text.replace(ESCAPED_LINEFEED_TAG, HTML_LINEFEED_TAG) +
        			HTML_END_TAG;
        }
        return text;
	}
}
