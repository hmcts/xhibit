package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.CheckBoxTableCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * Class handles the rendering of Offence Link selection within the offence link table 
 * @author Ross McArthur
 *
 */
public class OffSelectionCellRenderer extends JCheckBox implements TableCellRenderer {
	
	
	
	XTable table;
	OrderOffenceModel model;
	OrderOffencePanel parent;
	int row=0; int column=0;
	
	 public OffSelectionCellRenderer(XTable table, OrderOffencePanel parent,  OrderOffenceModel model) {
		 super();
		 this.table = table;
		 this.model = model;
		 this.parent = parent;
		 setHorizontalAlignment(JLabel.CENTER);
		 
		 ComboboxListener itemListener = new ComboboxListener();
         addItemListener(itemListener);
        
         setEnabled(true);
	        
	        
	  }

	

	 /**
	  * 
	  */
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) 
	 {
		 OffenceSelectorRowModel trm = (OffenceSelectorRowModel)((OffenceSelectorModel)table.getModel()).getDataAt(row);
		 if(isSelected){
             setForeground(table.getSelectionForeground());
             super.setBackground(table.getSelectionBackground());
         }else {
             setForeground(table.getForeground());
             setBackground(table.getBackground());
         }
         setSelected(value != null && ((Boolean) value).booleanValue());
			 
		 
		 setEnabled(!(trm.getFinalD20() || trm.getINTD20()));	
		

	     return this;
	    
	 }
	 
	 /**
	  * Internal Class handles the actions that occur after the checkbox has been selected
	  * @author mcarthurr
	  *
	  */
	 class ComboboxListener implements ItemListener {
			
		 	JCheckBox prev;
		    public void itemStateChanged(ItemEvent evt) {
		        JCheckBox cb = (JCheckBox)evt.getSource();
		        
		        Object item = evt.getItem();
		        
//		        if (evt.getStateChange() == ItemEvent.SELECTED  || evt.getStateChange() == ItemEvent.DESELECTED) {
//		        	try {
//		        		
//		        		if(prev==null)
//		        			prev=cb;
//		        		else if(cb==prev)
//		        		{
//		        			if(cb.isSelected()== prev.isSelected())
//		        				return;
//		        		}
//						 table.setValueAt(cb.isSelected(), table.getSelectedRow(), table.getSelectedColumn());
//						 parent.stepUpdateViewState();
//						 prev= cb;
//		        		
//		            } catch (CSRecoverableException csre) {
//		                XHIBITErrorHandler.handleError(csre);
//		            }
//		            
//		        }
		    }
		    
		    
				
	}
		
	 

}


