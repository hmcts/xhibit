package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;

public class OtherCasesPanelFilterSelectionDropDownBoxCellRenderer extends DefaultListCellRenderer{
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * Renderer that sets background colour of dropdown and the text
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
		 if (value.getClass()==RefHearingTypeBasicValue.class) {
			RefHearingTypeBasicValue refHearingTypeBasicValue = (RefHearingTypeBasicValue)value;
        	setText(refHearingTypeBasicValue.getHearingTypeDesc());
        	 setBackground(isSelected ? UIManager.getColor("ComboBox.selectionBackground") : UIManager
                     .getColor("ComboBox.background"));
             setForeground(isSelected ? UIManager.getColor("ComboBox.selectionForeground") : UIManager
                     .getColor("ComboBox.foreground"));
             return this;
		 }	
		 else if(value.getClass()==RefSystemCodeBasicValue.class) {
			RefSystemCodeBasicValue policeVal = (RefSystemCodeBasicValue)value;			
				setText((policeVal.getDecode().toUpperCase()));
        	 setBackground(isSelected ? UIManager.getColor("ComboBox.selectionBackground") : UIManager
                     .getColor("ComboBox.background"));
             setForeground(isSelected ? UIManager.getColor("ComboBox.selectionForeground") : UIManager
                     .getColor("ComboBox.foreground"));
             return this;
			}
		// Any other render should use the standard values
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    } 
		
}
