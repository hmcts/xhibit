
package mseries.nationality;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import mseries.ui.MImagePanel;

/**
 * GUI for the date selector (Calendar) popup
 */
public class MNationalitySelectorUI extends JDialog implements MNationalityListener {

    private static final long serialVersionUID = 1L;

    private JTextField nationality;

    private GridBagConstraints c = new GridBagConstraints();

    private JButton okButton;

    private JButton cancelButton;

    private ResourceBundle rb;

    MImagePanel innerPanel;

    public MNationalitySelectorUI(
            JFrame parent, 
            MNationalitySelectorPanel panel, 
            MNationalitySelector controller, 
            ResourceBundle rb,
            String nationality) {

        super(parent, "MSeries Nationality Selector", true);

        setResizable(false);

        this.rb = rb;

        innerPanel = new MImagePanel(new GridBagLayout());

        innerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.nationality = new JTextField(nationality) {
            private static final long serialVersionUID = 1L;
            public boolean isFocusTraversable() {
                return false;
            }
        };
        this.nationality.setEditable(false);

        setColours(this.nationality);
        this.nationality.setHorizontalAlignment(SwingConstants.CENTER);

        c.insets = new Insets(0, 0, 4, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = GridBagConstraints.RELATIVE;

        innerPanel.add(this.nationality, c);
        c.gridheight = 5;
        c.gridy = 1;
        innerPanel.add(panel, c);

        c.gridheight = 1;
        c.gridwidth = 1;
        okButton = new JButton();
        okButton.setActionCommand("ok");
        // setColours(okButton);

        cancelButton = new JButton();
        cancelButton.setActionCommand("cancel");
        // setColours(cancelButton);

        setLabels();

        c.insets = new Insets(0, 4, 4, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.SOUTH;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 1;
        innerPanel.add(okButton, c);

        c.anchor = GridBagConstraints.SOUTH;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 2;
        innerPanel.add(cancelButton, c);

        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 5;
        //innerPanel.add(todayButton, c);

        okButton.addActionListener(controller);
        cancelButton.addActionListener(controller);

        innerPanel.registerKeyboardAction(controller, "cancel", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        panel.addMNationalityListener(this);

        updateNationality(panel.getNationality());
        getContentPane().add(innerPanel);

        this.nationality.setOpaque(false);
        cancelButton.setOpaque(false);
        okButton.setOpaque(false);
        getRootPane().setDefaultButton(okButton);
        pack();
        setSize(getMinimumSize());
    }

    public void setImageFile(String imageFile) {
        innerPanel.setImageFile(imageFile);
    }

    private void setLabels() {
        okButton.setText(getString("OK", "OK"));
        cancelButton.setText(getString("Cancel", "Cancel"));
    }

    private String getString(String in, String def) {
        String ret;
        if (rb == null) {
            return def;
        }
        try {
            ret = rb.getString(in);
        } catch (MissingResourceException e) {
            ret = def;
        }
        return ret;
    }

    private void updateNationality(String nationality) {
        this.nationality.setText(nationality);
    }

    /**
     * Reacts to all changes in the data model (MNationality) which is given by the
     * event type. from mseries.utils.MNationalityListener interface
     */
    public void dataChanged(MNationalityEvent e) {
        String nationality = e.getNewNationality();
        switch (e.getType()) {
        case MNationalityEvent.NEW_NATIONALITY:
            updateNationality(nationality);
            break;
        default:
            break;
        }
    }

    protected void setColours(Component c) {
        c.setBackground(UIManager.getColor("control"));
        c.setForeground(UIManager.getColor("Button.foreground"));
    }
}

