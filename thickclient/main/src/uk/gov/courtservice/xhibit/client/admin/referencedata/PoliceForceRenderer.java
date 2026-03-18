package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;

/**
 * Renderer for PoliceForce Combo controls.
 * 
 * @author grewalg
 *
 */
public class PoliceForceRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -8454260583821827300L;

	public PoliceForceRenderer() {
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
	 * Utility method to construct the Police Force values.
	 * 
	 * @param rscbv
	 *            The RefSystemCodeBasicValue to create the name from.
	 * @return A well formatted System Code as a string.
	 */
	public String buildSystemCode(RefSystemCodeBasicValue rscbv) {
		StringBuffer sb = new StringBuffer();
		if (rscbv != null) {
			sb.append(rscbv.getCode());
			sb.append(" - ");
			sb.append(rscbv.getDecode());
		} else {
			sb.append("");
		}

		return sb.toString();
	}
}