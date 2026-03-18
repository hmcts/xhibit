package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.order.screens.model.OffenceModel;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;

public class OffenceSelectorRowModel {
	
	private OffenceModel OffenceModel; 
	private JCheckBox selectorBox = null; 
	private OrderOffencePanel parent;
	private OrderOffenceModel model;
	
	public OffenceSelectorRowModel(OrderOffencePanel parent, OrderOffenceModel model, OffenceModel offenceModel)
	{
		this.parent = parent;
		
		
		this.model = model;
		this.OffenceModel = offenceModel;
	}
	
	
	
	public boolean getSelectorBox()
	{
		return OffenceModel.isChecked();
	}
	
	public void setSelectorBox(boolean value)
	{
		 OffenceModel.setCheck(value);
		 updateClickCounter(value);
		 
	}
	
	private void updateClickCounter(boolean source) {
		
		if(source){
			model.incrementClickCount();
			
		}
		else{
			model.decrementClickCount();
			
		}
	}
	
	public String getDVLAOffence()
	{
		return OffenceModel.getDvlaOffence(); 
	}
	
	public String getOffenceCode()
	{
		return OffenceModel.getOffence();
	}
	
	public String getOffenceDescription()
	{
		return OffenceModel.getOffenceDescription();
	}
	
	public boolean getINTD20(){
		return OffenceModel.isIntD20();
	}
	
	public boolean getFinalD20(){
		return OffenceModel.isFinD20();
	}
	
	public String  getConvictionDate()
	{
		return OffenceModel.getConvictionDateString();
	}
	
	public String getDateSentOnD20()
	{
		return OffenceModel.getOffenceDateString();
	}

}
