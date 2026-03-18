package uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;

/**
 * <p>
 * Title: CourtLayoutHelper. A helper class that arranges court combo box
 * components on a JPanel.
 * <p>
 * Description: This class is utilised by class MultipleCourtListHelper that
 * determines which courts list(s) should be displayed
 * </p>
 * This class checks int values returned from class CourtListHelper and displays
 * components based on the returned int value.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Desmond Johnston
 * @version 1.0
 */
public class CourtLayoutHelper extends OrderPanel {
    private static final String LJA_LABEL = "Local Justice Area: ";

    private static final String STR_PETTY = "petty";

    // Components to display a radio button.
    private CourtOption option1;

    private CourtOption option2;

    private CourtOption option3;

    // Xml values that contain default court names for each default court
    private String crownReference;

    private String magistrateReference;

    private JLabel magistrateName;

    public void setLayoutOrder(CourtOption opt1, CourtOption opt2, CourtOption opt3, JComboBox boxCb, JLabel courtName,
            JLabel crownName, JLabel magistrateName, JLabel orderTypeName, JLabel youthName, int chkNo,
            String crownRef, String magistrateRef, String courtLabel) {
        this.option1 = opt1;
        this.option2 = opt2;
        this.option3 = opt3;
        this.crownReference = crownRef;
        this.magistrateReference = magistrateRef;
        this.magistrateName = magistrateName;
        // Initially set visibility of all components to false
        magistrateName.setVisible(false);
        crownName.setVisible(false);
        orderTypeName.setVisible(false);
        youthName.setVisible(false);
        courtName.setVisible(false);
        option3.setVisible(false);
        option2.setVisible(false);
        option1.setVisible(false);

        // Check the int value.
        switch (chkNo) {
        // Display all court radiobuttons - plus checkbox
        case 1:
            option1.setVisible(true);
            option2.setVisible(true);
            option3.setVisible(true);
            courtName.setVisible(true);
            this.option1.setSelected(true);

            // boxCb.setSelectedItem(this.crownReference); // Des Johnston
            // SCR 52737
            break;
        // Display crown/magistrate court radiobuttons - plus checkbox
        case 2:
            option1.setVisible(true);
            option2.setVisible(true);
            courtName.setVisible(true);
            this.option1.setSelected(true);

            // boxCb.setSelectedItem(this.crownReference); // Des Johnston
            // SCR 52737
            break;
        // Display magistrate court list checkbox (no radiobuttons)
        case 3:
            this.option2.setSelected(true);
            magistrateName.setVisible(true);
            // boxCb.setSelectedItem(this.magistrateReference); // Des
            // Johnston SCR 52737
            if (courtLabel.equals(STR_PETTY)) {
                magistrateName.setText(LJA_LABEL);
            }
            break;
        // Display crown court list checkbox (no radiobuttons)
        case 4:
            this.option1.setSelected(true);
            crownName.setVisible(true);
            // boxCb.setSelectedItem(this.crownReference); // Des Johnston
            // SCR 52737
            break;
        case 5:
            option1.setVisible(true);
            option2.setVisible(true);
            option3.setVisible(true);
            courtName.setVisible(true);
            this.option3.setSelected(true);

            // boxCb.setSelectedItem(this.crownReference); // Des Johnston
            // SCR 52737
            break;
        }
    }
}
