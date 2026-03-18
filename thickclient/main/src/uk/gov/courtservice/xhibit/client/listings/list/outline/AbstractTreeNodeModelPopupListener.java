package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.netbeans.swing.outline.Outline;

/**
 * Base class for displaying popup menus on tree node models in the table/tree.
 * 
 * @author uphillj
 *
 */
public abstract class AbstractTreeNodeModelPopupListener extends MouseAdapter {

	private Outline outline;
	
	public AbstractTreeNodeModelPopupListener(Outline outline) {
		this.outline = outline;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		showPopupMenu(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		showPopupMenu(e);
	}
	
	/**
	 * Ensures row right-clicked on is selected and if a tree node model
	 * is stored in that row, delegate to sub-classes to show popup menu.
	 * 
	 * @param e
	 */
	private void showPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			// Get row which has been right-clicked on
			int currentRow = outline.rowAtPoint(e.getPoint());

			// Ensure the row that has been clicked on is selected
			outline.setRowSelectionInterval(currentRow, currentRow);

			// Show the correct popup menu for the selected row
			TreeNodeController controller = OutlineUtils.getTreeNodeController(outline, currentRow);
			if (controller != null) {
				showPopupMenu(e, controller);
			}
		}
	}
	
	/**
	 * Sub-classes implement this method to show popup menu for tree node model.
	 * 
	 * @param e
	 * @param controller
	 */
	protected abstract void showPopupMenu(MouseEvent e, TreeNodeController controller);
}
