package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;

/**
 * Renderer for JudgeType Combo and Ticket Type List controls.
 * 
 * @author grewalg
 *
 */
public class JudgeRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -8454260583821827300L;

	public JudgeRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setHorizontalAlignment(LEFT);
		String txt = buildSystemCode((RefSystemCodeBasicValue) value);
		this.setText(txt);
		if (isSelected)
			list.setToolTipText(txt);

		return this;
	}

	/**
	 * Utility method to construct the Judge Type and Ticket Type values.
	 * 
	 * @param rscbv
	 *            The RefSystemCodeBasicValue to create the name from.
	 * @return A well formatted System Code as a string.
	 */
	public String buildSystemCode(RefSystemCodeBasicValue rscbv) {
		StringBuffer sb = new StringBuffer();
		sb.append(rscbv.getCode());
		// Default value should be left as it is
		if (rscbv.getDecode() != null && !rscbv.getDecode().equals("")) {
			sb.append(" - ");
			sb.append(rscbv.getDecode());
		}

		return sb.toString();
	}
}