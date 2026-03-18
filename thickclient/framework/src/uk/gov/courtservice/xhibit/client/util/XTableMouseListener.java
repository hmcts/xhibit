package uk.gov.courtservice.xhibit.client.util;

// *******************************************************************************
// * XTableMouseListener implements MouseListener
// *
// * Purpose : Custom TableMouseListener to click the given button on a double
// *           click event in a table row.
// * Notes :
// *******************************************************************************

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTable;

public class XTableMouseListener implements MouseListener {
	private JButton button = null;
	private JTable table = null;

	public XTableMouseListener(JTable table) {
		this.table = table;
	}

	public XTableMouseListener(JTable table, JButton button) {
		this.table = table;
		this.button = button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ((2 == e.getClickCount()) && (button != null)) { // Double-click
			Integer rowClicked = table.rowAtPoint(e.getPoint());
			if (-1 != rowClicked) { // Get -1 if inside table but not on
									// table data
				table.setRowSelectionInterval(rowClicked, rowClicked);
				button.doClick();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
