package uk.gov.courtservice.xhibit.client.results.util.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextAreaFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author Simon Gilmore
 * @author tz0d5m
 * @version $Revision: 1.11 $
 */
public class AdditionalInfoTableCell extends JPanel {
    public static final int SHOW_NOTHING = 0;

    public static final int SHOW_OFFENCE = 1;

    public static final int SHOW_OTHER = 2;

    /**
     * Constant used to represent the property name for events whenn the offence
     * text changes.
     */
    public static final String OFFENCE_SELECTED = "OFFENCE_SELECTED";

    private static final String lineBreak = "<BR>";

    // only to be set in the constructor...
    private final XhibitApplicationController xac;

    private JButton button = null;

    private JTextArea offenceTextField;

    private JScrollPane offenceScrollPane;

    private JScrollPane otherScrollPane;

    private JLabel otherLabel = null;

    private JTextArea otherText = null;

    private RefOffenceBasicValue refOffence;

    private int type = 0;

    /**
     * Constructor used to set up all of the required components.
     * 
     * @param xac
     *            The application controller
     * @param offenceTextFieldEditable
     *            <code>boolean</code> value to indicate whether the offence
     *            text field should be editable or not.
     */
    public AdditionalInfoTableCell(XhibitApplicationController xac, boolean offenceTextFieldEditable) {
        this.xac = xac;

        setLayout(new GridBagLayout());

        add(getOffenceScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(getSearchOffenceBtn(), new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));

        add(getOtherLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
        add(getOtherScrollPane(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 2, 0, 0), 0, 0));

        setPreferredSize(new Dimension(150, 25));

        // the offence text field is disabled/enabled based upon the screen...
        if (!offenceTextFieldEditable) {
            getOffenceTextArea().setEditable(false);
            getOffenceScrollPane().setBorder(BorderFactory.createEmptyBorder());
        }
    }

    private JScrollPane getOffenceScrollPane() {
        if (offenceScrollPane == null) {
            offenceScrollPane = new JScrollPane(getOffenceTextArea(), ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }

        return offenceScrollPane;
    }

    private JScrollPane getOtherScrollPane() {
        if (otherScrollPane == null) {
            otherScrollPane = new JScrollPane(getOtherTextArea(), ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }

        return otherScrollPane;
    }

    /**
     * Lazy instantiates the offence text.
     * 
     * @return the offence text.
     */
    private JTextArea getOffenceTextArea() {
        if (offenceTextField == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.limitedText(240) });
            offenceTextField = JTextAreaFactory.getTextArea(doc);
        }

        return offenceTextField;
    }

    public String getText() {
        if (this.type == SHOW_OFFENCE) {
            return getOffenceTextArea().getText();
        } else if (this.type == SHOW_OTHER) {
            return getOtherTextArea().getText();
        }

        return "";
    }

    public void setOffenceText(String text, boolean firePropertyChange) {
        getOffenceTextArea().setText(text);
        setToolTipText(splitString(text, 30));
        if (firePropertyChange) {
            firePropertyChange(OFFENCE_SELECTED, null, text);
        } else {
            // Setting up panel so null out ref offence so it is only ever
            // set
            // when the search button is clicked.
            refOffence = null;
        }
    }

    private JButton getSearchOffenceBtn() {
        if (button == null) {
            button = new JButton();
            // button.setAction(new SearchOffenceAction());
            Dimension buttonSize = new Dimension(XHIBITConstant.getLineHeight(), XHIBITConstant.getLineHeight());
            button.setMinimumSize(buttonSize);
            button.setPreferredSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.setFocusPainted(false);
        }
        return button;
    }

    private JLabel getOtherLabel() {
        if (otherLabel == null) {
            // @todo - this needs to be externalized...
            otherLabel = new JLabel("Other:");
        }
        return otherLabel;
    }

    private JTextArea getOtherTextArea() {
        if (otherText == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.limitedText(80) });
            otherText = JTextAreaFactory.getTextArea(doc);
        }
        return otherText;
    }

    public void show(AdditionalInfoTableCellComponent tclc, boolean isSelected) {
        this.type = tclc.getShowParameter();

        // determine which component to display...
        getOffenceScrollPane().setVisible(this.type == SHOW_OFFENCE);
        getSearchOffenceBtn().setVisible(this.type == SHOW_OFFENCE);

        getOtherLabel().setVisible(this.type == SHOW_OTHER);
        getOtherScrollPane().setVisible(this.type == SHOW_OTHER);

        if (this.type == SHOW_OFFENCE) {
            // set the offence text, the tool tip text is handled in method...
            setOffenceText(tclc.getText(), false);
            if (tclc.getCode().equals("GAO") // Plea
                    || tclc.getCode().equals("GLO") // Plea
                    || tclc.getCode().equals("GA") // Verdict
                    || tclc.getCode().equals("GAJ") // Verdict
                    || tclc.getCode().equals("GL") // Verdict
                    || tclc.getCode().equals("GLJ")) // Verdict
            {
                button.setAction(new SearchObsoleteOffenceAction());
            } else {
                button.setAction(new SearchOffenceAction());
            }
        } else {
            // update the label colours if displaying the other text...
            if (this.type == SHOW_OTHER) {
                final Color textColor = isSelected ? SystemColor.textHighlightText : SystemColor.textText;
                getOtherLabel().setForeground(textColor);
                getOtherTextArea().setText(tclc.getText());
            }

            // always clear the tool tip text...
            setToolTipText("");
        }
    }

    public Dimension getPreferredSize() {
        if (type == SHOW_OFFENCE) {
            return getOffenceTextArea().getPreferredSize();
        } else if (type == SHOW_OTHER) {
            return getOtherTextArea().getPreferredSize();
        } else {
            return super.getPreferredSize();
        }
    }

    public int getType() {
        return type;
    }

    /**
     * Method to acquire the <code>RefOffenceBasicValue</code> that was
     * generated when a search has been successfully performed to find an
     * offence.
     * 
     * @return <i>null </i> if no lookup has been performed (or was cancelled),
     *         otherwise the looked up value will be returned.
     */
    public RefOffenceBasicValue getRefOffenceBasicValue() {
        return refOffence;
    }

    public void setRefOffenceBasicValue(RefOffenceBasicValue value) {
        refOffence = value;
    }

    /**
     * Private utility method used to break the passed in <code>String</code>
     * up with &lt;br&gt; tags at the first space prior to the length passed in.
     * The returned response is then wrapped in html tags.
     * 
     * @param s
     *            The <code>String</code> to split.
     * @param splitLength
     *            The maximum length of each line.
     * @return
     */
    private String splitString(String s, int splitLength) {
        String returnString = s;
        int lastPoint = splitLength;
        int stringLength = s.length();
        while (lastPoint < stringLength) {
            int firstSpace = returnString.indexOf(' ', lastPoint);
            if (firstSpace > 0) {
                returnString = returnString.substring(0, firstSpace) + lineBreak
                        + returnString.substring(firstSpace + 1);
                lastPoint = firstSpace + splitLength;
            } else {
                lastPoint = stringLength;
            }
        }

        return "<HTML>" + returnString + "</HTML>";
    }

    /**
     * <p>
     * Title: Search Offence Action
     * </p>
     * <p>
     * Description: Action for the search for offence button that triggers the
     * search for offence action.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Simon Gilmore
     */
    public class SearchOffenceAction extends XAction implements SearchProcessHandler {
        private AdditionalInfoTableCell info = getThis();

        /**
         * Creates a search offence action.
         */
        public SearchOffenceAction() {
            setController(xac);
            setIcon(XHIBITConstant.imageRoot + "search.gif");
        }

        /**
         * The processing performed when the action is fired.
         * 
         * @param e
         *            the action event. A semantic event which indicates that a
         *            component-defined action occured. This high-level event is
         *            generated by a component (such as a Button) when the
         *            component-specific action occurs (such as being pressed).
         * @throws java.lang.Exception
         */
        public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
            AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                    (XhibitApplicationController) getController(), XhibitActions.OpenSearchOffence);

            sa.setCaller(this);
            sa.xActionPerformed(e);
        }

        /**
         * Processes the results of the search for offence.
         * 
         * @param searchAction
         *            the search action.
         * @throws CSRecoverableException
         */
        public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
            Collection col = searchAction.getResults();
            Iterator it = col.iterator();
            if (it.hasNext()) {
                refOffence = (RefOffenceBasicValue) it.next();
                setOffenceText(refOffence.getOffenceDesc(), true);
            } else {
                throw new UserCancelException();
            }
        }

        public AdditionalInfoTableCell getOuter() {
            return info;
        }

    }

    /**
     * Method to enable the vertical scroll bar on for this cell.
     * 
     * @since Version 1.7
     */
    public void displayScrollBars() {
        getOffenceScrollPane().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        getOtherScrollPane().setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * <p>
     * Title: Search Obsolete Offence Action
     * </p>
     * <p>
     * Description: Action for the search for offence (including obsolete
     * offences) button that triggers the search for offence action.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author Simon Gilmore
     */
    public class SearchObsoleteOffenceAction extends XAction implements SearchProcessHandler {
        private AdditionalInfoTableCell info = getThis();

        /**
         * Creates a search offence action.
         */
        public SearchObsoleteOffenceAction() {
            setController(xac);
            setIcon(XHIBITConstant.imageRoot + "search.gif");
        }

        /**
         * The processing performed when the action is fired.
         * 
         * @param e
         *            the action event. A semantic event which indicates that a
         *            component-defined action occured. This high-level event is
         *            generated by a component (such as a Button) when the
         *            component-specific action occurs (such as being pressed).
         * @throws java.lang.Exception
         */
        public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
            AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                    (XhibitApplicationController) getController(), XhibitActions.OpenSearchObsoleteOffence);

            sa.setCaller(this);
            sa.xActionPerformed(e);
        }

        /**
         * Processes the results of the search for offence.
         * 
         * @param searchAction
         *            the search action.
         * @throws CSRecoverableException
         */
        public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
            Collection col = searchAction.getResults();
            Iterator it = col.iterator();
            if (it.hasNext()) {
                refOffence = (RefOffenceBasicValue) it.next();
                setOffenceText(refOffence.getOffenceDesc(), true);
            } else {
                throw new UserCancelException();
            }
        }

        public AdditionalInfoTableCell getOuter() {
            return info;
        }

    }

    private AdditionalInfoTableCell getThis() {
        return this;
    }
}