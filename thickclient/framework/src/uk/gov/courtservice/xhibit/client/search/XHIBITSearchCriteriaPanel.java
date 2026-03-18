package uk.gov.courtservice.xhibit.client.search;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

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
 * @version $Revision: 1.4 $
 */
public class XHIBITSearchCriteriaPanel extends XHIBITSearchPanel {
    private XHIBITSearch myParentSeachControl;

    private JButton searchButton;

    public XHIBITSearchCriteriaPanel(XHIBITSearchCriteria xsCriteria, XHIBITSearch xsSearch) {
        super(xsSearch);
        try {
            this.myParentSeachControl = xsSearch;
            jbInit(xsCriteria);
        } catch (Exception e) {
            log.error("Exception caught during instantiation of XHIBITSearchCriteriaPanel.");
            if (this.xsSearch.isInternalDebug()) {
                log.error(e);
                e.printStackTrace(System.err);
            } else {
                CSRecoverableException csre = new CSRecoverableException(
                        "gui.user.search.genericFailedMessage",
                        "myParentSeachControl.doSearch() returned exception to XHIBITSearchCriteriaPanel's SearcAction.",
                        e);
                XHIBITConstant.handleError(csre);
            }
        }
    }

    public void jbInit(XHIBITSearchCriteria xsCriteria) {
        // iterate on the criteria and add them to panel
        stepTitle = new JLabel(xsCriteria.getStepTitle());
        stepDescription = new JLabel(xsCriteria.getStepDescription());
        searchButton = new JButton();
        searchButton.setAction(new SearchAction());
        searchButton.setMnemonic(((XAction) searchButton.getAction()).getMnemonicKey().intValue());

        super.jbInit();

        Enumeration keysEnum = xsCriteria.getTheCriteriaKeys().elements();

        int noOfCriteria = xsCriteria.theCriteria.size();
        if (this.xsSearch.isInternalDebug())
            log.debug("found " + noOfCriteria + " criteria keys.");

        int criteriaCounter = 0;

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        while (keysEnum.hasMoreElements()) {
            // get the key (always a Criteria Attribute Name
            String key = (String) keysEnum.nextElement();
            criteriaCounter++;
            if (this.xsSearch.isInternalDebug())
                log.debug("Processing criteria no " + criteriaCounter + " named '" + key + "'.");

            Object component = xsCriteria.getTheCriteriaComponents().get(key);

            if (component != null) {
                // put the label
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridy = gbc.gridy + 1;
                gbc.gridx = 0;
                gbc.weightx = 0.0;
                String labelText = (String) xsCriteria.theCriteriaLabels.get(key);
                if (labelText != null && !labelText.isEmpty()) {
                	JLabel jLabel = new JLabel(labelText + ":");
                    doAdd(jLabel, gbc);	
                }

                // put the component
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                if (component instanceof ButtonGroup) {
                	JPanel radioButtonPanel = getRadioButtonPanel((ButtonGroup) component);
                	add(radioButtonPanel, gbc);
                } else {
                	doAdd((JComponent) component, gbc);
                }
            }
        }

        gbc = new GridBagConstraints(1, gbc.gridy + 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
        doAdd(searchButton, gbc);

        // CR58 BEGIN
        if (xsCriteria.getMethodName().equals("findOffences")) {
            JButton button = new JButton();
            button.setVisible(true);
            gbc = new GridBagConstraints(1, gbc.gridy + 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
            XAction uncodedOffenceAction = XhibitActions.getAction(myParentSeachControl.getXAC(),
                    XhibitActions.AddUncodedOffence);
            uncodedOffenceAction.setCaller(this);
            button.setAction(uncodedOffenceAction);
	    //disable if its called from general tab
            if(this.getMyParentSeachControl().getParentFrame().getTitle().indexOf("Create Trial Case")>=0) {
            	button.setEnabled(false);
            }
            doAdd(button, gbc);
        }
        // CR58 END

        gbc = new GridBagConstraints(0, gbc.gridy + 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        doAdd(XHIBITConstant.getSpacer(), gbc);

        this.xsSearch.setSearchScreenName("Criteria");
    }

    public void stepActivate() {
        ((OkCancelPanel) xsSearch.getButtonPanel()).getOkAction().setEnabled(false);
    }

    private class SearchAction extends XAction {
        public SearchAction() {
            populateFromBundle("btnSearch");
        }

        public void xActionPerformed(ActionEvent actionEvent) throws CSRecoverableException {
            try {
                myParentSeachControl.doSearch();
            } catch (Exception e) {
                CSRecoverableException csre = new CSRecoverableException(
                        "gui.user.search.genericFailedMessage",
                        "myParentSeachControl.doSearch() returned exception to XHIBITSearchCriteriaPanel's SearcAction.",
                        e);
                throw csre;
            }
        }
    }

    public XHIBITSearch getMyParentSeachControl() {
        return this.myParentSeachControl;
    }
    
    private JPanel getRadioButtonPanel(ButtonGroup buttonGroup) {
    	GridBagConstraints rbGbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridBagLayout());
    	Enumeration<AbstractButton> radioButtons = buttonGroup.getElements();
		while (radioButtons.hasMoreElements()) {
			JRadioButton radioButton = (JRadioButton) radioButtons.nextElement();
			if (radioButton != null) {
				panel.add(radioButton, rbGbc);
				if (radioButtons.hasMoreElements()) {
					rbGbc.gridx++;
				}
			}
		}
		return panel;
    }
}