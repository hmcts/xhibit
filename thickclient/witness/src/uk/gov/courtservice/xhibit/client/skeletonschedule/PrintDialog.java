package uk.gov.courtservice.xhibit.client.skeletonschedule;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Witness Dialog
 * </p>
 * <p>
 * Description: The trial time estimate dialog
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */

public class PrintDialog extends XDialog {
    /*
     * Mode Key Constants
     */

    public static final int BYDAY = 0;

    public static final int BYWEEK = 1;

    /*
     * Resource Key Constants
     */

    private static final String PRINT_TITLE = "skeletonschedule.print.title";

    private static final String PRINT_BYDAY = "skeletonschedule.print.byday";

    private static final String PRINT_BYWEEK = "skeletonschedule.print.byweek";

    /*
     * Logger
     */

    private static final Logger log = CSServices.getLogger(PrintDialog.class);

    /*
     * Implementation
     */

    private Integer witnessId;

    private XhibitApplicationController xac;

    private PrintPanel printPanel;

    public PrintDialog(XhibitApplicationController newXac) // throws
    // CSRecoverableException
    {
        super(newXac, getResource(PRINT_TITLE), true, OKCANCEL, DEFAULTOK);

        log.debug("PrintDialog(" + newXac + ")");
        this.xac = newXac;
        printPanel = new PrintPanel(xac);
        addBodyPanel(printPanel);
        pack();
    }

    public int getPrintMode() {
        return printPanel.getPrintMode();
    }

    /*
     * Body Panel Class
     */

    private static class PrintPanel extends XPanel {

        /*
         * Constraints
         */

        private static final GridBagConstraints byDayRbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);

        private static final GridBagConstraints byWeekRbConstraints = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);

        private int selectedRadio;

        /*
         * Implementation
         */

        private final XhibitApplicationController xac;

        private JRadioButton byDayRadio;

        private JRadioButton byWeekRadio;

        private ButtonGroup printGroup;

        public PrintPanel(XhibitApplicationController newXac) {
            super(new GridBagLayout());

            log.debug("PrintPanel(" + newXac + ")");

            xac = newXac;

            stepInitialise();
            jbInit();
        }

        private void jbInit() {
            add(getByDayRadio(), byDayRbConstraints);
            add(getByWeekRadio(), byWeekRbConstraints);
        }

        /*
         * Life Cycle Methods
         */

        public void stepInitialise() {
        }

        public void stepActivate() {
            log.debug("stepActivate()");
            if (!getByWeekRadio().isSelected() && !getByDayRadio().isSelected()) {
                getByDayRadio().setSelected(true);
            }
        }

        public void stepUpdateViewState() {
        }

        public void stepValidate() {
        }

        public void stepDeactivate() {
        }

        public void stepDeinitialise(boolean update) {
            if (update) {
                if (byDayRadio.isSelected())
                    selectedRadio = BYDAY;
                else if (byWeekRadio.isSelected())
                    selectedRadio = BYWEEK;
                else
                    selectedRadio = BYDAY;
            }
        }

        /*
         * Utility Methods
         */

        public int getPrintMode() {
            return selectedRadio;
        }

        private ButtonGroup getButtonGroup() {
            if (printGroup == null) {
                printGroup = new ButtonGroup();
            }
            return printGroup;
        }

        private JRadioButton getByDayRadio() {
            if (byDayRadio == null) {
                JRadioButton newRb = createRadio(getResource(PRINT_BYDAY));
                getButtonGroup().add(newRb);
                byDayRadio = newRb;
            }
            return byDayRadio;
        }

        private JRadioButton getByWeekRadio() {
            if (byWeekRadio == null) {
                JRadioButton newRb = createRadio(getResource(PRINT_BYWEEK));
                getButtonGroup().add(newRb);
                byWeekRadio = newRb;
            }
            return byWeekRadio;
        }
    }

    /*
     * Component Factory Methods
     */

    private static JRadioButton createRadio(String text) {
        return new JRadioButton(text);
    }

    /*
     * Utility Methods
     */

    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }

}
