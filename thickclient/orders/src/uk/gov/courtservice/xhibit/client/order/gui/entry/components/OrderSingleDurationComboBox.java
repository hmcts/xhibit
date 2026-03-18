package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomDurationComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.helpers.DateFieldHelper;
import uk.gov.courtservice.xhibit.client.order.gui.helpers.SingleDateFieldHelper;
import uk.gov.courtservice.xhibit.client.util.ComboBoxRendererRightJustify;

public class OrderSingleDurationComboBox extends AbstractOrderComponent implements ItemListener {
	Logger log = CSServices.getLogger(OrderSingleDurationComboBox.class);
	
	private CustomDurationComboBox cb;
	
	public void initComponent(){
		log.debug("[initComponent]");
		String ref = getHelper().getAttribute("ref");

        // defect 6669, I Simmons - 18/07/2011
        String refValue = null;
        refValue = getHelper().getValue(ref);
        if (refValue==null || refValue.equalsIgnoreCase("")){
            refValue="0";
        }
        // end
                        
		SingleDateFieldHelper dateHelper = new SingleDateFieldHelper();
		String maxValue = getHelper().getAttribute("maxValue");
        
       
        if (maxValue != null) {
            dateHelper.setValueMax(Integer.parseInt(maxValue));
        }
        
		cb = new CustomDurationComboBox(dateHelper.getValues(),ref,refValue);
		cb.addItemListener(this);
		
		ComboBoxRendererRightJustify renderer = new ComboBoxRendererRightJustify();
		cb.setRenderer(renderer);
		
		setVisualComponent(cb);
	//	cb.setSelectedIndex(5);
	}

	public void itemStateChanged(ItemEvent e) {
		CustomDurationComboBox b = ((CustomDurationComboBox) e.getSource());
        String ref = b.getRef();

        getHelper().setValues(ref, e.getItem().toString());
		
	}
	
	public void setEnabled(boolean enabled) {
        if (enabled) {
        	String ref = cb.getRef();
        	getHelper().setValues(ref, cb.getSelectedItem().toString());
        	
        }
        cb.setEnabled(enabled);
    }

}
