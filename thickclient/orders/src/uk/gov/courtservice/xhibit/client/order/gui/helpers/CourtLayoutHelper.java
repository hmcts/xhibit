package uk.gov.courtservice.xhibit.client.order.gui.helpers;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomCourtOption;

import javax.swing.*;
import java.awt.*;

/**
 * <p>Title: CourtLayoutHelper.  A helper class that arranges court combo box components.
 * This class is utilised by class MultipleCourtListHelper that determines which courts list(s)
 * should be displayed</p>
 * <p>Description: This class checks int values returned from class CourtListHelper.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Des Johnston
 * @version 1.0
 */
public class CourtLayoutHelper extends JPanel {
    // Components to display a radio button.
    private CustomCourtOption option1;
    private CustomCourtOption option2;
    private CustomCourtOption option3;
    private CustomCourtOption option4;
    //Xml values that contain default court names for each default court
    private String crownReference;
    private String magistrateReference;
    private JLabel magistrateName;
    private JLabel courtName;

    public void setLayoutOrder(CustomCourtOption opt1,
                                    CustomCourtOption opt2,
                                    CustomCourtOption opt3,
                                    CustomCourtOption opt4,
                                    JComboBox boxCb,
                                    JLabel courtName,
                                    JLabel crownName,
                                    JLabel magistrateName,
                                    JLabel orderTypeName,
                                    JLabel youthName,
                                    int chkNo,
                                    String crownRef,
                                    String magistrateRef,
                                    String courtLabel){
        this.option1 = opt1;
        this.option2 = opt2;
        this.option3 = opt3;
        this.crownReference = crownRef;
        this.magistrateReference = magistrateRef;
        this.magistrateName = magistrateName;
        this.courtName = courtName;

        //Initially set visibility of all components to false
        magistrateName.setVisible(false);
        crownName.setVisible(false);
        orderTypeName.setVisible(false);
        youthName.setVisible(false);
        this.courtName.setVisible(false);
        option3.setVisible(false);
        option2.setVisible(false);
        option1.setVisible(false);

        //Check the int value.
        switch (chkNo){
			// Display all court radiobuttons
            case 1:
                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(true);
                this.courtName.setVisible(true);
                boxCb.setSelectedItem(this.crownReference);
                break;
            // Display crown/magistrate court radiobuttons
            case 2:
                option1.setVisible(true);
                option2.setVisible(true);
                this.courtName.setVisible(true);
                boxCb.setSelectedItem(this.crownReference);
                break;
            // Display magistrate court list (no radiobuttons)
            case 3:
                this.option2.setSelected(true);
                magistrateName.setVisible(true);
                boxCb.setSelectedItem(this.magistrateReference);
                if (courtLabel.equals("petty")){
                    magistrateName.setText("Petty Sessional Area: ");

                }
                break;
            // Display crown court list (no radiobuttons)
            case 4:
                this.option1.setSelected(true);
                crownName.setVisible(true);
                boxCb.setSelectedItem(this.crownReference);
                break;
        }

    }
}
