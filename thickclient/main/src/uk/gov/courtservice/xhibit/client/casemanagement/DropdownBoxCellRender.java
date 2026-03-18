package uk.gov.courtservice.xhibit.client.casemanagement;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

import org.apache.commons.lang.WordUtils;

import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHateSentencingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;

public class DropdownBoxCellRender extends DefaultListCellRenderer {

	/**
	 * serialversionuid
	 */
	private static final long serialVersionUID = 1L;
	private static final String SELECTION_BACKGROUND = "ComboBox.selectionBackground";
	private static final String SELECTION_FOREGROUND = "ComboBox.selectionForeground";
	private static final String BACKGROUND = "ComboBox.background";
	private static final String FOREGROUND = "ComboBox.foreground";

	private boolean format = false;

	public DropdownBoxCellRender() {
		// default constructor
	}

	public DropdownBoxCellRender(boolean newFormat) {
		this.format = newFormat;
	}

	public void setFormat(boolean newFormat) {
		this.format = newFormat;
	}

	/**
	 * Renderer that sets background colour of dropdown and the text
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		if (value == null) {
			return this;
		}
		
		if (value.getClass() == CourtSiteBasicValue.class) {
			CourtSiteBasicValue courtSite = (CourtSiteBasicValue) value;
			if (courtSite.getCourtSiteCode() == null) {
				setText(courtSite.getCourtSiteName());
			} else {
				setText(courtSite.getCourtSiteCode() + "-" + courtSite.getCourtSiteName());
			}
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == RefSystemCodeBasicValue.class) {
			RefSystemCodeBasicValue policeVal = (RefSystemCodeBasicValue) value;

			if (format) {
				if (policeVal.getCode() == null)
					setText(WordUtils.capitalizeFully(policeVal.getDecode()));
				else
					setText(policeVal.getCode() + " - " + WordUtils.capitalizeFully(policeVal.getDecode()));
			} else {
				setText(WordUtils.capitalizeFully(policeVal.getDecode()));
			}

			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == RefListingDataBasicValue.class) {
			RefListingDataBasicValue listingVal = (RefListingDataBasicValue) value;
			setText(WordUtils.capitalizeFully(listingVal.getRefDataValue()));
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == RefCourtBasicValue.class) {
			RefCourtBasicValue refCourtVal = (RefCourtBasicValue) value;

			if (!format) {
				setText(WordUtils.capitalizeFully(refCourtVal.getCourtFullName()));
			} else {
				if (refCourtVal.getCourtId() == null)
					setText("Select Crown Court");
				else
					setText(WordUtils.capitalizeFully(refCourtVal.getCourtFullName()) + " ("
							+ refCourtVal.getCrestCode() + ")");
			}

			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == RefHearingTypeBasicValue.class) {
			RefHearingTypeBasicValue refHearingTypeBasicValue = (RefHearingTypeBasicValue) value;
			setText(WordUtils.capitalizeFully(refHearingTypeBasicValue.getHearingTypeDesc()));
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == RefMonitoringCategoryBasicValue.class) {
			RefMonitoringCategoryBasicValue refMonitoringCategory = (RefMonitoringCategoryBasicValue) value;
			setText(WordUtils.capitalizeFully(refMonitoringCategory.getMonitoringCategoryName()));
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == DropdownCodeStringValue.class) {
			DropdownCodeStringValue val = (DropdownCodeStringValue) value;
			if (val.getIntCode()) {
				setText(val.getCode() + "-" + val.getDisplayName());
			} else {
				setText(val.getDisplayName());
			}
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == RefHateSentencingTypeBasicValue.class) {
			RefHateSentencingTypeBasicValue hateVal = (RefHateSentencingTypeBasicValue) value;
			setText(WordUtils.capitalizeFully(hateVal.getTitle()));
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == CourtBasicValue.class) {
			CourtBasicValue courtVal = (CourtBasicValue) value;
			setText(courtVal.getCourtName().toUpperCase() + " (" + courtVal.getCrestCourtId() + ")");

			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == XhbCourtBasicValue.class) {
			XhbCourtBasicValue xhbCourtVal = (XhbCourtBasicValue) value;

			// Check if ID present, if so then valid value else "Select Crown
			// Court"
			if (xhbCourtVal.getCourtId() != null)
				setText(xhbCourtVal.getCourtName().toUpperCase() + " (" + xhbCourtVal.getCrestCourtId() + ")");
			else
				setText("Select Crown Court");

			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		} else if (value.getClass() == CourtRoomBasicValue.class) {
			CourtRoomBasicValue courtRoom = (CourtRoomBasicValue) value;
			setText(WordUtils.capitalizeFully(courtRoom.getDisplayName()));
			setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
			setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
		}/* else if (value.getClass().isArray() && value.getClass() == String.class) {
			// Should be a String array
			String[] thisVal = (String[]) value;
			if (thisVal.length > 0) {
				setText(thisVal[1]);
				setBackground(isSelected ? UIManager.getColor(SELECTION_BACKGROUND) : UIManager.getColor(BACKGROUND));
				setForeground(isSelected ? UIManager.getColor(SELECTION_FOREGROUND) : UIManager.getColor(FOREGROUND));
			}
		}*/

		return this;
	}

}
