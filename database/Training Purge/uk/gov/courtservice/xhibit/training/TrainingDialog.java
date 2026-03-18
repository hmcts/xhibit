package uk.gov.courtservice.xhibit.training;

import java.awt.*;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * <p>Training Script User Interface: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class TrainingDialog extends JDialog implements ActionListener
{
    private JPanel panel = new JPanel();

    private GridBagLayout gbl = new GridBagLayout();

	private JTextField userNameTxt;
	private JPasswordField passwordFld;

	private static String CMD_RunTrainingScript = "6546513215843";

    public TrainingDialog(Frame frame, String title, boolean modal)
    {
        super(frame, title, modal);
        try
        {
            jbInit();
            pack();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public TrainingDialog()
    {
        this(null, "XHIBIT 2 Training Script", false);
    }

    private void jbInit() throws Exception
    {
        panel.setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5,5,5,5);

		JLabel userNameLbl = new JLabel("user name :");

		panel.add(userNameLbl, gbc);

		gbc.gridx = gbc.gridx + 1;

		userNameTxt = new JTextField();
		userNameTxt.setColumns(20);

		panel.add(userNameTxt, gbc);

		gbc.gridx = 0;
		gbc.gridy = gbc.gridy + 1;

		JLabel passwordLbl = new JLabel("password :");

		panel.add(passwordLbl, gbc);

		gbc.gridx = gbc.gridx + 1;

		passwordFld = new JPasswordField();
		passwordFld.setColumns(20);
		passwordFld.setEchoChar('*');


		panel.add(passwordFld, gbc);

		gbc.gridx = 0;
		gbc.gridy = gbc.gridy + 1;
		gbc.gridwidth = 2;
		gbc.anchor = gbc.CENTER;

		JButton button = new JButton("Run Training Script");
		button.addActionListener(this);
		button.setActionCommand(this.CMD_RunTrainingScript);

		panel.add(button, gbc);



        getContentPane().add(panel);
    }

	public void actionPerformed(ActionEvent actionEvent)
	{
		if (actionEvent.getActionCommand().equals(CMD_RunTrainingScript))
		{
			try
			{
				char[] pwd = passwordFld.getPassword();
				String password = new String();
				password = password.copyValueOf(pwd);
				PurgeScript.runScript(userNameTxt.getText(), password);
				JOptionPane.showMessageDialog(null, "The script executed successfully.", "Done.", JOptionPane.INFORMATION_MESSAGE );
			}
			catch(Exception e)
			{
				StringWriter s = new StringWriter();
				PrintWriter p = new PrintWriter(s);
				e.printStackTrace(p);

				int firstEnter = s.toString().indexOf("\n");

				String shortReport = s.toString().substring(0, firstEnter == -1 ? s.toString().length() : firstEnter);

				JOptionPane.showMessageDialog(null, "Problem whilst running the script.\n\n"+shortReport, "Exception.", JOptionPane.ERROR_MESSAGE );
			}
		}
	}


	protected void processWindowEvent(WindowEvent e)
	{
		if ( e.getID() == WindowEvent.WINDOW_CLOSING )
		{
			System.exit(0);
		}
		else
		{
			super.processWindowEvent(e);
		}
	}

	public static void main (String[] arguments)
	{
		TrainingDialog x = new TrainingDialog();
		x.setSize(350,150);
		x.show();
	}

}