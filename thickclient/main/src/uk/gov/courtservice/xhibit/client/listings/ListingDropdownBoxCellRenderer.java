package uk.gov.courtservice.xhibit.client.listings;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.UIManager;

import org.apache.commons.lang.WordUtils;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;

/**
 * Class used by listings to render the data in the dropdowns
 * @author harrism
 */
public class ListingDropdownBoxCellRenderer extends DropdownBoxCellRender {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		if (value.getClass() == CourtSiteBasicValue.class) {
			// Override the standard "CourtSiteCode-CourtSiteName" with just "CourtSiteName"
			CourtSiteBasicValue courtSite = (CourtSiteBasicValue) value;
			setText(WordUtils.capitalizeFully(courtSite.getCourtSiteName()));
			setBackground(isSelected ? UIManager.getColor("ComboBox.selectionBackground")
					: UIManager.getColor("ComboBox.background"));
			setForeground(isSelected ? UIManager.getColor("ComboBox.selectionForeground")
					: UIManager.getColor("ComboBox.foreground"));
			return this;
		}
		
		// Any other render should use the standard values
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
	
}